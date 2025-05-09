package com.lilma.legou.controller;

import com.lilma.legou.mapper.AdminMapper;
import com.lilma.legou.pojo.*;
import com.lilma.legou.service.*;
import com.lilma.legou.utils.ThreadLocalUtil;
import com.lilma.legou.utils.jwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminMapper adminMapper;

    @PostMapping("/login")
    public Result login(String name,String pwd,HttpServletRequest request){
        pwd= DigestUtils.md5DigestAsHex(pwd.getBytes());
        Admin admin=adminService.findByName(name);
        if(admin==null)return Result.error("该管理员不存在");
        if(pwd.equals(admin.getPwd())){
            Map<String,Object> claims=new HashMap<>();
            claims.put("id",admin.getId());
            claims.put("adminname",admin.getAdminname());
            String token= jwtUtil.genToken(claims);
            AdminLoginData loginData=new AdminLoginData(admin,token);
            // 获取客户端 IP 地址
            String clientIp = getClientIp(request);
            // 可以在这里将 IP 地址存储到日志或者进行其他操作
            adminService.loginRecord(admin.getId(),clientIp);
            return Result.success(loginData);
        }
        //密码错误则返回错误信息
        return Result.error("密码错误");
    }

    private String getClientIp(HttpServletRequest request) {
        String xffHeader = request.getHeader("X-Forwarded-For");
        if (xffHeader == null) {
            return request.getRemoteAddr();
        }
        return xffHeader.split(",")[0];
    }

    @GetMapping("/logout")
    public Result logout(){
        Map<String,Object> claim= ThreadLocalUtil.get();
        Integer id=(Integer)claim.get("id");
        adminService.logout(id);
        return Result.success();
    }

    @GetMapping("/getsaler")
    public Result getSaler(){
        Saler[] list=adminService.getSaler();
        return Result.success(list);
    }

    @PostMapping("/addsaler")
    public Result addSaler(Saler saler,HttpServletRequest request){
        String pwd=saler.getPwd();
        pwd=DigestUtils.md5DigestAsHex(pwd.getBytes());
        saler.setPwd(pwd);
        adminService.addSaler(saler);
        Map<String,Object> claim=ThreadLocalUtil.get();
        String adminname=(String)claim.get("adminname");
        String ip=getClientIp(request);
        String salername=saler.getSalername();
        String content=adminname+"添加销售员："+salername;
        adminService.operateRecord(adminname,ip,content);
        return Result.success("添加新销售员成功");
    }

    @GetMapping("/delsaler")
    public Result delSaler(Integer id,HttpServletRequest request){
        adminService.delSaler(id);
        Map<String,Object> claim=ThreadLocalUtil.get();
        String adminname=(String)claim.get("adminname");
        String ip=getClientIp(request);
        String salername=adminService.findSalerById(id);
        String content=adminname+"删除销售员："+salername;
        adminService.operateRecord(adminname,ip,content);
        return Result.success("删除销售员成功");
    }

    @PostMapping("/modifypwd")
    public Result modifyPwd(Integer id,String pwd,HttpServletRequest request){
        pwd=DigestUtils.md5DigestAsHex(pwd.getBytes());
        adminService.modifyPwd(id,pwd);
        Map<String,Object> claim=ThreadLocalUtil.get();
        String adminname=(String)claim.get("adminname");
        String ip=getClientIp(request);
        String salername=adminService.findSalerById(id);
        String content=adminname+"修改销售员："+salername+"登录密码";
        adminService.operateRecord(adminname,ip,content);
        return Result.success("修改销售员登录口令成功");
    }

    @GetMapping("/getsales")
    public Result getSales(Integer id){
        Order[] list=adminService.getSales(id);
        return Result.success(list);
    }

    @GetMapping("/getreport")
    public Result getGoods(){
        Status[] list=adminService.getGoods();
        return Result.success(list);
    }

    @PostMapping("/addamount")
    public Result addAmount(Integer goodsid,Integer amount,HttpServletRequest request){
        adminService.addAmount(goodsid,amount);
        Map<String,Object> claim=ThreadLocalUtil.get();
        String adminname=(String)claim.get("adminname");
        String ip=getClientIp(request);
        String goodsname=adminMapper.findGoodsNameById(goodsid);
        String content=adminname+"修改商品："+goodsname+"库存";
        adminService.operateRecord(adminname,ip,content);
        return Result.success("修改库存成功");
    }

    @GetMapping("/getratio")
    public Result getRatio(){
        Rcategory[] list=adminService.getRatio();
        return Result.success(list);
    }

    @GetMapping("/addcategory")
    public Result addCategory(String categoryname,HttpServletRequest request){
        adminService.addCategorys(categoryname);
        Map<String,Object> claim=ThreadLocalUtil.get();
        String adminname=(String)claim.get("adminname");
        String ip=getClientIp(request);
        String content=adminname+"添加商品种类："+categoryname;
        adminService.operateRecord(adminname,ip,content);
        return Result.success("添加新商品种类成功");
    }

    @GetMapping("/getcategorys")
    public Result getCategorys(){
        Categorys[] list=adminService.getCategorys();
        return Result.success(list);
    }


    @GetMapping("/getlabel")
    public Result getLabel(){
        List<Integer> userids=adminMapper.getUserids();
        UserLabel[] userLabels=new UserLabel[userids.size()];
        int i=0;
        for(int userid:userids){
            PurchasePowerService purchasePowerService=new PurchasePowerService(adminMapper.getPurchaseOrder());
            String purchasepower=purchasePowerService.getPurchasePowerLevel(userid);
            List<BehaviorSequenceCategory> testData = adminMapper.getCateBehavior(userid);
            UserPreferenceAnalysis.DataProcessor processor = new UserPreferenceAnalysis.DataProcessor();
            Map<Integer, List<UserPreferenceAnalysis.CategoryFeature>> featuresMap = processor.processData(testData);
            List<UserPreferenceAnalysis.CategoryFeature> features = featuresMap.getOrDefault(userid, Collections.emptyList());
            int dynamicClusters = features.size() >= 3 ? 3 : Math.max(features.size(), 1);
            UserPreferenceAnalysis.PreferenceClusterer clusterer = new UserPreferenceAnalysis.PreferenceClusterer(dynamicClusters, 0xCAFEBABEL);
            Map<Integer, String> result = clusterer.analyzeUserPreference(userid, features);
            List<String>categorys=new ArrayList<>();
            for(int categoryid:result.keySet()){
                if(result.get(categoryid).equals("高兴趣")){
                    categorys.add(adminMapper.findCategoryNameById(categoryid));
                }
            }
            String location=adminMapper.findLocById(userid);
            UserLabel label=new UserLabel(userid,location,purchasepower,categorys);
            userLabels[i]=label;
            i++;
        }
        return Result.success(userLabels);
    }

    @GetMapping("/anomal")
    public Result getAnomal(){
        List<AnomalOrder> orders =adminMapper.getAnomalOrder();
        AnomalyDetectionService detector = new AnomalyDetectionService();
        List<AnomalyDetectionService.AnomalyDetail> anomalies = detector.detectAnomalies(orders);
        return Result.success(anomalies);
    }

    @GetMapping("/deleteorder")
    public Result deleteOrder(Integer orderid){
        adminMapper.deleteOrder(orderid);
        return Result.success("删除订单成功");
    }

    @GetMapping("/trend")
    public Result getTrend(Integer goodsid){
        List<OrderRecord> testData =adminMapper.getOrderRecord();
        int n=adminMapper.getGoodsnum();
        SalesTrendPredictionService predictor = new SalesTrendPredictionService(testData, n);
        String trend = predictor.predictTrend(goodsid);
        return Result.success(trend);
    }

    @GetMapping("/getlogindata")
    public Result getLoginData(){
        BigDataLogin[] list=adminMapper.getLoginData();
        return Result.success(list);
    }

    @GetMapping("/getoperatedata")
    public Result getOperateData(){
        OperateData[] list=adminMapper.getOperateData();
        return Result.success(list);
    }

    @GetMapping("/delcategory")
    public Result delCategory(Integer id){
        adminMapper.delcategory(id);
        return Result.success("删除商品种类成功");
    }


}



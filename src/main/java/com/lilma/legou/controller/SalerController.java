package com.lilma.legou.controller;

import com.lilma.legou.pojo.*;
import com.lilma.legou.service.SalerService;
import com.lilma.legou.utils.ThreadLocalUtil;
import com.lilma.legou.utils.jwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/saler")
public class SalerController {
    @Autowired
    private SalerService salerService;

    @PostMapping("/login")
    public Result login(String name,String pwd,HttpServletRequest request){
        pwd= DigestUtils.md5DigestAsHex(pwd.getBytes());
        Saler saler=salerService.findByName(name);
        if(saler==null)return Result.error("该销售员不存在");
        if(pwd.equals(saler.getPwd())){
            Map<String,Object> claims=new HashMap<>();
            claims.put("id",saler.getId());
            claims.put("salername",saler.getSalername());
            String token= jwtUtil.genToken(claims);
            SalerLoginData loginData=new SalerLoginData(saler,token);
            // 获取客户端 IP 地址
            String clientIp = getClientIp(request);
            // 可以在这里将 IP 地址存储到日志或者进行其他操作
            salerService.loginRecord(saler.getId(),clientIp);
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
        Map<String,Object> claim=ThreadLocalUtil.get();
        Integer id=(Integer)claim.get("id");
        salerService.logout(id);
        return Result.success();
    }

    @GetMapping("/getgoods")
    public Result getGoods(){
        Map<String,Object> claim= ThreadLocalUtil.get();
        Integer id=(Integer)claim.get("id");
        Goods[] list=salerService.getGoods(id);
        return Result.success(list);
    }

    @PostMapping("/add")
    public Result addGoods(@RequestBody Goods goods,HttpServletRequest request){
        Map<String,Object> claim=ThreadLocalUtil.get();
        String salername=(String)claim.get("salername");
        String ip=getClientIp(request);
        String goodsname=goods.getGoodsname();
        String content=salername+"添加商品："+goodsname;
        salerService.operateRecord(salername,ip,content);
        salerService.addGoods(goods);
        return Result.success("商品添加成功");
    }

    @GetMapping("/del")
    public Result delGoods(Integer id,HttpServletRequest request){
        Map<String,Object> claim=ThreadLocalUtil.get();
        String salername=(String)claim.get("salername");
        String ip=getClientIp(request);
        String goodsname=salerService.findById(id);
        String content=salername+"删除商品："+goodsname;
        salerService.operateRecord(salername,ip,content);
        salerService.delGoods(id);
        return Result.success("商品删除成功");
    }

    @PostMapping("/update")
    public Result updateGoods(@RequestBody Goods goods,HttpServletRequest request){
        Map<String,Object> claim=ThreadLocalUtil.get();
        String salername=(String)claim.get("salername");
        String ip=getClientIp(request);
        String goodsname=goods.getGoodsname();
        String content=salername+"更新商品："+goodsname;
        salerService.operateRecord(salername,ip,content);
        salerService.updateGoods(goods);
        return Result.success("商品更新成功");
    }

    @GetMapping("/getrecord")
    public Result getRecord(){
        Map<String,Object> claim=ThreadLocalUtil.get();
        Integer id=(Integer)claim.get("id");
        Records[] list=salerService.getRecord(id);
        return Result.success(list);
    }

    @GetMapping("/ratio")
    public Result ratio(){
        Map<String,Object> claim=ThreadLocalUtil.get();
        Integer id=(Integer)claim.get("id");
        List<Rorder> list=salerService.ratio(id);
        return Result.success(list);
    }

    @GetMapping("/getstatus")
    public Result getStatus(){
        Map<String,Object> claim=ThreadLocalUtil.get();
        Integer id=(Integer)claim.get("id");
        Status[] list=salerService.getStatus(id);
        return Result.success(list);
    }
}

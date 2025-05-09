package com.lilma.legou.service.impl;

import com.lilma.legou.mapper.AdminMapper;
import com.lilma.legou.pojo.*;
import com.lilma.legou.service.AdminService;
import com.lilma.legou.service.PurchasePowerService;
import com.lilma.legou.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin findByName(String name) {
        return adminMapper.findByName(name);
    }

    @Override
    public Saler[] getSaler() {
        return adminMapper.getSaler();
    }

    @Override
    public void addSaler(Saler saler) {
        adminMapper.addSaler(saler);
    }

    @Override
    public void delSaler(Integer id) {
        adminMapper.delSaler(id);
    }

    @Override
    public void modifyPwd(Integer id, String pwd) {
        adminMapper.modifyPwd(id,pwd);
    }

    @Override
    public Order[] getSales(Integer id) {
       return adminMapper.getSales(id);
    }

    @Override
    public Status[] getGoods() {
        return adminMapper.getGoods();
    }

    @Override
    public void addAmount(Integer goodsid, Integer amount) {
        adminMapper.addAmount(goodsid,amount);
    }

    @Override
    public Rcategory[] getRatio() {
        return adminMapper.getRatio();
    }

    @Override
    public void addCategorys(String categoryname) {
        adminMapper.addCategorys(categoryname);
    }

    @Override
    public Categorys[] getCategorys() {
        return adminMapper.getCategorys();
    }

    @Override
    public void loginRecord(Integer id, String clientIp) {
        adminMapper.loginRecord(id,clientIp);
    }

    @Override
    public void logout(Integer id) {
        ThreadLocalUtil.remove();
        adminMapper.logout(id);
    }

    @Override
    public void operateRecord(String adminname, String ip, String content) {
        adminMapper.operateRecord(adminname,ip,content);
    }

    @Override
    public String findSalerById(Integer id) {
        return adminMapper.findSalerById(id);
    }



    /*@Override
    public UserLabel getLabel(Integer userid) {
        String location=adminMapper.findLocById(userid);
        List<Order> orders=adminMapper.getOrder();
        PurchasePowerService purchasePowerService=new PurchasePowerService(orders);
        String purchasePower=purchasePowerService.getPurchasePowerLevel(userid);
        return new UserLabel(1,",","",null);
    }*/


}

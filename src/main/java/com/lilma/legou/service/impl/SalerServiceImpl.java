package com.lilma.legou.service.impl;

import com.lilma.legou.mapper.SalerMapper;
import com.lilma.legou.pojo.*;
import com.lilma.legou.service.SalerService;
import com.lilma.legou.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalerServiceImpl implements SalerService {
    @Autowired
    private SalerMapper salerMapper;


    @Override
    public Saler findByName(String salername) {
        return salerMapper.findByName(salername);
    }

    @Override
    public Goods[] getGoods(Integer id) {
        return salerMapper.getGoods(id);
    }

    @Override
    public void addGoods(Goods goods) {
        salerMapper.addGoods(goods);
    }

    @Override
    public void delGoods(Integer id) {
        salerMapper.delGoods(id);
    }

    @Override
    public void updateGoods(Goods goods) {
        salerMapper.updateGoods(goods);
    }

    @Override
    public Records[] getRecord(Integer id) {
        return salerMapper.getRecord(id);
    }

    @Override
    public List<Rorder> ratio(Integer id) {
        return salerMapper.ratio(id);
    }

    @Override
    public Status[] getStatus(Integer id) {
        return salerMapper.getStatus(id);
    }

    @Override
    public void logout(Integer id) {
        ThreadLocalUtil.remove();
        salerMapper.logout(id);
    }

    @Override
    public void loginRecord(Integer id, String clientIp) {
        salerMapper.loginRecord(id, clientIp);
    }

    @Override
    public void operateRecord(String salername, String ip, String content) {
        salerMapper.operateRecord(salername,ip,content);
    }

    @Override
    public String findById(Integer id) {
        return salerMapper.findById(id);
    }
}

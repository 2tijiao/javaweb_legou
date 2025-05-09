package com.lilma.legou.service;

import com.lilma.legou.pojo.*;

import java.util.List;

public interface SalerService {
    Saler findByName(String salername);

    Goods[] getGoods(Integer id);

    void addGoods(Goods goods);

    void delGoods(Integer id);

    void updateGoods(Goods goods);

    Records[] getRecord(Integer id);

    List<Rorder> ratio(Integer id);

    Status[] getStatus(Integer id);

    void logout(Integer id);

    void loginRecord(Integer id, String clientIp);

    void operateRecord(String salername, String ip, String content);

    String findById(Integer id);
}

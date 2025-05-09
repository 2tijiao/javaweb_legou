package com.lilma.legou.service;

import com.lilma.legou.pojo.*;

public interface AdminService {
    Admin findByName(String name);

    Saler[] getSaler();

    void addSaler(Saler saler);

    void delSaler(Integer id);

    void modifyPwd(Integer id, String pwd);

    Order[] getSales(Integer id);

    Status[] getGoods();

    void addAmount(Integer goodsid, Integer amount);


    Rcategory[] getRatio();

    void addCategorys(String categoryname);

    Categorys[] getCategorys();

    void loginRecord(Integer id, String clientIp);

    void logout(Integer id);

    void operateRecord(String adminname, String ip, String content);

    String findSalerById(Integer id);



    /*UserLabel getLabel(Integer userid);*/
}

package com.lilma.legou.service;

import com.lilma.legou.pojo.Categorys;
import com.lilma.legou.pojo.Goods;

import java.sql.Timestamp;

public interface GoodsService {
    Categorys[] getCategorys();

    Goods[] getNewGoods();

    Goods[] getCategoryGoods(Integer categoryid);

    Goods getGoodsDetail(Integer goodsid);

    Goods[] search(String goodsname);

    void browsingRecord(Integer userid, Integer goodsid, Long dwelltime);
}

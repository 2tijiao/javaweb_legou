package com.lilma.legou.service.impl;

import com.lilma.legou.mapper.GoodsMapper;
import com.lilma.legou.pojo.Categorys;
import com.lilma.legou.pojo.Goods;
import com.lilma.legou.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public Categorys[] getCategorys() {
        return goodsMapper.getCategorys();
    }

    @Override
    public Goods[] getNewGoods() {
        return goodsMapper.getNewGoods();
    }

    @Override
    public Goods[] getCategoryGoods(Integer categoryid) {
        return goodsMapper.getCategoryGoods(categoryid);
    }

    @Override
    public Goods getGoodsDetail(Integer goodsid) {
        return goodsMapper.getGoodsDetail(goodsid);
    }

    @Override
    public Goods[] search(String goodsname) {
        return goodsMapper.search(goodsname);
    }

    @Override
    public void browsingRecord(Integer userid, Integer goodsid, Long dwelltime) {
        goodsMapper.browsingRecord(userid,goodsid,dwelltime);
    }


}

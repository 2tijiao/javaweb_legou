package com.lilma.legou.controller;

import com.lilma.legou.mapper.GoodsMapper;
import com.lilma.legou.pojo.*;
import com.lilma.legou.service.DwellTimeCF;
import com.lilma.legou.service.GoodsService;
import com.lilma.legou.service.SalesTrendPredictionService;
import com.lilma.legou.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsMapper goodsMapper;

    @GetMapping("/category")
    public Result getCategorys(){
        Categorys[]categorys=goodsService.getCategorys();
        return Result.success(categorys);
    }

    @GetMapping("/categorygoods")
    public Result getCategoryGoods(Integer categoryid)
    {
        Goods[] goodslist=goodsService.getCategoryGoods(categoryid);
        return Result.success(goodslist);
    }

    @GetMapping("/detail")
    public Result getGoodsDetail(Integer goodsid)
    {
        Goods goods=goodsService.getGoodsDetail(goodsid);
        return Result.success(goods);
    }

    @GetMapping("/search")
    public Result search(String goodsname){
        Goods[] lists=goodsService.search(goodsname);
        return Result.success(lists);
    }

    @PostMapping("/browsing")
    public Result browsingRecord(Integer goodsid, Long dwelltime){
        Map<String,Object> claim=ThreadLocalUtil.get();
        Integer userid=(Integer)claim.get("id");
        goodsService.browsingRecord(userid,goodsid,dwelltime);
        return Result.success();
    }

    @GetMapping("/random")
    public Result getRandomGoods(){
        Goods[] goodslist=goodsMapper.getRandomGoods();
        return Result.success(goodslist);
    }

    @GetMapping("/new")
    public Result getNewGoods(){
        Goods[] goodslist=goodsService.getNewGoods();
        return Result.success(goodslist);
    }

    @GetMapping("/userhot")
    public Result getUserHotGoods(){
        Map<String,Object> claim=ThreadLocalUtil.get();
        Integer userid=(Integer)claim.get("id");
        List<BehaviorSequenceGoods> data = goodsMapper.getBehavior();
        DwellTimeCF.EnhancedCFModel model = DwellTimeCF.preprocessData(data);
        List<Integer> recommendations = DwellTimeCF.recommendForUser(model, userid);
        Goods[] goodslist=new Goods[recommendations.size()];
        int i=0;
        for(int goodsid:recommendations){
            goodslist[i]=goodsMapper.getGoodsDetail(goodsid);
            i++;
        }
        return Result.success(goodslist);
    }

    @GetMapping("/hot")
    public Result getHotGoods(){
        Integer userid=0;
        List<BehaviorSequenceGoods> data = goodsMapper.getBehavior();
        DwellTimeCF.EnhancedCFModel model = DwellTimeCF.preprocessData(data);
        List<Integer> recommendations = DwellTimeCF.recommendForUser(model, userid);
        Goods[] goodslist=new Goods[recommendations.size()];
        int i=0;
        for(int goodsid:recommendations){
            goodslist[i]=goodsMapper.getGoodsDetail(goodsid);
            i++;
        }
        return Result.success(goodslist);
    }

}

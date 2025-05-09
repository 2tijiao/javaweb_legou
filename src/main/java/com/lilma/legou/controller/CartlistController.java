package com.lilma.legou.controller;

import com.lilma.legou.pojo.CartList;
import com.lilma.legou.pojo.Result;
import com.lilma.legou.service.CartlistService;
import com.lilma.legou.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cartlist")
public class CartlistController {
    @Autowired
    private CartlistService cartlistService;

    @GetMapping("/getlist")
    public Result getCartlist(){
        Map<String,Object> claim=ThreadLocalUtil.get();
        Integer userid=(Integer) claim.get("id");
        CartList[] list=cartlistService.getlist(userid);
        return Result.success(list);
    }

    @PostMapping("/addcart")
    public Result addCartlist(Integer goodsid,Integer goodsnum){
        Map<String,Object> claim= ThreadLocalUtil.get();
        Integer userid=(Integer)claim.get("id");
        cartlistService.addCartlist(goodsid,goodsnum,userid);
        return Result.success("添加成功");
    }

    @PostMapping("/del")
    public Result delete(Integer id){
        cartlistService.delete(id);
        return Result.success();
    }

    @PostMapping("/finish")
    public Result finish(@RequestBody CartList[] lists){
        if(cartlistService.finish(lists)){
        return Result.success("下单成功");}
        else{return Result.error("所有商品库存不足，下单失败");}
    }
}

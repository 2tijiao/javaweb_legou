package com.lilma.legou.controller;

import com.lilma.legou.pojo.Order;
import com.lilma.legou.pojo.Result;
import com.lilma.legou.service.OrderService;
import com.lilma.legou.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/get")
    public Result getOrder(){
        Map<String,Object> claim= ThreadLocalUtil.get();
        Integer userid=(Integer) claim.get("id");
        Order[] list=orderService.getOrder(userid);
        return Result.success(list);
    }

    @GetMapping("/change")
    public Result changeState(Integer id){
        orderService.changeState(id);
        return Result.success();
    }

    @GetMapping("/del")
    public Result del(Integer id){
        orderService.del(id);
        return Result.success();
    }
}

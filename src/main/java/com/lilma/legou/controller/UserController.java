package com.lilma.legou.controller;

import com.lilma.legou.mapper.UserMapper;
import com.lilma.legou.pojo.BehaviorSequenceGoods;
import com.lilma.legou.pojo.LoginData;
import com.lilma.legou.pojo.Result;
import com.lilma.legou.pojo.User;
import com.lilma.legou.service.DwellTimeCF;
import com.lilma.legou.service.UserService;
import com.lilma.legou.utils.ThreadLocalUtil;
import com.lilma.legou.utils.jwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lilma.legou.service.DwellTimeCF.preprocessData;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @PostMapping("/register")
    public Result register(String username,String pwd,String email,Integer age,String location){
        User u=userService.findByName(username);
        if(u==null){
            pwd=DigestUtils.md5DigestAsHex(pwd.getBytes());
            userService.register(username,pwd,email,age,location);
            return Result.success();
        }
        else{return Result.error("该用户已存在");}
    }

    @PostMapping("/login")
    public Result login(String name, String pwd, HttpServletRequest request) {
        pwd = DigestUtils.md5DigestAsHex(pwd.getBytes());
        User u = userService.findByName(name);
        if (u == null) return Result.error("请先完成注册");
        if (pwd.equals(u.getPwd())) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", u.getId());
            claims.put("username", u.getUsername());
            String token = jwtUtil.genToken(claims);
            LoginData loginData = new LoginData(u, token);
            // 获取客户端 IP 地址
            String clientIp = getClientIp(request);
            // 可以在这里将 IP 地址存储到日志或者进行其他操作
            userService.loginRecord(u.getId(),clientIp);
            return Result.success(loginData);
        }
        // 密码错误则返回错误信息
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
        userService.logout(id);
        return Result.success();
    }

    @GetMapping("/cancel")
    public Result cancel(){
        Map<String,Object> claim= ThreadLocalUtil.get();
        Integer id=(Integer)claim.get("id");
        userService.cancel(id);
        return Result.success();
    }

}

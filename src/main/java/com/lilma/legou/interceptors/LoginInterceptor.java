package com.lilma.legou.interceptors;

import com.lilma.legou.utils.ThreadLocalUtil;
import com.lilma.legou.utils.jwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true; // 对OPTIONS请求不执行拦截逻辑
        }
        else{
            //令牌验证：获取头部指定属性的值（登录生成的token值）
            String token=request.getHeader("Authorization");
            //验证token
            try{
                Map<String,Object> claims= jwtUtil.parseToken(token);
                //把业务数据存放在threadlocal中
                ThreadLocalUtil.set(claims);
                //放行
                return true;
            }catch(Exception e){
                response.setStatus(401);
                return false;
            }
        }
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清空threadlocal中的数据
        ThreadLocalUtil.remove();
    }
}

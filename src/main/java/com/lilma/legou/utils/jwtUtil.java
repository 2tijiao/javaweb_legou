package com.lilma.legou.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.Map;

public class jwtUtil {
    /**
     * JWT密钥
     */
    private static final String KEY = "lilma";
    /**
     * 生成JWT令牌
     * @param claims 存储在JWT中的声明
     * @return 生成的JWT令牌字符串
     */
    public static String genToken(Map<String,Object> claims){
        return JWT.create()
                .withClaim("claims", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 365)) // 设置令牌有效期为1年
                .sign(Algorithm.HMAC256(KEY)); // 使用HMAC256算法和密钥签名
    }
    /**
     * 解析JWT令牌
     * @param token 需要解析的JWT令牌字符串
     * @return 包含声明的Map对象
     */
    public static Map<String,Object> parseToken(String token){
        return JWT.require(Algorithm.HMAC256(KEY))
                .build()
                .verify(token) // 验证令牌
                .getClaim("claims") // 获取claims声明
                .asMap(); // 将claims声明转换为Map对象
    }
}

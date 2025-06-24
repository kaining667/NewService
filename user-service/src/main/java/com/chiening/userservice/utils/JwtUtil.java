package com.chiening.userservice.utils;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Repository;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;

/**
 * @description: JWT工具类，使用HS256算法
 * @author: ChiefNing
 * @date: 2025年06月17日
 */
@Repository
public class JwtUtil {

    // TOKEN的有效期一天（秒）
    private static final int TOKEN_TIME_OUT = 3_600;
    // 加密KEY（HS256使用普通字符串密钥）
    private static final String TOKEN_ENCRY_KEY = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY";
    // 最小刷新间隔(秒)
    private static final int REFRESH_TIME = 300;

    // 生成Token
    public static String getToken(Long id) {
        Map<String, Object> claimMaps = new HashMap<>();
        claimMaps.put("id", id);

        String jwt = Jwts.builder()
                .setClaims(claimMaps)
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_TIME_OUT * 1000))
                .signWith(SignatureAlgorithm.HS256, TOKEN_ENCRY_KEY)
                .compact();
        return jwt;
    }

    /**
     * 获取token中的claims信息
     *
     * @param token
     * @return
     */
    private static Jws<Claims> getJws(String token) {
        if (token == null || token.trim().isEmpty()) {
            System.out.println("JWT为空，无法解析");
            return null;
        }

        // 验证JWT格式（必须包含两个句点）
        if (token.chars().filter(c -> c == '.').count() != 2) {
            System.out.println("无效的JWT格式，缺少句点分隔符，Token: " + token);
            return null;
        }
        return Jwts.parser()
                .setSigningKey(TOKEN_ENCRY_KEY)
                .parseClaimsJws(token);
    }

    /**
     * 获取payload body信息
     *
     * @param token
     * @return
     */
    public static Claims getClaimsBody(String token) {
        try {
            return getJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return null;
        }
    }

    /**
     * 获取hearder body信息
     *
     * @param token
     * @return
     */
    public static JwsHeader getHeaderBody(String token) {
        return getJws(token).getHeader();
    }

    /**
     * 是否过期
     *
     * @param claims
     * @return -1：有效，0：有效但需要刷新，1：过期，2：异常
     */
    public static int verifyToken(Claims claims) {
        if (claims == null) {
            return 1;
        }
        try {
            Date expiration = claims.getExpiration();
            boolean isExpired = expiration.before(new Date());

            if (isExpired) {
                return 1; // 已过期
            } else {
                // 需要自动刷新TOKEN
                if ((expiration.getTime() - System.currentTimeMillis()) > REFRESH_TIME * 1000) {
                    return -1; // 有效，无需刷新
                } else {
                    return 0; // 有效，但接近过期需要刷新
                }
            }
        } catch (ExpiredJwtException ex) {
            return 1; // 已过期
        } catch (Exception e) {
            return 2; // 其他异常
        }
    }


}
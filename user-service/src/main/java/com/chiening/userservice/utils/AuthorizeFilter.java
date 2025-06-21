package com.chiening.userservice.utils;

import com.chiening.userservice.clients.PermissionClient;
import io.jsonwebtoken.Claims;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: TODO
 * @author: ChiefNing
 * @date: 2025年06月19日
 */
@Component
public class AuthorizeFilter {

    /**
     * 检查用户是否登录
     */
    public static boolean isUserLogin(HttpServletRequest request) {
        String token = getToken(request);
        if(token == null){
            return false;
        }else {
            Claims claims = JwtUtil.getClaimsBody(token);
            int result = JwtUtil.verifyToken(claims);
            if(result == 1 || result == 2) {
                return false;
            }
        }
        return true;
    }

    /**
     * 返回用户id
     *
     * @param request
     */
    public static Long getUserId(HttpServletRequest request) {
        return Long.parseLong(JwtUtil.getClaimsBody(getToken(request)).get("id").toString());
    }

    /**
     * 获取token
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request) {
        System.out.println(1);
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authHeader = authHeader.substring(7);
        }else {
            return null;
        }

        System.out.println(authHeader);
        return authHeader;
    }
}

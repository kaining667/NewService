package com.chiening.userservice.service;

import com.chiening.userservice.entity.R;
import com.chiening.userservice.entity.User;
import com.chiening.userservice.entity.UserInfo;
import com.chiening.userservice.entity.RegisterRequest;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;

/**
* @description: TODO
* @author: ChiefNing
* @date: 2025年06月17日
*/


public interface UserService {

    /**
     * 用户注册
     */
    R<User> register(RegisterRequest request) throws UnknownHostException;


    /**
     * 用户登录
     */
    R<UserInfo> login(String username, String password, HttpServletRequest request) throws UnknownHostException;

    /**
     * 根据ID获取用户信息
     */
    R<User> getUser(Long userId, HttpServletRequest request) throws UnknownHostException;

    /**
     * 更新用户信息
     */
    R<Void> updateUser(Long userId, User user, HttpServletRequest request) throws UnknownHostException;

    /**
     * 密码重置
     */
    R<Void> resetPassword(HttpServletRequest request) throws UnknownHostException;
}
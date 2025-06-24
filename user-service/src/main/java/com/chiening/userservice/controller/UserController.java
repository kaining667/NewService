package com.chiening.userservice.controller;

import com.chiening.userservice.entity.R;
import com.chiening.userservice.entity.User;
import com.chiening.userservice.entity.UserInfo;
import com.chiening.userservice.service.UserService;
import com.chiening.userservice.entity.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.UnknownHostException;

/**
 * @description: 用户操作类方法接口
 * @author: ChiefNing
 * @date: 2025年06月18日
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册接口
     */
    @PostMapping("/register")
    public R<User> register(@RequestBody @Valid RegisterRequest request) throws UnknownHostException {
        return userService.register(request);
    }

    /**
     * 用户登录接口
     */
    @PostMapping("/login")
    public R<UserInfo> login(@RequestParam String username, @RequestParam String password, HttpServletRequest request) throws UnknownHostException {
        return userService.login(username, password, request);
    }

    /**
     * 查询用户信息（需权限校验）
     */
    @GetMapping("/{userId}")
    public R<User> getUser(@PathVariable Long userId, HttpServletRequest request) throws UnknownHostException {
        return userService.getUser(userId, request);
    }

    /**
     * 修改用户信息（需权限校验）
     */
    @PutMapping("/{userId}")
    public R<Void> updateUser(@PathVariable Long userId, @RequestBody User user, HttpServletRequest request) throws UnknownHostException {
        return userService.updateUser(userId, user,request);
    }

    /**
     * 密码重置接口
     */
    @PostMapping("/resetPassword")
    public R<Void> resetPassword(HttpServletRequest request) throws UnknownHostException {
        return userService.resetPassword(request);
    }
}
package com.chiening.userservice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chiening.userservice.entity.PageResult;
import com.chiening.userservice.entity.R;
import com.chiening.userservice.entity.User;
import com.chiening.userservice.service.UserService;
import com.chiening.userservice.service.UsersService;
import com.chiening.userservice.entity.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @description: 分页查询接口
 * @author: ChiefNing
 * @date: 2025年06月19日
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;
    /**
     * 分页查询
     */
    @PostMapping
    public R<IPage<User>> selectPage(@RequestBody PageResult pageResult, HttpServletRequest request) {
        return usersService.selectPage(pageResult, request);
    }

}

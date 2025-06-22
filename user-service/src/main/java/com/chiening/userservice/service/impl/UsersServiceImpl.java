package com.chiening.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chiening.userservice.clients.PermissionClient;
import com.chiening.userservice.entity.PageResult;
import com.chiening.userservice.entity.R;
import com.chiening.userservice.entity.User;
import com.chiening.userservice.mapper.UserMapper;
import com.chiening.userservice.service.UsersService;
import com.chiening.userservice.utils.AuthorizeFilter;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @description: 分页查询实现类
 * @author: ChiefNing
 * @date: 2025年06月19日
 */
@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PermissionClient permissionClient;


    /**
     * 分页查询
     *
     * @param request
     */
    @Override
    public R<IPage<User>> selectPage(PageResult result,  HttpServletRequest request) {
        if(!AuthorizeFilter.isUserLogin(request)) {
            return R.error(403, "未登录！无法查询");
        }
        Long RoleId = AuthorizeFilter.getUserId(request);
        String RoleCode = permissionClient.getUserRoleCode(RoleId);
        Page<User> page = new Page<>(result.getPageNo(),result.getPageSize());
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if("user".equals(RoleCode)){//查询自己的
            queryWrapper.eq(User::getUserId, RoleId);
            System.out.println("user查询");
        }else if("admin".equals(RoleCode)){//查询普通用户的
            List<Long> usersIdList = permissionClient.getAllUser();
            queryWrapper.in(User::getUserId, permissionClient.getAllUser());
            System.out.println("admin查询");
            System.out.println(usersIdList);
        }else{
            System.out.println("super_admin查询");
        }
        return R.success(userMapper.selectPage(page, queryWrapper));
    }

}

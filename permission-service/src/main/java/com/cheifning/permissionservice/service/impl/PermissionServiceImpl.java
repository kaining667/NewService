package com.cheifning.permissionservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cheifning.permissionservice.entity.Role;
import com.cheifning.permissionservice.entity.UserRole;
import com.cheifning.permissionservice.mapper.RoleMapper;
import com.cheifning.permissionservice.mapper.UserRoleMapper;
import com.cheifning.permissionservice.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {


    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;
    /**
     * 绑定默认角色（普通用户）
     *
     * @param userId
     */
    @Override
    public void bindDefaultRole(Long userId) {
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(2);
        userRoleMapper.insert(userRole);
    }

    /**
     * 查询用户角色码
     *
     * @param userId
     */
    @Override
    public String getUserRoleCode(Long userId) {
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        UserRole userRole = userRoleMapper.selectOne(queryWrapper);
        if(userRole==null){
            throw  new RuntimeException("未查询到用户消息");
        }
        QueryWrapper<Role> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("role_id",userRole.getRoleId());
        Role role = roleMapper.selectOne(queryWrapper1);
        return role.getRoleCode();
    }

    /**
     * 超管调用：升级用户为管理员
     *
     * @param userId
     */
    @Override
    public void upgradeToAdmin(Long userId) {
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        UserRole userRole = userRoleMapper.selectOne(queryWrapper);
        if(userRole==null){
            throw  new RuntimeException("用户不存在");
        }
        if(userRole.getRoleId()==3){
            throw  new RuntimeException("用户已经是管理员");
        }
        userRoleMapper.deleteById(userRole.getRoleId());
        UserRole userRole1 = new UserRole();
        userRole1.setUserId(userId);
        userRole1.setRoleId(3);
        userRoleMapper.insert(userRole1);
    }

    /**
     * 超管调用：降级用户为普通角色
     *
     * @param userId
     */
    @Override
    public void downgradeToUser(Long userId) {
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        UserRole userRole = userRoleMapper.selectOne(queryWrapper);
        if(userRole==null){
            throw  new RuntimeException("用户不存在");
        }
        if(userRole.getRoleId()==2){
            throw  new RuntimeException("用户已经是普通角色");
        }
        userRoleMapper.deleteById(userRole.getRoleId());
        UserRole userRole1 = new UserRole();
        userRole1.setUserId(userId);
        userRole1.setRoleId(2);
        userRoleMapper.insert(userRole1);
    }

    /**
     * 获取所有的user的id
     */
    @Override
    public List<Long> getAllUser() {
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id",2);
        List<UserRole> userRoles = userRoleMapper.selectList(queryWrapper);
        List<Long> userIds = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            userIds.add(userRole.getUserId());
        }
        return userIds;
    }
}

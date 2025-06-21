package com.cheifning.permissionservice.controller;

import com.cheifning.permissionservice.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @description: TODO
 * @author: ChiefNing
 * @date: 2025年06月19日
 */

@RestController
@RequestMapping("/")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 绑定用户id
     * @param userId
     */
    @PostMapping("/bindDefaultRole/{userId}")
    public void bindDefaultRole(@PathVariable Long userId) {
        permissionService.bindDefaultRole(userId);
    }

    /**
     * 查询用户状态码
     * @param userId
     */
    @GetMapping("/getUserRoleCode/{userId}")
    public String getUserRoleCode(@PathVariable Long userId) {
        return permissionService.getUserRoleCode(userId);
    }

    /**
     * 超管调用：升级用户为管理员
     * @param userId
     */
    @PutMapping("/upgradeToAdmin/{userId}")
    public void upgradeToAdmin(@PathVariable Long userId) {
        permissionService.upgradeToAdmin(userId);
    }

    /**
     * 超管调用：降级用户为普通角色
     * @param userId
     */
    @PutMapping("/downgradeToUser/{userId}")
    public void downgradeToUser(@PathVariable Long userId) {
        permissionService.downgradeToUser(userId);
    }

}

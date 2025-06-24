package com.chiening.userservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

/**
 * @description: PRC接口，为nacos上注册的perminssionservice上的一系列方法，调用对应接口并且进行查询
 * @author: ChiefNing
 * @date: 2025年06月19日
 */

@FeignClient("permissionservice")
public interface PermissionClient {

    /**
     * 绑定用户id
     * @param userId
     */
    @PostMapping("/bindDefaultRole/{userId}")
    void bindDefaultRole(@PathVariable Long userId);

    /**
     * 查询用户状态码
     * @param userId
     */
    @GetMapping("/getUserRoleCode/{userId}")
    String getUserRoleCode(@PathVariable Long userId);

    /**
     * 超管调用：升级用户为管理员
     * @param userId
     */
    @PutMapping("/upgradeToAdmin/{userId}")
    void upgradeToAdmin(@PathVariable Long userId);

    /**
     * 超管调用：降级用户为普通角色
     * @param userId
     */
    @PutMapping("/downgradeToUser/{userId}")
    void downgradeToUser(@PathVariable Long userId);

    /**
     * 返回所有普通角色的id
     */
    @GetMapping("/getAllUser")
    List<Long> getAllUser();
}

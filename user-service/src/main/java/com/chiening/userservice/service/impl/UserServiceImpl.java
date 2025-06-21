package com.chiening.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.chiening.userservice.clients.PermissionClient;
import com.chiening.userservice.entity.LogEvent;
import com.chiening.userservice.entity.R;
import com.chiening.userservice.entity.User;
import com.chiening.userservice.entity.UserInfo;
import com.chiening.userservice.mapper.UserMapper;
import com.chiening.userservice.producer.LogProducer;
import com.chiening.userservice.service.UserService;
import com.chiening.userservice.utils.AuthorizeFilter;
import com.chiening.userservice.utils.JwtUtil;
import com.chiening.userservice.entity.RegisterRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * @description: UserService实现类
 * @author: ChiefNing
 * @date: 2025年06月17日
 */


@Service
public  class UserServiceImpl implements UserService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LogProducer logProducer;

    @Autowired
    private PermissionClient permissionClient;


    public R<User> register(RegisterRequest request) throws UnknownHostException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", request.getUsername());
        User existUser = userMapper.selectOne(queryWrapper);
        if (existUser != null) {
            return R.error("用户名已经存在");
        }
        User user = new User();
        BeanUtils.copyProperties(request, user);
        user.setGmtCreate(new Date());
        byte[] saltBytes = new byte[8];
        new java.security.SecureRandom().nextBytes(saltBytes);
        user.setSalt(Base64.getEncoder().encodeToString(saltBytes));

        // 密码加密
        user.setPassword(DigestUtils.md5DigestAsHex((user.getPassword() + user.getSalt()).getBytes()));

        userMapper.insert(user);

        // 调用权限服务绑定默认角色
        permissionClient.bindDefaultRole(user.getUserId());

        // 发送操作日志到MQ
        buildLogEvent(user, "REGISTER", InetAddress.getLocalHost().getHostAddress(), "用户注册成功，用户名是：" + user.getUsername() + "密码是：" + user.getPassword());

        return R.success(user);
    }

    /**
     * 用户登录
     *
     * @param username
     * @param password
     */
    @Override
    public R<UserInfo> login(String username, String password, HttpServletRequest request) throws UnknownHostException {
        // 判断是否登录
        if(AuthorizeFilter.isUserLogin(request)) {
            return R.error("用户已经登录！");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        if(user == null){
            return R.error("用户不存在！");
        }
        password = DigestUtils.md5DigestAsHex((password + user.getSalt()).getBytes());
        if (!password.equals(user.getPassword())) {
            return R.error("密码错误！");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getUserId());
        userInfo.setUsername(username);
        userInfo.setToken(JwtUtil.getToken(user.getUserId()));

        buildLogEvent(user, "LOGIN", InetAddress.getLocalHost().getHostAddress(), "用户登录成功，用户名是：" + user.getUsername());

        return R.success(userInfo);
    }

    /**
     * 根据ID获取用户信息
     *
     * @param userId
     */
    @Override
    public R<User> getUser(Long userId, HttpServletRequest request) throws UnknownHostException {
        if(!AuthorizeFilter.isUserLogin(request)) {
            return R.error("未登录，无法进行操作！");
        }
        User user = userMapper.selectById(userId);
        if(user == null){
            return R.error("用户不存在！");
        }
        //Role是查询发出者，user是目标
        Long RoleId = AuthorizeFilter.getUserId(request);
        String RoleCode = permissionClient.getUserRoleCode(RoleId);
        String UserCode = permissionClient.getUserRoleCode(userId);
        if("admin".equals(RoleCode) && "super_admin".equals(UserCode)) {
            buildLogEvent(user, "QUERY", InetAddress.getLocalHost().getHostAddress(), "查询失败，越级查询，查询目标为" + user.getUsername() +
                    ",其级别是super_admin，查询者为" + userMapper.selectById(RoleId).getUsername() + ",其级别是admin");
            return R.error("越级查询！");
        }
        if("user".equals(RoleCode) && !RoleId.equals(userId)){
            buildLogEvent(user, "QUERY", InetAddress.getLocalHost().getHostAddress(), "查询失败，越级查询，查询目标为" + user.getUsername() +
                    "，查询者为" + userMapper.selectById(RoleId).getUsername() + "其级别是user，只能查询自己的");
            return R.error("越级查询！");
        }
        buildLogEvent(user, "QUERY", InetAddress.getLocalHost().getHostAddress(), "查询成功，越级查询，查询目标为" + user.getUsername() +
                "，查询者为" + userMapper.selectById(RoleId).getUsername());
        return R.success(user);
    }

    /**
     * 更新用户信息
     *
     * @param userId
     * @param user
     */
    @Override
    public R<Void> updateUser(Long userId, User user, HttpServletRequest request) throws UnknownHostException {
        if(!AuthorizeFilter.isUserLogin(request)) {
            return R.error("未登录，无法进行操作");
        }
        User existUser = userMapper.selectById(userId);
        if (existUser == null) {
            return R.error("用户不存在");
        }
        //Role是查询发出者，user是目标
        Long RoleId = AuthorizeFilter.getUserId(request);
        String RoleCode = permissionClient.getUserRoleCode(RoleId);
        String UserCode = permissionClient.getUserRoleCode(userId);
        if(("admin".equals(RoleCode) && "super_admin".equals(UserCode)) || ("user".equals(RoleCode) && !RoleId.equals(userId))) {
            buildLogEvent(user, "UPDATE", InetAddress.getLocalHost().getHostAddress(), "查询失败，越级修改，修改目标为" + user.getUsername() +
                    "，修改者为" + userMapper.selectById(RoleId).getUsername());
            return R.error("用户没有修改权限");
        }
        buildLogEvent(user, "UPDATE", InetAddress.getLocalHost().getHostAddress(), "修改成功！修改前的username,password,phone,email分别是" + existUser.getUsername() + " " +
                existUser.getPassword() + " " + existUser.getPhone() + "  " + existUser.getEmail() + "修改后的username,password,phone,email分别是" +
                user.getUsername() + " " + user.getPassword() + " " + user.getPhone() + "  " + user.getEmail());
        BeanUtils.copyProperties(user, existUser, "userId", "password", "gmtCreate");
        userMapper.updateById(existUser);
        return R.success(null);
    }

    /**
     * 密码重置
     * @param request
     */
    @Override
    public R<Void> resetPassword(HttpServletRequest request) throws UnknownHostException {
        Long RoleId = AuthorizeFilter.getUserId(request);
        String RoleCode = permissionClient.getUserRoleCode(RoleId);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", RoleId);
        User user = userMapper.selectOne(queryWrapper);
        if("user".equals(RoleCode)) {
            user.setPassword(DigestUtils.md5DigestAsHex(("123456" + user.getSalt()).getBytes()));
            userMapper.updateById(user);
        }else if("admin".equals(RoleCode)) {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            List<User> users = userMapper.selectList(wrapper);
            for(User iuser : users){
                Long userId = iuser.getUserId();
                String userCode = permissionClient.getUserRoleCode(userId);
                if("user".equals(userCode)) {
                    iuser.setPassword(DigestUtils.md5DigestAsHex(("123456" + user.getSalt()).getBytes()));
                    userMapper.updateById(iuser);
                }
            }
        }else if("super_admin".equals(RoleCode)) {
            LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(User::getPassword, "123456");
            userMapper.update(null, updateWrapper);
        }
        buildLogEvent(user, "RESEATPASSWORD", InetAddress.getLocalHost().getHostAddress(), "重置成功！");
        return R.success(null);
    }

    void buildLogEvent(User user, String action, String IP, String detail) {
        LogEvent logEvent = new LogEvent();
        logEvent.setUserId(user.getUserId());
        logEvent.setAction(action);
        logEvent.setIp(IP);
        logEvent.setDetail(detail);
        logProducer.sendLogEvent(logEvent);
    }
}

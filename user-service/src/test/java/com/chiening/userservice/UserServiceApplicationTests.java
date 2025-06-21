package com.chiening.userservice;

import com.chiening.userservice.clients.PermissionClient;
import com.chiening.userservice.entity.User;
import com.chiening.userservice.mapper.UserMapper;
import com.chiening.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.util.Base64;
import java.util.Date;

@SpringBootTest
class UserServiceApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PermissionClient permissionClient;
    @Test
    void contextLoads() {
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUsername("zyf" + i);
            user.setGmtCreate(new Date());
            byte[] saltBytes = new byte[8];
            new java.security.SecureRandom().nextBytes(saltBytes);
            user.setSalt(Base64.getEncoder().encodeToString(saltBytes));

            // 密码加密
            user.setPassword(DigestUtils.md5DigestAsHex((user.getPassword() + user.getSalt()).getBytes()));

            userMapper.insert(user);

            permissionClient.bindDefaultRole(user.getUserId());
        }
    }

}

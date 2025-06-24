package com.chiening.userservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chiening.userservice.entity.PageResult;
import com.chiening.userservice.entity.R;
import com.chiening.userservice.entity.User;

import javax.servlet.http.HttpServletRequest;

public interface UsersService {
    /**
     * 分页查询
     */
    R<IPage<User>> selectPage(PageResult result, HttpServletRequest request);
}

package com.chiening.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chiening.userservice.entity.User;
import org.apache.ibatis.annotations.Mapper;
/**
 * @description: mybaitsplus user
 * @author: ChiefNing
 * @date: 2025年06月17日
 */

@Mapper
public interface UserMapper extends BaseMapper<User> {}
package com.cheifning.loggingservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cheifning.loggingservice.entity.LogEvent;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description: TODO
 * @author: ChiefNing
 * @date: 2025年06月21日
 */

@Mapper
public interface LogMapper extends BaseMapper<LogEvent> {

}

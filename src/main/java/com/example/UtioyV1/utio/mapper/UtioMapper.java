package com.example.UtioyV1.utio.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.UtioyV1.utio.LogInfo.LogEntry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author user
* @description 针对表【sys_log(系统日志表)】的数据库操作Mapper
* @createDate 2025-12-12 16:50:10
* @Entity com.example.UtioyV1.model.SysLog
*/
@Mapper
public interface UtioMapper extends BaseMapper<LogEntry> {

    Integer save_log(@Param("list") List<LogEntry> log);

}





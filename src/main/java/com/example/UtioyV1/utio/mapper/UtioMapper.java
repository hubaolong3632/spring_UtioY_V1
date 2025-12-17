package com.example.UtioyV1.utio.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.UtioyV1.utio.LogInfo.LogEntryModel;
import com.example.UtioyV1.utio.model.ConfigKeyModel;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
* @author user
* @description 针对表【sys_log(系统日志表)】的数据库操作Mapper
* @createDate 2025-12-12 16:50:10
* @Entity com.example.UtioyV1.model.SysLog
*/
@Mapper
public interface UtioMapper{

    /**
     * 批量保存日志
     * @param log
     * @return
     */
    @Insert("""
    <script>
          INSERT INTO sys_log (
            type,
            message,
            user,
            level,
            reserved,
            create_time
            ) VALUES
            <!-- 遍历List集合，拼接多条值 -->
            <foreach collection="list" item="log" separator=",">
                (
                #{log.type},
                #{log.message},
                #{log.user},
                #{log.level.value}, <!-- 枚举转字符串：取枚举名称（INFO/ERROR） -->
                #{log.reserved},
                #{log.create_time}
                )
            </foreach>
    
    </script>
    
    """)
    Integer save_log(@Param("list") List<LogEntryModel> log);




    /**
     * 查询最近最新的N条记录
     */
    @Select("""
 <script>
        select type, reserved, message, user, level, create_time 
        from sys_log 
        <where>
            <if test='type != null and type != ""'>
                and type = #{type} 
            </if>
            <if test='level != null and level != ""'>
                and level = #{level} 
            </if>
       </where>
        order by create_time desc, id desc 
        limit #{sum}
      
 </script>
""")
    List<LogEntryModel> from_log(
            @Param("level") String level,  // 日志级别（可为空，空则忽略）
            @Param("type") String type,    // 日志类型（可为空，空则忽略）
            @Param("sum") Integer sum      // 查询条数（建议Controller设默认值如20）
    );


    /**
     * 查询配置文件
     * @return
     */
    @Select("""
      select config_key, config_value from sys_config where `is_status`= 1;
    """)
    List<ConfigKeyModel> from_config();
}
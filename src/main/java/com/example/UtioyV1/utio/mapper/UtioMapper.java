package com.example.UtioyV1.utio.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.UtioyV1.utio.LogInfo.LogEntryModel;
import com.example.UtioyV1.utio.model.ConfigKeyModel;
import com.example.UtioyV1.utio.model.JurisdictionModel;
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







    /**
     * 查询权限表配置权限，并转换为Map（key=name:jurisdiction_name，value=JurisdictionModel）
     * @return 目标Map
     */
    @Select("""
    WITH RECURSIVE
    -- 步骤1：生成数字序列（拆分逗号UID用）
    num_sequence AS (
        SELECT @row := @row + 1 AS num
        FROM sys_jurisdiction, (SELECT @row := 0) t
        LIMIT 100
    ),
    -- 步骤2：基础层：获取所有type=role的用户 + 拆分其UID为单个权限ID
    permission_tree AS (
        SELECT
            j1.jurisdiction AS role_name, -- 保留用户名
            j2.id AS perm_id,
            j2.uid AS perm_uid,
            j2.jurisdiction AS perm_name,
            j2.`describe` AS perm_desc,
            j2.uid AS parent_uid,
            1 AS recursion_depth -- 新增：递归深度标记，初始为1
        FROM sys_jurisdiction j1
        -- 拆分role的UID为单个值
        JOIN num_sequence nums
            ON nums.num <= LENGTH(j1.uid) - LENGTH(REPLACE(j1.uid, ',', '')) + 1
        -- 关联对应的权限
        LEFT JOIN sys_jurisdiction j2
            ON j2.type = 'jurisdiction'
            AND j2.id = SUBSTRING_INDEX(SUBSTRING_INDEX(j1.uid, ',', nums.num), ',', -1)
        WHERE
            j1.type = 'role'  -- 只查type=role的用户
            AND j2.is_status = 1
            AND j2.jurisdiction IS NOT NULL

        UNION ALL

        -- 步骤3：递归层：查询权限的子集（防循环引用+深度限制）
        SELECT
            pt.role_name, -- 继承用户名
            j3.id AS perm_id,
            j3.uid AS perm_uid,
            j3.jurisdiction AS perm_name,
            j3.`describe` AS perm_desc,
            j3.uid AS parent_uid,
            pt.recursion_depth + 1 AS recursion_depth -- 深度+1
        FROM permission_tree pt
        JOIN sys_jurisdiction j3
            ON j3.type = 'jurisdiction'
            AND j3.uid = pt.perm_id
            AND j3.is_status = 1
            AND j3.id <> pt.perm_id -- 核心：禁止权限自引用（防循环）
            AND pt.recursion_depth < 50 -- 限制递归深度（最多50层，可调整）
    ),
    -- 步骤4：判断每个权限是否有子集 + 拼接子集信息
    permission_with_children AS (
        SELECT
            pt.role_name,
            pt.perm_id,
            pt.perm_uid,
            pt.perm_name,
            pt.perm_desc,
            -- 判断是否有子集
            CASE
                WHEN EXISTS (
                    SELECT 1
                    FROM sys_jurisdiction j
                    WHERE j.type = 'jurisdiction'
                    AND j.uid = pt.perm_id
                    AND j.is_status = 1
                    AND j.id <> pt.perm_id -- 排除自引用
                ) THEN 1
                ELSE 0
            END AS has_children,
            -- 拼接子集ID（排除自引用）
            GROUP_CONCAT(CASE WHEN j4.id <> pt.perm_id THEN j4.id END SEPARATOR ',') AS children_ids,
            -- 拼接子集名称（排除自引用）
            GROUP_CONCAT(CASE WHEN j4.id <> pt.perm_id THEN j4.jurisdiction END SEPARATOR ',') AS children_names
        FROM permission_tree pt
        LEFT JOIN sys_jurisdiction j4
            ON j4.type = 'jurisdiction'
            AND j4.uid = pt.perm_id
            AND j4.is_status = 1
        GROUP BY pt.role_name, pt.perm_id, pt.perm_uid, pt.perm_name, pt.perm_desc
    )
    -- 最终结果：按用户名分组，展示每个用户的所有权限详情
    SELECT DISTINCT
        role_name AS name,
        perm_id AS id,
        perm_uid AS uid,
        perm_name AS jurisdiction_name,
        perm_desc AS jurisdiction_desc,
        has_children AS son_as, -- 1=有，0=无
        IFNULL(NULLIF(children_ids, ''), '') AS son_id, -- 清空空字符串转NULL的情况
        IFNULL(NULLIF(children_names, ''), '') AS son_name,
        -- 新增：拼接key字段（供后续封装使用）
        CONCAT(role_name, ':', perm_name) AS map_key
    FROM permission_with_children
    ORDER BY role_name,perm_id;
""")
// 先以map_key为临时键返回Map，后续再转换（MyBatis@MapKey仅支持单字段）
    @MapKey("map_key")
    Map<String, JurisdictionModel> from_jurisdiction();


}
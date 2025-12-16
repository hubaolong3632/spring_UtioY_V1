CREATE TABLE `sys_log` (
                           `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                           `type` varchar(100) DEFAULT NULL COMMENT '日志类别',
                           `message` text COMMENT '日志内容',
                           `user` varchar(100) DEFAULT NULL COMMENT '操作用户',
                           `level` varchar(20) NOT NULL COMMENT '日志等级',
                           `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           PRIMARY KEY (`id`),
                           KEY `idx_level` (`level`),
                           KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB  COMMENT='系统日志表';
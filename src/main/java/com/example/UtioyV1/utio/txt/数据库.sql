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


CREATE TABLE `sys_config` (
                              `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
                              `config_key` varchar(255)    DEFAULT NULL COMMENT '配置文件的键',
                              `config_value` varchar(400)   DEFAULT NULL COMMENT '配置文件的值',
                              `describe` varchar(255) DEFAULT NULL COMMENT '描述',
                              `is_status` tinyint DEFAULT '1' COMMENT '是否启用',
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB  COMMENT='配置表';
INSERT INTO `utio`.`sys_config` ( `config_key`, `config_value`, `describe`, `is_status`) VALUES ('file_url', 'https://smartfarmservice.00000.work', '文件url', 1);

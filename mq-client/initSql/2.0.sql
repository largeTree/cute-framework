-- mq消费失败记录
CREATE TABLE `mq_failed` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `msg_id` VARCHAR(50) NOT NULL COMMENT '消息ID',
  `topic` VARCHAR(200) NOT NULL COMMENT '消息主题',
  `tags` VARCHAR(200) NOT NULL COMMENT '消息标签',
  `biz_key` VARCHAR(200) NOT NULL COMMENT '消息业务主键',
  `biz_body` TEXT COMMENT '业务数据',
  `stacktrace` TEXT COMMENT '调用栈',
  `ext_prop` TEXT COMMENT '扩展属性',
  `reconsume_times` INT(11) NOT NULL COMMENT '重新消费次数',
  `born_time` TIMESTAMP NULL DEFAULT NULL COMMENT '消息生成时间',
  `server_id` VARCHAR(200) NOT NULL COMMENT '服务器',
  `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
  `unit_id` BIGINT(20) NOT NULL COMMENT '单元ID',
  `global_id` BIGINT(20) NOT NULL COMMENT 'GlobalId',
  `created_date` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '新增时间',
  `updated_date` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UQ_mq_failed_msgId` (`msg_id`)
)COMMENT='MQ消息消费失败记录';
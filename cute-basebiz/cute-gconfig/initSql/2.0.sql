CREATE TABLE `sc_gconfig` (
  `id` BIGINT(20) NOT NULL,
  `code` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '配置代码',
  `name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '配置名称',
  `domain` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '配置域',
  `val` VARCHAR(512) NOT NULL DEFAULT '' COMMENT '默认值',
  `input_type` TINYINT(3) NOT NULL DEFAULT '0' COMMENT '输入类型',
  `cat_id` TINYINT(3) NOT NULL DEFAULT '0' COMMENT '配置类别',
  `show_order` INT(11) NOT NULL DEFAULT '0' COMMENT '显示顺序',
  `created_time` DATETIME NOT NULL COMMENT '创建时间',
  `created_by` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '创建人',
  `updated_time` DATETIME NOT NULL COMMENT '更新时间',
  `updated_by` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_scConfig_code` (`code`),
  KEY `idx_scConfig_name` (`name`)
)COMMENT'全局配置';

CREATE TABLE `sc_gconfig_options` (
  `id` BIGINT(20) NOT NULL,
  `code` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '配置代码',
  `name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '配置选项名称',
  `opt_val` VARCHAR(512) NOT NULL DEFAULT '' COMMENT '选项值',
  `show_order` INT(11) NOT NULL DEFAULT '0' COMMENT '显示顺序',
  `created_time` DATETIME NOT NULL COMMENT '创建时间',
  `created_by` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '创建人',
  `updated_time` DATETIME NOT NULL COMMENT '更新时间',
  `updated_by` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_scConfigOptions_co` (`code`,`opt_val`)
)COMMENT'全局配置选项';

CREATE TABLE `sc_gconfig_owner_val` (
  `id` BIGINT(20) NOT NULL,
  `code` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '配置代码',
  `val` VARCHAR(512) NOT NULL DEFAULT '' COMMENT '默认值',
  `owner_type` INT(11) NOT NULL DEFAULT '0' COMMENT '所有者类型',
  `owner_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '所有者id',
  `created_time` DATETIME NOT NULL COMMENT '创建时间',
  `created_by` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '创建人',
  `updated_time` DATETIME NOT NULL COMMENT '更新时间',
  `updated_by` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_scGconfigOwnerVal_coo` (`code`,`owner_type`,`owner_id`)
) COMMENT'全局配置所有者值';
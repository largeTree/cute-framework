CREATE TABLE `ct_func`(
	`id` VARCHAR(50) NOT NULL PRIMARY KEY COMMENT'菜单ID',
	`parent_id` VARCHAR(50) NOT NULL COMMENT'上级菜单ID',
	`level` INT(3) NOT NULL COMMENT'菜单深度',
	`func_type` INT(11) NOT NULL COMMENT'菜单类型',
	`name` VARCHAR(10) NOT NULL COMMENT'菜单名',
	`term_cap` BIGINT(20) NOT NULL COMMENT'终端类型',
	`show_order` INT(11) NOT NULL COMMENT'显示顺序',
	`extra` JSON NULL COMMENT'扩展属性',
	`rem` VARCHAR(50) NOT NULL COMMENT'备注'
)COMMENT'功能菜单表';
CREATE INDEX `idx_func_pid` ON `ct_func` (`parent_id`);
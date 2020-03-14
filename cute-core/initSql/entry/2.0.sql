CREATE TABLE `ds_unit`(
	`id` BIGINT(20) NOT NULL PRIMARY KEY,
	`unit_id` BIGINT(20) NOT NULL DEFAULT 0 COMMENT'单元ID',
	`ds_id` VARCHAR(20) NOT NULL DEFAULT '' COMMENT'数据ID',
	`created_by` BIGINT(20) NOT NULL DEFAULT 0 COMMENT'创建人',
	`created_time` DATETIME NOT NULL COMMENT'创建时间',
	`updated_by` BIGINT(20) NOT NULL DEFAULT 0 COMMENT'更新人',
	`updated_time` DATETIME NOT NULL COMMENT'更新时间'
);
CREATE UNIQUE INDEX `UK_dsUnit_ud` ON ds_unit(`unit_id`, `ds_id`);
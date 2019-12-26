CREATE TABLE `request_log`(
	`id` BIGINT(20) NOT NULL PRIMARY KEY ,
	`api_key` VARCHAR(100) NOT NULL COMMENT'接口号',
	`server_id` VARCHAR(20) NOT NULL COMMENT'服务器id',
	`req_ip` VARCHAR(50) NOT NULL COMMENT'请求ip',
	`req_url` VARCHAR(500) NOT NULL COMMENT'requestUrl',
	`req_start_time` DATETIME(3) NOT NULL COMMENT'请求开始时间',
	`req_end_time` DATETIME(3) NOT NULL COMMENT'请求结束时间'
);
CREATE INDEX `idx_request_log_ApiKey` ON request_log(`api_key`);
ALTER TABLE `request_log` ADD COLUMN `status` TINYINT(3) NOT NULL DEFAULT 1 COMMENT'请求状态';
ALTER TABLE `request_log` ADD COLUMN `global_id` BIGINT(20) NOT NULL DEFAULT 0 COMMENT'请求状态';
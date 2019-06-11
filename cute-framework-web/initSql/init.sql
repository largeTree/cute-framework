CREATE TABLE `api_request_log`(
	`id` BIGINT(20) NOT NULL PRIMARY KEY ,
	`api_key` VARCHAR(100) NOT NULL COMMENT'接口号',
	`server_id` VARCHAR(20) NOT NULL COMMENT'服务器id',
	`req_ip` VARCHAR(50) NOT NULL COMMENT'请求ip',
	`req_url` VARCHAR(500) NOT NULL COMMENT'requestUrl',
	`req_start_time` DATETIME(3) NOT NULL COMMENT'请求开始时间',
	`req_end_time` DATETIME(3) NOT NULL COMMENT'请求结束时间'
);
CREATE INDEX `idx_request_log_ApiKey` ON api_request_log(`api_key`);
ALTER TABLE `api_request_log` ADD COLUMN `status` TINYINT(3) NOT NULL DEFAULT 1 COMMENT'请求状态';

-- 创建视图，方便查询数据
CREATE ALGORITHM = UNDEFINED VIEW v_api_request_log AS 
SELECT *,ROUND(UNIX_TIMESTAMP(req_end_time) * 1000 - UNIX_TIMESTAMP(req_start_time) * 1000, 0) AS cost_ms FROM api_request_log;
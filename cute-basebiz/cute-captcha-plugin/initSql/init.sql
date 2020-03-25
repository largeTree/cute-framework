-- 验证码
CREATE TABLE `cute_captcha` (
	`id` BIGINT(20) NOT NULL PRIMARY KEY COMMENT'主键',
	`session_key` VARCHAR(50) NOT NULL COMMENT'会话Key',
	`ip` VARCHAR(50) NOT NULL COMMENT'ip',
	`captcha` VARCHAR(20) NOT NULL COMMENT'验证码',
	`time_limit` BIGINT(20) NOT NULL COMMENT'时限,秒',
	`created_time` DATETIME NOT NULL COMMENT'创建时间'
)COMMENT'验证码';

CREATE UNIQUE INDEX `UK_captcha_SessionKey` ON `cute_captcha`(`session_key`);

-- 验证码历史记录
CREATE TABLE `cute_captcha_history` (
	`id` BIGINT(20) NOT NULL PRIMARY KEY COMMENT'主键',
	`session_key` VARCHAR(50) NOT NULL COMMENT'会话Key',
	`ip` VARCHAR(50) NOT NULL COMMENT'ip',
	`captcha` VARCHAR(20) NOT NULL COMMENT'验证码',
	`time_limit` BIGINT(20) NOT NULL COMMENT'时限',
	`cap_created_time` DATETIME NOT NULL COMMENT'验证码创建时间',
	`created_time` DATETIME NOT NULL COMMENT'创建时间'
)COMMENT'验证码历史记录';

CREATE INDEX `idx_captchaHistory_SessionKey` ON `cute_captcha_history`(`session_key`);
CREATE INDEX `idx_captchaHistory_Ip` ON `cute_captcha_history`(`ip`);

-- 黑名单
CREATE TABLE `cute_captcha_blacklist` (
	`id` BIGINT(20) NOT NULL PRIMARY KEY COMMENT'主键',
	`session_key` VARCHAR(50) NOT NULL COMMENT'会话Key',
	`time_limit` BIGINT(20) NOT NULL COMMENT'时限,秒,-1代表永久',
	`reason` VARCHAR(200) NOT NULL COMMENT'拉黑原因',
	`created_time` DATETIME NOT NULL COMMENT'创建时间'
);
CREATE INDEX `idx_blacklist_sessionKey` ON `cute_captcha_blacklist`(`session_key`);
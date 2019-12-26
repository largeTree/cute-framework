package com.qiuxs.captcha.dao;

import org.apache.ibatis.annotations.Param;

import com.qiuxs.captcha.entity.CaptchaBlacklist;
import com.qiuxs.cuteframework.core.persistent.database.dao.IBaseDao;
import com.qiuxs.cuteframework.tech.mybatis.dao.MyBatisRepository;

/**
 * Dao接口
 * 
 * 创建时间 ：2019-03-28 22:55:09
 * @author qiuxs
 *
 */
@MyBatisRepository
public interface CaptchaBlacklistDao extends IBaseDao<Long, CaptchaBlacklist> {

	/**
	 * 获取一个有效的黑名单记录
	 * sessionKey等于指定sessionKey，且当前时间减创建时间小于有效期或有效期为永久
	 * 
	 * 2019年4月2日 下午10:36:23
	 * @auther qiuxs
	 * @param sessionKey
	 * @return
	 */
	public CaptchaBlacklist getValidBySessionKey(@Param("sessionKey") String sessionKey);

}

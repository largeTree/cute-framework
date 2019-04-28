package com.qiuxs.captcha.dao;

import org.springframework.stereotype.Repository;

import com.qiuxs.cuteframework.core.persistent.database.dao.IBaseDao;
import com.qiuxs.captcha.entity.Captcha;

/**
 * 验证码Dao接口
 * 
 * 创建时间 ：2019-03-28 22:55:09
 * @author qiuxs
 *
 */
@Repository
public interface CaptchaDao extends IBaseDao<Long, Captcha> {

	/**
	 * 根据sessionKey获取一个已经发出去的验证码
	 * 
	 * 2019年4月9日 下午10:08:21
	 * @auther qiuxs
	 * @param sessionKey
	 * @return
	 */
	public Captcha getBySessionKey(String sessionKey);

}

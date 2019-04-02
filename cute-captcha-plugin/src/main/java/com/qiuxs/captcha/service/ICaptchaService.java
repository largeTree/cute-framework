package com.qiuxs.captcha.service;

import com.qiuxs.captcha.dao.CaptchaDao;
import com.qiuxs.captcha.entity.Captcha;
import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataPropertyService;


/**
 * 验证码服务接口
 * 
 * 创建时间 ：2019-03-28 22:55:09
 * @author qiuxs
 */
public interface ICaptchaService extends IDataPropertyService<Long, Captcha, CaptchaDao> {

	/**
	 * 生成一个验证码记录
	 * 
	 * 2019年4月2日 下午10:09:33
	 * @auther qiuxs
	 * @param mobile
	 * 		手机号
	 * @param ip
	 * 		ip地址
	 * @return
	 */
	public Captcha genCaptcha(String mobile, String ip);

}

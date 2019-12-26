package com.qiuxs.captcha.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qiuxs.captcha.dao.CaptchaDao;
import com.qiuxs.captcha.entity.Captcha;
import com.qiuxs.captcha.service.ICaptchaService;
import com.qiuxs.cuteframework.core.basic.utils.generator.RandomGenerator;
import com.qiuxs.cuteframework.core.persistent.database.modal.BaseField;
import com.qiuxs.cuteframework.core.persistent.database.modal.PropertyWrapper;
import com.qiuxs.cuteframework.core.persistent.database.service.AbstractDataPropertyService;
import com.qiuxs.cuteframework.core.persistent.database.service.filter.IServiceFilter;
import com.qiuxs.cuteframework.core.persistent.database.service.filter.impl.IdGenerateFilter;

/**
 * 验证码服务类
 *
 * @author qiuxs
 *
 */
@Service
public class CaptchaService extends AbstractDataPropertyService<Long, Captcha, CaptchaDao> implements ICaptchaService {

	private static final String TABLE_NAME = "cute_captcha";
	private static final String PK_FIELD = "id";

	public CaptchaService() {
		super(Long.class, Captcha.class, TABLE_NAME, PK_FIELD);
	}

	@Resource
	private CaptchaDao captchaDao;

	@Override
	protected CaptchaDao getDao() {
		return this.captchaDao;
	}
	
	@Override
	public Captcha getBySessionKey(String sessionKey) {
		return this.getDao().getBySessionKey(sessionKey);
	}

	@Override
	@Transactional
	public Captcha genCaptcha(String mobile, String ip) {
		Captcha captcha = new Captcha();
		captcha.setSessionKey(mobile);
		captcha.setIp(ip);
		captcha.setCaptcha(RandomGenerator.getRandCode());
		this.save(captcha);
		return captcha;
	}

	@Override
	protected void initServiceFilters(List<IServiceFilter<Long, Captcha>> serviceFilters) {
		serviceFilters.add(new IdGenerateFilter<>(TABLE_NAME));
	}

	@Override
	protected void initProps(List<PropertyWrapper<?>> props) {
		super.initProps(props);

		PropertyWrapper<?> prop = null;

		prop = new PropertyWrapper<String>(new BaseField("sessionKey", "会话Key", String.class), null);
		props.add(prop);

		prop = new PropertyWrapper<String>(new BaseField("ip", "ip", String.class), null);
		props.add(prop);

		prop = new PropertyWrapper<String>(new BaseField("captcha", "验证码", String.class), null);
		props.add(prop);

		prop = new PropertyWrapper<Long>(new BaseField("timeLimit", "时限,秒", Long.class), null);
		props.add(prop);
	}

}

package com.qiuxs.captcha.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qiuxs.captcha.config.CaptchaEnvironmentConfig;
import com.qiuxs.captcha.dao.CaptchaBlacklistDao;
import com.qiuxs.captcha.entity.CaptchaBlacklist;
import com.qiuxs.captcha.service.ICaptchaBlacklistService;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.NumberUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.persistent.database.modal.BaseField;
import com.qiuxs.cuteframework.core.persistent.database.modal.PropertyWrapper;
import com.qiuxs.cuteframework.core.persistent.database.service.AbstractDataPropertyService;
import com.qiuxs.cuteframework.core.persistent.database.service.filter.IServiceFilter;
import com.qiuxs.cuteframework.core.persistent.database.service.filter.impl.IdGenerateFilter;

/**
 * 验证码黑名单服务类
 *
 * @author qiuxs
 *
 */
@Service
public class CaptchaBlacklistService extends AbstractDataPropertyService<Long, CaptchaBlacklist, CaptchaBlacklistDao> implements ICaptchaBlacklistService {

	private static final String TABLE_NAME = "cute_captcha_blacklist";
	
	@Resource
	private CaptchaEnvironmentConfig captchaEnvironmentConfig;
	
	public CaptchaBlacklistService() {
		super(Long.class, CaptchaBlacklist.class, TABLE_NAME);
	}

	@Resource
	private CaptchaBlacklistDao captchaBlacklistDao;

	@Override
	public void checkInBlacklist(String sessionKey) {
		CaptchaBlacklist blacklist = this.getDao().getValidBySessionKey(sessionKey);
		if (blacklist != null) {
			ExceptionUtils.throwLogicalException(blacklist.getReason());
		}
	}
	
	@Override
	protected void initCreate(CaptchaBlacklist bean) {
		// 没设置的设置为默认值
		if (StringUtils.isBlank(bean.getReason())) {
			bean.setReason(this.captchaEnvironmentConfig.getDefaultBalcklistReason());
		}
		if (NumberUtils.isEmpty(bean.getTimeLimit())) {
			bean.setTimeLimit(this.captchaEnvironmentConfig.getDefaultTimeLimit());
		}
		super.initCreate(bean);
	}

	@Override
	protected CaptchaBlacklistDao getDao() {
		return this.captchaBlacklistDao;
	}

	@Override
	protected void initServiceFilters(List<IServiceFilter<Long, CaptchaBlacklist>> serviceFilters) {
		serviceFilters.add(new IdGenerateFilter<>(TABLE_NAME));
	}

	@Override
	protected void initProps(List<PropertyWrapper<?>> props) {
		super.initProps(props);

		PropertyWrapper<?> prop = null;

		prop = new PropertyWrapper<String>(new BaseField("sessionKey", "会话Key", String.class), null);
		props.add(prop);

		prop = new PropertyWrapper<Long>(new BaseField("timeLimit", "时限,秒,-1代表永久", Long.class), null);
		props.add(prop);

		prop = new PropertyWrapper<String>(new BaseField("reason", "拉黑原因", String.class), null);
		props.add(prop);
	}

}

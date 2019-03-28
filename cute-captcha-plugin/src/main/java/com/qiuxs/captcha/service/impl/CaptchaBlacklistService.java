package com.qiuxs.captcha.service.impl;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.core.persistent.database.modal.PropertyWrapper;
import com.qiuxs.cuteframework.core.persistent.database.modal.BaseField;
import com.qiuxs.cuteframework.core.persistent.database.service.AbstractDataPropertyService;
import com.qiuxs.cuteframework.core.persistent.database.service.filter.IServiceFilter;
import com.qiuxs.cuteframework.core.persistent.database.service.filter.impl.IdGenerateFilter;
import com.qiuxs.captcha.dao.CaptchaBlacklistDao;
import com.qiuxs.captcha.entity.CaptchaBlacklist;
import com.qiuxs.captcha.service.ICaptchaBlacklistService;
/**
 * 服务类
 *
 * @author qiuxs
 *
 */
@Service
public class CaptchaBlacklistService extends AbstractDataPropertyService<Long, CaptchaBlacklist, CaptchaBlacklistDao> implements ICaptchaBlacklistService {

	private static final String TABLE_NAME = "cute_captcha_blacklist";

	public CaptchaBlacklistService() {
		super(Long.class, CaptchaBlacklist.class, TABLE_NAME);
	}

	@Resource
	private CaptchaBlacklistDao captchaBlacklistDao;

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

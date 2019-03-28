package com.qiuxs.captcha.service.impl;

import java.util.List;
import javax.annotation.Resource;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.core.persistent.database.modal.PropertyWrapper;
import com.qiuxs.cuteframework.core.persistent.database.modal.BaseField;
import com.qiuxs.cuteframework.core.persistent.database.service.AbstractDataPropertyService;
import com.qiuxs.cuteframework.core.persistent.database.service.filter.IServiceFilter;
import com.qiuxs.cuteframework.core.persistent.database.service.filter.impl.IdGenerateFilter;
import com.qiuxs.captcha.dao.CaptchaHistoryDao;
import com.qiuxs.captcha.entity.CaptchaHistory;
import com.qiuxs.captcha.service.ICaptchaHistoryService;
/**
 * 验证码历史记录服务类
 *
 * @author qiuxs
 *
 */
@Service
public class CaptchaHistoryService extends AbstractDataPropertyService<Long, CaptchaHistory, CaptchaHistoryDao> implements ICaptchaHistoryService {

	private static final String TABLE_NAME = "cute_captcha_history";

	public CaptchaHistoryService() {
		super(Long.class, CaptchaHistory.class, TABLE_NAME);
	}

	@Resource
	private CaptchaHistoryDao captchaHistoryDao;

	@Override
	protected CaptchaHistoryDao getDao() {
		return this.captchaHistoryDao;
	}

	@Override
	protected void initServiceFilters(List<IServiceFilter<Long, CaptchaHistory>> serviceFilters) {
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
		
		prop = new PropertyWrapper<Long>(new BaseField("timeLimit", "时限", Long.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<Date>(new BaseField("capCreatedTime", "验证码创建时间", Date.class), null);
		props.add(prop);
	}

}

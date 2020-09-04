package com.qiuxs.gconfig.service.impl;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import com.qiuxs.cuteframework.core.persistent.database.modal.PropertyWrapper;
import com.qiuxs.cuteframework.core.persistent.database.modal.BaseField;
import com.qiuxs.cuteframework.core.persistent.database.service.AbstractDataPropertyUKService;
import com.qiuxs.cuteframework.core.basic.code.DirectCodeCenter;
import com.qiuxs.cuteframework.core.basic.code.utils.CodeUtils;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.persistent.database.service.filter.IServiceFilter;
import com.qiuxs.cuteframework.core.persistent.database.service.filter.impl.IdGenerateFilter;
import com.qiuxs.gconfig.dao.ScGconfigDao;
import com.qiuxs.gconfig.entity.ScGconfig;
import com.qiuxs.gconfig.service.IScGconfigService;
/**
 * 全局配置服务类
 *
 * @author qiuxs
 *
 */
@Service
public class ScGconfigService extends AbstractDataPropertyUKService<Long, ScGconfig, ScGconfigDao> implements IScGconfigService {

	private static final String TABLE_NAME = "sc_gconfig";
	private static final String PK_FIELD = "id";

	public ScGconfigService() {
		super(Long.class, ScGconfig.class, TABLE_NAME, PK_FIELD);
		CodeUtils.genDirectCode(ScGconfig.class);
	}

	@Resource
	private ScGconfigDao scGconfigDao;

	@Override
	protected ScGconfigDao getDao() {
		return this.scGconfigDao;
	}
	
	public ScGconfig getByUk(String domain, String code) {
		return super.getByUkInner("domain", domain, "code", code);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteByUk(String domain, String code) {
		return super.deleteByUkInner("domain", domain, "code", code);
	}
	
	public boolean isExistByUk(String domain, String code) {
		return super.isExistByUkInner("domain", domain, "code", code);
	}
	
	public boolean isExistOtherByUk(Long pk, String domain, String code) {
		return super.isExistOtherByUkInner(pk, "domain", domain, "code", code);
	}
	
	protected void createInner(ScGconfig bean) {
		if (!this.isExistByUk(bean.getDomain(), bean.getCode())) {
			this.getDao().insert(bean);
		} else {
			ExceptionUtils.throwLoginException("dup_records");
		}
	}

	@Override
	protected void initServiceFilters(List<IServiceFilter<Long, ScGconfig>> serviceFilters) {
		serviceFilters.add(new IdGenerateFilter<>(TABLE_NAME));
	}

	@Override
	protected void initProps(List<PropertyWrapper<?>> props) {
		super.initProps(props);
		
		PropertyWrapper<?> prop = null;
		
		prop = new PropertyWrapper<String>(new BaseField("code", "配置代码", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<String>(new BaseField("name", "配置名称", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<String>(new BaseField("domain", "配置域", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<String>(new BaseField("val", "默认值", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<Integer>(new BaseField("inputType", "输入类型", Integer.class), DirectCodeCenter.getDirectCodeHouse(ScGconfig.DOMAIN_INPUT_TYPE));
		props.add(prop);
		
		prop = new PropertyWrapper<Integer>(new BaseField("catId", "配置类别", Integer.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<Integer>(new BaseField("showOrder", "显示顺序", Integer.class), null);
		props.add(prop);
	}

}

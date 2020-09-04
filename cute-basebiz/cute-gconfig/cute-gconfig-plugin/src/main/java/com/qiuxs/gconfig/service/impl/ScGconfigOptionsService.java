package com.qiuxs.gconfig.service.impl;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import com.qiuxs.cuteframework.core.persistent.database.modal.PropertyWrapper;
import com.qiuxs.cuteframework.core.persistent.database.modal.BaseField;
import com.qiuxs.cuteframework.core.persistent.database.service.AbstractDataPropertyUKService;
import com.qiuxs.cuteframework.core.basic.Constants.DsType;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.MapUtils;
import com.qiuxs.cuteframework.core.persistent.database.service.filter.IServiceFilter;
import com.qiuxs.cuteframework.core.persistent.database.service.filter.impl.IdGenerateFilter;
import com.qiuxs.gconfig.dao.ScGconfigOptionsDao;
import com.qiuxs.gconfig.entity.ScGconfigOptions;
import com.qiuxs.gconfig.service.IScGconfigOptionsService;
/**
 * 全局配置选项服务类
 *
 * @author qiuxs
 *
 */
@Service
public class ScGconfigOptionsService extends AbstractDataPropertyUKService<Long, ScGconfigOptions, ScGconfigOptionsDao> implements IScGconfigOptionsService {

	private static final String TABLE_NAME = "sc_gconfig_options";
	private static final String PK_FIELD = "id";

	public ScGconfigOptionsService() {
		super(Long.class, ScGconfigOptions.class, TABLE_NAME, PK_FIELD);
	}

	@Resource
	private ScGconfigOptionsDao scGconfigOptionsDao;
	
	
	@Override
	public List<ScGconfigOptions> getByCode(String domian, String code) {
		return this.findByMap(MapUtils.genMap("domian", domian, "code", code));
	}

	@Override
	protected ScGconfigOptionsDao getDao() {
		return this.scGconfigOptionsDao;
	}
	
	@Override
	public String getDsType() {
		return DsType.ENTRY.value();
	}
	
	public ScGconfigOptions getByUk(String domian, String code, String optVal) {
		return super.getByUkInner("domian", domian, "code", code, "optVal", optVal);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteByUk(String domian, String code, String optVal) {
		return super.deleteByUkInner("domian", domian, "code", code, "optVal", optVal);
	}
	
	public boolean isExistByUk(String domian, String code, String optVal) {
		return super.isExistByUkInner("domian", domian, "code", code, "optVal", optVal);
	}
	
	public boolean isExistOtherByUk(Long pk, String domian, String code, String optVal) {
		return super.isExistOtherByUkInner(pk, "domian", domian, "code", code, "optVal", optVal);
	}
	
	protected void createInner(ScGconfigOptions bean) {
		if (!this.isExistByUk(bean.getDomain(), bean.getCode(), bean.getOptVal())) {
			this.getDao().insert(bean);
		} else {
			ExceptionUtils.throwLoginException("dup_records");
		}
	}

	@Override
	protected void initServiceFilters(List<IServiceFilter<Long, ScGconfigOptions>> serviceFilters) {
		serviceFilters.add(new IdGenerateFilter<>(TABLE_NAME));
	}

	@Override
	protected void initProps(List<PropertyWrapper<?>> props) {
		super.initProps(props);
		
		PropertyWrapper<?> prop = null;
		
		prop = new PropertyWrapper<String>(new BaseField("domain", "业务域", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<String>(new BaseField("code", "配置代码", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<String>(new BaseField("name", "配置选项名称", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<String>(new BaseField("optVal", "选项值", String.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<Integer>(new BaseField("showOrder", "显示顺序", Integer.class), null);
		props.add(prop);
	}

}

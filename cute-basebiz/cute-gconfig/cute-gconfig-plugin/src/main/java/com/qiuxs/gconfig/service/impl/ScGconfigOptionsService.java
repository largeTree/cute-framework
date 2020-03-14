package com.qiuxs.gconfig.service.impl;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import com.qiuxs.cuteframework.core.persistent.database.modal.PropertyWrapper;
import com.qiuxs.cuteframework.core.persistent.database.modal.BaseField;
import com.qiuxs.cuteframework.core.persistent.database.service.AbstractDataPropertyUKService;
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
	public List<ScGconfigOptions> getByCode(String code) {
		return this.findByMap(MapUtils.genMap("code", code));
	}

	@Override
	protected ScGconfigOptionsDao getDao() {
		return this.scGconfigOptionsDao;
	}
	
	public ScGconfigOptions getByUk(String code, String optVal) {
		return super.getByUkInner("code", code, "optVal", optVal);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteByUk(String code, String optVal) {
		return super.deleteByUkInner("code", code, "optVal", optVal);
	}
	
	public boolean isExistByUk(String code, String optVal) {
		return super.isExistByUkInner("code", code, "optVal", optVal);
	}
	
	public boolean isExistOtherByUk(Long pk, String code, String optVal) {
		return super.isExistOtherByUkInner(pk, "code", code, "optVal", optVal);
	}
	
	protected void createInner(ScGconfigOptions bean) {
		if (!this.isExistByUk(bean.getCode(), bean.getOptVal())) {
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
package com.qiuxs.cuteframework.core.persistent.unit.service.impl;

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
import com.qiuxs.cuteframework.core.persistent.database.service.filter.IServiceFilter;
import com.qiuxs.cuteframework.core.persistent.database.service.filter.impl.IdGenerateFilter;
import com.qiuxs.cuteframework.core.persistent.unit.dao.DsUnitDao;
import com.qiuxs.cuteframework.core.persistent.unit.entity.DsUnit;
import com.qiuxs.cuteframework.core.persistent.unit.service.IDsUnitService;
/**
 * 单元对应数据库关系服务类
 *
 * @author qiuxs
 *
 */
@Service
public class DsUnitService extends AbstractDataPropertyUKService<Long, DsUnit, DsUnitDao> implements IDsUnitService {

	private static final String TABLE_NAME = "ds_unit";
	private static final String PK_FIELD = "id";

	public DsUnitService() {
		super(Long.class, DsUnit.class, TABLE_NAME, PK_FIELD);
	}

	@Resource
	private DsUnitDao dsUnitDao;

	@Override
	protected DsUnitDao getDao() {
		return this.dsUnitDao;
	}
	
	@Override
	public String getDsType() {
		return DsType.ENTRY.value();
	}
	
	@Override
	public boolean hasDsUnit() {
		return this.getDao().hasDsUnit() > 0;
	}
	
	public DsUnit getByUk(Long unitId, String dsId) {
		return super.getByUkInner("unitId", unitId, "dsId", dsId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteByUk(Long unitId, String dsId) {
		return super.deleteByUkInner("unitId", unitId, "dsId", dsId);
	}
	
	public boolean isExistByUk(Long unitId, String dsId) {
		return super.isExistByUkInner("unitId", unitId, "dsId", dsId);
	}
	
	public boolean isExistOtherByUk(Long pk, Long unitId, String dsId) {
		return super.isExistOtherByUkInner(pk, "unitId", unitId, "dsId", dsId);
	}
	
	protected void createInner(DsUnit bean) {
		if (!this.isExistByUk(bean.getUnitId(), bean.getDsId())) {
			this.getDao().insert(bean);
		} else {
			ExceptionUtils.throwLoginException("dup_records");
		}
	}

	@Override
	protected void initServiceFilters(List<IServiceFilter<Long, DsUnit>> serviceFilters) {
		serviceFilters.add(new IdGenerateFilter<>(TABLE_NAME));
	}

	@Override
	protected void initProps(List<PropertyWrapper<?>> props) {
		super.initProps(props);
		
		PropertyWrapper<?> prop = null;
		
		prop = new PropertyWrapper<Long>(new BaseField("unitId", "单元ID", Long.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<String>(new BaseField("dsId", "数据ID", String.class), null);
		props.add(prop);
	}

}

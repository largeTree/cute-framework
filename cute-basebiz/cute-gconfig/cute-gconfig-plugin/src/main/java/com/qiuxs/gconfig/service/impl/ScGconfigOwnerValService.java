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
import com.qiuxs.gconfig.dao.ScGconfigOwnerValDao;
import com.qiuxs.gconfig.entity.ScGconfig;
import com.qiuxs.gconfig.entity.ScGconfigOwnerVal;
import com.qiuxs.gconfig.service.IScGconfigOwnerValService;

/**
 * 全局配置所有者的值服务类
 *
 * @author qiuxs
 *
 */
@Service
public class ScGconfigOwnerValService extends AbstractDataPropertyUKService<Long, ScGconfigOwnerVal, ScGconfigOwnerValDao> implements IScGconfigOwnerValService {

	private static final String TABLE_NAME = "sc_gconfig_owner_val";
	private static final String PK_FIELD = "id";

	public ScGconfigOwnerValService() {
		super(Long.class, ScGconfigOwnerVal.class, TABLE_NAME, PK_FIELD);
		CodeUtils.genDirectCode(ScGconfigOwnerVal.class);
	}

	@Resource
	private ScGconfigOwnerValDao scGconfigOwnerValDao;

	@Resource
	private ScGconfigService scGconfigService;

	@Override
	public ScGconfigOwnerVal getOwnerVal(int ownerType, Long userId, String code) {
		ScGconfigOwnerVal ownerVal = this.getByUk(code, ownerType, userId);
		// 没有取到该用户的值，则继续取上级
		if (ownerVal == null) {
			// 取系统级参数配置
			ownerVal = this.getByUk(code, ScGconfigOwnerVal.OWNER_TYPE_SYSTEM, 0L);
			// 还是为空，取参数默认值
			if (ownerVal == null) {
				ScGconfig scGconfig = scGconfigService.getByUk(code);
				if (scGconfig == null) {
					ExceptionUtils.throwLogicalException("gconfig_not_exists", code);
				}
				ownerVal = new ScGconfigOwnerVal();
				ownerVal.setCode(code);
				ownerVal.setVal(scGconfig.getVal());
				ownerVal.setOwnerId(userId);
				ownerVal.setOwnerType(ownerType);
				ownerVal.setCreatedBy(scGconfig.getCreatedBy());
				ownerVal.setCreatedTime(scGconfig.getCreatedTime());
				ownerVal.setUpdatedBy(scGconfig.getUpdatedBy());
				ownerVal.setUpdatedTime(scGconfig.getUpdatedTime());
			}
		}
		return ownerVal;
	}

	@Override
	protected ScGconfigOwnerValDao getDao() {
		return this.scGconfigOwnerValDao;
	}

	public ScGconfigOwnerVal getByUk(String code, Integer ownerType, Long ownerId) {
		return super.getByUkInner("code", code, "ownerType", ownerType, "ownerId", ownerId);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteByUk(String code, Integer ownerType, Long ownerId) {
		return super.deleteByUkInner("code", code, "ownerType", ownerType, "ownerId", ownerId);
	}

	public boolean isExistByUk(String code, Integer ownerType, Long ownerId) {
		return super.isExistByUkInner("code", code, "ownerType", ownerType, "ownerId", ownerId);
	}

	public boolean isExistOtherByUk(Long pk, String code, Integer ownerType, Long ownerId) {
		return super.isExistOtherByUkInner(pk, "code", code, "ownerType", ownerType, "ownerId", ownerId);
	}

	protected void createInner(ScGconfigOwnerVal bean) {
		if (!this.isExistByUk(bean.getCode(), bean.getOwnerType(), bean.getOwnerId())) {
			this.getDao().insert(bean);
		} else {
			ExceptionUtils.throwLoginException("dup_records");
		}
	}

	@Override
	protected void initServiceFilters(List<IServiceFilter<Long, ScGconfigOwnerVal>> serviceFilters) {
		serviceFilters.add(new IdGenerateFilter<>(TABLE_NAME));
	}

	@Override
	protected void initProps(List<PropertyWrapper<?>> props) {
		super.initProps(props);

		PropertyWrapper<?> prop = null;

		prop = new PropertyWrapper<String>(new BaseField("code", "配置代码", String.class), null);
		props.add(prop);

		prop = new PropertyWrapper<String>(new BaseField("val", "默认值", String.class), null);
		props.add(prop);

		prop = new PropertyWrapper<Integer>(new BaseField("ownerType", "所有者类型", Integer.class), DirectCodeCenter.getDirectCodeHouse(ScGconfigOwnerVal.DOMAIN_OWNER_TYPE));
		props.add(prop);

		prop = new PropertyWrapper<Long>(new BaseField("ownerId", "所有者id", Long.class), null);
		props.add(prop);
	}

}

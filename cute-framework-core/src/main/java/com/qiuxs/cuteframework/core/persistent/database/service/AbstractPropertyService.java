package com.qiuxs.cuteframework.core.persistent.database.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.basic.Constants;
import com.qiuxs.cuteframework.core.basic.utils.JsonUtils;
import com.qiuxs.cuteframework.core.basic.utils.ListUtils;
import com.qiuxs.cuteframework.core.persistent.database.entity.IEntity;
import com.qiuxs.cuteframework.core.persistent.database.modal.PropertyWrapper;
import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IPropertyService;

/**
 * 功能描述: <br/>
 * 新增原因: TODO<br/>
 * 新增日期: 2018年4月18日 下午10:29:22 <br/>
 * 
 * @author qiuxs
 * @version 1.0.0
 */
public abstract class AbstractPropertyService<PK extends Serializable, T extends IEntity<PK>>
        implements IPropertyService<PK, T> {

	protected static Logger log = LogManager.getLogger(AbstractPropertyService.class);

	private Class<T> pojoClass;

	private Class<PK> pkClass;

	private List<PropertyWrapper<?>> properties;

	public AbstractPropertyService(Class<PK> pkClass, Class<T> pojoClass) {
		this.pkClass = pkClass;
		this.pojoClass = pojoClass;
	}

	@Override
	public Class<T> getPojoClass() {
		return pojoClass;
	}

	@Override
	public Class<PK> getPkClass() {
		return pkClass;
	}

	protected List<PropertyWrapper<?>> getProperties() {
		if (this.properties == null) {
			this.properties = new ArrayList<>();
			this.initProps(this.properties);
		}
		return this.properties;
	}

	@Override
	public JSONObject translateBean(T bean, boolean wrapper) {
		if (bean == null) {
			return new JSONObject();
		}
		JSONObject jbean = JsonUtils.toJSONObject(bean);
		if (wrapper) {
			List<PropertyWrapper<?>> props = this.getProperties();
			JSONObject captions = new JSONObject();
			for (PropertyWrapper<?> prop : props) {
				if (prop.hasTranslater()) {
					String fileName = prop.getField().getName();
					String caption = prop.getCaption(jbean.get(fileName));
					captions.put(fileName, caption);
				}
			}
			jbean.put(Constants.CAPTION_KEY, captions);
		}
		return jbean;
	}

	@Override
	public JSONArray translateBeans(Collection<T> beans, boolean wrapper) {
		if (ListUtils.isNullOrEmpty(beans)) {
			return new JSONArray();
		}
		JSONArray dataList = new JSONArray(beans.size());
		for (Iterator<T> iter = beans.iterator(); iter.hasNext();) {
			dataList.add(this.translateBean(iter.next(), wrapper));
		}
		return dataList;
	}

	protected abstract void initProps(List<PropertyWrapper<?>> props);
}

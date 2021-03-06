package com.qiuxs.cuteframework.core.persistent.database.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.basic.Constants;
import com.qiuxs.cuteframework.core.basic.utils.JsonUtils;
import com.qiuxs.cuteframework.core.basic.utils.ListUtils;
import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.cuteframework.core.persistent.database.entity.IEntity;
import com.qiuxs.cuteframework.core.persistent.database.modal.BaseField;
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
	private Map<String, PropertyWrapper<?>> mapProperties;

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
		if (this.properties == null || EnvironmentContext.isDebug()) {
			this.initProperties();
		}
		return new ArrayList<PropertyWrapper<?>>(this.properties);
	}
	
	protected Map<String, PropertyWrapper<?>> getMapProperties() {
		if (this.mapProperties == null || EnvironmentContext.isDebug()) {
			this.initProperties();
		}
		return this.mapProperties;
	}
	
	private synchronized void initProperties() {
		if (this.properties == null || EnvironmentContext.isDebug()) {
    		this.properties = new ArrayList<>();
    		this.initProps(this.properties);
    		this.mapProperties = new HashMap<>();
    		this.properties.forEach(item -> {
    			this.mapProperties.put(item.getField().getName(), item);
    		});
		}
	}

	@Override
	public <DT> JSONObject translateBean(DT bean, boolean wrapper) {
		if (bean == null) {
			return new JSONObject();
		}
		JSONObject jbean = JsonUtils.toJSONObject(bean);
		if (wrapper) {
			Map<String, PropertyWrapper<?>> properties = this.getMapProperties();
			JSONObject captions = new JSONObject();

			for (Iterator<String> iter = jbean.keySet().iterator(); iter.hasNext();) {
				String fieldName = iter.next();
				PropertyWrapper<?> prop = properties.get(fieldName);
				if (prop != null && prop.hasTranslater()) {
					String caption = prop.getCaption(jbean.get(fieldName));
					captions.put(fieldName, caption);
				}
			}
			jbean.put(Constants.CAPTION_KEY, captions);
		}
		return jbean;
	}

	@Override
	public JSONArray translateBeans(Collection<?> beans, boolean wrapper) {
		if (ListUtils.isNullOrEmpty(beans)) {
			return new JSONArray();
		}
		JSONArray dataList = new JSONArray(beans.size());
		for (Iterator<?> iter = beans.iterator(); iter.hasNext();) {
			dataList.add(this.translateBean(iter.next(), wrapper));
		}
		return dataList;
	}

	protected void initProps(List<PropertyWrapper<?>> props) {
		PropertyWrapper<?> prop = null;
		
		prop = new PropertyWrapper<Date>(new BaseField("unitId", "单元ID", Long.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<Date>(new BaseField("createdBy", "创建人", Long.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<Date>(new BaseField("createdTime", "创建时间", Date.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<Date>(new BaseField("updatedBy", "更新人", Long.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<Date>(new BaseField("updatedTime", "更新时间", Date.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<Date>(new BaseField("deletedBy", "删除人", Long.class), null);
		props.add(prop);
		
		prop = new PropertyWrapper<Date>(new BaseField("deletedTime", "删除时间", Date.class), null);
		props.add(prop);
	}
}

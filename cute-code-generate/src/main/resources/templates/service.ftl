package ${packageName}.service.impl;

import java.util.List;
import javax.annotation.Resource;
<#if importClasses??>
<#list importClasses as importClass>
import ${importClass}
</#list>
</#if>

import org.springframework.stereotype.Service;
<#if (ukFields?size > 0)>
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
</#if>

import com.qiuxs.cuteframework.core.persistent.database.modal.PropertyWrapper;
import com.qiuxs.cuteframework.core.persistent.database.modal.BaseField;
<#if (ukFields?size == 0)>
import com.qiuxs.cuteframework.core.persistent.database.service.AbstractDataPropertyService;
<#else>
import com.qiuxs.cuteframework.core.persistent.database.service.AbstractDataPropertyUKService;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
</#if>
import com.qiuxs.cuteframework.core.persistent.database.service.filter.IServiceFilter;
import com.qiuxs.cuteframework.core.persistent.database.service.filter.impl.IdGenerateFilter;
import ${packageName}.dao.${className}Dao;
import ${packageName}.entity.${className};
import ${packageName}.service.I${className}Service;
/**
 * ${desc!}服务类
 *
 * @author ${author}
 *
 */
@Service
public class ${className}Service extends <#if (ukFields?size == 0)>AbstractDataPropertyService<#else>AbstractDataPropertyUKService</#if><${pkClass}, ${className}, ${className}Dao> implements I${className}Service {

	private static final String TABLE_NAME = "${tableName}";
	private static final String PK_FIELD = "${pkField}";

	public ${className}Service() {
		super(${pkClass}.class, ${className}.class, TABLE_NAME, PK_FIELD);
	}

	@Resource
	private ${className}Dao ${className?uncap_first}Dao;

	@Override
	protected ${className}Dao getDao() {
		return this.${className?uncap_first}Dao;
	}
	
	<#if (ukFields?size == 0)><#else>
	public ${className} getByUk(<#list ukFields as field>${field.javaType} ${field.name}<#if field_index &lt; ukFields?size - 1>, </#if></#list>) {
		return super.getByUkInner(<#list ukFields as field>"${field.name}", ${field.name}<#if field_index &lt; ukFields?size - 1>, </#if></#list>);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteByUk(<#list ukFields as field>${field.javaType} ${field.name}<#if field_index &lt; ukFields?size - 1>, </#if></#list>) {
		return super.deleteByUkInner(<#list ukFields as field>"${field.name}", ${field.name}<#if field_index &lt; ukFields?size - 1>, </#if></#list>);
	}
	
	public boolean isExistByUk(<#list ukFields as field>${field.javaType} ${field.name}<#if field_index &lt; ukFields?size - 1>, </#if></#list>) {
		return super.isExistByUkInner(<#list ukFields as field>"${field.name}", ${field.name}<#if field_index &lt; ukFields?size - 1>, </#if></#list>);
	}
	
	public boolean isExistOtherByUk(${pkClass} pk, <#list ukFields as field>${field.javaType} ${field.name}<#if field_index &lt; ukFields?size - 1>, </#if></#list>) {
		return super.isExistOtherByUkInner(pk, <#list ukFields as field>"${field.name}", ${field.name}<#if field_index &lt; ukFields?size - 1>, </#if></#list>);
	}
	
	protected void createInner(${className} bean) {
		if (!this.isExistByUk(<#list ukFields as field>bean.get${field.name?cap_first}()<#if field_index &lt; ukFields?size - 1>, </#if></#list>)) {
			this.getDao().insert(bean);
		} else {
			ExceptionUtils.throwLoginException("dup_records");
		}
	}
	</#if>

	@Override
	protected void initServiceFilters(List<IServiceFilter<${pkClass}, ${className}>> serviceFilters) {
		serviceFilters.add(new IdGenerateFilter<>(TABLE_NAME));
	}

	@Override
	protected void initProps(List<PropertyWrapper<?>> props) {
		super.initProps(props);
		
		PropertyWrapper<?> prop = null;
		<#list fields as field>
		<#if !field.ignoreEntity>
		
		prop = new PropertyWrapper<${field.javaType}>(new BaseField("${field.name}", "${field.comment!field.name}", ${field.javaType}.class), null);
		props.add(prop);
		</#if>
		</#list>
	}

}

package ${packageName}.service;

import ${packageName}.entity.${className};
import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataPropertyService;


/**
 * ${desc!}服务接口
 * 
 * 创建时间 ：${.now?string("yyyy-MM-dd HH:mm:ss")}
 * @author ${author!}
 */
public interface I${className}Service extends IDataPropertyService<${pkClass}, ${className}> {

	<#if (ukFields?size == 0)><#else>
	public ${className} getByUk(<#list ukFields as field>${field.javaType} ${field.name}<#if field_index &lt; ukFields?size - 1>, </#if></#list>);

	public int deleteByUk(<#list ukFields as field>${field.javaType} ${field.name}<#if field_index &lt; ukFields?size - 1>, </#if></#list>);
	
	public boolean isExistByUk(<#list ukFields as field>${field.javaType} ${field.name}<#if field_index &lt; ukFields?size - 1>, </#if></#list>);
	
	public boolean isExistOtherByUk(${pkClass} pk, <#list ukFields as field>${field.javaType} ${field.name}<#if field_index &lt; ukFields?size - 1>, </#if></#list>);
	</#if>

}

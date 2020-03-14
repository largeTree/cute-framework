package ${packageName}.dao;

<#if (ukFields?size == 0)>
import com.qiuxs.cuteframework.core.persistent.database.dao.IBaseDao;
<#else>
import com.qiuxs.cuteframework.core.persistent.database.dao.IBaseUKDao;
</#if>
import com.qiuxs.cuteframework.tech.mybatis.dao.MyBatisRepository;
import ${packageName}.entity.${className};

/**
 * ${desc!}Dao接口
 * 
 * 创建时间 ：${.now?string("yyyy-MM-dd HH:mm:ss")}
 * @author ${author}
 *
 */
@MyBatisRepository
public interface ${className}Dao extends <#if (ukFields?size == 0)>IBaseDao<#else>IBaseUKDao</#if><${pkClass}, ${className}> {

}

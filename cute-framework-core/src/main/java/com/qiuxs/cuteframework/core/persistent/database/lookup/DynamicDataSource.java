package com.qiuxs.cuteframework.core.persistent.database.lookup;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.qiuxs.cuteframework.core.basic.Constants.DsType;
import com.qiuxs.cuteframework.core.basic.utils.ReflectUtils;
import com.qiuxs.cuteframework.core.basic.utils.TypeAdapter;
import com.qiuxs.cuteframework.core.persistent.database.lookup.dto.DsInfo;
import com.qiuxs.cuteframework.tech.log.NoDbLogger;

public class DynamicDataSource extends AbstractRoutingDataSource {

	/** 查询数据库配置用的sql */
	private static final String SELECT_DS_SQL = "SELECT id,url,driver_class as driverClass,user_name AS userName,`password`,`type`,`flag`,`max_num` AS maxNum,`used_num` AS usedNum FROM ds_info;";

	/** 所有配置的数据库 */
	private Map<Object, DataSource> targetDataSources = new TreeMap<Object, DataSource>();

	/** 默认数据库 */
	private BasicDataSource defaultTargetDataSource;

	/** 数据库类型对应的数据ID,业务库List<String>,非业务库String */
	private Map<String, Object> mapDsTypeId = new HashMap<>();

	@Override
	protected Object determineCurrentLookupKey() {
		String dsId = DataSourceContext.getDsId();
		return dsId == null ? getEntryDb() : dsId;
	}

	@Override
	public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
		super.setDefaultTargetDataSource(defaultTargetDataSource);
		this.defaultTargetDataSource = (BasicDataSource) defaultTargetDataSource;
		this.fillTragetDataSources();
	}

	/**
	 * 根据数据库中dsInfo配置填充数据库
	 * 
	 * @author qiuxs
	 *
	 *
	 *         创建时间：2018年7月27日 下午11:58:31
	 */
	private void fillTragetDataSources() {
		List<DsInfo> dsInfos = new ArrayList<>();
		try (Connection conn = this.defaultTargetDataSource.getConnection();
				PreparedStatement stat = conn.prepareStatement(SELECT_DS_SQL);
				ResultSet rs = stat.executeQuery();) {
			while (rs.next()) {
				dsInfos.add(this.parseDsInfo(rs));
			}
		} catch (Exception e) {
			NoDbLogger.log.error("Connect to EntryDb Error ext=" + e.getLocalizedMessage(), e);
		}
		for (DsInfo dsInfo : dsInfos) {
			fillOneDs(dsInfo);
		}
		// 没有配置入口库时，设置默认数据库为入口库
		String entryType = DsType.ENTRY.value();
		if (this.mapDsTypeId.get(entryType) == null) {
			this.targetDataSources.put(entryType, defaultTargetDataSource);
			this.mapDsTypeId.put(entryType, entryType);
		}
		super.setTargetDataSources(new TreeMap<>(targetDataSources));
	}

	/**
	 * 转换一行为DsInfo对象
	 * 
	 * @author qiuxs
	 *
	 * @param rs
	 * @return
	 *
	 * 		创建时间：2018年7月27日 下午11:58:51
	 */
	private DsInfo parseDsInfo(ResultSet rs) {
		DsInfo dsInfo = new DsInfo();
		try {
			List<Field> fields = ReflectUtils.getDeclaredFields(DsInfo.class);
			for (Field field : fields) {
				field.setAccessible(true);
				String fieldName = field.getName();
				Object val = rs.getObject(fieldName);
				field.set(dsInfo, TypeAdapter.adapter(val, field.getType()));
			}
		} catch (Exception e) {
			NoDbLogger.log.error("parseDsInfo Error ext=" + e.getLocalizedMessage(), e);
		}
		return dsInfo;
	}

	/**
	 * 填充一个数据源
	 * 
	 * @author qiuxs
	 *
	 * @param dsInfo
	 *
	 *            创建时间：2018年7月27日 下午11:59:03
	 */
	private void fillOneDs(DsInfo dsInfo) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl(dsInfo.getUrl());
		dataSource.setUsername(dsInfo.getUserName());
		dataSource.setPassword(dsInfo.getPassword());
		dataSource.setDriverClassName(dsInfo.getDriverClass());
		dataSource.setInitialSize(this.defaultTargetDataSource.getInitialSize());
		dataSource.setMaxTotal(this.defaultTargetDataSource.getMaxTotal());
		dataSource.setMinIdle(this.defaultTargetDataSource.getMinIdle());
		dataSource.setMaxWaitMillis(this.defaultTargetDataSource.getMaxWaitMillis());
		dataSource.setValidationQuery(this.defaultTargetDataSource.getValidationQuery());
		String dsId = dsInfo.getId();
		this.targetDataSources.put(dsId, dataSource);
		String type = dsInfo.getType();
		if (DsType.ENTRY.value().equals(type)) {
			this.mapDsTypeId.put(DsType.ENTRY.value(), dsId);
		} else if (DsType.LOG.value().equals(type)) {
			this.mapDsTypeId.put(DsType.LOG.value(), dsId);
		} else if (DsType.SEQ.value().equals(type)) {
			this.mapDsTypeId.put(DsType.SEQ.value(), dsId);
		} else if (DsType.BIZ.value().equals(type)) {
			@SuppressWarnings("unchecked")
			List<String> bizDsIds = (List<String>) this.mapDsTypeId.get(DsType.BIZ.value());
			if (bizDsIds == null) {
				bizDsIds = new ArrayList<>();
				this.mapDsTypeId.put(DsType.BIZ.value(), bizDsIds);
			}
			bizDsIds.add(dsId);
		}
	}

	/**
	 * 获取所有数据库
	 * 
	 * @author qiuxs
	 *
	 * @return
	 *
	 * 		创建时间：2018年7月27日 下午11:59:11
	 */
	public Map<Object, DataSource> getTargetDataSources() {
		return targetDataSources;
	}

	/**
	 * 获取入口库DsId
	 * 
	 * @author qiuxs
	 *
	 * @return
	 *
	 * 		创建时间：2018年7月27日 下午11:59:20
	 */
	public String getEntryDb() {
		return (String) this.mapDsTypeId.get(DsType.ENTRY.value());
	}

	/**
	 * 获取日志库DsId
	 * 
	 * @author qiuxs
	 *
	 * @return
	 *
	 * 		创建时间：2018年7月27日 下午11:59:29
	 */
	public String getLogDb() {
		return (String) this.mapDsTypeId.get(DsType.LOG.value());
	}

	/**
	 * 获取序列库DsId
	 * 没有指定序列库时定位到入口库
	 * @author qiuxs
	 *
	 * @return
	 *
	 * 		创建时间：2018年7月28日 上午12:00:10
	 */
	public String getSeqDb() {
		String seqDbId = (String) this.mapDsTypeId.get(DsType.SEQ.value());
		if (seqDbId == null) {
			seqDbId = (String) this.mapDsTypeId.get(DsType.ENTRY.value());
		}
		return seqDbId;
	}

	/**
	 * 根据数据库类型获取数据ID
	 * 
	 * @author qiuxs
	 *
	 * @param value
	 * @return
	 *
	 * 		创建时间：2018年7月27日 下午10:26:49
	 */
	public Object getDsId(String dsType) {
		return this.mapDsTypeId.get(dsType);
	}

}

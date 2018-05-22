package com.qiuxs.cuteframework.core.persistent.mybatis.dynamicsql;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.persistent.annotations.Column;
import com.qiuxs.cuteframework.core.persistent.annotations.Id;
import com.qiuxs.cuteframework.core.persistent.annotations.Table;
import com.qiuxs.cuteframework.core.persistent.annotations.Transient;

@Component
public class DynamicSqlProvider {

	public static final String DYNAMIC_SQL = "dynamicSQL";

	private static final String SELECT = "select";
	private static final String FROM = "from";

	public String dynamicSQL(Object record) {
		return "dynamicSQL";
	}

	public String deleteById(Class<?> pojoClass) {
		return null;
	}

	public String get(Class<?> pojoClass) {
		Table table = pojoClass.getAnnotation(Table.class);
		if (table == null) {
			throw new RuntimeException("pojoClass[" + pojoClass.getName() + "] has no Annotation @Table(\"your tableName\")");
		}
		List<FieldWrapper> fieldAnnotations = getFields(pojoClass);
		StringBuilder sql = new StringBuilder();
		sql.append(SELECT).append(" ");
		for (FieldWrapper fw : fieldAnnotations) {
			sql.append(fw.selectField());
		}
		sql.append(" ").append(FROM).append("").append(table.value());
		return "select * from user where id = #{id}";
	}

	public String getByIds(Class<?> pojoClass) {
		return null;
	}

	public String list(Class<?> pojoClass) {
		return null;
	}

	public String insert(Class<?> pojoClass) {
		return null;
	}

	public String update(Class<?> pojoClass) {
		return null;
	}

	/**
	 * 获取pojoClass的字段定义
	 * @param pojoClass
	 * @return
	 */
	private List<FieldWrapper> getFields(Class<?> pojoClass) {
		Field[] fields = pojoClass.getDeclaredFields();
		List<FieldWrapper> annotations = new ArrayList<>(fields.length);
		int length = fields.length;
		boolean hasPk = false;
		for (int i = 0; i < length; i++) {
			Field field = fields[i];
			String columnName = null;
			String fieldName = field.getName();
			// 非持久化字段
			Transient transAnno = field.getAnnotation(Transient.class);
			if (transAnno != null) {
				continue;
			}
			// 获取列注解
			Column cloumnAnno = field.getAnnotation(Column.class);
			// 获取主键注解
			Id idAnno = field.getAnnotation(Id.class);
			if (hasPk && idAnno != null) {
				throw new RuntimeException("pojoClass [" + pojoClass.getName() + "] repeat Annotation @Id ");
			}
			if (cloumnAnno != null) {
				columnName = cloumnAnno.value();
			} else if (idAnno != null) {
				// 主键注解
				columnName = idAnno.value();
			}
			if (StringUtils.isBlank(columnName)) {
				columnName = fieldName;
			}
			annotations.add(new FieldWrapper(columnName, fieldName, idAnno != null, i == length - 1));
		}
		return annotations;
	}
}

class FieldWrapper {
	String column;
	String fieldName;
	boolean lastFlag;
	boolean isPk;

	public FieldWrapper(String column, String fieldName, boolean isPk, boolean lastFlag) {
		super();
		this.column = column;
		this.fieldName = fieldName;
		this.isPk = isPk;
		this.lastFlag = lastFlag;
	}

	String selectField() {
		StringBuilder sb = new StringBuilder();
		sb.append(column);
		if (!column.equals(fieldName)) {
			sb.append(" as ").append(fieldName);
			if (!this.lastFlag) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

}
package com.qiuxs.cuteframework.view.pagemodel.utils;

import java.util.ArrayList;
import java.util.List;

import com.qiuxs.cuteframework.core.basic.Constants;
import com.qiuxs.cuteframework.core.basic.bean.Pair;
import com.qiuxs.cuteframework.core.basic.utils.ListUtils;
import com.qiuxs.cuteframework.view.pagemodel.Field;

/**
 * 页面模型工具
 * @author qiuxs
 *
 */
public class PageModelUtils {

	/**
	 * 对字段进行分组，隐藏字段独立出来
	 * @param fields
	 * @param rowSize
	 * @return
	 */
	public static Pair<List<Field>, List<List<Field>>> groupFields(List<Field> fields, int rowSize) {
		List<List<Field>> groupedSfs = new ArrayList<>();
		List<Field> hiddenFields = new ArrayList<>();

		if (ListUtils.isNotEmpty(fields)) {
			List<Field> sfOneRow = new ArrayList<>();
			for (Field sf : fields) {
				// 隐藏字段
				if (Field.TYPE_HIDDEN.equals(sf.getType())) {
					hiddenFields.add(sf);
					continue;
				}
				
				// 独占一行的字段
				if (sf.getSingleRow() == Constants.TRUE) {
					if (sfOneRow.size() > 0) {
    					groupedSfs.add(sfOneRow);
    					sfOneRow = new ArrayList<>();
					}
					sfOneRow.add(sf);
					groupedSfs.add(sfOneRow);
					sfOneRow = new ArrayList<>();
					continue;
				}
				
				// 正常字段进行分组
				sfOneRow.add(sf);
				if (sfOneRow.size() == rowSize) {
					groupedSfs.add(sfOneRow);
					sfOneRow = new ArrayList<>();
				}
			}
			if (sfOneRow.size() > 0) {
				groupedSfs.add(sfOneRow);
			}
		}
		return new Pair<>(hiddenFields, groupedSfs);
	}
	
}

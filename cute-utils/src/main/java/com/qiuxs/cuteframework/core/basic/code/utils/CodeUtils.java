package com.qiuxs.cuteframework.core.basic.code.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qiuxs.cuteframework.core.basic.bean.Pair;
import com.qiuxs.cuteframework.core.basic.code.DirectCodeCenter;
import com.qiuxs.cuteframework.core.basic.code.annotation.Code;
import com.qiuxs.cuteframework.core.basic.code.annotation.CodeDomain;
import com.qiuxs.cuteframework.core.basic.code.provider.DirectCodeHouse;

/**
 * 编码集工具类
 * @author qiuxs
 * 2019年3月27日 下午10:24:09
 */
public class CodeUtils {

	private static final Logger log = LogManager.getLogger(CodeUtils.class);

	/**
	 * 生成直接编码集
	 * 
	 * 2019年3月27日 下午10:25:04
	 * @auther qiuxs
	 * @param clz
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void genDirectCode(Class<?> clz) {
		Field[] fields = clz.getDeclaredFields();
		Map<String, DirectCodeHouse<?>> mapCodeHouse = new HashMap<>();

		List<Pair<Code, Field>> codeFields = new ArrayList<>();

		// 收集所有code，并创建codeHouse
		for (Field f : fields) {
			// 去除安全检查
			f.setAccessible(true);
			CodeDomain codeDomainAnno = f.getAnnotation(CodeDomain.class);
			if (codeDomainAnno != null) {
				try {
					Object oCodeDomain = f.get(clz);
					if (oCodeDomain == null) {
						throw new RuntimeException("Null CodeDomain with Field [" + clz.getName() + "#" + f.getName() + "]");
					}
					String sCodeDomain = String.valueOf(oCodeDomain);
					mapCodeHouse.put(sCodeDomain, DirectCodeCenter.getDirectCodeHouse(sCodeDomain, true));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					log.error("ext = " + e.getLocalizedMessage(), e);
				}
			} else {
				Code codeAnno = f.getAnnotation(Code.class);
				if (codeAnno != null) {
					codeFields.add(new Pair<>(codeAnno, f));
				}
			}
		}

		// 装在codeHouse
		for (Iterator<Pair<Code, Field>> iterator = codeFields.iterator(); iterator.hasNext();) {
			Pair<Code, Field> item = iterator.next();
			Code codeAnno = item.getV1();
			Field f = item.getV2();
			String domain = codeAnno.domain();
			DirectCodeHouse directCodeHouse = mapCodeHouse.get(domain);
			if (directCodeHouse != null) {
				try {
					directCodeHouse.addCode(f.get(clz), codeAnno.caption());
				} catch (IllegalArgumentException | IllegalAccessException e) {
					log.error("ext = " + e.getLocalizedMessage(), e);
				}
			}
		}

	}

}

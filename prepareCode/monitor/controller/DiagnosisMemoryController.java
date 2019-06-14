package com.hzecool.frm.monitor.controller;

import java.lang.reflect.Field;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hzecool.core.context.ApplicationContextHolder;
import com.hzecool.fdn.exception.utils.ExceptionUtil;
import com.hzecool.fdn.utils.converter.JsonUtils;
import com.hzecool.fdn.utils.reflect.Reflections;

/**
 * 内存诊断工具，用具检查正在运行的程序中的缓存数据
 * @author qiuxs
 *
 */
@RestController
@RequestMapping(value = "/devops/memory", produces = "application/json; charset=utf-8")
public class DiagnosisMemoryController {

	/**
	 * 显示静态字段
	 * ex:
	 * 	/diagnosis/static/field?fieldName=com.hzecool.slh.slh1Adapter.product.ProductConfigContext$INTERFACE_DESC
	 * @param fieldName
	 * @return
	 */
	@GetMapping(value = "/staticField")
	public String showStaticFieldValue(@RequestParam("fieldName") String fieldName) {
		String[] c_f = fieldName.split("\\$");
		try {
			Class<?> clz = Class.forName(c_f[0]);
			Field field = clz.getDeclaredField(c_f[1]);
			field.setAccessible(true);
			Object object = field.get(clz);
			return JsonUtils.toJSONString(object);
		} catch (ReflectiveOperationException e) {
			return JsonUtils.genJson("error", ExceptionUtil.getStackTraceStirng(e)).toJSONString();
		}
	}

	/**
	 * 显示服务类的成员变量
	 * @param fileName
	 * @return
	 */
	@GetMapping("/serviceField")
	public String showFiledValue(@RequestParam("fieldName") String fileName) {
		String[] split = fileName.split("\\.");
		try {
			Object bean = ApplicationContextHolder.getBean(split[0]);
			if (bean != null) {
				Object fieldValue = Reflections.getFieldValue(bean, split[1]);
				return JsonUtils.toJSONString(fieldValue);
			} else {
				return "{}";
			}
		} catch (Exception e) {
			return JsonUtils.genJson("error", ExceptionUtil.getStackTraceStirng(e)).toJSONString();
		}
	}

}

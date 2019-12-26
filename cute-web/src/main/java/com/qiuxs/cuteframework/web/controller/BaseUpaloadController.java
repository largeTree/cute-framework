package com.qiuxs.cuteframework.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.qiuxs.cuteframework.web.utils.RequestUtils;

/**
 * 基础文件上传类
 * @author qiuxs
 *
 */
public abstract class BaseUpaloadController extends BaseController {

	@PostMapping("/upload")
	public String upload(HttpServletRequest request) {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getServletContext());
		// 文件列表
		List<MultipartFile> files = new ArrayList<>();
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			Map<String, MultipartFile> fileMap = multiRequest.getFileMap();
			fileMap.forEach((fileName, file) -> {
				files.add(file);
			});
		}
		Map<String, String> params = RequestUtils.getRequestParams(request);
		return this.upload(files, params);
	}

	/**
	 * 子类实现
	 * 完成对上传的文件的操作
	 * @param files 
	 * 		上传的文件列表，如没有上传文件则为空列表
	 * @param params
	 * 		额外参数
	 * @return
	 */
	public abstract String upload(List<MultipartFile> files, Map<String, String> params);

}

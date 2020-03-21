package com.qiuxs.upyun;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.qiuxs.cuteframework.core.basic.config.IConfiguration;
import com.qiuxs.cuteframework.core.basic.config.UConfigUtils;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.basic.utils.converter.Base64Utils;
import com.qiuxs.cuteframework.core.utils.FunctionUtils;
import com.qiuxs.upyun.dto.FileInfo;
import com.upyun.RestManager;
import com.upyun.UpException;

import okhttp3.Response;

/**
 * 又拍云sdk包装
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年3月21日 下午3:43:40 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class RestManagerWrapper {

	private static final String CONFIG_DOMAIN = "upyun";

	private static final int DEFAULT_TIMEOUT = 60;

	private final String bucketDomain;
	private final RestManager manager;

	private static Map<String, RestManagerWrapper> managerMap = new ConcurrentHashMap<String, RestManagerWrapper>();

	/**
	 * 私有化构造函数
	 * Creates a new instance of RestManagerWrapper.  
	 *  
	 * @param bucketName
	 * @param oper
	 * @param opPassword
	 * @param domain
	 * @param timeout
	 */
	private RestManagerWrapper(String bucketDomain, String bucketName, String oper, String opPassword, String domain, int timeout) {
		this.bucketDomain = bucketDomain;
		manager = new RestManager(bucketName, oper, opPassword);
		if (StringUtils.isNotBlank(domain)) {
			manager.setApiDomain(domain);
		}
		if (timeout > 0) {
			manager.setTimeout(timeout);
		}
	}

	/**
	 * 获取一个包装
	 *  
	 * @author qiuxs  
	 * @param bucketName
	 * @return
	 */
	public static RestManagerWrapper getWrapper(final String domain, final String bucketName) {
		return FunctionUtils.doubleLockCheckGet("upyun_rest_manager_lock", v -> {
			return v != null;
		}, () -> {
			return managerMap.get(bucketName);
		}, () -> {
			IConfiguration configDomain = UConfigUtils.getDomain(CONFIG_DOMAIN);
			RestManagerWrapper wrapper = new RestManagerWrapper(domain, bucketName, configDomain.getString("store.oper"), configDomain.getString("store.password"), configDomain.getString("store.domain"), configDomain.getInt("store.timeout", DEFAULT_TIMEOUT));
			managerMap.put(bucketName, wrapper);
			return wrapper;
		});
	}

	/**
	 * 创建目录
	 *  
	 * @author qiuxs  
	 * @param dir
	 */
	public void mkdir(String dir) {
		if (StringUtils.isBlank(dir)) {
			ExceptionUtils.throwLogicalException("dir is null ot empty");
		}
		try {
			Response resp = this.manager.mkDir(dir);
			if (!resp.isSuccessful()) {
				ExceptionUtils.throwLogicalException(resp.message());
			}
		} catch (IOException | UpException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * 获取文件信息
	 *  
	 * @author qiuxs  
	 * @param filePath
	 */
	public FileInfo getFileInfo(String filePath) {
		return FunctionUtils.catchThrows(() -> {
			Response resp = this.manager.getFileInfo(filePath);
			int code = resp.code();
			FileInfo fileInfo = new FileInfo();
			if (code == 404) {
				fileInfo.setExists(false);
			} else {
				fileInfo.setExists(true);
				fileInfo.setType(resp.header("x-upyun-file-type"));
				fileInfo.setSize(Long.parseLong(resp.header("x-upyun-file-size")));
				fileInfo.setDate(resp.header("x-upyun-file-date"));
				fileInfo.setMd5(resp.header("Content-Md5"));
			}
			return fileInfo;
		});
	}
	
	/**
	 * 上传文件，base64方式
	 *  
	 * @author qiuxs  
	 * @param filePath
	 * @param base64
	 * @param params
	 * @return
	 */
	public String upload(String filePath, String base64, Map<String, String> params, boolean coverFlag) {
		byte[] data = Base64Utils.decodeFromStringWithPrefix(base64);
		return upload(filePath, data, params, coverFlag);
	}

	/**
	 * 上传文件
	 *  
	 * @author qiuxs  
	 * @param filePath
	 * @param data
	 * @param params
	 */
	public String upload(String filePath, byte[] data, Map<String, String> params) {
		return upload(filePath, data, params, false);
	}
	
	/**
	 * 上传文件
	 *  
	 * @author qiuxs  
	 * @param filePath
	 * 		目标路径
	 * @param data
	 * 		数据字节数组
	 * @param params
	 * 		宽展参数
	 * @param coverFlag
	 * 		是否覆盖
	 * @return
	 */
	public String upload(String filePath, byte[] data, Map<String, String> params, boolean coverFlag) {
		return FunctionUtils.catchThrows(() -> {
			// 非覆盖的情况下，校验一下文件是否存在
			if (!coverFlag && this.getFileInfo(filePath).isExists()) {
				throw ExceptionUtils.newLogicException(filePath + " alread exists");
			} else {
				Response response = this.manager.writeFile(filePath, data, params);
				if (response.isSuccessful()) {
					return this.toUrl(filePath);
				} else {
					throw ExceptionUtils.newLoginException(response.message());
				}
			}
		});
	}

	/***
	 * 上传文件
	 *  
	 * @author qiuxs  
	 * @param filePath
	 * 		目标路径
	 * @param file
	 * 		文件对象
	 * @param params
	 * 		扩展参数
	 */
	public String upload(String filePath, File file, Map<String, String> params) {
		return upload(filePath, file, params, false);
	}
	
	/**
	 * 上传一个文件
	 *  
	 * @author qiuxs  
	 * @param filePath
	 * 		目标路径
	 * @param file
	 * 		文件对象
	 * @param params
	 * 		扩展参数
	 * @param coverFlag
	 * 		是否覆盖
	 * @return
	 */
	public String upload(String filePath, File file, Map<String, String> params, boolean coverFlag) {
		return FunctionUtils.catchThrows(() -> {
			// 非覆盖的情况下，校验一下文件是否存在
			if (!coverFlag && this.getFileInfo(filePath).isExists()) {
				throw ExceptionUtils.newLogicException(filePath + " alread exists");
			} else {
				Response response = this.manager.writeFile(filePath, file, params);
				if (response.isSuccessful()) {
					return this.toUrl(filePath);
				} else {
					throw ExceptionUtils.newLoginException(response.message());
				}
			}
		});
	}

	/***
	 * 上传文件
	 *  
	 * @author qiuxs  
	 * @param filePath
	 * 		目标路径
	 * @param in
	 * 		输入流
	 * @param params
	 * 		扩展参数
	 */
	public String upload(String filePath, InputStream in, Map<String, String> params) {
		return upload(filePath, in, params, false);
	}
	
	/**
	 * 上传文件
	 *  
	 * @author qiuxs  
	 * @param filePath
	 * 		目标路径
	 * @param in
	 * 		输入流
	 * @param params
	 * 		扩展参数
	 * @param coverFlag
	 * 		是否覆盖
	 * @return
	 */
	public String upload(String filePath, InputStream in, Map<String, String> params, boolean coverFlag) {
		return FunctionUtils.catchThrows(() -> {
			// 非覆盖的情况下，校验一下文件是否存在
			if (!coverFlag && this.getFileInfo(filePath).isExists()) {
				throw ExceptionUtils.newLogicException(filePath + " alread exists");
			} else {
				Response response = this.manager.writeFile(filePath, in, params);
				if (response.isSuccessful()) {
					return this.toUrl(filePath);
				} else {
					throw ExceptionUtils.newLoginException(response.message());
				}
			}
		});
	}

	/**
	 * 拼接文件地址，前方不带前方不带http:或https:
	 *  
	 * @author qiuxs  
	 * @param path
	 * @return
	 */
	public String toUrl(String path) {
		return StringUtils.append("//" + this.bucketDomain + path);
	}
}

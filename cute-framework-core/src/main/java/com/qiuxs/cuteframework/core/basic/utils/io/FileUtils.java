package com.qiuxs.cuteframework.core.basic.utils.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.qiuxs.cuteframework.core.basic.utils.StringUtils;

public class FileUtils {

	public static InputStream readAsInputStream(String location) throws FileNotFoundException {
		if (StringUtils.isBlank(location)) {
			throw new NullPointerException("location is empty or Null!");
		}
		return new FileInputStream(location);
	}

}

package com.qiuxs.cuteframework.tech.mybatis.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;

import com.qiuxs.cuteframework.core.basic.utils.ClassPathResourceUtil;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;

/**
 * 刷新MyBatis Mapper XML 线程
 */
public class MybatisMapperRefresher implements java.lang.Runnable {

	public static Logger log = LogManager.getLogger(MybatisMapperRefresher.class);

	private static boolean refresh; // 刷新启用后，是否启动了刷新线程

	private Set<String> location; // Mapper实际资源路径

	private List<Resource> mapperLocations; // Mapper资源路径
	private Configuration configuration; // MyBatis配置对象

	private Long beforeTime = 0L; // 上一次刷新时间
	private static int delaySeconds = 10; // 延迟刷新秒数
	private static int sleepSeconds = 3; // 休眠时间

	private static String[] DEFAULT_MAPPER_PATHS = { "classpath*:/com/qiuxs/**/mapper/",
			"classpath*:/mybatis/**/*.xml" };

	public static void startRefresher(Collection<String> paths) {
		SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) ApplicationContextHolder.getBean("sqlSessionFactory");
		Configuration configuration = sqlSessionFactory.getConfiguration();
		List<Resource> mapperLocations = new ArrayList<Resource>();
		for (String path : paths) {
			mapperLocations.add(ClassPathResourceUtil.getSingleResource(path));
		}
		// 额外加上默认的
		mapperLocations.addAll(ClassPathResourceUtil.getResourceList(DEFAULT_MAPPER_PATHS));
		new MybatisMapperRefresher(mapperLocations, configuration).run();
	}

	public static boolean isRefresh() {
		return refresh;
	}

	public MybatisMapperRefresher(List<Resource> mapperLocations, Configuration configuration) {
		this.mapperLocations = mapperLocations;
		this.configuration = configuration;
	}

	@Override
	public void run() {

		beforeTime = System.currentTimeMillis();

		boolean debugEnabled = log.isDebugEnabled();
		if (debugEnabled) {
			log.debug("[location] " + location);
			log.debug("[configuration] " + configuration);
		}

		// 启动刷新线程
		final MybatisMapperRefresher runnable = this;
		new Thread(new java.lang.Runnable() {
			@Override
			public void run() {

				if (location == null) {
					location = new HashSet<>();
					if (debugEnabled) {
						log.debug("MapperLocation's length:" + mapperLocations.size());
					}
					for (Resource mapperLocation : mapperLocations) {
						String s = mapperLocation.toString().replaceAll("\\\\", "/");
						s = s.substring("file [".length(), s.length() - 1);
						if (!location.contains(s)) {
							location.add(s);
							if (debugEnabled) {
								log.debug("Location:" + s);
							}
						}
					}
					if (debugEnabled) {
						log.debug("Locarion's size:" + location.size());
					}
				}

				try {
					Thread.sleep(delaySeconds * 1000);
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
				refresh = true;

				log.info("========= Enabled refresh mybatis mapper =========");

				while (true) {
					try {
						for (String s : location) {
							runnable.refresh(s, beforeTime);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					try {
						Thread.sleep(sleepSeconds * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
		}, "MyBatis-Mapper-Refresh").start();
	}

	/**
	 * 执行刷新
	 * 
	 * @param filePath
	 *            刷新目录
	 * @param beforeTime
	 *            上次刷新时间
	 * @throws NestedIOException
	 *             解析异常
	 * @throws FileNotFoundException
	 *             文件未找到
	 * @author ThinkGem
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void refresh(String filePath, Long beforeTime) throws Exception {

		// 本次刷新时间
		Long refrehTime = System.currentTimeMillis();

		// 获取需要刷新的Mapper文件列表
		List<File> fileList = this.getRefreshFile(new File(filePath), beforeTime);
		if (fileList.size() > 0) {
			log.debug("Refresh file: " + fileList.size());
		}
		for (int i = 0; i < fileList.size(); i++) {
			InputStream inputStream = new FileInputStream(fileList.get(i));
			String resource = fileList.get(i).getAbsolutePath();
			try {

				// 清理原有资源，更新为自己的StrictMap方便，增量重新加载
				String[] mapFieldNames = new String[] { "mappedStatements", "caches", "resultMaps", "parameterMaps",
						"keyGenerators", "sqlFragments" };
				for (String fieldName : mapFieldNames) {
					Field field = configuration.getClass().getDeclaredField(fieldName);
					field.setAccessible(true);
					Map map = ((Map) field.get(configuration));
					if (!(map instanceof StrictMap)) {
						Map newMap = new StrictMap(StringUtils.capitalize(fieldName) + "collection");
						for (Object key : map.keySet()) {
							try {
								newMap.put(key, map.get(key));
							} catch (IllegalArgumentException ex) {
								newMap.put(key, ex.getMessage());
							}
						}
						field.set(configuration, newMap);
					}
				}

				// 清理已加载的资源标识，方便让它重新加载。
				Field loadedResourcesField = configuration.getClass().getDeclaredField("loadedResources");
				loadedResourcesField.setAccessible(true);
				Set loadedResourcesSet = ((Set) loadedResourcesField.get(configuration));
				loadedResourcesSet.remove(resource);

				// 重新编译加载资源文件。
				XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(inputStream, configuration, resource,
						configuration.getSqlFragments());
				xmlMapperBuilder.parse();
				log.info("Refershed MyBatis Mapper [" + resource + "]");
			} catch (Exception e) {
				throw new NestedIOException("Failed to parse mapping resource: '" + resource + "'", e);
			} finally {
				ErrorContext.instance().reset();
			}
			// System.out.println("Refresh file: " + mappingPath +
			// StringUtils.substringAfterLast(fileList.get(i).getAbsolutePath(),
			// mappingPath));
			if (log.isDebugEnabled()) {
				log.debug("Refresh file: " + fileList.get(i).getAbsolutePath());
				log.debug("Refresh filename: " + fileList.get(i).getName());
			}
		}
		// 如果刷新了文件，则修改刷新时间，否则不修改
		if (fileList.size() > 0) {
			this.beforeTime = refrehTime;
		}
	}

	/**
	 * 获取需要刷新的文件列表
	 * 
	 * @param dir
	 *            目录
	 * @param beforeTime
	 *            上次刷新时间
	 * @return 刷新文件列表
	 */
	private List<File> getRefreshFile(File dir, Long beforeTime) {
		List<File> fileList = new ArrayList<File>();

		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (!file.getName().endsWith(".xml")) {
					continue;
				}
				if (file.isDirectory()) {
					fileList.addAll(this.getRefreshFile(file, beforeTime));
				} else if (file.isFile()) {
					if (this.checkFile(file, beforeTime)) {
						fileList.add(file);
					}
				} else {
					log.error("Error file." + file.getName());
				}
			}
		}
		return fileList;
	}

	/**
	 * 判断文件是否需要刷新
	 * 
	 * @param file
	 *            文件
	 * @param beforeTime
	 *            上次刷新时间
	 * @return 需要刷新返回true，否则返回false
	 */
	private boolean checkFile(File file, Long beforeTime) {
		if (file.lastModified() > beforeTime) {
			return true;
		}
		return false;
	}

	/**
	 * 重写 org.apache.ibatis.session.Configuration.StrictMap 类 来自 MyBatis3.4.0版本，修改
	 * put 方法，允许反复 put更新。
	 */
	public static class StrictMap<V> extends HashMap<String, V> {

		private static final long serialVersionUID = -4950446264854982944L;
		private String name;

		public StrictMap(String name, int initialCapacity, float loadFactor) {
			super(initialCapacity, loadFactor);
			this.name = name;
		}

		public StrictMap(String name, int initialCapacity) {
			super(initialCapacity);
			this.name = name;
		}

		public StrictMap(String name) {
			super();
			this.name = name;
		}

		public StrictMap(String name, Map<String, ? extends V> m) {
			super(m);
			this.name = name;
		}

		@SuppressWarnings("unchecked")
		public V put(String key, V value) {
			// ThinkGem 如果现在状态为刷新，则刷新(先删除后添加)
			if (MybatisMapperRefresher.isRefresh()) {
				remove(key);
				// MapperRefresh.log.debug("refresh key:" + key.substring(key.lastIndexOf(".") +
				// 1));
			}
			// ThinkGem end
			if (containsKey(key)) {
				throw new IllegalArgumentException(name + " already contains value for " + key);
			}
			if (key.contains(".")) {
				final String shortKey = getShortName(key);
				if (super.get(shortKey) == null) {
					super.put(shortKey, value);
				} else {
					super.put(shortKey, (V) new Ambiguity(shortKey));
				}
			}
			return super.put(key, value);
		}

		public V get(Object key) {
			V value = super.get(key);
			if (value == null) {
				throw new IllegalArgumentException(name + " does not contain value for " + key);
			}
			if (value instanceof Ambiguity) {
				throw new IllegalArgumentException(((Ambiguity) value).getSubject() + " is ambiguous in " + name
						+ " (try using the full name including the namespace, or rename one of the entries)");
			}
			return value;
		}

		private String getShortName(String key) {
			final String[] keyparts = key.split("\\.");
			return keyparts[keyparts.length - 1];
		}

		protected static class Ambiguity {
			private String subject;

			public Ambiguity(String subject) {
				this.subject = subject;
			}

			public String getSubject() {
				return subject;
			}
		}
	}
}
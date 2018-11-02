/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.common;

import com.huayin.common.configuration.ConfigProvider;
import com.huayin.common.configuration.ConfigProviderFactory;

/**
 * <pre>
 * 公共 - 全局配置类
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
public class PropertiesConfig {

	/**
	 * 当前对象实例
	 */
	private static PropertiesConfig instance = new PropertiesConfig();
	/**
	 * 属性文件加载对象
	 */
	private ConfigProvider<String> cp = ConfigProviderFactory.getPropertiesInstance("printmanager.properties","utf-8");

	/**
	 * 获取当前对象实例
	 */
	public static PropertiesConfig getInstance() {
		return instance;
	}
	
	/**
	 * 获取配置
	 */
	public static String getConfig(String key) {
		return getInstance().getCp().getConfigByPrimaryKey(key);
	}
	
	
	public ConfigProvider<String> getCp()
	{
		return cp;
	}
}

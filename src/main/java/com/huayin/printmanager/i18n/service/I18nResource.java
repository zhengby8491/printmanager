/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年12月22日 下午5:02:41
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.i18n.service;

import com.huayin.printmanager.i18n.ResourceBundleMessageSource;

/**
 * <pre>
 * 国际化框架 - 资源数据
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月22日
 */
public class I18nResource
{
	public static String VALIDATE_FAIL = "数据没有通过验证，请刷新页面后重试";

	public static String VALIDATE_NAME_EXIST = "名称已经存在";
	
	public static String VALIDATE_PIC_NOT_EXIST = "图片不存在";
	
	public static String SUCCESS = "成功";
	
	public static String FAIL = "失败";
	
	public static String UPLOAD_SUCCESS = "上传成功";
	
	public static String UPLOAD_FAIL = "上传失败";
	
	public static String DELETE_SUCCESS = "删除成功";
	
	public static String DELETE_FAIL = "删除失败";
	
	public static String UPDATE_SUCCESS = "修改成功";
	
	public static String UPDATE_FAIL = "修改失败";

	/**
	 * <pre>
	 * 重新加载
	 * </pre>
	 * @since 1.0, 2017年12月26日 上午10:40:02, think
	 */
	public static void load()
	{
		try
		{
			VALIDATE_FAIL = ResourceBundleMessageSource.getMessageExist("i18n.common.validate.fail");
			VALIDATE_NAME_EXIST = ResourceBundleMessageSource.getMessageExist("i18n.common.validate.name.exist");
			VALIDATE_PIC_NOT_EXIST = ResourceBundleMessageSource.getMessageExist("i18n.common.validate.pic.not.exist");
			SUCCESS = ResourceBundleMessageSource.getMessageExist("i18n.common.success");
			FAIL = ResourceBundleMessageSource.getMessageExist("i18n.common.fail");
			UPLOAD_SUCCESS = ResourceBundleMessageSource.getMessageExist("i18n.common.upload.success");
			UPLOAD_FAIL = ResourceBundleMessageSource.getMessageExist("i18n.common.upload.fail");
			DELETE_SUCCESS = ResourceBundleMessageSource.getMessageExist("i18n.common.delete.success");
			DELETE_FAIL = ResourceBundleMessageSource.getMessageExist("i18n.common.delete.fail");
			UPDATE_SUCCESS = ResourceBundleMessageSource.getMessageExist("i18n.common.update.success");
			UPDATE_FAIL = ResourceBundleMessageSource.getMessageExist("i18n.common.update.fail");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

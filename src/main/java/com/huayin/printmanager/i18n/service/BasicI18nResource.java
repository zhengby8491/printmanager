/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年12月26日 上午10:33:52
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.i18n.service;

import com.huayin.printmanager.i18n.ResourceBundleMessageSource;

/**
 * <pre>
 * 国际化框架 - 基础设置资源数据
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月26日
 */
public class BasicI18nResource
{
	// ============ 税率信息 ============

	public static String TAXRATE_VALIDATE_NAME_EXIST = "税率值已经存在";

	// ============ 供应商 ============

	public static String SUPPLIER_VALIDATE_MSG1 = "供应商[{0}]已被引用";

	public static String SUPPLIER_VALIDATE_MSG2 = "供应商[{0}]的销售员{1}不存在";

	// ============ 客户 ============

	public static String CUSTOMER_VALIDATE_MSG1 = "客户[{0}]已被引用";

	public static String CUSTOMER_VALIDATE_MSG2 = "客户[{0}]的是否有效值异常";

	public static String CUSTOMER_VALIDATE_MSG3 = "客户[{0}]的销售员{1}不存在";

	// ============ 材料 ============

	public static String MATERIAL_VALIDATE_MSG1 = "材料[{0}]已被引用";

	// ============ 产品 ============

	public static String PRODUCT_VALIDATE_CUSTOMER_CODE_EXIST = "客户料号已经存在";

	public static String PRODUCT_VALIDATE_MSG1 = "产品[{0}]已被引用";

	// ============ 单位换算 ============

	public static String UNITCONVERT_VALIDATE_NAME_EXIST = "此单位换算公式已存在";

	// ============ 工序信息 ============

	public static String PROCEDURE_VALIDATE_MSG1 = "工序[{0}]已被引用";
	
	// ============ 机台信息 ============
	
	public static String MACHINE_VALIDATE_MSG1 = "机台[{0}]已被引用";

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
			// ============ 税率信息 ============
			TAXRATE_VALIDATE_NAME_EXIST = ResourceBundleMessageSource.getMessageExist("i18n.basic.taxrate.validate.name.exist");
			// ============ 供应商 ============
			SUPPLIER_VALIDATE_MSG1 = ResourceBundleMessageSource.getMessageExist("i18n.basic.supplier.validate.msg1");
			SUPPLIER_VALIDATE_MSG2 = ResourceBundleMessageSource.getMessageExist("i18n.basic.supplier.validate.msg2");
			// ============ 客户 ============
			CUSTOMER_VALIDATE_MSG1 = ResourceBundleMessageSource.getMessageExist("i18n.basic.customer.validate.msg1");
			CUSTOMER_VALIDATE_MSG2 = ResourceBundleMessageSource.getMessageExist("i18n.basic.customer.validate.msg2");
			CUSTOMER_VALIDATE_MSG3 = ResourceBundleMessageSource.getMessageExist("i18n.basic.customer.validate.msg3");
			// ============ 材料 ============
			MATERIAL_VALIDATE_MSG1 = ResourceBundleMessageSource.getMessageExist("i18n.basic.material.validate.msg1");
			// ============ 产品 ============
			PRODUCT_VALIDATE_CUSTOMER_CODE_EXIST = ResourceBundleMessageSource.getMessageExist("i18n.basic.product.validate.customer.code.exist");
			PRODUCT_VALIDATE_MSG1 = ResourceBundleMessageSource.getMessageExist("i18n.basic.product.validate.msg1");
			// ============ 单位换算 ============
			UNITCONVERT_VALIDATE_NAME_EXIST = ResourceBundleMessageSource.getMessageExist("i18n.basic.unitconvert.validate.name.exist");
			// ============ 工序信息 ============
			PROCEDURE_VALIDATE_MSG1 = ResourceBundleMessageSource.getMessageExist("i18n.basic.procedure.validate.msg1");
		  // ============ 机台信息 ============
			MACHINE_VALIDATE_MSG1 = ResourceBundleMessageSource.getMessageExist("i18n.basic.machine.validate.msg1");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

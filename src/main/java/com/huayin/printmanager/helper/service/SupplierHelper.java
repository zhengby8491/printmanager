/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月24日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.helper.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.huayin.printmanager.helper.AbsHelper;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;

/**
 * <pre>
 * 基础设置 - 供应商常用功能
 * </pre>
 * @author zhengby
 * @version 1.0, 2017年12月26日 下午6:43:55
 */
public class SupplierHelper extends AbsHelper
{
	/**
	 * <pre>
	 * 根据供应商/客户 名称 换Id
	 * </pre>
	 * @param queryParam
	 * @since 1.0, 2017年12月26日 下午6:50:00, zhengby
	 */
	public static Long transferToId(String supplierName)
	{
		if (StringUtils.isNotBlank(supplierName))
		{
			Long supplierId = serviceFactory.getSupplierService().findIdByName(supplierName);
			return supplierId;
		} 
		else
		{
			return null;
		}
	}

	/**
	 * <pre>
	 * 根据供应商/客户 名称 换Ids(支持模糊查询)
	 * </pre>
	 * @param queryParam
	 * @since 1.0, 2017年12月28日 上午10:15:39, zhengby
	 */
	public static void transferToIds(QueryParam queryParam)
	{
		if (StringUtils.isNotBlank(queryParam.getSupplierName()))
		{
			List<Long> idList = serviceFactory.getSupplierService().findIdsByName(queryParam.getSupplierName());
			// 用ID替换字符串查询
			if(CollectionUtils.isNotEmpty(idList))
			{
				queryParam.setSupplierIdList(idList);
				queryParam.setSupplierName(null);
			}
		}
	}
}

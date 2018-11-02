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

import com.google.common.collect.Lists;
import com.huayin.printmanager.helper.AbsHelper;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;

/**
 * <pre>
 * 基础设置 - 客户功能
 * </pre>
 * @author zhengby
 * @version 1.0, 2017年12月26日 下午6:43:55
 */
public class CustomerHelper extends AbsHelper
{
	/**
	 * <pre>
	 * 把客户名称 转换成 客户ID
	 * </pre>
	 * @param queryParam
	 * @since 1.0, 2017年12月26日 上午10:08:38, zhengby
	 */
	public static Long transferToId(String customerName)
	{
		if (StringUtils.isNotBlank(customerName))
		{
			Long customerId = serviceFactory.getCustomerService().findIdByName(customerName);
			return customerId;
		}
		else
		{
			return null;
		}
	}

	/**
	 * <pre>
	 * 把客户名称 转换成 客户IDs(支持模糊查询)
	 * </pre>
	 * @param queryParam
	 * @since 1.0, 2017年12月28日 上午10:27:15, zhengby
	 */
	public static void transferToIds(QueryParam queryParam)
	{
		List<Long> idList = Lists.newArrayList();
		if (StringUtils.isNotBlank(queryParam.getCustomerName()))
		{
			if (BoolValue.YES.equals(queryParam.getIsOem()))
			{
				idList = serviceFactory.getCustomerService().findIdsByName(queryParam.getCustomerName(), queryParam.getIsOem());
			}
			else
			{
				idList = serviceFactory.getCustomerService().findIdsByName(queryParam.getCustomerName());
			}
		}

		// 用ID替换字符串查询
		if (CollectionUtils.isNotEmpty(idList))
		{
			queryParam.setCustomerIdList(idList);
			queryParam.setCustomerName(null);
		}
	}
}

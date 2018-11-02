/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年7月18日 上午9:49:48
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.job.monitor;

import com.huayin.printmanager.job.BaseJob;

/**
 * <pre>
 * 外部通知  - 通知采购订单的状态
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年7月18日上午9:49:48, zhengby
 */
public class NoticePOStatusJob extends BaseJob
{

	@Override
	public void exec(boolean isTimeout)
	{
		serviceFactory.getExteriorPurchService().noticePOStatus();
	}

}

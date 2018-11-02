/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年7月12日 上午11:51:52
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.service;

import com.huayin.printmanager.exterior.dto.ResponseDto;

/**
 * <pre>
 * 外部接口  - 注册功能
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年7月12日上午11:51:52, zhengby
 */
public interface ExteriorRegisterService
{

	/**
	 * <pre>
	 * 注册印刷家账户
	 * </pre>
	 * @param responseDto
	 * @return
	 * @since 1.0, 2018年7月20日 上午11:32:16, zhengby
	 */
	public boolean register(ResponseDto responseDto);

}

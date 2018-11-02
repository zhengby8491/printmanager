/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年7月2日 上午9:24:32
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.service;

import com.huayin.printmanager.exterior.dto.ReqBody;
import com.huayin.printmanager.exterior.dto.RequestDto;
import com.huayin.printmanager.exterior.dto.ResponseDto;
import com.huayin.printmanager.persist.entity.sys.User;

/**
 * <pre>
 * 外部接口  - 总控制接口
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年7月2日上午9:24:32, zhengby
 */
public interface ExteriorService
{
	/**
	 * <pre>
	 * 功能  - 3.1	商户平台向印刷家平台上传用户信息接口
	 * </pre>
	 * @param requestDto
	 * @return
	 * @since 1.0, 2018年7月5日 下午2:01:50, zhengby
	 */
	public void uploadUser(User user);

	/**
	 * <pre>
	 * 功能  - 3.2	商户平台获取印刷家平台用户信息接口
	 * </pre>
	 * @param requestDto
	 * @return
	 * @since 1.0, 2018年7月5日 下午2:09:30, zhengby
	 */
	public ResponseDto userInfo(String dataId);

	/**
	 * <pre>
	 * 功能  - 3.3	授权用户登录印刷家平台接口
	 * </pre>
	 * @param requestDto
	 * @return
	 * @since 1.0, 2018年7月5日 下午2:11:14, zhengby
	 */
	public ResponseDto checkUser(User user);

	/**
	 * <pre>
	 * 功能  - 3.4	授权用户登录商户平台接口
	 * </pre>
	 * @param requestDto
	 * @return
	 * @since 1.0, 2018年7月5日 下午2:11:49, zhengby
	 */
	public ResponseDto thirdCheckUser(RequestDto requestDto);
	
	/**
	 * <pre>
	 * 功能  - 3.5	印刷家平台通知商户平台同步用户信息接口
	 * </pre>
	 * @param requestDto
	 * @return
	 * @since 1.0, 2018年7月5日 下午2:09:53, zhengby
	 */
	public ResponseDto acceptUserNotice(RequestDto requestDto);

	/**
	 * <pre>
	 * 功能  - 账户签订跳转印刷家的协议
	 * </pre>
	 * @return
	 * @since 1.0, 2018年7月10日 下午1:51:23, zhengby
	 */
	public User agreement();

	/**
	 * <pre>
	 * 查找账户是否存在
	 * </pre>
	 * @param uid
	 * @param token
	 * @param isPHUser
	 * @return
	 * @since 1.0, 2018年7月11日 下午7:10:46, zhengby
	 */
	public User findUser(String uid, String token, String isPHUser);

	/**
	 * <pre>
	 * TODO
	 * </pre>
	 * @param reqBody
	 * @return
	 * @since 1.0, 2018年7月16日 上午10:41:14, zhengby
	 */
	public User getUserByCondition(ReqBody reqBody);
	
}

/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年7月2日 上午9:25:04
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.service.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.exterior.dto.ReqBody;
import com.huayin.printmanager.exterior.dto.RequestDto;
import com.huayin.printmanager.exterior.dto.ResponseDto;
import com.huayin.printmanager.exterior.dto.RspBody;
import com.huayin.printmanager.exterior.dto.RspHeader;
import com.huayin.printmanager.exterior.enums.ActionType;
import com.huayin.printmanager.exterior.service.ExteriorService;
import com.huayin.printmanager.exterior.utils.ExteriorHelper;
import com.huayin.printmanager.persist.entity.sys.User;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 外部接口
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年7月2日上午9:25:04, zhengby
 */
@Service
public class ExteriorServiceImpl extends BaseServiceImpl implements ExteriorService
{
	protected static final ExecutorService threadPool = Executors.newCachedThreadPool();
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void uploadUser(User user)
	{
		/**
		 * 向印刷家上传用户账户信息
		 */
		// 禁止上传超级管理账号信息
		if (user.getCompanyId().equals(UserUtils.getSystemCompanyId()))
		{
			return;
		}
		// 请求体ReqBody
		ReqBody reqBody = new ReqBody();
		reqBody.setThirdPlatformUserId(user.getId().toString());
		reqBody.setMobile(user.getMobile());
		reqBody.setToken(user.getUserNo());
		reqBody.setEmail(user.getEmail());
		reqBody.setLinkMan(user.getCompany().getLinkName());
		reqBody.setLinkPhoneNum(user.getCompany().getTel());
		reqBody.setCompanyName(user.getCompany().getName());
		reqBody.setCompanyDeclinedAddress(user.getCompany().getAddress());
			
		ExteriorHelper.buildRequest(reqBody, ActionType.UPLOAD_USER);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ResponseDto userInfo(String dataId)
	{
		// 请求体ReqBody
		ReqBody reqBody = new ReqBody();
		reqBody.setDataId(dataId);
		ResponseDto rspDto = ExteriorHelper.buildRequest(reqBody, ActionType.USER_INFO);
		return rspDto;
	}

	@Override
	public ResponseDto checkUser(User user)
	{
		// 请求体ReqBody
		ReqBody reqBody = new ReqBody();
		// uid为空的说明是印管家的账户，不为空的说明是印刷家的账户
		if (null == user.getUid())
		{
			reqBody.setUid(user.getId().toString());
			reqBody.setToken(user.getUserNo());
		} else
		{
			reqBody.setUid(user.getUid());
			reqBody.setToken(user.getToken());
		}
		reqBody.setMobile(user.getMobile());
		
		ResponseDto response = ExteriorHelper.buildRequest(reqBody, ActionType.CHECK_USER);
		return response;
	}

	@Override
	public ResponseDto thirdCheckUser(RequestDto requestDto)
	{
		ResponseDto rsp = new ResponseDto();
		RspHeader rspHead = new RspHeader();
		RspBody rspBody = new RspBody();
		
		ReqBody reqBody = requestDto.getReqBody();
		if ( null == reqBody.getUid() || null == reqBody.getToken())
		{
			rspHead.setRspCode("EC3001");
			rspHead.setRspMsg("必填字段不能为空");
			rspBody.setVerifyState("fail");
		} else
		{
			User user = null;
			// 印刷家平台用户
			if (requestDto.getReqBody().getIsPHUser().equals("true"))
			{
				user = getUserByCondition(requestDto.getReqBody());
			} else //印管家平台用户
			{
				String uid = requestDto.getReqBody().getUid();
				user = serviceFactory.getUserService().get(Long.valueOf(uid));
			}
			
			// 响应用户是否存在
			if (null != user)
			{
				rspHead.setRspCode("EC0000");
				rspHead.setRspMsg("系统处理成功");
				rspBody.setVerifyState("success");
				rspBody.setForwardURL(ExteriorHelper.url(requestDto.getReqBody()));
			} else
			{
				rspHead.setRspCode("EC3001");
				rspHead.setRspMsg("不存在此账户");
				rspBody.setVerifyState("fail");
				rspBody.setForwardURL(SystemConfigUtil.getConfig(SysConstants.SYSTEM_URL));
			}
		}
		rsp.setResponseHeader(rspHead);
		rsp.setRspBody(rspBody);
		return rsp;
	}

	@Override
	@Transactional
	public ResponseDto acceptUserNotice(RequestDto requestDto)
	{
		// 印刷家通知用户登录的接口，对方请求此接口，我方获取到用户的id（此id为对方生成的）
		String dataId = requestDto.getReqBody().getDataId();
		ResponseDto responseDto = new ResponseDto();
		RspHeader header = new RspHeader();
		RspBody body = new RspBody();
		if (Validate.validateObjectsNullOrEmpty(dataId))
		{
			header.setRspCode("EC9999");
			header.setRspMsg("系统处理失败");
			body.setState("fail");
			logger.info("接收印刷家dataId失败");
		} else
		{
			// 验证是否已注册过此账户
			ReqBody query_body = new ReqBody();
			query_body.setUid(dataId);
			if (Validate.validateObjectsNullOrEmpty(getUserByCondition(query_body)))
			{
				// 请求获取账户信息
				ResponseDto responseDto_ = serviceFactory.getExteriorService().userInfo(dataId);
				if (responseDto_ != null)
				{
					// 需校验该用户的手机号是否已存在印管家，如果存在的话则不需注册，且修改user表的Uid字段，uid = dataId;
					ReqBody query_body2 = new ReqBody();
					query_body2.setMobile(responseDto_.getRspBody().getMobile());
					
					User _user = getUserByCondition(query_body2);
					if (Validate.validateObjectsNullOrEmpty(_user))
					{
						serviceFactory.getExteriorRegisterService().register(responseDto_); // 注册
					} else
					{
						_user.setUid(dataId); // 修改uid
						_updateUser(_user);
					}
				} else
				{
					logger.error("请求无响应");
				}
			} 
			header.setRspCode("EC0000");
			header.setRspMsg("系统处理成功");
			body.setState("success");
			logger.info("接收印刷家dataId成功："+dataId);
		}
		responseDto.setRspBody(body);
		responseDto.setResponseHeader(header);
	
		return responseDto;
	}
	
	/* （非 Javadoc）
	 * @see com.huayin.printmanager.exterior.service.ExteriorService#agreement()
	 */
	@Override
	@Transactional
	public User agreement()
	{
		final User _user ;
		// 签订绑定协议后上传用户信息
		User user = daoFactory.getCommonDao().lockObject(User.class, UserUtils.getUserId());
		user.setCompany(UserUtils.getCompany());
		user.setIsSign(BoolValue.YES);
		_user = daoFactory.getCommonDao().updateEntity(user);
		// 上传给印刷家
		threadPool.execute(new Runnable()
		{
			@Override
			public void run()
			{
				uploadUser(_user);
			}
		});
		UserUtils.updateSessionUser();
		return user;
	}
	
	/* （非 Javadoc）
	 * @see com.huayin.printmanager.exterior.service.ExteriorService#findUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public User findUser(String uid,String token, String isPHUser)
	{
		DynamicQuery query = new DynamicQuery(User.class);
		if (isPHUser.equals("true")) // 印刷家平台账户
		{
			query.eq("uid", uid);
			query.eq("token", token);
		} else // 印管家平台账户
		{
			query.eq("id", Long.valueOf(uid));
			query.eq("userNo", token);
		}
		
		return daoFactory.getCommonDao().getByDynamicQuery(query, User.class);
	}
	
	/**
	 * <pre>
	 * 查询账户
	 * </pre>
	 * @param reqBody
	 * @return
	 * @since 1.0, 2018年7月12日 下午2:21:20, zhengby
	 */
	@Override
	public User getUserByCondition(ReqBody reqBody)
	{
		DynamicQuery query = new DynamicQuery(User.class);
		if (null != reqBody.getUid())
		{
			query.eq("uid", reqBody.getUid());
		}
		if (null != reqBody.getMobile())
		{
			query.eq("mobile", reqBody.getMobile());
		}
		return daoFactory.getCommonDao().getByDynamicQuery(query, User.class);
	}
	
	/**
	 * <pre>
	 * 更新账户信息
	 * </pre>
	 * @param user
	 * @since 1.0, 2018年7月12日 下午2:21:17, zhengby
	 */
	private void _updateUser(User user)
	{
		User user_ = daoFactory.getCommonDao().lockObject(User.class, user.getId());
		user_.setUid(user.getUid());
		daoFactory.getCommonDao().updateEntity(user_);
	}
}

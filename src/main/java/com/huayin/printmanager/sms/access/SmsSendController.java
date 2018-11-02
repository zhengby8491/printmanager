/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月26日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.sms.access;

import java.util.regex.Pattern;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.exception.ServiceResult;
import com.huayin.common.util.MD5;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.constants.ErrorCodeConstants;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.persist.entity.sys.SmsPortal;
import com.huayin.printmanager.persist.enumerate.SmsType;
import com.huayin.printmanager.persist.enumerate.State;
import com.huayin.printmanager.sms.vo.RequestMessage;

/**
 * <pre>
 * 短信平台 - 短信发送
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月26日, 整理规范
 */
@Controller
@RequestMapping(value = "sms")
public class SmsSendController extends BaseController
{
	/**
	 * <pre>
	 * 功能 - 发送短信
	 * </pre>
	 * @param msg
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:53:34, think
	 */
	@RequestMapping(value = "send")
	@ResponseBody
	public ServiceResult<String> send(@RequestBody RequestMessage msg)
	{
		ServiceResult<String> result = new ServiceResult<String>();

		if (Validate.validateObjectsNullOrEmpty(msg, msg.getAccountId(), msg.getMobile(), msg.getContent(), msg.getDigest()))
		{
			result.setCode(ErrorCodeConstants.CommonCode.COMMON_DATA_NOT_VALIDATA);
			result.setMessage("数据验证不通过");
			result.setIsSuccess(false);
		}
		else if (!Pattern.compile("^(13[0-9]|14[5,7]|15[0-9]|18[0-9]|17[0-9])[0-9]{8}$").matcher(msg.getMobile()).matches())
		{// 手机号码格式不正确
			result.setCode(ErrorCodeConstants.CommonCode.COMMON_DATA_NOT_VALIDATA);
			result.setMessage("手机号码格式不正确");
			result.setIsSuccess(false);
		}
		else
		{
			SmsPortal portal = serviceFactory.getSmsPortalService().getByAccountId(msg.getAccountId());
			if (portal == null)
			{
				result.setCode(ErrorCodeConstants.CommonCode.COMMON_DATA_NOT_FOUND);
				result.setMessage("账户不存在");
				result.setIsSuccess(false);
			}
			else
			{
				if (portal.getState() == State.CLOSED)
				{
					result.setCode(ErrorCodeConstants.CommonCode.COMMON_STATE_ERROR);
					result.setMessage("账户已关闭");
					result.setIsSuccess(false);
				}
				else
				{
					String right_digest = MD5.encodeString(msg.getAccountId() + portal.getSecretkey() + msg.getContent(), "utf-8");
					if (msg.getDigest().equals(right_digest))
					{// 摘要校验通过
						serviceFactory.getSmsSendService().sendNow(portal.getAccountId(), msg.getMobile(), portal.getSign() + msg.getContent(), portal.getPartnerId(), SmsType.SMSPORTAL);
					}
					else
					{
						result.setCode(ErrorCodeConstants.CommonCode.COMMON_DIGEST_ERROR);
						result.setMessage("摘要错误!正确摘要[" + right_digest + "]");
						result.setIsSuccess(false);
					}
				}
			}
		}
		return result;
	}

}

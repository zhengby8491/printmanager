/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年6月28日 上午9:54:17
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.exterior.dto.RequestDto;
import com.huayin.printmanager.exterior.dto.ResponseDto;
import com.huayin.printmanager.exterior.dto.RspBody;
import com.huayin.printmanager.exterior.dto.RspHeader;
import com.huayin.printmanager.exterior.dto.RspPurchItems;
import com.huayin.printmanager.exterior.dto.ValidateReturnDto;
import com.huayin.printmanager.exterior.enums.ActionType;
import com.huayin.printmanager.exterior.support.SignValidate;
import com.huayin.printmanager.exterior.utils.ExteriorHelper;
import com.huayin.printmanager.persist.entity.sys.User;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 外部接口
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年6月28日上午9:54:17, zhengby
 */
@Controller
@RequestMapping(value = "${basePath}/exterior")
public class ExteriorController extends BaseController
{
	protected static final ExecutorService threadPool = Executors.newCachedThreadPool();

	/**
	 * <pre>
	 * 功能  - 所有请求的总controller
	 * </pre>
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @since 1.0, 2018年7月5日 上午11:41:26, zhengby
	 */
	@RequestMapping(value = "handler")
	public void handler(HttpServletRequest request, HttpServletResponse response, ModelMap map)
	{
		/**
		 * 1.先验证签名
		 * 2.验证签名工具返回验证是否通过标识isPass,及请求的参数实体
		 * 3.根据请求参数实体的action值分配到不同的方法中
		 */
		ValidateReturnDto returnDto = SignValidate.validate(request, response);// 验证签名工具
		ResponseDto responseDto = new ResponseDto(); // 响应参数实体
		RequestDto requestDto = returnDto.getRequestDto();// 请求参数实体
		String xiangying = null ;  //响应字符串
		if (returnDto.isPass())
		{
			logger.info("=====请求名称：【" + requestDto.getReqHead().getAction()+"】");
			if (requestDto.getReqHead().getAction().equals(ActionType.GET_USER_NOTICE.getText()))
			{
				responseDto = serviceFactory.getExteriorService().acceptUserNotice(requestDto);
			}
			else if (requestDto.getReqHead().getAction().equals(ActionType.THIRD_CHECK_USER.getText()))
			{
				responseDto = serviceFactory.getExteriorService().thirdCheckUser(requestDto);
			} else if (requestDto.getReqHead().getAction().equals(ActionType.GET_PO_NOTICE.getText()))
			{
				responseDto = serviceFactory.getExteriorPurchService().acceptPONotice(requestDto);
			}
			logger.info("=====请求名称：【" + requestDto.getReqHead().getAction()+"】响应");
		}
		else
		{
			RspHeader responseHeader = new RspHeader();
			responseHeader.setRspCode("EC9999");
			responseHeader.setRspMsg("签名验证失败");
			responseDto.setResponseHeader(responseHeader);
		}
		try
		{
			xiangying = ExteriorHelper.toJsonObejct(responseDto).toString();
			response.getOutputStream().write(xiangying.getBytes("utf-8"));
		}
		catch (UnsupportedEncodingException e)
		{
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		catch (IOException e)
		{
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * <pre>
	 * 功能  - 外部登录接口
	 * </pre>
	 * @param request
	 * @param response
	 * @param mobile
	 * @param token
	 * @param uid
	 * @param isPHUser
	 * @return
	 * @since 1.0, 2018年7月11日 下午7:06:35, zhengby
	 */
	@RequestMapping(value = "login")
	public String login(HttpServletRequest request, HttpServletResponse response,String t,String u,String PH)
	{
		String uid = u;
		String token = t;
		String isPHUser = PH;
		if (null == uid || null == token || null == isPHUser )
		{
			return "login";
		}
		User user = serviceFactory.getExteriorService().findUser(uid, token, isPHUser);
		if (null != user)
		{
			UserUtils.login(user);
		}
		return "redirect:"+ basePath+"/loginSuc";
	}
	
	/**
	 * <pre>
	 * 功能  - 跳转到印刷家
	 * </pre>
	 * @param userId
	 * @since 1.0, 2018年7月9日 下午1:44:21, zhengby
	 */
	@RequestMapping(value = "forwardYSJ")
	@ResponseBody
	public AjaxResponseBody forwardYSJ(HttpServletRequest request, HttpServletResponse response, ModelMap map, Long userId)
	{
		User user = serviceFactory.getUserService().get(UserUtils.getUserId());
		// 查询用户是否已签订协议
		if (user.getIsSign() == BoolValue.YES)
		{
			return returnSuccessBody();
		}
		else
		{
			// 用户未绑定印刷家
			return returnErrorBody("");
		}
	}

	/**
	 * <pre>
	 * 页面  - 查看声明绑定印刷家提示页面
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2018年7月9日 下午6:02:05, zhengby
	 */
	@RequestMapping(value = "viewAgreement")
	public String viewAgreement(HttpServletRequest request, HttpServletResponse response, ModelMap map)
	{
		return "exterior/agreement";
	}

	/**
	 * <pre>
	 * 功能  - 签订印刷家协议
	 * </pre>
	 * @return
	 * @since 1.0, 2018年7月10日 下午1:54:05, zhengby
	 */
	@RequestMapping(value = "submitAgreement")
	@ResponseBody
	public AjaxResponseBody submitAgreement(HttpServletRequest request, ModelMap map)
	{
		User user = serviceFactory.getExteriorService().agreement();// 签订协议
		if (user.getIsSign() == BoolValue.YES)
		{
			return returnSuccessBody("");
		}
		else
		{
			// 用户未绑定印刷家
			return returnErrorBody("用户未绑定印刷家");
		}
	}
	
	/**
	 * <pre>
	 * 页面  - 跳转页面
	 * </pre>
	 * @return
	 * @since 1.0, 2018年7月11日 下午3:58:33, zhengby
	 */
	@RequestMapping(value = "load")
	public String load()
	{
		return "exterior/load";
	}
	
	/**
	 * <pre>
	 * 功能  - 获取跳转url
	 * </pre>
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @since 1.0, 2018年7月11日 下午3:58:07, zhengby
	 */
	@RequestMapping(value = "forward")
	@ResponseBody
	public AjaxResponseBody forward(HttpServletRequest request, HttpServletResponse response, ModelMap map)
	{
		// 实时查询user
		User user = serviceFactory.getUserService().get(UserUtils.getUserId());
		
		if (user.getIsSign() == BoolValue.YES)
		{
			ResponseDto responseDto = serviceFactory.getExteriorService().checkUser(user);// 向印刷家获取授权url
			if (null != responseDto && !responseDto.getResponseHeader().getRspCode().equals("EC0000"))
			{
				String errMsg = responseDto.getResponseHeader().getRspCode() + responseDto.getResponseHeader().getRspMsg();
				logger.error(errMsg);
				return returnErrorBody(errMsg);
			}
			if (null == responseDto)
			{
				logger.error("印刷家无响应");
				return returnErrorBody("印刷家无响应");
			}
			String url = responseDto.getRspBody().getForwardURL();
			return returnSuccessBody(url);
		}
		else // 未签订协议情况下
		{ // 判断账号有没有填写手机号
			if(StringUtils.isBlank(user.getMobile()))
			{
				// 未填写电话
				return returnErrorBody("NOPHONE");
			} else
			{
				// 未绑定协议
				return returnErrorBody("NOSIGN");
			}
		}
	}
	
	public static void main(String[] args)
	{
		RspPurchItems items = new RspPurchItems();
		items.setBrandName("海利");
		items.setBuyNum("20180799112");
		items.setCategory("外部");
		items.setDiscountPrice("2");
		items.setItemTotalPrice("18.8");
		items.setItemPrice("2.4");
		items.setItemName("钢管");
		items.setBuyUnit("吨");
		RspPurchItems items2 = new RspPurchItems();
		items2.setBrandName("海利2");
		items2.setBuyNum("20180799112");
		items2.setCategory("外部");
		items2.setDiscountPrice("2");
		items2.setItemTotalPrice("18.8");
		items2.setItemPrice("2.4");
		items2.setItemName("钢管2");
		items2.setBuyUnit("吨");
		List<RspPurchItems> _items = Lists.newArrayList();
		_items.add(items);
		_items.add(items2);
		
		RspBody rspBody = new RspBody();
		rspBody.setItems(_items);
		rspBody.setDeliveryMethod("1");
		rspBody.setUid("USER201807121542141000000141");
		rspBody.setPaymentType("1");
		rspBody.setShipmentType("2");
		rspBody.setInvoiceType("1");
		rspBody.setDeliveryMethod("1");
		rspBody.setOrderStatus("1");
		rspBody.setSellerName("海利南山店");
		rspBody.setOrderFreight("32.1");
		rspBody.setPurchaseOrderNm("PO20180714991");
		
		RspHeader header = new RspHeader();
		
		header.setRspCode("C0000");
		header.setRspMsg("处理成功");
		
		ResponseDto dto = new ResponseDto();
		dto.setResponseHeader(header);
		dto.setRspBody(rspBody);
	}
}

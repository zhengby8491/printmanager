<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>${projectName}-注册</title>
<meta name="keywords" content="印管家,印管家V2.0,印刷,印刷行业系统,后台管理,企业后台管理系统,ERP">
<meta name="description" content="印管家...">
<script type="text/javascript">
	$(function()
	{
/* 		$.validator.addMethod("isMobile", function(value, element)
				{
					alert(0);
					var length = value.length;
					var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
					return this.optional(element) || (length == 11 && mobile.test(value));
				}, "请正确填写您的手机号码");
		 */
		$("#registerForm").validate({
			submitHandler : function(form)
			{
				form.submit();
			},
			rules : {
				"userName" : {
					required : true,
					maxlength : 30,
					stringCheck : true,
					remote : {
						type : "POST",
						url : Helper.basePath + '/register/exist/userName',
						dataType : "json",
						data : {
							userName : function()
							{
								return $("#userName").val()
							}
						}
					}
				},
				"mobile" : {
					required : true,
					isMobile : true,
					remote : {
						type : "POST",
						url : Helper.basePath + '/register/exist/mobile',
						dataType : "json",
						data : {
							mobile : function()
							{
								return $("#mobile").val()
							}
						}
					}
				},
				"password" : {
					minlength : 6,
					required : true,
					isPwd : false
				},
				"confirm_password" : {
					required : true,
					minlength : 6,
					equalTo : "#password"
				},
				"validCode" : {
					required : true,
					remote : {
						type : "POST",
						url : Helper.basePath + '/public/checkValidCode',
						dataType : "json",
						data : {
							mobile : function()
							{
								return $("#mobile").val().trim()
							},
							code : function()
							{
								return $("#validCode").val().trim()
							}
						}
					}
				}
			},
			messages : {
				userName : {
					required : '<span class="m_error"><i>*</i>请输入用户名</span>',
					maxlength : '<span class="m_error"><i>*</i>最多可以输入 {0}个字符</span>',
					stringCheck : '<span class="m_error"><i>*</i>只能包含中文、英文、数字、下划线等字符</span>',
					remote : '<span class="m_error"><i>*</i>用户名已被注册</span>'
				},
				mobile : {
					required : '<span class="m_error"><i>*</i>请输入手机号码</span>',
					isMobile : '<span class="m_error"><i>*</i>手机号码格式有误</span>',
					remote : '<span class="m_error"><i>*</i>手机号码已被注册</span>'
				},
				password : {
					minlength : '<span class="m_error"><i>*</i>最少要输入 6 个字符</span>',
					required : '<span class="m_error"><i>*</i>请输入密码</span>',
					isPwd : '<span class="m_error"><i>*</i>以字母开头，长度在6-12之间，只能包含字符、数字和下划线。</span>',
				},
				confirm_password : {
					required : '<span class="m_error"><i>*</i>请输入密码</span>',
					minlength : '<span class="m_error"><i>*</i>最少要输入 6 个字符</span>',
					equalTo : '<span class="m_error"><i>*</i>密码和确认密码不一致</span>'
				},
				validCode : {
					required : '<span class="m_error"><i>*</i> 请输入验证码</span>',
					remote : '<span class="m_error"><i>*</i> 请输入正确的验证码</span>'
				}
			},
			onkeyup : false,
			onfocusout : false,
			onsubmit : true
		});
	});

	
</script>
</head>
<body>
	<div class="hy_count">
		<div class="hy_wrap">
			<!--头部Start-->
			<div class="header">
				<div class="header_wrap">
					<div class="logo">
						<a href="#" style="text-decoration: none;"> 
							<span class="icon icon-ygj" style="font-size: 85px; color: #02A578;"></span>
						</a>
					</div>
					<div class="info">
						<span class="info_1">欢迎来到印管家！</span> <span class="info_2"> 当前在线用户： <b>${fns:sessionCount()}</b> 人
						</span>
					</div>
					<div class="link">
						<a href="${ctx }">已有账号，立即登录</a>
					</div>
				</div>
			</div>
			<!--头部End-->
			<!--主要部分Start-->
			<div class="main">
				<div class="main_wrap">
					<div class="main_container">
						<!--注册账号Start-->
						<div class="l main_left">
							<form id="registerForm" method="post" action="${ctx }/register_submit">
								<h3>注&nbsp;册&nbsp;账&nbsp;号</h3>
								<div>
									<div class="m_item">
										<label class="m_label">用户名：</label> <input class="m_input" id="userName" name="userName" placeholder="请输入用户名" type="text" />
									</div>
									<div class="m_item">
										<label class="m_label">手机号码：</label> <input class="m_input" placeholder="请输入要注册的手机号码" id="mobile" name="mobile" type="text" />
									</div>
									<div class="m_item register_pw">
										<label class="m_label">密码：</label> <input class="m_input" name="password" type="password" placeholder="请输入密码，至少六个字符" id="password" />
									</div>
									<div class="m_item">
										<label class="m_label">确认密码：</label> <input class="m_input" type="password" placeholder="请确认密码" name="confirm_password" id="confirm_password" />
									</div>

									<div class="m_item m_recover_yzm" id="recover_yzm">
										<label class="m_label">验证码：</label>
										<sys:captcha name="captcha" />
									</div>

									<div class="m_item register_yzm">
										<label class="m_label">短信验证码：</label>
										<sys:smsValidCode type="REGEDIT_VALIDCODE" code_css="m_code" btn_css="m_send m_active" />
									</div>
									<div class="m_item">
										<label class="m_label"></label> <input class="m_submit m_active" type="submit" value="注 册" />
									</div>
								</div>
							</form>
						</div>
						<!--注册账号End-->
						<!--相关产品Start-->
						<div class="l main_right">
							<h3>相关产品</h3>
							<div class="product_items">
								<div class="p_item">
									<a href="#"> <img src="${ctxStatic}/layout/images/menu/web.png" />
									</a>
								</div>
								<div class="p_item">
									<a href="#"> <img src="${ctxStatic}/layout/images/menu/app.png" />
									</a>
								</div>
								<div class="p_item">
									<a href="#"> <img src="${ctxStatic}/layout/images/menu/O2O.png" />
									</a>
								</div>
								<div class="p_item">
									<a href="#"> <img src="${ctxStatic}/layout/images/menu/chain.png" />
									</a>
								</div>
							</div>
						</div>
						<!--相关产品End-->
					</div>
				</div>
			</div>
			<!--主要部分End-->
			<!-- 底部Start -->
			<sys:footer />
			<!-- 底部End -->
		</div>
	</div>
</body>
</html>

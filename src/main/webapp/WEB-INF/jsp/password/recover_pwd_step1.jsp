<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>找回密码-确认账号</title>
<meta name="keywords" content="印管家,印管家V2.0,印刷,印刷行业系统,后台管理,企业后台管理系统,ERP">
<meta name="description" content="印管家...">
<script type="text/javascript" src="${ctxStatic}/site/password/recover_pwd_step1.js?v=${v}">
</script>
</head>
<body>
	<sys:message content="${message}" />
	<div class="hy_count">
		<!--头部Start-->
		<div class="header">
			<div class="header_wrap">
				<div class="logo">
					<a href="#" style="text-decoration: none;">
						<span class="icon icon-ygj" style="font-size: 85px; color: #02A578;"></span>
					</a>
				</div>
				<div class="info">
					<span class="info_1">欢迎来到印管家！</span>
					<span class="info_2">
						当前在线用户：
						<b>${fns:sessionCount()}</b>
						人
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
					<!--找回密码Start-->
					<p class="m_title">
						<span>| 找&nbsp;回&nbsp;密&nbsp;码</span>
					</p>
					<div class="m_process">
						<div class="m_step m_step_1">
							<div class="step step_1 step_active">
								<i>1</i>
								<span>确认账号</span>
							</div>
							<div class="step step_2">
								<i>2</i>
								<span>安全验证</span>
							</div>
							<div class="step step_3">
								<i>3</i>
								<span>重置密码</span>
							</div>
						</div>
						<p class="m_p">
							<span>请填写您需要找回的账号</span>
						</p>
						<div class="m_form">
							<!--表单域Start-->
							<form id="resetForm" action="${ctx }/password/recover/step2">
								<div class="m_item">
									<input class="m_input" type="text" placeholder="请您输入用户名" id="searchContent" name="searchContent" required="true" value="${searchContent }" />

								</div>
								<div class="m_item m_recover_yzm">
									<sys:captcha name="captcha" />
								</div>
								<div class="m_item">
									<input class="m_submit m_submit_next m_active" type="submit" value="下 一 步" />
								</div>
							</form>
							<!--表单域End-->
						</div>
					</div>
					<!--找回密码End-->
				</div>
			</div>
		</div>
		<!--主要部分End-->
	</div>
</body>
</html>

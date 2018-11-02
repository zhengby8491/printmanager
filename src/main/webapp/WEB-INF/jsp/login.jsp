<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta http-equiv="Cache-Control" content="no-siteapp" />
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta name="keywords" content="印管家,印刷,印刷行业系统,后台管理,企业后台管理系统,ERP">
<meta name="description" content="印管家,印刷,印刷行业系统,后台管理,企业后台管理系统,ERP">
<title>印管家云平台</title>
<link rel="shortcut icon" href="${ctxHYUI }/images/favicon.icon" type="image/x-icon">
<c:if test="${mode == 'dev' }">
	<link rel="stylesheet" type="text/css" href="${ctxHYUI}/css/hyui.css?v=${v }" />
	<link rel="stylesheet" type="text/css" href="${ctxHYUI}/src/login.css?v=${v }" />
</c:if>
<c:if test="${mode == 'pro' }">
	<link rel="stylesheet" type="text/css" href="${ctxHYUI}/css/hyui.login.css?v=${v }" />
</c:if>
</head>
<body>
	<input type="hidden" id="path" value="${path}">
	<span id="keleyivisitorip" style="display: none"></span>
	<div class="hy_count">
		<div class="hy_wrap">
			<!--头部Start-->
			<div class="header">
				<div class="header_wrap">
					<div class="logo">
						<span class="icon icon-ygj" style="font-size: 85px; color: #02A578;"></span>
					</div>
					<div class="info">
						<span class="info_1">欢迎来到印管家！</span> <span class="info_2" style="margin-right: 20px;"> 当前在线用户： <b>${fns:sessionCount()}</b> 人
						</span> <span class="info_2" style="margin-right: 20px;">技术服务：${fns:getConfig('SITE_SERVICE_PHONE')}</span>
					</div>
					<div class="wx">
						<span class="icon icon-wx" style="font-size: 20px;">微信版 </span>
						<div class="r qrode">
							<img alt="" src="${ctxHYUI }/images/login_qrcode.jpg" />
						</div>
					</div>
				</div>
			</div>
			<!--头部End-->
			<!--主要部分Start-->
			<div class="main login_bg">
				<div class="main_wrap">
					<!-- 广告轮播 Start-->
					<div id="slideBox" class="slideBox">
						<div class="hd">
							<ul></ul>
						</div>
						<div class="bd">
							<ul>
							</ul>
						</div>
						<!-- 下面是前/后按钮代码，如果不需要删除即可 -->
						<a class="prev" href="javascript:void(0)"></a> <a class="next" href="javascript:void(0)"></a>
					</div>
					<!-- 广告轮播 End-->
					<!--登录面板Start-->
					<div class="m_login">
						<div class="m_box m_top">
							<form action="${ctx}/login" method="post" id="loginForm">
								<h3>
									<fmt:message key="i18n.login.label.welcomeback" />
								</h3>
								<div class="login_item">
									<span class="icon icon-user"></span> <input class="login_input" type="text" placeholder="<fmt:message key="syslabel.username" />" id="username" name="username" required="true" value="${username }" />
								</div>
								<div class="login_item">
									<span class="icon icon-key"></span> <input class="login_input" type="password" placeholder="<fmt:message key="syslabel.password" />" id="password" name="password" required="true" />
								</div>
								<c:if test="${isShowCaptcha}">
									<div class="login_item" style="width: 80px">
										<sys:captcha name="captcha" />
									</div>
								</c:if>
								<div class="login_forgetPW cl">
									<a href="${ctx }/password/recover/step1">忘记密码?</a>
								</div>
								<div class="login_operation">
									<label class="m_remember" title="<fmt:message key="syslabel.rememberMe.title" />"> <input type="checkbox" id="rememberMe" name="rememberMe" ${rememberMe ? 'checked' : ''} /> <span> <fmt:message key="syslabel.rememberMe" />
									</span>
									</label> <input type="button" class="login_submit m_active" style="cursor: pointer;" id="loginBtn" value="<fmt:message key="syslabel.login" />" />
								</div>
							</form>
						</div>
						<div class="m_box m_bottom">
							<a style="margin-right: 5px;" href="${ctx }/public/expresslogin/TENCENT" title="QQ登录" target="_blank"> <span class="icon-qq-logo" style="font-size: 20px; line-height: 40px;"> <span class="path1"></span><span class="path2"></span><span class="path3"></span><span class="path4"></span><span class="path5"></span><span class="path6"></span><span class="path7"></span>
							</span>
							</a> <a href="${ctx }/public/expresslogin/WEIXIN" title="微信登录" target="_blank"> <i class="icon icon-wx-logo" style="font-size: 20px; line-height: 40px;"></i>
							</a> <span class="r"> 还没有账号? <a href="${ctx }/register">马上注册！</a>
							</span>
						</div>
					</div>
					<!--登录面板End-->
					<div>
						<div class="m_bg m_bg_1">
							<%-- 							<img alt="" src="${ctxStatic }/layout/images/login/login_bg.png" /> --%>
						</div>
					</div>
					<div class="m_describe">
						<p class="m_p1">全 新 升 级</p>
						<p class="m_p2">大道至简-让更多的企业轻松掌握管理</p>
					</div>
				</div>
			</div>
			<!--主要部分End-->
			<!-- 底部Start -->
			<%-- <sys:footer /> --%>
			<div class="footer">
				<div>
					<ul class="link_ul">
						<li><a href="http://www.huayinsoft.com/" target="_blank">关于印管家</a></li>
						<li>|</li>
						<li><a href="http://www.yinzhierp.com/" target="_blank">合作伙伴</a></li>
						<li>|</li>
						<li><a href="http://www.huayinsoft.com/support.html" target="_blank">联系客服</a></li>
						<li>|</li>
						<li><a href="http://www.jobui.com/company/12196084/jobs/" target="_blank">诚征英才</a></li>
						<li>|</li>
						<li><a href="http://www.sipo.gov.cn/" target="_blank">知识产权</a></li>
					</ul>
				</div>
				<div class="copyright">
					版权所有Copyright &copy; 2013-现在&nbsp;&nbsp;深圳华印信息技术有限公司 &nbsp;&nbsp; <a href="http://www.miitbeian.gov.cn" target="_blank">粤ICP备17027007号-4</a>
				</div>
				<div>
					<ul class="link_ul">
						<li><a href="http://www.51ygj.com/print/login" target="_blank">印管家云ERP</a></li>
						<li>|</li>
						<li><a href="http://www.huayinsoft.com/product_wechat.html" target="_blank">印管家微信电商平台</a></li>
					</ul>
				</div>
			</div>
			<!-- 底部End -->
		</div>
	</div>
</body>
<!--[if lt IE 9]>
<script type="text/javascript" src="${ctxHYUI}/plugins/html5.js"></script>
<script type="text/javascript" src="${ctxHYUI}/plugins/respond.min.js"></script>
<script type="text/javascript" src="${ctxHYUI}/plugins/PIE_IE678.js"></script>
<![endif]-->
<c:if test="${mode == 'dev' }">
	<script type="text/javascript" src="${ctxHYUI}/plugins/jquery/1.9.1/jquery.min.js?v=${v }"></script>
	<script type="text/javascript" src="${ctxHYUI}/plugins/jquery.form/jquery.form.js?v=${v }"></script>
	<script type="text/javascript" src="${ctxHYUI}/plugins/jquery.cookie/jquery.cookie.js?v=${v }"></script>
	<script type="text/javascript" src="${ctxHYUI}/plugins/jquery.validation/1.14.0/jquery.validate.min.js?v=${v }"></script>
	<script type="text/javascript" src="${ctxHYUI}/plugins/jquery.SuperSlide/jquery.SuperSlide.min.js?v=${v }"></script>
	<script type="text/javascript" src="${ctxHYUI}/src/public.js?v=${v }"></script>
	<script type="text/javascript" src="${ctxHYUI}/src/login.js?v=${v }"></script>
</c:if>
<c:if test="${mode == 'pro' }">
	<script type="text/javascript" src="${ctxHYUI}/js/hyui.login.js?v=${v }"></script>
</c:if>
<script type="text/javascript" src="${ctxHYUI}/plugins/layer/layer.js?v=${v }"></script>
<script type="text/javascript">
	//$.noConflict(true);  // <- this：Uncaught TypeError: Cannot read property 'addMethod' of undefined
	Helper.locale = "${locale}";
	Helper.basePath = "${ctx}";
	Helper.staticPath = "${ctxStatic}";
	// 如果在框架或在对话框中，则弹出提示并跳转到首页
	if (self.frameElement && self.frameElement.tagName == "IFRAME")
	{
		alert('登录超时。请重新登录，谢谢！');
		top.location = "${ctx}";
	}
	// 登录错误信息
	var _errorMsg = "${error_message}";
	if (Helper.validata.isNotEmpty(_errorMsg))
	{
		Helper.message.warn(_errorMsg);
	}
	// 记住我
	if ("true" == $.cookie("rememberMe"))
	{
		$("#username").val($.cookie("username"));			// 设置用户名
	}
</script>
</html>

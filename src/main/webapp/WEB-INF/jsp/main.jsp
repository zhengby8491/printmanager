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
	<link rel="stylesheet" type="text/css" href="${ctxHYUI}/src/main.css?v=${v }" />
	<link rel="stylesheet" type="text/css" href="${ctxHYUI}/src/animate.css?v=${v }" />
	<link rel="stylesheet" type="text/css" href="${ctxHYUI}/plugins/jquery.powerFloat/css/powerFloat.css?v=${v }" />
	<link rel="stylesheet" type="text/css" href="${ctxHYUI}/plugins/rightMenu/smartMenu.css?v=${v }" />
</c:if>
<c:if test="${mode == 'pro' }">
	<link rel="stylesheet" type="text/css" href="${ctxHYUI}/css/hyui.main.css?v=${v }" />
</c:if>
<!-- 皮肤样式  -->
<link id="topskin" rel="stylesheet" type="text/css" href="" />
</head>
<body>
	<!--整体布局-->
	<div class="cl wrapper">
		<!-- logo -->
		<div class="main_logo icon icon-ygj" style="font-size: 40px; top: 3px;"></div>
		<!--左侧菜单栏-->
		<div class="side-menu">
			<div class="minimenu">
				<div class="skinmenu"></div>
			</div>
			<ul class="nav menu-list">
				<c:forEach items="${fns:getNavigationMenu()}" var="m1">
					<li class="item">
						<div class="cl">
							<a <c:if test="${m1.url!='#' && m1.url!=''}"> _href="${ctx }${m1.url}"</c:if> data-title="${m1.name }流程图" refresh="${m1.refresh.value }" href="javascript:void(0)"> <i class="icon icon-${m1.icon }"> <%--                   <img alt="" class="menu_img" src="${ctxStatic }/layout/images/menu/${m1.name }.png" /> --%>
							</i> <span class="item-tit">${m1.name }</span>
							</a>
						</div>
						<div class="left_border"></div>
						<div class="menu_arrow"></div>
						<div class="submenu" data-title="${m1.name }">
							<c:forEach items="${m1.childrens }" var="m2">
								<dl class="cl submenu_item">
									<dt>
										<a data-title="${m2.name }" refresh="${m2.refresh.value }" href="javascript:void(0)">${m2.name }</a>
									</dt>
									<c:if test="${!empty m2.childrens }">
										<c:forEach items="${m2.childrens }" var="m3">
											<dd>
												<!-- 注意<span>|</span>不能换行 -->
												<a class="menu3" _href="${ctx }${m3.url}" data-title="${m3.name }" refresh="${m3.refresh.value }" href="javascript:void(0)">${m3.name }</a><span>|</span>
											</dd>
										</c:forEach>
									</c:if>
								</dl>
							</c:forEach>
						</div>
					</li>
				</c:forEach>
			</ul>
		</div>
		<!--主要内容-->
		<div class="cl container">
			<input type="hidden" id="userIsSign" value="${loginUser.isSign }">
			<div class="cl topnav">
				<div class="l">
					<p class="main_title">
						<span> 印管家云平台 <!-- ${loginUser.company.name } -->
						</span>
						<c:if test="${! empty photoUrl}">
							<img class="proxy_photo" src="${photoUrl }">
						</c:if>
						<c:if test="${! empty name}">
							<span>${name }</span>
						</c:if>
					</p>
				</div>
				<div class='marquee'>${fns:getLastNotice() }</div>
				<div class="r topnav_right">
					<div id="consult_box">
						<ul class="box_ul">
							<li>QQ咨询</li>
							<c:forEach var="serviceQQ" items="${fn:split(fns:getConfig('SITE_SERVICE_QQ'),'|')}">
								<li><a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=${fn:split(serviceQQ, ':')[1]}&site=qq&menu=yes" class="qqLinks"> <span>${fn:split(serviceQQ, ':')[0]}</span> <span>${fn:split(serviceQQ, ':')[1]}</span>
								</a></li>
							</c:forEach>
							<li class="consult_phone">电话</li>
							<li>${fns:getConfig('SITE_SERVICE_PHONE')}</li>
						</ul>
					</div>
					<div id="guide_box">
						<ul class="box_ul">
							<li><a id="new_guide" href="javascript:void(0);">新手指南</a></li>
							<li><a id="online_ask" href="javascript:void(0);">在线提问</a></li>
							<li><a id="about" href="javascript:void(0);">关于印管家</a></li>
							<li><a href="http://pan.baidu.com/s/1o8At90I" target="_blank">印管家操作教程</a></li>
						</ul>
					</div>
					<c:if test="${1 == loginUser.company.systemVersion }">
						<div id="tryout_box">
							<ul class="box_ul">
								<li><a id="" href="${ctx }/demos/1">工单-送货-对账-收款</a></li>
								<li><a id="" href="${ctx }/demos/2">订单-工单-送货-对账-收款</a></li>
								<li><a id="" href="${ctx }/demos/3">销售-生产-采购-财务</a></li>
								<li><a id="" href="${ctx }/demos/4">销售-生产-发外-财务</a></li>
								<li><a id="" href="${ctx }/demos/5">销售-生产-采购-发外-财务</a></li>
								<li><a id="" href="${ctx }/demos/6">销售-生产-采购-库存-财务</a></li>
								<li><a id="" href="${ctx }/demos/7">销售-生产-采购-发外-库存-财务</a></li>
							</ul>
						</div>
					</c:if>
					<div id="skin_box" class="skin_list">
						<ul class="box_ul cl">
							<li><input type="hidden" value="default"> <span class="skin_default" style="background: #21DBA8"> <img src="${ctxStatic }/layout/images/skin/select_skin.png">
							</span></li>
							<li><input type="hidden" value="blue"> <span class="skin_blue"> <img src="${ctxStatic }/layout/images/skin/select_skin.png">
							</span></li>
							<li><input type="hidden" value="brown"> <span class="skin_brown"> <img src="${ctxStatic }/layout/images/skin/select_skin.png">
							</span></li>
						</ul>
					</div>
					<ul class="links_list cl">
						<li><span class="timeGreeting"></span> <a href="#" class="userName"> ${loginUser.employeeId ne null?(fns:basicInfoFiledValue('EMPLOYEE',loginUser.employeeId,'name')): loginUser.userName} </a></li>
						<li class="space">|</li>
						<li class="consult" src="consult_box"><i class="icon icon-top-counsel"></i>
							<div class="box_container"></div></li>
						<li class="guide" src="guide_box"><i class="icon icon-top-guide"></i>
							<div class="box_container"></div></li>
						<li class="skin" src="skin_box"><i class="icon icon-top-setting"></i>
							<div class="box_container"></div></li>
						<li class="buy"><a href="#" id="buy"> <i class="icon icon-top-cart"></i>
						</a></li>
						<!-- 判断是否开启对外调整的接口 -->
						<c:set var="switchForYSJ" value="${fns:getConfig('SWITCH_FOR_YSJ')}" />
						<c:set var="companyId" value="${fns:getUser().companyId}" />
						<c:if test="${switchForYSJ == 'YES' && companyId != 1 }">
							<li class="skin"><a href="#" id="forward" title="跳转到印刷家"> <font color="yellow">印刷家采购</font>
							</a>
								<div class="box_container"></div></li>
						</c:if>
						<c:if test="${1 == loginUser.company.systemVersion }">
							<li class="skin" src="tryout_box">
								<!-- 								<i class="fa fa-check-circle-o"></i> --> <span style="color: yellow;">多版本演示</span>
								<div class="box_container"></div>
							</li>
						</c:if>
						<li class="space">|</li>
						<li><c:set var="uname" value="${ loginUser.userName}&quot"></c:set> <c:if test="${fn:contains(fns:getConfig('SITE_DEMO_USERNAMES'), uname)}">
								<a class="perform" href="${ctx }/loginout"> 进入正式版 </a>
							</c:if> <c:if test="${!fn:contains(fns:getConfig('SITE_DEMO_USERNAMES'), uname)}">
								<a class="perform" href="${ctx }/demo"> 进入演示版 </a>
							</c:if></li>
						<li class="space">|</li>
						<li><a href="${ctx}/public/downLoad?fileName=印管家.html">一键安装</a></li>
						<!--             <li style="display: none;"> -->
						<!--               <div class="dropdown"> -->
						<!--                 <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu2" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> -->
						<!--                   语言 -->
						<!--                   <span class="caret"></span> -->
						<!--                 </button> -->
						<!--                 <ul class="dropdown-menu" aria-labelledby="dropdownMenu2"> -->
						<!--                   <li> -->
						<!--                     <a href="http://localhost:8080/printmanager/print/public/language/cn_zh">中文</a> -->
						<!--                   </li> -->
						<!--                   <li> -->
						<!--                     <a href="http://localhost:8080/printmanager/print/public/language/ko">韩文</a> -->
						<!--                   </li> -->
						<!--                 </ul> -->
						<!--               </div> -->
						<!--             </li> -->
						<li><a class="exit" href="${ctx }/loginout"> <i class="icon icon-switch"></i> 退出
						</a></li>
					</ul>
				</div>
				<!--iframe显示-->
				<div class="article-box">
					<div id="tabNav" class="tabNav hidden-xs">
						<div class="tabNav-wp">
							<button class="btn tab-swith-btn prev-tab">
								<i class="icon icon-backward2" aria-hidden="true"></i>
							</button>
							<ul id="min_title_list" class="acrossTab cl">
								<li class="active"><span title="印管家云平台" data-href="1-0.html">印管家云平台</span></li>
							</ul>
							<div class="contextmenu"></div>
							<button class="btn tab-swith-btn next-tab">
								<i class="icon icon-forward3" aria-hidden="true"></i>
							</button>
						</div>
					</div>
					<div id="iframe_box" class="article_content">
						<div class="show_iframe">
							<div style="display: none" class="loading"></div>
							<c:if test="${1 == loginUser.company.systemVersion }">
								<iframe id="iframe" scrolling="yes" frameborder="0" src="welcomeWork"></iframe>
							</c:if>
							<c:if test="${1 != loginUser.company.systemVersion }">
								<iframe id="iframe" scrolling="yes" frameborder="0" src="welcome"></iframe>
							</c:if>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!--  关于弹出框    -->
		<div class="about_mask">
			<div class="about">
				<i class="clo_icon"></i>
				<div class="layer_main_logo icon icon-ygj" style="font-size: 150px; top: 70px;"></div>
				<div class="about_info">
					<p>当前版本：V7.6</p>
					<p>
						官网地址： <a target="_blank" class="official_add" href="http://www.huayinsoft.com">www.huayinsoft.com</a>
					</p>
					<p>联系电话：${fns:getConfig('SITE_SERVICE_PHONE')}</p>
				</div>
				<div class="copy">
					版权所有Copyright © 2013-现在 深圳华印信息技术有限公司 <a href="http://www.miitbeian.gov.cn" target="_blank">粤ICP备17027007号-4</a>
				</div>
			</div>
		</div>
		<%-- <sys:footer/> --%>
	</div>
</body>
<!--[if lt IE 9]>
<script type="text/javascript" src="${ctxHYUI}/plugins/html5.js"></script>
<script type="text/javascript" src="${ctxHYUI}/plugins/respond.min.js"></script>
<script type="text/javascript" src="${ctxHYUI}/plugins/PIE_IE678.js"></script>
<![endif]-->
<c:if test="${mode == 'dev' }">
	<script type="text/javascript" src="${ctxHYUI}/plugins/jquery/1.9.1/jquery.min.js?v=${v }"></script>
	<script type="text/javascript" src="${ctxHYUI}/plugins/jquery.cookie/jquery.cookie.js?v=${v }"></script>
	<script type="text/javascript" src="${ctxHYUI}/plugins/jquery.marquee/jquery.marquee.min.js?v=${v }"></script>
	<script type="text/javascript" src="${ctxHYUI}/plugins/jquery.powerFloat/js/mini/jquery-powerFloat-min.js?v=${v }"></script>
	<script type="text/javascript" src="${ctxHYUI}/plugins/rightMenu/smartMenu.js?v=${v }"></script>
	<script type="text/javascript" src="${ctxHYUI}/src/public.js?v=${v }"></script>
	<script type="text/javascript" src="${ctxHYUI}/src/public.ext.js?v=${v }"></script>
	<script type="text/javascript" src="${ctxHYUI}/src/main.js?v=${v }"></script>
</c:if>
<c:if test="${mode == 'pro' }">
	<script type="text/javascript" src="${ctxHYUI}/js/hyui.main.js?v=${v }"></script>
</c:if>
<script type="text/javascript" src="${ctxHYUI}/plugins/layer/layer.js?v=${v }"></script>
<script type="text/javascript">
	//$.noConflict(true);  // <- this：Uncaught TypeError: Cannot read property 'addMethod' of undefined
	Helper.locale = "${locale}";
	Helper.basePath = "${ctx}";
	Helper.staticPath = "${ctxStatic}";
	Helper.v = "${v}";
	Helper.ctxHYUI = "${ctxHYUI}";
	/* 初始化首页皮肤 */
	function initTopSkin()
	{
		var cssType = $.cookie('ygj_skin');
		if (cssType == "" || cssType == null || cssType == "default")
		{
			$("#topskin").attr("href", Helper.ctxHYUI + '/themes/default/style.css?v=' + Helper.v);
		} else if (cssType == "brown")
		{
			$("#topskin").attr("href", Helper.ctxHYUI + '/themes/brown/style.css?v=' + Helper.v);
		} else if (cssType == "blue")
		{
			$("#topskin").attr("href", Helper.ctxHYUI + '/themes/blue/style.css?v=' + Helper.v);
		}
	}
	initTopSkin();
</script>
</html>

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
	<link rel="stylesheet" type="text/css" href="${ctxHYUI}/src/welcome.css?v=${v }" />
	<link rel="stylesheet" type="text/css" href="${ctxHYUI}/plugins/bootstrap-table/bootstrap.min.css?v=${v }" />
	<link rel="stylesheet" type="text/css" href="${ctxHYUI}/plugins/zabuto-calendar/style.css?v=${v }" />
	<link rel="stylesheet" type="text/css" href="${ctxHYUI}/plugins/zabuto-calendar/zabuto_calendar.min.css?v=${v }" />
	<link rel="stylesheet" type="text/css" href="${ctxHYUI}/src/common.css?v=${v }" />
</c:if>
<c:if test="${mode == 'pro' }">
	<link rel="stylesheet" type="text/css" href="${ctxHYUI}/css/hyui.welcome.css?v=${v }" />
</c:if>
<!-- 皮肤样式  -->
<link id="topskin" rel="stylesheet" type="text/css" href="" />
</head>
<body>
	<span id="keleyivisitorip" style="display: none"></span>
	<div class="hy_workdesk hy_wd">
		<div class="hy_left">
			<div class="">
				<div class="todo_item">
					<div class="info-box red-bg">
						<i class="icon icon-bell"></i>
						<div class="title">数据预警</div>
					</div>
					<div class="todo_content">
						<ul class="item_list">
							<li id="div_materialMinStock">
								<div class="item_icon"></div> <a class="item_content" href="#"> <span id="materialMinStock"></span> 条 -- 低于材料最低库存
							</a> <a class="view">查看</a>
							</li>
							<li id="div_notStockPurch">
								<div class="item_icon"></div> <a class="item_content" href="#"> <span id="notStockPurch"></span> 条 -- 3日内未入库采购
							</a> <a class="view">查看</a>
							</li>
							<li id="div_notArriveOutSource">
								<div class="item_icon"></div> <a class="item_content" href="#"> <span id="notArriveOutSource"></span> 条 -- 3日内未到货的发外
							</a> <a class="view">查看</a>
							</li>
							<li id="div_notDeliveSale">
								<div class="item_icon"></div> <a class="item_content" href="#"> <span id="notDeliveSale"></span> 条 -- 3日内未送货的订单
							</a> <a class="view">查看</a>
							</li>
							<li id="div_notReceiveOrder">
								<div class="item_icon"></div> <a class="item_content" href="#"> <span id="notReceiveOrder"></span> 条 -- 3日内未收款的账单
							</a> <a class="view">查看</a>
							</li>
							<li id="div_notPayment">
								<div class="item_icon"></div> <a class="item_content" href="#"> <span id="notPayment"></span> 条 -- 3日内未付款的账单
							</a> <a class="view">查看</a>
							</li>
						</ul>
					</div>
				</div>
				<div class="todo_item">
					<div class="info-box green-bg">
						<i class="icon icon-checkbox-checked"></i>
						<div class="title">待审核任务</div>
					</div>
					<div class="todo_content">
						<ul class="item_list">
							<li id="div_notCheckSale">
								<div class="item_icon"></div> <a class="item_content" href="#"> <span id="notCheckSale"></span> 条 -- 销售订单待审核
							</a> <a class="view">查看</a>
							</li>
							<li id="div_notCheckWork">
								<div class="item_icon"></div> <a class="item_content" href="#"> <span id="notCheckWork"></span> 条 -- 生产工单待审核
							</a> <a class="view">查看</a>
							</li>
							<li id="div_notCheckPurch">
								<div class="item_icon"></div> <a class="item_content" href="#"> <span id="notCheckPurch"></span> 条 -- 采购订单待审核
							</a> <a class="view">查看</a>
							</li>
							<li id="div_notCheckOutSource">
								<div class="item_icon"></div> <a class="item_content" href="#"> <span id="notCheckOutSource"></span> 条 -- 发外加工待审核
							</a> <a class="view">查看</a>
							</li>
							<li id="div_notCheckPayment">
								<div class="item_icon"></div> <a class="item_content" href="#"> <span id="notCheckPayment"></span> 条 -- 付款单待审核
							</a> <a class="view">查看</a>
							</li>
							<li id="div_notCheckWriteoffPayment">
								<div class="item_icon"></div> <a class="item_content" href="#"> <span id="notCheckWriteoffPayment"></span> 条 -- 付款核销单待审核
							</a> <a class="view">查看</a>
							</li>
							<li id="div_notCheckReceive">
								<div class="item_icon"></div> <a class="item_content" href="#"> <span id="notCheckReceive"></span> 条 -- 收款单待审核
							</a> <a class="view">查看</a>
							</li>
							<li id="div_notCheckWriteoffReceive">
								<div class="item_icon"></div> <a class="item_content" href="#"> <span id="notCheckWriteoffReceive"></span> 条 -- 收款核销单待审核
							</a> <a class="view">查看</a>
							</li>
							<li class="border0" style="display: none">
								<div class="item_icon"></div> <a class="item_content" href="#">更多...</a> <a class="view">查看</a>
							</li>
						</ul>
					</div>
				</div>
				<div class="todo_item">
					<div class="info-box blue-bg">
						<i class="icon icon-pencil2"></i>
						<div class="title">待办任务</div>
					</div>
					<div class="todo_content">
						<ul class="item_list">
							<!-- 								<li id="div_workSaleSumQty"> -->
							<!-- 									<div class="item_icon"></div> -->
							<!-- 									<a class="item_content" href="#"><span id="workSaleSumQty"></span>条 -- 未生产订单</a><a class="view">查看</a> -->
							<!-- 								</li> -->
							<li id="div_deliverSumQty">
								<div class="item_icon"></div> <a class="item_content" href="#"> <span id="deliverSumQty"></span> 条 -- 待送货订单
							</a> <a class="view">查看</a>
							</li>
							<li id="div_workPurchQty">
								<div class="item_icon"></div> <a class="item_content" href="#"> <span id="workPurchQty"></span> 条 -- 待采购物料
							</a> <a class="view">查看</a>
							</li>
							<li id="div_purchSumQty">
								<div class="item_icon"></div> <a class="item_content" href="#"> <span id="purchSumQty"></span> 条 -- 待采购入库
							</a> <a class="view">查看</a>
							</li>
							<li id="div_workStockQty">
								<div class="item_icon"></div> <a class="item_content" href="#"> <span id="workStockQty"></span> 条 -- 待入库工单
							</a> <a class="view">查看</a>
							</li>
							<li id="div_workTakeQty">
								<div class="item_icon"></div> <a class="item_content" href="#"> <span id="workTakeQty"></span> 条 -- 待领料物料
							</a> <a class="view">查看</a>
							</li>
							<li id="div_arriveSumQty">
								<div class="item_icon"></div> <a class="item_content" href="#"> <span id="arriveSumQty"></span> 条 -- 待到货发外
							</a> <a class="view">查看</a>
							</li>
							<li id="div_receiveSumQty">
								<div class="item_icon"></div> <a class="item_content" href="#"> <span id="receiveSumQty"></span> 条 -- 待收款销售
							</a> <a class="view">查看</a>
							</li>
							<li id="div_paymentPurchSumQty">
								<div class="item_icon"></div> <a class="item_content" href="#"> <span id="paymentPurchSumQty"></span> 条 -- 待付款采购
							</a> <a class="view">查看</a>
							</li>
							<li id="div_paymentOutSourceSumQty">
								<div class="item_icon"></div> <a class="item_content" href="#"> <span id="paymentOutSourceSumQty"></span> 条 -- 待付款发外
							</a> <a class="view">查看</a>
							</li>


							<li id="div_purchReconcilQty">
								<div class="item_icon"></div> <a class="item_content" href="#"> <span id="purchReconcilQty"></span> 条 -- 待采购对账
							</a> <a class="view">查看</a>
							</li>
							<li id="div_saleReconcilQty">
								<div class="item_icon"></div> <a class="item_content" href="#"> <span id="saleReconcilQty"></span> 条 -- 待销售对账
							</a> <a class="view">查看</a>
							</li>
							<li id="div_processReconcilQty">
								<div class="item_icon"></div> <a class="item_content" href="#"> <span id="processReconcilQty"></span> 条 -- 待加工对账
							</a> <a class="view">查看</a>
							</li>
						</ul>
					</div>
				</div>
			</div>
			<div class="hy_left_bottom">
				<div class="hy_menu_con">
					<div class="memorandum_head">
						常用功能 <i id="btn_oftenMenu" class="icon icon-cogs"></i>
					</div>
					<div class="hy_menu_list cl">
						<c:forEach items="${oftenList }" var="menu" varStatus="status">
							<div class="offen_menu_item">
								<span class="offen_menu_index">${ status.index + 1}</span>
								<div class="offen_menu_name" title="${menu.name}" data-url="${menu.url }">${menu.name }</div>
							</div>
						</c:forEach>
					</div>
				</div>
				<div class="notice_con">
					<div class="memorandum_head">公告</div>
					<div class="notice_item">${notice}</div>
				</div>
				<div class="hy_demonstration_menu">
					<div class="memorandum_head">多版本功能的演示试用</div>
					<div class="demonstration_content">
						<div class="demonstration_list" data-type="1">
							<span class="offen_menu_index_to"> <img class="demonstration_menu_img" src="${ctxHYUI}/images/welcome_banben.png">
							</span>
							<div class="demonstration_menu_item" style="white-space: nowrap;">工单-送货-对账-收款</div>
						</div>
						<div class="demonstration_list" data-type="2">
							<span class="offen_menu_index_to"> <img class="demonstration_menu_img" src="${ctxHYUI}/images/welcome_banben.png">
							</span>
							<div class="demonstration_menu_item" style="white-space: nowrap;">订单-工单-送货-对账-收款</div>
						</div>
						<div class="demonstration_list" data-type="3">
							<span class="offen_menu_index_to"> <img class="demonstration_menu_img" src="${ctxHYUI}/images/welcome_banben.png">
							</span>
							<div class="demonstration_menu_item" style="white-space: nowrap;">销售-生产-采购-财务</div>
						</div>
						<div class="demonstration_list" data-type="4">
							<span class="offen_menu_index_to"> <img class="demonstration_menu_img" src="${ctxHYUI}/images/welcome_banben.png">
							</span>
							<div class="demonstration_menu_item" style="white-space: nowrap;">销售-生产-发外-财务</div>
						</div>
						<div class="demonstration_list" data-type="5">
							<span class="offen_menu_index_to"> <img class="demonstration_menu_img" src="${ctxHYUI}/images/welcome_banben.png">
							</span>
							<div class="demonstration_menu_item" style="white-space: nowrap;">销售-生产-采购-发外-财务</div>
						</div>
						<div class="demonstration_list" data-type="6">
							<span class="offen_menu_index_to"> <img class="demonstration_menu_img" src="${ctxHYUI}/images/welcome_banben.png">
							</span>
							<div class="demonstration_menu_item" style="white-space: nowrap;">销售-生产-采购-库存-财务</div>
						</div>
						<div class="demonstration_list" data-type="7">
							<span class="offen_menu_index_to"> <img class="demonstration_menu_img" src="${ctxHYUI}/images/welcome_banben.png">
							</span>
							<div class="demonstration_menu_item" style="white-space: nowrap;">销售-生产-采购-发外-库存-财务</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="hy_right">
			<div class="memorandum_head">
				<i class="icon icon-plus add_memorandum_i"></i>
			</div>
			<div id="my-calendar" class="my-calendar" style="height:225px"></div>
			<div class="adv_con">
				<div class="memorandum_head">热门推荐</div>
				<div class="adv_list_div">
					<ul class="adv_ul">
					</ul>
				</div>
			</div>
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
	<script type="text/javascript" src="${ctxHYUI}/plugins/jquery.cookie/jquery.cookie.js?v=${v }"></script>
	<script type="text/javascript" src="${ctxHYUI}/plugins/zabuto-calendar/zabuto_calendar.min.js?v=${v }"></script>
	<script type="text/javascript" src="${ctxHYUI}/src/public.js?v=${v }"></script>
	<script type="text/javascript" src="${ctxHYUI}/src/public.ext.js?v=${v }"></script>
	<script type="text/javascript" src="${ctxHYUI}/src/welcome.js?v=${v }"></script>
</c:if>
<c:if test="${mode == 'pro' }">
	<script type="text/javascript" src="${ctxHYUI}/js/hyui.welcome.js?v=${v }"></script>
</c:if>
<script type="text/javascript" src="${ctxHYUI}/plugins/layer/layer.js"></script>
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
	/* 处理键盘事件 */
	function doKey(e)
	{
		var ev = e || window.event; //获取event对象   
		var obj = ev.target || ev.srcElement; //获取事件源   
		var t = obj.type || obj.getAttribute('type'); //获取事件源类型   
		if (ev.keyCode == 8 && t != "password" && t != "text" && t != "textarea")
		{
			return false;
		}
	}
	// 禁止后退键 作用于Firefox、Opera   
	document.onkeypress = doKey;
	// 禁止后退键  作用于IE、Chrome   
	document.onkeydown = doKey;
</script>
</html>

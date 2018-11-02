<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic }/site/transmit.js?v=${v }"></script>
<title>订单转送货</title>
<script type="text/javascript" src="${ctxStatic }/site/sale/transmit/from_offer.js?v=${v }"></script>
</head>
<body>
	<form id="transmitForm"></form>
	<div class="page-container">
		<div class="page-border">
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="销售管理-转单功能-报价转订单"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="sale:order:create">
						<button class="nav_btn table_nav_btn" id="btn_transmit">生成销售订单</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="sale:offer:complete">
						<button class="nav_btn table_nav_btn" id="btn_complete">强制完工</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="sale:offer:complete:cancel">
						<button class="nav_btn table_nav_btn" id="btn_complete_cancel" style="display: none;">取消强制完工</button>
					</shiro:hasPermission>
				</div>
				<div id="outerdiv" style="position: fixed; top: 0; left: 0; background: rgba(0, 0, 0, 0.7); z-index: 100000; width: 100%; height: 100%; display: none;">
					<div id="innerdiv" style="position: absolute;">
						<img id="bigimg" style="border: 5px solid #fff;" src="" />
					</div>
				</div>

			</div>
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">
						<dd class="row-dd">
							<sys:dateConfine label="报价日期" />
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">客户名称：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_3s" name="customerName" id="customerName" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">产品名称：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_3s" name="name" id="name" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">报价单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_3s" name="offerNo" id="offerNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<button type="button" class="nav_btn table_nav_btn search_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查询
							</button>
						</dd>
					</dl>
				</div>
				<div>
					<div class="radio-box">
						<label>
							<input type="radio" value="YES" name="completeFlag" />
							已强制完工
						</label>
						<label>
							<input type="radio" value="NO" name="completeFlag" checked="checked" />
							未强制完工
						</label>
					</div>
				</div>
			</div>
			<!--表格Start-->
			<div class="boot-mar">
				<table class="border-table resizable" id="bootTable">
				</table>
			</div>
			<!--表格End-->
		</div>
	</div>
</body>
</html>
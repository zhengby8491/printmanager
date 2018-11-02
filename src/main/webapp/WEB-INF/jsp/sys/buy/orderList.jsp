<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>购买记录</title>
<script type="text/javascript" src="${ctxStatic}/site/sys/buy/orderList.js?${v}"></script>
</head>
<body>
	<input type="hidden" value="${ctx}" id="ctx">
	<div class="page-container">
		<div class="page-border">
			<!--搜索栏-->
			<div class="">
				<div class="cl order-info" style="width: 1140px">
					<dl class="cl row-dl">
						<dd class="row-dd">
							<sys:dateConfine label="购买时间" />
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_8">是否支付：</label>
							<span class="ui-combo-wrap form_text">
								<select name="isPay" id="isPay" class="input-txt input-txt_3">
									<option value="">全部</option>
									<option value="1">是</option>
									<option value="0">否</option>
								</select>
							</span>

						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">邀请人：</label>
							<span class="ui-combo-wrap">
								<input type="text" class="input-txt input-txt_3" id="inviter" name="inviter" />

							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_8">邀请人电话：</label>
							<span class="ui-combo-wrap">
								<input type="text" class="input-txt input-txt_24" id="inviterPhone" name="inviterPhone" />
							</span>
						</dd>
						<dd class="row-dd">
							<button class="nav_btn table_nav_btn search_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查询
							</button>
							<button class="nav_btn table_nav_btn more_show" id="btn_more">更多</button>
						</dd>
					</dl>
					<dl class="cl hide_container" style="display: none;" id="more_div">
						<dd class="row-dd">
							<label class="form-label label_ui ">公司名称：</label>
							<span class="ui-combo-wrap">
								<input type="text" class="input-txt input-txt_13" id="companyName" name="companyName" />
							</span>
						</dd>

						<dd class="row-dd">
							<label class="form-label label_ui label_8">发票信息：</label>
							<span class="ui-combo-wrap form_text">
								<select name="invoiceInfor" id="invoiceInfor" class="input-txt input-txt_3">
									<option value="">全部</option>
									<option value="0">不需要发票</option>
									<option value="1">增值税发票6%</option>
									<option value="2">增值税发票17%</option>
								</select>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">联系人：</label>
							<span class="ui-combo-wrap">
								<input type="text" class="input-txt input-txt_3" id="linkMan" name="linkMan" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_8">联系电话：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_24" name="telephone" id="telephone" />
							</span>

						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">订单编号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_13" name="billNo" id="billNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_8">产品名称：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_3" name="productName" id="productName" />
							</span>

						</dd>


						<dd class="row-dd">
							<label class="form-label label_ui">用户名：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_3" name="userName" id="userName" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_8">订单状态：</label>
							<span class="ui-combo-wrap form_text" style="width: 65px;">
								<select name="orderState" id="orderState" style="border-radius: 3px;">
									<option value="">全部</option>
									<option value="1">待支付</option>
									<option value="2">已完成</option>
									<option value="0">已取消</option>
								</select>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui" style="width: 67px;">订单类型：</label>
							<span class="ui-combo-wrap form_text" style="width: 77px">
								<select name="orderType" id="orderType" style="border-radius: 3px;">
									<option value="">全部</option>
									<option value="1">在线订单</option>
									<option value="2">线下订单</option>
								</select>
							</span>
						</dd>
					</dl>
				</div>
			</div>
			<!--按钮栏-->
			<div class="btn-bar">
				<shiro:hasPermission name="sys:buyOrder:create">
					<span>
						<a href="javascript:;" onclick="orderCreate()" class="nav_btn table_nav_btn">
							<i class="fa fa-plus-square"></i>
							新增购买信息
						</a>
					</span>
				</shiro:hasPermission>
			</div>
			<!--表格-->
			<div class="boot-mar">
				<table class="border-table resizable" id="bootTable">
				</table>
			</div>
		</div>
</body>
</html>
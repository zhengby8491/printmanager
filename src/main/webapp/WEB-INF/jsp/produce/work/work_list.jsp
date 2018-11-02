<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/produce/work/work_list.js?v=${v }"></script>

<title>生产工单列表</title>
<style type="text/css">
</style>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl topPath"></div>
			<!--头部END-->
			<!--查询表单START-->
			<!--查询表单START-->
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">
						<dd class="row-dd">
							<sys:dateConfine label="制单日期" />
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_6">生产单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_3" name="billNo" id="billNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">客户名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_21	" id="customerName" name="customerName" />
								<div class="select-btn" id="customer_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">成品名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_3" id="productName" name="productName" />
								<div class="select-btn" id="product_quick_select">...</div>
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
							<label class="form-label label_ui">交货日期：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'deliverDateMax\')}'})" 
									class="input-txt input-txt_0 Wdate" id="deliverDateMin" name="deliverDateMin" value="<fmt:formatDate value="${deliverDateMin }" type="date" pattern="yyyy-MM-dd" />" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_2 " style="text-align: left; margin-left: -2px; width: 18px">至</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'deliverDateMin\')}'})"
									class="input-txt input-txt_0 Wdate" id="deliverDateMax" name="deliverDateMax" value="<fmt:formatDate value="${deliverDateMax }" type="date" pattern="yyyy-MM-dd" />" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_6">销售单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_3" name="saleBillNo" id="saleBillNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui ">客户料号：</label>
							<span class="ui-combo-wrap form_text wrap-width">
								<input type="text" class="input-txt input-txt_21" name="customerMaterialCode" id="customerMaterialCode" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">产品规格：</label>
							<span class="ui-combo-wrap">
								<input type="text" class="input-txt input-txt_21" id="style" name="style" />
							</span>
						</dd>
					</dl>
					<dl class="cl hide_container" style="display: none;" id="more_div2">
						<dd class="row-dd">
							<label class="form-label label_ui">客户单号：</label>
							<span class="ui-combo-wrap">
								<input type="text" class="input-txt input-txt_13" id="customerBillNo" name="customerBillNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_6">整单发外：</label>
							<span class="ui-combo-wrap">
								<phtml:list defaultOption="全部" defaultValue="-1" name="isOutSource" textProperty="text" cssClass="input-txt input-txt_3 hy_select2" type="com.huayin.printmanager.persist.enumerate.BoolValue"></phtml:list>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">印刷类型：</label>
							<span class="ui-combo-wrap">
								<phtml:list defaultOption="全部" defaultValue="-1" name="productType" textProperty="text" cssClass="input-txt input-txt_30 hy_select2" type="com.huayin.printmanager.persist.enumerate.ProductType"></phtml:list>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">制&nbsp;&nbsp;单&nbsp;&nbsp;人：</label>
							<span class="ui-combo-wrap">
								<input type="text" class="input-txt input-txt_21" id="createName" name="createName" />
							</span>
						</dd>
					</dl>
				</div>
				<div>
					<div class="radio-box">
						<c:if test="${auditflag!='true' }">
							<label>
								<input type="radio" value="-1" name="auditFlag" checked="checked" />
								全部
							</label>
							<label>
								<input type="radio" value="YES" name="auditFlag" />
								已审核
							</label>
							<label>
								<input type="radio" value="NO" name="auditFlag" />
								未审核
							</label>
						</c:if>
						<c:if test="${auditflag=='true' }">
							<label>
								<input type="radio" value="-1" name="auditFlag" />
								全部
							</label>
							<label>
								<input type="radio" value="YES" name="auditFlag" />
								已审核
							</label>
							<label>
								<input type="radio" value="NO" name="auditFlag" checked="checked" />
								未审核
							</label>
						</c:if>
					</div>
				</div>
			</div>
			<!-- 按钮栏Start -->
			<div class="btn-bar" style="margin-bottom: 0">
				<shiro:hasPermission name="produce:work:create">
					<span>
						<a href="javascript:;" class="nav_btn table_nav_btn" onclick="work_create()">
							<i class="fa fa-plus-square"></i>
							新增生产工单
						</a>
					</span>
					<span>
						<a href="javascript:;" class="nav_btn table_nav_btn" onclick="work_create('ROTARY')">
							<i class="fa fa-plus-square"></i>
							新增轮转工单
						</a>
					</span>
				</shiro:hasPermission>
			</div>
			<!--按钮栏End-->
			<!--查询表单END-->
			<!--表格部分START-->
			<div>
				<div class="boot-mar">
					<table class="border-table" id="bootTable">
					</table>
				</div>
			</div>
			<!--表格部分END-->
		</div>
	</div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>代理商</title>
<script type="text/javascript" src="${ctxStatic}/site/sys/agentquotient/list.js?${v}"></script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--顶部导航-->
			<div class="cl topPath"></div>
			<!--搜索栏-->
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">
						<%-- <dd class="row-dd">
							<sys:dateConfine label="发布日期" dateMin="${queryParam.dateMin}"
								dateMax="${queryParam.dateMax}" initDate="false" />
						</dd> --%>

						<%-- <dd class="row-dd">
							<label class="form-label label_ui label_1">是否发布：</label> <span
								class="ui-combo-wrap"> <phtml:list
									cssClass="input-txt input-txt_1 hy_select2"
									defaultOption="全部" textProperty="text" defaultValue="-1"
									type="com.huayin.printmanager.persist.enumerate.BoolValue"
									name="publish"></phtml:list>
							</span>
						</dd> --%>
						<dd class="row-dd">
							<label class="form-label label_ui">材料商名称：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_9" name="name" id="name" value="${queryParam.name}" />
							</span>
						</dd>
						<dd class="row-dd">
							<button type="button" class="nav_btn table_nav_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查询
							</button>
						</dd>
					</dl>
				</div>
				<!--表格-->
				<!-- 按钮栏Start -->
				<div class="btn-bar" style="margin-bottom: 0">
					<shiro:hasPermission name="sys:agentquotient:create">
						<span>
							<a href="javascript:;" class="nav_btn table_nav_btn" onclick="create()">
								<i class="fa fa-plus-square"></i>
								新增材料商
							</a>
						</span>
					</shiro:hasPermission>

				</div>
				<div class="boot-mar">
					<table class="border-table" id="bootTable">
					</table>
				</div>
			</div>
</body>
</html>
﻿<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/basic/department/list.js?v=${v }"></script>
<title><fmt:message key="i18n.systemlog.basic.department.label"/></title>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--搜索栏-->
			<div class="search_container">
				<input type="text" class="input-txt" style="width: 250px; height: 27px" value="${departmentName}" placeholder="<fmt:message key="i18n.jsp.common.table.department.name"/>" id="departmentName" name="departmentName">
				<button type="button" class="nav_btn table_nav_btn search_btn" id="btn_search">
					<i class="fa fa-search"></i>
					<fmt:message key="i18n.jsp.common.table.button.query"/>
				</button>
			</div>
			<!--/搜索栏-->

			<!--表格-->
			<div>
				<!--按钮组-->
				<div class="btn-bar">
					<shiro:hasPermission name="basic:department:create">
						<span>
							<a href="javascript:;" onclick="depart_create()" class="nav_btn table_nav_btn">
								<i class="fa fa-plus-square"></i>
								<fmt:message key="i18n.jsp.common.table.department.add"/>
							</a>
						</span>
					</shiro:hasPermission>
				</div>

				<!-- 表格内容 -->
				<div class="boot-mar">
					<table class="border-table resizable" id="bootTable">
					</table>
				</div>
			</div>
			<!--/表格-->
		</div>
	</div>

	<!-- jsp国际化 -->
	<%@include file="/WEB-INF/jsp/common/i18n_common.jsp"%>
</body>
</html>
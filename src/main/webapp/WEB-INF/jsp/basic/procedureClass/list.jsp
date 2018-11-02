<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/basic/procedureClass/list.js?v=${v }"></script>
<title>工序分类</title>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--搜索栏-->
			<div class="search_container">
				<%-- 				<phtml:list name="procedureType" textProperty="text" cssClass="select_prc" type="com.huayin.printmanager.persist.enumerate.ProcedureType" selected="${ procedureType}" /> --%>
				<input type="text" class="input-txt" style="width: 250px; height: 27px" value="${procedureClassName}" placeholder="输入工序分类名称" id="procedureClassName" name="procedureClassName">
				<button type="button" class="nav_btn table_nav_btn" id="btn_search">
					<i class="fa fa-search"></i>
					查询
				</button>
			</div>
			<!--按钮栏-->
			<div class="btn-bar">
				<shiro:hasPermission name="basic:procedureClass:create">
					<span>
						<a href="javascript:;" onclick="procedureClass_add()" class="nav_btn table_nav_btn">
							<i class="fa fa-plus-square"></i>
							添加工序分类
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
	</div>
</body>
</html>
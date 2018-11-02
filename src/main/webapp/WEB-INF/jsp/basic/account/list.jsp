<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/basic/account/list.js?v=${v }"></script>
<title>账户信息</title>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--搜索栏-->
			<%-- 			<form id="searchForm" action="${ctx}/basic/account/list" method="get"> --%>
			<div class="search_container">
				<input type="text" class="input-txt" style="width: 250px; height: 27px" placeholder="输入公司账户" id="bankNo" name="bankNo">
				<button type="button" class="nav_btn table_nav_btn search_btn" id="btn_search">
					<i class="fa fa-search"></i>
					查询
				</button>
			</div>
			<!-- 			</form> -->
			<!--表格-->
			<div>
				<!--按钮组-->
				<div class="btn-bar">
					<shiro:hasPermission name="basic:account:create">
						<span>
							<a href="javascript:;" onclick="account_create()" class="nav_btn table_nav_btn">
								<i class="fa fa-plus-square"></i>
								添加账户
							</a>
						</span>
					</shiro:hasPermission>
				</div>
				<!--按钮组-->

				<!-- 表格 -->
				<div class="boot-mar">
					<table class="border-table resizable" id="bootTable">
					</table>
				</div>
				<!-- 表格 -->
			</div>
			<div class="pagination_container">${page}</div>
		</div>
	</div>
</body>
</html>
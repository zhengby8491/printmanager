<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/sys/role/list.js?v=${v }"></script>
<title>所有公司角色列表</title>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--顶部导航-->
			<div class="cl topPath"></div>
			<form:form id="searchForm" action="${ctx}/sys/role/allList" method="post" class="search_bar">
				<input id="pageNo" name="pageNo" type="hidden" />
				<input id="pageSize" name="pageSize" type="hidden" />
				<div class="search_container">
					<input type="text" class="input-txt" style="width: 250px; height: 27px" value="${companyId }" placeholder="输入公司ID" id="companyId" name="companyId">
					<input type="text" class="input-txt" style="width: 250px; height: 27px" value="${companyName }" placeholder="输入公司名" id="companyName" name="companyName">
					<input type="text" class="input-txt" style="width: 250px; height: 27px" value="${name }" placeholder="输入角色名" id="name" name="name">
					<button type="submit" class="nav_btn table_nav_btn" id="btn_search" name="btn_search">
						<i class="fa fa-search"></i>
						查询
					</button>
				</div>
			</form:form>

			<!--表格-->
			<div style="clear: both">
				<div class="table-container table-hy">
					<table class="border-table">
						<thead>
							<tr>
								<th>公司ID</th>
								<th>公司名称</th>
								<th>角色名称</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="r" items="${page.list }">
								<tr>
									<td>${r.company.id }</td>
									<td>${r.company.name }</td>
									<td>${r.role.name }</td>
									<td class="td-manage">
										<shiro:hasPermission name="sys:role:edit">
											<a title="编辑" href="javascript:;" onclick="role_edit(${r.role.id},'high_role')" style="margin-right: 50px">
												<i class="fa fa-pencil"></i>
											</a>
										</shiro:hasPermission>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>

			<div class="pagination_container">${page}</div>
		</div>
	</div>
</body>
</html>
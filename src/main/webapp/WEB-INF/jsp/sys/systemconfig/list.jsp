<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/sys/systemconfig/list.js?v=${v}"></script>
<title>系统参数列表</title>

</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--顶部导航-->
			<div class="cl topPath"></div>
			<!--搜索栏-->
			<form:form id="searchForm" action="${ctx}/sys/systemconfig/list" method="post" class="search_bar">
				<input id="pageNo" name="pageNo" type="hidden" />
				<input id="pageSize" name="pageSize" type="hidden" />
				<div class="search_container">
					<input type="text" class="input-txt" style="width: 250px; height: 27px" value="${systemConfigId}" placeholder="输入参数标识" id="systemConfigId" name="systemConfigId">
					<button type="submit" class="nav_btn table_nav_btn" id="btn_search" name="btn_search">
						<i class="fa fa-search"></i>
						查找
					</button>
				</div>
			</form:form>
			<!--表格-->
			<div>
				<!--按钮组-->
				<shiro:hasPermission name="sys:systemconfig:create">
					<div class="btn-bar">
						<span>
							<a href="javascript:;" id="btn_systemconfig_create" class="nav_btn table_nav_btn">
								<i class="fa fa-plus-square"></i>
								添加
							</a>
						</span>
					</div>
				</shiro:hasPermission>
				<div class="table-container table-hy" style="max-height: 100%;">
					<table class="border-table">
						<thead>
							<tr>
								<th width="150" align="left">标识</th>
								<th width="150">值</th>
								<th width="200">描述</th>
								<th width="140">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="systemConfig" items="${page.list }">
								<tr onDblClick="systemConfig_edit('${systemConfig.id}')">
									<td align="left">${systemConfig.id }</td>
									<td>
										<div style="width: 720px; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;">${systemConfig.value }</div>
									</td>
									<td>${systemConfig.description }</td>
									<td class="td-manage">
										<shiro:hasPermission name="sys:systemconfig:edit">
											<a title="编辑" href="javascript:;" onclick="systemConfig_edit('${systemConfig.id}')" style="margin-right: 10px;">
												<i class="fa fa-pencil"></i>
											</a>

										</shiro:hasPermission>
										<shiro:hasPermission name="sys:systemconfig:del">
											<a title="删除" href="javascript:;" style="margin-right: 10px;" onclick="systemConfig_del(this,'${systemConfig.id}')">
												<i class="fa fa-trash-o"></i>
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
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/sys/role/list.js?v=${v }"></script>
<title>角色列表</title>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--顶部导航-->
			<div class="cl topPath"></div>
			<!--表格-->
			<div style="clear: both">
				<!--按钮栏-->
				<div class="btn-bar">
					<shiro:hasPermission name="sys:role:create">
						<span>
							<a href="javascript:;" id="btn_role_create" class="nav_btn table_nav_btn">
								<i class="fa fa-plus-square"></i>
								添加角色
							</a>
						</span>
					</shiro:hasPermission>
				</div>
				<!-- 				<h1 class="font-bigcenter">角色管理列表</h1> -->
				<div class="table-container table-hy">
					<table class="border-table">
						<thead>
							<tr>
								<th>名称</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="r" items="${list }">
								<tr id="${r.id }">
									<td>${r.name }</td>
									<td class="td-manage">
										<shiro:hasPermission name="sys:role:edit">
											<a title="编辑" href="javascript:;" onclick="role_edit(${r.id},'role')" style="margin-right: 50px">
												<i class="fa fa-pencil"></i>
											</a>
										</shiro:hasPermission>
										<shiro:hasPermission name="sys:role:del">
											<a title="删除" href="javascript:;" onclick="role_del(this,${r.id})">
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
		</div>
	</div>
</body>
</html>
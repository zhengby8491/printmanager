<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/sys/user/list.js?v=${v}"></script>
<title>管理员列表</title>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--顶部导航-->
			<div class="cl topPath"></div>
			<!--搜索栏-->
			<form:form id="searchForm" action="${ctx}/sys/user/list" method="post" class="search_bar">
				<input id="pageNo" name="pageNo" type="hidden" />
				<input id="pageSize" name="pageSize" type="hidden" />
				<div class="search_container">
					<input type="text" class="input-txt" style="width: 250px; height: 27px" value="${userName }" placeholder="输入用户名" id="userName" name="userName">
					<button type="submit" class="nav_btn table_nav_btn" id="btn_search" name="btn_search">
						<i class="fa fa-search"></i>
						查询
					</button>
				</div>
			</form:form>

			<!--表格-->
			<div>
				<!--按钮组-->
				<shiro:hasPermission name="sys:user:create">
					<div class="btn-bar">
						<span>
							<a href="javascript:;" id="btn_user_add" class="nav_btn table_nav_btn">
								<i class="fa fa-plus-square"></i>
								添加用户
							</a>
						</span>
					</div>
				</shiro:hasPermission>
				<div class="table-container table-hy">
					<table class="border-table">
						<thead>
							<tr>
								<th width="150">登录名</th>
								<th width="80">员工</th>
								<th width="90">手机</th>
								<th width="150">邮箱</th>
								<th width="130">创建时间</th>
								<th width="130">最近登录时间</th>
								<th width="100">用户状态</th>
								<th width="140">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="user" items="${page.list }">
								<tr onDblClick="user_edit(${user.id})">
									<td>${user.userName }</td>
									<td>${user.realName }</td>
									<td>${user.mobile }</td>
									<td>${user.email }</td>
									<td>
										<fmt:formatDate value="${user.createTime}" type="date" pattern="yyyy-MM-dd" />
									</td>
									<td>
										<c:if test="${not empty user.lastLoginTime }">
											<fmt:formatDate value="${user.lastLoginTime}" type="date" pattern="yyyy-MM-dd" />
										</c:if>
									</td>
									<td class="td-status">
										<span class="label <c:if test="${user.state eq 'NORMAL'}">label-success radius</c:if><c:if test="${user.state eq 'CLOSED'}">label-closed radius</c:if>">${user.state.text }</span>
									</td>
									<td class="td-manage">
										<shiro:hasPermission name="sys:user:edit">
											<a title="编辑" href="javascript:;" onclick="user_edit(${user.id})" style="margin-right: 10px;">
												<i class="fa fa-pencil"></i>
											</a>

										</shiro:hasPermission>
										<shiro:hasPermission name="sys:user:state">
											<c:if test="${user.state eq 'NORMAL'}">
												<a title="停用" href="javascript:;" onClick="user_stop(this,${user.id})" style="margin-right: 10px;">
													<i class="fa fa-minus-square-o"></i>
												</a>
											</c:if>

											<c:if test="${user.state eq 'CLOSED'}">
												<a title="正常" href="javascript:;" onClick="user_start(this,${user.id})" style="margin-right: 10px;">
													<i class="fa fa-check-square-o"></i>
												</a>
											</c:if>
										</shiro:hasPermission>
										<shiro:hasPermission name="sys:user:resetpwd">
											<a title="重置密码" class="reset_password" href="javascript:;" onclick="user_prompt(${user.id})">
												<i class="fa fa-key"></i>
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
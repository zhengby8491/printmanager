<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<%-- <link
	href="${ctxHYUI}/plugins/treeTable/themes/vsStyle/treeTable.min.css"
	rel="stylesheet" type="text/css" />
<script src="${ctxHYUI}/plugins/treeTable/jquery.treeTable.min.js?v=${v }"
	type="text/javascript"></script>
 --%>
<link href="${ctxHYUI}/plugins/treeTable/3.2/css/jquery.treetable.css" rel="stylesheet" type="text/css" />
<link href="${ctxHYUI}/plugins/treeTable/3.2/css/jquery.treetable.theme.default.css" rel="stylesheet" type="text/css" />
<script src="${ctxHYUI}/plugins/treeTable/3.2/jquery.treetable.js?v=${v }" type="text/javascript"></script>

<script type="text/javascript" src="${ctxStatic}/site/sys/menu/list.js?v=${v }"></script>
<title>菜单列表</title>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--顶部导航-->
			<div class="iframe-top">
				<sys:nav struct="系统管理-权限管理-菜单管理"></sys:nav>
			</div>
			<div class="search_container">
				<form id="searchForm" action="${ctx}/sys/menu/list" method="post" class="search_bar">
					<input type="text" class="input-txt" style="width: 250px" value="${name }" placeholder="输入菜单名" id="name" name="name">
					<button type="submit" class="nav_btn table_nav_btn" id="btn_search" name="btn_search">
						<i class="fa fa-search"></i>
						查询
					</button>
					<label style="cursor: pointer; vertical-align: sub">
						<input type="checkbox" id="isShowFunction" name="isShowFunction" ${isShowFunction?'checked':''} value="${isShowFunction}" />
						显示按钮菜单
					</label>
				</form>
			</div>
			<!--按钮栏-->
			<div class="btn-bar">
				<span class="l">
					<a href="javascript:;" id="btn_menu_create" class="nav_btn table_nav_btn">
						<i class="fa fa-plus-square"></i>
						添加菜单
					</a>
				</span>
				<span class="r">
					<a href="javascript:;" onclick="$('#menuTable').treetable('expandAll'); return false;" class="nav_btn table_nav_btn">展开</a>
					<a href="javascript:;" onclick="$('#menuTable').treetable('collapseAll'); return false;" class="nav_btn table_nav_btn">收起</a>
				</span>
			</div>
			<!--表格-->
			<div style="clear: both; margin-top: 40px;">
				<!-- 				<h1 class="font-bigcenter">菜单列表</h1> -->
				<div class="table-container table-hy" style="margin-bottom: 10px; height: auto; max-height: none">
					<table class="border-table" id="menuTable" coldrag="false" style="margin: 0; height: auto;">
						<thead>
							<tr>
								<th width="140">ID</th>
								<th width="80">名称</th>
								<th width="140">链接</th>
								<th width="60">类型</th>
								<th width="100">是否立即刷新</th>
								<th width="100">是否基础版本</th>
								<th width="60">排序</th>
								<th width="100">权限标识</th>
								<th width="120">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="m" items="${list }">
								<c:if test="${m.type=='MENU'||isShowFunction }">
									<tr class="text-c" data-tt-id="${m.id }" data-tt-parent-id="${m.parentId==-1?'':m.parentId}">
										<td>${m.id }</td>
										<td>${m.name }</td>
										<td>${m.url }</td>
										<td>${m.type.text }</td>
										<td>${m.refresh.text }</td>
										<td>${m.isBase.text }</td>
										<td>${m.sort}</td>
										<td>${m.identifier }</td>
										<td class="td-manage">
											<shiro:hasPermission name="sys:menu:edit">
												<a title="编辑" href="javascript:;" onclick="menu_edit(${m.id})" style="margin-right: 8px">
													<i class="fa fa-pencil"></i>
												</a>
											</shiro:hasPermission>
											<shiro:hasPermission name="sys:menu:create">
												<a title="添加" href="javascript:;" onclick="menu_add(${m.id})" style="margin-right: 8px">
													<i class="fa fa-plus"></i>
												</a>
											</shiro:hasPermission>
											<shiro:hasPermission name="sys:menu:del">
												<a title="删除" href="javascript:;" onclick="menu_del(this,${m.id})">
													<i class="fa fa-trash-o"></i>
												</a>
											</shiro:hasPermission>
										</td>
									</tr>
								</c:if>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
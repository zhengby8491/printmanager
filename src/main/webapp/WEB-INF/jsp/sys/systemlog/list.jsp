<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>系统日志列表</title>
<script type="text/javascript" src="${ctxStatic}/site/sys/systemlog/list.js?v=${v }"></script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--顶部导航-->
			<div class="cl topPath"></div>
			<!--搜索栏-->
			<form:form id="searchForm" action="${ctx}/sys/systemlog/list" method="post" class="search_bar">
				<input id="pageNo" name="pageNo" type="hidden" />
				<input id="pageSize" name="pageSize" type="hidden" />
				<div class="cl">
					<div class="cl order-info">
						<dl class="cl row-dl">
							<dd class="row-dd">
								<sys:dateConfine label="创建日期" />
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui">类型：</label>
								<span class="ui-combo-wrap form_text">
									<phtml:list name="type" defaultValue="" selected="${type}" textProperty="text" cssClass="input-txt input-txt_3 hy_select2" type="com.huayin.printmanager.persist.enumerate.SystemLogType"></phtml:list>
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">公司名称：</label>
								<span class="ui-combo-wrap form_text">
									<input name="companyName" type="text" class="input-txt input-txt_3" value="${companyName }" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">用户名：</label>
								<span class="ui-combo-wrap form_text">
									<input name="userName" type="text" class="input-txt input-txt_3" value="${userName }" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">IP：</label>
								<span class="ui-combo-wrap form_text">
									<input name="ip" type="text" class="input-txt input-txt_3" value="${ip }" />
								</span>
							</dd>
							<dd class="row-dd">
								<button type="submit" class="nav_btn table_nav_btn" id="btn_search" name="btn_search">
									<i class="fa fa-search"></i>
									查询
								</button>

							</dd>
						</dl>
					</div>
				</div>
			</form:form>
			<!--表格-->
			<div>
				<div class="table-container">
					<table class="border-table">
						<thead>
							<tr>
								<th width="50">执行日期</th>
								<th width="30">类型</th>
								<th width="50">用户名称</th>
								<th width="50">员工姓名</th>
								<th width="20">操作模块</th>
								<th width="60">操作结果</th>
								<th width="50">设备</th>
								<th width="50">系统</th>
								<th width="50">浏览器</th>
								<th width="50">IP</th>
								<th width="80">公司名称</th>

							</tr>
						</thead>
						<tbody>
							<c:forEach var="systemLog" items="${page.list }">
								<tr>
									<td>
										<fmt:formatDate value="${systemLog.execTime }" type="date" pattern="yyyy-MM-dd HH:mm" />
									</td>
									<td>${systemLog.type.text}</td>
									<td>${systemLog.user.userName}</td>
									<td>${systemLog.employeeName}</td>
									<td>${systemLog.module}</td>
									<td>${systemLog.operationResult}</td>
									<td>${systemLog.deviceType}</td>
									<td>${systemLog.userAgent}</td>
									<td>${systemLog.browser}</td>
									<td>${systemLog.operatorIp}</td>
									<td>${systemLog.company.name}</td>
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
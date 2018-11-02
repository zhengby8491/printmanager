<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/sys/smslog/list.js?v=${v }"></script>
<title>短信日志列表</title>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--顶部导航-->
			<div class="cl topPath"></div>
			<!--搜索栏-->
			<form:form id="searchForm" action="${ctx}/sys/smslog/list" method="post" class="search_bar">
				<input id="pageNo" name="pageNo" type="hidden" />
				<input id="pageSize" name="pageSize" type="hidden" />
				<div class="cl">
					<div class="cl order-info">
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui">手机：</label>
								<span class="ui-combo-wrap form_text">
									<input type="text" class="input-txt input-txt_3" name="mobile" id="mobile" value="${mobile}" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui">日志内容：</label>
								<span class="ui-combo-wrap form_text">
									<input type="text" class="input-txt input-txt_3" name="content" id="content" value="${content}" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui">网关类型：</label>
								<span class="ui-combo-wrap form_text">
									<phtml:list name="smsSendType" defaultValue="" selected="${smsSendType}" textProperty="text" cssClass="input-txt input-txt_3 hy_select2" type="com.huayin.printmanager.persist.enumerate.SmsSendType"></phtml:list>
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui">内容类型：</label>
								<span class="ui-combo-wrap form_text">
									<phtml:list name="type" textProperty="text" defaultValue="" selected="${type}" cssClass="input-txt input-txt_3 hy_select2" type="com.huayin.printmanager.persist.enumerate.SmsType"></phtml:list>
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">状态：</label>
								<span class="ui-combo-wrap wrap-width">
									<phtml:list name="state" textProperty="text" defaultValue="" selected="${state}" cssClass="input-txt input-txt_3 hy_select2" type="com.huayin.printmanager.persist.enumerate.SmsLogState"></phtml:list>
								</span>
							</dd>
							<dd class="row-dd">
								<button type="submit" class="nav_btn table_nav_btn" id="btn_search" name="btn_search">
									<i class="fa fa-search"></i>
									搜日志
								</button>

							</dd>
						</dl>
					</div>
				</div>
			</form:form>
			<!--表格-->
			<div>
				<div class="table-container table-hy">
					<table class="border-table">
						<thead>
							<tr>
								<th width="90">手机</th>
								<th width="80">网关类型</th>
								<th width="80">内容类型</th>
								<th width="150">日志内容</th>
								<th width="80">定时发送时间</th>
								<th width="80">发送优先级</th>
								<th width="80">发送类型</th>
								<th width="80">发送时间</th>
								<th width="80">最迟发送时间</th>
								<th width="60">状态</th>
								<th width="80">成功时间</th>
								<th width="40">重发次数</th>
								<th width="80">失败原因</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="smsLog" items="${page.list }">
								<tr>
									<td>${smsLog.mobile }</td>
									<td>${smsLog.smsSendType.text}</td>
									<td>${smsLog.type.text}</td>
									<td onmouseover="this.title=this.innerHTML">${smsLog.content}</td>
									<td>
										<fmt:formatDate value="${smsLog.scheduledTime }" type="date" pattern="yyyy-MM-dd HH:mm" />
									</td>
									<td>${smsLog.sendPriority}</td>
									<td>${smsLog.sendState.text}</td>
									<td>
										<fmt:formatDate value="${smsLog.sendTime }" type="date" pattern="yyyy-MM-dd HH:mm" />
									</td>
									<td>
										<fmt:formatDate value="${smsLog.expireTime }" type="date" pattern="yyyy-MM-dd HH:mm" />
									</td>
									<td>${smsLog.state.text}</td>
									<td>
										<fmt:formatDate value="${smsLog.successTime }" type="date" pattern="yyyy-MM-dd HH:mm" />
									</td>
									<td>${smsLog.retryCount}</td>
									<td>${smsLog.message}</td>
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
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/sys/role/list.js?v=${v }"></script>
<title>在线用户</title>
<style type="text/css">
.table-container {
	max-height: inherit;
}

.online-table tbody tr.active td {
	cursor: pointer
}

.userInfo {
	display: none;
	padding: 20px;
	width: 340px;
	min-height: 50px;
	max-height: 500px;
	background-color: #f7f7f7
}

.userInfo>div {
	margin: 4px 0
}

.userInfo label {
	display: inline-block;
	width: 80px;
	text-align: right;
	vertical-align: top;
}

.userInfo span {
	display: inline-block;
	width: 260px;
	color: #555
}
</style>
<script type="text/javascript" src="${ctxStatic}/site/sys/analysis/online.js?${v}"></script>
</head>
<body>
	<c:set var="currTime" value="<%=new java.util.Date().getTime()%>"></c:set>
	<c:set var="onlineUsers" value="${fns:onlineUsers()}"></c:set>
	<div class="page-container">
		<div class="page-border">
			<!--顶部导航-->
			<div class="cl topPath"></div>
			<!--表格-->
			<div>
				<%-- 				<c:if test="${loginUser.company.id eq '1'}"> --%>
				<div align="center">
					<span style="font-size: 25px">
						<span id="remain_time"></span>
						刷新
					</span>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<span id="currTime" style="color: blue; font-size: 15px"></span>
				</div>
				<div class="btn-bar">
					<span>
						当前实际在线人数：
						<c:if test="${!empty onlineUsers}">
						${onlineUsers.loginedUserCount+onlineUsers.unLoginUserCount} (未登录：${onlineUsers.unLoginUserCount} &nbsp;&nbsp;已登录：${onlineUsers.loginedUserCount} ) 
						</c:if>
					</span>
					<div id="select_user" class="select_user"></div>
				</div>
				<div class="table-container table-hy">
					<table class="border-table online-table">
						<thead>
							<tr>
								<th width="100">公司名称</th>
								<th width="100">用户名[最近访问时间]</th>
							</tr>
						</thead>
						<tbody>
							<c:if test="${!empty onlineUsers}">
								<c:forEach var="item" items="${onlineUsers.companyUsersMap}">
									<tr>
										<td align="right">
											<a onclick="company_view(${fn:split(item.key,'_')[0]})">${fn:split(item.key,'_')[1]}</a>
											&nbsp;&nbsp;
										</td>
										<td style="overflow: visible; white-space: normal" align="left">
											&nbsp;&nbsp;
											<c:forEach var="u" items="${item.value}" varStatus="sta">
												<a class="online_user" onclick="user_view('${u.user.id}')">${u.user.userName}[<fmt:formatDate value="${u.lastAccessTime }" type="date" pattern="HH:mm:ss" />
													]
												</a>
												<div class="online_user_session" style="display: none;" name="session">
													<div>
														<div>
															<label class="label-warning">SessionID</label>${u.session.id }</div>
														<div>
															<label class="label-warning">访问IP</label>${u.host }</div>
													</div>
													<div>
														<div>
															<label class="label-inverse">开始时间</label>
															<fmt:formatDate value="${u.startTimestamp }" type="date" pattern="yyyy-MM-dd HH:mm:ss" />
														</div>
														<div>
															<label class="label-inverse">最近访问时间</label>
															<fmt:formatDate value="${u.lastAccessTime }" type="date" pattern="yyyy-MM-dd HH:mm:ss" />
														</div>

														<div>
															<label class="label-inverse">超时时间</label>
															<fmt:formatNumber value="${u.timeout/1000/60 }" pattern="#" />
															分，剩余：
															<fmt:formatNumber value="${(u.timeout-(currTime-u.lastAccessTime.time))/1000/60 }" pattern="#" />
															:
															<fmt:formatNumber value="${(u.timeout-(currTime-u.lastAccessTime.time))/1000%60 }" pattern="#" />
														</div>
													</div>
												</div>
												<c:if test="${!sta.last }">、</c:if>
											</c:forEach>
										</td>
									</tr>
								</c:forEach>
							</c:if>
						</tbody>
					</table>
				</div>
				<%-- 				</c:if> --%>
			</div>
		</div>
	</div>
</body>
</html>
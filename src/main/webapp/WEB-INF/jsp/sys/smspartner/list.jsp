<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/sys/smspartner/list.js?v=${v }"></script>
<title>短信渠道列表</title>

</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--顶部导航-->
			<div class="cl topPath"></div>
			<!--搜索栏-->
			<form:form id="searchForm" action="${ctx}/sys/smspartner/list" method="post" class="search_bar">
				<input id="pageNo" name="pageNo" type="hidden" />
				<input id="pageSize" name="pageSize" type="hidden" />
				<div class="search_container">
					<input type="text" class="input-txt" style="width: 250px; height: 27px" value="${smsPartnerName}" placeholder="输入短信供应商名称" id="smsPartnerName" name="smsPartnerName">
					<button type="submit" class="nav_btn table_nav_btn" id="btn_search" name="btn_search">
						<i class="fa fa-search"></i>
						查找
					</button>
				</div>
			</form:form>
			<!--表格-->
			<div>
				<!--按钮组-->
				<shiro:hasPermission name="sys:smspartner:create">
					<div class="btn-bar">
						<span>
							<a href="javascript:;" id="btn_smspartner_create" class="nav_btn table_nav_btn">
								<i class="fa fa-plus-square"></i>
								添加短信渠道
							</a>
						</span>
					</div>
				</shiro:hasPermission>
				<div class="table-container table-hy">
					<table class="border-table">
						<thead>
							<tr>
								<th width="60">渠道编号</th>
								<th width="150">渠道名称</th>
								<th width="80">分配的商户编号</th>
								<th width="90">密钥</th>
								<th width="80">发送优先级</th>
								<th width="130">短信终端类型</th>
								<th width="100">渠道状态</th>
								<th width="100">渠道创建时间</th>
								<th width="150">描述</th>
								<th width="140">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="smsPartner" items="${page.list }">
								<tr onDblClick="smsPartner_edit(${smsPartner.id})">
									<td>${smsPartner.id }</td>
									<td>${smsPartner.name }</td>
									<td>${smsPartner.partnerId }</td>
									<td>${smsPartner.secretKey }</td>
									<td>${smsPartner.priority }</td>
									<td>${smsPartner.smsSendType.text }</td>
									<td>${smsPartner.state.text }</td>
									<td>
										<fmt:formatDate value="${smsPartner.createTime}" type="date" pattern="yyyy-MM-dd" />
									</td>
									<td>${smsPartner.remark }</td>
									<td class="td-manage">
										<shiro:hasPermission name="sys:smspartner:edit">
											<a title="编辑" href="javascript:;" onclick="smsPartner_edit(${smsPartner.id})" style="margin-right: 10px;">
												<i class="fa fa-pencil"></i>
											</a>

										</shiro:hasPermission>
										<shiro:hasPermission name="sys:smspartner:del">
											<a title="删除" href="javascript:;" style="margin-right: 10px;" onclick="smsPartner_del(this,${smsPartner.id})">
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
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/sys/smsportal/list.js?v=${v }"></script>
<title>短信接入列表</title>

</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--顶部导航-->
			<div class="cl topPath"></div>

			<!--表格-->
			<div>
				<!--按钮组-->

				<div class="btn-bar">
					<span>
						<shiro:hasPermission name="sys:smsportal:create">
							<a href="javascript:;" id="btn_smsportal_create" class="nav_btn table_nav_btn">
								<i class="fa fa-plus-square"></i>
								添加短信接入商
							</a>
						</shiro:hasPermission>
						<a href="javascript:;" id="btn_smsportal_test" class="nav_btn table_nav_btn">
							<i class="fa fa-plus-square"></i>
							短信发送测试
						</a>
					</span>
				</div>
				<div class="table-container table-hy">
					<table class="border-table">
						<thead>
							<tr>
								<th width="60">接入商编号</th>
								<th width="150">接入商名称</th>
								<th width="90">密钥</th>
								<th width="80">签名</th>
								<th width="80">发送优先级</th>
								<th width="130">短信通道ID</th>
								<th width="100">接入商状态</th>
								<th width="100">创建时间</th>
								<th width="100">更新时间</th>
								<th width="150">描述</th>
								<th width="140">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="smsportal" items="${result }">
								<tr onDblClick="smsportal_edit(${smsportal.id})">
									<td>${smsportal.accountId }</td>
									<td>${smsportal.name }</td>
									<td>${smsportal.secretkey }</td>
									<td>${smsportal.sign }</td>
									<td>${smsportal.priority }</td>
									<td>${smsportal.partnerId }</td>
									<td>${smsportal.state.text }</td>
									<td>
										<fmt:formatDate value="${smsportal.createTime}" type="date" pattern="yyyy-MM-dd" />
									</td>
									<td>
										<fmt:formatDate value="${smsportal.updateTime}" type="date" pattern="yyyy-MM-dd" />
									</td>
									<td>${smsportal.remark }</td>
									<td class="td-manage">
										<shiro:hasPermission name="sys:smsportal:edit">
											<a title="编辑" href="javascript:;" onclick="smsportal_edit(${smsportal.id})" style="margin-right: 10px;">
												<i class="fa fa-pencil"></i>
											</a>

										</shiro:hasPermission>
										<shiro:hasPermission name="sys:smsportal:del">
											<a title="删除" href="javascript:;" style="margin-right: 10px;" onclick="smsportal_del(this,${smsportal.id})">
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
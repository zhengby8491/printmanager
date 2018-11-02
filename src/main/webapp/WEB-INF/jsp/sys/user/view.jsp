<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/sys/user/edit.js?v=${v }"></script>
<title>添加用户 - 用户管理</title>
</head>
<body>
	<div class="layer_container">
		<!--表单部分START-->
		<form id="form-user">
			<input type="hidden" name="id" value="${user.id }" />
			<div class="online_view">
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">用户名：</label>
						<div class="ui-combo-wrap form_text divWidth">${user.userName }</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">真实姓名：</label>
						<div class="ui-combo-wrap form_text divWidth">${user.realName }</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">手机：</label>
						<div class="ui-combo-wrap form_text divWidth">${user.mobile}</div>
					</div>

					<div class="row-div">
						<label class="form-label label_ui label_4mar">邮箱：</label>
						<div class="ui-combo-wrap form_text divWidth">${user.email}</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">用户状态：</label>
						<div class="ui-combo-wrap form_text divWidth">${user.state.text}</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">登录次数：</label>
						<div class="ui-combo-wrap form_text divWidth">${user.loginCount}</div>
					</div>
				</div>

				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">最近登录时间：</label>
						<div class="ui-combo-wrap form_text divWidth">${user.lastLoginTime}</div>
					</div>

					<div class="row-div">
						<label class="form-label label_ui label_4mar">最近登录IP：</label>
						<div class="ui-combo-wrap form_text divWidth">${user.lastLoginIp}</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							角色：
						</label>
						<div class="ui-combo-wrap form_text check_box" style="width: 500px">
							<c:forEach var="item" items="${hasRoleList}" varStatus="sta">
							${item.name}
							<c:if test="${!sta.last }">、</c:if>
							</c:forEach>
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">备注：</label>
						<div class="form_textarea_rlt view_area" style="width: 502px; height: 60px; resize: none">${user.memo}</div>
					</div>
				</div>
			</div>
		</form>
		<!--表单部分END-->
	</div>
</body>
</html>
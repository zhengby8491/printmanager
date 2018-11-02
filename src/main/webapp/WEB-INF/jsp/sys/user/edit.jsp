<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/sys/user/edit.js?vf=${v }"></script>
<title>编辑用户 - 用户管理</title>
</head>
<body>
	<div class="layer_container">
		<!--表单部分START-->
		<form id="form-user">
			<input type="hidden" name="id" id="user_id" value="${user.id }" />
			<div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							用户名：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" value="${user.userName }" placeholder="" id="userName" name="userName" readonly="readonly">
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							员工：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list items="${fns:basicList2('EMPLOYEE')}" valueProperty="id" defaultOption="请选择" defaultValue="" selected="${user.employeeId }" name="employeeId" textProperty="name" cssClass="hy_select2"></phtml:list>
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							初始密码：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" readonly="true" placeholder="密码" value="******" readonly="readonly" />
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							手机：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="constraint_mobile input-txt input-txt_7" value="${user.mobile }" placeholder="" id="mobile" name="mobile">
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							邮箱：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" placeholder="@" value="${user.email}" id="email" name="email">
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							用户状态：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">${user.state.text}</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">备注：</label>
						<div class="form_textarea_rlt">
							<textarea name="memo" id="memo" cols="" rows="" class="input-txt_7 input-txt_onlymemos" placeholder="说点什么...100个字符以内" onKeyUp="Helper.doms.textarealength(this,100)" style="width: 502px; height: 60px; resize: none">${user.memo}</textarea>
							<p class="textarea-numberbar">
								<em>0</em>
								/100
							</p>
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar"> 业务数据授权：</label>
						<div class="form_text check_box" style="width: 504px" id="colleague"></div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							选择角色：
						</label>
						<div class="form_text check_box" style="width: 504px">
							<phtml:list name="roles" valueProperty="id" multiple="true" textProperty="name" items="${ allRoleList}" selected="${hasRoleIdList}" inputType="checkbox"></phtml:list>
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<input class="nav_btn table_nav_btn" type="submit" style="margin-left: 140px; margin-top: 5px;" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
					</div>
				</div>
			</div>
		</form>
		<!--表单部分END-->
	</div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/sys/user/add.js?v=${v}"></script>
<title>添加用户 - 用户管理</title>
</head>
<body>
	<div class="layer_container">
		<!--表单部分START-->
		<form id="form-user">
			<div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							用户名：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" value="" placeholder="" id="userName" name="userName" />
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							员工：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list items="${fns:basicList2('EMPLOYEE')}" valueProperty="id" defaultOption="请选择" defaultValue="" name="employeeId" textProperty="name" cssClass="hy_select2"></phtml:list>
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
							<input type="text" class="input-txt input-txt_7" placeholder="密码" id="password" name="password" />
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							手机：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="constraint_mobile input-txt input-txt_7" placeholder="" id="mobile" name="mobile" />
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
							<input type="text" class="input-txt input-txt_7" placeholder="@" id="email" name="email" />
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							用户状态：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list name="state" textProperty="text" cssClass="hy_select2" items="${stateList }"></phtml:list>
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">备注：</label>
						<div class="form_textarea_rlt">
							<textarea name="memo" id="memo" class="input-txt_7 " placeholder="说点什么...100个字符以内" dragonfly="true" onKeyUp="Helper.doms.textarealength(this,100)" style="width: 502px; height: 60px; resize: none"></textarea>
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
							<phtml:list name="roles" valueProperty="id" multiple="true" textProperty="name" items="${roleList}" inputType="checkbox"></phtml:list>
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<input class="nav_btn table_nav_btn" type="submit" style="margin-left: 140px; margin-top: 5px;" value="&nbsp;&nbsp;提交&nbsp;&nbsp;" />
					</div>
				</div>
			</div>
		</form>
		<!--表单部分END-->
	</div>
</body>
</html>
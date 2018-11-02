<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/basic/employee/create.js?v=${v}"></script>
<title>快捷添加员工 - 员工管理</title>
</head>
<body>
	<div class="layer_container">
		<!--表单部分START-->
		<form id="jsonForm">
			<div>
				<div class="cl">
						<div class="row-div" style="display: none;">
							<label class="form-label label_ui label_4mar">
								<span class="c-red">*</span>
								排序：
							</label>
							<div class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_7 constraint_number" value="${fns:nextSort('employee')}" placeholder="" id="sort" name="sort">
							</div>
						</div>
						<div class="row-div" style="display: none;">
							<label class="form-label label_ui label_4mar">
								<span class="c-red">*</span>
								员工工号：
							</label>
							<div class="ui-combo-wrap form_text error_tip">
								<input type="text" class="input-txt input-txt_7" readonly="true" value="${code }" placeholder="" id="code" name="code" />
							</div>
						</div>
					</div>
					<div class="cl">
						<div class="row-div">
							<label class="form-label label_ui label_4mar">
								<span class="c-red">*</span>
								员工名称：
							</label>
							<div class="ui-combo-wrap form_text error_tip">
								<input type="text" class="input-txt input-txt_7" value="" placeholder="" id="name" name="name" />
							</div>
						</div>
						
					</div>
					<div class="cl">
						<div class="row-div">
							<label class="form-label label_ui label_4mar">
								<span class="c-red">*</span>
								部门：
							</label>
							<div class="ui-combo-wrap form_text div_select_wrap">
								<phtml:list items="${fns:basicList('DEPARTMENT')}" valueProperty="id" cssClass="hy_select2" name="departmentId" textProperty="name"></phtml:list>
							</div>
						</div>
					</div>
					<div class="cl">
						<div class="row-div">
							<label class="form-label label_ui label_4mar">
								<span class="c-red">*</span>
								职位：
							</label>
							<div class="ui-combo-wrap form_text div_select_wrap">
								<phtml:list items="${fns:basicList('POSITION')}" valueProperty="id" cssClass="hy_select2" name="positionId" textProperty="name"></phtml:list>
							</div>
						</div>
						<div class="row-div" style="display: none;">
							<label class="form-label label_ui label_4mar">
								<span class="c-red"></span>
								电话：
							</label>
							<div class="ui-combo-wrap form_text error_tip">
								<input type="text" class="input-txt input-txt_7" value="" placeholder="" id="mobile" name="mobile" />
							</div>
						</div>
					</div>
					<div class="cl">
						<div class="row-div">
							<label class="form-label label_ui label_4mar">
								<span class="c-red">*</span>
								性别：
							</label>
							<div class="ui-combo-wrap form_text div_select_wrap">
								<phtml:list name="sexType" textProperty="text" cssClass="hy_select2" type="com.huayin.printmanager.persist.enumerate.SexType"></phtml:list>
							</div>
						</div>
						<div class="row-div" style="display: none;">
							<label class="form-label label_ui label_4mar">邮箱：</label>
							<div class="ui-combo-wrap form_text error_tip">
								<input type="text" class="input-txt input-txt_7" value="" placeholder="" id="email" name="email" />
							</div>
						</div>
					</div>
					<div class="cl" style="display: none;">
						<div class="row-div">
							<label class="form-label label_ui label_4mar">
								<span class="c-red"></span>
								入职时间：
							</label>
							<div class="ui-combo-wrap form_text error_tip">
								<input type="text" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'departureTime\')}',maxDate:'%y-%M-%d'})" class="input-txt input-txt_7 Wdate" id="entryTime" name="entryTime" />
							</div>
						</div>
						<div class="row-div">
							<label class="form-label label_ui label_4mar">离职时间：</label>
							<div class="ui-combo-wrap form_text error_tip">
								<input type="text" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'entryTime\')}',maxDate:'%y-%M-%d'})" class="input-txt input-txt_7 Wdate" id="dateMax" name="departureTime" />
							</div>
						</div>
					</div>
					<div class="cl" style="display: none;">
						<div class="row-div">
							<label class="form-label label_ui label_4mar">
								<span class="c-red">*</span>
								状态：
							</label>
							<div class="ui-combo-wrap form_text div_select_wrap">
								<phtml:list name="state" textProperty="text" cssClass="hy_select2" type="com.huayin.printmanager.persist.enumerate.EmployeeState"></phtml:list>
							</div>
						</div>
					</div>
					<div class="cl" style="display: none;">
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
							<input class="nav_btn table_nav_btn" type="submit" style="margin-left: 140px; margin-top: 5px;" value="&nbsp;&nbsp;提交&nbsp;&nbsp;" />
						</div>
					</div>
			</div>
		</form>
		<!--表单部分END-->
	</div>
</body>
</html>
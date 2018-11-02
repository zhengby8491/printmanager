<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/sys/smsportal/edit.js?v=${v}"></script>
<meta charset="UTF-8">
<title>编辑短信接入商</title>
</head>
<body>
	<div class="layer_container">
		<!--表单部分START-->
		<!--表单部分START-->
		<form id="jsonForm">
			<input type="hidden" name="id" value="${smsportal.id }">
			<div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							接入商编号：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" id="accountId" name="accountId" value="${smsportal.accountId }" />
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							接入商名称：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" id="name" name="name" value="${smsportal.name }" />
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							密钥：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" id="secretkey" name="secretkey" value="${smsportal.secretkey }" />
						</div>
					</div>
				</div>
				<div class="cl">

					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							签名：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" id="sign" name="sign" value="${smsportal.sign }" />
						</div>
					</div>

					<div class="cl">
						<div class="row-div">
							<label class="form-label label_ui label_4mar">
								<span class="c-red"></span>
								发送优先级：
							</label>
							<div class="ui-combo-wrap form_text div_select_wrap">
								<input type="text" class="input-txt input-txt_7" id="priority" name="priority" value="${smsportal.priority }" />
							</div>
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							通道：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list name="partnerId" cssClass="hy_select2" items="${partnerList}" textProperty="name" valueProperty="id"></phtml:list>
						</div>
					</div>

				</div>
				<div class="cl">
				<div class="row-div">
					<label class="form-label label_ui label_4mar">
						<span class="c-red"></span>
						状态：
					</label>
					<div class="ui-combo-wrap form_text div_select_wrap">
						<phtml:list name="state" selected="${smsportal.state }" textProperty="text" cssClass="hy_select2" type="com.huayin.printmanager.persist.enumerate.State"></phtml:list>
					</div>
				</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">描述：</label>
						<div class="form_textarea_rlt">
							<textarea name="remark" id="remark" class="input-txt_7 " placeholder="说点什么...100个字符以内" dragonfly="true" onKeyUp="Helper.doms.textarealength(this,100)" style="width: 824px;">${smsportal.remark}</textarea>
							<p class="textarea-numberbar">
								<em>0</em>
								/100
							</p>
						</div>
					</div>
				</div>
			</div>
			<!--表单部分END-->

			<div style="margin-left: 850px; margin-top: 10px">
				<input style="position: absolute; left: 50%; margin-left: -30px;" class="nav_btn table_nav_btn" type="button" id="btn_save" onclick="checkData();" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
			</div>
		</form>
	</div>
</body>
</html>

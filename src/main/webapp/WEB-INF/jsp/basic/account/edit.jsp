<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/basic/account/edit.js?v=${v}"></script>
<title>编辑公司账户 - 公司账户管理</title>
</head>
<body>
	<div class="layer_container">
		<form id="jsonForm">
			<input type="hidden" id="id" name="id" value="${account.id}" />
			<div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							排序：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7 constraint_number" value="${account.sort}" placeholder="" id="sort" name="sort" />
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							银行卡类型：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list name="bankType" selected="${account.bankType}" textProperty="text" cssClass="input-txt input-txt_7 hy_select2" type="com.huayin.printmanager.persist.enumerate.BankType"></phtml:list>
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							支行名称：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" value="${account.branchName}" placeholder="" id="branchName" name="branchName" />
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							账户：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" value="${account.bankNo}" placeholder="" id="bankNo" name="bankNo" />
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							币种：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list name="currencyType" textProperty="text" selected="${account.currencyType}" cssClass="input-txt input-txt_7 hy_select2" type="com.huayin.printmanager.persist.enumerate.CurrencyType"></phtml:list>
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							账号类型：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list name="accountType" selected="${account.accountType}" textProperty="text" cssClass="input-txt input-txt_7 hy_select2" type="com.huayin.printmanager.persist.enumerate.AccountType"></phtml:list>
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">备注：</label>
						<div class="form_textarea_rlt">
							<textarea name="memo" id="memo" class="input-txt_7 " placeholder="说点什么...100个字符以内" dragonfly="true" onKeyUp="Helper.doms.textarealength(this,100)" style="width: 502px; height: 60px; resize: none">${account.memo}</textarea>
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
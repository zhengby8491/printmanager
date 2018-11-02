<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/sys/company/maintain.js?vs=${v}"></script>
<title>维护</title>
</head>
<body>
	<div class="layer_container">
		<!--表单部分START-->
		<form id="form-company">
			<input type="hidden" name="id" value="${company.id }" />
			<div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							合同公司：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" value="${company.contractCompanyName }" placeholder="" id="contractCompanyName" name="contractCompanyName">
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							合同日期：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" onfocus="WdatePicker({contractTime:'#F{$dp.$D(\'contractTime\')||\'%y-%M-%d\'}'})" class="input-txt input-txt_0 Wdate" id="contractTime" name="contractTime" value="<fmt:formatDate value="${company.contractTime}" pattern="yyyy-MM-dd"/>" />
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							正式用户：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<phtml:list cssClass="input-txt input-txt_7 hy_select2" textProperty="text" type="com.huayin.printmanager.persist.enumerate.BoolValue" name="isFormal" selected="${company.isFormal}"></phtml:list>
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							到期时间：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" onfocus="WdatePicker({expireTime:'#F{$dp.$D(\'expireTime\')||\'%y-%M-%d\'}'})" class="input-txt input-txt_0 Wdate" id="expireTime" name="expireTime" value="<fmt:formatDate value="${company.expireTime}" pattern="yyyy-MM-dd"/>" />
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							最大角色数量：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7 constraint_negative" value="${company.roleCountMax }" placeholder="" id="roleCountMax" name="roleCountMax">
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							代理商：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<phtml:list items="${agentList }" cssClass="input-txt input-txt_0 hy_select2" textProperty="name" valueProperty="id" inputType="select" name="agentQuotientId" selected="${company.agentQuotientId }" defaultOption="请选择" defaultValue=""></phtml:list>
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							状态：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<span class="ui-combo-wrap">
								<phtml:list cssClass="input-txt input-txt_7 hy_select2" textProperty="text" selected="${company.state }" type="com.huayin.printmanager.persist.enumerate.CompanyState" name="companyState"></phtml:list>
							</span>

						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							版本：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<span class="ui-combo-wrap">
								<select id="systemVersion" name=systemVersion class="input-txt input-txt_0 hy_select2">
									<option value="1" <c:if test="${company.systemVersion==1 }">selected="selected"</c:if>>工单版</option>
									<option value="2" <c:if test="${company.systemVersion==2 || company.systemVersion==null }">selected="selected"</c:if>>标准版</option>
								</select>
							</span>
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							开启代工业务：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<span class="ui-combo-wrap">
								<phtml:list cssClass="input-txt input-txt_7 hy_select2" textProperty="text" selected="${company.isOem }" type="com.huayin.printmanager.persist.enumerate.BoolValue" name="isOem"></phtml:list>
							</span>
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
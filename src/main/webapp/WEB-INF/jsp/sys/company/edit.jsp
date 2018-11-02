<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/sys/company/edit.js?${v}"></script>
<title>编辑公司</title>
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
							公司名称：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" value="${company.name }" placeholder="" id="name" name="name">
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							联系人：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" value="${company.linkName }" placeholder="" id="linkName" name="linkName">
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							公司电话：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" value="${company.tel }" placeholder="" id="tel" name="tel">
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							公司邮箱：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" value="${company.email}" id="email" name="email">
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							公司传真：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" value="${company.fax }" placeholder="" id="fax" name="fax">
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							公司网站：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" placeholder="" value="${company.website }" id="website" name="website">
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							公司地址：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" placeholder="" value="${company.address }" id="address" name="address">

						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							到期时间：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<fmt:formatDate value="${company.expireTime}" type="date" pattern="yyyy-MM-dd" />
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							微信号：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" placeholder="" value="${company.weixin}" id="weixin" name="weixin">

						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							正式用户：
						</label>
						${company.isFormal.text}
					</div>
				</div>
				<div class="cl">
					<div class="row-div"  style="margin-top: 18px;">
						<label class="form-label label_ui label_4mar">代工平台：</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list selected="${company.isOem }" name="isOem" textProperty="text" cssClass="" type="com.huayin.printmanager.persist.enumerate.BoolValue"></phtml:list>
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">公司简介：</label>
						<div class="form_textarea_rlt" style="margin-top: 8px">
							<textarea name="introduction" id="introduction" cols="" rows="" class="input-txt_7 " style="width: 502px; height: 60px">${company.introduction}</textarea>
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
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/offer/setting/bfluteEdit.js?v=${v}"></script>
<print:body>
	<print:form>
		<form id="jsonForm">
			<input type="hidden" id="id" name="id" value="${bflute.id }"> <input type="hidden" name="offerType" id="offerType" value="${bflute.offerType }">
			<div class="cl row-div">
				<label class="form-label label_ui label_7mar"> 坑型： </label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_7" value="${bflute.pit }" name="pit">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_7mar"> 纸质： </label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_7" value="${bflute.paperQuality }" name="paperQuality">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_7mar"> 单价&nbsp;(元/千平方英寸)：</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_7 constraint_decimal_negative" value="${bflute.price }" name="price">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar"></label>
				<div class="form-label label_ui">
					<input class="nav_btn table_nav_btn" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
				</div>
			</div>
		</form>
	</print:form>
</print:body>
</html>
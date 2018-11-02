<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/offer/setting/bfluteCreate.js?v=${v}"></script>
<print:body>
	<print:form>
		<form id="jsonForm">
			<input type="hidden" id="id" name="id" value=" "> <input type="hidden" id="isBatch" value="${isBatch }"> <input type="hidden" id="offerType" name="offerType" value="${offerType }">
			<div class="cl row-div">
				<label class="form-label label_ui label_7mar"> 坑型： </label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_7" name="pit">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_7mar"> 纸质： </label>
				<div class="ui-combo-wrap form_text error_tip">
					<c:if test="${isBatch == false }">
						<input type="text" class="input-txt input-txt_7" name="paperQuality">
					</c:if>
					<c:if test="${isBatch == true }">
						<c:forEach begin="1" end="10" step="1">
							<input type="text" class="input-txt input-txt_14" name="batchList.paperQuality">
						</c:forEach>
					</c:if>
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_7mar"> 单价&nbsp;(元/千平方英寸)：</label>
				<div class="ui-combo-wrap form_text error_tip">
					<c:if test="${isBatch == false }">
						<input type="text" class="input-txt input-txt_7 constraint_decimal_negative" name="price">
					</c:if>
					<c:if test="${isBatch == true }">
						<c:forEach begin="1" end="10" step="1">
							<input type="text" class="input-txt input-txt_14 constraint_decimal_negative" name="batchList.price">
						</c:forEach>
					</c:if>
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar"></label>
				<div class="form-label label_ui">
					<input style="margin-left: 155px" class="nav_btn table_nav_btn" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
				</div>
			</div>
		</form>
	</print:form>
</print:body>
</html>
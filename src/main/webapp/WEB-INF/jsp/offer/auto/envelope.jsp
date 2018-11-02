<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/offer/auto/common.js?v=${v}"></script>
<print:body>
	<!-- 导航按钮 -->
	<print:nav title="报价系统-自动报价-${offerType.text }自动报价">
		<shiro:hasPermission name="offer:auto:envelope:save"> 
			<button class="nav_btn table_nav_btn" id="btn_save">保存</button>
		</shiro:hasPermission>
	</print:nav>
	<print:listLeft>
		<%@include file="/WEB-INF/jsp/offer/auto/_menuAuto.jsp"%>
	</print:listLeft>

	<print:listRight>
		<!-- 表格部分 -->
		<print:table>
			<!-- 表格内容 -->
			<print:tableContent formId="quote_form">
				<thead>
					<tr>
						<th width="60" colspan="2">
							<span style="font-size: 20px; font-family: 'STHeiti'">
								<strong>${offerType.text }自动报价</strong>
							</span>
							<span style="font-size: 11px;">：信封，利是封</span>
						</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td width="10%">成品尺寸（mm）</td>
						<td width="300px" style="text-align: left;">
							<span class="ui-combo-wrap" style="margin-left: 23px">
								<phtml:list name="spec" textProperty="text" valueProperty="style" cssClass="input-txt input-txt_9 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferEnvelopeSpecType" />
							</span>
							<span class="hiddenSpan" style="display: none;">
								<span class="mgl2">展长</span>
								<input type="text" id="length" class="constraint_decimal_negative input-txt input-txt_25 whiteBg">
								<span class="mgl">展宽</span>
								<input type="text" id="width" class="constraint_decimal_negative input-txt input-txt_25 whiteBg">
							</span>
						</td>
					</tr>
					<tr>
						<td width="120px">印刷数量</td>
						<td width="300px" style="text-align: left;">
							<input type="text" style="margin-left: 23px" class="constraint_decimal_negative input-txt input-txt_29 whiteBg" id="amount" name="amount">
							<!-- 设计费 -->
							<span class="design" style="margin-left: 39px;">
								<select class="designType input-txt input-txt_3 hy_select">
									<option value="SIMPLE">板材设计</option>
									<option value="NORMAL">来样设计</option>
									<option value="COMPLEX">创意设计 </option>
									<option value="" selected>无需设计</option>
								</select>
								<input style="margin-left: 39px;" type="text" class="designFee input-txt input-txt_25 constraint_decimal_negative" readonly="readonly" value=""><span>元/款</span>
							</span>
						</td>
					</tr>
					<!---------------------------------------- 公共 > 基础 --------------------------------------------->
					<%@include file="/WEB-INF/jsp/offer/auto/_commonBase.jsp"%>
				</tbody>
			</print:tableContent>
			<!---------------------------------------- 公共 --------------------------------------------->
			<%@include file="/WEB-INF/jsp/offer/auto/_commonQuote.jsp"%>
		</print:table>
	</print:listRight>
</print:body>

</html>
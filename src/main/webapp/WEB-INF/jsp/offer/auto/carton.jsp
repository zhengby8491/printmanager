<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/offer/auto/common.js?v=${v}"></script>
<script type="text/javascript" src="${ctxStatic}/site/offer/auto/carton.js?v=${v}"></script>
<print:body>
	<!-- 导航按钮 -->
	<print:nav title="报价系统-自动报价-${offerType.text }自动报价">
		<shiro:hasPermission name="offer:auto:carton:save">
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
						<th width="60" colspan="2"><span style="font-size: 20px; font-family: 'STHeiti'"> <strong>${offerType.text }自动报价</strong> <input type="hidden" id="offerType" name="offerType" value="${offerType}">
						</span> <span style="font-size: 11px;">：包装盒，礼品盒，彩盒，彩箱</span></th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td colspan="2">
							<div class="dl carton_type">
								<input type="hidden" name="paperType" value="${paperType }"> <img class="cb_img" src="/static/layout/images/offer/box/扣底带挂勾盒1.jpg" title="扣底带挂勾盒" data-type="KDDGGH" style="border: 2px solid red;"> <img class="cb_img" src="/static/layout/images/offer/box/平口箱盒2.jpg" title="平口箱盒" data-type="PKXH"> <img class="cb_img" src="/static/layout/images/offer/box/收缩盒2.jpg" title="收缩盒" data-type="SSH"> <img class="cb_img" src="/static/layout/images/offer/box/双插带安全扣盒2.jpg" title="双插带安全扣盒" data-type="SCDAQKH"> <img class="cb_img" src="/static/layout/images/offer/box/双插带挂勾盒2.jpg" title="双插带挂勾盒" data-type="SXDGGH"> <img class="cb_img" src="/static/layout/images/offer/box/双插盒2.jpg"
									title="双插盒" data-type="SXH"> <img class="cb_img" src="/static/layout/images/offer/box/提手盒2.jpg" title="提手盒" data-type="TSH"> <img class="cb_img" src="/static/layout/images/offer/box/自动扣底盒2.jpg" title="自动扣底盒" data-type="ZDKDH">
								<button type="button" class="cb_img" title="其他盒型" data-type="QTHX">其他盒型</button>
								<button type="button" class="cb_img" title="多部件盒型" data-type="DBJHX">多部件盒型</button>
							</div>
						</td>
					</tr>
					<tr>
						<td width="10%">成品尺寸（mm）</td>
						<td width="300px" style="text-align: left;"><span class="mgl">长</span> <input type="text" id="slength" name="length" class="constraint_decimal_negative input-txt input-txt_25 whiteBg" value="80"> <span class="mgl">宽</span> <input type="text" id="swidth" name="width" class="constraint_decimal_negative input-txt input-txt_25 whiteBg" value="40"> <span class="mgl">高</span> <input type="text" id="sheight" name="height" class="constraint_decimal_negative input-txt input-txt_25 whiteBg" value="120"> <span class="mgl2">展长</span> <input type="text" id="length" name="styleLength" class="constraint_decimal_negative input-txt input-txt_25 whiteBg" value="265"> <span class="mgl">展宽</span>
							<input type="text" id="width" name="styleWidth" class="constraint_decimal_negative input-txt input-txt_25 whiteBg" value="230"></td>
					</tr>
					<tr>
						<td width="120px">印刷数量</td>
						<td width="300px" style="text-align: left;"><input type="text" style="margin-left: 23px" class="constraint_decimal_negative input-txt input-txt_29 whiteBg" name="amount" id="amount">
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

					<!---------------------------------------- 成品工序   --------------------------------------------->
					<tr>
						<td>成品工序</td>
						<td style="text-align: left;" class="productProcedureTd">
							<div class="dl" style="margin-left: 23px">
								<c:forEach items="${procedureList }" varStatus="status" var="procList">
									<c:if test="${procList.switchStatus =='ENABLED' && procList.procedureType == 'FINISHED' }">
										<label class="offer-label label_ui label_10"> <input type="checkbox" name="productDetail.isChecked"> <input type="hidden" name="productDetail.offerType" value="${procList.offerType }"> <input type="hidden" name="productDetail.procedureType" value="${procList.procedureType }"> <input type="hidden" name="productDetail.procedureClass" value="${procList.procedureClass }"> <input type="hidden" name="productDetail.procedureId" value="${procList.id }"> <input type="hidden" name="productDetail.price" value="${procList.price }"> <input type="hidden" name="productDetail.procedureName" value="${procList.name }"> <input type="hidden"
											name="productDetail.procedureUnit" value="${procList.procedureUnit }"> <input type="hidden" name="productDetail.lowestPrice" value="${procList.lowestPrice }"> <input type="hidden" name="productDetail.startPrice" value="${procList.startPrice }"> <input type="hidden" name="productDetail.offerProcedureFormulaType" value="${procList.offerProcedureFormulaType }"> <span>${procList.name }</span> <c:if test="${procList.procedureUnit == 'UNIT3'}">
												<input type="text" name="productDetail.length" class="constraint_decimal_negative input-txt input-txt_28 whiteBg">
												×
												<input type="text" name="productDetail.width" class="constraint_decimal_negative input-txt input-txt_28 whiteBg">
												mm 
											</c:if>
										</label>
									</c:if>
								</c:forEach>
							</div>
						</td>
					</tr>
				</tbody>
			</print:tableContent>
			<!---------------------------------------- 公共 --------------------------------------------->
			<%@include file="/WEB-INF/jsp/offer/auto/_commonQuote.jsp"%>
		</print:table>
	</print:listRight>
</print:body>

</html>
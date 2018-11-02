<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<script type='text/javascript' src='${ctxStatic}/wx/js/offer/auto/carton.js?v=${v}' charset='utf-8'></script>
<script type='text/javascript' src='${ctxStatic}/wx/js/offer/common.js?v=${v}' charset='utf-8'></script>
<title>彩盒纸箱</title>
</head>
<body>
	<div class="page page-current">
		<div class="content content-padded offer_content">
			<form action="" id="quote_form" method="post">
				<div class="offer_img_list cl">
					<div class="offer_img_content">
						<div class="img_item" data-bigimg="${ctxStatic}/wx/img/box/扣底带挂勾盒2.jpg" title="扣底带挂勾盒" data-type="KDDGGH" style="border: 2px solid red;">
							<img src="${ctxStatic}/wx/img/box/扣底带挂勾盒1.jpg" />
							<p>扣底带挂勾盒</p>
						</div>
						<div class="img_item" data-bigimg="${ctxStatic}/wx/img/box/平口箱盒1.jpg" title="平口箱盒" data-type="PKXH">
							<img src="${ctxStatic}/wx/img/box/平口箱盒2.jpg" />
							<p>平口箱盒</p>
						</div>
						<div class="img_item" data-bigimg="${ctxStatic}/wx/img/box/收缩盒1.jpg" title="收缩盒" data-type="SSH">
							<img src="${ctxStatic}/wx/img/box/收缩盒2.jpg" />
							<p>收缩盒</p>
						</div>
						<div class="img_item" data-bigimg="${ctxStatic}/wx/img/box/双插带安全扣盒1.jpg" title="双插带安全扣盒" data-type="SCDAQKH">
							<img src="${ctxStatic}/wx/img/box/双插带安全扣盒2.jpg" />
							<p>双插带安全扣盒</p>
						</div>
						<div class="img_item" data-bigimg="${ctxStatic}/wx/img/box/双插带挂勾盒1.jpg" title="双插带挂勾盒" data-type="SXDGGH">
							<img src="${ctxStatic}/wx/img/box/双插带挂勾盒2.jpg" />
							<p>双插带挂勾盒</p>
						</div>
						<div class="img_item" data-bigimg="${ctxStatic}/wx/img/box/双插盒1.jpg" title="双插盒" data-type="SXH">
							<img src="${ctxStatic}/wx/img/box/双插盒2.jpg" />
							<p>双插盒</p>
						</div>
						<div class="img_item" data-bigimg="${ctxStatic}/wx/img/box/提手盒1.jpg" title="提手盒" data-type="TSH">
							<img src="${ctxStatic}/wx/img/box/提手盒2.jpg" />
							<p>提手盒</p>
						</div>
						<div class="img_item" data-bigimg="${ctxStatic}/wx/img/box/自动扣底盒1.png" title="自动扣底盒" data-type="ZDKDH">
							<img src="${ctxStatic}/wx/img/box/自动扣底盒2.jpg" />
							<p>自动扣底盒</p>
						</div>
						<button type="button" class="img_item" style="background: white" title="其他盒型" data-type="QTHX">其他盒型</button>
						<button type="button" class="img_item" style="background: white" title="多部件盒型" data-type="DBJHX">多部件盒型</button>
					</div>
				</div>
				<table class="offer_table">
					<tr>
						<td>成品尺寸</td>
						<td>
							<div>
								<input type="number" class="constraint_negative custom_length" id="slength" name="length" value="80" placeholder="长">
								<label>X</label>
								<input type="number" class="constraint_negative custom_width" id="swidth" name="width" value="40" placeholder="宽">
								<label>X</label>
								<input type="number" class="constraint_negative custom_height" id="sheight" name="height" value="120" placeholder="高">
								mm
							</div>
							<div class="carton_custom_div">
								<label>展开长</label>
								<input type="number" class="constraint_negative width" id="length" name="styleLength" value="265">
								<label>展开宽</label>
								<input type="number" class="constraint_negative width" id="width" name="styleWidth" value="230">
							</div>
						</td>
					</tr>
					<tr>
						<td>印刷数量</td>
						<td>
							<input type="number" id="amount" class="constraint_negative amount" name="amount">
							<!-- 设计费 -->
							<span class="design">
								<select class="designType input-txt input-txt_3 hy_select">
									<option value="SIMPLE">板材设计</option>
									<option value="NORMAL">来样设计</option>
									<option value="COMPLEX">创意设计 </option>
									<option value="" selected>无需设计</option>
								</select>
								<input type="number" class="constraint_negative designFee" style="background-color: rgb(241, 241, 241);" readonly="readonly" value=""><span>元/款</span>
							</span>
						</td>
					</tr>
					<!---------------------------------------- 公共 > 基础 --------------------------------------------->
					<%@include file="/WEB-INF/jsp/wx/offer/auto/_commonBase.jsp"%>

					<tr>
					<!------------------------------------------成品工序 ----------------------------------------------->
					<tr>
						<td>成品工序</td>
						<td style="text-align: left;" class="productProcedureTd">
							<div class="dl">
								<c:forEach items="${procedureList }" varStatus="status" var="procList">
									<c:if test="${procList.switchStatus =='ENABLED' && procList.procedureType == 'FINISHED' }">
										<label class="label-checkbox item-content offer-label label_ui label_9">
										<input type="checkbox" class="check_procedure" name="productDetail.isChecked">
											<span class="item-media check_icon" style="margin-right: 0;">
												<i class="icon icon-form-checkbox"></i>
											</span>
											<input type="hidden" name="productDetail.offerType" value="${procList.offerType }">
											<input type="hidden" name="productDetail.procedureType" value="${procList.procedureType }">
											<input type="hidden" name="productDetail.procedureClass" value="${procList.procedureClass }">
											<input type="hidden" name="productDetail.procedureId" value="${procList.id }">
											<input type="hidden" name="productDetail.price" value="${procList.price }">
											<input type="hidden" name="productDetail.procedureName" value="${procList.name }">
											<input type="hidden" name="productDetail.procedureUnit" value="${procList.procedureUnit }">
											<input type="hidden" name="productDetail.lowestPrice" value="${procList.lowestPrice }">
											<input type="hidden" name="productDetail.startPrice" value="${procList.startPrice }">
											<input type="hidden" name="productDetail.offerProcedureFormulaType" value="${procList.offerProcedureFormulaType }">
											<span>${procList.name }</span>
											<c:if test="${procList.procedureUnit == 'UNIT3'}">
												<input type="number" name="productDetail.length" class="constraint_negative amount">
												×
												<input type="number" name="productDetail.width" class="constraint_negative amount">
												mm 
											</c:if>
										</label>
									</c:if>
								</c:forEach>
							</div>
						</td>
					</tr>
					<!---------------------------------------- 公共 > POSTDATA --------------------------------------------->
					<%@include file="/WEB-INF/jsp/wx/offer/auto/_commonPost.jsp"%>
				</table>
			</form>
			<div class="offer_bottom">
				<input id="btn_cal" class="button button-fill button-success btn_offer" type="button" value="点击报价">
			</div>
		</div>
		<jsp:include page="/WEB-INF/jsp/wx/foot.jsp">
			<jsp:param value="offer" name="module" />
		</jsp:include>
	</div>
</body>
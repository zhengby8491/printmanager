<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<script type='text/javascript' src='${ctxStatic}/wx/js/offer/common.js?v=${v}' charset='utf-8'></script>
<title>画册书刊</title>
</head>
<body>
	<div class="page page-current">
		<div class="content content-padded offer_content">
			<form action="" id="quote_form" method="post">
				<table class="offer_table">
					<tr>
						<td>成品尺寸</td>
						<td>
							<div class="dl">
								<label class="offer-label label_ui2 label_2">
									<phtml:list name="spec" textProperty="text" valueProperty="style" cssClass="input-txt input-txt_3 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferSpecType" />

									<!---------------------------------------- 公共 > 自定义尺寸填写 --------------------------------------------->
									<%@include file="/WEB-INF/jsp/wx/offer/auto/_commonStyleSpan.jsp"%></label>
							</div>
							<span class="ui-combo-wrap" style="margin-left: 8px;">
								<phtml:list items="${fns:basicList('TAXRATE')}" cssClass="input-txt input-txt_3 hy_select" valueProperty="percent" textProperty="name" name="taxPercent" selected="" />
							</span>
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
					<tr>
						<td>页数</td>
						<td class="offer_page_td">
							<input type="hidden" name="offerPartList.partName" value="封面">
							<input type="hidden" name="offerPartList.partName" value="内页">
							<input type="hidden" name="offerPartList.partName" value="插页">
							<div class="offer_page_div">
								<label>封面</label>
								<phtml:list name="offerPartList.pages" textProperty="text" valueProperty="value" cssClass="input-txt input-txt_26 pageNum_select" type="com.huayin.printmanager.persist.enumerate.OfferCoverPageType" />
								<span class="customInp" style="display: none;">
									<input type="number" name="offerPartList.customPages" class="constraint_negative amount">
								</span>
							</div>
							<div class="offer_page_div">
								<label>内页</label>
								<phtml:list name="offerPartList.pages" textProperty="text" valueProperty="value" cssClass="input-txt input-txt_26 pageNum_select" type="com.huayin.printmanager.persist.enumerate.OfferInsidePageType" />
								<span class="customInp" style="display: none;">
									<input type="number" name="offerPartList.customPages" class="constraint_negative amount">
								</span>
							</div>
							<div class="offer_page_div insert_tr">
								<label>插页</label>
								<phtml:list name="offerPartList.pages" textProperty="text" valueProperty="value" cssClass="input-txt input-txt_26 pageNum_select" type="com.huayin.printmanager.persist.enumerate.OfferInsidePageType" />
								<span class="customInp" style="display: none;">
									<input type="number" name="offerPartList.customPages" class="constraint_negative amount">
								</span>
							</div>
							<input type="button" id="btn_add_insertP" class="button btn_add_insertP" value="添加插页" />
						</td>
					</tr>
					<tr>
						<td>封面</td>
						<td class="offer_page_td">
							<div class="offer_color_div">
								<label>
									<span class="ui-combo-wrap" style="margin-left: 3px">
										<select style="" class="input-txt input-txt_26 hy_select" id="offerPrintStyleType" name="offerPartList.offerPrintStyleType">
											<option value="SINGLE">单面印刷</option>
											<c:if test="${offerType == 'SINGLE' }">
												<option value="DOUBLE">双面印刷</option>
											</c:if>
											<c:if test="${offerType == 'ALBUMBOOK'}">
												<option value="HEADTAIL">正反印刷</option>
												<option value="CHAOS">自翻印刷</option>
											</c:if>
										</select>
									</span>
								</label>
								<span>
									<span class="ui-combo-wrap" style="margin-left: 3px">
										<span class="mgl">正面</span>
										<phtml:list name="offerPartList.offerPrintColorType" textProperty="text" cssClass="input-txt input-txt_26 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferPrintColorType" selected="" />
									</span>
									<span class="ui-combo-wrap" style="margin-left: 3px">
										<phtml:list name="offerPartList.offerSpotColorType" textProperty="text" cssClass="input-txt input-txt_26 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferSpotColorType" selected="" />
									</span>
								</span>
							</div>
							<div class="offer_color_div">
								<span class="doubleColor" style="display: none;">
									<span class="ui-combo-wrap">
										<span class="mgl">反面</span>
										<phtml:list name="offerPartList.offerPrintColorType2" textProperty="text" cssClass="input-txt input-txt_26 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferPrintColorType" />
									</span>
									<span class="ui-combo-wrap" style="margin-left: 3px">
										<phtml:list name="offerPartList.offerSpotColorType2" textProperty="text" cssClass="input-txt input-txt_26 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferSpotColorType" />
									</span>
								</span>
							</div>
						</td>
					</tr>
					<tr>
						<td>内页</td>
						<td class="offer_page_td">
							<div class="offer_color_div">
								<label>
									<span class="ui-combo-wrap" style="margin-left: 3px">
										<select style="" class="input-txt input-txt_26 hy_select" id="offerPrintStyleType" name="offerPartList.offerPrintStyleType">
											<option value="SINGLE">单面印刷</option>
											<c:if test="${offerType == 'SINGLE' }">
												<option value="DOUBLE">双面印刷</option>
											</c:if>
											<c:if test="${offerType == 'ALBUMBOOK'}">
												<option value="HEADTAIL">正反印刷</option>
												<option value="CHAOS">自翻印刷</option>
											</c:if>
										</select>
									</span>
								</label>
								<span>
									<span class="ui-combo-wrap" style="margin-left: 3px">
										<span class="mgl">正面</span>
										<phtml:list name="offerPartList.offerPrintColorType" textProperty="text" cssClass="input-txt input-txt_26 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferPrintColorType" selected="" />
									</span>
									<span class="ui-combo-wrap" style="margin-left: 3px">
										<phtml:list name="offerPartList.offerSpotColorType" textProperty="text" cssClass="input-txt input-txt_26 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferSpotColorType" selected="" />
									</span>
								</span>
							</div>
							<div class="offer_color_div">
								<span class="doubleColor" style="display: none;">
									<span class="ui-combo-wrap">
										<span class="mgl">反面</span>
										<phtml:list name="offerPartList.offerPrintColorType2" textProperty="text" cssClass="input-txt input-txt_26 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferPrintColorType" />
									</span>
									<span class="ui-combo-wrap" style="margin-left: 3px">
										<phtml:list name="offerPartList.offerSpotColorType2" textProperty="text" cssClass="input-txt input-txt_26 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferSpotColorType" />
									</span>
								</span>
							</div>
						</td>
					</tr>
					<tr class="insert_tr">
						<td>插页</td>
						<td class="offer_page_td ">
							<div class="offer_color_div ">
								<label>
									<span class="ui-combo-wrap" style="margin-left: 3px">
										<select style="" class="input-txt input-txt_26 hy_select" id="offerPrintStyleType" name="offerPartList.offerPrintStyleType">
											<option value="SINGLE">单面印刷</option>
											<c:if test="${offerType == 'SINGLE' }">
												<option value="DOUBLE">双面印刷</option>
											</c:if>
											<c:if test="${offerType == 'ALBUMBOOK'}">
												<option value="HEADTAIL">正反印刷</option>
												<option value="CHAOS">自翻印刷</option>
											</c:if>
										</select>
									</span>
								</label>
								<span>
									<span class="ui-combo-wrap" style="margin-left: 3px">
										<span class="mgl">正面</span>
										<phtml:list name="offerPartList.offerPrintColorType" textProperty="text" cssClass="input-txt input-txt_26 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferPrintColorType" />
									</span>
									<span class="ui-combo-wrap" style="margin-left: 3px">
										<phtml:list name="offerPartList.offerSpotColorType" textProperty="text" cssClass="input-txt input-txt_26 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferSpotColorType" selected="" />
									</span>
								</span>
							</div>
							<div class="offer_color_div">
								<span class="doubleColor" style="display: none;">
									<span class="ui-combo-wrap">
										<span class="mgl">反面</span>
										<phtml:list name="offerPartList.offerPrintColorType2" textProperty="text" cssClass="input-txt input-txt_26 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferPrintColorType" />
									</span>
									<span class="ui-combo-wrap" style="margin-left: 3px">
										<phtml:list name="offerPartList.offerSpotColorType2" textProperty="text" cssClass="input-txt input-txt_26 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferSpotColorType" />
									</span>
								</span>
							</div>
						</td>
					</tr>
					<tr>
						<td>材料</td>
						<td class="offer_page_td">
							<div class="offer_material_div">
								<label>封面</label>
								<span class="ui-combo-wrap" style="margin-left: 3px">
									<phtml:list items="${fns:getOfferPaperList(offerType)}" cssClass="input-txt input-txt_26 hy_select" valueProperty="name" textProperty="name" defaultOption="请选择" name="offerPartList.paperName" onchange="selectPaperType(this)" />
								</span>
								<span class="ui-combo-wrap" style="margin-left: 3px">
									<select class="machine_sel" name="offerPartList.paperWeight" id="paperWeight">
										<option value="0">请选择</option>
									</select>
									<!-- 吨价 -->
									<input type="hidden" name="offerPartList.paperTonPrice" value="0" />
								</span>
							</div>
							<div class="offer_material_div">
								<label>内页</label>
								<span class="ui-combo-wrap" style="margin-left: 3px">
									<phtml:list items="${fns:getOfferPaperList(offerType)}" cssClass="input-txt input-txt_26 hy_select" valueProperty="name" textProperty="name" defaultOption="请选择" name="offerPartList.paperName" onchange="selectPaperType(this)" />
								</span>
								<span class="ui-combo-wrap" style="margin-left: 3px">
									<select class="machine_sel" name="offerPartList.paperWeight" id="paperWeight2">
										<option value="0">请选择</option>
									</select>
									<!-- 吨价 -->
									<input type="hidden" name="offerPartList.paperTonPrice" value="0" />
								</span>
							</div>
							<div class="offer_material_div insert_tr">
								<label>插页</label>
								<span class="ui-combo-wrap" style="margin-left: 3px">
									<phtml:list items="${fns:getOfferPaperList(offerType)}" cssClass="input-txt input-txt_26 hy_select" valueProperty="name" textProperty="name" defaultOption="请选择" name="offerPartList.paperName" onchange="selectPaperType(this)" />
								</span>
								<span class="ui-combo-wrap" style="margin-left: 3px">
									<select class="machine_sel" name="offerPartList.paperWeight" id="paperWeight3">
										<option value="0">请选择</option>
									</select>
									<!-- 吨价 -->
									<input type="hidden" name="offerPartList.paperTonPrice" value="0" />
								</span>
							</div>
						</td>
					</tr>
					<tr class="procedureTr">
						<td>封面工序</td>
						<td style="text-align: left;">
							<div class="dl">
								<c:forEach items="${procedureList }" varStatus="status" var="procList">
									<c:if test="${procList.switchStatus =='ENABLED' && procList.procedureType != 'FINISHED' }">
										<label class="label-checkbox item-content label_ui label_9">
											<input type="checkbox" name="partDetail.isChecked" class="check_procedure">
											<span class="item-media check_icon" style="margin-right: 0;">
												<i class="icon icon-form-checkbox"></i>
											</span>
											<span class="procedure_item_name">${procList.name}</span>
											<input type="hidden" name="partDetail.offerType" value="${procList.offerType }">
											<input type="hidden" name="partDetail.procedureType" value="${procList.procedureType }">
											<input type="hidden" name="partDetail.procedureClass" value="${procList.procedureClass }">
											<input type="hidden" name="partDetail.procedureId" value="${procList.id }">
											<input type="hidden" name="partDetail.price" value="${procList.price }">
											<input type="hidden" name="partDetail.procedureName" value="${procList.name }">
											<input type="hidden" name="partDetail.procedureUnit" value="${procList.procedureUnit }">
											<input type="hidden" name="partDetail.lowestPrice" value="${procList.lowestPrice }">
											<input type="hidden" name="partDetail.startPrice" value="${procList.startPrice }">
											<input type="hidden" name="partDetail.offerProcedureFormulaType" value="${procList.offerProcedureFormulaType }">
											<c:if test="${procList.procedureUnit == 'UNIT3'}">
												<input type="number" name="partDetail.length" class="constraint_negative ">
													×
												<input type="number" name="partDetail.width" class="constraint_negative ">
												  mm 
										  </c:if>
										</label>
									</c:if>
								</c:forEach>
							</div>
						</td>
					</tr>
					<tr class="procedureTr">
						<td>内页工序</td>
						<td style="text-align: left;">
							<div class="dl">
								<c:forEach items="${procedureList }" varStatus="status" var="procList">
									<c:if test="${procList.switchStatus =='ENABLED' && procList.procedureType != 'FINISHED' }">
										<label class="label-checkbox item-content label_ui label_9">
											<input type="checkbox" name="partDetail.isChecked" class="check_procedure">
											<span class="item-media check_icon" style="margin-right: 0;">
												<i class="icon icon-form-checkbox"></i>
											</span>
											<span class="procedure_item_name">${procList.name}</span>
											<input type="hidden" name="partDetail.offerType" value="${procList.offerType }">
											<input type="hidden" name="partDetail.procedureType" value="${procList.procedureType }">
											<input type="hidden" name="partDetail.procedureClass" value="${procList.procedureClass }">
											<input type="hidden" name="partDetail.procedureId" value="${procList.id }">
											<input type="hidden" name="partDetail.price" value="${procList.price }">
											<input type="hidden" name="partDetail.procedureName" value="${procList.name }">
											<input type="hidden" name="partDetail.procedureUnit" value="${procList.procedureUnit }">
											<input type="hidden" name="partDetail.lowestPrice" value="${procList.lowestPrice }">
											<input type="hidden" name="partDetail.startPrice" value="${procList.startPrice }">
											<input type="hidden" name="partDetail.offerProcedureFormulaType" value="${procList.offerProcedureFormulaType }">
											<c:if test="${procList.procedureUnit == 'UNIT3'}">
												<input type="number" name="partDetail.length" class="constraint_negative ">
													×
												<input type="number" name="partDetail.width" class="constraint_negative ">
												  mm 
										  </c:if>
										</label>
									</c:if>
								</c:forEach>
							</div>
						</td>
					</tr>
					<tr class="insert_tr procedureTr">
						<td>插页工序</td>
						<td style="text-align: left;">
							<div class="dl">
								<c:forEach items="${procedureList }" varStatus="status" var="procList">
									<c:if test="${procList.switchStatus =='ENABLED' && procList.procedureType != 'FINISHED' }">
										<label class="label-checkbox item-content label_ui label_9">
											<input type="checkbox" name="partDetail.isChecked" class="check_procedure">
											<span class="item-media check_icon" style="margin-right: 0;">
												<i class="icon icon-form-checkbox"></i>
											</span>
											<span class="procedure_item_name">${procList.name}</span>
											<input type="hidden" name="partDetail.offerType" value="${procList.offerType }">
											<input type="hidden" name="partDetail.procedureType" value="${procList.procedureType }">
											<input type="hidden" name="partDetail.procedureClass" value="${procList.procedureClass }">
											<input type="hidden" name="partDetail.procedureId" value="${procList.id }">
											<input type="hidden" name="partDetail.price" value="${procList.price }">
											<input type="hidden" name="partDetail.procedureName" value="${procList.name }">
											<input type="hidden" name="partDetail.procedureUnit" value="${procList.procedureUnit }">
											<input type="hidden" name="partDetail.lowestPrice" value="${procList.lowestPrice }">
											<input type="hidden" name="partDetail.startPrice" value="${procList.startPrice }">
											<input type="hidden" name="partDetail.offerProcedureFormulaType" value="${procList.offerProcedureFormulaType }">
											<c:if test="${procList.procedureUnit == 'UNIT3'}">
												<input type="number" name="partDetail.length" class="constraint_negative ">
													×
												<input type="number" name="partDetail.width" class="constraint_negative ">
												  mm 
										  </c:if>
										</label>
									</c:if>
								</c:forEach>
							</div>
						</td>
					</tr>
					<tr>
						<td>装订工序</td>
						<td style="text-align: left;" class="productProcedureTd">
							<div class="dl">
								<c:forEach items="${procedureList }" varStatus="status" var="procList">
									<c:if test="${procList.switchStatus =='ENABLED' && procList.procedureType == 'FINISHED' }">
										<label class="label-checkbox item-content label_ui label_9">
											<input type="checkbox" name="productDetail.isChecked" class="check_procedure">
											<span class="item-media check_icon" style="margin-right: 0;">
												<i class="icon icon-form-checkbox"></i>
											</span>
											<span class="procedure_item_name">${procList.name}</span>
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
										</label>
									</c:if>
								</c:forEach>
							</div>
						</td>
					</tr>
				</table>

				<!---------------------------------------- 公共 > POSTDATA --------------------------------------------->
				<%@include file="/WEB-INF/jsp/wx/offer/auto/_commonPost.jsp"%>
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
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<tr>
	<td>印刷颜色</td>
	<td class="offer_page_td">
		<div class="offer_color_div">
			<label>
				<span class="ui-combo-wrap" style="margin-left: 3px">
					<select style="" class="input-txt input-txt_3 hy_select" id="offerPrintStyleType" name="offerPartList.offerPrintStyleType">
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
					<phtml:list name="offerPartList.offerPrintColorType" textProperty="text" cssClass="input-txt input-txt_3 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferPrintColorType" />
				</span>
				<span class="ui-combo-wrap" style="margin-left: 3px">
					<phtml:list name="offerPartList.offerSpotColorType" textProperty="text" cssClass="input-txt input-txt_3 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferSpotColorType" />
				</span>
			</span>
			<div class="dl" style="position:relative;top:10px;">
			<span class="ui-combo-wrap">
				<a href="#" style="display: none;" class="delCopyTr" onclick="delCopyTr(this)">删除多部件</a>
			</span>
			</div>
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
	<td>印刷纸张</td>
	<td style="text-align: left;">
		<div class="dl">
			<span class="ui-combo-wrap">
				<phtml:list items="${fns:getOfferPaperList(offerType)}" cssClass="input-txt input-txt_3 hy_select" valueProperty="name" textProperty="name" defaultOption="请选择" name="offerPartList.paperName" onchange="selectPaperType(this)" />
			</span>
			<span class="ui-combo-wrap" style="margin-left: 3px">
				<select class="machine_sel" name="offerPartList.paperWeight" id="paperWeight">
					<option value="0">请选择</option>
				</select>
				<!-- 吨价 -->
				<input type="hidden" name="offerPartList.paperTonPrice" value="0" />
			</span>
			<span class="ui-combo-wrap" style="margin-left: 8px;">
				<phtml:list items="${fns:basicList('TAXRATE')}" cssClass="input-txt input-txt_3 hy_select" valueProperty="percent" textProperty="name" name="taxPercent" selected="" />
			</span>
		</div>
		
		<div class="dl" style="position:relative;top:10px;">
			<c:if test="${offerType == 'CARTONBOX' }">
				<span class="ui-combo-wrap">
					<label class="label-checkbox item-content">
						<input type="checkbox" style="margin-left: 23px;" name="offerPartList.containBflute" class="containBfluteCheck" >
						<span class="item-media check_icon" style="margin-right: 0;">
							<i class="icon icon-form-checkbox"></i>
						</span>
						<span class="procedure_item_name">加坑纸</span>
					</label>
				</span>
			</c:if>
		</div>
	</td>
</tr>
<tr class="bfluteTr" style="display: none;">
	<td>坑纸</td>
	<td style="text-align: left;">
		<div class="dl">
			<span class="ui-combo-wrap">
				<phtml:list items="${fns:getOfferBfluteList()}" cssClass="input-txt input-txt_3 hy_select" defaultValue="" valueProperty="pit" textProperty="pit" defaultOption="请选择" name="offerPartList.bflutePit" onchange="selectBfluteType(this)" />
			</span>
			<span class="ui-combo-wrap">
				<select class="machine_sel" name="offerPartList.bflutePaperQuality">
					<option value="0">请选择</option>
				</select>
			</span>
			<input type="hidden" name="offerPartList.bflutePrice" />
		</div>
	</td>
</tr>
<tr class="procedureTr">
	<td>后道工序</td>
	<td style="text-align: left;">
		<div class="dl">
			<c:forEach items="${procedureList }" varStatus="status" var="procList">
				<c:if test="${procList.switchStatus =='ENABLED' && procList.procedureType != 'FINISHED' }">
					<label class="label-checkbox item-content label_ui label_9">
						<input type="checkbox" class="check_procedure" name="partDetail.isChecked">
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
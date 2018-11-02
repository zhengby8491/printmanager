<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<input type="hidden" name="paperType">
<tr>
	<td>印刷颜色</td>
	<td style="text-align: left;">
		<div class="dl" style="margin-left: 23px">
			<span class="ui-combo-wrap">
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

			<span class="ui-combo-wrap" style="margin-left: 3px">
				<span class="mgl">正面</span>
				<phtml:list name="offerPartList.offerPrintColorType" textProperty="text" cssClass="input-txt input-txt_3 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferPrintColorType" />
			</span>
			<span class="ui-combo-wrap" style="margin-left: 42px">
				<phtml:list name="offerPartList.offerSpotColorType" textProperty="text" cssClass="input-txt input-txt_3 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferSpotColorType" />
			</span>
			<span class="doubleColor" style="display: none;">
				<span class="ui-combo-wrap" style="margin-left: 3px">
					<span class="mgl">反面</span>
					<phtml:list name="offerPartList.offerPrintColorType2" textProperty="text" cssClass="input-txt input-txt_3 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferPrintColorType" />
				</span>
				<span class="ui-combo-wrap" style="margin-left: 42px">
					<phtml:list name="offerPartList.offerSpotColorType2" textProperty="text" cssClass="input-txt input-txt_3 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferSpotColorType" />
				</span>
			</span>
			<span class="ui-combo-wrap" style="margin-left: 42px">
				<a href="#" class="delCopyTr" style="display: none;" onclick="delCopyTr(this)">删除多部件</a>
			</span>
		</div>
	</td>
</tr>
<tr>
	<td>印刷纸张</td>
	<td style="text-align: left;">
		<div class="dl" style="margin-left: 23px">
			<span class="ui-combo-wrap">
				<phtml:list items="${fns:getOfferPaperList(offerType)}" cssClass="input-txt input-txt_3 hy_select" valueProperty="name" textProperty="name" defaultOption="请选择" name="offerPartList.paperName" onchange="selectPaperType(this)" />
			</span>
			<span class="ui-combo-wrap" style="margin-left: 3px">
				<span class="mgl">克重</span>
				<select class="machine_sel" name="offerPartList.paperWeight" id="paperWeight">
					<option value="0">请选择</option>
				</select>
				<!-- 吨价 -->
				<input type="hidden" name="offerPartList.paperTonPrice" value="0" />
			</span>

			<span class="ui-combo-wrap" style="margin-left: 42px">
				<phtml:list items="${fns:basicList('TAXRATE')}" cssClass="input-txt input-txt_3 hy_select" valueProperty="percent" textProperty="name" name="taxPercent" />
			</span>

			<span class="ui-combo-wrap" style="margin-left: 19px">
				<label class="offer-label label_ui">
					<input type="checkbox" name="offerPartList.customPaper">
					客来纸
				</label>
			</span>
			<c:if test="${offerType == 'CARTONBOX' }">
				<span class="ui-combo-wrap" style="margin-left: 3px">
					<label class="offer-label label_ui">
						<input type="checkbox" style="margin-left: 23px;" name="offerPartList.containBflute">
						加坑纸
					</label>
				</span>
			</c:if>
		</div>
	</td>
</tr>
<tr class="bfluteTr" style="display: none;">
	<td>坑纸</td>
	<td style="text-align: left;">
		<div class="dl" style="margin-left: 23px">
			<span class="ui-combo-wrap">
				<phtml:list items="${fns:getOfferBfluteList()}" cssClass="input-txt input-txt_3 hy_select" defaultValue="" valueProperty="pit" textProperty="pit" defaultOption="请选择" name="offerPartList.bflutePit" onchange="selectBfluteType(this)" />
			</span>
			<span class="ui-combo-wrap" style="margin-left: 42px">
				<select class="machine_sel" name="offerPartList.bflutePaperQuality">
					<option value="0">请选择</option>
				</select>
			</span>
			<!-- TODO 坑纸单价 -->
			<input type="hidden" name="offerPartList.bflutePrice" />
		</div>
	</td>
</tr>
<tr>
	<td>后道工序</td>
	<td style="text-align: left;" class="procedureTd">
		<div class="dl" style="margin-left: 23px">
			<c:forEach items="${procedureList }" varStatus="status" var="procList">
				<c:if test="${procList.switchStatus =='ENABLED' && procList.procedureType != 'FINISHED' }">
					<label class="offer-label label_ui label_10">
						<input type="checkbox" name="partDetail.isChecked">
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
						<span>${procList.name }</span>
						<c:if test="${procList.procedureUnit == 'UNIT3'}">
							<input type="text" name="partDetail.length" class="constraint_decimal_negative input-txt input-txt_28 whiteBg">
							×
							<input type="text" name="partDetail.width" class="constraint_decimal_negative input-txt input-txt_28 whiteBg">
						  mm 
					  </c:if>
				</c:if>
				</label>
			</c:forEach>
		</div>
	</td>
</tr>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/offer/auto/common.js?v=${v}"></script>
<style type="text/css">
.machine_sel {
	width: 69px !important;
}
</style>
<print:body>
	<!-- 导航按钮 -->
	<print:nav title="报价系统-自动报价-${offerType.text }自动报价">
		<shiro:hasPermission name="offer:auto:book:save">
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
						<th width="60" colspan="2"><span style="font-size: 20px; font-family: 'STHeiti'"> <strong>${offerType.text }自动报价</strong> <input type="hidden" id="offerType" value="${offerType }">
						</span> <span style="font-size: 11px;">：画册，书刊，精装书，书籍</span></th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td width="10%">成品尺寸（mm）</td>
						<td width="300px" style="text-align: left;"><span class="ui-combo-wrap" style="margin-left: 23px"> <phtml:list name="spec" textProperty="text" valueProperty="style" cssClass="input-txt input-txt_3 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferSpecType" selected=""></phtml:list>
						</span> <!---------------------------------------- 公共 > 自定义尺寸填写 ---------------------------------------------> <%@include file="/WEB-INF/jsp/offer/auto/_commonStyleSpan.jsp"%></td>
					</tr>
					<tr>
						<td width="120px">印刷数量</td>
						<td width="300px" style="text-align: left;"><input type="text" style="margin-left: 23px" class="constraint_decimal_negative input-txt input-txt_29 whiteBg" id="amount" name="amount">
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
					<tr>
						<td>封面</td>
						<td style="text-align: left;">
							<div class="dl" style="margin-left: 23px">
								<input type="hidden" name="offerPartList.partName" value="封面"> <span class="ui-combo-wrap"> <sys:selectEditNew name="offerPartList.pages" inputCss="constraint_decimal_negative input-txt_25 whiteBg hy_select" defaultVal="4">
										<option>0</option>
										<option>2</option>
										<option>4</option>
										<option>8</option>
									</sys:selectEditNew> P
								</span> <span class="ui-combo-wrap" style="margin-left: 22px"> <phtml:list items="${fns:getOfferPaperList(offerType)}" cssClass="input-txt input-txt_26 hy_select" valueProperty="name" textProperty="name" defaultOption="请选择" name="offerPartList.paperName" onchange="selectPaperType(this)" />
								</span> <span class="ui-combo-wrap" style="margin-left: 7px"> <select class="machine_sel" name="offerPartList.paperWeight" id="paperWeight">
										<option value="0">请选择</option>
								</select> <!-- 吨价 --> <input type="hidden" name="offerPartList.paperTonPrice" />
								</span> <span class="ui-combo-wrap" style="margin-left: 7px"> <select style="" class="input-txt input-txt_26 hy_select" id="offerPrintStyleType" name="offerPartList.offerPrintStyleType">
										<option value="SINGLE">单面印刷</option>
										<c:if test="${offerType == 'SINGLE' }">
											<option value="DOUBLE">双面印刷</option>
										</c:if>
										<c:if test="${offerType == 'ALBUMBOOK'}">
											<option value="HEADTAIL">正反印刷</option>
											<option value="CHAOS">自翻印刷</option>
										</c:if>
								</select>
								</span> <span class="ui-combo-wrap" style="margin-left: 7px"> <span class="mgl">正面</span> <phtml:list name="offerPartList.offerPrintColorType" textProperty="text" cssClass="input-txt input-txt_26 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferPrintColorType" />
								</span> <span class="ui-combo-wrap" style="margin-left: 7px"> <phtml:list name="offerPartList.offerSpotColorType" textProperty="text" cssClass="input-txt input-txt_26 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferSpotColorType" />
								</span> <span class="doubleColor" style="display: none;"> <span class="ui-combo-wrap" style="margin-left: 7px"> <span class="mgl">反面</span> <phtml:list name="offerPartList.offerPrintColorType2" textProperty="text" cssClass="input-txt input-txt_26 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferPrintColorType" />
								</span> <span class="ui-combo-wrap" style="margin-left: 7px"> <phtml:list name="offerPartList.offerSpotColorType2" textProperty="text" cssClass="input-txt input-txt_26 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferSpotColorType" />
								</span>
								</span> <span class="ui-combo-wrap" style="margin-left: 7px"> <label class="offer-label label_ui"> <input type="checkbox" name="offerPartList.customPaper"> 客来纸
								</label>
								</span> <span class="ui-combo-wrap" style="margin-left: 23px"> <phtml:list items="${fns:basicList('TAXRATE')}" cssClass="input-txt input-txt_3 hy_select" valueProperty="percent" textProperty="name" name="taxPercent" />
								</span>
							</div>
						</td>
					</tr>
					<tr>
						<td>内页</td>
						<td style="text-align: left;">
							<div class="dl" style="margin-left: 23px">
								<input type="hidden" name="offerPartList.partName" value="内页"> <span class="ui-combo-wrap"> <sys:selectEditNew name="offerPartList.pages" inputCss="constraint_decimal_negative input-txt_25 whiteBg hy_select" defaultVal="32">
										<option>0</option>
										<option>4</option>
										<option>8</option>
										<option>12</option>
										<option>16</option>
										<option>20</option>
										<option>24</option>
										<option>28</option>
										<option>32</option>
										<option>36</option>
										<option>40</option>
										<option>44</option>
										<option>48</option>
										<option>52</option>
										<option>56</option>
										<option>60</option>
										<option>64</option>
										<option>68</option>
										<option>72</option>
										<option>76</option>
										<option>80</option>
										<option>84</option>
										<option>88</option>
										<option>92</option>
										<option>96</option>
									</sys:selectEditNew> P
								</span> <span class="ui-combo-wrap" style="margin-left: 22px"> <phtml:list items="${fns:getOfferPaperList(offerType)}" cssClass="input-txt input-txt_26 hy_select" valueProperty="name" textProperty="name" defaultOption="请选择" name="offerPartList.paperName" onchange="selectPaperType(this)" />
								</span> <span class="ui-combo-wrap" style="margin-left: 7px"> <select class="machine_sel " name="offerPartList.paperWeight" id="paperWeight2">
										<option value="0">请选择</option>
								</select> <!-- 吨价 --> <input type="hidden" name="offerPartList.paperTonPrice" />
								</span> <span class="ui-combo-wrap" style="margin-left: 7px"> <select style="" class="input-txt input-txt_26 hy_select" id="offerPrintStyleType" name="offerPartList.offerPrintStyleType">
										<option value="SINGLE">单面印刷</option>
										<c:if test="${offerType == 'SINGLE' }">
											<option value="DOUBLE">双面印刷</option>
										</c:if>
										<c:if test="${offerType == 'ALBUMBOOK'}">
											<option value="HEADTAIL">正反印刷</option>
											<option value="CHAOS">自翻印刷</option>
										</c:if>
								</select>
								</span> <span class="ui-combo-wrap" style="margin-left: 7px"> <span class="mgl">正面</span> <phtml:list name="offerPartList.offerPrintColorType" textProperty="text" cssClass="input-txt input-txt_26 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferPrintColorType" />
								</span> <span class="ui-combo-wrap" style="margin-left: 7px"> <phtml:list name="offerPartList.offerSpotColorType" textProperty="text" cssClass="input-txt input-txt_26 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferSpotColorType" />
								</span> <span class="doubleColor" style="display: none;"> <span class="ui-combo-wrap" style="margin-left: 7px"> <span class="mgl">反面</span> <phtml:list name="offerPartList.offerPrintColorType2" textProperty="text" cssClass="input-txt input-txt_26 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferPrintColorType" />
								</span> <span class="ui-combo-wrap" style="margin-left: 7px"> <phtml:list name="offerPartList.offerSpotColorType2" textProperty="text" cssClass="input-txt input-txt_26 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferSpotColorType" />
								</span>
								</span> <span class="ui-combo-wrap" style="margin-left: 7px"> <label class="offer-label label_ui"> <input type="checkbox" name="offerPartList.customPaper"> 客来纸
								</label>
								</span> <span class="ui-combo-wrap" style="margin-left: 23px;"> <a herf="javascript:;" id="btn_add_insertP">添加插页</a>
								</span>
							</div>
						</td>
					</tr>
					<tr class="insert_tr">
						<td>插页</td>
						<td style="text-align: left;">
							<div class="dl" style="margin-left: 23px">
								<input type="hidden" name="offerPartList.partName" value="插页"> <span class="ui-combo-wrap"> <sys:selectEditNew name="offerPartList.pages" inputCss="constraint_decimal_negative input-txt_25 whiteBg hy_select" defaultVal="32">
										<option>0</option>
										<option>4</option>
										<option>8</option>
										<option>12</option>
										<option>16</option>
										<option>20</option>
										<option>24</option>
										<option>28</option>
										<option>32</option>
										<option>36</option>
										<option>40</option>
										<option>44</option>
										<option>48</option>
										<option>52</option>
										<option>56</option>
										<option>60</option>
										<option>64</option>
										<option>68</option>
										<option>72</option>
										<option>76</option>
										<option>80</option>
										<option>84</option>
										<option>88</option>
										<option>92</option>
										<option>96</option>
									</sys:selectEditNew> P
								</span> <span class="ui-combo-wrap" style="margin-left: 22px"> <phtml:list items="${fns:getOfferPaperList(offerType)}" cssClass="input-txt input-txt_26 hy_select" valueProperty="name" textProperty="name" defaultOption="请选择" name="offerPartList.paperName" onchange="selectPaperType(this)" />
								</span> <span class="ui-combo-wrap" style="margin-left: 7px"> <select class="machine_sel" name="offerPartList.paperWeight" id="paperWeight3">
										<option value="0">请选择</option>
								</select> <!-- 吨价 --> <input type="hidden" name="offerPartList.paperTonPrice" />
								</span> <span class="ui-combo-wrap" style="margin-left: 7px"> <select style="" class="input-txt input-txt_26 hy_select" id="offerPrintStyleType" name="offerPartList.offerPrintStyleType">
										<option value="SINGLE">单面印刷</option>
										<c:if test="${offerType == 'SINGLE' }">
											<option value="DOUBLE">双面印刷</option>
										</c:if>
										<c:if test="${offerType == 'ALBUMBOOK'}">
											<option value="HEADTAIL">正反印刷</option>
											<option value="CHAOS">自翻印刷</option>
										</c:if>
								</select>
								</span> <span class="ui-combo-wrap" style="margin-left: 7px"> <span class="mgl">正面</span> <phtml:list name="offerPartList.offerPrintColorType" textProperty="text" cssClass="input-txt input-txt_26 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferPrintColorType" />
								</span> <span class="ui-combo-wrap" style="margin-left: 7px"> <phtml:list name="offerPartList.offerSpotColorType" textProperty="text" cssClass="input-txt input-txt_26 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferSpotColorType" selected="" />
								</span> <span class="doubleColor" style="display: none;"> <span class="ui-combo-wrap" style="margin-left: 7px"> <span class="mgl">反面</span> <phtml:list name="offerPartList.offerPrintColorType2" textProperty="text" cssClass="input-txt input-txt_26 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferPrintColorType" />
								</span> <span class="ui-combo-wrap" style="margin-left: 7px"> <phtml:list name="offerPartList.offerSpotColorType2" textProperty="text" cssClass="input-txt input-txt_26 hy_select" type="com.huayin.printmanager.persist.enumerate.OfferSpotColorType" />
								</span>
								</span> <span class="ui-combo-wrap" style="margin-left: 7px"> <label class="offer-label label_ui"> <input type="checkbox" name="offerPartList.customPaper"> 客来纸
								</label>
								</span>
							</div>
						</td>
					</tr>
					<tr>
						<td>封面工序</td>
						<td style="text-align: left;" class="procedureTd">
							<div class="dl" style="margin-left: 23px">
								<c:forEach items="${procedureList }" varStatus="status" var="procList">
									<c:if test="${procList.switchStatus =='ENABLED' && procList.procedureType != 'FINISHED' }">
										<label class="offer-label label_ui label_10"> <input type="checkbox" name="partDetail.isChecked"> <input type="hidden" name="partDetail.offerType" value="${procList.offerType }"> <input type="hidden" name="partDetail.procedureType" value="${procList.procedureType }"> <input type="hidden" name="partDetail.procedureClass" value="${procList.procedureClass }"> <input type="hidden" name="partDetail.procedureId" value="${procList.id }"> <input type="hidden" name="partDetail.price" value="${procList.price }"> <input type="hidden" name="partDetail.procedureName" value="${procList.name }"> <input type="hidden" name="partDetail.procedureUnit"
											value="${procList.procedureUnit }"> <input type="hidden" name="partDetail.lowestPrice" value="${procList.lowestPrice }"> <input type="hidden" name="partDetail.startPrice" value="${procList.startPrice }"> <input type="hidden" name="partDetail.offerProcedureFormulaType" value="${procList.offerProcedureFormulaType }"> <span>${procList.name }</span> <c:if test="${procList.procedureUnit == 'UNIT3'}">
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
					<tr>
						<td>内页工序</td>
						<td style="text-align: left;" class="procedureTd">
							<div style="margin-left: 23px">
								<c:forEach items="${procedureList }" varStatus="status" var="procList">
									<c:if test="${procList.switchStatus =='ENABLED' && procList.procedureType != 'FINISHED' }">
										<label class="offer-label label_ui label_10"> <input type="checkbox" name="partDetail.isChecked"> <input type="hidden" name="partDetail.offerType" value="${procList.offerType }"> <input type="hidden" name="partDetail.procedureType" value="${procList.procedureType }"> <input type="hidden" name="partDetail.procedureClass" value="${procList.procedureClass }"> <input type="hidden" name="partDetail.procedureId" value="${procList.id }"> <input type="hidden" name="partDetail.price" value="${procList.price }"> <input type="hidden" name="partDetail.procedureName" value="${procList.name }"> <input type="hidden" name="partDetail.procedureUnit"
											value="${procList.procedureUnit }"> <input type="hidden" name="partDetail.lowestPrice" value="${procList.lowestPrice }"> <input type="hidden" name="partDetail.startPrice" value="${procList.startPrice }"> <input type="hidden" name="partDetail.offerProcedureFormulaType" value="${procList.offerProcedureFormulaType }"> <span>${procList.name }</span> <c:if test="${procList.procedureUnit == 'UNIT3'}">
												<input type="text" name="partDetail.length" class="constraint_decimal_negative input-txt input-txt_28 whiteBg">
												×
												<input type="text" name="partDetail.width" class="constraint_decimal_negative input-txt input-txt_28 whiteBg">
												mm 
											</c:if>
										</label>
									</c:if>
								</c:forEach>
							</div>
						</td>
					</tr>
					<tr class="insert_tr">
						<td>插页工序</td>
						<td style="text-align: left;" class="procedureTd">
							<div style="margin-left: 23px">
								<c:forEach items="${procedureList }" varStatus="status" var="procList">
									<c:if test="${procList.switchStatus =='ENABLED' && procList.procedureType != 'FINISHED' }">
										<label class="offer-label label_ui label_10"> <input type="checkbox" name="partDetail.isChecked"> <input type="hidden" name="partDetail.offerType" value="${procList.offerType }"> <input type="hidden" name="partDetail.procedureType" value="${procList.procedureType }"> <input type="hidden" name="partDetail.procedureClass" value="${procList.procedureClass }"> <input type="hidden" name="partDetail.procedureId" value="${procList.id }"> <input type="hidden" name="partDetail.price" value="${procList.price }"> <input type="hidden" name="partDetail.procedureName" value="${procList.name }"> <input type="hidden" name="partDetail.procedureUnit"
											value="${procList.procedureUnit }"> <input type="hidden" name="partDetail.lowestPrice" value="${procList.lowestPrice }"> <input type="hidden" name="partDetail.startPrice" value="${procList.startPrice }"> <input type="hidden" name="partDetail.offerProcedureFormulaType" value="${procList.offerProcedureFormulaType }"> <span>${procList.name }</span> <c:if test="${procList.procedureUnit == 'UNIT3'}">
												<input type="text" name="partDetail.length" class="constraint_decimal_negative input-txt input-txt_28 whiteBg">
												×
												<input type="text" name="partDetail.width" class="constraint_decimal_negative input-txt input-txt_28 whiteBg">
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
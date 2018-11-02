<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<style type="text/css">
.valu_title_show {
	border-bottom: 1px solid white;
	background: white;
}

.valu_title_hide {
	border-bottom: 1px solid #aaa;
	background: #f1f1f1;
}
</style>
<link rel="stylesheet" type="text/css" href="${ctxHYUI}/plugins/codemirror/codemirror.css" />
<script type="text/javascript" src="${ctxHYUI}/plugins/codemirror/codemirror.js"></script>
<script type="text/javascript" src="${ctxStatic}/site/offer/setting/machineEdit.js?v=${v}"></script>
</head>
<print:body>
	<print:nav title="报价系统-报价设置-${offerType.text }-机台设置-修改机台">
		<shiro:hasOfferPermissions name="machine:edit" offerType="${offerType }">
			<button class="nav_btn table_nav_btn" id="btnSave">保存</button>
		</shiro:hasOfferPermissions>
		<button class="nav_btn table_nav_btn" id="btnCancel">取消</button>
	</print:nav>

	<print:form>
		<form class="form-wrap" id="settingMachineForm">
			<input type="hidden" name="machine.offerType" id="machineOfferType" value="${offerType }"> <input type="hidden" name="machine.id" id="machineId" value="${machine.id }">
			<fieldset class="cl form_content">
				<dl class="cl row-dl">
					<dd class="row-dd2">
						<label class="form-label label_ui label_m1">机台名称：</label> <span class="ui-combo-wrap"> <input type="text" name="machine.name" id="machineName" class="input-txt input-txt_3" value="${machine.name }" />
						</span>
					</dd>
					<dd class="row-dd2">
						<label class="form-label label_ui label_m1">机台属性：</label> <span class="ui-combo-wrap"> <phtml:list cssClass="input-txt input-txt_3 hy_select2" name="machine.machinePro" textProperty="text" type="com.huayin.printmanager.persist.enumerate.MachinePro" selected="${machine.machinePro }"></phtml:list>
						</span>
					</dd>
					<dd class="row-dd2">
						<label class="form-label label_ui label_m1">机台印色：</label> <span class="ui-combo-wrap"> <phtml:list cssClass="input-txt input-txt_3 hy_select2" name="machine.machinePrintColor" textProperty="text" type="com.huayin.printmanager.persist.enumerate.MachinePrintColor" selected="${machine.machinePrintColor }"></phtml:list>
						</span>
					</dd>
				</dl>
				<dl class="cl row-dl">
					<dd class="row-dd2">
						<label class="form-label label_ui label_m1">最大上机(mm)：</label> <span class="ui-combo-wrap"> <input type="text" name="machine.maxStyle" id="machineMaxStyle" class="constraint_style input-txt input-txt_3" value="${machine.maxStyle }" placeholder="xxxx*xxxx" />
						</span>
					</dd>
					<dd class="row-dd2">
						<label class="form-label label_ui label_m1">最小上机(mm)：</label> <span class="ui-combo-wrap"> <input type="text" name="machine.minStyle" id="machineMinStyle" class="constraint_style input-txt input-txt_3" value="${machine.minStyle }" placeholder="xxxx*xxxx" />
						</span>
					</dd>
					<dd class="row-dd2">
						<label class="form-label label_ui label_m1">最少印刷厚度(g)：</label> <span class="ui-combo-wrap"> <input type="text" name="machine.minPrintPly" id="machineMinPrintPly" class="constraint_negative input-txt input-txt_3" value="${machine.minPrintPly }" />
						</span>
					</dd>
					<dd class="row-dd2">
						<label class="form-label label_ui label_m1">最大印刷厚度(g)：</label> <span class="ui-combo-wrap"> <input type="text" name="machine.maxPrintPly" id="machineMaxPrintPly" class="constraint_negative input-txt input-txt_3" value="${machine.maxPrintPly }" />
						</span>
					</dd>
				</dl>
				<dl class="cl row-dl">
					<div class="valuation_div" style="width: 100%; height: 560px;">
						<div class="valuation_title" style="width: 100%;">
							<div class="valuation_title_left <c:if test="${machine.offerMachineType == 'START_PRINT' }">valu_title_show</c:if>" id="valuation_title_left" style="width: 49.9%;">
								<div>
									<span class="valuation_span">开机费+印工计价</span>
								</div>
							</div>
							<div class="valuation_title_right <c:if test="${machine.offerMachineType == 'CUSTOM' }">valu_title_show</c:if>" id="valuation_title_right" style="width: 49.9%;">
								<div>
									<span class="valuation_span">自定义公式计价</span>
								</div>
							</div>
						</div>
						<!--  开机费+印工计价   -->
						<div class="fixed-div" id="fixed" style="width: 50%; <c:if test="${machine.offerMachineType != 'START_PRINT' }">display: none;</c:if>">
							<div>
								<span class="checkBox_span"> <input type="hidden" name="start.id" value="${machine.offerStartPrint != null ? machine.offerStartPrint.id : '' }"> <input type="checkbox" name="start.joinSpotColor" id="spotColorCheckbox" <c:if test="${machine.offerStartPrint != null && machine.offerStartPrint.joinSpotColor == 'YES' }">checked="checked"</c:if>> <label for="spotColorCheckbox">加入专色计价</label>
								</span> <span class="checkBox_span"> <input type="checkbox" name="start.joinReamColor" id="reamColorCheckbox" <c:if test="${machine.offerStartPrint != null && machine.offerStartPrint.joinReamColor == 'YES' }">checked="checked"</c:if>> <label for="reamColorCheckbox">加入色令计价</label>
								</span>
							</div>
							<div>
								<table class="valuation_table">
									<thead>
										<tr>
											<th colspan="2">起步价（含版费）</th>
											<th colspan="2">印工费（元/千次）</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td width="200px">开机费</td>
											<td width="200px">包含印次</td>
											<td width="200px"><sys:selectEdit name="start.thousandSpeedBelow" id="machineThousandSpeedBelow" css="constraint_negative input-txt" style="display: inline-block; width: 50px;" selectStyle="width: 50px;" value="${machine.offerStartPrint != null ? machine.offerStartPrint.thousandSpeedBelow : '' }">
													<div class=select_edit_item>1000</div>
													<div class=select_edit_item>2000</div>
													<div class=select_edit_item>3000</div>
													<div class=select_edit_item>4000</div>
													<div class=select_edit_item>5000</div>
													<div class=select_edit_item>6000</div>
													<div class=select_edit_item>7000</div>
													<div class=select_edit_item>8000</div>
													<div class=select_edit_item>9000</div>
													<div class=select_edit_item>10000</div>
												</sys:selectEdit> <span style="padding-left: 5px;">次以下</span></td>
											<td width="200px"><input type="text" name="start.thousandSpeedAbove" id="machineThousandSpeedAbove" class="constraint_negative input-txt input-txt_25" value="${machine.offerStartPrint != null ? machine.offerStartPrint.thousandSpeedAbove : '' }" readonly="readonly" /> <span>次以上</span></td>
										</tr>
										<tr>
											<td width="200px"><input type="text" name="start.startFee" id="machineStartFee" class="constraint_negative input-txt input-txt_25" value="${machine.offerStartPrint != null ? machine.offerStartPrint.startFee : '' }" /> <span>元</span></td>
											<td width="200px"><input type="text" name="start.startSpeed" id="machineStartSpeed" class="constraint_negative input-txt input-txt_25" value="${machine.offerStartPrint != null ? machine.offerStartPrint.startSpeed : '' }" /></td>
											<td width="200px"><input type="text" name="start.thousandSpeedBelowMoney" id="machineThousandSpeedBelowMoney" class="constraint_negative input-txt input-txt_25" value="${machine.offerStartPrint != null ? machine.offerStartPrint.thousandSpeedBelowMoney : '' }" /> <span>元/千次</span></td>
											<td width="200px"><input type="text" name="start.thousandSpeedAboveMoney" id="machineThousandSpeedAboveMoney" class="constraint_negative input-txt input-txt_25" value="${machine.offerStartPrint != null ? machine.offerStartPrint.thousandSpeedAboveMoney : '' }" /> <span>元/千次</span></td>
										</tr>
									</tbody>
								</table>
							</div>
							<div>
								<!-- 加入专色计价 -->
								<div id="spotColorDiv" style="<c:if test="${machine.offerStartPrint != null && machine.offerStartPrint.joinSpotColor == 'NO' }">display: none;</c:if>">
									<span class="red-font">&nbsp;普色+专色（如果有普色+专色时，按普色+专色计价：开机费+印工单价*（印数-1000））</span>
									<table class="valuation_table">
										<thead>
											<tr>
												<th colspan="2">起步价（含版费）</th>
												<th colspan="2">印工费（元/千次）</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td width="200px">开机费</td>
												<td width="200px">包含印次</td>
												<td width="200px"><sys:selectEdit name="start.spotColorThousandSpeedBelow" id="machineSpotColorThousandSpeedBelow" css="constraint_negative input-txt" style="display: inline-block; width: 50px;" selectStyle="width: 50px;" value="${machine.offerStartPrint != null ? machine.offerStartPrint.spotColorThousandSpeedBelow : '' }">
														<div class=select_edit_item>1000</div>
														<div class=select_edit_item>2000</div>
														<div class=select_edit_item>3000</div>
														<div class=select_edit_item>4000</div>
														<div class=select_edit_item>5000</div>
														<div class=select_edit_item>6000</div>
														<div class=select_edit_item>7000</div>
														<div class=select_edit_item>8000</div>
														<div class=select_edit_item>9000</div>
														<div class=select_edit_item>10000</div>
													</sys:selectEdit> <span style="padding-left: 5px;">次以下</span></td>
												<td width="200px"><input type="text" name="start.spotColorThousandSpeedAbove" id="machineSpotColorThousandSpeedAbove" class="constraint_negative input-txt input-txt_25" value="${machine.offerStartPrint != null ? machine.offerStartPrint.spotColorThousandSpeedAbove : '' }" readonly="readonly" /> <span>次以上</span></td>
											</tr>
											<tr>
												<td width="200px"><input type="text" name="start.spotColorStartFee" id="machineSpotColorStartFee" class="constraint_negative input-txt input-txt_25" value="${machine.offerStartPrint != null ? machine.offerStartPrint.spotColorStartFee : '' }" /> <span>元</span></td>
												<td width="200px"><input type="text" name="start.spotColorStartSpeed" id="machineSpotColorStartSpeed" class="constraint_negative input-txt input-txt_25" value="${machine.offerStartPrint != null ? machine.offerStartPrint.spotColorStartSpeed : '' }" /></td>
												<td width="200px"><input type="text" name="start.spotColorThousandSpeedBelowMoney" id="machineSpotColorThousandSpeedBelowMoney" class="constraint_negative input-txt input-txt_25" value="${machine.offerStartPrint != null ? machine.offerStartPrint.spotColorThousandSpeedBelowMoney : '' }" /> <span>元/千次</span></td>
												<td width="200px"><input type="text" name="start.spotColorThousandSpeedAboveMoney" id="machineSpotColorThousandSpeedAboveMoney" class="constraint_negative input-txt input-txt_25" value="${machine.offerStartPrint != null ? machine.offerStartPrint.spotColorThousandSpeedAboveMoney : '' }" /> <span>元/千次</span></td>
											</tr>
										</tbody>
									</table>
								</div>
								<!-- 单印专色 -->
								<div id="spotColorDiv2" style="<c:if test="${machine.offerStartPrint != null && machine.offerStartPrint.joinSpotColor == 'NO' }">display: none;</c:if>">
									<span class="red-font">&nbsp;单印专色（如果只有专色时，按专色计价：开机费+印工单价*（印数-1000））</span>
									<table class="valuation_table">
										<thead>
											<tr>
												<th colspan="2">起步价（含版费）</th>
												<th colspan="2">印工费（元/千次）</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td width="200px">开机费</td>
												<td width="200px">包含印次</td>
												<td width="200px"><sys:selectEdit name="start.spotColor2ThousandSpeedBelow" id="machineSpotColor2ThousandSpeedBelow" css="constraint_negative input-txt" style="display: inline-block; width: 50px;" selectStyle="width: 50px;" value="${machine.offerStartPrint != null ? machine.offerStartPrint.spotColor2ThousandSpeedBelow : '' }">
														<div class=select_edit_item>1000</div>
														<div class=select_edit_item>2000</div>
														<div class=select_edit_item>3000</div>
														<div class=select_edit_item>4000</div>
														<div class=select_edit_item>5000</div>
														<div class=select_edit_item>6000</div>
														<div class=select_edit_item>7000</div>
														<div class=select_edit_item>8000</div>
														<div class=select_edit_item>9000</div>
														<div class=select_edit_item>10000</div>
													</sys:selectEdit> <span style="padding-left: 5px;">次以下</span></td>
												<td width="200px"><input type="text" name="start.spotColor2ThousandSpeedAbove" id="machineSpotColor2ThousandSpeedAbove" class="constraint_negative input-txt input-txt_25" value="${machine.offerStartPrint != null ? machine.offerStartPrint.spotColor2ThousandSpeedAbove : '' }" readonly="readonly" /> <span>次以上</span></td>
											</tr>
											<tr>
												<td width="200px"><input type="text" name="start.spotColor2StartFee" id="machineSpotColor2StartFee" class="constraint_negative input-txt input-txt_25" value="${machine.offerStartPrint != null ? machine.offerStartPrint.spotColor2StartFee : '' }" /> <span>元</span></td>
												<td width="200px"><input type="text" name="start.spotColor2StartSpeed" id="machineSpotColor2StartSpeed" class="constraint_negative input-txt input-txt_25" value="${machine.offerStartPrint != null ? machine.offerStartPrint.spotColor2StartSpeed : '' }" /></td>
												<td width="200px"><input type="text" name="start.spotColor2ThousandSpeedBelowMoney" id="machineSpotColor2ThousandSpeedBelowMoney" class="constraint_negative input-txt input-txt_25" value="${machine.offerStartPrint != null ? machine.offerStartPrint.spotColor2ThousandSpeedBelowMoney : '' }" /> <span>元/千次</span></td>
												<td width="200px"><input type="text" name="start.spotColor2ThousandSpeedAboveMoney" id="machineSpotColor2ThousandSpeedAboveMoney" class="constraint_negative input-txt input-txt_25" value="${machine.offerStartPrint != null ? machine.offerStartPrint.spotColor2ThousandSpeedAboveMoney : '' }" /> <span>元/千次</span></td>
											</tr>
										</tbody>
									</table>
								</div>
								<!-- 加入色令计算  -->
								<div id="reamColorDiv" style="<c:if test="${machine.offerStartPrint != null && machine.offerStartPrint.joinReamColor == 'NO' }">display: none;</c:if>">
									<span class="red-font">&nbsp;加入色令计算（超出色令印数的将按照色令+版费计价：色令价*色数*印数/1000+版费*色数）</span>
									<table id="" class="valuation_table">
										<thead>
											<th width="270px">色令印数开始值</th>
											<th width="270px">色令价</th>
											<th width="260px">版费</th>
										</thead>
										<tbody>
											<tr>
												<td><input type="text" name="start.reamColorStartSpeed" id="machineReamColorStartSpeed" class="constraint_negative input-txt input-txt_25" value="${machine.offerStartPrint != null ? machine.offerStartPrint.reamColorStartSpeed : '' }" /> <span>次</span></td>
												<td><input type="text" name="start.reamColorMoney" id="machineReamColorMoney" class="constraint_negative input-txt input-txt_25" value="${machine.offerStartPrint != null ? machine.offerStartPrint.reamColorMoney : '' }" /> <span>元/千次</span></td>
												<td><input type="text" name="start.reamColorCopyFee" id="machineReamColorCopyFee" class="constraint_negative input-txt input-txt_25" value="${machine.offerStartPrint != null ? machine.offerStartPrint.reamColorCopyFee : '' }" /> <span>元/版</span></td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>

							<!-- 按钮 -->
							<div style="text-align: center;">
								<input type="button" class="btn test_btn" id="startPrintTestBtn" value="测试" />
							</div>
						</div>

						<!-- 测试费用 -->
						<div class="fixed-div" id="testDiv" style="margin-top: 28px; margin-left: 10px; height: 440px; overflow: auto; display: none;">
							<table class="valuation_table2" id="">
								<thead>
									<th width="100px">印数</th>
									<th width="100px">基本印工费</th>
									<th width="100px">色令印工费</th>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>

						<!-- 自定义计价公式div -->
						<div class="formula_2content" id="custom" style="<c:if test="${machine.offerMachineType != 'CUSTOM' }">display: none;</c:if>">
							<input type="hidden" name="custom.id" id="customId" value="${machine.offerFormula != null ? machine.offerFormula.id : '' }"> <input type="hidden" name="custom.formula" id="custom.formula" value="" />
							<div class="formula_content3 cl" style="margin-top: 30px;">
								<fieldset id="calculator" class="calculator" style="position: absolute; left: 10px;">
									<legend>计算器</legend>
									<p>
										<input type="button" value="1"> <input type="button" value="2"> <input type="button" value="3"> <input type="button" value="+">
									</p>
									<p>
										<input type="button" value="4"> <input type="button" value="5"> <input type="button" value="6"> <input type="button" value="-">
									</p>
									<p>
										<input type="button" value="7"> <input type="button" value="8"> <input type="button" value="9"> <input type="button" value="*">
									</p>
									<p>
										<input type="button" value="0"> <input type="button" value="00"> <input type="button" value="."> <input type="button" value="/">
									</p>
									<p>
										<input type="button" value="("> <input type="button" value=")"> <input type="button" value=">"> <input type="button" value="<">
									</p>
									<p>
										<input type="button" value=">="> <input type="button" value="<="> <input type="button" value="="> <input type="button" value="!=">
									</p>
									<p>
										<input type="button" value="{"> <input type="button" value="}"> <input type="button" value="||"> <input type="button" value="&amp;&amp;">
									</p>
									<p>
										<input type="button" value="if"> <input type="button" value="else"> <input type="button" value="result">
									</p>
								</fieldset>
								<fieldset class="param_fieldset" style="position: absolute; left: 290px;">
									<legend>计算参数</legend>
									<p class="clear" id="div_contr_1"></p>
									<ul id="custom_params" class="custom_params">
										<phtml:list name="isPublic" textProperty="text" inputType="li" type="com.huayin.printmanager.persist.enumerate.ParamsLadderType"></phtml:list>
									</ul>
									<p></p>
								</fieldset>
								<div class="edit_area2" style="position: absolute;">
									<p class="formula_tip">价格总数用result表示，默认result=0 &nbsp;&nbsp;&nbsp;&nbsp;</p>
									<p class="formula_tip">例:if([印张数量]<=1000){result=[印张数量]*0.4}</p>
									<textarea id="code" name="code" style="width: 100%">${machine.offerFormula != null ? machine.offerFormula.formula : '' }</textarea>
									<input type="hidden" id="custom_formula" value="${machine.offerFormula != null ? machine.offerFormula.formula : '' }" /> <input type="hidden" id="check_result" value="false" />
									<div>
										<div id="check_formula_tip" class="check_formula_tip"></div>
										<div class="btns">
											<input type="button" id="check" class="btn" value="校验" />
										</div>
									</div>
								</div>
							</div>
							<!-- 公式div end -->
						</div>
					</div>
				</dl>
			</fieldset>
		</form>
	</print:form>
</print:body>
</html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<style type="text/css">
.choosen {
	background-color: #70A9F1 !important;
}

tr.choosen td input {
	color: white !important;
}

.tab_input_cus {
	width: 138%;
	height: 100%;
	line-height: 25px;
	border: 0;
	text-align: center;
}

.select-btn {
	right: -89px;
	width: 32px;
	height: 25px;
}

#dy_form, #nb_form, #fee_form {
	display: none;
}
</style>

<!---------------------------------------- 中间按钮 --------------------------------------------->
<div style="margin-top: -9px; margin-bottom: 9px">
	<input type="button" class="btn offer_btn2 btn_cal" value="计算报价" style="background: #9994c3;">
	<input type="button" class="btn offer_btn2 btn_dy" value="对外报价">
	<input type="button" class="btn offer_btn2 btn_nb" value="内部核价">
	<input type="button" class="btn offer_btn2 btn_pb" value="拼版开料图">
</div>
<!---------------------------------------- 详细报价 --------------------------------------------->
<div class="autoprice">
	<div class="autoprice_div1" style="display: none;">
		<table id="ladder" class="border-table resizable table-hover table-striped" rules="all">
			<thead>
				<tr>
					<th style="text-align: left;" colspan="4">
						<span class="mgl">
							阶梯数量行数（
							<input class="constraint_decimal_negative input-txt input-txt_14 whiteBg offer-post" id="rows" name="ladderCol" value="5" style="text-align: center;">
							），阶梯数量间隔（
							<input class="constraint_decimal_negative input-txt input-txt_14 whiteBg offer-post" id="spaceQty" name="ladderSpeed" value="1000" style="text-align: center;">
							）
						</span>
					</th>
					<th style="text-align: right;" colspan="4">
						<span class="mgr">
							<input type="button" class="btn offer_btn2 btn_fee" value="费用明细">
						</span>
					</th>
				</tr>
			</thead>
		</table>
		<!---------------------------------------- 对外报价 --------------------------------------------->
		<print:tableContent formId="dy_form" tableId="dy_table">
			<tbody>
				<tr>
					<td>报价日期</td>
					<td colspan="2">
						<input type="text" id="createDateTime" class="tab_input" name="createDateTime" readonly="readonly">
					</td>
					<td width="150px;">交货日期</td>
					<td width="150px;">
						<input type="text" id="deliveryDate" name="deliveryDate" class="tab_input bg_color WdateFmtErr" onfocus="WdatePicker({lang:'zh-cn',minDate: '%y-%M-%d' })">
					</td>
					<td>报价单号</td>
					<td colspan="2">
						<input type="text" id="offerNo" name="offerNo" class="tab_input" value="保存成功后生成" readonly="readonly">
					</td>
				</tr>
				<tr>
					<td>客户名称</td>
					<td colspan="2">
						<span class="ui-combo-wrap wrap-width" style="width: 100%; height: 100%;">
							<input type="hidden" name="customerId" id="customerId" value="">
							<input type="text" class="tab_input bg_color offer-post" name="customerName" id="customerName" value="">
							<div class="select-btn" id="customer_quick_select" style="right: 0; height: 100%;">...</div>
						</span>
					</td>

					<td colspan="2" rowspan="3">
						<span style="font-size: 30px; font-family: 'STHeiti'">报价单</span>
					</td>

					<td>客户地址</td>
					<td colspan="2">
						<input type="text" class="tab_input bg_color offer-post" name="linkAddress" id="linkAddress" value="">
					</td>
				</tr>
				<tr>
					<td>联系人</td>
					<td colspan="2">
						<input type="text" class="tab_input bg_color offer-post" name="linkName" id="linkName" value="">
					</td>
					<td>联系电话</td>
					<td colspan="2">
						<input type="text" class="tab_input bg_color offer-post" name="phone" id="phone" value="">
					</td>
				</tr>
				<tr>
					<td>成品名称</td>
					<td colspan="2">
						<span class="ui-combo-wrap wrap-width" style="width: 100%; height: 100%;">
							<input type="text" class="tab_input bg_color offer-post" name="productName" id="productName">
							<div class="select-btn" id="product_quick_select" style="right: 0; height: 100%;">...</div>
						</span>
					</td>
					<td>成品尺寸（mm）</td>
					<td colspan="2">
						<input type="text" class="tab_input bg_color" name="specification" id="specification">
					</td>
				</tr>
				<tr style="font-size: 13px; font-weight: bold;">
					<td>印刷纸张</td>
					<td>颜色</td>
					<td>加工工序</td>
					<td>数量</td>
					<td>单价</td>
					<td>金额</td>
					<td>含税单价</td>
					<td>含税金额</td>
				</tr>
			</tbody>
		</print:tableContent>
		<!---------------------------------- 内部核价 ------------------------------------------------->
		<print:tableContent formId="nb_form" tableId="nb_table">
			<tbody id="nb_tbody">
				<tr class="first_tr" style="font-size: 13px; font-weight: bold;">
					<td width="50px">
						<input type="checkbox">
					</td>
					<td width="100px">类型</td>
					<td>印刷机</td>
					<td>上机尺寸</td>
					<td>印刷方式</td>
					<c:if test="${offerType =='ALBUMBOOK'}">
						<td>贴/手数</td>
					</c:if>
					<td>
						<c:choose>
							<c:when test="${offerType =='ALBUMBOOK' }">单面拼数</c:when>
							<c:otherwise> 拼版数</c:otherwise>
						</c:choose>
					</td>
					<td>印张正数</td>
					<td>损耗数</td>
					<td>大纸尺寸</td>
					<td>纸开度</td>
					<td>大纸张数</td>
					<c:if test="${offerType !='ALBUMBOOK'}">
						<td>坑纸数</td>
					</c:if>
					<td>最低价</td>
				</tr>
				<tr class="sec_tr" style="font-size: 13px; font-weight: bold;">
					<td>序号</td>
					<td>数量</td>
					<td>纸张费</td>
					<td>印刷费</td>
					<td>工序费</td>
					<td>其他费</td>
					<td>运费</td>
					<td>成本金额</td>
					<td>利润</td>
					<td>未税金额</td>
					<td>未税单价</td>
					<td>含税金额</td>
					<td>含税单价</td>
				</tr>
			</tbody>
		</print:tableContent>
		<div id="pb_div" style="display: none;">
			<!-- 			<div style="padding: 10px;"> -->
			<!-- 				<div class="l"> -->
			<!-- 					<div style="font-size: 16px;"> -->
			<!-- 						<span style="margin-left: 10px;">多部件型号1</span> -->
			<!-- 						<span>开料</span> -->
			<!-- 					</div> -->
			<!-- 					<div style="padding-right: 20px;"> -->
			<!-- 						<div class="l"> -->
			<%-- 							<img alt="" src="${ctxStatic}/layout/images/opening/2.jpg"> --%>
			<!-- 							<div style="font-size: 16px; text-align: center;"> -->
			<!-- 								<span style="margin-left: 10px;">长</span> -->
			<!-- 								<span>1194</span> -->
			<!-- 								<span>mm</span> -->
			<!-- 							</div> -->
			<!-- 						</div> -->
			<!-- 						<div class="r" style="margin-top: 10%; text-align: center;"> -->
			<!-- 							<div>宽</div> -->
			<!-- 							<div>889</div> -->
			<!-- 							<div>mm</div> -->
			<!-- 						</div> -->
			<!-- 					</div> -->
			<!-- 				</div> -->
			<!-- 				<div class="l"> -->
			<!-- 					<div style="font-size: 16px;"> -->
			<!-- 						<span style="margin-left: 10px;">多部件型号1</span> -->
			<!-- 						<span>拼版</span> -->
			<!-- 					</div> -->
			<!-- 					<div style="padding-right: 20px;"> -->
			<!-- 						<div class="l" style="width: 300px;"> -->
			<!-- 							<table class="border-table resizable" rules="all"> -->
			<!-- 								<tbody> -->
			<!-- 									<tr> -->
			<!-- 										<td>65*91</td> -->
			<!-- 										<td>65*91</td> -->
			<!-- 									</tr> -->
			<!-- 									<tr> -->
			<!-- 										<td>65*91</td> -->
			<!-- 										<td>65*91</td> -->
			<!-- 									</tr> -->
			<!-- 								</tbody> -->
			<!-- 							</table> -->
			<!-- 							<div style="font-size: 16px; text-align: center;"> -->
			<!-- 								<span style="margin-left: 10px;">长</span> -->
			<!-- 								<span>393</span> -->
			<!-- 								<span>mm</span> -->
			<!-- 							</div> -->
			<!-- 						</div> -->
			<!-- 						<div class="r" style="text-align: center;"> -->
			<!-- 							<div>宽</div> -->
			<!-- 							<div>546</div> -->
			<!-- 							<div>mm</div> -->
			<!-- 						</div> -->
			<!-- 					</div> -->
			<!-- 				</div> -->
			<!-- 				<div class="clear"></div> -->
			<!-- 			</div> -->
		</div>
	</div>
	<div class="autoprice_div2" style="display: none; margin: auto 10%;">
		<!---------------------------------- 费用明细 ------------------------------------------------->
		<print:tableContent formId="fee_form" tableId="fee">
			<thead>
				<tr>
					<th width="90px">项目</th>
					<th>计算方案</th>
					<th width="90px">金额</th>
				</tr>
			</thead>
			<tbody>
				<tr class="last">
					<td colspan="3" style="background-color: white;">
						<input type="button" id="btn_fee_close" class="btn" value="关闭" style="width: 100%;">
					</td>
				</tr>
			</tbody>
		</print:tableContent>
	</div>
</div>
<!---------------------------------------- 对外报价 --------------------------------------------->
<div class="hy_hide">
	<table id="template_nb">
		<tbody>
			<tr class="copyTr_nb changeTr">
				<td>
					<input type="checkbox" class=" " name="ckbox">
				</td>
				<td>
					<input type="text" class="tab_input " name="machineType" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="name" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="style" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="offerPrintStyleTypeText" readonly="readonly">
				</td>
				<!-- 贴数 -->
				<c:if test="${offerType =='ALBUMBOOK'}">
					<td>
						<input type="text" class="tab_input " name="thread" readonly="readonly">
					</td>
				</c:if>
				<td>
					<input type="text" class="tab_input " name="sheetNum" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="impositionNum" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="waste" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="paperTypeText" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="materialOpening" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="bigPaperNum" readonly="readonly">
				</td>
				<c:if test="${offerType !='ALBUMBOOK'}">
					<td>
						<input type="text" class="tab_input " name="bfluteNum" readonly="readonly">
					</td>
				</c:if>
				<td>
					<!-- 最低价 -->
					<input type="text" class="tab_input " name="lowerPrice" readonly="readonly">
					<!-- 机台ID -->
					<input type="hidden" name="machineId">
				</td>
			</tr>
		</tbody>
	</table>
</div>

<!-- 模板 -->
<div class="hy_hide">
	<table id="template">
		<tbody>
			<tr class="copyTr">
				<td>
					<!-- 印刷纸张 -->
					<input type="text" class="tab_input " name="offerOrderQuoteOutList.printName" readonly="readonly">
				</td>
				<td>
					<!-- 颜色 -->
					<input type="text" class="tab_input " name="offerOrderQuoteOutList.printColor" readonly="readonly">
				</td>
				<td>
					<!-- 加工工序 -->
					<input type="text" class="tab_input " name="offerOrderQuoteOutList.printProcedure" readonly="readonly">
				</td>
				<td>
					<!-- 数量 -->
					<input type="text" class="tab_input " name="offerOrderQuoteOutList.amount" readonly="readonly">
				</td>
				<td>
					<!-- 单价 -->
					<input type="text" class="tab_input " name="offerOrderQuoteOutList.price" readonly="readonly">
				</td>
				<td>
					<!-- 金额 -->
					<input type="text" class="tab_input " name="offerOrderQuoteOutList.fee" readonly="readonly">
				</td>
				<td>
					<!-- 含税单价 -->
					<input type="text" class="tab_input " name="offerOrderQuoteOutList.taxPrice" readonly="readonly">
				</td>
				<td>
					<!-- 含税金额 -->
					<input type="text" class="tab_input " name="offerOrderQuoteOutList.taxFee" readonly="readonly">
				</td>
			</tr>
		</tbody>
	</table>
</div>
<!-- 模板 -->
<div class="hy_hide">
	<table id="template_nb2">
		<tbody>
			<tr class="copyTr_nb">
				<td>
					<input type="text" class="tab_input " name="offerOrderQuoteInnerList.amount" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="offerOrderQuoteInnerList.paperFee" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="offerOrderQuoteInnerList.printFee" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="offerOrderQuoteInnerList.procedureFee" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="offerOrderQuoteInnerList.ohterFee" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="offerOrderQuoteInnerList.freightFee" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="offerOrderQuoteInnerList.costMoney" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="offerOrderQuoteInnerList.profitFee" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="offerOrderQuoteInnerList.untaxedFee" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="offerOrderQuoteInnerList.untaxedPrice" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="offerOrderQuoteInnerList.taxFee" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="offerOrderQuoteInnerList.taxPrice" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="" readonly="readonly">
				</td>
			</tr>
		</tbody>
	</table>
</div>
<!-- 模板 -->
<div class="hy_hide">
	<table id="template_nbfee">
		<tbody>
			<tr class="copyTr_nb">
				<td>
					<input type="text" class="tab_input " name="" readonly="readonly">
				</td>
				<td style="text-align: left;"></td>
				<td>
					<input type="text" class="tab_input " name="" readonly="readonly">
				</td>
			</tr>
		</tbody>
	</table>
</div>
<!-- 模板 -->
<div class="hy_hide">
	<div id="template_opening">
		<div class="copyTr_nb" style="padding: 10px;">
			<div class="l">
				<div style="font-size: 16px;">
					<span style="margin-left: 10px;" id="open_name1"></span>
					<span>开料</span>
				</div>
				<div style="padding-right: 20px;">
					<div class="l">
						<img alt="" src="${ctxStatic}/layout/images/opening/2.jpg" id="open_opening">
						<div style="font-size: 16px; text-align: center;">
							<span style="margin-left: 10px;">长</span>
							<span id="open_paperLen">1194</span>
							<span>mm</span>
						</div>
					</div>
					<div class="r" style="margin-top: 10%; text-align: center;">
						<div>宽</div>
						<div id="open_paperWidth">889</div>
						<div>mm</div>
					</div>
				</div>
			</div>
			<!-- 			<div class="l"> -->
			<!-- 				<div style="font-size: 16px;"> -->
			<!-- 					<span style="margin-left: 10px;" id="open_name2"></span> -->
			<!-- 					<span>拼版</span> -->
			<!-- 				</div> -->
			<!-- 				<div style="padding-right: 20px;"> -->
			<!-- 					<div class="l" style="width: 300px;"> -->
			<!-- 						<table class="border-table resizable" rules="all" id="open_sheetNum"> -->
			<!-- 							<tbody> -->
			<!-- 							</tbody> -->
			<!-- 						</table> -->
			<!-- 						<div style="font-size: 16px; text-align: center;"> -->
			<!-- 							<span style="margin-left: 10px;">长</span> -->
			<!-- 							<span id="open_openingLen">393</span> -->
			<!-- 							<span>mm</span> -->
			<!-- 						</div> -->
			<!-- 					</div> -->
			<!-- 					<div class="r" style="text-align: center;"> -->
			<!-- 						<div>宽</div> -->
			<!-- 						<div id="open_openingWidth">546</div> -->
			<!-- 						<div>mm</div> -->
			<!-- 					</div> -->
			<!-- 				</div> -->
			<!-- 			</div> -->
			<div class="clear"></div>
		</div>
	</div>
</div>


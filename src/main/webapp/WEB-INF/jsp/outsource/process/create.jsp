<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>创建发外加工单</title>
<script type="text/javascript" src="${ctxStatic }/site/outsource/process/create.js?v=${v }"></script>
</head>
<body>
<input type="hidden" value="${type}" id="type">
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="发外管理-订单管理-创建发外加工单"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="outsource:process:create">
						<button class="nav_btn table_nav_btn" id="btn_save">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="outsource:process:create,outsource:process:audit">
						<button class="nav_btn table_nav_btn" id="btn_save_audit">保存并审核</button>
					</shiro:hasManyPermissions>
					<button class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
				</div>
			</div>
			<form id="form_process">
				<input type="hidden" name="type" value="${type }" /> <input type="hidden" name="originCompanyId" id="originCompanyId" />
				<!--主表-订单表单-->
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_6">供应商名称：</label> <span class="ui-combo-wrap wrap-width"> <input type="text" class="input-txt input-txt_7" readonly="readonly" id="supplierName" name="supplierName" /> <input type="hidden" id="supplierId" name="supplierId" /> <input type="hidden" id="supplierCode" name="supplierCode" />
										<div class="select-btn" id="supplier_quick_oem">...</div>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">联 系 人：</label> <span class="ui-combo-wrap"> </select> <input type="text" class="input-txt input-txt_3" name="linkName" id="linkName" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">联系电话：</label> <span class="ui-combo-wrap"> <input type="text" class="input-txt input-txt_1" name="mobile" id="mobile" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">交货日期：</label> <span class="ui-combo-wrap"> <input type="text" onfocus="WdatePicker({lang:'zh-cn',minDate: '%y-%M-%d'})" style="background: #fff" class="input-txt input-txt_1 Wdate" readonly="true" id="deliveryTime" name="deliveryTime" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${order.deliveryTime}' type='date' />" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">发外员：</label> <span class="ui-combo-wrap" class="form_text"> <input type="hidden" name="employeeId" id="employeeId" /> <input type="text" class="input-txt input-txt_1" readonly name="employeeName" id="employeeName" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">

								<dd class="row-dd">
									<label class="form-label label_ui label_6">供应商地址：</label> <span class="ui-combo-wrap"> <input type="text" class="input-txt input-txt_22" name="supplierAddress" id="supplierAddress" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">送货方式：</label> <span class="ui-combo-wrap"> <phtml:list items="${fns:basicList2('DELIVERYCLASS')}" valueProperty="id" textProperty="name" name="deliveryClassId" cssClass="input-txt input-txt_1 hy_select2" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">付款方式：</label> <span class="ui-combo-wrap" class="form_text"> <phtml:list items="${fns:basicList2('PAYMENTCLASS')}" valueProperty="id" textProperty="name" name="paymentClassId" cssClass="input-txt input-txt_1 hy_select2" />
									</span>
								</dd>
								<dd class="row-dd" style="display: none">
									<label class="form-label label_ui">税 率：</label> <span class="ui-combo-wrap" class="form_text"> <phtml:list items="${fns:basicList2('TAXRATE')}" valueProperty="id" textProperty="name" name="rate" cssClass="input-txt input-txt_1 hy_select2" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_6">备注：</label> <span class="form_textarea"> <textarea class="noborder" name="memo" style="width: 907px" placeholder="说点什么...100个字符以内" onKeyUp="Helper.doms.textarealength(this,100)"></textarea>
									<p class="textarea-numberbar">
										<em>0</em> /100
									</p>
									</span>
								</dd>
							</dl>
						</div>
					</div>
				</div>
				<!--从表-订单表格-->
				<div>
					<div class="table-container">
						<div id="toolbar"></div>
						<table class="border-table" style="width: 1450px" id="detailList">
							<thead>
								<tr>
									<th width="30">序号</th>
									<th width="40">操作</th>
									<th width="100">源单类型</th>
									<th width="100">发外类型</th>
									<th width="100">源单单号</th>
									<th width="100">成品名称</th>
									<th width="100">工序名称</th>
									<th width="100">加工规格</th>
									<th width="100">部件名称</th>
									<th width="100">加工数量</th>
									<shiro:hasPermission name="outsource:process:money">
										<th width="100">单价</th>
										<th width="100">金额</th>
									</shiro:hasPermission>
									<th width="100" style="display: none">不含税单价</th>
									<th width="100" style="display: none">不含税金额</th>
									<th width="100">生产数量</th>
									<th width="100">税收 <i id="batch_edit_taxRate" class="fa fa-edit" src="batch_taxRate_box"></i>
										<div class="batch_box_container"></div>
									</th>
									<th width="100" style="display: none">税率值%</th>
									<shiro:hasPermission name="outsource:process:money">
										<th width="100">税额</th>
									</shiro:hasPermission>
									<%--                   <c:if test="${type == 'PRODUCT'}"> --%>
									<th width="100">工艺信息</th>
									<th width="100">材料信息</th>
									<%--                   </c:if> --%>
									<th width="100">加工要求</th>
									<th width="120">备注</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="detail" items="${list}" varStatus="status">
									<tr name="tr">
										<input name="detailList.sourceBillType" type="hidden" value="${detail.sourceBillType }" />
										<input name="detailList.type" type="hidden" value="${detail.type }" />
										<input name="detailList.productType" type="hidden" value="${detail.productType }" />
										<input name="detailList.workProcedureType" type="hidden" value="${detail.workProcedureType }" />

										<input name="detailList.sourceId" type="hidden" value="${detail.sourceId }" />
										<input name="detailList.sourceDetailId" type="hidden" value="${detail.sourceDetailId }" />
										<input name="detailList.sourceQty" type="hidden" value="${detail.sourceQty }" />

										<input name="detailList.productId" type="hidden" value="${detail.productId }" />
										<input name="detailList.procedureId" type="hidden" value="${detail.procedureId }" />
										<input name="detailList.procedureCode" type="hidden" value="${detail.procedureCode }" />
										<input name="detailList.workId" type="hidden" value="${detail.workId }" />
										<c:if test="${detail.procedureType!=null && detail.procedureType!=''}">
											<input name="detailList.procedureType" type="hidden" value="${detail.procedureType }" />
										</c:if>
										<input name="detailList.taxRateName" type="hidden" value="${detail.taxRateId }" />

										<td>${status.count}</td>
										<td class="td-manage"><i title="删除行" class="fa fa-trash-o row_delete"></i></td>
										<td><input class="tab_input" type="text" readonly value="${detail.sourceBillType.text }" /></td>
										<td><input class="tab_input" type="text" readonly value="${detail.type.text }" /></td>
										<td><input class="tab_input" type="text" name="detailList.sourceBillNo" readonly value="${detail.sourceBillNo }" /></td>
										<td><c:if test="${detail.type=='PRODUCT' }">
												<input class="tab_input" type="text" name="detailList.productName" readonly="readonly" value="${detail.productName }" />
												<input class="tab_input" type="hidden" name="detailList.productNames" readonly="readonly" value="" />
											</c:if> <c:if test="${detail.type=='PROCESS' }">
												<input class="tab_input" type="hidden" name="detailList.productName" readonly="readonly" value="" />
												<input class="tab_input" type="text" name="detailList.productNames" readonly="readonly" value="${detail.productNames }" />
											</c:if></td>
										<td><input class="tab_input" type="text" name="detailList.procedureName" readonly="readonly" value="${detail.procedureName }" /></td>
										<td><input class="tab_input bg_color" type="text" name="detailList.style" value="${detail.style }" /></td>
										<td><input class="tab_input" type="text" name="detailList.partName" readonly="readonly" value="${detail.partName }" /></td>
										<td><input class="tab_input bg_color" type="text" name="detailList.qty" <c:choose>
												<c:when test="${detail.productType == 'ROTARY' && detail.workProcedureType == 'PART'}">
													value = "${detail.qty}"
												</c:when>
												<c:otherwise>
													value = "${fns:removeLastZero(detail.qty)}"
												</c:otherwise>
											</c:choose> /></td>
										<!-- 判断是否有权限查看金额 --begin -->

										<td><input class="tab_input bg_color constraint_decimal_negative" type="text" name="detailList.price" value="${detail.price }" /></td>
										<td><input class="tab_input constraint_decimal bg_color" type="text" name="detailList.money" value="${detail.qty*detail.price }" /></td>

										<!-- 判断是否有权限查看金额 --end -->
										<td style="display: none"><input class="tab_input" type="text" name="detailList.noTaxPrice" readonly="readonly" value="${detail.noTaxPrice }" /></td>
										<td style="display: none"><input class="tab_input" type="text" name="detailList.noTaxMoney" readonly="readonly" value="${detail.noTaxMoney }" /></td>
										<td><input class="tab_input" type="text" name="detailList.produceNum" readonly="readonly" value="${detail.produceNum }" /></td>
										<td><phtml:list items="${fns:basicList2('TAXRATE')}" valueProperty="id" name="detailList.taxRateId" textProperty="name" cssClass="tab_input bg_color"></phtml:list></td>
										<td style="display: none"><input type="text" class="tab_input" name="detailList.taxRatePercent" readonly="readonly" value="${detail.taxRatePercent }" /></td>
										<!-- 判断是否有权限查看金额 --begin -->

										<td><input class="tab_input" type="text" name="detailList.tax" readonly="readonly" value="${detail.tax }" /></td>
										<%--                     <c:if test="${type == 'PRODUCT'}"> --%>
										<td class="otherView"><input class="tab_input" type="hidden" name="detailList.workProcedures" readonly="readonly" value="${detail.workProcedures }" /> ${detail.workProcedures }
											<div class="loading_effect">
												<div style="margin-left: 30px;">加</div>
												<div>载</div>
												<div>中</div>
											</div></td>
										<td class="otherView"><input class="tab_input" type="hidden" name="detailList.workMaterials" readonly="readonly" value="${detail.workMaterials }" /> ${detail.workMaterials }
											<div class="loading_effect">
												<div style="margin-left: 30px;">加</div>
												<div>载</div>
												<div>中</div>
											</div></td>
										<%--                     </c:if> --%>
										<!-- 判断是否有权限查看金额 --begin -->
										<td><input class="tab_input bg_color" type="text" name="detailList.processRequire" value="${detail.processRequire }" /></td>
										<td><input class="tab_input bg_color memo" type="text" onmouseover="this.title=this.value" name="detailList.memo" value="${detail.memo }" /></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
				<!--表格End-->
				<div class="cl form_content">
					<dl class="cl row-dl-foot">
						<dd class="row-dd">
							<label class="form-label label_ui label_7">金 额：</label> <span class="ui-combo-wrap" class="form_text"> <input type="text" class="input-txt input-txt_3" id="sum_money" name="totalMoney" readonly value='0' />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">金额(不含税)：</label> <span class="ui-combo-wrap" class="form_text"> <input type="text" class="input-txt input-txt_3" id="sum_noTaxMoney" name="noTaxTotalMoney" readonly value='0' />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">税 额：</label> <span class="ui-combo-wrap" class="form_text"> <input type="text" class="input-txt input-txt_3" id="sum_tax" name="totalTax" readonly value="0" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">币 种：</label> <span class="ui-combo-wrap" class="form_text"> <input id="currencyType" name="currencyType" type="hidden" /> <input id="currencyTypeText" type="text" class="input-txt input-txt_3" readonly="readonly" />
							</span>
						</dd>
					</dl>
					<dl class="cl row-dl-foot">
						<dd class="row-dd">
							<label class="form-label label_ui label_7">制 单 人：</label> <span class="ui-combo-wrap" class="form_text"> <input name="createName" type="text" class="input-txt input-txt_3" readonly value="${loginUser.realName}" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">制单日期：</label> <span class="ui-combo-wrap" class="form_text"> <input name="createTime" type="text" class="input-txt input-txt_3" readonly value="" id="createDate" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">审 核 人：</label> <span class="ui-combo-wrap" class="form_text"> <input name="checkUserName" type="text" class="input-txt input-txt_3" readonly value="" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">审核日期：</label> <span class="ui-combo-wrap" class="form_text"> <input name="checkTime" type="text" class="input-txt input-txt_3" readonly value="" />
							</span>
						</dd>
					</dl>
				</div>
				<!-- 批量悬浮窗 satrt -->
				<div id="batch_taxRate_box" class="batch_taxRate_box">
					<phtml:list items="${fns:basicList2('TAXRATE')}" cssClass="tab_input bg_color  input-txt_3 hy_select2 batch_taxRate_select" valueProperty="id" textProperty="name" name="rate" onchange="batchEditTaxRate()" />
				</div>
				<!-- 批量悬浮窗 end -->
			</form>
			<!-- 标识工艺信息和材料信息是否加载完 -->
			<input id="loading_flag" type="hidden" value="false" />
		</div>
	</div>
</body>
</html>
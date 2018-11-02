<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>编辑对账单</title>
<script type="text/javascript">
	var hasPermission = Helper.basic.hasPermission('oem:reconcil:money');

	$(function()
	{
		if (!hasPermission)
		{
			$("td").has("input[name='detailList.price']").hide();
			$("td").has("input[name='detailList.tax']").hide();
			$("td").has("input[name='detailList.money']").hide();
			$("dd").has("input[id=totalMoney]").hide();
			$("dd").has("input[id=noTaxTotalMoney]").hide();
			$("dd").has("input[id=totalTax]").hide();
		}
		//取消
		$("#btn_cancel").click(function()
		{
			closeTabAndJump("代工对账列表");
		});
		// 初始结算日期
		if ($("#reconcilTime").val() == "")
		{
			$("#reconcilTime").val(new Date().format('yyyy-MM-dd'));
		}
		// 初始化制单日期
		//$("#createDate").val(new Date().format('yyyy-MM-dd'));

		//保存
		$("#btn_save,#btn_save_audit").click(function()
		{
			fixEmptyValue();
			if (Helper.isEmpty($("#settlementClassId").val()))
			{
				Helper.message.warn("请选择结算方式");
				return false;
			}
			if ($("#detailList input[name='detailList.qty']").size()<= 0)
			{
				Helper.message.warn("请录入明细");
				return false;
			}
			$("#isCheck").val($(this).attr("id") == "btn_save_audit" ? "YES" : "NO");
			form_submit();
		})

		//所有单价改变事件
		$("table tbody").on("blur", "input[name='detailList.price']", function()
		{

			calcMoney(this);
		});

		//修改金额
		$("table tbody").on("blur", "input[name='detailList.money']", function()
		{
		//	var money_value = Number($(this).val()).tomoney();
		//	$(this).val(money_value);
			calcPrice(this);
		});
		//删除一行数据
		$("table").on("click", ".row_delete", function()
		{
			$(this).parent().parent().remove();
			sum();
			resetSequenceNum();
		});
		//重新设置序号  
		function resetSequenceNum()
		{
			$("table tbody tr").each(function(index)
			{
				$(this).find("td").first().html(++index);
			});
		}
		//所有数量改变事件
		$("table tbody").on("keyup blur", "input[name='detailList.qty']", function()
		{
			var tr_dom = $(this).parent().parent();
			if (tr_dom.find("input[name='detailList.sourceBillType']").val() == "OEM_ER" && Number($(this).val()) > 0)
			{
				$(this).val(0 - Number($(this).val()));
				$(this).trigger("input");
			} 
			if (tr_dom.find("input[name='detailList.sourceBillType']").val() != "OEM_ER" && Number($(this).val()) < 0)
			{
				$(this).val(0 - Number($(this).val()));
				$(this).trigger("input");
			}
			var qty_value = Number($(this).val());
			var saveQty = Number($(this).parent().parent().find("input[name='detailList.saveQty']").val());
			
			if (qty_value == saveQty)
			{
				var saveMoney = $(this).parent().parent().find("input[name='detailList.saveMoney']").val();
				$(this).parent().parent().find("input[name='detailList.money']").val(saveMoney);
				calcPrice(this);
				return;
			}
			calcMoney(this);
		});
		//trigger();
		// 解决性能问题 -- 最后在汇总
		sum();
		formatterPrice();
		taxSelectNum();
	});

	function trigger()
	{
		// 	  for (var i = 0; i < $("input[name='detailList.qty']").length; i++) {
		// 	    	$("input[name='detailList.qty']").eq(i).trigger("input");
		//         }
		$("input[name='detailList.money']").trigger("blur");
	}
	//格式化单价
	function formatterPrice()
	{
		for (var i = 0; i < $("input[name='detailList.price']").length; i++)
		{
			var price = $("input[name='detailList.price']").eq(i).val();
			$("input[name='detailList.price']").eq(i).val(Number(price));
		}
	}
	//修改金额
	function calcPrice(obj)
	{
		var qty_dom = $(obj).parent().parent().find("input[name='detailList.qty']");
		var price_dom = $(obj).parent().parent().find("input[name='detailList.price']");
		var money_dom = $(obj).parent().parent().find("input[name='detailList.money']");
		if (qty_dom.val() == 0 || qty_dom.val().trim() == '')
		{
			Helper.message.warn("请先输入数量");
			money_dom.val(0);
			return;
		}
		var price = Number(money_dom.val()).div(qty_dom.val()).toFixed(4);
		price_dom.val(price);
		calcTaxRate(obj);
	}

	//改变所有税率
	function taxSelect()
	{
		var taxRateId = $("#rateId").val();
		$("select[name='detailList.taxRateId']").val(taxRateId);
		$("input[name='detailList.percent']").val(Helper.basic.info("TAXRATE", taxRateId).percent);
		$("table select[name='detailList.taxRateId']").trigger("change");
	}

	//改变单个税率  
	function taxSelectNum()
	{
		$("select[name='detailList.taxRateId']").change(function()
		{
			var flg = true;
			var taxRateId = $(this).val();
			$(this).parent().next().children().val(Helper.basic.info("TAXRATE", taxRateId).percent);
			calcTaxRate(this);
		});
	}

	function form_submit()
	{
		$("table tbody tr").each(function()
		{
			$(this).find("td select[name='detailList.taxRateId']").removeAttr("disabled");
		});
		$("#btn_save").attr({
			"disabled" : "disabled"
		});
		$("#btn_save_audit").attr({
			"disabled" : "disabled"
		});
		
		Helper.request({
			url : Helper.basePath + "/oem/reconcil/update",
			data : $("#form_order").formToJson(),
			success : function(data)
			{
				if (data.success)
				{
					Helper.message.suc('已保存!');
					location.href = Helper.basePath + '/oem/reconcil/view/' + data.obj.id;
				} else
				{
					Helper.message.warn(data.message);
					$("#btn_save").removeAttr("disabled");
					$("#btn_save_audit").removeAttr("disabled");
				}
			},
			error : function(data)
			{
				$("#btn_save").removeAttr("disabled");
				$("#btn_save_audit").removeAttr("disabled");
			}
		});
	}

	//计算金额
	function calcMoney(obj)
	{
		var qty_dom = $(obj).parent().parent().find("input[name='detailList.qty']");
		var price_dom = $(obj).parent().parent().find("input[name='detailList.price']");
		var money_dom = $(obj).parent().parent().find("input[name='detailList.money']");

		money_dom.val(Number(price_dom.val()).mul(Number(qty_dom.val())).tomoney());//金额=(单价*数量)

		calcTaxRate(obj);
	}
	//计算税额
	function calcTaxRate(obj, needSum)
	{
		//获取金额对象并格式化
		var money_dom = $(obj).parent().parent().find("input[name='detailList.money']");
		var money_value = Number(money_dom.val()).tomoney();//金额
		money_dom.val(money_value);

		var taxRatePercent = Number($(obj).parent().parent().find("input[name='detailList.percent']").val());//税率值
		//不含税金额计算
		var noTaxMoney_dom = $(obj).parent().parent().find("input[name='detailList.noTaxMoney']");
		noTaxMoney_dom.val(Number(money_value).div(Number(1 + (taxRatePercent / 100))).tomoney());//不含税金额=（金额/(1+税率值/100)）

		//税额计算
		var tax_dom = $(obj).parent().parent().find("input[name='detailList.tax']");
		tax_dom.val(money_value.subtr(Number(noTaxMoney_dom.val())).tomoney());//税额=(金额-不含税金额)

		var qty_value = Number($(obj).parent().parent().find("input[name='detailList.qty']").val());//数量
		//单价计算
		var price_dom = $(obj).parent().parent().find("input[name='detailList.price']");

		//不含税单价计算
		var noTaxPrice_dom = $(obj).parent().parent().find("input[name='detailList.noTaxPrice']");
		noTaxPrice_dom.val(Number(price_dom.val()).div(Number(1 + (taxRatePercent / 100))).toFixed(4));//不含税单价=(单价/(1+税率值/100))
		//汇总(首次初始化补需要持续汇总 --- 解决性能问题)
		if (false !== needSum)
		{
			sum();
		}
	}
	//汇总
	function sum()
	{
		var sum_qty = 0;
		var sum_tax = 0;
		var sum_noTaxMoney = 0;
		var sum_money = 0;
		$("table tbody tr").each(function()
		{
			$(this).find("td select[name='detailList.taxRateId']").attr("disabled", true);

			sum_qty = Number(sum_qty).add(Number($(this).find("td input[name='detailList.qty']").val()));
			sum_tax = Number(sum_tax).add(Number($(this).find("td input[name='detailList.tax']").val())).tomoney();
			sum_noTaxMoney = Number(sum_noTaxMoney).add(Number($(this).find("td input[name='detailList.noTaxMoney']").val()));
			sum_money = Number(sum_money).add(Number($(this).find("td input[name='detailList.money']").val()));
		});
		$("#qty").val(sum_qty);
		$("#tax").val(sum_tax.tomoney());
		$("#noTaxMoney").val(sum_noTaxMoney.tomoney());
		$("#money").val(sum_money.tomoney());
		$("#totalMoney").val(sum_money.tomoney());
		$("#noTaxTotalMoney").val(sum_noTaxMoney.tomoney());
		$("#totalTax").val(sum_tax.tomoney());
	}
</script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="代工管理-代工对账-编辑"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="oem:reconcil:edit">
						<button class="nav_btn table_nav_btn" id="btn_save">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="oem:reconcil:edit,oem:reconcil:audit">
						<button class="nav_btn table_nav_btn" id="btn_save_audit">保存并审核</button>
					</shiro:hasManyPermissions>
					<button class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
				</div>
			</div>
			<form id="form_order">
				<!--主表-订单表单-->
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl">
								<input type="hidden" name="customerId" id="customerId" value="${order.customerId }">
								<input type="hidden" name="originCompanyId" id="originCompanyId" value="${order.originCompanyId }">
								<input type="hidden" id="index" />
								<input type="hidden" id="id" value="${order.id }" name="id" />
								<input type="hidden" name="isCheck" id="isCheck" />
								<dd class="row-dd">
									<label class="form-label label_ui label_1">客户名称：</label>
									<span class="ui-combo-wrap wrap-width">
										<input type="text" class="input-txt input-txt_7" readonly="true" name="customerName" id="customerName" value="${order.customerName }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">联 系 人：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_3" name="linkName" id="linkName" value="${order.linkName }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">联系电话：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_1" name="mobile" id="mobile" value="${order.mobile }" />
									</span>
								</dd>

								<dd class="row-dd">
									<label class="form-label label_ui label_1">对账日期：</label>
									<span class="ui-combo-wrap">
										<input type="text" name="reconcilTime" id="reconcilTime" class="input-txt input-txt_3 Wdate" value="<fmt:formatDate value="${order.reconcilTime}" type="date" pattern="yyyy-MM-dd" />" onFocus="WdatePicker({lang:'zh-cn',minDate: '%y-%M-%d' })" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">销 售 员：</label>
									<span class="ui-combo-wrap">
										<input name="employeeId" type="hidden" value="${order.employeeId }" />
										<input type="text" class="input-txt input-txt_3" readonly="readonly" value="${order.employeeName}">
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">送货地址：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_22" name="deliveryAddress" id="deliveryAddress" value="${order.deliveryAddress }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">结算方式：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('SETTLEMENTCLASS')}" valueProperty="id" defaultValue="" defaultOption="请选择" name="settlementClassId" textProperty="name" cssClass="nput-txt input-txt_1 hy_select2" selected="${order.settlementClassId}"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">付款方式：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('PAYMENTCLASS')}" valueProperty="id" defaultValue="" defaultOption="请选择" selected="${order.paymentClassId }" name="paymentClassId" textProperty="name" cssClass="input-txt input-txt_3 hy_select2"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd" style="display: none">
									<label class="form-label label_ui">税 收：</label>
									<span class="ui-combo-wrap" class="form_text">
										<phtml:list items="${fns:basicList2('TAXRATE')}" valueProperty="id" name="rateId" textProperty="name" cssClass="input-txt input-txt_1 hy_select2" onchange="taxSelect();" selected="${customer.taxRateId }"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">对账单号：</label>
									<span class="ui-combo-wrap">
										<input type="text" name="billNo" id="billNo" value="${order.billNo}" readonly="readonly" class="input-txt input-txt_3" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">备注：</label>
									<span class="form_textarea">
										<textarea name="memo" id="memo" style="width: 937px" placeholder="说点什么...100个字符以内" onKeyUp="Helper.doms.textarealength(this,100)">${order.memo}</textarea>
										<p class="textarea-numberbar">
											<em>0</em>
											/100
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
						<table class="border-table" style="width: 1600px" id="detailList">
							<thead>
								<tr>
									<th width="40">序号</th>
									<th width="40">操作</th>
									<th width="80">源单类型</th>
									<th width="140">成品名称</th>
									<th width="100">工序名称</th>
									<th width="100">加工规格</th>
									<th width="120">代工单号</th>
									<th width="80">数量</th>
									<th width="80">源单数量</th>
									<th width="100">源单单号</th>
									<shiro:hasPermission name="oem:reconcil:money">
										<th width="60">单价</th>
										<th width="60">金额</th>
										<th width="60">税额</th>
									</shiro:hasPermission>

									<th width="100">税收</th>
									<th width="80" style="display: none">税率值</th>
									<th width="100" style="display: none">不含税单价</th>
									<th width="100" style="display: none">不含税金额</th>
									<th width="80">部件名称</th>
									<th width="100">送/退货日期</th>
									<th width="100">加工要求</th>
									<th width="100">备注</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="detail" items="${order.detailList}" varStatus="status">
									<tr name="tr">
										<input name="detailList.id" type="hidden" readonly="readonly" value="${detail.id }" />
										<td>${status.count}</td>
										<td class="td-manage">
											<i title="删除行" class="fa fa-trash-o row_delete"></i>
										</td>
										<td class="td-manage">
											<c:if test="${detail.sourceBillType=='OEM_ED' }">代工送货单</c:if>
											<c:if test="${detail.sourceBillType=='OEM_ER' }">代工退货单</c:if>
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.productName" readonly="readonly" value="${detail.productName }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.procedureName" readonly="readonly" value="${detail.procedureName }" />
											<input type="hidden" name="detailList.procedureId" readonly="readonly" value="${detail.procedureId }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.style" readonly="readonly" value="${detail.style }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.oemOrderBillNo" readonly="readonly" value="${detail.oemOrderBillNo }" />
										</td>
										<td>
											<input class="tab_input bg_color <c:if test="${detail.sourceBillType=='OEM_ER' }">constraint_decimal_positive</c:if> <c:if test="${detail.sourceBillType=='OEM_ED' }">constraint_decimal_negative</c:if>" type="text" name="detailList.qty" value="${detail.qty }" />
											<input type="hidden" name="detailList.saveQty" value="<c:if test="${detail.sourceBillType=='OEM_ER' }">-</c:if>${detail.qty }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.sourceQty" readonly="readonly" value="${detail.sourceQty }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.sourceBillNo" readonly="readonly" value="${detail.sourceBillNo }" />
										</td>

										<td>
											<input class="tab_input bg_color" type="text" name="detailList.price" value="${detail.price }" />
										</td>
										<td>
											<input class="tab_input bg_color" type="text" name="detailList.money" value="${detail.money }" />
											<input type="hidden" name="detailList.saveMoney" value="${detail.money }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.tax" readonly="readonly" value="${detail.tax }" />
										</td>

										<td>
											<phtml:list items="${fns:basicList2('TAXRATE')}" cssClass="tab_input" valueProperty="id" textProperty="name" name="detailList.taxRateId" selected="${detail.taxRateId }" />
										</td>
										<td style="display: none">
											<input class="tab_input" type="text" name="detailList.percent" readonly="readonly" value="${detail.percent }" />
										</td>
										<td style="display: none">
											<input class="tab_input" type="text" name="detailList.noTaxPrice" readonly="readonly" value="${detail.noTaxPrice }" />
										</td>
										<td style="display: none">
											<input class="tab_input" type="text" name="detailList.noTaxMoney" readonly="readonly" value="${detail.noTaxMoney }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.partName" readonly="readonly" value="${detail.partName }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.deliveryTime" readonly="readonly" value="<fmt:formatDate value="${detail.deliveryTime }" type="date" pattern="yyyy-MM-dd" />" />
										</td>
										<td>
											<input class="tab_input bg_color memo" onmouseover="this.title=this.value" type="text" name="detailList.processRequire" value="${detail.processRequire }" />
										</td>
										<td>
											<input class="tab_input bg_color memo" onmouseover="this.title=this.value" type="text" name="detailList.memo" value="${detail.memo }" />
											<input type="hidden" name="detailList.receiveMoney" value="${detail.receiveMoney }" />
											<input type="hidden" name="detailList.originProcedureId" readonly="readonly" value="${detail.originProcedureId }" />
											<input type="hidden" name="detailList.originCompanyId" readonly="readonly" value="${detail.originCompanyId }" />
											<input type="hidden" name="detailList.sourceId" readonly="readonly" value="${detail.sourceId }" />
											<input type="hidden" name="detailList.sourceDetailId" readonly="readonly" value="${detail.sourceDetailId }" />
								      <input type="hidden" name="detailList.sourceBillType" readonly="readonly" value="${detail.sourceBillType }" />
											<input type="hidden" name="detailList.originBillNo" readonly="readonly" value="${detail.originBillNo }" />
											<input type="hidden" name="detailList.originBillId" readonly="readonly" value="${detail.originBillId }" />
											<input type="hidden" name="detailList.productId" readonly="readonly" value="${detail.productId }" />
										</td>
									</tr>
								</c:forEach>
							</tbody>
							<tfoot style="display: none">
								<tr>
									<td>合 计</td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td>
										<input class="tab_input" id="qty" readonly="readonly" />
									</td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td>
										<input class="tab_input" id="money" readonly="readonly" />
									</td>
									<td></td>
									<td></td>
									<td>
										<input class="tab_input" type="text" id="tax" readonly="readonly" />
									</td>
									<td></td>
									<td></td>
									<td></td>
								</tr>
							</tfoot>
						</table>
					</div>
				</div>
				<div class="cl form_content">
					<dl class="cl row-dl-foot">

						<dd class="row-dd">
							<label class="form-label label_ui label_7">金 额：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" name="totalMoney" id="totalMoney" class="input-txt input-txt_3" readonly value="" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">金额(不含税)：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" name="noTaxTotalMoney" id="noTaxTotalMoney" class="input-txt input-txt_3" readonly value="" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">税 额：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" name="totalTax" id="totalTax" class="input-txt input-txt_3" readonly value="" />
							</span>
						</dd>

						<dd class="row-dd">
							<label class="form-label label_ui label_7">币 种：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input id="currencyType" name="currencyType" type="hidden" value="${order.currencyType}" />
								<input type="text" class="input-txt input-txt_3" readonly="readonly" value="${order.currencyType.text }" />
							</span>
						</dd>
					</dl>
					<dl class="cl row-dl-foot">
						<dd class="row-dd">
							<label class="form-label label_ui label_7">制 单 人：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="createName" type="text" class="input-txt input-txt_3" readonly value="${fns:getUser().realName}" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">制单日期：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="createTime" type="text" class="input-txt input-txt_3" readonly="readonly" value="" id="createDate" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">审 核 人：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="checkUserName" type="text" class="input-txt input-txt_3" readonly value="" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">审核日期：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="checkTime" type="text" class="input-txt input-txt_3" readonly value="" />
							</span>
						</dd>

					</dl>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
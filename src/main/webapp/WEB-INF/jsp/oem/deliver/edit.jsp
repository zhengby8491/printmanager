<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>编辑送货单</title>
<script type="text/javascript">
	var hasPermission = Helper.basic.hasPermission('oem:deliver:money');
	$(function()
	{
		if (!hasPermission)
		{
			$("#detailList").find("th[name='price']").hide();
			$("#detailList").find("th[name='money']").hide();
			$("#detailList").find("th[name='tax']").hide();
			$("td").has("input[name='detailList.price']").hide();
			$("td").has("input[name='detailList.money']").hide();
			$("td").has("input[name='detailList.tax']").hide();
			$("dd").has("input[name='totalMoney']").hide();
			$("dd").has("input[name='noTaxTotalMoney']").hide();
			$("dd").has("input[name='totalTax']").hide();

		}
		// 取消  
		$("#btn_cancel").click(function()
		{
			closeTabAndJump("代工送货");
		});
		// 删除
		$("table").on("click", ".row_delete", function()
		{
			$(this).parent().parent().remove();
			resetSequenceNum();
			sum();
		});
		// 保存
		$("#btn_save,#btn_save_audit").click(function()
		{
			fixEmptyValue();
			var validate = true;
			var flg = true;
			if (validate)
			{
				if ($("#detailList input[name='detailList.qty']").size()<= 0)
				{
					Helper.message.warn("请录入明细");
					flg = false;
					validate = false;
					return false;
				};
			}
			if (validate)
			{
				$("#detailList input[name='detailList.qty']").each(function()
				{
					if (Number($(this).val()) <= 0)
					{
						Helper.message.warn("产品数量必须大于0");
						$(this).focus();
						flg = false;
						validate = false;
						return false;
					}
				});
			}
			if (validate)
			{
				$("#detailList select[name='detailList.taxRateId']").each(function()
				{
					if (Number($(this).val()) <= 0)
					{
						Helper.message.warn("请选择税收");
						flg = false;
						return false;
					}
				});
			}
			if (flg)
			{
				$("#isCheck").val($(this).attr("id") == "btn_save_audit" ? "YES" : "NO");
				form_submit();
			}
		});
		// 所有金额改变事件
		$("#detailList tbody").on("blur", "input[name='detailList.money']", function()
		{
			var money_value = Number($(this).val()).tomoney();//控制金额小数位，保留2位
			$(this).val(money_value);
			calcPrice(this);
		});
		// 所有单价改变事件
		$("#detailList tbody").on("blur", "input[name='detailList.price']", function()
		{
			calcMoney(this);
		});
		// 所有数量改变事件
		$("#detailList tbody").on("input", "input[name='detailList.qty']", function()
		{
			/* var qty_value = Number($(this).val()).trunc();
			$(this).val(qty_value); */
			var saveQty = Number($(this).parent().parent().find("input[name='detailList.saveQty']").val());
			var qty_value = Number($(this).val());
			if ($(this).val() == saveQty)
			{
				var saveMoney = $(this).parent().parent().find("input[name='detailList.saveMoney']").val();
				$(this).parent().parent().find("input[name='detailList.money']").val(saveMoney);
				calcPrice(this);
				return;
			}
			if (qty_value > 0)
			{
				calcMoney(this);
			}
		});
		// 初始化批量修改仓库、税收、交货日期悬浮窗
		$("#batch_edit_taxRate,#batch_edit_deliveryTime").each(function()
		{
			$(this).powerFloat({
				eventType : "click",
				targetAttr : "src",
				reverseSharp : true,
				container : $(this).siblings(".batch_box_container")
			})
		})
	  trigger();
		taxSelectNum();
		sum();
	})
	// 当页面渲染完毕后 ，重新算一遍数据，以免多批次送货时税额计算错误
	function trigger()
	{
		$("input[name='detailList.money']").trigger("blur");
	}
	//改变单个税率  
	function taxSelectNum()
	{
		$("select[name='detailList.taxRateId']").off('change').on('change', function()
		{
			if ($(this).val() == -1)
			{
				shotCutWindow('TAXRATE', true, $(this));
			} else
			{
				var flg = true;
				var taxRateId = $(this).val();
				if (taxRateId > 0)
				{
					$(this).parents("tr").find("input[name='detailList.percent']").val(Helper.basic.info("TAXRATE", taxRateId).percent);
				}
				calcTaxRate(this);
			}
		});
	}
	//重新设置序号  
	function resetSequenceNum()
	{
		$("#detailList tbody tr").each(function(index)
		{
			$(this).find("td").first().html(++index);
		});
	}

	//表单提交
	function form_submit()
	{
		$("#btn_save").attr({
			"disabled" : "disabled"
		});
		$("#btn_save_audit").attr({
			"disabled" : "disabled"
		});

		// 提交到服务器保存
		Helper.request({
			url : Helper.basePath + "/oem/deliver/update",
			data : $("#form_order").formToJson(),
			success : function(data)
			{
				if (data.success)
				{
					Helper.message.suc('已保存!');
					location.href = Helper.basePath + '/oem/deliver/view/' + $("#id").val();
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

	//计算加工单价 （输入金额）
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
	function calcTaxRate(obj)
	{
		//获取金额对象并格式化
		var money_dom = $(obj).parent().parent().find("input[name='detailList.money']");

		var money_value = Number(money_dom.val()).tomoney();//金额

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

		//汇总
		sum();
	}
	//汇总
	function sum()
	{
		var sum_qty = 0;
		var sum_tax = 0;
		var sum_noTaxMoney = 0;
		var sum_money = 0;
		$("#detailList tbody tr").each(function()
		{
			sum_qty = Number(sum_qty).add(Number($(this).find("td input[name='detailList.qty']").val()));
			sum_tax = Number(sum_tax).add(Number($(this).find("td input[name='detailList.tax']").val()));
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

	//批量修改税收
	function batchEditTaxRate()
	{
		var taxRateId = $(".batch_taxRate_select").val();
		if (taxRateId == -1)
		{
			shotCutWindow('TAXRATE', true, $(".batch_taxRate_select"), '', "TAXRATE");
		} else
		{
			$("#detailList select[name='detailList.taxRateId']").val(taxRateId);
			if (taxRateId > 0)
			{
				$("input[name='detailList.percent']").val(Helper.basic.info("TAXRATE", taxRateId).percent);
			} else
			{
				$("input[name='detailList.percent']").val(0);
			}
			$("#detailList select[name='detailList.taxRateId']").trigger("change");
		}
	}
	//批量修改交货日期
	function batchEditDeliveryTime()
	{
		$("#detailList input[name='detailList.deliveryTime']").val($(".batch_deliveryTime_input").val());
	}
	// 税率默认值
	function defaultPercent()
	{
		var taxRateId = $("#rateId").val();
		$("select[name='detailList.taxRateId']").val(taxRateId);
		if (taxRateId > 0) // 避免选中自定义或者空选项时js报错
		{
			$("input[name='detailList.percent']").val(Helper.basic.info("TAXRATE", taxRateId).percent);
		} else
		{
			$("input[name='detailList.percent']").val(0);
		}
		$("input[name='detailList.deliveryTime']").val($("#deliveryTime").val());
	}
</script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="代工管理-代工送货-编辑"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="oem:deliver:edit">
						<button class="nav_btn table_nav_btn" id="btn_save">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="oem:deliver:edit,oem:deliver:audit">
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
								<dd class="row-dd">
									<label class="form-label label_ui label_1">客户名称：</label>
									<span class="ui-combo-wrap wrap-width">
										<input type="text" class="input-txt input-txt_7" readonly="true" name="customerName" id="customerName" value="${order.customerName }" />
										<input type="hidden" name="customerId" id="customerId" value="${customer.id }">
										<input type="hidden" name="originCompanyId" id="originCompanyId" value="${order.originCompanyId }">
										<input type="hidden" id="index" />
										<input type="hidden" name="id" id="id" value="${order.id }" />
										<input type="hidden" name="isCheck" id="isCheck" />
										<input type="hidden" name="forceCheck" id="forceCheck" value="NO" />
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
										<input type="text" class="input-txt input-txt_3" name="mobile" id="mobile" value="${order.mobile }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">交货日期：</label>
									<span class="ui-combo-wrap">
										<input type="text" name="deliveryTime" id="deliveryTime" class="input-txt input-txt_3 Wdate" onFocus="WdatePicker({lang:'zh-cn' })" onchange="selectTime();" value="<fmt:formatDate value="${order.deliveryTime}" type="date" pattern="yyyy-MM-dd" />"/>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">销售员：</label>
									<span class="ui-combo-wrap">
										<input type="hidden" id="employeeId" name="employeeId" value="${order.employeeId}" class="input-txt input-txt_1" />
										<input type="text" class="input-txt input-txt_3" readonly name="employeeName" id="employeeName" value="${order.employeeName}" />
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
									<label class="form-label label_ui label_1">送货方式：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('DELIVERYCLASS')}" valueProperty="id" defaultValue="" defaultOption="请选择" selected="${order.deliveryClassId }" name="deliveryClassId" textProperty="name" cssClass="input-txt input-txt_3 hy_select2"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">付款方式：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('PAYMENTCLASS')}" valueProperty="id" defaultValue="" defaultOption="请选择" selected="${order.paymentClassId }" name="paymentClassId" textProperty="name" cssClass="input-txt input-txt_3 hy_select2"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">送货单号：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_3" readonly="readonly" id="billNo" value="${order.billNo }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">备注：</label>
									<span class="form_textarea">
										<textarea name="memo" id="memo" class="noborder" style="width: 952px" placeholder="说点什么...100个字符以内" onKeyUp="Helper.doms.textarealength(this,100)">${order.memo }</textarea>
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
						<table class="border-table" style="width: 1400px" id="detailList">
							<thead>
								<tr>
									<th width="40">序号</th>
									<th width="40">操作</th>
									<th width="140">成品名称</th>
									<th width="100">工序名称</th>
									<th width="60">加工规格</th>
									<th width="80">送货数量</th>
									<shiro:hasPermission name="oem:deliver:money">
										<th width="50">单价</th>
										<th width="50">金额</th>
										<th width="50">税额</th>
									</shiro:hasPermission>
									
									<th width="75">
										税收
										<i id="batch_edit_taxRate" class="fa fa-edit" src="batch_taxRate_box"></i>
										<div class="batch_box_container"></div>
									</th>
									<th width="80" style="display: none">已退货数量</th>
									<th width="80" style="display: none">已对账数量</th>
									<th width="120">源单单号</th>
									<th width="120">发外单号</th>
									<th width="80">源单数量</th>

									<th width="80" style="display: none">税率值</th>
									<th width="80" style="display: none">不含税单价</th>
									<th width="80" style="display: none">不含税金额</th>
									<th width="50">部件名称</th>
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
										<td>
											<input class="tab_input" type="text" name="detailList.productName" readonly="readonly" value="${detail.productName }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.procedureName" readonly="readonly" value="${detail.procedureName }" />
											<input type="hidden" name="detailList.procedureId" readonly="readonly" value="${detail.procedureId }" />
											<input type="hidden" name="detailList.procedureClassId" readonly="readonly" value="${detail.procedureClassId }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.style" readonly="readonly" value="${detail.style }" />
										</td>
										<td>
											<input class="tab_input bg_color constraint_decimal_negative" type="text" name="detailList.qty" value="${detail.qty }" />
											<input type="hidden" name="detailList.saveQty" value="${detail.qty }" />
										</td>
										<td>
											<input class="tab_input bg_color constraint_decimal_negative" type="text" name="detailList.price" value="${detail.price }" />
										</td>
										<td>
											<input class="tab_input bg_color constraint_decimal_negative" type="text" name="detailList.money" value="${detail.money }" />
											<input type="hidden" name="detailList.saveMoney" value="${detail.money }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.tax" readonly="readonly" value="${detail.tax }" />
										</td>
										<td>
											<phtml:list items="${fns:basicList2('TAXRATE')}" cssClass="tab_input bg_color" valueProperty="id" textProperty="name" name="detailList.taxRateId" selected="${detail.taxRateId }" />
										</td>
										<td style="display: none">
											<input class="tab_input" type="text" name="detailList.returnedQty" readonly="readonly" value="0" />
										</td>
										<td style="display: none">
											<input class="tab_input" type="text" name="detailList.reconcilQty" readonly="readonly" value="0" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.sourceBillNo" readonly="readonly" value="${detail.sourceBillNo }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.originBillNo" readonly="readonly" value="${detail.originBillNo }" />
											<input type="hidden" name="detailList.originBillId" readonly="readonly" value="${detail.originBillId }" />
										</td>
								    <td>
											<input class="tab_input" type="text" name="detailList.sourceQty" readonly="readonly" value="${detail.sourceQty }" />
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
											<input class="tab_input bg_color memo" type="text" onmouseover="this.title=this.value" name="detailList.processRequire" value="${detail.processRequire }" />
										</td>
										<td>
											<input class="tab_input bg_color memo" type="text" onmouseover="this.title=this.value" name="detailList.memo" value="${detail.memo }" />
											<input type="hidden" name="detailList.originProcedureId" readonly="readonly" value="${detail.originProcedureId }" />
											<input type="hidden" name="detailList.originCompanyId" readonly="readonly" value="${detail.originCompanyId }" />
											<input type="hidden" name="detailList.sourceId" readonly="readonly" value="${detail.sourceId }" />
											<input type="hidden" name="detailList.sourceDetailId" readonly="readonly" value="${detail.sourceDetailId }" />
								      <input type="hidden" name="detailList.sourceBillType" readonly="readonly" value="${detail.sourceBillType }" />
											<input type="hidden" name="detailList.productId" readonly="readonly" value="${detail.productId }" />
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
				<div class="cl form_content">
					<dl class="cl row-dl-foot">
						<!-- 判断是否有查看金额权限---begin -->
						<dd class="row-dd">
							<label class="form-label label_ui label_7">金 额：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" name="totalMoney" id="totalMoney" class="input-txt input-txt_3" readonly="readonly" value="" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">金额(不含税)：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" name="noTaxTotalMoney" id="noTaxTotalMoney" class="input-txt input-txt_3" readonly="readonly" value="" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">税 额：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" name="totalTax" id="totalTax" class="input-txt input-txt_3" readonly="readonly" value="" />
							</span>
						</dd>
						<!-- 判断是否有查看金额权限---end -->
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
								<input name="createName" type="text" class="input-txt input-txt_3" readonly="readonly" value="${loginUser.realName}" />
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
								<input name="checkUserName" type="text" class="input-txt input-txt_3" readonly="readonly" value="" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">审核日期：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="checkTime" type="text" class="input-txt input-txt_3" readonly="readonly" value="" />
							</span>
						</dd>
					</dl>
				</div>
				<!-- 批量悬浮窗 satrt -->
				<div id="batch_wareHouse_box" class="batch_wareHouse_box">
					<phtml:list items="${fns:basicListParam2('WAREHOUSE', 'warehouseType','PRODUCT')}" cssClass="tab_input bg_color input-txt_3 batch_wareHouse_select hy_select2" valueProperty="id" name="" textProperty="name" onchange="batchEditWareHouse()"></phtml:list>
				</div>
				<div id="batch_taxRate_box" class="batch_taxRate_box">
					<phtml:list items="${fns:basicList2('TAXRATE')}" cssClass="tab_input bg_color  input-txt_3 batch_taxRate_select hy_select2" valueProperty="id" textProperty="name" name="rateId" selected="${customer.taxRateId }" onchange="batchEditTaxRate()" />
				</div>
				<!-- 批量悬浮窗 end -->
			</form>
		</div>
	</div>
</body>
</html>
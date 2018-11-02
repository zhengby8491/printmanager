var hasPermission = Helper.basic.hasPermission('sale:return:money');
$(function()
{
	var customerJSON = $("#customerJSON").val();
	var deliverDetailJSON = $("#deliverDetailJSON").val();
	var basicListParam2 = $("#basicListParam2").val();
	if (!hasPermission)
	{
		$("th[name='price']").hide();
		$("th[name='money']").hide();
		$("th[name='tax']").hide();
		$("dd").has("input[name='totalMoney']").hide();
		$("dd").has("input[name='noTaxTotalMoney']").hide();
		$("dd").has("input[name='totalTax']").hide();
	}
	// 取消
	$("#btn_cancel").click(function()
	{
		closeTabAndJump("销售退货列表");
	});
	/* 选择客户 */
	$("#selectCustomer").click(function()
	{
		Helper.popup.show('选择客户', Helper.basePath + '/quick/customer_select', '900', '490');
	});
	/* 选择退货源 */
	$("#select_deliver").click(function()
	{
		var _customerId = $("#customerId").val();
		if (Helper.isEmpty(_customerId))
		{
			Helper.message.alert("请先选择客户信息");
			return false;
		}
		Helper.popup.show('选择送货单', Helper.basePath + '/sale/deliver/quick_select?multiple=true&rowIndex=1&customerId=' + _customerId, '800', '490');
	});

	/* 删除 */
	$("table tbody").on("click", "a[name='btn_del']", function()
	{
		$(this).parent().parent().remove();
		resetSequenceNum();
		sum();
	});

	// 初始付款日期
	$("#deliveryTime").val(new Date().format('yyyy-MM-dd'));
	// 初始化制单日期
	$("#createDate").val(new Date().format('yyyy-MM-dd'));

	// 保存
	$("#btn_save,#btn_save_audit").click(function()
	{
		fixEmptyValue();
		if (Helper.isEmpty($("#customerId").val()))
		{
			Helper.message.warn("请选择客户信息")
			return false;
		}

		if ($("input[name='detailList.qty']").size() <= 0)
		{
			Helper.message.warn("请选择送货单");
			validate = false;
			return;
		}
		/*
		 * if(Number($("#totalMoney").val())<=0) { Helper.message.warn("金额必须大于0");
		 * return false; }
		 */
		var flg = true;
		$("table input[name='detailList.qty']").each(function()
		{
			if (Number($(this).val()) <= 0)
			{
				Helper.message.warn("产品数量必须大于0");
				flg = false;
				return false;
			}
		});
		if (flg)
		{
			$(this).attr("disabled", "true");
			$("#isCheck").val($(this).attr("id") == "btn_save_audit" ? "YES" : "NO");
			form_submit();
		}
	})

	// 所有数量改变事件
	$("table tbody").on("keyup blur", "input[name='detailList.qty']", function()
	{
		var qty_value = Number($(this).val()).trunc();
		$(this).val(qty_value);
		var saveQty = $(this).parent().parent().find("input[name='detailList.saveQty']").val();
		if ($(this).val() == saveQty)
		{
			var saveMoney = $(this).parent().parent().find("input[name='detailList.saveMoney']").val();
			$(this).parent().parent().find("input[name='detailList.money']").val(Number(saveMoney).tomoney());
			calcTaxRate(this);
			return;
		}
		calcMoney(this);
	});
	// 初始化批量修改仓库、税收、交货日期悬浮窗
	$("#batch_edit_wareHouse,#batch_edit_taxRate,#batch_edit_deliveryTime").each(function()
	{
		$(this).powerFloat({
			eventType : "click",
			targetAttr : "src",
			reverseSharp : true,
			container : $(this).siblings(".batch_box_container")
		})
	});

	// 从送货单转到退货单
	if (customerJSON)
	{
		var obj = $.parseJSON(customerJSON);
		getCallInfo_customer(obj);
		if (deliverDetailJSON)
		{
			var rows = $.parseJSON(deliverDetailJSON);
			getCallInfo_deliverSingle(rows);
		}
	}

	// 监听仓库
	$("table tbody").on("change", "select[name='detailList.warehouseId']", function()
	{
		shotCutWindow('WAREHOUSE', true, $(this), 'PRODUCT');
	})
});

// 默认值
function defaultPercent()
{
	$("input[name='detailList.deliveryTime']").val($("#deliveryTime").val());
}

// 批量修改税收
function batchEditTaxRate()
{
	var taxRateId = $(".batch_taxRate_select").val();
	if (taxRateId == -1)
	{
		shotCutWindow('TAXRATE', true, $(".batch_taxRate_select"), '', "TAXRATE");
	}
	if (taxRateId != -1) // 选中了自定义后不触发其他的下拉框
	{
		$("table select[name='detailList.taxRateId']").val(taxRateId);
		$("table select[name='detailList.taxRateId']").trigger("change");
	}

}
// 批量修改仓库
function batchEditWareHouse()
{
	var wareTypeId = $(".batch_wareHouse_select").val();
	if (wareTypeId == -1)
	{
		shotCutWindow('WAREHOUSE', true, $(".batch_wareHouse_select"), 'PRODUCT', "WAREHOUSE");
	} else
	{
		$("table select[name='detailList.warehouseId']").val($(".batch_wareHouse_select").val());
	}
}

// 改变单个税率
function taxSelectNum()
{
	$("select[name='detailList.taxRateId']").change(function()
	{
		if ($(this).val() == -1) // 自定义
		{
			shotCutWindow("TAXRATE", true, $(this));
		} else
		{
			var flg = true;
			var taxRateId = $(this).val();
			$(this).parent().next().children().val(Helper.basic.info("TAXRATE", taxRateId).percent);
			calcTaxRate(this);
		}
	});
}

// 选择交货日期事件
function selectTime()
{
	$("input[name='detailList.deliveryTime']").val($("#deliveryTime").val());
}

function form_submit()
{
	$("#btn_save").attr({
		"disabled" : "disabled"
	});
	$("#btn_save_audit").attr({
		"disabled" : "disabled"
	});

	Helper.request({
		url : Helper.basePath + "/sale/return/save",
		data : $("#form_order").formToJson(),
		success : function(data)
		{
			if (data.isSuccess)
			{
				Helper.message.suc('已保存!');
				if (data.returnObject != null && data.returnObject == false)
				{
					Helper.message.open({
						title : '',
						content : '审核失败',
						yes : function(index)
						{
							location.href = Helper.basePath + '/sale/return/view/' + data.returnValue.id;
							layer.close(index);
						},
						closeBtn : 0
					})
				} else
				{
					location.href = Helper.basePath + '/sale/return/view/' + data.returnValue.id;
				}
			} else
			{
				Helper.message.warn('保存失败!');
				$("#btn_save").removeAttr("disabled");
				$("#btn_save_audit").removeAttr("disabled");
			}
		},
		error : function(data)
		{
			// console.log(data);
			$("#btn_save").removeAttr("disabled");
			$("#btn_save_audit").removeAttr("disabled");
		}
	});
}

function getCallInfo_deliverSingle_dbl(rows)
{
	var array = new Array();
	array[0] = rows;
	getCallInfo_deliverSingle(array);
}
// 获取返回产品数组信息
function getCallInfo_deliverSingle(rows)
{
	if (rows.length > 0)
	{
		appendTr("detailList", rows);
		resetSequenceNum();
	}
	calculation();
	sum();
	defaultPercent();
	taxSelectNum();
}
// 获取返回客户信息
function getCallInfo_customer(obj)
{
	var old_customerId = $("#customerId").val();
	$("#employeeId").val(obj.employeeId);
	$("#customerCode").val(obj.code);
	$("#customerName").val(obj.name);
	$("#customerId").val(obj.id);
	if (Helper.isNotEmpty(obj.defaultAddress))
	{
		$("#linkName").val(obj.defaultAddress.userName);
		$("#mobile").val(obj.defaultAddress.mobile);
		$("#deliveryAddress").val(obj.defaultAddress.address);
	} else
	{
		$("#linkName").val("");
		$("#mobile").val("");
		$("#deliveryAddress").val("");
	}
	$("#deliveryClassId").val(obj.deliveryClassId).trigger("change");
	$("#paymentClassId").val(obj.paymentClassId).trigger("change");
	if (Helper.isNotEmpty(obj.taxRateId))
	{
		$("#rateId").val(Helper.basic.info('TAXRATE', obj.taxRateId).id).trigger("change");
	}

	$("#currencyTypeText").val(Helper.basic.getEnumText('com.huayin.printmanager.persist.enumerate.CurrencyType', obj.currencyType, 'text'));
	$("#currencyType").val(obj.currencyType);

	if (Helper.isNotEmpty(old_customerId) && obj.id != old_customerId)
	{// 如果改变供应商，则清空table
		// 清空table tr
		$("table tbody tr").remove();
	}

	if (Helper.isNotEmpty(obj.employeeId))
	{
		$("#employeeName").val(Helper.basic.info('EMPLOYEE', obj.employeeId).name);
	} else
	{
		$("#employeeName").val("");
	}
	sum();
}

function appendTr(tableId, rows)
{
	$.each(rows, function()
	{
		var _THIS = this;
		// //console.log(_THIS);
		var _TR = $("<tr/>");
		var idArray = $("table tbody tr td input[name='detailList.sourceDetailId']").map(function()
		{
			return this.value
		}).get();
		if (Helper.isNotEmpty(idArray) && idArray.contains("" + this.id))
		{// 如果已存在则跳过本次循环
			return true;// continue;
		}
		$("#" + tableId).find("thead tr th").each(function()
		{
			var name = $(this).attr("name");
			var value = eval("_THIS." + name);
			value = value == undefined ? "" : value;
			switch (name)
			{
			case 'seq':
				_TR.append('<td></td>');
				break;
			case 'operator':
				_TR.append('<td class="td-manage"><input name="detailList.productId" type="hidden" readonly="readonly" value="' + _THIS.productId + '"/><a title="删除行" href="javascript:void(0)" name="btn_del"><i class="delete fa fa-trash-o"></i></a></td>');
				break;
			case 'productCode':
				_TR.append('<td style="display: none"><input name="detailList.productCode" class="tab_input" type="text" readonly="readonly" value="' + value + '"/></td>');
				break;
			case 'productName':
				_TR.append('<td><input name="detailList.productName" class="tab_input" readonly="readonly" type="text" value="' + value + '"/></td>');
				break;
			case 'imgUrl':
				if (_THIS.imgUrl != '')
				{
					_TR.append('<td><img class="pimg" src="' + value + '"/></td>');
				} else
				{
					_TR.append('<td></td>');
				}
				break;
			case 'style':
				_TR.append('<td><input name="detailList.style" class="tab_input" readonly="readonly" type="text" value="' + value + '"/></td>');
				break;
			case 'unitId':
				_TR.append('<td><input name="detailList.unitId" class="tab_input" readonly="readonly" type="hidden" value="' + value + '"/><input name="detailList.unitName" class="tab_input" readonly="readonly" type="text" value="' + Helper.basic.info("UNIT", value).name + '"/></td>');
				break;
			case 'sourceQty':
				_TR.append('<td><input class="tab_input" type="text" name="detailList.sourceQty" readonly="readonly" value="' + _THIS.qty + '"/></td>');
				break;
			case 'billNo':
				_TR.append('<td><input class="tab_input" type="hidden" name="detailList.sourceDetailId" value="' + _THIS.id + '"/>' + '<input class="tab_input" type="hidden" name="detailList.sourceId" value="' + _THIS.masterId + '"/>' + '<input class="tab_input" type="hidden" name="detailList.sourceBillType" value="' + _THIS.master.billType + '"/>' + '<input class="tab_input" type="text" name="detailList.sourceBillNo" readonly="readonly" value="' + _THIS.master.billNo + '"/></td>');
				break;
			case 'saleOrderBillNo':
				_TR.append('<td><input class="tab_input" type="text" name="detailList.saleOrderBillNo" readonly="readonly" value="' + value + '"/></td>');
				break;
			case 'customerMaterialCode':
				_TR.append('<td><input class="tab_input" type="text" name="detailList.customerMaterialCode" readonly="readonly" value="' + value + '"/></td>');
				break;
			case 'customerBillNo':
				_TR.append('<td><input class="tab_input" type="text" name="detailList.customerBillNo" readonly="readonly" value="' + value + '"/></td>');
				break;
			case 'qty':
				_TR.append('<td><input class="tab_input bg_color" type="text" name="detailList.qty" value="' + (_THIS.qty - _THIS.returnQty - _THIS.reconcilQty || 0) + '"/>' + '<input type="hidden" name="detailList.saveQty" value="' + (_THIS.qty - _THIS.returnQty - _THIS.reconcilQty || 0) + '"/></td>');
				break;
			case 'reconcilQty':
				_TR.append('<td style="display: none"><input class="tab_input bg_color" type="text" name="detailList.reconcilQty" value="' + value + '"/></td>');
				break;
			case 'warehouseId':
				_TR.append('<td><phtml:list items="' + basicListParam2 + '"  valueProperty="id" textProperty="name" name="detailList.warehouseId" cssClass="tab_input bg_color"></phtml:list></td>');
				break;
			case 'price':
				_TR.append((hasPermission ? '<td>' : '<td style="display:none;">') + '<input class="tab_input" readonly="readonly" type="text" name="detailList.price" value="' + value + '"/></td>');
				break;
			case 'money':
				_TR.append((hasPermission ? '<td>' : '<td style="display:none;">') + '<input class="tab_input" type="text" readonly="readonly" name="detailList.money" value="' + (_THIS.money - _THIS.returnMoney - _THIS.reconcilMoney || 0) + '"/>' + '<input type="hidden" name="detailList.saveMoney" value="' + (_THIS.money - _THIS.returnMoney - _THIS.reconcilMoney || 0) + '"/></td>');
				break;
			case 'taxRateId':
				_TR.append('<td><input class="tab_input" type="text" name="detailList.taxRateName" value="' + Helper.basic.info("TAXRATE", value).name + '" readonly="readonly"/><input name="detailList.taxRateId" type="hidden" value="' + value + '"></td>');
				break;
			case 'percent':
				_TR.append('<td style="display: none"><input class="tab_input" type="text" name="detailList.percent" readonly="readonly" value="' + value + '"/></td>');
				break;
			case 'tax':
				_TR.append((hasPermission ? '<td>' : '<td style="display:none;">') + '<input class="tab_input" type="text" name="detailList.tax" readonly="readonly" value="' + value + '"/></td>');
				break;
			case 'noTaxPrice':
				_TR.append('<td style="display: none"><input class="tab_input" type="text" name="detailList.noTaxPrice" readonly="readonly" value="' + value + '"/></td>');
				break;
			case 'noTaxMoney':
				_TR.append('<td style="display: none"><input class="tab_input" type="text" name="detailList.noTaxMoney" readonly="readonly" value="' + value + '"/></td>');
				break;
			case 'memo':
				_TR.append('<td><input name="detailList.memo" class="tab_input bg_color memo" type="text" onmouseover="this.title=this.value" value="' + value + '"/></td>');
				break;
			}

		});
		_TR.appendTo("#" + tableId);
	});
}
// 重新设置序号
function resetSequenceNum()
{
	$("table tbody tr").each(function(index)
	{
		$(this).find("td").first().html(++index);
	});
}

// 计算金额
function calcMoney(obj)
{
	var qty_dom = $(obj).parent().parent().find("input[name='detailList.qty']");
	qty_dom.val(Number(qty_dom.val()).trunc());
	var price_dom = $(obj).parent().parent().find("input[name='detailList.price']");
	var money_dom = $(obj).parent().parent().find("input[name='detailList.money']");

	money_dom.val(Number(Number(price_dom.val()).mul(Number(qty_dom.val()))).tomoney());// 金额=(单价*数量)

	calcTaxRate(obj);
}
// 计算税额
function calcTaxRate(obj)
{
	// 获取金额对象并格式化
	var money_dom = $(obj).parent().parent().find("input[name='detailList.money']");
	var money_value = Number(money_dom.val()).tomoney();// 金额

	var taxRatePercent = Number($(obj).parent().parent().find("input[name='detailList.percent']").val());// 税率值
	// 不含税金额计算
	var noTaxMoney_dom = $(obj).parent().parent().find("input[name='detailList.noTaxMoney']");
	noTaxMoney_dom.val(Number(money_value).div(Number(1 + (taxRatePercent / 100))).tomoney());// 不含税金额=（金额/(1+税率值/100)）

	// 税额计算
	var tax_dom = $(obj).parent().parent().find("input[name='detailList.tax']");
	tax_dom.val(money_value.subtr(Number(noTaxMoney_dom.val())).tomoney());// 税额=(金额-不含税金额)

	var qty_value = Number($(obj).parent().parent().find("input[name='detailList.qty']").val());// 数量
	// 单价计算
	var price_dom = $(obj).parent().parent().find("input[name='detailList.price']");

	// 不含税单价计算
	var noTaxPrice_dom = $(obj).parent().parent().find("input[name='detailList.noTaxPrice']");
	noTaxPrice_dom.val(Number(price_dom.val()).div(Number(1 + (taxRatePercent / 100))).toFixed(4));// 不含税单价=(单价/(1+税率值/100))

	// 汇总
	sum();
}
// 初始计算金额和税额
function calculation()
{
	$("table tbody tr").each(function()
	{
		// var qty=$(this).find("td input[name='detailList.qty']").val();
		// var price=$(this).find("td input[name='detailList.price']").val();
		// var percent=$(this).find("td input[name='detailList.percent']").val();

		// var noTaxPrice_dom=$(this).find("td
		// input[name='detailList.noTaxPrice']");

		// //计算金额
		// var money_dom=$(this).find("td input[name='detailList.money']");
		// money_dom.val(Number(price).mul(Number(qty)).tomoney());//金额=(单价*数量)

		// //不含税金额计算
		// var noTaxMoney_dom=$(this).find("td
		// input[name='detailList.noTaxMoney']");
		// noTaxMoney_dom.val(Number(money_dom.val()).div(Number(1+(percent/100))).tomoney());//不含税金额=（金额/(1+税率值/100)）

		// //税额计算
		// var tax_dom=$(this).find("td input[name='detailList.tax']");
		// tax_dom.val(Number(money_dom.val()).subtr(Number(noTaxMoney_dom.val())).tomoney());//税额=(金额-不含税金额)
		$(this).find("td input[name='detailList.qty']").trigger("blur");
	});
}
// 汇总
function sum()
{
	var sum_qty = 0;
	var sum_tax = 0;
	var sum_noTaxMoney = 0;
	var sum_money = 0;
	$("table tbody tr").each(function()
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
// 选择仓库事件
function selectHouse()
{
	$("select[name='detailList.warehouseId']").val($("#warehouseId").val());
}
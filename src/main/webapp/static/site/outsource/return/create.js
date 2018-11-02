var hasPermission = Helper.basic.hasPermission('outsource:return:money');
$(function()
{
	if (!hasPermission)
	{
		$("dd").has("input[name='totalMoney']").hide();
		$("dd").has("input[name='noTaxTotalMoney']").hide();
		$("dd").has("input[name='totalTax']").hide();
	}
	// 取消
	$("#btn_cancel").click(function()
	{
		closeTabAndJump("发外退货列表");
	});
	/* 选择供应商 */
	$("#selectSupplier").click(function()
	{
		Helper.popup.show('选择供应商', Helper.basePath + '/quick/supplier_select?multiple=false&supplierType=PROCESS', '900', '500');
	});

	/* 删除行 */
	$("table").on("click", ".row_delete", function()
	{
		$(this).parent().parent().remove();
		resetSequenceNum();
		selectWarehouse();
		sum();
	});

	/* 来源选择 */
	$("#supplier_quick_select").click(function()
	{
		if ($(this).val() == "")
		{
			Helper.message.warn("请选择供应商");
			return;
		}
		Helper.popup.show('选择发外到货', Helper.basePath + '/outsource/return/quick_select?supplierId=' + $(this).val(), '800', '490');
	});
	// 初始化制单日期
	$("#createDate").val(new Date().format('yyyy-MM-dd'));

	// 全局税率改变事件
	$(document).on("change", "#taxRate_public_select", function()
	{
		var percent = Helper.basic.info('TAXRATE', $(this).val()).percent;// 税率值
		$("select[name='detailList.taxRate']").val($(this).val());// 改变明细税率
		$("table select[name='detailList.taxRate']").trigger("change");// 出发明细改变事件
	});

	// 仓库改变事件
	$(document).on("change", "#warehouse_public_select", function()
	{
		$("table select[name='detailList.warehouse']").val($(this).val());// 改变明细仓库

		$("table select[name='detailList.warehouse']").trigger("change");// 触发发明细仓库改变事件
	});

	// 所有单个仓库改变事件
	$(document).on("change", "table select[name='detailList.warehouse']", function()
	{
		$(this).parent().parent().find("input[name='detailList.warehouseId']").val($(this).val());// 更新仓库ID

	});

	// 所有退货数量改变事件
	$(document).on("blur", "table input[name='detailList.qty']", function()
	{
		var productType = $(this).parent().parent().find("input[name='detailList.productType']").val();
		var workProcedureType = $(this).parent().parent().find("input[name='detailList.workProcedureType']").val();
		var qty_value;
		if (productType == 'ROTARY' && workProcedureType == 'PART')
		{
			qty_value = Number($(this).val()).toFixed(2);
		} else
		{
			qty_value = Number($(this).val()).trunc();
		}
		$(this).val(qty_value);
		var saveQty = $(this).parent().parent().find("input[name='detailList.saveQty']").val();
		if ($(this).val() == saveQty)
		{
			var saveMoney = $(this).parent().parent().find("input[name='detailList.saveMoney']").val();
			$(this).parent().parent().find("input[name='detailList.money']").val(saveMoney);
			calcTaxRate(this);
			return;
		}
		calcMoney(this);
	});
	// 所有单个税率改变事件
	$(document).on("change", "table select[name='detailList.taxRateId']", function()
	{
		if ($(this).val() == -1)
		{
			shotCutWindow("TAXRATE", true, $(this));
		} else
		{
			var taxRate = Helper.basic.info('TAXRATE', $(this).val());// 税率值
			$(this).parent().parent().find("input[name='detailList.taxRateName']").val(taxRate.name);// 更新税率名称
			$(this).parent().parent().find("input[name='detailList.taxRatePercent']").val(taxRate.percent);// 更新税率值
			calcTaxRate(this);
		}
	});

	/*
	 * $("table select[name='detailList.taxRate']").trigger("change");//触发税率明细改变事件
	 * 
	 * //纠正税率名称 $("table select[name='detailList.taxRate']").each(function() {
	 * $(this).val($(this).parent().parent().find("input[name='detailList.taxRateId']").val());
	 * });
	 */
	// 纠正仓库名称
	$("table select[name='detailList.warehouse']").each(function()
	{

		$(this).val($(this).parent().parent().find("input[name='detailList.warehouseId']").val());

	});
	// 保存
	$("#btn_save,#btn_save_audit").click(function()
	{
		$("#forceCheck").val("NO");

		if (Helper.isEmpty($("#supplierId").val()))
		{
			Helper.message.warn("请选择供应商")
			return false;
		}
		if ($("input[name='detailList.qty']").size() <= 0)
		{
			Helper.message.warn("请录入明细");
			validate = false;
			return;
		}
		fixEmptyValue();
		var validate = true;
		paramObj = $("#form_return").formToJson();
		paramObj.isCheck = $(this).attr("id") == "btn_save_audit" ? "YES" : "NO";
		$("table input[name='detailList.qty']").each(function()
		{
			// console.log("qty="+$(this).val());
			if (Helper.isEmpty($(this).val()) || Number($(this).val()) <= 0)
			{
				Helper.message.warn("退货数量必须大于0");
				validate = false;
				return;
			}
		});
		if (validate)
		{// 保存
			form_submit();
		}
	})

	// 初始化【生成采购退货】
	var initSupplier = $("#supplierJson").val(), initDetailList = $("#detailListJson").val();
	if (initSupplier)
	{
		getCallInfo_supplier($.parseJSON(initSupplier));
	}
	if (initDetailList)
	{
		getCallInfo_refund($.parseJSON(initDetailList));
	}
	// 监听仓库事件
	/*
	 * $(document).on("change","table
	 * select[name='detailList.warehouseId']",function(){ if ($(this).val() == -1) {
	 * shotCutWindow('WAREHOUSE',true,$(this),'MATERIAL'); } })
	 */
});
function form_submit()
{
	paramObj.forceCheck = "NO";

	$("#btn_save").attr({
		"disabled" : "disabled"
	});
	$("#btn_save_audit").attr({
		"disabled" : "disabled"
	});
	Helper.request({
		url : Helper.basePath + "/outsource/return/save",
		data : paramObj,// 将form序列化成JSON字符串
		success : function(data)
		{
			if (data.isSuccess)
			{
				Helper.message.suc('已保存!');

				if (data.returnObject != null && data.returnObject.length > 0)
				{
					var str = '审核失败,以下产品库存数量不足\n';
					for (var i = 0; i < data.returnObject.length; i++)
					{
						str = str + '名称:' + data.returnObject[i].product.name + '   目前库存数量' + data.returnObject[i].qty + '\n';
					}
					Helper.message.confirm(str, function(index)
					{
						Helper.message.confirm("确认操作会引起负库存，是否允许负库存?", function(index)
						{
							forceCheck(data.returnValue.id);
						}, function(index)
						{
							location.href = Helper.basePath + '/outsource/return/view/' + data.returnValue.id;
						});
					}, function(index)
					{
						location.href = Helper.basePath + '/outsource/return/view/' + data.returnValue.id;
					});
					$("#btn_save").removeAttr("disabled");
					$("#btn_save_audit").removeAttr("disabled");

				} else
				{
					location.href = Helper.basePath + '/outsource/return/view/' + data.returnValue.id;
				}

			} else
			{
				Helper.message.warn('保存失败!' + data.message);
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

// 计算金额
function calcMoney(obj)
{
	var qty_dom = $(obj).parent().parent().find("input[name='detailList.qty']");
	var productType = $(obj).parent().parent().find("input[name='detailList.productType']").val();
	var workProcedureType = $(obj).parent().parent().find("input[name='detailList.workProcedureType']").val();

	if (productType == 'ROTARY' && workProcedureType == 'PART')
	{
		qty_dom.val(Number(qty_dom.val()).toFixed(2));
	} else
	{
		qty_dom.val(Number(qty_dom.val()).trunc());
	}
	var price_dom = $(obj).parent().parent().find("input[name='detailList.price']");
	var money_dom = $(obj).parent().parent().find("input[name='detailList.money']");

	money_dom.val(Number(price_dom.val()).mul(Number(qty_dom.val())).tomoney());// 金额=(单价*加工数量)

	calcTaxRate(obj);
}
// 计算税额
function calcTaxRate(obj)
{
	// 获取金额对象并格式化
	var money_dom = $(obj).parent().parent().find("input[name='detailList.money']");
	var money_value = Number(money_dom.val()).tomoney();// 金额

	var taxRatePercent = Number($(obj).parent().parent().find("input[name='detailList.taxRatePercent']").val());// 税率值
	// 不含税金额计算
	var noTaxMoney_dom = $(obj).parent().parent().find("input[name='detailList.noTaxMoney']");
	noTaxMoney_dom.val(Number(money_value).div(Number(1 + (taxRatePercent / 100))).tomoney());// 不含税金额=（金额/(1+税率值/100)）
	// console.log(noTaxMoney_dom.val() + "," +money_value + "," +
	// (Number(1+(taxRatePercent/100))))
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

// 汇总
function sum()
{
	var money_sum = 0;
	var tax_sum = 0;
	var noTaxMoney_sum = 0;

	$("table input[name='detailList.money']").each(function()
	{
		money_sum += (Number($(this).val()));
	});
	$("table input[name='detailList.noTaxMoney']").each(function()
	{
		noTaxMoney_sum += (Number($(this).val()));
	});
	$("table input[name='detailList.tax']").each(function()
	{
		tax_sum += (Number($(this).val()));
	});

	$("#sum_money").val(money_sum.tomoney());
	$("#sum_noTaxMoney").val(noTaxMoney_sum.tomoney());
	$("#sum_tax").val(tax_sum.tomoney());
}

// 获取返回信息
function getCallInfo_supplier(obj)
{
	var old_supplierId = $("#supplierId").val();
	$("#supplierId").val(obj.id);
	$("#supplierCode").val(obj.code);
	$("#supplierName").val(obj.name);
	$("#supplier_quick_select").val(obj.id);
	if (Helper.isNotEmpty(obj.defaultAddress))
	{
		$("#linkName").val(obj.defaultAddress.userName);
		$("#mobile").val(obj.defaultAddress.mobile);
		$("#supplierAddress").val(obj.defaultAddress.address);
	} else
	{
		$("#linkName").val("");
		$("#mobile").val("");
		$("#supplierAddress").val("");
	}
	$("#deliveryClassId").val(obj.deliveryClassId).trigger("change");
	$("#paymentClassId").val(obj.paymentClassId).trigger("change");
	$("#settlementClassId").val(obj.settlementClassId).trigger("change");
	if (Helper.isNotEmpty(obj.taxRateId))
	{
		$("#taxRate_public_select").val(Helper.basic.info('TAXRATE', obj.taxRateId).id);
		$("#taxRate_public_select").trigger("change");// 触发税率改变事件
	}
	if (Helper.isNotEmpty(old_supplierId) && obj.id != old_supplierId)
	{// 如果改变供应商，则清空table
		// 清空table tr
		$("table tbody tr").remove();
	}
	if (Helper.isNotEmpty(obj.employeeId))
	{
		$("#employeeName").val(Helper.basic.info('EMPLOYEE', obj.employeeId).name);
	} else
	{
		$("#employeeName").val();
	}
	$("#employeeId").val(obj.employeeId);
	$("#currencyTypeText").val(Helper.basic.getEnumText('com.huayin.printmanager.persist.enumerate.CurrencyType', obj.currencyType, 'text'));
	$("#currencyType").val(obj.currencyType);
}

// 重新设置序号
function resetSequenceNum()
{
	$("table tbody tr").each(function(index)
	{
		$(this).find("td").first().html(++index);
	});
}
function selectWarehouse()
{
	// 纠正仓库名称
	$("table select[name='detailList.warehouseId']").each(function()
	{
		$(this).val($(this).parent().parent().find("input[name='detailList.warehouse']").val());
	});
}
function getCallInfo_refund(rows)
{
	$.each(rows, function()
	{
		var _THIS = this;
		var _TR = $("<tr/>");
		var sourceDetailIdArray = $("table input[name='detailList.sourceDetailId']").map(function()
		{
			return this.value
		}).get();
		if (Helper.isNotEmpty(sourceDetailIdArray) && sourceDetailIdArray.contains("" + _THIS.id))
		{// 如果已存在则跳过本次循环
			return true;// continue;
		}
		$("#detailList").find("thead tr th").each(
				function()
				{

					var name = $(this).attr("name");
					var value = eval("_THIS." + name);
					value = value == undefined ? "" : value;

					switch (name)
					{
					case 'seq':
						_TR.append('<td></td>');
						if (!hasPermission)
						{
							_TR.append('<td style="display:none;"><input class="tab_input" type="text" name="detailList.tax" readonly="readonly" value="' + _THIS.tax + '"/>' + '<input class="tab_input" type="text" readonly="readonly" name="detailList.price" value="' + Number(_THIS.money - _THIS.returnMoney - _THIS.reconcilMoney).div(Number(_THIS.qty - _THIS.returnQty - _THIS.reconcilQty)).toFixed(4) + '"/>' + '<input class="tab_input" type="text" readonly="readonly" name="detailList.money" value="' + (_THIS.money - _THIS.returnMoney - _THIS.reconcilMoney || 0) + '"/>' + '<input type="hidden" name="detailList.saveMoney" value="' + (_THIS.money - _THIS.returnMoney - _THIS.reconcilMoney || 0) + '"/>');
						}
						break;
					case 'operator':
						_TR.append('<td class="td-manage"><i title="删除行" class="fa fa-trash-o row_delete"></i></td>');
						break;
					case 'warehouse':
						if (_THIS.type == 'PRODUCT')
						{
							_TR.append('<td>'+$("#PRODUCT").clone(true).html());
						}
						if (_THIS.type == 'PROCESS')
						{
							_TR.append('<td>'+$("#SEMI_PRODUCT").clone(true).html());
						}
						break;
					case 'productName':
						if (_THIS.type == 'PRODUCT')
						{
							_TR.append('<td><input class="tab_input" type="text" name="detailList.productName" readonly="readonly" value="' + (_THIS.productName || '') + '"/>' + '<input class="tab_input" type="hidden" name="detailList.productNames" readonly="readonly" value=""/>' + '</td>');
						}
						if (_THIS.type == 'PROCESS')
						{
							_TR.append('<td><input class="tab_input" type="text" name="detailList.productNames" readonly="readonly" value="' + (_THIS.productNames || '') + '"/>' + '<input class="tab_input" type="hidden" name="detailList.productName" readonly="readonly" value=""/>' + '</td>');
						}
						break;
					case 'procedureName':
						_TR.append('<td><input class="tab_input" type="text" name="detailList.procedureName" readonly="readonly" value="' + (_THIS.procedureName || '') + '"/></td>');
						break;
					case 'processRequire':
						_TR.append('<td><input class="tab_input" type="text" name="detailList.processRequire" readonly="readonly" value="' + (_THIS.processRequire || '') + '"/></td>');
						break;
					case 'qty':
						_TR.append('<td><input class="tab_input bg_color" type="text" name="detailList.qty" value="' + (_THIS.qty - _THIS.returnQty - _THIS.reconcilQty || 0) + '"/>'

						+ '<input type="hidden" name="detailList.saveQty" value="' + (_THIS.qty - _THIS.returnQty - _THIS.reconcilQty || 0) + '"/></td>');
						break;
					case 'price':
						_TR.append('<td><input class="tab_input" type="text" readonly="readonly" name="detailList.price" value="' + Number(_THIS.money - _THIS.returnMoney - _THIS.reconcilMoney).div(Number(_THIS.qty - _THIS.returnQty - _THIS.reconcilQty)).toFixed(4) + '"/></td>');
						break;
					case 'money':
						_TR.append('<td><input class="tab_input" type="text" readonly="readonly" name="detailList.money" value="' + (_THIS.money - _THIS.returnMoney - _THIS.reconcilMoney || 0) + '"/>' + '<input type="hidden" name="detailList.saveMoney" value="' + (_THIS.money - _THIS.returnMoney - _THIS.reconcilMoney || 0) + '"/></td>');
						break;
					case 'taxRate':
						_TR.append('<td><input name="detailList.taxRateName" readonly="readonly" class="tab_input" type="text" value="' + Helper.basic.info('TAXRATE', _THIS.taxRateId).name + '"/>' + '<input name="detailList.taxRateId" type="hidden" value="' + _THIS.taxRateId + '">');
						break;
					case 'taxRatePercent':
						_TR.append('<td style="display: none;"><input type="text" class="tab_input" name="detailList.taxRatePercent" readonly="readonly" value="' + _THIS.taxRatePercent + '"/></td>');
						break;
					case 'noTaxPrice':
						_TR.append('<td style="display: none;"><input class="tab_input" type="text" name="detailList.noTaxPrice" readonly="readonly" value="' + _THIS.noTaxPrice + '"/></td>');
						break;
					case 'noTaxMoney':
						_TR.append('<td style="display: none;"><input class="tab_input" type="text" name="detailList.noTaxMoney" readonly="readonly" value="' + _THIS.noTaxMoney + '"/></td>');
						break;
					case 'tax':
						_TR.append('<td><input class="tab_input" type="text" name="detailList.tax" readonly="readonly" value="' + _THIS.tax + '"/></td>');
						break;
					case 'sourceBillType':
						_TR.append('<td><input class="tab_input" type="text" readonly="readonly" value="' + Helper.basic.getEnumText('com.huayin.printmanager.persist.enumerate.BillType', _THIS.sourceBillType, 'text') + '"/></td>');
						break;
					case 'type':
						_TR.append('<td><input class="tab_input" type="text" readonly="readonly" value="' + Helper.basic.getEnumText('com.huayin.printmanager.persist.enumerate.OutSourceType', _THIS.type, 'text') + '"/></td>');
						break;
					case 'sourceBillNo':
						_TR.append('<td><input class="tab_input" type="text" name="detailList.sourceBillNo" readonly="readonly" value="' + (_THIS.sourceBillNo || '') + '"/></td>');
						break;
					case 'sourceQty':
						_TR.append('<td><input class="tab_input" type="text" name="detailList.sourceQty" readonly="readonly" value="' + _THIS.sourceQty + '"/></td>');
						break;
					case 'workBillNo':
						_TR.append('<td><input class="tab_input" type="text" name="detailList.workBillNo" readonly="readonly" value="' + (_THIS.workBillNo || '') + '"/>' + '<input name="detailList.workId" type="hidden" value="' + _THIS.workId + '"></td>');
						break;
					case 'produceNum':
						_TR.append('<td><input class="tab_input" type="text" name="detailList.produceNum" readonly="readonly" value="' + (_THIS.produceNum || '') + '"/></td>');
						break;
					case 'style':
						_TR.append('<td><input class="tab_input" type="text" name="detailList.style" readonly="readonly" value="' + (_THIS.style || '') + '"/></td>');
						break;
					case 'partName':
						_TR.append('<td><input class="tab_input" type="text" name="detailList.partName" readonly="readonly" value="' + (_THIS.partName || '') + '"/></td>');
						break;
					case 'memo':
						_TR.append('<td><input class="tab_input bg_color memo" onmouseover="this.title=this.value" type="text" name="detailList.memo" value="' + (_THIS.memo || '') + '"/></td>' + '<input name="detailList.procedureId" type="hidden" value="' + _THIS.procedureId + '">' + '<input name="detailList.procedureCode" type="hidden" value="' + (_THIS.procedureCode || "") + '">' + '<input name="detailList.procedureType" type="hidden" value="' + (_THIS.procedureType || "") + '">' + '<input name="detailList.sourceBillType" type="hidden" value="' + _THIS.master.billType + '"/>' + '<input name="detailList.sourceId" type="hidden" value="' + _THIS.master.id + '"/>'
								+ '<input name="detailList.sourceDetailId" type="hidden" value="' + _THIS.id + '"/>' + '<input name="detailList.outSourceBillNo" type="hidden" value="' + _THIS.outSourceBillNo + '"/>' + '<input name="detailList.productType" type="hidden" value="' + (_THIS.productType || "") + '"/>' + '<input name="detailList.workProcedureType" type="hidden" value="' + (_THIS.workProcedureType || "") + '"/>' + '<input name="detailList.productId" type="hidden" value="' + _THIS.productId + '"/>' + '<input name="detailList.type" type="hidden" value="' + _THIS.type + '"/>' + '</td></tr>');
						break;
					}
				});
		_TR.appendTo("#detailList");
	});
	resetSequenceNum();
	selectWarehouse();
	sum();
	trigger();
}
// 金额重新计算
function trigger()
{
	for (var i = 0; i < $("input[name='detailList.qty']").length; i++)
	{
		$("input[name='detailList.qty']").eq(i).trigger("blur");

	}
}

/**
 * 强制审核
 */
function forceCheck(id)
{
	$.ajax({
		cache : true,
		type : "POST",
		dataType : "json",
		url : Helper.basePath + '/outsource/return/forceCheck/' + id,
		async : false,
		error : function(request)
		{
			layer.alert("Connection error");
		},
		success : function(data)
		{
			if (data.success)
			{
				location.href = Helper.basePath + '/outsource/return/view/' + id;
			} else
			{
				layer.alert("审核失败")
			}
		}
	});
}
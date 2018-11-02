var hasPermission = Helper.basic.hasPermission('purch:order:money');
$(function()
{
	if (!hasPermission)
	{
		$("td").has("input[name='detailList.valuationPrice']").hide();
		$("td").has("input[name='detailList.money']").hide();
		$("td").has("input[name='detailList.tax']").hide();
		$("dd").has("input[name='totalMoney']").hide();
		$("dd").has("input[name='noTaxTotalMoney']").hide();
		$("dd").has("input[name='totalTax']").hide();
	}
	$("#btn_purch_save,#btn_purch_save_audit").click(function()
	{
		fixEmptyValue();// 处理自定义未输入数值
		var this_ = $(this);
		var validate = true;
		if ($("#supplierName").val() == "")
		{
			Helper.message.warn("请录入供应商");
			validate = false;
			return;
		}
		if ($("input[name='detailList.qty']").size() <= 0)
		{
			Helper.message.warn("请录入明细");
			validate = false;
			return;
		}
		$("input[name='detailList.qty']").each(function()
		{
			if (Helper.isEmpty($(this).val()) || Number($(this).val()) == 0)
			{
				Helper.message.warn("数量必须大于0");
				validate = false;
				return;
			}
		});
		/*
		 * $("table input[name='detailList.money']").each(function() { if
		 * (Helper.isEmpty($(this).val()) || Number($(this).val()) == 0) {
		 * Helper.message.warn("金额不能为0"); validate = false; return; } });
		 */

		$("table input[name='detailList.valuationPrice']").each(function()
		{
			if (Helper.isEmpty($(this).val()))
			{
				Helper.message.warn("单价不能为空");
				validate = false;
				return;
			}
		});
		$("table select[name='detailList.taxRateId']").each(function()
		{
			if (Number($(this).val()) <= 0)
			{
				Helper.message.warn("请选择税收");
				validate = false;
				return false;
			}
		});
		if (validate == true)
		{
			var isSave = false;// 是否弹出空规格提示
			var isEqual = false;// 是否有不同的单位
			$("input[name='detailList.specifications']").each(function()
			{
				if (Helper.isEmpty($(this).val()))
				{
					var tr_dom = $(this).parent().parent();
					var valuationUnitName = tr_dom.find("input[name='detailList.valuationUnitName']").val();
					var purchUnitName = tr_dom.find("input[name='detailList.purchUnitName']").val();
					if (valuationUnitName != purchUnitName)
					{
						isEqual = true;
					}
					isSave = true;
				}
			});
			if (isSave)
			{
				Helper.message.confirm('存在空的材料规格，是否保存', function(index)
				{
					if (isEqual == true)
					{
						Helper.message.warn("有需要单位换算的材料，请输入规格进行换算");
						return;
					} else
					{
						$("#isCheck").val(this_.attr("id") == "btn_purch_save_audit" ? "YES" : "NO");
						save();
					}
				})
			} else
			{
				$("#isCheck").val(this_.attr("id") == "btn_purch_save_audit" ? "YES" : "NO");
				save();
			}

		}

	});

	$("#btn_cancel").click(function()
	{
		location.href = Helper.basePath + '/purch/order/view/' + $("#id").val();
	});
	$("#purchTime").change(function()
	{

	});

	/* 选择供应商 */
	$("#selectSupplier").click(function()
	{
		Helper.popup.show('选择供应商', Helper.basePath + '/quick/supplier_select?multiple=false&supplierType=MATERIAL', '900', '490');
	});

	// 选择材料
	$("#material_quick_select").click(function()
	{
		Helper.popup.show('选择材料', Helper.basePath + '/quick/material_select?multiple=true', '900', '490');
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
	})

})
// 保存
function save()
{
	Helper.request({
		url : Helper.basePath + "/purch/order/update",
		data : $("#form_order").formToJson(),
		error : function(request)
		{
			Helper.message.warn("Connection error");
		},
		success : function(data)
		{
			if (data.success)
			{
				window.location.href = Helper.basePath + '/purch/order/view/' + data.obj.id;
			} else
			{
				Helper.message.warn(data.message);
			}
		}
	});

}
function add()
{
	window.location.href = Helper.basePath + '/purch/order/create';
}

// 获取返回信息
function getCallInfo_supplier(obj)
{
	$("#supplierId").val(obj.id);
	$("#supplierName").val(obj.name);
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
	if (Helper.isNotEmpty(obj.employeeId))
	{
		$("#employeeId").val(obj.employeeId);
		$("#employeeName").val(Helper.basic.info('EMPLOYEE', obj.employeeId).name);
	} else
	{
		$("#employeeName").val("");
	}

	$("#taxRateId").val(obj.taxRateId).trigger("change");
}

function getCallInfo_materialArray(rows)
{
	var deliveryTime = $("#purchTime").val();
	var taxRateId = $("#taxRateId").val();
	var percent = taxRateId > 0 ? Helper.basic.info('TAXRATE', taxRateId).percent : 0;
	var rowNum = $("input[name='detailList.code']").length;
	$.each(rows, function()
	{
		var _THIS = this;
		var _TR = $("<tr/>");

		$("#detailList").find("thead tr th").each(function()
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
					_TR.append('<td style="display:none;"><input name="detailList.tax" type="hidden" value="0" />' + '<input name="detailList.valuationPrice" type="hidden" value="' + (_THIS.lastPurchPrice || '0') + '" />' + '<input name="detailList.money" readonly="readonly" class="tab_input" type="text" value="0" /></td>');
				}
				break;
			case 'operator':
				_TR.append('<td class="td-manage"><input name="detailList.materialId" type="hidden" readonly="readonly" value="' + _THIS.id + '"/><i title="删除行" class="fa fa-trash-o row_delete"></i></td>');
				break;
			case 'code':
				_TR.append('<td><input readonly="readonly" name="detailList.code" class="tab_input" type="text" value="' + value + '"/><input name="detailList.materialClassId" type="text" value="' + _THIS.materialClassId + '"/></td>');
				break;
			case 'materialName':
				_TR.append('<td><input readonly="readonly" name="detailList.materialName" class="tab_input" type="text" value="' + _THIS.name + '"/></td>');
				break;
			case 'specifications':
				_TR.append('<td><input name="detailList.specifications"   class="bg_color tab_input" type="text" />');
				break;
			case 'weight':
				_TR.append('<td><input readonly="readonly" name="detailList.weight" class="tab_input" type="text" value="' + _THIS.weight + '" />');
				break;
			case 'purchUnitName':
				_TR.append('<td><input name="detailList.purchUnitName" readonly="readonly" class="tab_input" type="text" value="' + Helper.basic.info('UNIT', _THIS.stockUnitId).name + '"/><input name="detailList.unitId" type="hidden" value="' + _THIS.stockUnitId + '"/><input name="stockUnitAccuracy" type="hidden" value="' + Helper.basic.info('UNIT', _THIS.stockUnitId).accuracy + '"/></td>');
				break;
			case 'qty':
				_TR.append('<td><input name="detailList.qty" class="bg_color tab_input constraint_decimal" type="text" /></td>');
				break;
			case 'valuationUnitName':
				_TR.append('<td><input name="detailList.valuationUnitName" readonly="readonly" class="tab_input" type="text" value="' + Helper.basic.info('UNIT', _THIS.valuationUnitId).name + '"/><input name="detailList.valuationUnitId" type="hidden" value="' + _THIS.valuationUnitId + '"/><input name="valuationUnitAccuracy" type="hidden" value="' + Helper.basic.info('UNIT', _THIS.valuationUnitId).accuracy + '"/></td>');
				break;
			case 'valuationQty':
				_TR.append('<td><input name="detailList.valuationQty" readonly="readonly" class="tab_input constraint_decimal" type="text" /></td>');
				break;
			case 'valuationPrice':
				if (_THIS.lastPurchPrice != null)
				{
					_TR.append('<td><input name="detailList.valuationPrice" class="bg_color tab_input constraint_decimal" type="text" value="' + _THIS.lastPurchPrice + '" /></td>');
				} else
				{
					_TR.append('<td><input name="detailList.valuationPrice" class="bg_color tab_input constraint_decimal" type="text" value="0"/></td>');
				}
				break;
			case 'money':
				_TR.append('<td><input name="detailList.money" readonly="readonly" class="tab_input" type="text" value="0" /></td>');
				break;
			case 'taxRateId':
				_TR.append('<td>' + $("#taxRateId").clone(true).html() + '</td>');
				break;
			case 'percent':
				_TR.append('<td style="display: none"><input name="detailList.percent" readonly="readonly" class="tab_input" type="text" value="' + percent + '"/></td>');
				break;
			case 'tax':
				_TR.append('<td><input name="detailList.tax" readonly="readonly" class="tab_input" type="text" /></td>');
				break;
			case 'price':
				_TR.append('<td style="display: none"><input name="detailList.price" readonly="readonly" class="tab_input" type="text" value="0" /></td>');
				break;
			case 'deliveryTime':
				_TR.append('<td><input name="detailList.deliveryTime" onFocus="WdatePicker({lang:\'zh-cn\',minDate: \'%y-%M-%d\' })" readonly="readonly" class="tab_input" type="text" value="' + deliveryTime + '"/></td>');
				break;
			case 'sourceBillType':
				_TR.append('<td><input name="" readonly="readonly" class="tab_input" type="text" /></td>');
				break;
			case 'sourceBillNo':
				_TR.append('<td><input name="detailList.sourceBillNo" readonly="readonly" class="tab_input" type="text"/></td>');
				break;
			case 'sourceQty':
				_TR.append('<td style="display: none"><input name="detailList.sourceQty" readonly="readonly" class="tab_input" type="text"/></td>');
				break;
			case 'storageQty':
				_TR.append('<td style="display: none"><input name="detailList.storageQty" readonly="readonly" class="tab_input" type="text" /></td>');
				break;
			case 'noTaxMoney':
				_TR.append('<td style="display: none"><input name="detailList.noTaxMoney" readonly="readonly" class="tab_input" type="text" /></td>');
				break;
			case 'noTaxPrice':
				_TR.append('<td style="display: none"><input name="detailList.noTaxPrice" readonly="readonly" class="tab_input" type="text" /><input type="hidden" name="detailList.noTaxValuationPrice"/></td>');
				break;
			case 'productNames':
				_TR.append('<td><input name="detailList.productNames" readonly="readonly" class="tab_input" type="text" /></td>');
				break;
			case 'memo':
				_TR.append('<td><input name="detailList.memo" class="bg_color tab_input memo" type="text" />' + '</td></tr>');
				break;
			}
		});
		_TR.appendTo("#detailList");
	});
	resetSequenceNum();
	var newRowNum = $("input[name='detailList.code']").length;
	for (var i = rowNum; i < newRowNum; i++)
	{
		$("select[name='detailList.taxRateId']").eq(i).val(taxRateId);
	}
}
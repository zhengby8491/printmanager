var hasPermission = Helper.basic.hasPermission('purch:refund:money');
$(function()
{
	if (!hasPermission)
	{
		$("#detailList td").has("input[name='detailList.money']").hide();
		$("#detailList td").has("input[name='detailList.valuationPrice']").hide();
		$("#detailList td").has("input[name='detailList.tax']").hide();
		$("dd").has("input[name='totalMoney']").hide();
		$("dd").has("input[name='noTaxTotalMoney']").hide();
		$("dd").has("input[name='totalTax']").hide();
	}
	$("#btn_cancel").click(function()
	{
		location.href = Helper.basePath + '/purch/refund/view/' + $("#id").val();
	});
	$("#stock_quick_select").click(function()
	{
		if ($(this).val() == "")
		{
			Helper.message.warn("请选择供应商");
			return;
		}
		Helper.popup.show('来源采购入库', Helper.basePath + '/purch/refund/quick_select?supplierId=' + $(this).val(), '800', '490');
	});

	$("table").on("click", ".row_delete", function()
	{
		$(this).parent().parent().remove();
		countMoney();
		resetSequenceNum();
	});

	$("input[name='detailList.qty']").change(function()
	{
		var tr_dom = $(this).parent().parent();
		var sourceQty = tr_dom.find("input[name='detailList.sourceQty']").val();
		if (Number($(this).val()) > Number(sourceQty))
		{
			Helper.message.warn("输入数量大于可退货数量");
			$(this).val(sourceQty);
			$(this).trigger("input");
		}
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
	$("#forceCheck").val("NO");

	var paramObj = $("#form_order").formToJson();
	Helper.request({
		url : Helper.basePath + "/purch/refund/update",
		data : paramObj,
		error : function(request)
		{
			layer.alert("Connection error");
		},
		success : function(data)
		{
			if (data.isSuccess)
			{
				Helper.message.suc('已保存!');
				if (data.returnObject != null && data.returnObject.length > 0)
				{
					var str = '审核失败,以下材料库存数量不足\n';
					for (var i = 0; i < data.returnObject.length; i++)
					{
						str = str + '名称:' + data.returnObject[i].material.name + (data.returnObject[i].specifications || '') + '   目前库存数量' + data.returnObject[i].qty + '\n';
					}
					Helper.message.confirm(str, function(index)
					{
						Helper.message.confirm("确认操作会引起负库存，是否允许负库存?", function(index)
						{
							forceCheck(data.returnValue.id);
						}, function(index)
						{
							window.location.href = Helper.basePath + '/purch/refund/view/' + data.returnValue.id;
						});
					}, function(index)
					{
						window.location.href = Helper.basePath + '/purch/refund/view/' + data.returnValue.id;
					});
					$("#btn_save").removeAttr("disabled");
					$("#btn_save_audit").removeAttr("disabled");

				} else
				{
					window.location.href = Helper.basePath + '/purch/refund/view/' + data.returnValue.id;
				}

			} else
			{
				Helper.message.warn(data.message);
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

function selectWarehouse()
{
	// 纠正仓库名称
	$("table select[name='detailList.warehouseId']").each(function()
	{
		$(this).val($(this).parent().parent().find("input[name='detailList.warehouse']").val());
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

function getCallInfo_refund(rows)
{
	$.each(rows, function()
	{
		var _THIS = this;
		var _TR = $("<tr/>");
		var sourceDetailIdArray = $("table tbody tr td input[name='detailList.sourceDetailId']").map(function()
		{
			return this.value
		}).get();
		if (Helper.isNotEmpty(sourceDetailIdArray) && sourceDetailIdArray.contains("" + this.id))
		{// 如果已存在则跳过本次循环
			return true;// continue;
		}
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
					_TR.append('<td style="display:none;">' + '<input name="detailList.valuationPrice" class="bg_color tab_input constraint_decimal" type="text" value="' + _THIS.valuationPrice + '"/>' + '<input name="detailList.money" readonly="readonly" class="tab_input" type="text" value="' + _THIS.money + '"/>' + '<input name="detailList.tax" readonly="readonly" class="tab_input" type="text" value="' + _THIS.tax + '"/></td>');
				}
				break;
			case 'operator':
				_TR.append('<td class="td-manage"><input name="detailList.materialId" type="hidden" readonly="readonly" value="' + _THIS.materialId + '"/><i title="删除行" class="fa fa-trash-o row_delete"></i></td>');
				break;
			case 'code':
				_TR.append('<td><input readonly="readonly" name="detailList.code" class="tab_input" type="text" value="' + value + '"/><input name="detailList.materialClassId" type="text" value="' + _THIS.materialClassId + '"/></td>');
				break;
			case 'materialName':
				_TR.append('<td><input readonly="readonly" name="detailList.materialName" class="tab_input" type="text" value="' + _THIS.materialName + '"/></td>');
				break;
			case 'specifications':
				_TR.append('<td><input readonly="readonly" name="detailList.specifications" value="' + (_THIS.specifications || "") + '"  class="tab_input" type="text" />');
				break;
			case 'weight':
				_TR.append('<td><input readonly="readonly" name="detailList.weight" class="tab_input" type="text" value="' + _THIS.weight + '" />');
				break;
			case 'purchUnitName':
				_TR.append('<td><input name="detailList.purchUnitName" readonly="readonly" class="tab_input" type="text" value="' + Helper.basic.info('UNIT', _THIS.unitId).name + '"/><input name="detailList.unitId" type="hidden" value="' + _THIS.unitId + '"/><input name="stockUnitAccuracy" type="hidden" value="' + Helper.basic.info('UNIT', _THIS.unitId).accuracy + '"/></td>');
				break;
			case 'qty':
				_TR.append('<td><input name="detailList.qty" class="tab_input constraint_decimal bg_color" type="text" value="' + _THIS.qty + '"/></td>');
				break;
			case 'reconcilQty':
				_TR.append('<td style="display: none"><input readonly="readonly" name="detailList.reconcilQty" class="tab_input" type="text"/></td>');
				break;
			case 'valuationUnitName':
				_TR.append('<td><input name="detailList.valuationUnitName" readonly="readonly" class="tab_input" type="text" value="' + Helper.basic.info('UNIT', _THIS.valuationUnitId).name + '"/><input name="detailList.valuationUnitId" type="hidden" value="' + _THIS.valuationUnitId + '"/><input name="valuationUnitAccuracy" type="hidden" value="' + Helper.basic.info('UNIT', _THIS.valuationUnitId).accuracy + '"/></td>');
				break;
			case 'valuationQty':
				_TR.append('<td><input name="detailList.valuationQty" readonly="readonly" class="tab_input" type="text" value="' + _THIS.valuationQty + '"/></td>');
				break;
			case 'valuationPrice':
				_TR.append('<td><input name="detailList.valuationPrice" class="bg_color tab_input constraint_decimal" type="text" value="' + _THIS.valuationPrice + '"/></td>');
				break;
			case 'money':
				_TR.append('<td><input name="detailList.money" readonly="readonly" class="tab_input" type="text" value="' + _THIS.money + '"/></td>');
				break;
			case 'taxRateId':
				_TR.append('<td><input name="detailList.taxRateName" readonly="readonly" class="tab_input" type="text" value="' + Helper.basic.info('TAXRATE', _THIS.taxRateId).name + '"/><input name="detailList.taxRateId" type="hidden" value="' + _THIS.taxRateId + '"></td>');
				break;
			case 'percent':
				_TR.append('<td style="display: none"><input name="detailList.percent" readonly="readonly" class="tab_input" type="text" value="' + _THIS.percent + '"/></td>');
				break;
			case 'tax':
				_TR.append('<td><input name="detailList.tax" readonly="readonly" class="tab_input" type="text" value="' + _THIS.tax + '"/></td>');
				break;
			case 'warehouseId':
				_TR.append('<td>'+$("#phtml").clone(true).html()+'</td>');
				break;
			case 'price':
				_TR.append('<td style="display: none"><input name="detailList.price" readonly="readonly" class="tab_input" type="text" value="' + _THIS.price + '"/></td>');
				break;
			case 'sourceBillType':
				_TR.append('<td><input readonly="readonly" class="tab_input" type="text" value="' + Helper.basic.getEnumText('com.huayin.printmanager.persist.enumerate.BillType', _THIS.master.billType, 'text') + '"/>' + '<input name="detailList.sourceDetailId" type="hidden" value="' + _THIS.id + '">' + '<input name="detailList.sourceId" type="hidden" value="' + _THIS.master.id + '">' + '<input name="detailList.sourceBillType" type="hidden" value="' + _THIS.master.billType + '">' + '</td>');
				break;
			case 'sourceBillNo':
				_TR.append('<td><input name="detailList.sourceBillNo" readonly="readonly" class="tab_input" type="text" value="' + (_THIS.master.billNo || "") + '"/></td>');
				break;
			case 'productNames':
				_TR.append('<td><input name="detailList.productNames" readonly="readonly" class="tab_input" type="text" value="' + (_THIS.productNames || "") + '"/></td>');
				break;
			case 'sourceQty':
				_TR.append('<td style="display: none"><input name="detailList.sourceQty" readonly="readonly" class="tab_input" type="text" value="' + _THIS.qty + '"/></td>');
				break;
			case 'noTaxMoney':
				_TR.append('<td style="display: none"><input name="detailList.noTaxMoney" readonly="readonly" class="tab_input" type="text" value="' + value + '"/></td>');
				break;
			case 'noTaxPrice':
				_TR.append('<td style="display: none"><input name="detailList.noTaxPrice" readonly="readonly" class="tab_input" type="text" value="' + value + '"/><input type="hidden" name="detailList.noTaxValuationPrice" value="' + _THIS.noTaxValuationPrice + '"/></td>');
				break;
			case 'orderBillNo':
				if (_THIS.workBillNo != null)
				{
					_TR.append('<td style="display: none"><input name="detailList.orderBillNo" readonly="readonly" class="tab_input" type="text" value="' + (_THIS.sourceBillNo || "") + '"/>' + '<input name="detailList.workBillNo" type="hidden" value="' + (_THIS.workBillNo || "") + '"/>' + '<input name="detailList.workId" type="hidden" value="' + (_THIS.workId || "") + '"/></td>');
				} else
				{
					_TR.append('<td style="display: none"><input name="detailList.orderBillNo" readonly="readonly" class="tab_input" type="text" value="' + (_THIS.sourceBillNo || "") + '"/>' + '<input name="detailList.workBillNo" type="hidden" value=""/></td>' + '<input name="detailList.workId" type="hidden" value=""/></td>');
				}
				break;
			case 'memo':
				_TR.append('<td><input name="detailList.memo" onmouseover="this.title=this.value" class="bg_color tab_input memo" type="text" /></td></tr>');
				break;
			}
		});
		_TR.appendTo("#detailList");
	});
	trigger();
	resetSequenceNum();
	selectWarehouse();
}
// 转单过来时金额重新计算
function trigger()
{
	for (var i = 0; i < $("input[name='detailList.qty']").length; i++)
	{
		$("input[name='detailList.qty']").eq(i).trigger("input");

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
		url : Helper.basePath + '/purch/refund/forceCheck/' + id,
		async : false,
		error : function(request)
		{
			layer.alert("Connection error");
		},
		success : function(data)
		{
			if (data.success)
			{
				location.href = Helper.basePath + '/purch/refund/view/' + id;
			} else
			{
				layer.alert("审核失败")
			}
		}
	});
}
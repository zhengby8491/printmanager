/* 判断当前用户是否有权限查看金额 */
var hasPermission = Helper.basic.hasPermission('stock:materialInventory:money');
$(function()
{
	$("#btn_cancel").click(function()
	{
		closeTabAndJump("材料库存盘点列表");
	});
	setRowIndex();
	// 保存
	$(document).on("click", "#btn_save,#btn_save_audit", function()
	{
		fixEmptyValue();
		var validate = true;
		if ($("#warehouseId").val() < 0)
		{
			Helper.message.warn("请选择仓库");
			validate = false;
			return;
		}
		if ($("table input[name='detailList.qty']").size() <= 0)
		{
			Helper.message.warn("请录入明细");
			validate = false;
			return;
		}
		/*
		 * $("table input[name='detailList.qty']").each(function() {
		 * //console.log("qty="+$(this).val()); if (Helper.isEmpty($(this).val()) ||
		 * Number($(this).val()) <= 0) { Helper.message.warn("数量必须大于0"); validate =
		 * false; return; } });
		 */
		if (validate)
		{// 保存
			$("#isCheck").val($(this).attr("id") == "btn_save_audit" ? "YES" : "NO");
			form_submit();
		}
	});

	// 初始化到货日期
	$("#inventoryTime").val(new Date().format('yyyy-MM-dd'));

	$("#material_quick_select").click(function()
	{
		Helper.popup.show('选择材料库存', Helper.basePath + '/stock/material/quick_select?warehouseId=' + $(this).val() + '&isEmptyWare=no', '1030', '490');
	});

	$("table").on("click", ".row_delete", function()
	{
		$(this).parent().parent().remove();
		setRowIndex();
	});

	$("table").on("input", "input[name='detailList.qty']", function()
	{
		countValuationQty($(this));
		countMoney($(this));
		countProfitAndLossQty($(this));
		countProfitAndLossMoney($(this));
	});

	$(document).on("change", "input[name='detailList.qty']", function()
	{
		var stockUnitAccuracy = $(this).parent().parent().find("input[name='stockUnitAccuracy']").val();
		if ($(this).val().split(".")[1] != undefined && $(this).val().split(".")[1].length > Number(stockUnitAccuracy))
		{
			Helper.message.tips("精度为" + stockUnitAccuracy + "位小数", this);
			$(this).val($(this).val().split(".")[0]);
		}
		$(this).trigger("input");
	})

	// 选盘点人设置盘点人姓名
	$(document).on("change", "#employeeId", function()
	{
		$("#employeeName").val($(this).find("option:selected").text())
	})

	$(document).on("change", "#warehouseId", function()
	{
		$("#material_quick_select").val($(this).val());
	});

})

function setRowIndex()
{
	var rowList = $("td[name='rowIndex']")
	for (var i = 0; i < rowList.length; i++)
	{
		rowList.eq(i).text(i + 1);
	}

}

function form_submit()
{
	Helper.request({
		url : Helper.basePath + "/stockmaterial/inventory/save",
		data : $("#form_inventory").formToJson(),
		success : function(data)
		{
			if (data.success)
			{
				Helper.message.suc('已保存!');
				location.href = Helper.basePath + '/stockmaterial/inventory/view/' + data.obj.id;
			} else
			{
				Helper.message.warn('保存失败!' + data.message);
			}
		},
		error : function(data)
		{
			// console.log(data);
		}
	});
}
// 算金额
function countMoney(this_)
{
	var tr_dom = this_.parent().parent();
	var qty = tr_dom.find("input[name='detailList.qty']").val();
	var valuationQty = tr_dom.find("input[name='detailList.valuationQty']").val();
	var valuationPrice = ($("input[name='detailList.valuationPrice']").is(":visible")) ? tr_dom.find("input[name='detailList.valuationPrice']").val() : 0;
	var price;
	var money;
	if (price == "")
	{
		return;
	}
	money = Number(valuationQty).mul(valuationPrice).tomoney();
	if (money != 'NaN')
		tr_dom.find("input[name='detailList.money']").val(money);
}

// 算盈亏数量
function countProfitAndLossQty(this_)
{
	var tr_dom = this_.parent().parent();
	var stockQty = tr_dom.find("input[name='detailList.stockQty']").val();
	var qty = tr_dom.find("input[name='detailList.qty']").val();
	var profitAndLossQty = qty - stockQty;
	if (tr_dom.find("input[name='detailList.stockUnitName']").val() == "令")
	{
		var stockUnitId = tr_dom.find("input[name='detailList.stockUnitId']").val();
		var accuracy = Helper.basic.info('UNIT', stockUnitId).accuracy;
		profitAndLossQty = profitAndLossQty.toFixed(accuracy);
	}
	tr_dom.find("input[name='detailList.profitAndLossQty']").val(profitAndLossQty);

	if (profitAndLossQty < 0)
	{
		tr_dom.find("input[name='detailList.inventoryTypeName']").val("盘亏");
		tr_dom.find("input[name='detailList.inventoryType']").val("LOSS");
	} else
	{
		tr_dom.find("input[name='detailList.inventoryTypeName']").val("盘盈");
		tr_dom.find("input[name='detailList.inventoryType']").val("PROFIT");
	}
}
// 算盈亏金额
function countProfitAndLossMoney(this_)
{
	var tr_dom = this_.parent().parent();
	var profitAndLossQty = tr_dom.find("input[name='detailList.profitAndLossQty']").val();
	var price = ($("input[name='detailList.price']").is(":visible")) ? tr_dom.find("input[name='detailList.price']").val() : 0;
	var profitAndLossMoney = Number(price).mul(profitAndLossQty).tomoney();
	tr_dom.find("input[name='detailList.profitAndLossMoney']").val(profitAndLossMoney);
}
// 多选获取返回信息
function getCallInfo_material_stock(content)
{
	for (var i = 0; i < content.length; i++)
	{
		var obj = content[i];
		if ($(".id_" + obj.id) != undefined)
		{
			$(".id_" + obj.id).parent().parent().remove();
		}
		var str = '<tr><td name="rowIndex">1</td>' + '<td class="td-manage">' + '<button  class="row_delete"><i class="delete fa fa-trash-o"></i></button>' + '<td><input name="detailList.code" class="tab_input" type="text" readonly="readonly" value="' + obj.material.code + '"/></td>' + '<td><input name="detailList.materialName" class="tab_input" type="text" readonly="readonly" value="' + obj.material.name + '"/></td>' + '<td><input name="detailList.specifications" class="tab_input" readonly="true" type="text" value="' + (obj.specifications || "") + '"/></td>' + '<td><input name="detailList.weight" class="tab_input" type="text" value="' + obj.material.weight + '"/></td>'
				+ '<td><input name="detailList.stockUnitName" class="tab_input" type="text" readonly="readonly" value="' + Helper.basic.info('UNIT', obj.material.stockUnitId).name + '"/></td>' + '<td><input name="detailList.qty" class="tab_input constraint_decimal bg_color" type="text" value="' + obj.qty + '"/></td>' + '<td><input name="detailList.valuationUnitName" class="tab_input" type="text" readonly="readonly" value="' + Helper.basic.info('UNIT', obj.material.valuationUnitId).name + '"/></td>' + '<td><input name="detailList.valuationQty" class="tab_input" type="text" readonly="readonly" value=""/></td>' + '<td><input name="detailList.stockQty" class="tab_input" type="text" readonly="readonly" value="' + obj.qty
				+ '"/></td>' + (hasPermission ? '<td>' : '<td style="display:none;">') + '<input name="detailList.valuationPrice" class="tab_input constraint_decimal" type="text" readonly="readonly" value="' + (obj.valuationPrice || 0) + '"/></td>' + (hasPermission ? '<td>' : '<td style="display:none;">') + '<input name="detailList.price" class="tab_input" type="text" readonly="readonly" value="' + (obj.price || 0) + '"/></td>' + (hasPermission ? '<td>' : '<td style="display:none;">') + '<input name="detailList.money" class="tab_input" type="text" readonly="readonly" value=""/></td>' + '<td><input name="detailList.profitAndLossQty" class="tab_input" type="text" readonly="readonly" value="0"/></td>'
				+ (hasPermission ? '<td>' : '<td style="display:none;">') + '<input name="detailList.profitAndLossMoney" class="tab_input" type="text" readonly="readonly" value=""/></td>' + '<td><input name="detailList.inventoryTypeName" class="tab_input" type="text" readonly="readonly" value=""/></td>' + '<td><input name="detailList.memo" class="tab_input bg_color memo" onmouseover="this.title=this.value" type="text" value=""/></td>' + '<td style="display:none"><input name="detailList.warehouseId" class="tab_input" type="hidden" value="' + obj.warehouseId + '"/></td>' + '<td style="display:none"><input name="detailList.materialId" class="tab_input" type="hidden" readonly="readonly" value="' + obj.materialId
				+ '"/></td>' + '<td style="display:none"><input name="detailList.valuationUnitId" class="tab_input" type="hidden" readonly="readonly" value="' + obj.material.valuationUnitId + '"/></td>' + '<td style="display:none"><input name="detailList.materialClassId" class="tab_input" type="hidden" readonly="readonly" value="' + obj.material.materialClassId + '"/></td>' + '<td style="display:none"><input name="detailList.stockMaterialId" class="tab_input id_' + obj.id + '" type="hidden" readonly="readonly" value="' + obj.id + '"/></td>' + '<td style="display:none"><input name="detailList.inventoryType" class="tab_input" type="hidden" readonly="readonly" value=""/></td>'
				+ '<td style="display:none"><input name="valuationUnitAccuracy" class="tab_input" type="hidden" readonly="readonly" value="' + Helper.basic.info('UNIT', obj.material.valuationUnitId).accuracy + '"/></td>' + '<td style="display:none"><input name="stockUnitAccuracy" class="tab_input" type="hidden" readonly="readonly" value="' + Helper.basic.info('UNIT', obj.material.stockUnitId).accuracy + '"/></td>' + '<td style="display:none"><input name="detailList.stockUnitId" class="tab_input" type="hidden" readonly="readonly" value="' + obj.material.stockUnitId + '"/></td></tr>';
		$(".border-table").append(str);
	}
	setRowIndex();
	trigger();
}
// 转单过来时金额重新计算
function trigger()
{
	for (var i = 0; i < $("input[name='detailList.qty']").length; i++)
	{
		$("input[name='detailList.qty']").eq(i).trigger("input");

	}
}
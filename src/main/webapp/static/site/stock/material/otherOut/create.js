/* 判断当前用户是否有权限查看金额 */
var hasPermission = Helper.basic.hasPermission('stock:materialOtherout:money');

$(function()
{
	if (!hasPermission)
	{
		$("th[name='valuationPrice']").hide();
		$("th[name='price']").hide();
		$("th[name='money']").hide();
	}
	$("#btn_cancel").click(function()
	{
		closeTabAndJump("材料其它出库列表");
	});
	setRowIndex();
	// 初始化到货日期
	$("#outTime").val(new Date().format('yyyy-MM-dd'));

	$("#material_quick_select").click(function()
	{
		Helper.popup.show('选择材料库存', Helper.basePath + '/stock/material/quick_select?warehouseId=' + $(this).val(), '1030', '490');
	});

	$("table").on("click", ".delete_row", function()
	{
		$(this).parent().parent().remove();
		setRowIndex();
	});

	$("table").on("input", "input[name='detailList.qty']", function()
	{
		countValuationQty($(this));
		countMoney($(this));
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
		$("table input[name='detailList.qty']").each(function()
		{
			// console.log("qty="+$(this).val());
			if (Helper.isEmpty($(this).val()) || Number($(this).val()) <= 0)
			{
				Helper.message.warn("数量必须大于0");
				validate = false;
				return;
			}
		});
		if (validate)
		{// 保存
			$("#isCheck").val($(this).attr("id") == "btn_save_audit" ? "YES" : "NO");
			form_submit();
		}
	});
	$(document).on("change", "#warehouseId", function()
	{
		$("#material_quick_select").val($(this).val());
	});
})
// 算金额
function countMoney(this_)
{
	var tr_dom = this_.parent().parent();
	var qty = tr_dom.find("input[name='detailList.qty']").val();
	var valuationQty = tr_dom.find("input[name='detailList.valuationQty']").val();
	var valuationPrice = tr_dom.find("input[name='detailList.valuationPrice']").val();
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
		var str = '<tr><td name="rowIndex">1</td>' + '<td class="td-manage">' + '<i class="delete_row fa fa-trash-o"></i>' + '<td><input name="detailList.code" class="tab_input id_' + obj.id + '" type="text" readonly="readonly" value="' + obj.material.code + '"/></td>' + '<td><input name="detailList.materialName" class="tab_input" type="text" readonly="readonly" value="' + obj.material.name + '"/></td>' + '<td><input name="detailList.specifications" readonly="readonly" class="tab_input" type="text" value="' + (obj.specifications || "") + '"/></td>' + '<td><input name="detailList.weight" class="tab_input" type="text" readonly="readonly" value="' + obj.material.weight + '"/></td>'
				+ '<td><input name="detailList.stockUnitName" class="tab_input" type="text" readonly="readonly" value="' + Helper.basic.info('UNIT', obj.material.stockUnitId).name + '"/></td>' + '<td><input name="detailList.qty" class="tab_input constraint_decimal bg_color" type="text" value="' + obj.qty + '"/></td>' + '<td><input name="detailList.valuationQty" class="tab_input" type="text" readonly="readonly" value=""/></td>' + '<td><input name="detailList.valuationUnitName" class="tab_input" type="text" readonly="readonly" value="' + Helper.basic.info('UNIT', obj.material.valuationUnitId).name + '"/></td>' + (hasPermission ? '<td>' : '<td style="display:none;">')
				+ '<input name="detailList.valuationPrice" class="tab_input constraint_decimal" type="text" readonly="readonly" value="' + obj.valuationPrice + '"/></td>' + (hasPermission ? '<td>' : '<td style="display:none;">') + '<input name="detailList.price" class="tab_input" type="text" readonly="readonly" value="' + obj.price + '"/></td>' + (hasPermission ? '<td>' : '<td style="display:none;">') + '<input name="detailList.money" class="tab_input" type="text" readonly="readonly" value=""/></td>' + '<td><input name="detailList.memo" class="tab_input bg_color memo" onmouseover="this.title=this.value" type="text" value=""/></td>'
				+ '<td style="display:none"><input name="detailList.materialId" class="tab_input" type="hidden" readonly="readonly" value="' + obj.materialId + '"/></td>' + '<td style="display:none"><input name="detailList.valuationUnitId" class="tab_input" type="hidden" readonly="readonly" value="' + obj.material.valuationUnitId + '"/></td>' + '<td style="display:none"><input name="detailList.materialClassId" class="tab_input" type="hidden" readonly="readonly" value="' + obj.material.materialClassId + '"/></td>' + '<td style="display:none"><input name="detailList.stockMaterialId" class="tab_input" type="hidden" readonly="readonly" value="' + obj.id + '"/></td>'
				+ '<td style="display:none"><input name="valuationUnitAccuracy" class="tab_input" type="hidden" readonly="readonly" value="' + Helper.basic.info('UNIT', obj.material.valuationUnitId).accuracy + '"/></td>' + '<td style="display:none"><input name="stockUnitAccuracy" class="tab_input" type="hidden" readonly="readonly" value="' + Helper.basic.info('UNIT', obj.material.stockUnitId).accuracy + '"/></td>' + '<td style="display:none"><input name="detailList.stockUnitId" class="tab_input" type="hidden" readonly="readonly" value="' + obj.material.stockUnitId + '"/></td></tr>';
		$("#tbody").append(str);
	}
	setRowIndex();
	trigger();
}
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
	$("#forceCheck").val("NO");
	var data = $("#form_stockMaterial").formToJson();
	// 当没有选中员工时保存时会报错
	if (data['receiveEmployeeId'] < 0)
	{
		delete data['receiveEmployeeId'];
	}
	if (data['sendEmployeeId'] < 0)
	{
		delete data['sendEmployeeId'];
	}
	Helper.request({
		url : Helper.basePath + "/stockmaterial/otherout/save",
		data : data,
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
							location.href = Helper.basePath + '/stockmaterial/otherout/view/' + data.returnValue.id;
						});
					}, function(index)
					{
						location.href = Helper.basePath + '/stockmaterial/otherout/view/' + data.returnValue.id;
					});

				} else
				{
					location.href = Helper.basePath + '/stockmaterial/otherout/view/' + data.returnValue.id;
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
			// console.log(data);
			$("#btn_save").removeAttr("disabled");
			$("#btn_save_audit").removeAttr("disabled");
		}
	});
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
		url : Helper.basePath + '/stockmaterial/otherout/forceCheck/' + id,
		async : false,
		error : function(request)
		{
			layer.alert("Connection error");
		},
		success : function(data)
		{
			if (data.success)
			{
				location.href = Helper.basePath + '/stockmaterial/otherout/view/' + id;
			} else
			{
				layer.alert("审核失败")
			}
		}
	});
}
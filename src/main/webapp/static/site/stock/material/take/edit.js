/* 判断当前用户是否有权限查看金额 */
var hasPermission = Helper.basic.hasPermission('stock:materialTake:money');
$(function()
{
	if (!hasPermission)
	{
		$("td").has("input[name='detailList.price']").hide();
		$("td").has("input[name='detailList.money']").hide();
	}

	$("#btn_cancel").click(function()
	{
		location.href = Helper.basePath + '/stockmaterial/take/view/' + $("input[name='id']").val();
	});
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

	$("#material_quick_select").click(function()
	{
		Helper.popup.show('选择材料库存', Helper.basePath + '/stock/material/quick_select?warehouseId=' + $("#warehouseId").val(), '1030', '490');
	});
	// 删除明细记录
	$("table").on("click", ".row_delete", function()
	{
		$(this).parent().parent().remove();
		setRowIndex();
	});
	$(document).on("input", "input[name='detailList.qty']", function()
	{
		var price = $(this).parent().parent().find("input[name='detailList.price']").val();
		var money = $(this).parent().parent().find("input[name='detailList.money']");
		money.val(Number($(this).val()).mul(Number(price)).tomoney());
		countValuationQty($(this));
		countValuationPrice($(this))
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
	$(document).on("input", "input[name='detailList.price']", function()
	{
		var qty = $(this).parent().parent().find("input[name='detailList.qty']").val();
		var money = $(this).parent().parent().find("input[name='detailList.money']");
		money.val(Number($(this).val()).mul(Number(qty)).tomoney());
		countValuationPrice($(this))
	});
	setValuationQty();
	setRowIndex();
	$("input[name='detailList.qty']").trigger("input");
})

// 序号重新排列
function setRowIndex()
{
	var rowList = $("td[name='rowIndex']")
	for (var i = 0; i < rowList.length; i++)
	{
		rowList.eq(i).text(i + 1);
	}
}

function setValuationQty()
{
	for (var i = 0; i < $("detailList.qty").length; i++)
	{
		countValuationQty($("detailList.qty").eq(i))
	}
}
function form_submit()
{
	$("#forceCheck").val("NO");
	var data = $("#form_adjust").formToJson();
	// 当没有选中员工时保存时会报错
	if (data['sendEmployeeId'] < 0)
	{
		delete data['sendEmployeeId'];
	}
	if (data['receiveEmployeeId'] < 0)
	{
		delete data['receiveEmployeeId'];
	}
	Helper.request({
		url : Helper.basePath + "/stockmaterial/take/update",
		data : $("#form_take").formToJson(),
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
							location.href = Helper.basePath + '/stockmaterial/take/view/' + data.returnValue.id;
						});
					}, function(index)
					{
						location.href = Helper.basePath + '/stockmaterial/take/view/' + data.returnValue.id;
					});
					$("#btn_save").removeAttr("disabled");
					$("#btn_save_audit").removeAttr("disabled");

				} else
				{
					location.href = Helper.basePath + '/stockmaterial/take/view/' + data.returnValue.id;
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
		var str = '<tr><td name="rowIndex"></td>' + '<td class="td-manage">' + '<i class="row_delete fa fa-trash-o"></i></td>' + '<td><input name="detailList.code" class="tab_input" type="text" readonly="readonly" value="' + obj.material.code + '"/></td>' + '<td><input name="detailList.materialName" class="tab_input" type="text" readonly="readonly" value="' + obj.material.name + '"/></td>' + '<td><input name="detailList.specifications" class="tab_input"  readonly="readonly" type="text" value="' + (obj.specifications || "") + '"/></td>' + '<td><input name="detailList.stockUnitName" class="tab_input" type="text" readonly="readonly" value="' + Helper.basic.info('UNIT', obj.material.stockUnitId).name + '"/></td>'
				+ '<td><input name="detailList.qty" class="tab_input constraint_decimal bg_color" type="text" value="' + obj.qty + '"/></td>' + '<td><input name="detailList.sourceQty" class="tab_input" type="text" readonly="readonly" value=""/></td>' + (hasPermission ? '<td>' : '<td style="display:none;">') + '<input name="detailList.price" class="tab_input bg_color" type="text" value="' + obj.price + '"/></td>' + (hasPermission ? '<td>' : '<td style="display:none;">') + '<input name="detailList.money" class="tab_input" readonly="readonly" type="text" value=""/></td>' + '<td><input name="" class="tab_input" type="text" readonly="readonly" value=""/></td>'
				+ '<td><input name="" class="tab_input" type="text" readonly="readonly" value=""/></td>' + '<td><input name="" class="tab_input" type="text" readonly="readonly" value=""/></td>' + '<td>' + '<input name="detailList.memo" onmouseover="this.title=this.value" class="tab_input bg_color memo" type="text" value=""/>' + '<input name="detailList.weight" type="text" readonly="hidden" value="' + obj.material.weight + '"/>' + '<input name="detailList.valuationPrice" type="text" readonly="hidden" value="' + obj.valuationPrice + '"/>' + '<input name="detailList.materialId" type="hidden"  value="' + obj.materialId + '"/>' + '<input name="detailList.valuationUnitId" type="hidden" value="'
				+ obj.material.valuationUnitId + '"/>' + '<input name="detailList.materialClassId" type="hidden" value="' + obj.material.materialClassId + '"/>' + '<input name="detailList.stockMaterialId" type="hidden" value="' + obj.id + '"/>' + '<input name="valuationUnitAccuracy" type="hidden" value="' + Helper.basic.info('UNIT', obj.material.valuationUnitId).accuracy + '"/>' + '<input name="detailList.stockUnitId" type="hidden" value="' + obj.material.stockUnitId + '"/>' + '<input name="detailList.valuationQty" type="hidden" value=""/>' + '<input name="detailList.valuationUnitName" type="hidden" value="' + Helper.basic.info('UNIT', obj.material.valuationUnitId).name + '"/>'
				+ '<input name="stockUnitAccuracy" type="hidden" value="' + Helper.basic.info('UNIT', obj.material.stockUnitId).accuracy + '"/>' + '</td></tr>';
		$("#tbody").append(str);
	}
	setRowIndex();
	$("input[name='detailList.qty']").trigger("input");
}
function countValuationPrice(this_)
{
	var tr_dom = this_.parent().parent();
	var qty = tr_dom.find("input[name='detailList.qty']").val();
	var valuationQty = tr_dom.find("input[name='detailList.valuationQty']").val();
	if (valuationQty == 0)
	{
		tr_dom.find("input[name='detailList.valuationPrice']").val(0);
		return;
	}
	var price = tr_dom.find("input[name='detailList.price']").val();
	var valuationPrice;
	var money;
	if (price == "")
	{
		return;
	}
	valuationPrice = Number(qty).mul(Number(price)).div(Number(valuationQty)).toFixed(4);
	if (valuationPrice != 'NaN')
	{
		tr_dom.find("input[name='detailList.valuationPrice']").val(valuationPrice);
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
		url : Helper.basePath + '/stockmaterial/take/forceCheck/' + id,
		async : false,
		error : function(request)
		{
			layer.alert("Connection error");
		},
		success : function(data)
		{
			if (data.success)
			{
				location.href = Helper.basePath + '/stockmaterial/take/view/' + id;
			} else
			{
				layer.alert("审核失败")
			}
		}
	});
}

function cancelReturn()
{
	history.go(-1);
}
/* 判断当前用户是否有权限查看金额 */
var hasPermission = Helper.basic.hasPermission('stock:productTransfer:money');
$(function()
{
	if (!hasPermission)
	{
		$("td").has("input[name='detailList.price']").hide();
		$("td").has("input[name='detailList.money']").hide();
	}
	$("#btn_cancel").click(function()
	{
		location.href = Helper.basePath + '/stockproduct/transfer/view/' + $("input[name='id']").val();
	});
	setRowIndex();
	// 保存
	$(document).on("click", "#btn_save,#btn_save_audit", function()
	{
		fixEmptyValue();
		var validate = true;
		var outWarehouseId = $("#outWarehouseId").val();
		var inWarehouseId = $("#inWarehouseId").val();
		if (outWarehouseId < 0 || inWarehouseId < 0)
		{
			Helper.message.warn("请选择仓库");
			validate = false;
			return;
		}
		fixEmptyValue();
		if (outWarehouseId == inWarehouseId)
		{
			Helper.message.warn("请选择不同的调拨仓库");
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

	$("#product_quick_select").click(function()
	{
		Helper.popup.show('选择产品库存', Helper.basePath + '/stock/product/quick_select?warehouseId=' + $(this).val(), '1030', '490');
	});

	$("table").on("click", ".delete_row", function()
	{
		$(this).parent().parent().remove();
		setRowIndex();
	});

	// 选调整人设置调整人姓名
	$(document).on("change", "#employeeId", function()
	{
		$("#employeeName").val($(this).find("option:selected").text())
	})

	$(document).on("change", "#outWarehouseId", function()
	{
		$("#product_quick_select").val($(this).val());
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
	$("#forceCheck").val("NO");
	var data = $("#form_transfer").formToJson();
	// 当没有选中员工时保存时会报错
	if (data['employeeId'] < 0)
	{
		delete data['employeeId'];
	}
	Helper.request({
		url : Helper.basePath + "/stockproduct/transfer/update",
		data : data,
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
							location.href = Helper.basePath + '/stockproduct/transfer/view/' + data.returnValue.id;
						});
					}, function(index)
					{
						location.href = Helper.basePath + '/stockproduct/transfer/view/' + data.returnValue.id;
					});
					$("#btn_save").removeAttr("disabled");
					$("#btn_save_audit").removeAttr("disabled");

				} else
				{
					location.href = Helper.basePath + '/stockproduct/transfer/view/' + data.returnValue.id;
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
			$("#btn_save").removeAttr("disabled");
			$("#btn_save_audit").removeAttr("disabled");
		}
	});
}

// 多选获取返回信息
function getCallInfo_product_stock(content)
{

	for (var i = 0; i < content.length; i++)
	{
		var obj = content[i];
		if ($(".id_" + obj.id) != undefined)
		{
			$(".id_" + obj.id).parent().parent().remove();
		}
		var str = '<tr><td name="rowIndex">1</td>' + '<td class="td-manage">' + '<i class="delete_row fa fa-trash-o"></i>' + '<td><input name="detailList.code" class="tab_input id_' + obj.id + '" type="text" readonly="readonly" value="' + obj.product.code + '"/></td>' + '<td><input name="detailList.productName" class="tab_input" type="text" readonly="readonly" value="' + obj.product.name + '"/></td>' + '<td><input name="detailList.specifications" class="tab_input" readonly="readonly" type="text" value="' + (obj.product.specifications || "") + '"/></td>' + '<td><input name="detailList.unitName" class="tab_input" type="text" readonly="readonly" value="' + Helper.basic.info('UNIT', obj.product.unitId).name
				+ '"/></td>' + '<td><input name="detailList.qty" class="tab_input constraint_negative bg_color" type="text" value="' + obj.qty + '"/></td>' + (hasPermission ? '<td>' : '<td style="display:none;">') + '<input name="detailList.price" class="tab_input" type="text" readonly="readonly" value="' + obj.price + '"/></td>' + (hasPermission ? '<td>' : '<td style="display:none;">') + '<input name="detailList.money" class="tab_input" type="text" readonly="readonly" value=""/></td>' + '<td><input name="detailList.memo" class="tab_input bg_color memo" onmouseover="this.title=this.value" type="text" value=""/></td>' + '<td>' + (obj.product.imgUrl == '' ? '' : '<img class="pimg" src="' + obj.product.imgUrl + '"/>')
				+ '</td>' + '<td style="display:none"><input name="detailList.stockQty" class="tab_input" type="hidden" value="' + obj.qty + '"/></td>' + '<td style="display:none"><input name="detailList.productId" class="tab_input" type="hidden" readonly="readonly" value="' + obj.productId + '"/></td>' + '<td style="display:none"><input name="detailList.stockProductId" class="tab_input" type="hidden" readonly="readonly" value="' + obj.id + '"/></td>' + '<td style="display:none"><input name="detailList.productClassId" class="tab_input" type="hidden" readonly="readonly" value="' + obj.product.productClassId + '"/></td>'
				+ '<td style="display:none"><input name="detailList.unitId" class="tab_input" type="hidden" readonly="readonly" value="' + obj.product.unitId + '"/></td></tr>';
		$(".border-table").append(str);
	}
	setRowIndex();
	trigger();
}
function cancelReturn()
{
	history.go(-1);
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
		url : Helper.basePath + '/stockproduct/transfer/forceCheck/' + id,
		async : false,
		error : function(request)
		{
			layer.alert("Connection error");
		},
		success : function(data)
		{
			if (data.success)
			{
				location.href = Helper.basePath + '/stockproduct/transfer/view/' + id;
			} else
			{
				layer.alert("审核失败")
			}
		}
	});
}
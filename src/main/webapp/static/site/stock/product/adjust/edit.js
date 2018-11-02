/* 判断当前用户是否有权限查看金额 */
var hasPermission = Helper.basic.hasPermission('stock:productAdjust:money');
$(function()
{
	if (!hasPermission)
	{
		$("td").has("input[name='detailList.price']").hide();
		$("td").has("input[name='detailList.money']").hide();
	}
	$("#btn_cancel").click(function()
	{
		location.href = Helper.basePath + '/stockproduct/adjust/view/' + $("input[name='id']").val();
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
		 * $("table input[name='detailList.qty']").each(function() { if
		 * (Helper.isEmpty($(this).val()) || Number($(this).val()) <= 0) {
		 * Helper.message.warn("数量必须大于0"); validate = false; return; } });
		 */
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

	$(document).on("change", "#warehouseId", function()
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
	var data = $("#form_adjust").formToJson();
	// 当没有选中员工时保存时会报错
	if (data['employeeId'] < 0)
	{
		delete data['employeeId'];
	}
	Helper.request({
		url : Helper.basePath + "/stockproduct/adjust/update",
		data : data,
		success : function(data)
		{
			if (data.success)
			{
				Helper.message.suc('已保存!');
				location.href = Helper.basePath + '/stockproduct/adjust/view/' + data.obj.id;
			} else
			{
				Helper.message.warn(data.message);
			}
		},
		error : function(data)
		{
			// console.log(data);
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
		var str = '<tr><td name="rowIndex">1</td>' + '<td class="td-manage">' + '<i class="delete_row fa fa-trash-o"></i>' + '<td><input name="detailList.code" class="tab_input id_' + obj.id + '" type="text" readonly="readonly" value="' + obj.product.code + '"/></td>' + '<td><input name="detailList.productName" class="tab_input" type="text" readonly="readonly" value="' + obj.product.name + '"/></td>' + '<td><input name="detailList.specifications" readonly="readonly" class="tab_input" type="text" value="' + (obj.product.specifications || "") + '"/></td>' + '<td><input name="detailList.unitName" class="tab_input" type="text" readonly="readonly" value="' + Helper.basic.info('UNIT', obj.product.unitId).name
				+ '"/></td>' + '<td><input name="detailList.qty" class="tab_input constraint_negative bg_color" type="text" value="' + obj.qty + '"/></td>' + (hasPermission ? '<td>' : '<td style="display:none;">') + '<input name="detailList.price" class="tab_input" type="text" readonly="readonly" value="' + obj.price + '"/></td>' + (hasPermission ? '<td>' : '<td style="display:none;">') + '<input name="detailList.money" class="tab_input" type="text" readonly="readonly" value=""/></td>' + '<td><input name="detailList.memo" onmouseover="this.title=this.value" class="tab_input bg_color memo" type="text" value=""/></td>'
				+ '<td style="display:none"><input name="detailList.stockProductlId" class="tab_input" type="hidden" value="' + obj.id + '"/></td>' + '<td style="display:none"><input name="detailList.productId" class="tab_input" type="hidden" readonly="readonly" value="' + obj.productId + '"/></td>' + '<td style="display:none"><input name="detailList.productClassId" class="tab_input" type="hidden" readonly="readonly" value="' + obj.product.productClassId + '"/></td>' + '<td style="display:none"><input name="detailList.unitId" class="tab_input" type="hidden" readonly="readonly" value="' + obj.product.unitId + '"/></td>' + '<td>' + (obj.product.imgUrl == '' ? '' : '<img class="pimg"  src="' + obj.product.imgUrl + '"/>')
				+ '</td></tr>';
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
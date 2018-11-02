/* 判断当前用户是否有权限查看金额 */
var hasPermission = Helper.basic.hasPermission('stock:productOtherin:money');
$(function()
{
	if (!hasPermission)
	{
		$("td").has("input[name='detailList.price']").hide();
		$("td").has("input[name='detailList.money']").hide();
	}
	$("#btn_cancel").click(function()
	{
		location.href = Helper.basePath + '/stockproduct/otherin/view/' + $("input[name='id']").val();
	});

	$(".product_quick_select").click(function()
	{
		Helper.popup.show('选择产品', Helper.basePath + '/quick/product_select?multiple=true', '900', '490');
	});

	// $("#detailList").bootstrapTable('append', {code:1});
	$("table").on("click", ".delete_row", function()
	{
		$(this).parent().parent().remove();
		setRowIndex();
	});
	$("table").on("click", "input[name='btn_update']", function()
	{
		var index = $(this).parent().parent().index();
		Helper.popup.show('选择产品', Helper.basePath + '/quick/product_select?multiple=false&param_index=' + index, '900', '490');

	});
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

})

// 获取返回信息
function getCallInfo_productArray(rows)
{

	$.each(rows, function()
	{

		var obj = this;
		if ($(".id_" + obj.id) != undefined)
		{
			$(".id_" + obj.id).parent().parent().remove();
		}
		var str = '<tr><td name="rowIndex">1</td>' + '<td class="td-manage">' + '<i class="delete_row fa fa-trash-o"></i></td>' + '<td><input name="detailList.code" class="tab_input id_' + obj.id + '" type="text" readonly="readonly" value="' + obj.code + '"/></td>' + '<td><input name="detailList.productName" class="tab_input" type="text" readonly="readonly" value="' + obj.name + '"/></td>' + '<td><input name="detailList.specifications" class="tab_input" readonly="readonly" type="text" value="' + (obj.specifications || "") + '"/></td>' + '<td><input name="detailList.unitName" class="tab_input" type="text" readonly="readonly" value="' + Helper.basic.info('UNIT', obj.unitId).name + '"/></td>'
				+ '<td><input name="detailList.qty" class="tab_input constraint_negative bg_color" type="text" value=""/></td>' + (hasPermission ? '<td>' : '<td style="display:none;">') + '<input name="detailList.price" class="tab_input bg_color" type="text" value="' + (obj.salePrice || "0") + '"/></td>' + (hasPermission ? '<td>' : '<td style="display:none;">') + '<input name="detailList.money" class="tab_input" type="text" readonly="readonly" value=""/></td>' + '<td><input name="detailList.memo" class="tab_input bg_color memo" onmouseover="this.title=this.value" type="text" value=""/></td>' + '<td>' + (obj.imgUrl == '' ? '' : '<img class="pimg" src="' + obj.imgUrl + '"/>') + '</td>'
				+ '<td style="display:none"><input name="detailList.productId" class="tab_input" type="hidden" readonly="readonly" value="' + obj.id + '"/></td>' + '<td style="display:none"><input name="detailList.productClassId" class="tab_input" type="hidden" readonly="readonly" value="' + obj.productClassId + '"/></td>' + '<td style="display:none"><input name="detailList.unitId" class="tab_input" type="hidden" readonly="readonly" value="' + obj.unitId + '"/></td></tr>';
		$("#tbody").append(str);
	})
	setRowIndex();

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
	var data = $("#form_stockProduct").formToJson();
	// 当没有选中员工时保存时会报错
	if (data['employeeId'] < 0)
	{
		delete data['employeeId'];
	}
	Helper.request({
		url : Helper.basePath + "/stockproduct/otherin/update",
		data : data,
		success : function(data)
		{
			if (data.success)
			{
				Helper.message.suc('已保存!');
				location.href = Helper.basePath + '/stockproduct/otherin/view/' + data.obj.id;
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
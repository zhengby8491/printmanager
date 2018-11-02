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
		location.href = Helper.basePath + '/stockproduct/in/view/' + $("input[name='id']").val();
	});
	setRowIndex()

	$(".product_quick_select").click(function()
	{
		Helper.popup.show('选择产品', Helper.basePath + '/quick/product_select?multiple=true', '800', '490');
	});
	$(document).on("change", "#employeeId", function()
	{
		$("#employeeName").val($(this).find("option:selected").text())
	})

	// $("#detailList").bootstrapTable('append', {code:1});
	// 删除一行数据
	$("#tbody").on("click", ".row_delete", function()
	{
		$(this).parent().parent().remove();
		setRowIndex();
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
		url : Helper.basePath + "/stockproduct/in/update",
		data : data,
		success : function(data)
		{
			if (data.success)
			{
				Helper.message.suc('已保存!');
				location.href = Helper.basePath + '/stockproduct/in/view/' + data.obj.id;
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
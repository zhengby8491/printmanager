$(function()
{
	$("#btn_cancel").click(function()
	{
		location.href = Helper.basePath + '/purch/stock/view/' + $("#id").val();
	});
	$("input[name='detailList.qty']").change(function()
	{
		var tr_dom = $(this).parent().parent();
		var sourceQty = tr_dom.find("input[name='detailList.sourceQty']").val();
		if (Number($(this).val()) > Number(sourceQty))
		{
			Helper.message.warn("输入数量大于源单数量")
			$(this).trigger("input");
		}
	});
	$(document).on("input", "input[name='detailList.freeQty']", function()
	{
		countValuationQty2($(this));
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
// 复制
function copy(id)
{
	window.location.href = Helper.basePath + '/purch/order/copy/' + id;
}
// 保存
function save()
{
	var paramObj = $("#form_order").formToJson();
	Helper.request({
		url : Helper.basePath + "/purch/stock/update",
		data : paramObj,
		error : function(request)
		{
			Helper.message.warn("Connection error");
		},
		success : function(data)
		{
			if (data.success)
			{
				window.location.href = Helper.basePath + '/purch/stock/view/' + data.obj.id;
			} else
			{
				Helper.message.warn(data.message);
			}
		}
	});
}
var hasPermission = Helper.basic.hasPermission('purch:stock:money');

$(function()
{
	if (!hasPermission)
	{
		$("td").has("input[name='detailList.valuationPrice']").hide();
		$("td").has("input[name='detailList.tax']").hide();
		$("td").has("input[name='detailList.money']").hide();
		$("dd").has("input[name='totalMoney']").hide();
		$("dd").has("input[name='noTaxTotalMoney']").hide();
		$("dd").has("input[name='totalTax']").hide();
	}
	$("#btn_cancel").click(function()
	{
		closeTabAndJump("采购入库列表");
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
	$("#storageTime").val(new Date().format('yyyy-MM-dd'));

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
	trigger()

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
	paramObj.isCheck = isCheckVal;
	Helper.request({
		url : Helper.basePath + "/purch/stock/save",
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
				Helper.message.warn("保存失败！" + data.message);
			}
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
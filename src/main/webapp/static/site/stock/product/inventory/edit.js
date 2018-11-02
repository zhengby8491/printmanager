/* 判断当前用户是否有权限查看金额 */
var hasPermission = Helper.basic.hasPermission('stock:productInventory:money');
$(function()
{
	if (!hasPermission)
	{
		$("td").has("input[name='detailList.price']").hide();
		$("td").has("input[name='detailList.money']").hide();
		$("td").has("input[name='detailList.profitAndLossMoney']").hide();
	}
	$("#btn_cancel").click(function()
	{
		location.href = Helper.basePath + '/stockproduct/inventory/view/' + $("input[name='id']").val();
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
		$("table input[name='detailList.price']").each(function()
		{
			// console.log("qty="+$(this).val());
			if (Helper.isEmpty($(this).val()))
			{
				Helper.message.warn("请录入单价");
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
		Helper.popup.show('选择产品库存', Helper.basePath + '/stock/product/quick_select?warehouseId=' + $(this).val() + '&isEmptyWare=no', '1030', '490');
	});

	$("table").on("click", ".delete_row", function()
	{
		$(this).parent().parent().remove();
		setRowIndex();
	});

	$("table").on("input", "input[name='detailList.qty']", function()
	{
		countProfitAndLossQty($(this));
		countProfitAndLossMoney($(this))

	});
	// 选盘点人设置盘点人姓名
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
	var data = $("#form_inventory").formToJson();
	// 当没有选中员工时保存时会报错
	if (data['employeeId'] < 0)
	{
		delete data['employeeId'];
	}
	Helper.request({
		url : Helper.basePath + "/stockproduct/inventory/update",
		data : data,
		success : function(data)
		{
			if (data.success)
			{
				Helper.message.suc('已保存!');
				location.href = Helper.basePath + '/stockproduct/inventory/view/' + data.obj.id;
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

// 算盈亏数量
function countProfitAndLossQty(this_)
{
	var tr_dom = this_.parent().parent();
	var stockQty = tr_dom.find("input[name='detailList.stockQty']").val();
	var qty = tr_dom.find("input[name='detailList.qty']").val();
	var profitAndLossQty = qty - stockQty;
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
	var price = tr_dom.find("input[name='detailList.price']").val();
	var profitAndLossMoney = Number(price).mul(profitAndLossQty).tomoney();
	tr_dom.find("input[name='detailList.profitAndLossMoney']").val(profitAndLossMoney);
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
		var str = '<tr><td name="rowIndex">1</td>' + '<td class="td-manage">' + '<i class="delete_row fa fa-trash-o"></i>' + '<td><input name="detailList.code" class="tab_input" type="text" readonly="readonly" value="' + obj.product.code + '"/></td>' + '<td><input name="detailList.productName" class="tab_input" type="text" readonly="readonly" value="' + obj.product.name + '"/></td>' + '<td><input name="detailList.specifications" readonly="readonly" class="tab_input" type="text" value="' + (obj.product.specifications || "") + '"/></td>' + '<td><input name="detailList.unitName" class="tab_input" type="text" readonly="readonly" value="' + Helper.basic.info('UNIT', obj.product.unitId).name + '"/></td>'
				+ '<td><input name="detailList.qty" class="tab_input constraint_negative bg_color" type="text" value="' + obj.qty + '"/></td>' + '<td><input name="detailList.stockQty" class="tab_input" type="text" readonly="readonly" value="' + obj.qty + '"/></td>' + (hasPermission ? '<td>' : '<td style="display:none;">') + '<input name="detailList.price" class="tab_input" type="text" readonly="readonly" value="' + obj.price + '"/></td>' + (hasPermission ? '<td>' : '<td style="display:none;">') + '<input name="detailList.money" class="tab_input" type="text" readonly="readonly" value=""/></td>' + '<td><input name="detailList.profitAndLossQty" class="tab_input" type="text" readonly="readonly" value="0"/></td>'
				+ (hasPermission ? '<td>' : '<td style="display:none;">') + '<input name="detailList.profitAndLossMoney" class="tab_input" type="text" readonly="readonly" value=""/></td>' + '<td><input name="detailList.inventoryTypeName" class="tab_input" type="text" readonly="readonly" value=""/></td>' + '<td><input name="detailList.memo" class="tab_input bg_color memo" onmouseover="this.title=this.value" type="text" value=""/></td>' + '<td style="display:none"><input name="detailList.productId" class="tab_input" type="hidden" readonly="readonly" value="' + obj.productId + '"/></td>' + '<td style="display:none"><input name="detailList.productClassId" class="tab_input" type="hidden" readonly="readonly" value="'
				+ obj.product.productClassId + '"/></td>' + '<td style="display:none"><input name="detailList.stockProductId" class="tab_input id_' + obj.id + '" type="hidden" readonly="readonly" value="' + obj.id + '"/></td>' + '<td style="display:none"><input name="detailList.inventoryType" class="tab_input" type="hidden" readonly="readonly" value=""/></td>' + '<td style="display:none"><input name="detailList.unitId" class="tab_input" type="hidden" readonly="readonly" value="' + obj.product.unitId + '"/></td>' + '<td>' + (obj.product.imgUrl == '' ? '' : '<img class="pimg" width="35px" height="35px"  src="' + obj.product.imgUrl + '"/>') + '</td></tr>';
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
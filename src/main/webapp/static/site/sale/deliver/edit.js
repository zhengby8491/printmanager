var hasPermission = Helper.basic.hasPermission('sale:deliver:money');
$(function()
{
	if (!hasPermission)
	{
		$("#detailList").find("th[name='salePrice']").hide();
		$("#detailList").find("th[name='money']").hide();
		$("#detailList").find("th[name='tax']").hide();
		$("td").has("input[name='detailList.price']").hide();
		$("td").has("input[name='detailList.money']").hide();
		$("td").has("input[name='detailList.tax']").hide();
		$("dd").has("input[name='totalMoney']").hide();
		$("dd").has("input[name='noTaxTotalMoney']").hide();
		$("dd").has("input[name='totalTax']").hide();
	}
	// 取消
	$("#btn_cancel").click(function()
	{
		location.href = Helper.basePath + '/sale/deliver/view/' + $("#order_id").val();
	});
	// 保存
	$("#btn_save,#btn_save_audit").click(function()
	{
		fixEmptyValue();
		var flg = true;
		if ($("input[name='detailList.qty']").size() <= 0)
		{
			Helper.message.warn("请录入明细");
			flg = false;
			return;
		}
		$("table input[name='detailList.qty']").each(function()
		{
			if (Number($(this).val()) <= 0)
			{
				Helper.message.warn("产品数量必须大于0");
				flg = false;
				return false;
			}
		});
		$("table select[name='detailList.warehouseId']").each(function()
		{
			if (Helper.isEmpty($(this).val()) || $(this).val() < 0)
			{
				Helper.message.warn("请选择仓库");
				flg = false;
				return false;
			}
		});
		if (flg)
		{
			$(this).attr("disabled", "true");
			$("#isCheck").val($(this).attr("id") == "btn_save_audit" ? "YES" : "NO");
			form_submit();
		}
	})

	// 所有数量改变事件
	$("table tbody").on("keyup blur", "input[name='detailList.qty']", function()
	{
		var qty_value = Number($(this).val()).trunc();
		$(this).val(qty_value);
		var saveQty = $(this).parent().parent().find("input[name='detailList.saveQty']").val();

		if ($(this).val() == saveQty)
		{
			var saveMoney = $(this).parent().parent().find("input[name='detailList.saveMoney']").val();
			$(this).parent().parent().find("input[name='detailList.money']").val(saveMoney);
			calcTaxRate(this);
			return;
		}
		calcMoney(this);
	});

	// 修改单价
	$("table tbody").on("keyup blur", "input[name='detailList.price']", function()
	{
		calcMoney(this);
	});

	// 修改金额
	$("table tbody").on("blur", "input[name='detailList.money']", function()
	{
		var money_value = Number($(this).val()).tomoney();
		$(this).val(money_value);
		calcPrice(this);
	});

	/* 删除 */
	$("table tbody").on("click", "a[name='btn_del']", function()
	{
		$(this).parent().parent().remove();
		resetSequenceNum();
		sum();
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
	trigger();
	formatterPrice();
	sum();
	taxSelectNum();
});

// 修改金额
function calcPrice(obj)
{
	var qty_dom = $(obj).parent().parent().find("input[name='detailList.qty']");
	var price_dom = $(obj).parent().parent().find("input[name='detailList.price']");
	var money_dom = $(obj).parent().parent().find("input[name='detailList.money']");
	if (qty_dom.val() == 0 || qty_dom.val().trim() == '')
	{
		Helper.message.warn("请先输入数量");
		money_dom.val(0);
		return;
	}
	var price = Number(money_dom.val()).div(qty_dom.val()).toFixed(4);
	price_dom.val(price);
	calcTaxRate(obj);
}

// 批量修改税收
function batchEditTaxRate()
{
	var taxRateId = $(".batch_taxRate_select").val();
	if (taxRateId == -1)
	{
		shotCutWindow('TAXRATE', true, $(".batch_taxRate_select"), '', "TAXRATE");
	}
	if (taxRateId != -1) // 选中了自定义后不触发其他的下拉框
	{
		$("table select[name='detailList.taxRateId']").val(taxRateId);
		$("table select[name='detailList.taxRateId']").trigger("change");
	}
}
// 批量修改仓库
function batchEditWareHouse()
{
	var wareTypeId = $(".batch_wareHouse_select").val();
	if (wareTypeId == -1)
	{
		shotCutWindow('WAREHOUSE', true, $(".batch_wareHouse_select"), 'PRODUCT', "WAREHOUSE");
	} else
	{
		$("table select[name='detailList.warehouseId']").val($(".batch_wareHouse_select").val());
	}
}
// 改变单个税收
function taxSelectNum()
{
	$("select[name='detailList.taxRateId']").off('change').on('change', function()
	{
		if ($(this).val() == -1)
		{
			shotCutWindow('TAXRATE', true, $(this));
		} else
		{
			var flg = true;
			var taxRateId = $(this).val();
			$(this).parent().next().children().val(Helper.basic.info("TAXRATE", taxRateId).percent);
			calcTaxRate(this);
		}
	})
}

// 选择仓库事件
function selectHouse()
{
	$("select[name='detailList.warehouseId']").val($("#warehouseId").val());
}

function form_submit()
{
	$("#forceCheck").val("NO");

	$("#btn_save").attr({
		"disabled" : "disabled"
	});
	$("#btn_save_audit").attr({
		"disabled" : "disabled"
	});

	var form_payment = $("#form_payment").formToJson();
	form_payment.checkTime = null;												// 保存并审核时不需要携带审核时间
	Helper.request({
		url : Helper.basePath + "/sale/deliver/update",
		data : form_payment,
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
							location.href = Helper.basePath + '/sale/deliver/view/' + data.returnValue.id;
						});

					}, function(index)
					{
						location.href = Helper.basePath + '/sale/deliver/view/' + data.returnValue.id;
					});
					$("#btn_save").removeAttr("disabled");
					$("#btn_save_audit").removeAttr("disabled");

				} else
				{
					location.href = Helper.basePath + '/sale/deliver/view/' + data.returnValue.id;
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

// 重新设置序号
function resetSequenceNum()
{
	$("table tbody tr").each(function(index)
	{
		$(this).find("td").first().html(++index);
	});
}
// 转单过来时金额重新计算
function trigger()
{
	for (var i = 0; i < $("input[name='detailList.qty']").length; i++)
	{
		$("input[name='detailList.qty']").eq(i).trigger("blur");

	}
}
// 格式化单价
function formatterPrice()
{
	for (var i = 0; i < $("input[name='detailList.price']").length; i++)
	{
		var price = $("input[name='detailList.price']").eq(i).val();
		$("input[name='detailList.price']").eq(i).val(Number(price));
	}
}
// 计算金额
function calcMoney(obj)
{
	var qty_dom = $(obj).parent().parent().find("input[name='detailList.qty']");
	qty_dom.val(Number(qty_dom.val()).trunc());
	var price_dom = $(obj).parent().parent().find("input[name='detailList.price']");
	var money_dom = $(obj).parent().parent().find("input[name='detailList.money']");

	money_dom.val(Number(price_dom.val()).mul(Number(qty_dom.val())).tomoney());// 金额=(单价*数量)

	calcTaxRate(obj);
}
// 计算税额
function calcTaxRate(obj)
{
	// 获取金额对象并格式化
	var money_dom = $(obj).parent().parent().find("input[name='detailList.money']");
	var money_value = Number(money_dom.val()).tomoney();// 金额

	var taxRatePercent = Number($(obj).parent().parent().find("input[name='detailList.percent']").val());// 税率值
	// 不含税金额计算
	var noTaxMoney_dom = $(obj).parent().parent().find("input[name='detailList.noTaxMoney']");
	noTaxMoney_dom.val(Number(money_value).div(Number(1 + (taxRatePercent / 100))).tomoney());// 不含税金额=（金额/(1+税率值/100)）

	// 税额计算
	var tax_dom = $(obj).parent().parent().find("input[name='detailList.tax']");
	tax_dom.val(money_value.subtr(Number(noTaxMoney_dom.val())).tomoney());// 税额=(金额-不含税金额)

	var qty_value = Number($(obj).parent().parent().find("input[name='detailList.qty']").val());// 数量
	// 单价计算
	var price_dom = $(obj).parent().parent().find("input[name='detailList.price']");

	// 不含税单价计算
	var noTaxPrice_dom = $(obj).parent().parent().find("input[name='detailList.noTaxPrice']");
	noTaxPrice_dom.val(Number(price_dom.val()).div(Number(1 + (taxRatePercent / 100))).toFixed(4));// 不含税单价=(单价/(1+税率值/100))

	// 汇总
	sum();
}
// 汇总
function sum()
{
	var sum_qty = 0;
	var sum_tax = 0;
	var sum_noTaxMoney = 0;
	var sum_money = 0;
	$("table tbody tr").each(function()
	{
		sum_qty = Number(sum_qty).add(Number($(this).find("td input[name='detailList.qty']").val()));
		sum_tax = Number(sum_tax).add(Number($(this).find("td input[name='detailList.tax']").val()));
		sum_noTaxMoney = Number(sum_noTaxMoney).add(Number($(this).find("td input[name='detailList.noTaxMoney']").val()));
		sum_money = Number(sum_money).add(Number($(this).find("td input[name='detailList.money']").val()));
	});
	$("#qty").val(sum_qty);
	$("#tax").val(sum_tax.tomoney());
	$("#noTaxMoney").val(sum_noTaxMoney.tomoney());
	$("#money").val(sum_money.tomoney());
	$("#totalMoney").val(sum_money.tomoney());
	$("#noTaxTotalMoney").val(sum_noTaxMoney.tomoney());
	$("#totalTax").val(sum_tax.tomoney());
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
		url : Helper.basePath + '/sale/deliver/forceCheck/' + id,
		async : false,
		error : function(request)
		{
			layer.alert("Connection error");
		},
		success : function(data)
		{
			if (data.success)
			{
				location.href = Helper.basePath + '/sale/deliver/view/' + id;
			} else
			{
				layer.alert("审核失败")
			}
		}
	});
}
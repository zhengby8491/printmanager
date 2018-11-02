var hasPermission = Helper.basic.hasPermission('sale:reconcil:money');

$(function()
{
	if (!hasPermission)
	{
		$("td").has("input[name='detailList.price']").hide();
		$("td").has("input[name='detailList.tax']").hide();
		$("td").has("input[name='detailList.money']").hide();
		$("dd").has("input[id=totalMoney]").hide();
		$("dd").has("input[id=noTaxTotalMoney]").hide();
		$("dd").has("input[id=totalTax]").hide();
	}
	// 取消
	$("#btn_cancel").click(function()
	{
		closeTabAndJump("销售对账列表");
	});
	// 初始结算日期
	if ($("#reconcilTime").val() == "")
	{
		$("#reconcilTime").val(new Date().format('yyyy-MM-dd'));
	}
	// 初始化制单日期
	$("#createDate").val(new Date().format('yyyy-MM-dd'));

	// 保存
	$("#btn_save,#btn_save_audit").click(function()
	{
		fixEmptyValue();
		if (Helper.isEmpty($("#settlementClassId").val()))
		{
			Helper.message.warn("请选择结算方式");
			return false;
		}
		if ($("input[name='detailList.qty']").size() <= 0)
		{
			Helper.message.warn("请录入明细");
			return false;
		}
		$("#isCheck").val($(this).attr("id") == "btn_save_audit" ? "YES" : "NO");
		form_submit();
	})
	/* 删除 */
	$("table tbody").on("click", "a[name='btn_del']", function()
	{
		$(this).parent().parent().remove();
		resetSequenceNum();
		sum();
	});
	// 所有单价改变事件
	$("table tbody").on("input", "input[name='detailList.price']", function()
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

	// 所有数量改变事件
	$("table tbody").on("input", "input[name='detailList.qty']", function()
	{
		var qty_value = Number($(this).val()).trunc();
		$(this).val(qty_value);
		// var
		// saveQty=$(this).parent().parent().find("input[name='detailList.saveQty']").val();
		// if(Math.abs($(this).val())==Math.abs(saveQty)){
		// var
		// saveMoney=$(this).parent().parent().find("input[name='detailList.saveMoney']").val();
		// $(this).parent().parent().find("input[name='detailList.money']").val(saveMoney);
		// calcTaxRate(this, false);
		// return;
		// }
		calcMoney(this);
	});
	$("table tbody").on("blur", "input[name='detailList.qty']", function()
	{
		var tr_dom = $(this).parent().parent();
		if (tr_dom.find("input[name='detailList.sourceBillType']").val() == "SALE_IR" && Number($(this).val()) > 0)
		{
			$(this).val(0 - Number($(this).val()));
			$(this).trigger("input");
		}
	});
	trigger();
	// 解决性能问题 -- 最后在汇总
	sum();
	formatterPrice();
	taxSelectNum();
});

function trigger()
{
	$("input[name='detailList.qty']").trigger("input");
}
// 重新设置序号
function resetSequenceNum()
{
	$("table tbody tr").each(function(index)
	{
		$(this).find("td").first().html(++index);
	});
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

// 改变所有税率
function taxSelect()
{
	var taxRateId = $("#rateId").val();
	$("select[name='detailList.taxRateId']").val(taxRateId);
	$("input[name='detailList.percent']").val(Helper.basic.info("TAXRATE", taxRateId).percent);
	$("table select[name='detailList.taxRateId']").trigger("change");
}

// 改变单个税率
function taxSelectNum()
{
	$("select[name='detailList.taxRateId']").change(function()
	{
		var flg = true;
		var taxRateId = $(this).val();
		$(this).parent().next().children().val(Helper.basic.info("TAXRATE", taxRateId).percent);
		calcTaxRate(this);
	});
}

function form_submit()
{
	$("table tbody tr").each(function()
	{
		$(this).find("td select[name='detailList.taxRateId']").removeAttr("disabled");
	});
	$("#btn_save").attr({
		"disabled" : "disabled"
	});
	$("#btn_save_audit").attr({
		"disabled" : "disabled"
	});

	Helper.request({
		url : Helper.basePath + "/sale/reconcil/update",
		data : $("#form_order").formToJson(),
		success : function(data)
		{
			if (data.success)
			{
				Helper.message.suc('已保存!');
				location.href = Helper.basePath + '/sale/reconcil/view/' + data.obj.id;
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
function calcTaxRate(obj, needSum)
{
	// 获取金额对象并格式化
	var money_dom = $(obj).parent().parent().find("input[name='detailList.money']");
	var money_value = Number(money_dom.val()).tomoney();// 金额
	money_dom.val(money_value);

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
	// 汇总(首次初始化补需要持续汇总 --- 解决性能问题)
	if (false !== needSum)
	{
		sum();
	}
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
		$(this).find("td select[name='detailList.taxRateId']").attr("disabled", true);

		sum_qty = Number(sum_qty).add(Number($(this).find("td input[name='detailList.qty']").val()));
		sum_tax = Number(sum_tax).add(Number($(this).find("td input[name='detailList.tax']").val())).tomoney();
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
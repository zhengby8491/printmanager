$(function()
{
	// 输入数量计算计价数量
	$("table").on("input", "input[name='detailList.qty']", function()
	{
		countValuationQty($(this));
		countMoneyByValuationPrice($(this));
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
	// 输入规则计算计价数量
	$("table").on("change", "input[name='detailList.specifications']", function()
	{

		countValuationQty($(this));
		countMoneyByValuationPrice($(this));
	});
	$("table").on("input", "input[name='detailList.valuationPrice']", function()
	{
		countMoneyByValuationPrice($(this));
	});
})

function countMoneyByValuationPrice(this_)
{
	var tr_dom = this_.parent().parent();
	var qty = tr_dom.find("input[name='detailList.qty']").val();
	var valuationQty = tr_dom.find("input[name='detailList.valuationQty']").val();
	var valuationPrice = tr_dom.find("input[name='detailList.valuationPrice']").val();
	var price;
	var money;
	if (price == "")
	{
		return;
	}
	money = Number(valuationQty).mul(valuationPrice).tomoney();
	price = Number(money).div(qty).toFixed(4);
	if (money != 'NaN')
		tr_dom.find("input[name='detailList.money']").val(money);

	if (price != 'NaN')
		tr_dom.find("input[name='detailList.price']").val(price);
}

function cancelReturn()
{
	history.go(-1);
}

// 规格键盘输入控制
$(document).on('keypress', "input[name='detailList.specifications']", function()
{
	var flag = (/\d|\*|[.]{1}/.test(String.fromCharCode(event.keyCode)));
	return flag;
});
// 规格失去焦点判断
$(document).on('blur', "input[name='detailList.specifications']", function()
{
	if (!Helper.validata.isMaterialSize($(this).val()))
	{
		Helper.message.warn("请录入正确的规格格式xxx或xxx*xxx或xxx*xxx*xxx (小数最多4位)");
		$(this).val("");
	}
});
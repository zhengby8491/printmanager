$(function()
{
	$(document).on("input", "input[name='detailList.qty']", function()
	{
		countValuationQty($(this));
		countPrice($(this));
		// 当与印刷家不合作了，这段代码可以删除
		if (!Helper.isEmpty($(this).parent().parent().find("input[name='detailList.extOrderId']").val()))
		{
			var $parent = $(this).parent().parent(); 
			var qty =  Number($parent.find("input[name='detailList.qty']").val());
			var price = Number($parent.find("input[name='detailList.valuationPrice']").val());
			var saveQty = Number($parent.find("input[name='detailList.saveQty']").val());
			var savePrice = Number($parent.find("input[name='detailList.saveValuationPrice']").val());
			var saveMoney = Number($parent.find("input[name='detailList.saveMoney']").val());
			if (qty == saveQty && price == savePrice)
			{
				$parent.find("input[name='detailList.money']").val(saveMoney);
			}
		}
		countMoney();
	})// stockUnitAccuracy
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
	$(document).on("input", "input[name='detailList.valuationPrice']", function()
	{
		countPrice($(this));
		// 当与印刷家不合作了，这段代码可以删除
		if (!Helper.isEmpty($(this).parent().parent().find("input[name='detailList.extOrderId']").val()))
		{
			var $parent = $(this).parent().parent(); 
			var qty =  Number($parent.find("input[name='detailList.qty']").val());
			var price = Number($parent.find("input[name='detailList.valuationPrice']").val());
			var saveQty = Number($parent.find("input[name='detailList.saveQty']").val());
			var savePrice = Number($parent.find("input[name='detailList.saveValuationPrice']").val());
			var saveMoney = Number($parent.find("input[name='detailList.saveMoney']").val());
			if (qty == saveQty && price == savePrice)
			{
				$parent.find("input[name='detailList.money']").val(saveMoney);
			}
		}
		countMoney();
	})
	$(document).on("change", "select[name='detailList.taxRateId']", function()
	{
		if ($(this).val() == -1)
		{
			shotCutWindow('TAXRATE', true, $(this));
		} else
		{
			onTaxSelect($(this));
			countMoney();
		}
	})
	$(document).on("change", "select[name='currencyType']", function()
	{
		selectCurrencyType($(this))
		countMoney();
	})
	$(document).on("change", "input[name='detailList.specifications']", function()
	{
		countValuationQty($(this));
		countPrice($(this));
		countMoney();
	})
	// 删除一行数据
	$("table").on("click", ".row_delete", function()
	{
		$(this).parent().parent().remove();
		resetSequenceNum();
		countMoney();
	});
	// 监听明细表里的仓库事件
	$(document).on("change", "table select[name='detailList.warehouseId']", function()
	{
		if ($(this).val() == -1)
		{
			shotCutWindow('WAREHOUSE', true, $(this), 'MATERIAL');
		}
	})
	$("#btn_save,#btn_save_audit").click(function()
	{
		fixEmptyValue();// 处理自定义未输入数值
		var validate = true;
		isCheckVal = $(this).attr("id") == "btn_save_audit" ? "YES" : "NO";
		if ($("#supplierName").val() == "")
		{
			Helper.message.warn("请录入供应商");
			validate = false;
			return;
		}
		if ($("input[name='detailList.qty']").size() <= 0)
		{
			Helper.message.warn("请录入明细");
			validate = false;
			return;
		}
		$("input[name='detailList.qty']").each(function()
		{
			if (Helper.isEmpty($(this).val()) || Number($(this).val()) == 0)
			{
				Helper.message.warn("数量必须大于0");
				validate = false;
				return;
			}
		});
		$("table select[name='detailList.warehouseId']").each(function()
		{
			if (Helper.isEmpty($(this).val()) || $(this).val() < 0)
			{
				Helper.message.warn("请选择仓库");
				validate = false;
				return;
			}
		})
		/*
		 * $("table input[name='detailList.money']").each(function(){
		 * if(Helper.isEmpty($(this).val())||Number($(this).val())==0) {
		 * Helper.message.warn("金额不能为0"); validate=false; return; } });
		 */
		$("table input[name='detailList.valuationPrice']").each(function()
		{
			if (Helper.isEmpty($(this).val()))
			{
				Helper.message.warn("单价不能为空");
				validate = false;
				return;
			}
		});

		if (validate == true)
		{
			$("#isCheck").val($(this).attr("id") == "btn_save_audit" ? "YES" : "NO");
			save();
		}

	});
})
// 重新设置序号
function resetSequenceNum()
{
	$("table tbody tr").each(function(index)
	{
		$(this).find("td").first().html(++index);
	});
}
// 算金额 税额
function countPrice(this_)
{
	var tr_dom = this_.parent().parent();
	var valuationPrice = tr_dom.find("input[name='detailList.valuationPrice']").val();
	var valuationQty = tr_dom.find("input[name='detailList.valuationQty']").val();
	var taxRate = tr_dom.find("input[name='detailList.percent']").val();
	var qty = tr_dom.find("input[name='detailList.qty']").val();

	if (valuationPrice == "" || valuationQty == "")
	{
		return;
	}
	var money = (Number(valuationPrice).mul(Number(valuationQty))).tomoney();// 金额

	var noTaxMoney = (Number(money).div((Number(100).add(taxRate)).div(100))).tomoney();// 不含税金额
	var tax = (Number(money).subtr(noTaxMoney)).tomoney();// 税额
	var price = (Number(money).div(qty)).toFixed(4);// 库存单价
	var noTaxPrice = (Number(price).div((Number(100).add(taxRate)).div(100))).toFixed(4);// 不含税单价
	if (price == "NaN")
	{
		price = 0;
	}
	var noTaxValuationPrice = (Number(valuationPrice).mul((Number(100).subtr(taxRate)).div(100))).toFixed(4);// 不含税计价单价
	if (noTaxValuationPrice == "NaN")
	{
		noTaxValuationPrice = 0;
	}
	tr_dom.find("input[name='detailList.money']").val(money);// 金额
	tr_dom.find("input[name='detailList.tax']").val(tax);// 税额
	tr_dom.find("input[name='detailList.noTaxMoney']").val(noTaxMoney);// 不含税金额
	tr_dom.find("input[name='detailList.noTaxPrice']").val(noTaxPrice);// 不含税单价
	tr_dom.find("input[name='detailList.price']").val(price);// 库存单价
	tr_dom.find("input[name='detailList.noTaxValuationPrice']").val(noTaxValuationPrice);// 不含税计价单价
}
// 算总金额
function countMoney()
{// PURCH_PK PURCH_PR
	var totalMoney = 0;
	var tax = 0;
	for (var i = 0; i < $("input[name='detailList.code']").length; i++)
	{
		totalMoney = Number(totalMoney).add(Number($("input[name='detailList.money']").eq(i).val()));
	}
	for (var i = 0; i < $("input[name='detailList.tax']").length; i++)
	{
		tax = Number(tax).add(Number($("input[name='detailList.tax']").eq(i).val()));
	}
	$("#totalMoney").val(totalMoney.tomoney());
	$("#totalTax").val(tax.tomoney());
	$("#noTaxTotalMoney").val((totalMoney.subtr(tax)).tomoney());
}
// 选择主表税收后重新计算数据
function taxSelect()
{
	var taxRateId = $("#taxRateId").val();
	if (Helper.isNotEmpty(Helper.basic.info('TAXRATE', taxRateId)))
	{
		var percent = Helper.basic.info('TAXRATE', taxRateId).percent;
	}
	for (var i = 0; i < $("input[name='detailList.code']").length; i++)
	{
		var taxRateValue = $("#taxRateId").find("option:selected").val();
		$("input[name='detailList.percent']").eq(i).val(percent);
		$("select[name='detailList.taxRateId']").eq(i).val(taxRateValue).trigger("change");
		countPrice($("input[name='detailList.percent']").eq(i));
	}
}
// 价格全部重新计算
function selectCurrencyType()
{
	for (var i = 0; i < $("input[name='detailList.code']").length; i++)
	{
		countPrice($("input[name='detailList.code']").eq(i));
	}
}
// 选择主表交货日期 同步到从表所有交货日期
function purchTimeSelect()
{
	$("input[name='detailList.deliveryTime']").val($("#purchTime").val());
}
// 选择单个税率
function onTaxSelect(this_)
{
	var tr_dom = this_.parent().parent();
	var taxRateId = tr_dom.find("select[name='detailList.taxRateId']").val();
	if (taxRateId > 0)
	{
		tr_dom.find("input[name='detailList.percent']").val(Helper.basic.info('TAXRATE', taxRateId).percent);
	}
	countPrice(this_);
}

/*
 * function warehouseSelect(){
 * $("select[name='detailList.warehouseId']").val($("#warehouseId").val()); }
 */
// 批量修改税收
function batchEditTaxRate()
{
	var taxRateId = $(".batch_taxRate_select").val();
	if (taxRateId == -1)
	{
		shotCutWindow('TAXRATE', true, $(".batch_taxRate_select"), '', "TAXRATE");
		return;
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
		shotCutWindow('WAREHOUSE', true, $(".batch_wareHouse_select"), 'MATERIAL', "WAREHOUSE");
	} else
	{
		$("table select[name='detailList.warehouseId']").val($(".batch_wareHouse_select").val());
	}
}
// 批量修改交货日期
function batchEditDeliveryTime()
{
	$("table input[name='detailList.deliveryTime']").val($(".batch_deliveryTime_input").val());
}
// 规格键盘输入控制
$(document).on('keypress', "input[name='detailList.specifications']", function()
{
	var f = (/\d|\*|[.]{1}/.test(String.fromCharCode(event.keyCode)));
	return f;
});
// 规格失去焦点判断
$(document).on('blur', "input[name='detailList.specifications']", function()
{

	if (!Helper.validata.isMaterialSize($(this).val()))
	{
		Helper.message.warn("请录入正确的规格格式xxx或xxx*xxx或xxx*xxx*xxx (小数最多4位)");

		$(this).val("");
	}

	/*
	 * if(!/^[0-9]+([.]{1}[0-9]{1,4})?\*[0-9]+([.]{1}[0-9]{1,4})?$/.test($(this).val())) {
	 * Helper.message.warn("请录入正确的规格格式 xxxx*xxxx(小数最多4位)"); $(this).val(""); }
	 */
});

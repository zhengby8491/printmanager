var hasPermission = Helper.basic.hasPermission('outsource:arrive:money');
$(function()
{
	if (!hasPermission)
	{
		$("#detailList td").has("input[name='detailList.money']").hide();
		$("#detailList td").has("input[name='detailList.price']").hide();
		$("#detailList td").has("input[name='detailList.tax']").hide();
		$("dd").has("input[name='totalMoney']").hide();
		$("dd").has("input[name='noTaxTotalMoney']").hide();
		$("dd").has("input[name='totalTax']").hide();
	}
	// 取消
	$("#btn_cancel").click(function()
	{
		closeTabAndJump("发外到货列表");
	});

	// 初始化到货日期
	$("#deliveryTime").val(new Date().format('yyyy-MM-dd'));

	// 初始化制单日期
	$("#createDate").val(new Date().format('yyyy-MM-dd'));

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

	// 删除一行数据
	$("table").on("click", ".row_delete", function()
	{
		$(this).parent().parent().remove();
		resetSequenceNum();
		sum();
	});
	// 重新设置序号
	function resetSequenceNum()
	{
		$("table tbody tr").each(function(index)
		{
			$(this).find("td").first().html(++index);
		});
	}
	// 所有到货数量改变事件
	$("table input[name='detailList.qty']").on("blur", function()
	{
		var productType = $(this).parent().parent().find("input[name='detailList.productType']").val();
		var workProcedureType = $(this).parent().parent().find("input[name='detailList.workProcedureType']").val();
		var qty_value;
		if (productType == 'ROTARY' && workProcedureType == 'PART')
		{
			qty_value = Number($(this).val()).toFixed(2);
		} else
		{
			qty_value = Number($(this).val()).trunc();
		}
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

	// 所有单个税收改变事件
	$("table select[name='detailList.taxRateId']").on("change", function()
	{
		if ($(this).val() == -1)
		{
			shotCutWindow("TAXRATE", true, $(this));
		} else
		{
			var taxRate = Helper.basic.info('TAXRATE', $(this).val());// 税率
			$(this).parent().parent().find("input[name='detailList.taxRateName']").val(taxRate.name);// 更新税收名称
			$(this).parent().parent().find("input[name='detailList.taxRatePercent']").val(taxRate.percent);// 更新税率
			calcTaxRate(this);
		}
	});

	$("table select[name='detailList.taxRateId']").trigger("change");// 出发税收明细改变事件

	/*
	 * //纠正税收名称 $("table select[name='detailList.taxRate']").each(function() {
	 * $(this).val($(this).parent().parent().find("input[name='detailList.taxRateId']").val());
	 * });
	 */

	// 保存
	$("#btn_save,#btn_save_audit").click(function()
	{
		fixEmptyValue();
		var form_check_flag = true;
		paramObj = $("#form_arrive").formToJson();
		paramObj.isCheck = $(this).attr("id") == "btn_save_audit" ? "YES" : "NO";
		if (Helper.isEmpty($("#supplierId").val()))
		{
			Helper.message.warn("请选择供应商")
			return false;
		}
		if (form_check_flag)
		{
			$("table select[name='detailList.warehouseId']").each(function()
			{
				if (Helper.isEmpty($(this).val()) || Number($(this).val()) < 0)
				{
					Helper.message.warn("请选择仓库");
					form_check_flag = false;
					$(this).focus().select();
					return false;
				}
			});
		}
		if (form_check_flag)
		{
			if ($("input[name='detailList.qty']").size() <= 0)
			{
				Helper.message.warn("请录入明细");
				form_check_flag = false;
				return false;
			}
		}
		if (form_check_flag)
		{
			$("table input[name='detailList.qty']").each(function()
			{
				if (Helper.isEmpty($(this).val()) || Number($(this).val()) <= 0)
				{
					Helper.message.warn("加工数量必须大于0");
					form_check_flag = false;
					$(this).focus().select();
					return false;
				}
			});
		}
		if (form_check_flag)
		{
			$("table input[name='detailList.price']").each(function()
			{
				if (Helper.isEmpty($(this).val()) || Number($(this).val()) <= 0)
				{
					Helper.message.warn("单价必须大于0");
					form_check_flag = false;
					$(this).focus().select();
					return false;
				}
			});
		}
		if (form_check_flag)
		{
			$("table input[name='detailList.money']").each(function()
			{
				if (Helper.isEmpty($(this).val()) || Number($(this).val()) <= 0)
				{
					Helper.message.warn("加工金额必须大于0");
					form_check_flag = false;
					$(this).focus().select();
					return false;
				}
			});
		}
		if (form_check_flag)
		{// 保存
			form_submit();
		}
	})
});
// 批量修改税收
function batchEditTaxRate()
{
	var taxRateId = $(".batch_taxRate_select").val();
	if (taxRateId == -1)
	{
		shotCutWindow('TAXRATE', true, $(".batch_taxRate_select"), '', "TAXRATE");
	} else
	{
		$("table select[name='detailList.taxRateId']").val(taxRateId);
		// $("input[name='detailList.percent']").val(Helper.basic.info("TAXRATE",
		// taxRateId).percent);
		$("table select[name='detailList.taxRateId']").trigger("change");
	}
}
function form_submit()
{
	$("#btn_save").attr({
		"disabled" : "disabled"
	});
	$("#btn_save_audit").attr({
		"disabled" : "disabled"
	});
	Helper.request({
		url : Helper.basePath + "/outsource/arrive/save",
		data : paramObj,// 将form序列化成JSON字符串
		success : function(data)
		{
			if (data.success)
			{
				Helper.message.suc('已保存!');
				location.href = Helper.basePath + '/outsource/arrive/view/' + data.obj.id;
			} else
			{
				Helper.message.warn('保存失败!' + data.message);
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
	var productType = $(obj).parent().parent().find("input[name='detailList.productType']").val();
	var workProcedureType = $(obj).parent().parent().find("input[name='detailList.workProcedureType']").val();

	if (productType == 'ROTARY' && workProcedureType == 'PART')
	{
		qty_dom.val(Number(qty_dom.val()).toFixed(2));
	} else
	{
		qty_dom.val(Number(qty_dom.val()).trunc());
	}

	var price_dom = $(obj).parent().parent().find("input[name='detailList.price']");
	var money_dom = $(obj).parent().parent().find("input[name='detailList.money']");

	money_dom.val(Number(price_dom.val()).mul(Number(qty_dom.val())).tomoney());// 金额=(单价*加工数量)

	calcTaxRate(obj);
}
// 计算税额
function calcTaxRate(obj)
{
	// 获取金额对象并格式化
	var money_dom = $(obj).parent().parent().find("input[name='detailList.money']");
	var money_value = Number(money_dom.val()).tomoney();// 金额

	var taxRatePercent = Number($(obj).parent().parent().find("input[name='detailList.taxRatePercent']").val());// 税率
	// 不含税金额计算
	var noTaxMoney_dom = $(obj).parent().parent().find("input[name='detailList.noTaxMoney']");
	noTaxMoney_dom.val(Number(money_value).div(Number(1 + (taxRatePercent / 100))).tomoney());// 不含税金额=（金额/(1+税率/100)）

	// 税额计算
	var tax_dom = $(obj).parent().parent().find("input[name='detailList.tax']");
	tax_dom.val(money_value.subtr(Number(noTaxMoney_dom.val())).tomoney());// 税额=(金额-不含税金额)

	var qty_value = Number($(obj).parent().parent().find("input[name='detailList.qty']").val());// 数量
	// 单价计算
	var price_dom = $(obj).parent().parent().find("input[name='detailList.price']");
	// price_dom.val(Number(money_dom.val()).div(qty_value).tomoney());//不含税单价=（不含税金额/数量）

	// 不含税单价计算
	var noTaxPrice_dom = $(obj).parent().parent().find("input[name='detailList.noTaxPrice']");
	noTaxPrice_dom.val(Number(price_dom.val()).div(Number(1 + (taxRatePercent / 100))).toFixed(4));// 不含税单价=(单价/(1+税率/100))

	// 汇总
	sum();
}

// 汇总
function sum()
{
	var money_sum = 0;
	var tax_sum = 0;
	var noTaxMoney_sum = 0;

	$("table input[name='detailList.money']").each(function()
	{
		money_sum += (Number($(this).val()));
	});
	$("table input[name='detailList.noTaxMoney']").each(function()
	{
		noTaxMoney_sum += (Number($(this).val()));
	});
	$("table input[name='detailList.tax']").each(function()
	{
		tax_sum += (Number($(this).val()));
	});
	$("#sum_money").val(money_sum.tomoney());
	$("#sum_noTaxMoney").val(noTaxMoney_sum.tomoney());
	$("#sum_tax").val(tax_sum.tomoney());
}
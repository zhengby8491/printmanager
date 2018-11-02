$(function()
{
	$("#btn_purch_save,#btn_purch_save_audit").click(function()
	{
		fixEmptyValue();// 处理自定义未输入数值
		var this_ = $(this);
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
		$("table input[name='detailList.money']").each(function()
		{
			if (Helper.isEmpty($(this).val()) || Number($(this).val()) == 0)
			{
				Helper.message.warn("金额不能为0");
				validate = false;
				return;
			}
		});
		$("table input[name='detailList.valuationPrice']").each(function()
		{
			if (Helper.isEmpty($(this).val()))
			{
				Helper.message.warn("单价不能为空");
				validate = false;
				return;
			}
		});
		$("table select[name='detailList.taxRateId']").each(function()
		{
			if (Number($(this).val()) <= 0)
			{
				Helper.message.warn("请选择税收");
				validate = false;
				return false;
			}
		});
		if (validate == true)
		{
			var isSave = false;// 是否弹出空规格提示
			var isEqual = false;// 是否有不同的单位
			$("input[name='detailList.specifications']").each(function()
			{
				if (Helper.isEmpty($(this).val()))
				{
					var tr_dom = $(this).parent().parent();
					var valuationUnitName = tr_dom.find("input[name='detailList.valuationUnitName']").val();
					var purchUnitName = tr_dom.find("input[name='detailList.purchUnitName']").val();
					if (valuationUnitName != purchUnitName)
					{
						isEqual = true;
					}
					isSave = true;
				}
			});

			if (isSave)
			{
				Helper.message.confirm('存在空的材料规格，是否保存', function(index)
				{
					if (isEqual == true)
					{
						Helper.message.warn("有需要单位换算的材料，请输入规格进行换算");
						return;
					} else
					{
						$("#isCheck").val(this_.attr("id") == "btn_purch_save_audit" ? "YES" : "NO");
						save();
					}
				})
			} else
			{
				$("#isCheck").val(this_.attr("id") == "btn_purch_save_audit" ? "YES" : "NO");
				save();
			}

		}

	});
	$("#btn_cancel").click(function()
	{
		location.href = Helper.basePath + '/purch/reconcil/view/' + $("#id").val();
	});
	$("input[name='detailList.qty']").change(function()
	{
		var tr_dom = $(this).parent().parent();
		var sourceQty = tr_dom.find("input[name='detailList.sourceQty']").val();
		if (Number($(this).val()) > Number(sourceQty))
		{
			Helper.message.warn("输入数量大于源单数量");
			$(this).val(sourceQty);
			$(this).trigger("input");
		}
	});
	$("input[name='detailList.qty']").blur(function()
	{
		var tr_dom = $(this).parent().parent();
		if (tr_dom.find("input[name='detailList.sourceBillType']").val() == "PURCH_PR" && Number($(this).val()) > 0)
		{
			$(this).val(0 - Number($(this).val()));
			$(this).trigger("input");
		}
	});
	$("#selectLinkName").change(function()
	{
		$("#mobile").val($(this).val());
	});
	// 初始化批量修改仓库、税收、交货日期悬浮窗
	$("#batch_edit_wareHouse,#batch_edit_taxRate,#batch_edit_deliveryTime").each(function()
	{
		var boxWidth = $("#" + $(this).attr("src")).innerWidth() * (-0.5) + 10;
		$(this).powerFloat({
			eventType : "click",
			targetAttr : "src",
			reverseSharp : true,
			container : $(this).siblings(".batch_box_container")
		})
	})
})
function save()
{
	var paramObj = $("#purchReconcilAction").formToJson();
	Helper.request({
		url : Helper.basePath + '/purch/reconcil/update',
		data : paramObj,
		error : function(request)
		{
			Helper.message.warn("Connection error");
		},
		success : function(data)
		{
			if (data.success)
			{
				window.location.href = Helper.basePath + '/purch/reconcil/view/' + data.obj.id;
			} else
			{
				Helper.message.warn(data.message);
			}
		}
	});
}
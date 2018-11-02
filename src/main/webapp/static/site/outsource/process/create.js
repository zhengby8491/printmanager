var hasPermission = Helper.basic.hasPermission('outsource:process:money');
	
	$(function()
	{
		var _type = $("#type").val();
		// 添加工艺信息和材料信息
		appendProceduresMaterials();
		
		if (!hasPermission)
		{
			$("#detailList td").has("input[name='detailList.money']").hide();
			$("#detailList td").has("input[name='detailList.price']").hide();
			$("#detailList td").has("input[name='detailList.tax']").hide();
			$("dd").has("input[name='totalMoney']").hide();
			$("dd").has("input[name='noTaxTotalMoney']").hide();
			$("dd").has("input[name='totalTax']").hide();
		}
		//取消
		$("#btn_cancel").click(function()
		{
			closeTabAndJump("发外加工列表");
		});
		/* 选择代工平台供应商 */
		$("#supplier_quick_oem").click(function()
		{
			if (_type === 'PROCESS')
			{
				Helper.popup.show('选择供应商', Helper.basePath + '/quick/supplierOem?multiple=false&supplierType=PROCESS', '900', '490');
			} else if (_type === 'PRODUCT')
			{
				Helper.popup.show('选择供应商', Helper.basePath + '/quick/supplier_select?multiple=false&supplierType=PROCESS', '900', '490');
			}
		});
		// 初始化交货日期
		$("#deliveryTime").val(new Date().format('yyyy-MM-dd'));

		// 初始化制单日期
		$("#createDate").val(new Date().format('yyyy-MM-dd'));

		//初始化批量修改仓库、税收、交货日期悬浮窗
		$("#batch_edit_wareHouse,#batch_edit_taxRate,#batch_edit_deliveryTime").each(function()
		{
			$(this).powerFloat({
				eventType : "click",
				targetAttr : "src",
				reverseSharp : true,
				container : $(this).siblings(".batch_box_container")
			})
		})

		//删除一行数据
		$("table").on("click", ".row_delete", function()
		{
			$(this).parent().parent().remove();
			resetSequenceNum();
			sum();
		});
		//重新设置序号  
		function resetSequenceNum()
		{
			$("table tbody tr").each(function(index)
			{
				$(this).find("td").first().html(++index);
			});
		}

		//税率改变事件
		$("#rate").change(function()
		{

			var percent = Helper.basic.info('TAXRATE', $(this).val()).percent;//税率值

			$("select[name='detailList.taxRateId']").val($(this).val());

			$("table input[name='detailList.taxRatePercent']").val(percent);
			$("table select[name='detailList.taxRateId']").trigger("change");
		});

		//所有加工金额改变事件
		$("table input[name='detailList.money']").on("blur", function()
		{
			var money_value = Number($(this).val()).tomoney();
			$(this).val(money_value);
			calcPrice(this);
		});
		//所有单价改变事件
		$("table input[name='detailList.price']").on("blur", function()
		{
			var price_value = Number($(this).val()).toFixed(4);
			$(this).val(price_value)
			calcMoney(this);
		});
		//所有加工数量改变事件
		$("table input[name='detailList.qty']").on("blur", function()
		{
			var productType = $(this).parent().parent().find("input[name='detailList.productType']").val();
			var workProcedureType = $(this).parent().parent().find("input[name='detailList.workProcedureType']").val();
			var qty_value;
			if (productType == "ROTARY" && workProcedureType == "PART")
			{
				qty_value = Number($(this).val()).toFixed(2);
			} else
			{
				qty_value = Number($(this).val()).trunc();
			}
			$(this).val(qty_value);
			calcMoney(this);
		});

		//所有单个税率改变事件
		$("table select[name$='taxRateId']").on("change", function()
		{
			if ($(this).val() == -1)
			{
				shotCutWindow("TAXRATE", true, $(this));
			} else
			{
				var taxRate = Helper.basic.info('TAXRATE', $(this).val());//税率值

				$(this).parent().parent().find("input[name='detailList.taxRateName']").val(taxRate.name);//更新税率名称
				$(this).parent().parent().find("input[name='detailList.taxRatePercent']").val(taxRate.percent);//更新税率值
				calcTaxRate(this);
			}
		});

		$("#rate").trigger("change");//触发全局税率改变事件，同时计算汇率

		//保存
		$("#btn_save,#btn_save_audit").click(function()
		{
			fixEmptyValue();
			var form_check_flag = true;
			paramObj = $("#form_process").formToJson();
			paramObj.isCheck = $(this).attr("id") == "btn_save_audit" ? "YES" : "NO";
			if (Helper.isEmpty($("#supplierId").val()))
			{
				Helper.message.warn("请选择供应商")
				return false;
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
			// 检查是否加载完工艺信息和材料信息
			var loading_flag = $("#loading_flag").val();
			if (loading_flag != "true")
			{
				Helper.message.warn("工艺信息或材料信息加载中");
				return false;
			}

			if (form_check_flag)
			{//保存
				form_submit();
			}
		})
	});
	//批量修改税收
	function batchEditTaxRate()
	{
		var taxRateId = $(".batch_taxRate_select").val();
		if (taxRateId == -1)
		{
			shotCutWindow('TAXRATE', true, $(".batch_taxRate_select"), '', "TAXRATE");
		} else
		{
			$("table select[name='detailList.taxRateId']").val(taxRateId);
			//$("input[name='detailList.percent']").val(Helper.basic.info("TAXRATE", taxRateId).percent);
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
			url : Helper.basePath + "/outsource/process/save",
			data : paramObj,//将form序列化成JSON字符串  
			success : function(data)
			{
				if (data.success)
				{
					Helper.message.suc('已保存!');
					location.href = Helper.basePath + '/outsource/process/view/' + data.obj.id;
				} else
				{
					Helper.message.warn('保存失败!' + data.message);
					$("#btn_save").removeAttr("disabled");
					$("#btn_save_audit").removeAttr("disabled");
				}
			},
			error : function(data)
			{
				Helper.message.suc(data);
				$("#btn_save").removeAttr("disabled");
				$("#btn_save_audit").removeAttr("disabled");
			}
		});
	}
	//计算加工金额
	function calcMoney(obj)
	{
		var productType = $(obj).parent().parent().find("input[name='detailList.productType']").val();
		var workProcedureType = $(obj).parent().parent().find("input[name='detailList.workProcedureType']").val();
		var qty_dom = $(obj).parent().parent().find("input[name='detailList.qty']");

		if (productType == 'ROTARY' && workProcedureType == 'PART')
		{
			qty_dom.val(Number(qty_dom.val()).toFixed(2));
		} else
		{
			qty_dom.val(Number(qty_dom.val()).trunc());
		}
		var price_dom = $(obj).parent().parent().find("input[name='detailList.price']");
		var money_dom = $(obj).parent().parent().find("input[name='detailList.money']");

		money_dom.val(Number(price_dom.val()).mul(Number(qty_dom.val())).tomoney());//金额=(单价*加工数量)

		calcTaxRate(obj);
	}
	//计算加工单价 （输入金额）
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
	//计算税额
	function calcTaxRate(obj)
	{
		//获取金额对象并格式化
		var money_dom = $(obj).parent().parent().find("input[name='detailList.money']");
		var money_value = Number(money_dom.val()).tomoney();//金额
		money_dom.val(money_value);

		var taxRatePercent = Number($(obj).parent().parent().find("input[name='detailList.taxRatePercent']").val());//税率值
		//不含税金额计算
		var noTaxMoney_dom = $(obj).parent().parent().find("input[name='detailList.noTaxMoney']");
		noTaxMoney_dom.val(Number(money_value).div(Number(1 + (taxRatePercent / 100))).tomoney());//不含税金额=（金额/(1+税率值/100)）

		//税额计算
		var tax_dom = $(obj).parent().parent().find("input[name='detailList.tax']");
		tax_dom.val(money_value.subtr(Number(noTaxMoney_dom.val())).tomoney());//税额=(金额-不含税金额)

		var qty_value = Number($(obj).parent().parent().find("input[name='detailList.qty']").val());//数量
		//单价计算
		var price_dom = $(obj).parent().parent().find("input[name='detailList.price']");

		//不含税单价计算
		var noTaxPrice_dom = $(obj).parent().parent().find("input[name='detailList.noTaxPrice']");
		noTaxPrice_dom.val(Number(price_dom.val()).div(Number(1 + (taxRatePercent / 100))).toFixed(4));//不含税单价=(单价/(1+税率值/100))

		//汇总
		sum();
	}

	//汇总
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

	// 代工平台供应商返回信息
	function getCallInfo_supplierOem(obj)
	{
		$("#originCompanyId").val(obj.originCompanyId);
		getCallInfo_supplier(obj);
	}

	// 正常供应商返回信息
	function getCallInfo_supplier(obj)
	{
		$("#supplierId").val(obj.id);
		$("#supplierCode").val(obj.code);
		$("#supplierName").val(obj.name);
		if (Helper.isNotEmpty(obj.defaultAddress))
		{
			$("#linkName").val(obj.defaultAddress.userName);
			$("#mobile").val(obj.defaultAddress.mobile);
			$("#supplierAddress").val(obj.defaultAddress.address);
		} else
		{
			$("#linkName").val("");
			$("#mobile").val("");
			$("#supplierAddress").val("");
		}
		$("#deliveryClassId").val(obj.deliveryClassId).trigger("change");
		$("#paymentClassId").val(obj.paymentClassId).trigger("change");
		$("#settlementClassId").val(obj.settlementClassId).trigger("change");
		if (Helper.isNotEmpty(obj.taxRateId))
		{
			$("#rate").val(Helper.basic.info('TAXRATE', obj.taxRateId).id);
			$("#rate").trigger("change");//触发税率改变事件
		}
		if (Helper.isNotEmpty(obj.employeeId) && obj.employeeId > 0)
		{
			$("#employeeName").val(Helper.basic.info('EMPLOYEE', obj.employeeId).name);
		} else
		{
			$("#employeeName").val("");
		}
		$("#employeeId").val(obj.employeeId);
		$("#currencyTypeText").val(Helper.basic.getEnumText('com.huayin.printmanager.persist.enumerate.CurrencyType', obj.currencyType, 'text'));
		$("#currencyType").val(obj.currencyType);
	}

	// 异步获取工艺信息与材料信息
	function appendProceduresMaterials()
	{
		// 获取工单id
		var detailList = [];
		var arr = $("table tbody tr");
		$.each(arr, function(index, item)
		{
			var detail = {};
			detail.id = $(item).find("input[name='detailList.sourceDetailId']").val();
			detail.workId = $(item).find("input[name='detailList.workId']").val();
			$(item).find("input[name='detailList.workId']").val("");
			detailList.push(detail);
		});
		
		$.ajax
		({
		    url : Helper.basePath + "/outsource/process/appendProceduresMaterials",
		    type : "POST",
		    async : true,
		    data : JSON.stringify(detailList),
		    contentType : 'application/json;charset=utf-8',
		    dataType : "json",
		    success : function(data)
		    {
		    	$.each(data, function(index, item)
					{
						var target = $("table tbody tr").find("input[name='detailList.sourceDetailId'][value='" + item.id + "']");
						if (item.workProcedures != "")
						{
							target.parent("tr").find("input[name='detailList.workProcedures']").val(item.workProcedures);
							target.parent("tr").find("input[name='detailList.workProcedures']").parent(".otherView").append(item.workProcedures);
						}
						if (item.workMaterials != "")
						{
							target.parent("tr").find("input[name='detailList.workMaterials']").val(item.workMaterials);
							target.parent("tr").find("input[name='detailList.workMaterials']").parent(".otherView").append(item.workMaterials);
						}
						target.parent("tr").find("input[name='detailList.workProcedures']").parent(".otherView").find(".loading_effect").css("display", "none");
						target.parent("tr").find("input[name='detailList.workMaterials']").parent(".otherView").find(".loading_effect").css("display", "none");
					});
		    	$("#loading_flag").val("true");
		    }
		});
	}
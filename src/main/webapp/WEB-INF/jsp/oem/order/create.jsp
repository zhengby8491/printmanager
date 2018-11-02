<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>创建页面</title>
<style type="text/css">
.select-btn2 {
	position: absolute;
	right: 1px;
	top: -2px;
	width: 26px;
	height: 30px;
	line-height: 16px;
	border-left: 1px solid #ccc;
	text-align: center;
	background-color: #EEF8FF;
	cursor: pointer;
}

.select-btn2:hover {
	background-color: #E0EDF7
}

span.btn2 {
	position: absolute;
	left: 7px;
	top: 3px;
	font-size: large;
}

input.tab {
	text-align: left;
}

.ui-combo-wrap2 {
	position: relative;
	display: block;
	float: left;
	left: -16px;
	height: 25px;
	line-height: 24px;
	box-sizing: content-box;
	-webkit-box-sizing: content-box;
}
</style>
</head>
<script type="text/javascript">
	var hasPermission = Helper.basic.hasPermission('oem:order:money');
	$(function()
	{
		if (!hasPermission)
		{
			$("td").has("input[name='detailList.price']").hide();
			$("td").has("input[name='detailList.money']").hide();
			$("td").has("input[name='detailList.tax']").hide();
			$("td").has("input[name='detailList.noTaxPrice']").hide();
			$("td").has("input[name='detailList.noTaxMoney']").hide();
			$("dd").has("input[name='totalMoney']").hide();
			$("dd").has("input[name='noTaxTotalMoney']").hide();
			$("dd").has("input[name='totalTax']").hide();
		}
		// 取消  
		$("#btn_cancel").click(function()
		{
			closeTabAndJump("代工订单列表");
		});
		// 选择代工平台客户
		$("#customer_quick_oem").click(function()
		{
			Helper.popup.show('选择客户', Helper.basePath + '/quick/customerOem?multiple=false', '900', '500');
		});
		// 选择发外单	
		$("#select_source").click(function()
		{
			var originCompanyId = $("#originCompanyId").val();
			var customerId = $("#customerId").val();
			if (Helper.isEmpty(customerId))
			{
				Helper.message.alert("请先选择客户信息");
				return false;
			}
			Helper.popup.show('选择发外加工单', Helper.basePath + '/quick/oem_source_select?originCompanyId=' + originCompanyId, '910', '500');
		});

		// 增加工序
		$("#select_procedure").click(function()
		{
			Helper.popup.show('选择工序', Helper.basePath + '/quick/procedure_select?multiple=true', '900', '500');
		});

		// 选择工序
		$(".select-btn2").click(function()
		{
			Helper.popup.show('选择工序', Helper.basePath + '/quick/procedure_select?multiple=false&isToOem=true', '900', '500');
		});

		// 删除
		$("#detailList tbody").on("click", "a[name='btn_del']", function()
		{
			$(this).parent().parent().remove();
			resetSequenceNum();
			sum();
		});
		// 初始付款日期
		$("#deliveryTime").val(new Date().format('yyyy-MM-dd'));
		// 初始化制单日期
		$("#createDate").val(new Date().format('yyyy-MM-dd'));
		// 保存
		$("#btn_save,#btn_save_audit").click(function()
		{
			fixEmptyValue();
			if (Helper.isEmpty($("#customerId").val()))
			{
				Helper.message.warn("请选择客户信息");
				return false;
			}
			var validate = true;
			if ($("#detailList input[name='detailList.qty']").size() <= 0)
			{
				Helper.message.warn("请选择工序");
				return;
			}
			var flg = true;
			/* if (validate)
			{
				$("#detailList input[name='detailList.productName']").each(function(){
					if ($(this).val() == "")
					{
						Helper.message.warn("请填写成品名称");
						$(this).focus();
						flg = false;
						validate = false;
						return false;
					}
				});
			} */
			if (validate)
			{
				$("#detailList input[name='detailList.procedureName']").each(function()
				{
					if ($(this).val() == "")
					{
						Helper.message.warn("工序不能为空");
						$(this).focus();
						flg = false;
						validate = false;
						return false;
					}
				});
			}
			if (validate)
			{
				$("#detailList input[name='detailList.qty']").each(function()
				{
					if (Number($(this).val()) <= 0)
					{
						Helper.message.warn("产品数量必须大于0");
						$(this).focus();
						flg = false;
						validate = false;
						return false;
					}
				});
			}
			if (validate)
			{
				$("#detailList input[name='detailList.price']").each(function()
				{
					if (Number($(this).val()) <= 0)
					{
						Helper.message.warn("单价必须大于0");
						$(this).focus();
						flg = false;
						validate = false;
						return false;
					}
				});
			}
			if (validate)
			{
				$("#detailList select[name='detailList.taxRateId']").each(function()
				{
					if (Number($(this).val()) <= 0)
					{
						Helper.message.warn("请选择税收");
						flg = false;
						return false;
					}
				});
			}
			if (flg)
			{
				$("#isCheck").val($(this).attr("id") == "btn_save_audit" ? "YES" : "NO");
				form_submit();
			}
		});
		// 所有金额改变事件
		$("#detailList tbody").on("blur", "input[name='detailList.money']", function()
		{
			var money_value = Number($(this).val()).tomoney();//控制金额小数位，保留2位
			$(this).val(money_value);
			calcPrice(this);
		});
		// 所有单价改变事件
		$("#detailList tbody").on("blur", "input[name='detailList.price']", function()
		{
			calcMoney(this);
		});
		// 所有数量改变事件
		$("#detailList tbody").on("input", "input[name='detailList.qty']", function()
		{
			/* var qty_value = Number($(this).val()).trunc();
			$(this).val(qty_value); */
			var saveQty = Number($(this).parent().parent().find("input[name='detailList.saveQty']").val());
			var qty_value = Number($(this).val());
			if ($(this).val() == saveQty)
			{
				var saveMoney = $(this).parent().parent().find("input[name='detailList.saveMoney']").val();
				$(this).parent().parent().find("input[name='detailList.money']").val(saveMoney);
				calcPrice(this);
				return;
			}
			if (qty_value > 0)
			{
				calcMoney(this);
			}
		});
		// 初始化批量修改仓库、税收、交货日期悬浮窗
		$("#batch_edit_taxRate,#batch_edit_deliveryTime").each(function()
		{
			$(this).powerFloat({
				eventType : "click",
				targetAttr : "src",
				reverseSharp : true,
				container : $(this).siblings(".batch_box_container")
			})
		})

		// 明细表中点击工序按钮事件
		$("#detailList tbody").on("click", "div.select-btn2", function()
		{
			$("#detailList tbody").find("td.active").removeClass("active");
			$(this).parent().parent().addClass("active");
		});
		// 初始化完毕后触发明细表中的金额计算
		defaultPercent();
		taxSelectNum();
		trigger();
	})

	// 选择发外源单后拼接tr
	function appendTr(tableId, rows)
	{
		$.each(rows, function()
		{
			var _THIS = this;
			var idArray = $("#detailList tbody tr td input[name='detailList.sourceDetailId']").map(function()
			{
				return this.value
			}).get();
			//判断是否已存在工序ID
			if (Helper.isNotEmpty(idArray) && idArray.contains("" + this.id))
			{//如果已存在跳出本次循环
				return true;
			}
			var procedure = []; 
			findProcedureByName(procedure, _THIS.procedureName);
			
			var copyTr = $("#cloneTR").find("tr").clone(true);
			var taxRateId = $("#rateId").val();
			copyTr.find("input[name='detailList.productName']").val(_THIS.productNames);
			copyTr.find("input[name='detailList.productId']").val(_THIS.productId);
			copyTr.find("input[name='detailList.partName']").val(_THIS.partName);
			copyTr.find("input[name='detailList.originProcedureName']").val(_THIS.procedureName);
			copyTr.find("input[name='detailList.originProcedureId']").val(_THIS.procedureId);
			copyTr.find("input[name='detailList.originProcedureClassId']").val(_THIS.procedureClassId);

			copyTr.find("input[name='detailList.originBillNo']").val(_THIS.master.billNo);
			copyTr.find("input[name='detailList.originBillId']").val(_THIS.masterId);
			copyTr.find("input[name='detailList.sourceQty']").val(_THIS.qty);
			copyTr.find("input[name='detailList.sourceId']").val(_THIS.masterId);
			copyTr.find("input[name='detailList.sourceDetailId']").val(_THIS.id);
			copyTr.find("input[name='detailList.sourceBillNo']").val(_THIS.master.billNo);
			copyTr.find("input[name='detailList.sourceBillType']").val(_THIS.master.billType);
			copyTr.find("input[name='detailList.originCompanyId']").val(_THIS.companyId);

			copyTr.find("input[name='detailList.procedureName']").val(procedure.name);
			copyTr.find("input[name='detailList.procedureId']").val(procedure.id);
			copyTr.find("input[name='detailList.procedureClassId']").val(procedure.procedureClassId);
			
			copyTr.find("input[name='detailList.style']").val(_THIS.style);
			copyTr.find("input[name='detailList.soureQty']").val(_THIS.qty);
			copyTr.find("input[name='detailList.produceNum']").val(_THIS.produceNum);
			copyTr.find("input[name='detailList.qty']").val(_THIS.qty);
			copyTr.find("input[name='detailList.saveQty']").val(_THIS.qty);
			//copyTr.find("input[name='detailList.price']").val(_THIS.price);
			copyTr.find("input[name='detailList.money']").val(_THIS.money);
			copyTr.find("input[name='detailList.saveMoney']").val(_THIS.money);
			copyTr.find("select[name='detailList.taxRateId']").val(taxRateId);
			copyTr.find("input[name='detailList.percent']").val(Helper.basic.info("TAXRATE", taxRateId).percent);
			copyTr.find("input[name='detailList.deliveryTime']").val(new Date(_THIS.master.deliveryTime).format("yyyy-MM-dd"));
			copyTr.find("input[name='detailList.processRequire']").val(_THIS.processRequire);
			copyTr.find("input[name='detailList.memo']").val(_THIS.memo);

			$("#" + tableId + " tbody").append(copyTr);
		});
	}
	// 根据客户的源单工序查找是否有该工序
	function findProcedureByName(procedure, procedureName)
	{
		if (procedureName == "" || procedureName == null )
		{
			return;
		}
		
		$.ajax({
			url : Helper.basePath + '/basic/procedure/findProcedureByPrecise',
			data :"{\"procedureName\":\"" + procedureName + "\"}",
			type : "POST",
		  async : false,
		  dataType : "json",
		  contentType : 'application/json;charset=utf-8',
		  success : function (data)
		 	{
		  	procedure['name'] = data.name;
		  	procedure['procedureClassId'] = data.procedureClassId;
		  	procedure['id'] = data.id;
		 	}
		});
	}
	// 增加工序
	function appendTrProcedure(tableId, rows)
	{
		$.each(rows, function(index, obj)
		{
			var _THIS = this;
			var idArray = $("#detailList tbody tr td input[name='detailList.procedureId']").map(function()
			{
				return this.value
			}).get();
			//判断是否已存在工序ID
			if (Helper.isNotEmpty(idArray) && idArray.contains("" + this.id))
			{//如果已存在跳出本次循环
				return true;
			}
			var copyTr = $("#cloneTR").find("tr").clone(true);
			var taxRateId = $("#rateId").val();
			var originCompanyId = $("#originCompanyId").val();
			copyTr.find("input[name='detailList.procedureName']").val(Helper.basic.info("PROCEDURE", _THIS.id).name);
			copyTr.find("input[name='detailList.procedureId']").val(_THIS.id);
			copyTr.find("input[name='detailList.procedureClassId']").val(_THIS.procedureClassId);
			copyTr.find("input[name='detailList.originCompanyId']").val(originCompanyId);
			copyTr.find("select[name='detailList.taxRateId']").val(taxRateId);
			copyTr.find("input[name='detailList.percent']").val(Helper.basic.info("TAXRATE", taxRateId).percent);
			$("#" + tableId + " tbody").append(copyTr);
		});
		resetSequenceNum();
	};

	//改变单个税率  
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
				if (taxRateId > 0)
				{
					$(this).parents("tr").find("input[name='detailList.percent']").val(Helper.basic.info("TAXRATE", taxRateId).percent);
				}
				calcTaxRate(this);
			}
		});
	}
	//重新设置序号  
	function resetSequenceNum()
	{
		$("#detailList tbody tr").each(function(index)
		{
			$(this).find("td").first().html(++index);
		});
	}

	//表单提交
	function form_submit()
	{
		$("#btn_save").attr({
			"disabled" : "disabled"
		});
		$("#btn_save_audit").attr({
			"disabled" : "disabled"
		});

		// 提交到服务器保存
		Helper.request({
			url : Helper.basePath + "/oem/order/save",
			data : $("#form_order").formToJson(),
			success : function(data)
			{
				if (data.success)
				{
					Helper.message.suc('已保存!');
					location.href = Helper.basePath + '/oem/order/view/' + data.obj.id;
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

	//获取返回工序数组信息
	function getCallInfo_procedureArray(rows)
	{
		if (rows.length > 0)
		{
			appendTrProcedure("detailList", rows);
			resetSequenceNum();
		}
		//defaultPercent();
		taxSelectNum();
	}

	// 获取返回工序信息，填充到明细表中的工序名称
	function getCallInfo_procedure_oem(row)
	{
		if (row != null)
		{
			var percent = Helper.basic.info('TAXRATE', $("#rateId").val()).percent;
			$("td.active").find("input[name='detailList.procedureName']").val(row.name);
			$("td.active").find("input[name='detailList.procedureId']").val(row.id);
			$("td.active").find("input[name='detailList.procedureClassId']").val(row.procedureClassId);
			$("td.active").prop("title", row.name);
		}
	}
	// 代工平添客户返回信息
	function getCallInfo_customerOem(obj)
	{
		var old_customerId = $("#customerId").val();
		$("#customerName").val(obj.name);
		$("#customerId").val(obj.id);
		$("#originCompanyId").val(obj.originCompanyId);
		if (Helper.isNotEmpty(obj.defaultAddress))
		{
			$("#linkName").val(obj.defaultAddress.userName);
			$("#mobile").val(obj.defaultAddress.mobile);
			$("#deliveryAddress").val(obj.defaultAddress.address);
		}
		$("#deliveryClassId").val(obj.deliveryClassId).trigger("change");
		$("#paymentClassId").val(obj.paymentClassId).trigger("change");
		if (Helper.isNotEmpty(obj.taxRateId))
		{
			$("#rateId").val(Helper.basic.info('TAXRATE', obj.taxRateId).id);
		}
		$("#rateId").trigger("change");
		$("#currencyTypeText").val(Helper.basic.getEnumText('com.huayin.printmanager.persist.enumerate.CurrencyType', obj.currencyType, 'text'));
		$("#currencyType").val(obj.currencyType);
		if (Helper.isNotEmpty(old_customerId) && obj.id != old_customerId)
		{//如果改变供应商，则清空table
			//清空table tr
			$("#detailList tbody tr").remove();
		}
		if (Helper.isNotEmpty(obj.employeeId) && obj.employeeId > 0)// 避免选中自定义或者空选项时js报错
		{
			$("#employeeName").val(Helper.basic.info('EMPLOYEE', obj.employeeId).name);
		} else
		{
			$("#employeeName").val("");
		}
		$("#employeeId").val(obj.employeeId);
		sum();
	}
	// 选择发外加工单后，返回代加工单信息
	function getCallInfo_oemOrder(rows)
	{
		if (rows.length > 0)
		{
			appendTr("detailList", rows);
			resetSequenceNum();
		}
		//defaultPercent();
		taxSelectNum();
		trigger();
	}

	// 触发金额计算
	function trigger()
	{
		$("input[name='detailList.money']").trigger("blur");
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

	//计算金额
	function calcMoney(obj)
	{
		var qty_dom = $(obj).parent().parent().find("input[name='detailList.qty']");
		var price_dom = $(obj).parent().parent().find("input[name='detailList.price']");
		var money_dom = $(obj).parent().parent().find("input[name='detailList.money']");

		money_dom.val(Number(price_dom.val()).mul(Number(qty_dom.val())).tomoney());//金额=(单价*数量)

		calcTaxRate(obj);
	}
	//计算税额
	function calcTaxRate(obj)
	{
		//获取金额对象并格式化
		var money_dom = $(obj).parent().parent().find("input[name='detailList.money']");

		var money_value = Number(money_dom.val()).tomoney();//金额

		var taxRatePercent = Number($(obj).parent().parent().find("input[name='detailList.percent']").val());//税率值
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
		var sum_qty = 0;
		var sum_tax = 0;
		var sum_noTaxMoney = 0;
		var sum_money = 0;
		$("#detailList tbody tr").each(function()
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

	//批量修改税收
	function batchEditTaxRate()
	{
		var taxRateId = $(".batch_taxRate_select").val();
		if (taxRateId == -1)
		{
			shotCutWindow('TAXRATE', true, $(".batch_taxRate_select"), '', "TAXRATE");
		} else
		{
			$("#detailList select[name='detailList.taxRateId']").val(taxRateId);
			if (taxRateId > 0)
			{
				$("input[name='detailList.percent']").val(Helper.basic.info("TAXRATE", taxRateId).percent);
			} else
			{
				$("input[name='detailList.percent']").val(0);
			}
			$("#detailList select[name='detailList.taxRateId']").trigger("change");
		}
	}
	//批量修改交货日期
	function batchEditDeliveryTime()
	{
		$("#detailList input[name='detailList.deliveryTime']").val($(".batch_deliveryTime_input").val());
	}
	// 税率默认值
	function defaultPercent()
	{
		var taxRateId = $("#rateId").val();
		$("select[name='detailList.taxRateId']").val(taxRateId);
		if (taxRateId > 0) // 避免选中自定义或者空选项时js报错
		{
			$("input[name='detailList.percent']").val(Helper.basic.info("TAXRATE", taxRateId).percent);
		} else
		{
			$("input[name='detailList.percent']").val(0);
		}
		//$("input[name='detailList.deliveryTime']").val($("#deliveryTime").val());
	}
	//规格失去焦点判断
	/* $(document).on('blur', "input[name='detailList.style']", function()
	{

		if (!Helper.validata.isMaterialSize($(this).val()))
		{
			Helper.message.warn("请录入正确的规格格式xxx或xxx*xxx或xxx*xxx*xxx (小数最多4位)");

			$(this).val("");
		}
	}); */
</script>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="代工管理-代工订单-创建"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="oem:order:create">
						<button class="nav_btn table_nav_btn" id="btn_save">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="oem:order:create,oem:order:audit">
						<button class="nav_btn table_nav_btn" id="btn_save_audit">保存并审核</button>
					</shiro:hasManyPermissions>
					<button class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
				</div>
			</div>
			<form id="form_order">
				<!--主表-订单表单-->
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl">
								<input type="hidden" name="customerId" id="customerId" value="${order.customerId }" />
								<input type="hidden" name="originCompanyId" id="originCompanyId" value="${order.originCompanyId }" />
								<input type="hidden" id="index" />
								<input type="hidden" name="isCheck" id="isCheck" />
								<dd class="row-dd">
									<label class="form-label label_ui label_1">客户名称：</label>
									<span class="ui-combo-wrap wrap-width">
										<input type="text" class="input-txt input-txt_7" name="customerName" id="customerName" readonly="readonly" value="${order.customerName =='-'?'':order.customerName }" />
										<div class="select-btn" id="customer_quick_oem">...</div>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">联 系 人：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_3" name="linkName" id="linkName" value="${order.linkName}" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">联系电话：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_3" name="mobile" id="mobile" value="${order.mobile }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">销售员：</label>
									<span class="ui-combo-wrap">
										<input type="hidden" id="employeeId" name="employeeId" class="input-txt input-txt_1" value="${order.employeeId}" />
										<input type="text" class="input-txt input-txt_3" readonly name="employeeName" id="employeeName" value="${order.employeeName}" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">送货地址：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_22" name="deliveryAddress" id="deliveryAddress" value="${order.deliveryAddress }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">送货方式：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('DELIVERYCLASS')}" valueProperty="id" defaultValue="" defaultOption="请选择" selected="${order.deliveryClassId }" name="deliveryClassId" textProperty="name" cssClass="nput-txt input-txt_3 hy_select2" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">付款方式：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('PAYMENTCLASS')}" valueProperty="id" defaultValue="" defaultOption="请选择" selected="${order.paymentClassId }" name="paymentClassId" textProperty="name" cssClass="input-txt input-txt_3 hy_select2"></phtml:list>
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">备 注：</label>
									<span class="form_textarea">
										<textarea name="memo" id="memo" class="noborder" style="width: 954px" placeholder="说点什么...100个字符以内" onKeyUp="Helper.doms.textarealength(this,100)">${order.memo }</textarea>
										<p class="textarea-numberbar">
											<em>0</em>
											/100
										</p>
									</span>
								</dd>
							</dl>
						</div>
					</div>
				</div>
				<!-- 按钮栏Start -->
				<div class="btn-billbar" id="toolbar">
					<a id="select_source" class="nav_btn table_nav_btn">
						<i class="fa fa-plus-square"></i>
						选择发外加工单
					</a>
					<a id="select_procedure" class="nav_btn table_nav_btn">
						<i class="fa fa-plus-square"></i>
						选择工序
					</a>
				</div>
				<!--按钮栏End-->
				<!--从表-订单表格-->
				<div>
					<div class="table-container">
						<table class="border-table" style="width: 1450px" id="detailList">
							<thead>
								<tr>
									<th width="30">序号</th>
									<th width="30">操作</th>
									<th width="60">成品名称</th>
									<th width="60">源单工序名称</th>
									<th width="60">工序名称</th>
									<th width="40">加工规格</th>
									<th width="60">源单单号</th>
									<th width="40">源单数量</th>
									<th width="40">生产数量</th>
									<th width="40">加工数量</th>
									<shiro:hasPermission name="oem:order:money">
									<th width="30">单价</th>
									<th width="30">金额</th>
									<th width="30">税额</th>
									</shiro:hasPermission>
									<th width="50">
										税收
										<i id="batch_edit_taxRate" class="fa fa-edit" src="batch_taxRate_box"></i>
										<div class="batch_box_container"></div>
									</th>
									<th width="70"style="display: none">税率值</th>
									<th width="55">
										交货日期
										<i id="batch_edit_deliveryTime" class="fa fa-edit" src="batch_deliveryTime_box"></i>
										<div class="batch_box_container"></div>
									</th>
									<shiro:hasPermission name="oem:order:money">
									<th width="40">不含税单价</th>
									<th width="40">不含税金额</th>
									</shiro:hasPermission>
									<th width="40">部件名称</th>
									<th width="60 ">加工要求</th>
									<th width="40 ">备注</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="detail" items="${order.detailList}" varStatus="status">
									<tr name="tr">
										<td>${status.count}</td>
										<td class="td-manage">
											<input name="detailList.id" type="hidden" readonly="readonly" value="${detail.id }" />
											<input name="detailList.productId" type="hidden" readonly="readonly" value="${detail.productId }" />
											<a title="删除行" href="javascript:void(0)" name="btn_del">
												<i class="delete fa fa-trash-o"></i>
											</a>
										</td>
										<td>
											<input class="tab_input bg_color" type="text" name="detailList.productName" value="${detail.productName }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.originProcedureName" readonly="readonly" value="${detail.originProcedureName }" />
											<input type="hidden" name="detailList.originProcedureId" readonly="readonly" value="${detail.originProcedureId }" />
											<input type="hidden" name="detailList.originCompanyId" readonly="readonly" value="${detail.originCompanyId }" />
											<input type="hidden" name="detailList.originBillNo" readonly="readonly" value="${detail.originBillNo }" />
											<input type="hidden" name="detailList.originBillId" readonly="readonly" value="${detail.originBillId }" />
										</td>
										<td>
											<span class="ui-combo-wrap2">
												<input class="tab_input" type="text" name="detailList.procedureName" readonly="readonly" value="${detail.procedureName }" />
											</span>
											<span class="ui-combo-wrap">
												<input type="hidden" name="detailList.procedureId" readonly="readonly" value="${detail.procedureId }" />
												<input type="hidden" name="detailList.procedureClassId" readonly="readonly" value="${detail.procedureClassId }" />
												<div class="select-btn2">
													<span class="btn2">...</span>
												</div>
											</span>
										</td>
										<td>
											<input class="tab_input bg_color" type="text" name="detailList.style" value="${detail.style }" />
										</td>
										<td>
											<input type="text" class="tab_input" name="detailList.sourceBillNo" readonly="readonly" value="${detail.sourceBillNo }" />
											<input type="hidden" name="detailList.sourceId" readonly="readonly" value="${detail.sourceId }" />
											<input type="hidden" name="detailList.sourceDetailId" readonly="readonly" value="${detail.sourceDetailId }" />
											<input type="hidden" name="detailList.sourceBillType" readonly="readonly" value="${detail.sourceBillType }" />
										</td>
										<td>
											<input type="text" class="tab_input" name="detailList.sourceQty" readonly="readonly" value="${detail.sourceQty }" />
										</td>
										<td>
											<input type="text" class="tab_input" name="detailList.produceNum" readonly="readonly" value="${detail.produceNum }" />
										</td>
										<td>
											<input class="tab_input bg_color constraint_decimal_negative" type="text" name="detailList.qty" value="${detail.qty }" />
											<input type="hidden" name="detailList.saveQty" value="${detail.qty }" />
										</td>
										<td>
											<input class="tab_input bg_color constraint_decimal_negative" type="text" name="detailList.price" value="${detail.price }" />
										</td>
										<td>
											<input class="tab_input bg_color constraint_decimal_negative" type="text" name="detailList.money" value="${detail.money }" />
											<input type="hidden" name="detailList.saveMoney" value="${detail.money }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.tax" readonly="readonly" value="${detail.tax }" />
										</td>
										<td>
											<phtml:list items="${fns:basicList2('TAXRATE')}" cssClass="tab_input bg_color" valueProperty="id" textProperty="name" name="detailList.taxRateId" selected="${detail.taxRateId }"/>
										</td>
										<td style="display: none">
											<input class="tab_input" type="text" name="detailList.percent" readonly="readonly" value="${detail.percent }" />
										</td>
										<td>
											<input class="tab_input bg_color" type="text" readonly="readonly" name="detailList.deliveryTime" onFocus="WdatePicker({lang:'zh-cn',minDate: '%y-%M-%d' })" 
												value="<fmt:formatDate value="${detail.deliveryTime}" type="date" pattern="yyyy-MM-dd" />" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.noTaxPrice" readonly="readonly" value="${detail.noTaxPrice }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.noTaxMoney" readonly="readonly" value="${detail.noTaxMoney }" />
										</td>
										<td>
											<input type="text" class="tab_input" name="detailList.partName" readonly="readonly" value="${detail.partName }" />
										</td>
										<td>
											<input class="tab_input bg_color memo" type="text" name="detailList.processRequire" value="${detail.processRequire }" />
										</td>
										<td>
											<input class="tab_input bg_color memo" type="text" onmouseover="this.title=this.value" name="detailList.memo" value="${detail.memo }" />
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
				<div class="cl form_content">
					<dl class="cl row-dl-foot">

						<dd class="row-dd">
							<label class="form-label label_ui label_7">金 额：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" name="totalMoney" id="totalMoney" class="input-txt input-txt_3" readonly value="${order.totalMoney }" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">金额(不含税)：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" name="noTaxTotalMoney" id="noTaxTotalMoney" class="input-txt input-txt_3" readonly value="${order.noTaxTotalMoney }" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">税 额：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" name="totalTax" id="totalTax" class="input-txt input-txt_3" readonly value="${order.totalTax }" />
							</span>
						</dd>

						<dd class="row-dd">
							<label class="form-label label_ui label_7">币 种：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input id="currencyType" name="currencyType" type="hidden" value="${customer.currencyType}" />
								<input id="currencyTypeText" type="text" class="input-txt input-txt_3" readonly="readonly" value="${customer.currencyType.text }" />
							</span>
						</dd>
					</dl>
					<dl class="cl row-dl-foot">
						<dd class="row-dd">
							<label class="form-label label_ui label_7">制 单 人：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="createName" type="text" class="input-txt input-txt_3" readonly />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">制单日期：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="createTime" type="text" class="input-txt input-txt_3" readonly="readonly" id="createDate" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">审 核 人：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="checkUserName" type="text" class="input-txt input-txt_3" readonly />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">审核日期：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="checkTime" type="text" class="input-txt input-txt_3" readonly />
							</span>
						</dd>
					</dl>
				</div>
				<!-- 批量悬浮窗 satrt -->
				<div id="batch_deliveryTime_box" class="batch_deliveryTime_box">
					<input type="text" class="input-txt input-txt_8 batch_deliveryTime_input" name="" value="" readonly="true" onFocus="WdatePicker({onpicked:batchEditDeliveryTime}) " />
				</div>
				<div id="batch_taxRate_box" class="batch_taxRate_box">
					<phtml:list items="${fns:basicList2('TAXRATE')}" cssClass="tab_input bg_color  input-txt_3 batch_taxRate_select" valueProperty="id" textProperty="name" name="rateId" selected="${customer.taxRateId }" onchange="batchEditTaxRate()" />
				</div>
				<!-- 批量悬浮窗 end -->
			</form>
		</div>
	</div>
	<div id="cloneTR" style="display: none;">
		<table>
			<tr>
				<!-- 序号 -->
				<td></td>
				<!-- 操作 -->
				<td>
					<a title="删除行" href="javascript:void(0)" name="btn_del">
						<i class="delete fa fa-trash-o"></i>
					</a>
				</td>
				<!-- 成品名称  -->
				<td>
					<input class="tab_input bg_color" type="text" name="detailList.productName" value="" />
					<input class="tab_input" type="hidden" name="detailList.productId" readonly="readonly" value="" />
				</td>
				<!-- 源单工序名称 -->
				<td>
					<input class="tab_input" type="text" name="detailList.originProcedureName" readonly="readonly" value="" />
					<input type="hidden" name="detailList.originProcedureId" readonly="readonly" value="" />
					<input type="hidden" name="detailList.originCompanyId" readonly="readonly" value="" />
					<input type="hidden" name="detailList.originBillNo" readonly="readonly" value="" />
					<input type="hidden" name="detailList.originBillId" readonly="readonly" value="" />
				</td>
				<!-- 工序名称 -->
				<td>
					<span class="ui-combo-wrap2">
						<input class="tab_input" type="text" name="detailList.procedureName" readonly="readonly" value="" />
					</span>
					<span class="ui-combo-wrap">
						<input type="hidden" name="detailList.procedureId" readonly="readonly" value="" />
						<input type="hidden" name="detailList.procedureClassId" readonly="readonly" value="" />
						<div class="select-btn2">
							<span class="btn2">...</span>
						</div>
					</span>
				</td>
				<!-- 规格 -->
				<td>
					<input class="tab_input bg_color" type="text" name="detailList.style" value="" />
				</td>
				<!-- 源单单号 -->
				<td>
					<input class="tab_input" type="text" name="detailList.sourceBillNo" readonly="readonly" value="" />
					<input type="hidden" name="detailList.sourceId" readonly="readonly" value="" />
					<input type="hidden" name="detailList.sourceDetailId" readonly="readonly" value="" />
					<input type="hidden" name="detailList.sourceBillType" readonly="readonly" value="" />
				</td>
				<!-- 源单数量 -->
				<td>
					<input class="tab_input" type="text" name="detailList.sourceQty" readonly="readonly" value="" />
				</td>
				<!-- 生产数量 -->
				<td>
					<input class="tab_input" type="text" name="detailList.produceNum" readonly="readonly" value="" />
				</td>
				<!-- 数量 -->
				<td>
					<input class="tab_input bg_color constraint_decimal_negative" type="text" name="detailList.qty" value="0" />
					<input type="hidden" name="detailList.saveQty" value="" />
				</td>
				<!-- 单价 -->
				<td>
					<input class="tab_input bg_color constraint_decimal_negative" type="text" name="detailList.price" value="0" />
				</td>
				<!-- 金额 -->
				<td>
					<input class="tab_input bg_color  constraint_decimal" type="text" name="detailList.money" value="0" />
					<input type="hidden" name="detailList.saveMoney" value="" />
				</td>
				<!-- 税额 -->
				<td>
					<input class="tab_input" type="text" name="detailList.tax" readonly="readonly" value="" />
				</td>
				<!-- 税收 -->
				<td>
					<phtml:list items="${fns:basicList2('TAXRATE')}" cssClass="tab_input bg_color" valueProperty="id" textProperty="name" name="detailList.taxRateId"  />
				</td>
				<td style="display: none">
					<input class="tab_input" type="text" name="detailList.percent" readonly="readonly" value="" />
				</td>
				<!-- 交货日期 -->
				<td>
					<input class="tab_input bg_color" type="text" name="detailList.deliveryTime" readonly="readonly" onFocus="WdatePicker({lang:'zh-cn',minDate: '%y-%M-%d' })" value="<fmt:formatDate value="${now}" pattern="yyyy-MM-dd" />" />
				</td>
				<!-- 不含税单价 -->
				<td>
					<input class="tab_input" type="text" name="detailList.noTaxPrice" readonly="readonly" value="" />
				</td>
				<!-- 不含税金额 -->
				<td>
					<input class="tab_input" type="text" name="detailList.noTaxMoney" readonly="readonly" value="" />
				</td>
				<!-- 部件名称 -->
				<td>
					<input class="tab_input" type="text" name="detailList.partName" readonly="readonly" value="" />
				</td>
				<!-- 加工要求 -->
				<td>
					<input class="tab_input bg_color memo" type="text" name="detailList.processRequire" value="" />
				</td>
				<!-- 备注 -->
				<td>
					<input class="tab_input bg_color memo" type="text" name="detailList.memo" value="" />
				</td>
			</tr>
		</table>
	</div>
</body>
</html>
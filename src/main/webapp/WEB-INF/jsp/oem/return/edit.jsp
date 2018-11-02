<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>编辑退货单</title>
<script type="text/javascript">
	var hasPermission = Helper.basic.hasPermission('oem:return:money');
	$(function()
	{
		if (!hasPermission)
		{
			$("#detailList").find("th[name='price']").hide();
			$("#detailList").find("th[name='money']").hide();
			$("#detailList").find("th[name='tax']").hide();
			$("td").has("input[name='detailList.price']").hide();
			$("td").has("input[name='detailList.money']").hide();
			$("td").has("input[name='detailList.tax']").hide();
			$("dd").has("input[name='totalMoney']").hide();
			$("dd").has("input[name='noTaxTotalMoney']").hide();
			$("dd").has("input[name='totalTax']").hide();
		}
		//取消
		$("#btn_cancel").click(function()
		{
			location.href = Helper.basePath + '/oem/return/view/' + $("#order_id").val();
		});
		/* 选择客户 */
		$("#selectCustomer").click(function()
		{
			Helper.popup.show('选择客户', Helper.basePath + '/quick/customer_select', '900', '490');
		});
		/* 选择退货源 */
		$("#select_deliver").click(function()
		{
			var _customerId = $("#customerId").val();
			if (Helper.isEmpty(_customerId))
			{
				Helper.message.alert("请先选择客户信息");
				return false;
			}
			Helper.popup.show('选择退货源', Helper.basePath + '/oem/deliver/quick_select?multiple=true&rowIndex=1&customerId=' + _customerId, '800', '490');
		});

		/* 删除 */
		$("table tbody").on("click", "a[name='btn_del']", function()
		{
			$(this).parent().parent().remove();
			resetSequenceNum();
			sum();
		});

		// 初始付款日期
		$("#deliveryTime").val(new Date().format('yyyy-MM-dd'));

		//保存
		$("#btn_save,#btn_save_audit").click(function()
		{
			fixEmptyValue();
			if (Helper.isEmpty($("#customerId").val()))
			{
				Helper.message.warn("请选择客户信息")
				return false;
			}

			var flg = true;
			if ($("input[name='detailList.qty']").size() <= 0)
			{
				Helper.message.warn("请选择送货单");
				validate = false;
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
			if (flg)
			{
				$(this).attr("disabled", "true");
				$("#isCheck").val($(this).attr("id") == "btn_save_audit" ? "YES" : "NO");
				form_submit();
			}
		})
		// 所有金额改变事件
		$("#detailList tbody").on("blur", "input[name='detailList.money']", function()
		{
			//var money_value = Number($(this).val()).tomoney();
			//$(this).val(money_value);
			calcPrice(this);
		});
		//所有数量改变事件
		$("table tbody").on("input", "input[name='detailList.qty']", function()
		{
			var qty_value = Number($(this).val());
			var saveQty = Number($(this).parent().parent().find("input[name='detailList.saveQty']").val());
			if ($(this).val() == saveQty)
			{
				var saveMoney = $(this).parent().parent().find("input[name='detailList.saveMoney']").val();
				$(this).parent().parent().find("input[name='detailList.money']").val(saveMoney);
				calcPrice(this);
				return;
			}
			if (qty_value < 0)
			{
				$(this).val(0 - qty_value);
			}
			calcMoney(this);

		});
		//初始化批量修改仓库、税收、交货日期悬浮窗
		$("#batch_edit_taxRate,#batch_edit_deliveryTime").each(function()
		{
			$(this).powerFloat({
				eventType : "click",
				targetAttr : "src",
				reverseSharp : true,
				container : $(this).siblings(".batch_box_container")
			})
		})

		sum();
		defaultPercent();
		taxSelectNum();
		formatterPrice();
	});
	//当页面渲染完毕后 ，重新算一遍数据，以免多批次送货时税额计算错误
	function trigger()
	{
		$("input[name='detailList.money']").trigger("blur");
	}
	//默认值
	function defaultPercent()
	{

	}
	//批量修改税收
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
	//改变单个税率  
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
	//格式化单价
	function formatterPrice()
	{
		for (var i = 0; i < $("input[name='detailList.price']").length; i++)
		{
			var price = $("input[name='detailList.price']").eq(i).val();
			$("input[name='detailList.price']").eq(i).val(Number(price));
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
			url : Helper.basePath + "/oem/return/update",
			data : $("#form_order").formToJson(),
			success : function(data)
			{
				if (data.success)
				{
					Helper.message.suc('已保存!');
					location.href = Helper.basePath + '/oem/return/view/' + $("#order_id").val();
				} else
				{
					Helper.message.warn(data.message);
					$("#btn_save").removeAttr("disabled");
					$("#btn_save_audit").removeAttr("disabled");
				}
			},
			error : function(data)
			{
				//console.log(data);
				$("#btn_save").removeAttr("disabled");
				$("#btn_save_audit").removeAttr("disabled");
			}
		});
	}
	
	function getCallInfo_deliverSingle_dbl(rows)
	{
		var array = new Array();
		array[0] = rows;
		getCallInfo_deliverSingle(array);
	}
	//获取返回产品数组信息
	function getCallInfo_deliverSingle(rows)
	{
		if (rows.length > 0)
		{
			appendTr("detailList", rows);
			resetSequenceNum();
		}
		sum();
		defaultPercent();
		taxSelectNum();
		trigger();
	}
	//获取返回客户信息
	function getCallInfo_customer(obj)
	{
		var old_customerId = $("#customerId").val();
		$("#customerCode").val(obj.code);
		$("#customerName").val(obj.name);
		$("#customerId").val(obj.id);
		if (Helper.isNotEmpty(obj.defaultAddress))
		{
			$("#linkName").val(obj.defaultAddress.userName);
			$("#mobile").val(obj.defaultAddress.mobile);
			$("#deliveryAddress").val(obj.defaultAddress.address);
		} else
		{
			$("#linkName").val("");
			$("#mobile").val("");
			$("#deliveryAddress").val("");
		}
		$("#deliveryClassId").val(obj.deliveryClassId).trigger("change");
		$("#paymentClassId").val(obj.paymentClassId).trigger("change");
		if (Helper.isNotEmpty(obj.taxRateId))
		{
			$("#rateId").val(Helper.basic.info('TAXRATE', obj.taxRateId).id).trigger("change");
		}
		$("#currencyType").val(obj.currencyType).trigger("change");
		if (Helper.isNotEmpty(old_customerId) && obj.id != old_customerId)
		{//如果改变供应商，则清空table
			//清空table tr
			$("table tbody tr").remove();
		}
		sum();
	}

	function appendTr(tableId, rows)
	{
		$.each(rows, function()
		{
			var _THIS = this;
			var _TR = $("<tr/>");
			var idArray = $("table tbody tr td input[name='detailList.sourceDetailId']").map(function()
			{
				return this.value
			}).get();

			//判断是否已存在客户ID
			if (Helper.isNotEmpty(idArray) && idArray.contains("" + this.id))
			{//如果已存在则跳过本次循环
				return true;//continue;	
			}
			$("#" + tableId).find("thead tr th").each(
					function()
					{
						var name = $(this).attr("name");
						var value = eval("_THIS." + name);
						value = value == undefined ? "" : value;
						switch (name)
						{
						case 'seq':
							_TR.append('<td></td>');
							if (!hasPermission)
							{
								_TR.append('<td style="display:none;"><input class="tab_input" readonly="readonly" type="text" name="detailList.price" value="'+value+'"/>' 
									  + '<input class="tab_input" type="text" readonly="readonly" name="detailList.money" value="' + ((_THIS.money - _THIS.returnMoney - _THIS.reconcilMoney).toFixed(2) || 0) + '"/>' 
										+ '<input class="tab_input" type="text" name="detailList.tax" readonly="readonly" value="'+value+'"/>')
							}
							break;
						case 'operator':
							_TR.append('<td class="td-manage"><input name="detailList.productId" type="hidden" readonly="readonly" value="'+_THIS.productId+
										 '"/><a title="删除行" href="javascript:void(0)" name="btn_del"><i class="delete fa fa-trash-o"></i></a></td>');
							break;
						case 'productName':
							_TR.append('<td><input name="detailList.productName" class="tab_input" readonly="readonly" type="text" value="'+value+'"/></td>');
							break;
						case 'procedureName':
							_TR.append('<td><input name="detailList.procedureName" class="tab_input" readonly="readonly" type="text" value="'+value+'"/>' + 
										'<input name="detailList.procedureId" type="hidden" value="'+ _THIS.procedureId +'"></td>');
							break;
						case 'style':
							_TR.append('<td><input name="detailList.style" class="tab_input" readonly="readonly" type="text" value="'+value+'"/></td>');
							break;
						case 'sourceQty':
							_TR.append('<td><input class="tab_input" type="text" name="detailList.sourceQty" readonly="readonly" value="'+_THIS.qty+'"/></td>');
							break;
						case 'billNo':
							_TR.append('<td><input class="tab_input" type="hidden" name="detailList.sourceDetailId" value="'+_THIS.id+'"/>' 
									+ '<input class="tab_input" type="hidden" name="detailList.sourceId" value="'+_THIS.masterId+'"/>' 
									+ '<input class="tab_input" type="hidden" name="detailList.sourceBillType" value="'+_THIS.master.billType+'"/>'
									+ '<input class="tab_input" type="text" name="detailList.sourceBillNo" readonly="readonly" value="'+_THIS.master.billNo+'"/>'
									+ '<input type="hidden" name="detailList.originCompanyId" readonly="readonly" value="'+_THIS.originCompanyId+'"/>'
									+ '<input type="hidden" name="detailList.originBillNo" readonly="readonly" value="'+_THIS.originBillNo+'"/>'
									+ '<input type="hidden" name="detailList.originBillId" readonly="readonly" value="'+_THIS.originBillId+'"/>'
									+ '<input type="hidden" name="detailList.originProcedureId" readonly="readonly" value="'+_THIS.originProcedureId+'"/></td>');
							break;
						case 'oemOrderBillNo':
							_TR.append('<td><input class="tab_input" type="text" name="detailList.oemOrderBillNo" readonly="readonly" value="'+value+'"/>'
									+ '<input class="tab_input" type="text" name="detailList.oemOrderBillId" readonly="readonly" value="'+_THIS.oemOrderBillId +'" /></td>');
							break;
						case 'qty':
							_TR.append('<td><input class="tab_input bg_color constraint_decimal_negative" type="text" name="detailList.qty" value="' + ((_THIS.qty - _THIS.returnQty - _THIS.reconcilQty).toFixed(2) || 0) + '"/>' + 
										'<input type="hidden" name="detailList.saveQty" value="' + ((_THIS.qty - _THIS.returnQty - _THIS.reconcilQty).toFixed(2) || 0) + '"/></td>');
							break;
						case 'reconcilQty':
							_TR.append('<td style="display: none"><input class="tab_input bg_color" type="text" name="detailList.reconcilQty" value="'+value+'"/></td>');
							break;
						case 'price':
							_TR.append('<td><input class="tab_input" readonly="readonly" type="text" name="detailList.price" value=""/></td>');
							break;
						case 'money':
							_TR.append('<td><input class="tab_input" type="text" readonly="readonly" name="detailList.money" value="' + ((_THIS.money - _THIS.returnMoney - _THIS.reconcilMoney).toFixed(2) || 0) + '"/>' + 
										'<input type="hidden" name="detailList.saveMoney" value="' + ((_THIS.money - _THIS.returnMoney - _THIS.reconcilMoney).toFixed(2) || 0) + '"/></td>');
							break;
						case 'taxRateId':
							_TR.append('<td><input class="tab_input" type="text" name="detailList.taxRateName" value="' + Helper.basic.info("TAXRATE", value).name + '" readonly="readonly"/><input name="detailList.taxRateId" type="hidden" value="'+value+'"></td>');
							break;
						case 'percent':
							_TR.append('<td style="display: none"><input class="tab_input" type="text" name="detailList.percent" readonly="readonly" value="'+value+'"/></td>');
							break;
						case 'tax':
							_TR.append('<td><input class="tab_input" type="text" name="detailList.tax" readonly="readonly" value="'+value+'"/></td>');
							break;
						case 'noTaxPrice':
							_TR.append('<td style="display: none"><input class="tab_input" type="text" name="detailList.noTaxPrice" readonly="readonly" value="'+value+'"/></td>');
							break;
						case 'noTaxMoney':
							_TR.append('<td style="display: none"><input class="tab_input" type="text" name="detailList.noTaxMoney" readonly="readonly" value="'+value+'"/></td>');
							break;
						case 'partName':
							_TR.append('<td><input class="tab_input" type="text" name="detailList.partName" readonly="readonly" value="'+value+'"/></td>');
							break;
						case 'processRequire':
							_TR.append('<td><input name="detailList.processRequire" class="tab_input bg_color memo" type="text" onmouseover="this.title=this.value" value="'+value+'"/></td>');
							break;
						case 'memo':
							_TR.append('<td><input name="detailList.memo" class="tab_input bg_color memo" type="text" onmouseover="this.title=this.value" value="'+value+'"/></td>');
							break;
						}

					});
			_TR.appendTo("#" + tableId);
		});
	}
	//重新设置序号  
	function resetSequenceNum()
	{
		$("table tbody tr").each(function(index)
		{
			$(this).find("td").first().html(++index);
		});
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
		money_dom.val(money_value);

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
</script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="代工管理-代工退货-编辑"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="oem:return:edit">
						<button class="nav_btn table_nav_btn" id="btn_save">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="oem:return:edit,oem:return:audit">
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

								<dd class="row-dd">
									<label class="form-label label_ui label_1">客户名称：</label>
									<span class="ui-combo-wrap wrap-width">
										<input type="text" readonly="true" class="input-txt input-txt_7" name="customerName" id="customerName" value="${order.customerName }" />
										<input type="hidden" name="id" id="order_id" value="${order.id }">
										<input type="hidden" name="customerId" id="customerId" value="${order.customerId }">
										<input type="hidden" name="employeeId" id="employeeId" value="${order.employeeId }">
										<input type="hidden" name="originCompanyId" id="originCompanyId" value="${order.originCompanyId }">
										<input type="hidden" id="index" />
										<input type="hidden" name="isCheck" id="isCheck" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">联 系 人：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_3" name="linkName" id="linkName" value="${order.linkName }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">联系电话：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_3" name="mobile" id="mobile" value="${order.mobile }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">退货类型：</label>
									<span class="ui-combo-wrap">
										<phtml:list name="returnType" textProperty="text" cssClass="input-txt input-txt_3 hy_select2" selected="${order.returnType }" type="com.huayin.printmanager.persist.enumerate.ReturnGoodsType"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">销售员：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_3" readonly="true" value="${order.employeeName}" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">送货地址：</label>
									<span class="ui-combo-wrap wrap_width">
										<input type="text" class="input-txt input-txt_22" name="deliveryAddress" id="deliveryAddress" value="${order.deliveryAddress }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">送货方式：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('DELIVERYCLASS')}" valueProperty="id" defaultValue="" defaultOption="请选择" selected="${order.deliveryClassId }" name="deliveryClassId" textProperty="name" cssClass="nput-txt input-txt_3 hy_select2"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">付款方式：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('PAYMENTCLASS')}" valueProperty="id" defaultValue="" defaultOption="请选择" selected="${order.paymentClassId }" name="paymentClassId" textProperty="name" cssClass="input-txt input-txt_3 hy_select2"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd" style="display: none">
									<label class="form-label label_ui label_1">税 收：</label>
									<span class="ui-combo-wrap" class="form_text">
										<phtml:list items="${fns:basicList2('TAXRATE')}" valueProperty="id" name="rateId" textProperty="name" cssClass="input-txt input-txt_3 hy_select2" onchange="taxSelect();" selected="${order.rateId }"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">退货单号：</label>
									<span class="ui-combo-wrap">
										<input type="text" name="billNo" id="billNo" value="${order.billNo }" readonly="readonly" class="input-txt input-txt_3" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">备注：</label>
									<span class="form_textarea">
										<textarea name="memo" id="memo" class="noborder" style="width: 952px" placeholder="说点什么...100个字符以内" onKeyUp="Helper.doms.textarealength(this,100)">${order.memo}</textarea>
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
					<a id="select_deliver" class="nav_btn table_nav_btn">
						<i class="fa fa-plus-square"></i>
						选择送货单
					</a>
				</div>
				<!--按钮栏End-->
				<!--从表-订单表格-->
				<div>
					<div class="table-container">
						<table class="border-table" style="width: 1600px" id="detailList">
							<thead>
								<tr>
									<th width="40" name="seq">序号</th>
									<th width="40" name="operator">操作</th>
									<th width="120" name="productName">成品名称</th>
									<th width="120" name="procedureName">工序名称</th>
									<th width="100" name="style">加工规格</th>
									<th width="50" name="sourceQty">源单数量</th>
									<th width="100" name="billNo">源单单号</th>
									<th width="100" name="oemOrderBillNo">代工单号</th>
									<th width="80" name="qty">换/退货数量</th>
									<th width="80" name="reconcilQty" style="display: none">已对账数量</th>
									<shiro:hasPermission name="oem:return:money">
									<th width="50" name="price">单价</th>
									<th width="50" name="money">金额</th>
									<th width="50" name="tax">税额</th>
									</shiro:hasPermission>
									<th width="70" name="taxRateId">税收</th>
									<th width="70" name="percent" style="display: none">税率值</th>
									<th width="100" name="noTaxPrice" style="display: none">不含税单价</th>
									<th width="100" name="noTaxMoney" style="display: none">不含税金额</th>
									<th name="partName" width="80">部件名称</th>
									<th name="processRequire" width="100">加工要求</th>
									<th name="memo" width="100">备注</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="detail" items="${order.detailList}" varStatus="status">
									<tr name="tr">
										<input type="hidden" name="detailList.id" value="${detail.id }">
										<td>${status.count}</td>
										<td class="td-manage">
											<input name="detailList.productId" type="hidden" readonly="readonly" value="${detail.productId }" />
											<a title="删除行" href="javascript:void(0)" name="btn_del">
												<i class="delete fa fa-trash-o"></i>
											</a>
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.productName" readonly="readonly" value="${detail.productName }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.procedureName" readonly="readonly" value="${detail.procedureName }" />
											<input type="hidden" name="detailList.procedureId" readonly="readonly" value="${detail.procedureId }"/>
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.style" readonly="readonly" value="${detail.style }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.sourceQty" readonly="readonly" value="${detail.sourceQty }" />
										</td>
										<td>
											<input class="tab_input" type="hidden" name="detailList.sourceDetailId" value="${detail.sourceDetailId }" />
											<input class="tab_input" type="hidden" name="detailList.sourceId" value="${detail.sourceId }" />
											<input class="tab_input" type="hidden" name="detailList.sourceBillType" value="${detail.sourceBillType }" />
											<input class="tab_input" type="text" name="detailList.sourceBillNo" readonly="readonly" value="${detail.sourceBillNo }" />
											<input type="hidden" name="detailList.originCompanyId" readonly="readonly" value="${detail.originCompanyId }"/>
									    <input type="hidden" name="detailList.originBillNo" readonly="readonly" value="${detail.originBillNo }"/>
									    <input type="hidden" name="detailList.originBillId" readonly="readonly" value="${detail.originBillId }"/>
									    <input type="hidden" name="detailList.originProcedureId" readonly="readonly" value="${detail.originProcedureId }"/>
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.oemOrderBillNo" readonly="readonly" value="${detail.oemOrderBillNo }" />
											<input class="tab_input" type="text" name="detailList.oemOrderBillId" readonly="readonly" value="${detail.oemOrderBillId }" />
										</td>
										<td>
											<input class="tab_input bg_color constraint_decimal" type="text" name="detailList.qty" value="${detail.qty }" />
											<input type="hidden" name="detailList.saveQty" value="${detail.qty }" />
										</td>
										<td style="display: none">
											<input class="tab_input" type="text" name="detailList.reconcilQty" readonly="readonly" value="${detail.reconcilQty }" />
										</td>
										<td>
											<input class="tab_input constraint_decimal" type="text" name="detailList.price" value="${detail.price }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.money" value="${detail.money }" readonly="readonly" />
											<input type="hidden" name="detailList.saveMoney" value="${detail.money }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.tax" readonly="readonly" value="${detail.tax }" />
										</td>

										<td>
											<input class="tab_input" type="text" name="detailList.taxRateName" value="${detail.taxRateName }" readonly="readonly" />
											<input name="detailList.taxRateId" type="hidden" value="${detail.taxRateId }">
										</td>
										<td style="display: none">
											<input class="tab_input" type="text" name="detailList.percent" readonly="readonly" value="${detail.percent }" />
										</td>
										<td style="display: none">
											<input class="tab_input" type="text" name="detailList.noTaxPrice" readonly="readonly" value="${detail.noTaxPrice }" />
										</td>
										<td style="display: none">
											<input class="tab_input" type="text" name="detailList.noTaxMoney" readonly="readonly" value="${detail.noTaxMoney }" />
										</td>
										<td>
											<input class="tab_input" type="text" name="detailList.partName" readonly="readonly" value="${detail.partName }" />
										</td>
										<td>
											<input class="tab_input bg_color memo" type="text" onmouseover="this.title=this.value" name="detailList.processRequire" value="${detail.processRequire }" />
										</td>
										<td>
											<input class="tab_input bg_color memo" type="text" onmouseover="this.title=this.value" name="detailList.memo" value="${detail.memo }" />
										</td>
									</tr>
								</c:forEach>
							</tbody>
							<tfoot style="display: none">
								<tr>
									<td>合 计</td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td>
										<input class="tab_input" id="qty" readonly="readonly" />
									</td>
									<td></td>
									<td></td>
									<td></td>
									<td>
										<input class="tab_input" id="money" readonly="readonly" />
									</td>
									<td></td>
									<td></td>
									<td>
										<input class="tab_input" type="text" id="tax" readonly="readonly" />
									</td>
									<td></td>
									<td></td>
									<td></td>
								</tr>
							</tfoot>
						</table>
					</div>
				</div>
				<div class="cl form_content">
					<dl class="cl row-dl-foot">

						<dd class="row-dd">
							<label class="form-label label_ui label_7">金 额：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" name="totalMoney" id="totalMoney" class="input-txt input-txt_3" readonly value="" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">金额(不含税)：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" name="noTaxTotalMoney" id="noTaxTotalMoney" class="input-txt input-txt_3" readonly value="" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">税 额：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" name="totalTax" id="totalTax" class="input-txt input-txt_3" readonly value="" />
							</span>
						</dd>

						<dd class="row-dd">
							<label class="form-label label_ui label_7">币 种：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" class="input-txt input-txt_3" readonly="true" value="${order.currencyType.text }" />
							</span>
						</dd>
					</dl>
					<dl class="cl row-dl-foot">
						<dd class="row-dd">
							<label class="form-label label_ui label_7">制 单 人：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="createName" type="text" class="input-txt input-txt_3" readonly value="${order.createName}" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">制单日期：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" class="input-txt input-txt_3" readonly="readonly" value="<fmt:formatDate value="${order.createTime }" type="date" pattern="yyyy-MM-dd" />" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">审 核 人：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="checkUserName" type="text" class="input-txt input-txt_3" readonly value="" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">审核日期：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input name="checkTime" type="text" class="input-txt input-txt_3" readonly value="" />
							</span>
						</dd>

					</dl>
				</div>
			</form>
		</div>
	</div>
	<!-- 批量悬浮窗 satrt -->
	<div id="batch_taxRate_box" class="batch_taxRate_box">
		<phtml:list items="${fns:basicList2('TAXRATE')}" cssClass="tab_input bg_color  input-txt_3 hy_select2 batch_taxRate_select" valueProperty="id" textProperty="name" name="detailList.taxRateId" selected="${order.rateId }" onchange="batchEditTaxRate()" />
	</div>
	<!-- 批量悬浮窗 end -->
</body>
</html>
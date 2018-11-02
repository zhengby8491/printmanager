<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>创建付款单</title>
<script type="text/javascript">
	var BALANCE_ADVANCEMONEY = 0;
	$(function()
	{
		//取消
		$("#btn_cancel").click(function()
		{
			closeTabAndJump("付款单列表");
		});
		/* 选择供应商 */
		$("#selectSupplier").click(function()
		{
			Helper.popup.show('选择供应商', Helper.basePath + '/quick/supplier_select?multiple=false', '900', '490');
		});
		/* 选择付款源 */
		$("#select_payment_source").click(function()
		{
			var _supplierId = $("#supplierId").val();
			if (Helper.isEmpty(_supplierId))
			{
				Helper.message.alert("请先选择供应商");
				$("#supplierName").focus();
				return false;
			}
			Helper.popup.show('选择付款源', Helper.basePath + '/quick/payment_select?multiple=true&supplierId=' + _supplierId, '1000', '490');

		});

		/* 删除付款源 */
		$("table tbody").on("click", "a[name='btn_del']", function()
		{
			$(this).parent().parent().remove();
			resetSequenceNum();
			sum();
			countOrderMoney();
		});
		/*分摊明细按钮事件 */
		$("#shareMoney").on("click", function()
		{
			shareMoney();
		});
		/* 修改总金额*/
		$("#order_money").on("blur", function()
		{
			var _orderMoney = Number($("#order_money").val());
			var _discount = Number($("#order_discount").val());
			var _sumMoney = Number($("#sum_money").val());
			if ((_orderMoney+_discount) > _sumMoney )
			{
				var _i = _orderMoney+_discount - _sumMoney;
				if (_i < _discount)
				{
					$("#order_discount").val(_discount - _i);
				} else
				{
					$("#order_discount").val(0);
				}
			}
			countOrderAdvance();
			countBalanceMoney();//计算应付余额
		});
		/* 修改总折扣 */
		$("#order_discount").on("blur", function()
		{
			if (Number($("#order_discount").val()) > Number($("#sum_money").val()))
			{
				var sum_money = Number($("#sum_money").val());
				$("#order_discount").val(toDecimal2(sum_money));
			}
			// 当输入折扣金额时大于0时,且收款金额+
			if ($(this).val() != 0)
			{
				if ((Number($("#order_money").val())+ Number($("#order_discount").val()))> Number($("#sum_money").val()))
				{
					var _orderMoney =  Number($("#sum_money").val()) - Number($("#order_discount").val());
					$("#order_money").val(_orderMoney);
				}
			}
			countOrderAdvance();
			countBalanceMoney();
		});
		/* 修改明细付款 */
		$("table tbody").on("blur", "input[name='detailList.money']", function()
		{
			sum();
			countOrderMoney();
		});
		// 初始付款日期
		$("#billTime").val(new Date().format('yyyy-MM-dd'));

		// 初始化制单日期
		$("#createDate").val(new Date().format('yyyy-MM-dd'));

		//保存
		$("#btn_save,#btn_save_audit").click(function()
		{
			fixEmptyValue();
			if (Helper.isEmpty($("#supplierId").val()))
			{
				Helper.message.warn("请选择供应商")
				return false;
			}

			if (Helper.isEmpty($("#order_money").val()) || Number($("#order_money").val()) <= 0)
			{
				Helper.message.warn("付款金额必须大于0");
				return false;
			}
			if (Number($("#order_advance").val()) < 0)
			{
				Helper.message.warn("预付款金额不能小于0");
				return false;
			}

			if (Number($("#order_money").val()).add(Number($("#order_discount").val())).subtr(Number($("#order_advance").val())) != Number($("#sum_money").val()))
			{
				Helper.message.warn("金额计算有误，总付款不等于明细汇总");
				return false;
			}
			if (Number($("#sum_money").val()) < Number($("#sum_sourceBalanceMoney").val()))
			{
				if (Number($("#order_advance").val()) > 0)
				{
					Helper.message.warn("明细未付完前预付款不能存在预付款");
					return false;
				}
			} else if (Number($("#sum_money").val()) > Number($("#sum_sourceBalanceMoney").val()))
			{
				Helper.message.warn("付款金额不能大于源单未付费金额");
				return false;
			}
			if (Helper.isEmpty($("#accountId").val()) || $("#accountId").val() == -1 || $("#accountId").val() == -99)
			{
				Helper.message.warn("保存失败！账户信息不能空，请添加账户信息");
				return false;
			}
			var paramObj = $("#form_payment").formToJson();
			paramObj.isCheck = $(this).attr("id") == "btn_save_audit" ? "YES" : "NO";
			if (paramObj.accountId == -1 || paramObj.accountId == -99)
			{
				paramObj.accountId = "";
			}
			$("#btn_save,#btn_save_audit").attr({
				"disabled" : "disabled"
			});
			Helper.request({
				url : Helper.basePath + "/finance/payment/save",
				data : paramObj,
				success : function(data)
				{
					if (data.success)
					{
						Helper.message.suc('已保存!');
						location.href = Helper.basePath + '/finance/payment/view/' + data.obj.id;
					} else
					{
						Helper.message.warn('保存失败!' + data.message);
						$("#btn_save,#btn_save_audit").removeAttr("disabled");
					}
				},
				error : function(data)
				{
					//console.log(data);
				}
			});
		});

		//初始化
		if ($("#supplierId").val() != "")
		{
			getPaymentMoneys();
			sum();
			countOrderMoney();
		}
	});

	//获取返回付款源数组信息
	function getCallInfo_paymentArray(rows)
	{
		if (rows.length > 0)
		{
			appendTr("detailList", rows);
			//sumCurrPayBalance();
			resetSequenceNum();
		}

		sum();
		countOrderMoney();
		countBalanceMoney();
		//sumCurrPayBalance();
	}
	//绑定"修改本次付款金额"事件
	/* function sumCurrPayBalance(){
	 $("#detailList").find("input[name='detailList.money']").off().on('blur',function(){
	 var currPay = Number($(this).parent().parent().find("input[name='detailList.money']").val()); //本次付款金额
	 var unPay = Number($(this).parent().parent().find("input[name='detailList.sourceBalanceMoney']").val()); //未付款金额
	 var currPayBalance = ((unPay - currPay)<0 ) ? 0 : (unPay - currPay) ;
	 $(this).parent().parent().find("input[name='currentPaymentMoneyBalance']").val(toDecimal2(currPayBalance));
	 sum();
	 });
	 } */
	//计算应付款余额
	function countBalanceMoney()
	{
		var balancePayMoney = Number($("#shouldPayTotalMoney").val()).subtr(Number($("#order_discount").val())).subtr(Number($("#order_money").val()));
		if (Number($("#order_money").val()).add(Number($("#order_discount").val())) > Number($("#sum_money").val()))
		{
			balancePayMoney = Number($("#shouldPayTotalMoney").val()).subtr(Number($("#sum_money").val()));
		}
		if (balancePayMoney < 0)
		{
			$("#balance_shouldPayTotalMoney").val(toDecimal2(0));
		} else
		{
			$("#balance_shouldPayTotalMoney").val(toDecimal2(balancePayMoney));
		}

	}
	//获取返回信息
	function getCallInfo_supplier(obj)
	{
		var old_supplierId = $("#supplierId").val();
		$("#supplierId").val(obj.id);
		$("#supplierName").val(obj.name);
		if (Helper.isNotEmpty(old_supplierId) && obj.id != old_supplierId)
		{//如果改变供应商，则清空table
			//清空table tr
			$("table tbody tr").remove();
		}
		// --------------------------------------------------
		getPaymentMoneys();
		sum();
		countOrderMoney();
	}

	function getPaymentMoneys()
	{
		var params = {};
		params.supplierName = $("#supplierName").val();
		params.pageNumber = 1;
		params.pageSize = 10;

		Helper.request({
			url : Helper.basePath + "/finance/sum/paymentList",
			data : params,//将form序列化成JSON字符串  
			success : function(data)
			{
				if (data.result.length != 0)
				{
					$.each(data.result, function()
					{
						var that = this;
						if (that.supplierId == $("#supplierId").val())
						{
							// 应付总额
							var shouldPayTotalMoney = that.beginMoney + that.shouldMoney + that.adjustMoney - that.discount -that.money;
							$("#shouldPayTotalMoney").val(toDecimal2(shouldPayTotalMoney));
							// 应付余额
							var order_money = Number($("#order_money").val());
							var balance_shouldPayTotalMoney = shouldPayTotalMoney - order_money;
							$("#balance_shouldPayTotalMoney").val(toDecimal2(balance_shouldPayTotalMoney));
							// 预付总额
							var advanceMoney = that.surAdvance;
							$("#advanceMoney").val(advanceMoney);
							// 预付余额
							var balance_advanceMoney = Number($("#advanceMoney").val()) + Number($("#order_advance").val());
							$("#balance_advanceMoney").val(toDecimal2(balance_advanceMoney));
						}
					})
					// 临时容器，存放预付余额，防止被分摊操作搞死
					BALANCE_ADVANCEMONEY = Number($("#advanceMoney").val()).add(Number($("#order_advance").val()));
				}
			},
			error : function(data)
			{
				//console.log(data);
			}
		});
	}

	function appendTr(tableId, rows)
	{
		$.each(rows, function()
		{
			var _THIS = this;
			var _TR = $("<tr/>");
			var sourceDetailIdArray = $("table tbody tr td input[name='detailList.sourceDetailId']").map(function()
			{
				return this.value
			}).get();
			//判断是否已存在明细ID
			if (Helper.isNotEmpty(sourceDetailIdArray) && sourceDetailIdArray.contains("" + this.id))
			{//如果已存在则跳过本次循环
				return true;//continue;	
			}
			$("#" + tableId).find("thead tr th").each(function()
			{
				var name = $(this).attr("name");
				var value = eval("_THIS." + name);
				value = value == undefined ? "" : value;
				switch (name)
				{
				case 'seq':
					_TR.append('<td></td>');
					break;
				case 'operator':
					_TR.append('<td class="td-manage"><input name="detailList.sourceId" type="hidden" readonly="true" value="'+_THIS.master.id+'"/><input name="detailList.sourceDetailId" type="hidden" readonly="true" value="'+_THIS.id+'"/><a title="删除行" href="javascript:void(0)" name="btn_del"><i class="delete fa fa-trash-o"></i></a></td>');
					break;
				case 'master.billNo':
					_TR.append('<td><input name="detailList.sourceBillNo" class="tab_input" type="text" readonly="true" value="'+value+'"/></td>');
					break;
				case 'master.billType':
					_TR.append('<td><input name="detailList.sourceBillType" type="hidden" value="'+value+'"/>' + _THIS.master.billTypeText + '</td>');
					break;
				case 'master.createTime':
					_TR.append('<td>' + new Date(value).format("yyyy-MM-dd") + '</td>');
					break;
				case 'name':
					if (_THIS.master.billType == "PURCH_PK")
					{
						_TR.append('<td><input name="detailList.materialId" class="tab_input" type="hidden" value="' + (_THIS.materialId || "") + '"/><input name="detailList.materialName" class="tab_input" readonly="true" type="text" value="' + (_THIS.materialName || "") + '"/>');
						_TR.append('<input name="detailList.productId" type="hidden"/><input name="detailList.productName" type="hidden"/>');
						_TR.append('<input name="detailList.procedureId" type="hidden"/><input name="detailList.procedureName" type="hidden"/></td>');
					} else if (_THIS.master.billType == "OUTSOURCE_OC")
					{
						if (_THIS.type == "PRODUCT")
						{
							_TR.append('<td><input name="detailList.productId" class="tab_input" type="hidden" value="' + (_THIS.productId || "") + '"/><input name="detailList.productName" class="tab_input" readonly="true" type="text" value="' + (_THIS.productName || "") + '"/>');
							_TR.append('<input name="detailList.materialId" type="hidden"/><input name="detailList.materialName" type="hidden"/>');
							_TR.append('<input name="detailList.procedureId" type="hidden"/><input name="detailList.procedureName" type="hidden"/></td>');
						} else if (_THIS.type == "PROCESS")
						{
							_TR.append('<td><input name="detailList.procedureId" class="tab_input" type="hidden" value="' + (_THIS.procedureId || "") + '"/><input name="detailList.procedureName" class="tab_input" readonly="true" type="text" value="' + (_THIS.procedureName || "") + '"/>');
							_TR.append('<input name="detailList.materialId" type="hidden"/><input name="detailList.materialName" type="hidden"/>');
							_TR.append('<input name="detailList.productId" type="hidden"/><input name="detailList.productName" type="hidden"/></td>');
						}
					} else
					{
						_TR.append('<td><input name="detailList.materialId" type="hidden"/><input name="detailList.materialName" type="hidden"/>');
						_TR.append('<input name="detailList.procedureId" type="hidden"/><input name="detailList.procedureName" type="hidden"/>');
						_TR.append('<input name="detailList.productId" type="hidden"/><input name="detailList.productName" type="hidden"/></td>');
					}
					break;
				case 'style':
					if (_THIS.master.billType == "PURCH_PK")
					{
						_TR.append('<td><input name="detailList.style" class="tab_input" type="text" readonly="true" value="' + (_THIS.specifications || "") + '"/></td>');
					} else
					{
						_TR.append('<td><input name="detailList.style" class="tab_input" type="text" readonly="true" value="' + (_THIS.style || "") + '"/></td>');
					}
					break;
				case 'money':
					if (_THIS.master.billType == "BEGIN_SUPPLIER")
					{
						_TR.append('<td><input name="detailList.sourceMoney" class="tab_input" type="text" readonly="true" value="'+_THIS.paymentMoney+'"/></td>');
					} else if (_THIS.master.billType == "FINANCE_ADJUST")
					{
						_TR.append('<td><input name="detailList.sourceMoney" class="tab_input" type="text" readonly="true" value="'+_THIS.adjustMoney+'"/></td>');
					} else
					{
						_TR.append('<td><input name="detailList.sourceMoney" class="tab_input" type="text" readonly="true" value="'+value+'"/></td>');
					}
					break;
				case 'paymentMoney':
					if (_THIS.master.billType == "BEGIN_SUPPLIER")
					{
						_TR.append('<td><input name="detailList.sourcePaymentMoney" class="tab_input" type="text" readonly="true" value="'+_THIS.paymentedMoney+'"/></td>');
					} else if (_THIS.master.billType == "FINANCE_ADJUST")
					{
						_TR.append('<td><input name="detailList.sourcePaymentMoney" class="tab_input" type="text" readonly="true" value="'+_THIS.receiveOrPayMoney+'"/></td>');
					} else
					{
						_TR.append('<td><input name="detailList.sourcePaymentMoney" class="tab_input" type="text" readonly="true" value="'+value+'"/></td>');
					}
					break;
				case 'unPaymentMoney':
					if (_THIS.master.billType == "BEGIN_SUPPLIER")
					{
						_TR.append('<td><input name="detailList.sourceBalanceMoney" class="tab_input" type="text" readonly="true" value="' + (Number(_THIS.paymentMoney).subtr(Number(_THIS.paymentedMoney))) + '"/></td>');
					} else if (_THIS.master.billType == "FINANCE_ADJUST")
					{
						_TR.append('<td><input name="detailList.sourceBalanceMoney" class="tab_input" type="text" readonly="true" value="' + (Number(_THIS.adjustMoney).subtr(Number(_THIS.receiveOrPayMoney))) + '"/></td>');
					} else
					{
						_TR.append('<td><input name="detailList.sourceBalanceMoney" class="tab_input" type="text" readonly="true" value="' + (Number(_THIS.money).subtr(Number(_THIS.paymentMoney))) + '"/></td>');
					}
					break;
				case 'currentPaymentMoney':
					if (_THIS.master.billType == "BEGIN_SUPPLIER")
					{
						var readonly = (_THIS.paymentMoney - _THIS.paymentedMoney) < 0 ? "readonly" : "";
						_TR.append('<td><input name="detailList.money" class="constraint_decimal tab_input" type="text" ' + readonly + ' value="' + (Number(_THIS.paymentMoney).subtr(Number(_THIS.paymentedMoney))) + '"/></td>');
					} else if (_THIS.master.billType == "FINANCE_ADJUST")
					{
						var readonly = (_THIS.adjustMoney - _THIS.receiveOrPayMoney) < 0 ? "readonly" : "";
						_TR.append('<td><input name="detailList.money" class="constraint_decimal tab_input" type="text" ' + readonly + ' value="' + (Number(_THIS.adjustMoney).subtr(Number(_THIS.receiveOrPayMoney))) + '"/></td>');
					} else
					{
						var readonly = (_THIS.money - _THIS.paymentMoney) < 0 ? "readonly" : "";
						_TR.append('<td><input name="detailList.money" class="constraint_decimal tab_input" type="text" ' + readonly + ' value="' + (Number(_THIS.money).subtr(Number(_THIS.paymentMoney))) + '"/></td>');
					}
					break;
				case 'currentPaymentMoneyBalance':
					if (_THIS.master.billType == "BEGIN_SUPPLIER")
					{
						_TR.append('<td><input name="currentPaymentMoneyBalance" class="tab_input" type="text" readonly="true" value="'+0.00+'"/></td>');
					} else
					{
						_TR.append('<td><input name="currentPaymentMoneyBalance" class="tab_input" type="text" readonly="true" value="'+0.00+'"/></td>');
					}
					break;
				}
				;
			});

			if (_THIS.money - _THIS.paymentMoney < 0)
			{
				_TR.prependTo("#" + tableId);
			} else
			{
				_TR.appendTo("#" + tableId);
			}
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
	function sum()
	{
		var sum_sourceMoney = 0;
		var sum_sourcePaymentMoney = 0;
		var sum_sourceBalanceMoney = 0;
		var sum_sourceCurPayBalance = 0;
		var sum_money = 0;
		$("table tbody tr").each(function()
		{
			sum_sourceMoney = (Number(sum_sourceMoney)).add(Number($(this).find("td input[name='detailList.sourceMoney']").val()));
			sum_sourcePaymentMoney = (Number(sum_sourcePaymentMoney)).add(Number($(this).find("td input[name='detailList.sourcePaymentMoney']").val()));
			sum_sourceBalanceMoney = (Number(sum_sourceBalanceMoney)).add(Number($(this).find("td input[name='detailList.sourceBalanceMoney']").val()));
			sum_money = (Number(Number(sum_money)).add(Number($(this).find("td input[name='detailList.money']").val())));
			sum_sourceCurPayBalance = (Number(sum_sourceCurPayBalance)).add(Number($(this).find("td input[name='currentPaymentMoneyBalance']").val()));
		});
		$("#sum_sourceMoney").val(toDecimal2(sum_sourceMoney));
		$("#sum_sourcePaymentMoney").val(toDecimal2(sum_sourcePaymentMoney));
		$("#sum_sourceBalanceMoney").val(toDecimal2(sum_sourceBalanceMoney));
		$("#sum_sourceCurPayBalance").val(toDecimal2(sum_sourceCurPayBalance));
		sum_money = toDecimal2(sum_money);
		$("#sum_money").val(sum_money);
	}
	function countOrderMoney()
	{
		//if(Number($("#order_money").val())<Number($("#sum_money").val()))
		//{
		var order_money = Number($("#sum_money").val()).add(Number($("#order_advance").val())).subtr(Number($("#order_discount").val()));
		$("#order_money").val(toDecimal2(order_money));
		//}
		countOrderAdvance();
		countBalanceMoney();

	}
	//计算预付款
	function countOrderAdvance()
	{
		var yufukuan = Number($("#order_money").val()).add(Number($("#order_discount").val())).subtr(Number($("#sum_money").val()));
		if (yufukuan < 0)
		{
			yufukuan = 0;
		}
		var yufukuan = toDecimal2(yufukuan);
		$("#order_advance").val(yufukuan);
		var money = Number($("#advanceMoney").val()).add(Number($("#order_advance").val()))
		$("#balance_advanceMoney").val(toDecimal2(money));
	}

	//总额分摊到明细
	function shareMoney()
	{
		if (Helper.isEmpty($("#order_money").val()))
		{
			Helper.message.warn("付费金额不能为空");
		}

		//重新设置明细付款金额
		var totalMoney = Number($("#order_money").val()).add(Number($("#order_discount").val()));
		$("table tbody tr").each(function()
		{
			var sourceBalanceMoney = Number($(this).find("td input[name='detailList.sourceBalanceMoney']").val());//源单余额
			if (totalMoney >= sourceBalanceMoney)
			{
				$(this).find("td input[name='detailList.money']").val(sourceBalanceMoney);
				totalMoney = totalMoney.subtr(sourceBalanceMoney);
			} else
			{
				$(this).find("td input[name='detailList.money']").val(totalMoney);
				totalMoney = 0;
			}
		});
		//计算本次付款余额
		$.each($("table tbody tr"), function()
		{
			var $this = $(this);
			var currRec = Number($this.find("input[name='detailList.money']").val()); //本次付款金额
			var unRec = Number($this.find("input[name='detailList.sourceBalanceMoney']").val()); //未付款金额
			var currRecBalance = ((unRec - currRec) < 0) ? 0 : (unRec - currRec);
			$this.find("input[name='currentPaymentMoneyBalance']").val(currRecBalance.tomoney());
		})
		sum();
		countOrderAdvance();
		// 计算预付余额
		var money = 0;
		if ($("#order_advance").val() == "0" || $("#order_advance").val() == "0.00")
		{
			money = BALANCE_ADVANCEMONEY;
		} else
		{
			money = Number($("#advanceMoney").val()).add(Number($("#order_advance").val()));
		}

		$("#balance_advanceMoney").val(toDecimal2(money));
		//$("#detailList").find("input[name='detailList.money']").trigger('blur');BALANCE_ADVANCEMONEY

		//金额重新合计
		countBalanceMoney();//计算应收余额
	}

	//制保留2位小数，如：2，会在2后面补上00.即2.00    
	function toDecimal2(x)
	{
		var f = parseFloat(x);
		if (isNaN(f))
		{
			return false;
		}
		var f = Math.round(x * 100) / 100;
		var s = f.toString();
		var rs = s.indexOf('.');
		if (rs < 0)
		{
			rs = s.length;
			s += '.';
		}
		while (s.length <= rs + 2)
		{
			s += '0';
		}
		return s;
	}
</script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="财务管理-付款单-创建"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="finance:payment:create">
						<button class="nav_btn table_nav_btn" id="btn_save">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="finance:payment:create,finance:payment:audit">
						<button class="nav_btn table_nav_btn" id="btn_save_audit">保存并审核</button>
					</shiro:hasManyPermissions>
					<button class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
				</div>
			</div>
			<form id="form_payment">
				<!--主表-订单表单-->
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl" style="width: 1250px">
								<dd class="row-dd">
									<label class="form-label label_ui label_6">供应商名称：</label>
									<span class="ui-combo-wrap wrap-width">
										<input type="text" readonly class="input-txt input-txt_7" name="supplierName" id="supplierName" value="${supplier.name }" />
										<div class="select-btn" id="selectSupplier">...</div>
										<input type="hidden" readonly name="supplierId" id="supplierId" value="${supplier.id }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">账 号：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('ACCOUNT')}" valueProperty="id" textProperty="bankNo" name="accountId" cssClass="input-txt input-txt_7 hy_select2" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">应付总额：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="constraint_decimal_negative input-txt input-txt_1" id="shouldPayTotalMoney" value="0.00" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">应付余额：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="constraint_decimal_negative input-txt input-txt_1" id="balance_shouldPayTotalMoney" value="0.00" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">预付总额：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="constraint_decimal_negative input-txt input-txt_1" id="advanceMoney" value="0.00" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">预付余额：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="constraint_decimal_negative input-txt input-txt_1" id="balance_advanceMoney" value="0.00" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_6">结算方式：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('SETTLEMENTCLASS')}" valueProperty="id" textProperty="name" name="settlementClassId" cssClass="input-txt input-txt_16 hy_select2" selected="${settlementClassId  }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">付款日期：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_7 Wdate" onFocus="WdatePicker({lang:'zh-cn'})" name="billTime" id="billTime" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">付 款 人：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('EMPLOYEE')}" valueProperty="id" textProperty="name" name="employeeId" cssClass="input-txt input-txt_1 hy_select2" />
									</span>
								</dd>

								<dd class="row-dd">
									<label class="form-label label_ui label_1">付款金额：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="constraint_decimal_negative input-txt input-txt_1" name="money" id="order_money" value="0.00" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">折扣金额：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="constraint_decimal_negative input-txt input-txt_1" name="discount" id="order_discount" value="0" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_6">备注：</label>
									<span class="form_textarea">
										<textarea class="noborder" name="memo" style="width: 972px" placeholder="说点什么...100个字符以内" onKeyUp="Helper.doms.textarealength(this,100)"></textarea>
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
					<a id="select_payment_source" href="javascript:;" class="nav_btn table_nav_btn">
						<i class="fa fa-plus-square"></i>
						选择源单
					</a>
					<a id="shareMoney" href="javascript:;" class="nav_btn table_nav_btn">分摊</a>
				</div>
				<!--按钮栏End-->

				<!--从表-订单表格-->
				<div>
					<div class="table-container">
						<table class="border-table" style="" id="detailList">
							<thead>
								<tr>
									<th width="40" name="seq">序号</th>
									<th width="80" name="operator">操作</th>
									<th width="100" name="master.billNo">源单单号</th>
									<th width="100" name="master.billType">源单类型</th>
									<th width="80" name="master.createTime">制单日期</th>
									<th width="100" name="name">产品/材料/工序</th>
									<th width="100" name="style">规格</th>
									<th width="100" name="money">单据金额</th>
									<th width="100" name="paymentMoney">已付款金额</th>
									<th width="100" name="currentPaymentMoneyBalance">本次付款余额</th>
									<th width="100" name="unPaymentMoney">未付款金额</th>
									<th width="120" name="currentPaymentMoney">本次付款金额</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="detailList" items="${detail}" varStatus="status">
									<tr name="tr">
										<%-- <input type="hidden" name="id" value="${order.id }"> --%>
										<td>${status.count}</td>
										<td class="td-manage">
											<input name="detailList.sourceId" type="hidden" readonly="readonly" value="${detailList.master.id }" />
											<input name="detailList.sourceDetailId" type="hidden" readonly="readonly" value="${detailList.id } " />
											<a title="删除行" href="javascript:void(0)" name="btn_del">
												<i class="delete fa fa-trash-o"></i>
											</a>
										</td>
										<td>
											<input name="detailList.sourceBillNo" type="text" class="tab_input" readonly="readonly" value="${detailList.master.billNo }" />
										</td>
										<td>
											<input name="detailList.sourceBillType" type="hidden" class="tab_input" value="${detailList.master.billType }" />
											<input type="text" class="tab_input" readonly="readonly" value="${detailList.master.billTypeText }" />
										</td>
										<td>
											<input class="tab_input" type="text" readonly="readonly" value="<fmt:formatDate value="${detailList.master.createTime}" type="date" pattern="yyyy-MM-dd" />" />
										</td>
										<td>
											<c:if test="${detailList.master.billType eq 'PURCH_PK' }">
												<input name="detailList.materialId" class="tab_input" type="hidden" value="${detailList.materialId }" />
												<input name="detailList.materialName" class="tab_input" readonly="true" type="text" value="${detailList.materialName }" />
											</c:if>
											<c:if test="${detailList.master.billType eq 'OUTSOURCE_OC' }">
												<c:if test="${order.type eq 'PRODUCT' }">
													<input name="detailList.productId" class="tab_input" type="hidden" value="${detailList.productId }" />
													<input name="detailList.productName" type="text" class="tab_input" readonly="readonly" value="${detailList.productName }" />
												</c:if>
												<c:if test="${detailList.type eq 'PROCESS' }">
													<input name="detailList.procedureId" class="tab_input" type="hidden" value="${detailList.procedureId }" />
													<input name="detailList.procedureName" type="text" class="tab_input" readonly="readonly" value="${detailList.procedureName }" />
												</c:if>
											</c:if>
										</td>
										<td>
											<c:choose>
												<c:when test="${detailList.master.billType eq 'PURCH_PK'  }">
													<input name="detailList.style" type="text" class="tab_input" readonly="readonly" value="${detailList.specifications }" />
												</c:when>
												<c:otherwise>
													<input name="detailList.style" type="text" class="tab_input" readonly="readonly" value="${detailList.style }" />
												</c:otherwise>
											</c:choose>

										</td>
										<!-- 单据金额 -->
										<td>
											<c:choose>
												<c:when test="${detailList.master.billType == 'BEGIN_SUPPLIER'}">
													<input name="detailList.sourceMoney" class="tab_input" readonly="readonly" type="text" value="${detailList.paymentMoney }" />
												</c:when>
												<c:when test="${detailList.master.billType == 'FINANCE_ADJUST'}">
													<input name="detailList.sourceMoney" class="tab_input" readonly="readonly" type="text" value="${detailList.adjustMoney }" />
												</c:when>
												<c:otherwise>
													<input name="detailList.sourceMoney" class="tab_input" readonly="readonly" type="text" value="${detailList.money }" />
												</c:otherwise>
											</c:choose>
										</td>
										<!-- 已付款金额 -->
										<td>
											<c:choose>
												<c:when test="${detailList.master.billType == 'BEGIN_SUPPLIER'}">
													<input name="detailList.sourcePaymentMoney" class="tab_input" readonly="readonly" type="text" value="${detailList.paymentedMoney }" />
												</c:when>
												<c:when test="${detailList.master.billType == 'FINANCE_ADJUST'}">
													<input name="detailList.sourcePaymentMoney" class="tab_input" readonly="readonly" type="text" value="${detailList.receiveOrPayMoney }" />
												</c:when>
												<c:otherwise>
													<input name="detailList.sourcePaymentMoney" class="tab_input" readonly="readonly" type="text" value="${detailList.paymentMoney }" />
												</c:otherwise>
											</c:choose>
										</td>
										<!-- 本次付款余额 -->
										<td>
											<input name="currentPaymentMoneyBalance" class="tab_input" type="text" readonly="readonly" value="0.00"/></td>
										</td>
										<!-- 未付款金额 -->
										<td>
											<c:choose>
												<c:when test="${detailList.master.billType == 'BEGIN_SUPPLIER'}">
													<input name="detailList.sourceBalanceMoney" class="tab_input" readonly="readonly" type="text" value="${detailList.paymentMoney - detailList.paymentedMoney }" />
												</c:when>
												<c:when test="${detailList.master.billType == 'FINANCE_ADJUST'}">
													<input name="detailList.sourceBalanceMoney" class="tab_input" readonly="readonly" type="text" value="${detailList.adjustMoney - detailList.receiveOrPayMoney }" />
												</c:when>
												<c:otherwise>
													<input name="detailList.sourceBalanceMoney" class="tab_input" readonly="readonly" type="text" value="${detailList.money - detailList.paymentMoney }" />
												</c:otherwise>
											</c:choose> 
										</td>
										<!-- 本次付款金额 -->
										<td>
											<c:choose>
												<c:when test="${detailList.master.billType == 'BEGIN_SUPPLIER'}">
													<input name="detailList.money" class="tab_input" readonly="readonly" type="text" value="${detailList.paymentMoney - detailList.paymentedMoney }" />
												</c:when>
												<c:when test="${detailList.master.billType == 'FINANCE_ADJUST'}">
													<input name="detailList.money" class="tab_input" readonly="readonly" type="text" value="${detailList.adjustMoney - detailList.receiveOrPayMoney }" />
												</c:when>
												<c:otherwise>
													<input name="detailList.money" class="tab_input" readonly="readonly" type="text" value="${detailList.money - detailList.paymentMoney }" />
												</c:otherwise>
											</c:choose> 
										</td>
									</tr>

								</c:forEach>
							</tbody>
							<tfoot>
								<tr>
									<td>合计</td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td>
										<input id="sum_sourceMoney" class="tab_input" type="text" readonly="readonly">
									</td>
									<td>
										<input id="sum_sourcePaymentMoney" class="tab_input" type="text" readonly="readonly">
									</td>
									<td>
										<input id="sum_sourceCurPayBalance" class="tab_input" type="text" readonly="readonly">
									</td>
									<td>
										<input id="sum_sourceBalanceMoney" class="tab_input" type="text" readonly="readonly">
									</td>
									<td>
										<input id="sum_money" class="tab_input" type="text" readonly="readonly">
									</td>
								</tr>
							</tfoot>
						</table>
					</div>
				</div>
				<div class="cl form_content">
					<dl class="cl row-dl-foot">
						<dd class="row-dd">
							<label class="form-label label_ui label_1">预 付 款：</label>
							<span class="ui-combo-wrap">
								<input class="constraint_decimal_negative input-txt input-txt_1" type="text" name="advance" id="order_advance" readonly="readonly" value="0" />
							</span>
						</dd>

						<dd class="row-dd">
							<label class="form-label label_ui label_1">制 单 人：</label>
							<span class="ui-combo-wrap">
								<input name="createName" type="text" class="input-txt input-txt_1" readonly value="${loginUser.realName}" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">制单日期：</label>
							<span class="ui-combo-wrap">
								<input name="createTime" type="text" class="input-txt input-txt_1" readonly="readonly" id="createDate" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">审 核 人：</label>
							<span class="ui-combo-wrap">
								<input name="checkUserName" type="text" class="input-txt input-txt_1" readonly="readonly" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">审核日期：</label>
							<span class="ui-combo-wrap">
								<input name="checkTime" type="text" class="input-txt input-txt_1" readonly="readonly" />
							</span>
						</dd>

					</dl>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
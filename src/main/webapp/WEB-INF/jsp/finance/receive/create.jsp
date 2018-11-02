<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>创建收款单</title>
<script type="text/javascript">
	var BALANCE_ADVANCEMONEY = 0;
	$(function()
	{
		//取消
		$("#btn_cancel").click(function()
		{
			closeTabAndJump("收款单列表");
		});
		/* 选择客户 */
		$("#selectCustomer").click(function()
		{
			Helper.popup.show('选择客户', Helper.basePath + '/quick/customer_select', '900', '490');
		});
		/* 选择收款源 */
		$("#select_receive_source").click(function()
		{
			var _customerId = $("#customerId").val();
			if (Helper.isEmpty(_customerId))
			{
				Helper.message.alert("请先选择客户");
				$("#customerName").focus();
				return false;
			}
			Helper.popup.show('选择收款源', Helper.basePath + '/quick/receive_select?multiple=true&customerId=' + _customerId, '1158', '490');
		});

		/* 删除收款源 */
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
			countBalanceMoney();//计算应收余额
		});
		/* 修改总折扣 */
		$("#order_discount").on("blur", function()
		{
			if (Number($("#order_discount").val()) > Number($("#sum_money").val()))
			{
				$("#order_discount").val($("#sum_money").val());
				$("#balance_shouldRecTotalMoney").val($("#sum_money").val());
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
			countBalanceMoney();
			countOrderAdvance();
		});
		/* 修改明细收款 */
		$("table tbody").on("blur", "input[name='detailList.money']", function()
		{
			sum();
			countOrderMoney();
		});
		// 初始收款日期
		$("#billTime").val(new Date().format('yyyy-MM-dd'));

		// 初始化制单日期
		$("#createDate").val(new Date().format('yyyy-MM-dd'));

		//保存
		$("#btn_save,#btn_save_audit").click(function()
		{
			fixEmptyValue(); // 删除自定义项
			if (Helper.isEmpty($("#customerId").val()))
			{
				Helper.message.warn("请选择客户")
				return false;
			}
			if (Helper.isEmpty($("#order_money").val()) || Number($("#order_money").val()) <= 0)
			{
				Helper.message.warn("收款金额必须大于0");
				return false;
			}
			if (Number($("#order_advance").val()) < 0)
			{
				Helper.message.warn("预收款金额不能小于0");
				return false;
			}

			if (Number($("#order_money").val()).add(Number($("#order_discount").val())).subtr(Number($("#order_advance").val())) != Number($("#sum_money").val()))
			{
				Helper.message.warn("金额计算有误，总收款不等于明细汇总");
				return false;
			}
			if (Number($("#sum_money").val()) < Number($("#sum_sourceBalanceMoney").val()))
			{
				if (Number($("#order_advance").val()) > 0)
				{
					Helper.message.warn("明细未收完前预收款不能存在预收款");
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
			var paramObj = $("#form_receive").formToJson();
			paramObj.isCheck = $(this).attr("id") == "btn_save_audit" ? "YES" : "NO";
			if (paramObj.accountId == -1 || paramObj.accountId == -99)
			{
				paramObj.accountId = "";
			}
			$("#btn_save,#btn_save_audit").attr({
				"disabled" : "disabled"
			});
			Helper.request({
				url : Helper.basePath + "/finance/receive/save",
				data : paramObj,//将form序列化成JSON字符串  
				success : function(data)
				{
					if (data.success)
					{
						Helper.message.suc('已保存!');
						location.href = Helper.basePath + '/finance/receive/view/' + data.obj.id;
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
		})
		// 初始化后进行操作
		if ($("#customerId").val() != "")
		{
			getReceiveMoneys();
			sum();
			countOrderMoney();
		}
	});

	//获取返回收款源数组信息
	function getCallInfo_receiveArray(rows)
	{
		if (rows.length > 0)
		{
			appendTr("detailList", rows);
			resetSequenceNum();
		}
		sum();
		countOrderMoney();
		countBalanceMoney();
		//sumCurrRecBalance();
	}

	//绑定"修改本次收款金额"事件
	/* function sumCurrRecBalance(){
	 $("#detailList").find("input[name='detailList.money']").off().on('blur',function(){
	 var currRec = Number($(this).parent().parent().find("input[name='detailList.money']").val()); //本次收款金额
	 var unRec = Number($(this).parent().parent().find("input[name='detailList.sourceBalanceMoney']").val()); //未收款金额
	 var currRecBalance = ((unRec - currRec)<0 ) ? 0 : (unRec - currRec) ;
	 $(this).parent().parent().find("input[name='currentReceiveMoneyBalance']").val(currRecBalance.tomoney());
	 sum();
	 });
	 } */

	//获取返回信息
	function getCallInfo_customer(obj)
	{
		var old_customerId = $("#customerId").val();
		$("#customerId").val(obj.id);
		$("#customerName").val(obj.name);
		if (Helper.isNotEmpty(old_customerId) && obj.id != old_customerId)
		{//如果改变客户，则清空table
			//清空table tr
			$("table tbody tr").remove();
		}
		// -------------
		getReceiveMoneys();
		sum();
		countOrderMoney();
	}

	function getReceiveMoneys()
	{
		var params = {};
		params.customerName = $("#customerName").val();
		params.pageNumber = 1;
		params.pageSize = 10;

		Helper.request({
			url : Helper.basePath + "/finance/sum/receiveList",
			data : params,//将form序列化成JSON字符串  
			success : function(data)
			{
				if (data.result.length != 0)
				{
					$.each(data.result, function()
					{
						var that = this;
						if (that.customerId == $("#customerId").val())
						{
							console.log(that);
							// 应收款总额
							var shouldRecTotalMoney = that.beginMoney + that.shouldMoney + that.adjustMoney - that.discount - that.money;
							$("#shouldRecTotalMoney").val(toDecimal2(shouldRecTotalMoney));
							// 应付余额
							var order_money = $("#order_money").val();
							var balance_shouldRecTotalMoney = shouldRecTotalMoney - order_money;
							$("#balance_shouldRecTotalMoney").val(toDecimal2(balance_shouldRecTotalMoney));
							// 预付总额
							var advanceMoney = that.surAdvance;
							$("#advanceMoney").val(toDecimal2(advanceMoney));
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
			{console.log(this)
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
				case 'product':
					_TR.append('<td><input name="detailList.productId" class="tab_input" type="hidden" value="' + (_THIS.productId || "") + '"/><input name="detailList.productName" class="tab_input" readonly="true"  type="text" value="' + (_THIS.productName || "") + '"/></td>');
					break;
				case 'procedure':
					_TR.append('<td><input name="detailList.procedureId" class="tab_input" type="hidden" value="' + (_THIS.procedureId || "") + '"/><input name="detailList.procedureName" class="tab_input" readonly="true"  type="text" value="' + (_THIS.procedureName || "") + '"/></td>');
					break;
				case 'style':
					_TR.append('<td><input name="detailList.style" class="tab_input" readonly="true" type="text" value="' + (_THIS.style || "") + '"/></td>');
					break;
				case 'money':
					if (_THIS.master.billType == "SALE_SK" || _THIS.master.billType == "OEM_EC")
					{
						_TR.append('<td><input name="detailList.sourceMoney" class="tab_input" type="text" readonly="true" value="'+value+'"/></td>');
					} else if (_THIS.master.billType == "FINANCE_ADJUST")
					{
						_TR.append('<td><input name="detailList.sourceMoney" class="tab_input" type="text" readonly="true" value="'+_THIS.adjustMoney+'"/></td>');
					} else
					{
						_TR.append('<td><input name="detailList.sourceMoney" class="tab_input" type="text" readonly="true" value="'+_THIS.receiveMoney+'"/></td>');
					}

					break;
				case 'receiveMoney':
					if (_THIS.master.billType == "SALE_SK" || _THIS.master.billType == "OEM_EC")
					{
						_TR.append('<td><input name="detailList.sourceReceiveMoney" class="tab_input" type="text" readonly="true" value="'+value+'"/></td>');
					} else if (_THIS.master.billType == "FINANCE_ADJUST")
					{
						_TR.append('<td><input name="detailList.sourceReceiveMoney" class="tab_input" type="text" readonly="true" value="'+_THIS.receiveOrPayMoney+'"/></td>');
					} else
					{
						_TR.append('<td><input name="detailList.sourceReceiveMoney" class="tab_input" type="text" readonly="true" value="'+_THIS.receivedMoney+'"/></td>');
					}
					break;
				case 'unReceiveMoney':
					if (_THIS.master.billType == "SALE_SK" || _THIS.master.billType == "OEM_EC")
					{
						_TR.append('<td><input name="detailList.sourceBalanceMoney" class="tab_input" type="text" readonly="true" value="' + (Number(_THIS.money).subtr(Number(_THIS.receiveMoney))) + '"/></td>');
					} else if (_THIS.master.billType == "FINANCE_ADJUST")
					{
						_TR.append('<td><input name="detailList.sourceBalanceMoney" class="tab_input" type="text" readonly="true" value="' + (Number(_THIS.adjustMoney).subtr(Number(_THIS.receiveOrPayMoney))) + '"/></td>');
					} else
					{
						_TR.append('<td><input name="detailList.sourceBalanceMoney" class="tab_input" type="text" readonly="true" value="' + (Number(_THIS.receiveMoney).subtr(Number(_THIS.receivedMoney))) + '"/></td>');
					}

					break;
				case 'currentReceiveMoney':
					if (_THIS.master.billType == "SALE_SK" || _THIS.master.billType == "OEM_EC")
					{
						var readonly = (_THIS.money - _THIS.receiveMoney) < 0 ? "readonly" : "";
						_TR.append('<td><input name="detailList.money" class="constraint_decimal tab_input" type="text" ' + readonly + ' value="' + (Number(_THIS.money).subtr(Number(_THIS.receiveMoney))) + '"/></td>');
					} else if (_THIS.master.billType == "FINANCE_ADJUST")
					{
						var readonly = (_THIS.adjustMoney - _THIS.receiveOrPayMoney) < 0 ? "readonly" : "";
						_TR.append('<td><input name="detailList.money" class="constraint_decimal tab_input" type="text" ' + readonly + ' value="' + (Number(_THIS.adjustMoney).subtr(Number(_THIS.receiveOrPayMoney))) + '"/></td>');
					} else
					{
						var readonly = (_THIS.receiveMoney - _THIS.receivedMoney) < 0 ? "readonly" : "";
						_TR.append('<td><input name="detailList.money" class="constraint_decimal tab_input" type="text" ' + readonly + ' value="' + (Number(_THIS.receiveMoney).subtr(Number(_THIS.receivedMoney))) + '"/></td>');
					}

					break;
				case 'currentReceiveMoneyBalance':
					if (_THIS.master.billType == "BEGIN_SUPPLIER")
					{
						_TR.append('<td><input name="currentReceiveMoneyBalance" class="tab_input" type="text" readonly="true" value="'+0.00+'"/></td>');
					} else
					{
						_TR.append('<td><input name="currentReceiveMoneyBalance" class="tab_input" type="text" readonly="true" value="'+0.00+'"/></td>');
					}
					break;
				}

			});
			if (_THIS.money - _THIS.receiveMoney < 0)
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
		var sum_sourceReceiveMoney = 0;
		var sum_sourceBalanceMoney = 0;
		var sum_sourceCurRecBalance = 0;
		var sum_money = 0;
		$("table tbody tr").each(function()
		{
			sum_sourceMoney = (Number(sum_sourceMoney)).add(Number($(this).find("td input[name='detailList.sourceMoney']").val()));
			sum_sourceReceiveMoney = (Number(sum_sourceReceiveMoney)).add(Number($(this).find("td input[name='detailList.sourceReceiveMoney']").val()));
			sum_sourceBalanceMoney = (Number(sum_sourceBalanceMoney)).add(Number($(this).find("td input[name='detailList.sourceBalanceMoney']").val()));
			sum_money = (Number(sum_money)).add(Number($(this).find("td input[name='detailList.money']").val()));
			sum_sourceCurRecBalance = (Number(sum_sourceCurRecBalance)).add(Number($(this).find("td input[name='currentReceiveMoneyBalance']").val()));
		});
		$("#sum_sourceMoney").val(toDecimal2(sum_sourceMoney));
		$("#sum_sourceReceiveMoney").val(toDecimal2(sum_sourceReceiveMoney));
		$("#sum_sourceBalanceMoney").val(toDecimal2(sum_sourceBalanceMoney));
		$("#sum_sourceCurRecBalance").val(toDecimal2(sum_sourceCurRecBalance));
		sum_money = toDecimal2(sum_money);
		$("#sum_money").val(sum_money);
	}
	function countOrderMoney()
	{
		//if(Number($("#order_money").val())<Number($("#sum_money").val()))
		//{
		// 计算收款金额 = 本次收款合计+预付款 - 折扣
		$("#order_money").val(Number($("#sum_money").val()).add(Number($("#order_advance").val())).subtr(Number($("#order_discount").val())));
		//}
		countOrderAdvance();
		countBalanceMoney();
	}
	//计算预收款
	function countOrderAdvance()
	{
		var yushoukuan = Number($("#order_money").val()).add(Number($("#order_discount").val())).subtr(Number($("#sum_money").val()));
		if (yushoukuan < 0)
		{
			yushoukuan = 0;
		}
		yufukuan = toDecimal2(yushoukuan);
		$("#order_advance").val(yushoukuan);
		$("#balance_advanceMoney").val(Number($("#advanceMoney").val()).add(Number($("#order_advance").val())));//预收款余额
	}
	//计算应收款余额
	function countBalanceMoney()
	{
		var balanceRecMoney = Number($("#shouldRecTotalMoney").val()).subtr(Number($("#order_money").val())).subtr(Number($("#order_discount").val()));
		if (Number($("#order_money").val()).add(Number($("#order_discount").val())) > Number($("#sum_money").val()))
		{
			balanceRecMoney = Number($("#shouldRecTotalMoney").val()).subtr(Number($("#sum_money").val()));
		}
		if (balanceRecMoney < 0)
		{
			$("#balance_shouldRecTotalMoney").val(0.00);
		} else
		{
			$("#balance_shouldRecTotalMoney").val(toDecimal2(balanceRecMoney));
		}
	}
	//总额分摊到明细
	function shareMoney()
	{
		if (Helper.isEmpty($("#order_money").val()))
		{
			Helper.message.warn("收费金额不能为空");
		}

		//重新设置明细收款金额
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
		// 计算本次收款余额
		//$("#detailList").find("input[name='detailList.money']").trigger('blur');

		$.each($("table tbody tr"), function()
		{
			var $this = $(this);
			var currRec = Number($this.find("input[name='detailList.money']").val()); //本次收款金额
			var unRec = Number($this.find("input[name='detailList.sourceBalanceMoney']").val()); //未收款金额
			var currRecBalance = ((unRec - currRec) < 0) ? 0 : (unRec - currRec);
			$this.find("input[name='currentReceiveMoneyBalance']").val(currRecBalance.tomoney());
		})
		sum();
		countOrderAdvance();
		//计算预收余额
		var money = 0;
		if ($("#order_advance").val() == "0" || $("#order_advance").val() == "0.00")
		{
			money = BALANCE_ADVANCEMONEY;
		} else
		{
			money = Number($("#advanceMoney").val()).add(Number($("#order_advance").val()));
		}
		$("#balance_advanceMoney").val(toDecimal2(money));

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
					<sys:nav struct="财务管理-收款单-创建"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="finance:receive:create">
						<button class="nav_btn table_nav_btn" id="btn_save">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="finance:receive:create,finance:receive:audit">
						<button class="nav_btn table_nav_btn" id="btn_save_audit">保存并审核</button>
					</shiro:hasManyPermissions>
					<button class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
				</div>
			</div>
			<form id="form_receive">
				<!--主表-订单表单-->
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl" style="width: 1250px">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">客户名称：</label>
									<span class="ui-combo-wrap wrap-width">
										<input type="text" readonly class="input-txt input-txt_7" name="customerName" id="customerName" value="${customer.name }" />
										<div class="select-btn" id="selectCustomer">...</div>
										<input type="hidden" readonly name="customerId" id="customerId" value="${customer.id }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">账 号：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('ACCOUNT')}" valueProperty="id" textProperty="bankNo" name="accountId" cssClass="input-txt input-txt_7 hy_select2" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">应收总额：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="constraint_decimal_negative input-txt input-txt_1" id="shouldRecTotalMoney" value="0.00" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">应收余额：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="constraint_decimal_negative input-txt input-txt_1" id="balance_shouldRecTotalMoney" value="0.00" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">预收总额：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="constraint_decimal_negative input-txt input-txt_1" id="advanceMoney" value="0.00" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">预收余额：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="constraint_decimal_negative input-txt input-txt_1" id="balance_advanceMoney" value="0.00" />
									</span>
								</dd>
							</dl>

							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">结算方式：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('SETTLEMENTCLASS')}" valueProperty="id" textProperty="name" name="settlementClassId" cssClass="input-txt input-txt_16 hy_select2" selected="${order.settlementClassId  }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">收款日期：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_7 Wdate" onFocus="WdatePicker({lang:'zh-cn'})" name="billTime" id="billTime" value="${order.billTime }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">收 款 人：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('EMPLOYEE')}" valueProperty="id" textProperty="name" name="employeeId" cssClass="input-txt input-txt_1 hy_select2" />

										<%--  <input
										type="hidden" name="employeeId" id="employeeId"
										value="${order.employeeId }" /> <input type="text"
										class="input-txt input-txt_0" name="employeeName"
										id="employeeName"
										value="${fns:basicInfo('EMPLOYEE',order.employeeId).name}" /> --%>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">收款金额：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="constraint_decimal_negative input-txt input-txt_1" name="money" id="order_money" value="${order.money }" />
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
									<label class="form-label label_ui label_1">备 注：</label>
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
					<a id="select_receive_source" href="javascript:;" class="nav_btn table_nav_btn">
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
									<th width="40" name="operator">操作</th>
									<th width="100" name="master.billNo">源单单号</th>
									<th width="80" name="master.billType">源单类型</th>
									<th width="80" name="master.createTime">制单日期</th>
									<th width="120" name="product">成品名称</th>
									<th width="100" name="procedure">工序名称</th>
									<th width="100" name="style">规格</th>
									<th width="40" name="money">单据金额</th>
									<th width="40" name="receiveMoney">已收款金额</th>
									<th width="40" name="currentReceiveMoneyBalance">本次收款余额</th>
									<th width="40" name="unReceiveMoney">未收款金额</th>
									<th width="50" name="currentReceiveMoney">本次收款金额</th>
								</tr>
							</thead>
							<tbody>
									<c:forEach items="${detail }" var="detailList" varStatus="status">
										<tr>
											<td>${status.count}</td>
											<td class="td-manage">
												<input name="detailList.sourceId" type="hidden" readonly="readonly" value="${detailList.master.id }"/>
												<input name="detailList.sourceDetailId" type="hidden" readonly="readonly" value="${detailList.id }"/>
												<a title="删除行" name="btn_del" href="#"><i class="delete fa fa-trash-o"></i></a>
											</td>
											<td>
												<input name="detailList.sourceBillNo" class="tab_input" type="text" readonly="readonly" value="${detailList.master.billNo }"/>
											</td>
											<td>
												<input class="tab_input" type="text" readonly="readonly" value="${detailList.master.billTypeText }"/>
												<input name="detailList.sourceBillType" class="tab_input" type="hidden" readonly="readonly" value="${detailList.master.billType }"/>
											</td>
											<td>
												<input class="tab_input" type="text" readonly="readonly" value="<fmt:formatDate value="${detailList.master.createTime}" type="date" pattern="yyyy-MM-dd" />" />
											</td>
											<td>
												<input name="detailList.productId" readonly="readonly"  type="hidden" value="${detailList.productId }"/>
												<input name="detailList.productName" class="tab_input" readonly="readonly"  type="text" value="${detailList.productName }"/>
											</td>
											<td>
												<c:if test="${detailList.master.billType == 'OEM_EC' }">
												<input name="detailList.procedureId" readonly="readonly"  type="hidden" value="${detailList.procedureId }"/>
												<input name="detailList.procedureName" class="tab_input" readonly="readonly"  type="text" value="${detailList.procedureName }"/>
												</c:if>
											</td>
											<td>
												<input name="detailList.style" class="tab_input" readonly="readonly"  type="text" value="${detailList.style }"/>
											</td>
											<td>
												<c:choose>
													<c:when test="${detailList.master.billType == 'SALE_SK' || detailList.master.billType == 'OEM_EC' }">
														<input name="detailList.sourceMoney" class="tab_input" readonly="readonly"  type="text" value="${detailList.money }"/>
													</c:when>
													<c:when test="${detailList.master.billType == 'FINANCE_ADJUST'}">
														<input name="detailList.sourceMoney" class="tab_input" readonly="readonly"  type="text" value="${detailList.adjustMoney }"/>
													</c:when>
													<c:otherwise>
														<input name="detailList.sourceMoney" class="tab_input" readonly="readonly"  type="text" value="${detailList.receiveMoney }"/>
													</c:otherwise>
												</c:choose>
											</td>
											<td>
												<c:choose>
													<c:when test="${detailList.master.billType == 'SALE_SK' || detailList.master.billType == 'OEM_EC' }">
														<input name="detailList.sourceReceiveMoney" class="tab_input" readonly="readonly"  type="text" value="${detailList.receiveMoney }"/>
													</c:when>
													<c:when test="${detailList.master.billType == 'FINANCE_ADJUST'}">
														<input name="detailList.sourceReceiveMoney" class="tab_input" readonly="readonly"  type="text" value="${detailList.receiveOrPayMoney }"/>
													</c:when>
													<c:otherwise>
														<input name="detailList.sourceReceiveMoney" class="tab_input" readonly="readonly"  type="text" value="${detailList.receivedMoney }"/>
													</c:otherwise>
												</c:choose>
											</td>
											<td>
												<input name="currentReceiveMoneyBalance" class="tab_input" type="text" readonly="readonly" value="0.00"/>
											</td>
											<td>
												<c:choose>
													<c:when test="${detailList.master.billType == 'SALE_SK' || detailList.master.billType == 'OEM_EC' }">
														<input name="detailList.sourceBalanceMoney" class="tab_input" readonly="readonly"  type="text" value="${detailList.money - detailList.receiveMoney }"/>
													</c:when>
													<c:when test="${detailList.master.billType == 'FINANCE_ADJUST'}">
														<input name="detailList.sourceBalanceMoney" class="tab_input" readonly="readonly"  type="text" value="${detailList.adjustMoney - detailList.receiveOrPayMoney }"/>
													</c:when>
													<c:otherwise>
														<input name="detailList.sourceBalanceMoney" class="tab_input" readonly="readonly"  type="text" value="${detailList.receiveMoney - detailList.receivedMoney }"/>
													</c:otherwise>
												</c:choose>
											</td>
											<td>
												<c:choose>
													<c:when test="${detailList.master.billType == 'SALE_SK' || detailList.master.billType == 'OEM_EC' }">
														<input name="detailList.money" class="constraint_decimal tab_input" type="text" 
														<c:if test="${detailList.money - detailList.receiveMoney < 0}">readonly</c:if> value="${detailList.money - detailList.receiveMoney }"/>
													</c:when>
													<c:when test="${detailList.master.billType == 'FINANCE_ADJUST'}">
														<input name="detailList.money" class="tab_input" readonly="readonly"  type="text"
														<c:if test="${detailList.adjustMoney - detailList.receiveOrPayMoney < 0}">readonly</c:if> value="${detailList.adjustMoney - detailList.receiveOrPayMoney }"/>
													</c:when>
													<c:otherwise>
														<input name="detailList.money" class="tab_input" readonly="readonly"  type="text"
														<c:if test="${detailList.receiveMoney - detailList.receivedMoney < 0}">readonly</c:if> value="${detailList.receiveMoney - detailList.receivedMoney }"/>
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
									<td></td>
									<td>
										<input id="sum_sourceMoney" class="tab_input" type="text" readonly="readonly">
									</td>
									<td>
										<input id="sum_sourceReceiveMoney" class="tab_input" type="text" readonly="readonly">
									</td>
									<td>
										<input id="sum_sourceCurRecBalance" class="tab_input" type="text" readonly="readonly">
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
							<label class="form-label label_ui label_1">预收款：</label>
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
								<input name="createTime" type="text" class="input-txt input-txt_1" readonly="readonly" value="" id="createDate" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">审 核 人：</label>
							<span class="ui-combo-wrap">
								<input name="checkUserName" type="text" class="input-txt input-txt_1" readonly value="${order.checkUserName}" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">审核日期：</label>
							<span class="ui-combo-wrap">
								<input name="checkTime" type="text" class="input-txt input-txt_1" readonly value="<fmt:formatDate value="${order.checkTime }" type="date" pattern="yyyy-MM-dd" />" />
							</span>
						</dd>

					</dl>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>创建收款核销单</title>
<script type="text/javascript">
	$(function()
	{
		//取消
		$("#btn_cancel").click(function()
		{
			closeTabAndJump("收款核销单列表");
		});
		/* 选择客户 */
		$("#selectCustomer").click(function()
		{
			Helper.popup.show('选择客户', Helper.basePath + '/quick/customer_select', '900', '490');
		});
		/* 选择收款核销源 */
		$("#select_receive_source").click(function()
		{
			var _customerId = $("#customerId").val();
			if (Helper.isEmpty(_customerId))
			{
				Helper.message.alert("请先选择客户");
				$("#customerName").focus();
				return false;
			}
			Helper.popup.show('选择收款核销源', Helper.basePath + '/quick/receive_select?multiple=true&customerId=' + _customerId, '1158', '490');
		});

		/* 删除收款核销源 */
		$("table tbody").on("click", "a[name='btn_del']", function()
		{
			$(this).parent().parent().remove();
			resetSequenceNum();
			sum();
			countOrderMoney();
		});
		/* 修改总折扣 */
		$("#order_discount").on("blur", function()
		{
			if (Number($("#order_discount").val()) > Number($("#sum_money").val()))
			{
				$("#order_discount").val($("#sum_money").val());
			}
			countOrderMoney();
		});
		/* 修改明细收款核销 */
		$("table tbody").on("blur", "input[name='detailList.money']", function()
		{
			sum();
			countOrderMoney();
		});
		// 初始收款核销日期
		$("#billTime").val(new Date().format('yyyy-MM-dd'));

		// 初始化制单日期
		$("#createDate").val(new Date().format('yyyy-MM-dd'));

		//保存
		$("#btn_save,#btn_save_audit").click(function()
		{

			fixEmptyValue();
			if (Helper.isEmpty($("#customerId").val()))
			{
				Helper.message.warn("请选择客户")
				return false;
			}
			if (Helper.isEmpty($("#order_money").val()) || Number($("#order_money").val()) <= 0)
			{
				Helper.message.warn("收款核销金额必须大于0");
				return false;
			}
			if (Number($("#order_money").val()).add(Number($("#order_discount").val())) != Number($("#sum_money").val()))
			{
				Helper.message.warn("金额计算有误，总收款核销不等于明细汇总");
				return false;
			}

			if (Number($("#sum_money").val()) > Number($("#sum_sourceBalanceMoney").val()))
			{
				Helper.message.warn("收款金额不能大于源单未付费金额");
				return false;
			}
			if (Number($("#customer_advanceMoney_after").val()) < 0)
			{
				Helper.message.warn("预收款不足，请修改明细金额");
				return false;
			}
			var paramObj = $("#form_writeoffReceive").formToJson();
			paramObj.isCheck = $(this).attr("id") == "btn_save_audit" ? "YES" : "NO";
			$(this).attr({
				"disabled" : "disabled"
			});
			Helper.request({
				url : Helper.basePath + "/finance/writeoffReceive/save",
				data : paramObj,
				success : function(data)
				{
					if (data.success)
					{
						Helper.message.suc('已保存!');
						location.href = Helper.basePath + '/finance/writeoffReceive/view/' + data.obj.id;
					} else
					{
						Helper.message.warn('保存失败!' + data.message);
						$(this).removeAttr("disabled");
					}
				},
				error : function(data)
				{
					//console.log(data);
				}
			});

		})
	});
	//获取返回收款核销源数组信息
	function getCallInfo_receiveArray(rows)
	{
		if (rows.length > 0)
		{
			appendTr("detailList", rows);
			resetSequenceNum();
		}
		sum();
		countOrderMoney();
		sumCurrRecBalance()
	}

	//绑定"修改本次收款金额"事件
	function sumCurrRecBalance()
	{
		$("#detailList").find("input[name='detailList.money']").off().on('blur', function()
		{
			var currRec = Number($(this).parent().parent().find("input[name='detailList.money']").val()); //本次收款金额
			var unRec = Number($(this).parent().parent().find("input[name='detailList.sourceBalanceMoney']").val()); //未收款金额
			var currRecBalance = unRec - currRec;
			$(this).parent().parent().find("input[name='currentReceiveMoneyBalance']").val(toDecimal2(currRecBalance));
			sum();
		});
	}
	//获取返回信息
	function getCallInfo_customer(obj)
	{
		var old_customerId = $("#customerId").val();
		$("#customerId").val(obj.id);
		$("#customerName").val(obj.name);
		$("#customer_advanceMoney_before").val(obj.advanceMoney);
		$("#customer_advanceMoney_after").val(obj.advanceMoney);
		if (Helper.isNotEmpty(old_customerId) && obj.id != old_customerId)
		{//如果改变客户，则清空table
			//清空table tr
			$("table tbody tr").remove();
		}
		sum();
		countOrderMoney();
	}

	function appendTr(tableId, rows)
	{
		//console.log(rows);
		$.each(rows, function()
		{
			var _THIS = this;
			var _TR = $("<tr/>");
			var sourceDetailIdArray = $("table tbody tr td input[name='detailList.sourceDetailId']").map(function()
			{
				return this.value
			}).get();
			//console.log(sourceDetailIdArray);
			//console.log(this.id);
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
					_TR.append('<td class="td-manage"><input name="detailList.sourceId" type="hidden" readonly="readonly" value="'+_THIS.master.id
								+'"/><input name="detailList.sourceDetailId" type="hidden" readonly="readonly" value="'+_THIS.id
								+'"/><a title="删除行" href="javascript:void(0)" name="btn_del"><i class="delete fa fa-trash-o"></i></a></td>');
					break;
				case 'master.billNo':
					_TR.append('<td><input name="detailList.sourceBillNo" class="tab_input" type="text" readonly="readonly" value="'+value+'"/></td>');
					break;
				case 'master.billType':
					_TR.append('<td><input name="detailList.sourceBillType" type="hidden" value="'+value+'"/>' + _THIS.master.billTypeText + '</td>');
					break;
				case 'master.createTime':
					_TR.append('<td>' + new Date(value).format("yyyy-MM-dd") + '</td>');
					break;
				case 'product':
					_TR.append('<td><input name="detailList.productId" class="tab_input" type="hidden" value="' + (_THIS.productId || "") 
								+ '"/><input name="detailList.productName" readonly="readonly" class="tab_input" type="text" value="' + (_THIS.productName || "") + '"/></td>');
					break;
				case 'procedure':
					_TR.append('<td><input name="detailList.procedureId" class="tab_input" type="hidden" value="' + (_THIS.procedureId || "") 
							+ '"/><input name="detailList.procedureName" class="tab_input" readonly="true"  type="text" value="' + (_THIS.procedureName || "") + '"/></td>');
					break;
				case 'style':
					_TR.append('<td><input name="detailList.style" class="tab_input" readonly="readonly" type="text" value="' + (_THIS.style || "") + '"/></td>');
					break;
				case 'money':
					if (_THIS.master.billType == "SALE_SK" || _THIS.master.billType == "OEM_EC")
					{
						_TR.append('<td><input name="detailList.sourceMoney" class="tab_input" type="text" readonly="readonly" value="'+value+'"/></td>');
					} else if(_THIS.master.billType == "FINANCE_ADJUST")
					{
						_TR.append('<td><input name="detailList.sourceMoney" class="tab_input" type="text" readonly="readonly" value="'+_THIS.adjustMoney+'"/></td>');
					}
					else
					{
						_TR.append('<td><input name="detailList.sourceMoney" class="tab_input" type="text" readonly="readonly" value="'+_THIS.receiveMoney+'"/></td>');
					}
					break;
				case 'receiveMoney':
					if (_THIS.master.billType == "SALE_SK" || _THIS.master.billType == "OEM_EC")
					{
						_TR.append('<td><input name="detailList.sourceReceiveMoney" class="tab_input" type="text" readonly="readonly" value="'+value+'"/></td>');
					} else if (_THIS.master.billType == "FINANCE_ADJUST")
					{
						_TR.append('<td><input name="detailList.sourceReceiveMoney" class="tab_input" type="text" readonly="readonly" value="'+_THIS.receiveOrPayMoney+'"/></td>');
					}
					else
					{
						_TR.append('<td><input name="detailList.sourceReceiveMoney" class="tab_input" type="text" readonly="readonly" value="'+_THIS.receivedMoney+'"/></td>');
					}
					break;
				case 'unReceiveMoney':
					if (_THIS.master.billType == "SALE_SK" || _THIS.master.billType == "OEM_EC")
					{
						_TR.append('<td><input name="detailList.sourceBalanceMoney" class="tab_input" type="text" readonly="readonly" value="' + toDecimal2(Number(_THIS.money).subtr(Number(_THIS.receiveMoney))) + '"/></td>');
					} else if (_THIS.master.billType == "FINANCE_ADJUST")
					{
						_TR.append('<td><input name="detailList.sourceBalanceMoney" class="tab_input" type="text" readonly="readonly" value="' + toDecimal2(Number(_THIS.adjustMoney).subtr(Number(_THIS.receiveOrPayMoney))) + '"/></td>');
					} else
					{
						_TR.append('<td><input name="detailList.sourceBalanceMoney" class="tab_input" type="text" readonly="readonly" value="' + toDecimal2(Number(_THIS.receiveMoney).subtr(Number(_THIS.receivedMoney))) + '"/></td>');
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
					}  else
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
		$("#sum_money").val(toDecimal2(sum_money));

	}
	function countOrderMoney()
	{
		$("#order_money").val(Number($("#sum_money").val()).subtr(Number($("#order_discount").val())));
		$("#customer_advanceMoney_after").val(Number($("#customer_advanceMoney_before").val()).subtr(Number($("#order_money").val())));

		if (Number($("#customer_advanceMoney_after")) < 0)
		{
			$("#customer_advanceMoney_after").css({
				"color" : "red"
			});
		} else
		{
			$("#customer_advanceMoney_after").attr({
				"color" : ""
			});
		}
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
					<sys:nav struct="财务管理-收款核销单-创建"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="finance:writeoffReceive:create">
						<button class="nav_btn table_nav_btn" id="btn_save">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="finance:writeoffReceive:create,finance:writeoffReceive:audit">
						<button class="nav_btn table_nav_btn" id="btn_save_audit">保存并审核</button>
					</shiro:hasManyPermissions>
					<button class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
				</div>
			</div>
			<form id="form_writeoffReceive">
				<!--主表-订单表单-->
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_3">客户名称：</label>
									<span class="ui-combo-wrap wrap-width">
										<input type="text" readonly class="input-txt input-txt_7" name="customerName" id="customerName" />
										<div class="select-btn" id="selectCustomer">...</div>
										<input type="hidden" readonly name="customerId" id="customerId" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_3">核销日期：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_1 Wdate" onFocus="WdatePicker({lang:'zh-cn'})" name="billTime" id="billTime" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_3">核销人员：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('EMPLOYEE')}" valueProperty="id" textProperty="name" name="employeeId" cssClass="input-txt input-txt_1 hy_select2" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_3">使用前预收款：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_16" readonly="readonly" id="customer_advanceMoney_before" value="0" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_3">使用后预收款：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_1" readonly="readonly" id="customer_advanceMoney_after" value="0" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_3">本次核销金额：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_1" readonly="readonly" name="money" id="order_money" value="0" />
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
									<label class="form-label label_ui label_3">备 注：</label>
									<span class="form_textarea">
										<textarea class="noborder" name="memo" style="width: 779px" placeholder="说点什么...100个字符以内" onKeyUp="Helper.doms.textarealength(this,100)"></textarea>
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
				</div>
				<!--按钮栏End-->

				<!--从表-订单表格-->
				<div>
					<div class="table-container">
						<table class="border-table" style="width: 1250px" id="detailList">
							<thead>
								<tr>
									<th width="40" name="seq">序号</th>
									<th width="40" name="operator">操作</th>
									<th width="120" name="master.billNo">源单编号</th>
									<th width="100" name="master.billType">源单类型</th>
									<th width="80" name="master.createTime">制单日期</th>
									<th width="140" name="product">成品名称</th>
									<th width="140" name="procedure">成品名称</th>
									<th width="100" name="style">产品规格</th>
									<th width="100" name="money">单据金额</th>
									<th width="100" name="receiveMoney">已收款金额</th>
									<th width="100" name="unReceiveMoney">未收款金额</th>
									<th width="100" name="currentReceiveMoneyBalance">本次收款余额</th>
									<th width="120" name="currentReceiveMoney">本次收款金额</th>
								</tr>
							</thead>
							<tbody>
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
										<input id="sum_sourceBalanceMoney" class="tab_input" type="text" readonly="readonly">
									</td>
									<td>
										<input id="sum_sourceCurRecBalance" class="tab_input" type="text" readonly="readonly">
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
							<label class="form-label label_ui label_3">制 单 人：</label>
							<span class="ui-combo-wrap">
								<input name="createName" type="text" class="input-txt input-txt_1" readonly value="${loginUser.realName}" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">制单日期：</label>
							<span class="ui-combo-wrap">
								<input name="createTime" type="text" class="input-txt input-txt_1" readonly="readonly" value="<fmt:formatDate value="${order.createTime }" type="date" pattern="yyyy-MM-dd" />" id="createDate" />
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
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>创建其它付款单</title>
<script type="text/javascript">
	$(function()
	{
		//取消
		$("#btn_cancel").click(function()
		{
			closeTabAndJump("其它付款单列表");
		});

		// 初始付款日期
		$("#billTime").val(new Date().format('yyyy-MM-dd'));

		// 初始化制单日期
		$("#createDate").val(new Date().format('yyyy-MM-dd'));

		//增加一行
		$("#add").click(function()
		{
			appendTr();
		})
		//删除一行
		$("table").on("click", ".delete_row", function()
		{
			$(this).parent().parent().remove();
			setRowIndex();
			$("#detailList").find("input[name='detailList.money']").trigger('blur');
		});

		//保存
		$("#btn_save,#btn_save_audit").click(function()
		{
			fixEmptyValue();
			if (Helper.isEmpty($("#recCompany").val()))
			{
				Helper.message.warn("请输入收款单位");
				return false;
			}
			if (Helper.isEmpty($("#accountId").val()) || $("#accountId").val() == -1 || $("#accountId").val() == -99)
			{
				Helper.message.warn("保存失败！账户信息不能空，请添加账户信息");
				return false;
			}
			if (Number($("#order_money").val()) <=0)
			{
				Helper.message.warn("付款金额必须大于0");
				return false;
			}
			$(this).attr({
				"disabled" : "disabled"
			});
			var paramObj = $("#form_otherPayment").formToJson();
			paramObj.isCheck = $(this).attr("id") == "btn_save_audit" ? "YES" : "NO";
			form_submit(paramObj);
		});
		//计算总付款金额
		$("table tbody").on("blur", "input[name='detailList.money']", function()
		{
			var money = 0;
			$.each($("table tbody input[name='detailList.money']"), function(index, value)
			{
				var $this = $(this);
				money += Number($this.val()).tomoney();
			});
			$("#order_money").val(money);
		});
	});

	//增加一行
	function appendTr()
	{
		var str = '<tr><td name="rowIndex">1</td>' + '<td class="td-manage">' 
		+ '<i class="delete_row fa fa-trash-o"></i></td>' 
		+ '<td><input name="detailList.summary" class="tab_input" type="text" /></td>' 
		+ '<td><input name="detailList.money" class="tab_input constraint_decimal_negative " type="text" value="0" /></td>'
		+ '<td><input name="detailList.memo" class="tab_input bg_color memo" onmouseover="this.title=this.value" type="text" /></td>'
		+ '</tr>';
		$("#tbody").append(str);
		setRowIndex();
	}

	function setRowIndex()
	{
		var rowList = $("td[name='rowIndex']")
		for (var i = 0; i < rowList.length; i++)
		{
			rowList.eq(i).text(i + 1);
		}

	}

	function form_submit(paramObj)
	{
		Helper.request({
			url : Helper.basePath + "/finance/otherPayment/save",
			data : paramObj,//将form序列化成JSON字符串  
			success : function(data)
			{
				if (data.success)
				{
					Helper.message.suc('已保存!');
					location.href = Helper.basePath + '/finance/otherPayment/view/' + data.obj.id;
				} else
				{
					Helper.message.warn('保存失败!' + data.message);
				}
			},
			error : function(data)
			{
				//console.log(data);
			}
		});
	}
</script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="财务管理-其它付款单-创建"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="finance:otherPayment:create">
						<button class="nav_btn table_nav_btn" id="btn_save">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="finance:otherPayment:create,finance:otherPayment:audit">
						<button class="nav_btn table_nav_btn" id="btn_save_audit">保存并审核</button>
					</shiro:hasManyPermissions>
					<button class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
				</div>
			</div>
			<form id="form_otherPayment">
				<!--主表-订单表单-->
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl" style="width: 1250px">
								<dd class="row-dd">
									<label class="form-label label_ui label_6">收款单位：</label>
									<span class="ui-combo-wrap wrap-width">
										<input type="text" class="input-txt input-txt_7" name="recCompany" id="recCompany" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">账 号：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('ACCOUNT')}" valueProperty="id" textProperty="bankNo" name="accountId" cssClass="input-txt input-txt_7 hy_select2" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">付 款 人：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('EMPLOYEE')}" valueProperty="id" textProperty="name" name="employeeId" cssClass="input-txt input-txt_1 hy_select2" />
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
									<label class="form-label label_ui label_1">付款金额：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="constraint_decimal_negative input-txt input-txt_1" readOnly name="money" id="order_money" value="0.00" />
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
					<a id="add" href="javascript:;" class="nav_btn table_nav_btn">
						<i class="fa fa-plus-square"></i>
						增加一行
					</a>
				</div>
				<!--按钮栏End-->

				<!--从表-订单表格-->
				<div>
					<div class="table-container">
						<table class="border-table" style="" id="detailList">
							<thead>
								<tr>
									<th width="5%" name="seq">序号</th>
									<th width="10%" name="operator">操作</th>
									<th width="50%" name="summary">摘要</th>
									<th width="10%">付款金额</th>
									<th width="25%">备注</th>
								</tr>
							</thead>
							<tbody id="tbody">

							</tbody>
						</table>
					</div>
				</div>
				<div class="cl form_content">
					<dl class="cl row-dl-foot">
						<dd class="row-dd">
							<label class="form-label label_ui label_6">制 单 人：</label>
							<span class="ui-combo-wrap">
								<input name="createName" type="text" class="input-txt input-txt_6" readonly value="${loginUser.realName}" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_6">制单日期：</label>
							<span class="ui-combo-wrap">
								<input name="createTime" type="text" class="input-txt input-txt_6" readonly="readonly" id="createDate" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_6">审 核 人：</label>
							<span class="ui-combo-wrap">
								<input name="checkUserName" type="text" class="input-txt input-txt_6" readonly="readonly" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_6">审核日期：</label>
							<span class="ui-combo-wrap">
								<input name="checkTime" type="text" class="input-txt input-txt_6" readonly="readonly" />
							</span>
						</dd>

					</dl>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<meta charset="UTF-8">
<title>财务调整</title>
<script type="text/javascript">
	$(function()
	{
		$("#btn_cancel").click(function()
		{
			closeTabAndJump("财务调整列表");
		});
		setRowIndex();
		//保存
		$(document).on("click", "#btn_save,#btn_save_audit", function()
		{
			fixEmptyValue();
			var validate = true;
			if ($("#tbody input[name='detailList.businessId']").size() <= 0)
			{
				Helper.message.warn("请录入明细");
				validate = false;
				return;
			}

			if (validate)
			{
				$("#tbody input[name='detailList.adjustMoney']").each(function()
				{
					if (Helper.isEmpty($(this).val()) || Number($(this).val()) == 0)
					{
						Helper.message.warn("请填写非0的调整金额");
						$(this).focus();
						validate = false;
						return false;
					}
				});
			}
			if (validate)
			{//保存
				$("#isCheck").val($(this).attr("id") == "btn_save_audit" ? "YES" : "NO");
				form_submit();
			}
		});

		// 初始化调整日期
		$("#adjustTime").val(new Date().format('yyyy-MM-dd'));

		$("#business_quick_select").click(function()
		{
			var adjustType = $("#adjustType").val();
			if (adjustType == 'PAY')
			{
				Helper.popup.show('选择供应商', Helper.basePath + '/quick/supplier_select?multiple=true', '900', '490');
			} else if (adjustType == 'RECEIVE')
			{
				Helper.popup.show('选择客户', Helper.basePath + '/quick/customer_select?multiple=true', '900', '490');
			} else
			{
				Helper.popup.show('选择账户', Helper.basePath + '/quick/account_select?multiple=true', '800', '490');
			}
		});

		$("table").on("click", ".delete_row", function()
		{
			$(this).parent().parent().parent().remove();
			setRowIndex();
		});

		//选调整人设置调整人姓名
		$(document).on("change", "#employeeId", function()
		{
			$("#employeeName").val($(this).find("option:selected").text())
		})
		// 切换调整类型
		$("#adjustType").change(function()
		{
			var val = $(this).val();
			if (val == "PAY")
			{
				$("#btnName").text("选择供应商");
			} else if (val == "RECEIVE")
			{
				$("#btnName").text("选择客户");
			} else
			{
				$("#btnName").text("选择账户");
			}
			// 切换调整类型时需要清空明细表的记录
			$("#tbody tr").remove();
		});
	})

	// 重新设置行号
	function setRowIndex()
	{
		var rowList = $("td[name='rowIndex']")
		for (var i = 0; i < rowList.length; i++)
		{
			rowList.eq(i).text(i + 1);
		}
	}

	function form_submit()
	{
		var data = $("#form_adjust").formToJson();
		// 当没有选中员工时保存时会报错
		if (data['employeeId'] < 0)
		{
			delete data['employeeId'];
		}
		Helper.request({
			url : Helper.basePath + "/finance/adjust/save",
			data : data,
			success : function(data)
			{
				if (data.success)
				{
					Helper.message.suc('已保存!');
					location.href = Helper.basePath + '/finance/adjust/view/' + data.obj.id;
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

	//多选获取返回信息
	function getCallInfo_business(rows, BUSINESSTYPE)
	{
		$.each(rows, function()
		{
			var _THIS = this;
			var idArray = $("#tbody tr td input[name='detailList.businessId']").map(function()
			{ // 判断是否重复，需外加判断是否同类型商家
				if ($(this).parents("tr").find("input[name='detailList.businessType']").val() == BUSINESSTYPE)
				{
					return this.value;
				}
			}).get();
			//判断是否已存在商家ID和商检类型
			if (Helper.isNotEmpty(idArray) && idArray.contains("" + this.id))
			{//如果已存在跳出本次循环
				return true;
			}
			var copyTr = $("table.cloneTable").find("tr").clone(true);
			// 商家类型
			var businessTypeText = Helper.basic.getEnumText('com.huayin.printmanager.persist.enumerate.AdjustType', BUSINESSTYPE, 'target');
			copyTr.find("input[name='detailList.businessTypeText']").val(businessTypeText);
			copyTr.find("input[name='detailList.businessType']").val(BUSINESSTYPE);
			// 客户/供应商编码
			copyTr.find("input[name='detailList.businessCode']").val(_THIS.code);
			// 客户/供应商/账户id
			copyTr.find("input[name='detailList.businessId']").val(_THIS.id);
			// 客户/供应商/账户名称
			copyTr.find("input[name='detailList.businessName']").val(_THIS.name);
			$("table.border-table tbody").append(copyTr);
		});
		setRowIndex();
	}
	// 选择供应商后回调方法
	function getCallInfo_supplierArray(rows)
	{
		getCallInfo_business(rows, 'PAY');
	}
	// 选择客户后回调方法
	function getCallInfo_customerArray(rows)
	{
		getCallInfo_business(rows, 'RECEIVE');
	}
	// 选择账户后回调方法
	function getCallInfo_accountArray(rows)
	{
		var _rows = [];console.log(rows)
		$.each(rows, function()
		{
			var this_ = this;
			var row = {};
			row.id = this_.id;
			row.name = this_.branchName;
			row.code = this_.bankNo;
			_rows.push(row);
		})
		console.log(_rows);
		getCallInfo_business(_rows, 'ACCOUNT');
	}
</script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="财务管理-财务调整-创建"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="finance:adjust:create">
						<button id="btn_save" class="nav_btn table_nav_btn">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="finance:adjust:create,finance:adjust:audit">
						<button class="nav_btn table_nav_btn" id="btn_save_audit">保存并审核</button>
					</shiro:hasManyPermissions>
					<button class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
				</div>
			</div>
			<form id="form_adjust" method="post">
				<input type="hidden" name="isCheck" id="isCheck" />
				<!--主表-订单表单-->
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">调整类型：</label>
									<phtml:list name="adjustType" textProperty="text" valueProperty="value" cssClass="input-txt input-txt_1 hy_select"
									 type="com.huayin.printmanager.persist.enumerate.AdjustType"></phtml:list>
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">调整单号：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_3s" readonly="readonly" value="${billNo }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">调整日期：</label>
									<span class="ui-combo-wrap">
										<input id="adjustTime" name="adjustTime" type="text" class="input-txt input-txt_1 Wdate" onFocus="WdatePicker({lang:'zh-cn'})" value="" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">调 整 人：</label>
									<span class="ui-combo-noborder">
										<phtml:list items="${fns:basicList2('EMPLOYEE')}" valueProperty="id" cssClass="input-txt input-txt_1 hy_select2" name="employeeId" textProperty="name"></phtml:list>
										<input id="employeeName" name="employeeName" type="hidden" value="${fns:basicList('EMPLOYEE')[0].name}" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">制 单 人：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_1" readonly="readonly" value="" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">制单日期：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_3s" readonly="readonly" value="" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">备注：</label>
									<span class="form_textarea">
										<textarea name="memo" class="noborder" style="width: 996px" placeholder="说点什么...100个字符以内" onKeyUp="Helper.doms.textarealength(this,100)"></textarea>
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
					<button type="button" id="business_quick_select" class="nav_btn table_nav_btn">
						<i class="fa fa-plus-square"></i>
						<span id="btnName">选择客户</span>
					</button>
				</div>
				<!--按钮栏End-->
				<!--从表-订单表格-->
				<div>
					<div class="table-container">
						<table class="border-table" style="width: 1000px">
							<thead>
								<tr>
									<th width="40">序号</th>
									<th width="40">操作</th>
									<th width="80">调整类型</th>
									<th width="140" class="code">对象编号</th>
									<th width="160" class="name">调整对象</th>
									<th width="100" class="adjustMoney">调整金额</th>
									<th width="200" class="reason">调整事由</th>
									<th width="200">备注</th>
								</tr>
							</thead>
							<tbody id="tbody">

							</tbody>
						</table>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
<div style="display: none;">
	<table class="cloneTable">
		<tr>
			<td name="rowIndex"></td>
			<td>
				<a title="删除行" href="javascript:void(0)">
					<i class="delete fa fa-trash-o delete_row"></i>
				</a>
			</td>
			<td>
				<input readonly="readonly" type="hidden" name="detailList.businessType" value="" />
				<input class="tab_input" readonly="readonly" type="text" name="detailList.businessTypeText" value="" />
			</td>
			<td>
				<input class="tab_input" readonly="readonly" type="text" name="detailList.businessCode" value="" />
			</td>
			<td>
				<input class="tab_input" type="hidden" name="detailList.businessId" value="" />
				<input class="tab_input" readonly="readonly" type="text" name="detailList.businessName" value="" />
			</td>
			<td>
				<input class="tab_input bg_color constraint_decimal" type="text" name="detailList.adjustMoney" value="" />
			</td>
			<td>
				<input class="tab_input bg_color memo" alt="调整事由" type="text" name="detailList.reason" value="" />
			</td>
			<td>
				<input class="tab_input bg_color memo" type="text" name="detailList.memo" value="" />
			</td>
		</tr>
	</table>
</div>
</html>

/**
 * Author:		   think
 * Create:	 	   2018年1月5日 上午10:30:08
 * Copyright: 	 Copyright (c) 2018
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 */
$(function()
{
	$("#btn_cancel").click(function()
	{
		closeTabAndJump("账户期初列表");
	});
	setRowIndex();
	// 保存
	$(document).on("click", "#btn_save,#btn_save_audit", function()
	{
		var validate = true;
		if ($("table input[name='detailList.beginMoney']").size() <= 0)
		{
			Helper.message.warn("请录入明细");
			validate = false;
			return;
		}
		$("table input[name='detailList.beginMoney']").each(function()
		{
			if (Helper.isEmpty($(this).val()) || Number($(this).val()) <= 0)
			{
				Helper.message.warn("请录入金额");
				validate = false;
				return;
			}
		});
		if (validate)
		{// 保存
			$("#isCheck").val($(this).attr("id") == "btn_save_audit" ? "YES" : "NO");
			form_submit();
		}
	});

	// 初始化期初日期
	$("#beginTime").val(new Date().format('yyyy-MM-dd'));
	// 删除一行数据
	$("table").on("click", ".row_delete", function()
	{
		$(this).parent().parent().remove();
		setRowIndex();
	});

	$("#account_quick_select").click(function()
	{
		Helper.popup.show('选择账户', Helper.basePath + '/quick/account_select?multiple=true&isBegin=false', '800', '490');
	});

})

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
	Helper.request({
		url : Helper.basePath + "/begin/account/save",
		data : $("#form_order").formToJson(),// 将form序列化成JSON字符串
		success : function(data)
		{
			if (data.success)
			{
				Helper.message.suc('已保存!');
				location.href = Helper.basePath + '/begin/account/view/' + data.obj.id;
			} else
			{
				Helper.message.warn('保存失败!' + data.message);
			}
		},
		error : function(data)
		{
			// console.log(data);
		}
	});
}

function getCallInfo_accountArray(rows)
{
	$.each(rows, function()
	{
		if ($(".id_" + this.id) != undefined)
		{
			$(".id_" + this.id).parent().parent().remove();
		}
		var str = '<tr><td name="rowIndex">1</td>' + '<td class="td-manage"><a title="删除行" href="javascript:void(0)" class="row_delete"><i class="delete fa fa-trash-o"></i></a></td>' + '<td><input name="detailList.bankNo" class="tab_input" type="text" readonly="readonly" value="' + this.bankNo + '"/></td>' + '<td><input name="detailList.accountId" class="tab_input id_' + this.id + '" type="hidden" value="' + this.id + '"/><input name="detailList.branchName" class="tab_input" readonly="readonly" onmouseover="this.title=this.value" type="text" value="' + this.branchName + '"/></td>' + '<td><input name="detailList.currencyType" class="tab_input" type="hidden" value="' + this.currencyType + '"/>'
				+ '<input name="detailList.currencyTypeText" class="tab_input" type="text" readonly="readonly" value="' + Helper.basic.getEnumText("com.huayin.printmanager.persist.enumerate.CurrencyType", this.currencyType, "text") + '"/></td>' + '<td><input name="detailList.beginMoney" class="tab_input constraint_decimal bg_color" type="text" value=""/></td>' + '<td><input name="detailList.memo" class="tab_input bg_color memo" onmouseover="this.title=this.value" type="text" value=""/></td>'
		$("#tbody").append(str);

	});
	setRowIndex();
}
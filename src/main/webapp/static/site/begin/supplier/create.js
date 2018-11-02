/**
 * Author:		   think
 * Create:	 	   2018年1月5日 上午10:53:22
 * Copyright: 	 Copyright (c) 2018
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 */
$(function()
{
	$("#btn_cancel").click(function()
	{
		closeTabAndJump("供应商期初列表");
	});
	setRowIndex();
	
	// 保存
	$(document).on("click", "#btn_save,#btn_save_audit", function()
	{
		var validate = true;
		if ($("table input[name='detailList.paymentMoney']").size() <= 0)
		{
			Helper.message.warn("请录入明细");
			validate = false;
			return;
		}
		$("table input[name='detailList.paymentMoney']").each(function()
		{
			if (Helper.isEmpty($(this).val()) || Number($(this).val()) <= 0)
			{
				Helper.message.warn("应付款不能为0");
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

	$("#supplier_quick_select").click(function()
	{
		Helper.popup.show('选择供应商', Helper.basePath + '/quick/supplier_select?multiple=true&&isBegin=YES', '900', '490');
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
		url : Helper.basePath + "/begin/supplier/save",
		data : $("#form_order").formToJson(),// 将form序列化成JSON字符串
		success : function(data)
		{
			if (data.success)
			{
				Helper.message.suc('已保存!');
				location.href = Helper.basePath + '/begin/supplier/view/' + data.obj.id;
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

function cancelReturn()
{
	history.go(-1);
}

function getCallInfo_supplierArray(rows)
{
	nowTime = new Date().format('yyyy-MM-dd');
	$.each(rows, function()
	{
		if ($(".id_" + this.id) != undefined)
		{
			$(".id_" + this.id).parent().parent().remove();
		}
		var str = '<tr><td name="rowIndex">1</td>' + '<td class="td-manage"><a title="删除行" href="javascript:void(0)" class="row_delete"><i class="delete fa fa-trash-o"></i></a></td>' + '<td><input name="detailList.receiveTime" class="tab_input" type="text" readonly="readonly" onFocus="WdatePicker({lang:\'zh-cn\'})" value="' + nowTime + '"/></td>' + '<td><input name="detailList.supplierCode" class="tab_input" type="text" readonly="readonly" value="' + this.code + '"/></td>' + '<td><input name="detailList.supplierId" class="tab_input id_' + this.id + '" type="hidden" value="' + this.id + '"/><input name="detailList.supplierName" class="tab_input" type="text" value="' + this.name + '"/></td>'
				+ '<td><input name="detailList.currencyType" class="tab_input" type="hidden" value="' + this.currencyType + '"/>' + '<input name="detailList.currencyTypeText" class="tab_input" type="text" readonly="readonly" value="' + (Helper.basic.getEnumText("com.huayin.printmanager.persist.enumerate.CurrencyType", this.currencyType, "text") || " ") + '"/></td>' + '<td><input name="detailList.paymentMoney" class="tab_input constraint_decimal bg_color" type="text" value=""/></td>' + '<td><input name="detailList.advanceMoney" class="tab_input constraint_decimal bg_color" type="text" value=""/></td>'
				+ '<td><input name="detailList.memo" class="tab_input bg_color memo" onmouseover="this.title=this.value" type="text" value=""/></td>'
		$("#tbody").append(str);

	});
	setRowIndex();
}

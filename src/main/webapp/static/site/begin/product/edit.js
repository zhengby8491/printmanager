/**
 * Author:		   think
 * Create:	 	   2018年1月5日 上午10:53:41
 * Copyright: 	 Copyright (c) 2018
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 */
$(function()
{
	$("#btn_cancel").click(function()
	{
		location.href = Helper.basePath + '/begin/product/view/' + $("input[name='id']").val();
	});
	setRowIndex();
	// 保存
	$(document).on("click", "#btn_save,#btn_save_audit", function()
	{
		fixEmptyValue();
		var validate = true;
		if ($("#warehouseId").val() < 0)
		{
			Helper.message.warn("请选择仓库");
			validate = false;
			return;
		}
		if ($("table input[name='detailList.qty']").size() <= 0)
		{
			Helper.message.warn("请录入明细");
			validate = false;
			return;
		}
		$("table input[name='detailList.money']").each(function()
		{
			// console.log("qty="+$(this).val());
			if (Helper.isEmpty($(this).val()) || Number($(this).val()) <= 0)
			{
				Helper.message.warn("金额不能为0");
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
	// $("#beginTime").val(new Date().format('yyyy-MM-dd'));
	// 删除一行数据
	$("table").on("click", ".row_delete", function()
	{
		$(this).parent().parent().remove();
		setRowIndex();
	});

	$("#product_quick_select").click(function()
	{
		Helper.popup.show('选择产品', Helper.basePath + '/quick/product_select?multiple=true&&isBegin=YES&&warehouseId=' + $("#warehouseId").val(), '900', '490');
	});

	$("table").on("input", "input[name='detailList.qty']", function()
	{
		countMoney($(this));
	});

	$("table").on("input", "input[name='detailList.price']", function()
	{
		countMoney($(this));
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
		url : Helper.basePath + "/begin/product/update",
		data : $("#form_order").formToJson(),// 将form序列化成JSON字符串
		success : function(data)
		{
			if (data.success)
			{
				Helper.message.suc('已保存!');
				location.href = Helper.basePath + '/begin/product/view/' + data.obj.id;
			} else
			{
				Helper.message.warn(data.message);
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

function getCallInfo_productArray(rows)
{
	$.each(rows, function()
	{
		if ($(".id_" + this.id) != undefined)
		{
			$(".id_" + this.id).parent().parent().remove();
		}
		var str = '<tr><td name="rowIndex">1</td>' 
				+ '<td class="td-manage"><a title="删除行" href="javascript:void(0)" class="row_delete"><i class="delete fa fa-trash-o"></i></a></td>' 
				+ '<td><input readonly="readonly" name="detailList.productCode" class="tab_input" type="text" readonly="readonly" value="' + this.code + '"/></td>' 
				+ '<td><input readonly="readonly" name="detailList.customerMaterialCode" class="tab_input" type="text" readonly="readonly" value="' + this.customerMaterialCode + '"/></td>'
				+ '<td><input name="detailList.productId" class="tab_input id_' + this.id + '" type="hidden" value="' + this.id + '"/><input readonly="readonly" name="detailList.productName" class="tab_input" type="text" value="' + this.name + '"/></td>' + '<td><input readonly="readonly" name="detailList.specifications" class="tab_input" type="text" value="' + (this.specifications || "") + '"/></td>'
				+ '<td><input readonly="readonly" name="detailList.unitName" class="tab_input" type="text" value="' + Helper.basic.info('UNIT', this.unitId).name + '"/><input name="detailList.unit" class="tab_input" type="hidden" value="' + this.unitId + '"/></td>' + '<td><input name="detailList.qty" class="tab_input constraint_negative bg_color" type="text" value=""/></td>' + '<td><input name="detailList.price" class="tab_input constraint_decimal bg_color" type="text" value="' + (this.salePrice || "") + '"/></td>' + '<td><input readonly="readonly" name="detailList.money" class="tab_input" type="text" value=""/></td>'
				+ '<td><input name="detailList.memo" class="tab_input bg_color memo" onmouseover="this.title=this.value" type="text" value=""/></td>'
		$("#tbody").append(str);

	});
	setRowIndex();
}

function countMoney(this_)
{
	var tr_dom = this_.parent().parent();
	var qty = tr_dom.find("input[name='detailList.qty']").val();
	var price = tr_dom.find("input[name='detailList.price']").val();
	if (qty == "" || price == "")
	{
		return;
	}
	var money = Number(qty).mul(price).tomoney();
	tr_dom.find("input[name='detailList.money']").val(money);
}

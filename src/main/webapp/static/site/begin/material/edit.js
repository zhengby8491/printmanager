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
		location.href = Helper.basePath + '/begin/material/view/' + $("input[name='id']").val();
	});
	setRowIndex();
	// 保存
	$(document).on("click", "#btn_save,#btn_save_audit", function()
	{
		fixEmptyValue();
		var this_ = $(this);
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
			if (Helper.isEmpty($(this).val()) || Number($(this).val()) <= 0)
			{
				Helper.message.warn("金额不能为0");
				validate = false;
				return;
			}
		});
		if (validate)
		{// 保存
			var isSave = false;// 是否弹出空规格提示
			var isEqual = false;// 是否有不同的单位
			$("input[name='detailList.specifications']").each(function()
			{
				if (Helper.isEmpty($(this).val()))
				{
					var tr_dom = $(this).parent().parent();
					var valuationUnitName = tr_dom.find("input[name='detailList.valuationUnitName']").val();
					var stockUnitName = tr_dom.find("input[name='detailList.stockUnitName']").val();
					if (valuationUnitName != stockUnitName)
					{
						isEqual = true;
					}
					isSave = true;
				}
			});
			if (isSave)
			{
				Helper.message.confirm('存在空的材料规格，是否保存', function(index)
				{
					if (isEqual == true)
					{
						Helper.message.warn("有需要单位换算的材料，请输入规格进行换算");
						return;
					} else
					{
						$("#isCheck").val(this_.attr("id") == "btn_save_audit" ? "YES" : "NO");
						form_submit();
					}
				})
			} else
			{
				$("#isCheck").val(this_.attr("id") == "btn_save_audit" ? "YES" : "NO");
				form_submit();
			}
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

	$("#material_quick_select").click(function()
	{
		Helper.popup.show('选择材料', Helper.basePath + '/quick/material_select?multiple=true', '900', '490');
	});
	selectInit();

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
		url : Helper.basePath + "/begin/material/update",
		data : $("#form_order").formToJson(),// 将form序列化成JSON字符串
		success : function(data)
		{
			if (data.success)
			{
				if (data.obj.id)
				{
					Helper.message.suc('已保存!');
					location.href = Helper.basePath + '/begin/material/view/' + data.obj.id;
				} else
				{
					var str = '保存失败,以下材料已作期初\n';
					for (var i = 0; i < data.obj.length; i++)
					{
						str = str + '名称:' + data.obj[i].materialName + '   规格' + data.obj[i].specifications + '\n';
					}
					layer.alert(str)
				}
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
function getCallInfo_materialArray(rows)
{
	for (var i = 0; i < rows.length; i++)
	{
		var obj = rows[i];
		var str = '<tr><td name="rowIndex">1</td>' + '<td class="td-manage"><a title="删除行" href="javascript:void(0)" class="row_delete"><i class="delete fa fa-trash-o"></i></a></td>' + '<td><input name="detailList.materialCode" class="tab_input id_' + obj.id + '" type="text" readonly="readonly" value="' + obj.code + '"/><input name="detailList.materialClassId" class="tab_input" type="hidden" value="' + obj.materialClassId + '"/></td>' + '<td><input name="detailList.materialName" class="tab_input" type="text" readonly="readonly" value="' + obj.name + '"/><input name="detailList.materialId" class="tab_input" type="hidden" readonly="readonly" value="' + obj.id + '"/></td>'
				+ '<td><input name="detailList.specifications" class="tab_input bg_color" type="text" value=""/></td>' + '<td><input name="detailList.weight" class="tab_input" type="text" readonly="readonly" value="' + obj.weight + '"/></td>' + '<td><input name="detailList.stockUnitId" class="tab_input" type="hidden" readonly="readonly" value="' + obj.stockUnitId + '"/><input name="detailList.stockUnitName" class="tab_input" type="text" readonly="readonly" value="' + Helper.basic.info('UNIT', obj.stockUnitId).name + '"/></td>' + '<td><input name="detailList.qty" class="tab_input constraint_decimal bg_color" type="text" value=""/></td>'
				+ '<td><input name="detailList.valuationPrice" class="tab_input constraint_decimal bg_color" type="text" value="' + (obj.lastPurchPrice || 0) + '"/></td>' + '<td><input name="detailList.valuationUnitId" class="tab_input" type="hidden" readonly="readonly" value="' + obj.valuationUnitId + '"/><input name="detailList.valuationUnitName" class="tab_input" type="text" readonly="readonly" value="' + Helper.basic.info('UNIT', obj.valuationUnitId).name + '"/></td>' + '<td><input name="detailList.valuationQty" class="tab_input" type="text" readonly="readonly" value=""/><input name="valuationUnitAccuracy" class="tab_input" type="hidden" readonly="readonly" value="'
				+ Helper.basic.info('UNIT', obj.valuationUnitId).accuracy + '"/></td>' + '<td><input name="detailList.price" class="tab_input" readonly="readonly" type="text" value=""/></td>' + '<td><input name="detailList.money" class="tab_input" type="text" readonly="readonly" value=""/><input name="stockUnitAccuracy" class="tab_input" type="hidden" readonly="readonly" value="' + Helper.basic.info('UNIT', obj.stockUnitId).accuracy + '"/></td>' + '<td><input name="detailList.memo" class="tab_input bg_color memo" onmouseover="this.title=this.value" type="text" value=""/><input name="valuationUnitAccuracy" class="tab_input" type="hidden" readonly="readonly" value="'
				+ Helper.basic.info('UNIT', obj.valuationUnitId).accuracy + '"/></td></tr>'
		$("#tbody").append(str);
	}
	setRowIndex();
}
function selectInit()
{
	var select_id = $("#warehouse_hid").val();
	var select_value = Helper.basic.info('WAREHOUSE', select_id).name;
	$("#warehouseId").prepend("<option value='" + select_id + "'>" + select_value + "</option>");
	$("#warehouseId").val(select_id);
}
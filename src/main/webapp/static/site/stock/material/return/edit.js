/* 判断当前用户是否有权限查看金额 */
var hasPermission = Helper.basic.hasPermission('stock:materialReturn:money');
$(function()
{
	if (!hasPermission)
	{
		$("th[name='valuationPrice']").hide();
		$("th[name='price']").hide();
		$("th[name='money']").hide();
		$("td").has("input[name='detailList.valuationPrice']").hide();
		$("td").has("input[name='detailList.price']").hide();
		$("td").has("input[name='detailList.money']").hide();
	}
	$("#btn_cancel").click(function()
	{
		location.href = Helper.basePath + '/stockmaterial/return/view/' + $("input[name='id']").val();
	});
	$("#material_quick_select").click(function()
	{
		Helper.popup.show('选择材料', Helper.basePath + '/quick/material_select?multiple=true', '900', '490');
	});

	// $("#detailList").bootstrapTable('append', {code:1});
	$("table").on("click", ".delete_row", function()
	{
		$(this).parent().parent().remove();
		setRowIndex();
	});
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
		if ($("#workBillNo").val() == "")
		{
			Helper.message.warn("请录入生产单号");
			validate = false;
			return;
		}
		if ($("table input[name='detailList.qty']").size() <= 0)
		{
			Helper.message.warn("请录入明细");
			validate = false;
			return;
		}
		$("table input[name='detailList.qty']").each(function()
		{
			// console.log("qty="+$(this).val());
			if (Helper.isEmpty($(this).val()) || Number($(this).val()) <= 0)
			{
				Helper.message.warn("数量必须大于0");
				validate = false;
				return;
			}
		});
		$("table input[name='detailList.valuationPrice']").each(function()
		{
			if (Helper.isEmpty($(this).val()))
			{
				Helper.message.warn("请录入计价单价");
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
					var purchUnitName = tr_dom.find("input[name='detailList.purchUnitName']").val();
					if (valuationUnitName != purchUnitName)
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

	$("#workBillNo_quick_select").click(function()
	{
		Helper.popup.show('选择工单', Helper.basePath + '/produce/work/quick_select_work', '800', '490');
	});

})

// 获取返回信息
function getCallInfo_materialArray(rows)
{
	$.each(rows, function()
	{
		var _THIS = this;
		var _TR = $("<tr/>");

		$("#detailList").find("thead tr th").each(function()
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
				_TR.append('<td class="td-manage"><input name="detailList.materialId" type="hidden" readonly="readonly" value="' + _THIS.id + '"/><i class="delete fa fa-trash-o"></i></td>');
				break;
			case 'code':
				_TR.append('<td><input name="detailList.code" readonly="readonly" class="tab_input" type="text" value="' + value + '"/><input name="detailList.materialClassId" type="text" value="' + _THIS.materialClassId + '"/></td>');
				break;
			case 'materialName':
				_TR.append('<td><input name="detailList.materialName" readonly="readonly" class="tab_input" type="text" value="' + _THIS.name + '"/></td>');
				break;
			case 'specifications':
				_TR.append('<td><input name="detailList.specifications"  class="tab_input bg_color" type="text" />');
				break;
			case 'weight':
				_TR.append('<td><input name="detailList.weight" class="tab_input" readonly="readonly" type="text" value="' + (_THIS.weight || 0) + '" />');
				break;
			case 'purchUnitName':
				_TR.append('<td><input name="detailList.purchUnitName" readonly="readonly" class="tab_input" type="text" value="' + Helper.basic.info('UNIT', _THIS.stockUnitId).name + '"/><input name="detailList.stockUnitId" type="hidden" value="' + _THIS.stockUnitId + '"/><input name="stockUnitAccuracy" type="hidden" value="' + Helper.basic.info('UNIT', _THIS.stockUnitId).accuracy + '"/></td>');
				break;
			case 'qty':
				_TR.append('<td><input name="detailList.qty" class="tab_input constraint_decimal bg_color" type="text" /></td>');
				break;
			case 'valuationUnitName':
				_TR.append('<td><input name="detailList.valuationUnitName" readonly="readonly" class="tab_input" type="text" value="' + Helper.basic.info('UNIT', _THIS.valuationUnitId).name + '"/><input name="detailList.valuationUnitId" type="hidden" value="' + _THIS.valuationUnitId + '"/><input name="valuationUnitAccuracy" type="hidden" value="' + Helper.basic.info('UNIT', _THIS.valuationUnitId).accuracy + '"/></td>');
				break;
			case 'valuationQty':
				_TR.append('<td><input name="detailList.valuationQty" readonly="readonly" class="tab_input" type="text" /></td>');
				break;
			case 'valuationPrice':
				if (_THIS.lastPurchPrice != null)
				{
					_TR.append((hasPermission ? '<td>' : '<td style="display:none;">') + '<input name="detailList.valuationPrice" class="tab_input constraint_decimal bg_color" type="text" value="' + _THIS.lastPurchPrice + '" /></td>');
				} else
				{
					_TR.append((hasPermission ? '<td>' : '<td style="display:none;">') + '<input name="detailList.valuationPrice" class="tab_input constraint_decimal bg_color" type="text" /></td>');
				}
				break;
			case 'money':
				_TR.append((hasPermission ? '<td>' : '<td style="display:none;">') + '<input name="detailList.money" readonly="readonly" class="tab_input" type="text" /></td>');
				break;
			case 'price':
				_TR.append((hasPermission ? '<td>' : '<td style="display:none;">') + '<input name="detailList.price" readonly="readonly" class="tab_input" type="text" value=""/></td>');
				break;
			case 'memo':
				_TR.append('<td><input name="detailList.memo" onmouseover="this.title=this.value" class="tab_input bg_color memo" type="text" /></td></tr>');
				break;
			}
		});
		_TR.appendTo("#detailList");
	});
	setRowIndex();
}

// 重新设置序号
function setRowIndex()
{
	$("table tbody tr").each(function(index)
	{
		$(this).find("td").first().html(++index);
	});
}
function form_submit()
{
	var data = $("#form_stockMaterial").formToJson();
	// 当没有选中员工时保存时会报错
	if (data['returnEmployeeId'] < 0)
	{
		delete data['returnEmployeeId'];
	}
	if (data['receiveEmployeeId'] < 0)
	{
		delete data['receiveEmployeeId'];
	}
	Helper.request({
		url : Helper.basePath + "/stockmaterial/return/update",
		data : data,
		success : function(data)
		{
			if (data.success)
			{
				Helper.message.suc('已保存!');
				location.href = Helper.basePath + '/stockmaterial/return/view/' + data.obj.id;
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

function getCallInfo_produce(row)
{
	$("#workBillNo").val(row.billNo);
	$("#workId").val(row.id);
	$("#workBillType").val(row.billType);
	$("input[name='detailList.workBillNo']").val(row.billNo);
}
$(function()
{
	$(".tree").on("click", "a[name='materialClassId']", function()
	{
		$(".tree a[name='materialClassId']").css("color", "");
		$(this).css("color", "red");
		var id = $(this).attr("_id");
		$("#materialClassId").val(id);
		$("#bootTable").bootstrapTable("refreshOptions", {
			pageNumber : 1
		});
	});

	// 查询
	$("#btn_search").click(function()
	{
		$("#bootTable").bootstrapTable("refreshOptions", {
			pageNumber : 1
		});
	});

	// 选择确认
	$("#btn_ok").click(function()
	{
		if ($("#multiple").val().toBoolean())
		{
			parent.getCallInfo_material_stock($("#bootTable").bootstrapTable('getAllSelections'));
		}
		Helper.popup.close();
	});

	$("#btn_cancel").click(function()
	{
		Helper.popup.close();
	});

	// 订单详情table
	$("#bootTable").bootstrapTable({
		url : Helper.basePath + "/stock/material/quick_ajaxList",
		method : "post",
		contentType : 'application/json', // 设置请求头信息
		dataType : "json",
		pagination : true, // 是否显示分页（*）
		sidePagination : 'server',// 设置为服务器端分页
		pageList : [ 10, 20, 50 ],
		queryParamsType : "",
		pageSize : 10,
		pageNumber : 1,
		queryParams : queryParams,// 参数
		responseHandler : responseHandler,

		// showColumns : true, //是否显示所有的列
		// minimumCountColumns : 2, //最少允许的列数
		striped : true, // 是否显示行间隔色
		cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
		sortable : false, // 是否启用排序
		clickToSelect : true, // 是否启用点击选中行
		height : 360, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
		cardView : false, // 是否显示详细视图
		detailView : false, // 是否显示父子表
		checkbox : $("#multiple").val().toBoolean(),// 开启多选
		uniqueId : "id",// 定义列表唯一键
		// resizable : true, //是否启用列拖动
		columns : [ {
			field : 'state',
			checkbox : true,
			visible : $("#multiple").val().toBoolean(),
			width : 60
		}, {
			field : 'warehouseId',
			title : '仓库',
			width : 80,
			formatter : function(value, row, index)
			{
				if (Helper.isNotEmpty(value))
				{
					return Helper.basic.info('WAREHOUSE', value).name;
				} else
				{
					return "";
				}
			}
		}, {
			field : 'materialClassId',
			title : '分类',
			width : 80,
			formatter : function(value, row, index)
			{
				if (Helper.isNotEmpty(value))
				{
					return Helper.basic.info('MATERIALCLASS', value).name;
				} else
				{
					return "";
				}
			}
		}, {
			field : 'code',
			title : '材料编号',
			width : 100,
			formatter : function(value, row, index)
			{
				return row.material.code;
			}
		}, {
			field : 'materialName',
			title : '材料名称',
			width : 160,
			formatter : function(value, row, index)
			{
				return row.material.name;
			}
		}, {
			field : 'specifications',
			title : '材料规格',
			width : 120
		}, {
			field : 'weight',
			title : '克重',
			width : 80

		}, {
			field : 'stockUnitId',
			title : '单位',
			width : 60,
			formatter : function(value, row, index)
			{
				if (row.material.stockUnitId)
				{
					return Helper.basic.info('UNIT', row.material.stockUnitId).name;
				}
			}
		}, {
			field : 'valuationUnitId',
			title : '计价单位',
			width : 60,
			formatter : function(value, row, index)
			{
				if (row.material.valuationUnitId)
				{
					return Helper.basic.info('UNIT', row.material.valuationUnitId).name;
				}
			}
		}, {
			field : 'qty',
			title : '库存数量',
			width : 80

		} ],
		onDblClickRow : function(row)
		{
			// 双击选中事件
			selectRow(row);
		}

	});

});

function selectRow(row)
{
	if ($("#multiple").val().toBoolean())
	{// 多选,则双击事件无效
		parent.getCallInfo_material_stock([ row ]);
	} else
	{// 单选，双击事件立即返回,处理返回index事件
		parent.getCallInfo_material_stock(row);
		Helper.popup.close();
	}

}
// 请求参数
function queryParams(params)
{
	params['materialClassId'] = $("#materialClassId").val();
	params['materialName'] = $("#materialName").val();
	params['warehouseId'] = $("#warehouseId").val();
	params['isEmptyWare'] = $("#isEmptyWare").val();
	params['specifications'] = $("#specifications").val();
	return params;
}
// ajax结果
function responseHandler(res)
{
	return {
		rows : res.result,
		total : res.count
	};
}
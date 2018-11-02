$(function()
{
	$("#btn_transmit").click(function()
	{
		var rows = getSelectedRows();
		if (Helper.isNotEmpty(rows))
		{
			var _paramStr = "";
			$(rows).each(function(index, item)
			{
				// console.log(index,item)
				if (index == 0)
				{
					_paramStr = "ids=" + item.id;
				} else
				{
					_paramStr = _paramStr + "&ids=" + item.id;
				}
			});
			var url = Helper.basePath + '/purch/order/warnToPurch?' + _paramStr;
			var title = "采购订单";
			admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
		} else
		{
			Helper.message.warn("请选择工单");
		}
	});

	// 订单详情table
	$("#bootTable").bootstrapTable({
		url : Helper.basePath + "/stock/material/stock_warn_list",
		method : "post",
		contentType : 'application/json', // 设置请求头信息
		dataType : "json",
		pagination : true, // 是否显示分页（*）
		sidePagination : 'server',// 设置为服务器端分页
		pageList : Helper.bootPageList,
		queryParamsType : "",
		pageSize : 10,
		pageNumber : 1,
		queryParams : queryParams,// 参数
		responseHandler : responseHandler,

		// resizable : true, //是否启用列拖动
		showColumns : true, // 是否显示所有的列
		minimumCountColumns : 2, // 最少允许的列数
		striped : true, // 是否显示行间隔色
		cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
		sortable : false, // 是否启用排序
		clickToSelect : true, // 是否启用点击选中行
		height : 430, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
		cardView : false, // 是否显示详细视图
		detailView : false, // 是否显示父子表

		showExport : true,// 是否显示导出按钮
		// exportDataType: 'all',
		exportTypes : [ 'csv', 'txt', 'excel', 'doc' ],

		cookie : true,// 是否启用COOKIE
		cookiesEnabled : [ 'bs.table.columns' ],// 默认会记住很多属性，这里控制只记住列选择属性
		cookieIdTable : "print_stock_material_warn",// 必须制定唯一的表格cookieID

		uniqueId : "id",// 定义列表唯一键
		columns : [ {
			field : 'state',
			checkbox : true,
			visible : true,
			width : 60
		}, {
			field : 'warehouseId',
			title : '仓库',
			width : 80,
			formatter : function(value, row, index)
			{
				return Helper.basic.info('WAREHOUSE', value).name;
			}
		}, {
			field : 'materialClassId',
			title : '材料分类',
			width : 100,
			formatter : function(value, row, index)
			{
				return Helper.basic.info('MATERIALCLASS', value).name;
			}
		}, {
			field : 'code',
			title : '材料编号',
			width : 80,
			formatter : function(value, row, index)
			{
				return row.material.code;
			}
		}, {
			field : 'materialName',
			title : '材料名称',
			width : 120,
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
			width : 80,
			formatter : function(value, row, index)
			{
				return row.material.weight;
			}
		}, {
			field : 'stockUnitId',
			title : '单位',
			width : 80,
			formatter : function(value, row, index)
			{

				return Helper.basic.info('UNIT', row.material.stockUnitId).name;

			}
		}, {
			field : 'qty',
			title : '库存数量',
			width : 60
		}, {
			field : 'valuationUnitId',
			title : '计价单位',
			width : 80,
			formatter : function(value, row, index)
			{

				return Helper.basic.info('UNIT', row.material.valuationUnitId).name;

			}
		}, {
			field : 'valuationQty',
			title : '计价数量',
			width : 80
		}, {
			field : 'minStockNum',
			title : '最低库存数',
			width : 80,
			formatter : function(value, row, index)
			{
				return row.material.minStockNum;
			}

		} ]
	});
	/* 表格工具栏 */
	$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
	if (!Helper.basic.hasPermission('stock:material_stockwarn:export'))
	{
		$(".export.btn-group").remove();
	}
	$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
});
function queryParams(params)
{
	return params;
}
$(function()
{
	/* 判断当前用户是否有权限查看金额 */
	var hasPermission = Helper.basic.hasPermission('stock:material:money');
	$("#btn_search").click(function()
	{
		$("#bootTable").bootstrapTable("refreshOptions", {
			pageNumber : 1
		});
	});
	$("#btn_more").click(function()
	{
		$("#more_div").toggle();
	});
	$("#material_quick_select").click(function()
	{
		Helper.popup.show('选择材料', Helper.basePath + '/quick/material_select?multiple=false', '900', '490');
	});

	// 订单详情table
	$("#bootTable").bootstrapTable({
		url : Helper.basePath + "/stock/material/ajaxList",
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
		cookieIdTable : "print_stock_material_list",// 必须制定唯一的表格cookieID

		uniqueId : "id",// 定义列表唯一键
		columns : [ {
			field : 'index',
			title : '序号',
			width : 40,
			formatter : function(value, row, index)
			{
				return index + 1;
			}
		}, {
			field : 'warehouseName',
			title : '仓库',
			width : 80
		}, {
			field : 'materialType',
			title : '材料类别',
			width : 80,
			formatter : function(value, row, index)
			{
				return row.material.materialTypeText;
			}
		}, {
			field : 'materialClassName',
			title : '材料分类',
			width : 80
		}, {
			field : 'code',
			title : '材料编号',
			width : 120,
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
			width : 100
		}, {
			field : 'weight',
			title : '克重',
			width : 80
		}, {
			field : 'unitName',
			title : '单位',
			width : 60,
			formatter : function(value, row, index)
			{
				return row.material.stockUnitName;
			}
		}, {
			field : 'qty',
			title : '库存数量',
			width : 80
		}, {
			field : 'valuationUnitName',
			title : '计价单位',
			width : 60,
			formatter : function(value, row, index)
			{
				return row.material.valuationUnitName;
			}
		}, {
			field : 'valuationQty',
			title : '计价数量',
			width : 80
		}, {
			field : 'valuationPrice',
			title : '计价单价',
			visible : hasPermission,
			width : 80,
			formatter : function(value, row, index)
			{
				return value || 0;
			}
		}, {
			field : 'price',
			title : '成本单价',
			visible : hasPermission,
			width : 80,
			formatter : function(value, row, index)
			{
				return value || 0;
			}
		},

		{
			field : 'money',
			title : '成本金额',
			visible : hasPermission,
			width : 80
		} ],
		onLoadSuccess : function()
		{
			if ($(".glyphicon-th").next().html() == '')
			{
				$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
				// 控制筛选菜单金额选择
				if (!hasPermission)
				{
					$("div[title='列'] ul[role=menu]").find("input[value=12]").parent().parent().remove();
					$("div[title='列'] ul[role=menu]").find("input[value=13]").parent().parent().remove();
					$("div[title='列'] ul[role=menu]").find("input[value=14]").parent().parent().remove();
				}

				if (!Helper.basic.hasPermission('stock:material_adjust:export'))
				{
					$(".export.btn-group").remove();
				}
				$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
			}

			$("#bootTable tbody").find("tr:last").find("td:first").text("合计");
			var _tds = $("#bootTable tbody").find("tr:last").find("td");
			for (var index = 0; index < _tds.length; index++)
			{
				if (_tds.eq(index).text() == "-")
				{
					_tds.eq(index).text('');
				}
			}
		},
		onColumnSwitch : function(field, checked)
		{
			$("#bootTable tbody").find("tr:last").find("td:first").text("合计");
			var _tds = $("#bootTable tbody").find("tr:last").find("td");
			for (var index = 0; index < _tds.length; index++)
			{
				if (_tds.eq(index).text() == "-")
				{
					_tds.eq(index).text('');
				}
			}

			// 在使用筛选增加或减少列时重置表格列拖动效果
			bootstrapTable_ColDrag($("#bootTable"));
		},
		onLoadError : function()
		{
			// alert("数据加载异常");
		}

	});
})
// 获取返回信息
function getCallInfo_material(obj)
{
	$("#materialName").val(obj.name);
}

// 请求参数
function queryParams(params)
{
	params['materialName'] = $("#materialName").val();
	params['warehouseId'] = $("#warehouseId").val();
	params['materialType'] = $("#materialType").val() == -1 ? null : $("#materialType").val();
	params['materialClassId'] = $("#materialClassId").val();
	params['isEmptyWare'] = $("#isEmptyWare").is(':checked') == true ? "YES" : null;

	params['code'] = $("#code").val();
	params['specifications'] = $("#specifications").val();
	params['weight'] = $("#weight").val();
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
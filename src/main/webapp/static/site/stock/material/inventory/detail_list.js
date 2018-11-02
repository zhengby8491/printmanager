$(function()
{
	/* 判断当前用户是否有权限查看金额 */
	var hasPermission = Helper.basic.hasPermission('stock:materialInventory_detail:money');
	/* 选择材料 */
	$("#material_quick_select").click(function()
	{
		Helper.popup.show('选择材料', Helper.basePath + '/quick/material_select?multiple=false', '900', '490');
	});

	/* 搜索 */
	$("#btn_search").click(function()
	{
		$("#bootTable").bootstrapTable("refresh");
		// $("#bootTable").bootstrapTable("refreshOptions", {
		// pageNumber : 1
		// });
	});
	$("input[type='radio']").change(function()
	{
		$("#btn_search").click();
	})
	// 订单详情table
	$("#bootTable").bootstrapTable({
		url : Helper.basePath + "/stockmaterial/inventory/ajaxDetailList",
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
		cookieIdTable : "print_stockmaterial_inventory_detail",// 必须制定唯一的表格cookieID

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
			field : 'isCheck',
			title : '单据状态',
			width : 70,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return;
				}
				if (row.master.isCheck == 'YES')
				{
					return '已审核';
				} else
				{
					return '未审核';
				}
			},
			cellStyle : function(value, row, index, field)
			{
				if (value == '未审核')
				{
					return {
						css : {
							"color" : "#f00"
						}
					};
				} else
				{
					return {
						css : {
							"color" : "#080"
						}
					};
				}
			}
		}, {
			field : 'inventoryTypeText',
			title : '盘点类型',
			width : 80
		}, {
			field : 'warehouseName',
			title : '仓库',
			width : 80,
			formatter : function(value, row, index)
			{
				return row.master.warehouseName;
			}
		}, {
			field : 'inventoryTime',
			title : '盘点日期',
			width : 80,
			formatter : function(value, row, index)
			{
				if (row.master.inventoryTime)
				{
					return new Date(row.master.inventoryTime).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'billNo',
			title : '盘点单号',
			width : 120,
			formatter : function(value, row, index)
			{
				return idTransToUrl(row.master.id, row.master.billNo);
			}
		}, {
			field : 'code',
			title : '材料编号',
			width : 120
		}, {
			field : 'materialName',
			title : '材料名称',
			width : 120
		}, {
			field : 'specifications',
			title : '材料规格',
			width : 100
		}, {
			field : 'stockUnitName',
			title : '单位',
			width : 60
		}, {
			field : 'qty',
			title : '盘点数量',
			width : 80
		}, {
			field : 'profitAndLossQty',
			title : '盈亏数量',
			width : 80
		}, {
			field : 'price',
			title : '成本单价',
			visible : hasPermission,
			width : 80
		}, {
			field : 'profitAndLossMoney',
			title : '盈亏金额',
			visible : hasPermission,
			width : 100
		}, {
			field : 'memo',
			title : '备注',
			'class' : 'memoView',
			width : 100
		} ],
		onLoadSuccess : function()
		{
			// alert("数据加载完成");
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
		onDblClickRow : function(row)
		{
			if (row.id == null)
			{
				return;
			}
			var url = Helper.basePath + '/stockmaterial/inventory/view/' + row.master.id;
			var title = "材料库存盘点";
			admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
		},
		onClickRow : function(row, $element)
		{
			$element.addClass("tr_active").siblings().removeClass("tr_active");
		}
	});
	/* 表格工具栏 */
	$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
	// 控制筛选菜单金额选择,删除已隐藏字段的选项
	if (!hasPermission)
	{
		$("div[title='列'] ul[role=menu]").find("input[value=12]").parent().parent().remove();
		$("div[title='列'] ul[role=menu]").find("input[value=13]").parent().parent().remove();
	}
	;
	if (!Helper.basic.hasPermission('stock:material_inventory:export'))
	{
		$(".export.btn-group").remove();
	}
	$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
});
// 获取返回信息
function getCallInfo_material(obj)
{
	$("#materialName").val(obj.name);
}
// 请求参数
function queryParams(params)
{
	params['dateMin'] = $("#dateMin").val();
	params['dateMax'] = $("#dateMax").val();
	params['billNo'] = $("#billNo").val();
	params['materialName'] = $("#materialName").val();
	params['warehouseId'] = $("#warehouseId").val();
	params['auditFlag'] = $("input[name='auditFlag']:checked").val() == "-1" ? null : $("input[name='auditFlag']:checked").val();
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
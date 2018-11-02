$(function()
{
	/* 判断当前用户是否有权限查看金额 */
	var hasPermission = Helper.basic.hasPermission('stock:material_logsum:money');
	/* 选择材料 */
	$("#material_quick_select").click(function()
	{
		Helper.popup.show('选择材料', Helper.basePath + '/quick/material_select?multiple=false', '900', '500');
	});

	/* 搜索 */
	$("#btn_search").click(function()
	{
		$("#bootTable").bootstrapTable("refreshOptions", {
			pageNumber : 1
		});
	});
	// 订单详情table
	$("#bootTable").bootstrapTable({
		url : Helper.basePath + "/stock/material/ajaxlogSumList",
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
		cookieIdTable : "print_stock_material_logSumList",// 必须制定唯一的表格cookieID

		uniqueId : "id",// 定义列表唯一键
		columns : [ {
			field : 'warehouseName',
			title : '仓库',
			width : 80
		}, {
			field : 'materialClassName',
			title : '材料分类',
			width : 80
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
			field : 'weight',
			title : '克重',
			width : 80
		}, {
			field : 'unitName',
			title : '单位',
			width : 60
		}, {
			field : 'inQty',
			title : '入库数量',
			width : 80
		}, {
			field : 'inMoney',
			title : '入库金额',
			visible : hasPermission,
			width : 80
		}, {
			field : 'outQty',
			title : '出库数量',
			width : 80
		}, {
			field : 'outMoney',
			title : '出库金额',
			visible : hasPermission,
			width : 80
		} ],
		onLoadSuccess : function()
		{
			// alert("数据加载完成");
			if ($(".glyphicon-th").next().html() == '')
			{
				$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
				// 控制筛选菜单金额选择,删除已隐藏字段的选项
				if (!hasPermission)
				{
					$("div[title='列'] ul[role=menu]").find("input[value=8]").parent().parent().remove();
					$("div[title='列'] ul[role=menu]").find("input[value=10]").parent().parent().remove();
				}
				;
				if (!Helper.basic.hasPermission('stock:material_logsum:export'))
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
		},
		/*
		 * onDblClickRow : function(row) { var url = Helper.basePath +
		 * '/stockmaterial/transfer/view/'+row.master.id; var title = "材料库存调拨";
		 * admin_tab($("<a _href='"+url+"' data-title='"+title+"' />")); },
		 */
		onClickRow : function(row, $element)
		{
			$element.addClass("tr_active").siblings().removeClass("tr_active");
		}

	});
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
	params['materialName'] = $("#materialName").val();
	params['warehouseId'] = $("#warehouseId").val();
	params['materialClassId'] = $("#materialClassId").val();
	params['specifications'] = $("#specifications").val();
	// console.log(params)
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
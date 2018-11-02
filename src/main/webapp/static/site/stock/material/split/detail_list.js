$(function()
{
	/* 选择材料 */
	$("#material_quick_select").click(function()
	{
		Helper.popup.show('选择材料', Helper.basePath + '/quick/material_select?multiple=false', '900', '490');
	});

	/* 搜索 */
	$("#btn_search").click(function()
	{
		$("#bootTable").bootstrapTable("refreshOptions", {
			pageNumber : 1
		});
	});
	$("input[type='radio']").change(function()
	{
		$("#btn_search").click();
	})
	// 订单详情table
	$("#bootTable").bootstrapTable({
		url : Helper.basePath + "/stockmaterial/split/ajaxDetailList",
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
		height : 400, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
		cardView : false, // 是否显示详细视图
		detailView : false, // 是否显示父子表

		showExport : true,// 是否显示导出按钮
		// exportDataType: 'all',
		exportTypes : [ 'csv', 'txt', 'excel', 'doc' ],

		cookie : true,// 是否启用COOKIE
		cookiesEnabled : [ 'bs.table.columns' ],// 默认会记住很多属性，这里控制只记住列选择属性
		cookieIdTable : "print_stockmaterial_split_detail",// 必须制定唯一的表格cookieID

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
			field : 'splitTime',
			title : '分切日期',
			width : 80,
			formatter : function(value, row, index)
			{
				if (row.master.splitTime)
				{
					return new Date(row.master.splitTime).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'billNo',
			title : '分切单号',
			width : 120,
			formatter : function(value, row, index)
			{
				return idTransToUrl(row.master.id, row.master.billNo);
			}
		}, {
			field : 'mSplitTypeText',
			title : '分切类型',
			width : 80,
			formatter : function(value, row, index)
			{
				return row.master.splitTypeText;
			}
		}, {
			field : 'mWarehouseName',
			title : '领料仓',
			width : 70,
			formatter : function(value, row, index)
			{
				return row.master.warehouseName;
			}
		}, {
			field : 'mMaterialName',
			title : '材料名称',
			width : 120,
			formatter : function(value, row, index)
			{
				return row.master.materialName;
			}
		}, {
			field : 'mSpecifications',
			title : '材料规格',
			width : 100,
			formatter : function(value, row, index)
			{
				return row.master.specifications;
			}
		}, {
			field : 'mWeight',
			title : '克重',
			width : 100,
			formatter : function(value, row, index)
			{
				if (row.master.material)
				{
					return row.master.material.weight;
				}

			}
		}, {
			field : 'mStockUnitName',
			title : '单位',
			width : 60,
			formatter : function(value, row, index)
			{
				if (row.master.stockUnitId)
				{
					return Helper.basic.info('UNIT', row.master.stockUnitId).name;
				}

			}
		}, {
			field : 'mQty',
			title : '数量',
			width : 80,
			formatter : function(value, row, index)
			{
				return row.master.qty;
			}
		}, {
			field : 'mValuationUnitName',
			title : '计价单位',
			width : 80,
			formatter : function(value, row, index)
			{
				if (row.master.valuationUnitId)
				{
					return Helper.basic.info('UNIT', row.master.valuationUnitId).name;
				}
			}
		}, {
			field : 'mValuationQty',
			title : '计价数量',
			width : 80,
			formatter : function(value, row, index)
			{
				return row.master.valuationQty;
			}
		}, {
			field : 'warehouseName',
			title : '入库仓',
			width : 70
		}, {
			field : 'specifications',
			title : '分切规格',
			width : 100
		}, {
			field : 'qty',
			title : '分切数量',
			width : 100
		}, {
			field : 'valuationQty',
			title : '计价数量',
			width : 100
		}, {
			field : 'memo',
			title : '备注',
			'class' : 'memoView',
			width : 200
		} ],
		onLoadSuccess : function()
		{
			// alert("数据加载完成");
			if ($(".glyphicon-th").next().html() == '')
			{
				$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
				if (!Helper.basic.hasPermission('stock:material_split:export'))
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
		onDblClickRow : function(row)
		{
			if (row.id == null)
			{
				return;
			}
			var url = Helper.basePath + '/stockmaterial/split/view/' + row.master.id;
			var title = "材料分切";
			admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
		},
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
	params['billNo'] = $("#billNo").val().trim();
	params['materialName'] = $("#materialName").val().trim();
	params['specifications'] = $("#specifications").val().trim();
	params['outWarehouseId'] = $("#outWarehouseId").val();
	params['inWarehouseId'] = $("#inWarehouseId").val();
	params['splitType'] = $("#splitType").val() == "-1" ? null : $("#splitType").val();
	params['auditFlag'] = $("input[name='auditFlag']:checked").val() == "-1" ? null : $("input[name='auditFlag']:checked").val();
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
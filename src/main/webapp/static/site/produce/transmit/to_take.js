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
				if (item.id)
				{
					if (index == 0)
					{
						_paramStr = "ids=" + item.id;
					} else
					{
						_paramStr = _paramStr + "&ids=" + item.id;
					}
				}
			});
			var url = Helper.basePath + '/stockmaterial/take/toTake?' + _paramStr;
			var title = "生产领料";
			admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
		} else
		{
			Helper.message.warn("请选择工单");
		}
	});
	// 强制完工
	$("#btn_complete").on("click", function()
	{
		var rows = getSelectedRows();
		var ids = $(rows).map(function()
		{
			return this.id;
		}).get();
		if (Helper.isNotEmpty(rows))
		{
			Helper.post(Helper.basePath + '/stockmaterial/take/forceCompleteYes', {
				"ids[]" : ids
			}, function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
				} else
				{
					Helper.message.warn(data.message);
				}
				$("#bootTable").bootstrapTable("refreshOptions", {
					pageNumber : 1
				});
			});
		} else
		{
			Helper.message.warn("请选择工单");
		}
	});
	// 取消强制完工
	$("#btn_complete_cancel").on("click", function()
	{
		var rows = getSelectedRows();
		var ids = $(rows).map(function()
		{
			return this.id;
		}).get();
		if (Helper.isNotEmpty(rows))
		{
			Helper.post(Helper.basePath + '/stockmaterial/take/forceCompleteNo', {
				"ids[]" : ids
			}, function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
				} else
				{
					Helper.message.warn(data.message);
				}
				$("#bootTable").bootstrapTable("refreshOptions", {
					pageNumber : 1
				});
			});
		} else
		{
			Helper.message.warn("请选择工单");
		}
	});

	// 订单详情table
	$("#bootTable").bootstrapTable({
		url : Helper.basePath + "/produce/transmit/to_take_list",
		method : "post",
		contentType : 'application/json', // 设置请求头信息
		dataType : "json",
		pagination : false, // 是否显示分页（*）
		sidePagination : 'server',// 设置为服务器端分页
		pageList : Helper.bootPageList,
		queryParamsType : "",
		pageSize : 20,
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
		cookieIdTable : "print_workmaterial_take",// 必须制定唯一的表格cookieID

		uniqueId : "id",// 定义列表唯一键
		columns : [ {
			field : 'index',
			title : '序号',
			width : 40,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return;
				}
				return index + 1;
			}
		}, {
			field : 'state',
			checkbox : true,
			visible : true,
			width : 60
		}, {
			field : 'type',
			title : '工单类型',
			width : 80,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return;
				}
				return row.work.billTypeText;
			}
		}, {
			field : 'work_billNo',
			title : '生产单号',
			width : 100,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return;
				}
				return billNoTransToUrl(row.work.billNo);
			}
		}, {
			field : 'createTime',
			title : '制单日期',
			width : 140,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return;
				}
				return new Date(row.work.createTime).format("yyyy-MM-dd");
			}
		}, {
			field : 'productNames',
			title : '成品名称',
			width : 120
		}, {
			field : 'materialName',
			title : '材料名称',
			width : 120
		}, {
			field : 'style',
			title : '材料规格',
			width : 120
		}, {
			field : 'weight',
			title : '克重',
			width : 80
		}, {
			field : 'stockUnitName',
			title : '单位',
			width : 80
		}, {
			field : 'qty',
			title : '材料用量',
			width : 60
		}, {
			field : 'takeQty',
			title : '已领料数量',
			width : 80
		}, {
			field : 'notTakeQty',
			title : '未领料数量',
			width : 80,
			formatter : function(value, row, index)
			{
				return Number(row.qty).subtr(Number(row.takeQty));
			}
		}, {
			field : 'memo',
			title : '备注',
			'class' : 'memoView',
			width : 200
		} ],
		onLoadSuccess : function(data)
		{
			$("#bootTable tbody").find("tr:last").find("td:first").text("合计");
			$("#bootTable tbody").find("tr:last").find("td:first").next().children("input[type='checkbox']").remove();
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
			// 在使用筛选增加或减少列时重置表格列拖动效果
			bootstrapTable_ColDrag($("#bootTable"));
		}
	});
	$("#bootTable").on('load-success.bs.table column-switch.bs.table page-change.bs.table search.bs.table', function()
	{
		/* 表格工具栏 */
		if ($(".glyphicon-th").next().html() == '')
		{
			$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
			if (!Helper.basic.hasPermission('transmit:to_take:export'))
			{
				$(".export.btn-group").remove();
			}
			$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
		}
	});
});

function queryParams(params)
{
	params['dateMin'] = $("#dateMin").val().trim();
	params['dateMax'] = $("#dateMax").val().trim();
	params['billNo'] = $("#billNo").val().trim();
	params['materialName'] = $("#materialName").val().trim();
	params['completeFlag'] = $("input[name='completeFlag']:checked").val().trim();

	params['productStyle'] = $("#style").val().trim();

	return params;
}
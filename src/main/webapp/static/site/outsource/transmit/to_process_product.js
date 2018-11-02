$(function()
{
	/* 生成外发加工单 */
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
			var url = Helper.basePath + '/outsource/process/create?type=PRODUCT&' + _paramStr;
			var title = "发外加工单";
			admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
		} else
		{
			Helper.message.warn("请选择加工单");
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
			Helper.post(Helper.basePath + '/outsource/transmit/product_complete', {
				"tableType" : "DETAIL",
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
			Helper.message.warn("请选择加工单");
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
			Helper.post(Helper.basePath + '/outsource/transmit/product_complete_cancel', {
				"tableType" : "DETAIL",
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
			Helper.message.warn("请选择加工单");
		}
	});

	// 订单详情table
	$("#bootTable").bootstrapTable({
		url : Helper.basePath + "/outsource/transmit/to_process_product_list",
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
		cookieIdTable : "print_outsource_transmit_process_product",// 必须制定唯一的表格cookieID

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
			field : 'work_createTime',
			title : '制单日期',
			width : 80,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return;
				}
				if (Helper.isNotEmpty(row.master.createTime))
				{
					return new Date(row.master.createTime).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'work_billType',
			title : '工单类型',
			width : 80,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return;
				}
				return row.master.billTypeText;
			}
		}, {
			field : 'work_billNo',
			title : '生产单号',
			width : 140,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return;
				}
				return billNoTransToUrl(row.master.billNo);
			}
		}, {
			field : 'productName',
			title : '成品名称',
			width : 80
		}, {
			field : 'style',
			title : '加工规格',
			width : 40
		}, {
			field : 'produceQty',
			title : '应外发数量',
			width : 60
		}, {
			field : 'outOfQty',
			title : '已外发数量',
			width : 60
		}, {
			field : 'noOutQty',
			title : '未外发数量',
			width : 60,
			formatter : function(value, row, index)
			{
				return Number(row.produceQty).subtr(Number(row.outOfQty));
			}
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
			if (!Helper.basic.hasPermission('transmit:to_process_product:export'))
			{
				$(".export.btn-group").remove();
			}
			$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
		}
	});
});

function queryParams(params)
{
	params['workBillNo'] = $("#workBillNo").val().trim();
	params['productName'] = $("#productName").val().trim();
	params['completeFlag'] = $("input[name='completeFlag']:checked").val().trim();

	params['dateMin'] = $("#dateMin").val();
	params['dateMax'] = $("#dateMax").val();
	return params;
}
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
			var url = Helper.basePath + '/outsource/process/create?type=PROCESS&' + _paramStr;
			var title = "发外加工单";
			admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
		} else
		{
			Helper.message.warn("请选择加工单");
		}
	});
	// 取消发外
	/*
	 * $("#btn_cancel_out").on("click", function() { var rows = getSelectedRows();
	 * var ids=$(rows).map(function(){return this.id;}).get();
	 * if(Helper.isNotEmpty(rows)) { Helper.post(Helper.basePath +
	 * '/outsource/transmit/cancelProcedureOut', { "ids[]": ids }, function(data) {
	 * if(data.success) { Helper.message.suc(data.message); }else {
	 * Helper.message.warn(data.message); }
	 * $("#bootTable").bootstrapTable("refreshOptions",{pageNumber:1}); }); }else {
	 * Helper.message.warn("请选择工序"); } });
	 */

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
			Helper.post(Helper.basePath + '/outsource/transmit/procedure_complete', {
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
			Helper.post(Helper.basePath + '/outsource/transmit/procedure_complete_cancel', {
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
		url : Helper.basePath + "/outsource/transmit/to_process_procedure_list",
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
		cookieIdTable : "print_outsource_transmit_process_procedure",// 必须制定唯一的表格cookieID

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
			width : 30
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
				if (Helper.isNotEmpty(row.work.createTime))
				{
					return new Date(row.work.createTime).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'billTypeText',
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
			field : 'workBillNo',
			title : '生产单号',
			width : 140,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return;
				}
				return billNoTransToUrl(value);
			}
		}, {
			field : 'productNames',
			title : '成品名称',
			width : 140
		}, {
			field : 'workPart_partName',
			title : '部件名称',
			width : 80,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return;
				}
				if (row.workProcedureType == "PART")
				{
					return row.workPart.partName;
				}
			}
		}, {
			field : 'procedureName',
			title : '工序名称',
			width : 80
		}, {
			field : 'style',
			title : '加工规格',
			width : 80,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return;
				}
				if (row.workProcedureType == "PART")
				{
					return row.workPart.style;
				} else
				{
					return row.workPack.style;
				}

			}
		}, {
			field : 'produceNum',
			title : '生产数量',
			width : 80,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return row.sumQty;
				}
				if (row.workProcedureType == "PART")
				{
					return row.workPart.qty;
				} else
				{
					return row.inputQty;
				}

			}
		}, {
			field : 'inputQty',
			title : '应外发数量',
			width : 80
		}, {
			field : 'outOfQty',
			title : '已外发数量',
			width : 80
		}, {
			field : 'noOutQty',
			title : '未外发数量',
			width : 80,
			formatter : function(value, row, index)
			{
				return Number(row.inputQty).subtr(Number(row.outOfQty));
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
			if (!Helper.basic.hasPermission('transmit:to_process_procedure:export'))
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
	params['procedureName'] = $("#procedureName").val().trim();
	params['completeFlag'] = $("input[name='completeFlag']:checked").val().trim();

	params['dateMin'] = $("#dateMin").val();
	params['dateMax'] = $("#dateMax").val();
	return params;
}
$(function()
{
	/* 更多 */
	$("#btn_more").click(function()
	{
		$("#more_div2").toggle();
	});
	/* 转对账 */
	$("#btn_transmit").click(function()
	{
		var rows = getSelectedRows();
		if (Helper.isNotEmpty(rows))
		{
			var supplierId = "";
			var supplierArr = new Array();
			$(rows).each(function()
			{
				if (this.master)
				{
					supplierId = this.master.supplierId;
					if (!supplierArr.contains(supplierId))
					{
						supplierArr.push(supplierId);
					}
				}
			});
			if (supplierArr.length == 1)
			{// 判断是否统一供应商
				var _paramStr = "";
				$(rows).each(function(index, item)
				{
					if (item.id)
					{
						if (index == 0)
						{
							if (item.master.billType == 'OUTSOURCE_OA')
							{
								_paramStr = "arriveIds=" + item.id;
							} else if (item.master.billType == 'OUTSOURCE_OR')
							{
								_paramStr = "returnIds=" + item.id;
							}
						} else
						{
							if (item.master.billType == 'OUTSOURCE_OA')
							{
								_paramStr = _paramStr + "&arriveIds=" + item.id;
							} else if (item.master.billType == 'OUTSOURCE_OR')
							{
								_paramStr = _paramStr + "&returnIds=" + item.id;
							}
						}
					}
				});
				var url = Helper.basePath + '/outsource/reconcil/create?supplierId=' + supplierId + '&' + _paramStr;
				var title = "发外对账单";
				admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));

			} else
			{
				Helper.message.warn("请选择同一供应商");
			}
		} else
		{
			Helper.message.warn("至少选择1项");
		}
	});
	// 强制完工
	$("#btn_complete").on("click", function()
	{
		var rows = getSelectedRows();
		var arriveIds = $(rows).map(function()
		{
			if (this.master && this.master.billType == 'OUTSOURCE_OA')
				return this.id;
			else
				return null;
		}).get();
		var returnIds = $(rows).map(function()
		{
			if (this.master && this.master.billType == 'OUTSOURCE_OR')
				return this.id;
			else
				return null;
		}).get();
		if (Helper.isNotEmpty(rows))
		{
			Helper.post(Helper.basePath + '/outsource/reconcil/complete', {
				"tableType" : "DETAIL",
				"arriveIds" : arriveIds,
				"returnIds" : returnIds
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
			Helper.message.warn("至少选择1项");
		}
	});
	// 取消强制完工
	$("#btn_complete_cancel").on("click", function()
	{
		var rows = getSelectedRows();
		var arriveIds = $(rows).map(function()
		{
			if (this.master && this.master.billType == 'OUTSOURCE_OA')
				return this.id;
			else
				return null;
		}).get();
		var returnIds = $(rows).map(function()
		{
			if (this.master && this.master.billType == 'OUTSOURCE_OR')
				return this.id;
			else
				return null;
		}).get();
		if (Helper.isNotEmpty(rows))
		{
			Helper.post(Helper.basePath + '/outsource/reconcil/complete_cancel', {
				"tableType" : "DETAIL",
				"arriveIds" : arriveIds,
				"returnIds" : returnIds
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
			Helper.message.warn("至少选择1项");
		}
	});

	// 订单详情table
	$("#bootTable").bootstrapTable({
		url : Helper.basePath + "/outsource/transmit/to_reconcil_list",
		method : "post",
		contentType : 'application/json', // 设置请求头信息
		dataType : "json",
		pagination : false, // 是否显示分页（*）
		sidePagination : 'server',// 设置为服务器端分页
		pageList : Helper.bootPageList,
		queryParamsType : "",
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
		cookieIdTable : "print_outsource_transmit_reconcil",// 必须制定唯一的表格cookieID

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
			field : 'master_createTime',
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
			field : 'master_billType',
			title : '单据类型',
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
			field : 'master_billNo',
			title : '源单单号',
			width : 100,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return;
				}
				return idTransToUrl(row.masterId, row.master.billNo);
			}
		}, {
			field : 'workBillNo',
			title : '生产单号',
			width : 140,
			formatter : function(value, row, index)
			{
				return billNoTransToUrl(value);
			}
		}, {
			field : 'produceNum',
			title : '生产数量',
			width : 80
		}, {
			field : 'qty',
			title : '到/退货数量',
			width : 100,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return value;
				}
				if (row.master.billType == "OUTSOURCE_OR" && value != 0)
				{
					return "-" + value;
				} else
				{
					return value;
				}
			}
		}, {
			field : 'productNames',
			title : '成品名称',
			width : 120,
			formatter : function(value, row, index)
			{
				if (row.type == 'PRODUCT')
				{
					return row.productName;
				} else if (row.type == 'PROCESS')
				{
					return row.productNames;
				}
			}
		}, {
			field : 'procedureName',
			title : '工序名称',
			width : 120
		}, {
			field : 'style',
			title : '加工规格',
			width : 80
		}, {
			field : 'outSourceBillNo',
			title : '加工单号',
			width : 140,
			formatter : function(value, row, index)
			{
				return billNoTransToUrl(value);
			}
		}, {
			field : 'master_supplierName',
			title : '供应商名称',
			width : 200,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return;
				}
				return row.master.supplierName;
			}
		}, {
			field : 'partName',
			title : '部件名称',
			width : 80,
			formatter : function(value, row, index)
			{
				if (value == "null")
				{
					return "";
				} else
				{
					return value;
				}
			}
		}, {
			field : 'qty',
			title : '应对账数量',
			width : 80,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return value;
				}
				if (row.master.billType == "OUTSOURCE_OR")
				{
					return "-" + value;
				} else
				{
					return value;
				}
			}
		}, {
			field : 'reconcilQty',
			title : '已对账数量',
			width : 80,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return value;
				}
				if (row.master.billType == "OUTSOURCE_OR" && value != 0)
				{
					return "-" + value;
				} else
				{
					return value;
				}
			}
		}, {
			field : 'noReconcilQty',
			title : '未对账数量',
			width : 80,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return Number(row.qty).subtr(Number(row.reconcilQty));
					;
				}
				if (row.master.billType == "OUTSOURCE_OR")
				{
					return '-' + Number(row.qty).subtr(Number(row.reconcilQty));
				} else
				{
					return Number(row.qty).subtr(Number(row.reconcilQty));
				}
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
			if (!Helper.basic.hasPermission('transmit:to_reconcil:export'))
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
	params['productName'] = $("#productName").val().trim();
	params['procedureName'] = $("#procedureName").val().trim();
	params['supplierName'] = $("#supplierName").val().trim();
	params['completeFlag'] = $("input[name='completeFlag']:checked").val().trim();

	params['sourceBillNo'] = $("#outSourceBillNo").val().trim();
	params['workBillNo'] = $("#workBillNo").val().trim();
	return params;
}
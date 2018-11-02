$(function()
{
	/* 更多 */
	$("#btn_more").click(function()
	{
		$("#more_div").toggle();
	});
	/* 搜索 */
	$("#btn_search").click(function()
	{
		$("#bootTable").bootstrapTable("refreshOptions", {
			pageNumber : 1
		});
	});
	$("#btn_report").click(function()
	{
		var rows = getSelectedRows();
		if (Helper.isNotEmpty(rows))
		{

			var _paramStr = "";
			$(rows).each(function(index, item)
			{
				if (index == 0)
				{
					_paramStr = "ids=" + item.id;
				} else
				{
					_paramStr = _paramStr + "&ids=" + item.id;
				}
			});

			var url = Helper.basePath + '/produce/report/create?' + _paramStr;
			var title = "产量上报";
			admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
		} else
		{
			Helper.message.warn("至少选择1项");
		}
	});

	// 强制完工 or 取消强制完工
	$("#btn_complete,#btn_complete_cancel").on("click", function()
	{
		var rows = getSelectedRows();
		var ids = $(rows).map(function()
		{
			return this.id;
		}).get();
		var boolValue = $(this).attr("id") == "btn_complete" ? "YES" : "NO";
		var pathUrl = Helper.basePath + '/produce/report/completeTask';
		if (boolValue == "NO")
		{
			pathUrl = Helper.basePath + '/produce/report/completeTaskCancel';
		}
		if (Helper.isNotEmpty(rows))
		{
			Helper.post(pathUrl, {
				"ids[]" : ids,
				"boolValue" : boolValue
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
		url : Helper.basePath + "/produce/task/ajaxList",
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
		exportTypes : [ 'csv', 'txt', 'excel', 'doc' ],

		cookie : true,// 是否启用COOKIE
		cookiesEnabled : [ 'bs.table.columns' ],// 默认会记住很多属性，这里控制只记住列选择属性
		cookieIdTable : "print_report_list",// 必须制定唯一的表格cookieID

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
			field : 'createTime',
			title : '生产日期',
			width : 80,
			formatter : function(value, row, index)
			{
				if (Helper.isNotEmpty(row.createTime))
				{
					return new Date(row.createTime).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'deliveryTime',
			title : '交货日期',
			width : 80,
			formatter : function(value, row, index)
			{
				if (Helper.isNotEmpty(row.deliveryTime))
				{
					return new Date(row.deliveryTime).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'billNo',
			title : '生产单号',
			width : 100,
			formatter : function(value, row, index)
			{
				return billNoTransToUrl(value);
			}
		}, {
			field : 'sourceBillNo',
			title : '销售单号',
			width : 100,
			formatter : function(value, row, index)
			{
				if (value.split(",").length == 1)
				{
					return billNoTransToUrl(value);
				} else
				{
					return value;
				}
			}
		}, {
			field : 'customerBillNo',
			title : '客户单号',
			width : 100
		}, {
			field : 'customerName',
			title : '客户名称',
			width : 160
		}, {
			field : 'customerMaterialCode',
			title : '客户料号',
			width : 100
		}, {
			field : 'productName',
			title : '产品名称',
			width : 120
		}, {
			field : 'specifications',
			title : '产品规格',
			width : 100
		}, {
			field : 'produceQty',
			title : '生产数量',
			width : 100
		}, {
			field : 'partName',
			title : '部件名称',
			width : 100
		}, {
			field : 'procedureName',
			title : '工序名称',
			width : 100
		}, {
			field : 'yieldQty',
			title : '应产数',
			width : 100
		}, {
			field : 'reportQty',
			title : '已上报数',
			width : 100
		}, {
			field : 'unreportQty',
			title : '未上报数',
			width : 100
		}, {
			field : 'style',
			title : '上机规格',
			width : 100
		}, {
			field : 'machineName',
			title : '机台名称',
			width : 100
		} ],
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
			if (!Helper.basic.hasPermission('report:work:export'))
			{
				$(".export.btn-group").remove();
			}
			$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
		}
	});

	/* 查询强制完工状态 */
	$("input[name='completeFlag']").change(function()
	{
		$("#bootTable").bootstrapTable("refreshOptions", {
			pageNumber : 1
		});
		if ($(this).val() == 'YES')
		{
			$("#btn_complete_cancel").show();
			$("#btn_complete").hide();
			$("#btn_report").hide();
		} else
		{
			$("#btn_complete_cancel").hide();
			$("#btn_complete").show();
			$("#btn_report").show();
		}
	});

	// 获取选择项
	function getSelectedRows()
	{
		return $("#bootTable").bootstrapTable('getAllSelections');
	}

});

// 获取选择项
function getSelectedArray()
{
	$("#transmitForm").empty();
	var array = $('tbody input[type=checkbox]:checked').map(function()
	{
		return this.value
	}).get();
	$('tbody input[type=checkbox]:checked').each(function()
	{
		$("#transmitForm").append("<input type='hidden' name='ids' value='" + this.value + "'/>");
	});
	return array;
}

function responseHandler(res)
{
	return {
		rows : res.result,
		total : res.count
	};
}

function queryParams(params)
{
	params['dateMin'] = $("#dateMin").val().trim();
	params['dateMax'] = $("#dateMax").val().trim();
	params['billNo'] = $("#billNo").val().trim();
	params['productName'] = $("#productName").val().trim();
	params['customerName'] = $("#customerName").val().trim();
	params['completeFlag'] = $("input[name='completeFlag']:checked").val().trim();
	params['saleBillNo'] = $("#saleBillNo").val().trim();
	params['customerMaterialCode'] = $("#customerMaterialCode").val().trim();
	params['procedureName'] = $("#procedureName").val().trim();
	params['customerBillNo'] = $("#customerBillNo").val().trim();
	params['machineName'] = $("#machineName").val().trim();

	return params;
}

// 获取返回信息
function getCallInfo_customer(obj)
{
	$("#customerName").val(obj.name);
}

// 获取返回信息
function getCallInfo_product(obj)
{
	$("#productName").val(obj.name);
}
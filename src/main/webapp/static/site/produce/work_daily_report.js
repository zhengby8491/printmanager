$(function()
{ /* 员工信息 */
	$("#employee_quick_select").click(function()
	{
		Helper.popup.show('选择员工', Helper.basePath + '/quick/employee_select?multiple=false', '800', '500');
	});

	/* 更多 */
	$("#btn_more").click(function()
	{
		$("#more_div").toggle();
	});

	$("#bootTable").on('load-success.bs.table column-switch.bs.table page-change.bs.table search.bs.table', function()
	{
		/* 表格工具栏 */
		if ($(".glyphicon-th").next().html() == '')
		{
			$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
			if (!Helper.basic.hasPermission('daily:work:export'))
			{
				$(".export.btn-group").remove();
			}
			$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
		}
	});
	// 订单详情table
	$("#bootTable").bootstrapTable({
		url : Helper.basePath + "/produce/work/ajaxDailyReport",
		method : "post",
		contentType : 'application/json', // 设置请求头信息
		dataType : "json",
		pagination : true, // 是否显示分页（*）
		sidePagination : 'server',// 设置为服务器端分页
		pageList : Helper.bootPageList,
		queryParamsType : "",
		pageSize : 20,
		pageNumber : 1,
		queryParams : queryParams,// 参数
		responseHandler : responseHandler,
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
		cookieIdTable : "print_transmit_product_in",// 必须制定唯一的表格cookieID
		uniqueId : "id",// 定义列表唯一键
		columns : [
		// {
		// field: 'state',
		// checkbox: true,
		// visible: true,
		// width:40
		// },
		{
			field : 'index',
			title : '序号',
			width : 40,
			formatter : function(value, row, index)
			{
				return index + 1;
			}
		}, {
			field : 'type',
			title : '单据状态',
			width : 60,
			formatter : function(value, row, index)
			{
				if (row.isCancel == 'NO')
				{
					if (row.isCheck == 'YES')
					{

						return "已审核"
					} else
					{
						return "未审核"
					}
				}
				return "已取消"
			},
			cellStyle : function(value, row, index, field)
			{
				if (value == '已审核')
				{
					return {
						css : {
							"color" : "#080"
						}
					};
				} else
				{
					return {
						css : {
							"color" : "#f00"
						}
					};

				}
			}
		}, {
			field : 'createTime',
			title : '制单日期',
			width : 80,
			formatter : function(value, row, index)
			{
				if (Helper.isNotEmpty(row.report.createTime))
				{
					return new Date(row.report.createTime).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'employeeName',
			title : '员工信息',
			width : 60,
		}, {
			field : 'masterBillNo',
			title : '上报单号',
			width : 100,
			formatter : function(value, row, index)
			{
				return idTransToUrl(row.masterId, row.masterBillNo);
			}
		}, {
			field : 'billNo',
			title : '生产单号',
			width : 100,
			formatter : function(value, row, index)
			{
				return billNoTransToUrl(row.task.billNo);
			}
		}, {
			field : 'customerName',
			title : '客户名称',
			width : 160,
			formatter : function(value, row, index)
			{
				return row.task.customerName || "";
				;
			}
		}, {
			field : 'customerMaterialCode',
			title : '客户料号',
			width : 100,
			formatter : function(value, row, index)
			{
				return row.task.customerMaterialCode || "";
				;
			}
		}, {
			field : 'productName',
			title : '产品名称',
			width : 120,
			formatter : function(value, row, index)
			{
				return row.task.productName || "";
				;
			}
		}, {
			field : 'style',
			title : '产品规格',
			width : 100,
			formatter : function(value, row, index)
			{
				return row.task.specifications || "";
				;
			}
		}, {
			field : 'produceQty',
			title : '生产数量',
			width : 60,
			formatter : function(value, row, index)
			{
				return row.task.produceQty || "";
				;
			}
		}, {
			field : 'produceQty',
			title : '部件名称',
			width : 100,
			formatter : function(value, row, index)
			{
				return row.task.partName || "";
				;
			}
		}, {
			field : 'procedureName',
			title : '工序名称',
			width : 100,
			formatter : function(value, row, index)
			{
				return row.task.procedureName || "";
				;
			}
		}, {
			field : 'yieldQty',
			title : '应产数',
			width : 80,
			formatter : function(value, row, index)
			{
				return row.task.yieldQty || "";
				;
			}
		}, {
			field : 'reportQty',
			title : '上报数',
			width : 80,
			formatter : function(value, row, index)
			{
				return row.reportQty || "0";
				;
			}
		}, {
			field : 'qualifiedQty',
			title : '合格数',
			width : 60,
			formatter : function(value, row, index)
			{
				return row.qualifiedQty || "0";
				;
			}
		}, {
			field : 'unqualified',
			title : '不合格数',
			width : 60,
			formatter : function(value, row, index)
			{
				return row.unqualified || "0";
				;
			}
		}, {
			field : 'unqualified',
			title : '合格率',
			width : 60,
			formatter : function(value, row, index)
			{
				if (row.qualifiedQty / row.reportQty <= 1 && row.qualifiedQty / row.reportQty >= 0)
				{
					return parseInt(row.qualifiedQty / row.reportQty * 100) + '%' || "0";
					;
				} else
				{
					return 0 + '%'
				}
			}
		}, {
			field : 'style',
			title : '上机规格',
			width : 80,
			formatter : function(value, row, index)
			{
				return row.task.style || "";
				;
			}
		}, {
			field : 'startTime',
			title : '开始时间',
			width : 150,
			formatter : function(value, row, index)
			{
				if (Helper.isNotEmpty(row.task.createTime))
				{
					return new Date(row.startTime).format("yyyy-MM-dd hh:mm:ss");
				}
			}
		}, {
			field : 'endTime',
			title : '结束时间',
			width : 150,
			formatter : function(value, row, index)
			{
				if (Helper.isNotEmpty(row.task.createTime))
				{
					return new Date(row.endTime).format("yyyy-MM-dd hh:mm:ss");
				}
			}
		} ],
		onDblClickRow : function(row)
		{
			// 双击选中事件
			order_view(row.masterId);
		},
		onClickRow : function(row, $element)
		{
			$element.addClass("tr_active").siblings().removeClass("tr_active");
		}
	});

	function queryParams(params)
	{
		params['dateMin'] = $("#dateMin").val().trim();
		params['dateMax'] = $("#dateMax").val().trim();
		params['procedureName'] = $("#procedureName").val().trim();
		params['customerName'] = $("#customerName").val().trim();
		params['productName'] = $("#productName").val().trim();
		var auditFlag = $("input[name='auditFlag']:checked").val();
		if ($.trim(auditFlag) != "")
			params['auditFlag'] = $.trim(auditFlag);

		params['masterBillNo'] = $("#masterBillNo").val().trim();
		params['billNo'] = $("#billNo").val().trim();
		params['employeeName'] = $("#employeeName").val().trim();
		params['customerMaterialCode'] = $("#customerMaterialCode").val().trim();

		return params;
	}

	/* 查询强制完工状态 */
	$("input[name='auditFlag']").change(function()
	{
		$("#bootTable").bootstrapTable("refreshOptions", {
			pageNumber : 1
		});

	});
});

// 获取员工信息
function getCallInfo_empoyee(obj)
{
	$("#employeeName").val(obj.name);
}
// 查看
function order_view(id)
{
	if (!Helper.isNotEmpty(id))
	{
		return;
	}
	var url = Helper.basePath + '/produce/report/view/' + id;
	var title = "产量上报";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}
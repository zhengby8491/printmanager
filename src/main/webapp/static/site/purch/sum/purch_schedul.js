$(function()
{
	/* 判断当前用户是否有权限查看金额 */
	var hasPermission = Helper.basic.hasPermission('purch:purch_schedule:money');
	// 查询，刷新table
	$("#btn_search").click(function()
	{
		$("#bootTable").bootstrapTable("refreshOptions", {
			pageNumber : 1
		});
	});
	/* 更多 */
	$("#btn_more").click(function()
	{
		$("#more_div").toggle();
		$("#more_div2").toggle();
	});
	// 订单详情table
	$("#bootTable").bootstrapTable({
		url : Helper.basePath + "/purch/sum/purch_schedule_list",
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
		cookieIdTable : "print_purch_schedule_list",// 必须制定唯一的表格cookieID

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
			field : 'schedule',
			title : '进度',
			width : 150,
			cellStyle : function(value, row, index, field)
			{
				return {
					css : {
						"color" : "#ccc",
						"font-weight" : "bold"
					}
				};
			},
			formatter : function(value, row, index)
			{
				var r1 = "";
				var r2 = "";
				var r3 = "";
				if (row.stockQty != 0)
				{
					r1 = "style='color:#25A73A'";
				}
				if (row.reconcilQty != 0)
				{
					r2 = "style='color:#25A73A'";
				}
				if (row.paymentMoney != 0)
				{
					r3 = "style='color:#25A73A'";
				}
				var str = "<span style='color:#25A73A'>订单</span>-<span " + r1 + ">入库</span>-<span " + r2 + ">对账</span>-<span " + r3 + ">付款</span>"
				return str;
			}
		}, {
			field : 'createTime',
			title : '制单日期',
			width : 90,
			formatter : function(value, row, index)
			{
				return new Date(value).format("yyyy-MM-dd");
			}
		}, {
			field : 'deliveryTime',
			title : '交货日期',
			width : 90,
			formatter : function(value, row, index)
			{
				return new Date(value).format("yyyy-MM-dd");
			}
		}, {
			field : 'workNo',
			title : '生产单号',
			width : 110,
			formatter : function(value, row, index)
			{
				return billNoTransToUrl(value);
			}
		}, {
			field : 'billNo',
			title : '采购单号',
			width : 110,
			formatter : function(value, row, index)
			{
				return billNoTransToUrl(value);
			}
		}, {
			field : 'productNames',
			title : '成品名称',
			width : 160
		}, {
			field : 'supplierName',
			title : '供应商名称',
			width : 160
		}, {
			field : 'employeeName',
			title : '采购员',
			width : 90
		}, {
			field : 'materialName',
			title : '材料名称',
			width : 100
		}, {
			field : 'style',
			title : '材料规格',
			width : 90
		}, {
			field : 'weight',
			title : '克重',
			width : 90
		}, {
			field : 'unit',
			title : '单位',
			width : 90
		}, {
			field : 'purchQty',
			title : '订单数量',
			width : 90
		}, {
			field : 'money',
			title : '金额',
			visible : hasPermission,
			width : 90
		}, {
			field : 'stockQty',
			title : '已入库数量',
			width : 90
		}, {
			field : 'noStockQty',
			title : '未入库数量',
			width : 90,
			formatter : function(value, row, index)
			{
				return (Number(row.purchQty).subtr(Number(row.stockQty))) < 0 ? 0 : (Number(row.purchQty).subtr(Number(row.stockQty)));
			}
		}, {
			field : 'refundQty',
			title : '退货数量',
			width : 90
		}, {
			field : 'reconcilQty',
			title : '对账数量',
			width : 90
		}, {
			field : 'noReconcilQty',
			title : '未对账数量',
			width : 90,
			formatter : function(value, row, index)
			{
				return (Number(row.stockQty).subtr(Number(row.reconcilQty))) < 0 ? 0 : (Number(row.stockQty).subtr(Number(row.reconcilQty)));
			}
		}, {
			field : 'reconcilMoney',
			title : '对账金额',
			visible : hasPermission,
			width : 90
		}, {
			field : 'paymentMoney',
			title : '已付款金额',
			visible : hasPermission,
			width : 90
		}, {
			field : 'noPaymentMoney',
			title : '未付款金额',
			visible : hasPermission,
			width : 90,
			formatter : function(value, row, index)
			{
				return (Number(row.reconcilMoney).subtr(Number(row.paymentMoney))) < 0 ? 0 : Number((Number(row.reconcilMoney).subtr(Number(row.paymentMoney)))).tomoney();
			}
		} ],
		onDblClickRow : function(row)
		{
			if (row.purchOrderId == null)
			{
				return;
			}
			view(row.purchOrderId);
		},
		onClickRow : function(row, $element)
		{
			$element.addClass("tr_active").siblings().removeClass("tr_active");
		},
		onLoadSuccess : function()
		{
			// alert("数据加载完成");
			if ($(".glyphicon-th").next().html() == '')
			{
				$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
				// 控制筛选菜单金额选择
				if (!hasPermission)
				{
					$("div[title='列'] ul[role=menu]").find("input[value=13]").parent().parent().remove();
					$("div[title='列'] ul[role=menu]").find("input[value=19]").parent().parent().remove();
					$("div[title='列'] ul[role=menu]").find("input[value=20]").parent().parent().remove();
					$("div[title='列'] ul[role=menu]").find("input[value=21]").parent().parent().remove();
				}
				;
				if (!Helper.basic.hasPermission('purch:purch_schedule:export'))
				{
					$(".export.btn-group").remove();
				}
				$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
			}
		},
		onColumnSwitch : function(field, checked)
		{
			// 在使用筛选增加或减少列时重置表格列拖动效果
			bootstrapTable_ColDrag($("#bootTable"));
		},
	});
});

function responseHandler(res)
{
	return {
		rows : res.result,
		total : res.count
	};
}
function queryParams(params)
{
	params['dateMin'] = $("#dateMin").val();
	params['dateMax'] = $("#dateMax").val();
	params['billNo'] = $("#billNo").val();
	params['supplierName'] = $("#supplierName").val();
	params['materialName'] = $("#materialName").val();
	params['materialClassId'] = $("#materialClassId").val();
	params['supplierClassId'] = $("#supplierClassId").val();
	params['employeeId'] = $("#employeeId").val();

	params['workBillNo'] = $("#workNo").val();
	params['productStyle'] = $("#style").val();
	params['deliverDateMin'] = $("#deliveryTimeMin").val();
	params['deliverDateMax'] = $("#deliveryTimeMax").val();
	return params;
}
function view(id)
{
	var url = Helper.basePath + '/purch/order/view/' + id;
	var title = "采购订单";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}
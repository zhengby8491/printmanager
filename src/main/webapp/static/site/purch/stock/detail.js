$(function()
{
	/* 判断当前用户是否有权限查看金额 */
	var hasPermission = Helper.basic.hasPermission('purch:stock_detail:money');
	// 查询，刷新table
	$("#btn_search").click(function()
	{
		$("#detailList").bootstrapTable("refreshOptions", {
			pageNumber : 1
		});
	});

	/* 更多 */
	$("#btn_more").click(function()
	{
		$("#more_div").toggle();
		$("#more_div2").toggle();
	});

	$("#detailList").bootstrapTable({
		url : Helper.basePath + "/purch/stock/ajaxDetailList",
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
		cookieIdTable : "print_purch_stockDetail_master",// 必须制定唯一的表格cookieID

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
			field : 'createTime',
			title : '制单日期',
			width : 80,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return;
				}
				return new Date(row.master.createTime).format("yyyy-MM-dd");
			}
		}, {
			field : 'billNo',
			title : '入库单号',
			width : 140,
			formatter : function(value, row, index)
			{
				return billNoTransToUrl(row.master.billNo);
			}
		}, {
			field : 'supplierName',
			title : '供应商名称',
			width : 220,
			formatter : function(value, row, index)
			{
				return row.master.supplierName;
			}
		}, {
			field : 'code',
			title : '材料编号',
			visible : false,
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
			width : 60
		}, {
			field : 'purchUnitName',
			title : '单位',
			width : 60
		}, {
			field : 'qty',
			title : '入库数量',
			width : 80
		}, {
			field : 'reconcilQty',
			title : '已对账数量',
			width : 80
		}, {
			field : 'noReconcilQty',
			title : '未对账数量',
			visible : false,
			width : 80,
			formatter : function(value, row, index)
			{
				return (row.qty - row.reconcilQty) < 0 ? 0 : Number(row.qty).subtr(Number(row.reconcilQty));
			}
		}, {
			field : 'refundQty',
			title : '已退货数量',
			visible : false,
			width : 80
		}, {
			field : 'valuationQty',
			title : '计价数量',
			width : 80
		}, {
			field : 'valuationPrice',
			title : '计价单价',
			visible : hasPermission,
			width : 80
		}, {
			field : 'money',
			title : '金额',
			visible : hasPermission,
			width : 100
		}, {
			field : 'tax',
			title : '税额',
			visible : hasPermission,
			width : 80
		}, {
			field : 'noTaxMoney',
			title : '不含税金额',
			visible : false,
			width : 120
		}, {
			field : 'sourceBillTypeText',
			title : '源单类型',
			width : 80
		}, {
			field : 'workBillNo',
			title : '生产单号',
			width : 140,
			formatter : function(value, row, index)
			{
				return billNoTransToUrl(value);
			}
		}, {
			field : 'sourceBillNo',
			title : '源单单号',
			width : 140,
			formatter : function(value, row, index)
			{
				return billNoTransToUrl(value);
			}
		}, {
			field : 'productNames',
			title : '成品名称',
			width : 100
		}, {
			field : 'employeeName',
			title : '采购员',
			visible : false,
			width : 80,
			formatter : function(value, row, index)
			{
				return row.master.employeeName;
			}
		}, {
			field : 'currencyTypeText',
			title : '币别',
			visible : false,
			width : 60,
			formatter : function(value, row, index)
			{
				return row.master.currencyTypeText;
			}
		}, {
			field : 'warehouseName',
			title : '仓库',
			width : 80
		}, {
			field : 'memo',
			title : '备注',
			'class' : 'memoView',
			width : 50
		} ],
		onDblClickRow : function(row)
		{
			// 双击选中事件
			if (row.id == null)
			{
				return;
			}
			view(row.master.id);
		},
		onClickRow : function(row, $element)
		{
			// 单击事件
			$element.addClass("tr_active").siblings().removeClass("tr_active");
		},
		onLoadSuccess : function()
		{
			$("#detailList tbody").find("tr:last").find("td:first").text("合计");
			var _tds = $("#detailList tbody").find("tr:last").find("td");
			for (var index = 0; index < _tds.length; index++)
			{
				if (_tds.eq(index).text() == "-")
				{
					_tds.eq(index).text('');
				}
			}

			// alert("数据加载完成");
			if ($(".glyphicon-th").next().html() == '')
			{
				$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
				// 控制筛选菜单金额选择
				if (!hasPermission)
				{
					$("div[title='列'] ul[role=menu]").find("input[value=15]").parent().parent().remove();
					$("div[title='列'] ul[role=menu]").find("input[value=16]").parent().parent().remove();
					$("div[title='列'] ul[role=menu]").find("input[value=17]").parent().parent().remove();
					$("div[title='列'] ul[role=menu]").find("input[value=18]").parent().parent().remove();
				}
				if (!Helper.basic.hasPermission('purch:stock_detail:export'))
				{
					$(".export.btn-group").remove();
				}
				$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
			}
		},
		onColumnSwitch : function(field, checked)
		{
			$("#detailList tbody").find("tr:last").find("td:first").text("合计");
			var _tds = $("#detailList tbody").find("tr:last").find("td");
			for (var index = 0; index < _tds.length; index++)
			{
				if (_tds.eq(index).text() == "-")
				{
					_tds.eq(index).text('');
				}
			}

			// 在使用筛选增加或减少列时重置表格列拖动效果
			bootstrapTable_ColDrag($("#detailList"));
		},
		onLoadError : function()
		{
			// alert("数据加载异常");
		}

	});

	$("#supplier_quick_select").click(function()
	{
		Helper.popup.show('选择供应商', Helper.basePath + '/quick/supplier_select?multiple=false', '900', '490');
	});
	$("#material_quick_select").click(function()
	{
		Helper.popup.show('选择材料', Helper.basePath + '/quick/material_select?multiple=false', '900', '490');
	});
	$("input[type='radio']").change(function()
	{
		$("#btn_search").click();
	})

	$("#detailList").on('load-success.bs.table', function()
	{
		// bootstrap_table加载完后触发列拖动效果
		bootstrapTable_ColDrag($("#detailList"));
	});
})

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
	params['warehouseId'] = $("#warehouseId").val();
	params['auditFlag'] = $("input[name='auditFlag']:checked").val() == "-1" ? null : $("input[name='auditFlag']:checked").val();

	params['sourceBillNo'] = $("#sourceBillNo").val();
	params['workBillNo'] = $("#workBillNo").val();
	params['productStyle'] = $("#style").val();
	return params;
}
function view(id)
{
	var url = Helper.basePath + '/purch/stock/view/' + id;
	var title = "采购入库";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}
// 获取返回信息
function getCallInfo_supplier(obj)
{
	$("#supplierName").val(obj.name);
}
// 获取返回信息
function getCallInfo_material(obj)
{
	$("#materialName").val(obj.name);
}
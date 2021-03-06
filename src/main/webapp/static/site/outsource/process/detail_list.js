$(function()
{
	/* 判断当前用户是否有权限查看金额 */
	var hasPermission = Helper.basic.hasPermission('outsource:process_detail:money');
	/* 选择供应商 */
	// $("#supplier_quick_select").click(function()
	// {
	// Helper.popup.show('选择供应商', Helper.basePath +
	// '/quick/supplier_select?multiple=false', '900', '490');
	// });
	/* 选择工序 */
	// $("#procedure_quick_select").click(function()
	// {
	// Helper.popup.show('选择工序', Helper.basePath +
	// '/quick/procedure_select?multiple=false', '900', '490');
	// });
	/* 更多 */
	$("#btn_more").click(function()
	{
		$("#more_div").toggle();
		$("#more_div2").toggle();
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
		url : Helper.basePath + "/outsource/process/ajaxDetailList",
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

		// resizable: true, //是否启用列拖动
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
		cookieIdTable : "print_outsource_process_detail",// 必须制定唯一的表格cookieID

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
			field : 'master_createTime',
			title : '制单日期',
			width : 100,
			formatter : function(value, row, index)
			{
				if (row.master.createTime)
				{
					return new Date(row.master.createTime).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'master_deliveryTime',
			title : '交货日期',
			width : 100,
			formatter : function(value, row, index)
			{
				if (row.master.deliveryTime)
				{
					return new Date(row.master.deliveryTime).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'typeText',
			title : '发外类型',
			width : 100
		}, {
			field : 'sourceBillTypeText',
			title : '源单类型',
			width : 100
		}, {
			field : 'sourceBillNo',
			title : '源单单号',
			width : 100,
			formatter : function(value, row, index)
			{
				return billNoTransToUrl(value);
			}
		}, {
			field : 'productNames',
			title : '成品名称',
			width : 100,
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
			width : 100
		}, {
			field : 'master_billNo',
			title : '加工单号',
			width : 140,
			formatter : function(value, row, index)
			{
				return billNoTransToUrl(row.master.billNo);
			}
		}, {
			field : 'master_supplierName',
			title : '供应商名称',
			width : 180,
			formatter : function(value, row, index)
			{
				return row.master.supplierName
			}
		}, {
			field : 'partName',
			title : '部件名称',
			width : 140
		}, {
			field : 'procedureTypeText',
			title : '工序类型',
			width : 100
		}, {
			field : 'qty',
			title : '加工数量',
			width : 100
		}, {
			field : 'arriveQty',
			title : '已到货数量',
			width : 100
		}, {
			field : 'arriveQty2',
			title : '未到货数量',
			width : 80,
			formatter : function(value, row, index)
			{
				return (row.qty - row.arriveQty).toFixed(2);
			}
		}, {
			field : 'price',
			title : '单价',
			visible : hasPermission,
			width : 100
		}, {
			field : 'money',
			title : '金额',
			visible : hasPermission,
			width : 100
		}, {
			field : 'tax',
			title : '税额',
			visible : hasPermission,
			width : 100
		}, {
			field : 'noTaxMoney',
			title : '不含税金额',
			visible : hasPermission,
			width : 100
		}, {
			field : 'processRequire',
			title : '工序要求',
			width : 160
		}, {
			field : 'memo',
			title : '备注',
			'class' : 'memoView',
			width : 100
		} ],
		onLoadSuccess : function(data)
		{
			// alert("数据加载完成");
			if ($(".glyphicon-th").next().html() == '')
			{
				$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
				// 控制筛选菜单金额选择
				if (!hasPermission)
				{
					$("div[title='列'] ul[role=menu]").find("input[value=16]").parent().parent().remove();
					$("div[title='列'] ul[role=menu]").find("input[value=17]").parent().parent().remove();
					$("div[title='列'] ul[role=menu]").find("input[value=18]").parent().parent().remove();
					$("div[title='列'] ul[role=menu]").find("input[value=19]").parent().parent().remove();
				}
				if (!Helper.basic.hasPermission('outsource:process_detail:export'))
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
			var url = Helper.basePath + '/outsource/process/view/' + row.master.id;
			var title = "发外加工单";
			admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"))
		},
		onClickRow : function(row, $element)
		{
			$element.addClass("tr_active").siblings().removeClass("tr_active");
		}
	});

});

// 获取返回信息
function getCallInfo_supplier(obj)
{
	$("#supplierName").val(obj.name);
}
// 获取返回信息
function getCallInfo_procedure(obj)
{
	$("#procedureName").val(obj.name);
}
// 请求参数
function queryParams(params)
{
	params['dateMin'] = $("#dateMin").val();
	params['dateMax'] = $("#dateMax").val();
	params['billNo'] = $("#billNo").val();
	params['supplierName'] = $("#supplierName").val();
	params['procedureName'] = $("#procedureName").val();
	params['supplierClassId'] = $("#supplierClassId").val();
	params['procedureType'] = $("#procedureType").val() == "-1" ? null : $("#procedureType").val();
	params['outSourceType'] = $("#outSourceType").val() == "-1" ? null : $("#outSourceType").val();
	params['auditFlag'] = $("input[name='auditFlag']:checked").val() == "-1" ? null : $("input[name='auditFlag']:checked").val();

	params['sourceBillNo'] = $("#sourceBillNo").val();
	params['productName'] = $("#productName").val();
	params['deliverDateMin'] = $("#deliveryTimeMin").val();
	params['deliverDateMax'] = $("#deliveryTimeMax").val();
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
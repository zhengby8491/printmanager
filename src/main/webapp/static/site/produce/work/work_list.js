$(function()
{
	/* 选择客户 */
	$("#customer_quick_select").click(function()
	{
		Helper.popup.show('选择客户', Helper.basePath + '/quick/customer_select?multiple=false', '900', '500');
	});
	/* 选择产品 */
	$("#product_quick_select").click(function()
	{
		Helper.popup.show('选择产品', Helper.basePath + '/quick/product_select?multiple=false', '900', '490');
	});

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
		url : Helper.basePath + "/produce/work/ajaxDetailList",
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
		cookieIdTable : "print_produce_work_master",// 必须制定唯一的表格cookieID

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
			field : 'productType',
			title : '印刷类型',
			width : 80,
			formatter : function(value, row, index)
			{
				return row.master.productTypeText;
			}
		}, {
			field : 'billTypeText',
			title : '工单类型',
			width : 80,
			formatter : function(value, row, index)
			{
				return row.master.billTypeText;
			}
		}, {
			field : 'isOutSource',
			title : '整单发外',
			width : 80,
			formatter : function(value, row, index)
			{
				if ('YES' == row.master.isOutSource)
					return '是';
				else
					return '否';
			}
		}, {
			field : 'createTime',
			title : '制单日期',
			width : 80,
			formatter : function(value, row, index)
			{
				return new Date(row.master.createTime).format("yyyy-MM-dd");
			}
		}, {
			field : 'billNo',
			width : 140,
			title : '生产单号',
			formatter : function(value, row, index)
			{
				return billNoTransToUrl(row.master.billNo);
			}
		}, {
			field : 'deliveryTime',
			title : '交货日期',
			width : 80,
			formatter : function(value, row, index)
			{
				if (value)
				{
					return new Date(value).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'sourceBillNo',
			width : 120,
			title : '销售单号',
			formatter : function(value, row, index)
			{
				return billNoTransToUrl(value);
			}
		}, {
			field : 'customerBillNo',
			title : '客户单号',
			width : 120,

		}, {
			field : 'customerName',
			width : 200,
			title : '客户名称'
		}, {
			field : 'customerMaterialCode',
			title : '客户料号',
			width : 120
		}, {
			field : 'productName',
			width : 120,
			title : '成品名称'
		}, {
			field : 'productCode',
			title : '产品编号',
			width : 120
		}, {
			field : 'style',
			title : '产品规格',
			width : 100
		}, {
			field : 'unitName',
			title : '单位',
			width : 60
		}, {
			field : 'sourceQty',
			width : 80,
			title : '订单数量',
			width : 80
		}, {
			field : 'spareProduceQty',
			width : 80,
			title : '备品数量'
		}, {
			field : 'produceQty',
			width : 80,
			title : '生产数量'
		}, {
			field : 'inStockQty',
			width : 80,
			title : '已入库数量'
		}, {
			field : 'noInStockQty',
			width : 80,
			title : '未入库数量',
			formatter : function(value, row, index)
			{
				return (row.produceQty - row.inStockQty) < 0 ? 0 : row.produceQty - row.inStockQty;
			}

		},{
			field : 'createName',
			width : 80,
			title : '制单人',
			formatter : function(value, row, index)
			{
				return row.master.createName;
			}
		},{
			field : 'checkTime',
			width : 80,
			title : '审核日期',
			formatter : function(value, row, index)
			{
				return new Date(row.master.checkTime).format("yyyy-MM-dd");
			}
		},{
			field : 'checkUserName',
			width : 80,
			title : '审核人',
			formatter : function(value, row, index)
			{
				return row.master.checkUserName;
			}
		}, {
			field : 'memo',
			title : '备注',
			'class' : 'memoView',
			width : 100,
			formatter : function(value, row, index)
			{
				return row.master.memo;
			}
		}, {
			field : 'operator',
			title : '操作',
			width : 120,
			formatter : function(value, row, index)
			{
				var operator = "";
				if (Helper.basic.hasPermission('produce:work:list'))
				{
					operator += '<span class="table_operator"><a title="查看" href="javascript:;" onclick="work_view(' + row.master.id + ')" style="padding: 0 8px; color: green"><i class="fa fa-search"></i></a></span>';
				}
				if (Helper.isNotEmpty(row.master.isCheck) && row.master.isCheck == 'NO')
				{
					operator = "";
					if (Helper.basic.hasPermission('produce:work:list'))
					{
						operator += '<span class="table_operator"><a title="查看" href="javascript:;" onclick="work_view(' + row.master.id + ')" style="padding: 0 8px; color: green"><i class="fa fa-search"></i></a>';
					}
					if (Helper.basic.hasPermission('produce:work:edit'))
					{
						operator += '<a title="编辑" href="javascript:;" onclick="work_edit(' + row.master.id + ')" style="padding: 0 8px; color: green"><i class="fa fa-pencil"></i></a>';
					}
					if (Helper.basic.hasPermission('produce:work:del'))
					{
						operator += '<a title="删除" href="javascript:;" onclick="work_del(this,' + row.master.id + ')" style="padding: 0 8px; color: red"><i class="fa fa-trash-o"></i></a></span>';
					}
				}
				return operator;

			}
		}

		],
		onDblClickRow : function(row)
		{
			work_view(row.masterId);
		},
		onClickRow : function(row, $element)
		{
			$element.addClass("tr_active").siblings().removeClass("tr_active");
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
			if (!Helper.basic.hasPermission('produce:work_detail:export'))
			{
				$(".export.btn-group").remove();
			}
			$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
		}
	});
});
function responseHandler(res)
{
	console.info(res);
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
	params['customerName'] = $("#customerName").val();
	params['productName'] = $("#productName").val();
	params['deliverDateMin'] = $("#deliverDateMin").val();
	params['deliverDateMax'] = $("#deliverDateMax").val();
	params['customerMaterialCode'] = $("#customerMaterialCode").val();
	params['saleBillNo'] = $("#saleBillNo").val();
	params['auditFlag'] = $("input[name='auditFlag']:checked").val() == "-1" ? null : $("input[name='auditFlag']:checked").val();
	
	params['createName'] = $("#createName").val();
	params['master.checkUserName'] = $("#checkUserName").val();
	params['master.checkTime'] = $("#checkTime").val();
	
	params['customerBillNo'] = $("#customerBillNo").val();
	params['productStyle'] = $("#style").val();
	params['isOutSource'] = $("#isOutSource").val() == "-1" ? null : $("#isOutSource").val();
	params['productType'] = $("#productType").val() == "-1" ? null : $("#productType").val();
	return params;
}

// 获取返回信息
function getCallInfo_customer(obj)
{
	$("#customerName").val(obj.name);
}
/* 查看 */
function work_view(id)
{
	var url = Helper.basePath + '/produce/work/view/' + id;
	var title = "生产工单";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"))
}
// 获取返回信息
function getCallInfo_product(obj)
{
	$("#productName").val(obj.name);
}
/* 新增 */
function work_create(productType)
{
	var url = "";
	if (productType != null)
	{
		url = Helper.basePath + '/produce/work/create?productType=' + productType;
	} else
	{
		url = Helper.basePath + '/produce/work/create';
	}

	var title = "生产工单";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}
/* 编辑 */
function work_edit(id)
{
	var url = Helper.basePath + '/produce/work/edit/' + id;
	var title = "生产工单";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}

/* 删除 */
function work_del(obj, id)
{
	Helper.message.confirm('确认要删除吗？', function(index)
	{
		Helper.post(Helper.basePath + '/produce/work/del/' + id, function(data)
		{
			if (data.success)
			{
				$("#bootTable").bootstrapTable("removeByUniqueId", id);
				$("#bootTable").bootstrapTable("refresh");
				Helper.message.suc('已删除!');
			} else
			{
				Helper.message.warn('删除失败：' + data.message);
			}
		});
	});
}
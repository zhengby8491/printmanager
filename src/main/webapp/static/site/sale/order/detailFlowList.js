$(function()
{
	/* 判断当前用户是否有权限查看金额 */
	var hasPermission = Helper.basic.hasPermission('sale:flow:money');
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
		url : Helper.basePath + "/sale/order/ajaxFlowList",
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
		cookieIdTable : "print_sale_order_detail",// 必须制定唯一的表格cookieID

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
			field : 'progress',
			title : '进度',
			width : 180,
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
				var progress = [ '订单', '工单', '入库', '送货', '对账', '收款' ];
				if (row.qty > 0)
				{
					progress.splice(0, 1, '<span class="c-green">订单</span>');
				}
				if (row.produceedQty > 0)
				{
					if (!row.produceedCheck)
					{
						progress.splice(1, 1, '<span class="c-green">工单</span>');
					} else
					{
						progress.splice(1, 1, '<span class="c-red">工单</span>');
					}
				}
				if (row.stockQty > 0)
				{
					if (!row.stockCheck)
					{
						progress.splice(2, 1, '<span class="c-green">入库</span>');
					} else
					{
						progress.splice(2, 1, '<span class="c-red">入库</span>');
					}
				}
				if (row.deliverQty > 0)
				{
					if (!row.deliverCheck)
					{
						progress.splice(3, 1, '<span class="c-green">送货</span>');
					} else
					{
						progress.splice(3, 1, '<span class="c-red">送货</span>');
					}
				}
				if (row.reconcilQty > 0)
				{
					if (!row.reconcilCheck)
					{
						progress.splice(4, 1, '<span class="c-green">对账</span>');
					} else
					{
						progress.splice(4, 1, '<span class="c-red">对账</span>');
					}
				}
				if (row.receiveMoney > 0)
				{
					if (!row.receiveCheck)
					{
						progress.splice(5, 1, '<span class="c-green">收款</span>');
					} else
					{
						progress.splice(5, 1, '<span class="c-red">收款</span>');
					}
				}

				return progress.join('-');
			}
		}, {
			field : 'createTime',
			title : '制单日期',
			width : 80,
			formatter : function(value, row, index)
			{
				if (row.master.createTime)
				{
					return new Date(row.master.createTime).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'deliveryTime',
			title : '交货日期',
			width : 80,
			formatter : function(value, row, index)
			{
				return new Date(value).format("yyyy-MM-dd");
			}
		}, {
			field : 'billNo',
			title : '销售单号',
			width : 140,
			formatter : function(value, row, index)
			{
				return billNoTransToUrl(row.master.billNo);
			}
		}, {
			field : 'customerBillNo',
			title : '客户单号',
			width : 120,
			formatter : function(value, row, index)
			{
				return row.master.customerBillNo
			}
		}, {
			field : 'customerName',
			title : '客户名称',
			width : 180,
			formatter : function(value, row, index)
			{
				return row.master.customerName
			}
		}, {
			field : 'productCode',
			title : '产品编号',
			width : 100
		}, {
			field : 'productName',
			title : '成品名称',
			width : 120
		}, {
			field : 'customerMaterialCode',
			title : '客户料号',
			width : 100
		}, {
			field : 'style',
			title : '产品规格',
			width : 80
		}, {
			field : 'qty',
			title : '订单数量',
			width : 80
		}, {
			field : 'produceedQty',
			title : '生产数量',
			width : 80
		}, {
			field : 'produceedQty',
			title : '未生产数量',
			width : 80,
			formatter : function(value, row, index)
			{
				return (row.qty - value) < 0 ? 0 : (row.qty - value);
			}
		}, {
			field : 'stockQty',
			title : '入库数量',
			width : 80
		}, {
			field : 'stockQty',
			title : '未入库数量',
			width : 80,
			formatter : function(value, row, index)
			{
				return (row.qty - value) < 0 ? 0 : (row.qty - value);
			}
		}, {
			field : 'deliverQty',
			title : '送货数量',
			width : 80
		}, {
			field : 'deliverQty',
			title : '未送货数量',
			width : 80,
			formatter : function(value, row, index)
			{
				return (row.qty - value) < 0 ? 0 : (row.qty - value);
			}
		}, {
			field : 'reconcilQty',
			title : '对账数量',
			width : 80
		}, {
			field : 'reconcilQty',
			title : '未对账数量',
			width : 80,
			formatter : function(value, row, index)
			{
				return (row.qty - value) < 0 ? 0 : (row.qty - value);
			}
		}, {
			field : 'receiveMoney',
			title : '已收款金额',
			visible : hasPermission,
			width : 80
		}, {
			field : 'receiveMoney',
			title : '未收款金额',
			visible : hasPermission,
			width : 80,
			formatter : function(value, row, index)
			{
				return (row.money - value) < 0 ? 0 : (row.money - value);
			}
		}, {
			title : '产品图片',
			width : 60,
			formatter : function(value, row, index)
			{
				if (row.imgUrl != "" && row.imgUrl != null)
				{
					return '<img class="pimg" src="' + row.imgUrl + '"/>';
				} else
				{
					return "";
				}
			}
		} ],
		onLoadSuccess : function(data)
		{
			$("#bootTable tbody").find("tr:last").hide();
		},

		onColumnSwitch : function(field, checked)
		{
			$("#bootTable tbody").find("tr:last").hide();
			
			// 在使用筛选增加或减少列时重置表格列拖动效果
			bootstrapTable_ColDrag($("#bootTable"));
		},
		onDblClickRow : function(row)
		{
			var url = Helper.basePath + '/sale/order/view/' + row.master.id;
			var title = "销售订单";
			admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"))
		},
		onClickRow : function(row, $element)
		{
			$element.addClass("tr_active").siblings().removeClass("tr_active");
		}
	});
	$("#bootTable").on('load-success.bs.table column-switch.bs.table page-change.bs.table search.bs.table', function()
	{
		/* 表格工具栏 */
		if ($(".glyphicon-th").next().html() == '')
		{
			$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
			// 控制筛选菜单金额选择
			if (!hasPermission)
			{
				$("div[title='列'] ul[role=menu]").find("input[value=20]").parent().parent().remove();
				$("div[title='列'] ul[role=menu]").find("input[value=21]").parent().parent().remove();
			}
			if (!Helper.basic.hasPermission('sale:flow:export'))
			{
				$(".export.btn-group").remove();
			}
			$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
		}
	});

});

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
// 请求参数
function queryParams(params)
{
	params['dateMin'] = $("#dateMin").val();
	params['dateMax'] = $("#dateMax").val();
	params['billNo'] = $("#billNo").val();
	params['customerName'] = $("#customerName").val();// 客户ID传到后台
	params['productName'] = $("#productName").val();
	params['deliverDateMin'] = $("#deliverDateMin").val();
	params['deliverDateMax'] = $("#deliverDateMax").val();
	params['employeeId'] = $("#employeeId").val();
	params['customerClassId'] = $("#customerClassId").val();
	params['productClassId'] = $("#productClassId").val();
	params['auditFlag'] = 'YES';

	params['customerBillNo'] = $("#customerBillNo").val().trim();
	params['customerMaterialCode'] = $("#customerMaterialCode").val().trim();
	params['productStyle'] = $("#style").val().trim();

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
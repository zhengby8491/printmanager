$(function()
{
	/* 更多 */
	$("#btn_more").click(function()
	{
		$("#more_div").toggle();
	});

	// 查询，刷新table
	$("#btn_search").click(function()
	{
		$("#bootTable").bootstrapTable("refreshOptions", {
			pageNumber : 1
		});
	});

	// 订单详情table
	$("#bootTable").bootstrapTable({
		url : Helper.basePath + "/sys/buy/ajaxOrderList",
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
		cookieIdTable : "print_sys_product",// 必须制定唯一的表格cookieID

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
			field : 'createTime',
			title : '购买时间',
			width : 100,
			formatter : function(value, row, index)
			{
				return new Date(value).format("yyyy-MM-dd");
			}
		}, {
			field : 'billNo',
			title : '订单编号',
			width : 150
		}, {
			field : 'productName',
			title : '产品名称',
			width : 150
		}, {
			field : 'type',
			title : '购买类型',
			width : 100,
			formatter : function(value, row, index)
			{
				if (value == 1)
				{
					return '购买';
				}
				if (value == 2)
				{
					return '续费';
				}
			}
		}, {
			field : 'price',
			title : '金额',
			width : 100
		}, {
			field : 'tax',
			title : '税额',
			width : 100
		}, {
			field : 'userName',
			title : '用户名',
			width : 100
		}, {
			field : 'companyName',
			title : '公司名称',
			width : 180
		}, {
			field : 'linkMan',
			title : '联系人',
			width : 100
		}, {
			field : 'telephone',
			title : '联系电话',
			width : 100
		}, {
			field : 'inviter',
			title : '邀请人',
			width : 100
		}, {
			field : 'inviterPhone',
			title : '邀请电话',
			width : 100
		}, {
			field : 'invoiceInfor',
			title : '发票信息',
			width : 100,
			formatter : function(value, row, index)
			{
				if (value == "0")
				{
					return '不需要发票'
				}
				if (value == "1")
				{
					return '增值税发票6%';
				}
				if (value == "2")
				{
					return '增值税发票17%';
				}
			}
		}, {
			field : 'payTime',
			title : '支付时间',
			width : 150,
			formatter : function(value, row, index)
			{
				if (value)
					return new Date(value).format("yyyy-MM-dd hh:mm:ss");
				else
					return "-";
			}
		}, {
			field : 'payPrice',
			title : '支付金额',
			width : 100
		}, {
			field : 'isPay',
			title : '支付状态',
			width : 100,
			formatter : function(value, row, index)
			{
				if (value == "YES")
				{
					return '已支付'
				}
				if (value == "NO")
				{
					return '未支付';
				}
			}
		}, {
			field : 'orderState',
			title : '订单状态',
			width : 100,
			formatter : function(value, row, index)
			{
				if (value == 0)
				{
					return '已取消'
				}
				if (value == 1)
				{
					return '待支付';
				}
				if (value == 2)
				{
					return '已完成';
				}
			}
		}, {
			field : 'orderType',
			title : '订单类型',
			width : 100,
			formatter : function(value, row, index)
			{
				if (value == 1)
				{
					return '在线订单'
				}
				if (value == 2)
				{
					return '线下订单';
				}
			}
		}, {
			field : 'operator',
			title : '操作',
			width : 300,
			formatter : function(value, row, index)
			{
				if (row.orderType == 2 && Helper.basic.hasPermission('sys:buyOrder:create'))
				{
					return '&nbsp;&nbsp;<a href="#" style="color:green" onClick="orderEdit(' + row.id + ');">修改订单</a>';
				}
				var operator = "";
				if (row.isPay == "NO" && row.orderState == 1)
				{

					if (Helper.basic.hasPermission('sys:buyOrder:pay'))
					{
						operator += '&nbsp;&nbsp;<a  style="color:green" href="' + $("#ctx").val() + '/pay/step3/' + row.billNo + '" target="_bank">立即支付</a>';
					}
					if (Helper.basic.hasPermission('sys:buyOrder:cancel'))
					{
						operator += '&nbsp;&nbsp;<a  style="color:green" href="' + $("#ctx").val() + '/sys/buy/cancelOrder/' + row.id + '">取消订单</a>';
					}

				}
				if (row.isPay == "YES" && row.type == 1 && row.orderState == 2)
				{
					if (Helper.basic.hasPermission('sys:buyOrder:up'))
					{
						operator += '&nbsp;&nbsp;<a  style="color:green" href="#" onClick="upOrder(' + row.id + ');">版本升级</a>';
					}
				}

				return operator;

			}
		} ],
		onDblClickRow : function(row, $element)
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

			if (!Helper.basic.hasPermission('sys:buyOrder:export'))
			{
				$(".export.btn-group").remove();
			}
			$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
		}
	});

});

// 新增订单
function orderCreate()
{

	Helper.popup.show('新增购买信息', Helper.basePath + '/sys/buy/orderCreate', '1000', '550');
}

/* 修改订单 */
function orderEdit(id)
{
	Helper.popup.show('修改购买信息', Helper.basePath + '/sys/buy/orderEdit/' + id, '1000', '550');
}

// 升级订单
function upOrder(orderId)
{

	Helper.popup.show('选择产品', Helper.basePath + '/quick/buyProductSelect?orderId=' + orderId + "&type=1", '900', '490');
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
	params['dateMin'] = $("#dateMin").val();
	params['dateMax'] = $("#dateMax").val();
	params['tel'] = $("#telephone").val();
	params['inviter'] = $("#inviter").val();
	params['inviterPhone'] = $("#inviterPhone").val();
	params['isPay'] = $("#isPay").val();
	params['invoiceInfor'] = $("#invoiceInfor").val();
	params['productName'] = $("#productName").val();
	params['companyName'] = $("#companyName").val();
	params['companyLinkName'] = $("#linkMan").val();
	params['billNo'] = $("#billNo").val();
	params['orderType'] = $("#orderType").val();
	params['orderState'] = $("#orderState").val();
	params['userName'] = $("#userName").val();
	return params;
}

function triggerClick()
{
	$("#bootTable").bootstrapTable("refreshOptions", {
		pageNumber : 1
	});
}

// 修改购买产品的版本
function getCallInfo_buyOrder(row, orderId)
{
	if (row && orderId)
	{
		window.location.href = Helper.basePath + "/sys/buy/updateOrder/" + orderId + "/" + row.id;
	}
}
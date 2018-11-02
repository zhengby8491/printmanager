$(function()
{
	// 查询
	$("#btn_search").click(function()
	{
		$("#bootTable").bootstrapTable("refreshOptions", {
			pageNumber : 1
		});
	});
	// 选择确认
	$("#btn_ok").click(function()
	{
		// console.log(parent)
		parent.getCallInfo_deliverSingle($("#bootTable").bootstrapTable('getAllSelections'));
		Helper.popup.close();
	});

	$("#btn_cancel").click(function()
	{
		Helper.popup.close();
	});
	// console.log($("#multiple").val().toBoolean());
	// 订单详情table
	$("#bootTable").bootstrapTable({
		url : Helper.basePath + "/sale/deliver/ajaxDeliverList",
		method : "post",
		contentType : 'application/json', // 设置请求头信息
		dataType : "json",
		pagination : true, // 是否显示分页（*）
		sidePagination : 'server',// 设置为服务器端分页
		pageList : [ 10, 20, 50 ],
		queryParamsType : "",
		pageSize : 10,
		pageNumber : 1,
		queryParams : queryParams,// 参数
		responseHandler : responseHandler,

		// showColumns : true, //是否显示所有的列
		// minimumCountColumns : 2, //最少允许的列数
		striped : true, // 是否显示行间隔色
		cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
		sortable : false, // 是否启用排序
		clickToSelect : true, // 是否启用点击选中行
		height : 360, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
		cardView : false, // 是否显示详细视图
		detailView : false, // 是否显示父子表
		checkbox : $("#multiple").val().toBoolean(),// 开启多选
		uniqueId : "id",// 定义列表唯一键
		// resizable : true, //是否启用列拖动
		columns : [ {
			field : 'state',
			checkbox : true,
			visible : $("#multiple").val().toBoolean(),
			width : 40
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
			field : 'billNo',
			title : '送货单号',
			width : 80,
			formatter : function(value, row, index)
			{
				return row.master.billNo
			}
		}, {
			field : 'customerName',
			title : '客户名称',
			width : 80,
			formatter : function(value, row, index)
			{
				return row.master.customerName
			}
		}, {
			field : 'customerMaterialCode',
			title : '客户料号',
			width : 80
		}, {
			field : 'productName',
			title : '成品名称',
			width : 140
		}, {
			field : 'style',
			title : '产品规格',
			width : 80
		}, {
			field : 'unitName',
			title : '单位',
			width : 40

		}, {
			field : 'qty',
			title : '数量',
			width : 60
		}, {
			field : 'returnQty',
			title : '已退货数量',
			width : 80
		}, {
			field : 'qty_returnQty',
			title : '未退货数量',
			width : 80,
			formatter : function(value, row, index)
			{
				return row.qty - row.returnQty;
			}
		}, {
			filed : 'imgUrl',
			visible : false,
		} ],
		onDblClickRow : function(row)
		{
			// 双击选中事件
			selectRow(row);
		}

	});

});

function selectRow(row)
{
	parent.getCallInfo_deliverSingle_dbl(row);

}
// 请求参数
function queryParams(params)
{
	params['dateMin'] = $("#dateMin").val();
	params['dateMax'] = $("#dateMax").val();
	params['customerId'] = $("#customerId").val();
	params['billNo'] = $("#billNo").val();
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
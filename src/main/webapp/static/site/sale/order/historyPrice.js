$(function()
{
	// 查询
	$("#btn_search").click(function()
	{
		$("#bootTable").bootstrapTable("refreshOptions", {
			pageNumber : 1
		});
	});

	$("#btn_cancel").click(function()
	{
		Helper.popup.close();
	});
	// 订单详情table
	$("#bootTable").bootstrapTable({
		url : Helper.basePath + "/sale/order/ajaxDetailList",
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
		checkbox : true,// 开启多选
		uniqueId : "id",// 定义列表唯一键
		columns : [ {
			field : 'index',
			title : '序号',
			width : 20,
			formatter : function(value, row, index)
			{
				if (row.id)
				{
					return index + 1;
				}
			}
		}, {
			field : 'createTime',
			title : '制单日期',
			width : 50,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return;
				}
				if (row.master.createTime)
				{
					return new Date(row.master.createTime).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'master_billNo',
			title : '销售单号',
			width : 55,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return;
				}
				return row.master.billNo
			}
		}, {
			field : 'customerMaterialCode',
			title : '客户单号',
			width : 55,
			formatter : function(value, row, index)
			{
				return row.master.customerBillNo;
			}
		}, {
			field : 'customerCode',
			title : '客户编号',
			width : 45,
			formatter : function(value, row, index)
			{
				return row.master.customerCode;
			}
		}, {
			field : 'customerName',
			title : '客户名称',
			width : 100,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return;
				}
				return row.master.customerName;
			}
		}, {
			field : 'price',
			title : '单价',
			width : 25
		}, {
			field : 'qty',
			title : '数量',
			width : 30
		} ],
		onLoadSuccess : function(data)
		{
			if ($("#bootTable tbody").find("tr:last").find("td:first").val() == "")
			{
				$("#bootTable tbody").find("tr:last").remove();
			}
		}
	});

});

// 请求参数
function queryParams(params)
{
	params['dateMin'] = $("#dateMin").val();
	params['dateMax'] = $("#dateMax").val();
	params['customerName'] = $("#customerName").val();
	params['productId'] = $("#productId").val();
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
$(function()
{

	// 订单详情table
	$("#bootTable").bootstrapTable({
		url : Helper.basePath + "/stockproduct/inventory/ajaxNotCheckList",
		method : "post",
		contentType : 'application/json', // 设置请求头信息
		dataType : "json",
		pagination : false, // 是否显示分页（*）
		sidePagination : 'server',// 设置为服务器端分页
		queryParamsType : "",
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

		cookie : true,// 是否启用COOKIE
		cookieIdTable : "print_stockproduct_not_check_list",// 必须制定唯一的表格cookieID

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
			field : 'billTypeText',
			title : '单据类型',
			width : 80
		}, {
			field : 'billNo',
			title : '单据编号',
			width : 100
		}, ]
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
	return params;
}
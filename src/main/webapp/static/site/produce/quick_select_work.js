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
		url : Helper.basePath + "/produce/work/ajaxList",
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

		// showColumns : true, //是否显示所有的列
		// minimumCountColumns : 2, //最少允许的列数
		striped : true, // 是否显示行间隔色
		cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
		sortable : false, // 是否启用排序
		clickToSelect : true, // 是否启用点击选中行
		height : 360, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
		cardView : false, // 是否显示详细视图
		detailView : false, // 是否显示父子表
		checkbox : false,// 开启多选
		uniqueId : "id",// 定义列表唯一键
		// resizable : true, //是否启用列拖动
		columns : [ {
			field : 'state',
			checkbox : false,
			visible : false,
			width : 60
		}, {
			field : 'billTypeText',
			title : '单据类型',
			width : 100
		}, {
			field : 'billNo',
			title : '生产单号',
			width : 120
		}, {
			field : 'createTime',
			title : '工单日期',
			width : 80,
			formatter : function(value, row, index)
			{
				return new Date(value).format("yyyy-MM-dd");
			}
		}, {
			field : 'createName',
			title : '创建人',
			width : 80
		}, {
			field : 'memo',
			title : '备注',
			'class' : 'memoView',
			width : 180
		} ],
		onDblClickRow : function(row)
		{
			selectRow(row);
		}
	});

});

// 请求参数
function queryParams(params)
{
	params['dateMin'] = $("#dateMin").val();
	params['dateMax'] = $("#dateMax").val();
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
function selectRow(row)
{
	parent.getCallInfo_produce(row);
	Helper.popup.close();
}
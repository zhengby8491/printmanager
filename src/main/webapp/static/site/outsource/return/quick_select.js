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
		parent.getCallInfo_refund($("#bootTable").bootstrapTable('getAllSelections'));
		Helper.popup.close();
	});

	$("#btn_cancel").click(function()
	{
		Helper.popup.close();
	});

	// 订单详情table
	$("#bootTable").bootstrapTable({
		url : Helper.basePath + "/outsource/return/ajaxSource",
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
		// resizable : true, //是否启用列拖动
		columns : [ {
			field : 'state',
			checkbox : true,
			visible : true,
			width : 40
		}, {
			field : 'master_createTime',
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
			field : 'master_billNo',
			title : '到货单号',
			width : 100,
			formatter : function(value, row, index)
			{
				return row.master.billNo
			}
		}, {
			field : 'type',
			title : '发外类型',
			width : 100,
			formatter : function(value, row, index)
			{
				var show = Helper.basic.getEnumText('com.huayin.printmanager.persist.enumerate.OutSourceType', value, 'text');
				return show;
			}
		}, {
			field : 'workBillNo',
			title : '生产单号',
			width : 100
		}, {
			field : 'sourceBillNo',
			title : '加工单号',
			width : 100
		}, {
			field : 'productName',
			title : '工序名称',
			width : 100,
			formatter : function(value, row, index)
			{
				if (row.type == 'PRODUCT')
				{
					return row.productName;
				} else if (row.type == 'PROCESS')
				{
					return row.procedureName;
				}
			}
		}, {
			field : 'qty',
			title : '到货数量',
			width : 100
		}, {
			field : 'returnQty',
			title : '退货数量',
			width : 80
		}, {
			field : 'qty',
			title : '未退货数量',
			width : 80,
			formatter : function(value, row, index)
			{
				// 轮转工单的特殊处理小数位
				if (row.productType == 'ROTARY' && row.workProcedureType == 'PART')
				{
					return (row.qty - row.returnQty).roundFixed(2);
				} else
				{
					return row.qty - row.returnQty;
				}
			}
		}, {
			field : 'memo',
			title : '备注',
			width : 200
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
	params['supplierId'] = $("#supplierId").val();
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
	parent.getCallInfo_refund([ row ]);
}
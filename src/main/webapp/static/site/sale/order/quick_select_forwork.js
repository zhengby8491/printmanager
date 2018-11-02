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
		// var arr=$("#bootTable").bootstrapTable('getAllSelections');
		var _productArray = new Array();
		$.each($("#bootTable").bootstrapTable('getAllSelections'), function(i, value)
		{
			_productArray.push(convertSaleDetailToProduct(value));
		})
		parent.getCallInfo_productArray(_productArray);
		Helper.popup.close();
	});

	$("#btn_cancel").click(function()
	{
		Helper.popup.close();
	});
	// 订单详情table
	$("#bootTable").bootstrapTable({
		url : Helper.basePath + "/sale/order/ajaxSaleListForWork",
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
			field : 'state',
			checkbox : true,
			visible : true,
			width : 30
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
				if (row.master.createTime)
				{
					return new Date(row.master.createTime).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'master_billNo',
			title : '销售单号',
			width : 140,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return;
				}
				return row.master.billNo
			}
		}, {
			field : 'deliveryTime',
			title : '交货日期',
			width : 100,
			formatter : function(value, row, index)
			{
				if (value && row.id != null)
				{
					return new Date(value).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'master_customerName',
			title : '客户名称',
			width : 200,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return;
				}
				return row.master.customerName
			}
		}, {
			field : 'customerMaterialCode',
			title : '客户料号',
			width : 120
		}, {
			field : 'productName',
			title : '成品名称',
			width : 140
		}, {
			field : 'imgUrl',
			visible : false
		}, {
			field : 'qty',
			title : '销售数量',
			width : 80
		}, {
			field : 'produceedQty',
			title : '已生产数量',
			width : 80
		}, {
			field : 'spareQty',
			title : '备品数量',
			width : 80
		}, {
			field : 'produceSpareedQty',
			title : '已备品数量',
			width : 80
		} ],
		onLoadSuccess : function(data)
		{
			if ($("#bootTable tbody").find("tr:last").find("td:first").val() == "")
			{
				$("#bootTable tbody").find("tr:last").remove();
			}
		},
		onDblClickRow : function(row)
		{
			selectRow(row);
		}
	});

});

function selectRow(row)
{
	var obj = convertSaleDetailToProduct(row);
	parent.getCallInfo_productArray([ obj ]);

}
function convertSaleDetailToProduct(row)
{
	var _product = {};
	_product.id = row.productId;
	_product.name = row.productName;
	_product.specifications = row.style;
	_product.unitId = row.unitId;
	_product.sourceQty = row.qty;
	_product.saleProduceQty = row.qty - row.produceedQty;
	_product.spareProduceQty = row.spareQty - row.produceSpareedQty;
	_product.sourceId = row.master.id;
	_product.sourceDetailId = row.id;
	_product.sourceBillType = row.master.billType;
	_product.sourceBillNo = row.master.billNo;
	_product.customerBillNo = row.master.customerBillNo;
	_product.customerId = row.master.customerId;
	_product.customerCode = row.master.customerCode;
	_product.customerName = row.master.customerName;
	_product.customerMaterialCode = row.customerMaterialCode;
	_product.deliveryTime = row.deliveryTime;
	_product.customerRequire = row.custRequire;
	_product.memo = row.memo;
	_product.salePrice = row.price;
	_product.imgUrl = row.imgUrl;
	// 判断下produceedQty（已生产数量）是否为0，为0的话money取销售订单最初填写的金额;
	if (row.produceedQty == 0)
	{
		_product.money = row.money;
	} else
	{
		_product.money = ((row.qty - row.produceedQty) * row.price).toFixed(2);
	}
	_product.code = row.productCode;
	return _product;
}
// 请求参数
function queryParams(params)
{
	params['dateMin'] = $("#dateMin").val();
	params['dateMax'] = $("#dateMax").val();
	params['billNo'] = $("#billNo").val();
	params['customerName'] = $("#customerName").val();
	params['productType'] = $("#productType").val();
	params['productName'] = $("#productName").val();
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
$(function()
{
	/* 更多 */
	$("#btn_more").click(function()
	{
		$("#more_div").toggle();
	});

	if (Helper.isEmpty($("#createTime1").val()))
	{
		$("#createTime1").val(new Date().add("m", -1).format('yyyy-MM-dd'));
	}
	if (Helper.isEmpty($("#createTime2").val()))
	{
		$("#createTime2").val(new Date().format('yyyy-MM-dd'));
	}

	// 订单详情table
	$("#bootTable").bootstrapTable({
		url : Helper.basePath + "/sale/order/ajaxSaleListForWork",
		method : "post",
		contentType : 'application/json', // 设置请求头信息
		dataType : "json",
		// pagination: true, // 是否显示分页（*）
		sidePagination : 'server',// 设置为服务器端分页
		pageList : Helper.bootPageList,
		queryParamsType : "",
		// pageSize: 20,
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
		exportTypes : [ 'csv', 'txt', 'excel', 'doc' ],

		cookie : true,// 是否启用COOKIE
		cookiesEnabled : [ 'bs.table.columns' ],// 默认会记住很多属性，这里控制只记住列选择属性
		cookieIdTable : "print_sale_transmit_deliver",// 必须制定唯一的表格cookieID

		uniqueId : "id",// 定义列表唯一键
		columns : [ {
			field : 'index',
			title : '序号',
			width : 40,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return;
				}
				return index + 1;
			}
		}, {
			field : 'state',
			checkbox : true,
			visible : true,
			width : 30
		}, {
			field : '',
			title : '交期天数',
			width : 100,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return;
				}
				value = row.master.createTime
				return DateDiff(new Date(row.deliveryTime).format("yyyy-MM-dd"), new Date(value).format("yyyy-MM-dd"));
			}
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
				return billNoTransToUrl(row.master.billNo);
			}
		}, {
			field : 'deliveryTime',
			title : '交货日期',
			width : 100,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return;
				}
				if (value)
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
			field : 'master_customerBillNo',
			title : '客户单号',
			width : 200,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return;
				}
				return row.master.customerBillNo
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
			field : 'style',
			title : '产品规格',
			width : 100
		}, {
			field : 'unitName',
			title : '单位',
			width : 100
		}, {
			field : 'productType',
			title : '产品类型',
			width : 100,
			visible : false,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return;
				}
				if (value == 'PACKE')
				{
					return '包装印刷';
				}
				if (value == 'BOOK')
				{
					return '书刊印刷';
				}
				if (value == 'ROTARY')
				{
					return '轮转印刷';
				}
			}
		}, {
			field : 'qty',
			title : '订单数量',
			width : 80
		}, {
			field : 'produceedQty',
			title : '已生产数量',
			width : 80
		}, {
			field : 'produceedQty2',
			title : '未生产数量',
			width : 80,
			formatter : function(value, row, index)
			{
				return row.qty - row.produceedQty;
			}
		}, {
			field : 'spareQty',
			title : '备品数量',
			width : 80
		}, {
			field : 'produceSpareedQty',
			title : '已备品数量',
			width : 80
		}, {
			field : 'produceSpareedQty2',
			title : '未备品数量',
			width : 80,
			formatter : function(value, row, index)
			{
				return row.spareQty - row.produceSpareedQty;
			}
		}, {
			field : 'memo',
			title : '备注',
			width : 80
		} ],
		onLoadSuccess : function(data)
		{
			$("#bootTable tbody").find("tr:last").find("td:first").text("合计");
			$("#bootTable tbody").find("tr:last").find("td:first").next().children("input[type='checkbox']").remove();
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
			if (!Helper.basic.hasPermission('trandsmit:deliver:export'))
			{
				$(".export.btn-group").remove();
			}
			$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
		}
	});

	$("#btn_transmit").click(function()
	{
		var _productArray = new Array();
		var packeCount = 0;
		var bookCount = 0;
		var productType = "PACKE";
		var _ids = "";
		$.each($("#bootTable").bootstrapTable('getAllSelections'), function(i, value)
		{
			if (value.id)
			{
				if (value.productType == 'PACKE')
				{
					packeCount++;
					productType = "PACKE";
				}
				if (value.productType == 'BOOK')
				{
					bookCount++;
					productType = "BOOK";
				}
				_productArray.push(value);
				_ids += "ids=" + value.id + "&";
			}
		})
		if (packeCount > 0 && bookCount > 0)
		{
			layer.msg('不能同时开书刊和包装的产品工单，请重新勾选', {
				icon : 5,
				area : '400px'
			})
			return;
		} else if (_productArray.length == 0)
		{
			Helper.message.warn("至少选择1项");
			return;
		}
		// var productArray = JSON.stringify(_productArray).toString().trim();
		var url = Helper.basePath + '/produce/work/create?1=1&' + _ids + "productType=" + productType;
		var title = "生产工单";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));

	});
	// 生产轮转工单
	$("#btn_transmit_rotary").click(function()
	{
		var _productArray = new Array();
		var packeCount = 0;
		var bookCount = 0;
		var productType = "ROTARY";
		var _ids = "";
		$.each($("#bootTable").bootstrapTable('getAllSelections'), function(i, value)
		{
			if (value.id)
			{
				// _productArray.push(convertSaleDetailToProduct(value));
				_ids += "&ids=" + value.id;
			}
		})
		if (_ids != "")
		{
			// var productArray = JSON.stringify(_productArray).toString().trim();
			var url = Helper.basePath + '/produce/work/create?1+1' + _ids + "&productType=" + productType;
			var title = "生产工单";
			admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
		} else
		{
			Helper.message.warn("至少选择1项");
		}
	});

	// 强制完工
	$("#btn_complete").on("click", function()
	{
		var rows = getSelectedRows();
		var ids = new Array();
		for (var i = 0; i < rows.length; i++)
		{
			if (rows[i].master)
			{
				var billNo = rows[i].master.billNo;
				ids.push(rows[i].id + "," + billNo.substring(0, 1));
			}
		}

		if (Helper.isNotEmpty(rows))
		{
			Helper.post(Helper.basePath + '/sale/transmit/complete', {
				"tableType" : "DETAIL",
				"ids[]" : ids
			}, function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
				} else
				{
					Helper.message.warn(data.message);
				}
				$("#bootTable").bootstrapTable("refreshOptions", {
					pageNumber : 1
				});
			});
		} else
		{
			Helper.message.warn("至少选择1项");
		}
	});
	// 取消强制完工
	$("#btn_complete_cancel").on("click", function()
	{
		var rows = getSelectedRows();
		var ids = new Array();
		for (var i = 0; i < rows.length; i++)
		{
			if (rows[i].master)
			{
				var billNo = rows[i].master.billNo;
				ids.push(rows[i].id + "," + billNo.substring(0, 1));
			}
		}

		if (Helper.isNotEmpty(rows))
		{
			Helper.post(Helper.basePath + '/sale/transmit/complete_cancel', {
				"tableType" : "DETAIL",
				"ids[]" : ids
			}, function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
				} else
				{
					Helper.message.warn(data.message);
				}
				$("#bootTable").bootstrapTable("refreshOptions", {
					pageNumber : 1
				});
			});
		} else
		{
			Helper.message.warn("至少选择1项");
		}
	});

});

function queryParams(params)
{
	params['dateMin'] = $("#createTime1").val();
	params['dateMax'] = $("#createTime2").val();
	params['billNo'] = $("#billNo").val();
	params['customerName'] = $("#customerName").val();
	params['productName'] = $("#productName").val();
	params['customerBillNo'] = $("#customerBillNo").val();
	params['customerMaterialCode'] = $("#customerMaterialCode").val();
	params['productStyle'] = $("#style").val();
	params['deliverDateMin'] = $("#dateMin").val();
	params['deliverDateMax'] = $("#dateMax").val();
	params['completeFlag'] = $("input[name='completeFlag']:checked").val().trim();
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

// 计算天数差的函数，通用
function DateDiff(sDate1, sDate2)
{ // sDate1和sDate2是2002-12-18格式
	s2 = (new Date(sDate2).getTime());
	s1 = (new Date(sDate1.replace(/-/g, "/"))).getTime();
	var days = (s1) - (s2);
	var time = Math.floor(days / (1000 * 60 * 60 * 24)) + 1;
	return time;
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
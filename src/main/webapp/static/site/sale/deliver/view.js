$(function()
{
	var saleDeliverId = $("#saleDeliverId").val();
	/* 判断当前用户是否有权限查看金额 */
	var hasPermission = Helper.basic.hasPermission('sale:deliver:money');
	var RECONCIL_ARR = new Array();

	// 订单详情table
	$("#detailList").bootstrapTable({
		url : Helper.basePath + "/sale/deliver/viewAjax/" + saleDeliverId,
		method : "post",
		contentType : 'application/json', // 设置请求头信息
		dataType : "json",
		pagination : false, // 是否显示分页（*）
		sidePagination : 'server',// 设置为服务器端分页
		pageList : [ 10, 20, 50 ],
		queryParamsType : "",
		pageSize : 10,
		pageNumber : 1,
		responseHandler : viewResponseHandler,
		// resizable : true, //是否启用列拖动
		showColumns : true, // 是否显示所有的列
		minimumCountColumns : 2, // 最少允许的列数
		striped : true, // 是否显示行间隔色
		cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
		sortable : false, // 是否启用排序
		clickToSelect : true, // 是否启用点击选中行
		height : undefined, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
		cardView : false, // 是否显示详细视图
		detailView : false, // 是否显示父子表

		showExport : false,// 是否显示导出按钮
		// exportDataType: 'all',
		exportTypes : [ 'csv', 'txt', 'excel', 'doc' ],

		cookie : true,// 是否启用COOKIE
		cookiesEnabled : [ 'bs.table.columns' ],// 默认会记住很多属性，这里控制只记住列选择属性
		cookieIdTable : "print_sale_deliver_master",// 必须制定唯一的表格cookieID

		uniqueId : "id",// 定义列表唯一键
		columns : [ {
			field : 'index',
			title : '序号',
			width : 40,
			formatter : function(value, row, index)
			{
				return (index + 1);
			}
		}, {
			field : 'productCode',
			title : '产品编号',
			width : 120,
			visible : false
		}, {
			field : 'productName',
			title : '成品名称',
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
			field : 'qty',
			title : '数量',
			width : 80
		}, {
			field : 'returnQty',
			title : '已退货数量',
			width : 100,
			visible : false
		}, {
			field : 'reconcilQty',
			title : '已对账数量',
			width : 100,
			visible : false
		}, {
			field : 'spareQty',
			title : '备品数量',
			width : 80
		}, {
			field : 'sourceQty',
			title : '源单数量',
			width : 80
		}, {
			field : 'sourceBillNo',
			title : '源单单号',
			width : 100,
			formatter : function(value, row, index)
			{
				return billNoTransToUrl(value);
			}
		}, {
			field : 'customerMaterialCode',
			title : '客户料号',
			width : 100
		}, {
			field : 'customerBillNo',
			title : '客户单号',
			width : 100
		}, {
			field : 'price',
			title : '单价',
			visible : hasPermission,
			width : 60,
			formatter : function(value, row, index)
			{
				return value + '<input class="tab_input" type="hidden" name="detailList.price" value="' + (value || "") + '"/>'
			}
		}, {
			field : 'money',
			title : '金额',
			visible : hasPermission,
			width : 100,
			formatter : function(value, row, index)
			{
				return value + '<input class="tab_input" type="hidden" name="detailList.money" value="' + (value || "") + '"/>';
			}
		}, {
			field : 'tax',
			title : '税额',
			visible : hasPermission,
			width : 80,
			formatter : function(value, row, index)
			{
				return value + '<input type="hidden" name="detailList.tax" readonly value="' + (value || "") + '"/>';
			}
		}, {
			field : 'taxRateName',
			title : '税收',
			width : 100
		}, {
			field : 'percent',
			title : '税率值%',
			width : 80,
			formatter : function(value, row, index)
			{
				return value + '<input type="hidden" name="detailList.percent" readonly value="' + (value || "") + '%"/>';
			},
			visible : false
		}, {
			field : 'warehouseName',
			title : '仓库',
			width : 80
		}, {
			field : 'noTaxPrice',
			title : '不含税单价',
			width : 80,
			formatter : function(value, row, index)
			{
				return value + '<input class="tab_input" type="hidden" name="detailList.noTaxPrice" readonly value="' + (value || "") + '"/>'
			},
			visible : false
		}, {
			field : 'noTaxMoney',
			title : '不含税金额',
			width : 100,
			formatter : function(value, row, index)
			{
				return value + '<input class="tab_input" type="hidden" name="detailList.noTaxMoney" readonly value="' + (value || "") + '"/>';
			},
			visible : false
		}, {
			field : 'memo',
			title : '备注',
			'class' : 'memoView',
			width : 120
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
			var result = data.rows;
			var ReturnList = new Array();
			// 判断是否已全部退货RECONCIL_ARR
			$.each(result, function(index, item)
			{
				if (item.returnQty >= item.qty)
				{
					ReturnList.push(item);
				}

				if (item.reconcilQty < item.qty)
				{
					RECONCIL_ARR.push(item.id);
				}
			});
			if (ReturnList.length >= result.length)
			{
				$("#transmitToReturn").hide();
			}
			if (RECONCIL_ARR.length == 0)
			{
				$("#generateTransmit").hide();
			}
		},
		onLoadError : function()
		{
			// alert("数据加载异常");
		},
		onColumnSwitch : function(field, checked)
		{	
			// 在使用筛选增加或减少列时重置表格列拖动效果
			bootstrapTable_ColDrag($("#detailList"));
		}
	});
	/* 表格工具栏 */
	$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
	// 控制筛选菜单金额选择
	if (!hasPermission)
	{
		$("div[title='列'] ul[role=menu]").find("input[value=13]").parent().parent().remove();
		$("div[title='列'] ul[role=menu]").find("input[value=14]").parent().parent().remove();
		$("div[title='列'] ul[role=menu]").find("input[value=15]").parent().parent().remove();
		$("div[title='列'] ul[role=menu]").find("input[value=19]").parent().parent().remove();
		$("div[title='列'] ul[role=menu]").find("input[value=20]").parent().parent().remove();
	}
	;
	/* 返回显示列表 */
	$("#btn_back").on("click", function()
	{
		var url = Helper.basePath + '/sale/deliver/list';
		var title = "销售送货列表";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	});
	/* 编辑 */
	$("#btn_edit").on("click", function()
	{
		var url = Helper.basePath + '/sale/deliver/edit/' + $("#id").val();
		var title = "销售送货";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	});
	// 审核
	$("#btn_audit").on("click", function()
	{
		var order_id = $("#id").val();
		Helper.post(Helper.basePath + '/sale/deliver/check/' + order_id, function(data)
		{
			if (data.success)
			{
				if (data.obj == null)
				{
					Helper.message.suc(data.message);
					location.href = Helper.basePath + "/sale/deliver/view/" + order_id;
				} else
				{
					var str = '审核失败,以下产品库存数量不足\n';
					for (var i = 0; i < data.obj.length; i++)
					{
						str = str + '名称：' + data.obj[i].product.name + '   目前库存数量' + data.obj[i].qty + '\n';
					}
					Helper.message.confirm(str, function(index)
					{
						Helper.message.confirm("确认操作会引起负库存，是否允许负库存?", function(index)
						{
							forceCheck(order_id);
						});
					});
				}
			} else
			{
				Helper.message.warn(data.message);
			}
		});
	});
	// 反审核
	$("#btn_audit_cancel").on("click", function()
	{
		var order_id = $("#id").val();
		Helper.post(Helper.basePath + '/sale/deliver/checkBack/' + order_id, function(data)
		{
			if (data.success)
			{
				Helper.message.suc(data.message);
				location.href = Helper.basePath + "/sale/deliver/view/" + order_id;
			} else
			{
				if (data.obj)
				{
					var msg = "";
					var index = 1;
					$.each(data.obj, function(i, n)
					{
						msg += (index++) + ".反审核失败,已被下游单据引用：<br/>"
						$.each(n, function(k, j)
						{
							if (i == 'IR')
							{
								msg += '&emsp;&emsp;销售退货单<a href="javascript:;" onclick="return_view(' + j.id + ')">' + j.billNo + '</a><br/>';
							}
							if (i == 'SK')
							{
								msg += '&emsp;&emsp;销售对账单<a href="javascript:;" onclick="reconcil_view(' + j.id + ')">' + j.billNo + '</a><br/>';
							}

						});
					});
					Helper.message.view(msg);
				} else
				{
					Helper.message.warn(data.message);
				}
			}
		});
	});

	// 强制完工
	$("#btn_complete").on("click", function()
	{
		var order_id = $("#id").val();
		Helper.post(Helper.basePath + '/sale/deliver/complete', {
			"tableType" : "MASTER",
			"ids" : [ order_id ]
		}, function(data)
		{
			if (data.success)
			{
				Helper.message.suc(data.message);
				location.href = Helper.basePath + "/sale/deliver/view/" + order_id;
			} else
			{
				Helper.message.warn(data.message);
			}
		});
	});
	// 取消强制完工
	$("#btn_complete_cancel").on("click", function()
	{
		var order_id = $("#id").val();
		Helper.post(Helper.basePath + '/sale/deliver/complete_cancel', {
			"tableType" : "MASTER",
			"ids" : [ order_id ]
		}, function(data)
		{
			if (data.success)
			{
				Helper.message.suc(data.message);
				location.href = Helper.basePath + "/sale/deliver/view/" + order_id;
			} else
			{
				Helper.message.warn(data.message);
			}
		});
	});

	/* 删除 */
	$("#btn_del").on("click", function()
	{
		Helper.message.confirm('确认要删除吗？', function(index)
		{
			Helper.post(Helper.basePath + '/sale/deliver/del/' + $("#id").val(), function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
					closeTabAndJump("销售送货列表");
				} else
				{
					Helper.message.warn(data.message);
				}
			});
		});
	});
	$("#btn_print").loadTemplate('SALE_IV', '/sale/deliver/viewAjax/' + saleDeliverId);
	// 打印
	/*
	 * $("#btn_print").on("click",function(){ window.open(Helper.basePath +
	 * '/sale/deliver/print/' + $("#id").val()); });
	 */
	$("#transmitToReturn").on("click", function()
	{
		transmitToReturn();
	});
	$("#transmitToReconcil").on("click", function()
	{
		transmitToReconcil(RECONCIL_ARR);
	});

	$("#detailList").on('load-success.bs.table', function()
	{
		// bootstrap_table加载完后触发列拖动效果
		bootstrapTable_ColDrag($("#detailList"));
	});
});

// 生成销售退货
function transmitToReturn()
{
	var customerId = $("#customerId").val();
	var billNo = $("#billNo").val();
	var url = Helper.basePath + "/sale/return/create?customerId=" + customerId + "&billNo=" + billNo;
	var title = "销售退货";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}
// 生成销售对账
function transmitToReconcil(RECONCIL_ARR)
{
	var customerId = $("#customerId").val();
	var ids = "";
	$.each(RECONCIL_ARR, function(i, val)
	{
		ids = ids + "&deliverIds=" + val;
	});
	var url = Helper.basePath + '/sale/reconcil/create?customerId=' + customerId + ids;
	var title = "销售对账";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}
// 查看
function return_view(id)
{
	if (!Helper.isNotEmpty(id))
	{
		return;
	}
	var url = Helper.basePath + '/sale/return/view/' + id;
	var title = "销售退货";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}

// 查看
function reconcil_view(id)
{
	if (!Helper.isNotEmpty(id))
	{
		return;
	}
	var url = Helper.basePath + '/sale/reconcil/view/' + id;
	var title = "销售对账";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}

/**
 * 强制审核
 */
function forceCheck(id)
{
	$.ajax({
		cache : true,
		type : "POST",
		dataType : "json",
		url : Helper.basePath + '/sale/deliver/forceCheck/' + id,
		async : false,
		error : function(request)
		{
			layer.alert("Connection error");
		},
		success : function(data)
		{
			if (data.success)
			{
				location.reload();
			} else
			{
				layer.alert("审核失败")
			}
		}
	});
}
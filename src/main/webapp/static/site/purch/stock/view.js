$(function()
{
	// 当前详情id列表数据（）
	var currentDataIdList = [];
	// 当前采购退货id列表
	var currentReturnIdList = [];
	// 当前采购对账id列表
	var currentReconcilIdList = [];
	/* 判断当前用户是否有权限查看金额 */
	var hasPermission = Helper.basic.hasPermission('purch:stock:money');

	var url = "";
	/* 区分从采购入库单列表点击查看还是从点击单号查看 */
	if (!/^PN/g.test($("#stockId").val())) // 从采购入库列表点击查看，则是带id请求服务
	{
		url = Helper.basePath + "/purch/stock/viewAjax/" + $("#stockId").val();
	} else if (/^PN/g.test($("#stockId").val())) // 从其他单据点击源单单号查看的，则是带订单号请求服务
	{
		var billNo = $("#stockId").val();
		url = Helper.basePath + "/purch/stock/toViewAjax/" + billNo;
	}

	$("#detailList").bootstrapTable({
		url : url,// 不需要查询
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
		cookieIdTable : "print_purch_stock_master_view",// 必须制定唯一的表格cookieID
		columns : [ {
			field : 'id',
			title : '序号',
			width : 40,
			formatter : function(value, row, index)
			{
				return index + 1;
			}
		}, {
			field : 'code',
			title : '材料编号',
			width : 120

		}, {
			field : 'materialName',
			title : '材料名称',
			width : 100

		}, {
			field : 'specifications',
			title : '材料规格',
			width : 100
		}, {
			field : 'weight',
			title : '克重',
			width : 60
		}, {
			field : 'purchUnitName',
			title : '单位',
			width : 60
		}, {
			field : 'qty',
			title : '入库数量',
			width : 80,
			formatter : function(value, row, index)
			{
				return value + '<input name="detailList.qty" type="hidden" value="' + value + '"/><input name="detailList.reconcilQty" type="hidden" value="' + row.reconcilQty + '"/><input name="detailList.refundQty" type="hidden" value="' + row.refundQty + '"/>';
			}
		}, {
			field : 'freeQty',
			title : '赠送数量',
			width : 80,
			formatter : function(value, row, index)
			{
				return value || 0;
			}
		}, {
			field : 'reconcilQty',
			title : '已对账数量',
			width : 100,
			visible : false,
			formatter : function(value, row, index)
			{
				return value;
			}
		}, {
			field : 'refundQty',
			title : '已退货数量',
			width : 100,
			visible : false,
			formatter : function(value, row, index)
			{
				return value;
			}
		}, {
			field : 'valuationUnitName',
			title : '计价单位',
			width : 60
		}, {
			field : 'valuationQty',
			title : '计价数量',
			width : 80
		}, {
			field : 'valuationPrice',
			title : '计价单价',
			visible : hasPermission,
			width : 60
		}, {
			field : 'money',
			title : '金额',
			visible : hasPermission,
			width : 100
		}, {
			field : 'tax',
			title : '税额',
			visible : hasPermission,
			width : 60
		}, {
			field : 'taxRateId',
			title : '税收',
			width : 100,
			formatter : function(value, row, index)
			{
				return Helper.basic.info('TAXRATE', value).name;
			}
		}, {
			field : 'percent',
			title : '税率值',
			visible : false,
			width : 80
		}, {
			field : 'warehouseId',
			title : '仓库',
			width : 80,
			formatter : function(value, row, index)
			{
				return Helper.basic.info('WAREHOUSE', value).name;
			}
		}, {
			field : 'price',
			title : '库存单价',
			visible : false,
			width : 80
		}, {
			field : 'sourceBillType',
			title : '源单类型',
			width : 80,
			formatter : function(value, row, index)
			{
				return Helper.basic.getEnumText('com.huayin.printmanager.persist.enumerate.BillType', value, 'text');
			}
		}, {
			field : 'sourceBillNo',
			title : '源单单号',
			width : 100,
			formatter : function(value, row, index)
			{
				return billNoTransToUrl(value);
			}
		}, {
			field : 'workBillNo',
			title : '生产单号',
			visible : false,
			width : 80,
			formatter : function(value, row, index)
			{
				return billNoTransToUrl(value);
			}
		}, {
			field : 'productNames',
			title : '成品名称',
			width : 100
		}, {
			field : 'sourceQty',
			title : '源单数量',
			visible : false,
			width : 80
		}, {
			field : 'noTaxMoney',
			title : '不含税金额',
			visible : false,
			width : 100
		}, {
			field : 'noTaxPrice',
			title : '不含税单价',
			visible : false,
			width : 100
		},

		{
			field : 'memo',
			title : '备注',
			'class' : 'memoView',
			width : 200
		} ],
		onLoadSuccess : function(data)
		{
			/* 打印 */
			var id = data.rows[0].masterId;
			$("#btn_print").loadTemplate('PURCH_PN', '/purch/stock/printAjax/' + id);
			for (var i = 0; i < data.rows.length; i++)
			{
				var obj = data.rows[i];
				currentDataIdList.push(obj.id);
				// 采购退货 >【入库数量 大于 已退货数量】 并且 【入库数量 大于 已对账数量】
				if (obj.qty > obj.refundQty && obj.qty > obj.reconcilQty)
				{
					currentReturnIdList.push(obj.id);
				}
				// 采购对账 >【入库数量 大于 已对账数量】
				if (obj.qty > obj.reconcilQty)
				{
					currentReconcilIdList.push(obj.id);
				}
			}
			// 隐藏【生成采购退货】
			if (currentReturnIdList.length <= 0)
			{
				$("#btn_transmit_return").hide();
			}
			// 隐藏【生成采购对账】
			if (currentReconcilIdList.length <= 0)
			{
				$("#btn_transmit_reconcil").hide();
			}
			// 隐藏所有【生成】
			if (currentReturnIdList.length <= 0 && currentReconcilIdList.length <= 0)
			{
				$("#generateTransmit").hide();
			}
			// alert("数据加载完成");
		},
		onLoadError : function()
		{
			// alert("数据加载异常");
		},
		onClickRow : function(row, $element)
		{
			$element.addClass("tr_active").siblings().removeClass("tr_active");
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
		$("div[title='列'] ul[role=menu]").find("input[value=10]").parent().parent().remove();
		$("div[title='列'] ul[role=menu]").find("input[value=11]").parent().parent().remove();
		$("div[title='列'] ul[role=menu]").find("input[value=12]").parent().parent().remove();
		$("div[title='列'] ul[role=menu]").find("input[value=16]").parent().parent().remove();
		$("div[title='列'] ul[role=menu]").find("input[value=21]").parent().parent().remove();
		$("div[title='列'] ul[role=menu]").find("input[value=22]").parent().parent().remove();
	}

	$("#btn_audit").click(function()
	{
		$.ajax({
			cache : true,
			type : "POST",
			url : Helper.basePath + '/purch/stock/check/' + $("#id").val(),
			async : false,
			dataType : "json",
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
					Helper.message.warn(data.message);
				}
			}
		});
	});
	$("#btn_audit_cancel").click(function()
	{
		var $id = $("#id").val();
		$.ajax({
			cache : true,
			type : "POST",
			url : Helper.basePath + '/purch/stock/checkBack/' + $id,
			async : false,
			dataType : "json",
			error : function(request)
			{
				layer.alert("Connection error");
			},
			success : function(data)
			{
				if (data.success)
				{
					if (data.obj == null)
					{
						location.reload();
					} else
					{
						var str = '反审失败,以下材料库存数量不足\n';
						for (var i = 0; i < data.obj.length; i++)
						{
							str = str + '名称:' + data.obj[i].material.name + (data.obj[i].specifications || '') + '   目前库存数量' + data.obj[i].qty + '\n    ';
						}
						Helper.message.confirm(str, function(index)
						{
							Helper.message.confirm("确认操作会引起负库存，是否允许负库存?", function(index)
							{
								forceCheckBack($id);
							});
						});

					}
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
								if (i == 'PR')
								{
									msg += '&emsp;&emsp;采购退货单<a href="javascript:;" onclick="refund_view(' + j.id + ')">' + j.billNo + '</a><br/>';
								}
								if (i == 'PK')
								{
									msg += '&emsp;&emsp;采购对账单<a href="javascript:;" onclick="reconcil_view(' + j.id + ')">' + j.billNo + '</a><br/>';
								}

							});
						});
						Helper.message.view(msg);
					} else
					{
						Helper.message.warn(data.message);
					}
				}
			}
		});

	});

	$("#btn_del").click(function()
	{
		Helper.message.confirm('确认要删除吗？', function(index)
		{
			$.ajax({
				cache : true,
				type : "POST",
				url : Helper.basePath + '/purch/stock/delete/' + $("#id").val(),
				async : false,
				dataType : "json",
				error : function(request)
				{
					// console.log(request)
					layer.alert("Connection error");
				},
				success : function(data)
				{
					if (data.success)
					{
						closeTabAndJump("采购入库列表");
					} else
					{
						layer.alert("失败");
					}
				}
			});
		});
	});

	$("#btn_edit").click(function()
	{
		window.location.href = Helper.basePath + '/purch/stock/edit/' + $("#id").val();
	});

	$("#btn_back").click(function()
	{
		var url = Helper.basePath + '/purch/stock/list';
		var title = "采购入库列表";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	});

	// 强制完工
	$("#btn_complete").on("click", function()
	{
		var order_id = $("#id").val();
		Helper.post(Helper.basePath + '/purch/stock/complete', {
			"tableType" : "MASTER",
			"ids" : [ order_id ]
		}, function(data)
		{
			if (data.success)
			{
				Helper.message.suc(data.message);
				location.href = Helper.basePath + "/purch/stock/view/" + order_id;
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
		Helper.post(Helper.basePath + '/purch/stock/complete_cancel', {
			"tableType" : "MASTER",
			"ids" : [ order_id ]
		}, function(data)
		{
			if (data.success)
			{
				Helper.message.suc(data.message);
				location.href = Helper.basePath + "/purch/stock/view/" + order_id;
			} else
			{
				Helper.message.warn(data.message);
			}
		});
	});
	// 生成采购退货
	$("#btn_transmit_return").click(function()
	{
		if (currentReturnIdList.length > 0)
		{
			var _paramStr = "";
			$(currentReturnIdList).each(function(index, item)
			{
				if (index == 0)
				{
					_paramStr = "stockIds=" + item;
				} else
				{
					_paramStr = _paramStr + "&stockIds=" + item;
				}
			});
			_paramStr += "&supplierId=" + $("#supplierId").val();
			var url = Helper.basePath + '/purch/refund/create?' + _paramStr;
			var title = "生成采购退货";
			admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
		}
	});
	// 生成采购对账
	$("#btn_transmit_reconcil").click(function()
	{
		if (currentReconcilIdList.length > 0)
		{
			var _paramStr = "";
			$(currentReconcilIdList).each(function(index, item)
			{
				if (index == 0)
				{
					_paramStr = "ids=PN_" + item;
				} else
				{
					_paramStr = _paramStr + "&ids=PN_" + item;
				}
			});
			var url = Helper.basePath + '/purch/reconcil/toReconcil?' + _paramStr;
			var title = "生成采购对账";
			admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
		}
	});

	$("#detailList").on('load-success.bs.table', function()
	{
		// bootstrap_table加载完后触发列拖动效果
		bootstrapTable_ColDrag($("#detailList"));
	});
});

function refund_view(id)
{
	if (!Helper.isNotEmpty(id))
	{
		return;
	}
	var url = Helper.basePath + '/purch/refund/view/' + id;
	var title = "采购退货";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}

function reconcil_view(id)
{
	if (!Helper.isNotEmpty(id))
	{
		return;
	}
	var url = Helper.basePath + '/purch/reconcil/view/' + id;
	var title = "采购对账";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}

/**
 * 强制反审核
 */
function forceCheckBack(id)
{
	$.ajax({
		cache : true,
		type : "POST",
		dataType : "json",
		url : Helper.basePath + '/purch/stock/forceCheckBack/' + id,
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
				if (data.obj)
				{
					var msg = "";
					var index = 1;
					$.each(data.obj, function(i, n)
					{
						msg += (index++) + ".反审核失败,已被下游单据引用：<br/>"
						$.each(n, function(k, j)
						{
							if (i == 'PR')
							{
								msg += '&emsp;&emsp;采购退货单<a href="javascript:;" onclick="refund_view(' + j.id + ')">' + j.billNo + '</a><br/>';
							}
							if (i == 'PK')
							{
								msg += '&emsp;&emsp;采购对账单<a href="javascript:;" onclick="reconcil_view(' + j.id + ')">' + j.billNo + '</a><br/>';
							}

						});
					});
					Helper.message.view(msg);
				} else
				{
					Helper.message.warn(data.message);
				}
			}
		}
	});
}
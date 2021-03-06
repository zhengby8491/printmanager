$(function()
{
	// 当前详情id列表数据（）
	var currentDataIdList = [];
	// 当前采购入库id列表
	var currentInStockIdList = [];
	/* 判断当前用户是否有权限查看金额 */
	var hasPermission = Helper.basic.hasPermission('purch:order:money');
	var url = "";
	/* 区分从采购订单列表点击查看还是从点击单号查看 */
	if (!/^PO/g.test($("#orderId").val())) // 从销售订单点击查看，则是带id请求服务
	{
		url = Helper.basePath + "/purch/order/viewAjax/"+$("#orderId").val();
	} else if (/^PO/g.test($("#orderId").val())) // 从其他单据点击源单单号查看的，则是带订单号请求服务
	{
		var billNo = $("#orderId").val();
		url = Helper.basePath + "/purch/order/toViewAjax/" + billNo;
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
		cookieIdTable : "print_purch_order_master_view",// 必须制定唯一的表格cookieID
		columns : [ {
			field : 'state',
			title : '单选',
			radio : true,
			visible : true,
			width : 60
		}, {
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
			width : 100

		}, {
			field : 'materialName',
			title : '材料名称',
			width : 120

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
			title : '采购数量',
			width : 80,
			formatter : function(value, row, index)
			{
				return value + '<input name="detailList.qty" type="hidden" value="' + value + '"/>';
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
			width : 80
		}, {
			field : 'money',
			title : '金额',
			visible : hasPermission,
			width : 80
		}, {
			field : 'tax',
			title : '税额',
			visible : hasPermission,
			width : 60
		}, {
			field : 'taxRateId',
			title : '税收',
			width : 80,
			formatter : function(value, row, index)
			{
				return Helper.basic.info('TAXRATE', value).name;
			}
		}, {
			field : 'percent',
			title : '税率值',
			visible : false,
			width : 100
		}, {
			field : 'price',
			title : '库存单价',
			visible : false,
			width : 80
		}, {
			field : 'deliveryTime',
			title : '交货日期',
			width : 80,
			formatter : function(value, row, index)
			{
				return new Date(value).format("yyyy-MM-dd");
			}
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
			title : '生产单号',
			width : 100,
			formatter : function(value, row, index)
			{
				return billNoTransToUrl(value);
			}
		}, {
			field : 'sourceQty',
			title : '源单数量',
			visible : false,
			width : 80
		}, {
			field : 'storageQty',
			title : '已入库数量',
			visible : false,
			width : 100,
			formatter : function(value, row, index)
			{
				return value + '<input name="detailList.storageQty" type="hidden" value="' + value + '"/>';
			}
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
		}, {
			field : 'productNames',
			title : '成品名称',
			width : 100
		}, {
			field : 'memo',
			title : '备注',
			'class' : 'memoView',
			width : 150
		} ],
		onLoadSuccess : function(data)
		{
			/* 打印 */
			var id = data.rows[0].masterId;
			$("#btn_print").loadTemplate('PURCH_PO', '/purch/order/printAjax/' + id);
			for (var i = 0; i < data.rows.length; i++)
			{
				var obj = data.rows[i];
				currentDataIdList.push(obj.id);
				// 采购数量 大于 库存数量
				if (obj.qty > obj.storageQty)
				{
					currentInStockIdList.push(obj.id);
				}
			}
			// 隐藏【生成采购入库】
			if (currentInStockIdList.length <= 0)
			{
				$("#btn_transmit_in_stock").hide();
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
		$("div[title='列'] ul[role=menu]").find("input[value=9]").parent().parent().remove();
		$("div[title='列'] ul[role=menu]").find("input[value=10]").parent().parent().remove();
		$("div[title='列'] ul[role=menu]").find("input[value=11]").parent().parent().remove();
		$("div[title='列'] ul[role=menu]").find("input[value=14]").parent().parent().remove();
		$("div[title='列'] ul[role=menu]").find("input[value=20]").parent().parent().remove();
		$("div[title='列'] ul[role=menu]").find("input[value=21]").parent().parent().remove();
		$("div[title='列'] ul[role=menu]").find("input[value=22]").parent().parent().remove();
	}
	;
	$("#btn_audit").click(function()
	{
		$.ajax({
			cache : true,
			type : "POST",
			url : Helper.basePath + '/purch/order/check/' + $("#id").val(),
			async : false,
			dataType : "json",
			error : function(request)
			{
				Helper.message.warn("Connection error");
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
		$.ajax({
			cache : true,
			type : "POST",
			url : Helper.basePath + '/purch/order/checkBack/' + $("#id").val(),
			async : false,
			dataType : "json",
			error : function(request)
			{
				Helper.message.warn("服务器繁忙");
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
								if (i == 'PN')
								{
									msg += '&emsp;&emsp;采购入库单<a href="javascript:;" onclick="stock_view(' + j.id + ')">' + j.billNo + '</a><br/>';
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
				url : Helper.basePath + '/purch/order/delete/' + $("#id").val(),
				async : false,
				dataType : "json",
				error : function(request)
				{
					Helper.message.warn("Connection error");
				},
				success : function(data)
				{
					if (data.success)
					{
						closeTabAndJump("采购订单列表");
					} else
					{
						Helper.message.warn("失败");
					}
				}
			});
		});

	});

	$("#btn_edit").click(function()
	{
		window.location.href = Helper.basePath + '/purch/order/edit/' + $("#id").val();
	});

	$("#btn_back").click(function()
	{
		var url = Helper.basePath + '/purch/order/list';
		var title = "采购订单列表";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	});

	// 强制完工
	$("#btn_complete").on("click", function()
	{
		var order_id = $("#id").val();
		Helper.post(Helper.basePath + '/purch/order/complete', {
			"tableType" : "MASTER",
			"ids" : [ order_id ]
		}, function(data)
		{
			if (data.success)
			{
				Helper.message.suc(data.message);
				location.href = Helper.basePath + "/purch/order/view/" + order_id;
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

		Helper.post(Helper.basePath + '/purch/order/complete_cancel', {
			"tableType" : "MASTER",
			"ids" : [ order_id ]
		}, function(data)
		{
			if (data.success)
			{
				Helper.message.suc(data.message);
				location.href = Helper.basePath + "/purch/order/view/" + order_id;
			} else
			{
				Helper.message.warn(data.message);
			}
		});
	});
	// 生成采购入库
	$("#btn_transmit_in_stock").click(function()
	{
		if (currentInStockIdList.length > 0)
		{
			var _paramStr = "";
			$(currentInStockIdList).each(function(index, item)
			{
				if (index == 0)
				{
					_paramStr = "ids=" + item;
				} else
				{
					_paramStr = _paramStr + "&ids=" + item;
				}
			});
			var url = Helper.basePath + '/purch/stock/toStock?' + _paramStr;
			var title = "生成采购入库";
			admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
		}
	});
	// 单价变更
	$("#priceChange").on("click", function()
	{
		var row = $("#detailList").bootstrapTable('getSelections');
		if (Helper.isEmpty(row))
		{
			Helper.message.warn("请先选择一个材料");
			return;
		}
		Helper.popup.show('更新单价交期', Helper.basePath + '/purch/order/editPrice/' + row[0].id, '390', '300');
	});
	// 复制
	$("#btn_copy").on("click", function()
	{
		var order_id = $("#id").val();
		location.href = Helper.basePath + "/purch/order/copy/" + order_id;
	});

	$("#detailList").on('load-success.bs.table', function()
	{
		// bootstrap_table加载完后触发列拖动效果
		bootstrapTable_ColDrag($("#detailList"));
	});
});

function stock_view(id)
{
	if (!Helper.isNotEmpty(id))
	{
		return;
	}
	var url = Helper.basePath + '/purch/stock/view/' + id;
	var title = "采购入库";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}
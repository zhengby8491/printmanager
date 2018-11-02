/* 判断当前用户是否有权限查看金额 */
var hasPermission = Helper.basic.hasPermission('sale:order:money');
var _currentSelectProduct = null;
var PRODUCED_ARR = new Array();
var DELIVER_ARR = new Array();
var url = "";
/* 区分是从销售订单点击查看还是从其他单据跳转查看 */
$(function()
{
	if (!/^SO/g.test($("#orderId").val())) // 从销售订单点击查看，则是带id请求服务
	{
		url = Helper.basePath + "/sale/order/viewAjax/" + $("#orderId").val();
	} else if (/^SO/g.test($("#orderId").val()))// 从其他单据点击源单单号查看的，则是带订单号请求服务
	{
		var billNo = $("#orderId").val();
		url = Helper.basePath + "/sale/order/toViewAjax/" + billNo;
	}
	// 订单详情table
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
		cookieIdTable : "print_sale_order_view",// 必须制定唯一的表格cookieID

		uniqueId : "id",// 定义列表唯一键
		columns : [ {
			field : 'state',
			title : '单选',
			radio : true,
			visible : true,
			width : 60
		}, {
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
			field : 'customerMaterialCode',
			title : '客户料号',
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
			field : 'spareQty',
			title : '备品数量',
			width : 80
		}, {
			field : 'deliverSpareedQty',
			title : '已送备品数量',
			width : 80,
			visible : false
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
			field : 'deliveryTime',
			title : '交货日期',
			width : 80,
			formatter : function(value, row, index)
			{
				if (value)
				{
					return new Date(value).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'operator',
			title : '工序材料',
			width : 70,
			formatter : function(value, row, index)
			{
				if (!Helper.isNotEmpty(row.id))
				{
					return;
				}

				var operator = '<span class="">';
				operator += '<a title="工序材料详情" href="javascript:;" name="btn_procedure" onclick="procedureMaterial_view(' + row.id + ",'" + row.productType + "'" + ')" style="padding: 0 8px; color: green"><i class="fa fa-info-circle"></i></a>';
				operator += '</span>';

				return operator;
			}
		}, {
			field : 'deliverQty',
			title : '已送货数量',
			width : 80,
			visible : false
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
			field : 'offerNo',
			title : '报价单号',
			width : 90,
			formatter : function(value, row, index)
			{
				return idTransToUrl(row.offerId, value);
			}
		}, {
			field : 'memo',
			'class' : 'memoView',
			title : '备注',
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
			// 打印模板加载
			var id = data.rows[0].masterId;
			$("#btn_print").loadTemplate('SALE_SO', '/sale/order/viewAjax/' + id);
			var rows = data.rows;
			var billNo = $("#billNo").val();

			$(rows).each(function(index, item)
			{
				// 判断已生产数 是否达到订单数量
				if (item.produceedQty < item.qty)
				{
					PRODUCED_ARR.push(item.id + "-" + item.productType);
				}

				// 判断是否已经完成送货 送货数量>=订单数量
				if (item.deliverQty < item.qty)
				{
					DELIVER_ARR.push(item.id);
				}
			});

			if (PRODUCED_ARR.length == 0)
			{
				$("#transmitToProduce").hide();
			}
			// 若已全部送货，生成按钮隐藏
			if (DELIVER_ARR.length == 0)
			{
				$(".isFlowCheckYES").hide();
			}
			// 若为库存订单，生成工单按钮隐藏
			if ($("#orderTypeText").val() == "发库存订单")
			{
				$("#transmitToProduce").hide();
			}
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
		$("div[title='列'] ul[role=menu]").find("input[value=18]").parent().parent().remove();
		$("div[title='列'] ul[role=menu]").find("input[value=19]").parent().parent().remove();
	}
	;
	// $(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
	/* 返回显示列表 */
	$("#btn_back").on("click", function()
	{
		var url = Helper.basePath + '/sale/order/list';
		var title = "销售订单列表";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	});
	/* 编辑 */
	$("#btn_edit").on("click", function()
	{
		var url = Helper.basePath + '/sale/order/edit/' + $("#id").val();
		var title = "销售订单";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	});
	// 审核
	$("#btn_audit").on("click", function()
	{
		var order_id = $("#id").val();
		Helper.post(Helper.basePath + '/sale/order/audit/' + order_id, function(data)
		{
			if (data.success)
			{
				Helper.message.suc(data.message);
				location.href = Helper.basePath + "/sale/order/view/" + order_id;
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
		Helper.post(Helper.basePath + '/sale/order/audit_cancel/' + order_id, function(data)
		{
			if (data.success)
			{
				Helper.message.suc(data.message);
				location.href = Helper.basePath + "/sale/order/view/" + order_id;
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
							if (i == 'MO')
							{
								msg += '&emsp;&emsp;生产工单<a href="javascript:;" onclick="work_view(' + j.id + ')">' + j.billNo + '</a><br/>';
							}
							if (i == 'IV')
							{
								msg += '&emsp;&emsp;销售送货单<a href="javascript:;" onclick="deliver_view(' + j.id + ')">' + j.billNo + '</a><br/>';
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
		Helper.post(Helper.basePath + '/sale/order/complete', {
			"tableType" : "MASTER",
			"ids" : [ order_id ]
		}, function(data)
		{
			if (data.success)
			{
				Helper.message.suc(data.message);
				location.href = Helper.basePath + "/sale/order/view/" + order_id;
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
		Helper.post(Helper.basePath + '/sale/order/complete_cancel', {
			"tableType" : "MASTER",
			"ids" : [ order_id ]
		}, function(data)
		{
			if (data.success)
			{
				Helper.message.suc(data.message);
				location.href = Helper.basePath + "/sale/order/view/" + order_id;
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
			Helper.post(Helper.basePath + '/sale/order/del/' + $("#id").val(), function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
					closeTabAndJump("销售订单列表");
					/* location.href=Helper.basePath+"/sale/order/list"; */
				} else
				{
					Helper.message.warn(data.message);
				}
			});
		});
	});
	// 单价变更
	$("#priceChange").on("click", function()
	{

		var row = $("#detailList").bootstrapTable('getSelections');
		if (Helper.isEmpty(row))
		{
			Helper.message.warn("请先选择一个产品");
			return;
		}

		Helper.popup.show('更新单价交期', Helper.basePath + '/sale/order/editPrice/' + row[0].id, '390', '300');
	});

	// 查看历史单价
	$("#historyPrice").on("click", function()
	{
		var row = $("#detailList").bootstrapTable('getSelections');
		if (Helper.isEmpty(row))
		{
			Helper.message.warn("请先选择一个产品");
			return;
		}

		Helper.popup.show('查看历史单价', Helper.basePath + '/sale/order/historyPrice/' + row[0].productId, '660', '500');
	});

	/* 打印 */
	/*
	 * $("#btn_print").on("click",function(){ window.open(Helper.basePath +
	 * '/sale/order/print/' + $("#id").val()); });
	 */
	$("#btn_copy").on("click", function()
	{
		var order_id = $("#id").val();
		location.href = Helper.basePath + "/sale/order/copy/" + order_id;
	});

	// 生成生产工单
	$("#transmitToProduce").on("click", function()
	{
		transmitToProduce();
	});
	// 生产销售送货单
	$("#transmitToDeliver").on("click", function()
	{
		transmitToDeliver();
	});
	
	$("#detailList").on('load-success.bs.table', function()
	{
		// bootstrap_table加载完后触发列拖动效果
		bootstrapTable_ColDrag($("#detailList"));
	});
});
/* 查看工序材料详情 */
function procedureMaterial_view(id, productType)
{
	Helper.popup.show('销售工序材料详情', Helper.basePath + '/sale/order/quick_procedure?id=' + id + '&productType=' + productType, '1200', '550');
}
/* 查看 */
function work_view(id)
{
	var url = Helper.basePath + '/produce/work/view/' + id;
	var title = "生产工单";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"))
}
// 查看
function deliver_view(id)
{
	if (!Helper.isNotEmpty(id))
	{
		return;
	}
	var url = Helper.basePath + '/sale/deliver/view/' + id;
	var title = "销售送货";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}
// 生成生产工单
function transmitToProduce()
{
	var billNo = $("#billNo").val();
	var _productArray = new Array();
	var _ids = "";
	var packeCount = 0;
	var bookCount = 0;

	for (var i = 0; i < PRODUCED_ARR.length; i++)
	{
		var id = PRODUCED_ARR[i].split("-")[0];
		var productType = PRODUCED_ARR[i].split("-")[1];

		if (productType == "BOOK")
		{
			bookCount++;
		}

		if (productType == "PACKE")
		{
			packeCount++;
		}
		_ids += "&ids=" + id;
	}
	if (packeCount > 0 && bookCount > 0)
	{
		Helper.message.warn("不能同时开书刊和包装产品的工单");
		return;
	}

	var url = Helper.basePath + '/produce/work/create?1=1' + _ids + "&productType=" + productType;
	var title = "生产工单";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}

// 生产销售送货单
function transmitToDeliver()
{
	var billNo = $("#billNo").val();
	var paramStr = "";
	$.each(DELIVER_ARR, function(i, val)
	{
		paramStr += "&idsBillNos=" + billNo + "_" + val;
	});

	var url = Helper.basePath + '/sale/deliver/create?1=1' + paramStr;
	var title = "销售送货";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}
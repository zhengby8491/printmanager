$(function()
{
	/* 判断当前用户是否有权限查看金额 */
	var hasPermission = Helper.basic.hasPermission('sale:reconcil:money');
	var PARAM = "";
	var reconcilList = new Array();
	// 订单详情table
	$("#detailList").bootstrapTable({
		url : Helper.basePath + "/sale/reconcil/viewAjax/" + $("#reconcilId").val(),
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
		cookieIdTable : "print_sale_reconcil_master",// 必须制定唯一的表格cookieID

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
			field : 'sourceBillTypeText',
			title : '源单类型',
			width : 100
		}, {
			field : 'productCode',
			title : '产品编号',
			width : 160,
			visible : false
		}, {
			field : 'productName',
			title : '成品名称',
			width : 160
		}, {
			field : 'style',
			title : '产品规格',
			width : 80
		}, {
			field : 'unitName',
			title : '单位',
			width : 60
		}, {
			field : 'customerMaterialCode',
			title : '客户料号',
			width : 120
		}, {
			field : 'customerBillNo',
			title : '客户单号',
			width : 120
		}, {
			field : 'saleOrderBillNo',
			title : '销售单号',
			width : 120,
			formatter : function(value, row, index)
			{
				return billNoTransToUrl(value);
			}
		}, {
			field : 'qty',
			title : '数量',
			width : 80
		}, {
			field : 'sourceQty',
			title : '源单数量',
			width : 80
		}, {
			field : 'sourceBillNo',
			title : '源单单号',
			width : 120,
			visible : false,
			formatter : function(value, row, index)
			{
				return idTransToUrl(row.sourceId, value);
			}
		}, {
			field : 'spareQty',
			title : '备品数量',
			width : 80,
			visible : false
		}, {
			field : 'price',
			title : '单价',
			visible : hasPermission,
			width : 80,
			formatter : function(value, row, index)
			{
				return value + '<input class="tab_input" type="hidden" name="detailList.price" value="' + (value || "") + '"/>'
			}
		}, {
			field : 'money',
			title : '金额',
			visible : hasPermission,
			width : 60,
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
			width : 100,
			formatter : function(value, row, index)
			{
				return value + '<input type="hidden" name="detailList.percent" readonly value="' + (value || "") + '%"/>';
			},
			visible : false
		}, {
			field : 'noTaxPrice',
			title : '不含税单价',
			width : 100,
			formatter : function(value, row, index)
			{
				return value + '<input class="tab_input" type="hidden" name="detailList.noTaxPrice" readonly value="' + (value || "") + '"/>'
			},
			visible : false
		}, {
			field : 'noTaxMoney',
			title : '不含税金额',
			width : 160,
			formatter : function(value, row, index)
			{
				return value + '<input class="tab_input" type="hidden" name="detailList.noTaxMoney" readonly value="' + (value || "") + '"/>';
			},
			visible : false
		}, {
			field : 'deliveryTime',
			title : '送/退货日期',
			width : 100,
			formatter : function(value, row, index)
			{
				return new Date(value).format("yyyy-MM-dd");
			}
		}, {
			field : 'memo',
			title : '备注',
			'class' : 'memoView',
			width : 160
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
			console.log(result);
			$.each(result, function(index, item)
			{
				var money = 0;
				var receiveMoney = 0;
				if (item.sourceBillType == "SALE_IR")
				{
					money = 0 - Number(item.money);
					receiveMoney = 0 - Number(item.receiveMoney);
				} else
				{
					money = item.money;
					receiveMoney = item.receiveMoney;
				}
				if (money > receiveMoney)
				{
					reconcilList.push(item.id);
				}
			});
			if (reconcilList.length <= 0)
			{
				$(".isFlowCheckYES").hide();
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
		$("div[title='列'] ul[role=menu]").find("input[value=18]").parent().parent().remove();
		$("div[title='列'] ul[role=menu]").find("input[value=19]").parent().parent().remove();
	}
	;
	/* 返回显示列表 */
	$("#btn_back").on("click", function()
	{
		var url = Helper.basePath + '/sale/reconcil/list';
		var title = "销售对账列表";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	});
	/* 编辑 */
	$("#btn_edit").on("click", function()
	{
		var url = Helper.basePath + '/sale/reconcil/edit/' + $("#id").val();
		var title = "销售对账";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	});
	// 审核
	$("#btn_audit").on("click", function()
	{
		var order_id = $("#id").val();
		Helper.post(Helper.basePath + '/sale/reconcil/audit/' + order_id, function(data)
		{
			if (data.success)
			{
				Helper.message.suc(data.message);
				location.href = Helper.basePath + "/sale/reconcil/view/" + order_id;
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
		Helper.post(Helper.basePath + '/sale/reconcil/audit_cancel/' + order_id, function(data)
		{
			if (data.success)
			{
				Helper.message.suc(data.message);
				location.href = Helper.basePath + "/sale/reconcil/view/" + order_id;
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
							if (i == 'RC')
							{
								msg += '&emsp;&emsp;收款单<a href="javascript:;" onclick="receive_view(' + j.id + ')">' + j.billNo + '</a><br/>';
							}
							if (i == 'WRC')
							{
								msg += '&emsp;&emsp;收款核销单<a href="javascript:;" onclick="writeoffReceive(' + j.id + ')">' + j.billNo + '</a><br/>';
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
		Helper.post(Helper.basePath + '/sale/reconcil/complete', {
			"tableType" : "MASTER",
			"ids" : [ order_id ]
		}, function(data)
		{
			if (data.success)
			{
				Helper.message.suc(data.message);
				location.href = Helper.basePath + "/sale/reconcil/view/" + order_id;
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
		Helper.post(Helper.basePath + '/sale/reconcil/complete_cancel', {
			"tableType" : "MASTER",
			"ids" : [ order_id ]
		}, function(data)
		{
			if (data.success)
			{
				Helper.message.suc(data.message);
				location.href = Helper.basePath + "/sale/reconcil/view/" + order_id;
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
			Helper.post(Helper.basePath + '/sale/reconcil/del/' + $("#id").val(), function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
					closeTabAndJump("销售对账列表");
					/* location.href=Helper.basePath+"/sale/reconcil/list"; */
				} else
				{
					Helper.message.warn(data.message);
				}
			});
		});
	});
	// 打印模板加载
	$("#btn_print").loadTemplate('SALE_SK', '/sale/reconcil/printAjax/${id}');
	// 生成收款单
	$("#transmitToReceive").on("click", function()
	{
		transmitToReceive(reconcilList);
	});
	
	$("#detailList").on('load-success.bs.table', function()
	{
		// bootstrap_table加载完后触发列拖动效果
		bootstrapTable_ColDrag($("#detailList"));
	});
});
/* 生成收款单 */
function transmitToReceive(reconcilList)
{
	_paramStr = "";
	$.each(reconcilList, function(i, n)
	{
		_paramStr = _paramStr + "&ids=" + n;
	});
	var url = Helper.basePath + '/finance/receive/create?1=1&billType=SALE_SK' + _paramStr;
	var title = "收款单";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"))
}
/* 查看 */
function receive_view(id)
{
	var url = Helper.basePath + '/finance/receive/view/' + id;
	var title = "收款单";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"))
}

/* 查看 */
function writeoffReceive(id)
{
	var url = Helper.basePath + '/finance/writeoffReceive/view/' + id;
	var title = "收款核销单";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"))
}
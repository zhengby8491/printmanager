$(function()
{
	$("#btn_transmit").click(function()
	{
		var rows = getSelectedRows();
		if (Helper.isNotEmpty(rows))
		{
			var customerName = "";
			var customerNameArr = new Array();
			$(rows).each(function()
			{
				customerName = this.customerName;
				if (!customerNameArr.contains(customerName))
				{
					customerNameArr.push(customerName);
				}
			});
			if (customerNameArr.length == 1)
			{// 判断是否统一供应商
				var ids = [];
				$(rows).each(function(index, item)
				{
					ids.push(item.id);
				});
				Helper.post(Helper.basePath + '/sale/order/createFromOfferCheck', {
					"from" : "OFFER",
					"ids[]" : ids
				}, function(data)
				{
					if (data.success)
					{
						var str = '';
						if (data.obj.ERROR)
						{
							var arr = data.obj.ERROR;
							str += "生成销售订单失败,已被下游单据引用：<br/>";
							for (var i = 0; i < arr.length; i++)
							{
								var _tips = arr[i].split(",");
								str += '&emsp;&emsp;销售订单<a href="javascript:;" onclick="saleView(' + _tips[1] + ')">' + _tips[0] + '</a><br/>';
							}
							Helper.message.view(str);
						} else if (data.obj.SUCCESS && data.obj.SUCCESS.length > 0)
						{
							var arr = data.obj.SUCCESS;
							for (var i = 0; i < arr.length; i++)
							{
								str += '<div>' + arr[i] + ';</div>';
							}
							str += '<div>如果基础资料不存在，将不能生成销售订单;</div>';
							str += '<div>请问是否自动新增 ?</div>';
							str += '<div>您也可以点击<span style="color: red; font-size: 16px;">"取消"</span>先去基础资料模块里手动增加所有基础资料后再进行转单生成销售订单</div>';
							Helper.message.confirm(str, function(index)
							{
								createFromOffer(ids);
								layer.close(index);
							});
						} else
						{
							createFromOffer(ids);
						}

					} else
					{
						Helper.message.warn(data.message);
					}
				});
			} else
			{
				Helper.message.warn("请选择同一客户");
			}
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
		// 全选要除去最后的合计后
		for (var i = 0; i < rows.length; i++)
		{
			ids.push(rows[i].id);
		}
		if (Helper.isNotEmpty(rows))
		{
			Helper.post(Helper.basePath + '/offer/complete', {
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
		// 全选要除去最后的合计后
		for (var i = 0; i < rows.length; i++)
		{
			ids.push(rows[i].id);
		}
		if (Helper.isNotEmpty(rows))
		{
			Helper.post(Helper.basePath + '/offer/completeCancel', {
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

	// 订单详情table
	$("#bootTable").bootstrapTable({
		url : Helper.basePath + "/offer/ajaxList",
		method : "post",
		contentType : 'application/json', // 设置请求头信息
		dataType : "json",
		pagination : false, // 是否显示分页（*）
		sidePagination : 'server',// 设置为服务器端分页
		pageList : Helper.bootPageList,
		queryParamsType : "",
		pageSize : 20,
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
			width : 60
		}, {
			field : '',
			title : '超期天数',
			width : 100,
			formatter : function(value, row, index)
			{
				if (row.id == null)
				{
					return;
				}
				return Helper.Date.diff(new Date(row.deliveryDate).format("yyyy-MM-dd"), new Date().format("yyyy-MM-dd"));
			}
		}, {
			field : 'deliveryDate',
			title : '交货日期',
			width : 60,
			formatter : function(value, row, index)
			{
				if (value)
				{
					return new Date(value).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'createDateTime',
			title : '报价日期',
			width : 60,
			formatter : function(value, row, index)
			{
				if (value)
				{
					return new Date(value).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'offerNo',
			title : '报价单号',
			width : 80,
			formatter : function(value, row, index)
			{
				return billNoTransToUrl(value);
			}
		}, {
			field : 'customerName',
			title : '客户名称',
			width : 120
		}, {
			field : 'productName',
			title : '产品名称',
			width : 80
		}, {
			field : 'spec',
			title : '产品规格',
			width : 80
		}, {
			field : 'amount',
			title : '报价数量',
			width : 80
		}, {
			field : 'unitPrice',
			title : '单价',
			width : 60
		}, {
			field : 'costMoney',
			title : '总费用',
			width : 60
		}, {
			field : 'offerTypeText',
			title : '报价类型',
			width : 80
		} ],
		onLoadSuccess : function(data)
		{
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
			if (!Helper.basic.hasPermission('sale:transmit:offer:export'))
			{
				$(".export.btn-group").remove();
			}
			$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
		}
	});
});

// 获取选择项
function getSelectedArray()
{
	$("#transmitForm").empty();
	var array = $('tbody input[type=checkbox]:checked').map(function()
	{
		return this.value
	}).get();
	$('tbody input[type=checkbox]:checked').each(function()
	{
		$("#transmitForm").append("<input type='hidden' name='ids' value='" + this.value + "'/>");
	});
	return array;
}
// 获取返回产品信息
function getCallInfo_product(obj)
{
	$("#productName").val(obj.name);
}
// 获取返回客户信息
function getCallInfo_customer(obj)
{
	$("#customerName").val(obj.name);
}

function queryParams(params)
{
	params['dateMin'] = $("#dateMin").val();
	params['dateMax'] = $("#dateMax").val();
	params['customerName'] = $.trim($("#customerName").val());
	params['name'] = $.trim($("#name").val());
	params['billNo'] = $.trim($("#offerNo").val());
	params['saleBillNo'] = "YES";
	params['completeFlag'] = $("input[name='completeFlag']:checked").val().trim();
	return params;
}

/**
 * 报价转销售订单
 * @param params
 * @returns {___anonymous5070_5075}
 * @since 1.0, 2018年2月9日 上午11:15:13, think
 */
function createFromOffer(ids)
{
	var param = "1=1";
	$(ids).each(function(i, val)
	{
		param += "&ids[]=" + val;
	});
	var url = Helper.basePath + '/sale/order/createFromOffer?' + param;
	var title = "销售订单";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}
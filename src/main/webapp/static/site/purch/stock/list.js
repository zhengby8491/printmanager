$(function()
{
	/* 判断当前用户是否有权限查看金额 */
	var hasPermission = Helper.basic.hasPermission('purch:stock:money');
	/* 查询审核状态 */
	$("input[name='auditFlag']").change(function()
	{
		$("#detailList").bootstrapTable("refreshOptions", {
			pageNumber : 1
		});
	});
	// 查询，刷新table
	$("#btn_search").click(function()
	{
		$("#detailList").bootstrapTable("refreshOptions", {
			pageNumber : 1
		});
	});

	$("#detailList").bootstrapTable({
		url : Helper.basePath + "/purch/stock/ajaxList",
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
		// exportDataType: 'all',
		exportTypes : [ 'csv', 'txt', 'excel', 'doc' ],

		cookie : true,// 是否启用COOKIE
		cookiesEnabled : [ 'bs.table.columns' ],// 默认会记住很多属性，这里控制只记住列选择属性
		cookieIdTable : "print_purch_stock_master",// 必须制定唯一的表格cookieID

		uniqueId : "id",// 定义列表唯一键

		columns : [ {
			field : 'index',
			title : '序号',
			width : 40,
			formatter : function(value, row, index)
			{
				return index + 1;
			}
		}, {
			field : 'isCheck',
			title : '单据状态',
			width : 70,
			formatter : function(value, row, index)
			{
				if (value == 'YES')
				{
					return '已审核';
				}
				if (value == 'NO')
				{
					return '未审核';
				}
			},
			cellStyle : function(value, row, index, field)
			{
				if (value == '未审核')
				{
					return {
						css : {
							"color" : "#f00"
						}
					};
				} else
				{
					return {
						css : {
							"color" : "#080"
						}
					};
				}
			}
		}, {
			field : 'billTypeText',
			title : '单据类型',
			width : 100
		},

		{
			field : 'storageTime',
			title : '入库日期',
			width : 80,
			formatter : function(value, row, index)
			{
				if (value == null)
				{
					return;
				}
				return new Date(value).format("yyyy-MM-dd");
			}

		}, {
			field : 'billNo',
			title : '入库单号',
			width : 140,
			formatter : function(value, row, index)
			{
				return billNoTransToUrl(value);
			}
		}, {
			field : 'supplierName',
			title : '供应商名称',
			width : 220
		}, {
			field : 'employeeName',
			title : '采购员',
			visible : false,
			width : 80
		}, {
			field : 'linkName',
			title : '联系人',
			width : 80
		}, {
			field : 'mobile',
			title : '联系电话',
			width : 100
		}, {
			field : 'totalMoney',
			title : '金额',
			visible : hasPermission,
			width : 100
		}, {
			field : 'totalTax',
			title : '税额',
			visible : hasPermission,
			width : 100

		}, {
			field : 'noTaxTotalMoney',
			title : '不含税金额',
			visible : false,
			width : 100
		}, {
			field : 'createName',
			title : '制单人',
			width : 80
		}, {
			field : 'createTime',
			title : '制单日期',
			width : 80,
			formatter : function(value, row, index)
			{
				if (value == null)
				{
					return;
				}
				return new Date(value).format("yyyy-MM-dd");
			}
		}, {
			field : 'checkUserName',
			title : '审核人',
			visible : true,
			width : 80
		}, {
			field : 'checkTime',
			title : '审核日期',
			width : 80,
			formatter : function(value, row, index)
			{
				if (value != null)
				{
					return new Date(value).format("yyyy-MM-dd");
				} else
				{
					return "";
				}

			}
		}, {
			field : 'operator',
			title : '操作',
			width : 120,
			formatter : function(value, row, index)
			{
				if (!Helper.isNotEmpty(row.id))
				{
					return;
				}
				var operator = "";
				if (Helper.basic.hasPermission('purch:stock:list'))
				{
					operator += '<span class="table_operator"><a title="查看" href="javascript:;" onclick="stock_view(' + row.id + ')" style="padding: 0 8px; color: green"><i class="fa fa-search"></i></a></span>';
				}
				if (Helper.isNotEmpty(row.isCheck) && row.isCheck == 'NO')
				{
					operator = "";
					if (Helper.basic.hasPermission('purch:stock:list'))
					{
						operator += '<span class="table_operator"><a title="查看" href="javascript:;" onclick="stock_view(' + row.id + ')" style="padding: 0 8px; color: green"><i class="fa fa-search"></i></a>';
					}
					if (Helper.basic.hasPermission('purch:stock:edit'))
					{
						operator += '<a title="编辑" href="javascript:;" onclick="stock_edit(' + row.id + ')" style="padding: 0 8px; color: green"><i class="fa fa-pencil"></i></a>';
					}
					if (Helper.basic.hasPermission('purch:stock:del'))
					{
						operator += '<a title="删除" href="javascript:;" onclick="stock_del(' + row.id + ')" style="padding: 0 8px; color: red"><i class="fa fa-trash-o"></i></a></span>';
					}
				}
				return operator;
			}
		}

		],
		onDblClickRow : function(row)
		{
			// 双击选中事件
			stock_view(row.id);
		},
		onLoadSuccess : function()
		{
			// alert("数据加载完成");
			$("#detailList tbody").find("tr:last").find("td:first").text("合计");
			var _tds = $("#detailList tbody").find("tr:last").find("td");
			for (var index = 0; index < _tds.length; index++)
			{
				// console.log(_tds[index].innerHTML);
				if (_tds.eq(index).text() == "-")
				{
					_tds.eq(index).text('');
				}
			}

			if ($(".glyphicon-th").next().html() == '')
			{
				$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
				// 控制筛选菜单金额选择
				if (!hasPermission)
				{
					$("div[title='列'] ul[role=menu]").find("input[value=9]").parent().parent().remove();
					$("div[title='列'] ul[role=menu]").find("input[value=10]").parent().parent().remove();
					$("div[title='列'] ul[role=menu]").find("input[value=11]").parent().parent().remove();
				}
				if (!Helper.basic.hasPermission('purch:stock:export'))
				{
					$(".export.btn-group").remove();
				}
				$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
			}
		},
		onColumnSwitch : function(field, checked)
		{
			$("#detailList tbody").find("tr:last").find("td:first").text("合计");
			var _tds = $("#detailList tbody").find("tr:last").find("td");
			for (var index = 0; index < _tds.length; index++)
			{
				// console.log(_tds[index].innerHTML);
				if (_tds.eq(index).text() == "-")
				{
					_tds.eq(index).text('');
				}
			}

			// 在使用筛选增加或减少列时重置表格列拖动效果
			bootstrapTable_ColDrag($("#detailList"));
		},
		onLoadError : function()
		{
			// alert("数据加载异常");
		},
		onClickRow : function(row, $element)
		{
			$element.addClass("tr_active").siblings().removeClass("tr_active");
		}

	});

	$("#supplier_quick_select").click(function()
	{
		Helper.popup.show('选择供应商', Helper.basePath + '/quick/supplier_select?multiple=false', '900', '490');
	});
	$("input[type='radio']").change(function()
	{
		$("#searchForm").submit();
	})

	$("#detailList").on('load-success.bs.table', function()
	{
		// bootstrap_table加载完后触发列拖动效果
		bootstrapTable_ColDrag($("#detailList"));
	});
})

function responseHandler(res)
{
	return {
		rows : res.result,
		total : res.count
	};
}
function queryParams(params)
{

	params['dateMin'] = $("#dateMin").val();
	params['dateMax'] = $("#dateMax").val();
	params['billNo'] = $("#billNo").val();
	params['supplierName'] = $("#supplierName").val();
	params['auditFlag'] = $("input[name='auditFlag']:checked").val() == "-1" ? null : $("input[name='auditFlag']:checked").val();
	return params;
}

function add()
{
	var url = Helper.basePath + '/purch/stock/create';
	var title = "采购入库";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}
function stock_edit(id)
{
	if (!Helper.isNotEmpty(id))
	{
		return;
	}
	var url = Helper.basePath + '/purch/stock/edit/' + id;
	var title = "采购入库";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}
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
function stock_del(id)
{
	Helper.message.confirm('确认要删除吗？', function(index)
	{
		$.ajax({
			cache : true,
			type : "POST",
			url : Helper.basePath + '/purch/stock/delete/' + id,
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
					$("#detailList").bootstrapTable("removeByUniqueId", id);
					$("#detailList").bootstrapTable("refresh");
					Helper.message.suc('已删除!');

					// window.location.href=Helper.basePath + '/purch/stock/list';
				} else
				{
					layer.alert("失败");
				}
			}
		});
	});
}
// 获取返回信息
function getCallInfo_supplier(obj)
{
	$("#supplierName").val(obj.name);
}
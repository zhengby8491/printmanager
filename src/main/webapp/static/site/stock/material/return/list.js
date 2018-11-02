$(function()
{
	/* 查询审核状态 */
	$("input[name='auditFlag']").change(function()
	{
		$("#bootTable").bootstrapTable("refresh");
	});
	// 查询，刷新table
	$("#btn_search").click(function()
	{
		$("#bootTable").bootstrapTable("refresh");
	});

	/* 更多 */
	$("#btn_more").click(function()
	{
		$("#more_div").toggle();
	});

	// 订单详情table
	$("#bootTable").bootstrapTable({
		url : Helper.basePath + "/stockmaterial/return/ajaxList",
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
		cookieIdTable : "print_stockmaterial_return_master",// 必须制定唯一的表格cookieID

		uniqueId : "id",// 定义列表唯一键
		columns : [ {
			field : 'index',
			title : '序号',
			width : 50,
			formatter : function(value, row, index)
			{
				// console.log($(this).pageSixze)
				return index + 1;
			}
		}, {
			field : 'warehouseId',
			title : '仓库',
			width : 50,
			formatter : function(value, row, index)
			{
				return Helper.basic.info('WAREHOUSE', value).name;
			}
		}, {
			field : 'isCheck',
			title : '单据状态',
			width : 70,
			formatter : function(value, row, index)
			{
				return value == 'NO' ? '未审核' : '已审核';
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
			width : 80
		}, {
			field : 'returnTime',
			title : '退料日期',
			width : 80,
			formatter : function(value, row, index)
			{
				if (value)
				{
					return new Date(value).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'billNo',
			title : '退料单号',
			width : 100,
			formatter : function(value, row, index)
			{
				return idTransToUrl(row.id, value);
			}
		}, {
			field : 'returnEmployeeName',
			title : '退料人',
			width : 80
		}, {
			field : 'receiveEmployeeName',
			title : '收料人',
			width : 80
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
				if (value)
				{
					return new Date(value).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'checkUserName',
			title : '审核人',
			width : 80
		}, {
			field : 'checkTime',
			title : '审核日期',
			width : 80,
			formatter : function(value, row, index)
			{
				if (value)
				{
					return new Date(value).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'memo',
			title : '备注',
			'class' : 'memoView',
			width : 200
		}, {
			field : 'operator',
			title : '操作',
			width : 120,
			formatter : function(value, row, index)
			{

				var operator = "";
				if (Helper.basic.hasPermission('stock:materialReturn:list'))
				{

					operator += '<span class="table_operator"><a title="查看" href="javascript:;" onclick="return_view(' + row.id + ')" style="padding: 0 8px; color: green"><i class="fa fa-search"></i></a></span>';

				}
				if (Helper.isNotEmpty(row.isCheck) && row.isCheck == 'NO')
				{
					operator = "";
					if (Helper.basic.hasPermission('stock:materialReturn:list'))
					{
						operator += '<span class="table_operator"><a title="查看" href="javascript:;" onclick="return_view(' + row.id + ')" style="padding: 0 8px; color: green"><i class="fa fa-search"></i></a>';

					}
					if (Helper.basic.hasPermission('stock:materialReturn:edit'))
					{
						operator += '<a title="编辑" href="javascript:;" onclick="return_edit(' + row.id + ')" style="padding: 0 8px; color: green"><i class="fa fa-pencil"></i></a>';
					}
					if (Helper.basic.hasPermission('stock:materialReturn:del'))
					{
						operator += '<a title="删除" href="javascript:;" onclick="return_del(this,' + row.id + ')" style="padding: 0 8px; color: red"><i class="fa fa-trash-o"></i></a></span>';
					}
				}
				return operator;
			}
		} ],
		onDblClickRow : function(row)
		{
			return_view(row.id);
		},
		onClickRow : function(row, $element)
		{
			$element.addClass("tr_active").siblings().removeClass("tr_active");
		},
		onColumnSwitch : function(field, checked)
		{
			// 在使用筛选增加或减少列时重置表格列拖动效果
			bootstrapTable_ColDrag($("#bootTable"));
		}
	});
	/* 表格工具栏 */
	$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
	if (!Helper.basic.hasPermission('stock:material_return:export'))
	{
		$(".export.btn-group").remove();
	}
	$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
});
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
	params['sendEmployeeId'] = $("#returnEmployeeId").val();
	params['receiveEmployeeId'] = $("#receiveEmployeeId").val();
	params['warehouseId'] = $("#warehouseId").val();
	params['auditFlag'] = $("input[name='auditFlag']:checked").val() == "-1" ? null : $("input[name='auditFlag']:checked").val();
	// console.log(params)
	return params;
}
/* 编辑 */
function return_edit(id)
{
	var url = Helper.basePath + '/stockmaterial/return/edit/' + id;
	var title = "生产退料";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}

// 查看
function return_view(id)
{
	var url = Helper.basePath + '/stockmaterial/return/view/' + id;
	var title = "生产退料";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}

/* 删除 */
function return_del(obj, id)
{
	Helper.message.confirm('确认要删除吗？', function(index)
	{
		Helper.post(Helper.basePath + '/stockmaterial/return/delete/' + id, function(data)
		{
			if (data.success)
			{
				$("#bootTable").bootstrapTable("removeByUniqueId", id);
				$("#bootTable").bootstrapTable("refresh");
				Helper.message.suc('已删除!');
			} else
			{
				Helper.message.warn('删除失败：' + data.message);
			}
		});
	});
}

/* 新增 */
function toCreate()
{
	var url = Helper.basePath + '/stockmaterial/return/create';
	var title = "生产退料";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}
$(function()
{
	/* 查询审核状态 */
	$("input[name='auditFlag']").change(function()
	{
		$("#bootTable").bootstrapTable("refreshOptions", {
			pageNumber : 1
		});
	});
	// 查询，刷新table
	$("#btn_search").click(function()
	{
		$("#bootTable").bootstrapTable("refreshOptions", {
			pageNumber : 1
		});
	});
	// 订单详情table
	$("#bootTable").bootstrapTable({
		url : Helper.basePath + "/produce/work/ajaxList",
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
		cookieIdTable : "print_produce_work_master",// 必须制定唯一的表格cookieID

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
			field : 'isEmergency',
			title : '急单',
			width : 40,
			formatter : function(value, row, index)
			{
				return value == 'YES' ? '是' : '否';
			},
			cellStyle : function(value, row, index, field)
			{
				if (row.isEmergency == 'YES')
				{
					return {
						css : {
							"color" : "#FF740D",
							"font-weight" : "bold"
						}
					};
				} else
				{
					return {};
				}
			}
		}, {
			field : 'isOutSource',
			title : '整单发外',
			width : 70,
			formatter : function(value, row, index)
			{
				return value == 'YES' ? '是' : '否';
			},
			cellStyle : function(value, row, index, field)
			{
				if (row.isOutSource == 'YES')
				{
					return {
						css : {
							"color" : "#FF740D",
							"font-weight" : "bold"
						}
					};
				} else
				{
					return {};
				}
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
				if (row.isCheck == 'NO')
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
			field : 'productTypeText',
			title : '产品类型',
			width : 80
		}, {
			field : 'turningCount',
			title : '翻单次数',
			width : 70
		}, {
			field : 'supplementCount',
			title : '补单次数',
			width : 70
		}, {
			field : 'billNo',
			title : '生产单号',
			width : 140
		}, {
			field : 'checkUserName',
			title : '审核人',
			width : 80
		}, {
			field : 'checkTime',
			title : '审核时间',
			width : 100,
			formatter : function(value, row, index)
			{
				if (value)
				{
					return new Date(value).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'createName',
			title : '制单人',
			width : 80
		}, {
			field : 'createTime',
			title : '制单时间',
			width : 100,
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
				if (Helper.basic.hasPermission('produce:work:list'))
				{
					operator += '<span class="table_operator"><a title="查看" href="javascript:;" onclick="work_view(' + row.id + ')" style="padding: 0 8px; color: green"><i class="fa fa-search"></i></a></span>';
				}
				if (Helper.isNotEmpty(row.isCheck) && row.isCheck == 'NO')
				{
					operator = "";
					if (Helper.basic.hasPermission('produce:work:list'))
					{
						operator += '<span class="table_operator"><a title="查看" href="javascript:;" onclick="work_view(' + row.id + ')" style="padding: 0 8px; color: green"><i class="fa fa-search"></i></a>';
					}
					if (Helper.basic.hasPermission('produce:work:edit'))
					{
						operator += '<a title="编辑" href="javascript:;" onclick="work_edit(' + row.id + ')" style="padding: 0 8px; color: green"><i class="fa fa-pencil"></i></a>';
					}
					if (Helper.basic.hasPermission('produce:work:del'))
					{
						operator += '<a title="删除" href="javascript:;" onclick="work_del(this,' + row.id + ')" style="padding: 0 8px; color: red"><i class="fa fa-trash-o"></i></a></span>';
					}
				}
				return operator;

			}
		} ],
		onDblClickRow : function(row)
		{
			work_view(row.id);
		},
		onClickRow : function(row, $element)
		{
			$element.addClass("tr_active").siblings().removeClass("tr_active");
		}
	});
	$("#bootTable").on('load-success.bs.table column-switch.bs.table page-change.bs.table search.bs.table', function()
	{
		/* 表格工具栏 */
		if ($(".glyphicon-th").next().html() == '')
		{
			$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
			if (!Helper.basic.hasPermission('produce:work:export'))
			{
				$(".export.btn-group").remove();
			}
			$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
		}
	});
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
	params['auditFlag'] = $("input[name='auditFlag']:checked").val() == "-1" ? null : $("input[name='auditFlag']:checked").val();
	return params;
}
/* 新增 */
function work_create(id)
{
	var url = Helper.basePath + '/produce/work/create';
	var title = "生产工单";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}
/* 编辑 */
function work_edit(id)
{
	var url = Helper.basePath + '/produce/work/edit/' + id;
	var title = "生产工单";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}

/* 查看 */
function work_view(id)
{
	var url = Helper.basePath + '/produce/work/view/' + id;
	var title = "生产工单";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"))
}
/* 删除 */
function work_del(obj, id)
{
	Helper.message.confirm('确认要删除吗？', function(index)
	{
		Helper.post(Helper.basePath + '/produce/work/del/' + id, function(data)
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
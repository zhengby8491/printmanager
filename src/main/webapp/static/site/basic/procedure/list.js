$(function()
{
	// 查询，刷新table
	$("#btn_search").click(function()
	{
		$("#bootTable").bootstrapTable("refreshOptions", {
			pageNumber : 1
		});
	});

	// 订单详情table
	$("#bootTable").bootstrapTable({
		url : Helper.basePath + "/basic/procedure/listAjax",
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
		cookieIdTable : "print_basic_procedure",// 必须制定唯一的表格cookieID

		uniqueId : "id",// 定义列表唯一键
		columns : [ {
			field : 'state',
			checkbox : true,
			visible : true,
			width : 40
		}, {
			field : 'index',
			title : '序号',
			width : 60,
			formatter : function(value, row, index)
			{
				return index + 1;
			}
		}, {
			field : 'procedureTypeText',
			title : '工序类型',
			width : 80
		}, {
			field : 'procedureClassName',
			title : '工序分类',
			width : 80
		}, {
			field : 'code',
			title : '工序编号',
			width : 80
		}, {
			field : 'name',
			title : '工序名称',
			width : 80
		}, {
			field : 'produceTypeText',
			title : '生产方式',
			width : 80
		}, {
			field : 'yieldReportingTypeText',
			title : '上报方式',
			width : 80
		}, {
			field : 'scheduleDataSourceText',
			title : '排程数据源',
			width : 80
		}, {
			field : 'isProduceText',
			title : '是否生产',
			width : 100
		}, {
			field : 'isScheduleText',
			title : '是否排程',
			width : 100
		}, {
			field : 'isQuotationText',
			title : '是否报价',
			width : 100
		},
		/*
		 * { field : 'formulaName', title : '报价公式', width : 200 },
		 */
		{
			field : 'createName',
			title : '创建人',
			visible : false,
			width : 70
		}, {
			field : 'updateName',
			title : '修改人',
			visible : false,
			width : 70
		}, {
			field : 'createTime',
			title : '创建时间',
			visible : false,
			width : 75,
			formatter : function(value, row, index)
			{
				if (Helper.isNotEmpty(value))
				{
					return new Date(value).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'updateTime',
			title : '修改时间',
			visible : false,
			width : 75,
			formatter : function(value, row, index)
			{
				if (Helper.isNotEmpty(value))
				{
					return new Date(value).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'operator',
			title : '操作',
			width : 100,
			formatter : function(value, row, index)
			{
				var operator = "";
				if (Helper.basic.hasPermission("basic:procedure:edit"))
				{
					operator += '<a title="编辑" href="javascript:;" onclick="procedure_edit(' + row.id + ')" style="margin-right:20px"><i class="fa fa-pencil"></i></a>';
				}

				if (Helper.basic.hasPermission("basic:procedure:del"))
				{
					operator += '<a title="删除" href="javascript:;" onclick="procedure_del(this,' + row.id + ')"><i class="fa fa-trash-o"></i></a>';
				}

				if (Helper.basic.hasPermission("basic:procedure:copyCreate"))
				{
					operator += '<a title="复制" href="javascript:;" onclick="procedure_copyCreate(' + row.id + ')" style="margin-left:20px"><i class="fa fa-copy"></i></a>';
				}
				return operator;
			}
		} ],
		onDblClickRow : function(row)
		{
			procedure_edit(row.id);
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
			if (!Helper.basic.hasPermission('basic:procedure:export'))
			{
				$(".export.btn-group").remove();
			}
			$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
		}
	});

	/* 批量删除 */
	$("#batch_delete").on("click", function()
	{
		Helper.message.confirm('确认要删除吗？', function(index)
		{
			var rows = getSelectedRows();
			var ids = $(rows).map(function()
			{
				return this.id;
			}).get();
			if (Helper.isNotEmpty(rows))
			{
				Helper.post(Helper.basePath + '/basic/procedure/batchDelete', {
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
});

/* 导入 */
function import_info()
{
	Helper.popup.show('导入信息', Helper.basePath + '/import/company_procedure', '540', '150');
}

/* 工序-增加 */
function procedure_add()
{
	Helper.popup.show('添加工序', Helper.basePath + '/basic/procedure/create', '1016', '300');
}

/* 工序-复制添加 */
function procedure_copyCreate(id)
{
	Helper.popup.show('添加工序', Helper.basePath + '/basic/procedure/copyCreate/' + id, '1016', '300');
}

/* 工序-编辑 */
function procedure_edit(id)
{
	Helper.popup.show('编辑工序', Helper.basePath + '/basic/procedure/edit/' + id, '1016', '300');
}

/* 工序-删除 */
function procedure_del(obj, id)
{
	Helper.message.confirm('确认要删除吗？', function(index)
	{
		// 此处请求后台程序，下方是成功后的前台处理……
		$.post(Helper.basePath + '/basic/procedure/delete/' + id, function(result)
		{
			if (result.toBoolean())
			{
				$(obj).parents("tr").remove();
				Helper.message.suc("已删除！")
			} else
			{
				Helper.message.warn("删除失败，已被引用")
			}

		});
	});
}
/* 工序名称-变更 */
function procedure_updateName()
{
	var row = getSelectedRows();
	if (row.length > 1)
	{
		return Helper.message.warn("至多选择1项");
	} else if (row.length == 0)
	{
		return Helper.message.warn("至少选择1项");
	}
	var id = row[0].id;
	Helper.popup.show('变更工序名称', Helper.basePath + '/basic/procedure/updateName/' + id, '400', '200');
}
/* 工序信息-来源报价系统 */
function procedureFromOffer()
{
	Helper.popup.show('添加工序', Helper.basePath + '/quick/offer_procedure_select?multiple=true', '900', '490');
}

function responseHandler(res)
{
	return {
		rows : res.result,
		total : res.count
	};
}
// 获取选择项
function getSelectedRows()
{
	return $("#bootTable").bootstrapTable('getAllSelections');
}

function queryParams(params)
{
	params['procedureName'] = $("#procedureName").val();
	return params;
}

/**
 * 来源报价系统 - 新增
 * @param objs
 * @since 1.0, 2018年2月12日 下午2:13:18, think
 */
function getCallInfo_procedureArray(objs)
{
	var _procedureList = [];
	$.each(objs, function(i, o){
		_procedureList.push({
			name : o.name,
			className : o.className,
			procedureType : o.type
		});
	});
	
	// 快速添加
	Helper.request({
		url : Helper.basePath + "/basic/procedure/saveQuick",
		data : _procedureList,
		async : false,
		success : function(data)
		{
			if (data.success)
			{
				location.reload();
			} else
			{
				layer.alert('创建失败：' + data.message);
			}
		}
	});
}
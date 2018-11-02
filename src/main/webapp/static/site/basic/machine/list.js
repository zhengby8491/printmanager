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
		url : Helper.basePath + "/basic/machine/listAjax",
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
		cookieIdTable : "print_basic_machine",// 必须制定唯一的表格cookieID

		uniqueId : "id",// 定义列表唯一键
		columns : [ {
			field : 'state',
			checkbox : true,
			visible : true,
			width : 40
		}, {
			field : 'name',
			title : '机台名称',
			width : 80
		}, {
			field : 'code',
			title : '规格型号',
			width : 80
		}, {
			field : 'manufacturer',
			title : '生产厂商',
			width : 100
		}, {
			field : 'machineTypeText',
			title : '设备属性',
			width : 80
		}, {
			field : 'money',
			title : '设备金额',
			width : 80
		}, {
			field : 'capacity',
			title : '标准产能',
			width : 80
		}, {
			field : 'maxStyle',
			title : '最大上机',
			width : 80
		}, {
			field : 'minStyle',
			title : '最小上机',
			width : 80
		}, {
			field : 'colorQty',
			title : '最大印色',
			width : 80
		}, {
			field : 'createName',
			title : '创建人',
			visible : false,
			width : 80
		}, {
			field : 'createTime',
			title : '创建时间',
			visible : false,
			width : 80,
			formatter : function(value, row, index)
			{
				if (Helper.isNotEmpty(value))
				{
					return new Date(value).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'updateName',
			title : '修改人',
			visible : false,
			width : 60
		}, {
			field : 'updateTime',
			title : '修改时间',
			visible : false,
			width : 80,
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
			width : 80,
			formatter : function(value, row, index)
			{
				var operator = "";
				if (Helper.basic.hasPermission("basic:machine:edit"))
				{
					operator += '<a title="编辑" href="javascript:;"onclick="machine_edit(' + row.id + ')" style="margin-right: 20px"><i class="fa fa-pencil"></i></a>';
				}

				if (Helper.basic.hasPermission("basic:machine:del"))
				{
					operator += '<a title="删除" href="javascript:;" onclick="machine_del(this,' + row.id + ')"><i class="fa fa-trash-o"></i></a>';
				}

				if (Helper.basic.hasPermission("basic:machine:copyCreate"))
				{
					operator += '<a title="复制" href="javascript:;"onclick="machine_copyCreate(' + row.id + ')" style="margin-left: 20px"><i class="fa fa-copy"></i></a>';
				}
				return operator;
			}
		} ],
		onDblClickRow : function(row)
		{
			machine_edit(row.id);
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
	$("#bootTable").on('load-success.bs.table column-switch.bs.table page-change.bs.table search.bs.table', function()
	{
		/* 表格工具栏 */
		if ($(".glyphicon-th").next().html() == '')
		{
			$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
			if (!Helper.basic.hasPermission('basic:machine:export'))
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
				Helper.post(Helper.basePath + '/basic/machine/batchDelete', {
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

/* 机台信息-增加 */
function machine_create()
{
	Helper.popup.show('添加机台信息', Helper.basePath + '/basic/machine/create', '700', '325');
}
/* 机台信息-编辑 */
function machine_edit(id)
{
	Helper.popup.show('编辑机台信息', Helper.basePath + '/basic/machine/edit/' + id, '700', '325');
}

/* 机台信息-复制添加 */
function machine_copyCreate(id)
{
	Helper.popup.show('添加机台信息', Helper.basePath + '/basic/machine/copyCreate/' + id, '700', '325');
}
/* 机台信息-删除 */
function machine_del(obj, id)
{
	Helper.message.confirm('确认要删除吗？', function(index)
	{
		// 此处请求后台程序，下方是成功后的前台处理……
		$.post(Helper.basePath + '/basic/machine/delete/' + id, function(result)
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

function responseHandler(res)
{
	return {
		rows : res.result,
		total : res.count
	};
}

function queryParams(params)
{
	params['machineName'] = $("#machineName").val().trim();
	return params;
}

function getSelectedRows()
{
	return $("#bootTable").bootstrapTable('getAllSelections');
}
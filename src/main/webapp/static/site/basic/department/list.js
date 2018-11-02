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
		url : Helper.basePath + "/basic/department/listAjax",
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
		cookieIdTable : "print_basic_department",// 必须制定唯一的表格cookieID

		uniqueId : "id",// 定义列表唯一键
		columns : [ {
			field : 'index', // 序号
			title : Helper.i18n.getMsg("i18n_common_table_index"),
			width : 80,
			formatter : function(value, row, index)
			{
				return index + 1;
			}
		}, {
			field : 'name', // 部门名称
			title : Helper.i18n.getMsg("i18n_common_table_department_name"),
			width : 120
		}, {
			field : 'memo', // 备注
			title : Helper.i18n.getMsg("i18n_common_table_remark"),
			'class' : 'memoView',
			width : 200
		}, {
			field : 'operator', // 操作
			title : Helper.i18n.getMsg("i18n_common_table_operator"),
			width : 80,
			formatter : function(value, row, index)
			{
				var operator = "";
				if (Helper.basic.hasPermission("basic:department:edit"))
				{
					var $title = Helper.i18n.getMsg("i18n_common_table_button_edit");
					operator += '<a title="' + $title + '" href="javascript:;" onclick="depart_edit(' + row.id + ')" style="padding: 0 20px; color: green"><i class="fa fa-pencil"></i></a>';
				}

				if (Helper.basic.hasPermission("basic:department:del"))
				{
					var $title = Helper.i18n.getMsg("i18n_common_table_button_del");
					operator += '<a title="' + $title + '" href="javascript:;" onclick="depart_del(this,' + row.id + ')" style="padding: 0 20px; color: red"><i class="fa fa-trash-o"></i></a>';
				}
				return operator;
			}
		} ],
		onDblClickRow : function(row)
		{
			depart_edit(row.id);
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
			var $filter = Helper.i18n.getMsg("i18n_common_table_button_filter");
			$(".glyphicon-th").after("<span class='glyphicon_font'>" + $filter + "</span>");
			if (!Helper.basic.hasPermission('basic:department:export'))
			{
				$(".export.btn-group").remove();
			}
			var $export = Helper.i18n.getMsg("i18n_common_table_button_export");
			$(".glyphicon-export").after("<span class='glyphicon_font'>" + $export + "</span>");
		}
	});
});

/**
 * 添加部门
 * @since 1.0, 2018年1月4日 上午10:25:23, think
 */
function depart_create()
{
	var $title = Helper.i18n.getMsg("i18n_common_table_department_add");
	Helper.popup.show($title, Helper.basePath + '/basic/department/create', '400', '280');
}

/**
 * 编辑部门
 * @param id
 * @since 1.0, 2018年1月4日 上午10:25:41, think
 */
function depart_edit(id)
{
	var $title = Helper.i18n.getMsg("i18n_common_table_department_edit");
	Helper.popup.show($title, Helper.basePath + '/basic/department/edit/' + id, '400', '280');
}

/**
 * 部门-删除
 * @param obj
 * @param id
 * @since 1.0, 2018年1月4日 上午10:26:23, think
 */
function depart_del(obj, id)
{
	Helper.message.confirm(Helper.i18n.getMsg("i18n_common_del_confirm"), function(index)
	{
		// 此处请求后台程序，下方是成功后的前台处理……
		$.post(Helper.basePath + '/basic/department/delete/' + id, function(result)
		{
			if (result.toBoolean())
			{
				$(obj).parents("tr").remove();
				Helper.message.suc(Helper.i18n.getMsg("i18n_common_deleted"))
			} else
			{
				Helper.message.warn(Helper.i18n.getMsg("i18n_common_del_fail_ref"))
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
	params['departmentName'] = $("#departmentName").val();
	return params;
}

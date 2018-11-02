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
		url : Helper.basePath + "/basic/employee/listAjax",
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
		height : 400, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
		cardView : false, // 是否显示详细视图
		detailView : false, // 是否显示父子表

		showExport : true,// 是否显示导出按钮
		// exportDataType: 'all',
		exportTypes : [ 'csv', 'txt', 'excel', 'doc' ],

		cookie : true,// 是否启用COOKIE
		cookiesEnabled : [ 'bs.table.columns' ],// 默认会记住很多属性，这里控制只记住列选择属性
		cookieIdTable : "print_basic_employee",// 必须制定唯一的表格cookieID

		uniqueId : "id",// 定义列表唯一键
		columns : [ {
			field : 'index',
			title : '序号',
			width : 40,
			formatter : function(value, row, index)
			{
				// console.log($(this).pageSixze)
				return index + 1;
			}
		}, {
			field : 'code',
			title : '员工工号',
			width : 140
		}, {
			field : 'name',
			title : '员工名称',
			width : 140
		}, {
			field : 'departmentName',
			title : '部门',
			width : 60
		}, {
			field : 'positionName',
			title : '职位',
			width : 120
		}, {
			field : 'mobile',
			title : '电话',
			width : 80
		}, {
			field : 'sexTypeText',
			title : '性别',
			width : 60
		}, {
			field : 'email',
			title : '邮箱',
			width : 80
		}, {
			field : 'entryTime',
			title : '入职时间',
			width : 80,
			formatter : function(value, row, index)
			{
				if (Helper.isNotEmpty(value))
				{
					return new Date(value).format("yyyy-MM-dd");
				}
			}
		}, {
			field : 'departureTime',
			title : '离职时间',
			width : 80,
			formatter : function(value, row, index)
			{
				if (Helper.isNotEmpty(value))
				{
					return new Date(value).format("yyyy-MM-dd");
				}
			}
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
			title : '修改日期',
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
			field : 'memo',
			title : '备注',
			'class' : 'memoView',
			width : 200
		}, {
			field : 'stateText',
			title : '状态',
			width : 80,
			cellStyle : function(value, row, index, field)
			{
				console.log(value + "=" + row + "=" + index + "=" + field)
				if (value == '休假')
				{
					return {
						css : {
							"color" : "#f00"
						}
					};
				} else if (value == '离职')
				{
					return {
						css : {
							"color" : "#ccc"
						}
					};
				} else if (value == '正常')
				{
					return {
						css : {
							"color" : "#080"
						}
					};
				}
			}
		}, {
			field : 'operator',
			title : '操作',
			width : 80,
			formatter : function(value, row, index)
			{
				var operator = "";
				if (Helper.basic.hasPermission("basic:employee:edit"))
				{
					operator += '<a title="编辑" href="javascript:;"onclick="employee_edit(' + row.id + ')" style="margin-right:8px;"><i class="fa fa-pencil"></i></a>';

					if (row.state == 'NORMAL')
					{

						operator += '<a title="休假" href="javascript:;" onClick="employee_holiday(this,' + row.id + ')" style="margin-right:8px;" ><i class="fa fa-minus-square-o"></i></a>';
					}
					if (row.state == 'HOLIDAY')
					{

						operator += '<a title="离职" href="javascript:;" onClick="employee_stop(this,' + row.id + ')" style="margin-right:8px;" ><i class="fa fa-minus-square-o"></i></a>';
					}
					if (row.state == 'LEAVEJOB')
					{

						operator += '<a title="正常"  href="javascript:;" onClick="employee_start(this,' + row.id + ')" style="margin-right:8px;"><i class="fa fa-check-square-o"></i></a>';
					}
				}
				if (Helper.basic.hasPermission("basic:employee:del"))
				{
					operator += '<a title="删除" href="javascript:;" onclick="employee_del(this,' + row.id + ')" style="margin-right:8px; color: red"><i class="fa fa-trash-o"></i></a>';
				}
				return operator;
			}
		} ],
		onDblClickRow : function(row)
		{
			employee_edit(row.id);
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
			if (!Helper.basic.hasPermission('basic:employee:export'))
			{
				$(".export.btn-group").remove();
			}
			$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
		}
	});
});

/* 导入 */
function import_info()
{
	Helper.popup.show('导入信息', Helper.basePath + '/import/company_employee', '540', '150');
}
/* 员工信息-增加 */
function employee_create()
{
	Helper.popup.show('添加员工', Helper.basePath + '/basic/employee/create', '700', '400');
}
/* 员工信息-编辑 */
function employee_edit(id)
{
	Helper.popup.show('编辑员工', Helper.basePath + '/basic/employee/edit/' + id, '700', '400');
}
/* 员工信息-删除 */
function employee_del(obj, id)
{
	Helper.message.confirm('确认要删除吗？', function(index)
	{

		// 此处请求后台程序，下方是成功后的前台处理……
		$.post(Helper.basePath + '/basic/employee/delete/' + id, function(result)
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
/* 员工-离职 */
function employee_stop(obj, id)
{
	Helper.message.confirm('确认要离职吗？', function(index)
	{
		$.post(Helper.basePath + '/basic/employee/stop/' + id, function(result)
		{
			if (result.toBoolean())
			{
				Helper.message.suc("已离职！")
				$("#bootTable").bootstrapTable("refresh");
			}
		});

	});
}

/* 员工-休假 */
function employee_holiday(obj, id)
{
	Helper.message.confirm('确认要启用休假吗？', function(index)
	{
		Helper.request({// 验证新增是否成功
			url : Helper.basePath + "/basic/employee/holiday/" + id,
			async : false,
			success : function(data)
			{
				if (data.success || data == true)
				{
					Helper.message.suc("已休假！");
					$("#bootTable").bootstrapTable("refresh");
				} else
				{
					Helper.message.warn(data.message);
				}
			}
		});

	});
}

/* 员工-正常 */
function employee_start(obj, id)
{
	Helper.message.confirm('确认要启用正常吗？', function(index)
	{
		Helper.request({// 验证新增是否成功
			url : Helper.basePath + "/basic/employee/start/" + id,
			async : false,
			success : function(data)
			{
				if (data.success || data == true)
				{
					Helper.message.suc("已正常！");
					$("#bootTable").bootstrapTable("refresh");
				} else
				{
					Helper.message.warn(data.message);
				}
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
	params['employeeName'] = $("#employeeName").val();
	params['code'] = $("#code").val();
	return params;
}
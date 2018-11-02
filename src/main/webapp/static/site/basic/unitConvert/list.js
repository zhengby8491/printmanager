/**
 * Author:		   think
 * Create:	 	   2018年1月3日 上午9:36:44
 * Copyright: 	 Copyright (c) 2018
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 */
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
		url : Helper.basePath + "/basic/unitConvert/listAjax",
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
		cookieIdTable : "print_basic_unitConvert",// 必须制定唯一的表格cookieID

		uniqueId : "id",// 定义列表唯一键
		columns : [ {
			field : 'index',
			title : '序号',
			width : 100,
			formatter : function(value, row, index)
			{
				return index + 1;
			}
		}, {
			field : 'name',
			title : '单位换算名称',
			width : 200
		}, {
			field : 'sourceUnitName',
			title : '源单位',
			width : 200
		}, {
			field : 'conversionUnitName',
			title : '换算单位',
			width : 200
		}, {
			field : 'formula',
			title : '换算公式',
			formatter : function(value, row, index)
			{
				value = value.replace(new RegExp("length", "g"), "材料长");
				value = value.replace(new RegExp("width", "g"), "材料宽");
				value = value.replace(new RegExp("height", "g"), "材料高");
				value = value.replace(new RegExp("weight", "g"), "克重");
				value = value.replace(new RegExp("qty", "g"), "原单位数量");
				return value;
			}
		}, {
			field : 'operator',
			title : '操作',
			width : 140,
			formatter : function(value, row, index)
			{
				var operator = "";
				if (Helper.basic.hasPermission("basic:unit:edit") && row.isOrigin != "YES")
				{
					operator += '<a title="编辑" href="javascript:;" onclick="unit_edit(' + row.id + ')" style="margin-right:20px"><i class="fa fa-pencil"></i></a>';
				}

				if (Helper.basic.hasPermission("basic:unit:del") && row.isOrigin != "YES")
				{
					operator += '<a title="删除" href="javascript:;"onclick="unit_del(this,' + row.id + ')"><i class="fa fa-trash-o"></i></a>';
				}
				return operator;
			}
		} ],
		onDblClickRow : function(row)
		{
			if (row.isOrigin != "YES")
			{
				unit_edit(row.id);
			} else
			{
				return;
			}
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
			if (!Helper.basic.hasPermission('basic:unitConvert:export'))
			{
				$(".export.btn-group").remove();
			}
			$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
		}
	});
});

/* 单位信息-增加 */
function unit_create()
{
	Helper.popup.show('添加单位换算', Helper.basePath + '/basic/unitConvert/create', '450', '300');
}

/* 单位信息-编辑 */
function unit_edit(id)
{
	Helper.popup.show('编辑单位换算', Helper.basePath + '/basic/unitConvert/edit/' + id, '450', '300');
}

/* 单位信息-删除 */
function unit_del(obj, id)
{
	Helper.message.confirm('确认要删除吗？', function(index)
	{
		// 此处请求后台程序，下方是成功后的前台处理……
		$.post(Helper.basePath + '/basic/unitConvert/delete/' + id, function(result)
		{
			if (result.toBoolean())
			{
				$(obj).parents("tr").remove();
				$("#bootTable").bootstrapTable("refreshOptions", {
					pageNumber : 1
				});
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
	params['unitName'] = $("#unitName").val();
	return params;
}
/**
 * Author:		   think
 * Create:	 	   2017年10月17日 下午3:55:11
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 */
$(function()
{
	init(); // 初始化

	/**
	 * 初始化功能、按钮事件
	 * @since 1.0, 2017年10月17日 下午3:55:45, think
	 */
	function init()
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
			url : Helper.basePath + "/offer/setting/ajaxMachineList",
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
			cookieIdTable : "machine_setting",// 必须制定唯一的表格cookieID

			uniqueId : "id",// 定义列表唯一键
			columns : [ {
				field : 'machineProText',
				title : '机台属性',
				width : 80
			}, {
				field : 'name',
				title : '机台名称',
				width : 80
			}, {
				field : 'machinePrintColorText',
				title : '印刷颜色',
				width : 100
			}, {
				field : 'maxStyle',
				title : '最大上机(mm)',
				width : 80
			}, {
				field : 'minStyle',
				title : '最小上机(mm)',
				width : 80
			}, {
				field : 'minPrintPly',
				title : '最小印刷厚度(g)',
				width : 80
			}, {
				field : 'maxPrintPly',
				title : '最大印刷厚度(g)',
				width : 80
			}, {
				field : 'operator',
				title : '操作',
				width : 80,
				formatter : function(value, row, index)
				{
					var operator = "";
					if (Helper.basic.hasOfferPermission($("#offerType").val(), "machine:edit"))
					{
						operator += '<a title="编辑" href="javascript:;"onclick="machineEdit(' + row.id + ')" style="margin-right: 20px"><i class="fa fa-pencil"></i></a>';
					}

					if (Helper.basic.hasOfferPermission($("#offerType").val(), "machine:del"))
					{
						operator += '<a title="删除" href="javascript:;" onclick="machineDel(this,' + row.id + ')"><i class="fa fa-trash-o"></i></a>';
					}

					return operator;
				}
			} ],
			onDblClickRow : function(row)
			{
				machineView(row.id);
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
				/*
				 * if(!Helper.basic.hasPermission('basic:machine:export')){
				 * $(".export.btn-group").remove(); }
				 */
				$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");  
			}
		});
	}

	/**
	 * 响应处理（默认写上即可）
	 * @since 1.0, 2017年10月23日 上午9:16:22, think
	 */
	function responseHandler(res)
	{
		return {
			rows : res.result,
			total : res.count
		};
	}

	/**
	 * 查询条件
	 * @since 1.0, 2017年10月23日 上午9:16:22, think
	 */
	function queryParams(params)
	{
		params['offerType'] = $("#offerType").val();
		params['machineName'] = $("#machineName").val().trim();
		return params;
	}
});


/**
 * 
 * 页面 - 查看
 * @since 1.0, 2017年10月23日 下午3:57:57, think
 */
function machineView(id)
{
	var url = Helper.basePath + '/offer/setting/machineView/' + id + '?type=' + $("#offerType").val();
	var title = "机台";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"))
}

/**
 * 
 * 页面 - 新增
 * @since 1.0, 2017年10月17日 下午3:57:57, think
 */
function machineCreate(type)
{
	var url = Helper.basePath + '/offer/setting/machineCreate?type=' + type;
	var title = "机台";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}

/**
 * 
 * 页面 - 编辑
 * @since 1.0, 2017年10月17日 下午3:58:02, think
 */
function machineEdit(id)
{
	var url = Helper.basePath + '/offer/setting/machineEdit/' + id + '?type=' + $("#offerType").val();
	var title = "机台";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}

/**
 * 
 * 页面 - 删除
 * @since 1.0, 2017年10月23日 下午3:57:57, think
 */
function machineDel(obj, id)
{
	Helper.message.confirm('确认要删除吗？', function(index) {
    Helper.post(Helper.basePath + '/offer/setting/deleteMachine/' + id + '?type=' + $("#offerType").val(), function(data) {
      if(data.success)
      {
        $("#bootTable").bootstrapTable("removeByUniqueId", id);
        $("#bootTable").bootstrapTable("refresh");
        Helper.message.suc('已删除!');
      }else
      {
        Helper.message.warn('删除失败：' + data.message);
      }
    });
  });
}

/**
 * Author:		   think
 * Create:	 	   2018年1月5日 上午10:53:51
 * Copyright: 	 Copyright (c) 2018
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 */
$(function()
{
	$("#btn_back").click(function()
	{
		var url = Helper.basePath + '/begin/product/list';
		var title = "产品期初列表";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	});

	$("#detailList").bootstrapTable({
		url : Helper.basePath + "/begin/product/viewAjax/" + $("#id").val(),// 不需要查询
		method : "post",
		contentType : 'application/json', // 设置请求头信息
		dataType : "json",
		pagination : false, // 是否显示分页（*）
		sidePagination : 'server',// 设置为服务器端分页
		pageList : [ 10, 20, 50 ],
		queryParamsType : "",
		pageSize : 10,
		pageNumber : 1,
		responseHandler : viewResponseHandler,
		// resizable : true, //是否启用列拖动
		showColumns : true, // 是否显示所有的列
		minimumCountColumns : 2, // 最少允许的列数
		striped : true, // 是否显示行间隔色
		cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
		sortable : false, // 是否启用排序
		clickToSelect : true, // 是否启用点击选中行
		height : undefined, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
		cardView : false, // 是否显示详细视图
		detailView : false, // 是否显示父子表

		showExport : false,// 是否显示导出按钮
		// exportDataType: 'all',
		exportTypes : [ 'csv', 'txt', 'excel', 'doc' ],

		cookie : true,// 是否启用COOKIE
		cookiesEnabled : [ 'bs.table.columns' ],// 默认会记住很多属性，这里控制只记住列选择属性
		cookieIdTable : "print_begin_product_view",// 必须制定唯一的表格cookieID

		columns : [ {
			field : 'id',
			title : '序号',
			width : 60,
			formatter : function(value, row, index)
			{
				return index + 1;
			}
		}, {
			field : 'productCode',
			title : '产品编号',
			width : 120

		},{
			field : 'customerMaterialCode',
			title : '客户料号',
			width : 120

		}, {
			field : 'productName',
			title : '成品名称',
			width : 120

		}, {
			field : 'specifications',
			title : '规格',
			width : 100

		}, {
			field : 'unit',
			title : '单位',
			width : 60,
			formatter : function(value, row, index)
			{
				if (value != null)
				{
					return Helper.basic.info('UNIT', value).name;
				}
				return null;
			}
		}, {
			field : 'qty',
			title : '期初数量',
			width : 80

		}, {
			field : 'price',
			title : '单价',
			width : 60

		}, {
			field : 'money',
			title : '金额',
			width : 100

		}, {
			field : 'memo',
			title : '备注',
			'class' : 'memoView',
			width : 200
		} ],
		onLoadError : function()
		{
			// alert("数据加载异常");
		},
		onColumnSwitch : function()
		{
			resetLastTr();
			
			// 在使用筛选增加或减少列时重置表格列拖动效果
			bootstrapTable_ColDrag($("#detailList"));
		},
		onLoadSuccess : function()
		{
			resetLastTr();
		}
	});
	resetLastTr();
	/* 表格工具栏 */
	$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");

	$("#btn_edit").click(function()
	{
		window.location.href = Helper.basePath + '/begin/product/edit/' + $("#id").val();
	});
	$("#btn_audit").click(function()
	{
		$.ajax({
			cache : true,
			type : "POST",
			dataType : "json",
			url : Helper.basePath + '/begin/product/audit/' + $("#id").val(),
			async : false,
			error : function(request)
			{
				layer.alert("Connection error");
			},
			success : function(data)
			{
				// console.log(data.success);
				if (data.success)
				{
					location.reload();
				} else
				{
					Helper.message.warn(data.message);
				}
			}
		});
	});
	
	$("#btn_audit_cancel").click(function()
	{
		$.ajax({
			cache : true,
			type : "POST",
			dataType : "json",
			url : Helper.basePath + '/begin/product/auditCancel/' + $("#id").val(),
			async : false,
			error : function(request)
			{
				layer.alert("Connection error");
			},
			success : function(data)
			{
				// console.log(data.success);
				if (data.success)
				{
					location.reload();
				} else
				{
					Helper.message.warn(data.message);
				}
			}
		});
	});

	$("#btn_del").click(function()
	{
		Helper.message.confirm('确认要删除吗？', function(index)
		{
			$.ajax({
				cache : true,
				type : "POST",
				dataType : "json",
				url : Helper.basePath + '/begin/product/delete/' + $("#id").val(),
				async : false,
				error : function(request)
				{
					// console.log(request);
				},
				success : function(data)
				{
					// console.log(data.success);
					if (data.success)
					{
						closeTabAndJump("产品期初列表");/*
																			 * window.location.href=Helper.basePath +
																			 * '/begin/product/list';
																			 */
					} else
					{
						layer.alert("失败");
					}
				}
			});
		});
	});
	
	$("#detailList").on('load-success.bs.table', function()
	{
		// bootstrap_table加载完后触发列拖动效果
		bootstrapTable_ColDrag($("#detailList"));
	});
});

function resetLastTr()
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
		if (_tds.eq(index).text().indexOf("NaN") > 0)
		{
			_tds.eq(index).text('');
		}
	}
}
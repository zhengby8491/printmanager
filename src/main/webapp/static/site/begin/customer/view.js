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
		var url = Helper.basePath + '/begin/customer/list';
		var title = "客户期初列表";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	});

	$("#detailList").bootstrapTable({
		url : Helper.basePath + "/begin/customer/viewAjax/" + $("#id").val(),// 不需要查询
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
		cookieIdTable : "print_begin_customer_view",// 必须制定唯一的表格cookieID
		columns : [ {
			field : 'id',
			title : '序号',
			formatter : function(value, row, index)
			{
				return index + 1;
			}
		}, {
			field : 'receiveTime',
			title : '收款日期',
			formatter : function(value, row, index)
			{
				value == undefined ? value = "" : value = new Date(value).format("yyyy-MM-dd");
				return value;
			}
		}, {
			field : 'customerCode',
			title : '客户编号'
		}, {
			field : 'customerName',
			title : '客户名称',
		}, {
			field : 'currencyType',
			title : '币别',
			formatter : function(value, row, index)
			{
				return Helper.basic.getEnumText("com.huayin.printmanager.persist.enumerate.CurrencyType", row.currencyType, "text");
			}

		}, {
			field : 'receiveMoney',
			title : '应收款'
		}, {
			field : 'advanceMoney',
			title : '预收款'
		},

		{
			field : 'memo',
			'class' : 'memoView',
			title : '备注'
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
		onLoadSuccess : function(data)
		{
			resetLastTr();
		}
	});
	resetLastTr();
	/* 表格工具栏 */
	$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");

	$("#btn_edit").click(function()
	{
		window.location.href = Helper.basePath + '/begin/customer/edit/' + $("#id").val();
	});
	$("#btn_audit").click(function()
	{
		$.ajax({
			cache : true,
			type : "POST",
			dataType : "json",
			url : Helper.basePath + '/begin/customer/audit/' + $("#id").val(),
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
			url : Helper.basePath + '/begin/customer/auditCancel/' + $("#id").val(),
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
		this_ = $(this)
		Helper.message.confirm('确认要删除吗？', function(index)
		{
			$.ajax({
				cache : true,
				type : "POST",
				dataType : "json",
				url : Helper.basePath + '/begin/customer/delete/' + $("#id").val(),
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
						closeTabAndJump("客户期初列表");
						window.location.href = Helper.basePath + '/begin/customer/list';
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
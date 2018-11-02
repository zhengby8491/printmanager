$(function()
{
	$("input[name='checkType']").change(function()
	{
		var url = Helper.basePath + '/purch/sum/by_material_class';
		var title = "原材料采购订单汇总";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	});
	// 查询，刷新table
	$("#btn_search").click(function()
	{
		$("#bootTable").bootstrapTable("refreshOptions", {
			pageNumber : 1
		});
	});
	/* 日期年 */
	$("#year").select('/purch/sum/getYearsFromPurchOrder', {}, function()
	{
		$("#year").trigger("change");

		// 订单详情table
		$("#bootTable").bootstrapTable({
			url : Helper.basePath + "/purch/sum/by_material_name_list",
			method : "post",
			contentType : 'application/json', // 设置请求头信息
			dataType : "json",
			sidePagination : 'server',// 设置为服务器端分页
			pageList : Helper.bootPageList,
			queryParamsType : "",
			queryParams : queryParams,// 参数
			responseHandler : responseHandler,
			onLoadSuccess : function(data)
			{
				if (data.rows.length > 0)
				{
					optionbin.series[0].data = [];
					optionzhu.xAxis.categories = [];
					optionzhu.series[0].data = [];
					$.each(data.rows, function(intex, item)
					{
						if (item.style == null)
						{
							item.style = '';
						}
						if (item.id != null)
						{
							optionbin.series[0].data.push([ item.name + item.style, item.sumMoney ]);
							optionzhu.xAxis.categories.push(item.name + item.style);
							optionzhu.series[0].data.push(item.sumMoney);
						}
					});
					$('#pie_chart').html('');
					$('#pie_chart').highcharts(optionbin);
					$('#column_chart').highcharts(optionzhu);
				} else
				{
					$('#pie_chart').append('<div>暂无数据</div>');
				}

				$("#bootTable tbody").find("tr:last").find("td:first").text("合计");
				var _tds = $("#bootTable tbody").find("tr:last").find("td");
				for (var index = 0; index < _tds.length; index++)
				{
					if (_tds.eq(index).text() == "-")
					{
						_tds.eq(index).text('');
					}
				}
			},
			onLoadError : function()
			{
				$('#pie_chart').append('</div>暂无数据</div>');
			},
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
			cookieIdTable : "print_purch_sum_material_name",// 必须制定唯一的表格cookieID

			uniqueId : "id",// 定义列表唯一键
			columns : [ {
				field : 'name',
				title : '材料名称',
				width : 100
			}, {
				field : 'style',
				title : '材料规格',
				width : 100
			}, {
				field : 'sumMoney',
				title : '采购总额',
				width : 90
			}, {
				field : 'january',
				title : '一月',
				width : 80
			}, {
				field : 'february',
				title : '二月',
				width : 80
			}, {
				field : 'march',
				title : '三月',
				width : 80
			}, {
				field : 'april',
				title : '四月',
				width : 80
			}, {
				field : 'may',
				title : '五月',
				width : 80
			}, {
				field : 'june',
				title : '六月',
				width : 80
			}, {
				field : 'july',
				title : '七月',
				width : 80
			}, {
				field : 'august',
				title : '八月',
				width : 80
			}, {
				field : 'september',
				title : '九月',
				width : 80
			}, {
				field : 'october',
				title : '十月',
				width : 80
			}, {
				field : 'november',
				title : '十一月',
				width : 80
			}, {
				field : 'december',
				title : '十二月',
				width : 80
			}

			],
			onColumnSwitch : function(field, checked)
			{
				$("#bootTable tbody").find("tr:last").find("td:first").text("合计");
				var _tds = $("#bootTable tbody").find("tr:last").find("td");
				for (var index = 0; index < _tds.length; index++)
				{
					if (_tds.eq(index).text() == "-")
					{
						_tds.eq(index).text('');
					}
				}
			}
		});
	});
	$("#bootTable").on('load-success.bs.table column-switch.bs.table page-change.bs.table search.bs.table', function()
	{
		/* 表格工具栏 */
		if ($(".glyphicon-th").next().html() == '')
		{
			$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
			if (!Helper.basic.hasPermission('purch:by_material:export'))
			{
				$(".export.btn-group").remove();
			}
			$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
		}
	});

	/* 3D饼状图 */
	var optionbin = {
		chart : {
			type : 'pie',
			options3d : {
				enabled : true,
				alpha : 45,
				beta : 0
			}
		},
		colors : [ '#7CB5EC', '#44b5e6', '#38bbbf', '#5acb8b', '#95cd5c', '#c3cd5c', '#e7b520', '#e78623', '#b1704a', '#6181a1' ],
		title : {
			text : '饼图'
		},
		tooltip : {
			pointFormat : '{series.name}: <b>{point.percentage:.1f}%</b>'
		},
		plotOptions : {
			pie : {
				allowPointSelect : true,
				cursor : 'pointer',
				depth : 35,
				dataLabels : {
					enabled : true,
					format : '{point.name}'
				}
			}
		},
		series : [ {
			type : 'pie',
			name : '采购总额',
			data : []
		} ],
		exporting : {
			enabled : false
		},
		credits : {
			enabled : false
		}
	};

	/* 3D柱形图 */
	var optionzhu = {
		chart : {
			type : 'column',
			margin : 75,
			options3d : {
				enabled : true,
				alpha : 10,
				beta : 5,
				depth : 70
			}
		},
		title : {
			text : '柱形图'
		},
		plotOptions : {
			column : {
				depth : 25
			}
		},
		xAxis : {
			categories : []
		},
		yAxis : {
			title : {
				text : null
			}
		},
		series : [ {
			name : '采购总额',
			data : []
		} ],
		exporting : {
			enabled : false
		},
		credits : {
			enabled : false
		}
	};

	var num = 0;
	$(".switch_btn").click(function()
	{
		if (num == 0)
		{
			$(".boot-mar").hide();
			$(".chart").show();
			$(this).html('<i class="fa fa-th-large"></i> 汇总表');
			num = 1;
		} else if (num == 1)
		{
			$(".boot-mar").show();
			$(".chart").hide();
			$(this).html('<i class="fa fa-bar-chart"></i> 甘特图');
			num = 0;
		}
	});

	$("#year").change(function()
	{
		var year = new Date().format("yyyy-MM-dd").substring(0, 4)
		$("#dateMin").val($(this).val() == null ? year : $(this).val() + "-01-01");
		$("#dateMax").val($(this).val() == null ? year : $(this).val() + "-12-31");
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
	params['year'] = $("#year").val();
	params['dateMin'] = $("#dateMin").val();
	params['dateMax'] = $("#dateMax").val();
	params['materialName'] = $("#materialName").val();
	return params;
}

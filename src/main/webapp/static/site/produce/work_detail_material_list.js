$(function()
{
	$("#material_quick_select").click(function()
	{
		Helper.popup.show('选择材料', Helper.basePath + '/quick/material_select?multiple=false', '900', '490');
	});

	/* 搜索 */
	$("#btn_search").click(function()
	{
		$("#bootTable").bootstrapTable("refreshOptions", {
			pageNumber : 1
		});
	});
	$("input[type='radio']").change(function()
	{
		$("#btn_search").click();
	})
	// 订单详情table
	$("#bootTable").bootstrapTable({
		url : Helper.basePath + "/produce/work/ajaxWorkMaterialDetailList",
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
		onLoadSuccess : function(data)
		{
			if (data.rows.length > 0)
			{
				optionzhu.xAxis.categories = [];
				optionzhu.series[0].data = [];
				optionzhu.series[1].data = [];
				$.each(data.rows, function(intex, item)
				{
					optionzhu.xAxis.categories.push(item.materialName);
					optionzhu.series[0].data.push(item.materialQty);
					optionzhu.series[1].data.push(item.takeQty);
					$('#column_chart').highcharts(optionzhu);
				});
			} else
			{
				$('#column_chart').empty().append('<div>暂无数据</div>');
			}
		},
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
		cookieIdTable : "print_produce_work_material",// 必须制定唯一的表格cookieID

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
			field : 'isCheck',
			title : '单据状态',
			width : 60,
			formatter : function(value, row, index)
			{
				return row.isCheck == 'YES' ? '已审核' : '未审核';
			},
			cellStyle : function(value, row, index, field)
			{
				if (value == '未审核')
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
			title : '工单类型',
			width : 60
		}, {
			field : 'createTime',
			title : '制单日期',
			width : 60,
			formatter : function(value, row, index)
			{
				return new Date(row.createTime).format("yyyy-MM-dd");
			}
		}, {
			field : 'billNo',
			width : 80,
			title : '生产单号',
			formatter : function(value, row, index)
			{
				return billNoTransToUrl(value);
			}
		}, {
			field : 'partName',
			title : '部件名称',
			width : 60
		}, {
			field : 'workPartQty',
			title : '部件数量',
			width : 60
		}, {
			field : 'workPartStyle',
			title : '上机规格',
			width : 60
		}, {
			field : 'materialName',
			title : '材料名称',
			width : 60
		}, {
			field : 'splitQty',
			title : '材料开数',
			width : 60
		}, {
			field : 'materialStyle',
			title : '材料规格',
			width : 60
		}, {
			field : 'materialQty',
			title : '材料用量',
			width : 60
		}, {
			field : 'takeQty',
			title : '已领料数量',
			width : 60
		}, {
			field : 'takeQty/materialQty',
			title : '用料率',
			width : 60,
			formatter : function(value, row, index)
			{
				if (row.takeQty == 0)
				{
					return row.takeQty + "%";
				}
				return (row.takeQty / row.materialQty).toFixed(2) * 100 + "%"
			}
		}, {
			field : 'materialQty-takeQty',
			title : '未领料数量',
			width : 60,
			formatter : function(value, row, index)
			{

				return row.materialQty - row.takeQty;
			}
		}, {
			field : 'supplementQty',
			title : '补料数量',
			width : 60
		}, {
			field : 'returnQty',
			title : '退料数量',
			width : 60
		} ],
		onDblClickRow : function(row)
		{
			work_view(row.workId);
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
			if (!Helper.basic.hasPermission('produce:material:export'))
			{
				$(".export.btn-group").remove();
			}
			$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
		}
	});

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
		colors : [ '#7CB5EC', '#44b5e6', '#38bbbf', '#5acb8b', '#95cd5c', '#c3cd5c', '#e7b520', '#e78623', '#b1704a', '#6181a1' ],
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
			name : '材料用量',
			data : []
		}, {
			name : '已领料数量',
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

});
// 获取返回信息

function getCallInfo_material(obj)
{
	$("#materialName").val(obj.name);
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
	params['dateMin'] = $("#dateMin").val();
	params['dateMax'] = $("#dateMax").val();
	params['billNo'] = $("#billNo").val();
	params['materialName'] = $("#materialName").val();
	params['auditFlag'] = $("input[name='auditFlag']:checked").val() == "-1" ? null : $("input[name='auditFlag']:checked").val();
	params['billType'] = $("#billType").val() == "-1" ? null : $("#billType").val();

	params['productStyle'] = $("#materialStyle").val();

	return params;
}
/* 查看 */
function work_view(id)
{
	var url = Helper.basePath + '/produce/work/view/' + id;
	var title = "生产工单";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"))
}
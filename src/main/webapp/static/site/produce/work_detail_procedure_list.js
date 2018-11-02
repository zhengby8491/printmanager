$(function()
{
	/* 更多 */
	$("#btn_more").click(function()
	{
		$("#more_div").toggle();
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
		url : Helper.basePath + "/produce/work/ajaxWorkProcedureDetailList",
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
		cookieIdTable : "print_produce_work_procedure",// 必须制定唯一的表格cookieID

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
				return row.work.isCheck == 'YES' ? '已审核' : '未审核';
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
			width : 60,
			formatter : function(value, row, index)
			{
				return row.work.billTypeText;
			}
		}, {
			field : 'createTime',
			title : '制单日期',
			width : 80,
			formatter : function(value, row, index)
			{
				return new Date(row.work.createTime).format("yyyy-MM-dd");
			}
		}, {
			field : 'billNo',
			width : 80,
			title : '生产单号',
			formatter : function(value, row, index)
			{
				return billNoTransToUrl(row.work.billNo);
			}
		}, {
			field : 'partName',
			title : '部件名称',
			width : 60,
			formatter : function(value, row, index)
			{
				if (Helper.isNotEmpty(row.workPart))
				{
					return row.workPart.partName;
				}

			}
		}, {
			field : 'workPart.qty',
			title : '部件数量',
			width : 60,
			formatter : function(value, row, index)
			{
				if (Helper.isNotEmpty(row.workPart))
				{
					return row.workPart.qty;
				}
			}
		}, {
			field : 'workPart.style',
			title : '上机规格',
			width : 60,
			formatter : function(value, row, index)
			{
				if (Helper.isNotEmpty(row.workPart))
				{
					return row.workPart.style;
				}
			}
		}, {
			field : 'procedureName',
			title : '工序名称',
			width : 60
		}, {
			field : 'procedureClassName',
			title : '工序分类',
			width : 60
		}, {
			field : 'procedureTypeText',
			title : '工序类型',
			width : 60
		}, {
			field : 'isOutSourceText',
			title : '是否发外',
			width : 60
		}, {
			field : 'inputQty',
			width : 80,
			title : '投入数',
			width : 80
		}, {
			field : 'outputQty',
			width : 80,
			title : '产出数'
		}, {
			field : 'outOfQty',
			width : 80,
			title : '已发外数'
		}, {
			field : 'arriveOfQty',
			width : 80,
			title : '已到货数'
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
			if (!Helper.basic.hasPermission('produce:procedure:export'))
			{
				$(".export.btn-group").remove();
			}
			$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
		}
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
	params['dateMin'] = $("#dateMin").val();
	params['dateMax'] = $("#dateMax").val();
	params['billNo'] = $("#billNo").val();
	params['auditFlag'] = $("input[name='auditFlag']:checked").val() == "-1" ? null : $("input[name='auditFlag']:checked").val();
	params['billType'] = $("#billType").val() == "-1" ? null : $("#billType").val();
	params['procedureType'] = $("#procedureType").val() == "-1" ? null : $("#procedureType").val();

	params['procedureName'] = $("#procedureName").val();
	params['partName'] = $("#partName").val();
	return params;
}

/* 查看 */
function work_view(id)
{
	var url = Helper.basePath + '/produce/work/view/' + id;
	var title = "生产工单";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"))
}
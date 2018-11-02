$(function()
{

	// 订单详情table
	$("#bootTable").bootstrapTable({
		url : null,
		method : "post",
		contentType : 'application/json', // 设置请求头信息
		dataType : "local",
		pagination : true, // 是否显示分页（*）
		sidePagination : 'client',// 设置为服务器端分页
		pageList : Helper.bootPageList,
		queryParamsType : "",
		pageSize : 20,
		pageNumber : 1,
		queryParams : queryParams,// 参数
		responseHandler : responseHandler,
		showColumns : true, // 是否显示所有的列
		minimumCountColumns : 2, // 最少允许的列数
		striped : false, // 是否显示行间隔色
		cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
		sortable : true, // 是否启用排序
		clickToSelect : false, // 是否启用点击选中行
		height : 430, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
		cardView : false, // 是否显示详细视图
		detailView : false, // 是否显示父子表
		showExport : false,// 是否显示导出按钮
		exportTypes : [ 'csv', 'txt', 'excel', 'doc' ],
		cookie : true,// 是否启用COOKIE
		cookiesEnabled : [ 'bs.table.columns' ],// 默认会记住很多属性，这里控制只记住列选择属性
		cookieIdTable : "print_transmit_product_in",// 必须制定唯一的表格cookieID
		uniqueId : "id",// 定义列表唯一键
		columns : [
		/*
		 * { field : 'state', checkbox : true, visible : true, width : 40 },
		 */
		{
			field : 'operate',
			title : '操作',
			width : 40,
			align : 'center',
			events : operateEvents,
			formatter : operateFormatter
		}, {
			field : 'index',
			title : '序号',
			width : 40,
			formatter : function(value, row, index)
			{
				return index + 1;
			}
		}, {
			field : 'billNo',
			title : '生产单号',
			width : 100,
			formatter : function(value, row, index)
			{
				return billNoTransToUrl(value);
			}
		}, {
			field : 'sourceBillNo',
			title : '销售单号',
			width : 100,
			formatter : function(value, row, index)
			{
				return billNoTransToUrl(value);
			}
		}, {
			field : 'customerName',
			title : '客户名称',
			width : 160
		}, {
			field : 'customerBillNo',
			title : '客户单号',
			width : 100
		}, {
			field : 'productName',
			title : '产品名称',
			width : 120
		}, {
			field : 'specifications',
			title : '产品规格',
			width : 100
		}, {
			field : 'produceQty',
			title : '生产数量',
			width : 60
		}, {
			field : 'partName',
			title : '部件名称',
			width : 60
		}, {
			field : 'procedureName',
			title : '工序名称',
			width : 100
		}, {
			field : 'yieldQty',
			title : '应产数',
			width : 80
		}, {
			field : 'reportQty',
			title : '上报数',
			width : 60,
			formatter : function(value, row, index)
			{
				// return index + 1;
				var isRotary = row.productType == 'ROTARY';
				var isPart = row.isPart == 'YES';
				if (isRotary && isPart)
				{
					return "<input name=\"reportQty\" class=\"tab_input bg_color constraint_decimal_negative\" value=" + row.unreportQty + ">";
				} else
				{
					return "<input name=\"reportQty\" class=\"tab_input bg_color constraint_negative\" value=" + row.unreportQty + ">";
				}
			}
		}, {
			field : 'qualifiedQty',
			title : '合格数',
			width : 60,
			formatter : function(value, row, index)
			{
				var isRotary = row.productType == 'ROTARY';
				var isPart = row.isPart == 'YES';
				if (isRotary && isPart)
				{
					return "<input name=\"qualifiedQty\" class=\"tab_input constraint_decimal_negative\" readonly='readonly' value=" + row.unreportQty + ">";
				} else
				{
					return "<input name=\"qualifiedQty\" class=\"tab_input constraint_negative\" readonly='readonly' value=" + row.unreportQty + ">";
				}
			}
		}, {
			field : 'unqualified',
			title : '不合格数',
			width : 80,
			formatter : function(value, row, index)
			{

				var isRotary = row.productType == 'ROTARY';
				var isPart = row.isPart == 'YES';
				if (isRotary && isPart)
				{
					return "<input name=\"unqualified\" class=\"tab_input bg_color constraint_decimal_negative\" value=\"0\">" + "</input>";
				} else
				{
					return "<input name=\"unqualified\" class=\"tab_input bg_color constraint_negative\" value=\"0\">" + "</input>";
				}
			}
		}, {
			field : 'startTime',
			title : '开始时间',
			width : 150,
			formatter : function(value, row, index)
			{
				var startTime = new Date().format("yyyy-MM-dd hh:mm:ss");

				return "<input type=\"text\" onfocus=\"WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:formatterTime})\" class=\"tab_input bg_color input-txt input-txt_0 Wdate \"  name=\"startTime\" value=\"" + startTime + "\"/>";
			}
		}, {
			field : 'endTime',
			title : '结束时间',
			width : 150,
			formatter : function(value, row, index)
			{
				var endTime = new Date().format("yyyy-MM-dd hh:mm:ss");
				return "<input type=\"text\" onfocus=\"WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:formatterTime})\" class=\"tab_input bg_color input-txt input-txt_0 Wdate \"  name=\"endTime\" value=\"" + endTime + "\"/>";
			}
		} ],
		onColumnSwitch : function(field, checked)
		{
			// 在使用筛选增加或减少列时重置表格列拖动效果
			bootstrapTable_ColDrag($("#bootTable"));
		}
	});
	/* 表格工具栏 */
	(function showChoose()
	{
		if ($(".glyphicon-th").next().html() == '')
		{
			$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
			if (!Helper.basic.hasPermission('produce:work_detail:export'))
			{
				$(".export.btn-group").remove();
			}
			// $(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
		}
	})()
	function queryParams(params)
	{
		return params;
	}

	function responseHandler(res)
	{
		return {
			rows : res.result,
			total : res.count
		};
	}

	initReport();

	// TODO 优化JS initCommonReport('reportQty','qualifiedQty','unqualified',test);
});

/* 点击时间控件并设置提交到后台的数据 */
function formatterTime(dp)
{
	var field = $(this).attr('name');
	var id = $(this).parent().parent().data('uniqueid');
	console.log(field + "," + id)
	postDataEditable[id][field] = dp.cal.getNewDateStr();
	console.log(postDataEditable)
}

function test(a, b, c, d)
{
	console.log("a=" + a + ",b=" + b + ",c=" + c + ",d=" + d)
}

var postDataEditable = {};

window.operateEvents = {
	'click .remove' : function(e, value, row, index)
	{
		// 同时删除提交数据中的对象
		delete postDataKey[row.id];
		$('#bootTable').bootstrapTable('remove', {
			field : 'id',
			values : [ row.id ]
		});
	}
};

function operateFormatter(value, row, index)
{
	return [ '<a class="remove" href="javascript:void(0)" title="Remove">', '<i class="delete fa fa-trash-o"></i>', '</a>' ].join('');
}
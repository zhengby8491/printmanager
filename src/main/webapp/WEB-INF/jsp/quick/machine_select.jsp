<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>机台信息</title>
<style type="text/css">
.btn-bar{
 margin-bottom: 0px;
}
</style>
<script type="text/javascript">
	$(function()
	{
		//查询，刷新table
		$("#btn_search").click(function()
		{
			$("#bootTable").bootstrapTable("refreshOptions", {
				pageNumber : 1
			});
		});

		//订单详情table
		$("#bootTable").bootstrapTable({
			url : Helper.basePath + "/basic/machine/listAjax",
			method : "post",
			contentType : 'application/json', //设置请求头信息  
			dataType : "json",
			pagination : true, // 是否显示分页（*）
			sidePagination : 'server',//设置为服务器端分页
			pageList : Helper.bootPageList,
			queryParamsType : "",
			pageSize : 10,
			pageNumber : 1,
			queryParams : queryParams,//参数
			responseHandler : responseHandler,

			//resizable : true, //是否启用列拖动
			showColumns : true, //是否显示所有的列
			minimumCountColumns : 2, //最少允许的列数
			striped : true, // 是否显示行间隔色
			cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			sortable : false, // 是否启用排序
			clickToSelect : true, // 是否启用点击选中行
			height : 400, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			cardView : false, // 是否显示详细视图
			detailView : false, // 是否显示父子表

			showExport : true,//是否显示导出按钮
			//exportDataType: 'all',
			exportTypes : [ 'csv', 'txt', 'excel', 'doc' ],

			cookie : true,//是否启用COOKIE
			cookiesEnabled : [ 'bs.table.columns' ],//默认会记住很多属性，这里控制只记住列选择属性
			cookieIdTable : "print_basic_machine",//必须制定唯一的表格cookieID

			uniqueId : "id",//定义列表唯一键
			columns : [ {
				field : 'name',
				title : '机台名称',
				width : 80
			}, {
				field : 'code',
				title : '规格型号',
				width : 80
			}, {
				field : 'manufacturer',
				title : '生产厂商',
				width : 100
			}, {
				field : 'machineTypeText',
				title : '设备属性',
				width : 80
			}, {
				field : 'money',
				title : '设备金额',
				width : 80
			}, {
				field : 'capacity',
				title : '标准产能',
				width : 80
			}, {
				field : 'maxStyle',
				title : '最大上机',
				width : 80
			}, {
				field : 'minStyle',
				title : '最小上机',
				width : 80
			}, {
				field : 'colorQty',
				title : '最大印色',
				width : 80
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
				title : '修改时间',
				visible : false,
				width : 80,
				formatter : function(value, row, index)
				{
					if (Helper.isNotEmpty(value))
					{
						return new Date(value).format("yyyy-MM-dd");
					}
				}
			} ],
			onDblClickRow : function(row)
			{
				machine_select(row);
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
				if (!Helper.basic.hasPermission('basic:machine:export'))
				{
					$(".export.btn-group").remove();
				}
				$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
			}
		});
	});

	function machine_select(row)
	{
		parent.getCallInfo_machine(row);
		Helper.popup.close();
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
		params['machineName'] = $("#machineName").val().trim();
		return params;
	}

	function getSelectedRows()
	{
		return $("#bootTable").bootstrapTable('getAllSelections');
	}
	
</script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--搜索栏-->
			<div class="search_container">
				<input type="text" class="input-txt" style="width: 250px; height: 27px" value="${machineName}" placeholder="输入机台名称" id="machineName" name="machineName">
				<button type="button" class="nav_btn table_nav_btn" id="btn_search">
					<i class="fa fa-search"></i>
					查询
				</button>
			</div>
			<!--表格-->
			<div>
				<!--按钮组-->
				<div class="btn-bar">
					<span>
						<a href="javascript:;" onclick="shotCutWindow('WORK')" class="nav_btn table_nav_btn">
							<i class="fa fa-plus-square"></i>
							添加机台信息
						</a>
					</span>
				</div>

				<!-- 表格内容 -->
				<div class="boot-mar">
					<table class="border-table resizable" id="bootTable">
					</table>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
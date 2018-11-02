<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>

<meta charset="UTF-8">
<title>选择工序</title>
<script type="text/javascript">
	$(function()
	{
		$(".tree").on("click", "a[name='procedureClassId']", function()
		{
			$(".tree a[name='procedureClassId']").css("color", "");
			$(this).css("color", "red");
			var id = $(this).attr("_id");
			$("#procedureClassId").val(id);
			$("#bootTable").bootstrapTable("refreshOptions", {
				pageNumber : 1
			});
		});

		//查询
		$("#btn_search").click(function()
		{
			$("#bootTable").bootstrapTable("refreshOptions", {
				pageNumber : 1
			});
		});

		//选择确认
		$("#btn_ok").click(function()
		{
			if ($("#multiple").val().toBoolean())
			{
				parent.getCallInfo_procedureArray($("#bootTable").bootstrapTable('getAllSelections'));
			}
			Helper.popup.close();
		});

		$("#btn_cancel").click(function()
		{
			Helper.popup.close();
		});
		//订单详情table
		$("#bootTable").bootstrapTable({
			url : Helper.basePath + "/quick/procedure_list",
			method : "post",
			contentType : 'application/json', //设置请求头信息  
			dataType : "json",
			pagination : true, // 是否显示分页（*）
			sidePagination : 'server',//设置为服务器端分页
			pageList : [ 10, 20, 50 ],
			queryParamsType : "",
			pageSize : 10,
			pageNumber : 1,
			queryParams : queryParams,//参数
			responseHandler : responseHandler,

			//showColumns : true, //是否显示所有的列
			//minimumCountColumns : 2, //最少允许的列数
			striped : true, // 是否显示行间隔色
			cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			sortable : false, // 是否启用排序
			clickToSelect : true, // 是否启用点击选中行
			height : 360, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			cardView : false, // 是否显示详细视图
			detailView : false, // 是否显示父子表
			checkbox : $("#multiple").val().toBoolean(),//开启多选
			uniqueId : "id",//定义列表唯一键
			//resizable : true, //是否启用列拖动
			columns : [ {
				field : 'state',
				checkbox : true,
				width : 60,
				visible : $("#multiple").val().toBoolean()
			}, {
				field : 'procedureTypeText',
				title : '类型',
				width : 80
			}, {
				field : 'procedureClassName',
				title : '分类名称',
				width : 100
			}, {
				field : 'name',
				width : 80,
				title : '工序名称'
			} ],
			onDblClickRow : function(row)
			{
				selectRow(row);
			}

		});

	});

	//请求参数
	function queryParams(params)
	{
		params['procedureClassId'] = $("#procedureClassId").val();
		params['procedureName'] = $("#procedureName").val();
		// 工具工序分类过滤
		params['procedureTypeArray'] = $("input[name='procedureTypeArray']").map(function()
				{
			return $(this).val();
		}).get();

		return params;
	}
	//ajax结果
	function responseHandler(res)
	{
		return {
			rows : res.result,
			total : res.count
		};
	}
	function selectRow(row)
	{
		if ($("#multiple").val().toBoolean())
		{//多选,则双击事件无效
			parent.getCallInfo_procedureArray([ row ]);
		} else
		{//单选，双击事件立即返回,处理返回index事件
			if ($("#isToOem").val().toBoolean())
			{
				parent.getCallInfo_procedure_oem(row);
			} else
			{
				parent.getCallInfo_procedure(row);
			}	
			Helper.popup.close();
		}

	}
</script>
</head>
<body>
	<input type="hidden" id="procedureClassId" value="" />
	<input type="hidden" id="multiple" value="${multiple }" />
	<input type="hidden" id="isToOem" value="${isToOem }" />
	<c:forEach items="${procedureTypeArray }" var="pitem">
		<input type="hidden" name="procedureTypeArray" value="${pitem }">
	</c:forEach>
	<div class="layer_container">
		<div class="cl layer_content">
			<!--分类选择-->
			<div class="tree">
				<ul>
					<a href="javascript:void(0);" _id="" name="procedureClassId">全部</a>
					<c:forEach items="${fns:basicList('PROCEDURECLASS') }" var="item">
						<a href="javascript:void(0);" _id="${item.id }" name="procedureClassId">${item.name }</a>
					</c:forEach>
				</ul>
			</div>
			<!--表格容器左START-->
			<div class="layer_table_container wrap_width_1">
				<div class="cl layer_top">
					<div class="row-dd top_bar">
						<label class="form-label label_ui"></label>
						<span class="ui-combo-wrap" class="form_text">
							<input type="text" class="input-txt input-txt_9" id="procedureName" name="procedureName" placeholder="工序名称" />
						</span>
						<div class="layer_btns">
							<button class="nav_btn table_nav_btn search_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查找
							</button>
							<button type="button" class="nav_btn table_nav_btn" id="btn_ok">确认</button>
							<button type="button" class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
						</div>
					</div>
				</div>
				<!-- 表格 -->
				<div class="table-wrap" style="height: 390px;">
					<table class="layer_table" id="bootTable">

					</table>
				</div>
			</div>
			<!--表格容器左END-->
		</div>
	</div>
</body>
</html>

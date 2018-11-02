<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>

<meta charset="UTF-8">
<title>选择账户</title>
<script type="text/javascript">
	$(function()
	{

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

			//console.log($("#multiple").val());
			if ($("#multiple").val().toBoolean())
			{//多选,则双击事件
				parent.getCallInfo_accountArray($("#bootTable").bootstrapTable('getAllSelections'));
			}
			Helper.popup.close();
		});

		$("#btn_cancel").click(function()
		{
			Helper.popup.close();
		});

		$("#btn_create").click(function()
		{
			shotCutWindow("ACCOUNT");
		})
		var isBegin = $("#isBegin").val().toBoolean();
		console.log("shit" + (Helper.isNotEmpty(isBegin)))
		var url_;
		if (Helper.isNotEmpty(isBegin))
		{
			url_ = Helper.basePath + "/quick/account_list?isBegin=${isBegin}" ;
		} else
		{
			url_ = Helper.basePath + "/quick/account_list";
		}
		
		//订单详情table
		$("#bootTable").bootstrapTable({
			url : url_,
			method : "post",
			contentType : 'application/json', //设置请求头信息  
			dataType : "json",
			striped : true, // 是否显示行间隔色
			cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			sortable : false, // 是否启用排序
			clickToSelect : true, // 是否启用点击选中行
			height : 360, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			cardView : false, // 是否显示详细视图
			detailView : false, // 是否显示父子表
			checkbox : true,//开启多选
			uniqueId : "id",//定义列表唯一键
			columns : [ {
				field : 'state',
				checkbox : true,
				visible : true,
				width : 60
			}, {
				field : 'bankNo',
				title : '账户',
				width : 160
			}, {
				field : 'branchName',
				title : '支行名称',
				width : 200

			}, {
				field : 'currencyTypeText',
				title : '币种',
				width : 80
			} ],
			onDblClickRow : function(row)
			{
				//双击选中事件	
				selectRow(row);
			}
		});

	});
	function selectRow(row)
	{
		if ($("#multiple").val().toBoolean())
		{//多选,双击事件
			parent.getCallInfo_accountArray([ row ]);
		} else
		{//单选，双击事件立即返回
			parent.getCallInfo_account(row);
			Helper.popup.close();
		}
	}
</script>
</head>
<body>
	<input type="hidden" id="multiple" value="${multiple}">
	<input type="hidden" id="isBegin" value="${isBegin }">
	<div class="layer_container">
		<div class="cl layer_content">

			<!--表格容器左START-->
			<div class="layer_table_container" style="width: 787px; left: 0">
				<div class="cl layer_top">
					<div class="row-dd" style="margin-left: 5px">
						<div class="layer_btns">
							<button type="button" class="nav_btn table_nav_btn" id="btn_ok">确认</button>
							<button type="button" class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
							<button type="button" style="float: right; margin-right: -14px;" class="nav_btn table_nav_btn" id="btn_create">
								<i class="fa fa-plus-square"></i>
								<span style="margin-left: 4px;">新增</span>
							</button>
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

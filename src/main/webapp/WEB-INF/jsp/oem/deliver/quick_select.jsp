<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>

<meta charset="UTF-8">
<title>选择退货源</title>
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
			//console.log(parent)
			parent.getCallInfo_deliverSingle($("#bootTable").bootstrapTable('getAllSelections'));
			Helper.popup.close();
		});

		$("#btn_cancel").click(function()
		{
			Helper.popup.close();
		});
		//console.log($("#multiple").val().toBoolean());
		//订单详情table
		$("#bootTable").bootstrapTable({
			url : Helper.basePath + "/oem/deliver/ajaxDeliverList",
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
				visible : $("#multiple").val().toBoolean(),
				width : 40
			}, {
				field : 'createTime',
				title : '制单日期',
				width : 80,
				formatter : function(value, row, index)
				{
					if (row.master.createTime)
					{
						return new Date(row.master.createTime).format("yyyy-MM-dd");
					}
				}
			}, {
				field : 'billNo',
				title : '送货单号',
				width : 80,
				formatter : function(value, row, index)
				{
					return row.master.billNo
				}
			}, {
				field : 'productName',
				title : '成品名称',
				width : 140
			}, {
				field : 'procedureName',
				title : '工序名称',
				width : 80
			}, {
				field : 'partName',
				title : '部件名称',
				width : 80
			},{
				field : 'style',
				title : '加工规格',
				width : 80
			}, {
				field : 'qty',
				title : '数量',
				width : 60
			}, {
				field : 'returnQty',
				title : '已退货数量',
				width : 80
			}, {
				field : 'qty_returnQty',
				title : '未退货数量',
				width : 80,
				formatter : function(value, row, index)
				{
					return row.qty - row.returnQty;
				}
			}],
			onDblClickRow : function(row)
			{
				//双击选中事件	
				selectRow(row);
			}

		});

	});

	function selectRow(row)
	{
		parent.getCallInfo_deliverSingle_dbl(row);

	}
	//请求参数
	function queryParams(params)
	{
		params['dateMin'] = $("#dateMin").val();
		params['dateMax'] = $("#dateMax").val();
		params['customerId'] = $("#customerId").val();
		params['billNo'] = $("#billNo").val();
		params['procedureName'] = $("#procedureName").val();
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
</script>
</head>
<body>
	<input type="hidden" id="multiple" value="${multiple }">
	<input type="hidden" id="customerId" value="${customerId }">
	<input type="hidden" id="rowIndex" value="${rowIndex }">
	<div class="layer_container">
		<div class="cl layer_content">
			<!--分类选择-->
			<!--表格容器左START-->
			<div class="layer_table_container" style="left: 0; width: 785px">
				<div class="cl layer_top">
					<div class="row-dd" style="margin-left: 5px">
						<label class="form-label label_ui"></label>
						<span class="ui-combo-wrap" class="form_text">
							<sys:dateConfine label="制单日期" />
							<input type="text" class="input-txt input-txt_9" name="billNo" id="billNo" value="${billNo }" placeholder="送货单号" />
							<input type="text" class="input-txt input-txt_9" name="procedureName" id="procedureName" placeholder="工序名称" />
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

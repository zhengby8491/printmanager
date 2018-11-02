<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>

<meta charset="UTF-8">
<title>选择材料</title>
<script type="text/javascript">
	$(function() {
		$(".tree").on("click","a[name='materialClassId']",function() {
			$(".tree a[name='materialClassId']").css("color","");
			$(this).css("color","red");
			var id = $(this).attr("_id");
		    $("#materialClassId").val(id);
			$("#bootTable").bootstrapTable("refreshOptions",{pageNumber:1});
		});
		
		//查询
		$("#btn_search").click(function(){
			$("#bootTable").bootstrapTable("refreshOptions",{pageNumber:1});
		});
		
		//选择确认
		$("#btn_ok").click(function(){
		  if($("#multiple").val().toBoolean())
	  	  {//多选,则双击事件
			parent.getCallInfo_materialArray($("#bootTable").bootstrapTable('getAllSelections'));
	  	  }
	  	  Helper.popup.close();
		});
		
		$("#btn_add").click(function(){
				Helper.popup.show('快速添加材料', Helper.basePath + '/quick/material_add', '530','250');
		});
		
		$("#btn_cancel").click(function(){
			Helper.popup.close();
		});
		//订单详情table
		$("#bootTable")
				.bootstrapTable(
						{
							url : Helper.basePath
									+ "/quick/material_list",
							method : "post",
							contentType : 'application/json', //设置请求头信息  
							dataType : "json",
							pagination : true, // 是否显示分页（*）
							sidePagination : 'server',//设置为服务器端分页
							pageList : [ 10, 20,50 ],
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
							checkbox: $("#multiple").val().toBoolean(),//开启多选
							uniqueId : "id",//定义列表唯一键
							//resizable : true, //是否启用列拖动
							columns : [
							         {
						                field: 'state',
						                checkbox: true,
						                visible: $("#multiple").val().toBoolean(),
						                width:40
				          			  },
				          			{
				          				field : 'materialClassName',
										title : '分类',
										width : 80
				          			},
				          			{
										field : 'code',
										title : '材料编号',
										width : 120
									},
									{
										field : 'name',
										title : '材料名称',
										width : 120
									},
									{
										field : 'weight',
										title : '克重',
										width : 50
									},
									{
										field : 'stockUnitName',
										title : '单位',
										width : 50
									},
									{
										field : 'valuationUnitName',
										title : '计价单位',
										width : 60
									},
									{
										field : 'lastPurchPrice',
										title : '最近采购价',
										width : 90
									}
									],
							onDblClickRow : function(row) {
							  	selectRow(row);
								
								
							},
							onPageChange : function(number,size){
							    if(size == 20){
							        setTimeout(function(){
							            if($('#bootTable tbody').find('tr').length > 10){
							                $('.fixed-table-header').css('margin-right','17px');  
							            }
							        },1000);
							       
							    }	
							    else if(size == 50){
							        setTimeout(function(){
							            if($('#bootTable tbody').find('tr').length > 10){
							                $('.fixed-table-header').css('margin-right','17px');
							            }   
							        },2000);
							       
							    }	
							}
													
						});
	});
	
	//请求参数
	function queryParams(params) {
		params['materialClassId'] = $("#materialClassId").val();
		params['materialName'] = $("#materialName").val();
	//	console.log(params)
		return params;
	}
	//ajax结果
	function responseHandler(res) {
		return {
			rows : res.result,
			total : res.count
		};
	}
	function selectRow(row){

		//console.log(row)
		if($("#multiple").val().toBoolean())
		{//多选,则双击事件无效
		 	parent.getCallInfo_materialArray([row]);
		}else
		{//单选，双击事件立即返回,处理返回index事件
			parent.getCallInfo_material(row);
			Helper.popup.close();
		}
		
	}
</script>
</head>
<body>
	<input type="hidden" id="materialClassId" value="" />
	<input type="hidden" id="multiple" value="${multiple }" />
	<div class="layer_container">
		<div class="cl layer_content">
			<!--分类选择-->
			<div class="tree">
				<ul>
					<a href="javascript:void(0);" _id="" name="materialClassId">全部</a>
					<c:forEach items="${fns:basicList('MATERIALCLASS') }" var="item">
						<a href="javascript:void(0);" _id="${item.id }" name="materialClassId">${item.name }</a>
					</c:forEach>
				</ul>
			</div>
			<!--表格容器左START-->
			<div class="layer_table_container wrap_width_1">
				<div class="cl layer_top">
					<div class="row-dd top_bar">
						<label class="form-label label_ui"></label>
						<span class="ui-combo-wrap" class="form_text">
							<input type="text" class="input-txt input-txt_9" id="materialName" name="materialName" placeholder="材料名称" />
						</span>
						<div class="layer_btns">
							<button class="nav_btn table_nav_btn search_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查找
							</button>
							<button type="button" class="nav_btn table_nav_btn" id="btn_ok" />
							确认
							</button>
							<button type="button" class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
							<button type="button" class="nav_btn table_nav_btn add_btn" id="btn_add">
								&nbsp;
								<i class="fa fa-plus-square"></i>
								新增&nbsp;
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

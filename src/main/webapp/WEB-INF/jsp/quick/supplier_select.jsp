<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>

<meta charset="UTF-8">
<title>选择供应商</title>
<script type="text/javascript">
	$(function() {
		$(".tree").on("click","a[name='supplierClassId']",function() {
			$(".tree a[name='supplierClassId']").css("color","");
			$(this).css("color","red");
			var id = $(this).attr("_id");
		    $("#supplierClassId").val(id);
			$("#bootTable").bootstrapTable("refreshOptions",{pageNumber:1});
		});
		
		//查询
		$("#btn_search").click(function(){
			$("#bootTable").bootstrapTable("refreshOptions",{pageNumber:1});
		});
		//不能多选则隐藏确认按钮
		if(!$("#multiple").val().toBoolean()){
			$("#btn_ok").hide();
		}
		//选择确认
		$("#btn_ok").click(function(){
		  	if($("#multiple").val().toBoolean())
		  	{//多选,则双击事件
				parent.getCallInfo_supplierArray($("#bootTable").bootstrapTable('getAllSelections'));
			}
			Helper.popup.close();
		});
		
		$("#btn_cancel").click(function(){
			Helper.popup.close();
		});
		$("#btn_add").click(function(){
		    var add_url= Helper.basePath + "/quick/supplier_add";
		    if(Helper.isNotEmpty($("#supplierType").val()))
		    {
		        add_url=add_url+"?defaultSupplierType="+$("#supplierType").val();
		    }
			Helper.popup.show('快速添加供应商信息',add_url, '530','300');
		});
	//	console.log($("#multiple").val().toBoolean());
		//订单详情table
		$("#bootTable")
				.bootstrapTable(
						{
							url : Helper.basePath
									+ "/quick/supplier_list",
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
							columns : [{
						                field: 'state',
						                checkbox: true,
						                visible: $("#multiple").val().toBoolean(),
						                width:60
				          			  },
				          			  {
										field : 'supplierClassName',
										title : '供应商分类',
										width : 80
									},
				          			  {
										field : 'code',
										title : '供应商编号',
										width : 100
									},
									{
										field : 'name',
										title : '供应商名称',
										width : 180
									},
									{
										field : 'userName',
										title : '联系人',
										width : 60,
										formatter : function(value, row, index) {
										  if(Helper.isNotEmpty(row.defaultAddress)){
										    return row.defaultAddress.userName;
										  }
										  return "";
										}
									},
									{
										field : 'mobile',
										title : '手机',
										width : 80,
										formatter : function(value, row, index) {
										  if(Helper.isNotEmpty(row.defaultAddress)){
										    return row.defaultAddress.mobile;
										  }
										  return "";
										}
									},
									{
										field : 'employeeName',
										title : '采购人员',
										width : 60
									},
									{
										field : 'advanceMoney',
										title : '预付款',
										visible: false	
									},
									{
										field : 'isBegin',
										title : '期初客户',
										visible: false	
									}
									],
							onDblClickRow : function(row) {
								//双击选中事件	
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
	
	function selectRow(row)
	{
		if($("#multiple").val().toBoolean())
		{//多选,则双击事件
		 	parent.getCallInfo_supplierArray([row]);
		}else
		{//单选，双击事件立即返回,处理返回index事件
			parent.getCallInfo_supplier(row);
			Helper.popup.close();
		}
	}
	//请求参数
	function queryParams(params) {
		params['supplierClassId'] = $("#supplierClassId").val();
		params['supplierName'] = $("#supplierName").val();
		params['supplierType'] = $("#supplierType").val()==""?null:$("#supplierType").val();
		params['isBegin'] = $("#isBegin").val()==""?null:$("#isBegin").val();
		return params;
	}
	//ajax结果
	function responseHandler(res) {
		return {
			rows : res.result,
			total : res.count
		};
	}
</script>
</head>
<body>
	<input type="hidden" id="multiple" value="${multiple }">
	<input type="hidden" id="supplierClassId" value="" />
	<input type="hidden" id="supplierType" value="${supplierType }" />
	<input type="hidden" id="isBegin" value="${isBegin }">
	<div class="layer_container">
		<div class="cl layer_content">
			<!--分类选择-->
			<div class="tree">
				<ul>
					<a href="javascript:void(0);" _id="" name="supplierClassId">全部</a>
					<c:forEach items="${fns:basicList('SUPPLIERCLASS') }" var="item">
						<a href="javascript:void(0);" _id="${item.id }" name="supplierClassId">${item.name }</a>
					</c:forEach>
				</ul>
			</div>
			<!--表格容器左START-->
			<div class="layer_table_container wrap_width_1">
				<div class="cl layer_top">
					<div class="row-dd top_bar">
						<label class="form-label label_ui"></label>
						<span class="ui-combo-wrap" class="form_text">
							<input type="text" class="input-txt input-txt_9" id="supplierName" placeholder="供应商名称" name="supplierName" />
						</span>
						<div class="layer_btns">
							<button class="nav_btn table_nav_btn search_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查找
							</button>
							<button type="button" class="nav_btn table_nav_btn" id="btn_ok">确认</button>
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

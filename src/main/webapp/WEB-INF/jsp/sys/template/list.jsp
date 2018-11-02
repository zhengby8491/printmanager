<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>

<title>模板列表</title>
<script type="text/javascript">
	$(function() {
		
		//查询，刷新table
		$("#btn_search").click(function() {
			$("#bootTable").bootstrapTable("refreshOptions",{pageNumber:1});
		});
		//订单详情table
		$("#bootTable")
				.bootstrapTable(
						{
							url : Helper.basePath + "/template/query",
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
							//resizable : true, //是否启用列拖动
							showColumns : true, //是否显示所有的列
							minimumCountColumns : 2, //最少允许的列数
							striped : true, // 是否显示行间隔色
							cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
							sortable : false, // 是否启用排序
							clickToSelect : true, // 是否启用点击选中行
							height : 430, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
							cardView : false, // 是否显示详细视图
							detailView : false, // 是否显示父子表

							showExport : false,//是否显示导出按钮
							//exportDataType: 'all',
							//exportTypes : [ 'csv', 'txt', 'excel', 'doc' ],

							cookie : true,//是否启用COOKIE
 							cookiesEnabled:['bs.table.columns'],//默认会记住很多属性，这里控制只记住列选择属性
							cookieIdTable : "print_template_list",//必须制定唯一的表格cookieID

							uniqueId : "id",//定义列表唯一键
							columns : [
									{
										field : 'companyName',
										title : '公司名称',
										width : 140
									},
									{
										field : 'isFormal',
										title : '正式客户',
										width : 70
									},
									{
										field : 'tel',
										title : '联系电话',
										width : 70										
									},
									{
										field : 'billTypeText',
										title : '模块名称',
										width : 140
									},
									{
										field : 'title',
										title : '模板名称',
										width : 140
									},
									{
										field : 'isPublicText',
										title : '是否公共模板',
										width : 70
									},
									{
										field : 'memo',
										title : '备注',
										width : 120,
										'class' : 'memoView',
										visible: false										
									},
									{
										field : 'operator',
										title : '操作',
										width : 70,
										formatter : function(value, row, index) {
											if(!Helper.isNotEmpty(row.id)){
												return;
											}
										    var operator = '<span class="table_operator">';
											        operator += '<a title="编辑" href="javascript:;" onclick="edit('
													+ row.id
													+ ',\''+row.billType+'\')" style="padding: 0 8px; color: green"><i class="fa fa-pencil"></i></a>'; 
											        operator += '<a title="删除" href="javascript:;" onclick="del(this,'
													+ row.id
													+ ')" style="padding: 0 8px; color: red"><i class="fa fa-trash-o"></i></a>'; 
	                   						 operator+='</span>';
											return operator;
										}
									} ],
							onDblClickRow : function(row) {
								//双击选中事件
		  						view(row.id);
							},
							onClickRow : function(row,$element){
							    $element.addClass("tr_active").siblings().removeClass("tr_active");
 							},
 							onLoadSuccess:function (data){
 							},
 							onColumnSwitch : function(field,checked)
 							{
 								// 在使用筛选增加或减少列时重置表格列拖动效果
 								bootstrapTable_ColDrag($("#bootTable"));
							} 
						});
			$("#bootTable").on('load-success.bs.table column-switch.bs.table page-change.bs.table search.bs.table', function () {
		        /* 表格工具栏 */
		        if($(".glyphicon-th").next().html() == ''){
		            $(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
		        }	
	    	});


	});
	function responseHandler(res) {
		return {
			rows : res.result,
			total : res.count
		};
	}
	function queryParams(params) {
		params['printModleName'] = $("#printModleName").val();
		params['tel']=$("#tel").val();
		params['companyName'] = $("#companyName").val();
		params['templateTitle']=$("#title").val();
		params['title']=$("#title").val();
		params['publish']=$("#isPublic").prop("checked")?'YES':'NO';
		//console.log(params);
		return params;
	}
	
	//编辑
	function edit(id,billType){
		if(!Helper.isNotEmpty(id)){
			return;
		}
	  	var url = Helper.basePath + '/sys/template/edit?id=' + id + '&title='+billType;
		var title = "编辑模板";
		admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
	}
	 
	
	/* 删除 */
	function del(obj, id) {
		Helper.message.confirm('确认要删除吗？', function(index) {
		  $.ajax({
   	                cache: true,
   	                type: "POST",
   	                url:Helper.basePath + '/template/del?id='+id,		              
   	                async: false,
   					dataType:"json", 
   	                error: function(request) {
   	                    layer.alert("Connection error");
   	                },
   	                success: function(data) {
   						if(data.success){
   							$("#bootTable").bootstrapTable("refreshOptions",{pageNumber:1});
   							layer.closeAll('dialog');
   						}else{
   							  Helper.message.warn(data.message);
   						}
   	                }
   	            });
		});
	}
	
	/*新增*/
	function create() {
	    var url = Helper.basePath + '/sys/template/create';
		var title = "新增模板";
		admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
	}
</script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl topPath"></div>
			<!--查询表单START-->
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">

						<dd class="row-dd">
							<label class="form-label label_ui">公司名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_7" id="companyName" name="companyName" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">电话：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_3" name="tel" id="tel" />
							</span>
						</dd>

						<dd class="row-dd">
							<label class="form-label label_ui">模块名称：</label>
							<span class="ui-combo-wrap form_text">
								<phtml:list name="printModleName" textProperty="text" cssClass="input-txt input-txt_3 hy_select2" type="com.huayin.printmanager.persist.enumerate.PrintModleName"></phtml:list>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">模板名称：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_7" name="title" id="title" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">是否公共：</label>
							<span class="ui-combo-wrap form_text">
								<input type="checkbox" id="isPublic" />
							</span>
						</dd>
						<dd class="row-dd">
							<button type="button" class="nav_btn table_nav_btn search_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查询
							</button>
						</dd>
					</dl>
				</div>
			</div>
			<!-- 按钮栏Start -->
			<div class="btn-bar" style="margin-bottom: 0">
				<shiro:hasPermission name="sys:template:add">
					<span>
						<a href="javascript:;" class="nav_btn table_nav_btn" onclick="create()">
							<i class="fa fa-plus-square"></i>
							新增模板
						</a>
					</span>
				</shiro:hasPermission>

			</div>
			<!--按钮栏End-->
			<!--表格Start-->
			<div class="boot-mar">
				<table class="border-table resizable" id="bootTable">
				</table>
			</div>
			<!--表格End-->
		</div>
	</div>
</body>
</html>
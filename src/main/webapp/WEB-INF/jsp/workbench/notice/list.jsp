<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<meta charset="UTF-8">
<title>公告</title>
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<script type="text/javascript">	
		$(function(){
			//查询，刷新table
			$("#btn_search").click(function() {
				$("#detailList").bootstrapTable("refreshOptions",{pageNumber:1});
			});
			
			$("#detailList").bootstrapTable({
				url : Helper.basePath+ "/workbench/notice/ajaxList",
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
				
// 				resizable : true, //是否启用列拖动
				showColumns : true, //是否显示所有的列
				minimumCountColumns : 2, //最少允许的列数
				striped : true, // 是否显示行间隔色
				cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
				sortable : false, // 是否启用排序
				clickToSelect : true, // 是否启用点击选中行
				height : 430, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
				cardView : false, // 是否显示详细视图
				detailView : false, // 是否显示父子表
		
				showExport : true,//是否显示导出按钮
				//exportDataType: 'all',
				exportTypes : [ 'csv', 'txt', 'excel', 'doc' ],
		
				cookie : true,//是否启用COOKIE
				cookiesEnabled:['bs.table.columns'],//默认会记住很多属性，这里控制只记住列选择属性
				cookieIdTable : "print_workbench_notice_list",//必须制定唯一的表格cookieID
		
				uniqueId : "id",//定义列表唯一键
				
				columns :[
						{
							field : 'index',
							title : '序号',
							width : 40,
							formatter : function(value, row, index) {
								return index + 1;										
							}
						},
						{
							field : 'createName',
							title : '创建人',
							width : 100
						},
						{
							field : 'createTime',
							title : '创建时间',
							width : 100,
							formatter : function(value, row, index) {
							  if(value!=null){
							  	  return new Date(value).format("yyyy-MM-dd");
							  }else{
							  	  return "";
						  	}
							}
						},
						{
							field : 'content',
							title : '公告内容',
							width : 200
						},
						{
							field : 'operator',
							title : '操作',
							width : 120,
							formatter : function(value, row, index) {
								if(!Helper.isNotEmpty(row.id)){
									return;
								}
								var operator = "";
								  	if(Helper.basic.hasPermission('workbench:notice:edit')){
										operator += '<a title="编辑" href="javascript:;" onclick="notice_edit('
												+ row.id
												+ ')" style="padding: 0 8px; color: green"><i class="fa fa-pencil"></i></a>';
								  	}
								  	if(Helper.basic.hasPermission('workbench:notice:del')){
										operator += '<a title="删除" href="javascript:;" onclick="notice_del('
												+ row.id
												+ ')" style="padding: 0 8px; color: red"><i class="fa fa-trash-o"></i></a></span>';
								  	}
								
								return operator;
							}
						}
						
				          ],
				onLoadSuccess : function() {
					/* 表格工具栏 */
		            if($(".glyphicon-th").next().html() == '') {
		                $(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
		                if(!Helper.basic.hasPermission('sys:systemnotice:export')){
		                  	 $(".export.btn-group").remove();
		                   }
		                $(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
		            }
				},
				onLoadError : function() {
					// alert("数据加载异常");
				},
				onClickRow : function(row,$element){
				    $element.addClass("tr_active").siblings().removeClass("tr_active");
				},
				onColumnSwitch : function(field, checked)
				{	
					// 在使用筛选增加或减少列时重置表格列拖动效果
					bootstrapTable_ColDrag($("#detailList"));
				}
			});	
	    
			$("#detailList").on('load-success.bs.table', function()
			{
				// bootstrap_table加载完后触发列拖动效果
				bootstrapTable_ColDrag($("#detailList"));
			});
		})
		function responseHandler(res) {
			return {
				rows : res.result,
				total : res.count
			};
		}
		function queryParams(params) {
	
			params['dateMin'] = $("#dateMin").val();
			params['dateMax'] = $("#dateMax").val();
			return params;
		}
		
		function add(){
			Helper.popup.show('发布公告', Helper.basePath
					+ '/workbench/notice/create', '352', '210');
		}
		function notice_edit(id){
		    Helper.popup.show('修改公告', Helper.basePath
		          					+ '/workbench/notice/edit/'+id, '352', '210');
		}		
		function notice_del(id){
		  Helper.message.confirm('确认要删除吗？', function(index) {
			$.ajax({
	                cache: true,
	                type: "POST",
	                url:Helper.basePath + '/workbench/notice/del/'+id,		              
	                async: false,
					dataType:"json", 
	                error: function(request) {
	                    Helper.message.warn("Connection error");
	                },
	                success: function(data) {
						if(data.success){
							layer.closeAll()
							save_success();
						}else{
							Helper.message.warn("失败");
						}
	                }
	            });
		  });
		}
		function save_success(){
		    $("#btn_search").trigger("click");
		}
	</script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl topPath"></div>
			<!--头部END-->
			<!--查询表单START-->
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">
						<dd class="row-dd">
							<sys:dateConfine label="单据日期" initDate="true" />
						</dd>
						<dd class="row-dd">
							<button class="nav_btn table_nav_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查询
							</button>
						</dd>
					</dl>
				</div>
			</div>
			<div class="btn-bar" style="margin-bottom: 0">
				<shiro:hasPermission name="workbench:notice:create">
					<span>
						<a href="javascript:add()" class="nav_btn table_nav_btn">
							<i class="fa fa-plus-square"></i>
							发布公告
						</a>
					</span>
				</shiro:hasPermission>
			</div>
			<!--查询表单END-->
			<!--表格部分Start-->
			<div class="boot-mar">
				<table class="border-table resizable" id="detailList">
				</table>
			</div>
			<!--表格部分End-->
		</div>
	</div>
</body>
</html>
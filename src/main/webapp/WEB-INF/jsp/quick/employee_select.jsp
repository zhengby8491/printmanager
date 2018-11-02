<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<meta charset="UTF-8">
<title>选择员工</title>
<script type="text/javascript">
	$(function()
	{
		$(".tree").on("click", "a[name='customerClassId']", function()
		{
			$(".tree a[name='customerClassId']").css("color", "");
			$(this).css("color", "red");
			var id = $(this).attr("_id");
			$("#customerClassId").val(id);
			$("#bootTable").bootstrapTable("refreshOptions", {
				pageNumber : 1
			});
		});
		//选择确认
		$("#btn_ok").click(function()
		{
			if ($("#multiple").val().toBoolean())
			{//多选,则双击事件
				parent.getCallInfo_customerArray($("#bootTable").bootstrapTable('getAllSelections'));
			}
			Helper.popup.close();
		});
		//新增
		$("#btn_create").click(function()
		{
			shotCutWindow("EMPLOYEE");
		});
		//查询
		$("#btn_search").click(function()
		{
			$("#bootTable").bootstrapTable("refreshOptions", {
				pageNumber : 1
			});
		});

		//员工列表
		$("#bootTable").bootstrapTable({
			url : Helper.basePath + "/basic/employee/listAjax",
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
			//showColumns : false, //是否显示所有的列
			minimumCountColumns : 2, //最少允许的列数
			striped : true, // 是否显示行间隔色
			cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			sortable : false, // 是否启用排序
			clickToSelect : true, // 是否启用点击选中行
			height : 360, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			cardView : false, // 是否显示详细视图
			detailView : false, // 是否显示父子表
			showExport : false,//是否显示导出按钮
			//exportDataType: 'all',
			exportTypes : [ 'csv', 'txt', 'excel', 'doc' ],
			cookie : true,//是否启用COOKIE
			cookiesEnabled : [ 'bs.table.columns' ],//默认会记住很多属性，这里控制只记住列选择属性
			cookieIdTable : "print_basic_employee",//必须制定唯一的表格cookieID
			uniqueId : "id",//定义列表唯一键
			columns : [ {
				field : 'index',
				title : '序号',
				width : 40,
				formatter : function(value, row, index)
				{
					//										console.log($(this).pageSixze)
					return index + 1;
				}
			}, {
				field : 'code',
				title : '员工工号',
				width : 140
			}, {
				field : 'name',
				title : '员工名称',
				width : 140
			}, {
				field : 'departmentName',
				title : '部门',
				width : 60
			}, {
				field : 'positionName',
				title : '职位',
				width : 120
			}, {
				field : 'mobile',
				title : '电话',
				width : 80
			}, {
				field : 'sexTypeText',
				title : '性别',
				width : 60
			}, {
				field : 'email',
				title : '邮箱',
				width : 80,
				visible : false
			}, {
				field : 'entryTime',
				title : '入职时间',
				width : 80,
				visible : false,
				formatter : function(value, row, index)
				{
					if (Helper.isNotEmpty(value))
					{
						return new Date(value).format("yyyy-MM-dd");
					}
				}
			}, {
				field : 'departureTime',
				title : '离职时间',
				width : 80,
				visible : false,
				formatter : function(value, row, index)
				{
					if (Helper.isNotEmpty(value))
					{
						return new Date(value).format("yyyy-MM-dd");
					}
				}
			}, {
				field : 'createName',
				title : '创建人',
				visible : false,
				width : 80,
				visible : false
			}, {
				field : 'createTime',
				title : '创建时间',
				visible : false,
				width : 80,
				visible : false,
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
				width : 60,
				visible : false
			}, {
				field : 'updateTime',
				title : '修改日期',
				visible : false,
				width : 80,
				visible : false,
				formatter : function(value, row, index)
				{
					if (Helper.isNotEmpty(value))
					{
						return new Date(value).format("yyyy-MM-dd");
					}
				}
			}, {
				field : 'memo',
				title : '备注',
				'class' : 'memoView',
				width : 200,
				visible : false
			}, {
				field : 'stateText',
				title : '状态',
				width : 80,
				visible : false,
				cellStyle : function(value, row, index, field)
				{
					console.log(value + "=" + row + "=" + index + "=" + field)
					if (value == '休假')
					{
						return {
							css : {
								"color" : "#f00"
							}
						};
					} else if (value == '离职')
					{
						return {
							css : {
								"color" : "#ccc"
							}
						};
					} else if (value == '正常')
					{
						return {
							css : {
								"color" : "#080"
							}
						};
					}
				}
			}, {
				field : 'operator',
				title : '操作',
				width : 80,
				visible : false,
				formatter : function(value, row, index)
				{
					var operator = "";
					if (Helper.basic.hasPermission("basic:employee:edit"))
					{
						operator += '<a title="编辑" href="javascript:;"onclick="employee_edit(' + row.id + ')" style="margin-right:8px;"><i class="fa fa-pencil"></i></a>';

						if (row.state == 'NORMAL')
						{

							operator += '<a title="休假" href="javascript:;" onClick="employee_holiday(this,' + row.id + ')" style="margin-right:8px;" ><i class="fa fa-minus-square-o"></i></a>';
						}
						if (row.state == 'HOLIDAY')
						{

							operator += '<a title="离职" href="javascript:;" onClick="employee_stop(this,' + row.id + ')" style="margin-right:8px;" ><i class="fa fa-minus-square-o"></i></a>';
						}
						if (row.state == 'LEAVEJOB')
						{

							operator += '<a title="正常"  href="javascript:;" onClick="employee_start(this,' + row.id + ')" style="margin-right:8px;"><i class="fa fa-check-square-o"></i></a>';
						}
					}
					if (Helper.basic.hasPermission("basic:employee:del"))
					{
						operator += '<a title="删除" href="javascript:;" onclick="employee_del(this,' + row.id + ')" style="margin-right:8px; color: red"><i class="fa fa-trash-o"></i></a>';
					}
					return operator;
				}
			} ],
			onDblClickRow : function(row)
			{
				selectRow(row);
			},
			onClickRow : function(row, $element)
			{
				$element.addClass("tr_active").siblings().removeClass("tr_active");
			}
		});

	});

	function selectRow(row)
	{
		//console.log(row)
		if ($("#multiple").val().toBoolean())
		{//多选,则双击事件无效
			parent.getCallInfo_customerArray([ row ]);
		} else
		{//单选，双击事件立即返回,处理返回index事件
			parent.getCallInfo_empoyee(row);
			Helper.popup.close();
		}
	}
	//请求参数
	function queryParams(params)
	{
		params['customerClassId'] = $("#customerClassId").val();
		params['employeeName'] = $("#employeeName").val();
		params['isBegin'] = $("#isBegin").val() == "" ? null : $("#isBegin").val();
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
	<input type="hidden" id="customerClassId" value="" />
	<input type="hidden" id="isBegin" value="${isBegin}" />
	<div class="layer_container">
		<div class="cl layer_content">
			<!--分类选择-->

			<!--表格容器左START-->
			<div class="layer_table_container" style="left: 10px">
				<div class="cl layer_top">
					<div class="row-dd top_bar">
						<label class="form-label label_ui"></label>
						<span class="ui-combo-wrap" class="form_text">
							<input type="text" class="input-txt input-txt_9" id="employeeName" placeholder="员工名称" name="name" />
						</span>
						<div class="layer_btns" style="float: left">
							<button class="nav_btn table_nav_btn search_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查找
							</button>
						</div>
						<button type="button" style="float: right" class="nav_btn table_nav_btn" id="btn_create">
							<i class="fa fa-plus-square"></i>
							<span style="margin-left: 4px;">新增</span>
						</button>
					</div>
				</div>
				<!-- 表格 -->
				<div class="table-wrap" style="height: 390px">
					<table class="layer_table" id="bootTable">

					</table>
				</div>
			</div>
			<!--表格容器左END-->
		</div>
	</div>
</body>
</html>
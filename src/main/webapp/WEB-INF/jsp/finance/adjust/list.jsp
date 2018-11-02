<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>

<title>财务调整列表</title>
<script type="text/javascript">
	$(function()
	{
		/* 查询审核状态 */
		$("input[name='auditFlag']").change(function()
		{
			$("#bootTable").bootstrapTable("refresh");
		});
		//查询，刷新table
		$("#btn_search").click(function()
		{
			$("#bootTable").bootstrapTable("refresh");
		});

		//订单详情table
		$("#bootTable").bootstrapTable({
			url : Helper.basePath + "/finance/adjust/ajaxList",
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
			height : 430, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			cardView : false, // 是否显示详细视图
			detailView : false, // 是否显示父子表

			showExport : true,//是否显示导出按钮
			//exportDataType: 'all',
			exportTypes : [ 'csv', 'txt', 'excel', 'doc' ],

			cookie : true,//是否启用COOKIE
			cookieIdTable : "print_finance_adjust_master",//必须制定唯一的表格cookieID

			uniqueId : "id",//定义列表唯一键
			columns : [ {
				field : 'index',
				title : '序号',
				width : 40,
				formatter : function(value, row, index)
				{
					return index + 1;
				}
			}, {
				field : 'isCheck',
				title : '单据状态',
				width : 70,
				formatter : function(value, row, index)
				{
					return value == 'NO' ? '未审核' : '已审核';
				},
				cellStyle : function(value, row, index, field)
				{
					if (value == '未审核')
					{
						return {
							css : {
								"color" : "#f00"
							}
						};
					} else
					{
						return {
							css : {
								"color" : "#080"
							}
						};
					}
				}
			}, {
				field : 'billTypeText',
				title : '单据类型',
				width : 80
			}, {
				field : 'adjustTime',
				title : '调整日期',
				width : 80,
				formatter : function(value, row, index)
				{
					if (value)
					{
						return new Date(value).format("yyyy-MM-dd");
					}
				}
			}, {
				field : 'billNo',
				title : '调整单号',
				width : 140,
				formatter : function (value, row, index)
				{
					return idTransToUrl(row.id, value);
				}
			}, {
				field : 'employeeName',
				title : '调整人',
				width : 80
			}, {
				field : 'createName',
				title : '制单人',
				width : 80
			}, {
				field : 'createTime',
				title : '制单日期',
				width : 80,
				formatter : function(value, row, index)
				{
					if (value)
					{
						return new Date(value).format("yyyy-MM-dd");
					}
				}
			}, {
				field : 'checkUserName',
				title : '审核人',
				width : 80
			}, {
				field : 'checkTime',
				title : '审核日期',
				width : 80,
				formatter : function(value, row, index)
				{
					if (value)
					{
						return new Date(value).format("yyyy-MM-dd");
					}
				}
			}, {
				field : 'memo',
				title : '备注',
				'class' : 'memoView',
				width : 200
			}, {
				field : 'operator',
				title : '操作',
				width : 120,
				formatter : function(value, row, index)
				{
					var operator = "";
					if (Helper.basic.hasPermission('finance:adjust:list'))
					{
						operator += '<span class="table_operator"><a title="查看" href="javascript:;" onclick="adjust_view(' + row.id + ')" style="padding: 0 8px; color: green"><i class="fa fa-search"></i></a></span>';
					}

					if (Helper.isNotEmpty(row.isCheck) && row.isCheck == 'NO')
					{
						operator = "";
						if (Helper.basic.hasPermission('finance:adjust:list'))
						{
							operator += '<span class="table_operator"><a title="查看" href="javascript:;" onclick="adjust_view(' + row.id + ')" style="padding: 0 8px; color: green"><i class="fa fa-search"></i></a>';
						}
						if (Helper.basic.hasPermission('finance:adjust:edit'))
						{
							operator += '<a title="编辑" href="javascript:;" onclick="adjust_edit(' + row.id + ')" style="padding: 0 8px; color: green"><i class="fa fa-pencil"></i></a>';
						}
						if (Helper.basic.hasPermission('finance:adjust:del'))
						{
							operator += '<a title="删除" href="javascript:;" onclick="adjust_del(this,' + row.id + ')" style="padding: 0 8px; color: red"><i class="fa fa-trash-o"></i></a></span>';
						}
					}
					return operator;
				}
			} ],
			onDblClickRow : function(row)
			{
				adjust_view(row.id);
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
		/* 表格工具栏 */
		$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
		if (!Helper.basic.hasPermission('stock:product_adjust:export'))
		{
			$(".export.btn-group").remove();
		}
		$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
	});
	function responseHandler(res)
	{
		return {
			rows : res.result,
			total : res.count
		};
	}
	function queryParams(params)
	{
		params['dateMin'] = $("#dateMin").val();
		params['dateMax'] = $("#dateMax").val();
		params['billNo'] = $("#billNo").val();
		params['employeeId'] = $("#employeeId").val();
		params['auditFlag'] = $("input[name='auditFlag']:checked").val() == "-1" ? null : $("input[name='auditFlag']:checked").val();
		return params;
	}
	/* 编辑 */
	function adjust_edit(id)
	{
		var url = Helper.basePath + '/finance/adjust/edit/' + id;
		var title = "财务调整单";
		admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
	}

	//查看
	function adjust_view(id)
	{
		var url = Helper.basePath + '/finance/adjust/view/' + id;
		var title = "财务调整单";
		admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
	}

	/* 删除 */
	function adjust_del(obj, id)
	{
		Helper.message.confirm('确认要删除吗？', function(index)
		{
			Helper.post(Helper.basePath + '/finance/adjust/delete/' + id, function(data)
			{
				if (data.success)
				{
					$("#bootTable").bootstrapTable("removeByUniqueId", id);
					$("#bootTable").bootstrapTable("refresh");
					Helper.message.suc('已删除!');
				} else
				{
					Helper.message.warn('删除失败：' + data.message);
				}
			});
		});
	}
	/*新增*/
	function toCreate()
	{
		var url = Helper.basePath + '/finance/adjust/create';
		var title = "财务调整单";
		admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
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
							<sys:dateConfine label="调整日期" />
						</dd>

						<dd class="row-dd">
							<label class="form-label label_ui label_1">调 整 人：</label>
							<span class="ui-combo-wrap wrap-width">
								<phtml:list items="${fns:basicList('EMPLOYEE')}" valueProperty="id" defaultValue="" defaultOption="请选择" name="employeeId" textProperty="name" cssClass="input-txt input-txt_1 hy_select2"></phtml:list>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">调整单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_3s" name="billNo" id="billNo" />
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
				<div>
					<div class="radio-box">
						<label>
							<input type="radio" value="-1" name="auditFlag" checked="checked" />
							全部
						</label>
						<label>
							<input type="radio" value="YES" name="auditFlag" />
							已审核
						</label>
						<label>
							<input type="radio" value="NO" name="auditFlag" />
							未审核
						</label>
					</div>
				</div>
			</div>

			<!-- 按钮栏Start -->
			<div class="btn-bar" id="toolbar" style="margin-bottom: 0">
				<shiro:hasPermission name="finance:adjust:create">
					<a href="javascript:;" class="nav_btn table_nav_btn" onclick="toCreate()">
						<i class="fa fa-plus-square"></i>
						新增财务调整
					</a>
				</shiro:hasPermission>
			</div>
			<!--按钮栏End-->
			<!--表格部分Start-->
			<div class="boot-mar">
				<table class="border-table resizable" id="bootTable">
				</table>
			</div>
			<!--表格部分End-->
		</div>
	</div>
</body>
</html>
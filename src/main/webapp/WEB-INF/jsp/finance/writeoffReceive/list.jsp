<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>

<title>收款核销单列表</title>
<script type="text/javascript">
	$(function()
	{
		/* 选择客户  */
		$("#customer_quick_select").click(function()
		{
			Helper.popup.show('选择客户 ', Helper.basePath + '/quick/customer_select?multiple=false', '800', '490');
		});
		/* 查询审核状态 */
		$("input[name='auditFlag'],input[name='isCancel']").change(function()
		{
			$("#bootTable").bootstrapTable("refreshOptions", {
				pageNumber : 1
			});
		});
		//查询，刷新table
		$("#btn_search").click(function()
		{
			$("#bootTable").bootstrapTable("refreshOptions", {
				pageNumber : 1
			});
		});
		//订单详情table
		$("#bootTable").bootstrapTable({
			url : Helper.basePath + "/finance/writeoffReceive/ajaxList",
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
			cookiesEnabled : [ 'bs.table.columns' ],//默认会记住很多属性，这里控制只记住列选择属性
			cookieIdTable : "print_finance_writeoffReceive_master",//必须制定唯一的表格cookieID

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
					if (row.id == null)
					{
						return;
					}
					return (value == 'NO' ? '未审核' : '已审核') + (row.isCancel == 'YES' ? '(作废)' : '');
				},
				cellStyle : function(value, row, index, field)
				{
					if (row.isCheck == 'NO')
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
				field : 'billTime',
				title : '核销日期',
				width : 120,
				formatter : function(value, row, index)
				{
					if (value)
					{
						return new Date(value).format("yyyy-MM-dd");
					}
				}
			}, {
				field : 'billNo',
				title : '核销单号',
				width : 120,
				formatter : function (value, row, index)
				{
					return idTransToUrl(row.id, value);
				}
			}, {
				field : 'customerName',
				title : '客户',
				width : 180
			}, {
				field : 'employeeName',
				title : '核销人',
				width : 80
			}, {
				field : 'money',
				title : '核销金额',
				width : 100
			}, {
				field : 'discount',
				title : '折扣金额',
				width : 100
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
				'class' : 'memoView',
				title : '备注',
				width : 200
			}, {
				field : 'operator',
				title : '操作',
				width : 120,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return;
					}
					var operator = "";
					if (Helper.basic.hasPermission('finance:writeoffReceive:list'))
					{
						operator += '<a title="查看" href="javascript:;" onclick="receive_view(' + row.id + ')" style="padding: 0 8px; color: green"><i class="fa fa-search"></i></a>';
					}
					return operator;
				}
			} ],
			onLoadSuccess : function()
			{
				$("#bootTable tbody").find("tr:last").find("td:first").text("合计");
				var _tds = $("#bootTable tbody").find("tr:last").find("td");
				for (var index = 0; index < _tds.length; index++)
				{
					if (_tds.eq(index).text() == "-")
					{
						_tds.eq(index).text('');
					}
				}
			},
			onColumnSwitch : function(field, checked)
			{
				$("#bootTable tbody").find("tr:last").find("td:first").text("合计");
				var _tds = $("#bootTable tbody").find("tr:last").find("td");
				for (var index = 0; index < _tds.length; index++)
				{
					if (_tds.eq(index).text() == "-")
					{
						_tds.eq(index).text('');
					}
				}
				
				// 在使用筛选增加或减少列时重置表格列拖动效果
				bootstrapTable_ColDrag($("#bootTable"));
			},
			onDblClickRow : function(row)
			{
				if (row.id == null)
				{
					return;
				}
				receive_view(row.id);
			},
			onClickRow : function(row, $element)
			{
				$element.addClass("tr_active").siblings().removeClass("tr_active");
			}
		});
		/* 表格工具栏 */
		$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
		if (!Helper.basic.hasPermission('finance:writeoffReceive:export'))
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
		params['customerName'] = $("#customerName").val();
		params['auditFlag'] = $("input[name='auditFlag']:checked").val() == "-1" ? null : $("input[name='auditFlag']:checked").val();
		params['isCancel'] = $("input[name='isCancel']:checked").val() == undefined ? null : $("input[name='isCancel']:checked").val();
		return params;
	}
	/* 新增 */
	function receive_create(id)
	{
		var url = Helper.basePath + '/finance/writeoffReceive/create';
		var title = "收款核销单";
		admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
	}
	/* 查看 */
	function receive_view(id)
	{
		var url = Helper.basePath + '/finance/writeoffReceive/view/' + id;
		var title = "收款核销单";
		admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"))
	}

	// 获取客户 返回信息
	function getCallInfo_customer(obj)
	{
		$("#customerName").val(obj.name);
		$("#customerId").val(obj.id);
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
							<sys:dateConfine label="制单日期" />
						</dd>

						<dd class="row-dd">
							<label class="form-label label_ui">客户名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_7" id="customerName" name="customerName" />
								<input type="hidden" id="customerId" name="customerId" />
								<div class="select-btn" id="customer_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">核销单号：</label>
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
							<input type="radio" value="-1" name="auditFlag" ${auditflag?"":"checked" } />
							全部
						</label>
						<label>
							<input type="radio" value="YES" name="auditFlag" />
							已审核
						</label>
						<label>
							<input type="radio" value="NO" name="auditFlag" ${auditflag?"checked":"" } />
							未审核
						</label>
						<label>
							<input type="checkbox" value="YES" name="isCancel" />
							作废
						</label>
					</div>
				</div>
			</div>

			<!-- 按钮栏Start -->
			<div class="btn-bar" style="margin-bottom: 0">
				<shiro:hasPermission name="finance:writeoffReceive:create">
					<span>
						<a href="javascript:;" onclick="receive_create()" class="nav_btn table_nav_btn">
							<i class="fa fa-plus-square"></i>
							新增收款核销单
						</a>
					</span>
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
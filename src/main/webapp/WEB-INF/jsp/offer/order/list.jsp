<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>

<title>报价单列表</title>
<script type="text/javascript">
	$(function()
	{
		//查询，刷新table
		$("#btn_search").click(function()
		{
			$("#bootTable").bootstrapTable("refresh");
		});
		// 查询审核状态
		$("input[name='auditFlag']").change(function()
		{
			$("#bootTable").bootstrapTable("refreshOptions", {
				pageNumber : 1
			});
		});

		//订单详情table
		$("#bootTable").bootstrapTable({
			url : Helper.basePath + "/offer/ajaxList",
			method : "post",
			contentType : 'application/json', //设置请求头信息  
			dataType : "json",
			pagination : true, // 是否显示分页（*）
			sidePagination : 'server',//设置为服务器端分页
			pageList : [ 10, 20 ],
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
			cookieIdTable : "offer_list",//必须制定唯一的表格cookieID

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
					if (value == "NO")
					{
						return '未审核';
					}
					if (value == "YES")
					{
						return '已审核';
					}
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
				field : 'offerTypeText',
				title : '报价类型',
				width : 80
			}, {
				field : 'createDateTime',
				title : '报价日期',
				width : 60,
				formatter : function(value, row, index)
				{
					if (value)
					{
						return new Date(value).format("yyyy-MM-dd");
					}
				}
			}, {
				field : 'offerNo',
				title : '报价单号',
				width : 80
			}, {
				field : 'productName',
				title : '成品名称',
				width : 80
			},

			{
				field : 'amount',
				title : '数量',
				width : 60
			}, {
				field : 'printFee',
				title : '印刷费',
				width : 60
			}, {
				field : 'procedureFee',
				title : '工序费',
				width : 60
			}, {
				field : 'paperFee',
				title : '纸张费',
				width : 60
			}, {
				field : 'freightFee',
				title : '运费',
				width : 60
			}, {
				field : 'ohterFee',
				title : '其他费',
				width : 60
			}, {
				field : 'costMoney',
				title : '总费用',
				width : 60
			}, {
				field : 'createName',
				title : '报价人',
				width : 60
			}, {
				field : 'operator',
				title : '操作',
				width : 80,
				formatter : function(value, row, index)
				{
					var operator = '<span class="table_operator">';
					
					operator += '<a title="查看" href="javascript:;"onclick="view(' + row.id + ')" style="padding: 0 8px; color: green"><i class="fa fa-search"></i></a>';

					if (row.isCheck == "NO" && Helper.basic.hasPermission("offer:order:del"))
					{
						operator += '<a title="删除" href="javascript:;" onclick="del(this,' + row.id + ')" style="padding: 0 8px; color: green"><i class="fa fa-trash-o"></i></a>';
					}
					
					operator+='</span>';
					return operator;
				}
			} ],
			onDblClickRow : function(row)
			{
				view(row.id);
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
		if (!Helper.basic.hasPermission('offer:order:list:export'))
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
		params['name'] = $("#name").val().trim();
		params['billNo'] = $("#offerNo").val().trim();
		params['auditFlag'] = $("input[name='auditFlag']:checked").val() == "-1" ? null : $("input[name='auditFlag']:checked").val();
		return params;
	}
	/* 查看 */
	function view(id)
	{
		var url = Helper.basePath + '/offer/view/' + id;
		var title = "报价单";
		admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
	}

	/* 删除 */
	function del(obj, id)
	{
		Helper.message.confirm('确认要删除吗？', function(index)
		{
			Helper.post(Helper.basePath + '/offer/deleteOfferBean/' + id, function(data)
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
							<sys:dateConfine label="报价日期" />
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">成品名称：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_3s" name="name" id="name" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">报价单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_3s" name="offerNo" id="offerNo" />
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
						<c:if test="${auditflag!='true' }">
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
						</c:if>
						<c:if test="${auditflag=='true' }">
							<label>
								<input type="radio" value="-1" name="auditFlag" />
								全部
							</label>
							<label>
								<input type="radio" value="YES" name="auditFlag" />
								已审核
							</label>
							<label>
								<input type="radio" value="NO" name="auditFlag" checked="checked" />
								未审核
							</label>
						</c:if>
					</div>
				</div>
			</div>

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
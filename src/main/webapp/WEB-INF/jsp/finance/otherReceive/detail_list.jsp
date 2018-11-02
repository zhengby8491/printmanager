<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>其它收款单明细列表</title>
<script type="text/javascript">
	$(function()
	{
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
			url : Helper.basePath + "/finance/otherReceive/ajaxDetailList",
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
			cookieIdTable : "print_finance_other_rec_detail",//必须制定唯一的表格cookieID

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
				width : 100,
				formatter : function(value, row, index)
				{
					if (row.master.id == null)
					{
						return;
					}
					return (row.master.isCheck == 'NO' ? '未审核' : '已审核');
				},
				cellStyle : function(value, row, index, field)
				{
					if (row.master.isCheck == 'NO')
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
				field : 'createTime',
				title : '制单日期',
				width : 80,
				formatter : function(value, row, index)
				{
					if (row.master.id != null)
					{
						return new Date(row.master.createTime).format("yyyy-MM-dd");
					}
				}
			}, {
				field : 'billNo',
				title : '收款单号',
				width : 120,
				formatter : function(value, row, index)
				{
					if (row.master != null)
					{
						return idTransToUrl(row.master.id, row.master.billNo);
					} else
					{
						return "";
					}
				}
			}, {
				field : 'recCompany',
				title : '付款单位',
				width : 200,
				formatter : function(value, row, index)
				{
					if (row.master != null)
					{
						return row.master.recCompany
					} else
					{
						return "";
					}

				}
			}, {
				field : 'summary',
				title : '摘要',
				width : 400
			}, {
				field : 'money',
				title : '收款金额',
				width : 80
			}, {
				field : 'master_billTime',
				title : '收款日期',
				width : 180,
				formatter : function(value, row, index)
				{
					if (row.master.billTime)
					{
						return new Date(row.master.billTime).format("yyyy-MM-dd");
					}
				}
			}, {
				field : 'memo',
				title : '备注',
				'class' : 'memoView',
				width : 200
			} ],
			onLoadSuccess : function(data)
			{
				if ($(".glyphicon-th").next().html() == '')
				{
					$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
					if (!Helper.basic.hasPermission('finance:otherReceive:detail_export'))
					{
						$(".export.btn-group").remove();
					}
					$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
				}
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
				if (row.master.id == null)
				{
					return;
				}
				payment_view(row.master.id);
			},
			onClickRow : function(row, $element)
			{
				$element.addClass("tr_active").siblings().removeClass("tr_active");
			}
		});
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
		params['name'] = $("#recCompany").val();
		params['auditFlag'] = $("input[name='auditFlag']:checked").val() == "-1" ? null : $("input[name='auditFlag']:checked").val();
		params['isCancel'] = $("input[name='isCancel']:checked").val() == undefined ? null : $("input[name='isCancel']:checked").val();
		return params;
	}

	/* 查看 */
	function payment_view(id)
	{
		var url = Helper.basePath + '/finance/otherReceive/view/' + id;
		var title = "其它收款单";
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
							<sys:dateConfine label="制单日期" />
						</dd>

						<dd class="row-dd">
							<label class="form-label label_ui">收款单号：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_3s" id="billNo" name="billNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">付款单位：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_9" name="recCompany" id="recCompany" />
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
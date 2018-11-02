<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic }/site/transmit.js?v=${v }"></script>
<title>外部采购转订单</title>
<script type="text/javascript">
	$(function()
	{
		/* 更多*/
		$("#btn_more").click(function()
		{
			$("#more_div").toggle();
		});

		$("#btn_transmit").click(function()
		{
			var rows = getSelectedRows();
			if (Helper.isNotEmpty(rows))
			{
				var supplierName = "";
				var supplierArr = new Array();
				$(rows).each(function()
				{
					if(this.sellerName)
					{
						supplierName = this.sellerName;
						if (!supplierArr.contains(supplierName))
						{
							supplierArr.push(supplierName);
						}
					}
				});
				if (supplierArr.length == 1)
				{//判断是否统一供应商
					var _paramStr = "";
					$(rows).each(function(index, item)
					{
						if (this.id)
						{
							_paramStr = _paramStr + "&ids=" + item.id;
						}
					});
					//console.log(_paramStr)
					var url = Helper.basePath + '/purch/order/createFromExterior?1=1' + _paramStr;
					var title = "采购订单";
					admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));

				} else
				{
					Helper.message.warn("请选择同一供应商");
				}
			} else
			{
				Helper.message.warn("请选择明细");
			}
		});
		//强制完工
		$("#btn_complete").on("click", function()
		{
			var rows = getSelectedRows();
			if (Helper.isNotEmpty(rows))
			{
				Helper.post(Helper.basePath + '/purch/reconcil/complete', {
					"tableType" : "DETAIL"
				}, function(data)
				{
					if (data.success)
					{
						Helper.message.suc(data.message);
					} else
					{
						Helper.message.warn(data.message);
					}
					$("#bootTable").bootstrapTable("refreshOptions", {
						pageNumber : 1
					});
				});
			} else
			{
				Helper.message.warn("至少选择1项");
			}
		});
		//取消强制完工
		$("#btn_complete_cancel").on("click", function()
		{
			var rows = getSelectedRows();
			if (Helper.isNotEmpty(rows))
			{
				Helper.post(Helper.basePath + '/purch/reconcil/complete_cancel', {
					"tableType" : "DETAIL",
				}, function(data)
				{
					if (data.success)
					{
						Helper.message.suc(data.message);
					} else
					{
						Helper.message.warn(data.message);
					}
					$("#bootTable").bootstrapTable("refreshOptions", {
						pageNumber : 1
					});
				});
			} else
			{
				Helper.message.warn("至少选择1项");
			}
		});

		//订单详情table
		$("#bootTable").bootstrapTable({
			url : Helper.basePath + "/purch/transmit/fromExteriorPurchList",
			method : "post",
			contentType : 'application/json', //设置请求头信息  
			dataType : "json",
			pagination : false, // 是否显示分页（*）
			sidePagination : 'server',//设置为服务器端分页     
			pageList : Helper.bootPageList,
			queryParamsType : "",
			queryParams : queryParams,//参数
			responseHandler : responseHandler,

			//resizable : true, //是否启用列拖动
			showColumns : true, //是否显示所有的列
			minimumCountColumns : 2, //最少允许的列数
			striped : true, // 是否显示行间隔色
			cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			sortable : false, // 是否启用排序
			clickToSelect : true, // 是否启用点击选中行
			height : 370, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			cardView : false, // 是否显示详细视图
			detailView : false, // 是否显示父子表

			showExport : true,//是否显示导出按钮
			//exportDataType: 'all',
			exportTypes : [ 'csv', 'txt', 'excel', 'doc' ],

			cookie : true,//是否启用COOKIE
			cookiesEnabled : [ 'bs.table.columns' ],//默认会记住很多属性，这里控制只记住列选择属性
			cookieIdTable : "print_exterior_purch_transmit",//必须制定唯一的表格cookieID

			uniqueId : "id",//定义列表唯一键
			columns : [ {
				field : 'index',
				title : '序号',
				width : 40,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return;
					}
					return index + 1;
				}
			}, {
				field : 'state',
				radio : true,
				visible : true,
				width : 60
			}, {
				field : 'createTime',
				title : '制单日期',
				width : 80,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return;
					}
					return new Date(row.createTime).format("yyyy-MM-dd");
				}
			},  {
				field : 'orderNm',
				title : '印刷家平台订单号',
				width : 100,
				formatter : function(value,row,index)
				{
					return row.orderNm;
				}
			}, {
				field : 'sellerName',
				title : '供应商名称',
				width : 200,
				formatter : function (value,row,index)
				{
					return row.sellerName;
				}
			}, {
				field : 'linkName',
				title : '联系人',
				width : 80
			}, {
				field : 'linkPhone',
				title : '联系电话',
				width : 100
			},{
				field : 'orderPrice',
				title : '订单金额',
				width : 80
			},{
				field : 'orderRemark',
				title : '订单备注',
				'class' : 'memoView',
				width : 200,
				formatter : function (value, row, index)
				{
					return row.orderRemark;
				}
			} ],
			onLoadSuccess : function(data)
			{
				$("#bootTable tbody").find("tr:last").find("td:first").text("合计");
				$("#bootTable tbody").find("tr:last").find("td:first").next().children("input[type='radio']").remove();
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
				// 在使用筛选增加或减少列时重置表格列拖动效果
				bootstrapTable_ColDrag($("#bootTable"));
			}
		});
		$("#bootTable").on('load-success.bs.table column-switch.bs.table page-change.bs.table search.bs.table', function()
		{
			/* 表格工具栏 */
			if ($(".glyphicon-th").next().html() == '')
			{
				$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
				if (!Helper.basic.hasPermission('transmit:to_purch_reconcil:export'))
				{
					$(".export.btn-group").remove();
				}
				$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
			}
		});
	});

	function queryParams(params)
	{
		params['dateMin'] = $("#dateMin").val().trim();
		params['dateMax'] = $("#dateMax").val().trim();
		params['billNo'] = $("#billNo").val().trim();
		// params['materialName'] = $("#materialName").val().trim();
		params['supplierName'] = $("#supplierName").val().trim();
		//params['completeFlag'] = $("input[name='completeFlag']:checked").val().trim();

		params['orderBillNo'] = $("#purchBillNo").val().trim();

		return params;
	}
</script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="采购管理-转单功能-采购未下单"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="purch:reconcil:create">
						<button class="nav_btn table_nav_btn" id="btn_transmit">生成采购订单</button>
					</shiro:hasPermission>
					<%-- <shiro:hasPermission name="purch:reconcil:complete">
						<button class="nav_btn table_nav_btn" id="btn_complete">强制完工</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="purch:reconcil:complete_cancel">
						<button class="nav_btn table_nav_btn" id="btn_complete_cancel" style="display: none;">取消强制完工</button>
					</shiro:hasPermission> --%>

				</div>
			</div>
			<!--头部END-->
			<!--查询表单START-->
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">
						<dd class="row-dd">
							<sys:dateConfine label="制单日期" initDate="true" />
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">订单号：</label>
							<span class="ui-combo-wrap form_text">
								<input id="billNo" name="billNo" type="text" class="input-txt input-txt_3" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_6">供应商名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_9" id="supplierName" name="supplierName" />
							</span>
						</dd>
						<dd class="row-dd">
							<button type="button" class="nav_btn table_nav_btn search_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查询
							</button>

							<button class="nav_btn table_nav_btn more_show" id="btn_more">更多</button>
						</dd>
					</dl>

					<dl class="cl row-dl" style="display: none;" id="more_div">
						<dd class="row-dd">
							<label class="form-label label_ui">采购单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_13" id="purchBillNo" />
							</span>
						</dd>
					</dl>
				</div>
				<!-- <div>
					<div class="radio-box">
						<label>
							<input type="radio" value="YES" name="completeFlag" />
							已强制完工
						</label>
						<label>
							<input type="radio" value="NO" name="completeFlag" checked="checked" />
							未强制完工
						</label>
					</div>
				</div> -->
		</div>
			<!--查询表单End-->
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
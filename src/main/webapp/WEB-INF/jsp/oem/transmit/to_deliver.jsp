<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic }/site/transmit.js?v=${v }"></script>
<title>代工订单转送货</title>
<script type="text/javascript">
	$(function()
	{
		/* 更多*/
		$("#btn_more").click(function()
		{
			$("#more_div").toggle();
		});
		// 默认时间
		function defaultTime()
		{
			$('#createTime1').val(new Date().add("m", -1).format('yyyy-MM-dd'))
			$('#createTime2').val(new Date().format('yyyy-MM-dd'))
		}
		defaultTime()
		/* 转到送货*/
		$("#btn_transmit").click(function()
		{
			var rows = getSelectedRows();
			if (Helper.isNotEmpty(rows))
			{
				var customerId = "";
				var supplierArr = new Array();
				$(rows).each(function()
				{
					if (this.master)
					{
						customerId = this.master.customerId;
						if (!supplierArr.contains(customerId))
						{
							supplierArr.push(customerId);
						}

						var type = (this.master.billNo).substring(0, 1);
					}
				});

				if (supplierArr.length == 1)
				{//判断是否统一供应商
					var _paramStr = "";
					$(rows).each(function(index, item)
					{
						if (item.id)
						{
							_paramStr += "&ids=" + item.id;
						}
					});
					var url = Helper.basePath + '/oem/deliver/create?1=1' + _paramStr;
					var title = "代工送货";
					admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));

				} else
				{
					Helper.message.warn("请选择同一客户");
				}
			} else
			{
				Helper.message.warn("至少选择1项");
			}
		});
		//强制完工
		$("#btn_complete").on("click", function()
		{
			var rows = getSelectedRows();
			var ids = new Array();
			// 全选要除去最后的合计后
			for (var i = 0; i < rows.length; i++)
			{
				if (rows[i].master)
				{
					ids.push(rows[i].id);
				}
			}

			if (Helper.isNotEmpty(rows))
			{
				Helper.post(Helper.basePath + '/oem/transmit/completeOrder', {
					"tableType" : "DETAIL",
					"ids[]" : ids
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
			//      var ids=$(rows).map(function(){return this.id;}).get();
			var ids = new Array();
			// 全选要除去最后的合计后
			for (var i = 0; i < rows.length; i++)
			{
				if (rows[i].master)
				{
					ids.push(rows[i].id);
				}
			}

			if (Helper.isNotEmpty(rows))
			{
				Helper.post(Helper.basePath + '/oem/transmit/completeOrderCancel', {
					"tableType" : "DETAIL",
					"ids[]" : ids
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
			url : Helper.basePath + "/oem/transmit/toDeliverList",
			method : "post",
			contentType : 'application/json', //设置请求头信息  
			dataType : "json",
			pagination : false, // 是否显示分页（*）
			sidePagination : 'server',//设置为服务器端分页
			pageList : Helper.bootPageList,
			queryParamsType : "",
			pageSize : 20,
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
			exportTypes : [ 'csv', 'txt', 'excel', 'doc' ],

			cookie : true,//是否启用COOKIE
			cookiesEnabled : [ 'bs.table.columns' ],//默认会记住很多属性，这里控制只记住列选择属性
			cookieIdTable : "print_oem_transmit_deliver",//必须制定唯一的表格cookieID

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
				checkbox : true,
				visible : true,
				width : 60
			}, {
				field : 'master.createTime',
				title : '制单日期',
				width : 80,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return;
					}
					if (Helper.isNotEmpty(row.master.createTime))
					{
						return new Date(row.master.createTime).format("yyyy-MM-dd");
					}
				}
			}, {
				field : 'master.billNo',
				title : '代工单号',
				width : 120,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return;
					}
					return idTransToUrl(row.master.id, row.master.billNo);
				}
			}, {
				field : 'deliveryTime',
				title : '交货日期',
				width : 80,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return;
					}
					if (Helper.isNotEmpty(row.deliveryTime))
					{
						return new Date(row.deliveryTime).format("yyyy-MM-dd");
					}
				}
			}, {
				field : 'master_customerName',
				title : '客户名称',
				width : 180,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return;
					}
					return row.master.customerName;
				}
			}, {
				field : 'productName',
				title : '成品名称',
				width : 120
			}, {
				field : 'procedureName',
				title : '工序名称',
				width : 120
			}, {
				field : 'style',
				title : '加工规格',
				width : 100
			}, {
				field : 'qty',
				title : '代工数量',
				width : 100
			}, {
				field : 'deliverQty',
				title : '已送货数量',
				width : 100
			}, {
				field : 'qty_deliverQty',
				title : '未送货数量',
				width : 100,
				formatter : function(value, row, index)
				{
					return Number(row.qty).subtr(Number(row.deliverQty));
				}
			}, {
				field : 'partName',
				title : '部件名称',
				width : 100
			}, {
				field : 'processRequire',
				title : '加工要求',
				'class' : 'memoView',
				width : 200
			}, {
				field : 'memo',
				title : '备注',
				'class' : 'memoView',
				width : 200
			} ],
			onLoadSuccess : function(data)
			{
				$("#bootTable tbody").find("tr:last").find("td:first").text("合计");
				$("#bootTable tbody").find("tr:last").find("td:first").next().children("input[type='checkbox']").remove();
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
				if (!Helper.basic.hasPermission('oem:deliver:transmit:export'))
				{
					$(".export.btn-group").remove();
				}
				$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
			}
		});
	});

	//获取选择项
	function getSelectedArray()
	{
		$("#transmitForm").empty();
		var array = $('tbody input[type=checkbox]:checked').map(function()
		{
			return this.value
		}).get();
		$('tbody input[type=checkbox]:checked').each(function()
		{
			$("#transmitForm").append("<input type='hidden' name='ids' value='"+this.value+"'/>");
		});
		return array;
	}
	// 获取返回产品信息
	function getCallInfo_product(obj)
	{
		$("#productName").val(obj.name);
	}
	// 获取返回客户信息
	function getCallInfo_customer(obj)
	{
		$("#customerName").val(obj.name);
	}

	function queryParams(params)
	{
		params['createTimeMin'] = $("#dateMin").val().trim();
		params['createTimeMax'] = $("#dateMax").val().trim();
		params['billNo'] = $("#billNo").val().trim();
		params['productName'] = $("#productName").val().trim();
		params['procedureName'] = $("#procedureName").val().trim();
		params['customerName'] = $("#customerName").val().trim();
		params['completeFlag'] = $("input[name='completeFlag']:checked").val().trim();

		params['deliverDateMin'] = $("#deliverDateMin").val().trim();
		params['deliverDateMax'] = $("#deliverDateMax").val().trim();
		return params;
	}
</script>
</head>
<body>
	<form id="transmitForm"></form>
	<div class="page-container">
		<div class="page-border">
			<div class="cl">
				<div class="iframe-top">
					<c:if test="${fns:hasCompanyPermission('oem:order:list')}">
						<sys:nav struct="代工管理-转单功能-代工订单转送货单"></sys:nav>
					</c:if>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="oem:deliver:create">
						<button class="nav_btn table_nav_btn" id="btn_transmit">生成代工送货单</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="oem:deliver:transmit:complete">
						<button class="nav_btn table_nav_btn" id="btn_complete">强制完工</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="oem:deliver:transmit:completecancel">
						<button class="nav_btn table_nav_btn" id="btn_complete_cancel" style="display: none;">取消强制完工</button>
					</shiro:hasPermission>
				</div>
			</div>
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">
						<dd class="row-dd">
							<sys:dateConfine label="制单日期" />
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">代工单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_3s" id="billNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">客户名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_9" id="customerName" />
								<div class="select-btn" id="customer_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">工序名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_6" id="procedureName" />
								<div class="select-btn" id="procedure_quick_select">...</div>
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
							<label class="form-label label_ui label_1">交货日期：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'deliverDateMax\')}'})" class="input-txt input-txt_0 Wdate" id="deliverDateMin" name="deliverDateMin" value="<fmt:formatDate value="${deliverDateMin }" type="date" pattern="yyyy-MM-dd" />" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_2 " style="text-align: left; margin-left: -3px; width: 18px">至</label>
							<span class="ui-combo-wrap form_text" style="margin-left:2px;">
								<input type="text" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'deliverDateMin\')}'})" class="input-txt input-txt_0 Wdate" id="deliverDateMax" name="deliverDateMax" value="<fmt:formatDate value="${deliverDateMax }" type="date" pattern="yyyy-MM-dd" />" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">成品名称：</label>
							<span class="ui-combo-wrap">
								<input type="text" class="input-txt input-txt_3s" id="productName" />
								<div class="select-btn" id="product_quick_select">...</div>
							</span>
						</dd>
					</dl>
				</div>
				<div>
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
				</div>
			</div>
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
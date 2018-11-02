<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<meta charset="UTF-8">
<title>采购订单</title>
<script type="text/javascript" src="${ctxStatic}/site/purch/purch.js "></script>
<script>
	$(function()
	{
		// 当前详情id列表数据（）
		var currentDataIdList = [];
		// 当前采购入库id列表
		var currentInStockIdList = [];
		/* 判断当前用户是否有权限查看金额 */
		var hasPermission = Helper.basic.hasPermission('purch:order:money');
		var url = "";
		/* 区分从采购订单列表点击查看还是从点击单号查看 */
		if (!/^PO/g.test("${id}")) // 从销售订单点击查看，则是带id请求服务
		{
			url = Helper.basePath + "/purch/order/viewAjax/${id}";
		} else if (/^PO/g.test("${id}")) // 从其他单据点击源单单号查看的，则是带订单号请求服务
		{
			var billNo = "${id}";
			url = Helper.basePath + "/purch/order/toViewAjax/" + billNo;
		}
		$("#detailList").bootstrapTable({
			url : url,//不需要查询
			method : "post",
			contentType : 'application/json', //设置请求头信息  
			dataType : "json",
			pagination : false, // 是否显示分页（*）
			sidePagination : 'server',//设置为服务器端分页
			pageList : [ 10, 20, 50 ],
			queryParamsType : "",
			pageSize : 10,
			pageNumber : 1,
			responseHandler : viewResponseHandler,
			//resizable : true, //是否启用列拖动
			showColumns : true, //是否显示所有的列
			minimumCountColumns : 2, //最少允许的列数
			striped : true, // 是否显示行间隔色
			cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			sortable : false, // 是否启用排序
			clickToSelect : true, // 是否启用点击选中行
			height : undefined, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			cardView : false, // 是否显示详细视图
			detailView : false, // 是否显示父子表

			showExport : false,//是否显示导出按钮
			//exportDataType: 'all',
			exportTypes : [ 'csv', 'txt', 'excel', 'doc' ],

			cookie : true,//是否启用COOKIE
			cookiesEnabled : [ 'bs.table.columns' ],//默认会记住很多属性，这里控制只记住列选择属性
			cookieIdTable : "print_purch_order_master_view",//必须制定唯一的表格cookieID
			columns : [ {
				field : 'state',
				title : '单选',
				radio : true,
				visible : true,
				width : 60
			}, {
				field : 'id',
				title : '序号',
				width : 40,
				formatter : function(value, row, index)
				{
					return index + 1;
				}
			}, {
				field : 'code',
				title : '材料编号',
				width : 100

			}, {
				field : 'materialName',
				title : '材料名称',
				width : 120

			}, {
				field : 'specifications',
				title : '材料规格',
				width : 100
			}, {
				field : 'weight',
				title : '克重',
				width : 60
			}, {
				field : 'purchUnitName',
				title : '单位',
				width : 60
			}, {
				field : 'qty',
				title : '采购数量',
				width : 80,
				formatter : function(value, row, index)
				{
					return value + '<input name="detailList.qty" type="hidden" value="'+value+'"/>';
				}
			}, {
				field : 'valuationUnitName',
				title : '计价单位',
				width : 60
			}, {
				field : 'valuationQty',
				title : '计价数量',
				width : 80
			}, {
				field : 'valuationPrice',
				title : '计价单价',
				visible : hasPermission,
				width : 80
			}, {
				field : 'money',
				title : '金额',
				visible : hasPermission,
				width : 80
			}, {
				field : 'tax',
				title : '税额',
				visible : hasPermission,
				width : 60
			}, {
				field : 'taxRateId',
				title : '税收',
				width : 80,
				formatter : function(value, row, index)
				{
					if (value != null && value != "")
					{
						return Helper.basic.info('TAXRATE', value).name;
					}

				}
			}, {
				field : 'percent',
				title : '税率值',
				visible : false,
				width : 100
			}, {
				field : 'price',
				title : '库存单价',
				visible : false,
				width : 80
			}, {
				field : 'deliveryTime',
				title : '交货日期',
				width : 80,
				formatter : function(value, row, index)
				{
					return new Date(value).format("yyyy-MM-dd");
				}
			}, {
				field : 'sourceBillType',
				title : '源单类型',
				width : 80,
				formatter : function(value, row, index)
				{
					return Helper.basic.getEnumText('com.huayin.printmanager.persist.enumerate.BillType', value, 'text');
				}
			}, {
				field : 'sourceBillNo',
				title : '生产单号',
				width : 100,
				formatter : function(value, row, index)
				{
					return billNoTransToUrl(value);
				}
			}, {
				field : 'sourceQty',
				title : '源单数量',
				visible : false,
				width : 80
			}, {
				field : 'storageQty',
				title : '已入库数量',
				visible : false,
				width : 100,
				formatter : function(value, row, index)
				{
					return value + '<input name="detailList.storageQty" type="hidden" value="'+value+'"/>';
				}
			}, {
				field : 'noTaxMoney',
				title : '不含税金额',
				visible : false,
				width : 100
			}, {
				field : 'noTaxPrice',
				title : '不含税单价',
				visible : false,
				width : 100
			}, {
				field : 'productNames',
				title : '成品名称',
				width : 100
			}, {
				field : 'memo',
				title : '备注',
				'class' : 'memoView',
				width : 150
			} ],
			onLoadSuccess : function(data)
			{
				/* 打印*/
				var id = data.rows[0].masterId;
				$("#btn_print").loadTemplate('PURCH_PO', '/purch/order/printAjax/' + id);
				for (var i = 0; i < data.rows.length; i++)
				{
					var obj = data.rows[i];
					currentDataIdList.push(obj.id);
					// 采购数量 大于 库存数量
					if (obj.qty > obj.storageQty)
					{
						currentInStockIdList.push(obj.id);
					}
				}
				// 隐藏【生成采购入库】
				if (currentInStockIdList.length <= 0)
				{
					$("#btn_transmit_in_stock").hide();
					$("#generateTransmit").hide();
				}
				// alert("数据加载完成");
			},
			onLoadError : function()
			{
				// alert("数据加载异常");
			},
			onClickRow : function(row, $element)
			{
				$element.addClass("tr_active").siblings().removeClass("tr_active");
			},
			onColumnSwitch : function(field, checked)
			{
				// 在使用筛选增加或减少列时重置表格列拖动效果
				bootstrapTable_ColDrag($("#detailList"));
			}
		});

		/* 表格工具栏 */
		$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
		// 控制筛选菜单金额选择
		if (!hasPermission)
		{
			$("div[title='列'] ul[role=menu]").find("input[value=9]").parent().parent().remove();
			$("div[title='列'] ul[role=menu]").find("input[value=10]").parent().parent().remove();
			$("div[title='列'] ul[role=menu]").find("input[value=11]").parent().parent().remove();
			$("div[title='列'] ul[role=menu]").find("input[value=14]").parent().parent().remove();
			$("div[title='列'] ul[role=menu]").find("input[value=20]").parent().parent().remove();
			$("div[title='列'] ul[role=menu]").find("input[value=21]").parent().parent().remove();
			$("div[title='列'] ul[role=menu]").find("input[value=22]").parent().parent().remove();
		}
		;
		$("#btn_audit").click(function()
		{
			$.ajax({
				cache : true,
				type : "POST",
				url : Helper.basePath + '/purch/order/check/' + $("#id").val(),
				async : false,
				dataType : "json",
				error : function(request)
				{
					Helper.message.warn("Connection error");
				},
				success : function(data)
				{
					if (data.success)
					{
						location.reload();
					} else
					{
						Helper.message.warn(data.message);
					}
				}
			});
		});
		$("#btn_audit_cancel").click(function()
		{
			$.ajax({
				cache : true,
				type : "POST",
				url : Helper.basePath + '/purch/order/checkBack/' + $("#id").val(),
				async : false,
				dataType : "json",
				error : function(request)
				{
					Helper.message.warn("服务器繁忙");
				},
				success : function(data)
				{
					if (data.success)
					{
						location.reload();
					} else
					{
						if (data.obj)
						{
							var msg = "";
							var index = 1;
							$.each(data.obj, function(i, n)
							{
								msg += (index++) + ".反审核失败,已被下游单据引用：<br/>"
								$.each(n, function(k, j)
								{
									if (i == 'PN')
									{
										msg += '&emsp;&emsp;采购入库单<a href="javascript:;" onclick="stock_view(' + j.id + ')">' + j.billNo + '</a><br/>';
									}

								});
							});
							Helper.message.view(msg);
						} else
						{
							Helper.message.warn(data.message);
						}
					}
				}
			});

		});

		$("#btn_del").click(function()
		{
			Helper.message.confirm('确认要删除吗？', function(index)
			{
				$.ajax({
					cache : true,
					type : "POST",
					url : Helper.basePath + '/purch/order/delete/' + $("#id").val(),
					async : false,
					dataType : "json",
					error : function(request)
					{
						Helper.message.warn("Connection error");
					},
					success : function(data)
					{
						if (data.success)
						{
							closeTabAndJump("采购订单列表");
						} else
						{
							Helper.message.warn("失败");
						}
					}
				});
			});

		});

		$("#btn_edit").click(function()
		{
			window.location.href = Helper.basePath + '/purch/order/edit/' + $("#id").val();
		});

		$("#btn_back").click(function()
		{
			var url = Helper.basePath + '/purch/order/list';
			var title = "采购订单列表";
			admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
		});

		//强制完工
		$("#btn_complete").on("click", function()
		{
			var order_id = $("#id").val();
			Helper.post(Helper.basePath + '/purch/order/complete', {
				"tableType" : "MASTER",
				"ids" : [ order_id ]
			}, function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
					location.href = Helper.basePath + "/purch/order/view/" + order_id;
				} else
				{
					Helper.message.warn(data.message);
				}
			});
		});
		//取消强制完工
		$("#btn_complete_cancel").on("click", function()
		{
			var order_id = $("#id").val();

			Helper.post(Helper.basePath + '/purch/order/complete_cancel', {
				"tableType" : "MASTER",
				"ids" : [ order_id ]
			}, function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
					location.href = Helper.basePath + "/purch/order/view/" + order_id;
				} else
				{
					Helper.message.warn(data.message);
				}
			});
		});
		// 生成采购入库
		$("#btn_transmit_in_stock").click(function()
		{
			if (currentInStockIdList.length > 0)
			{
				var _paramStr = "";
				$(currentInStockIdList).each(function(index, item)
				{
					if (index == 0)
					{
						_paramStr = "ids=" + item;
					} else
					{
						_paramStr = _paramStr + "&ids=" + item;
					}
				});
				var url = Helper.basePath + '/purch/stock/toStock?' + _paramStr;
				var title = "生成采购入库";
				admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
			}
		});
		// 单价变更
		$("#priceChange").on("click", function()
		{
			var row = $("#detailList").bootstrapTable('getSelections');
			if (Helper.isEmpty(row))
			{
				Helper.message.warn("请先选择一个材料");
				return;
			}
			Helper.popup.show('更新单价交期', Helper.basePath + '/purch/order/editPrice/' + row[0].id, '390', '300');
		});
		// 复制
		$("#btn_copy").on("click", function()
		{
			var order_id = $("#id").val();
			location.href = Helper.basePath + "/purch/order/copy/" + order_id;
		});

		$("#detailList").on('load-success.bs.table', function()
		{
			// bootstrap_table加载完后触发列拖动效果
			bootstrapTable_ColDrag($("#detailList"));
		});
	});

	function stock_view(id)
	{
		if (!Helper.isNotEmpty(id))
		{
			return;
		}
		var url = Helper.basePath + '/purch/stock/view/' + id;
		var title = "采购入库";
		admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
	}
</script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="采购管理-采购订单-查看"></sys:nav>
				</div>
				<div class="top_nav">
					<button class="nav_btn table_nav_btn" id="btn_back">返回</button>
					<span id="forceCompleteNO" style="display: none;"> <span id="isCheckNO" style="display: none;"> <shiro:hasPermission name="purch:order:edit">
								<button class="nav_btn table_nav_btn" id="btn_edit">修改</button>
							</shiro:hasPermission> <shiro:hasPermission name="purch:order:audit">
								<button class="nav_btn table_nav_btn" id="btn_audit">审核</button>
							</shiro:hasPermission> <shiro:hasPermission name="purch:order:del">
								<button class="nav_btn table_nav_btn" id="btn_del">删除</button>
							</shiro:hasPermission>
					</span> <span id="isCheckYES" style="display: none;"> <shiro:hasPermission name="purch:order:changePrice">
								<button class="nav_btn table_nav_btn" id="priceChange">单价变更</button>
							</shiro:hasPermission> <shiro:hasPermission name="purch:order:audit_cancel">
								<button class="nav_btn table_nav_btn" id="btn_audit_cancel">反审核</button>
							</shiro:hasPermission> <shiro:hasPermission name="purch:order:complete">
								<button class="nav_btn table_nav_btn" id="btn_complete">强制完工</button>
							</shiro:hasPermission>
					</span>
					</span> <span id="forceCompleteYES" style="display: none;"> <shiro:hasPermission name="purch:order:complete_cancel">
							<button class="nav_btn table_nav_btn" id="btn_complete_cancel">取消强制完工</button>
						</shiro:hasPermission>
					</span>
					<shiro:hasPermission name="purch:order:create">
						<button class="nav_btn table_nav_btn" id="btn_copy">复制</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="purch:order:print">
						<div class="btn-group" id="btn_print">
							<button class="nav_btn table_nav_btn " type="button">
								模板打印 <span class="caret"></span>
							</button>
							<div class="template_div"></div>
						</div>
					</shiro:hasPermission>
					<span class="isFlowCheckYES" id="generateTransmit" style="display: none;">
						<div class="btn-group" id="btn_print">
							<button class="nav_btn table_nav_btn " type="button">
								生成 <span class="caret"></span>
							</button>
							<div class="template_div">
								<ul class="dropdown-menu" role="menu">
									<li id="btn_transmit_in_stock"><a title="生成采购入库" href="javascript:;">生成采购入库</a></li>
								</ul>
							</div>
						</div>
					</span>
				</div>
			</div>
			<!--主表-订单表单-->
			<form action="${ctx}/purch/order/create" id="orderAction" method="post">
				<input type="hidden" id="id" />
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_6">供应商名称：</label> <span class="ui-combo-wrap"> <input type="text" class="input-txt input-txt_7" readonly="readonly" id="supplierName" name="supplierName" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">联 系 人：</label> <span class="ui-combo-wrap"> <input type="text" class="input-txt input-txt_3" readonly="readonly" name="linkName" id="linkName" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">联系电话：</label> <span class="ui-combo-wrap"> <input type="text" class="input-txt input-txt_1" name="mobile" id="mobile" readonly="readonly" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">送货方式：</label> <span class="ui-combo-wrap"> <input type="text" class="input-txt input-txt_1" readonly="readonly" name="deliveryClassName" id="deliveryClassName" />
									</span>
								</dd>
								<dd class="row-dd" style="display: none">
									<label class="form-label label_ui label_1">交货日期：</label> <input type="text" class="input-txt input-txt_1" readonly="readonly" id="purchTime" pattern="yyyy-MM-dd" />
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">采购人员：</label> <span class="ui-combo-wrap"> <input type="text" class="input-txt input-txt_1" readonly="readonly" name="employeeName" id="employeeName" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_6">供应商地址：</label> <span class="ui-combo-wrap"> <input type="text" class="input-txt input-txt_11" readonly="readonly" name="supplierAddress" id="supplierAddress" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">结算方式：</label> <span class="ui-combo-wrap"> <input type="text" class="input-txt input-txt_1" readonly="readonly" name="settlementClassName" id="settlementClassName" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">付款方式：</label> <span class="ui-combo-wrap"> <input type="text" class="input-txt input-txt_1" readonly="readonly" name="paymentClassName" id="paymentClassName" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">采购单号：</label> <span class="ui-combo-wrap"> <input type="text" class="input-txt input-txt_1" readonly="readonly" id="billNo" name="billNo" />
									</span>
								</dd>
								<dd class="row-dd" style="display: none">
									<label class="form-label label_ui label_1">税 收：</label> <span class="ui-combo-wrap"> <input type="text" class="input-txt input-txt_1" readonly="readonly" name="taxRateName" id="taxRateName" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_6">备注：</label> <span class="form_textarea"> <textarea class="noborder" readonly="readonly" style="width: 883px" name="memo" id="memo"></textarea>
									</span>
								</dd>
							</dl>
						</div>
					</div>
				</div>
				<!--表格部分Start-->
				<div class="table-view">
					<table class="border-table resizable" id="detailList">
					</table>
				</div>
				<!--表格部分End-->
				<div class="cl form_content">
					<dl class="cl row-dl-foot">
						<!-- 判断是否有查看金额的权限----begin -->
						<shiro:hasPermission name="purch:order:money">
							<dd class="row-dd">
								<label class="form-label label_ui label_3">金 额：</label> <span class="ui-combo-wrap"> <input id="totalMoney" name="totalMoney" type="text" class="input-txt input-txt_1" readonly="readonly" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_3">金额(不含税)：</label> <span class="ui-combo-wrap"> <input id="noTaxTotalMoney" name="noTaxTotalMoney" type="text" class="input-txt input-txt_1" readonly="readonly" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_3">税 额：</label> <span class="ui-combo-wrap"> <input id="totalTax" name="totalTax" type="text" class="input-txt input-txt_1" readonly="readonly" />
								</span>
							</dd>
						</shiro:hasPermission>
						<!-- 判断是否有查看金额的权限----end  -->
						<dd class="row-dd">
							<label class="form-label label_ui label_3">币 种：</label> <span class="ui-combo-wrap"> <input type="text" class="input-txt input-txt_1" readonly="readonly" id="currencyTypeText" name="currencyTypeText" />

							</span>
						</dd>
					</dl>
					<dl class="cl row-dl-foot">
						<dd class="row-dd">
							<label class="form-label label_ui label_3">制 单 人：</label> <span class="ui-combo-wrap"> <input name="createName" type="text" class="input-txt input-txt_1" readonly="readonly" id="createName" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_3">制单日期：</label> <span class="ui-combo-wrap"> <input name="createTime" id="createTime" type="text" class="input-txt input-txt_1" readonly="readonly" pattern="yyyy-MM-dd" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_3">审 核 人：</label> <span class="ui-combo-wrap"> <input name="checkUserName" id="checkUserName" type="text" class="input-txt input-txt_1" readonly="readonly" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_3">审核日期：</label> <span class="ui-combo-wrap"> <input name="checkTime" id="checkTime" type="text" class="input-txt input-txt_1" readonly="readonly" pattern="yyyy-MM-dd" />
							</span>
						</dd>
					</dl>
				</div>
			</form>

			<div class="review" style="display: none;">
				<span class="review_font">已审核</span>
			</div>
		</div>
	</div>
</body>
</html>


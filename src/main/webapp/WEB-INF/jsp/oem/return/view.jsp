<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>查看退货单</title>
<script type="text/javascript">
	$(function()
	{
		/* 判断当前用户是否有权限查看金额 */
		var hasPermission = Helper.basic.hasPermission('oem:return:money');
		var detailList = ${fns:toJson(order.detailList)};
		//订单详情table
		$("#detailList").bootstrapTable({
			data:detailList,
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
			cookieIdTable : "print_oem_return_master",//必须制定唯一的表格cookieID

			uniqueId : "id",//定义列表唯一键
			columns : [ {
				field : 'index',
				title : '序号',
				width : 40,
				formatter : function(value, row, index)
				{
					return (index + 1);
				}
			}, {
				field : 'productName',
				title : '成品名称',
				width : 120
			},  {
				field : 'procedureName',
				title : '工序名称',
				width : 100
			}, {
				field : 'style',
				title : '加工规格',
				width : 100
			}, {
				field : 'sourceQty',
				title : '源单数量',
				width : 80
			}, {
				field : 'sourceBillNo',
				title : '源单单号',
				width : 120,
				formatter : function(value, row, index)
				{
					return idTransToUrl(row.sourceId,value);
				}
			}, {
				field : 'oemOrderBillNo',
				title : '代工单号',
				width : 120,
				formatter : function(value, row, index)
				{
					return idTransToUrl(row.oemOrderBillId, value);
				}
			}, {
				field : 'qty',
				title : '换/退货数量',
				width : 100
			}, {
				field : 'reconcilQty',
				title : '已对账数量',
				width : 100,
				visible : false
			}, {
				field : 'price',
				title : '单价',
				visible : hasPermission,
				width : 60,
				formatter : function(value, row, index)
				{
					return value + '<input class="tab_input" type="hidden" name="detailList.price" value="' + (value || "") + '"/>'
				}
			}, {
				field : 'money',
				title : '金额',
				visible : hasPermission,
				width : 100,
				formatter : function(value, row, index)
				{
					return value + '<input class="tab_input" type="hidden" name="detailList.money" value="' + (value || "") + '"/>';
				}
			}, {
				field : 'tax',
				title : '税额',
				width : 80,
				visible : hasPermission,
				formatter : function(value, row, index)
				{
					return value + '<input type="hidden" name="detailList.tax" readonly value="' + (value || "") + '"/>';
				}
			}, {
				field : 'taxRateId',
				title : '税收',
				width : 80,
				formatter : function(value, row, index)
				{
					return Helper.basic.info('TAXRATE', value).name;
				}
			}, {
				field : 'percent',
				title : '税率值%',
				width : 80,
				formatter : function(value, row, index)
				{
					return value + '<input type="hidden" name="detailList.percent" readonly value="' + (value || "") + '%"/>';
				},
				visible : false
			}, {
				field : 'noTaxPrice',
				title : '不含税单价',
				width : 80,
				formatter : function(value, row, index)
				{
					return value + '<input class="tab_input" type="hidden" name="detailList.noTaxPrice" readonly value="' + (value || "") + '"/>'
				},
				visible : false
			}, {
				field : 'noTaxMoney',
				title : '不含税金额',
				width : 100,
				formatter : function(value, row, index)
				{
					return value + '<input class="tab_input" type="hidden" name="detailList.noTaxMoney" readonly value="' + (value || "") + '"/>';
				},
				visible : false
			}, {
				field : 'partName',
				title : '部件名称',
				width : 80
			}, {
				field : 'processRequire',
				title : '加工要求',
				'class' : 'memoView',
				width : 120
			}, {
				field : 'memo',
				title : '备注',
				'class' : 'memoView',
				width : 120
			} ],
			onLoadSuccess : function()
			{

			},
			onLoadError : function()
			{
				// alert("数据加载异常");
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
			$("div[title='列'] ul[role=menu]").find("input[value=15]").parent().parent().remove();
		}
		;
		/* 返回显示列表 */
		$("#btn_back").on("click", function()
		{
			var url = Helper.basePath + '/oem/return/list';
			var title = "代工退货列表";
			admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
		});
		/* 编辑 */
		$("#btn_edit").on("click", function()
		{
			var url = Helper.basePath + '/oem/return/edit/' + $("#id").val();
			var title = "代工退货";
			admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
		});
		//审核
		$("#btn_audit").on("click", function()
		{
			var order_id = $("#id").val();
			Helper.post(Helper.basePath + '/oem/return/audit/' + order_id, function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
					location.href = Helper.basePath + "/oem/return/view/" + order_id;
				} else
				{
					Helper.message.warn(data.message);
				}
			});
		});
		
		//反审核
		$("#btn_audit_cancel").on("click", function()
		{
			var order_id = $("#id").val();
			Helper.post(Helper.basePath + '/oem/return/auditCancel/' + order_id, function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
					location.href = Helper.basePath + "/oem/return/view/" + order_id;
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
								if (i == 'EC')
								{
									msg += '&emsp;&emsp;对账单<a href="javascript:;" onclick="reconcil_view(' + j.id + ')">' + j.billNo + '</a><br/>';
								}
							});
						});
						Helper.message.view(msg);
					} else
					{
						Helper.message.warn(data.message);
					}
				}
			});
		});

		//强制完工
		$("#btn_complete").on("click", function()
		{
			var order_id = $("#id").val();
			Helper.post(Helper.basePath + '/oem/return/complete', {
				"tableType" : "MASTER",
				"ids" : [ order_id ]
			}, function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
					location.href = Helper.basePath + "/oem/return/view/" + order_id;
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
			Helper.post(Helper.basePath + '/oem/return/completeCancel', {
				"tableType" : "MASTER",
				"ids" : [ order_id ]
			}, function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
					location.href = Helper.basePath + "/oem/return/view/" + order_id;
				} else
				{
					Helper.message.warn(data.message);
				}
			});
		});

		/* 删除 */
		$("#btn_del").on("click", function()
		{
			Helper.message.confirm('确认要删除吗？', function(index)
			{
				Helper.post(Helper.basePath + '/oem/return/del/' + $("#id").val(), function(data)
				{
					if (data.success)
					{
						Helper.message.suc(data.message);
						closeTabAndJump("代工退货列表");
					} else
					{
						Helper.message.warn(data.message);
					}
				});
			});

		});
		//打印模板加载
		$("#btn_print").loadTemplate('OEM_ER', '/oem/return/printAjax/${id}');
		
		// 表格列拖动效果
		bootstrapTable_ColDrag($("#detailList"));
	});

	//查看对账单
	function reconcil_view(id)
	{
		if (!Helper.isNotEmpty(id))
		{
			return;
		}
		var url = Helper.basePath + '/oem/reconcil/view/' + id;
		var title = "代工对账";
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
					<sys:nav struct="代工管理-代工退货-查看"></sys:nav>
				</div>
				<div class="top_nav">
					<button class="nav_btn table_nav_btn" id="btn_back">返回</button>
					<c:if test="${order.isForceComplete eq 'NO'}">
						<c:if test="${order.isCheck eq 'NO'}">
							<shiro:hasPermission name="oem:return:edit">
								<button class="nav_btn table_nav_btn" id="btn_edit">修改</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="oem:return:audit">
								<button class="nav_btn table_nav_btn" id="btn_audit">审核</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="oem:return:del">
								<button class="nav_btn table_nav_btn" id="btn_del">删除</button>
							</shiro:hasPermission>
						</c:if>
						<c:if test="${order.isCheck eq 'YES'}">
							<shiro:hasPermission name="oem:return:auditcancel">
								<button class="nav_btn table_nav_btn" id="btn_audit_cancel">反审核</button>
							</shiro:hasPermission>
						</c:if>
						<shiro:hasPermission name="oem:return:complete">
							<button class="nav_btn table_nav_btn" id="btn_complete">强制完工</button>
						</shiro:hasPermission>
					</c:if>
					<c:if test="${order.isForceComplete eq 'YES'}">
						<shiro:hasPermission name="oem:return:completecancel">
							<button class="nav_btn table_nav_btn" id="btn_complete_cancel">取消强制完工</button>
						</shiro:hasPermission>
					</c:if>
					<shiro:hasPermission name="oem:return:print">
						<div class="btn-group" id="btn_print">
							<button class="nav_btn table_nav_btn " type="button">
								模板打印
								<span class="caret"></span>
							</button>
							<div class="template_div"></div>
						</div>
					</shiro:hasPermission>
				</div>
			</div>
			<input type="hidden" name="id" id="id" value="${order.id }" />
			<!--主表-订单表单-->
			<div class="form-container">
				<div class="form-wrap">
					<div class="cl form_content">
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_1">客户名称：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_7" readonly="readonly" id="customerName" value="${order.customerName}" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">联 系 人：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_3" readonly="readonly" id="linkName" value="${order.linkName}" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">联系电话：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_3" readonly="readonly" id="mobile" value="${order.mobile}" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">退货类型：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_3" readonly="readonly" id="returnTypeText" value="${order.returnTypeText}" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">销售员：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_3" readonly="true" id="employeeName" value="${order.employeeName}" />
								</span>
							</dd>
						</dl>
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_1">送货地址：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_11" readonly="readonly" id="deliveryAddress" value="${order.deliveryAddress}" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">送货方式：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_3" readonly="readonly" id="deliveryClassName" value="${order.deliveryClassName}" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">付款方式：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_3" readonly="readonly" id="paymentClassName" value="${order.paymentClassName}" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">退货单号：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_3" readonly="readonly" id="billNo" value="${order.billNo }" />
								</span>
							</dd>
						</dl>
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_1">备注：</label>
								<span class="form_textarea">
									<textarea class="noborder" readonly="readonly" style="width: 928px" id="memo">${order.memo }</textarea>
								</span>
							</dd>
						</dl>
					</div>
				</div>
			</div>
			<!--从表-订单表格-->
			<!--表格Start-->
			<div class="table-view">
				<table class="border-table resizable" id="detailList">
				</table>
			</div>
			<!--表格End-->
			<div class="cl form_content">
				<dl class="cl row-dl-foot">
					<shiro:hasPermission name="oem:return:money">
						<dd class="row-dd">
							<label class="form-label label_ui label_7">金 额：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" class="input-txt input-txt_3" readonly="readonly" id="totalMoney" value="${order.totalMoney }" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">金额(不含税)：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" class="input-txt input-txt_3" readonly="readonly" id="noTaxTotalMoney" value="${order.noTaxTotalMoney }" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_7">税 额：</label>
							<span class="ui-combo-wrap" class="form_text">
								<input type="text" class="input-txt input-txt_3" readonly="readonly" id="totalTax" value="${order.totalTax }" />
							</span>
						</dd>
					</shiro:hasPermission>
					<dd class="row-dd">
						<label class="form-label label_ui label_7">币 种：</label>
						<span class="ui-combo-wrap" class="form_text">
							<input type="text" class="input-txt input-txt_3" readonly="readonly" id="currencyTypeText" value="${order.currencyTypeText }" />
						</span>
					</dd>
				</dl>
				<dl class="cl row-dl-foot">
					<dd class="row-dd">
						<label class="form-label label_ui label_7">制 单 人：</label>
						<span class="ui-combo-wrap" class="form_text">
							<input type="text" class="input-txt input-txt_3" readonly="readonly" id="createName" value="${order.createName }" />
						</span>
					</dd>
					<dd class="row-dd">
						<label class="form-label label_ui label_7">制单日期：</label>
						<span class="ui-combo-wrap" class="form_text">
							<input type="text" class="input-txt input-txt_3" readonly="readonly" id="createTime" pattern="yyyy-MM-dd" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${order.createTime }' type='date' />" />
						</span>
					</dd>
					<dd class="row-dd">
						<label class="form-label label_ui label_7">审 核 人：</label>
						<span class="ui-combo-wrap" class="form_text">
							<input type="text" class="input-txt input-txt_3" readonly="readonly" id="checkUserName" value="${order.checkUserName }" />
						</span>
					</dd>
					<dd class="row-dd">
						<label class="form-label label_ui label_7">审核日期：</label>
						<span class="ui-combo-wrap" class="form_text">
							<input type="text" class="input-txt input-txt_3" readonly="readonly" id="checkTime" pattern="yyyy-MM-dd" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${order.checkTime }' type='date' />" />
						</span>
					</dd>
				</dl>
			</div>

			<c:if test="${order.isCheck eq 'YES'}">
				<div class="review">
					<span class="review_font">已审核</span>
				</div>
			</c:if>
		</div>
	</div>

</body>
</html>
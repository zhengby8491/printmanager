<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>查看付款核销单</title>
<script type="text/javascript">
	$(function()
	{
		//订单详情table
		$("#detailList").bootstrapTable({
			data:${fns:toJson(order.detailList)},
			showColumns : true, //是否显示所有的列
			minimumCountColumns : 2, //最少允许的列数
			striped : true, // 是否显示行间隔色
			cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination : false, // 是否显示分页（*）
			sortable : false, // 是否启用排序
			clickToSelect : true, // 是否启用点击选中行
			cardView : false, // 是否显示详细视图
			detailView : false, // 是否显示父子表
			cookie : true,//是否启用COOKIE
			cookiesEnabled : [ 'bs.table.columns' ],//默认会记住很多属性，这里控制只记住列选择属性
			cookieIdTable : "print_finance_writeoffPayment_master_view",//必须制定唯一的表格cookieID
			//resizable : true, //是否启用列拖动
			columns : [ {
				field : 'sourceBillType',
				title : '源单类型',
				width : 80,
				formatter : function(value, row, index)
				{
					return Helper.basic.getEnumText('com.huayin.printmanager.persist.enumerate.BillType', value, 'text');
				}
			}, {
				field : 'sourceBillNo',
				title : '源单单号',
				width : 100,
				formatter : function(value, row, index)
				{
					return idTransToUrl(row.sourceId, value);
				}
			}, {
				field : 'material',
				title : '材料/产品/工序',
				width : 100,
				formatter : function(value, row, index)
				{
					if (row.materialName)
					{
						return row.materialName;
					} else if (row.productName)
					{
						return row.productName;
					} else if (row.procedureName)
					{
						return row.procedureName;
					}
				}
			}, {
				field : 'style',
				title : '规格',
				width : 100
			}, {
				field : 'sourceMoney',
				title : '源单金额',
				width : 100
			}, {
				field : 'sourceBalanceMoney',
				title : '余额',
				width : 100
			}, {
				field : 'money',
				title : '本次付款金额',
				width : 120
			} ],
			onLoadSuccess : function()
			{
				// alert("数据加载完成");
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
		/* 返回显示列表 */
		$("#btn_back").on("click", function()
		{
			var url = Helper.basePath + '/finance/writeoffPayment/list';
			var title = "付款核销单列表";
			admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"))
		});
		//审核
		$("#btn_audit").on("click", function()
		{
			var order_id = $("#order_id").val();
			Helper.post(Helper.basePath + '/finance/writeoffPayment/audit/' + order_id, function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
					location.href = Helper.basePath + "/finance/writeoffPayment/view/" + order_id;
				} else
				{
					Helper.message.warn(data.message);
				}
			});
		});
		//反审核
		$("#btn_audit_cancel").on("click", function()
		{
			var order_id = $("#order_id").val();
			Helper.post(Helper.basePath + '/finance/writeoffPayment/audit_cancel/' + order_id, function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
					location.href = Helper.basePath + "/finance/writeoffPayment/view/" + order_id;
				} else
				{
					Helper.message.warn(data.message);
				}
			});
		});
		//作废
		$("#btn_cancel").on("click", function()
		{
			var order_id = $("#order_id").val();
			Helper.post(Helper.basePath + '/finance/writeoffPayment/cancel/' + order_id, function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
					location.href = Helper.basePath + "/finance/writeoffPayment/view/" + order_id;
				} else
				{
					Helper.message.warn(data.message);
				}
			});
		});
		//反作废
		$("#btn_cancel_back").on("click", function()
		{
			var order_id = $("#order_id").val();
			Helper.post(Helper.basePath + '/finance/writeoffPayment/cancel_back/' + order_id, function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
					location.href = Helper.basePath + "/finance/writeoffPayment/view/" + order_id;
				} else
				{
					Helper.message.warn(data.message);
				}
			});
		});

		/* 打印*/
		$("#btn_print").loadTemplate('FINALCE_FKHX', '/finance/writeoffPayment/printAjax/${id}');
		
		// 表格列拖动效果
		bootstrapTable_ColDrag($("#detailList"));
	});
</script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="财务管理-付款核销单-查看"></sys:nav>
				</div>
				<div class="top_nav">
					<button class="nav_btn table_nav_btn" id="btn_back">返回</button>
					<c:if test="${order.isForceComplete eq 'NO'}">
						<c:if test="${order.isCheck eq 'NO'}">
							<shiro:hasPermission name="finance:writeoffPayment:audit">
								<button class="nav_btn table_nav_btn" id="btn_audit">审核</button>
							</shiro:hasPermission>
							<c:if test="${order.isCancel eq 'NO'}">
								<shiro:hasPermission name="finance:writeoffPayment:cancel">
									<button class="nav_btn table_nav_btn" id="btn_cancel">作废</button>
								</shiro:hasPermission>
							</c:if>
							<c:if test="${order.isCancel eq 'YES'}">
								<shiro:hasPermission name="finance:writeoffPayment:cancel_back">
									<button class="nav_btn table_nav_btn" id="btn_cancel_back">反作废</button>
								</shiro:hasPermission>
							</c:if>

						</c:if>
						<c:if test="${order.isCheck eq 'YES'}">
							<c:if test="${order.isCancel eq 'NO'}">
								<shiro:hasPermission name="finance:writeoffPayment:audit_cancel">
									<button class="nav_btn table_nav_btn" id="btn_audit_cancel">反审核</button>
								</shiro:hasPermission>
							</c:if>
						</c:if>
					</c:if>
					<shiro:hasPermission name="finance:writeoffPayment:print">
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

			<input type="hidden" name="id" id="order_id" value="${order.id }" />
			<!--主表-订单表单-->
			<div class="form-container">
				<div class="form-wrap">
					<div class="cl form_content">
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui">核销单号：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_3s" readonly="readonly" value="${order.billNo }" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_6">供应商名称：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_7" readonly="readonly" value="${order.supplierName }" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">付款日期：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_1" readonly="readonly" value="<fmt:formatDate value="${order.billTime}" type="date" pattern="yyyy-MM-dd" />" />
								</span>
							</dd>
						</dl>
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_1">核销人员：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_3s" readonly="readonly" value="${fns:basicInfo('EMPLOYEE',order.employeeId).name}" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_6">核销金额：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_7" readonly="readonly" value="${order.money}" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">折扣金额：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_1" readonly="readonly" value="${order.discount}" />
								</span>
							</dd>
						</dl>
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_1">备 注：</label>
								<span class="form_textarea">
									<textarea class="noborder" readonly="readonly" style="width: 552px">${order.memo }</textarea>
								</span>
							</dd>
						</dl>
					</div>
				</div>
			</div>
			<!--从表-订单表格-->

			<!--表格部分Start-->
			<div class="table-view">
				<table class="border-table resizable" id="detailList">
				</table>
			</div>
			<!--表格部分End-->

			<div class="cl form_content">
				<dl class="cl row-dl-foot">
					<dd class="row-dd">
						<label class="form-label label_ui label_1">制 单 人：</label>
						<span class="ui-combo-wrap">
							<input type="text" class="input-txt input-txt_1" readonly="readonly" value="${order.createName}" />
						</span>
					</dd>
					<dd class="row-dd">
						<label class="form-label label_ui label_1">制单日期：</label>
						<span class="ui-combo-wrap">
							<input type="text" class="input-txt input-txt_1" readonly="readonly" value="<fmt:formatDate value="${order.createTime }" type="date" pattern="yyyy-MM-dd" />" />
						</span>
					</dd>
					<dd class="row-dd">
						<label class="form-label label_ui label_1">审 核 人：</label>
						<span class="ui-combo-wrap">
							<input type="text" class="input-txt input-txt_1" readonly="readonly" value="${order.checkUserName}" />
						</span>
					</dd>
					<dd class="row-dd">
						<label class="form-label label_ui label_1">审核日期：</label>
						<span class="ui-combo-wrap">
							<input type="text" class="input-txt input-txt_1" readonly="readonly" value="<fmt:formatDate value="${order.checkTime }" type="date" pattern="yyyy-MM-dd" />" />
						</span>
					</dd>
				</dl>
			</div>
			<c:if test="${order.isCheck eq 'YES'}">
				<c:if test="${order.isCheck eq 'YES'}">
					<c:if test="${order.isCancel eq 'YES'}">
						<div class="disuse">
							<span class="disuse_font">已作废</span>
						</div>
					</c:if>
					<c:if test="${order.isCancel eq 'NO'}">
						<div class="review">
							<span class="review_font">已审核</span>
						</div>
					</c:if>
				</c:if>
			</c:if>

		</div>
	</div>
</body>
</html>
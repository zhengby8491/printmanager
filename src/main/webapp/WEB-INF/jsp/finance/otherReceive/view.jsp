<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>查看其它收款单</title>
</head>
<script type="text/javascript">
	$(function()
	{
		//订单详情table
		$("#detailList").bootstrapTable({
			data:${fns:toJson(order.detailList)},
			showColumns: true, //是否显示所有的列
			minimumCountColumns: 2, //最少允许的列数
			striped : true, // 是否显示行间隔色
			cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination : false, // 是否显示分页（*）
			sortable : false, // 是否启用排序
			clickToSelect : true, // 是否启用点击选中行
			cardView : false, // 是否显示详细视图
			detailView : false, // 是否显示父子表
			cookie : true,//是否启用COOKIE
			cookiesEnabled:['bs.table.columns'],//默认会记住很多属性，这里控制只记住列选择属性
			cookieIdTable : "print_finance_payment_master_view",//必须制定唯一的表格cookieID
			columns : [ {
				field : 'index',
				title : '序号',
				width : 40,
				formatter : function(value, row, index)
				{
					return (index + 1);
				}
			},{
				field : 'summary',
				title : '摘要',
				width : 200
			},
			{
				field : 'money',
				title : '付款金额',
				width : 40
			}, {
				field : 'memo',
				'class' : 'memoView',
				title : '备注',
				width : 120
			} ],
			onLoadSuccess : function()
			{

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
		$("#btn_back").on("click",function(){
			var url=Helper.basePath + '/finance/otherReceive/list';
			var title="其它收款单列表";
			admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"))
		});
		
		// 审核
		$("#btn_audit").on("click",function(){
			var order_id=$("#order_id").val();
			Helper.post(Helper.basePath + '/finance/otherReceive/audit/' + order_id,function(data) {
				if(data.success){
					Helper.message.suc(data.message);
					location.href=Helper.basePath+"/finance/otherReceive/view/"+ order_id;
				}else
				{
					Helper.message.warn(data.message);
				}
			});	
		});
		
		//反审核
		$("#btn_audit_cancel").on("click",function(){
			var order_id=$("#order_id").val();
			Helper.post(Helper.basePath + '/finance/otherReceive/audit_cancel/' + order_id,function(data) {
				if(data.success){
					Helper.message.suc(data.message);
					location.href=Helper.basePath+"/finance/otherReceive/view/"+ order_id;
				}else
				{
					Helper.message.warn(data.message);
				}
			});		
		});
		
		/* 编辑 */
		$("#btn_edit").on("click",function(){
			var order_id=$("#order_id").val();
		 	var url = Helper.basePath + '/finance/otherReceive/edit/' + order_id;
			var title = "其它收款单";
			admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
		});
		
		// 删除
		$("#btn_del").on("click",function(){
			var order_id=$("#order_id").val();
			Helper.message.confirm('确认要删除吗？', function(index)
			{
				Helper.post(Helper.basePath + '/finance/otherReceive/del/' + order_id, function(data)
				{
					if (data.success)
					{
						Helper.message.suc("已删除！");
						closeTabAndJump("其它收款单列表");
						/* var url=Helper.basePath + '/finance/otherReceive/list/';
						var title="其它收款单列表";
						admin_tab($("<a _href='"+url+"' data-title='"+title+"' />")) */
					} else
					{
						Helper.message.warn(data.message);
					}
				});
			});
		});
		
		//打印模板加载
		$("#btn_print").loadTemplate('FINALCE_OSK', '/finance/otherReceive/printAjax/${order.id}');
	
		// 表格列拖动效果
		bootstrapTable_ColDrag($("#detailList"));
	});
	
</script>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="财务管理-其它收款单-查看"></sys:nav>
				</div>
				<div class="top_nav">
					<button class="nav_btn table_nav_btn" id="btn_back">返回</button>
					<c:if test="${order.isForceComplete eq 'NO'}">
						<c:if test="${order.isCheck eq 'NO'}">
							<shiro:hasPermission name="finance:otherReceive:edit">
								<button class="nav_btn table_nav_btn" id="btn_edit">修改</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="finance:otherReceive:audit">
								<button class="nav_btn table_nav_btn" id="btn_audit">审核</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="finance:otherReceive:delete">
								<button class="nav_btn table_nav_btn" id="btn_del">删除</button>
							</shiro:hasPermission>
						</c:if>
						<c:if test="${order.isCheck eq 'YES'}">
							<c:if test="${order.isCancel eq 'NO'}">
								<shiro:hasPermission name="finance:otherReceive:audit_cancel">
									<button class="nav_btn table_nav_btn" id="btn_audit_cancel">反审核</button>
								</shiro:hasPermission>
							</c:if>
						</c:if>
					</c:if>
					<shiro:hasPermission name="finance:otherReceive:print">
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
						<dl class="cl row-dl" style="width: 1250px">
							<dd class="row-dd">
								<label class="form-label label_ui label_6">付款单位：</label>
								<span class="ui-combo-wrap wrap-width">
									<input type="text" class="input-txt input-txt_7" readonly="readonly" name="recCompany" id="recCompany" value="${order.recCompany }" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">账 号：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_7" readonly="readonly" value="${fns:basicInfo('ACCOUNT',order.accountId).bankNo}" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">收 款 人：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_1" readonly="readonly" value="${fns:basicInfo('EMPLOYEE',order.employeeId).name}" />
								</span>
							</dd>
						</dl>
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_6">结算方式：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_16" readonly="readonly" value="${fns:basicInfo('SETTLEMENTCLASS',order.settlementClassId).name}" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">收款日期：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_7" readonly="readonly" value="<fmt:formatDate value="${order.billTime}" type="date" pattern="yyyy-MM-dd" />" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">收款金额：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_1" readonly="readonly" name="money" id="order_money" value="${order.money }" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">收款单号：</label>
								<span class="ui-combo-wrap">
									<input type="text" class="input-txt input-txt_1" readonly="readonly" name="money" id="order_money" value="${order.billNo }" />
								</span>
							</dd>
						</dl>
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_6">备注：</label>
								<span class="form_textarea">
									<textarea class="noborder" readonly="readonly" style="width: 898px">${order.memo }</textarea>
								</span>
							</dd>
						</dl>
					</div>
				</div>
			</div>
			<!--从表-订单表格-->
			<!--表格Start-->
			<div class="table-view">
				<table class="border-table resizable" id="detailList"></table>
			</div>
			<!--表格End-->
			<div class="cl form_content">
				<dl class="cl row-dl-foot">
					<dd class="row-dd">
						<label class="form-label label_ui label_7">制 单 人：</label>
						<span class="ui-combo-wrap" class="form_text">
							<input type="text" class="input-txt input-txt_3" readonly="readonly" value="${order.createName}" />
						</span>
					</dd>
					<dd class="row-dd">
						<label class="form-label label_ui label_7">制单日期：</label>
						<span class="ui-combo-wrap" class="form_text">
							<input type="text" class="input-txt input-txt_1" readonly="readonly" value="<fmt:formatDate value="${order.createTime }" type="date" pattern="yyyy-MM-dd" />" />
						</span>
					</dd>
					<dd class="row-dd">
						<label class="form-label label_ui label_7">审 核 人：</label>
						<span class="ui-combo-wrap" class="form_text">
							<input type="text" class="input-txt input-txt_1" readonly="readonly" value="${order.checkUserName}" />
						</span>
					</dd>
					<dd class="row-dd">
						<label class="form-label label_ui label_7">审核日期：</label>
						<span class="ui-combo-wrap" class="form_text">
							<input type="text" class="input-txt input-txt_1" readonly="readonly" value="<fmt:formatDate value="${order.checkTime }" type="date" pattern="yyyy-MM-dd" />" />
						</span>
					</dd>

				</dl>
			</div>
			<!-- 审核 -->
			<c:if test="${order.isCheck eq 'YES'}">
				<c:if test="${order.isCancel eq 'NO'}">
					<div class="review">
						<span class="review_font">已审核</span>
					</div>
				</c:if>
			</c:if>

		</div>
	</div>

</body>
</html>
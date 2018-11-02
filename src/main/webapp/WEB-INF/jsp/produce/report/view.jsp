<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<style>
#productList_div .fold_table table tbody tr:nth-child(4n) {
	background: #fff;
}

#productList_div .fold_table table tbody .active {
	background-color: #D6EDF3 !important;
}

#productList_div {
	border: none;
}

#productList_div .product_table thead tr th {
	font-family: arial, "宋体", "微软雅黑", "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
	font-size: 12px;
	font-weight: bold;
}

#productList_div .fold_table tbody tr {
	background-color: #f9f9f9;
}
</style>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>查看产量上报</title>
<script type="text/javascript" src="${ctxStatic}/site/produce/report/view.js"></script>
</head>
<body>

	<input id="order_id" value="${workReport.id }" type="hidden" />
	<input id="order_billType" value="${workReport.billType }" type="hidden" />

	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="生产管理-产量上报-查看"></sys:nav>
				</div>
				<div class="top_nav">
					<button class="nav_btn table_nav_btn" id="btn_back">返回</button>
					<c:if test="${workReport.isForceComplete eq 'NO'}">

						<c:if test="${workReport.isCheck eq 'NO'}">
							<shiro:hasPermission name="report:work:edit">
								<button class="nav_btn table_nav_btn" id="btn_edit">修改</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="report:work:audit">
								<button class="nav_btn table_nav_btn" id="btn_audit">审核</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="report:work:del">
								<button class="nav_btn table_nav_btn" id="btn_del">删除</button>
							</shiro:hasPermission>
						</c:if>
						<c:if test="${workReport.isCheck eq 'YES'}">
							<shiro:hasPermission name="report:work:auditCancel">
								<button class="nav_btn table_nav_btn" id="btn_audit_cancel">反审核</button>
							</shiro:hasPermission>
						</c:if>
						<shiro:hasPermission name="report:work:complete">
							<button class="nav_btn table_nav_btn" id="btn_complete">强制完工</button>
						</shiro:hasPermission>
					</c:if>
					<c:if test="${workReport.isForceComplete eq 'YES'}">
						<shiro:hasPermission name="report:work:completeCancel">
							<button class="nav_btn table_nav_btn" id="btn_complete_cancel">取消强制完工</button>
						</shiro:hasPermission>
					</c:if>



				</div>
			</div>

			<input type="hidden" name="id" id="id" />

			<!--表单部分上-->
			<div class="form-container">
				<div class="form-wrap">
					<div class="cl form_content">
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_1">上报单号：</label>
								<span class="ui-combo-noborder">
									<input type="text" readonly="readonly" class="input-txt input-txt_3s" value="${workReport.billNo}" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">员工信息：</label>
								<span class="ui-combo-noborder">
									<input type="text" readonly="readonly" class="input-txt input-txt_3s" value="${workReport.employeeName}" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">审核时间：</label>
								<span class="ui-combo-noborder">
									<input type="text" readonly="readonly" class="input-txt input-txt_3s" value="<fmt:formatDate
										value="${workReport.checkTime}" type="date" pattern="yyyy-MM-dd" />" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">审核人：</label>
								<span class="ui-combo-wrap">
									<input type="text" readonly="readonly" class="input-txt input-txt_3s" value="${workReport.checkUserName}" />
								</span>
							</dd>
						</dl>
						<dl class="cl row-dl">
							<dd class="row-dd">
								<label class="form-label label_ui label_1">备注：</label>
								<span class="ui-combo-wrap">
									<input type="text" readonly="readonly" class="input-txt input-txt_3s" value="${workReport.memo}" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">上报日期：</label>
								<span class="ui-combo-wrap">
									<input type="text" readonly="readonly" class="input-txt input-txt_3s" value="<fmt:formatDate
										value="${workReport.reportTime}" type="date" pattern="yyyy-MM-dd" />" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">创建日期：</label>
								<span class="ui-combo-wrap">
									<input type="text" readonly="readonly" class="input-txt input-txt_3s" value="<fmt:formatDate
										value="${workReport.createTime}" type="date" pattern="yyyy-MM-dd" />" />
								</span>
							</dd>
							<dd class="row-dd">
								<label class="form-label label_ui label_1">创建人：</label>
								<span class="ui-combo-wrap">
									<input type="text" readonly="readonly" class="input-txt input-txt_3s" value="${workReport.createName}" />
								</span>
							</dd>
						</dl>
					</div>
				</div>
			</div>
			<!--从表-订单表格-->

			<!--表格Start-->
			<div class="cl mr_t">

				<!--上报信息Start-->
				<form action="" id="form_report">
					<div class="fold_wrap" id="productList_div">
						<div class="fold_table" style="overflow-x: auto; height: 450px; border: 1px solid #ccc;">
							<input type="hidden" name="isCheck" id="isCheck" value="${workReport.isCheck}" />
							<table class="work_table resizable product_table" id="product_table">
								<thead>
									<tr>
										<th width="50" style="border-left: none">序号</th>
										<th width="100">生产单号</th>
										<th width="160">客户名称</th>
										<th width="100">客户料号</th>
										<th width="120">产品名称</th>
										<th width="100">产品规格</th>
										<th width="60">生产数量</th>
										<th width="60">部件名称</th>
										<th width="120">工序名称</th>
										<th width="80">应产数</th>
										<th width="80">上报数</th>
										<th width="60">合格数</th>
										<th width="60">不合格数</th>
										<th width="80">上机规格</th>
										<th width="150">开始时间</th>
										<th width="150">结束时间</th>
										<th width="150" style="border-right: none">备注</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="report" items="${workReport.reportList}" varStatus="vs">
										<input type="hidden" name="reportList.taskId" value="${report.id}" />
										<tr>
											<td style="border-left: none">
												<input class="tab_input" type="text" readonly="readonly" value="${vs.index+1}" />
											</td>
											<td>
												<input class="tab_input" type="text" name="reportList.billNo" readonly="readonly" value="${report.task.billNo}" />
											</td>
											<td>
												<input class="tab_input" type="text" name="" readonly="readonly" value="${report.task.customerName}" />
											</td>
											<td>
												<input class="tab_input" type="text" name="" readonly="readonly" value="${report.task.customerMaterialCode}" />
											</td>
											<td>
												<input class="tab_input" type="text" name="" readonly="readonly" value="${report.task.productName}" />
											</td>
											<td>
												<input class="tab_input" type="text" name="" readonly="readonly" value="${report.task.specifications}" />
											</td>
											<td>
												<input class="tab_input" type="text" name="" readonly="readonly" value="${report.task.produceQty}" />
											</td>
											<td>
												<input class="tab_input" type="text" name="" readonly="readonly" value="${report.task.partName}" />
											</td>
											<td>
												<input class="tab_input" type="text" name="" readonly="readonly" value="${report.task.procedureName}" />
											</td>
											<td>
												<input class="tab_input " type="text" readonly="readonly" name="" value="${report.task.yieldQty}" />
											</td>
											<td>
												<input class="tab_input " type="text" readonly="readonly" name="reportList.reportQty" value="${report.reportQty}" />
											</td>
											<td>
												<input class="tab_input" type="text" readonly="readonly" name="reportList.qualifiedQty" value="${report.qualifiedQty}" />
											</td>
											<td>
												<input class="tab_input" type="text" readonly="readonly" name="reportList.unqualified" value="${report.unqualified}" />
											</td>
											<td>
												<input class="tab_input" type="text" readonly="readonly" name="" readonly="readonly" value="${report.task.style}" />
											</td>
											<td>
												<input type="text" class="tab_input" readonly="readonly" value="<fmt:formatDate
										value="${report.startTime}" type="date" pattern="yyyy-MM-dd hh:mm:ss" />" />
											</td>
											<td>
												<input type="text" class="tab_input" readonly="readonly" value="<fmt:formatDate
										value="${report.endTime }" type="date" pattern="yyyy-MM-dd hh:mm:ss" />" />
											</td>
											<td style="border-right: none">
												<input class="tab_input" readonly="readonly" type="text" name="reportList.memo" value="${report.memo}" />
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</form>
				<!--上报信息End-->

			</div>
			<!--表格End-->

			<!--审核标签-->
			<c:if test="${workReport.isCheck eq 'YES'}">
				<div class="review">
					<span class="review_font">已审核</span>
				</div>
			</c:if>
		</div>
	</div>
	<script>
		//$.each('.fold_table table tbody tr',function(){
		
			$('.fold_table table tbody tr').on('click',function(){
				// console.log($(this))
				$(this).addClass('active').siblings().removeClass('active')
			})
		//})
	</script>
</body>
</html>

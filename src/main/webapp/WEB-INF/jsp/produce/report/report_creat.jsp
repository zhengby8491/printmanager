<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic }/site/produce/report/report.js?v=${v}"></script>
<title>产量上报</title>
<style type="text/css">
#productList_div .fold_table table tbody tr:nth-child(4n) {
	
}

#productList_div .fold_table table tbody .active {
	background-color: #D6EDF3;
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
	background-color: #F1F1F1;
}
</style>
<script type="text/javascript" src="${ctxStatic}/site/produce/report/report_creat.js"></script>
</head>


<body>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="生产管理-产量上报-创建"></sys:nav>
				</div>

				<div class="top_nav">
					<button class="nav_btn table_nav_btn" id="btn_save">保存</button>
					<button class="nav_btn table_nav_btn" id="btn_save_audit">保存并审核</button>
					<button class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
				</div>

			</div>
			<form action="" id="form_report">
				<!--表单部分上-->
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">上报单号：</label>
									<span class="ui-combo-noborder">
										<input type="text" readonly="readonly" class="input-txt input-txt_3s" value="" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui">员工信息：</label>
									<span class="ui-combo-wrap wrap-width">
										<input type="text" class="input-txt input-txt_3s" id="employee" name="employeeName" />
										<div class="select-btn" id="employee_quick_select">...</div>
										<input type="hidden" id="employeeId" name="employeeId" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">审核时间：</label>
									<span class="ui-combo-noborder">
										<input type="text" readonly="readonly" class="input-txt input-txt_3s" value="" id="auditDate" name="checkTime" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">审核人：</label>
									<span class="ui-combo-wrap">
										<input type="text" readonly="readonly" class="input-txt input-txt_3s" value="" name="checkUserName" />
									</span>
								</dd>

							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">备注：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_3s" value="" name="memo" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">上报日期：</label>
									<span class="ui-combo-wrap">
										<input style="width: 142px;" type="text" class="input-txt input-txt_3s Wdate" name="reportTime" value="" id="reportDate" onClick="WdatePicker({maxDate:'%y-%M-%d'})" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">创建日期：</label>
									<span class="ui-combo-wrap">
										<input type="text" readonly="readonly" class="input-txt input-txt_3s" value="" id="createDate" name="createTime" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">创建人：</label>
									<span class="ui-combo-wrap">
										<input type="text" readonly="readonly" class="input-txt input-txt_3s" value="${fns:getUserEmployeeName()}" name="createName" />
									</span>
								</dd>
							</dl>
						</div>
					</div>
				</div>
				<!--表格部分-->
				<div class="cl mr_t">

					<!--上报信息Start-->
					<div class="fold_wrap" id="productList_div">
						<div class="fold_table" style="overflow-x: auto; height: 450px; border: 1px solid #ccc;"">
							<input type="hidden" name="isCheck" id="isCheck" />
							<table class="work_table resizable product_table" id="product_table">
								<thead>
									<tr>
										<th width="50" style="border-left: none">序号</th>
										<th width="40">操作</th>
										<th width="100">生产单号</th>
										<th width="160">客户名称</th>
										<th width="100">客户料号</th>
										<th width="120">产品名称</th>
										<th width="100">产品规格</th>
										<th width="60">生产数量</th>
										<th width="60">部件名称</th>
										<th width="60">工序名称</th>
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
									<c:forEach var="report" items="${reportList}" varStatus="vs">
										<tr>
											<td style="border-left: none">
												<input class="tab_input" type="text" readonly="readonly" value="${vs.index+1}" />
											</td>
											<td class="td-manage">
												<input type="hidden" name="reportList.taskId" value="${report.id}" />
												<i title="删除行" class="delete fa fa-trash-o"></i>
											</td>
											<td>
												<input class="tab_input" type="text" name="reportList.billNo" readonly="readonly" value="${report.billNo}" />
											</td>
											<td>
												<input class="tab_input" type="text" name="" readonly="readonly" value="${report.customerName}" />
											</td>
											<td>
												<input class="tab_input" type="text" name="" readonly="readonly" value="${report.customerMaterialCode}" />
											</td>
											<td>
												<input class="tab_input" type="text" name="" readonly="readonly" value="${report.productName}" />
											</td>
											<td>
												<input class="tab_input" type="text" name="" readonly="readonly" value="${report.specifications}" />
											</td>
											<td>
												<input class="tab_input" type="text" name="" readonly="readonly" value="${report.produceQty}" />
											</td>
											<td>
												<input class="tab_input" type="text" name="" readonly="readonly" value="${report.partName}" />
											</td>
											<td>
												<input class="tab_input" type="text" name="" readonly="readonly" value="${report.procedureName}" />
											</td>
											<td>
												<input class="tab_input " type="text" readonly="readonly" name="yieldQty" value="${report.yieldQty}" />
											</td>
											<td>
												<input name="unreportQty" type="hidden" value="${report.unreportQty}" />
												<input class="tab_input bg_color  <c:if test="${report.productType ne 'ROTARY'}">constraint_negative</c:if> <c:if test="${report.productType eq 'ROTARY'}">constraint_decimal_negative</c:if>" type="text" name="reportList.reportQty" value="${report.unreportQty}" />
											</td>
											<td>
												<input class="tab_input" readonly="readonly" type="text" name="reportList.qualifiedQty" value="${report.unreportQty}" />
											</td>
											<td>
												<input class="tab_input bg_color  <c:if test="${report.productType ne 'ROTARY'}">constraint_negative</c:if> <c:if test="${report.productType eq 'ROTARY'}">constraint_decimal_negative</c:if>" type="text" name="reportList.unqualified" value="0" />
											</td>
											<td>
												<input class="tab_input" type="text" name="" readonly="readonly" value="${report.style}" />
											</td>
											<td>
												<input type="text" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endTime${vs.index}\')}'})" class="tab_input bg_color Wdate" id="startTime${vs.index}" name="reportList.startTime" value="" />
											</td>
											<td>
												<input type="text" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startTime${vs.index}\')}'})" class="tab_input bg_color Wdate" id="endTime${vs.index}" name="reportList.endTime" value="" />
											</td>
											<td style="border-right: none">
												<input class="tab_input bg_color memo" type="text" name="reportList.memo" value="${report.memo}" />
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
	</div>
	</div>
</body>
</html>
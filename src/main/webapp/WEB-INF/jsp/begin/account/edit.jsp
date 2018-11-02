<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/begin/account/edit.js?v=${v }"></script>
<title>账户期初修改</title>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="系统管理-期初设置-账户期初"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="begin:account:edit">
						<button id="btn_save" class="nav_btn table_nav_btn">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="begin:account:edit,begin:account:audit">
						<button class="nav_btn table_nav_btn" id="btn_save_audit">保存并审核</button>
					</shiro:hasManyPermissions>
					<button id="btn_cancel" class="nav_btn table_nav_btn">取消</button>
				</div>
			</div>

			<form id="form_order" method="post">
				<input type="hidden" name="id" value="${order.id }" />
				<input type="hidden" name="isCheck" id="isCheck" />
				<!--主表-订单表单-->
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_1">单据编号：</label>
									<span class="ui-combo-wrap">
										<input type="text" readonly="readonly" name="billNo" class="input-txt input-txt_3" value="${order.billNo }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">期初日期：</label>
									<span class="ui-combo-wrap">
										<input type="text" id="beginTime" name="beginTime" class="input-txt input-txt_3 Wdate" onFocus="WdatePicker({lang:'zh-cn'})" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${order.beginTime }' type='date' />" />
									</span>
								</dd>

								<dd class="row-dd">
									<label class="form-label label_ui label_1">制 单 人：</label>
									<span class="ui-combo-noborder">
										<input type="text" readonly="readonly" class="input-txt input-txt_1" value="${order.createName }" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">制单日期：</label>
									<span class="ui-combo-wrap">
										<input type="text" readonly="readonly" class="input-txt input-txt_3" value="<fmt:formatDate pattern="yyyy-MM-dd"  value='${order.createTime }' type='date' />" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">审核日期：</label>
									<span class="ui-combo-wrap">
										<input type="text" readonly="readonly" class="input-txt input-txt_3" value="" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">审 核 人：</label>
									<span class="ui-combo-noborder">
										<input type="text" readonly="readonly" class="input-txt input-txt_1" value="" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">备注：</label>
									<span class="form_textarea">
										<textarea class="noborder" name="memo" style="width: 1020px">${order.memo }</textarea>
									</span>
								</dd>
							</dl>
						</div>
					</div>
				</div>

				<!-- 按钮栏Start -->
				<div class="btn-billbar" id="toolbar">
					<a id="account_quick_select" class="nav_btn table_nav_btn">
						<i class="fa fa-plus-square"></i>
						选择账户
					</a>
				</div>
				<!--按钮栏End-->

				<!--从表-订单表格-->
				<div>
					<div class="table-container">
						<table class="border-table">
							<thead>
								<tr>
									<th width="60">序号</th>
									<th width="100">操作</th>
									<th width="250">账户</th>
									<th width="180">支行名称</th>
									<th width="80">币别</th>
									<th width="100">期初金额</th>
									<th width="200">备注</th>
								</tr>
							</thead>
							<tbody id="tbody">
								<c:forEach var="item" items="${order.detailList }" varStatus="status">
									<tr>
										<td name="rowIndex"></td>
										<td class="td-manage">
											<a title="删除行" href="javascript:void(0)" class="row_delete">
												<i class="delete fa fa-trash-o"></i>
											</a>
										</td>
										<td>
											<input name="detailList.bankNo" class="tab_input" type="text" readonly="readonly" value="${item.bankNo }" />
										</td>
										<td>
											<input name="detailList.branchName" class="tab_input" onmouseover="this.title=this.value" readonly="readonly" type="text" value="${item.branchName }" />
											<input name="detailList.id" class="tab_input" type="hidden" readonly="readonly" value="${item.id }" />
											<input name="detailList.accountId" class="tab_input id_${item.accountId }" type="hidden" value="${item.accountId }" />
										</td>
										<td>
											<input name="detailList.currencyTypeText" class="tab_input" type="text" readonly="readonly" value="${item.currencyType.text }" />
											<input name="detailList.currencyType" class="tab_input" type="hidden" readonly="readonly" value="${item.currencyType }" />
										</td>
										<td>
											<input name="detailList.beginMoney" class="tab_input bg_color" type="text" value="${item.beginMoney }" />
										</td>
										<td>
											<input name="detailList.memo" class="tab_input bg_color memo" onmouseover="this.title=this.value" type="text" value="${item.memo }" />
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/offer/setting/profit.js?v=${v}"></script>
<print:body>
	<!-- 导航部分 -->
	<print:nav title="报价系统-报价设置-${offerType.text}-利润设置">
		<shiro:hasOfferPermissions name="profit:save" offerType="${offerType }">
			<button class="nav_btn table_nav_btn" id="btnSave">保存</button>
		</shiro:hasOfferPermissions>
		<button class="nav_btn table_nav_btn" id="btnCancel">取消</button>
	</print:nav>
	<print:listLeft>
		<%@include file="/WEB-INF/jsp/offer/setting/_menuSetting.jsp"%>
	</print:listLeft>
	<print:listRight>
		<!-- 表格部分 -->
		<print:table>
			<!-- 列表按钮 -->
			<print:listBtnBar>
				<div style="margin-bottom: 5px;">
					<phtml:list cssClass="input-txt input-txt_7 offerProftType" name="profit.offerProfitType" textProperty="text" type="com.huayin.printmanager.persist.enumerate.OfferProfitType" selected="${offerProfitType }" />
				</div>
			</print:listBtnBar>
			<print:tableContent formId="profit_form" tableId="profit_table">
				<thead>
					<tr>
						<th colspan="2" id="row_name"><span>数量范围</span></th>
						<th>利润百分比%</th>
						<th>固定金额</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<!-- 不为空 -->
					<c:if test="${settingProfitList != null && fn:length(settingProfitList) > 0 }">
						<c:forEach var="settingProfit" items="${settingProfitList }" varStatus="idx">
							<tr data-index="${idx.index }">
								<td><input class="constraint_negative input-txt input-txt_26 center" type="text" name="profit.rangeStart" value="${settingProfit.rangeStart }" readonly="readonly"></td>
								<td><input class="input-txt input-txt_26 <c:if test="${settingProfit.rangeEnd != null }">constraint_negative whiteBg</c:if>" type="text" name="profit.rangeEnd" value="${settingProfit.rangeEnd == null ? '无限大' : settingProfit.rangeEnd }" <c:if test="${settingProfit.rangeEnd == null }">readonly="readonly"</c:if>></td>
								<td><input class="input-txt input-txt_25 <c:if test="${settingProfit.percent != null }">whiteBg constraint_negative</c:if> valid_profit_percent" type="text" name="profit.percent" value="${settingProfit.percent }" <c:if test="${settingProfit.percent == null }">readonly="readonly"</c:if>> %</td>
								<td><input class="input-txt input-txt_26 <c:if test="${settingProfit.money != null }">whiteBg constraint_negative</c:if> valid_profit_fixed" type="text" name="profit.money" value="${settingProfit.money }" <c:if test="${settingProfit.money == null }">readonly="readonly"</c:if>></td>
								<td><c:if test="${not empty settingProfit.rangeEnd }">
										<a class="nav_btn table_nav_btn" onclick="add($(this))"><i class="fa fa-plus-square"></i></a>
										<a class="nav_btn table_nav_btn" onclick="del($(this))"><i class="fa fa-minus-square"></i></a>
									</c:if></td>
							</tr>
						</c:forEach>
					</c:if>
					<!-- 空数据 -->
					<c:if test="${settingProfitList != null && fn:length(settingProfitList) == 0 }">
						<tr data-index="0">
							<td><input class="constraint_negative input-txt input-txt_26 center" type="text" name="profit.rangeStart" value="0" readonly="readonly"></td>
							<td><input class="constraint_negative input-txt input-txt_26 whiteBg" type="text" name="profit.rangeEnd" value="5000"></td>
							<td><input class="constraint_negative input-txt input-txt_25 whiteBg valid_profit_percent" type="text" name="profit.percent" value=""> %</td>
							<td><input class="constraint_negative input-txt input-txt_26 whiteBg valid_profit_fixed" type="text" name="profit.money" value=""></td>
							<td><a class="nav_btn table_nav_btn" onclick="add($(this))"><i class="fa fa-plus-square"></i></a><a class="nav_btn table_nav_btn" onclick="del($(this))"><i class="fa fa-minus-square"></i></a></td>
						</tr>
						<tr data-index="1">
							<td><input class="constraint_negative input-txt input-txt_26 center" type="text" name="profit.rangeStart" value="5001" readonly="readonly"></td>
							<td><input class="input-txt input-txt_26 center" type="text" value="无限大" readonly="readonly"></td>
							<td><input class="constraint_negative input-txt input-txt_25 whiteBg valid_profit_percent" type="text" name="profit.percent"> %</td>
							<td><input class="constraint_negative input-txt input-txt_26 whiteBg valid_profit_fixed" type="text" name="profit.money"></td>
							<td></td>
						</tr>
					</c:if>
				</tbody>
			</print:tableContent>
		</print:table>
		<div style="margin-top: 10px; color: green">
			<span> 解析：
				<p>1. 利润百分比与固定金额二者只能输入其一，输入了利润百分百则固定金额为灰色只读不可输入，反之输入固定金额，利润百分百为灰色只读不可输入
				<p>2. 根据下拉框的内容显示是数量范围还是金额范围，如：下来选择的是按阶梯数量利润比计算，则显示的是数量范围的界面样式，反之则是金额范围显示界面样式
			</span>
		</div>
	</print:listRight>
</print:body>
</html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/offer/setting/waste.js?v=${v}"></script>
<print:body>
	<!-- 导航按钮 -->
	<print:nav title="报价系统-报价设置-损耗设置">
		<shiro:hasPermission name="offer:waste:save">
			<button class="nav_btn table_nav_btn" id="btnSave">保存</button>
		</shiro:hasPermission>
		<button class="nav_btn table_nav_btn" id="btnCancel">取消</button>
	</print:nav>

	<!-- 表单 -->
	<print:form></print:form>

	<!-- 表格部分 -->
	<print:table>
		<!-- 表格内容 -->
		<print:tableContent formId="settingWasteForm">
			<thead>
				<tr>
					<th width="60" colspan="6">纸张耗损数设置（按上机尺寸计算）损耗张数=每千印/版+起印张数/版，每一项后加工追加固定张数</th>
				</tr>
				<tr>
					<th width="60"></th>
					<th width="200" colspan="2">单色印刷</th>
					<th width="200" colspan="2">非单色印刷（双色以上印刷）</th>
					<th width="100">印后加工</th>
				</tr>
				<tr>
					<th></th>
					<th width="100px">起印张数/版</th>
					<th width="100px">每千印张数/版</th>
					<th width="100px">起印张数/版</th>
					<th width="100px">每千印张数/版</th>
					<th width="100px">追加张数/项</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="settingWaste" items="${settingWasteList }">
					<tr class="val">
						<td>${settingWaste.offerType.text }<input type="hidden" name="swList.id" value="${settingWaste.id }"> <input type="hidden" name="swList.memo" value="${settingWaste.memo }"> <input type="hidden" name="swList.sort" value="${settingWaste.sort }"> <input type="hidden" name="swList.offerType" value="${settingWaste.offerType }">
						</td>
						<td><input type="text" class="tab_input2 bg_color constraint_negative" name="swList.spStartSheetZQ" value="${settingWaste.spStartSheetZQ }" /> <span>&nbsp;张</span></td>
						<td><input type="text" class="tab_input2 bg_color constraint_negative" name="swList.spThousandSheetZQ" value="${settingWaste.spThousandSheetZQ }" /> <span>&nbsp;张</span></td>
						<td><input type="text" class="tab_input2 bg_color constraint_negative" name="swList.dpStartSheetZQ" value="${settingWaste.dpStartSheetZQ }" /> <span>&nbsp;张</span></td>
						<td><input type="text" class="tab_input2 bg_color constraint_negative" name="swList.dpThousandSheetZQ" value="${settingWaste.dpThousandSheetZQ }" /> <span>&nbsp;张</span></td>
						<td><input type="text" class="tab_input2 bg_color constraint_negative" name="swList.workAfter" value="${settingWaste.workAfter }" /> <span>&nbsp;张</span></td>
					</tr>
				</c:forEach>
			</tbody>
		</print:tableContent>
	</print:table>
</print:body>
</html>
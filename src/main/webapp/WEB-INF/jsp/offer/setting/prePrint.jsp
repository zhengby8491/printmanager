<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/offer/setting/prePrint.js?v=${v}"></script>
<style type="text/css">
table tr td input {
	text-align: center !important;
}
</style>
<print:body>

	<!-- 导航部分 -->
	<print:nav title="报价系统-报价设置-${offerType.text}-印前费用">
		<shiro:hasOfferPermissions name="preprint:save" offerType="${offerType }">
			<button class="nav_btn table_nav_btn" id="btn_save">保存</button>
		</shiro:hasOfferPermissions>
		<button class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
	</print:nav>
	<!-- 左竖向菜单部分 -->
	<print:listLeft>
		<%@include file="/WEB-INF/jsp/offer/setting/_menuSetting.jsp"%>
	</print:listLeft>

	<!-- 右部分 -->
	<print:listRight>
		<print:table>
			<print:tableContent formId="prePrint_form" useClass="false">
				<thead>
					<tr>
						<th width="180px;">出货时间</th>
						<th>运费</th>
					</tr>
				</thead>
				<input type="hidden" name="id" id="id" value="${offerPrePrint.id}">
				<input type="hidden" name="offerType" id="offerType" value="${offerType}">
				<input type="hidden" name="designFee" value="">
				<tbody>
					<tr>

						<td rowspan="2"><span> <input type="text" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'deliveryTimeMax\')||\'%y-%M-%d\'}'})" class="input-txt input-txt_0 Wdate whiteBg" id="deliveryTimeMin" name="deliveryTimeMin" value="<fmt:formatDate value="${offerPrePrint.deliveryTimeMin}" type="date" pattern="yyyy-MM-dd" />">
						</span>
							<p>
								<label class="label_2 align_c">至</label>
							</p> <span> <input type="text" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'deliveryTimeMin\')||\'%y-%M-%d\'}'})" class="input-txt input-txt_0 Wdate whiteBg" id="deliveryTimeMax" name="deliveryTimeMax" value="<fmt:formatDate value="${offerPrePrint.deliveryTimeMax}" type="date" pattern="yyyy-MM-dd" />">
						</span>
						</td>
						<!-- 运费 -->
						<td><span> <input type="checkbox" name="packingChk" id="packingChk" <c:if test="${offerPrePrint.packingChk=='YES' }">checked="checked"</c:if>> <label for="packingChk"> 包装费 <input type="text" name="packing" class="input-txt input-txt_25 constraint_decimal_negative	<c:if test="${offerPrePrint.packingChk=='YES'}">whiteBg</c:if> " <c:if test="${offerPrePrint.packingChk!='YES'}"> readonly="readonly"</c:if> value="${offerPrePrint.packing }" /> <span>元/</span> <input type="text" name="packingPer" class="input-txt input-txt_25 constraint_negative <c:if test="${offerPrePrint.packingChk=='YES'}">whiteBg</c:if> "
									<c:if test="${offerPrePrint.packingChk!='YES'}"> readonly="readonly"</c:if> value="${offerPrePrint.packingPer }" /> <span>个</span>
							</label>
						</span></td>
					</tr>
					<tr>
						<td><span style="margin-left: -72px;"> <input type="checkbox" name="freightChk" id="freightChk" <c:if test="${offerPrePrint.freightChk=='YES' }">checked="checked"</c:if>> <label for="freightChk"> 运费&nbsp;&nbsp;&nbsp;&nbsp; <input type="text" name="freight" class="input-txt input-txt_25 constraint_decimal_negative <c:if test="${offerPrePrint.freightChk =='YES' }">whiteBg</c:if>" <c:if test="${offerPrePrint.freightChk!='YES'}">readonly="readonly"</c:if> value="${offerPrePrint.freight }" />
							</label> <span>元</span>
						</span></td>
					</tr>
				</tbody>
			</print:tableContent>
		</print:table>
	</print:listRight>
</print:body>
</html>
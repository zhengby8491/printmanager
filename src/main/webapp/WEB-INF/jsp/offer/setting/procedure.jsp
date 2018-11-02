<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/offer/setting/procedure.js?v=${v}"></script>
<print:body>
	<!-- 导航按钮 -->
	<print:nav title="报价系统-报价设置-${offerType.text}-工序设置">
		<shiro:hasOfferPermissions name="procedure:save" offerType="${offerType }">
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
				<print:listBtn name="新增工序" icon="fa-plus-square" click="procedureCreate('${offerType}')"></print:listBtn>
				<print:listBtn name="添加选择工序" icon="fa-plus-square" click="procedureSyn()"></print:listBtn>
			</print:listBtnBar>

			<!-- 列表表格 -->
			<print:listTable>
				<thead>
					<tr>
						<th>序号</th>
						<th>工序类型</th>
						<th>工序分类</th>
						<th>工序名称</th>
						<th>单价/元</th>
						<th>单位</th>
						<th>每张最低单价/元</th>
						<th>起步价/元</th>
						<th>普通公式</th>
						<th>公式类型</th>
						<th>自定义公式</th>
						<th>状态</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="procedure" items="${list}" varStatus="p">
						<tr>
							<td>${p.index + 1 }</td>
							<td><input type="hidden" name="list.id" value="${procedure.id }"> <phtml:list name="list.procedureType" cssClass="tab_input bg_color" textProperty="text" type="com.huayin.printmanager.persist.enumerate.OfferProcedureType" selected="${procedure.procedureType }" /></td>
							<td><input class="tab_input bg_color" name="list.procedureClass" value="${procedure.procedureClass }"></td>
							<td><input class="tab_input bg_color" name="list.name" value="${procedure.name }"></td>
							<td><input class="tab_input bg_color constraint_decimal" name="list.price" value="${procedure.price }"></td>
							<td><phtml:list name="list.procedureUnit" cssClass="tab_input bg_color" textProperty="text" type="com.huayin.printmanager.persist.enumerate.ProcedureUnit" selected="${procedure.procedureUnit }"></phtml:list></td>
							<td>
								<!-- 只有单位选择了，元/m2或者元/指定m2时，才可以编辑输入，否则为只读 --> <input class="tab_input <c:if test="${procedure.procedureUnit == 'UNIT2' || procedure.procedureUnit == 'UNIT3' }">bg_color constraint_decimal</c:if> " name="list.lowestPrice" value="${procedure.lowestPrice }" <c:if test="${procedure.procedureUnit != 'UNIT2' && procedure.procedureUnit != 'UNIT3'  }">readonly="readonly"</c:if>>
							</td>
							<td><input class="tab_input bg_color constraint_decimal" name="list.startPrice" value="${procedure.startPrice }"></td>
							<td><input class="tab_input <c:if test="${procedure.offerProcedureFormulaType == 'NORMAL'}">bg_color</c:if> memoView" name="list.formulaText" value="${procedure.formulaText }" readonly="readonly"> <input type="hidden" name="list.formula" value="${procedure.formula }"></td>
							<td><phtml:list name="list.offerProcedureFormulaType" cssClass="tab_input bg_color" textProperty="text" type="com.huayin.printmanager.persist.enumerate.OfferProcedureFormulaType" selected="${procedure.offerProcedureFormulaType }"></phtml:list></td>
							<td><input class="tab_input <c:if test="${procedure.offerProcedureFormulaType == 'CUSTOM'}">bg_color</c:if>" name="list.customFormulaText" value="${procedure.customFormulaText }" <c:if test="${procedure.offerProcedureFormulaType == 'NORMAL'}">readonly="readonly"</c:if>> <input type="hidden" name="list.customFormula" value="${procedure.customFormula }"></td>
							<td><phtml:list name="list.switchStatus" cssClass="tab_input bg_color" textProperty="text" type="com.huayin.printmanager.persist.enumerate.SwitchStatus" selected="${procedure.switchStatus }"></phtml:list></td>
							<td><a title="复制" href="#" onclick="procedureCopy($(this))" style="margin-right: 10px"> <i class="fa fa-copy"></i>
							</a> <a title="删除" href="#" onclick="procedureDel($(this))"> <i class="fa fa-trash-o"></i>
							</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</print:listTable>
		</print:table>
	</print:listRight>

	<!-- 模板 -->
	<div class="hy_hide">
		<table id="template">
			<tbody>
				<tr>
					<td></td>
					<td><phtml:list name="list.procedureType" cssClass="tab_input bg_color" textProperty="text" type="com.huayin.printmanager.persist.enumerate.OfferProcedureType" /></td>
					<td><input class="tab_input bg_color" name="list.procedureClass"></td>
					<td><input class="tab_input bg_color" name="list.name"></td>
					<td><input class="tab_input bg_color constraint_decimal" name="list.price" value="0.00"></td>
					<td><phtml:list name="list.procedureUnit" cssClass="tab_input bg_color" textProperty="text" type="com.huayin.printmanager.persist.enumerate.ProcedureUnit"></phtml:list></td>
					<td>
						<!-- 只有单位选择了，元/m2或者元/指定m2时，才可以编辑输入，否则为只读 --> <input class="tab_input constraint_decimal" name="list.lowestPrice" readonly="readonly">
					</td>
					<td><input class="tab_input bg_color constraint_decimal" name="list.startPrice" value="0.00"></td>
					<td><input class="tab_input memoView" name="list.formulaText" readonly="readonly"> <input type="hidden" name="list.formula"></td>
					<td><phtml:list name="list.offerProcedureFormulaType" cssClass="tab_input bg_color" textProperty="text" type="com.huayin.printmanager.persist.enumerate.OfferProcedureFormulaType"></phtml:list></td>
					<td><input class="tab_input" name="list.customFormulaText" readonly="readonly"> <input type="hidden" name="list.customFormula"></td>
					<td><phtml:list name="list.switchStatus" cssClass="tab_input bg_color" textProperty="text" type="com.huayin.printmanager.persist.enumerate.SwitchStatus"></phtml:list></td>
					<td><a title="复制" href="#" onclick="procedureCopy($(this))" style="margin-right: 10px"> <i class="fa fa-copy"></i>
					</a> <a title="删除" href="#" onclick="procedureDel($(this))"> <i class="fa fa-trash-o"></i>
					</a></td>
				</tr>
			</tbody>
		</table>
	</div>
</print:body>
</html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<link rel="stylesheet" type="text/css" href="${ctxHYUI}/plugins/codemirror/codemirror.css" />
<script type="text/javascript" src="${ctxHYUI}/plugins/codemirror/codemirror.js"></script>
<script type="text/javascript" src="${ctxStatic}/site/offer/setting/procedureFormula.js?v=${v}"></script>
<body>
	<div class="page-container">
		<div class="page-border">
			<print:form>
				<!-- 自定义计价 -->
				<div class="formula_2content" id="custom">
					<!-- 公式显示处 -->
					<div class="formulaFont">
						<span>计算公式</span> <input type="text" class="input-txt input-txt_3" style="width: 800px; height: 30px; font-size: 16px;" id="formulaText" value="">
					</div>
					<div class="formula_content3 cl" style="position: relative">
						<fieldset id="calculator" class="calculator">
							<legend>计算器</legend>
							<p>
								<input type="button" value="1"> <input type="button" value="2"> <input type="button" value="3"> <input type="button" value="+">
							</p>
							<p>
								<input type="button" value="4"> <input type="button" value="5"> <input type="button" value="6"> <input type="button" value="-">
							</p>
							<p>
								<input type="button" value="7"> <input type="button" value="8"> <input type="button" value="9"> <input type="button" value="*">
							</p>
							<p>
								<input type="button" value="0"> <input type="button" value="00"> <input type="button" value="."> <input type="button" value="/">
							</p>
							<p>
								<input type="button" value="("> <input type="button" value=")"> <input type="button" value=">"> <input type="button" value="<">
							</p>
							<p>
								<input type="button" value=">="> <input type="button" value="<="> <input type="button" value="="> <input type="button" value="!=">
							</p>
							<p>
								<input type="button" value="{"> <input type="button" value="}"> <input type="button" value="||"> <input type="button" value="&amp;&amp;">
							</p>
							<p>
								<input type="button" value="if"> <input type="button" value="else"> <input type="button" value="result">
							</p>
						</fieldset>
						<fieldset class="param_fieldset">
							<legend>计算参数</legend>
							<p class="clear" id="div_contr_1"></p>
							<ul id="custom_params" class="custom_params">
								<phtml:list name="isPublic" textProperty="text" inputType="li" type="com.huayin.printmanager.persist.enumerate.ParamsLadderType"></phtml:list>
							</ul>
							<p></p>
						</fieldset>
						<div class="edit_area">
							<p class="formula_tip">价格总数用result表示，默认result=0 &nbsp;&nbsp;&nbsp;&nbsp;例:if([印张数量]<=1000){result=[印张数量]*0.4}
							<p>
								<textarea id="code" name="code" style="width: 100%"></textarea>
								<input type="hidden" id="custom_formula" value="" /> <input type="hidden" id="check_result" value="false" />
							<div>
								<div id="check_formula_tip" class="check_formula_tip"></div>
								<div class="btns">
									<input type="button" id="check" class="btn" value="校验" /> <input type="button" id="btnSave" class="btn save_btn" value="保存" /> <input type="button" id="btnCancel" class="btn cancel_btn" value="取消" />
								</div>
							</div>
						</div>
					</div>
				</div>
			</print:form>
		</div>
	</div>

</body>
</html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/basic/machine/edit.js?v=${v}"></script>
<title>编辑机台信息 - 机台信息管理</title>
</head>
<body>
	<div class="layer_container">
		<!--表单部分START-->
		<form id="jsonForm">
			<input type="hidden" id="id" name="id" value="${machine.id}">
			<div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							机台名称：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" id="name" name="name" value="${machine.name}">
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							生产厂商：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" id="manufacturer" name="manufacturer" value="${machine.manufacturer}">
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							规格型号：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" id="code" name="code" value="${machine.code}">
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							机台属性：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list name="machineType" textProperty="text" cssClass="hy_select2" type="com.huayin.printmanager.persist.enumerate.MachineType" selected="${machine.machineType}"></phtml:list>
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							设备金额：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7 constraint_decimal" id="money" name="money" value="${machine.money}">
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							标准产能：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7 constraint_negative" id="capacity" name="capacity" value="${machine.capacity}">
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							最大上机：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" id="maxStyle" name="maxStyle" value="${machine.maxStyle}">
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							最小上机：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" id="minStyle" name="minStyle" value="${machine.minStyle}">
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							最大印色：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<select id="colorQty" name="colorQty">
								<option value="1" <c:if test="${machine.colorQty==1 }">selected="selected"</c:if>>单色</option>
								<option value="2" <c:if test="${machine.colorQty==2 }">selected="selected"</c:if>>双色</option>
								<option value="4" <c:if test="${machine.colorQty==4 }">selected="selected"</c:if>>四色</option>
								<option value="5" <c:if test="${machine.colorQty==5 }">selected="selected"</c:if>>五色</option>
								<option value="6" <c:if test="${machine.colorQty==6 }">selected="selected"</c:if>>六色</option>
								<option value="7" <c:if test="${machine.colorQty==7 }">selected="selected"</c:if>>七色</option>
								<option value="8" <c:if test="${machine.colorQty==8 }">selected="selected"</c:if>>八色</option>
								<option value="10" <c:if test="${machine.colorQty==8 }">selected="selected"</c:if>>十色</option>
								<option value="12" <c:if test="${machine.colorQty==12 }">selected="selected"</c:if>>十二色</option>
							</select>
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							备注：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" id="memo" name="memo" value="${machine.memo}">
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<input class="nav_btn table_nav_btn" type="submit" style="margin-left: 140px; margin-top: 5px;" value="&nbsp;&nbsp;提交&nbsp;&nbsp;" />
					</div>
				</div>
			</div>
		</form>
		<!--表单部分END-->
	</div>
</body>
</html>
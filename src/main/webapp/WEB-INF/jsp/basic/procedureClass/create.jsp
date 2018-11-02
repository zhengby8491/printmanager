<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/basic/procedureClass/create.js?v=${v}"></script>
<title>添加工序分类</title>
</head>
<body>
	<div class="layer_container">
		<form id="jsonForm">
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					排序：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_7 constraint_number" value="${fns:nextSort('procedureClass')}" placeholder="" id="sort" name="sort">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					工序类型：
				</label>
				<div class="ui-combo-wrap form_text div_select_wrap">
					<phtml:list name="procedureType" textProperty="text" cssClass="hy_select2" type="com.huayin.printmanager.persist.enumerate.ProcedureType"></phtml:list>
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					工序分类名称：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_7" value="" placeholder="" id="name" name="name">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					产品分类：
				</label>
				<div class="ui-combo-wrap form_text div_select_wrap">
					<select style="" class="hy_select2 input-txt input-txt_3s" id="productType" name="productType">
						<option value="1" selected="selected">通用</option>
						<option value="2">包装印刷</option>
						<option value="3">书刊印刷</option>
					</select>
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">备注：</label>
				<div class="form_textarea_rlt">
					<textarea name="memo" id="memo" class="input-txt_7 " placeholder="说点什么...100个字符以内" dragonfly="true" onKeyUp="Helper.doms.textarealength(this,100)" style="height: 82px; resize: none"></textarea>
					<p class="textarea-numberbar">
						<em>0</em>
						/100
					</p>
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar"></label>
				<div class="form-label label_ui">
					<input class="nav_btn table_nav_btn" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
				</div>
			</div>
		</form>
	</div>
</body>
</html>
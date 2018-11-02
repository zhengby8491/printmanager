<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<link rel="stylesheet" type="text/css" href="${ctxHYUI}/plugins/zTree/css/zTreeStyle/zTreeStyle.css" />
<script type="text/javascript" src="${ctxHYUI}/plugins/zTree/js/jquery.ztree.core.js?v=${v }"></script>
<script type="text/javascript" src="${ctxStatic}/site/sys/menu/create.js?v=${v }"></script>
<title>菜单管理-创建菜单</title>
</head>
<body>
	<div class="layer_container">
		<!--表单部分START-->
		<form id="form-menu">
			<div>
				<div class="cl">
					<div class="row-div" style="margin-bottom: 13px">
						<label class="form-label label_ui label_4mar"> <span class="c-red">*</span> 上级菜单：
						</label>
						<div class="form_text error_tip">
							<sys:treeselect url="${ctx }/sys/menu/treeData" id="parentId" name="parentId" value="${parent.id }" showText="${parent.name }"></sys:treeselect>
						</div>
					</div>
					<span style="margin-left: 10px; line-height: 22px; color: green">*不选表示根目录</span>
				</div>
			</div>
			<div class="cl">
				<div class="row-div">
					<label class="form-label label_ui label_4mar"> <span class="c-red">*</span> 菜单ID：
					</label>
					<div class="ui-combo-wrap form_text error_tip">
						<input type="text" class="input-txt input-txt_7" required placeholder="" id="id" name="id" value="${newId }" />
					</div>
				</div>
				<div class="row-div">
					<label class="form-label label_ui label_4mar"> <span class="c-red">*</span> 菜单名称：
					</label>
					<div class="ui-combo-wrap form_text error_tip">
						<input type="text" class="input-txt input-txt_7" required placeholder="" id="name" name="name" />
					</div>
				</div>
			</div>
			<div class="cl">
				<div class="row-div">
					<label class="form-label label_ui label_4mar">链接：</label>
					<div class="ui-combo-wrap form_text error_tip">
						<input type="text" class="input-txt input-txt_7" placeholder="" id="url" name="url" />
					</div>
				</div>
				<div class="row-div">
					<label class="form-label label_ui label_4mar"> <span class="c-red">*</span> 排序：
					</label>
					<div class="ui-combo-wrap form_text error_tip">
						<input type="number" class="input-txt input-txt_7" placeholder="" name="sort" htmlEscape="false" value="${newSort }" required>
					</div>
				</div>
			</div>
			<div class="cl">
				<div class="row-div">
					<label class="form-label label_ui label_4mar">类型：</label>
					<div class="ui-combo-wrap form_text div_select_wrap">
						<phtml:list name="type" textProperty="text" cssClass="hy_select2" type="com.huayin.printmanager.persist.enumerate.PermissionType"></phtml:list>
					</div>
				</div>
				<div class="row-div">
					<label class="form-label label_ui label_4mar">权限标识：</label>
					<div class="ui-combo-wrap form_text error_tip">
						<input type="text" class="input-txt input-txt_7" placeholder="" id="identifier" name="identifier" />
					</div>
				</div>
			</div>
			<div class="cl">
				<div class="row-div">
					<label class="form-label label_ui label_4mar">是否点击即刷新：</label>
					<div class="ui-combo-wrap form_text div_select_wrap">
						<phtml:list selected="NO" name="refresh" textProperty="text" cssClass="hy_select2" type="com.huayin.printmanager.persist.enumerate.BoolValue"></phtml:list>
					</div>
				</div>
				<div class="row-div">
					<label class="form-label label_ui label_4mar">是否基础版本：</label>
					<div class="ui-combo-wrap form_text div_select_wrap">
						<phtml:list selected="YES" name="isBase" textProperty="text" cssClass="hy_select2" type="com.huayin.printmanager.persist.enumerate.BoolValue"></phtml:list>
					</div>
				</div>
			</div>
			<div class="cl">
				<div class="row-div">
					<label class="form-label label_ui label_4mar">ICON：</label>
					<div class="ui-combo-wrap form_text error_tip">
						<input type="text" class="input-txt input-txt_7" placeholder="" id="icon" name="icon" value="${menu.icon }" />
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
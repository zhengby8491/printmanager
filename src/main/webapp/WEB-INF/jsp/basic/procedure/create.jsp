<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/basic/procedure/create.js?v=${v}"></script>
<title>添加工序</title>
</head>
<body>
	<div class="layer_container">
		<!--表单部分START-->
		<form id="jsonForm">
			<div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							排序：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input name="sort" htmlEscape="false" type="number" class="input-txt input-txt_7 constraint_number" value="${fns:nextSort('procedure')}" required />
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							工序类型：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list name="procedureType" textProperty="text" cssClass="hy_select2" type="com.huayin.printmanager.persist.enumerate.ProcedureType"></phtml:list>
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							工序分类：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list items="${fns:basicList('PROCEDURECLASS')}" valueProperty="id" cssClass="hy_select2" name="procedureClassId" textProperty="name"></phtml:list>
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							工序编号：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" value="${code }" readonly="true" id="code" name="code">
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							工序名称：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" value="" placeholder="" id="name" name="name">
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							生产方式：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list name="produceType" textProperty="text" cssClass="hy_select2" type="com.huayin.printmanager.persist.enumerate.ProduceType"></phtml:list>
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							是否生产：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list name="isProduce" textProperty="text" cssClass="hy_select2" type="com.huayin.printmanager.persist.enumerate.BoolValue" selected="YES"></phtml:list>
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							是否报价：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list name="isQuotation" textProperty="text" cssClass="hy_select2" type="com.huayin.printmanager.persist.enumerate.BoolValue" selected="YES"></phtml:list>
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							上报方式：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list name="yieldReportingType" textProperty="text" cssClass="hy_select2" type="com.huayin.printmanager.persist.enumerate.YieldReportingType" selected="YES"></phtml:list>
						</div>
					</div>
				</div>
				<div class="cl">
					<!-- <div class="row-div">
					<label class="form-label label_ui label_4mar"><span
						class="c-red"></span>发外规格：</label>
					<div class="ui-combo-wrap form_text error_tip">
						<input type="text" class="input-txt input-txt_7"  style="color:blue" value="新添字段(下拉)" placeholder=""/>
					</div>
				</div> -->
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							是否排程：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list name="isSchedule" textProperty="text" cssClass="hy_select2" type="com.huayin.printmanager.persist.enumerate.BoolValue" selected="YES"></phtml:list>
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							排程数据源：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list name="scheduleDataSource" textProperty="text" cssClass="hy_select2" type="com.huayin.printmanager.persist.enumerate.ScheduleDataSource"></phtml:list>
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							单价：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input name="price" htmlEscape="false" class="input-txt input-txt_7 constraint_decimal" />
						</div>
					</div>
				</div>
				<div class="cl" style="display:none;">
					<div class="row-div normal_div">
						<label class="form-label label_ui label_4mar">常用报价公式：</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list name="paramsType" textProperty="text" cssClass="input-txt input-txt_3 hy_select2" defaultOption="请选择" defaultValue="" type="com.huayin.printmanager.persist.enumerate.ParamsType"></phtml:list>
						</div>
					</div>
					<div class="row-div custom_div" style="margin-left: 0">
						<label class="form-label label_ui label_4mar">自定义报价公式：</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list items="${fns:basicList('OFFERFORMULA')}" valueProperty="id" cssClass="hy_select2" defaultOption="请选择" defaultValue="" name="formulaId" textProperty="name"></phtml:list>
						</div>
					</div>
					<input type="checkbox" id="customCheckbox" style="margin-left: 10px">
					自定义


				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">备注：</label>
						<div class="form_textarea_rlt">
							<textarea name="memo" id="memo" placeholder="说点什么...100个字符以内" dragonfly="true" onKeyUp="Helper.doms.textarealength(this,100)" style="width: 824px; resize: none"></textarea>
							<p class="textarea-numberbar">
								<em>0</em>
								/100
							</p>
						</div>
					</div>
				</div>
			</div>
			<!--表单部分END-->
			<!--表格部分START-->
			<!-- <div class="info_table">
			<div class="tab_page">
				<ul class="tab_page_list">
					<li class="active">机台</li>
				</ul>
			</div>
			<div class="info_table_container">
				<div class="info_item info_item_1">
					<div class="info_item_btn">
						<button type="button" class="table_nav_btn info_btn"><i class="fa fa-plus"></i> 添加行</button>
					</div>
					<div class="info_item_table">
						<table class="layer_table layer_info_table" rules="all">
							<thead>
								<tr>
									<th width="30">#</th>
									<th width="60">默认</th>
									<th>机台名称</th>
									<th width="80">操作</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>1</td>
									<td><input type="checkbox"/></td>
									<td>机床一号</td>
									<td>
										<a title="删除" href="javascript:;"> <i class="fa fa-trash-o"></i></a>			
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					表格分页
					<div class="pagination_container info_pagination_container">--此处为分页区域--</div>
				</div>	
			</div>
		</div> -->
			<!--表格部分END-->
			<div style="margin-left: 138px; margin-top: 10px">
				<input class="nav_btn table_nav_btn" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
			</div>
		</form>
	</div>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/sys/smspartner/edit.js?v=${v}"></script>
<meta charset="UTF-8">
<title>编辑短信渠道</title>
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
							渠道编号：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" id="id" name="id" readonly="true" value="${smsPartner.id }" />
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							渠道名称：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" id="name" name="name" value="${smsPartner.name}" />
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							商户编号：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" id="partnerId" name="partnerId" value="${smsPartner.partnerId}" />
						</div>
					</div>
				</div>
				<div class="cl">

					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							密钥：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" id="secretKey" name="secretKey" value="${smsPartner.secretKey}" />
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							渠道状态：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list name="state" textProperty="text" cssClass="hy_select2" selected="${smsPartner.state}" type="com.huayin.printmanager.persist.enumerate.SmsPartnerState"></phtml:list>
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							短信终端类型：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list name="smsSendType" textProperty="text" cssClass="hy_select2" selected="${smsPartner.smsSendType}" type="com.huayin.printmanager.persist.enumerate.SmsSendType"></phtml:list>
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							发送优先级：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<input type="text" class="input-txt input-txt_7" id="priority" name="priority" value="${smsPartner.priority}" />
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">描述：</label>
						<div class="form_textarea_rlt">
							<textarea name="remark" id="remark" class="input-txt_7 " placeholder="说点什么...100个字符以内" dragonfly="true" onKeyUp="Helper.doms.textarealength(this,100)" style="width: 824px">${smsPartner.remark}</textarea>
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
			<div class="info_table">
				<div class="tab_page">
					<ul class="tab_page_list">
						<li class="active">配置信息</li>
					</ul>
				</div>
				<div class="info_table_container">
					<div class="info_item info_item_1">
						<div class="info_item_btn">
							<button type="button" class="table_nav_btn info_btn" onclick="addtr_extConfigs();">
								<i class="fa fa-plus"></i>
								添加
							</button>
						</div>
						<div class="info_item_table">
							<table class="layer_table layer_info_table" rules="all">
								<thead>
									<tr>
										<th width="100">属性</th>
										<th width="300">值</th>
										<th width="100">操作</th>
									</tr>
								</thead>
								<tbody id="smsPartner_extConfigs">
									<c:forEach var="config_map" items="${smsPartner.extConfigs}" varStatus="status">
										<tr>
											<td>
												<input class='tab_input' name='config_key' id='config_key' type='text' value="${config_map.key }" />
											</td>
											<td>
												<input class='tab_input' name='config_value' id='config_value' type='text' value="${config_map.value }" />
											</td>
											<td>
												<a title="删除" href="javascript:;" name="btn_del">
													<i class="fa fa-trash-o"></i>
												</a>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
			<!--表格部分END-->
			<div style="margin-left: 850px; margin-top: 10px">
				<input class="nav_btn table_nav_btn" type="button" id="btn_save" onclick="checkData();" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
			</div>
		</form>
	</div>
</body>
</html>

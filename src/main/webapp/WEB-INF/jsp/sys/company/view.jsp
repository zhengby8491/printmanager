<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/sys/company/edit.js?v=${v }"></script>
<title>公司信息</title>
</head>
<body>
	<div class="layer_container">
		<!--表单部分START-->
		<form id="form-company">
			<input type="hidden" name="id" value="${company.id }" />
			<div class="online_view">
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">公司名称：</label>
						<div class="ui-combo-wrap form_text divWidth">${company.name }</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">公司邮箱：</label>
						<div class="ui-combo-wrap form_text divWidth">${company.email}</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">公司电话：</label>
						<div class="ui-combo-wrap form_text divWidth">${company.tel }</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">联系人：</label>
						<div class="ui-combo-wrap form_text divWidth">${company.linkName }</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">公司传真：</label>
						<div class="ui-combo-wrap form_text divWidth">${company.fax }</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">公司网站：</label>
						<div class="ui-combo-wrap form_text divWidth">${company.website }</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">公司地址：</label>
						<div class="ui-combo-wrap form_text divWidth" onmouseover="this.title=this.innerHTML">${company.address }</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">到期时间：</label>
						<div class="ui-combo-wrap form_text divWidth">
							<fmt:formatDate value="${company.expireTime}" type="date" pattern="yyyy-MM-dd" />
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">微信号：</label>
						<div class="ui-combo-wrap form_text divWidth">${company.weixin}</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">正式用户：</label>
						<div class="ui-combo-wrap form_text divWidth">${company.isFormal.text}</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">代工平台：</label>
						<div class="ui-combo-wrap form_text divWidth">${company.isOem.text}</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">公司简介：</label>
						<div class="form_textarea_rlt view_area">${company.introduction}</div>
					</div>
				</div>
			</div>
		</form>
		<!--表单部分END-->
	</div>
</body>
</html>
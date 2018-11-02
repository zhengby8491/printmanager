<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<meta charset="UTF-8">
<title>留言版</title>
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<script type="text/javascript" src="${ctxStatic}/site/sys/comment/list.js?${v}"></script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl topPath"></div>
			<!--头部END-->
			<!--查询表单START-->
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">
						<dd class="row-dd">
							<sys:dateConfine label="创建时间" initDate="false" />
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">最后回复时间：</label>
							<span>
								<input type="text" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'dateMax\')||\'%y-%M-%d\'}'})" class="input-txt input-txt_0 Wdate" id="udateMin" />
							</span>
							<label class="label_2 align_c">至</label>
							<span>
								<input type="text" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'dateMin\')}'})" class="input-txt input-txt_0 Wdate" id="udateMax" />
							</span>
						</dd>
					</dl>
					<dl class="cl row-dl">
						<dd class="row-dd">
							<label class="form-label label_ui label_1">标题：</label>
							<span class="ui-combo-wrap form_text">
								<input id="title" type="text" class="input-txt" style="width: 528px" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">留言人：</label>
							<span class="ui-combo-wrap form_text">
								<input id="userName" type="text" class="input-txt input-txt_3" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">联系方式：</label>
							<span class="ui-combo-wrap form_text">
								<input id="contact" type="text" class="input-txt input-txt_3" />
							</span>
						</dd>
						<dd class="row-dd">
							<button class="nav_btn table_nav_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查询
							</button>
						</dd>
					</dl>
				</div>
				<div>
					<div class="radio-box">
						<label>
							<input type="radio" value="-1" name="auditFlag" checked="checked" />
							全部
						</label>
						<label>
							<input type="radio" value="YES" name="auditFlag" />
							已回复
						</label>
						<label>
							<input type="radio" value="NO" name="auditFlag" />
							未回复
						</label>
					</div>
				</div>
			</div>
			<!--查询表单END-->
			<!--表格部分Start-->
			<div class="boot-mar">
				<table class="border-table resizable" id="detailList">
				</table>
			</div>
			<!--表格部分End-->
		</div>
	</div>
</body>
</html>
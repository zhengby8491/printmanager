<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@include file="/WEB-INF/jsp/hyui/hyui_ueditor.jspf"%>
<title>服务支持-在线提问</title>
</head>
<script type="text/javascript" src="${ctxStatic}/site/sys/service/question.js?${v}"></script>
<body>
	<div class="service_cont">
		<ul id="myTab" class="nav nav-tabs">
			<li>
				<a href="${ctx}/sys/service/system_notice"> 系统消息 </a>
			</li>
			<li>
				<a href="${ctx}/sys/service/myQuestion">我的提问</a>
			</li>
			<li class="active">
				<a>在线提问</a>
			</li>
			<li>
				<a href="${ctx}/sys/service/service">服务通道</a>
			</li>
		</ul>
		<div id="myTabContent" class="tab-content">

			<div id="online_ask">
				<form id="form_online_ask">
					<div class="ask_item">
						<p class="tit">
							<i class="fa fa-comments"></i>
							欢迎您给我们提出意见和建议，您的问题我们会尽快回复，敬请留意我的提问，谢谢！
						</p>
					</div>
					<div class="ask_item" style="margin-top: 25px">
						<label>标题</label>

						<input id="title" name="title" type="text" class="input-txt" style="width: 900px;" />
					</div>
					<div class="ask_item">
						<label>内容</label>
						<!-- 加载编辑器的容器 -->
						<div id="content" name="content" class="l"></div>
					</div>
					<input id="sub_online_ask" type="button" class="btn btn-success r mar_top10" value="提交" />
				</form>
			</div>
		</div>
	</div>
</body>
</html>

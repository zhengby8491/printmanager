<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@include file="/WEB-INF/jsp/hyui/hyui_ueditor.jspf"%>
<%-- <script type="text/javascript" src="${ctxHYUI}/plugins/ueditor/ueditor.config.js"></script>
<script type="text/javascript" src="${ctxHYUI}/plugins/ueditor/ueditor.all.min.js"></script>
	<script type="text/javascript" src="${ctxHYUI}/plugins/ueditor/lang/zh_CN/zh_CN.js?v=${v }"></script> --%>
<title>服务支持</title>
</head>
<script type="text/javascript" src="${ctxStatic}/site/sys/service/question_detail.js?${v}"></script>
<body>
	<div class="service_cont">
		<ul id="myTab" class="nav nav-tabs">
			<li>
				<a href="${ctx}/sys/service/system_notice"> 系统消息 </a>
			</li>
			<li class="active">
				<a href="${ctx}/sys/service/myQuestion">我的提问</a>
			</li>
			<li>
				<a href="${ctx}/sys/service/question">在线提问</a>
			</li>
			<li>
				<a href="${ctx}/sys/service/service">服务通道</a>
			</li>
		</ul>
		<div id="myTabContent" class="tab-content">

			<div id="question_detail" class="qs_detail">
				<div class="title cl">
					<a id="detail_return">
						<i class="fa fa-mail-reply"></i>
						返回
					</a>
					<h3 id="question_title">${comment.title }</h3>
				</div>
				<div class="question_ctn qs_ctn">
					<ul>
						<li class="question">
							<h4>
								<i class="fa fa-question-circle"></i>
								提问内容：
							</h4>
							<div class="ctn">
								<p id="question_content">${comment.content }</p>

								<div class="time">
									<span id="question_time">
										<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value='${comment.createTime }' type='date' />
									</span>
								</div>
							</div>
						</li>
						<c:forEach items="${comment.detailList }" var="detail">
							<c:if test="${detail.isManagerReplay=='YES' }">
								<li class="answer">
									<h4>
										<i class="fa fa-comments-o"></i>
										客服回复
									</h4>
							</c:if>
							<c:if test="${detail.isManagerReplay=='NO' }">
								<li class="question">
									<h4>
										<i class="fa fa-question-circle"></i>
										提问内容：
									</h4>
							</c:if>

							<div class="ctn">
								<p id="ask_content">${detail.reply }</p>
								<div class="time">
									<span id="ask_time">
										<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value='${detail.updateTime }' type='date' />
									</span>
								</div>
							</div>
							</li>
						</c:forEach>
					</ul>
				</div>
				<div class="qs_continue">
					<input id="id" name="id" type="hidden" value="${comment.id}" />
					<input id="isManagerReplay" name="isManagerReplay" type="hidden" value="NO" />
					<div id="reply" name="reply" style="width: 100%"></div>
					<input id="reply_sub" type="submit" class="btn btn-success r mar_top10" value="继续提问" />
				</div>
			</div>
		</div>
	</div>
</body>
</html>

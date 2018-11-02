<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>系统公告</title>
<script type="text/javascript" src="${ctxStatic}/site/sys/advertisement/listLog.js?${v}"></script>
</head>
<body>
<input type="hidden" value="${params.id}" id="paramsId">
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="系统管理-轮播广告-查看"></sys:nav>
				</div>
				<div class="top_nav">
					<button class="nav_btn table_nav_btn" id="btn_back">返回</button>
				</div>
			</div>
			<!--搜索栏-->
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">
						<dd class="row-dd">
							<sys:dateConfine label="访问日期" dateMin="${queryParam.dateMin}" dateMax="${queryParam.dateMax}" initDate="false" />
						</dd>
						<dd class="row-dd">
							<button type="button" class="nav_btn table_nav_btn" id="btn_search">
								<i class="fa fa-search"></i>
								搜索
							</button>
						</dd>
					</dl>
				</div>
				<!--表格-->
				<div class="boot-mar">
					<table class="border-table" id="bootTable">
					</table>
				</div>
			</div>
</body>
</html>
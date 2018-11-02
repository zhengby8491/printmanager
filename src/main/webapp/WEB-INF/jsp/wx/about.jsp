<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<title>印管家</title>
<style type="text/css">
/**
 * 印管家logo样式
 **/
@font-face {
  font-family: 'iconhyui';
  src:  url('/static/hyui/fonts/iconhyui.eot?i4o94o');
  src:  url('/static/hyui/fonts/iconhyui.eot?i4o94o#iefix') format('embedded-opentype'),
    url('/static/hyui/fonts/iconhyui.ttf?i4o94o') format('truetype'),
    url('/static/hyui/fonts/iconhyui.woff?i4o94o') format('woff'),
    url('/static/hyui/fonts/iconhyui.svg?i4o94o#iconhyui') format('svg');
  font-weight: normal;
  font-style: normal;
}

[class^="icon-"], [class*=" icon-"] {
  /* use !important to prevent issues with browser extensions that change fonts */
  font-family: 'iconhyui' !important;
  speak: none;
  font-style: normal;
  font-weight: normal;
  font-variant: normal;
  text-transform: none;
  line-height: 1;

  /* Better Font Rendering =========== */
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.icon-ygj:before {
  content: "\e900";
}
</style>
</head>
<body>
	<div class="page page-current">
		<div class="content sum_content index_content about_content" style="bottom: 0">
			<div class="logo_div">
				<span class="icon icon-ygj" style="font-size: 100px; color: #02A578;"></span>
			</div>
			<ul id="itemlist" class="list-container list-block" style="margin-top: 1rem;">
				<li class="sum_item">
					<span class="item_title">当前版本 ：V7.6</span>
				</li>
				<li class="sum_item">
					<span class="item_title">官网地址 ：www.huayinsoft.com</span>
				</li>
				<li class="sum_item">
					<span class="item_title">联系电话 ：${fns:getConfig('SITE_SERVICE_PHONE')}</span>
				</li>
			</ul>
			<p class="text-center" style="margin-top: 1rem">版权所有Copyright &copy; 2013-现在&nbsp;&nbsp;</p>
			<p class="text-center">深圳华印信息技术有限公司</p>
			<p class="text-center"><a href="http://www.miitbeian.gov.cn">粤ICP备17027007号-4</a></p>
		</div>
	</div>
</body>

</html>
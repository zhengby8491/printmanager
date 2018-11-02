<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<input type="hidden" id="offerType" value="${offerType }">
<input type="hidden" id="offerTypeText" value="${offerType.text }">
<c:forEach items="${fns:getOfferSettingTypeList(offerType)}" var="ot">
	<!-- 只有彩盒存在坑纸 -->
	<c:if test="${ot != 'BFLUTE' || offerType == 'CARTONBOX'  }">
		<li class="offerSettingMenu sliderTag ${ot} <c:if test="${offerSettingType == ot }"> active</c:if>">
			<a href="javascript:;" data-title="${ot.text }" data-href="${ctx }/offer/setting/${ot.mapping}?type=${offerType}" target="_blank">
				<i class="fa ${ot.icon } "></i>${ot.text}</a>
			<i class="fa fa-caret-right"></i>
		</li>
	</c:if>
</c:forEach>
<script type="text/javascript">
	/**
	 * Author:		   think
	 * Create:	 	   2017年10月19日 上午9:57:08
	 * Copyright: 	 Copyright (c) 2017
	 * Company:		   Shenzhen HuaYin
	 * @since:       1.0
	 */
	$(function()
	{
		init(); // 初始化

		/**
		 * 
		 * 初始化功能、按钮事件
		 * @since 1.0, 2017年10月19日 上午9:57:39, think
		 */
		function init()
		{
			// 报价设置菜单切换
			$(".offerSettingMenu a").on("click", function()
			{
				var $this = $(this);
				var $url = $this.data("href");
				var $title = $this.data("title");
				changeMenu($url, $title);
			});
		}

		/**
		 * 
		 * 报价设置菜单切换
		 * @since 1.0, 2017年10月19日 上午9:57:39, think
		 */
		function changeMenu(url, title)
		{
			admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
		}
	});
</script>
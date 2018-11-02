<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<input type="hidden" id="offerType" value="${offerType }">
<input type="hidden" id="offerTypeText" value="${offerType.text }">
<input type="hidden" id="boxType" name="boxType" value="${offerType.text }">
<div class="navbox menu-z">
	<ul id="sliderNav">
		<c:forEach items="${fns:getOfferTypeList()}" var="type">
			<li id="${type.id}" class="offerAutoMenu sliderTag ${type}  <c:if test="${type == offerType }"> active</c:if>">
				<a href="javascript:;" data-title="${type.text }" data-href="${ctx }/offer/auto/${type.mapping}" target="_blank">
					<i class="fa ${type.icon }"></i>${type.text}</a>
				<i class="fa fa-caret-right"></i>
			</li>
		</c:forEach>
	</ul>
</div>

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
			$(".offerAutoMenu a").on("click", function()
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

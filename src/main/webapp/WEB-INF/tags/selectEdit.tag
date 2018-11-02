
<%
	/**
	 * <p>可编辑下拉框</p>
	 * <pre>
	 * Author:		   think
	 * Create:	 	   2017年10月20日 上午9:21:16
	 * Copyright: 	 Copyright (c) 2017
	 * Company:		   Shenzhen HuaYin
	 * @since:       1.0
	 * <pre>
	 */
%>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<%@ attribute name="id" type="java.lang.String" required="false"%>
<%@ attribute name="name" type="java.lang.String" required="false"%>
<%@ attribute name="css" type="java.lang.String" required="false"%>
<%@ attribute name="value" type="java.lang.String" required="false"%>
<%@ attribute name="style" type="java.lang.String" required="false"%>
<%@ attribute name="selectStyle" type="java.lang.String" required="false"%>
<div class="use_relative" style="${style}">
	<input id="${id }" name="${name }" type="text" class="${css }" value="${value}" style="width: 98%; text-align: left;" />
	<div class="style_sel_item">
		<div class="sel_ico"></div>
		<div class="hy_hide select_edit" style="${selectStyle}">
			<jsp:doBody></jsp:doBody>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(function()
	{
		// 颜色下拉按钮进入事件
		$(document).on("mouseenter", ".use_relative", function()
		{
			$(this).find(".sel_ico").addClass('active');
			//$(this).children(".td_box").css("width", $(this).parent().css("width"));
			$(this).find(".select_edit").show();
		});
		
		// 颜色下拉按钮移出事件
		$(document).on("mouseleave", ".use_relative", function()
		{
			$(this).find(".sel_ico").removeClass('active');
			$(this).find(".select_edit").hide();
		})
		
		/*颜色下拉内容单击选中事件*/
		$(document).on("click", ".select_edit_item", function()
		{
			$(this).parent().parent().parent().find("input:first").val($(this).text()).trigger('change');
			$(this).parent().hide();
		});
	});
</script>
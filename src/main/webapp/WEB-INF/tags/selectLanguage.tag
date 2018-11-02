<%@ tag language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<select name="language" id="language" class="lang_select hy_select2">
	<option value="zh_cn"><fmt:message key="syslabel.language.zh_cn" /></option>
	<option value="en"><fmt:message key="syslabel.language.en" /></option>
</select>
<script type="text/javascript">
	$("#language").on("change", function(value) {
		top.location.href = "${path}/public/language/" + $(this).val();
	});
	//设置当前语言栏
	(function() {
		var language = $.cookie("print.language");
		if (language == 'zh_cn') {
			$("#language").val("zh_cn");
		} else if (language == 'en') {
			$("#language").val("en");
		}
	})();
</script>
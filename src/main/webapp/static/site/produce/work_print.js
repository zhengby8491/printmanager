$(function()
{
	// 格式化材料用量
	$(".qty").each(function()
	{
		$(this).text(Number($(this).text()).tomoney());
	});
	$("#conform_print").click(function()
	{
		window.print()
	})
	$("#select_template").click(function()
	{
		Helper.popup.show('选择模板', Helper.basePath + '/common/printTemplate/selectTemplate?billType=PRODUCE_MO&url=' + $("#url").val() + '' + $("#orderId").val() + '', '700', '400');
	});
})
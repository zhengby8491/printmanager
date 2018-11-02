$(function()
{
	var urlEncode = $("#urlEncode").val();
	var orderId = $("#orderId").val();
	// 金额转大写
	$("#format").text(digitUppercase($("#totalMoney").val()));
	$("#conform_print").click(function()
	{
		window.print()
	});
	$("#select_template").click(function()
	{
		Helper.popup.show('选择模板', Helper.basePath + '/common/printTemplate/selectTemplate?billType=SALE_IV&url=' + urlEncode + '' + orderId, '700', '400');
	});
})
/**
 * 产量上报计算
 * @param reportQty
 * @param qualifiedQty
 * @param unqualified
 * @param yieldQty
 * @param unreportQty
 */
$(function()
{
	// 上报数
	$("table tbody").on("blur keyup", "input[name='reportList.reportQty']", function()
	{
		var reportQtyVal = $(this).val();
		var unqualified = $(this).parent().parent().find("input[name='reportList.unqualified']").val();
		var qualifiedQty = $(this).parent().parent().find("input[name='reportList.qualifiedQty']").val();

		if (unqualified)
		{
			$(this).parent().parent().find("input[name='reportList.qualifiedQty']").val((Number(reportQtyVal) - Number(unqualified)).toFixed(2));
		} else
		{
			$(this).parent().parent().find("input[name='reportList.qualifiedQty']").val(reportQtyVal);
			$(this).parent().parent().find("input[name='reportList.unqualified']").val(0);
		}

	});

	// 合格数
	$("table tbody").on("blur keyup", "input[name='reportList.qualifiedQty']", function()
	{
		var qualifiedQty = $(this).val();
		var unqualified = $(this).parent().parent().find("input[name='reportList.unqualified']").val();
		if (unqualified)
		{
			$(this).parent().parent().find("input[name='reportList.reportQty']").val((Number(qualifiedQty) + Number(unqualified)).toFixed(2));
		} else
		{
			$(this).parent().parent().find("input[name='reportList.reportQty']").val(qualifiedQty);

		}

	});

	// 不合格数
	$("table tbody").on("blur keyup", "input[name='reportList.unqualified']", function()
	{
		var unqualified = $(this).val();
		var reportQty = $(this).parent().parent().find("input[name='reportList.reportQty']").val();
		if (Number(reportQty) - Number(unqualified) >= 0)
		{
			$(this).parent().parent().find("input[name='reportList.qualifiedQty']").val((Number(reportQty) - Number(unqualified)).toFixed(2));
		} else
		{
			$(this).parent().parent().find("input[name='reportList.qualifiedQty']").val(0);
			$(this).parent().parent().find("input[name='reportList.reportQty']").val(unqualified);
		}

	});

});

$(function()
{
	/*点击页签切换表格*/
	$('.info_item').eq(0).show();// 显示第一个表格
	$('.tab_page_list li').click(function()
	{
		var ind = $(this).index();// 获取li位置
		$('.tab_page_list li').removeClass('active').eq(ind).addClass('active');
		$('.info_item').hide().eq(ind).show();
	})
	$("#customCheckbox").click(function()
	{
		showHide();
	})
	showHide();

	$("#jsonForm").validate({
		submitHandler : function()
		{// 必须写在验证前面，否则无法ajax提交
			if ($("#customCheckbox").prop("checked"))
			{
				$("#paramsType").select2("val", "请选择");
			} else
			{
				$("#formulaId").select2("val", "请选择");
			}
			Helper.request({// 验证新增是否成功
				url : Helper.basePath + "/basic/procedure/update",
				data : $("#jsonForm").formToJson(),
				async : false,
				success : function(data)
				{
					if (data.success)
					{
						parent.location.href = Helper.basePath + "/basic/procedure/list";
					} else
					{
						layer.alert('更新失败：' + data.message);
					}
				}
			});
		},
		rules : {
			name : {
				required : true,
			}
		},
		onkeyup : false,
		focusCleanup : true
	});
	// 工序类型改变事件
	$("#procedureType").on("change", function()
	{
		if ($(this).val() == "BEFORE")
		{
			$("#yieldReportingType").val("PLATEPCS");
		}
		if ($(this).val() == "PRINT" || $(this).val() == "AFTER")
		{
			$("#yieldReportingType").val("IMPRESSION");
		}
		if ($(this).val() == "FINISHED")
		{
			$("#yieldReportingType").val("PRODUCE");
		}
	});
});

function showHide()
{
	if ($("#customCheckbox").prop("checked"))
	{
		$(".normal_div").hide();
		$(".custom_div").show();
	} else
	{
		$(".normal_div").show();
		$(".custom_div").hide();
	}
}

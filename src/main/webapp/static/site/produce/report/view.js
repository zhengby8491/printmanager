$(function()
{

	/* 编辑 */
	$("#btn_edit").on("click", function()
	{
		location.href = Helper.basePath + '/produce/report/edit/' + $("#order_id").val();
	});

	// 审核 或 反审核
	$("#btn_audit, #btn_audit_cancel").on("click", function()
	{
		var order_id = $("#order_id").val();
		var order_billType = $("#order_billType").val();
		var boolValue = $(this).attr("id") == "btn_audit" ? "YES" : "NO";
		var pathUrl = Helper.basePath + '/produce/report/audit/' + order_id;
		if (boolValue == "NO")
		{
			pathUrl = Helper.basePath + '/produce/report/auditCancel/' + order_id;
		}

		Helper.post(pathUrl, {
			"billType" : order_billType,
			"boolValue" : boolValue
		}, function(data)
		{
			if (data.success)
			{
				Helper.message.suc(data.message);
				location.href = Helper.basePath + "/produce/report/view/" + order_id;
			} else
			{
				Helper.message.warn(data.message);
			}
		});
	});

	/* 删除 */
	$("#btn_del").on("click", function()
	{
		Helper.message.confirm('确认要删除吗？', function(index)
		{
			Helper.post(Helper.basePath + '/produce/report/del/' + $("#order_id").val(), function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
					closeTabAndJump("生产任务列表");
				} else
				{
					Helper.message.warn(data.message);
				}
			});
		});
	});

	// 强制完工
	$("#btn_complete,#btn_complete_cancel").on("click", function()
	{
		var order_id = $("#order_id").val();
		var boolValue = $(this).attr("id") == "btn_complete" ? "YES" : "NO";
		var pathUrl = Helper.basePath + '/produce/report/complete';
		if (boolValue == "NO")
		{
			pathUrl = Helper.basePath + '/produce/report/completeCancel';
		}
		Helper.post(pathUrl, {
			"tableType" : "MASTER",
			"ids" : [ order_id ],
			"boolValue" : boolValue
		}, function(data)
		{
			if (data.success)
			{
				Helper.message.suc(data.message);
				location.href = Helper.basePath + "/produce/report/view/" + order_id;
			} else
			{
				Helper.message.warn(data.message);
			}
		});
	});

	/* 返回显示列表 */
	$("#btn_back").on("click", function()
	{
		var url = Helper.basePath + '/produce/transmit/to_product_daily_report';
		var title = "生产日报表";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"))
	});

});
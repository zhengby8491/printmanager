$(function()
{
	// 初始化制单日期
	$("#createDate").val(new Date().format('yyyy-MM-dd'));
	$("#reportDate").val(new Date().format('yyyy-MM-dd'));
	$("input[name='reportList.startTime']").each(function()
	{
		$(this).val(new Date().format("yyyy-MM-dd hh:mm:ss"));
	});
	$("input[name='reportList.endTime']").each(function()
	{
		$(this).val(new Date().format("yyyy-MM-dd hh:mm:ss"));
	});
	// 删除一行数据
	$("table").on("click", ".delete", function()
	{
		$(this).parent().parent().remove();
		resetSequenceNum();
	});
	// 重新设置序号
	function resetSequenceNum()
	{
		$("table tbody tr").each(function(index)
		{
			$(this).find("td").first().html(++index);
		});
	}

	// 保存
	$("#btn_save,#btn_save_audit").click(function()
	{
		if (!$("#employee").val())
		{
			Helper.message.warn("请选择员工信息");
			return;
		}

		$("#isCheck").val($(this).attr("id") == "btn_save_audit" ? "YES" : "NO");
		form_submit();

	})

	// 取消
	$("#btn_cancel").click(function()
	{
		closeTabAndJump("生产任务列表");
	});

	// 选择员工
	$("#employee_quick_select").click(function()
	{
		Helper.popup.show('选择员工', Helper.basePath + '/quick/employee_select?multiple=false', '800', '500');
	});
});

function form_submit()
{
	$("#btn_save").attr({
		"disabled" : "disabled"
	});
	$("#btn_save_audit").attr({
		"disabled" : "disabled"
	});
	Helper.request({
		url : Helper.basePath + "/produce/report/save",
		data : $("#form_report").formToJson(),
		success : function(data)
		{
			if (data.success)
			{
				Helper.message.suc('已保存!');
				location.href = Helper.basePath + '/produce/report/view/' + data.obj.id;
			} else
			{
				Helper.message.warn('保存失败!' + data.message);
				$("#btn_save").removeAttr("disabled");
				$("#btn_save_audit").removeAttr("disabled");
			}
		},
		error : function(data)
		{
			$("#btn_save").removeAttr("disabled");
			$("#btn_save_audit").removeAttr("disabled");
		}
	});
}

// 获取员工信息
function getCallInfo_empoyee(obj)
{
	$("#employee").val(obj.name);
	$("#employeeId").val(obj.id);
}
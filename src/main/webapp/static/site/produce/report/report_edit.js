$(function()
{
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
	// 取消
	$("#btn_cancel").click(function()
	{
		location.href = Helper.basePath + '/produce/report/view/' + $("#workReportId").val();
	});

	// 选择员工
	$("#employee_quick_select").click(function()
	{
		Helper.popup.show('选择员工', Helper.basePath + '/quick/employee_select?multiple=false', '800', '500');
	});
});

// 获取员工信息
function getCallInfo_empoyee(obj)
{
	$("#employee").val(obj.name);
	$("#employeeId").val(obj.id);
}

function form_submit()
{
	$("#btn_save").attr({
		"disabled" : "disabled"
	});
	$("#btn_save_audit").attr({
		"disabled" : "disabled"
	});
	Helper.request({
		url : Helper.basePath + "/produce/report/update",
		data : $("#form_report").formToJson(),
		success : function(data)
		{
			if (data.success)
			{
				Helper.message.suc('已修改!');
				location.href = Helper.basePath + '/produce/report/view/' + data.obj.id;
			} else
			{
				Helper.message.warn(data.message);
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
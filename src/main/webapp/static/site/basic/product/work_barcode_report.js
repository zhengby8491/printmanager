/**
 * 条码上报js文件
 */
var postDataKey = {};
var employeeName;
var employeeId;

$(function()
{
	// 选择员工
	$("#employee_quick_select").click(function()
	{
		Helper.popup.show('选择员工', Helper.basePath + '/quick/employee_select?multiple=false', '800', '500');
	});
	$("#btn_cancel").click(function()
	{
		self.close();
	});
	// 保存
	$("#btn_save,#btn_save_audit").click(function()
	{
		employeeName = $("#employee").val();
		employeeId = $("#employeeId").val();
		var reportList = [];

		for ( var k in postDataKey)
		{
			var w = {};
			w["reportQty"] = postDataEditable[k].reportQty;
			w["qualifiedQty"] = postDataEditable[k].qualifiedQty;
			w["unqualified"] = postDataEditable[k].unqualified;
			w["startTime"] = postDataEditable[k].startTime;
			w["endTime"] = postDataEditable[k].endTime;
			w["taskId"] = k;
			reportList.push(w);
		}
		if (reportList.length == 0)
		{
			Helper.message.suc('当前未作任何修改');
			return;
		}
		if (employeeName == null || employeeName == '' || typeof (employeeName) == "undefined")
		{
			Helper.message.warn('请选择员工信息');
			return;
		}
		$("#btn_save").attr({
			"disabled" : "disabled"
		});
		$("#btn_save_audit").attr({
			"disabled" : "disabled"
		});
		Helper.request({
			url : Helper.basePath + "/produce/report/save",
			data : {
				"employeeName" : employeeName,
				"employeeId" : employeeId,
				"reportList" : reportList,
				"isCheck" : $(this).attr("id") == "btn_save_audit" ? "YES" : "NO"
			},
			success : function(data)
			{
				if (data.success)
				{
					Helper.message.suc('已保存!');
					var title = "产量上报";
					var url = Helper.basePath + '/produce/report/view/' + data.obj.id;
					admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
					$("#btn_save").removeAttr("disabled");
					$("#btn_save_audit").removeAttr("disabled");
					$("#bootTable").bootstrapTable('removeAll');

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
	});
})

function initReport(reportQty, qualifiedQty, unqualified)
{
	reportQty = reportQty || "reportQty";
	qualifiedQty = qualifiedQty || "qualifiedQty";
	unqualified = unqualified || "unqualified";
	var id;
	// 上报数
	$("table tbody").on("blur keyup", "input[name='" + reportQty + "']", function()
	{
		id = $(this).parent().parent().data('uniqueid');
		var reportQtyVal = $(this).parent().parent().find("input[name='" + reportQty + "']").val()
		if (Number(reportQty) > Number(postDataKey[id].yieldQty))
		{
			Helper.message.warn('上报数不能超过应上报数!');
		} else
		{
			$(this).parent().parent().find("input[name='" + qualifiedQty + "']").val(reportQtyVal);
			$(this).parent().parent().find("input[name='" + unqualified + "']").val(0);
			postDataEditable[id].reportQty = reportQtyVal;
			postDataEditable[id].unqualified = 0;
			postDataEditable[id].qualifiedQty = Number(reportQtyVal);
		}
	});

	// 合格数
	$("table tbody").on("blur keyup", "input[name='qualifiedQty']", function()
	{
		console.log($(this).parent().parent().find("input[name='qualifiedQty']").val());
		var reportQty = $(this).parent().parent().find("input[name='reportQty']").val()
		var qualifiedQty = $(this).val();
		if (Number(qualifiedQty) > Number(reportQty))
		{
			$(this).parent().parent().find("input[name='qualifiedQty']").val(reportQty);
			$(this).parent().parent().find("input[name='unqualified']").val(0);
			return;
		}
		var unqualified = Number(reportQty).subtr(Number(qualifiedQty)).tomoney()
		console.log("不合格数=" + unqualified);
		$(this).parent().parent().find("input[name='unqualified']").val(unqualified);
		postDataEditable[id].unqualified = unqualified;
		postDataEditable[id].qualifiedQty = Number(qualifiedQty);
	});

	// 不合格数
	$("table tbody").on("blur keyup", "input[name='unqualified']", function()
	{
		var reportQty = $(this).parent().parent().find("input[name='reportQty']").val()
		var unqualified = $(this).parent().parent().find("input[name='unqualified']").val();
		if (Number(unqualified) > Number(reportQty))
		{
			$(this).parent().parent().find("input[name='unqualified']").val(reportQty);
			$(this).parent().parent().find("input[name='qualifiedQty']").val(0);
			return;
		}
		var qualifiedQty = Number(reportQty).subtr(Number(unqualified)).tomoney()
		console.log("合格数=" + qualifiedQty);
		$(this).parent().parent().find("input[name='qualifiedQty']").val(qualifiedQty);
		id = $(this).parent().parent().data('uniqueid');
		postDataEditable[id].unqualified = Number(unqualified);
		postDataEditable[id].qualifiedQty = qualifiedQty;
	});
}

/** 监听到扫码任务的时候 */
function inputBillNo(obj)
{
	postDataEditable = {};
	postDataKey = {};
	var $table = $('#bootTable');
	var v = $(obj).val();
	$table.bootstrapTable('removeAll');
	$.ajax({
		cache : true,
		type : "get",
		url : Helper.basePath + '/produce/work/ajaxBarcodeReport?id=' + v,
		async : false,
		dataType : "json",
		success : function(data)
		{
			$(obj).val("");
			if (data && data.result)
			{
				if (data.result.length == 1)
				{
					var ret = data.result[0];
					if (ret.isSchedule == 'NO')
					{
						layer.open({
							title : '提示信息',
							content : '此工序不是排程工序，如需排程请在工序资料里面设置为排程工序'
						});
						return;
					}
					if (ret.isOutSource == 'YES')
					{
						layer.open({
							title : '提示信息',
							content : '此工序为发外工序'
						});
						return;
					}
					if (ret.isShow == "NO")
					{
						layer.open({
							title : '提示信息',
							content : '此工单未审核'
						});

						return;
					}
					if (ret.isForceComplete == "YES")
					{
						layer.open({
							title : '提示信息',
							content : '此工单已强制完工'
						});

						return;
					}
				} else if (data.result.length == 0)
				{
					if (v.substr(0, "MO".length) == "MO")
					{
						layer.open({
							title : '提示信息',
							content : '此工单已上报完工'
						});
					} else
					{
						layer.open({
							title : '提示信息',
							content : '此工序已上报完工'
						});
					}
					return;
				}

				// 隐藏之前的提示
				layer.close(layer.index);

				for (var i = 0; i < data.result.length; i++)
				{
					var ret = data.result[i];
					var arr = [ ret ];
					$table.bootstrapTable('append', arr);
					postDataKey[ret.id] = ret;
					postDataEditable[ret.id] = {
						reportQty : ret.unreportQty,
						qualifiedQty : ret.unreportQty,
						unqualified : 0,
						startTime : new Date().format("yyyy-MM-dd hh:mm:ss"),
						endTime : new Date().format("yyyy-MM-dd hh:mm:ss")
					};
				}

			}
		}
	});
}
// 获取员工信息
function getCallInfo_empoyee(obj)
{
	$("#employee").val(obj.name);
	employeeName = obj.name;
	$("#employeeId").val(obj.id);
}
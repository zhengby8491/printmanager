$(function()
{
	// 查询
	$("#btn_search").click(function()
	{

		$("#bootTable").bootstrapTable("refreshOptions", {
			pageNumber : 1
		});
	});
	// 确认选择
	$("#btn_confirm").click(function()
	{
		$("#bootTable tbody tr.active_tr").trigger("dblclick");
		Helper.popup.close();
	});
	// 关闭
	$("#btn_cancel").click(function()
	{
		Helper.popup.close();
	});
	// 总P数
	$("#totalPnum").on("blur", function()
	{
		validate($(this));
	});
	// 封面P数
	$("#coverPnum").on("blur", function()
	{
		validate2($(this));
	})
	// 开本
	$("#openNum").on("blur", function()
	{
		validate3($(this));
	});
	// 点击自动分贴
	$("#btn_ok").click(function()
	{
		try
		{
			if (!validate($("#totalPnum")))
			{
				$("#totalPnum").focus();
				throw "";
			}
			if (!validate2($("#coverPnum")))
			{
				$("#coverPnum").focus();
				throw "";
			}
			if (!validate3($("#openNum")))
			{
				$("#openNum").focus();
				throw "";
			}

			var result1 = auto_caculation(1); // 算法一
			var result2 = auto_caculation(2); // 算法二
			var _result = result1.concat(result2);
			var result = unique(_result);
			$("#bootTable tbody").empty();
			$.each(result, function(index, item)
			{
				var TR = $("<tr style='height:45px;'/>");
				TR.append("<td>" + (index + 1) + "</td><td style='line-height: 16px;text-align:left'>" + item + "</td>");
				TR.appendTo("#bootTable tbody");
			});
			// 默认选中第一条记录
			$("#bootTable tbody").find("tr:first").addClass("active_tr");
		} catch (e)
		{
		}
	});

	// 选中事件
	$("#bootTable tbody ").on("click", "tr", function()
	{
		$("#bootTable tbody tr.active_tr").removeClass("active_tr");
		$(this).addClass("active_tr");
	});
	// 列表双击事件
	$("#bootTable tbody").on("dblclick", "tr", function()
	{
		var row = {};
		var data = [];
		var tdStr = $(this).find("td").eq(1).text();
		var strArry = tdStr.split("，");
		for (var i = 0; i < strArry.length; i++)
		{
			var _data = {};
			// 封面4P1贴自翻（单面4P）
			var field1 = strArry[i].split("贴"); // [封面4P1，自翻（单面4P）]
			// 封面4P1，自翻（单面4P）
			var partName = field1[0].split("P")[0]; // 封面4
			// 部件名称
			_data.partName = partName + "P"; // 封面4P
			// P数
			var pageNum = partName.substring(2); // 封面4 → 4
			_data.pageNum = pageNum;
			// 印刷方式 自翻（单面4P）
			var printType = field1[1].split("（")[0]; // 自翻（单面4P）→ [自翻,单面4P）]
			if (printType == "自翻")
			{
				_data.printType = "MYSELF";
			} else if (printType == "正反")
			{
				_data.printType = "DOUBLE";
			}
			// 单面P数 单面4P）
			var pieceNum = (field1[1].split("（")[1]).split("P）")[0].substring(2);
			_data.pieceNum = pieceNum;

			data.push(_data);
		}
		selectRow(data);
	});

});

// 验证总帖数为偶数
function validate($item)
{
	var num = $item.val();
	var exp1 = new RegExp(/^\d*[02468]$/);
	if (!exp1.test(num))
	{
		Helper.message.tips("请输入偶数", $item);
		$item.focus();
		return false;
	}
	return true;
}
// 验证封面p数为偶数
function validate2($item)
{
	var num = $item.val();
	if (num == null || num == "")
		return true;
	var exp1 = new RegExp(/^\d*[02468]$/);
	if (!exp1.test(num))
	{
		Helper.message.tips("请输入偶数", $item);
		return false;
	}
	return true;
}
// 验证开本数为2的N次方
function validate3($item)
{
	var num = Number($item.val());
	var flag = true;
	if (num <= 0)
	{
		Helper.message.tips("请输入2的N次方", $item);
		return false;
	}
	do
	{
		if (num % 2 == 0)
		{
			num = num / 2;
		} else
		{
			Helper.message.tips("请输入2的N次方", $item);
			return false;
		}
	} while (num > 2)
	{
		return true;
	}
}
// 选中分贴方案返回到工单
function selectRow(row)
{
	parent.createPartFromAuto(row);
	Helper.popup.close();
}
// 自动分贴计算方法
function auto_caculation(caNum)
{
	var totalPageNum = $("#totalPnum").val(); // 总P数
	var coverPageNum = $("#coverPnum").val(); // 封面P数
	if (coverPageNum == null || coverPageNum == "")
	{
		coverPageNum = 0;
	}
	var openNum = $("#openNum").val(); // 开本数
	var insidepageTnum = (totalPageNum - coverPageNum) / (openNum * 2); // 内页总贴数:
																																			// 内页总贴数=（总P数-封面P数）/（开本*2）
	var insidePageTnumInt = parseInt(insidepageTnum);// 内页总贴数的整数
	var insidePageTnumFloat = insidepageTnum - insidePageTnumInt; // 内页总帖数的小数
	var outputCoverPageStr1 = ""; // 输出的封面部件描述①
	var outputCoverPageStr2 = ""; // 输出的封面部件描述②
	var outputInsidePageStr = ""; // 输出的内页部件描述
	var _insidePagetTnumFloat = insidePageTnumFloat;// 过程计算中的内页贴数
	var result = []; // 返回的结果
	// 封面部件
	if (coverPageNum > 0)
	{
		outputCoverPageStr1 = "封面" + coverPageNum + "P1贴自翻（单面" + coverPageNum + "P），"; // 封面部件：单面P数取封面P数
		outputCoverPageStr2 = "封面" + coverPageNum + "P1贴自翻（单面" + openNum + "P），"; // 封面部件：单面P数取开本数
	}
	// 如果总P数小于0 则直接返回封面部件
	if (totalPageNum <= 0)
	{
		outputCoverPageStr1 = "封面" + coverPageNum + "P1贴自翻（单面" + coverPageNum + "P）"; // 封面部件：单面P数取封面P数
		outputCoverPageStr2 = "封面" + coverPageNum + "P1贴自翻（单面" + openNum + "P）"; // 封面部件：单面P数取开本数
		result.push(outputCoverPageStr1);
		result.push(outputCoverPageStr2);
		return result;
	}
	// 第一个内页部件：内页总贴数的整数*（开本*2）得到第一个内页部件的内页P数正反版，单面p数取开本
	if (outputInsidePageStr == "" && insidePageTnumInt > 0)
	{
		outputInsidePageStr += "内页" + insidePageTnumInt * openNum * 2 + "P" + insidePageTnumInt + "贴正反（单面" + openNum + "P）";// 描述
	}

	// 内页部件：每个内页部件
	var mishu = Math.log(openNum) / Math.log(2);
	for (var i = 0; i < mishu; i++)
	{
		if (caNum == 1) // 第一种算法：
		{
			var m = Math.pow(2, i); // 2的n次方
			// 第i+1个内页部件
			if (_insidePagetTnumFloat >= 0.5 / m)
			{
				_insidePagetTnumFloat -= 0.5 / m;
				var Pnum = 0.5 / m * openNum * 2;
				if (outputInsidePageStr == "")
				{
					outputInsidePageStr += "内页" + Pnum + "P1贴自翻（单面" + Pnum + "P）";// 描述
				} else
				{
					outputInsidePageStr += "，内页" + Pnum + "P1贴自翻（单面" + Pnum + "P）";// 描述
				}
			} else
			{
				continue;
			}
		} else if (caNum == 2) // 第二种算法
		{
			var m = Math.pow(2, i);// 2的n次方
			// 第i+1个内页部件
			if (_insidePagetTnumFloat >= 0.5 / m)
			{
				_insidePagetTnumFloat -= 0.5 / m;
				var Pnum = 0.5 / m * openNum * 2;
				if (outputInsidePageStr == "")
				{
					outputInsidePageStr += "内页" + Pnum + "P1贴自翻（单面" + openNum + "P）";// 描述
				} else
				{
					outputInsidePageStr += "，内页" + Pnum + "P1贴自翻（单面" + openNum + "P）";// 描述
				}
			} else
			{
				continue;
			}
		}

	}

	outputCoverPageStr1 += outputInsidePageStr;
	outputCoverPageStr2 += outputInsidePageStr;
	result.push(outputCoverPageStr1);
	result.push(outputCoverPageStr2);
	return result;
}
// 去重复
function unique(a)
{
	var res = [];

	for (var i = 0, len = a.length; i < len; i++)
	{
		var item = a[i];
		(res.indexOf(item) === -1) && res.push(item);
	}
	return res;
}
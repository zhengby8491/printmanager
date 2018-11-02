$(function()
{
	$("#checkbox_all").click(function()
	{
		if ($(this).prop("checked"))
		{
			$("table input:checkbox").prop("checked", true);
		} else
		{
			$("table input:checkbox").prop("checked", false);
		}
	});

	$("#node .node1 input:checkbox").click(function()
	{
		var table = $(this).parent().parent().parent().parent();
		if ($(this).prop("checked"))
		{
			table.find("input:checkbox").prop("checked", true);
		} else
		{
			table.find("input:checkbox").prop("checked", false);
		}
	});
	$("#node .node2 input:checkbox").click(function()
	{
		var table = $(this).parent().parent().parent().parent();
		var parent_table = table.parents("table");

		if ($(this).prop("checked"))
		{
			table.find("input:checkbox").prop("checked", true);
			parent_table.find("input:checkbox").first().prop("checked", true);
		} else
		{
			table.find("input:checkbox").prop("checked", false);
			if (parent_table.find("input[type=checkbox]:checked").length <= 1)
			{// 父节点除了本身没有其他子节点被选择了，设置父节点为不选择状态
				parent_table.find("input:checkbox").first().prop("checked", false);
			}
		}
	});
	$("#node .node3 input:checkbox").click(function()
	{
		var tr = $(this).parent().parent().parent();
		var table = tr.parent();
		var parent_table = table.parents("table");

		if ($(this).prop("checked"))
		{
			tr.find("input:checkbox").prop("checked", true);
			table.find("input:checkbox").first().prop("checked", true);
			parent_table.find("input:checkbox").first().prop("checked", true);
		} else
		{
			tr.find("input:checkbox").prop("checked", false);
			if (table.find("input[type=checkbox]:checked").length <= 1)
			{// 父节点除了本身没有其他子节点被选择了，设置父节点为不选择状态
				table.find("input:checkbox").first().prop("checked", false);
			}

			if (parent_table.find("input[type=checkbox]:checked").length <= 1)
			{// 父节点除了本身没有其他子节点被选择了，设置父节点为不选择状态
				parent_table.find("input:checkbox").first().prop("checked", false);
			}
		}
	});

	// 优化支持node5
	$("#node .node4 input:checkbox").click(function()
	{
		var node1 = $(this).parents(".first_table").first();
		var node2 = $(this).parents(".second_table").find(".node2").first();
		var node2All = $(this).parents(".first_table").find(".node2");
		var node3 = $(this).parents(".third_table").parent().prev();
		var node3All = $(this).parents(".second_table").find(".node3");
		var node4 = $(this).parents(".third_table").find(".node4");
		var node4All = $(this).parents(".second_table td.node4");
		var node5 = $(this).parents(".third_table").find(".node5");
		var node5All = $(this).parents(".third_table td.node5");
		var isNode4 = $(this).parent().parent().hasClass("node4");
		var isNode5 = $(this).parent().parent().hasClass("node5");
//		debugger;
		// 新的方式获取node5
		// if (isNode5)
		// {
		// node5 = $(this).parents(".third_table").find(".node4").first();
		// }
		// 默认还是从second2去获取
		if ($(this).parents(".third_table").length <= 0)
		{
			node3 = $(this).parent().parent().prev();
			node4 = $(this).parent().parent();
		}

		if ($(this).prop("checked"))
		{
			node1.find("input:checkbox").first().prop("checked", true);
			node2.find("input:checkbox").first().prop("checked", true);
			node3.find("input:checkbox").first().prop("checked", true);
			
			// 第四层勾选，则勾选所有第五层
			if(isNode4 == true)
			{
				node5.find("input:checkbox").prop("checked", true);
			}
			
			// 第五层勾选，则勾选第四层
			if (isNode5 == true)
			{
				node4.find("input:checkbox").prop("checked", true);
			}
		} else
		{
			// 第五层全部取消，则取消第四层
			if (node5All.find("input[type=checkbox]:checked").length <= 0 && isNode5 == true)
			{
				node4.find("input:checkbox").prop("checked", false);
			}
			
			// 第四层取消，则取消所有第五层
			if(isNode4 == true)
			{
				node5.find("input:checkbox").prop("checked", false);
			}
			// 第四层全部取消，则取消第三层
			if (node4All.find("input[type=checkbox]:checked").length <= 0)
			{
				node3.find("input:checkbox").prop("checked", false);
			}
			
			// 第三层全部取消，则取消第二层
			if (node3All.find("input[type=checkbox]:checked").length <= 0)
			{
				node2.find("input:checkbox").first().prop("checked", false);
			}
			
			// 第二层全部取消，则取消第一层
			console.log(node2All)
			if (node2All.find("input[type=checkbox]:checked").length <= 0)
			{
				node1.find("input:checkbox").first().prop("checked", false);
			}
		}
	});
});

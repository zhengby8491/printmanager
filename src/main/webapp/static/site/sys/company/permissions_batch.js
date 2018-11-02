$(function() {
	$("#checkbox_all").click(function(){
		if($(this).prop("checked"))
		{
			$(".table_cont table input:checkbox").prop("checked",true);
		}else
		{
			$(".table_cont table input:checkbox").prop("checked",false);
		}
	});
	
	$("#node .node1 input:checkbox").click(function(){
		var table=$(this).parent().parent().parent().parent();
		if($(this).prop("checked")){
			table.find("input:checkbox").prop("checked",true);
		}
		else{
			table.find("input:checkbox").prop("checked",false);
		}
	});
	$("#node .node2 input:checkbox").click(function(){
		var table=$(this).parent().parent().parent().parent();
		var parent_table=table.parents("table");
		
		if($(this).prop("checked")){
			table.find("input:checkbox").prop("checked",true);
			parent_table.find("input:checkbox").first().prop("checked",true);
		}
		else{
			table.find("input:checkbox").prop("checked",false);
			if(parent_table.find("input[type=checkbox]:checked").length<=1)
			{//父节点除了本身没有其他子节点被选择了，设置父节点为不选择状态
				parent_table.find("input:checkbox").first().prop("checked",false);
			}
		}
	});
	$("#node .node3 input:checkbox").click(function(){
		var tr=$(this).parent().parent().parent();
		var table=tr.parent();
		var parent_table=table.parents("table");
		
		if($(this).prop("checked")){
			tr.find("input:checkbox").prop("checked",true);
			table.find("input:checkbox").first().prop("checked",true);
			parent_table.find("input:checkbox").first().prop("checked",true);
		}
		else{
			tr.find("input:checkbox").prop("checked",false);
			if(table.find("input[type=checkbox]:checked").length<=1)
			{//父节点除了本身没有其他子节点被选择了，设置父节点为不选择状态
				table.find("input:checkbox").first().prop("checked",false);
			}
			
			if(parent_table.find("input[type=checkbox]:checked").length<=1)
			{//父节点除了本身没有其他子节点被选择了，设置父节点为不选择状态
				parent_table.find("input:checkbox").first().prop("checked",false);
			}
		}
	});
	$("#node .node4 input:checkbox").click(function(){
		var tr=$(this).parent().parent().parent();
		var table=tr.parent();
		var parent_table=table.parents("table");
		
		if($(this).prop("checked")){
			tr.find("input:checkbox").first().prop("checked",true);
			table.find("input:checkbox").first().prop("checked",true);
			parent_table.find("input:checkbox").first().prop("checked",true);
		}
		else{
			
			if(tr.find("input[type=checkbox]:checked").length<=1)
			{
				tr.find("input:checkbox").first().prop("checked",false);
			}
			
			if(table.find("input[type=checkbox]:checked").length<=1)
			{//父节点除了本身没有其他子节点被选择了，设置父节点为不选择状态
				table.find("input:checkbox").first().prop("checked",false);
			}
			
			if(parent_table.find("input[type=checkbox]:checked").length<=1)
			{//父节点除了本身没有其他子节点被选择了，设置父节点为不选择状态
				parent_table.find("input:checkbox").first().prop("checked",false);
			}
		}
	});
});

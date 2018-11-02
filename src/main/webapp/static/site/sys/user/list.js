$(function() {

	/*用户-添加*/
	$("#btn_user_add").click(function() {
		Helper.popup.show('添加用户',Helper.basePath+'/sys/user/add','700','435');
	});
});


function page(n,s){
	if(n) $("#pageNo").val(n);
	if(s) $("#pageSize").val(s);
	//$("#searchForm").attr("action",Helper.basePath+"/sys/user/list");
	$("#searchForm").submit();
	return false;
}
/*用户-编辑*/
function user_edit(id){
	Helper.popup.show('编辑用户',Helper.basePath+'/sys/user/edit/'+id,'700','435');
}

/*用户-停用*/
function user_stop(obj,id){
	Helper.message.confirm('确认要停用吗？',function(index){
		$.post(Helper.basePath+'/sys/user/stop/'+id,function(result){
			if(result)
			{
				$(obj).parents("tr").find(".td-manage").children().first().after('<a onClick="user_start(this,'+id+')" href="javascript:;" style="margin-right:9px;margin-left:3px" title="正常"><i class="fa fa-check-square-o"></i></a>');
				$(obj).parents("tr").find(".td-status").html('<span class="label label-closed radius">停用</span>');
				$(obj).remove();
				layer.msg('已停用!',{icon: 5,time:1000});
			}
		});
		
	});
}

/*用户-启用*/
function user_start(obj,id){
	Helper.message.confirm('确认要启用正常吗？',function(index){
		$.post(Helper.basePath+'/sys/user/start/'+id,function(result){
			if(result)
			{
				$(obj).parents("tr").find(".td-manage").children().first().after('<a onClick="user_stop(this,'+id+')" href="javascript:;" title="停用" style="margin-right:9px;margin-left:3px"><i class="fa fa-minus-square-o"></i></a>');
				$(obj).parents("tr").find(".td-status").html('<span class="label label-success radius">正常</span>');
				$(obj).remove();
				layer.msg('已正常!', {icon: 6,time:1000});
			}
		});
	});
}

/*用户-重置密码*/
function user_prompt(id){
  var url = Helper.basePath + '/sys/user/toResetPwd/' + id;
  Helper.popup.show("重置密码", url, "500", "150");
  
}

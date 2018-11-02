$(function() {

	/*系统参数-添加*/
	$("#btn_systemconfig_create").click(function() {
		Helper.popup.show('添加系统参数',Helper.basePath+'/sys/systemconfig/create','400','200');
	});
});
/*系统参数-编辑*/
function systemConfig_edit(id){
	Helper.popup.show('编辑系统参数',Helper.basePath+'/sys/systemconfig/edit?id='+id,'400','200');
}

/*系统参数-删除*/
function systemConfig_del(obj,id){
	Helper.message.confirm('确认要删除吗？',function(index){
		//此处请求后台程序，下方是成功后的前台处理……
	  console.log(id)
		$.post(Helper.basePath+'/sys/systemconfig/del?id='+id,function(result){
			if(result)
			{
				$(obj).parents("tr").remove();
				layer.msg('已删除!',{icon:1,time:1000});
			}
		});
	});
}

function page(n,s){
  if(n) $("#pageNo").val(n);
  if(s) $("#pageSize").val(s);
  //$("#searchForm").attr("action",Helper.basePath+"/sys/user/list");
  $("#searchForm").submit();
  return false;
}

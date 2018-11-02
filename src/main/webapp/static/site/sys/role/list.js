$(function() {
	
	/*角色-添加*/
	$("#btn_role_create").click(function() {
		Helper.popup.show('添加角色',Helper.basePath+'/sys/role/create','800','500',true);
	});
});

/*角色-编辑*/
function role_edit(id, roleType){
	Helper.popup.show('编辑角色',Helper.basePath+'/sys/role/edit/'+id + '?roleType='+roleType,'800','500',true);
}
/*角色-删除*/
function role_del(obj,id){
	Helper.message.confirm('确认要删除吗？',function(index){
	    Helper.request({
          url : Helper.basePath+'/sys/role/del',
          data: {"id":id},
          async : false,
          success : function(data) {
            if (data.success) {
                $(obj).parents("tr").remove();
                Helper.message.suc('已删除!');
            } else {
                Helper.message.warn(data.message);
            }
          }
        });
	    
		//此处请求后台程序，下方是成功后的前台处理……
		/*Helper.request(Helper.basePath+'/sys/role/del/'+id,function(result){
			if(result)
			{
				$(obj).parents("tr").remove();
				layer.msg('已删除!',{icon:1,time:1000});
			}
		});*/
	});
}

function page(n,s){
    if(n) $("#pageNo").val(n);
    if(s) $("#pageSize").val(s);
    //$("#searchForm").attr("action",Helper.basePath+"/sys/user/list");
    $("#searchForm").submit();
    return false;
  }

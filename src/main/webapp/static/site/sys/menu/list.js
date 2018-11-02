$(function() {
	$("#menuTable").treetable({expandable: true ,initialState:"collapsed"});
	 //选中高亮
	   $("#menuTable tbody").on("mousedown", "tr", function() {
	     $(".selected").not(this).removeClass("selected");
	    $(this).toggleClass("selected");
	   });
	 //双击展开关闭效果
	   $("#menuTable tbody").on("dblclick", "tr", function() {
		   $("#menuTable").treetable("node",$(this).attr('data-tt-id')).toggle();
	   });
	   
	/*添加*/
	$("#btn_menu_create").click(function() {
		Helper.popup.show('添加菜单',Helper.basePath+'/sys/menu/create','700','320');
	});
	
	
	$("#isShowFunction").click(function(){
	    if($(this).is(':checked'))
	    {
	        $(this).val("true");
	    }else
	    {
          $(this).val("false");
	    }
	    $("#searchForm").submit();
	 })
});
/*菜单-编辑*/
function menu_edit(id){
	Helper.popup.show('编辑菜单',Helper.basePath+'/sys/menu/edit/'+id,'700','320');
}
/*菜单-添加*/
function menu_add(id){
	Helper.popup.show('添加菜单',Helper.basePath+'/sys/menu/create?parentId='+id,'700','320');
}
/*菜单-删除*/
function menu_del(obj,id){
	Helper.message.confirm('确认要删除吗？',function(index){
		//此处请求后台程序，下方是成功后的前台处理……
		$.post(Helper.basePath+'/sys/menu/del/'+id,function(result){
			if(result)
			{
				$(obj).parents("tr").remove();
				layer.msg('已删除!',{icon:1,time:1000});
			}
		});
	});
}

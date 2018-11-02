$(function() {

  /*短信接入商-添加*/
  $("#btn_smsportal_create").click(function() {
    Helper.popup.show('添加接入商',Helper.basePath+'/sys/smsportal/create','1000','500');
  });
  /*短信接入商-测试*/
  $("#btn_smsportal_test").click(function() {
      Helper.popup.show('测试接入商',Helper.basePath+'/sys/smsportal/test','1000','500');
    });
});

/*短信接入商-编辑*/
function smsportal_edit(id){
  Helper.popup.show('编辑接入商',Helper.basePath+'/sys/smsportal/edit/'+id,'1000','500');
}

/*短信接入商-删除*/
function smsportal_del(obj,id){
  Helper.message.confirm('确认要删除吗？',function(index){
    //此处请求后台程序，下方是成功后的前台处理……
    $.post(Helper.basePath+'/sys/smsportal/del/'+id,function(result){
      if(result)
      {
        $(obj).parents("tr").remove();
        layer.msg('已删除!',{icon:1,time:1000});
      }
    });
  });
}
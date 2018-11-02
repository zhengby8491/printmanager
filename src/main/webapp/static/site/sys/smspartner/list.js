$(function() {

  /*短信合作商-添加*/
  $("#btn_smspartner_create").click(function() {
    Helper.popup.show('添加短信渠道',Helper.basePath+'/sys/smspartner/create','1000','500');
  });
});

/*短信合作商-编辑*/
function smsPartner_edit(id){
  Helper.popup.show('编辑短信渠道',Helper.basePath+'/sys/smspartner/edit/'+id,'1000','500');
}

/*短信合作商-删除*/
function smsPartner_del(obj,id){
  Helper.message.confirm('确认要删除吗？',function(index){
    //此处请求后台程序，下方是成功后的前台处理……
    $.post(Helper.basePath+'/sys/smspartner/del/'+id,function(result){
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
  $("#searchForm").submit();
  return false;
}
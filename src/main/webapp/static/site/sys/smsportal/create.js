function checkData(){
  var flag=true;
  if(Helper.isEmpty($("input[name='name']").val()))
  {
    Helper.message.warn("接入商名称不能为空");
    flag=false;
    return false;
  }
  
  if(flag){
    form_submit();
  }
}

function form_submit()
{
 var request_json=$("#jsonForm").formToJson();
console.log(request_json);

  $("#btn_save").attr({"disabled":"disabled"});
  Helper.request({
    url: Helper.basePath+"/sys/smsportal/save",  
    data: request_json,//将form序列化成JSON字符串  
    success: function(data){  
      if(data.success){
        Helper.message.suc('已保存!');
        parent.location.href=Helper.basePath + '/sys/smsportal/list/';
      }else
      {
      	Helper.message.warn('保存失败!'+data.message);
        $("#btn_save").removeAttr("disabled");
      }
    },  
    error: function(data){  
      console.log(data)
      Helper.message.warn('保存失败!'+data.message);
    }  
  });  
}
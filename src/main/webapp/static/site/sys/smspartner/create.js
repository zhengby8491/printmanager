
/*点击页签切换表格*/
$(function() {
  $('.info_item').eq(0).show();//显示第一个表格
  
  $(document).on('keypress',"#priority",function(){
    return (/\d/.test(String.fromCharCode(event.keyCode)));
  });
  
  $(document).on('keypress',"#id",function(){
    return (/\d/.test(String.fromCharCode(event.keyCode)));
  });
})

function checkData(){
  var flag=true;
  if(Helper.isEmpty($("input[name='id']").val()))
  {
    Helper.message.warn("渠道编号不能为空");
    flag=false;
    return false;
  }
  if(Helper.isEmpty($("input[name='name']").val()))
  {
    Helper.message.warn("渠道名称不能为空");
    flag=false;
    return false;
  }
  
  $("input[name='config_key']").each(function(){
    if($(this).val()==""){
    	Helper.message.warn('配置属性不能为空！')
      flag=false;
      return false;
    }
  });
  $("input[name='config_value']").each(function(){
    if($(this).val()==""){
    	Helper.message.warn('配置属性值不能为空！')
      flag=false;
      return false;
    }
  });
  if(flag){
    form_submit();
  }
}

function form_submit()
{
 var request_json=$("#jsonForm").formToJson();
 request_json.extConfigs={};
  $("input[name='config_key']").each(function(){
    var _key=$(this).val();
    var _value=$(this).parent().parent().find("input[name='config_value']").val();
    if(Helper.isNotEmpty(_key)&&Helper.isNotEmpty(_value))
    {
      
      request_json.extConfigs[_key]=_value;
    
    }
  });
console.log(request_json);

  $("#btn_save").attr({"disabled":"disabled"});
  Helper.request({
    url: Helper.basePath+"/sys/smspartner/save",  
    data: request_json,//将form序列化成JSON字符串  
    success: function(data){  
      if(data.success){
        Helper.message.suc('已保存!');
        parent.location.href=Helper.basePath + '/sys/smspartner/list/';
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
function addtr_extConfigs() {
  var tab = document.getElementById("smsPartner_extConfigs");
  //添加行
  var newTR = tab.insertRow(tab.rows.length);
  //获取序号=行索引
  var xuhao = newTR.rowIndex.toString();
  var index = newTR.rowIndex-1;
  newTR.id = "tr" + xuhao;
  //添加列:属性
  var newEmailTD = newTR.insertCell(0);
  //添加列内容
  newEmailTD.innerHTML = "<input class='tab_input' name='config_key' id='config_key' type='text' />";
  //添加列:zh
  var newTelTD = newTR.insertCell(1);
  //添加列内容
  newTelTD.innerHTML = "<input class='tab_input' name='config_value' id='config_value' type='text' />";
  //添加列:删除按钮
  var newDeleteTD = newTR.insertCell(2);
  //添加列内容
  newDeleteTD.innerHTML = "<a title='删除' href='javascript:;' onclick=\"deltr_extConfigs('tr" + xuhao + "')\"><i class='fa fa-trash-o'></i></a>";
 
}
//删除其中一行
function deltr_extConfigs(trid) { //alert(trid);
  var tab = document.getElementById("smsPartner_extConfigs");
  var row = document.getElementById(trid);
  var index = row.rowIndex;//rowIndex属性为tr的索引值，从0开始  
  tab.deleteRow(index-1); //从table中删除
  //重新排列序号，如果没有序号，这一步省略
  var nextid;
  for(i = index; i < tab.rows.length; i++)
  {
    tab.rows[i].cells[0].innerHTML = i.toString();
    nextid = i + 1;
  }
}
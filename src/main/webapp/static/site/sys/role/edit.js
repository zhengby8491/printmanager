$(function() {
	var roleType = $("#roleType").val();
	var defaultSuccess = '/sys/role/list';
	if('high_role' == roleType)
		defaultSuccess = '/sys/role/allList';

  $("#btn_save").click(function()
  {
      $("#btn_save").attr("disabled", "disabled");
      var menuIdList = $("table input[type='checkbox']:checked").map(function()
      {
          return $(this).val();
      }).get();
      Helper.Remote.request({
          url: Helper.basePath + "/sys/role/update",
          data: {
              id: $("#id").val(),
              name: $("#name").val(),
              memo: $("#memo").val(),
              menuIdList: menuIdList
          },
          success: function(data)
          {
              if(data.success) {
                  parent.location.href = Helper.basePath + defaultSuccess;
              }else {
                  Helper.message.warn('操作失败：' + data.message);
                  $("#btn_save").removeAttr("disabled");
              }
          },
          error: function(data)
          {
              Helper.message.warn('操作失败：' + data);
              $("#btn_save").removeAttr("disabled");
          }
      });
  });
	
	
	$("#form-role").validate({
		submitHandler : function(form) {// 必须写在验证前面，否则无法ajax提交
			var  menuIdArray=new Array();
			$("table input[type='checkbox']").each(function(){
				var check=$(this).prop("checked");//判断是否选中
				if(check){ 
					var  value=$(this).val();
					menuIdArray.push(value);
				}
			})
			$("#menuIds").val(menuIdArray.toString());
			//alert($("#menuIds").val());
			Helper.Remote.fromSubmit(form, {// 验证新增是否成功
				type : "post",
				url:Helper.basePath+"/sys/role/update",
				dataType : "json",
				async:false,
				success : function(data) {
					if (data.success) {
						parent.location.href=Helper.basePath+"/sys/role/list";
						Helper.popup.close();
						return false;
					} else {
						layer.alert('更新失败：'+data.message);
					}
				}
			});
		},
		rules : {
			name : {required : true}
		},
		onkeyup:false,
		focusCleanup:true//如果该属性设置为True, 那么控件获得焦点时，移除出错的class定义，隐藏错误信息
	});
});

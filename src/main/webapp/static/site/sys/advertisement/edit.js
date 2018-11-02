$(function()
        {   
//         	$.validator.addMethod("linkedUrl",function(value,element,params){
//         		var reg = /^([a-zA-Z\d][a-zA-Z\d-_]+\.)+[a-zA-Z\d-_][^ ]*$/;
//         	    return reg.test(value);
//         	})
            $("#jsonForm").validate({

                submitHandler: function()
                {// 必须写在验证前面，否则无法ajax提交
                    $(jsonForm).ajaxSubmit({
                        url: Helper.basePath + "/sys/advertisement/update",
                        type: 'POST',
                        dataType: 'json',
                        success: function(data)
                        {
                            if(data.success) {
                                parent.location.href = Helper.basePath + "/sys/advertisement/list";
                            }else {
                                layer.alert('创建失败：' + data.message);
                            }
                        }
                    });
                },
//                 rules: {
//                 	linkedUrl:{
//                 		required:false
//                 	}
//                 },
//                 messages: {
//                 	linkedUrl: {
//                 		required:"请输入正确网址" 	
//                 	}
//                 },
                onkeyup: false
            });
        });
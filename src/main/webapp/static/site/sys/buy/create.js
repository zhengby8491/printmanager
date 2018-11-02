$(function(){
$("#jsonForm").validate({
        submitHandler: function()
        {
        	
        	
            Helper.request({// 验证新增是否成功
                url: Helper.basePath + "/sys/buy/save",
                data: $("#jsonForm").formToJson(),
                async: false,
                success: function(data)
                {
                    if(data.success) {
                        parent.location.href = Helper.basePath + "/sys/buy/list";
                    }else {
                        layer.alert('创建失败：' + data.message);
                    }
                }
            });
        },
        rules: {
            title: {
                required: true,
            }
        },
        onkeyup: false,
        focusCleanup: true
    });
});
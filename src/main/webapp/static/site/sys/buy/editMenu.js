$(function()
    {
        $("#btn_save").click(function()
        {
            $("#btn_save").attr("disabled", "disabled");
            var menuIdList = $("table input[type='checkbox']:checked").map(function()
            {
                return $(this).val();
            }).get();
            Helper.Remote.request({
                url: Helper.basePath + "/sys/buy/updateMenu",
                data: {
                    id: $("#id").val(),
                    menuIdList: menuIdList
                },
                success: function(data)
                {
                    if(data.success) {
                        parent.location.href = Helper.basePath + "/sys/buy/list";
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
    });
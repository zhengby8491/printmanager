$(function()
{
    var ue = UE.getEditor('ueDiv', {
        initialFrameHeight: 100,
        enableAutoSave: false,
        toolbars: [['Undo', 'Redo', 'Bold', 'Italic', 'Underline', '|', 'FontFamily', 'FontSize', 'ForeColor', 'backColor', '|', 'JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyJustify', 'Preview', 'FullScreen', 'Source']]
    });
    $("#jsonForm").validate({
        
        submitHandler: function()
        {// 必须写在验证前面，否则无法ajax提交
            $("#content").val(ue.getContent());
            Helper.request({// 验证新增是否成功
                url: Helper.basePath + "/sys/versionNotice/save",
                data: $("#jsonForm").formToJson(),
                async: false,
                success: function(data)
                {
                    if(data.success) {
                        parent.location.href = Helper.basePath + "/sys/versionNotice/list";
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

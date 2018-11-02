var UEditor;
    $(function()
    {
        var ue = UE.getEditor('reply',{
		        initialFrameHeight:100,
		        enableAutoSave: false,
		        toolbars: [
		                   ['Undo','Redo','Bold','Italic','Underline','|','FontFamily','FontSize','ForeColor','backColor','|','JustifyLeft',
		                    'JustifyCenter','JustifyRight','JustifyJustify','Preview','FullScreen','Source'
		                    ]
		              ]
	    });
        UEditor=ue;
        $("#detail_return").click(function(){
            var url=Helper.basePath + "/sys/service/myQuestion";
    		var title="服务支持";
    		admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"))
        });
        
        $("#reply_sub").click(function()
        {
            var validate = true;
            if(ue.getContentTxt().trim() == "") {
                Helper.message.warn("内容不能为空");
                validate = false;
                return;
            }
            if(validate == true) {
                save();
            }
        });
    })

    function save()
    {
        Helper.request({
            url: Helper.basePath + "/sys/comment/reply",
            data: {id:$("#id").val(),reply:UEditor.getContent(),isManagerReplay:"NO"},
            error: function(request)
            {
            	  Helper.message.warn("服务器繁忙");
            },
            success: function(data)
            {
                if(data.success) {
                    var url=Helper.basePath + "/sys/comment/question_detail/"+data.obj.id;
          		    var title="服务支持";
          		    admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"))
                }else {
                	  Helper.message.warn("服务器繁忙");
                }

            }
        });
    }
   
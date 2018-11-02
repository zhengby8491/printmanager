var UEditor;
    $(function()
    {
        var ue = UE.getEditor('content',{
		        initialFrameWidth: 900,
		        initialFrameHeight:250,
		        enableAutoSave: false,
		        toolbars: [
		                   ['Undo','Redo','Bold','Italic','Underline','|','FontFamily','FontSize','ForeColor','backColor','|','JustifyLeft',
		                    'JustifyCenter','JustifyRight','JustifyJustify','Preview','insertimage','FullScreen'
		                    ]
		              ]
	    });
        UEditor=ue;
        $("#sub_online_ask").click(function()
        {
            var validate = true;
            if($("#title").val().trim() == "") {
            	alert(0);
                Helper.message.warn("标题不能为空");
                validate = false;
                return;
            }
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

    //在线提问 保存
    function save()
    {
        Helper.request({
            url: Helper.basePath + "/sys/comment/save",
            data: {title:$("#title").val(),content:UEditor.getContent()},
            error: function(request)
            {
            	  Helper.message.warn("服务器繁忙");
            },
            success: function(data)
            {
                if(data.success) {
                    var url=Helper.basePath + "/sys/service/myQuestion";
          		     var title="服务支持";
          		    admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"))
                }else {
                	  Helper.message.warn("服务器繁忙");
                }

            }
        });
    }
    function admin_tab(obj)
    {
    	if ($(obj).attr('_href'))
    	{
    		var bStop = false;
    		var bStopIndex = 0;
    		var _href = $(obj).attr('_href');
    		var _titleName = $(obj).attr("data-title");
    		var _refresh = $(obj).attr('refresh');
    		var topWindow = $(window.parent.document);
    		var show_navLi = topWindow.find("#min_title_list li");
    		/* 遍历顶部整个li列表，判断是否存在左边菜单选项 */
    		show_navLi.each(function()
    		{
    			if ($(this).find('span').attr("title") == _titleName)
    			{
    				bStop = true;
    				bStopIndex = show_navLi.index($(this));
    				return false;
    			}
    		});
    		if (!bStop)
    		{
    			creatIframe(_href, _titleName, _refresh);
    			min_titleList();
    		}
    		/* 给点击标签li赋active 显示对应标签的frame */
    		else
    		{
    			show_navLi.removeClass("active").eq(bStopIndex).addClass("active");
    			show_navLi.eq(bStopIndex).find('span').html(_titleName);
    			var iframe_box = topWindow.find("#iframe_box");
    			/* 火狐display:none元素获取不到兼容写法 */
    			/*
    			 * iframe_box.find(".show_iframe").hide().eq(bStopIndex).show().find("iframe").attr("src",
    			 * _href);
    			 */
    			iframe_box.find(".show_iframe").eq(bStopIndex).show().find("iframe").attr("src", _href);
    			iframe_box.find(".show_iframe").eq(bStopIndex).siblings(".show_iframe").hide();
    		}
    	}
    }
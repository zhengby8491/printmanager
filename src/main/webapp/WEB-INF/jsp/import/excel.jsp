<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>导入</title>
<style type="text/css">
.import_container {
	width: 100%;
	padding: 30px 20px 10px;
}

.import_c {
	margin: 0 auto;
	float: left
}

.import_c .spanTit {
	color: #6c6c6c
}

.import_c .nav_btn {
	padding: 4px 12px
}

.import_input {
	margin-bottom: 10px;
}

.file_btn {
	position: absolute;
	opacity: 0;
	visibility: hidden;
}

.msg {
	color: #f00;
}

.download_muban a {
	color: #0376BB;
	text-decoration: underline
}
</style>
</head>
<body>
	<form class="form-horizontal" enctype="multipart/form-data" role="form" id="form">
		<div class="import_container">
			<div class="import_c">
				<input type="hidden" name="filetype" value="${filetype }">
				<div class="import_input">
					<span class="spanTit">请选择要导入的文件：</span>
					<input type="text" class="input-txt" style="width: 250px; height: 27px" readonly id="txt" />
					<input type="button" id="file_btn" class="nav_btn table_nav_btn" value="浏览" />
					<input type="file" id="file_import" name="excel" class="file_btn nav_btn table_nav_btn" />
					<input type="button" class="nav_btn table_nav_btn" id="submit_btn" value="导入" />
				</div>
				<div id="msg" class="msg"></div>
				<div class="download_muban">
					<a href="${ctx }/downLoad/do?filetype=${filetype }">下载导入模板</a>
				</div>
			</div>
		</div>
	</form>
	<script type="text/javascript">
        $(function()
        {
            var loadIndex;
            $('#file_import').change(function()
            {
                $('#txt').val($('#file_import').val().split("\\").pop());
            });
            $("#file_btn").click(function()
            {	
                $("#file_import").trigger("click");
            });
            $("#submit_btn").click(function()
            {	
                if($('#file_import').val() == ""){
                    layer.msg('请选择导入文件', {time: 2000, icon:0});  
                }
                $("#form").submit();
            });
            $("#form").validate({
                rules: {
                    excel: {
                        required: true
                    }
                },
                messages: {
                    excel: {
                        required: "",
                    }
                },
                submitHandler: function()
                {
                    $(form).ajaxSubmit({
                        type: "post", 
                        dataType:"json",
                        url: Helper.basePath + "/import/do",
                        beforeSubmit: function(){
                            loadIndex = layer.load(1, {
                                shade: [0.3,'#fff'] 
                              });
                        },
                        success: function(data)
                        {   
                            if(data.success){
                                layer.close(loadIndex);
                                layer.alert(data.message, {icon:6},
                                                function(){
                                    window.parent.location.reload();
                                    Helper.popup.close();
                                });
                            }else{
                                layer.close(loadIndex);
                                $("#msg").empty().html(data.message);
                                
                            }
                          
                            
                        },
                        error: function(data)
                        {	
                            layer.close(loadIndex);
                            $("#msg").empty().html(data.msg);
                            //layer.msg('处理失败', {time: 2000, icon:2});
                        }
                    });
                }
           	});

        });
    </script>
</body>
</html>

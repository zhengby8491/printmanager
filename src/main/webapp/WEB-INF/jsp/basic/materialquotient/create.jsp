<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>创建轮播广告</title>
</head>
<body>
	<div class="layer_container">
		<form id="jsonForm">
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					LOGO：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<span class="">
						<input type="file" class="input-txt_11" value="" placeholder="" id="pic" name="pic">
					</span>
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">名称：</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11" value="" placeholder="" id="name" name="name">
				</div>
			</div>

			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">联系人：</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11" value="" placeholder="" id="linkName" name="linkName">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">联系电话：</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11" value="" placeholder="" id="telNum" name="telNum">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">地址：</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11" value="" placeholder="" id="address" name="address">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">区域：</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11" value="" placeholder="" id="area" name="area">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">代理商属性：</label>
				<div class="row-div">
					<span>
						<phtml:list cssClass="input-txt input-txt_11 hy_select2" textProperty="text" type="com.huayin.printmanager.persist.enumerate.AgentType" name="agentType"></phtml:list>
					</span>
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar"></label>
				<div class="form-label label_ui">
					<input class="nav_btn table_nav_btn" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
				</div>
			</div>
		</form>
	</div>
	<script type="text/javascript">
        $(function()
        {
            $("#jsonForm").validate({

                submitHandler: function()
                {// 必须写在验证前面，否则无法ajax提交
                    $(jsonForm).ajaxSubmit({
                        url: Helper.basePath + "/sys/agentquotient/save",
                        type: 'POST',
                        dataType: 'json',
                        success: function(data)
                        {
                            if(data.success) {
                                parent.location.href = Helper.basePath + "/sys/agentquotient/list";
                            }else {
                                layer.alert('创建失败：' + data.message);
                            }
                        }
                    });
                },
                rules: {
                    pic: {
                        required: true
                    },
                },
                messages: {
                	pic: "请选择图片"
                },
                onkeyup: false
            });
        });
    </script>
</body>
</html>
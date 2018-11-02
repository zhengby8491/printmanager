<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>材料-快速添加</title>
<script type="text/javascript">
    $(function()
    {
        $("#material_form").validate({
            submitHandler: function()
            {// 必须写在验证前面，否则无法ajax提交
                Helper.request({
                    url: Helper.basePath + "/basic/material/save",
                    data: $("#material_form").formToJson(),//将form序列化成JSON字符串  
                    async: false,
                    success: function(data)
                    {
                        if(data.success) {
                            parent.layer.close();
                            parent.location.reload();
                            return false;
                        }else {
                            Helper.message.alert('创建失败：' + data.message);
                        }
                    }
                });
            },
            rules: {
                name: {
                    required: true
                },
                weight: {
                    required: true
                }
            },
            onkeyup: false,
            onfocusout: false,
            focusCleanup: true,
            onsubmit: true
        });
        $("#btn_cancel").click(function()
        {
            Helper.popup.close();
        });
        /* 下拉框样式--只应用此页面 */
        $(".select2").select2({
            language: "zh-CN",
            minimumResultsForSearch: 100,
        });
    })
</script>
<style type="text/css">
.select2-container--default .select2-dropdown .select2-results>.select2-results__options {
	max-height: 90px !important
}
</style>
</head>
<body>
	<form id="material_form">
		<div class="layer_container product_newadd">
			<div class="newadd_container">
				<table class="newadd_content" coldrag="false">
					<tbody>
						<tr>
							<td class="newadd_item_tt">
								<label>材料分类：</label>
							</td>
							<td class="newadd_txt">
								<phtml:list items="${fns:basicList('MATERIALCLASS')}" valueProperty="id" textProperty="name" name="materialClassId" cssClass="input-txt input-txt_6 select2" selected="${materialClassId }" />
							</td>
							<td class="newadd_item_tt newadd_label">
								<label>材料类别：</label>
							</td>
							<td class="newadd_txt">
								<phtml:list name="materialType" selected="${materialType}" textProperty="text" cssClass="input-txt input-txt_6 select2" type="com.huayin.printmanager.persist.enumerate.MaterialType"></phtml:list>
							</td>
						</tr>
						<tr>
							<td class="newadd_item_tt">
								<label>材料名称：</label>
							</td>
							<td class="newadd_txt">
								<input class="input-txt" type="text" value name="name" />
							</td>
							<td class="newadd_item_tt newadd_label">
								<label>库存单位：</label>
							</td>
							<td class="newadd_txt">
								<phtml:list items="${fns:basicList('UNIT')}" valueProperty="id" textProperty="name" name="stockUnitId" cssClass="input-txt input-txt_6 select2" selected="${stockUnitId }" />
							</td>
						</tr>
						<tr>
							<td class="newadd_item_tt">
								<label>计价单位：</label>
							</td>
							<td class="newadd_txt">
								<phtml:list items="${fns:basicList('UNIT')}" valueProperty="id" textProperty="name" name="valuationUnitId" cssClass="input-txt input-txt_6 select2 select2Width" selected="${valuationUnitId }" />
							</td>
							<td class="newadd_item_tt newadd_label">
								<label>克重：</label>
							</td>
							<td class="newadd_txt">
								<input class="input-txt constraint_negative" type="text" value="0" name="weight" />
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="layer_btns" style="margin-right: 60px">
				<input type="submit" class="nav_btn table_nav_btn" value="保存并关闭" />
				<input id="btn_cancel" type="button" class="nav_btn table_nav_btn" value="取消" />
			</div>
		</div>
	</form>
</body>
</html>
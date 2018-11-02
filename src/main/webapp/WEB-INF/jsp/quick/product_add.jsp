<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>产品-快速添加</title>
<script type="text/javascript">
    $(function()
    {

        $("#product_form").validate({
            submitHandler: function()
            {// 必须写在验证前面，否则无法ajax提交
                Helper.request({
                    url: Helper.basePath + "/basic/product/save",
                    data: $("#product_form").formToJson(),//将form序列化成JSON字符串  
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
                "name": {
                    required: true
                }
            },
            onkeyup: false,
            onfocusout: false,
            //focusCleanup : true,
            onsubmit: true
        });

        $("#btn_cancel").click(function()
        {
            Helper.popup.close();
        })

    });
</script>
</head>
<body>
	<form id="product_form">

		<div class="layer_container csr_newadd">
			<div class="newadd_container" style="padding-top: 30px">
				<table class="newadd_content" coldrag="false">
					<tbody>
						<tr>

							<input type="hidden" name="customerList.id" value="${customerId }" />
							<input type="hidden" name="isPublic" value="${customerId eq null?'YES':'NO' }" />
							<td class="newadd_item_tt">
								<label>产品分类：</label>
							</td>
							<td class="newadd_txt">
								<phtml:list items="${fns:basicList('PRODUCTCLASS')}" valueProperty="id" name="productClassId" textProperty="name" cssClass="newadd_slt hy_select2" />
							</td>
						</tr>
						<tr>
							<td class="newadd_item_tt">
								<label>成品名称：</label>
							</td>
							<td class="newadd_txt newadd_addr">
								<input name="name" type="text" class="input-txt newadd_csr_name" />
							</td>
						</tr>
						<tr>
							<td class="newadd_item_tt">
								<label>客户料号：</label>
							</td>
							<td class="newadd_txt newadd_addr">
								<input type="text" name="customerMaterialCode" class="input-txt" />
							</td>
						</tr>
						<tr>
							<td class="newadd_item_tt">
								<label>产品规格：</label>
							</td>
							<td class="newadd_txt">
								<input type="text" name="specifications" class="input-txt" />
							</td>
						</tr>
						<tr>
							<td class="newadd_item_tt">
								<label>单位：</label>
							</td>
							<td class="newadd_txt">
								<phtml:list items="${fns:basicList('UNIT')}" valueProperty="id" name="unitId" textProperty="name" cssClass="newadd_slt hy_select2" />
							</td>
						</tr>
					</tbody>
				</table>
				<div class="layer_btns">
					<input type="submit" class="nav_btn table_nav_btn" value="保存并关闭" id="btn_add" />
					<input type="reset" class="nav_btn table_nav_btn" value="重置" id="btn_reset" />
					<input type="button" class="nav_btn table_nav_btn" value="取消" id="btn_cancel" />
				</div>
			</div>
		</div>
	</form>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>供应商-快速添加</title>
<style type="text/css">
	.newadd_addr input {width:360px}
</style>
<script type="text/javascript">
	$(function()
	{
		$("#supplier_form").validate({
			submitHandler : function()
			{
				fixEmptyValue();
				// 必须写在验证前面，否则无法ajax提交
				Helper.request({
					url : Helper.basePath + "/basic/supplier/save",
					data : $("#supplier_form").formToJson(),//将form序列化成JSON字符串  
					async : false,
					success : function(data)
					{
						if (data.success)
						{
							parent.layer.close();
							parent.location.reload();
							return false;
						} else
						{
							Helper.message.alert('创建失败：' + data.message);
						}
					}
				});
			},
			rules : {
				name : {
					required : true
				},
				"addressList.address" : {
					required : true
				}
			},
			onkeyup : false,
			onfocusout : false,
			focusCleanup : true,
			onsubmit : true
		});

		$("#btn_cancel").click(function()
		{
			Helper.popup.close();
		})
	});
</script>
</head>
<body>
	<form id="supplier_form">
		<input type="hidden" name="type" id="type" value="${defaultSupplierType }" />
		<input type="hidden" name="originCompanyId" id="originCompanyId" value="${originCompanyId }" />
		<div class="layer_container csr_newadd">
			<div class="newadd_container">
				<table class="newadd_content" coldrag="false">
					<tbody>
						<tr>
							<td class="newadd_item_tt">
								<label>供应商分类：</label>
							</td>
							<td class="newadd_txt">
								<phtml:list items="${fns:basicList('SUPPLIERCLASS')}" valueProperty="id" name="supplierClassId" textProperty="name" cssClass="input-txt newadd_slt hy_select2" />
							</td>
							<td class="newadd_item_tt newadd_label">
								<label>采购员：</label>
							</td>
							<td class="newadd_txt">
								<phtml:list items="${fns:basicList2('EMPLOYEE')}" cssClass="newadd_slt hy_select2 shotcut" onchange="shotCutWindow('EMPLOYEE',true,$(this))" valueProperty="id" name="employeeId" textProperty="name"></phtml:list>
							</td>
						</tr>
						<tr>
							<td class="newadd_item_tt">
								<label>
									<span class="c-red">*</span>
									供应商名称：
								</label>
							</td>
							<td colspan="3" class="newadd_txt newadd_addr">
								<input name="name" type="text" class="input-txt newadd_csr_name" value="${supplierName }" />
							</td>
						</tr>
						<tr>
							<td class="newadd_item_tt">
								<label>
									<span class="c-red">*</span>
									供应商地址：
								</label>
							</td>
							<td colspan="3" class="newadd_txt newadd_addr">
								<input type="text" name="addressList.address" class="input-txt" value="${supplierAddress }" />
							</td>
						</tr>
						<tr>
							<td class="newadd_item_tt">
								<label>联 系 人：</label>
							</td>
							<td class="newadd_txt">
								<input type="text" name="addressList.userName" class="input-txt" value="${supplierLinkName }"/>
							</td>
							<td class="newadd_item_tt newadd_label">
								<label>联系电话：</label>
							</td>
							<td class="newadd_txt">
								<input class="input-txt constraint_tel" type="text" name="addressList.mobile" value="${supplierMobile }" />
							</td>
						</tr>
						<tr>
							<td class="newadd_item_tt">
								<label>送货方式：</label>
							</td>
							<td class="newadd_txt">
								<phtml:list items="${fns:basicList2('DELIVERYCLASS')}" valueProperty="id" onchange="shotCutWindow('DELIVERYCLASS',true,$(this))" name="deliveryClassId" textProperty="name" cssClass="newadd_slt hy_select2 shotcut" />
							</td>
							<td class="newadd_item_tt newadd_label">
								<label>付款方式：</label>
							</td>
							<td class="newadd_txt">
								<phtml:list items="${fns:basicList2('PAYMENTCLASS')}" valueProperty="id" onchange="shotCutWindow('PAYMENTCLASS',true,$(this))" name="paymentClassId" textProperty="name" cssClass="hy_select2 newadd_slt shotcut" />
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
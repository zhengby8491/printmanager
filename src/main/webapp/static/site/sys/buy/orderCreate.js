

$(function(){
	var mapCompany = {};
	var mapBuy = {};

	/* 选择客户 */
	$("#selectCompany").click(
		function() {
			Helper.popup.show('选择公司', Helper.basePath
					+ '/quick/company_select?multiple=false',
					'900', '490');
	});
	
	$("#selectProduct").click(
		function(){
			Helper.popup.show('选择产品', Helper.basePath + '/quick/buyProductSelect', '900', '490');
		}
	);
	
	$("#jsonForm").validate({
        submitHandler: function()
        {
			if($("#companyName").val() == "")
			{
				layer.alert('请选择公司名称');
				return false;
			}
			if($("#productName").val() == "")
			{
				layer.alert('请选择服务名称');
				return false;
			}
        	
            Helper.request({// 验证新增是否成功
                url: Helper.basePath + "/sys/buy/orderSave",
                data: $("#jsonForm").formToJson(),
                async: false,
                success: function(data)
                {
                    if(data.success) {
                        parent.location.href = Helper.basePath + "/sys/buy/orderList";
                    }else {
                        layer.alert('创建失败：' + data.message);
                    }
                }
            });
        },
        rules: {
        	payPrice:{
        		required: true
        	}
        },
        onkeyup: false,
        focusCleanup: true
    });
	

	

	
	// 开票信息
	$("#invoiceInfor").on('change', function() {
		var invoiceInfor = $(this).val();
		var price = $("#price").val();
		if($.trim(price) != "")
			calTaxRate(price, invoiceInfor);
	});
	
	// ------ 初始化数据
	$("#payTime").val(new Date().format('yyyy-MM-dd'));
});

function calTaxRate(price, invoiceInfor)
{
	var taxRate = 0;
	if(invoiceInfor == 2)
		taxRate = 0.10;
	else if(invoiceInfor == 1)
		taxRate = 0.06;
	$("#tax").val(price * taxRate)
}

//回写公司信息
function getCallInfo_company(obj) {
	$("#companyName").val(obj.company.name);
	$("#companyId").val(obj.company.id);
	
// 	var exp = mapCompany[obj.company.id];
// 	$("#userName").val(exp.userName);
// 	$("#linkMan").val(exp.linkMan);
// 	$("#telephone").val(exp.telephone);

    $("#userName").val(obj.registerUser.userName);
 	$("#linkMan").val(obj.company.linkName || '');
 	$("#telephone").val(obj.registerUser.mobile);
}

function getCallInfo_buyOrder(row){
	if(row){
		$("#productName").val(row.name);
		$("#productId").val(row.id);
		
		$("#price").val(row.price);
		$("#bonus").val(row.bonus);
		// 计算税额
		var invoiceInfor = $("#invoiceInfor").val();
		calTaxRate(row.price, invoiceInfor);
	}
}
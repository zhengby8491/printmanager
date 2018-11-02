$(function()
{
	var path = $("#path").val();
	// 请求广告图片和链接地址
	Helper.request({
		url : Helper.basePath + "/sys/advertisement/ajaxPublisList?advertisementType=LOGIN",
		success : function(data)
		{
			for (var d = 0; d < data.length; d++)
			{
				var li = '<li><a  href="http://' + data[d].linkedUrl + '" target="_blank" data-id="' + data[d].id + '" data-linkedUrl="' + data[d].linkedUrl + '"><img src="' + data[d].photoUrl + '" /></a></li>';
				$(".slideBox").find(".bd ul").append(li);
				$(".slideBox").find(".hd ul").append("<li></li>");
			}
			$(".slideBox").find(".hd").css("margin-right", $(".slideBox").find(".hd").width() * (-1));
			$(".slideBox").slide({
				mainCell : ".bd ul",
				effect : "fade",
				delayTime : 1000,
				interTime : 6000,
				autoPlay : true
			});
		}
	})
	// 统计广告点击数
	$(document).on("click", ".slideBox a", function()
	{
		var adId = $(this).data("id");
		var ip = $("#keleyivisitorip").html();
		var address = "";
		// 新浪ip地址查询接口
		$.getScript('http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip=' + ip, function()
		{
			if (remote_ip_info.ret == '1')
			{
				address = remote_ip_info.province + remote_ip_info.city;
				Helper.request({
					url : Helper.basePath + "/sys/advertisement/ajaxStatistics?id=" + adId + "&ip=" + ip + "&address=" + address
				})
			}
		});
	})
	// 二维码
	$(".wx").hover(function()
	{
		$(".qrode").show();
	}, function()
	{
		$(".qrode").hide();
	})
	// 输入框
	$('.login_input').focus(function()
	{
		$(this).parent().addClass('active');
		$(this).next().next().css('background-position', '-35px 0');
	})
	$('.login_input').blur(function()
	{
		$(this).parent().removeClass('active');
		$(this).next().next().css('background-position', '0 0');
	})
	$("#loginForm").validate({
		rules : {
			captcha : {
				remote : path + "/servlet/captcha"
			}
		},
		messages : {
			username : {
				required : "请填写用户名."
			},
			password : {
				required : "请填写密码."
			},
			captcha : {
				required : "请填写验证码.",
				remote : "验证码错误"
			}
		}
	});
	$("#loginBtn").on("click", function()
	{
		$("#loginForm").submit();
	});
	$("body").keydown(function(event)
	{
		if (event.keyCode === 13)
		{
			$("#loginBtn").click();
		}
	});
});
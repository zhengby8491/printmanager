/* 处理键盘事件 */
function doKey(e)
{
	var ev = e || window.event; // 获取event对象
	var obj = ev.target || ev.srcElement; // 获取事件源
	var t = obj.type || obj.getAttribute('type'); // 获取事件源类型
	if (ev.keyCode == 8 && t != "password" && t != "text" && t != "textarea")
	{
		return false;
	}
}
// 禁止后退键 作用于Firefox、Opera
document.onkeypress = doKey;
// 禁止后退键 作用于IE、Chrome
document.onkeydown = doKey;

$(function()
{
	/* 加载菜单图片 */
	function LoadMenuImg()
	{
		var menuIMG = $(".menu_img");
		$(".item").hover(function()
		{
			var menuTitle = $(this).find(".item-tit").text()
			//$(this).find(".menu_img").attr("src", Helper.staticPath + "/layout/images/menu/" + menuTitle + "_h.png");
			$(this).find(".menu_arrow").show();
		}, function()
		{
			var menuTitle = $(this).find(".item-tit").text()
			//$(this).find(".menu_img").attr("src", Helper.staticPath + "/layout/images/menu/" + menuTitle + ".png");
			$(this).find(".menu_arrow").hide();
		})
	}
	LoadMenuImg();

	/* 按时间问候 */
	function timeGreeting()
	{
		var dd = new Date();
		var st = $('.timeGreeting');
		var hour = dd.getHours();// 获取当前时
		if (hour > 0 && hour <= 6)
		{
			st.html("夜猫子，该休息了！");
		} else if (hour > 6 && hour <= 8)
		{
			st.html("上午好！ ");
		} else if (hour > 8 && hour <= 11)
		{
			st.html("早上好！ ");
		} else if (hour > 11 && hour <= 13)
		{
			st.html("中午好！ ");
		} else if (hour > 13 && hour <= 17)
		{
			st.html("下午好！ ");
		} else if (hour > 17 && hour <= 18)
		{
			st.html("傍晚好！ ");
		} else if (hour > 18 && hour <= 24)
		{
			st.html("晚上好！ ");
		}
	}
	timeGreeting();
	/* 浏览器窗口大小改变时-改变菜单图标大小 */
	window.onresize = function()
	{
		changeSize();
	}
	/* 页面加载时随浏览器窗口大小-改变菜单图标大小 */
	changeSize();
	function changeSize()
	{
		/*
		 * var h = document.documentElement.clientHeight;//获取页面可见高度 alert(h) if(h<615 &&
		 * h>500){ var num = parseInt((615-h)/7); $('.item
		 * i').css({"width":(38-num)+"px","height":(38-num)+"px"}); } else if(h>615 ||
		 * h==615){ $('.item i').css({"width":"30px","height":"30px"}); }
		 */
	}
	/* 二级菜单 */
	function subMenu()
	{
		var submenu = $(".submenu");
		var length = submenu.length;
		for (var i = 0; i < length; i++)
		{
			var menu = submenu.eq(i);
			switch (menu.data("title"))
			{
			case "销售管理":
			{
				menu.css({
					"width" : "440px"
				});
				break;
			}
			case "生产管理":
			{
				menu.css({
					"width" : "240px"
				});
				break;
			}
			case "采购管理":
			{
				menu.css({
					"width" : "440px"
				});
				break;
			}
			case "发外管理":
			{
				menu.css({
					"width" : "440px"
				});
				break;
			}
			case "库存管理":
			{
				menu.css({
					"width" : "630px"
				});
				break;
			}
			case "财务管理":
			{
				menu.css({
					"width" : "440px"
				});
				break;
			}
			case "基础设置":
			{
				menu.css({
					"width" : "580px"
				});
				break;
			}
			case "系统管理":
			{
				menu.css({
					"width" : "330px"
				});
				break;
			}
			case "代工管理":
			{
				menu.css({
					"width" : "440px"
				});
				break;
			}
			default:
			{
				menu.css({
					"width" : "330px"
				});
			}
			}
		}
		if (length <= 3)
		{
			for (var i = 0; i <= length; i++)
			{
				var menu = submenu.eq(i);
				menu.css({
					"top" : "0"
				});
			}
		} else if (length > 3 && length <= 6)
		{
			for (var i = 0; i < 3; i++)
			{
				var menu = submenu.eq(i);
				menu.css({
					"top" : "0"
				});
			}
			for (var i = 3; i < length; i++)
			{
				var height = submenu.eq(i).height();
				var menu = submenu.eq(i);
				if (height < 180)
				{
					menu.css({
						"bottom" : "0"
					});
				} else if (height >= 180)
				{
					menu.css({
						"bottom" : -1 * 0.5 * height + "px"
					});
				}
			}
		} else if (length > 6 && length <= 11)
		{
			for (var i = 0; i < 3; i++)
			{
				var menu = submenu.eq(i);
				menu.css({
					"top" : "0"
				});
			}
			for (var i = 3; i < 7; i++)
			{
				var height = submenu.eq(i).height();
				var menu = submenu.eq(i);
				if (height < 180)
				{
					menu.css({
						"bottom" : "0"
					});
				} else if (height >= 180)
				{
					menu.css({
						"bottom" : -1 * 0.5 * height + "px"
					});
				}
			}
			submenu.eq(7).css({
				"bottom" : "0"
			});
			submenu.eq(8).css({
				"bottom" : "0"
			});
			submenu.eq(9).css({
				"bottom" : "0"
			});
			submenu.eq(10).css({
				"bottom" : "0"
			});
		}

		/* 移除最后字段的竖线和下边框虚线 */
		$(".submenu").find("dl:last").css("border-bottom", "none");
		$(".submenu_item").find("dd:last").find("span").remove();
	}
	subMenu();

	/* 初始化导航栏菜单 */
	$(".links_list li").each(function()
	{
		var boxWidth = $("#" + $(this).attr("src")).innerWidth() * (-0.5) + 10;
		$(this).powerFloat({
			eventType : "hover",
			targetAttr : "src",
			reverseSharp : true,
			offsets : {
				x : boxWidth,
				y : -8
			},
			container : $(this).children(".box_container"),
		});
	})
	/* 悬浮显示子菜单控制 */
	$(".nav.menu-list .item").hover(function()
	{
		$(this).children(".submenu").fadeIn(250);
	}, function()
	{
		$(this).children(".submenu").fadeOut(10);
	})
	/* 广告跑马灯 */
	$(".marquee").css({
		"left" : $(".main_title").width() + 100,
		"right" : $(".topnav_right").width()
	}).marquee({
		speed : 40,
		gap : 500,
		delayBeforeStart : 1000,
		direction : 'left',
		// duplicated: true,这条会影响IE无限滚动
		pauseOnHover : true
	});
	/* 在线提问 */
	$("#online_ask").on("click", function(e)
	{
		var url = Helper.basePath + "/sys/service/system_notice";
		var title = "服务支持";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"))
	})
	/* 新手指南 */
	$("#new_guide").on("click", function(e)
	{
		var url = Helper.basePath + "/guide/begin_guide";
		var title = "新手指南";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"))
	})
	/* 关于 */
	$("#about").on("click", function(e)
	{
		$('.about').stop(true, true);
		$('.about_mask').show();
		$('.about').animate({
			top : "50%",
			opacity : "0.9"
		}, 300);
		$(".clo_icon").click(function()
		{
			$('.about').stop(true, true);
			$(".about").animate({
				top : "30%",
				opacity : "0"
			}, 300);
			$('.about_mask').hide();
		})
	})
	/* 换肤图标 */
	$("#change_skin").click(function()
	{
		$(".skin_list").toggle();
	})
	/* 切换 */
	$(".skin_list>ul>li>span").click(function()
	{
		var cssType = $(this).siblings("input").val(), skin_default_i = $(".skin_default"), skin_brown_i = $(".skin_brown"), skin_blue_i = $(".skin_blue");
		if (cssType == "default")
		{
			skin_default_i.addClass("active");
			skin_blue_i.removeClass("active");
			skin_brown_i.removeClass("active");
		} else if (cssType == "brown")
		{
			skin_brown_i.addClass("active");
			skin_default_i.removeClass("active");
			skin_blue_i.removeClass("active");
		} else if (cssType == "blue")
		{
			skin_blue_i.addClass("active");
			skin_default_i.removeClass("active");
			skin_brown_i.removeClass("active");
		}
		$("#topskin").attr("href", Helper.ctxHYUI + '/themes/' + cssType + '/style.css?v=' + Helper.v);
		$.cookie('ygj_skin', $(this).siblings("input").val(), {
			expires : 365
		});
	})
	/* 点击关闭单个标签 */
	$("#min_title_list").on("click", "li em", function()
	{
		removeIframe($(this).parent());
	});
	/* 给li双击事件，关闭当前标签 */
	$(document).on("dblclick", "#min_title_list li", function()
	{
		removeIframe($(this));
	});
	/* 单击选项卡单个tab（切换tab） */
	$(document).on("click", "#min_title_list li", function()
	{
		var bStopIndex = $(this).index();
		var iframe_box = $("#iframe_box");
		$("#min_title_list li").removeClass("active").eq(bStopIndex).addClass("active");
		var _iframe = iframe_box.find(".show_iframe").hide().eq(bStopIndex).show().find("iframe");
		if (_iframe.attr("every-refresh") == "true")
		{// 时时刷新
			_iframe.attr("src", _iframe.attr("src"));
		} else
		{
			_iframe.show();
		}
	});
	// 鼠标移动到选项卡事件（添加右键事件）
	$(document).on("mouseenter", "#min_title_list li", function()
	{
		createMouseDownMenuData($(this))
	});
	// 检查是否可以直接跳转到印刷家
	$(document).on("click", "#forward", function()
	{
		// 同意协议
		Helper.request({
			url : Helper.basePath + "/sys/user/getUser",
			error : function(request)
			{
				Helper.message.warn("服务器繁忙");
			},
			success : function(data)
			{
				if (data.success)
				{
					if (data.obj.mobile == "" || data.obj.mobile == null)
					{
						Helper.message.view("账户未填写手机号，请联系公司管理员");
						return;
					} else
					{
						if($("#userIsSign").val() == 'NO' || $("#userIsSign").val() == '')
						{
							Helper.popup.show('用户协议', Helper.basePath + '/exterior/viewAgreement', '590', '520');
						} else
						{
							var newWin = window.open('', '我要采购');
							newWin.location.href = Helper.basePath + "/exterior/load";
						}
					}
				}
			}
		});
	})
	// 点击左侧菜单标签触发函数;
	$(".menu-list a").on("click", function(e)
	{
		admin_tab(this);
		tabNavallwidth();
	});
	queryExpire();
	getVersionInfo();
	// 检查是否可以继续购买
	$("#buy").on("click", function()
	{
		$.ajax({
			type : "POST",
			async : false,
			url : Helper.basePath + "/sys/buy/isPay",
			dataType : "json",
			contentType : 'application/json;charset=utf-8', // 设置请求头信息
			success : function(data)
			{
				if (data.success)
				{
					if (data.obj)
					{
						window.open(Helper.basePath + "/pay/step1/choose");
					} else
					{
						Helper.message.warn("请先支付或取消订单，在进行操作!");
						return;
					}
				} else
				{
					// 如果登陆超时,只要重新调用location,则会自动跳到登陆页面
					window.location = "/";
				}
			}
		});
	});
})

// 跳转到印刷家
function forwardYSJ(callback)
{
	Helper.request({
		url : Helper.basePath + "/exterior/forwardYSJ",
		error : function(request)
		{
			Helper.message.warn("服务器繁忙");
		},
		success : function(data)
		{
			// 响应
			if (data.success)
			{
				callback.call(this);
			} else
			{
				// 用户未绑定印刷家
				if (data.message == "")
				{
					Helper.popup.show('用户协议', Helper.basePath + '/exterior/viewAgreement', '590', '520');
				} else
				{// 系统错误
					Helper.message.warn(data.message);
				}
			}
		}
	})
}

function queryExpire()
{
	$.ajax({
		type : "POST",
		url : Helper.basePath + "/queryExpire",
		dataType : "json",
		contentType : 'application/json;charset=utf-8', // 设置请求头信息
		success : function(data)
		{
			if (data.message.split("_")[0] == 0)
			{
			} else if (data.message.split("_")[0] == 1)
			{
				Helper.message.view("尊敬的印管家客户，您的印管家还有" + data.message.split("_")[1] + "天就到期了，为了避免产品过期影响您的使用，请尽快联系深圳华印进行续费，联系电话：400-800-8755，QQ2880157226");
			} else if (data.message.split("_")[0] == 2)
			{
				Helper.message.view("尊敬的印管家客户，您试用的印管家还有" + data.message.split("_")[1] + "天就到期了，如果您需要购买正式版，请联系深圳华印，联系电话：400-800-8755");
			} else if (data.message.split("_")[0] == 3)
			{
				Helper.message.view("尊敬的印管家客户，您的印管家已过期，请联系深圳华印，联系电话：400-800-8755");
			} else if (data.message.split("_")[0] == 4)
			{
				Helper.message.view("尊敬的印管家客户，您的印管家试用已过期，如果您需要购买正式版，请联系深圳华印，联系电话：400-800-8755");
			}
		},
		error : function(data)
		{
			// console.log(data);
		}
	});
}
// 查询版本更新通知
function getVersionInfo()
{
	$.ajax({
		type : "POST",
		url : Helper.basePath + "/getVersionInfo",
		dataType : "json",
		contentType : 'application/json;charset=utf-8', // 设置请求头信息
		success : function(data)
		{
			if (data.obj)
			{
				layer.open({
					title : '升级公告',
					content : data.obj.content
				});
			}
		},
		error : function(data)
		{
			// console.log(data);
		}
	});
}

/**
 * 同意印刷家协议
 */
function ysjAgreement()
{
	$("#userIsSign").val("YES");
}
/*
 * =============标签栏js=============
 */
/* 定义全局变量num和hide_nav */
var num = 0, oUl = $("#min_title_list"), hide_nav = $("#tabNav");

/* 定义变量获取对象 */
function min_titleList()
{
	var topWindow = $(window.parent.document);
	var show_nav = topWindow.find("#min_title_list");
	var aLi = show_nav.find("li");
}

/* 获得左侧菜单栏标签，在右边tab栏判断添加tab */
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

/* 给标签栏添加新的标签 */
function creatIframe(href, titleName, refresh)
{
	var topWindow = $(window.parent.document);
	var show_nav = topWindow.find('#min_title_list');
	show_nav.find('li').removeClass("active");
	var iframe_box = topWindow.find('#iframe_box');
	/* 为标签栏创建新li标签 */
	show_nav.append('<li class="active left-border"  onselectstart="return false" ><span data-href="' + href + '" title="' + titleName + '">' + titleName + '</span><i></i><em class="icon icon-cross"></em></li>');
	var taballwidth = 0, $tabNav = topWindow.find(".acrossTab"), $tabNavWp = topWindow.find(".tabNav-wp"), $tabNavitem = topWindow.find(".acrossTab li");
	/* 判断如果不存在标签，则返回 */
	if (!$tabNav[0])
	{
		return
	}
	/* 遍历整个标签栏中的标签，计算标签总长度 */
	$tabNavitem.each(function(index, element)
	{
		taballwidth += Number(parseFloat($(this).width() + 32));
	});
	$tabNav.width(taballwidth + 25);
	/* iframe */
	var iframeBox = iframe_box.find('.show_iframe');
	iframeBox.hide();
	/* 创建并添加新的iframe */
	iframe_box.append('<div class="show_iframe"><div class="loading"></div><iframe every-refresh="' + refresh + '" frameborder="0" src=' + href + '></iframe></div>');
	var showBox = iframe_box.find('.show_iframe:visible');// 找出iframe可见的父级div
	showBox.find('iframe').load(function()
	{
		showBox.find('.loading').hide();// 找出新添加的iframe，隐藏载入动画;
	});
	tabvisible();
}

/* 标签被遮挡处理,使新添标签左移，让用户可见 */
function tabvisible()
{
	var taballwidth = 0, count = 0, $tabNav = hide_nav.find(".acrossTab"), $tabNavitem = hide_nav.find(".acrossTab li"), $tabNavitem_active = hide_nav.find(".acrossTab li.active"), $tabNavWp = hide_nav.find(".tabNav-wp");
	$tabNavitem.each(function(index, element)
	{
		taballwidth += Number(parseFloat($(this).width() + 32))
	});
	/* 获取整个标签栏div的长度 */
	var w = $tabNavWp.width();
	/* 当标签总长度大于标签栏的总长度时，全部标签左移 */
	if (taballwidth > (w - 45))
	{
		var tabnum = Number((taballwidth - w + 70 + $tabNavitem_active.width()) * (-1));
		$tabNav.css('left', tabnum + 'px');
	}
}

/* 关闭最右tab显示上一个Iframe和tab,即当关闭最右边的li时,显示上一个li,iframe也显示对应的 */
function removeIframe(obj)
{
	var aCloseIndex = obj.index();
	var iframe_box = $("#iframe_box");
	if (aCloseIndex > 0)
	{
		obj.remove();
		$('#iframe_box').find('.show_iframe').eq(aCloseIndex).remove();
		num == 0 ? num = 0 : num--;
		if ($("#min_title_list li.active").size() == 0)
		{
			$("#min_title_list li").removeClass("active").eq(aCloseIndex - 1).addClass("active");
			iframe_box.find(".show_iframe").hide().eq(aCloseIndex - 1).show();
		}
		/* 标签栏长度发生改变时触发此函数 */
		tabNavallwidth();
	} else
	{
		return false;
	}

	/*
	 * var topWindow = $(window.parent.document); var aCloseIndex =
	 * $(obj).parent().index(); var tab = topWindow.find(".acrossTab li"); var
	 * iframe = topWindow.find('#iframe_box .show_iframe');
	 * tab.eq(aCloseIndex).remove(); iframe.eq(aCloseIndex).remove(); num == 0?num =
	 * 0:num--; if($("#min_title_list li.active").size()==0)
	 * {//如果没有已激活的则显示上一个iframe标签 tab.removeClass('active');
	 * tab.eq(i-1).addClass("active"); iframe.hide(); iframe.eq(i-1).show(); }
	 */
}

/* 获取顶部选项卡总长度,判断触发点击事件(左右翻标签) */
function tabNavallwidth()
{
	var taballwidth = 0, $tabNav = hide_nav.find(".acrossTab"), $tabNavWp = hide_nav.find(".tabNav-wp"), $tabNavitem = hide_nav.find(".acrossTab li"), $tabNavmore = hide_nav.find(".tab-swith-btn");
	if (!$tabNav[0])
	{
		return
	}
	$tabNavitem.each(function(index, element)
	{
		taballwidth += Number(parseFloat($(this).width() + 32))
	});
	var w = $tabNavWp.width();
	if (taballwidth > (w - 90))
	{
		/* 点击下翻按钮 */
		$('.next-tab').click(function()
		{
			num == oUl.find('li').length - 1 ? num = oUl.find('li').length - 1 : num++;//
			toNavPos();
		});
		/* 点击上翻按钮 */
		$('.prev-tab').click(function()
		{
			num == 0 ? num = 0 : num--;
			toNavPos();
		});
	} else
	{
		if ($tabNav.offset().left == 220)
		{
			$('.tab-swith-btn').off('click');
		}
	}
}

/* 停止所有动画标签动画并开始当前动画 */
function toNavPos()
{
	oUl.stop().animate({
		'left' : -num * 100
	}, 100);
}

/**
 * 页签右建事件
 */
function createMouseDownMenuData(obj)
{
	obj.unbind("mousedown");
	obj.one("mousedown", (function(e)
	{
		if (e.which == 3)
		{// 右键事件
			var _THIS = $(this);
			var SELF = "self";
			var OTHER = "other";
			var LEFT = "left";
			var RIGHT = "right";
			var ALL = "all";
			var opertionn = {
				name : "",
				offsetX : 2,
				offsetY : 2,
				textLimit : 10,
				beforeShow : $.noop,
				afterShow : $.noop
			};
			var refreshSelf = function()
			{
				var liCurrIndex = $("#min_title_list li").index(_THIS);// 当前页签的索引（从0开始）
				var _iframe = $('#iframe_box').find('.show_iframe').eq(liCurrIndex).find("iframe");
				_iframe[0].contentWindow.location.reload();
				// _iframe.attr('src', _iframe.attr('src'));
			}
			var closeByType = function(type)
			{
				var liCurrIndex = $("#min_title_list li").index(_THIS);// 当前页签的索引（从0开始）
				var liLength = $("#min_title_list li").length;
				switch (type)
				{
				case SELF:
					_THIS.remove();
					$('#iframe_box').find('.show_iframe').eq(liCurrIndex).remove();
					num == 0 ? num = 0 : num--;
					break;
				case OTHER:
					for (var i = 0; i < liLength; i++)
					{
						if (i != 0)
						{
							if (i < liCurrIndex)
							{
								$("#min_title_list li").eq(1).remove();
								$('#iframe_box').find('.show_iframe').eq(1).remove();
							} else if (i > liCurrIndex)
							{
								$("#min_title_list li").eq(2).remove();
								$('#iframe_box').find('.show_iframe').eq(2).remove();
							}
						}
					}
					num = 1;
					break;
				case LEFT:
					for (var k = 0; k < liCurrIndex; k++)
					{
						if (k != 0)
						{
							$("#min_title_list li").eq(1).remove();
							$('#iframe_box').find('.show_iframe').eq(1).remove();
							num == 0 ? num = 0 : num--;
						}
					}
					break;
				case RIGHT:
					for (var x = liCurrIndex; x < liLength; x++)
					{
						$("#min_title_list li").eq(liCurrIndex + 1).remove();
						$('#iframe_box').find('.show_iframe').eq(liCurrIndex + 1).remove();
						num == 0 ? num = 0 : num--;
					}
					break;
				case ALL:
					for (var y = 0; y < liLength; y++)
					{
						if (y != 0)
						{
							$("#min_title_list li").eq(1).remove();
							$('#iframe_box').find('.show_iframe').eq(1).remove();
						}
					}
					num = 0;
					break;
				}

				if (!$("#min_title_list li").hasClass("active"))
				{
					$("#min_title_list li").removeClass("active").eq(-1).addClass("active");
					$("#iframe_box").find(".show_iframe").hide().eq(-1).show();
				}
				tabNavallwidth();
			}

			var liCurrIndex = $("#min_title_list li").index($(this));// 当前页签的索引（从0开始）
			var liLength = $("#min_title_list li").length;
			function createMenuData()
			{

				var refreshSelf_tab = {
					text : "刷新",
					func : function()
					{
						refreshSelf();
					}
				};
				var closeSelf_tab = {
					text : "关闭",
					func : function()
					{
						closeByType(SELF);
					}
				};
				var closeOther_tab = {
					text : "关闭其它",
					func : function()
					{
						closeByType(OTHER);
					}
				};
				var closeLeft_tab = {
					text : "关闭左侧",
					func : function()
					{
						closeByType(LEFT);
					}
				};
				var closeRight_tab = {
					text : "关闭右侧",
					func : function()
					{
						closeByType(RIGHT);
					}
				};
				var closeAll_tab = {
					text : "关闭全部",
					func : function()
					{
						closeByType(ALL);
					}
				};
				if (liCurrIndex == 0)
				{
					return [ [ refreshSelf_tab ] ];
				} else
				{
					if (liLength == 2)
					{// 只有自己
						return [ [ refreshSelf_tab ], [ closeSelf_tab ] ];
					} else if (liCurrIndex == 1)
					{// 自己是第一个节点
						return [ [ refreshSelf_tab ], [ closeSelf_tab, closeRight_tab ], [ closeAll_tab ] ];
					} else if (liCurrIndex > 1 && (liCurrIndex < liLength - 1))
					{// 自己是中间节点
						return [ [ refreshSelf_tab ], [ closeSelf_tab, closeOther_tab, closeLeft_tab, closeRight_tab ], [ closeAll_tab ] ];
					} else
					{// 自己是末节点
						return [ [ refreshSelf_tab ], [ closeSelf_tab, closeLeft_tab ], [ closeAll_tab ] ];
					}
					return null;
				}

			}
			;
			_THIS.smartMenu(createMenuData(), opertionn);

		}
	}));
}

// 关闭当前标签并显示对应标题的页签
function closeTabAndJump(active_title, url)
{
	// 查找当前业签
	var _li_index = $(window.parent.document).find("#min_title_list").find("li.active").index();
	if (url)
	{
		admin_tab($("<a _href='" + url + "' data-title='" + active_title + "' />"));
	} else
	{
		// 显示被激活的业签
		$(window.parent.document).find("#min_title_list").find("li span[title='" + active_title + "']").parent().click();
	}
	// 关闭当前业签
	$(window.parent.document).find("#min_title_list").find("li:eq(" + _li_index + ") em").click();
}
// 图片缩放方法
function imgShow(outerdiv, innerdiv, bigimg, _this)
{
	var src = _this.attr("src");// 获取当前点击的pimg元素中的src属性
	$(bigimg).attr("src", src);// 设置#bigimg元素的src属性

	/* 获取当前点击图片的真实大小，并显示弹出层及大图 */
	$("<img/>").attr("src", src).load(function()
	{
		var windowW = $(window).width();// 获取当前窗口宽度
		var windowH = $(window).height();// 获取当前窗口高度
		var realWidth = this.width;// 获取图片真实宽度
		var realHeight = this.height;// 获取图片真实高度
		var imgWidth, imgHeight;
		var scale = 0.8;// 缩放尺寸，当图片真实宽度和高度大于窗口宽度和高度时进行缩放

		if (realHeight > windowH * scale)
		{// 判断图片高度
			imgHeight = windowH * scale;// 如大于窗口高度，图片高度进行缩放
			imgWidth = imgHeight / realHeight * realWidth;// 等比例缩放宽度
			if (imgWidth > windowW * scale)
			{// 如宽度扔大于窗口宽度
				imgWidth = windowW * scale;// 再对宽度进行缩放
			}
		} else if (realWidth > windowW * scale)
		{// 如图片高度合适，判断图片宽度
			imgWidth = windowW * scale;// 如大于窗口宽度，图片宽度进行缩放
			imgHeight = imgWidth / realWidth * realHeight;// 等比例缩放高度
		} else
		{// 如果图片真实高度和宽度都符合要求，高宽不变
			imgWidth = realWidth;
			imgHeight = realHeight;
		}
		$(bigimg).css("width", imgWidth);// 以最终的宽度对图片缩放

		var w = (windowW - imgWidth) / 2;// 计算图片与窗口左边距
		var h = (windowH - imgHeight) / 2;// 计算图片与窗口上边距
		$(innerdiv).css({
			"top" : h,
			"left" : w
		});// 设置#innerdiv的top和left属性
		$(outerdiv).fadeIn("fast");// 淡入显示#outerdiv及.pimg
	});

	$(outerdiv).click(function()
	{// 再次点击淡出消失弹出层
		$(this).fadeOut("fast");
	});
}
// 在单据模块中点击单据编号，可以直接打开单据编号对应的单据模块的查看界面
function jumpTo(url, title)
{
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}

/**
 * 根据单号转换url
 * @since 1.0, 2017年12月29日 上午9:08:20, zhengby
 */
function billNoTransToUrl(billNo)
{
	if (billNo == null || billNo == "")
	{
		return;
	}
	var url = "";
	var title = "";
	// 生产工单
	if (/^MO/g.test(billNo))
	{
		url = Helper.basePath + '/produce/work/toView/' + billNo;
		title = "生产工单";
	}
	// 销售订单
	else if (/^SO/g.test(billNo))
	{
		url = Helper.basePath + '/sale/order/view/' + billNo;
		title = "销售订单";
	}
	// 采购订单
	else if (/^PO/g.test(billNo))
	{
		url = Helper.basePath + '/purch/order/view/' + billNo;
		title = "采购订单";
	}
	// 采购退货单
	else if (/^PR/g.test(billNo))
	{
		url = Helper.basePath + '/purch/refund/view/' + billNo;
		title = "采购退货单";
	}
	// 采购入库单
	else if (/^PN/g.test(billNo))
	{
		url = Helper.basePath + '/purch/stock/view/' + billNo;
		title = "采购入库单";
	}
	// 发外加工单
	else if (/^OP/g.test(billNo))
	{
		url = Helper.basePath + '/outsource/process/toView/' + billNo;
		title = "发外加工单";
	}
	// 报价单
	else if (/^QU/g.test(billNo))
	{
		url = Helper.basePath + '/offer/view/no/' + billNo;
		title = "报价单";
	}
	return '<a class="jump-to" title="点击链接" onclick="jumpTo(&quot;' + url + '&quot;,&quot;' + title + '&quot;)">' + billNo + '</a>';
}

/**
 * 根据源单单号+id转换url
 * @since 1.0, 2017年12月29日 上午10:22:13, zhengby
 */
function idTransToUrl(id, billNo)
{
	if (id == null || id == "")
	{
		return;
	}
	var url = "";
	var title = "";
	// 销售退货单
	if (/^IR/g.test(billNo))
	{
		url = Helper.basePath + '/sale/return/view/' + id;
		title = "销售退货单";
	}
	// 销售送货单
	else if (/^IV/g.test(billNo))
	{
		url = Helper.basePath + '/sale/deliver/view/' + id;
		title = "销售送货单";
	}
	// 发外到货单
	else if (/^OA/g.test(billNo))
	{
		url = Helper.basePath + '/outsource/arrive/view/' + id;
		title = "发外到货单";
	}
	// 发外退货单
	else if (/^OR[0-9]/g.test(billNo))
	{
		url = Helper.basePath + '/outsource/return/view/' + id;
		title = "发外退货单";
	}
	// 发外对账单
	else if (/^OC/g.test(billNo))
	{
		url = Helper.basePath + '/outsource/reconcil/view/' + id;
		title = "发外对账";
	}
	// 采购对账单
	else if (/^PK/g.test(billNo))
	{
		url = Helper.basePath + '/purch/reconcil/view/' + id;
		title = "采购对账";
	}
	// 销售对账单
	else if (/^SK/g.test(billNo))
	{
		url = Helper.basePath + '/sale/reconcil/view/' + id;
		title = "销售对账";
	}
	// 报价单
	else if (/^QU/g.test(billNo))
	{
		url = Helper.basePath + '/offer/view/' + id;
		title = "报价单";
	}
	// 产量上报
	else if (/^DY/g.test(billNo))
	{
		url = Helper.basePath + '/produce/report/view/' + id;
		title = "产量上报";
	}
	// 生产领料
	else if (/^MR/g.test(billNo))
	{
		url = Helper.basePath + '/stockmaterial/take/view/' + id;
		title = "生产领料";
	}
	// 生产补料
	else if (/^SM[0-9]/g.test(billNo))
	{
		url = Helper.basePath + '/stockmaterial/supplement/view/' + id;
		title = "生产补料";
	}
	// 生产退料
	else if (/^RM/g.test(billNo))
	{
		url = Helper.basePath + '/stockmaterial/return/view/' + id;
		title = "生产退料";
	}
	// 材料其他入库
	else if (/^SMI/g.test(billNo))
	{
		url = Helper.basePath + '/stockmaterial/otherin/view/' + id;
		title = "材料其他入库";
	}
	// 材料其他出库
	else if (/^SMO/g.test(billNo))
	{
		url = Helper.basePath + '/stockmaterial/otherout/view/' + id;
		title = "材料其他出库";
	}
	// 材料库存调整
	else if (/^MA/g.test(billNo))
	{
		url = Helper.basePath + '/stockmaterial/adjust/view/' + id;
		title = "材料库存调整";
	}
	// 材料库存调拨
	else if (/^MT/g.test(billNo))
	{
		url = Helper.basePath + '/stockmaterial/transfer/view/' + id;
		title = "材料库存调拨";
	}
	// 材料库存盘点
	else if (/^MI/g.test(billNo))
	{
		url = Helper.basePath + '/stockmaterial/inventory/view/' + id;
		title = "材料库存盘点";
	}
	// 材料分切
	else if (/^CT/g.test(billNo))
	{
		url = Helper.basePath + '/stockmaterial/split/view/' + id;
		title = "材料分切";
	}
	// 成品入库
	else if (/^IS/g.test(billNo))
	{
		url = Helper.basePath + '/stockproduct/in/view/' + id;
		title = "成品入库";
	}
	// 成品其它入库
	else if (/^SPI/g.test(billNo))
	{
		url = Helper.basePath + '/stockproduct/otherin/view/' + id;
		title = "成品其它入库";
	}
	// 成品其它出库
	else if (/^SPO/g.test(billNo))
	{
		url = Helper.basePath + '/stockproduct/otherout/view/' + id;
		title = "成品其它出库";
	}
	// 成品库存调整
	else if (/^PA/g.test(billNo))
	{
		url = Helper.basePath + '/stockproduct/adjust/view/' + id;
		title = "成品库存调整";
	}
	// 成品库存调拨
	else if (/^PT/g.test(billNo))
	{
		url = Helper.basePath + '/stockproduct/transfer/view/' + id;
		title = "成品库存调拨";
	}
	// 成品库存盘点
	else if (/^PI/g.test(billNo))
	{
		url = Helper.basePath + '/stockproduct/inventory/view/' + id;
		title = "成品库存盘点";
	}
	// 付款单
	else if (/^RV/g.test(billNo))
	{
		url = Helper.basePath + '/finance/payment/view/' + id;
		title = "付款单";
	}
	// 收款单
	else if (/^RC/g.test(billNo))
	{
		url = Helper.basePath + '/finance/receive/view/' + id;
		title = "收款单";
	}
	// 付款核销单
	else if (/^WRV/g.test(billNo))
	{
		url = Helper.basePath + '/finance/writeoffPayment/view/' + id;
		title = "付款核销单";
	}
	// 收款核销单
	else if (/^WRC/g.test(billNo))
	{
		url = Helper.basePath + '/finance/writeoffReceive/view/' + id;
		title = "收款核销单";
	}
	// 其它付款单
	else if (/^OV/g.test(billNo))
	{
		url = Helper.basePath + '/finance/otherPayment/view/' + id;
		title = "其它付款单";
	}
	// 其它收款单
	else if (/^ORC/g.test(billNo))
	{
		url = Helper.basePath + '/finance/otherReceive/view/' + id;
		title = "其它收款单";
	}
	// 材料期初单
	else if (/^BM/g.test(billNo))
	{
		url = Helper.basePath + '/begin/material/view/' + id;
		title = "材料期初单";
	}
	// 产品期初单
	else if (/^BP/g.test(billNo))
	{
		url = Helper.basePath + '/begin/product/view/' + id;
		title = "产品期初单";
	}
	// 代工单
	else if (/^EO/g.test(billNo))
	{
		url = Helper.basePath + '/oem/order/view/' + id;
		title = "代工单";
	}
	// 代工送货单
	else if (/^ED/g.test(billNo))
	{
		url = Helper.basePath + '/oem/deliver/view/' + id;
		title = "代工送货";
	}
	// 代工退货单
	else if (/^ER/g.test(billNo))
	{
		url = Helper.basePath + '/oem/return/view/' + id;
		title = "代工退货";
	}
	// 代工对账单
	else if (/^EC/g.test(billNo))
	{
		url = Helper.basePath + '/oem/reconcil/view/' + id;
		title = "代工对账";
	}
	// 客户期初
	else if (/^BC/g.test(billNo))
	{
		url = Helper.basePath + '/begin/customer/view/' + id;
		title = "客户期初";
	} 
	// 供应商期初
	else if (/^BS/g.test(billNo))
	{
		url = Helper.basePath + '/begin/supplier/view/' + id;
		title = "供应商期初";
	}
	// 财务调整单
	else if (/^FA/g.test(billNo))
	{
		url = Helper.basePath + '/finance/adjust/view/' + id;
		title = "财务调整单";
	} else
	{
		return billNo;
	}

	return '<a class="jump-to" title="点击链接" onclick="jumpTo(&quot;' + url + '&quot;,&quot;' + title + '&quot;)">' + billNo + '</a>';
}

/**
 * 表格列拖动的公共方法，依赖jQuery插件与colResizable插件
 * @since 1.0, 2018年10月16日 上午9:54:22, zhengxchn@163.com
 */
var table_ColDrag = function(jQuery_$, params){
	if (jQuery_$ == undefined || jQuery_$ == null)
	{
		return false;
	}
	if (params == undefined|| params == null)
	{
		params = {};
	}
	jQuery_$.colResizable({disable: true});		// 先废除
	jQuery_$.colResizable({
		minWidth: params.minWidth,				// 一个单元格最小宽度，默认15
		resizeMode: params.resizeMode,		// 默认'fit'
		onResize: params.resize						// 拖动后触发的方法
	})
}

/**
 * bootstrap_table表格列拖动方法
 * @since 1.0, 2018年10月16日 上午9:54:22, zhengxchn@163.com
 */
var bootstrapTable_ColDrag = function(jQuery_$, params){
	if (jQuery_$ == undefined || jQuery_$ == null)
	{
		return false;
	}
	if (params == undefined|| params == null)
	{
		params = {};
	}
	params.resize = function(){
		jQuery_$.bootstrapTable('resetView');					// 每次拖动后重置bootstrapTable表格，用于重置表格的高度和宽度
	}
	table_ColDrag(jQuery_$, params);
}

/**
 * 为某一个标签下的所有未能列拖动的table标签添加列拖动效果，
 * 注意:
 *     该方法是筛选class不为"JColResizer"的Table标签
 * @since 1.0, 2018年10月18日 下午16:34:07, zhengxchn@163.com
 */
var allTable_ColDrag = function(jQuery_$, params){
	if (jQuery_$ == undefined || jQuery_$ == null)
	{
		return false;
	}
	if (params == undefined|| params == null)
	{
		params = {};
	}
	
	var tagName = jQuery_$.get(0).tagName;						// 检查当前的标签是否是Table标签
	if (tagName == "TABLE")
	{
		var coldrag = jQuery_$.attr("coldrag");
		if (coldrag != "false" && !jQuery_$.is(":hidden") && !jQuery_$.hasClass("JColResizer"))
		{
			// table没有coldrag属性，不是隐藏标签， class不包含JColResizer
			table_ColDrag(jQuery_$, params);
		}
	}
	
	var tables = jQuery_$.find("table");						// 查询该标签下的所有table
	$.each(tables, function(index, item)
	{
		var coldrag = $(item).attr("coldrag");
		if (coldrag == "false" || $(item).is(":hidden") || $(item).hasClass("JColResizer"))
		{
			// table有coldrag属性或是隐藏标签或包含JColResizer
			return true;
		}
		table_ColDrag($(item), params);
	});
}

/**
 * 默认开启表格列拖动方法
 * 适用于普通table和id为"bootTable"的bootstrap_table表格）
 * @since 1.0, 2018年10月16日 下午14:37:22, zhengxchn@163.com
 * */
$(function()
{
	var tables = $("body").find("table");			// 寻找页面上的table
	$.each(tables, function(index, item)
	{
		if ($(item).is(":hidden"))							// 隐藏table，跳过
		{
			return true;
		}
		
		var coldrag = $(item).attr("coldrag");	// coldrag用于关闭列拖动，在table标签加上coldrag="false"
		if (coldrag == "false")
		{
			return true;
		}
		
		var id = $(item).attr("id");						// 获取table的id属性
		if (id == "bootTable")									// table是bootstrap_table
		{
			$("#" + id).on('load-success.bs.table', function()			// bootstrap_table加载完后触发
			{
				bootstrapTable_ColDrag($("#" + id));
			})
			return true;
		}
		
		table_ColDrag($(item));																		// 普通table
	});
})
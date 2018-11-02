/**
 * Author:       THINK
 * Create:       2017年11月3日 上午9:38:09
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 */
$(function()
{	
	var BOXTYPE="KDDGGH";
	init();

	/**
	 * 初始化
	 * @since 1.0, 2017年11月3日 上午11:06:39, zhengby
	 */
	function init()
	{
		initBtn();
		initOtherBtn();
		initCarton();
	}

	/**
	 * 初始化初始化功能、按钮事件
	 * @since 1.0, 2017年11月6日 上午09:58:00, think
	 */
	function initBtn()
	{

	}

	/**
	 * 初始化其他不属于公共功能的按钮
	 * @since 1.0, 2017年11月3日 下午4:00:00, zhengby
	 */
	function initOtherBtn()
	{
		$("input[name='offerPartList.containBflute']").click(function()
		{
			if ($(this).prop("checked") == false)
			{
				$(this).parents("tr").next().hide();
			} else
			{
				$(this).parents("tr").next().show();
			}
		})
	}

	/**
	 * 初始化初始化功能、按钮事件
	 * @since 1.0, 2017年11月6日 上午09:58:00, think
	 */
	function initCarton()
	{
		// 默认设置为扣底带挂勾盒
		$("#boxType").val("扣底带挂勾盒");
		// 彩盒类型点击选中并改变为红色
		$(".carton_type > .cb_img").on("click", function()
		{
			var $this = $(this);
			var type = $this.data("type");
			var title = $this.attr("title");
			BOXTYPE = type; //全局变量
			// 设置盒子类型
			$("#boxType").val(title);

			// 边框全部设置为灰色
			$(".carton_type > .cb_img").css("border", "1px solid #aaa");
			// 边框当前选中的设置为红色
			$this.css("border", "2px solid red");
			// 彩盒计算
			changedCartonType(type);
		});
		
		// 计算
		$("select[name='offerPartList.bflutePaperQuality']").on("change",function(){
			var $this = $(this);
			var price = $this.find("option:selected").data("price"); // 获取自定义属性值
			$this.parents("td").find("input[name='offerPartList.bflutePrice']").val(price);
		})
		// 当填写了长宽高之后自动调用计算服务
		$("#slength,#swidth,#sheight").on("blur",function(){
			changedCartonType(BOXTYPE);
		})
	}

	/**
	 * 彩盒类型公式计算
	 * @since 1.0, 2017年11月6日 上午09:58:00, think
	 */
	function changedCartonType(type)
	{
		// 点击其他盒形后，输入成品尺寸，展长，展宽无法自动计算，必须手动输入，只能输入整形数字
		if ("QTHX" == type)
		{
			// 批量删除多部件
			delMulityBox();
		}
		// 点击多部件盒形：印刷颜色，印刷纸张，后道工序内容全部复制增加新的一行
		else if ("DBJHX" == type)
		{
			copyNewTr();
		}
		// 调用服务计算
		else
		{
			// 批量删除多部件
			delMulityBox();
			calCartonType(type);
		}
	}

	/**
	 * 点击多部件盒型功能
	 * @since 1.0, 2017年11月7日 下午6:11:05, zhengby
	 */
	function copyNewTr()
	{
		// 复制印刷颜色
		var copyTr = $("#quote_form").find("tr").eq(4).clone(true);
		copyTr.find("a").removeAttr("style");
		$(".productProcedureTd").parents("tr").before(copyTr);
		// 复制印刷纸张
		var copyTr2 = $("#quote_form").find("tr").eq(5).clone(true);
//		copyTr2.find("#containBflute").parent().remove();
		copyTr2.find("select").eq(2).parent().remove();
		copyTr2.find(".machine_sel").html("<option value='0'>请选择</option>");
		$(".productProcedureTd").parents("tr").before(copyTr2);
		// 复制坑纸数
		var copyTr3 = $("#quote_form").find("tr").eq(6).clone(true);
		copyTr3.find(".machine_sel").html("<option value='0'>请选择</option>");
		copyTr3.find("input[name='offerPartList.bflutePrice']").val("0");
		$(".productProcedureTd").parents("tr").before(copyTr3);
		// 复制后道工序
		var copyTr4 = $("#quote_form").find("tr").eq(7).clone(true);
		$(".productProcedureTd").parents("tr").before(copyTr4);
	}
	
	/**
	 * 彩盒类型公式计算
	 * @since 1.0, 2017年11月6日 上午09:58:00, think
	 */
	function calCartonType(type)
	{
		var $slength = $("#slength"), $slengthVal = $slength.val(); // 长
		var $swidth = $("#swidth"), $swidthVal = $swidth.val(); // 宽
		var $sheight = $("#sheight"), $sheightVal = $sheight.val(); // 高
		var $length = $("#length"); // 展长
		var $width = $("#width"); // 展宽

		$.ajax({
			type : "POST",
			url : Helper.basePath + "/offer/auto/countBoxFormula",
			data : {
				"boxFormula" : type,
				"length" : $slengthVal,
				"width" : $swidthVal,
				"high" : $sheightVal,
			},
			dataType : "json",
			async : true,// 默认异步请求
			success : function(data)
			{
				$length.val(Number(data.obj.length).toFixed(0));
				$width.val(Number(data.obj.width).toFixed(0));
			},
			error : function(data)
			{
				Helper.message.warn("请求错误")
			},
			beforeSend : function()
			{
				layer.load(1);
			},
			complete : function()
			{
				layer.closeAll('loading');
			}
		});
	}

});

/**
 * 删除多部件行
 * @since 1.0, 2017年11月7日 下午7:39:29, zhengby
 */
function delCopyTr(obj)
{
	var $this = $(obj);
	for (var i = 0; i < 3; i++)
	{
		$this.parents("tr").next().remove();
	}
	$this.parents("tr").remove();
}

/**
 * 批量删除多部件行
 * @since 1.0, 2017年12月20日 下午4:29:24, zhengby
 */
function delMulityBox()
{
	$(".delCopyTr:visible").each(function(index,items){
		delCopyTr(items);
	})
}

/**
 * 选择坑形后联动纸质下拉框
 * @since 1.0, 2017年11月9日 上午11:30:08, zhengby
 */
function selectBfluteType(obj)
{
	var $this = $(obj);
	var pit = $this.val();
	
	if (pit != null && pit != "")
	{
		$.ajax({
			type : "POST",
			url : Helper.basePath + "/offer/auto/getOfferBfluteList",
			data : {
				"pit" : pit
			},
			dataType : "json",
			async : false,// 同步请求
			success : function(data)
			{
				var str = "";
				if (data.length != 0)
				{
					$.each(data, function(idx, val)
					{
						str += "<option value='" + val.paperQuality + "' data-price=" + val.price + ">"+ val.paperQuality + "</option>";
					});
					$this.parent().next().find(".machine_sel").children().remove();// 删除"--请选择--"选项
					$this.parent().next().find(".machine_sel").append(str).trigger("change");
				}
			},
			error : function(data)
			{
				Helper.message.warn("请求错误");
			}
		});
	} else
	{
		// 当选择了“请选择”选项时，初始化第二个下拉框
		var str = "<option value='0'>请选择</option>";
		$this.parent().next().find(".machine_sel").children().remove();
		$this.parent().next().find(".machine_sel").append(str);
	}
	
}

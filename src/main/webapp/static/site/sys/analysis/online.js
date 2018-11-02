$(function()
{
	$(document).on("mouseenter", ".online_user", function()
	{
		$("#select_user").html($(this).next().html());
	})
	flushTime();
	window.setInterval("flushPage();", 60000);
	window.setInterval("flushTime();", 1000);
});
var lastFlushTime = (new Date()).getTime();
function flushTime()
{
	var currTime = (new Date()).getTime();
	$("#currTime").html(new Date().format());
	$("#remain_time").html("" + Number((61000 - (currTime - lastFlushTime)) / 1000).trunc() + "秒后");
}
function flushPage()
{
	self.location.reload();

}
/* 公司信息-查看 */
function company_view(id)
{
	Helper.popup.show('公司信息', Helper.basePath + '/sys/company/view/' + id, '700', '280');
}

/* 用户信息-查看 */
function user_view(id)
{
	Helper.popup.show('用户信息', Helper.basePath + '/sys/user/view/' + id, '700', '300');
}

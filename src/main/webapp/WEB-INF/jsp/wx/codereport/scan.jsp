<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<link rel="stylesheet" href="${ctxStatic}/wx/css/scan.css">
</script>
<title>扫码</title>

</head>
<body style="height: 0;">
	<div id="msCode">
		<div class="content">
			<div class="list-block">
				<ul>
					<!-- Text inputs -->
					<li style="display: none">
						<div class="item-content">
							<div class="item-inner">
								<div class="item-title label">条码扫码：</div>
								<div class="item-input">
									<input type="text" id="ms_getCod" placeholder="支持工序条码" data-taskId="">
									<i class="iconfont userMore">&#xe60b;</i>
								</div>
							</div>
						</div>
					</li>

					<li>
						<div class="item-content">
							<div class="item-inner">
								<div class="item-title label custom_item_title">客户名称</div>
								<div class="item-input">
									<input type="text" class="ms_clientName" readonly="readonly">
								</div>
							</div>
						</div>
					</li>

					<li>
						<div class="item-content">
							<div class="item-inner">
								<div class="item-title label custom_item_title">产品名称</div>
								<div class="item-input">
									<input type="text" class="ms_productName" readonly="readonly">
								</div>
							</div>
						</div>
					</li>
					<li>
						<div class="item-content">

							<div class="item-inner">
								<div class="item-title label custom_item_title">产品规格</div>
								<div class="item-input">
									<input type="text" class="ms_productStandard" readonly="readonly">
								</div>
							</div>
						</div>
					</li>
					<li>
						<div class="item-content">

							<div class="item-inner">
								<div class="item-title label custom_item_title">部件名称</div>
								<div class="item-input">
									<input type="text" class="ms_productModule" readonly="readonly">
								</div>
							</div>
						</div>
					</li>
					<li>
						<div class="item-content">

							<div class="item-inner">
								<div class="item-title label custom_item_title">工序名称</div>
								<div class="item-input">
									<input type="text" class="ms_processName" readonly="readonly">
								</div>
							</div>
						</div>
					</li>
					<li>
						<div class="item-content">

							<div class="item-inner">
								<div class="item-title label custom_item_title">应产数量</div>
								<div class="item-input">
									<input type="text" class="ms_shouldNo" readonly="readonly">
								</div>
							</div>
						</div>
					</li>
					<li>
						<div class="item-content">
							<div class="item-inner">
								<div class="item-title label custom_item_title">员工信息</div>

								<div class="item-input staffIfo">
									<select class="em_userselect " id="employeeContent">

										<script id="employeeContenttmpl" type="text/x-dot-template">
  									{{ for(var k in it) { }}
										{{? k ==0 }}
				  						<option selected="selected">{{=it[k].name}}</option> 
										{{??}}
										<option >{{=it[k].name}}</option> 
									{{?}}
 									{{ } }}

									</script>
									</select>
									<span>
										<i class="iconfont">&#xe64c;</i>
									</span>
								</div>
							</div>
					</li>
					<li>
						<div class="item-content">

							<div class="item-inner">
								<div class="item-title label custom_item_title">上报数量</div>
								<div class="item-input">
									<input class="custom_input" type="number" id="reportedNum" />
								</div>
							</div>
						</div>
					</li>
					<li>
						<div class="item-content">
							<div class="item-inner">
								<div class="item-title label custom_item_title">合格数量</div>
								<div class="item-input">
									<input type="text" value="0" class="" id="qualifiedNum" readonly="readonly" />
								</div>
							</div>

						</div>
					</li>
					<li>
						<div class="item-content">

							<div class="item-inner">
								<div class="item-title label custom_item_title">不合格数</div>
								<div class="item-input">
									<input type="text" class="custom_input" placeholder="0" value="0" id="disqualificationNum" />
								</div>
							</div>
						</div>
					</li>

					<li>
						<div class="item-content">

							<div class="item-inner">
								<div class="item-title label custom_item_title">上报时间</div>
								<div class="item-input item_time">
									<input class="" type="text" data-toggle='date' id="reportedTime" />
									<span>
										<i class="iconfont">&#xe64c;</i>
									</span>
								</div>
							</div>
						</div>
					</li>
					<li>
						<div class="item-content">
							<div class="item-inner">
								<div class="item-title label custom_item_title ">生产开始时间</div>
								<div class="item-input item_time">
									<input type="text" id='producBigan' class="" />
									<span>
										<i class="iconfont">&#xe64c;</i>
									</span>
								</div>
							</div>
						</div>
					</li>
					<li>
						<div class="item-content">
							<div class="item-inner">
								<div class="item-title label custom_item_title">生产结束时间</div>
								<div class="item-input item_time">
									<input type="text" id='producEnd' class="" />
									<span>
										<i class="iconfont">&#xe64c;</i>
									</span>
								</div>
							</div>
						</div>
					</li>
				</ul>
			</div>
			<div class="content-block">
				<div class="row">
					<div class="col-50 ">
						<a href="#" class="button button-fill button-success ms_save" data-type="saveData">保存</a>
					</div>
					<div class="col-50 ms_saveAuditDiv">
						<a href="#" class="button button-fill button-success ms_saveAudit">保存并审核</a>
					</div>
					<div class="col-50">
						<a href="#" class="button button-fill button-danger ms_cancel">取消</a>
					</div>
				</div>
			</div>

		</div>
		<jsp:include page="/WEB-INF/jsp/wx/foot.jsp">
			<jsp:param value="codereport" name="module" />
		</jsp:include>
	</div>
	<script type='text/javascript' src='${ctxStatic}/wx/js/plugin/sui/sm.min.js' charset='utf-8'></script>
	<script type='text/javascript' src='${ctxStatic}/wx/js/plugin/sui/sm-extend.min.js' charset='utf-8'></script>
	<script src="https://res.wx.qq.com/open/js/jweixin-1.1.0.js"></script>
	<script type="text/javascript">
		$(function() {
			//设置只读input背景色
			$("input[readonly='readonly']").css('background-color', '#eee')
			// 扫一扫配置初始化
			wx.config({
				debug : false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。  
				appId : '${appid}', // 必填，公众号的唯一标识  
				timestamp : '${ timestamp}', // 必填，生成签名的时间戳  
				nonceStr : '${ nonceStr}', // 必填，生成签名的随机串  
				signature : '${ signature}',// 必填，签名，见附录1  
				jsApiList : [ 'checkJsApi', 'chooseImage', 'previewImage', 'uploadImage', 'downloadImage', 'getNetworkType',//网络状态接口
				'openLocation',//使用微信内置地图查看地理位置接口
				'getLocation', //获取地理位置接口
				'hideOptionMenu',//界面操作接口1
				'showOptionMenu',//界面操作接口2
				'closeWindow', ////界面操作接口3
				'hideMenuItems',////界面操作接口4
				'showMenuItems',////界面操作接口5
				'hideAllNonBaseMenuItem',////界面操作接口6
				'showAllNonBaseMenuItem',////界面操作接口7
				'scanQRCode'// 微信扫一扫接口
				]
			});
			function getQueryString(name) {
				var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
				var r = window.location.search.substr(1).match(reg);
				if (r != null) {
					return unescape(r[2]);
				}
				return null
			}
			var billno = getQueryString('billno')
			if (billno != null) {
				reportData(billno)
			} else {
				return $.alert('未查询到该工序，请确定条码无误再试')
			}
// 			$('#ms_getCod').blur(function(){//调试
				
// 					var aab = $('#ms_getCod').val()
// 					reportData(aab)
// 			})
			function reportData(billno) {
				$.showIndicator()
				HYWX.requestByObj({
							url : '${ctx}/wx/codereport/scan/search?ids=' + billno,
							success : function(data) {
								$.hideIndicator()
								if (data.obj.length == 0){
									return $.alert('单号有误，确认条码无误后再试',function(){
										scanAgin()
									})
								}
								data = data.obj[0];
								if (data.unreportQty == 0) {
									return $.alert('此工序已上报完工', function() {
										scanAgin()
									})
								}
								if(data.isShow=='NO'){
									return $.alert('此工单尚未审核',function(){
										scanAgin()
									})
								}
								if(data.isForceComplete=='YES'){
									return $.alert('此工单已强制完工',function(){
										scanAgin()
									})
								}
								//判断是否排成
								if (data.isSchedule == 'NO') {
									return $.alert('此工序不是排程工序，如需排程请在工序资料里面设置为排程工序', function(){
										scanAgin()
									})
								}
								//判断是否发外
								if (data.isOutSource == 'YES') {
									return $.alert('此工序为发外工序', function() {
										scanAgin()
									})
								}
								if (!data) {
									return $.alert('单号不正确', function() {
										scanAgin()
									})
								}
								taskId = data.id;
								$('#ms_getCod').attr("data-taskId", taskId)
								$('.ms_clientName').val(data.customerName);
								$('.ms_clientName').val(data.customerName);
								$('.ms_productName').val(data.productName);
								$('.ms_productStandard').val(data.specifications);//产品规格
								$('.ms_productModule').val(data.partName);
								$('.ms_processName').val(data.procedureName);
								$('.ms_shouldNo').val(data.yieldQty);
								//上报数
								if (data.yieldQty >= data.reportQty && data.reportQty * data.yieldQty >= 0) {
									if (data.productType == 'ROTARY')
									{
										$('#reportedNum').val((data.yieldQty - data.reportQty).toFixed(2));
									} else 
									{
										$('#reportedNum').val(data.yieldQty - data.reportQty);
									}
								
								}
								//合格数
								if (data.unreportQty - Number($('#disqualificationNum').val()) >= 0
										&& data.unreportQty * Number($('#disqualificationNum').val()) >= 0) {
									if (data.productType == 'ROTARY')
									{
										$('#qualifiedNum').val((data.unreportQty - Number($('#disqualificationNum').val())).toFixed(2));
									} else 
									{
										$('#qualifiedNum').val(data.unreportQty - Number($('#disqualificationNum').val()));
									}
								}
								getEmployee();
							}
						})
			}
			//时间组件初始化
			function getLocalTime() {
				date = new Date()
				var second = date.getSeconds() <= 9 ? second = ('0' + date.getSeconds()) : second = date.getSeconds();
				var minute = date.getMinutes() <= 9 ? minut = ('0' + date.getMinutes()) : minut = date.getMinutes();
				var hour = date.getHours() <= 9 ? hour = ('0' + date.getHours()) : hour = date.getHours();
				var year = date.getFullYear() <= 9 ? year = ('0' + date.getFullYear()) : year = date.getFullYear();
				var month = (date.getMonth() + 1) <= 9 ? month = ('0' + (date.getMonth() + 1)) : month = date.getMonth() + 1;
				var date = (date.getDate() + 1) <= 9 ? date = ('0' + date.getDate()) : date = date.getDate();
				var dateStr1 = year + '-' + month + '-' + date + ' ' + hour + ':' + minute + ':' + second
				var dateStr2 = year + '-' + month + '-' + date
				return dateStr1
			}
			function initTime() {
				$('#producBigan,#producEnd,#reportedTime').attr('value', getLocalTime())
				$("#producBigan,#producEnd,#reportedTime").datetimePicker({});

			}
			initTime()
			//监听不合格数变化，反算合格数
			$(document).on('blur', '#disqualificationNum', function() {
				if (($('#reportedNum').val()) - ($('#disqualificationNum').val()) >= 0) {
					$('#qualifiedNum').val(($('#reportedNum').val()) - ($('#disqualificationNum').val()))
				} else {
					$.alert('输入有误请确认无误后重新输入')
				}
			})
			//监听上报数变化，反算合格数
			$(document).on('blur', '#reportedNum', function() {
				if (($('#reportedNum').val()) - ($('#disqualificationNum').val()) >= 0) {
					$('#qualifiedNum').val(($('#reportedNum').val()) - ($('#disqualificationNum').val()))
				} else {
					$.alert('输入有误请确认无误后重新输入')
				}
			})

			var staffIfo
			// 	获取员工信息
			function getEmployee() {
				$.ajax({
					url : '${ctx}/wx/common/allEmployee',
					type : 'get',
					success : function(data) {
						var data = JSON.parse(data).result;
						staffIfo = data[0].name;
						var interText = doT.template($("#employeeContenttmpl").text());
						$("#employeeContent").html(interText(data));
					}
				})
			}
			;
			getEmployee()
			//取消
			$('.ms_cancel').click(function() {
				//window.location.href='${ctx}/wx/codereport/scan/index'
				$('#msCode input').val('')
				scanAgin()
				//href="javascript:history.go(-1)" 
				//window.location.href = document.referrer
			})
			function scanAgin(){
				wx.scanQRCode({
				      needResult: 1,
				      desc: 'scanQRCode desc',
				      success: function (res) {
				    	  var attrBillNo = (res.resultStr.split(','))[1];
				    	  if(/^MO/g.test(attrBillNo)==true){
								return $.alert('微信上报只支持工序上报',function(){
									 scanAgin();
								})
							}else{
								window.location.href='${ctx}/wx/codereport/scan/index?billno='+attrBillNo;
							}
				      }
				 	});
			}
			//监听员工选择
			$('#employeeContent').change(function() {
				staffIfo = $(this).val();
			})
			// 保存上报
			$(document).on('click', '.ms_save,.ms_saveAuditDiv', function() {
				if ($('#reportedNum').val() == '' || $('#qualifiedNum').val() == '') {
					return $.alert('必填项不能为空')
				}
				if ($('#reportedNum').val() * $('#qualifiedNum').val() < 0) {
					return $.alert('查后重新输入')
				}
				//加载loding开启
				$.showIndicator()
				var check_code;
				//审核状态
				if ($(this).attr('data-type') == 'saveData') {
					check_code = 'NO'
				} else {
					check_code = 'YES'
				}
				var taskId = $('#ms_getCod').attr('data-taskId')
				var reportedNum = $('#reportedNum').val();
				var qualifiedNum = $('#qualifiedNum').val();
				var disqualificationNum = $('#disqualificationNum').val();
				var reportedTime = $("#reportTime").val();

				if (!staffIfo) {
					staffIfo = $('#employeeContent').val()
				}
				HYWX.requestByObj({
					url : '${ctx}/wx/codereport/save',
					data : {
						'isCheck' : check_code,
						'employeeName' : staffIfo,
						"reportList" : [ {
							"taskId" : taskId,
							"reportQty" : reportedNum,
							"qualifiedQty" : qualifiedNum,
							'startTime' : $('#producBigan').val(),
							'endTime' : $('#producEnd').val()
						} ]
					},
					success : function(res) {

						if (res.success) {

							if (check_code == 'NO') {
								$.hideIndicator()
								$.alert('保存成功', function() {
									scanAgin()
								})
							} else {
								$.hideIndicator()
								$.alert('保存并审核成功', function() {
									scanAgin()
								})
							}
						} else {
							$.hideIndicator();
							$.alert('保存失败，请确定条码或单号无误后重新保存', function() {
								scanAgin()
							})
						}
					}
				})
			})
		})
	</script>
</body>

</html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<input type="hidden" name="paperType">
<input type="hidden" id="offerType" name="offerType" value="${offerType }" />
<input type="hidden" id="boxType" name="boxType" value="${offerType.text }">
<input type="hidden" class="constraint_decimal_negative input-txt input-txt_14 whiteBg" id="rows" name="ladderCol" value="5">
<input type="hidden" class="constraint_decimal_negative input-txt input-txt_14 whiteBg" id="spaceQty" name="ladderSpeed" value="1000">
<style>
	table.info-table thead th,tbody tr td{font-size: 0.65rem;}
</style>
<script id="selectMachine" type="text/x-dot-template">
<div class="item-kv">
	<table class="info-table">
		<thead>
			<tr style="background: grey;">
				<th style="width:4rem;">类型</th>
        <th style="width:4rem;">印刷机</th>
		    <th style="width:4rem;">上机尺寸</th>
			  <th style="width:4rem;">大纸张数</th>
				<th style="width:3rem;">最低价</th>
			</tr>
		</thead>
		<tbody>
			{{ for(var k in it){ }}
			<tr class = "{{if(it[k].isLowerPrice == true){ }}{{= "choosenTr" }}{{ }}}">
				<td style="display:none;">
					<label class="label-checkbox item-content label_ui3 label_10">
						<input type="checkbox" class="check_procedure" name="partDetail.isChecked" {{ if (it[k].isLowerPrice == true){ }} {{= checked="checked" }} {{ } }} >
					</label>
					<input type="hidden" name="machineId" readonly=readonly value="{{= it[k].id }}" />
        </td>
				<td><input type="text" class="tab_input" name="boxType" readonly=readonly value="{{= it[k].boxType }}" /></td>
				<td>{{= it[k].name }}</td>
        <td>{{= it[k].style }}</td>
        <td>{{= it[k].bigPaperNum }}</td>
				<td>{{= it[k].lowerPrice }}</td>
			</tr>
			{{ } }}
		</tbody>
	</table>
</div>
</script>

<script id="offerResult" type="text/x-dot-template">                    
<div class="item">
	<div class="item-kv">
		<div class="item-key w-69">成品规格 :</div>
		<div class="item-value">{{= it.spec }}</div>
	</div>
	<div class="item-kv">
		<div class="item-key w-69">成品数量 :</div>
		<div class="item-value">{{= it.amount }}</div>
	</div>
	<div class="item-kv">
		<div class="item-key w-69">含税单价 :</div>
		<div class="item-value">￥{{= it.price }}</div>
	</div>
	<div class="item-kv">
		<div class="item-key w-69">印刷颜色 :</div>
		<div class="item-value">{{= it.printColor }}</div>
	</div>
	<div class="item-kv">
		<div class="item-key w-69">印刷纸张 :</div>
		<div class="item-value">{{= it.printName }}</div>
	</div>
	<div class="item-kv">
		<div class="item-key w-69">纸张费用 :</div>
		<div class="item-value w-70p">￥{{= it.paperFee }}</div>
	</div>
	<div class="item-kv">
		<div class="item-key w-69">印刷费用 :</div>
		<div class="item-value w-70p">￥{{= it.printFee }}</div>
	</div>
	<div class="item-kv">
		<div class="item-key w-69">工序费用 :</div>
		<div class="item-value w-70p">￥{{= it.procedureFee }}</div>
	</div>
	<div class="item-kv">
		<div class="item-key w-69">其他费 :</div>
		<div class="item-value">￥{{= it.ohterFee }}</div>
	</div>
	<div class="item-kv">
		<div class="item-key w-69">运费 :</div>
		<div class="item-value">￥{{= it.freightFee }}</div>
	</div>
	<div class="item-kv">
		<div class="item-key w-69">含税金额 :</div>
		<div class="item-value">￥{{= it.taxFee }}</div>
	</div>
	<div id="dy_form" style="">
		<div class="item-kv">
			<div class="item-key w-69">报价日期 :</div>
			<div class="item-value">
				<input type="text" id="createDateTime" name="createDateTime" class="save_input" value="{{= it.createDateTime }}" readOnly="readOnly" />
				<span class="star_tip">*</span>
			</div>
		</div>
		<div class="item-kv">
			<div class="item-key w-69">成品名称 :</div>
			<div class="item-value">
				<input type="text" id="productName" name="productName" class="save_input" value="{{= it.productName }}" />
				<span class="star_tip">*</span>
			</div>
		</div>
		<div class="item-kv">
			<div class="item-key w-69">客户名称 :</div>
			<div class="item-value">
				<input type="text" id="customerName" name="customerName" class="save_input" />
				<span class="star_tip">*</span>
			</div>
		</div>
		<div class="item-kv">
			<div class="item-key w-69">联系人 :</div>
			<div class="item-value">
				<input type="text" id="linkName" name="linkName" class="save_input" />
				<span class="star_tip">*</span>
			</div>
		</div>
		<div class="item-kv">
			<div class="item-key w-69">联系电话 :</div>
			<div class="item-value">
				<input type="number" id="phone" name="phone" class="save_input" />
				<span class="star_tip">*</span>
			</div>
		</div>
		<div class="item-kv">
			<div class="item-key w-69">联系地址 :</div>
			<div class="item-value">
				<input type="text" id="linkAddress" name="linkAddress" class="save_input" />
				<span class="star_tip">*</span>
			</div>
		</div>
		<div class="item-kv">
			<div class="item-key w-69">交货日期 :</div>
			<div class="item-value">
				<input type="text" id="deliveryDate" class="save_input" value="{{= it.deliveryDateStr }}" />
				<span class="star_tip">*</span>
			</div>
		</div>
	</div>
</div>
</script>

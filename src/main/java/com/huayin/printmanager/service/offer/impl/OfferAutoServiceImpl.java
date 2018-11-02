/**
 * <pre>
 * Author:		THINK
 * Create:	 	2017年11月3日 上午9:42:20
 * Copyright: 	Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.offer.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.huayin.common.util.ObjectHelper;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.helper.service.OfferCommonHelper;
import com.huayin.printmanager.helper.service.OfferFormulaHelper;
import com.huayin.printmanager.helper.service.OfferHelper;
import com.huayin.printmanager.helper.service.OfferFormulaHelper.CustomCal;
import com.huayin.printmanager.helper.service.OfferFormulaHelper.SheetNumber;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.offer.OfferMachine;
import com.huayin.printmanager.persist.entity.offer.OfferOrder;
import com.huayin.printmanager.persist.entity.offer.OfferOrderQuoteInner;
import com.huayin.printmanager.persist.entity.offer.OfferOrderQuoteOut;
import com.huayin.printmanager.persist.entity.offer.OfferPart;
import com.huayin.printmanager.persist.entity.offer.OfferPartProcedure;
import com.huayin.printmanager.persist.entity.offer.OfferPrePrint;
import com.huayin.printmanager.persist.entity.offer.OfferProcedure;
import com.huayin.printmanager.persist.entity.offer.OfferProfit;
import com.huayin.printmanager.persist.entity.offer.OfferWaste;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.FormulaType;
import com.huayin.printmanager.persist.enumerate.OfferType;
import com.huayin.printmanager.persist.enumerate.PaperType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.offer.OfferAutoService;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 报价系统 - 自动报价实现层
 * </pre>
 * @author zhengby
 * @version 1.0, 2017年11月3日 上午9:42:20
 */
@Service
public class OfferAutoServiceImpl extends BaseServiceImpl implements OfferAutoService
{

	// ==================== 自动报价 - 公共 ======================

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferAutoService#save(com.huayin.printmanager.persist.entity.offer.
	 * OfferOrder)
	 */
	@Transactional
	@Override
	public OfferOrder save(OfferOrder offerOrder)
	{
		// 获取创建人
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			offerOrder.setCreateName(e.getName());
		}
		else
		{
			offerOrder.setCreateName(UserUtils.getUserName());
		}
		offerOrder.setCompanyId(UserUtils.getCompanyId());
		// 1-1. 报价单号：QU+年+月+三位流水号，（沿用之前报价系统报价单号生成逻辑）
		offerOrder.setOfferNo(UserUtils.createNo("QU"));
		OfferOrder quoteOrder = quote(offerOrder);
		// 清除order里面的部件列表，以便加入计算好的部件信息
		offerOrder.getOfferPartList().clear();
		// 选中的机台id
		// Long machineId = offerOrder.getChooseedMachineId();
		// List<String> chooseedMachineList = offerOrder.getChooseedMachineList();

		// 查询最低报价得机台
		List<OfferMachine> lowerPriceMachineList = _filterLowerPriceMachineList(quoteOrder);

		// 根据机台Id找出选中保存的机台
		for (OfferMachine offerMachine : lowerPriceMachineList)
		{
			OfferPart offerPart = offerMachine.getOfferPart();

			offerPart.setMachineName(offerMachine.getName()); // 印刷机名
			offerPart.setMachineSpec(offerMachine.getStyle()); // 上机尺寸
			offerPart.setSheetNum(offerMachine.getSheetNum()); // 拼版数
			offerPart.setImpositionNum(offerMachine.getImpositionNum()); // 印张正数
			offerPart.setWaste(offerMachine.getWaste()); // 损耗数
			offerPart.setPaperType(offerMachine.getPaperType()); // 大纸尺寸
			offerPart.setMaterialOpening(offerMachine.getMaterialOpening()); // 开数
			offerPart.setBigPaperNum(offerMachine.getBigPaperNum()); // 大纸张数
			offerPart.setBfluteNum(offerMachine.getBfluteNum()); // 坑纸数
			offerPart.setLowerPrice(offerMachine.getLowerPrice()); // 最低价
			offerPart.setMaxStyle(offerMachine.getMaxStyle()); // 最大上机尺寸
			// 上机长、上机宽
			String[] machineSpec = offerPart.getMachineSpec().split("\\*");
			offerPart.setMachineLength(new Integer(machineSpec[0]));
			offerPart.setMachineWidth(new Integer(machineSpec[1]));
			// 最大上机长、最大上机宽
			String[] maxStyle = offerPart.getMaxStyle().split("\\*");
			offerPart.setMaxLength(maxStyle[0]);
			offerPart.setMaxWidth(maxStyle[1]);
			// 正反普色
			offerPart.setProsConsColor(offerPart.getOfferPrintColorType().getValue().toString() + "+" + offerPart.getOfferPrintColorType2().getValue().toString());
			// 正反专色
			offerPart.setProsConsSpot(offerPart.getOfferSpotColorType().getValue().toString() + "+" + offerPart.getOfferSpotColorType2().getValue().toString());
			// 坑纸计价数量
			if (BoolValue.YES == offerPart.getContainBflute())
			{
				offerPart.setBfluteCalNum(OfferFormulaHelper.bfluteCalNum(offerPart.getBfluteNum(), offerPart.getMachineSpec()));
			}
			// 材料数量
			offerPart.setMaterialAmount(OfferFormulaHelper.materialAmount(offerPart.getImpositionNum(), offerPart.getMaterialOpening(), offerPart.getWaste()));
//			if (offerOrder.getOfferType() == OfferType.ALBUMBOOK)
//			{
//				// 书刊类的算式：印张正数=每贴正数*贴数
//				offerPart.setMaterialAmount(OfferFormulaHelper.materialAmount(offerPart.getImpositionNum() * offerPart.getThread(), offerPart.getMaterialOpening(), offerPart.getWaste()));
//			}
//			else
//			{
//				// 包装类
//				offerPart.setMaterialAmount(OfferFormulaHelper.materialAmount(offerPart.getImpositionNum(), offerPart.getMaterialOpening(), offerPart.getWaste()));
//			}
			// 印版张数
			offerPart.setSheetZQ(OfferFormulaHelper.sheetZQ(offerOrder,offerPart));
			// 后道工序费
			StringBuilder proFeeStr = new StringBuilder();
			if (null != offerPart.getOfferPartProcedureList())
			{
				for (OfferPartProcedure opp : offerPart.getOfferPartProcedureList())
				{
					// 通过报价类型与工序id查找出工序设置
					OfferProcedure procedure = serviceFactory.getOfferSettingService().getProcedure(offerOrder.getOfferType(), opp.getProcedureId());
					CustomCal fee = OfferFormulaHelper.procedureFee(offerOrder.getAmount(), offerPart, opp, procedure, offerMachine, offerOrder);
					proFeeStr.append(opp.getProcedureName()).append("(").append(fee.getFee()).append(")").append("&nbsp;&nbsp;");
				}
				offerPart.setPartProcedureStr(proFeeStr.toString());
			}

			if (offerOrder.getOfferType() == OfferType.ALBUMBOOK)
			{
				// 书刊类印张正数
				offerPart.setImpositionNum(OfferFormulaHelper._impositinNum(offerOrder.getAmount(), offerPart.getSheetNum(), offerPart.getPages(), offerPart.getOfferPrintStyleType()));
				// 贴数
				offerPart.setThread(OfferFormulaHelper.thread(offerPart.getPages(), offerMachine.getSheetNum(), offerPart.getOfferPrintStyleType()));
				// 印版张数
				offerPart.setSheetZQ(OfferFormulaHelper.sheetZQ(offerOrder,offerPart));
			}
			// 总印张
			offerPart.setPaperTotal(offerPart.getImpositionNum() + offerPart.getWaste());
			// 计价数量
			offerPart.setCalNum(OfferFormulaHelper.calNum(offerPart.getPaperType(), offerPart.getPaperWeight(), offerPart.getBigPaperNum()));

			offerOrder.getOfferPartList().add(offerPart);
		}

		// 成品费用
		offerOrder.setProductFee(quoteOrder.getPrintFee() + quoteOrder.getPaperFee() + quoteOrder.getProcedureFee());
		// 其他费用
		offerOrder.setOhterFee(quoteOrder.getOhterFee());
		// 印刷费用
		offerOrder.setPrintFee(quoteOrder.getPrintFee());
		// 工序费
		offerOrder.setProcedureFee(quoteOrder.getProcedureFee());
		// 纸张费
		offerOrder.setPaperFee(quoteOrder.getPaperFee());
		// 运费
		offerOrder.setFreightFee(quoteOrder.getFreightFee());
		// 合计费用
		offerOrder.setCostMoney(quoteOrder.getCostMoney());
		// 单价
		offerOrder.setUnitPrice(quoteOrder.getUnitPrice());

		if (offerOrder.getOfferType() == OfferType.ALBUMBOOK || offerOrder.getOfferType() == OfferType.CARTONBOX)
		{
			// 装订工序
			StringBuilder proFeeStr2 = new StringBuilder();
			for (OfferPartProcedure opp : offerOrder.getProductProcedure())
			{
				// 通过报价类型与工序id查找出工序设置
				OfferProcedure procedure = serviceFactory.getOfferSettingService().getProcedure(offerOrder.getOfferType(), opp.getProcedureId());
				CustomCal fee = OfferFormulaHelper.procedureFee(offerOrder.getAmount(), null, opp, procedure, lowerPriceMachineList.get(0), offerOrder);
				proFeeStr2.append(opp.getProcedureName()).append("(").append(fee.getFee()).append(")").append("&nbsp;&nbsp;");
			}
			offerOrder.setProductProcedureStr(proFeeStr2.toString());
		}

		// 首先保存主部件
		OfferOrder offerOrderNew = daoFactory.getCommonDao().saveEntity(offerOrder);

		// 书刊类保存成品工序（微信报价暂时没有成功工序）
		if ((offerOrder.getOfferType() == OfferType.ALBUMBOOK || offerOrder.getOfferType() == OfferType.CARTONBOX) && offerOrder.getProductProcedure() != null)
		{
			for (OfferPartProcedure opp : offerOrder.getProductProcedure())
			{
				opp.setOrderId(offerOrderNew.getId());
				opp.setCompanyId(offerOrderNew.getCompanyId());
			}
			daoFactory.getCommonDao().saveAllEntity(offerOrder.getProductProcedure());
		}

		/* 保存对外报价单、内部核价单的阶梯数据报表、费用明细表 */
		// 保存内部核价单的阶梯数据报表
		if (null != offerOrder.getOfferOrderQuoteInnerList())
		{
			for (OfferOrderQuoteInner inner : offerOrder.getOfferOrderQuoteInnerList())
			{
				inner.setCompanyId(offerOrderNew.getCompanyId());
				inner.setMasterId(offerOrderNew.getId());
			}
			daoFactory.getCommonDao().saveAllEntity(offerOrder.getOfferOrderQuoteInnerList());
		}

		// 保存对外报价单的阶梯数据报表
		if (null != offerOrder.getOfferOrderQuoteOutList())
		{
			for (OfferOrderQuoteOut out : offerOrder.getOfferOrderQuoteOutList())
			{
				out.setCompanyId(offerOrderNew.getCompanyId());
				out.setMasterId(offerOrderNew.getId());
			}
			daoFactory.getCommonDao().saveAllEntity(offerOrder.getOfferOrderQuoteOutList());
		}

		// 遍历部件列表，给每个部件添加主部件的id
		for (OfferPart part : offerOrder.getOfferPartList())
		{
			part.setCompanyId(offerOrderNew.getCompanyId());
			part.setMasterId(offerOrderNew.getId());
			OfferPart _part = daoFactory.getCommonDao().saveEntity(part);
			// 当部件的工序不为空的时候，需要给所选的后道工序补上部件的id
			if (CollectionUtils.isNotEmpty(part.getOfferPartProcedureList()))
			{
				// 遍历每个工序，填充所属部件的id
				for (OfferPartProcedure partPro : part.getOfferPartProcedureList())
				{
					partPro.setCompanyId(UserUtils.getCompanyId());
					partPro.setPartId(_part.getId());
				}
				daoFactory.getCommonDao().saveAllEntity(part.getOfferPartProcedureList());
			}
		}
		return offerOrderNew;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.offer.OfferAutoService#quote(com.huayin.printmanager.persist.entity.offer.
	 * OfferOrder)
	 */
	@Transactional
	@Override
	public OfferOrder quote(OfferOrder offerOrder)
	{
		/**
		 * 1. 内部核价计算
		 * 2. 对外报价计算
		 * 3. 拼版开料图
		 */

		OfferOrder offerOrderNew = new OfferOrder();

		// 1. 内部核价计算
		_quoteInner(offerOrderNew, offerOrder);
		// 2. 对外报价计算
		_quoteOuter(offerOrderNew, offerOrder);
		// 3. 拼版开料图
		_quoteOpening(offerOrderNew, offerOrder);

		return offerOrderNew;
	}

	/**
	 * <pre>
	 * 对外报价计算
	 * </pre>
	 * @param offerOrderNew
	 * @param offerOrder
	 * @since 1.0, 2017年11月7日 上午11:34:45, think
	 */
	private void _quoteOuter(OfferOrder offerOrderNew, OfferOrder offerOrder)
	{
		// 1-1. 对外报价 - 报价单
		_quoteOuterOrder(offerOrderNew, offerOrder);
		// 1-2. 对外报价 - 报价单列表
		_quoteOuterOrderList(offerOrderNew, offerOrder);
	}

	/**
	 * <pre>
	 * 对外报价  - 报价单
	 * </pre>
	 * @param offerOrderNew
	 * @param offerOrder
	 * @since 1.0, 2017年11月8日 下午7:41:05, think
	 */
	private void _quoteOuterOrder(OfferOrder offerOrderNew, OfferOrder offerOrder)
	{
		// 1-1. 报价时间:取系统当前的默认时间
		offerOrderNew.setCreateDateTime(new Date());
		// 1-1. 交货时间：默认取系统当前时间，可以弹出框选择
		offerOrderNew.setDeliveryDate(new Date());
		// 1-1. 客户名称：可以手动输入，也可以弹出看选择印管家基础资料里的客户信息
		// 1-1. 客户地址：可以手动输入，也可以根据弹出框选择的客户名称携带客户地址
		// 1-1. 联系人：可以手动输入，也可以根据弹出框选择的客户名称携带客户地址
		// 1-1. 联系电话：可以手动输入，也可以根据弹出框选择的客户名称携带客户地址
		// 1-1. 联系电话：可以手动输入，也可以根据弹出框选择的客户名称携带客户地址
		// 1-1. 成品尺寸：直接取上面报价的成品尺寸，不可编辑输入，自渎
		offerOrderNew.setStyleLength(offerOrder.getStyleLength());
		offerOrderNew.setStyleWidth(offerOrder.getStyleWidth());
		// 1-1. 数量：根据前面阶梯数量的列数的不同生成不同的列数，根据前面不同阶梯数量间隔用报价数量+阶梯数量间隔生成不同数量
		offerOrderNew.setAmount(offerOrder.getAmount());
		offerOrderNew.setSpec(offerOrder.getSpec());
		offerOrderNew.setSpecification(offerOrder.getSpecification());
	}

	/**
	 * <pre>
	 * 对外报价 - 报价单列表
	 * </pre>
	 * @param offerOrderNew
	 * @param offerOrder
	 * @since 1.0, 2017年11月8日 下午7:41:15, think
	 */
	private void _quoteOuterOrderList(OfferOrder offerOrderNew, OfferOrder offerOrder)
	{
		// 计算内部核价的税额信息，方便通过数量获取
		Map<Integer, Map<String, Object>> innerMap = Maps.newHashMap();
		List<Map<String, Object>> offerMachineOrderList = offerOrderNew.getOfferMachineOrderList();
		for (Map<String, Object> orderMap : offerMachineOrderList)
		{
			int amount = (int) orderMap.get("amount");
			innerMap.put(amount, orderMap);
		}

		// 多部件
		List<OfferPart> offerPartList = Lists.newArrayList();
		// 原始数量
		int base = offerOrder.getAmount().intValue();
		// 间隔数量
		int speed = offerOrder.getLadderSpeed();
		for (int i = 0; i < offerOrder.getLadderCol(); i++)
		{
			OfferPart offerPart = new OfferPart();
			// 印刷纸张（所有纸张去除重复名）
			String printName = OfferCommonHelper.removeDuplicatePrintName(offerOrder.getOfferPartList());
			offerPart.setPrintName(printName);
			// 颜色
			String printColor = OfferCommonHelper.removeDuplicatePrintColor(offerOrder.getOfferPartList());
			offerPart.setPrintColor(printColor);
			// 加工工序
			String printProcedure = OfferCommonHelper.removeDuplicatePrintProcedure(offerOrder.getOfferPartList(),offerOrder.getProductProcedure());
			offerPart.setPrintProcedure(printProcedure);
			// 数量
			offerPart.setAmount(base);
			Map<String, Object> map = innerMap.get(base);
			// double costMoney = (double) map.get("costMoney");
			double costMoney = (double) map.get("untaxedFee");
			// 单价：金额/数量
			offerPart.setPrice(OfferFormulaHelper.scale(costMoney / base, 4));
			// 金额：内部核价的不含税金额
			offerPart.setFee(costMoney);
			// 含税单价：含税金额/数量
			offerPart.setTaxPrice((double) map.get("taxPrice"));
			// 含税金额：内部核价的含税金额
			offerPart.setTaxFee((double) map.get("taxFee"));

			// 数量累加
			base = base + speed;
			// 追加到列表
			offerPartList.add(offerPart);
		}

		offerOrderNew.setOfferPartList(offerPartList);
	}

	/**
	 * <pre>
	 * 内部核价计算
	 * </pre>
	 * @param offerOrderNew
	 * @param offerOrder
	 * @since 1.0, 2017年11月6日 上午11:22:16, think
	 */
	private void _quoteInner(OfferOrder offerOrderNew, OfferOrder offerOrder)
	{
		// 2. 内部核价计算
		// 2-1. 内部核价 - 机台列表
		_quoteInnerMachine(offerOrderNew, offerOrder);
		// 2-2. 内部核价 - 报价列表
		_quoteInnerOrder(offerOrderNew, offerOrder);
		// 2-3. 内部核价 - 费用明细
		_quoteInnerFeeDetail(offerOrderNew, offerOrder);
	}

	/**
	 * <pre>
	 * 内部核价 - 机台列表
	 * </pre>
	 * @param offerOrderNew
	 * @param offerOrder
	 * @since 1.0, 2017年11月7日 下午4:07:54, think
	 */
	private void _quoteInnerMachine(OfferOrder offerOrderNew, OfferOrder offerOrder)
	{
		/**
		 * 1. 获取所有的机台（正常情况下，小公司最多两三台）和 部件
		 * 2. 根据规格计算出最合适的机台
		 */

		// 1. 获取所有的机台（正常情况下，小公司最多两三台）和 部件

		// 获取所有的机台（正常情况下，小公司最多两三台）
		List<OfferMachine> offerMachineList = serviceFactory.getOfferSettingService().findAllMachine(offerOrder.getOfferType());
		// 获取所有部件
		List<OfferPart> offerPartList = offerOrder.getOfferPartList();
		// 创建一个新机台列表（部件 * 机台）
		List<OfferMachine> offerMachineListNew = Lists.newArrayList();

		// 2. 根据规格计算出最合适的机台
		// 自增长
		int index = 1;
		// 总P数
		int tpNumber = 0;
		for (OfferPart _offerPart : offerPartList)
		{
			// 只有书刊只有P数
			if (null != _offerPart.getPages())
			{
				tpNumber += _offerPart.getPages();
			}
			else
			{
				tpNumber += 0;
			}
		}
		// 构造部件
		for (OfferPart offerPart : offerPartList)
		{
			// 构建机台列表
			for (OfferMachine offerMachine : offerMachineList)
			{
				OfferMachine offerMachineNew = ObjectHelper.byteClone(offerMachine);
				OfferPart offerPartNew = ObjectHelper.byteClone(offerPart);
				offerMachineNew.setOfferPart(offerPartNew);

				/**
				 * 盒型
				 * 
				 * 画册书刊： 封面、内页、插页
				 * 彩盒纸箱：扣底带挂勾盒、平口箱盒、收缩盒、多部件盒型1、多部件盒型2
				 * 单张类
				 * 便签信纸
				 */
				// 盒型
				String boxType = offerOrder.getBoxType();
				// 多部件盒型特殊处理（多部件盒型1，多部件盒型2、多部件盒型N）
				if (offerPartList.size() > 1 && OfferType.CARTONBOX == offerOrder.getOfferType())
				{
					boxType = boxType + index;
				}
				// 画册书刊特殊处理（封面、内页、插页）
				if (offerPartList.size() > 1 && OfferType.ALBUMBOOK == offerOrder.getOfferType())
				{
					boxType = offerPart.getPartName();
				}
				offerMachineNew.setBoxType(boxType);
				// 印刷方式：根据报价时选择的是单面印刷还是双面印刷
				offerMachineNew.setOfferPrintStyleType(offerPart.getOfferPrintStyleType());

				// 计算出最优的拼版数
				List<SheetNumber> sheetNumberList = OfferFormulaHelper.sheetNumber(offerMachine.getMaxStyle(), offerMachine.getMinStyle(), offerOrder.getStyleLength(), offerOrder.getStyleWidth());
				// 最优拼版数
				SheetNumber best = null;
				PaperType bestPaperType = null;
				PaperType paperType = null;
				// 最大拼版数
				Integer bestSumSheetNum = 0;
				Integer bestSheetNum = 0;
				// 计算出最优的拼版数
				for (SheetNumber sheetNumber : sheetNumberList)
				{
					// 最大拼版数
					Integer _sheetNum = 0;

					// 没有设置PaperType, 则需要同时计算正度和大度纸，并得到最优的计算
					if (offerOrder.getPaperType() == null)
					{
						Integer sheetNum1 = _quoteInnerMachineBestSheetNum(offerOrder, sheetNumber, PaperType.ARE_DEGREES_PAPER, offerMachineNew);
						Integer sheetNum2 = _quoteInnerMachineBestSheetNum(offerOrder, sheetNumber, PaperType.MAGNANIMOUS_PAPER, offerMachineNew);
						// 最大乘积相等，选择正度纸（因为纸张小）；sheetNum1大于sheetNum2，选择sheetNum1
						if (sheetNum1 == sheetNum2 || sheetNum1 > sheetNum2)
						{
							_sheetNum = sheetNum1;
							paperType = PaperType.ARE_DEGREES_PAPER;
						}
						else
						{
							_sheetNum = sheetNum2;
							paperType = PaperType.MAGNANIMOUS_PAPER;
						}
					}
					// 按照页面正常计算
					else
					{
						_sheetNum = _quoteInnerMachineBestSheetNum(offerOrder, sheetNumber, offerOrder.getPaperType(), offerMachineNew);
						paperType = offerOrder.getPaperType();
					}

					// 最终最优的拼版数 = 最大拼版数 + 拼版数
					Integer _bestSumSheetNum = _sheetNum + sheetNumber.getSheetNum();
					// 优先乘积最大值， 乘积相等则取最大拼版数，最优拼版相等则取正度纸
					if (_sheetNum >= bestSheetNum && (_bestSumSheetNum > bestSumSheetNum || (_bestSumSheetNum == bestSumSheetNum && PaperType.ARE_DEGREES_PAPER == paperType)))
					{
						bestSheetNum = _sheetNum;
						bestSumSheetNum = _bestSumSheetNum;
						best = sheetNumber;
						bestPaperType = paperType;
					}

					// logger.info("--------------------");
				}

				if (best == null)
				{
					continue;
				}

				// 拼版数
				offerMachineNew.setSheetLen(best.getSheetLen());
				offerMachineNew.setSheetWidth(best.getSheetWidth());
				offerMachineNew.setSheetNum(best.getSheetNum());
				// 上机尺寸
				String style = OfferFormulaHelper.style(offerOrder.getStyleLength(), offerOrder.getStyleWidth(), best);
				offerMachineNew.setStyle(style);
				// 纸开度
				Integer materialOpening = OfferFormulaHelper.materialOpening(bestPaperType, style);
				offerMachineNew.setMaterialOpening(materialOpening);
				// 印张正数：包装类：印刷数量/拼版数 || 书刊类：印刷数量*部件P数/单面拼数*（单面=1， 双面=2）
				Integer impositionNum = OfferFormulaHelper.impositinNum(offerOrder.getAmount(), best.getSheetNum(), offerPart.getPages(), offerPart.getOfferPrintStyleType(), offerOrder);
				offerMachineNew.setImpositionNum(impositionNum);
				// 损耗数
				Integer waste = OfferFormulaHelper.waste(offerOrder, offerMachineNew, offerPart);
				offerMachineNew.setWaste(waste);
				// 大纸尺寸：根据上面最优的拼版方式算出是大度纸（889*1194）还是正度纸（787*1092）
				offerMachineNew.setPaperType(bestPaperType);
				// 大纸张数：（印张正数+损耗数）/纸开度
				Integer bigPaperNum = OfferFormulaHelper.bigPaperNum(impositionNum, waste, materialOpening);
				offerMachineNew.setBigPaperNum(bigPaperNum);
				// 坑纸数：印张正数+印后工序
				if (offerOrder.getOfferType() == OfferType.CARTONBOX && offerPart.getContainBflute() == BoolValue.YES)
				{
					Integer workAfter = 0;
					if (offerPart.getOfferPartProcedureList() != null && offerPart.getOfferPartProcedureList().size() > 0)
					{
						OfferWaste offerWaste = OfferHelper.findWasteSettingByOfferType(offerOrder.getOfferType());
						workAfter = offerWaste.getWorkAfter();
					}
					Integer bfluteNum = OfferFormulaHelper.bfluteNum(impositionNum, workAfter);
					offerMachineNew.setBfluteNum(bfluteNum);
				}
				else
				{
					offerMachineNew.setBfluteNum(0);
				}
				// 手/贴数：P数/单面拼数，进位取整，不足1的按一贴算(书刊类)
				if (offerOrder.getOfferType() == OfferType.ALBUMBOOK)
				{
					Integer thread = OfferFormulaHelper.thread(offerPart.getPages(), best.getSheetNum(), offerPart.getOfferPrintStyleType());
					offerPartNew.setThread(thread);
					offerMachineNew.setThread(thread);
				}

				offerMachineNew.setTpnumber(tpNumber);

				// 印刷机：根据上面计算的最优方案的拼版选择总金额的最少的印刷机 （机台名称）
				// 计算印刷费
				CustomCal cal = OfferFormulaHelper.quoteInnerMachineOptimal(offerMachineNew, offerOrder, offerPartNew);
				if(cal == null)
				{
					continue;
				}
				offerMachineNew.setLowerPrice(Double.valueOf(cal.getFee()));

				// 添加到集合中
				offerMachineListNew.add(offerMachineNew);
			}

			index++;
		}
		// 直接使用页面选中的机台
		List<String> chooseedMachineList = offerOrder.getChooseedMachineList();
		if (null == chooseedMachineList)
		{
			// 最低价的机台列表
			chooseedMachineList = Lists.newArrayList();

			// 每个机台最低价的部件
			Map<String, List<OfferMachine>> offerMachineMap = Maps.newHashMap();
			for (OfferMachine offerMachine : offerMachineListNew)
			{
				String boxType = offerMachine.getBoxType();
				List<OfferMachine> list = offerMachineMap.get(boxType);
				if (null == list)
				{
					list = Lists.newArrayList();
					offerMachineMap.put(boxType, list);
				}
				list.add(offerMachine);
			}

			// 计算最低价的机台
			for (Iterator<Entry<String, List<OfferMachine>>> it = offerMachineMap.entrySet().iterator(); it.hasNext();)
			{
				Entry<String, List<OfferMachine>> next = it.next();
				List<OfferMachine> value = next.getValue();
				// 遍历所有并设置最低机台
				double lowerPrice = -1;
				String lowerBoxType = null;

				for (OfferMachine offerMachine : value)
				{
					// 计算最小的机台价格（使用机台ID+部件名称，如果页面没有选中机台，则第一台暂时作为最低价）
					double price = offerMachine.getLowerPrice();
					if (lowerPrice == -1 || price < lowerPrice)
					{
						lowerPrice = price;
						lowerBoxType = offerMachine.getBoxType() + offerMachine.getId();
					}
				}

				chooseedMachineList.add(lowerBoxType);
			}

		}

		// 最值最低报价机台
		for (String key : chooseedMachineList)
		{
			for (OfferMachine offerMachine : offerMachineListNew)
			{
				// 使用机台ID+Boxtype作为Key
				Long id = offerMachine.getId();
				String boxType = offerMachine.getBoxType();
				String _key = boxType + id;
				if (key.equals(_key))
				{
					offerMachine.setIsLowerPrice(true);
					break;
				}
			}
		}

		offerOrderNew.setOfferMachineList(offerMachineListNew);

		// 设置总贴数，设置总P数
		if (offerOrder.getOfferType() == OfferType.ALBUMBOOK)
		{
			// 总贴数
			int threads = 0;
			List<OfferMachine> machineList = _filterLowerPriceMachineList(offerOrderNew);
			for (OfferMachine offerMachine : machineList)
			{
				// 累加总帖数
				threads += offerMachine.getThread();
			}
			// 每个机台设置总贴数
			for (OfferMachine offerMachine : machineList)
			{
				offerMachine.setThreads(threads);
				
			}
		}
	}

	/**
	 * <pre>
	 * 内部核价 - 计算机台最大拼版数
	 * </pre>
	 * @param offerOrder
	 * @param sheetNumber
	 * @param paperType
	 * @param offerMachineNew
	 * @return
	 * @since 1.0, 2017年11月13日 下午3:52:23, think
	 */
	private Integer _quoteInnerMachineBestSheetNum(OfferOrder offerOrder, SheetNumber sheetNumber, PaperType paperType, OfferMachine offerMachineNew)
	{
		// 上机尺寸
		String style = OfferFormulaHelper.style(offerOrder.getStyleLength(), offerOrder.getStyleWidth(), sheetNumber);
		// 纸开度
		Integer materialOpening = OfferFormulaHelper.materialOpening(paperType, style);
		// 最大拼版数
		Integer sheetNum = sheetNumber.getSheetNum() * materialOpening;

		// StringBuilder sb = new StringBuilder();
		// sb.append("\r\n");
		// sb.append("部件名称: ").append(offerMachineNew.getBoxType()).append("\r\n");
		// sb.append("机台: ").append(offerMachineNew.getName()).append("\r\n");
		// sb.append("机台尺寸: ").append(offerMachineNew.getMaxStyle()).append("\r\n");
		// sb.append("上机尺寸: ").append(style).append("\r\n");
		// sb.append("拼版数: ").append(sheetNumber.getSheetNum()).append("\r\n");
		// sb.append("纸开度: ").append(materialOpening).append("\r\n");
		// sb.append("最大拼版数: ").append(sheetNum).append("\r\n");
		// logger.info(sb.toString());

		return sheetNum;
	}

	/**
	 * <pre>
	 * 内部核价 - 报价列表
	 * </pre>
	 * @param offerOrderNew
	 * @param offerOrder
	 * @since 1.0, 2017年11月7日 下午7:06:58, think
	 */
	private void _quoteInnerOrder(OfferOrder offerOrderNew, OfferOrder offerOrder)
	{
		List<Map<String, Object>> offerMachineOrderList = Lists.newArrayList();
		// 利润
		List<OfferProfit> profitList = serviceFactory.getOfferSettingService().getProfit(offerOrder.getOfferType());
		// 税率
		Integer taxPercent = offerOrder.getTaxPercent();

		// 原始数量
		int base = offerOrder.getAmount().intValue();
		// 间隔数量
		int speed = offerOrder.getLadderSpeed();
		for (int i = 0; i < offerOrder.getLadderCol(); i++)
		{
			Map<String, Object> orderMap = Maps.newHashMap();
			// 数量：根据前面阶梯数量的列数的不同生成不同的列数，根据前面不同阶梯数量间隔用报价数量+阶梯数量间隔生成不同数量
			orderMap.put("amount", base);

			// 成本金额：纸张费+印刷费+工序费+其他费+运费
			double costMoney = 0;

			// 调用接口计算每个数量的数据，计算费用
			_quoteInnerFeeDetail(offerOrderNew, offerOrder, base);
			List<Map<String, Object>> offerMachineDetailFeeList = offerOrderNew.getOfferMachineDetailFeeList();
			for (Map<String, Object> map : offerMachineDetailFeeList)
			{
				String paperFeeType = (String) map.get("paperFeeType");
				Double fee = Double.valueOf((String) map.get("paperFee"));
				costMoney = costMoney + fee;
				// 纸张费
				if ("FEE_1".equals(paperFeeType))
				{
					orderMap.put("paperFee", fee);
				}
				// 印刷费
				else if ("FEE_2".equals(paperFeeType))
				{
					orderMap.put("printFee", fee);
				}
				// 工序费
				else if ("FEE_3".equals(paperFeeType))
				{
					orderMap.put("procedureFee", fee);
				}
				// 其他费
				else if ("FEE_4".equals(paperFeeType))
				{
					orderMap.put("ohterFee", fee);
				}
				// 运费
				else if ("FEE_5".equals(paperFeeType))
				{
					orderMap.put("freightFee", fee);
				}
			}
			// 成本金额：纸张费+印刷费+工序费+其他费+运费
			orderMap.put("costMoney", OfferFormulaHelper.scale(costMoney));
			// 利润：根据彩盒设置里的利润设置，计算出不同阶梯数量的利润金额
			double profitFee = OfferFormulaHelper.profitFee(base, profitList, costMoney);
			orderMap.put("profitFee", profitFee);
			// 未税金额：成本金额+利润
			double untaxedFee = OfferFormulaHelper.untaxedFee(costMoney, profitFee);
			orderMap.put("untaxedFee", untaxedFee);
			// 未税单价：未税金额/数量
			double untaxedPrice = OfferFormulaHelper.untaxedPrice(base, untaxedFee);
			orderMap.put("untaxedPrice", untaxedPrice);
			// 含税金额：未税金额*（1+税率）
			double taxFee = OfferFormulaHelper.taxFee(untaxedFee, taxPercent);
			orderMap.put("taxFee", taxFee);
			// 含税单价：含税金额/数量
			double taxPrice = OfferFormulaHelper.taxPrice(base, taxFee);
			orderMap.put("taxPrice", taxPrice);
			// 数量累加
			base = base + speed;
			// 追加到列表中
			offerMachineOrderList.add(orderMap);
		}

		offerOrderNew.setOfferMachineOrderList(offerMachineOrderList);
	}

	/**
	 * <pre>
	 * 内部核价 - 费用明细
	 * </pre>
	 * @param offerOrderNew
	 * @param offerOrder
	 * @since 1.0, 2017年11月7日 下午7:09:58, think
	 */
	private void _quoteInnerFeeDetail(OfferOrder offerOrderNew, OfferOrder offerOrder)
	{
		_quoteInnerFeeDetail(offerOrderNew, offerOrder, offerOrder.getAmount());
	}

	/**
	 * <pre>
	 * 内部核价 - 费用明细（根据数量计算）
	 * </pre>
	 * @param offerOrderNew
	 * @param offerOrder
	 * @param amount
	 * @since 1.0, 2017年11月9日 下午5:24:50, think
	 */
	private void _quoteInnerFeeDetail(OfferOrder offerOrderNew, OfferOrder offerOrder, Integer amount)
	{
		// 内部核价 - 费用明细列表
		List<Map<String, Object>> offerDetailFeeList = Lists.newArrayList();
		// 内部核价 - 费用明细部件列表
		List<OfferPart> offerDetailFeePartList = offerOrder.getOfferPartList();
		// 印前设置
		OfferPrePrint prePrint = serviceFactory.getOfferSettingService().getPrePrint(offerOrder.getOfferType());
		// 查询最低报价得机台
		List<OfferMachine> lowerPriceMachineList = _filterLowerPriceMachineList(offerOrderNew);

		// ==================== 费用明细 - 纸张费 ======================
		Map<String, Object> detailFee1 = Maps.newHashMap();
		detailFee1.put("paperFeeName", "纸张费");
		detailFee1.put("paperFeeType", "FEE_1");
		// 纸张费 - 计算方案
		StringBuilder paperFeeCal1 = new StringBuilder();
		// 纸张费 - 金额
		double fee = 0;
		// 自增长
		int index = 1;
		for (OfferMachine offerMachine : lowerPriceMachineList)
		{
			OfferPart offerPart = offerMachine.getOfferPart();
			// 印张正数（防止修改原生数据，使用克隆独立计算）
			Integer impositionNum = OfferFormulaHelper.impositinNum(amount, offerMachine.getSheetNum(), offerPart.getPages(), offerPart.getOfferPrintStyleType(), offerOrder);
			OfferOrder _offerOrder = ObjectHelper.byteClone(offerOrder);
			OfferPart _offerPart = ObjectHelper.byteClone(offerPart);
			OfferMachine _offerMachine = ObjectHelper.byteClone(offerMachine);
			_offerOrder.setAmount(amount);
			_offerPart.setAmount(amount);
			_offerMachine.setImpositionNum(impositionNum);
			// 损耗数
			Integer waste = OfferFormulaHelper.waste(_offerOrder, _offerMachine, _offerPart);
			// 大纸张费
			Integer bigPaperNum = OfferFormulaHelper.bigPaperNum(impositionNum, waste, offerMachine.getMaterialOpening());
			// 纸张费 - 大纸张费
			double bigPaperFee;// 如果是客来纸，则不需计算纸张费
			if (offerPart.getCustomPaper() == BoolValue.YES)
			{
				bigPaperFee = 0;
			}
			else
			{
				bigPaperFee = OfferFormulaHelper.bigPaperFee(offerMachine.getPaperType(), offerPart.getPaperWeight(), bigPaperNum, offerPart.getPaperTonPrice());
			}
			// 纸张费 - 坑纸费
			double bflutePaperFee = 0;
			// 画册书刊
			if (OfferType.ALBUMBOOK == offerOrder.getOfferType())
			{
				paperFeeCal1.append("【").append(offerPart.getPartName()).append("】");
			}
			// 多部件盒型（多条情况下，实用序号累加）
			else if (OfferType.CARTONBOX == offerOrder.getOfferType() && offerDetailFeePartList.size() > 1)
			{
				paperFeeCal1.append("【").append(offerOrder.getBoxType()).append(index).append("】");
			}
			else
			{
				paperFeeCal1.append("【").append(offerOrder.getBoxType()).append("】");
			}
			paperFeeCal1.append("大纸张数").append(offerMachine.getBigPaperNum()).append("*");
			paperFeeCal1.append("大纸尺寸").append(offerMachine.getPaperType().getStyle()).append("*");
			paperFeeCal1.append("大纸克重").append(offerPart.getPaperWeight()).append("*");
			paperFeeCal1.append("大纸单价").append(offerPart.getPaperTonPrice()).append("（吨）=").append(bigPaperFee);
			if (offerPart.getCustomPaper() == BoolValue.YES)
			{
				paperFeeCal1.append("【客来纸】");
			}
			paperFeeCal1.append("<br>");
			// 坑纸数
			if (offerPart.getContainBflute() != null && offerPart.getContainBflute() == BoolValue.YES)
			{
				// 坑纸数 = 印张正数+损耗数
				Integer workAfter = 0;
				if (offerPart.getOfferPartProcedureList() != null && offerPart.getOfferPartProcedureList().size() > 0)
				{
					OfferWaste offerWaste = OfferHelper.findWasteSettingByOfferType(offerOrder.getOfferType());
					workAfter = offerWaste.getWorkAfter();
				}
				Integer bfluteNum = OfferFormulaHelper.bfluteNum(impositionNum, workAfter);
				bflutePaperFee = OfferFormulaHelper.bflutePaperFee(bfluteNum, offerMachine.getStyle(), offerPart.getBflutePrice());
				paperFeeCal1.append("【").append(offerOrder.getBoxType()).append(index).append("】");
				paperFeeCal1.append(offerPart.getBflutePit()).append(offerPart.getBflutePaperQuality());
				paperFeeCal1.append("坑纸数").append(offerMachine.getBfluteNum()).append("*");
				paperFeeCal1.append("坑纸尺寸").append(offerMachine.getStyle()).append("*");
				paperFeeCal1.append("坑纸单价").append(offerPart.getBflutePrice()).append("（元/千平方英寸）=").append(bflutePaperFee);
				paperFeeCal1.append("<br>");
			}

			index++;
			fee = fee + bigPaperFee + bflutePaperFee;
			// 部件纸张费
			offerPart.setPartPaperFee(bigPaperFee + bflutePaperFee);
		}
		detailFee1.put("paperFeeCal", paperFeeCal1.toString());
		detailFee1.put("paperFee", OfferFormulaHelper.scale(fee));
		// 追加到列表中
		offerDetailFeeList.add(detailFee1);
		// 报价单的纸张费用
		offerOrderNew.setPaperFee(fee);
		// ==================== 费用明细 - 印刷费 ======================
		Map<String, Object> detailFee2 = Maps.newHashMap();
		detailFee2.put("paperFeeName", "印刷费");
		detailFee2.put("paperFeeType", "FEE_2");
		// 印刷费 - 计算方案
		StringBuilder paperFeeCal2 = new StringBuilder();
		// 纸张费 - 金额
		fee = 0;
		// 自增长
		index = 1;
		for (OfferMachine offerMachine : lowerPriceMachineList)
		{
			OfferPart offerPart = offerMachine.getOfferPart();
			// 印张正数（防止修改原生数据，使用克隆独立计算）
			Integer impositionNum = OfferFormulaHelper.impositinNum(amount, offerMachine.getSheetNum(), offerPart.getPages(), offerPart.getOfferPrintStyleType(), offerOrder);
			OfferOrder _OfferOrder = ObjectHelper.byteClone(offerOrder);
			_OfferOrder.setAmount(amount);
			OfferMachine _offerMachine = ObjectHelper.byteClone(offerMachine);
			_offerMachine.setImpositionNum(impositionNum);
			CustomCal startCal = OfferFormulaHelper.quoteInnerMachineOptimal(_offerMachine, _OfferOrder, offerPart);
			// 画册书刊
			if (OfferType.ALBUMBOOK == offerOrder.getOfferType())
			{
				paperFeeCal2.append("【").append(offerPart.getPartName()).append("】");
			}
			// 多部件盒型（多条情况下，实用序号累加）
			else if (OfferType.CARTONBOX == offerOrder.getOfferType() && offerDetailFeePartList.size() > 1)
			{
				paperFeeCal2.append("【").append(offerOrder.getBoxType()).append(index).append("】");
			}
			else
			{
				paperFeeCal2.append("【").append(offerOrder.getBoxType()).append("】");
			}
			// 区分机台的费用是自定义公式还是普通公式
			if (null == _offerMachine.getOfferFormula() || _offerMachine.getOfferFormula().getFormulaType() != FormulaType.CUSTOM)
			{
				// 普通公式
				paperFeeCal2.append(startCal.getFormula());
				paperFeeCal2.append("<br>");

			}
			else
			{
				// 自定义公式
				paperFeeCal2.append(startCal.getFormula() + "=").append(startCal.getFee());
				paperFeeCal2.append("<br>");
			}
			fee = fee + Double.valueOf(startCal.getFee());
			// 部件印刷费
			offerPart.setPartPrintFee(Double.valueOf(startCal.getFee()));
			index++;
		}
		detailFee2.put("paperFeeCal", paperFeeCal2.toString());
		detailFee2.put("paperFee", OfferFormulaHelper.scale(fee));
		// 追加到列表中
		offerDetailFeeList.add(detailFee2);
		// 报价单的印刷费用
		offerOrderNew.setPrintFee(fee);

		// ==================== 费用明细 - 工序费 ======================
		Map<String, Object> detailFee3 = Maps.newHashMap();
		// 工序费：根据选择的工序对应彩盒工序设置里的工序对应的计算公式，计算出每个部件的每道工序的费用
		detailFee3.put("paperFeeName", "工序费");
		detailFee3.put("paperFeeType", "FEE_3");
		// 印刷费 - 计算方案
		StringBuilder paperFeeCal3 = new StringBuilder();
		// 纸张费 - 金额
		fee = 0;
		// 自增长
		index = 1;
		for (OfferMachine offerMachine : lowerPriceMachineList)
		{
			OfferPart offerPart = offerMachine.getOfferPart();
			List<OfferPartProcedure> offerPartProcedureList = offerPart.getOfferPartProcedureList();
			if (offerPartProcedureList != null && offerPartProcedureList.size() > 0)
			{
				for (OfferPartProcedure offerPartProcedure : offerPartProcedureList)
				{
					// 通过报价类型与工序id查找出工序设置
					OfferProcedure procedure = serviceFactory.getOfferSettingService().getProcedure(offerOrder.getOfferType(), offerPartProcedure.getProcedureId());
					// 计算出工序费
					CustomCal procedureCal = OfferFormulaHelper.procedureFee(amount.intValue(), offerPart, offerPartProcedure, procedure, offerMachine, offerOrder);
					double procedureFee = Double.valueOf(procedureCal.getFee());
					// 画册书刊
					if (OfferType.ALBUMBOOK == offerOrder.getOfferType())
					{
						paperFeeCal3.append("【").append(offerPart.getPartName()).append("-" + offerPartProcedure.getProcedureName()).append("】");
					}
					// 多部件盒型（多条情况下，实用序号累加）
					else if (OfferType.CARTONBOX == offerOrder.getOfferType() && offerDetailFeePartList.size() > 1)
					{
						paperFeeCal3.append("【").append(offerOrder.getBoxType()).append(index).append("-" + offerPartProcedure.getProcedureName()).append("】");
					}
					else
					{
						paperFeeCal3.append("【").append(offerOrder.getBoxType()).append("-" + offerPartProcedure.getProcedureName()).append("】");
					}
					// 工序费文字描述
					paperFeeCal3.append("(" + procedureCal.getFormula() + ")=" + procedureCal.getFee());
					paperFeeCal3.append("<br>");
					fee = fee + procedureFee;
					// 部件工序费
					offerPart.setPartProcedureFee(procedureFee);
				}
			}
			index++;
		}
		// 书刊类、彩盒类的装订工序费用
		if (CollectionUtils.isNotEmpty(offerOrder.getProductProcedure()) && (offerOrder.getOfferType() == OfferType.ALBUMBOOK || offerOrder.getOfferType() == OfferType.CARTONBOX))
		{
			for (OfferPartProcedure opp : offerOrder.getProductProcedure())
			{
				// 通过报价类型与工序id查找出工序设置
				OfferProcedure procedure = serviceFactory.getOfferSettingService().getProcedure(offerOrder.getOfferType(), opp.getProcedureId());
				// 每个机台都设置总贴数和总P数，所以拿去第一个即可
				OfferMachine offerMachine = lowerPriceMachineList.get(0);
				// 计算费用
				CustomCal procedureCal = OfferFormulaHelper.procedureFee(amount.intValue(), null, opp, procedure, offerMachine, offerOrder);
				double procedureFee = Double.valueOf(procedureCal.getFee());
				if(OfferType.ALBUMBOOK == offerOrder.getOfferType())
				{
					paperFeeCal3.append("【").append("装订工序").append("-" + opp.getProcedureName()).append("】");
				}else{
					paperFeeCal3.append("【").append("成品工序").append("-" + opp.getProcedureName()).append("】");
				}
				paperFeeCal3.append(procedureCal.getFormula() + "="+ procedureCal.getFee());
				paperFeeCal3.append("<br>");
				fee = fee + procedureFee;
			}
		}
		detailFee3.put("paperFeeCal", paperFeeCal3.toString());
		detailFee3.put("paperFee", OfferFormulaHelper.scale(fee));
		// 追加到列表中
		offerDetailFeeList.add(detailFee3);
		// 报价单的工序费
		offerOrderNew.setProcedureFee(fee);

		// ==================== 费用明细 - 其他费 ======================
		Map<String, Object> detailFee4 = Maps.newHashMap();
		// 工序费：根据选择的工序对应彩盒工序设置里的工序对应的计算公式，计算出每个部件的每道工序的费用
		detailFee4.put("paperFeeName", "其他费");
		detailFee4.put("paperFeeType", "FEE_4");
		// 印刷费 - 计算方案
		StringBuilder paperFeeCal4 = new StringBuilder();
		// 纸张费 - 金额
		fee = 0;
		// 自增长
		index = 1;
		// 【设计费】创意设计500
		String designType = "";
		if (null != offerOrder.getDesignType())
		{
			if ("SIMPLE".equals(offerOrder.getDesignType()))
			{
				designType = "板材设计";
			}
			else if ("NORMAL".equals(offerOrder.getDesignType()))
			{
				designType = "来样设计";
			}
			else if ("COMPLEX".equals(offerOrder.getDesignType()))
			{
				designType = "创意设计";
			}
			else
			{
				designType = "无需设计";
			}
		}
		Integer designFee = 0;
		if (null != offerOrder.getDesignFee())
		{
			designFee = offerOrder.getDesignFee().intValue();
		}
		paperFeeCal4.append("【设计费】").append(designType).append(designFee);
		paperFeeCal4.append("<br>");
		// 【包装费】包装价40/包装数400x印品数量5000=500
		double packingFee = 0;
		if (null != prePrint)
		{
			if (BoolValue.YES == prePrint.getPackingChk())
			{
				packingFee = OfferFormulaHelper.packingFee(amount.intValue(), prePrint.getPacking(), prePrint.getPackingPer());
				paperFeeCal4.append("【包装费】");
				paperFeeCal4.append("包装价").append(prePrint.getPacking()).append("/");
				paperFeeCal4.append("包装数").append(prePrint.getPackingPer()).append("*");
				paperFeeCal4.append("印品数量").append(amount).append("=");
				paperFeeCal4.append(packingFee);
				paperFeeCal4.append("<br>");
			}
		}
		fee = fee + designFee + packingFee;
		detailFee4.put("paperFeeCal", paperFeeCal4.toString());
		detailFee4.put("paperFee", OfferFormulaHelper.scale(fee));
		// 追加到列表中
		offerDetailFeeList.add(detailFee4);
		// 报价单的其他费用合计
		offerOrderNew.setOhterFee(fee);

		// ==================== 费用明细 - 运费 ======================
		Map<String, Object> detailFee5 = Maps.newHashMap();
		// 工序费：根据选择的工序对应彩盒工序设置里的工序对应的计算公式，计算出每个部件的每道工序的费用
		detailFee5.put("paperFeeName", "运费");
		detailFee5.put("paperFeeType", "FEE_5");
		// 印刷费 - 计算方案
		StringBuilder paperFeeCal5 = new StringBuilder();
		// 纸张费 - 金额
		fee = 0;
		// 自增长
		index = 1;
		if (null != prePrint)
		{
			// 运费
			if (BoolValue.YES == prePrint.getFreightChk())
			{
				paperFeeCal5.append("【运费】").append(prePrint.getFreight().intValue());
				paperFeeCal5.append("<br>");

				fee = prePrint.getFreight().intValue();
			}
		}
		detailFee5.put("paperFeeCal", paperFeeCal5.toString());
		detailFee5.put("paperFee", OfferFormulaHelper.scale(fee));
		// 追加到列表中
		offerDetailFeeList.add(detailFee5);
		// 报价单的运费
		offerOrderNew.setFreightFee(fee);
		// 总费用
		offerOrderNew.setCostMoney(offerOrderNew.getFreightFee() + offerOrderNew.getOhterFee() + offerOrderNew.getProcedureFee() + offerOrderNew.getPrintFee() + offerOrderNew.getPaperFee());
		// 单价
		offerOrderNew.setUnitPrice(offerOrderNew.getCostMoney() / amount);

		offerOrderNew.setOfferMachineDetailFeeList(offerDetailFeeList);
	}

	/**
	 * <pre>
	 * 拼版开料图
	 * </pre>
	 * @param offerOrderNew
	 * @param offerOrder
	 * @since 1.0, 2017年11月10日 下午4:34:27, think
	 */
	private void _quoteOpening(OfferOrder offerOrderNew, OfferOrder offerOrder)
	{
		// 拼版开料列表
		List<Map<String, Object>> offerOpeningList = Lists.newArrayList();
		// 内部核价 - 机台列表
		List<OfferMachine> offerMachineList = offerOrderNew.getOfferMachineList();

		for (OfferMachine offerMachine : offerMachineList)
		{
			if(offerMachine.getIsLowerPrice()  == null)
			{
				continue;
			}
			
			Map<String, Object> map = Maps.newHashMap();
			map.put("name", offerMachine.getBoxType() + "/" + offerMachine.getName());

			// 开数
			map.put("opening", offerMachine.getMaterialOpening());
			// 开数长（纸张尺寸长/开数=787/2=393）
			Integer openingLen = offerMachine.getPaperType().getLength() / offerMachine.getMaterialOpening();
			map.put("openingLen", openingLen);
			// 开数宽（纸张尺寸宽/开数=1092/2=546）
			Integer openingWidth = offerMachine.getPaperType().getWidth() / offerMachine.getMaterialOpening();
			map.put("openingWidth", openingWidth);

			// 拼版数
			map.put("sheetNum", offerMachine.getSheetNum());
			// 拼版长（开数长/拼版数=393/6=65）
			Integer sheetLen = openingLen / offerMachine.getSheetNum();
			map.put("sheetLen", sheetLen);
			// 拼版宽（开数长/拼版数=546/6=91）
			Integer sheetWidth = openingWidth / offerMachine.getSheetNum();
			map.put("sheetWidth", sheetWidth);

			// 纸张尺寸 - 长
			map.put("paperLen", offerMachine.getPaperType().getLength());
			// 纸张尺寸 - 宽
			map.put("paperWidth", offerMachine.getPaperType().getWidth());

			// 追加到列表中
			offerOpeningList.add(map);
		}

		offerOrderNew.setOfferOpeningList(offerOpeningList);
	}

	/**
	 * <pre>
	 * 找到最低报价得所有部件机台
	 * </pre>
	 * @param offerOrderNew
	 * @since 1.0, 2017年12月4日 上午11:39:58, think
	 */
	private List<OfferMachine> _filterLowerPriceMachineList(OfferOrder offerOrderNew)
	{
		List<OfferMachine> lowerPriceMachineList = Lists.newArrayList();
		List<OfferMachine> offerMachineList = offerOrderNew.getOfferMachineList();
		for (OfferMachine offerMachine : offerMachineList)
		{
			if (offerMachine.getIsLowerPrice() != null && offerMachine.getIsLowerPrice())
			{
				lowerPriceMachineList.add(offerMachine);
			}
		}

		if (lowerPriceMachineList.size() == 0)
		{
			throw new BusinessException("找不到最低报价机台");
		}

		return lowerPriceMachineList;
	}
}

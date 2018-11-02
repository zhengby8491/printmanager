/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月19日 下午7:36:12
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.offer;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;
import com.huayin.printmanager.persist.enumerate.BoolValue;

/**
 * <pre>
 * 报价模块 - 机台设置 - 开机费+印工计价
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月19日
 */
@Entity
@Table(name = "offer_machine_start_print")
public class OfferStartPrint extends BaseBasicTableEntity
{
	private static final long serialVersionUID = 5833639809811926058L;

	/**
	 * 报价-机台-主表ID
	 */
	private Long masterId;

	/**
	 * 报价-机台-主表
	 */
	@Transient
	private OfferMachine master;

	/**
	 * 加入专色计价
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue joinSpotColor = BoolValue.NO;

	/**
	 * 加入色令计价
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue joinReamColor = BoolValue.NO;

	/**
	 * 【普通】开机费
	 */
	private Integer startFee;

	/**
	 * 【普通】开机次数
	 */
	private Integer startSpeed;

	/**
	 * 【普通】印工费 - 次（以下）
	 */
	private Integer thousandSpeedBelow;

	/**
	 * 【普通】印工费 - 每千次/元（以下）
	 */
	private Integer thousandSpeedBelowMoney;

	/**
	 * 【普通】印工费 - 次（以上）
	 */
	private Integer thousandSpeedAbove;

	/**
	 * 【普通】印工费 - 每千次/元（以上）
	 */
	private Integer thousandSpeedAboveMoney;

	/**
	 * 【专色+普通】开机费
	 */
	private Integer spotColorStartFee;

	/**
	 * 【专色+普通】开机次数
	 */
	private Integer spotColorStartSpeed;

	/**
	 * 【专色+普通】印工费 - 次（以下）
	 */
	private Integer spotColorThousandSpeedBelow;

	/**
	 * 【专色+普通】印工费 - 每千次/元（以下）
	 */
	private Integer spotColorThousandSpeedBelowMoney;

	/**
	 * 【专色+普通】印工费 - 次（以上）
	 */
	private Integer spotColorThousandSpeedAbove;

	/**
	 * 【专色+普通】印工费 - 每千次/元（以上）
	 */
	private Integer spotColorThousandSpeedAboveMoney;

	/**
	 * 【专色】开机费
	 */
	private Integer spotColor2StartFee;

	/**
	 * 【专色】开机次数
	 */
	private Integer spotColor2StartSpeed;

	/**
	 * 【专色】印工费 - 次（以下）
	 */
	private Integer spotColor2ThousandSpeedBelow;

	/**
	 * 【专色】印工费 - 每千次/元（以下）
	 */
	private Integer spotColor2ThousandSpeedBelowMoney;

	/**
	 * 【专色】印工费 - 次（以上）
	 */
	private Integer spotColor2ThousandSpeedAbove;

	/**
	 * 【专色】印工费 - 每千次/元（以上）
	 */
	private Integer spotColor2ThousandSpeedAboveMoney;

	/**
	 * 【色令】色令印数开始值
	 */
	private Integer reamColorStartSpeed;

	/**
	 * 【色令】色令价
	 */
	private Integer reamColorMoney;

	/**
	 * 【色令】版费
	 */
	private Integer reamColorCopyFee;

	/**
	 * 内部核价 - 印工费
	 */
	@Transient
	private Integer money;

	/**
	 * 内部核价 - 最终结果
	 */
	@Transient
	private double price;

	/**
	 * 内部核价 - 数量
	 */
	@Transient
	private Integer amount;

	public Long getMasterId()
	{
		return masterId;
	}

	public void setMasterId(Long masterId)
	{
		this.masterId = masterId;
	}

	public OfferMachine getMaster()
	{
		return master;
	}

	public void setMaster(OfferMachine master)
	{
		this.master = master;
	}

	public BoolValue getJoinSpotColor()
	{
		return joinSpotColor;
	}

	public void setJoinSpotColor(BoolValue joinSpotColor)
	{
		this.joinSpotColor = joinSpotColor;
	}

	public BoolValue getJoinReamColor()
	{
		return joinReamColor;
	}

	public void setJoinReamColor(BoolValue joinReamColor)
	{
		this.joinReamColor = joinReamColor;
	}

	public Integer getStartFee()
	{
		return startFee;
	}

	public void setStartFee(Integer startFee)
	{
		this.startFee = startFee;
	}

	public Integer getStartSpeed()
	{
		return startSpeed;
	}

	public void setStartSpeed(Integer startSpeed)
	{
		this.startSpeed = startSpeed;
	}

	public Integer getThousandSpeedBelow()
	{
		return thousandSpeedBelow;
	}

	public void setThousandSpeedBelow(Integer thousandSpeedBelow)
	{
		this.thousandSpeedBelow = thousandSpeedBelow;
	}

	public Integer getThousandSpeedBelowMoney()
	{
		return thousandSpeedBelowMoney;
	}

	public void setThousandSpeedBelowMoney(Integer thousandSpeedBelowMoney)
	{
		this.thousandSpeedBelowMoney = thousandSpeedBelowMoney;
	}

	public Integer getThousandSpeedAbove()
	{
		return thousandSpeedAbove;
	}

	public void setThousandSpeedAbove(Integer thousandSpeedAbove)
	{
		this.thousandSpeedAbove = thousandSpeedAbove;
	}

	public Integer getThousandSpeedAboveMoney()
	{
		return thousandSpeedAboveMoney;
	}

	public void setThousandSpeedAboveMoney(Integer thousandSpeedAboveMoney)
	{
		this.thousandSpeedAboveMoney = thousandSpeedAboveMoney;
	}

	public Integer getSpotColorStartFee()
	{
		return spotColorStartFee;
	}

	public void setSpotColorStartFee(Integer spotColorStartFee)
	{
		this.spotColorStartFee = spotColorStartFee;
	}

	public Integer getSpotColorStartSpeed()
	{
		return spotColorStartSpeed;
	}

	public void setSpotColorStartSpeed(Integer spotColorStartSpeed)
	{
		this.spotColorStartSpeed = spotColorStartSpeed;
	}

	public Integer getSpotColorThousandSpeedBelow()
	{
		return spotColorThousandSpeedBelow;
	}

	public void setSpotColorThousandSpeedBelow(Integer spotColorThousandSpeedBelow)
	{
		this.spotColorThousandSpeedBelow = spotColorThousandSpeedBelow;
	}

	public Integer getSpotColorThousandSpeedAbove()
	{
		return spotColorThousandSpeedAbove;
	}

	public void setSpotColorThousandSpeedAbove(Integer spotColorThousandSpeedAbove)
	{
		this.spotColorThousandSpeedAbove = spotColorThousandSpeedAbove;
	}

	public Integer getSpotColorThousandSpeedBelowMoney()
	{
		return spotColorThousandSpeedBelowMoney;
	}

	public void setSpotColorThousandSpeedBelowMoney(Integer spotColorThousandSpeedBelowMoney)
	{
		this.spotColorThousandSpeedBelowMoney = spotColorThousandSpeedBelowMoney;
	}

	public Integer getSpotColorThousandSpeedAboveMoney()
	{
		return spotColorThousandSpeedAboveMoney;
	}

	public void setSpotColorThousandSpeedAboveMoney(Integer spotColorThousandSpeedAboveMoney)
	{
		this.spotColorThousandSpeedAboveMoney = spotColorThousandSpeedAboveMoney;
	}

	public Integer getSpotColor2StartFee()
	{
		return spotColor2StartFee;
	}

	public void setSpotColor2StartFee(Integer spotColor2StartFee)
	{
		this.spotColor2StartFee = spotColor2StartFee;
	}

	public Integer getSpotColor2StartSpeed()
	{
		return spotColor2StartSpeed;
	}

	public void setSpotColor2StartSpeed(Integer spotColor2StartSpeed)
	{
		this.spotColor2StartSpeed = spotColor2StartSpeed;
	}

	public Integer getSpotColor2ThousandSpeedBelow()
	{
		return spotColor2ThousandSpeedBelow;
	}

	public void setSpotColor2ThousandSpeedBelow(Integer spotColor2ThousandSpeedBelow)
	{
		this.spotColor2ThousandSpeedBelow = spotColor2ThousandSpeedBelow;
	}

	public Integer getSpotColor2ThousandSpeedBelowMoney()
	{
		return spotColor2ThousandSpeedBelowMoney;
	}

	public void setSpotColor2ThousandSpeedBelowMoney(Integer spotColor2ThousandSpeedBelowMoney)
	{
		this.spotColor2ThousandSpeedBelowMoney = spotColor2ThousandSpeedBelowMoney;
	}

	public Integer getSpotColor2ThousandSpeedAbove()
	{
		return spotColor2ThousandSpeedAbove;
	}

	public void setSpotColor2ThousandSpeedAbove(Integer spotColor2ThousandSpeedAbove)
	{
		this.spotColor2ThousandSpeedAbove = spotColor2ThousandSpeedAbove;
	}

	public Integer getSpotColor2ThousandSpeedAboveMoney()
	{
		return spotColor2ThousandSpeedAboveMoney;
	}

	public void setSpotColor2ThousandSpeedAboveMoney(Integer spotColor2ThousandSpeedAboveMoney)
	{
		this.spotColor2ThousandSpeedAboveMoney = spotColor2ThousandSpeedAboveMoney;
	}

	public Integer getReamColorStartSpeed()
	{
		return reamColorStartSpeed;
	}

	public void setReamColorStartSpeed(Integer reamColorStartSpeed)
	{
		this.reamColorStartSpeed = reamColorStartSpeed;
	}

	public Integer getReamColorMoney()
	{
		return reamColorMoney;
	}

	public void setReamColorMoney(Integer reamColorMoney)
	{
		this.reamColorMoney = reamColorMoney;
	}

	public Integer getReamColorCopyFee()
	{
		return reamColorCopyFee;
	}

	public void setReamColorCopyFee(Integer reamColorCopyFee)
	{
		this.reamColorCopyFee = reamColorCopyFee;
	}

	public Integer getMoney()
	{
		return money;
	}

	public void setMoney(Integer money)
	{
		this.money = money;
	}

	public double getPrice()
	{
		return price;
	}

	public void setPrice(double price)
	{
		this.price = price;
	}

	public Integer getAmount()
	{
		return amount;
	}

	public void setAmount(Integer amount)
	{
		this.amount = amount;
	}
}

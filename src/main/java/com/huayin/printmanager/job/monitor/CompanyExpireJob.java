package com.huayin.printmanager.job.monitor;

import com.huayin.printmanager.job.BaseJob;

/**
 * <pre>
 * 公司到期提醒任务
 * </pre>
 * @author chenjian
 * @version 1.0, 2012-10-11
 */
public class CompanyExpireJob extends BaseJob
{
	@Override
	public void exec(boolean isTimeout)
	{
		serviceFactory.getCompanyService().expireNotify();
		serviceFactory.getCompanyService().experienceNotify();
	}

}

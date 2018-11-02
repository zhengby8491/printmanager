package com.huayin.printmanager.job.sms;

import com.huayin.printmanager.job.BaseJob;

/**
 * <pre>
 * 发送短信任务
 * </pre>
 * @author chenjian
 * @version 1.0, 2012-10-11
 */
public class SmsSendJob extends BaseJob
{
	@Override
	public void exec(boolean isTimeout)
	{
		serviceFactory.getSmsSendService().sendJobBatch();
	}

}

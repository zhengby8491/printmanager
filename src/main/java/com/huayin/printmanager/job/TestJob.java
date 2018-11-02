package com.huayin.printmanager.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestJob extends BaseJob
{

	protected final Log log = LogFactory.getLog(getClass());
	@Override
	public void exec(boolean isTimeout)
	{
		log.info("test job exec start");
		try{
			log.info("work 5 second");
			Thread.sleep(5000);
		}catch(Exception e)
		{
			
		}
		log.info("test job exec end");
	}

}

/**
 * 
 */
package com.cf.code.job;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.StatefulJob;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 * 
 */
@SuppressWarnings("deprecation")
public abstract class AbstractJob extends QuartzJobBean implements StatefulJob{

	protected final Logger log = LogManager.getLogger(this.getClass());
	
	private static final String APPLICATION_CONTEXT_KEY = "ApplicationContextKey";
	
	private ApplicationContext appcontext = null;
	
	public <T> T getBean(JobExecutionContext context,String name, Class<T> requiredType){
		try {
			if(appcontext == null){
				appcontext = (ApplicationContext)context.getScheduler().getContext().get(APPLICATION_CONTEXT_KEY);
			}
			return appcontext.getBean(name, requiredType);
		} catch (SchedulerException e) {
			log.error("在计划任务中获取bean异常", e);
		}
		return null;
	}
	
}

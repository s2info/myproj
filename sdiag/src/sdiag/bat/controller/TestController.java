package sdiag.bat.controller;

import java.util.List;
import java.util.Properties;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;


public class TestController extends QuartzJobBean{
	
		protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {

			try{
				/** batch logic start */
				System.out.println("bath logic start ==========================");
				/** batch logic end */
			} catch(Exception e){
				e.printStackTrace();
			}
		}

	}

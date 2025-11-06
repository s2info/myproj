package sdiag.bat.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import sdiag.bat.service.AutoSanctService;
import sdiag.com.service.CommonService;
import sdiag.sanct.service.SanctionService;

public class MailSendBatchController extends QuartzJobBean {
	/*private AutoSanctService autoSanctService;
	public void setAutoSanctService(AutoSanctService autoSanctService){
		this.autoSanctService = autoSanctService;
	}
	private SanctionService sanctionService;
	public void setSanctionService(SanctionService sanctionService) {
		this.sanctionService = sanctionService;
	}*/
	private CommonService commonService;
	public void setCommonService(CommonService commonService){
		this.commonService = commonService;
	}
	
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		//메일 전송 배치 실행 
		try{
			long currentTime = System.currentTimeMillis();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date resultDate = new Date(currentTime);
			System.out.println(sdf.format(resultDate) + "][***************** MAIL Send Execute ***********************");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}

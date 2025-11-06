package sdiag.bat.controller;

import java.lang.Object;
import java.lang.Class;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.antlr.grammar.v3.ANTLRParser.finallyClause_return;

import sdiag.com.service.CommonService;
import sdiag.com.service.MailSendLogVO;
import sdiag.com.service.SdiagProperties;
import sdiag.bat.service.AutoSanctService;

public class MailProcess {
	
	/**
	 * 메일전송 UTIL
	 * @param subject : 제목
	 * @param messageBody : 내용
	 * @param toEMail : 수신자
	 * @return
	 * @throws MessagingException
	 */
	public boolean SendMail(String emp_no, String subject, String messageBody, String toEMail, AutoSanctService autoSanctService) throws Exception{
		
		MailSendLogVO mailLog = new MailSendLogVO();		
		
		mailLog.setEmp_no(emp_no);
		mailLog.setTo_email(toEMail);
		mailLog.setBody_email(messageBody);
		mailLog.setSubj_email(subject);
		
		
		return SendMail(mailLog, autoSanctService);
	}
	public static boolean SendMail(MailSendLogVO mailLog, AutoSanctService autoSanctService) throws Exception{
		
		String resultMsg="";
		//TEST 용 추후 반드시 삭제해야함
		//String toEMail = "91117654@ktfriend.com"; //mailLog.getTo_email();
		String toEMail = "91117655@ktfriend.com"; 	

		String subject = mailLog.getSubj_email();
		String messageBody = mailLog.getBody_email();
		
		
		try{
			String smpt_host = SdiagProperties.getProperty("Globals.SMPT_HOST");
			String smpt_port = SdiagProperties.getProperty("Globals.SMTP_PORT");
			String from_address = SdiagProperties.getProperty("Globals.FROM_MAIL_ADDRESS");			
			
//			String smpt_host = "10.225.148.195";
//			String smpt_port = "25";
//			String from_address = "sldm.sec@kt.com";			
			
			Properties props = new Properties();
			props.put("mail.smtp.host", smpt_host);
			props.put("mail.smtp.protocol", "smtp");
			props.put("mail.smtp.port", smpt_port);
			props.put("mail.smtp.auth", "false");
			props.put("mail.smtp.starttls.enable", "true");
			
			//System.out.println("MailServer:========host:"+smpt_host + ":port:"+smpt_port);
			
			Session session = Session.getDefaultInstance(props);
			
			MimeMessage msg = new MimeMessage(session);
			
			msg.setFrom(new InternetAddress(from_address));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEMail));
			msg.setSubject(subject);
			msg.setText(messageBody);
			msg.setSentDate(new Date());
		
			Transport.send(msg);
			resultMsg = "Mail Send OK";
			
			
			mailLog.setResult_message(resultMsg);
			mailLog.setResult(true);
	
			return true;
		}catch(Exception e){
			e.printStackTrace();
			resultMsg = e.getMessage();
		
			mailLog.setResult_message(resultMsg);
			mailLog.setResult(false);
		
			return false;
		}finally{
			autoSanctService.setInsertMailLog(mailLog);
			//로그 저장
			//날짜/수신자/제목/내용/결과
		}
	}
}

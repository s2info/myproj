package sdiag.util;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.antlr.grammar.v3.ANTLRParser.finallyClause_return;

import sdiag.util.MailUtil;
import sdiag.com.service.MimeBodyPartVO;
import sdiag.com.service.CommonService;
import sdiag.com.service.MailInfoVO;
import sdiag.com.service.MailSendLogVO;
import sdiag.com.service.SdiagProperties;


public class MailUtil {
	//public static final String SMTP_HOST = "14.63.245.51";
	//public static final int SMTP_PORT = 25;
	//public static final String FROM_MAIL_ADDRESS = "sldm.sec@kt.com";
	//public static String image_path = "";
	/**
	 * 메일전송 UTIL
	 * @param subject : 제목
	 * @param messageBody : 내용
	 * @param toEMail : 수신자
	 * @return
	 * @throws MessagingException
	 */
	public static boolean SendMail(String emp_no, String subject, String messageBody, String toEMail, CommonService comService, List<MimeBodyPartVO> imageList) throws Exception{
		MailSendLogVO mailLog = new MailSendLogVO();
		mailLog.setEmp_no(emp_no);
		mailLog.setTo_email(toEMail);
		mailLog.setBody_email(messageBody);
		mailLog.setSubj_email(subject);
		mailLog.setImageArray(imageList);
		
		return SendMail(mailLog, comService);
	}
	public static boolean SendMail(MailSendLogVO mailLog, CommonService comService ) throws Exception{
		String resultMsg="";
		
		//String toEMail = "91117655@ktfriend.com"; //mailLog.getTo_email();
		String toEMail = mailLog.getTo_email();
		String subject = mailLog.getSubj_email();
		String messageBody = mailLog.getBody_email();
		try{
			
			String smpt_host = SdiagProperties.getProperty("Globals.SMPT_HOST");
			String smpt_port = SdiagProperties.getProperty("Globals.SMTP_PORT");
			String from_address = SdiagProperties.getProperty("Globals.FROM_MAIL_ADDRESS");
			String image_path = SdiagProperties.getProperty("image_path");
			Properties props = new Properties();
			props.put("mail.smtp.host", smpt_host);
			props.put("mail.smtp.protocol", "smtp");
			props.put("mail.smtp.port", smpt_port);
			props.put("mail.smtp.auth", "false");
			props.put("mail.smtp.starttls.enable", "true");
			
			Session session = Session.getDefaultInstance(props);
			
			Message msg = new MimeMessage(session);
			
			msg.setFrom(new InternetAddress(from_address));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEMail));
			msg.setSubject(subject);
			//msg.setText(messageBody);
			//msg.setContent(messageBody, "text/html; charset=utf-8");
			msg.setSentDate(new Date());
			
			MimeMultipart multipart = new MimeMultipart("related");
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(messageBody, "text/html;charset=utf-8");
			multipart.addBodyPart(messageBodyPart);
		
			//첨부이미지 적용
			if(mailLog.getImageArray() != null){
				for(MimeBodyPartVO bodyPartvo:mailLog.getImageArray()){
					MimeBodyPart msgBodyPart = new MimeBodyPart();
			        DataSource fds = new FileDataSource(image_path +  bodyPartvo.getImageName());
			        msgBodyPart.setDataHandler(new DataHandler(fds));
			        msgBodyPart.setHeader("Content-ID", bodyPartvo.getContentID());
			        msgBodyPart.setFileName(bodyPartvo.getFileName());
			        multipart.addBodyPart(msgBodyPart);
				}
			}
			
			
			msg.setContent(multipart);
			
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
			comService.setInsertMailLog(mailLog);
			//로그 저장
			//날짜/수신자/제목/내용/결과
		}
	}
	
	/**
	 * 메일 정보 생성 
	 * @param mode
	 * @param empno
	 * @param comService
	 * @return
	 * @throws Exception
	 */
	public static MailSendLogVO getMailMessage(String empno, String empnm, String email, String callInfo ) throws Exception{
		MailSendLogVO mailLog = new MailSendLogVO();
		String subj_email = "[임직원 보안수준진단] 일별 진단 보고서 안내";
		String mailTitle = "일별 진단 보고서";
		String userName = empnm;
		
		StringBuffer mailBody = new StringBuffer();
		
		mailBody.append(String.format("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>"
				+ "<html xmlns='http://www.w3.org/1999/xhtml'>"
				+ "<head>"
				+ "<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />"
				+ "<title>임직원수준진단</title>"
				+ "</head>"
				+ "<body style='padding:0;margin:0 auto;'>"));
		mailBody.append(String.format("<table cellpadding='0' cellspacing='0' style='width:700px;background:#efefef;color:#676767;'>"));
		mailBody.append(String.format("<tr>"
				+ "<td style='padding:10px;'><img src='http://sldm.kt.com/img/top_logo.png'  alt='KT임직원 보안수준진단' title='KT임직원 보안수준진단' />"
				+ "<span style='float:right;font:bold 12px 돋움;padding-top:20px'>%s</span>"
				+ "</td>"
				+ "</tr>", mailTitle));
		mailBody.append(String.format("<tr>"
				+ "<td style='padding:30px 20px;background:#ffffff;font:normal 12px 돋움;border-top:solid 2px #C30;line-height:25px;'>"
				+ "<strong>%s님 안녕하세요.</strong><br/><br/>"
				+ "KT임직원 보안수준진단 일별보고서 입니다.<br>"
				+ "<a href='http://sldm.kt.com/loginPage.do?returl=/report/sheetStar.do'>진단보고서 바로가기</a>를 클릭하여 진단보고서 페이지로 이동할 수 있습니다.<br><br>"
				+ "<a href='http://sldm.kt.com/loginPage.do?returl=/report/sheetStar.do'>진단보고서 바로가기</a>"
				+ "</td>"
				+ "</tr>"
				, userName));
		
		mailBody.append(String.format("<tr>"
				+ "<td style='padding:15px 0px 0px 20px;text-align:center;font:normal 11px 돋움;'>"
				+ " * 본 메일은 회신할 수 없는 발신전용 메일입니다.<br /><br />"
				+ " * 정책 및 소명관련 문의 : %s "
				+ "<p style='color:#b8b8b8;'>Copyright ⓒ <span style='font:bold 11px 돋움;color:#e71e25'>KT</span> All right reserverd.</p><br />"
				+ "</td>"
				+ "</tr>  "
				+ "</table>"
				+ "</body>"
				+ "</html>", callInfo));
				
		mailLog.setEmp_no(empno);
		mailLog.setSubj_email(subj_email);
		mailLog.setBody_email(mailBody.toString());
		mailLog.setTo_email(email);
		
		return mailLog;
	}
	public static MailSendLogVO getMailMessage(String mode, MailInfoVO mailInfoVO, String empno, CommonService comService) throws Exception{
		MailSendLogVO mailLog = new MailSendLogVO();
		
		HashMap<String, Object> info = comService.getPolIdxInfo(mailInfoVO);
		
		String subj_email = "";
		String mailTitle = "";
		String mailNotice = "";
		String userName = info.get("emp_nm").toString();
		if(mode.equals("P")){
			//PC 지키미
			subj_email = "[임직원보안수준진단] PC지키미 실행 처리 안내";
			mailTitle = String.format("[%s] PC지키미 실행", info.get("sec_pol_desc"));
			mailNotice = "[임직원 보안수준진단] 진단결과 진단점수가 기준치 이하로 산정되어  PC지키미가 실행되었습니다. <br/><br/>";
			mailNotice += "<strong>감사합니다.</strong>";
		}else if(mode.equals("S")){
			//제재 조치
			
			subj_email = "[임직원보안수준진단] 제재처리 안내";
			mailTitle = String.format("[%s] 제재처리", info.get("sec_pol_desc"));
			mailNotice = String.format("[임직원 보안수준진단] 진단결과 진단점수가 기준치 이하로 산정되어 사용자 PC에 대하여 [%s]조치 되었습니다.<br/>", info.get("sanct_type"));
			mailNotice += "결재시스템[BPM]에 해당정책에 대한 소명정보가 전송되었습니다.<br/>";
			mailNotice += "[BPM]사이트에 접속하여 소명 요청에 대한 처리를 진행하여 주십시오.<br/><br/>";
			mailNotice += "<strong>감사합니다.</strong>";
		}else{
			//소명조치
			subj_email = "[임직원보안수준진단] 소명요청 안내";
			mailTitle = String.format("[%s] 소명요청", info.get("sec_pol_desc"));
			mailNotice = "[임직원 보안수준진단] 진단결과 진단점수가 기준치 이하로 산정되어 해당 정책에 대한 소명을 해야 합니다.<br/>";
			mailNotice += "소명요청정보는 결재시스템[BPM]에 요청정보가 전송되었습니다.<br/>";
			mailNotice += "[BPM]사이트에 접속하여 소명 요청에 대한 처리를 진행하여 주십시오.<br/><br/>";
			mailNotice += "<strong>감사합니다.</strong>";
		}
		
		StringBuffer mailBody = new StringBuffer();
		
		mailBody.append(String.format("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>"
				+ "<html xmlns='http://www.w3.org/1999/xhtml'>"
				+ "<head>"
				+ "<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />"
				+ "<title>임직원수준진단</title>"
				+ "</head>"
				+ "<body style='padding:0;margin:0 auto;'>"));
		mailBody.append(String.format("<table cellpadding='0' cellspacing='0' style='width:700px;background:#efefef;color:#676767;'>"));
		mailBody.append(String.format("<tr>"
				+ "<td style='padding:10px;'><img src='http://sldm.kt.com/img/top_logo.png'  alt='KT임직원 보안수준진단' title='KT임직원 보안수준진단' />"
				+ "<span style='float:right;font:bold 12px 돋움;padding-top:20px'>%s</span>"
				+ "</td>"
				+ "</tr>", mailTitle));
		mailBody.append(String.format("<tr>"
				+ "<td style='padding:30px 20px;background:#ffffff;font:normal 12px 돋움;border-top:solid 2px #C30;line-height:25px;'>"
				+ "<strong>%s님 안녕하세요.</strong><br/><br/>"
				+ "%s"
				+ "</td>"
				+ "</tr>"
				, userName
				, mailNotice));
		mailBody.append(String.format("<tr>"
				+ "<td style='padding:30px 20px 80px 20px;background:#ffffff;font:normal 12px 돋움;border-collapse:collapse;'>"
				+ "<table cellpadding='0' cellspacing='0' style='width:680px;border:solid 1px #cfcfcf;'>"
				+ "<tr>"
				+ "<td colspan='2' style='color:#e71e25;font:bold 12px 돋움;text-align:center;padding:10px;background:#efefef;border-bottom:solid 1px #cfcfcf;'>진단상세정보</td>"
				+ "</tr>"
				+ "<tr style=''>"
				+ "<th style='padding:10px;border-right:solid 1px #cfcfcf;border-bottom:solid 1px #cfcfcf;background:#f7f7f7;'>진단일</th>"
				+ "<td style='padding:10px;border-bottom:solid 1px #cfcfcf;'>%s</td>"
				+ "</tr>"
				+ "<tr style=''>"
				+ "<th style='padding:10px;border-right:solid 1px #cfcfcf;border-bottom:solid 1px #cfcfcf;background:#f7f7f7;'>정책명</th>"
				+ "<td style='padding:10px;border-bottom:solid 1px #cfcfcf;'>%s</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<th style='padding:10px;border-right:solid 1px #cfcfcf;border-bottom:solid 1px #cfcfcf;background:#f7f7f7;'>정책구분</th>"
				+ "<td style='padding:10px;border-bottom:solid 1px #cfcfcf;'>%s</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<th style='padding:10px;border-right:solid 1px #cfcfcf;border-bottom:solid 1px #cfcfcf;background:#f7f7f7;'>진단결과</th>"
				+ "<td style='padding:10px;border-bottom:solid 1px #cfcfcf;'>건수 : <strong>%s건</strong>, 점수 : <strong>%s점</strong></td>"
				+ "</tr>"
				+ "<tr>"
				+ "<th style='padding:10px;border-right:solid 1px #cfcfcf;background:#f7f7f7;'>정책 및 조치방안<br />설명</th>"
				+ "<td style='padding:10px;'>%s</td>"
				+ "</tr> "
				+ "</table>"
				+ "</td>"
				+ "</tr> "
				, info.get("reg_date").toString()
				, info.get("sec_pol_desc").toString()
				, String.format("%s - %s - %s", info.get("diag1").toString(), info.get("diag2").toString(), info.get("sec_pol_desc").toString())
				, info.get("count").toString()
				, info.get("score").toString()
				, info.get("sec_pol_notice").toString().replaceAll("(\r\n|\n|\r)", "<br />")));
		mailBody.append(String.format("<tr>"
				+ "<td style='padding:15px 0px 0px 20px;text-align:center;font:normal 11px 돋움;'>"
				+ " * 본 메일은 회신할 수 없는 발신전용 메일입니다.<br /><br />"
				+ " * 정책 및 소명관련 문의 : %s "
				+ "<p style='color:#b8b8b8;'>Copyright ⓒ <span style='font:bold 11px 돋움;color:#e71e25'>KT</span> All right reserverd.</p><br />"
				+ "</td>"
				+ "</tr>  "
				+ "</table>"
				+ "</body>"
				+ "</html>", info.get("call_addr")));
				
		mailLog.setEmp_no(empno);
		mailLog.setSubj_email(subj_email);
		mailLog.setBody_email(mailBody.toString());
		mailLog.setTo_email(info.get("email").toString());
		
		return mailLog;
	}
}

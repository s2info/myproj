package sdiag.bat.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import javax.annotation.Resource;

//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;

import sdiag.bat.service.AutoSanctService;
import sdiag.com.service.BPMInfoVO;
import sdiag.com.service.CodeInfoVO;
//import sdiag.com.service.CodeInfoVO;
import sdiag.com.service.CommonService;
import sdiag.com.service.MailInfoVO;
import sdiag.com.service.MailSendLogVO;
import sdiag.sanct.service.SanctionService;
import sdiag.member.service.MemberVO;
import sdiag.member.service.SdiagMemberService;
import sdiag.util.BPMUtil;
import sdiag.util.CommonUtil;
import sdiag.util.MailUtil;
import sdiag.util.MajrCodeInfo;
import egovframework.rte.psl.dataaccess.util.EgovMap;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class AutoSanctController extends QuartzJobBean {

	private AutoSanctService autoSanctService;
	private SanctionService sanctionService;
	private CommonService commonService;

	public void setAutoSanctService(AutoSanctService autoSanctService) {
		this.autoSanctService = autoSanctService;
	}

	public void setSanctionService(SanctionService sanctionService) {
		this.sanctionService = sanctionService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {

		String mode = "";
		/**
		 * 소명조건 : appr_cate -> 0(자동) & appr_cate -> 1 and sanc_cate -> 0 1) 제재
		 * -> 자동, 소명 -> 자동 2) 제재 -> 수동, 소명 -> 자동 3) 제재 -> 자동, 소명 -> 수동
		 * 
		 * 제재조건 : sanc_cate -> 0 1) 제재 자동
		 */
		List<HashMap<String, String>> resultMap = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> logMap = new HashMap<String, String>();
		logMap.put("log_type", "A");
		try {

			CodeInfoVO codeInfo = new CodeInfoVO();
			codeInfo.setMajr_code(MajrCodeInfo.ExecuteCode); // EXC
			codeInfo.setMinr_code("AGT");
			CodeInfoVO agtInfo = autoSanctService.getCodeInfoForOne(codeInfo);
			codeInfo.setMinr_code("BPM");
			CodeInfoVO bpmInfo = autoSanctService.getCodeInfoForOne(codeInfo);
			codeInfo.setMinr_code("MAL");
			CodeInfoVO malInfo = autoSanctService.getCodeInfoForOne(codeInfo);

			codeInfo.setMajr_code(MajrCodeInfo.UsedCode);
			codeInfo.setMinr_code("BPM");
			CodeInfoVO isbpmInfo = autoSanctService.getCodeInfoForOne(codeInfo);
			codeInfo.setMinr_code("MAL");
			CodeInfoVO ismalInfo = autoSanctService.getCodeInfoForOne(codeInfo);

			boolean isAGT = agtInfo.getCode_desc().equals("Y") ? true : false;
			boolean isBPM = bpmInfo.getCode_desc().equals("Y") ? true : false;
			boolean isMail = malInfo.getCode_desc().equals("Y") ? true : false;
			boolean isBPMCode = isbpmInfo.getCode_desc().equals("Y") ? true
					: false;
			boolean isMailCode = ismalInfo.getCode_desc().equals("Y") ? true
					: false;

			int execCount = 0;
			if (isAGT) {
				List<EgovMap> resultList = autoSanctService.selectSanctList();
				execCount = resultList.size();
				mode = "A";
				for (EgovMap row : resultList) {
					HashMap<String, String> result = new HashMap<String, String>();

					HashMap<String, Object> param = new HashMap<String, Object>();

					String apprid = "";

					result.put("PTYPE", "소명요청");
					result.put("EMPNO", row.get("empno").toString());

					if (row.get("apprid") == null) {
						apprid = CommonUtil.CreateUUID();

						param.put("apprid", apprid);
						param.put("empno", row.get("empno"));
						param.put("apprlinecode", row.get("apprlinecode"));
						param.put("rgdtdate", row.get("idxrgdtdate"));
						param.put("mac", row.get("mac"));
						param.put("ip", row.get("ipaddress"));
						param.put("polidxid", row.get("polidxid"));
						param.put("mode", mode); // A:소명처리 S:제재

						/**
						 * BPM처리
						 * 
						 */
						try {
							boolean bpm_ret = false;
							boolean appr_ret = false;
							boolean mail_ret = false;
							boolean appr_inst = false;
							if (isBPM) {
								// BPM 처리성공시 소명정보 등록
								/**
								 * 소명정보 INSERT
								 */
								appr_inst = sanctionService.setApprInfoInsert(param);
								/**
								 * 소명코드 업데이트
								 */
								appr_ret = sanctionService.setApprInfoRegister(param);
								if (appr_ret && appr_inst) {
									bpm_ret = isBPMCode ? BPMUtil.BPMSend(row.get("empno").toString(), (String) row.get("apprlinecode"), apprid, commonService) : true;
									if (bpm_ret) {
										result.put("RESULT", "처리완료");
									} else {
										/**
										 * BPM 처리 실패시 소명정보 삭제
										 */
										boolean rollback = sanctionService.setApprInfoRollback(param);
										result.put("RESULT", "BPM실패");
									}
								} else {
									result.put("RESULT", "소명실패");
								}

							} else {
								result.put("RESULT", "BPM차단");
							}

							result.put("BPM", isBPM ? bpm_ret ? "전송완료" : "전송실패"
									: "전송차단");
							/**
							 * 메일전송 메일은 BPM 전송 완료 + 소명처리 완료시 전송함
							 */
							if (isMail) {
								if (bpm_ret && appr_ret) {
									MailInfoVO InfoVO = new MailInfoVO();
									InfoVO.setIdx_rgdt_date(param.get("rgdtdate").toString());
									InfoVO.setEmp_no(param.get("empno").toString());
									InfoVO.setIp(param.get("ip").toString());
									InfoVO.setMac(param.get("mac").toString());
									InfoVO.setPol_idx_id(param.get("polidxid").toString());
									InfoVO.setSancttype("");

									MailSendLogVO mailLog = MailUtil.getMailMessage(mode, InfoVO, param.get("empno").toString(),commonService);
									mail_ret = isMailCode ? MailUtil.SendMail(
											mailLog, commonService) : true;
									result.put("MAIL", mail_ret ? "전송완료"
											: "전송실패");
								} else {
									result.put("MAIL", "처리실패");
								}
							} else {
								result.put("MAIL", "전송차단");
							}

						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							/**
							 * BPM 및 소명 로그저장
							 */
							if (isBPM) {
								BPMInfoVO bpmLog = new BPMInfoVO();
								bpmLog.setAppr_id(apprid);
								bpmLog.setBpm_ret_code("-999");
								bpmLog.setBpm_id("");
								bpmLog.setStatus_type("L");
								bpmLog.setAppr_commment(String
										.format("BPM 및 소명 처리 : 결재구분 : %s, 진단날짜 : %s, MAC : %s, IP : %s, POL_IDX_ID : %s, BPM결과 : %s, 소명처리결과 : %s, 메일처리결과 : %s",
												param.get("apprlinecode"),
												param.get("rgdtdate"),
												param.get("mac"),
												param.get("ip"),
												param.get("polidxid"),
												result.get("BPM"),
												result.get("RESULT"),
												result.get("MAIL")));
								bpmLog.setEmp_no(row.get("empno").toString());
								autoSanctService.InsertBPMLog(bpmLog);
							}
						}
					} else {
						// System.out.println("RESULT:" + "이미 소명처리 상태임");
						result.put("RESULT", "소명상태");
					}
					resultMap.add(result);
					// logMap.put("emp_no", result.get("EMPNO"));
					// logMap.put("comment", String.format(format, args));
				}

				/**
				 * ============================================================
				 * ==============
				 **/
				// 제재처리
				// 제재 Pass 사이트 및 아이피 조회
				codeInfo.setMajr_code(MajrCodeInfo.SanctPassIP);
				codeInfo.setMinr_code("1");
				CodeInfoVO passInfo = autoSanctService
						.getCodeInfoForOne(codeInfo);
				/**
				 * 제재조치 시간 조회
				 */
				codeInfo.setMajr_code(MajrCodeInfo.SanctScreenBlockTime);
				codeInfo.setMinr_code("SEC");
				CodeInfoVO screenblockInfo = autoSanctService
						.getCodeInfoForOne(codeInfo);

				mode = "S";

				for (EgovMap col : resultList) {
					HashMap<String, String> result = new HashMap<String, String>();
					HashMap<String, Object> param = new HashMap<String, Object>();

					String sanctid = "";

					if (col.get("sancid") == null) {

						sanctid = CommonUtil.CreateUUID();
					} else {
						sanctid = (String) col.get("sancid");
					}

					result.put("PTYPE", "제재처리");
					result.put("EMPNO", col.get("empno").toString());
					result.put("BPM", "해당없음");

					param.put("sanctid", sanctid);
					param.put("empno", col.get("empno"));
					param.put("sancttype", col.get("blocktype"));
					param.put("pcgactparam", "");
					param.put("passid", passInfo.getCode_desc());
					param.put("rgdtdate", col.get("idxrgdtdate"));
					param.put("mac", col.get("mac"));
					param.put("ip", col.get("ipaddress"));
					param.put("polidxid", col.get("polidxid"));
					param.put("mode", mode);
					param.put("screenblock", screenblockInfo.getCode_desc());
					String apprcode = sanctionService.getApprCodeInfo(param);
					if (apprcode.equals("")) {
						result.put("RESULT", "소명없음");
						result.put("MAIL", isBPM ? "처리보류" : "전송차단");
						resultMap.add(result);
						continue;
					}
					if (sanctid.equals("")) {
						sanctid = apprcode;
					}
					param.put("sanctid", sanctid);
					boolean scnc_ret = sanctionService.setSanctInfoRegister(
							param, mode);
					result.put("RESULT", scnc_ret ? "처리완료" : "처리실패");
					/**
					 * 메일전송
					 */
					boolean mail_ret = false;
					if (isMail) {
						if (scnc_ret) {
							MailInfoVO InfoVO = new MailInfoVO();
							InfoVO.setIdx_rgdt_date(param.get("rgdtdate")
									.toString());
							InfoVO.setEmp_no(param.get("empno").toString());
							InfoVO.setIp(param.get("ip").toString());
							InfoVO.setMac(param.get("mac").toString());
							InfoVO.setPol_idx_id(param.get("polidxid")
									.toString());
							InfoVO.setSancttype("");

							MailSendLogVO mailLog = MailUtil.getMailMessage(
									mode, InfoVO,
									param.get("empno").toString(),
									commonService);
							mail_ret = MailUtil
									.SendMail(mailLog, commonService);
							result.put("MAIL", mail_ret ? "전송완료" : "전송실패");

						} else {
							result.put("MAIL", "처리실패");
						}
					} else {
						result.put("MAIL", "전송차단");
					}
					resultMap.add(result);
				}

				/**
				 * ============================================================
				 * ==============
				 **/
			}
			logMap.put("emp_no", "Agent");
			logMap.put(
					"comment",
					isAGT ? String.format("자동소명(제재) 실행 -> 소명(제재)수 : %s",
							execCount) : "자동제재 실행 시스템 차단");

		} catch (Exception e) {
			logMap.put("emp_no", "Agent");
			logMap.put("comment", e.getMessage());
			// e.printStackTrace();
		} finally {
			try {

				autoSanctService.InsertAutobatLog(logMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}

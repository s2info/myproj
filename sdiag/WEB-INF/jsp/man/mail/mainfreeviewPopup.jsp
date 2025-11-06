<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>
<html xmlns='http://www.w3.org/1999/xhtml'>
<head>
<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />

<script type="text/javascript" language="javascript">


</script>

</head>

<body >
<!-- S : contents -->

	<!-- S : center contents -->
    <c:choose>
		<c:when test="${sendInfo.gubun eq 'N'}">
		<!-- 공지사항 -->
		    <table cellpadding='0' cellspacing='0' style='width:100%;height:auto;padding:0;margin:0;'>
		    <tr>
		    	<td>
		    		${sendInfo.contents }
		    	</td>
		    </tr>
		    <tr>
		    	<td style='font-family:"돋움";font-size:11px;;text-align:center;padding:10px;margin:0px 0 0 0;background:#ffffff;'>
				${sendInfo.mail_ask }
		    	</td>
		    </tr>
		    <tr>
		    	<td style='font-family:"돋움";font-size:11px;text-align:center;padding:10px;margin:0px 0 0 0;background:#ffffff;'>Copyright ⓒ <span style='color:#ff6565;font-weight:bold;font-family:"돋움";font-size: 11pt;'>KT</span> All rights reserved.</td>
		    </tr>
		    </table>
	    </c:when>
	    <c:otherwise>
			<c:choose>
				<c:when test="${sendInfo.pol_type eq '04'}">
			    <!--  주가 보고 -->
	    			<table cellpadding='0' cellspacing='0' style='width:1000px;height:auto;background:#f1f1f1;padding:0;margin:0;'>
	    				<tr>
	    					<td style='width:1000px;height:50px;background:#ffffff;font-weight:bold;font-family:"맑은 고딕";font-size:18pt;line-height:40px;'><img src="/img/top_logo.png" style='vertical-align:middle;' /> 임직원보안 수준진단
	    					</td>
	    				</tr>
	    				<tr>
	    					<td style="padding:10px 10px 10px 20px;">
	    						${sendInfo.contents_top }
	    					</td>
	    				</tr>
	    				<tr>
	    					<td style='padding:0 17px;'>
	    						<table cellpadding='0' cellspacing='0' style='height:auto;background:#ffffff;border:solid 1px  #dbdbdb;padding:20px 15px 20px 28px;'>
	    						<tr>
	    							<td style='color:#414141;text-align:right;font-family:"돋움";font-size:12px;padding:10px 30px 10px 0;'> <span style='font-weight:bold;font-family:"돋움";font-size:12px;'>데이터 수집날짜 ㅣ</span>2018년 10월 19일</td>
	    						</tr>
	    						<tr>
	    							<td>
	    								<table cellpadding='0' cellspacing='0'>
	    									<tr>
	    										<td>
	    											<table cellpadding='0' cellspacing='0' style='width:270px;padding:0px 0;background:#5a87d2;color:#ffffff;text-align:center;'>
	    												<tr><td style='font-weight:bold;font-family:"돋움";font-size:12px;padding:10px 0 5px 0;'><font color='#ffffff'>본인</font><td></tr>
	    												<tr><td style='font-weight:bold;font-family:"맑은 고딕";font-size:18px;padding:0 0 0 0;'>score</td></tr>
	    												<tr><td style='font-weight:bold;font-family:"맑은 고딕";font-size:50px;padding:0 0 0 0;'>90</td></tr>
	    											</table>
	    										</td>
	    										<td style='padding-left:40px'>
	    											<table cellpadding='0' cellspacing='0' style='width:270px;padding:0px 0;background:#5a87d2;color:#ffffff;text-align:center;'>
	    												<tr><td style='font-weight:bold;font-family:"돋움";font-size:12px;padding:10px 0 5px 0;'><font color='#ffffff'>소속부서명</font><td></tr>
	    												<tr><td style='font-weight:bold;font-family:"맑은 고딕";font-size:18px;padding:0 0 0 0;'>score</td></tr>
	    												<tr><td style='font-weight:bold;font-family:"맑은 고딕";font-size:50px;padding:0 0 0 0;'>90</td></tr>
	    											</table>
	    										</td>
	    										<td style='padding-left:40px'>
	    											<table cellpadding='0' cellspacing='0' style='width:270px;padding:0px 0;background:#5a87d2;color:#ffffff;text-align:center;'>
	    												<tr><td style='font-weight:bold;font-family:"돋움";font-size:12px;padding:10px 0 5px 0;'><font color='#ffffff'>상위부서명</font><td></tr>
	    												<tr><td style='font-weight:bold;font-family:"맑은 고딕";font-size:18px;padding:0 0 0 0;'>score</td></tr>
	    												<tr><td style='font-weight:bold;font-family:"맑은 고딕";font-size:50px;padding:0 0 0 0;'>90</td></tr>
	    											</table>
	    										</td>
	    									</tr>
	    								</table>
	    							</td>
	    						</tr>
	    						<tr>
	    							<td>
	    								<table cellpadding='0' cellspacing='0' style='margin:20px 0'>
	    									<tr>
	    									<c:forEach var="i" begin="0" end="3" step="1">
	    										<td style='margin:0px 10px 10px 10px;'><!-- i % 4 = 1  <td style='margin:0px 10px 10px 0px;'> -->
	    											<table cellpadding='0' cellspacing='0' style='width:215px;height:170px;padding:0 10px 10px 10px;margin:10px 15px 10px 0;background:#edeef0;border:solid 1px #dbdbdb;display:inline-block;text-align:center;'>
	    												<tr><td style='font-weight:bold;font-family:"돋움";font-size:11px;text-align:center;border-bottom:solid 1px #dbdbdb;padding:10px 0 0 0;letter-spacing:-1pt;'>정책명 [100점]</td></tr>
	    												<tr><td style='width:193px;text-align:center;margin:5px 0;display:inline-block;'><img src="/img/state_1.png" /></td></tr>
	    												<tr><td style='text-align:left;padding:0 16px;overflow:hidden;font-family:"돋움";font-size:11px;margin:0 0 0 0;line-height:20px;'>발생사유</td></tr>
	    												<tr><td style='text-align:left;padding:0 16px;overflow:hidden;font-family:"돋움";font-size:11px;line-height:20px;'>
	    													발생사유
	    													</td>
	    												</tr>
	    											</table>
	    										</td>
	    									</c:forEach>
	    										
	    									</tr>
	    									<tr>
	    									
	    									</tr>
	    								</table>	
	    							</td>
	    						</tr>
	    						<tr>
	    							<td>${sendInfo.contents_bottom }</td>
	    						</tr>
	    						</table>
	    					</td>
	    				</tr>
	    				<tr>
	    					<td style='text-align:center;height:100px;'><a href='http://sldm.kt.com' target="_blank"><img src="/img/btn_go.png" title='임직원수준진단 사이트로 이동' alt='임직원수준진단 사이트로 이동' /></a></td>
	    				</tr>
	    				<tr>
	    					<td style='font-family:"돋움";font-size:11px;;text-align:center;padding:10px;margin:0px 0 0 0;background:#ffffff;'>
								${sendInfo.mail_ask }
						    	</td>
	    				</tr>
	    				<tr>
	    					<td style='font-family:\"돋움\";font-size:11px;text-align:center;padding:10px;margin:0px 0 0 0;background:#ffffff;'>Copyright ⓒ <span style='color:#ff6565;font-weight:bold;font-family:"돋움";font-size: 11pt;'>KT</span> All rights reserved.</td>
	    				</tr>
	    			</table>
	    		</c:when>
	    		<c:when test="${sendInfo.pol_type eq '05'}">
	    		<!--  춸간 보고 -->
	    			<table cellpadding='0' cellspacing='0' style='width:1000px;height:auto;background:#f1f1f1;padding:0;margin:0;'>
	    				<tr>
	    					<td style='width:1000px;height:50px;background:#ffffff;font-weight:bold;font-family:"맑은 고딕";font-size:18pt;line-height:40px;'><img src="/img/top_logo.png" style='vertical-align:middle;' /> 임직원보안 수준진단
	    					</td>
	    				</tr>
	    				<tr>
	    					<td style="padding:10px 10px 10px 20px;">
	    						${sendInfo.contents_top }
	    					</td>
	    				</tr>
	    				<tr>
	    					<td style='padding:0 17px;'>
	    						<table cellpadding='0' cellspacing='0' style='height:auto;background:#ffffff;border:solid 1px  #dbdbdb;padding:20px 15px 20px 28px;'>
	    						<tr>
	    							<td style='color:#414141;text-align:right;font-weight:normal;font-family:"돋움";font-size:11px;padding:10px 30px 10px 0;'><span style='font-weight:bold;font-family:"돋움";font-size:11px;'>데이터 수집날짜 ㅣ </span><span style='font-family:"돋움";font-size:11px;'>2018년 10월 10일</span></td>
	    						</tr>
	    						<tr>
	    							<td>
	    								<table cellpadding='0' cellspacing='0'>
	    									<tr>
	    										<td>
	    											<table cellpadding='0' cellspacing='0' style='background:#ffffff;color:#676767;text-align:center;'>
	    												<tr style='height:35px;'>
	    													<td style='border:solid 1px #dbdbdb;width:290px;background:#f2f2f2;padding-left:10px;font-weight:bold;font-family:"돋움";font-size:12px;text-align:left;'>소속부서<td>
	    													<td style='border:solid 1px #dbdbdb;padding-right:20px;width:114px;background:#ffffff;text-align:right;font-weight:bold;font-family:"돋움";font-size:12px;'>100점</td>
	    												</tr>	
	    											</table>
	    										</td>
	    									</tr>
	    								</table>
	    							</td>
	    						</tr>
	    						<tr style='height:40px;'><td style='font-weight:bold;font-family:"돋움";padding:0 0 0 20px;font-size:12px;'>[ ** ] 부서별 보안 점수</td></tr>
	    						<tr>
	    							<td>
	    								<table cellpadding='0' cellspacing='0'>
	    									<tr>
	    									<c:forEach var="i" begin="1" end="4" step="1">
	    										<c:choose><c:when test="${i eq 1}"><td></c:when><c:otherwise><td style='padding-left:20px;'></c:otherwise> </c:choose>
	    											<table cellpadding='0' cellspacing='0' style='width:215px;background:#edeef0;border:solid 1px #dbdbdb;display:inline-block;text-align:center;'>
	    												<tr><td style='height:150px;width:215px;text-align:center;'>
	    													<table cellpadding='0' cellspacing='0' style='width:100%;height:140px;background:#edeef0;'>
	    														<tr style='height:150px;'>
	    															<td style='vertical-align:bottom;background:#edeef0;text-align:center;width:89px;'>&nbsp;</td>
	    															<td style='vertical-align:bottom;background:#edeef0;text-align:center;width:37px;'>
	    																<table cellpadding='0' cellspacing='0' style='width:100%;height:140px;'>
	    																	<tr style='height:40px;'>
	    																		<td style='background:#EE1C29;'> </td>
	    																	</tr>	
	    																	<tr style='height:100px;'>
	    																		<td style='background:#52be34;'> </td>
	    																	</tr>	
	    																</table>
	    															</td>
	    															<td style='vertical-align:bottom;background:#edeef0;text-align:center;width:89px;'>&nbsp;</td>
	    														</tr>
	    													</table>		
	    													</td>
	    												</tr>		
	    												<tr><td style='height:25px;width:215px;text-align:center;padding:0 16px;overflow:hidden;font-weight:bold;font-family:\"돋움\";font-size: 10pt;line-height:20px;'>부서명</td></tr>
	    												<tr><td style='height:25px;width:215px;text-align:center;padding:0 16px;overflow:hidden;font-weight:bold;font-family:\"돋움\";font-size: 10pt;line-height:20px;'>100점</td></tr>
	    											</table>
	    											</td>
	    											
	    											
	    									</c:forEach>
	    									</tr>
	    								</table>	
	    							</td>
	    						</tr>
	    					<c:forEach var="i" begin="1" end="4" step="1">
	    						<tr><td style='border-bottom:dotted 2px #CCCCCC;padding-top:20px;'></td></tr>
	    						<tr>
	    							<td style='padding-top:20px;'>
	    								<table cellpadding='0' cellspacing='0'>
	    									<tr>
	    										<td style='height:35px;'>
	    											<table cellpadding='0' cellspacing='0' style='background:#ffffff;color:#676767;text-align:center;'>
	    												<tr style='height:35px;'>
	    													<td style='border:solid 1px #dbdbdb;width:290px;padding-left:10px;background:#f2f2f2;font-weight:bold;font-family:\"돋움\";font-size:12px;text-align:left;'>하위부서명<td>
	    													<td style='border:solid 1px #dbdbdb;padding-right:20px;width:114px;background:#ffffff;text-align:right;font-weight:bold;font-family:\"돋움\";font-size:12px;'>100s점</td>
	    												</tr>
													</table>
												</td>
											</tr>	    									
	    								</table>	
	    							</td>
	    						</tr>
	    						<tr style='height:40px;'><td style='font-weight:bold;font-family:\"돋움\";padding:0 0 0 20px;font-size:12px;'>상위 부서명 > 부서명 보안 점수</td></tr>
	    						<tr>
	    							<td>
	    								<table cellpadding='0' cellspacing='0'>
	    								<tr>
	    								<c:forEach var="j" begin="1" end="3" step="1">
	    									
	    										<c:choose><c:when test="${j eq 1}"><td></c:when><c:otherwise><td style='padding-left:20px;'></c:otherwise> </c:choose>
	    											<table cellpadding='0' cellspacing='0' style='width:215px;background:#edeef0;border:solid 1px #dbdbdb;display:inline-block;text-align:center;'>
	    												<tr><td style='height:150px;width:215px;text-align:center;'>
	    													<table cellpadding='0' cellspacing='0' style='width:100%;height:140px;background:#edeef0;'>
	    														<tr style='height:150px;'>
	    															<td style='vertical-align:bottom;background:#edeef0;text-align:center;width:89px;'>&nbsp;</td>
	    															<td style='vertical-align:bottom;background:#edeef0;text-align:center;width:37px;'>
	    																<table cellpadding='0' cellspacing='0' style='width:100%;height:140px;'>
	    																	<tr style='height:40px;'>
	    																		<td style='background:#EE1C29;'> </td>
	    																	</tr>
	    																	<tr style='height:100px;'>	
	    																		<td style='background:#52be34;'> </td>
	    																	</tr>
	    																</table>
	    															</td>
	    															<td style='vertical-align:bottom;background:#edeef0;text-align:center;width:89px;'>&nbsp;</td>
	    														</tr>
	    													</table>
	    													</td>
	    												</tr>
	    												<tr><td style='height:25px;width:215px;text-align:center;padding:0 16px;overflow:hidden;font-weight:bold;font-family:\"돋움\";font-size: 10pt;line-height:20px;'>부서명</td></tr>
	    												<tr><td style='height:25px;width:215px;text-align:center;padding:0 16px;overflow:hidden;font-weight:bold;font-family:\"돋움\";font-size: 10pt;line-height:20px;'>100점</td></tr>
	    											</table>	
	    										</td>
	    									
	    								</c:forEach>
	    								</tr>
	    								</table>
	    							</td>
	    						</tr>
	    					
	    							
	    					</c:forEach>
	    						
	    						<tr>
	    							<td style="padding-top:20px;">${sendInfo.contents_bottom }</td>
	    						</tr>
	    						</table>
	    					</td>
	    				</tr>
	    				<tr>
	    					<td style='text-align:center;height:100px;'><a href='http://sldm.kt.com' target="_blank"><img src="/img/btn_go.png" title='임직원수준진단 사이트로 이동' alt='임직원수준진단 사이트로 이동' /></a></td>
	    				</tr>
	    				<tr>
	    					<td style='font-family:"돋움";font-size:11px;;text-align:center;padding:10px;margin:0px 0 0 0;background:#ffffff;'>
								${sendInfo.mail_ask }
						    	</td>
	    				</tr>
	    				<tr>
	    					<td style='font-family:\"돋움\";font-size:11px;text-align:center;padding:10px;margin:0px 0 0 0;background:#ffffff;'>Copyright ⓒ <span style='color:#ff6565;font-weight:bold;font-family:"돋움";font-size: 11pt;'>KT</span> All rights reserved.</td>
	    				</tr>
	    			</table>
	    			
				</c:when>
	    		<c:otherwise>
	    			
	    			<table cellpadding="0" cellspacing="0" style="width:1000px;background:#ffffff;color:#393939;">
						<tr>
					    	<td style='width:1000px;height:50px;background:#ffffff;font-weight:bold;font-family:"맑은 고딕";font-size:18pt;line-height:40px;'><img src="/img/top_logo.png" style='vertical-align:middle;' /> 임직원보안 수준진단
					    </tr>
					    <tr>
					    	<td style="padding:30px 20px;background:#ffffff;font:normal 12px '돋움';border-top:solid 2px #5499de;line-height:25px;">
					       ${sendInfo.contents_top }   
					        </td>
					    </tr>
					    <tr>
					    	<td style="padding:0px 20px 80px 20px;background:#ffffff;font:normal 12px '돋움';border-collapse:collapse;">
					        	<table cellpadding="0" cellspacing="0" style="width:1000px;border:solid 1px #cfcfcf;">
					            	<tr>
					                	<th style="color:#ffffff;font:bold 12px '돋움';text-align:center;padding:10px;background:#5499de;border:solid 1px #cfcfcf;">수집날짜</th>
					                    <th style="color:#ffffff;font:bold 12px '돋움';text-align:center;padding:10px;background:#5499de;border:solid 1px #cfcfcf;">정책</th>
					                    <th style="color:#ffffff;font:bold 12px '돋움';text-align:center;padding:10px;background:#5499de;border:solid 1px #cfcfcf;">건수</th>
					                    <th style="color:#ffffff;font:bold 12px '돋움';text-align:center;padding:10px;background:#5499de;border:solid 1px #cfcfcf;">평균점수</th>
					                    <th style="color:#ffffff;font:bold 12px '돋움';text-align:center;padding:10px;background:#5499de;border:solid 1px #cfcfcf;">상태</th>
					                </tr>
					                <tr style="">
					                	<td style="padding:10px;border:solid 1px #cfcfcf;text-align:center;background:#f7f7f7;">2009-05-28</td>
					                    <td style="padding:10px;border:solid 1px #cfcfcf;text-align:center;">임직원 지수화 정책 1</td>
					                    <td style="padding:10px;border:solid 1px #cfcfcf;text-align:center;">0</td>
					                    <td style="padding:10px;border:solid 1px #cfcfcf;text-align:center;">100점</td>
					                    <td style="padding:10px;border:solid 1px #cfcfcf;text-align:center;"><span style="background:#55bc1e;color:#ffffff;padding:5px 5px 3px 5px;border-radius:3px;font-weight:bold;">양호</span></td>
					                </tr>
					                <tr style="">
					                	<td style="padding:10px;border:solid 1px #cfcfcf;text-align:center;background:#f7f7f7;">2009-05-28</td>
					                    <td style="padding:10px;border:solid 1px #cfcfcf;text-align:center;">임직원 지수화 정책 2</td>
					                    <td style="padding:10px;border:solid 1px #cfcfcf;text-align:center;">5</td>
					                    <td style="padding:10px;border:solid 1px #cfcfcf;text-align:center;">50점</td>
					                    <td style="padding:10px;border:solid 1px #cfcfcf;text-align:center;"><span style="background:#FEAB35;color:#ffffff;padding:5px 5px 3px 5px;border-radius:3px;font-weight:bold;">주의</span></td>
					                </tr>
					                <tr style="">
					                	<td style="padding:10px;border:solid 1px #cfcfcf;text-align:center;background:#f7f7f7;">2009-05-28</td>
					                    <td style="padding:10px;border:solid 1px #cfcfcf;text-align:center;">임직원 지수화 정책 3</td>
					                    <td style="padding:10px;border:solid 1px #cfcfcf;text-align:center;">20</td>
					                    <td style="padding:10px;border:solid 1px #cfcfcf;text-align:center;">0점</td>
					                    <td style="padding:10px;border:solid 1px #cfcfcf;text-align:center;"><span style="background:#E00000;color:#ffffff;padding:5px 5px 3px 5px;border-radius:3px;font-weight:bold;">취약</span></td>
					                </tr>                
					            </table>
					        </td>
					    </tr>     
						<tr>
   							<td style='background:#ffffff;'>${sendInfo.contents_bottom }</td>
	    				</tr>
	    				<tr>
	    					<td style='text-align:center;height:100px;'><a href='http://sldm.kt.com' target="_blank"><img src="/img/btn_go.png" title='임직원수준진단 사이트로 이동' alt='임직원수준진단 사이트로 이동' /></a></td>
	    				</tr>
	    				<tr>
	    					<td style='font-family:"돋움";font-size:11px;;text-align:center;padding:10px;margin:0px 0 0 0;background:#ffffff;'>
								${sendInfo.mail_ask }
						    	</td>
	    				</tr>
	    				<tr>
	    					<td style='font-family:\"돋움\";font-size:11px;text-align:center;padding:10px;margin:0px 0 0 0;background:#ffffff;'>Copyright ⓒ <span style='color:#ff6565;font-weight:bold;font-family:"돋움";font-size: 11pt;'>KT</span> All rights reserved.</td>
	    				</tr>       
					</table>
	    		</c:otherwise>
			</c:choose>
		</c:otherwise>
	</c:choose>
    <!-- E : center contents -->

</body>
</html>
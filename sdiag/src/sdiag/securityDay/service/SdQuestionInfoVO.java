package sdiag.securityDay.service;

/**
 * 예외 ip, 사번 정보 VO클래스로 구성한다.
 * @author 공통서비스 개발
 * @since 2017.04.07
 * @version 1.0
 * @see
 *
 * 
 */
public class SdQuestionInfoVO{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	//점검표 seq
	private int sdCheckNo;
	//문항번호
	private int questionNum;
	//문항 제목
	private String questionNm;
	//문항 구분
	private String questionType;
	//설명 
	private String explain;
	//문제
	private String question;
	//답             
	private String answer;  
	//등록자 사번     
	private String rgdtEmpNo;
	//등록일           
	private String rgdtDate;
	//수정일
	private String updtDate;
	//등록자 명
	private String rgdtEmpNm;
	//답  유형         
	private String answerType; 
	//첨부파일 Y/N
	private String fileYn;
	//첨부파일 문구
	private String fileText;
	//첨부파일 문구
	private String ordr;
	
	public int getSdCheckNo() {
		return sdCheckNo;
	}
	public void setSdCheckNo(int sdCheckNo) {
		this.sdCheckNo = sdCheckNo;
	}
	public int getQuestionNum() {
		return questionNum;
	}
	public void setQuestionNum(int questionNum) {
		this.questionNum = questionNum;
	}
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}
	public String getRgdtEmpNo() {
		return rgdtEmpNo;
	}
	public void setRgdtEmpNo(String rgdtEmpNo) {
		this.rgdtEmpNo = rgdtEmpNo;
	}
	public String getRgdtDate() {
		return rgdtDate;
	}
	public void setRgdtDate(String rgdtDate) {
		this.rgdtDate = rgdtDate;
	}
	public String getUpdtDate() {
		return updtDate;
	}
	public void setUpdtDate(String updtDate) {
		this.updtDate = updtDate;
	}
	public String getRgdtEmpNm() {
		return rgdtEmpNm;
	}
	public void setRgdtEmpNm(String rgdtEmpNm) {
		this.rgdtEmpNm = rgdtEmpNm;
	}
	public String getAnswerType() {
		return answerType;
	}
	public void setAnswerType(String answerType) {
		this.answerType = answerType;
	}
	public String getQuestionNm() {
		return questionNm;
	}
	public void setQuestionNm(String questionNm) {
		this.questionNm = questionNm;
	}
	public String getFileYn() {
		return fileYn;
	}
	public void setFileYn(String fileYn) {
		this.fileYn = fileYn;
	}
	public String getFileText() {
		return fileText;
	}
	public void setFileText(String fileText) {
		this.fileText = fileText;
	}
	public String getOrdr() {
		return ordr;
	}
	public void setOrdr(String ordr) {
		this.ordr = ordr;
	}
		
}

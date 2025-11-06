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
public class SdResultInfoVO{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	//점검표 그룹 seq
	private int clGroupNo=0;
	//점검표 그룹 명
	private String clGroupNm;
	//점검표 seq
	private int sdCheckNo=0;
	//문항번호
	private int questionNum=0;
	//문항 구분
	private String questionType;
	//설명 
	private String explain;
	//문제
	private String question;
	//답             
	private String answer="";
	//파일명
	private String fileNm="";
	//점검일           
	private String checkDate;
	//답  유형         
	private String answerType; 
	//점검여부       
	private String checkYn; 
	//구분 -1:대상 2:예외자
	private String gubun;
	//사번 
	private String empNo;
	//첨부파일
	private String fileYn;
	//첨부 파일 안내 문구
	private String fileText;
	//첨부파일
	private String checkStartDay;
	//첨부 파일 안내 문구
	private String checkEndDay;
	//지수화 시작일
	private String idxStartDay;
	//
	private int rowNum=0;
	
	private String ordr;
	
	private String resultYn="";
	
	private String infoText = "";
	
	
	public int getClGroupNo() {
		return clGroupNo;
	}
	public void setClGroupNo(int clGroupNo) {
		this.clGroupNo = clGroupNo;
	}
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
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
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
	public String getFileNm() {
		return fileNm;
	}
	public void setFileNm(String fileNm) {
		this.fileNm = fileNm;
	}
	public String getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}
	public String getAnswerType() {
		return answerType;
	}
	public void setAnswerType(String answerType) {
		this.answerType = answerType;
	}
	public String getCheckYn() {
		return checkYn;
	}
	public void setCheckYn(String checkYn) {
		this.checkYn = checkYn;
	}
	public String getGubun() {
		return gubun;
	}
	public void setGubun(String gubun) {
		this.gubun = gubun;
	}
	public String getEmpNo() {
		return empNo;
	}
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
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
	public String getCheckStartDay() {
		return checkStartDay;
	}
	public void setCheckStartDay(String checkStartDay) {
		this.checkStartDay = checkStartDay;
	}
	public String getCheckEndDay() {
		return checkEndDay;
	}
	public void setCheckEndDay(String checkEndDay) {
		this.checkEndDay = checkEndDay;
	}
	public String getIdxStartDay() {
		return idxStartDay;
	}
	public void setIdxStartDay(String idxStartDay) {
		this.idxStartDay = idxStartDay;
	}
	public String getClGroupNm() {
		return clGroupNm;
	}
	public void setClGroupNm(String clGroupNm) {
		this.clGroupNm = clGroupNm;
	}
	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	public String getOrdr() {
		return ordr;
	}
	public void setOrdr(String ordr) {
		this.ordr = ordr;
	}
	public String getResultYn() {
		return resultYn;
	}
	public void setResultYn(String resultYn) {
		this.resultYn = resultYn;
	}
	public String getInfoText() {
		return infoText;
	}
	public void setInfoText(String infoText) {
		this.infoText = infoText;
	}
	
}

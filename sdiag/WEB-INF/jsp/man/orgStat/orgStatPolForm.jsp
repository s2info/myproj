<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>


<%@ include file="/WEB-INF/jsp/cmm/title.jsp" %>
<script type="text/javascript" src="/js/jquery.form.js"></script>s
 <link type="text/css" rel="stylesheet" href="/css/jquery-plugin.css" />
 <style type="text/css">
 .view_right ul {width:100%;margin:10px 10px;}
 .input.srh {width:90%;background:#ffffff;padding:8px;margin-left:10px;border:solid 1px #dcdcdc;}
 .view_left {width:30%;height:550px;display:inline-block;float:left;background:#f3f3f3;border:solid 1px #dcdcdc;padding:10px;}
 .view_center {width:15%;display:inline-block;float:left;text-align:center;padding:230px 0 0 20px;margin:0 auto;}
 .view_right {position:relative;width:50%;height:570px;display:inline-block;background:#ffffff;float:right;border:solid 1px #dcdcdc;}
 .view_right ul.inputcell {width:100%;}
 .view_right li.view_scroll {display:block;height:520px;overflow-y:auto;overflow-x:hidden;}
</style>
<script type="text/javaScript" language="javascript">
$(function () {		
	

	$('#idxDate').datepicker({
        dateFormat: 'yy-mm-dd',
        monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
        dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
        changeMonth: true,
        changeYear: true,
        showMonthAfterYear: true,
        showButtonPanel: false,
        showOn: "both",
        //maxDate:"<c:out value="${nowDate}" />",
        buttonImage: "/img/icon_date.png",
        buttonImageOnly: true,
        beforeShow:function(){
        	setTimeout(function(){
        		$(".ui-datepicker").css("z-index","99999");
        	}, 0);
        }
    });
    $(".ui-datepicker-trigger").css("margin","0 0 -9px 0");
    
    $.polGubun = function(){
    	var gubun = $("#gubun").val();
    	
    	if(gubun=="P"){
    		$("#polList").css("display", "");
    		$("#uploadPol").css("display","none");
    	}else if(gubun=="U"){
    		$("#polList").css("display","none");
    		$("#uploadPol").css("display","");
    	}else{
    		$("#polList").css("display","none");
    		$("#uploadPol").css("display","none");
    	}
    };
    
    
	$(".btn_etc_1").click(function(){
		//var orgInfo = $(this).attr('orgInfo');
		//var orgType = $(this).attr('orgType');
		//var orgNm = $(this).attr('orgNm');
		 //alert(orgType);
		
		 var gubun = $("#gubun").val();
		
		 var polCnt = parseInt($("#polCnt").val());
		
	     
	    
	     var polIdxId = "";
		 var polIdxNm = "";
		 var strAdd = "<ul mod ='I' onclick='$.ulIndex(this);'>";
		 
		 if(gubun=="P"){
			 
			
			var polInfo = $("#polId").val();
			var sumRgdtDate =$("#idxDate").val().replace(/-/gi, "");
			var arr = new Array();
		    arr = polInfo.split("|");
		    
		    polIdxId = arr[0];
			polIdxNm = arr[1];
		    
			var mCnt = 0;
			if(polIdxId == "" &&polIdxNm == "" ){
    			alert("정책을 선택 해주세요.");
    			return false;
    		}
			
			if(sumRgdtDate ==""){
    			alert("날짜를 선택 해주세요.");
    			return false;
    		}
			
			$("input[name='polIdxId']").each(function(i){
				if(polIdxId == $("input[name='polIdxId']").eq(i).val() && sumRgdtDate ==  $("input[name='sumRgdtDate']").eq(i).val()){
					
					mCnt++;
					
				}
			});
			
			if(mCnt > 0 ){
				alert("동인한 정책의 날짜가 이미 존재합니다.\n 다시 한번 확인 해주세요.");
				return false;
			}else {
				strAdd +="<li class='ttcell3'>"+polIdxNm+"/"+$("#idxDate").val()+"</li>";
	    		strAdd +="<li><input type='text'   id='polWeightValue_p' name='polWeightValue_p' value='100'  class='srh' style='width:90%;background:#ffffff;padding:8px;margin-left:10px;border:solid 1px #dcdcdc;'></li>";
	    		strAdd +="<input type='hidden' id='polIdxId' name='polIdxId' value='"+polIdxId+"' />";
	    		strAdd +="<input type='hidden' id='polIdxNm' name='polIdxNm' value='"+polIdxNm+"' />";
	    		strAdd +="<input type='hidden' id='sumRgdtDate' name='sumRgdtDate' value='"+sumRgdtDate+"' />";
	    		strAdd +="<input type='hidden' id='gubun' name='gubun' value='"+gubun+"' /></ul>";
			}
    		
    	 }else if(gubun=="U"){
    		 
    		polIdxId  = "SHAN"+$("#polCnt").val();
    		polIdxNm = $("#uploadPolNm").val();
    		
    		if(polIdxNm == ""){
    			alert("정책명을 입력 해주세요.");
    			return false;
    		}
    		polCnt = polCnt+1;
    		$("#polCnt").val(polCnt);
    		strAdd +="<li class='ttcell3'>"+polIdxNm+"</li>";
    		strAdd +="<li style='margin-bottom: 5px;'><input type='text' id='polWeightValue_u' name='polWeightValue_u' value='100'  class='srh' style='width:90%;background:#ffffff;padding:8px;margin-left:10px;border:solid 1px #dcdcdc;'></li>";
    		strAdd += "<p> <br/>모든 대상자의 점수를 등록 해야 합니다. 엑셀은 사번, 점수 두개의 필드만 입력해서 업로드 해주세요.</p>";
    		strAdd += "<li style='width: 100%; margin-left: -16px;  margin-top: 5px;'><input type='file' id='excelFile_u' name='excelFile_u' style='width:92%;background:#ffffff;padding:6px;border:solid 1px #dcdcdc;' /></li>";
    		strAdd +="<input type='hidden' id='polIdxId_u' name='polIdxId_u' value='"+polIdxId+"' />";
    		strAdd +="<input type='hidden' id='polIdxNm_u' name='polIdxNm_u' value='"+polIdxNm+"' />";
    		strAdd +="<input type='hidden' id='gubun_u' name='gubun_u' value='"+gubun+"' /></ul>";
    		
  
    		
    	 }
		
		$(".view_scroll").append(strAdd);
		
		//self.close();
	});
	
	
	$(".btn_save").click( function(){
		/* $("input[name='excelFile']").each(function (i){
			var str = $("input[name='u_exText']").eq(i).val();
			alert(str);
			str.replace(/,/g, '&#44;');
			alert(str);
			$("input[name='u_exText']").eq(i).val(str);
		}); */
		
		if($("#subject").val() ==""){
			alert("통계 타이틀을 입력해주세요.");
			return false;
		}
		
		 var polList = [];
		 $("input[name='polIdxId']").each(function(i){
				var polIdxId = $("input[name='polIdxId']").eq(i).val();
				var polIdxNm =$("input[name='polIdxNm']").eq(i).val();
				var sumRgdtDate = $("input[name='sumRgdtDate']").eq(i).val();
				var gubun = $("input[name='gubun']").eq(i).val();
				var polWeightValue = $("input[name='polWeightValue_p']").eq(i).val();
				polList.push(polIdxId + "|" +polIdxNm + "|" +sumRgdtDate+ "|" +gubun+ "|" + polWeightValue);
		 })
		 
		
		 var upPolList = [];
		  $("input[name='polIdxId_u']").each(function(i){
				var polIdxId = $("input[name='polIdxId_u']").eq(i).val();
				var polIdxNm =$("input[name='polIdxNm_u']").eq(i).val();
				//var file ="";
				file = $("input[name='excelFile_u']").eq(i).val();
				var gubun = $("input[name='gubun_u']").eq(i).val();
				var polWeightValue = $("input[name='polWeightValue_u']").eq(i).val();
				
				
					if(file == ""){
						polList.push(polIdxId + "|" +polIdxNm + "|  |" +gubun+ "|" + polWeightValue);
					}else{
						upPolList.push(polIdxId + "|" +polIdxNm + "|" +file+ "|" +gubun+ "|" + polWeightValue);
					}					
		
				})
		$("#polIdxList").val(polList.toString());
		$("#upPolList").val(upPolList.toString());
		
		if(polList.toString() == "" && upPolList==""){
			alert("통계 정책을 추가해 주세요.");
			return false;
		}
		
		console.log(upPolList.toString());
		 var options = {
				 url: '/man/statInfoSave.do',
					//data: formData,
					type: 'POST',
					dataType: 'json',
					//mimrType : 'multipart/form-data',
					error: function (jqXHR, textStatus, errorThrown) {					
						if(jqXHR.status == 401){						
							alert('인증정보가 만료되었습니다.');						
							location.href='/';					
						}else{						
							alert(textStatus + '\r\n' + errorThrown);					
						}				
					},				
					success: function (data) {					
						if (data.ISOK) {
							$("#formMod").val(data.searchVO.formMod);
							$("#statSeq").val(data.searchVO.statSeq);
							if(!data.ISUP){
								alert(data.MSG);
							}else{alert("통계항목이 정상적으로 저장 되었습니다.");}
							
							
							document.saveForm.action = "<c:url value='/man/orgStatPolForm.do'/>";
							document.saveForm.method="post";
							document.saveForm.submit();
						}else{alert(data.MSG); }				
					} ,
					beforeSend: function () {
						$('#loading').find('img').attr('width', '100px').attr('height', '100px');
		           	 	var padingTop = (Number(($('.sch_view').css('height')).replace("px","")) / 2) + 50;
		                $('#loading').css('position', 'absolute');
		                $('#loading').css('left', $('.sch_view').offset().left + ($('.sch_view').css('width').replace("px","") / 2) - 130);
		                $('#loading').css('top', $('.sch_view').offset().top);
		                $('#loading').css('padding-top', 100);
		                $('#loading').show().fadeIn('fast');
		           },
		           complete: function () {
		               $('#loading').fadeOut();
		           }			
		 };
		$("#saveForm").ajaxForm(options).submit(); 
	});
    
	$(".btn_stat").click( function(){
		  
		
		 var polList = [];
		 $("input[name='polIdxId']").each(function(i){
				var polIdxId = $("input[name='polIdxId']").eq(i).val();
				var polIdxNm =$("input[name='polIdxNm']").eq(i).val();
				var sumRgdtDate = $("input[name='sumRgdtDate']").eq(i).val();
				var gubun = $("input[name='gubun']").eq(i).val();
				var polWeightValue = $("input[name='polWeightValue_p']").eq(i).val();
				polList.push(polIdxId + "|" +polIdxNm + "|" +sumRgdtDate+ "|" +gubun+ "|" + polWeightValue);
		 })
		 
		
		 var upPolList = [];
		  $("input[name='polIdxId_u']").each(function(i){
				var polIdxId = $("input[name='polIdxId_u']").eq(i).val();
				var polIdxNm =$("input[name='polIdxNm_u']").eq(i).val();
				//var file ="";
				file = $("input[name='excelFile_u']").eq(i).val();
				var gubun = $("input[name='gubun_u']").eq(i).val();
				var polWeightValue = $("input[name='polWeightValue_u']").eq(i).val();
				
				if($("#formMod").val() == "U"){
					if(file == ""){
						polList.push(polIdxId + "|" +polIdxNm + "|  |" +gubun+ "|" + polWeightValue);
					}else{
						upPolList.push(polIdxId + "|" +polIdxNm + "|" +file+ "|" +gubun+ "|" + polWeightValue);
					}					
				}else{
					if(file == ""){
						alert("수동 업로드할 파일을 업데이트 해주세요.");
						return false;
					}else{
						upPolList.push(polIdxId + "|" +polIdxNm + "|" +file+ "|" +gubun+ "|" + polWeightValue);
					}
				}
		 })
		  $("#polIdxList").val(polList.toString());
		$("#upPolList").val(upPolList.toString());
		
		 var options = {
				 url: '/man/orgStatInfoSave.do',
					//data: formData,
					type: 'POST',
					dataType: 'json',
					//mimrType : 'multipart/form-data',
					error: function (jqXHR, textStatus, errorThrown) {					
						if(jqXHR.status == 401){						
							alert('인증정보가 만료되었습니다.');						
							location.href='/';					
						}else{						
							alert(textStatus + '\r\n' + errorThrown);					
						}				
					},				
					success: function (data) {					
						if (data.ISOK) {
							document.saveForm.action = "<c:url value='/man/orgStatView.do'/>";
							document.saveForm.method="post";
							document.saveForm.submit();
						}else{alert(data.MSG); }				
					} ,
					beforeSend: function () {
						$('#loading').find('img').attr('width', '100px').attr('height', '100px');
		           	 	var padingTop = (Number(($('.sch_view').css('height')).replace("px","")) / 2) + 50;
		                $('#loading').css('position', 'absolute');
		                $('#loading').css('left', $('.sch_view').offset().left + ($('.sch_view').css('width').replace("px","") / 2) - 130);
		                $('#loading').css('top', $('.sch_view').offset().top);
		                $('#loading').css('padding-top', 100);
		                $('#loading').show().fadeIn('fast');
		           },
		           complete: function () {
		               $('#loading').fadeOut();
		           }			
		 };
		$("#saveForm").ajaxForm(options).submit(); 
	});
	
	$.ulIndex= function(obj){
		
		
		if($(obj).index() == $("#ulIndex").val()){
			$("#ulIndex").val(0);
			$(obj).children("li:first").css("background", "#ffffff");
		}
		else{
			$("#ulIndex").val($(obj).index());	
			$(obj).children("li:first").css("background", "gray");
		}
		
		
		
	};
	
	$(".btn_etc_2").click(function(){
		var i = $("#ulIndex").val();
		if(i > 0){
			$(".view_scroll ul").eq(i).remove();
		}
	});
	
	$(".btn_delete").click(function(){
		var options = {
				 url: '/man/orgStatInfoDelete.do',
					//data: formData,
					type: 'POST',
					dataType: 'json',
					//mimrType : 'multipart/form-data',
					error: function (jqXHR, textStatus, errorThrown) {					
						if(jqXHR.status == 401){						
							alert('인증정보가 만료되었습니다.');						
							location.href='/';					
						}else{						
							alert(textStatus + '\r\n' + errorThrown);					
						}				
					},				
					success: function (data) {					
						if (data.ISOK) {
							document.saveForm.action = "<c:url value='/man/statInfoList.do'/>";
							document.saveForm.method="post";
							document.saveForm.submit();
						}else{alert(data.MSG); }				
					} ,
					beforeSend: function () {
						$('#loading').find('img').attr('width', '100px').attr('height', '100px');
		           	 	var padingTop = (Number(($('.sch_view').css('height')).replace("px","")) / 2) + 50;
		                $('#loading').css('position', 'absolute');
		                $('#loading').css('left', $('.sch_view').offset().left + ($('.sch_view').css('width').replace("px","") / 2) - 130);
		                $('#loading').css('top', $('.sch_view').offset().top);
		                $('#loading').css('padding-top', 100);
		                $('#loading').show().fadeIn('fast');
		           },
		           complete: function () {
		               $('#loading').fadeOut();
		           }			
		 };
		$("#saveForm").ajaxForm(options).submit(); 
	});
});
</script>
</head>
<body>
<!-- S : header -->
	<%@ include file="/WEB-INF/jsp/cmm/topmenu.jsp" %>
<!-- E : header -->
<!-- S : contents -->
<div id="cnt">
	<!-- S : left contents -->
	<div id="cnt_L">
	<%@ include file="/WEB-INF/jsp/cmm/adminleftmenu.jsp" %>
	 </div>
    <!-- E : left contents -->	
	<!-- S : center contents -->
    <div id="cnt_R">
    <form:form id="saveForm" name="saveForm" method="post" enctype="multipart/form-data">
    	<input type="hidden" id="polCnt" value="${polCnt }"/>
    	<input type="hidden" id="formMod" name="formMod" value="${searchVO.formMod }"/>
    	<input type="hidden" id="statSeq" name="statSeq" value="${searchVO.statSeq }"/>
    	<input type="hidden" id="statSeqList" name="statSeqList" value="${searchVO.statSeq }"/>
    	<input type="hidden" id="polIdxList" name="polIdxList" value=""/>
    	<input type="hidden" id="upPolList" name="upPolList" value=""/>
    	<input type="hidden" id="ulIndex" name="" value=""/>
    	<div class="subTT"><span>기관통계 등록/산정 관리 화면 > 통계항목 등록</span></div>
        <div class="sch_view">
        	<div class="view_left">
            	<li>
                    <select id="gubun" name="gubun" style="width:100%;font-weight:bold;" onchange="$.polGubun();">
                    	<option value="">선택</option> 
                        <option value="P">수준진단 가져오기</option> 
                        <option value="U">수동등록</option>
                    </select>                
                </li>
                <!-- S : 수준진단 선택시 -->
            	<li id="polList" style="display: none;">
                	<p>수준진단 정책list 택1 (반복 적용 가능)</p>
                    <select id="polId" name="polId" style="width:60%">
                    	<option value="">선택</option>
                    <c:forEach var="result" items="${polList}" varStatus="status">
						<option value="${result.secPolId}|${result.secPolDesc }">${result.secPolDesc }</option>
					</c:forEach>
                    </select>
                    <input type="text"  class="srh" id="idxDate" name="idxDate" value=""  class="srh" style="width:25%">
                </li>
                <!-- E : 수준진단 선택시 -->
                <!-- S : 수동등록 선택시 -->
            	<li id="uploadPol" style="display: none;">
                	<p>수동등록 항목 입력</p>
                    <input type="text" style="width:95%" class="srh" id="uploadPolNm" placeholder="정책명 입력">
                </li>               
                <!-- E : 수동등록 선택시 -->               
            </div>
            <div class="view_center">
            	<li><a href="#" class="btn_etc_1">통계항목<br />등록</a></li>
                <li><a href="#" class="btn_etc_2">등록취소</a></li>
            </div>
            <div class="view_right">
            	<h4>통게 타이틀 : <input type="text"  class="srh" id="subject" name="subject" value="${statInfo.subject }"  class="srh" style="width:80%"></h4>
                <li class="view_scroll">
                	<ul class="ttblock">
                    	<li class="ttcell1">통계 항목</li>
                        <li class="ttcell2">항목별 가중치</li>
                    </ul>
                   
                    <c:forEach var="statPolList" items="${statPolList}" varStatus="status">
                     <ul mod = "U" onclick="$.ulIndex(this);">
						<c:choose>
						<c:when test="${statPolList.gubun eq 'P' }">
							<li class="ttcell3">${statPolList.polIdxNm}/${statPolList.sumRgdtDate }</li>
			    			<li><input type="text"   id="polWeightValue_p" name="polWeightValue_p" value="${statPolList.polWeightValue }"  class="srh" style="width:90%;background:#ffffff;padding:8px;margin-left:10px;border:solid 1px #dcdcdc;"></li>
			    		
			    			<input type="hidden" id="polIdxId" name="polIdxId" value="${statPolList.polIdxId}" />
			    			<input type="hidden" id="polIdxNm" name="polIdxNm" value="${statPolList.polIdxNm}" />
			    			<input type="hidden" id="sumRgdtDate" name="sumRgdtDate" value="${fn:replace(statPolList.sumRgdtDate, '-','') }" />
			    			<input type="hidden" id="gubun" name="gubun" value="${statPolList.gubun}" />
						</c:when>
						<c:otherwise>
							<li class="ttcell3">${statPolList.polIdxNm}</li>
			    			<li  style='margin-bottom: 5px;'><input type="text"   id="polWeightValue_u" name="polWeightValue_u" value="${statPolList.polWeightValue }"  class="srh" style="width:90%;background:#ffffff;padding:8px;margin-left:10px;border:solid 1px #dcdcdc;"></li>
							<p> <br/>모든 대상자의 점수를 등록 해야 합니다. 엑셀은 사번, 점수 두개의 필드만 입력해서 업로드 해주세요.</p>
							<li style="width: 100%; margin-left: -16px; margin-top:5px;"><input type='file' id='excelFile_u' name='excelFile_u' style='width:92%;background:#ffffff;padding:6px;border:solid 1px #dcdcdc;' /></li>
			    			<input type="hidden" id="polIdxId_u" name="polIdxId_u" value="${statPolList.polIdxId}" />
			    			<input type="hidden" id="polIdxNm_u" name="polIdxNm_u" value="${statPolList.polIdxNm}" />
			    			<input type="hidden" id="gubun_u" name="gubun_u" value="${statPolList.gubun}" />
						</c:otherwise>
						</c:choose>
						</ul>
					</c:forEach>
					
                </li>
            </div>
            <div style="clear:both"></div>
            <div class="btn_black3">
            <a href="/man/statInfoList.do" target="_self" class="btn_black"  style="margin-left: 10px;float: right;"><span>목록</span></a>
            <c:if test="${searchVO.formMod eq 'U' }">
            	<a href="#" class="btn_etc btn_stat">통계 계산 </a> 
            	<a href="#" class="btn_black btn_delete" style="float: right;" ><span>삭제</span></a>
            	 
            </c:if>
            	<a href="#" class="btn_etc btn_save">항목등록 완료</a>
            	
            	
            	
            </div>
        </div>
		</form:form>
    </div>
    <!-- E : center contents -->
    <!-- S : footer -->
    <div id="loading" style="display:none;" ><img style="margin:0 auto;" src="/img/loading.gif" /></div>
	<%@ include file="/WEB-INF/jsp/cmm/footer.jsp" %>
    <!-- E : footer -->
</div>
</body>
</html>
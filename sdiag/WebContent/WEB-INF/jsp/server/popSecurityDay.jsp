<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/cmm/title.jsp" %>
<script type="text/javascript" src="/js/jquery.form.js"></script>
 <link type="text/css" rel="stylesheet" href="/css/jquery-plugin.css" />
<script type="text/javascript" src="/js/jquery.bpopup.min.js"></script>

<style type="text/css">
.ly_box2 {
	width:940px;
	margin:0 auto;	
	text-align:center;
	padding:190px 20px 0px 20px;	
}
.ly_box2 li.ly_cell3 {
	padding:30px 0 10px 0;
	display:block;
	font-weight:bold;
	text-align:center;
	font-size:16px;
	line-height:40px;
}
.ly_box2 li.ly_cell3 span {
	font-size:16px;
	font-weight:normal;
}
.ly_box2 li.ly_cell3 p {
	font-size:16px;
}
.ly_block6 {width:982px;height:820px;display:inline-block; background:#5499de;}
.ly_block7 {width:982px;height:175px;display:inline-block;}
.security_tt {
	position:relative;
	height:300px;	
	background:url(/img/securityday_bg.png) no-repeat;
}
.security_tt span {
	position:absolute;
	bottom:30px;
	right:130px;
}
.security_tt span a {
	background:url(/img/btn_start_off.png) no-repeat;
	text-indent:-99999px;
	width:173px;
	height:53px;
	display:inline-block;
}
.security_tt span a:hover {
	background:url(/img/btn_start_on.png) no-repeat;	
}
.security_tt2 {
	position:relative;
	height:175px;	
	background:url(/img/securityday_test.png) no-repeat;
}
.security_tt2 span {
	position:absolute;
	top:20px;
	right:20px;
}
.security_tt2 span a {
	background:url(/img/btn_close.png) no-repeat;
	text-indent:-99999px;
	width:21px;
	height:21px;
	display:inline-block;
}
.security_box2 {
	width:962px;
	height:350px;
	margin:0 auto;	
	display:inline-block;
	text-align:center;
	padding:10px 0px 0px 20px;
	background:#ffffff;
}
.security_box {
	width:972px;
	height:400px;
	margin:0 auto;	
	display:inline-block;
	text-align:center;
	padding:10px 0px 0px 10px;
	background:#ffffff;
}
.security_cell1 {
	font-family:"맑은 고딕";
	font-size:30px;
	font-weight:bold;
	letter-spacing:-2px;
	color:#5499de;
	line-height:40px;
	padding:40px 0;
	display:inline-block;
}
.security_cell2 {
	width:100%;
	font-family:"맑은 고딕";
	font-size:15px;
	font-weight:bold;
	letter-spacing:-1px;
	color:#ffffff;	
	text-align:center;
	padding:35px 0 0 0;
}
.security_box table {
	width:600px;
	border:solid 1px #d7d7d7;
	margin:0 auto;
	font-family:"맑은 고딕";
	font-size:15px;
	color:#323232;
}
.security_box table th {
	border:solid 1px #d7d7d7;
	padding:20px 0;
	background:#f7f7f7;
}
.security_box table td {
	border:solid 1px #d7d7d7;
	padding:20px 20px;
	text-align:left;
}
.security_box2 li.ly_cell1 {
	padding:10px 0 0 0;
	display:block;
	font-weight:bold;
	text-align:left;
	font-size:15px;
}
.btn_black3 {position:relative;width:100%;float:right;padding:15px 0 10px 0;text-align:center;margin:10px 0 0 0;}
</style>
<script type="text/javascript" language="javascript">
$(function () {
	
	var reqCode = '${searchVO.reqCode}';
	if(reqCode == "3"){
		//window.self.close();
		//var pw = window.open('/securityDay/popSecurityDay.do?reqCode=4&formMod=R','securityDay3','width=400, height=300,scrollbars=yes, resizable=yes, menubar=no, status=no, toolbar=no');
		
	}	
	
	$.text_box = function(){
		
		var $this =$(".text_box1");
		var t = $this.text();
		$(".text_box1").html(t.replace('&lt;','<').replace('&gt;','>').replace('quot;','"'));
	};
	
	$("#start").click(function(){
		/* $("#reqCode").val("2");
		
		 goPage("/securityDay/popSecurityDay.do");
		 */
		//window.self.close();
		//var pw = window.open('/securityDay/popSecurityDay.do?reqCode=2','securityDay2','width=980, height=740,scrollbars=yes, resizable=yes, menubar=no, status=no, toolbar=no');
		$("#rowNum").val(1);
		$.saveResult();
		
		
	});
	
	
	$.q_next = function(){
		if($("#fileYn").val()=="Y"){
			if($("#file").val()==""){
				alert("증적자료를 첨부 하세요.");
				return false;
			}
				
		}
			
		
		if($("#question").val() != ""){
			$("#explainDiv").css("display","none");
			$("#questionDiv").css("display","");
		}else
			$.saveResult();
	};
	
	$.back = function(){
		$("#explainDiv").css("display","");
		$("#questionDiv").css("display","none");
	};
	
	
	
	
	$.answer_ch = function(){
		
			
		if($("#fileYn").val()=="Y" ){
			if($("#file").val()==""&& $("#resultYn").val()=="N"){
				alert("증적자료를 첨부 하세요.");
				return false;
			}else{
				$("#rowNum").val((parseInt($("#rowNum").val()))+1);
				$.saveResult();
			}
			//alert("1");
				
		}else if($("#question").val() != ""){
			var answer ="";
			//alert("2");
			if($("#answerType").val()=="2"){
				answer = $('input:radio[name=answerMc]:checked').val();
				var checked_length =  $('input:radio[name=answerMc]:checked').length;
				if(checked_length <= 0){
					alert('답을 선택하세요.');
					return false;
				}
				 
			}
			else{ 
				answer = $("#answerText").val();
				if(answer ==""){
					alert("답을 입력하세요.");
					$("#answerText").focus();
					return false;
				}
			}
			
			var q_answer = $("#q_answer").val();
			
			if(answer == q_answer){
				$("#answer").val(answer);
				alert("정답입니다.");
				$("#rowNum").val((parseInt($("#rowNum").val()))+1);
				$.saveResult();
			}else{
				alert("문항을 다시 한번 읽어보세요.");
				$("#answer").val("");
				return false;
			}
		}else{
			$("#rowNum").val((parseInt($("#rowNum").val()))+1);
			$.saveResult();
		}
		
	};
	
	$.end = function(){
		//window.self.close();
		window.parent.location.reload();
		window.parent.$("#popuplayer").bPopup().close();
		
	};
	
	$.close = function(){ 
		//window.self.close();
		//window.parent.location.reload();
		window.parent.$("#popuplayer").bPopup().close();
		var str ="";
		str +="<div class='popupContent'></div>";
		window.parent.$("#popuplayer").html(str);
		
	};
	
	
	$.saveResult = function(gubun){
		/* var formData = new FormData($("#sdForm")[0]);
		
		if($("#answerType").val()=="2")
			 answer = $('input:radio[name=answerMc]:checked').val();
		else 
			answer = $("#answerText").val();
	
		formData.append('reqCode', $("#reqCode").val());
		
		if($("fileYn").val() =='Y')
			formData.append('file', $("#file")[0].files[0]);
		
		
		formData.append('answer', answer);
		formData.append('questionNum', $("#questionNum").val());
		formData.append('clGroupNo', $("#clGroupNo").val());
		formData.append('sdCheckNo', $("#sdCheckNo").val());
		formData.append('empNo', $("#empNo").val());
		formData.append('fileYn', $("#fileYn").val());
		formData.append('formMod', $("#formMod").val());

		
		
		alert("왔음?");
		
		 $.ajax({
			url: '/securityDay/sdResultInfoSave.do',
			data: formData,
			type: 'POST',
			dataType: 'json',
			mimrType : 'multipart/form-data',
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
					alret ("예~!!!!!!!!");
				}else{alert(data.MSG); }				
			}
		}); */
		 
		 /* var url ="";
		
		if($("#reqCode") == "1"){
			url = "securityDay/popSecurityDay.do";
		}else{
			url ="/securityDay/sdResultInfoSave.do";
		} */
			
		 
		
		 var options = {
				 url: '/securityDay/sdResultInfoSave.do',
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
							/* goForm("/securityDay/sdCheckListForm.do"); */
							var str = "";
							
							if(data.searchVO.reqCode == "3"){								
								/* str +="<div class='ly_box2 sd_back'>";
								str +="<li class='ly_cell3'> 	Security Day<br/><br/>점검이 완료되었습니다.</li>";
								str +="</div><div class='btn_black3 btn_cancle'><a onclick='$.end();' class='btn_black'><span>닫기</span></a></div>"; */
								
								
							str +="	<div class='ly_block6'>";
							str +="    <div class='security_tt'>";
							str +="    </div>";
							str +="    <div class='security_box'>";
							str +="    	<div class='security_cell1'>";
							str +="        	Security Day<br/><br/>점검이 완료되었습니다.<br/>";
							str +="        </div>";
							str +="        <div class='btn_black3 btn_cancle'><a onclick='$.end();' class='btn_black'><span>닫기</span></a></div>";
							str +="    </div>";
							str +="</div>";
								
								/* str +="<div class='ly_block6'>";
								str +="<div class='security_tt'>";
							    </div>
							    <div class='security_box'>
							    	<div class='security_cell1'>
							        	${ sdResultInfo.checkStartDay} ~ ${ sdResultInfo.checkEndDay}은<br/>Security Day 정보보안 자가점검 기간입니다.<br/>
							        </div>
							        <table cellpadding='0' cellspacing='0'>
							        	<tr>
							            	<th>점검 사항</th>
							                <td>보관중인 불필요한 정보 삭제 <br/>(특히, 인수인계후)</td>
							            </tr>
							        	<tr>
							            	<th>지수화 시작일자 </th>
							                <td>${ sdResultInfo.idxStartDay}</td>
							            </tr>            
							        </table>
							    </div>
							    <div class='security_cell2'>시행기간 후 수준진단 점수에 반영되오니, 반드시 기간 내 점검완료 하시기 바랍니다.</div>           
							</div> */
							}else{
								$("#questionNum").val(data.sdResultInfo.questionNum);
								$("#clGroupNo").val(data.sdResultInfo.clGroupNo);
								$("#sdCheckNo").val(data.sdResultInfo.sdCheckNo);
								$("#empNo").val(data.sdResultInfo.empNo);
								$("#fileYn").val(data.sdResultInfo.fileYn);
								$("#formMod").val(data.searchVO.formMod);
								$("#reqCode").val(data.searchVO.reqCode);
								$("#q_answer").val(data.sdResultInfo.answer);
								$("#question").val(data.sdResultInfo.question);
								$("#answerType").val(data.sdResultInfo.answerType);
								$("#rowNum").val(data.sdResultInfo.rowNum);
								$("#resultYn").val(data.sdResultDetailInfo.resultYn);
								//str +="<div class='subTT'><span>SECURITY DAY</span> <a onclick='$.close()' style='margin-top: 10px; margin-right: 10px; float: right; cursor: pointer;'><img alt='' src='/img/ic_delete3.png'></a></div>";
								str +="<div  id='explainDiv'>";
								str +="<div class='ly_block5' ><div class='security_tt2'><span><a style='cursor:pointer;' onclick='$.close()'></a></span></div>";
								str +="<div class='ly_box1'>";
								str +="<li>";
								str +="<div class='text_box1' style='height:435px;'>"+data.sdResultInfo.explain+"</div></li>";
							    if(data.sdResultInfo.fileYn == 'Y'){
							    	str +="<li class='ly_cell1'>"+data.sdResultInfo.fileText+"</br>";
							    	str +="<input type='file' type='file' id='file' name='file' maxlength='24' class='srh' style='width:200px' />";
							    	str +="</li>";
							    }else{
							    	if(data.sdResultInfo.question !=""){
									    str +="<li class='ly_cell1'>Q."+data.sdResultInfo.questionNum+"  "+data.sdResultInfo.question+"</br></li>";
									    if(data.sdResultInfo.answerType !="" && data.sdResultInfo.answerType != null){
										    if(data.sdResultInfo.answerType == "1"){
										    	str +="<li class='ly_cell1'><input type='text' maxlength='24' class='srh' style='width:928px' id='answerText' name='answerText' value='"+data.sdResultDetailInfo.answer+"' /></li>";
										    }else{
										    	str +="<li class='ly_cell1 radio_button'>";
										    	for(var i =0; i<data.mcList.length; i++){
										    		str += "<label><input type='radio' id ='answerMc' name='answerMc' value='"+data.mcList[i].exNum+"'";
										    		if(data.mcList[i].exNum == data.sdResultDetailInfo.answer){
										    			str +="checked='checked'";
										    		}
										    		str +="/> <span>"+data.mcList[i].exNum+". "+data.mcList[i].exText+"</span></label><br/>";
										    	}
										    	str +="</li>";
										    }
									    }
							    	}
							    }
							    
							    str +="</div>";
							    str +="<div class='btn_black3'>";
							    //str +="<a class='btn_black' onclick='$.back();' style='margin-right: 15px;'><span>이전</span></a>"; 
							    if(data.sdResultInfo.rowNum>1){
							    	str +="<a class='btn_black'  style='margin-right:70px;'  onclick='$.back();'><span>이전</span></a>"; 	
							    }
							    str +="<a class='btn_black'  onclick='$.answer_ch();'><span>다음</span></a>"; 
							    str +="</div>";
							    str +="</div>";
							    
							   /*  str +="<div class='btn_black3'>";
							    
							    str +="</div>";
							    str +="</div>";
							    str +="<div id='questionDiv' style='display: none' >";
							    str +="<div class='ly_box1'>";
							    str +="<li class='ly_cell2'>Q."+data.sdResultInfo.questionNum+"</li>";
							    str +="<li><div class='text_box2'>"+data.sdResultInfo.question+"</div></li>";
							   
							    
							    
							    
							    str +="</div>";
							    str +="<div class='btn_black3'>";
							    str +="<a class='btn_black' onclick='$.back();' style='margin-right: 15px;'><span>이전</span></a>"; 
							    str +="<a class='btn_black' onclick='$.answer_ch();'><span>다음</span></a>"; 
							    str +="</div>";
							    str +="</div>"; */
							}
							$("#sdDiv").html(str);
							$.text_box();
						}else{alert(data.MSG); }				
					} 
		 };
		$("#sdForm").ajaxForm(options).submit();
	};
	
	
	$.back = function(){
		$("#rowNum").val((parseInt($("#rowNum").val()))-1);
		
		
		var data = {
				rowNum:$("#rowNum").val(),
				empNo:$("#empNo").val(),
				reqCode:$("#reqCode").val() 				
		}
		
		 $.ajax({
			url: '/securityDay/sdBackInfo.do',
			data: data,
			type: 'POST',
			dataType: 'json',
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
					/* goForm("/securityDay/sdCheckListForm.do"); */
					var str = "";
					
					if(data.searchVO.reqCode == "3"){								
						/* str +="<div class='ly_box2 sd_back'>";
						str +="<li class='ly_cell3'> 	Security Day<br/><br/>점검이 완료되었습니다.</li>";
						str +="</div><div class='btn_black3 btn_cancle'><a onclick='$.end();' class='btn_black'><span>닫기</span></a></div>"; */
						
						
					str +="	<div class='ly_block6'>";
					str +="    <div class='security_tt'>";
					str +="    </div>";
					str +="    <div class='security_box'>";
					str +="    	<div class='security_cell1'>";
					str +="        	Security Day<br/><br/>점검이 완료되었습니다.<br/>";
					str +="        </div>";
					str +="        <div class='btn_black3 btn_cancle'><a onclick='$.end();' class='btn_black'><span>닫기</span></a></div>";
					str +="    </div>";
					str +="</div>";
						
						/* str +="<div class='ly_block6'>";
						str +="<div class='security_tt'>";
					    </div>
					    <div class='security_box'>
					    	<div class='security_cell1'>
					        	${ sdResultInfo.checkStartDay} ~ ${ sdResultInfo.checkEndDay}은<br/>Security Day 정보보안 자가점검 기간입니다.<br/>
					        </div>
					        <table cellpadding='0' cellspacing='0'>
					        	<tr>
					            	<th>점검 사항</th>
					                <td>보관중인 불필요한 정보 삭제 <br/>(특히, 인수인계후)</td>
					            </tr>
					        	<tr>
					            	<th>지수화 시작일자 </th>
					                <td>${ sdResultInfo.idxStartDay}</td>
					            </tr>            
					        </table>
					    </div>
					    <div class='security_cell2'>시행기간 후 수준진단 점수에 반영되오니, 반드시 기간 내 점검완료 하시기 바랍니다.</div>           
					</div> */
					}else{
						$("#questionNum").val(data.sdResultInfo.questionNum);
						$("#clGroupNo").val(data.sdResultInfo.clGroupNo);
						$("#sdCheckNo").val(data.sdResultInfo.sdCheckNo);
						$("#empNo").val(data.sdResultInfo.empNo);
						$("#fileYn").val(data.sdResultInfo.fileYn);
						$("#formMod").val(data.searchVO.formMod);
						$("#reqCode").val(data.searchVO.reqCode);
						$("#q_answer").val(data.sdResultInfo.answer);
						$("#question").val(data.sdResultInfo.question);
						$("#answerType").val(data.sdResultInfo.answerType);
						$("#rowNum").val(data.sdResultInfo.rowNum);
						$("#resultYn").val(data.sdResultDetailInfo.resultYn);
						//str +="<div class='subTT'><span>SECURITY DAY</span> <a onclick='$.close()' style='margin-top: 10px; margin-right: 10px; float: right; cursor: pointer;'><img alt='' src='/img/ic_delete3.png'></a></div>";
						str +="<div  id='explainDiv'>";
						str +="<div class='ly_block5' ><div class='security_tt2'><span><a style='cursor:pointer;' onclick='$.close()'></a></span></div>";
						str +="<div class='ly_box1'>";
						str +="<li>";
						str +="<div class='text_box1' style='height:435px;'>"+data.sdResultInfo.explain+"</div></li>";
					    if(data.sdResultInfo.fileYn == 'Y'){
					    	str +="<li class='ly_cell1'>"+data.sdResultInfo.fileText+"</br>";
					    	str +="<input type='file' type='file' id='file' name='file' maxlength='24' class='srh' style='width:200px' />";
					    	str +="</li>";
					    }else{
					    	if(data.sdResultInfo.question !=""){
							    str +="<li class='ly_cell1'>Q."+data.sdResultInfo.questionNum+"  "+data.sdResultInfo.question+"</br></li>";
							    if(data.sdResultInfo.answerType !="" && data.sdResultInfo.answerType != null){
								    if(data.sdResultInfo.answerType == "1"){
								    	str +="<li class='ly_cell1'><input type='text' maxlength='24' class='srh' style='width:928px' id='answerText' name='answerText' value='"+data.sdResultDetailInfo.answer+"' /></li>";
								    }else{
								    	str +="<li class='ly_cell1 radio_button'>";
								    	for(var i =0; i<data.mcList.length; i++){
								    		str += "<label><input type='radio' id ='answerMc' name='answerMc' value='"+data.mcList[i].exNum+"'";
								    		if(data.mcList[i].exNum == data.sdResultInfo.answer){
								    			str +="checked='checked'";
								    		}
								    		str +="/> <span>"+data.mcList[i].exNum+". "+data.mcList[i].exText+"</span></label><br/>";
								    	}
								    	str +="</li>";
								    }
							    }
					    	}
					    }
					    
					    str +="</div>";
					    str +="<div class='btn_black3'>";
					    //str +="<a class='btn_black' onclick='$.back();' style='margin-right: 15px;'><span>이전</span></a>"; 
					    if(data.sdResultInfo.rowNum>1){
					    	str +="<a class='btn_black' style='margin-right:70px;'   onclick='$.back();'><span>이전</span></a>"; 	
					    }
					    str +="<a class='btn_black'  onclick='$.answer_ch();'><span>다음</span></a>"; 
					    str +="</div>";
					    str +="</div>";
					    
					   /*  str +="<div class='btn_black3'>";
					    
					    str +="</div>";
					    str +="</div>";
					    str +="<div id='questionDiv' style='display: none' >";
					    str +="<div class='ly_box1'>";
					    str +="<li class='ly_cell2'>Q."+data.sdResultInfo.questionNum+"</li>";
					    str +="<li><div class='text_box2'>"+data.sdResultInfo.question+"</div></li>";
					   
					    
					    
					    
					    str +="</div>";
					    str +="<div class='btn_black3'>";
					    str +="<a class='btn_black' onclick='$.back();' style='margin-right: 15px;'><span>이전</span></a>"; 
					    str +="<a class='btn_black' onclick='$.answer_ch();'><span>다음</span></a>"; 
					    str +="</div>";
					    str +="</div>"; */
					}
					$("#sdDiv").html(str);
					$.text_box();
				}else{alert(data.MSG); }				
			}
		});
    
	};
	
	
	
	
	
	
	
	function goPage(url) {

	    document.sdForm.action = "<c:url value='"+url+"'/>";
	    document.sdForm.method = 'post';
	    document.sdForm.submit();
	}
	
	$.text_box();
	
});



</script>

 

</head>

<body style="min-width: 0px;">
<!-- S : contents -->
	<div class="ly_block5">
	<!-- S : center contents -->
     
    <form:form  id="sdForm" name="sdForm" method="post"  enctype="multipart/form-data">
    <div id="sdDiv">
    	<c:if test="${searchVO.reqCode eq '1' }">
	    <div class="ly_block6">
		    <div class="security_tt">
		    	<span><a id = "start" style="cursor:pointer;">start</a></span>
		    </div>
		    <div class="security_box">
		    	<div class="security_cell1">
		        	${ sdResultInfo.checkStartDay} ~ ${ sdResultInfo.checkEndDay}은<br/>Security Day 정보보안 자가점검 기간입니다.<br/>
		        </div>
		        <table cellpadding="0" cellspacing="0">
		        	<tr>
		            	<th>점검 사항</th>
		                <td>${sdResultInfo.clGroupNm }</td>
		            </tr>
<%-- 		        	<tr>
		            	<th>점수 반영일 </th>
		                <td>${ sdResultInfo.idxStartDay}</td>
		            </tr>            
 --%>		        </table>
		    </div>
		    <div class="security_cell2">시행기간 후 수준진단 점수에 반영되오니, 반드시 기간 내 점검완료 하시기 바랍니다.</div>           
		</div>
		
    	</c:if>
    	
    	<c:if test="${searchVO.reqCode eq '2' }">
    	<div class="subTT"><span>SECURITY DAY</span> <a onclick="$.close()" style="margin-top: 10px; margin-right: 10px; float: right; cursor: pointer;"><img alt="" src="/img/ic_delete3.png"></a></div>
    	  	<div  id="explainDiv">
		    	<div class="ly_box1" >
			    	<div class="text_box1">${ sdResultInfo.explain}</div>
			    	<c:if test="${sdResultInfo.fileYn eq 'Y' }">
			    	<li class="ly_cell1">증적자료 첨부 
			    		<input type="file" type="file" id="file" name="file" maxlength="24" class="srh" style="width:200px" />  
			    	</li>
			    	</c:if>
		    	</div>
		    	<div class="btn_black3">
		    		
		    		<a class="btn_black" onclick="$.q_next();"><span>다음</span></a> 
		    	</div>
	    	</div>
	    	
	    	
	    	<div id="questionDiv" style="display: none" >
		    	<div class="ly_box1">
			    	<li class="ly_cell2">Q.${ sdResultInfo.questionNum}</li>
			    	<li><div class="text_box2">${ sdResultInfo.question}</div></li>
			    	<li class="ly_cell1">답을 입력하세요.</li>
			    	<c:if test="${sdResultInfo.answerType eq '1'}">
			        <li class="ly_cell1"><input type="text" maxlength="24" class="srh" style="width:928px" id="answerText" name="answerText"  /></li>
			        </c:if>
			        <c:if test="${sdResultInfo.answerType eq '2'}">
			        <li class="ly_cell1 radio_button">
			        	<c:forEach var="result" items="${mcList}" varStatus="status">
			            <label><input type="radio"  id ="answerMc" name='answerMc' value='${result.exNum}'/> <span>${result.exNum}. ${result.exText}</span></label><br/>
			            </c:forEach>
			        </li>
			        </c:if>
			    </div>
		    	<div class="btn_black3">
		    		<a class="btn_black" style='margin-right:70px;' onclick="$.back();"><span>이전</span></a> 
		    		<a class="btn_black" onclick="$.answer_ch();"><span>확인</span></a> 
		    	</div>
	    	</div>
    	
    	</c:if>
    	
    	<c:if test="${searchVO.reqCode eq '4' }">
    	<div class="ly_box2">
	    	<li class="ly_cell3 ">
	        	Security Day<br/><br/>
		            점검이 완료되었습니다.
		     </li>
	    </div>
	    <div class="btn_black3 btn_cancle"><a onclick="$.end();" class="btn_black"><span>닫기</span></a></div>
    	</c:if>
    	</div>
    	
    	<input type="hidden" id="reqCode" name="reqCode" value="${searchVO.reqCode}" />
    	<input type="hidden" id="q_answer" name="q_answer" value="${sdResultInfo.answer}" />
    	<input type="hidden" id="questionNum" name="questionNum" value="${sdResultInfo.questionNum}" />
    	<input type="hidden" id="question" name="question" value="${sdResultInfo.question}" />
    	<input type="hidden" id="clGroupNo" name="clGroupNo" value="${sdResultInfo.clGroupNo}" />
    	<input type="hidden" id="sdCheckNo" name="sdCheckNo" value="${sdResultInfo.sdCheckNo}" />
    	<input type="hidden" id="empNo" name="empNo" value="${sdResultInfo.empNo}" />
    	<input type="hidden" id="answerType" name="answerType" value="${sdResultInfo.answerType}" />
    	<input type="hidden" id="answer" name="answer" value="" />
    	<input type="hidden" id="formMod" name="formMod" value="${searchVO.formMod}" />
    	<input type="hidden" id="fileYn" name="fileYn" value="${sdResultInfo.fileYn}" />
    	<input type="hidden" id="rowNum" name="rowNum" value="${sdResultInfo.rowNum}" />
    	<input type="hidden" id="resultYn" name="resultYn" value="${sdResultInfo.resultYn}" />
    	
    	
		</form:form>
    </div>
    <!-- E : center contents -->
</div>
</body>
</html>
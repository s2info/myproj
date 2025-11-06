<%--
  Class Name : dashboard.jsp
  Description : dashboard 화면
  Modification Information
 
      수정일         수정자                   수정내용
    -------    		--------    		---------------------------
     2009.03.01    lee.c.j             		최초 생성
 
    author   : dashboard
    since    : 2015.10.12
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
	
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<%@ include file="/WEB-INF/jsp/cmm/title.jsp" %>
 <link type="text/css" rel="stylesheet" href="/css/jquery-plugin.css" />
 <link rel="stylesheet" type="text/css" href="/css/jquery.jqplot.min.css" /> 
	<style>
		#container_gaug {
			width: 250px;
			height: 200px;
			display: inline-block;
			margin: 1em;
		}
		
		.jqplot-highlighter-tooltip {
			background-color: #2c2c2c;
			padding: 7px;
			color: #fff;
			font-size: 0.9em;
		}
		
		.chart-container {
			border: 0px solid darkblue;
			padding: 0px 0px 0px 0px;
			margin: 10px 0px 10px 20px;
			width: auto;
			height: auto;
		}
		
		table.jqplot-table-legend {
			font-size: 0.65em;
			line-height: 2.5em;
			margin: 0px 0px 10px 15px;
			background-color: rgb(228, 228, 228);
			font-family: '돋움, Dotum, Verdana, sans-serif';
		}
		
		td.jqplot-table-legend-label {
			width: 8em;
			font-size: 1.65em;
			padding: 0px 0px 0px 5px;
			color: #000;
		}
		
		div.jqplot-table-legend-swatch {
			border-width: 6px 10.5px;
			margin: 0px 0px 0px 3px;
		}
		
		div.jqplot-table-legend-swatch-outline {
			border: none;
		}
		
		#chart5 {
			width: 80%;
			height: 80%;
		}
		
		td.controls li {
			list-style-type: none;
		}
		
		td.controls ul {
			margin-top: 0.5em;
			padding-left: 0.2em;
		}
		
		pre.code {
			margin-top: 45px;
			clear: both;
		}
		.blockcell1 {width:150px;display:inline-block;font-weight:bold;}

	</style>
	<script type="text/javascript" src="/js/js/Chart/gaugeSVG.js"></script>
	<script type="text/javascript" src="/js/js/jquery.jqplot.js"></script>
    <script type="text/javascript" src="/js/js/Chart/shCore.min.js"></script>
    <script type="text/javascript" src="/js/js/Chart/shBrushJScript.min.js"></script>
    <script type="text/javascript" src="/js/js/Chart/shBrushXml.min.js"></script>
    <script type="text/javascript" src="/js/js/Chart/jqplot.dateAxisRenderer.min.js"></script>
    <script type="text/javascript" src="/js/js/Chart/jqplot.logAxisRenderer.min.js"></script>
    <script type="text/javascript" src="/js/js/Chart/jqplot.canvasTextRenderer.min.js"></script>
    <script type="text/javascript" src="/js/js/Chart/jqplot.canvasAxisTickRenderer.min.js"></script>
    <script type="text/javascript" src="/js/js/Chart/jqplot.highlighter.js"></script>
    <script language="javascript" type="text/javascript" src="/js/js/Chart/jqplot.canvasOverlay.min.js"></script>
	<script type="text/javascript" src="/js/js/Chart/jqplot.enhancedLegendRenderer.min.js"></script>
    
<!--[if lte IE 9]><script language="javascript" type="text/javascript" src="/js/excanvas.js"></script><![endif]-->	

</head>

<body>

<!-- S : header -->
	<!-- Top Menu Begin -->
	<%@ include file="/WEB-INF/jsp/cmm/topmenu.jsp" %>
	<!-- Top Menu End -->
<!-- E : user info -->
<!-- S : contents -->
<div id="cnt">
	<!-- S : left contents -->
	<div id="cnt_L">
    	<!-- S : socre -->
    	<div class="block_score">

						<div style="margin-left: 8px; margin-right: auto; width:auto; height:auto; margin-top:-13px">
						<div id="container_gaug"></div>
						</div>

		</div>
        <div class="block1">
        	<li class="blockcell1">보안등급</li>
        	<c:if test="${curScoreUserStat == '양호'}" >
            <li class="blockcell2"><img src="/img/icon_class_1.png" /> ${curScoreUserStat}</li>
            </c:if>
        	<c:if test="${curScoreUserStat == '주의'}" >
            <li class="blockcell2"><img src="/img/icon_class_2.png" /> ${curScoreUserStat}</li>
            </c:if>
        	<c:if test="${curScoreUserStat == '위험'}" >
            <li class="blockcell2"><img src="/img/icon_class_3.png" /> ${curScoreUserStat}</li>
            </c:if>
        </div>
        <div class="block1">
        	<li class="blockcell1">${fn:substring(curScoreUserOrgName, 0, 9)}<c:if test="${fn:length(result.title) > 9}" >..</c:if></li>
            <li class="blockcell2">${curScoreUserInfo}</li>
        </div>
        <!-- E : socre -->	        
        <!-- S : notice -->
        <h4>Notice</h4>
        <div class="indexnotice">
        	<div class="mnoticeTT"><span><img src="/img/icon_notice.png" /> 공지사항 </span><a href="/man/noticelist.do">+ more</a></div>
            <ul class="mnotice">
            <c:forEach var="result" items="${noticeList}" varStatus="status">
            	<li><a style='cursor:pointer;' class='dash_notice_view' nseq='${result.sq_no}'>${fn:substring(result.title, 0, 15)}<c:if test="${fn:length(result.title) > 15}" >...</c:if></a> <span>${result.upd_date}</span></li>
            </c:forEach>
            </ul>            
        </div>
        <!-- E : notice-->
        <!-- S : qustions -->
        <h4>Questions</h4>
        <div class="indexnotice2">
        	<div class="mnoticeTT"><span><img src="/img/icon_question.png" /> FAQ </span><a href="/man/faqlist.do">+ more</a></div>
            <ul class="mnotice">
            <c:forEach var="result" items="${faqList}" varStatus="status">
           		<li><a style='cursor:pointer;' class='dash_faq_view' nseq='${result.sq_no}'>${fn:substring(result.title, 0, 15)}<c:if test="${fn:length(result.title) > 15}" >...</c:if></a> <span>${result.upd_date}</span></li>
            </c:forEach>
            </ul>            
        </div>
        <!-- E : qustions -->               
    </div>
    <!-- E : left contents -->
    <!-- S : center contents -->
    <div id="cnt_R">
    	<div class="index_block1">
        	<div class="index_block1cell"><img src="/img/icon_index.png" />보안수준진단 추이 그래프 [${mindate } ~ ${maxdate }]</div>
			<div class="chart-container" id="chart5"></div>	
        </div>
        <div class="block_warp">
            <div class="index_block2"  ${not empty total ? "style='margin-bottom: 10px; height:460px; '": "style='margin-bottom: 10px; '"}>
                <div class="index_block2cell">
                	<li class="cell1"><img src="/img/icon_index.png" />진단결과 [${maxdate }]</li>
                    <!-- <li class="cell2">평균점수 <span class="txcell1"><c:out value="${avgscore }" />점</span></li>-->
                </div>
                <div class="graph4 result_list">
                <c:if test="${not empty total}" >
                	<div id="diagresult" class="${endClass} diag_majr" diagDesc="${total.diag_desc}" diagcode="${total.diag_majr_code}" buseoindc="${total.buseo_indc}" style="cursor:pointer;">
                	<a>
	                    	<li class="datecell1"><img src="/img/img_icon1.png" /><span>${total.diag_desc}</span></li>
	                        <li class="datecell2">${total.count}건 / <span>${total.avg_score}점</span></li>
	                    </a>
                   	</div>
                </c:if>
            	<c:forEach var="result" items="${polcount}" varStatus="status">
            		<c:if test="${status.getCount() < listsize }" >
                	    <div id="diagresult" class="${startClass} diag_majr" diagDesc="${result.diag_desc}" diagcode="${result.diag_majr_code}" buseoindc="${result.buseo_indc}" style="cursor:pointer;">
                    </c:if>
                    <c:if test="${status.getCount() == listsize }" >
                    	<div id="diagresult" class="${endClass} diag_majr" diagDesc="${result.diag_desc}" diagcode="${result.diag_majr_code}" buseoindc="${result.buseo_indc}" style="cursor:pointer;">
                    </c:if>
		                    <a>
		                    	<li class="datecell1"><img src="/img/img_icon1.png" /><span>${result.diag_desc}</span></li>
		                        <li class="datecell2">${result.count}건 / <span>${result.avg_score}점</span></li>
		                    </a>
                    	</div>
                </c:forEach>
                </div>
            </div>
            <form id="searchVO" name="listForm" action="asdf" method="post">
            <input type="hidden" name="majrCode" id="majrCode" value="" />
            <input type="hidden" name="buseoIndc" id="buseoIndc" value="" />
            <input type="hidden" name="majrName" id="majrName" value="" />
            <input type="hidden" name="majCode" id="majCode" value="" />
            <input type="hidden" name="minCode" id="minCode" value="" />
            <input type="hidden" name="polCode" id="polCode" value="" />
            <input type="hidden" name="buseoType" id="buseoType" value="N" />
            </form>
            
            <div class="index_block3" ${not empty total ? "style='margin-bottom: 10px; height:460px; '": "style='margin-bottom: 10px; '"}>
            	<div class='list_contents_body'></div>
            </div>
            <div> <p>※ 정책별 평균점수는 정책별 가중치로 인해서 합산점수의 평균과 다를수 있습니다.</p></div>
        </div>
    </div>
    <!-- E : center contents -->
    
<script type="text/javaScript" language="javascript">
$(function () {	
	$('.list_contents_body').on('click', '.btn_pol_status', function(){
		$('#majCode').val($(this).attr('majCode'));
		$('#minCode').val($(this).attr('minCode'));
		$('#polCode').val($(this).attr('polCode'));
		
		goPage("/pol/policystatus.do");
	});
	
	
	$('.diag_majr').click(function(){
			
		$('#majrCode').val($(this).attr('diagcode'));
		$('#majrName').val($(this).attr('diagDesc'));
		$('#buseoIndc').val($(this).attr('buseoindc'));
		
		/*$('.diag_majr').each(function(){
			if($(this).hasClass('diag_majrSel')){
				$(this).removeClass('diag_majrSel');
			}
		});	
		
		$(this).addClass('diag_majrSel');
		*/
		
		//$(this).find('a').addClass('ON');
		$(this).siblings().removeClass('on');
		$(this).addClass('on');
		searchList(1);	
		
	});	


   function searchList(page)
   {
   	
   	var majrCode=$('#majrCode').val();
   	var buseoIndc=$('#buseoIndc').val();
   	var majrName=$('#majrName').val();
   	var data = {
   			majrCode:majrCode,
   			buseoIndc:buseoIndc,
   			majrName:majrName};
       $.ajax({
		url : '/report/diagMajrAjax.do',
		data : data,
		type : 'POST',
		dataType : 'json',
		error : function(jqXHR, textStatus, errorThrown) {
			if(jqXHR.status == 401){
				alert("인증정보가 만료되었습니다.");
				location.href="/";
			}else{
				alert(textStatus + "\r\n" + errorThrown);	
			}
		},
		success : function(data) {
			if (data.ISOK) {
				$('.list_contents_body').empty().append(data.strList);
			} else {
				alert("오류"+data.MSG);
			}

		},
		beforeSend: function () {
			$('#loading').find('img').attr('width', '100px').attr('height', '100px');
          	 	var padingTop = (Number(($('.list_contents_body').css('height')).replace("px","")) / 2) + 50;
               $('#loading').css('position', 'absolute');
               $('#loading').css('left', $('.list_contents_body').offset().left + ($('.list_contents_body').css('width').replace("px","") / 2) - 130);
               $('#loading').css('top', $('.list_contents_body').offset().top);
             
               $('#loading').css('padding-top', 100);
               
               $('#loading').show().fadeIn('fast');
          },
          complete: function () {
              $('#loading').hide().fadeOut(1000);
          }

	});
   }    

   function createGauge(){
	   var ckvalue = ${curScore};
		if (ckvalue <= 0){ckvalue = 0;} // display value 0 : 50 -- 1
	   var gauge = new GaugeSVG({
			id: "container_gaug",
			value: ckvalue,
			canvasBackColor: "#e5e5e5",
			title: "보안점수",
			label: "SCORE",
			labelColor: "#2C2C2C",
			labelScale: 3,
			gaugeWidthScale: 1.0,
			borderColor: "#ff5533",
			min: 0,
			max: 100,
			minmaxColor: "#2C2C2C",
			lowerActionLimit:${gaugeValue.danger},
			lowerWarningLimit:${gaugeValue.danger},
	   		upperWarningLimit:${gaugeValue.warning},
			upperActionLimit:100,
			showMinMax: true,
			needleColor: "#080808",
			optimumRangeColor: "#FEAB35",
			warningRangeColor: "#48CB01",
			actionRangeColor: "#E00000",

			gaugeBackColor: "#ccc"
		});
   }
   
   function createChart(){
	   $.jqplot._noToImageButton = true;
		var line0 = ${chartList0};
		var line1 = ${chartList1};
		var line2 = ${chartList2};
		var line3 = ${chartList3};
		var line4 = ${chartList4};
		var line5 = ${chartList5};
		var line_per = ${chartList_per};
		var ticks = ["4월", "5월", "6월", "7월", "8월", "9월", "10월"];
	
	    $.jqplot('chart5', [${plotLines}], {
	    	 seriesColors:[${seriesLegendText}],	    	
            highlighter: {
                show: true,
                sizeAdjust: 1,
                tooltipOffset: 9
            },
            grid: { background: '#F5F5F5', borderColor: 'transparent', shadow: false, drawBorder: false, shadowColor: 'transparent' },                
            legend: {
                show: true,
                placement: 'outside',
                renderer: $.jqplot.EnhancedLegendRenderer
            },
            seriesDefaults: {
                rendererOptions: {
                    smooth: true,
                    animation: {
                        show: true
                    }
                },
                showMarker: false,
                lineWidth: 1.65
                
            },
            axesDefaults: {
                rendererOptions: {
                    baselineWidth: 1.5,
                    baselineColor: '#888',
                    drawBaseline: true
                }
            },
            axes: {
                xaxis: {
                    renderer: $.jqplot.DateAxisRenderer,
                    tickRenderer: $.jqplot.CanvasAxisTickRenderer,
                    tickOptions: {
                        //formatString: "%b %e",
                        formatString: '%#m/%#d',
                        angle: -30,
                        textColor: '#666',
                        fontSize: '9pt',
                        fontFamily: '돋움, Dotum, Verdana, sans-serif'
                    },
                    min: "${minday}",
                    max: "${maxday}",
                    tickInterval: "${tickInterval}",
                    //tickInterval: "1 months",
                    drawMajorGridlines: true
                },
	            yaxis: {
	                tickOptions: {
	                	
	                    angle: 0,
	                    fontSize: '9pt',
	                    fontFamily: '돋움, Dotum, Verdana, sans-serif',
	                    showMark: false,
	                    showGridline: true,
	                    textColor: '#666'
	                }
	            }                    
                /*
                yaxis: {
                    renderer: $.jqplot.LogAxisRenderer,
                    pad: 0,
                    rendererOptions: {
                        minorTicks: 1
                    },
                    tickOptions: {
                        formatString: "%d",
                        showMark: false,
                        textColor: '#000'
                    }
                }
                */
            }
	         , series: [${labelList}]
	        , grid: { background: '#F5F5F5', borderColor: 'transparent', shadow: false, drawBorder: false, shadowColor: 'transparent' },
	        canvasOverlay: {
	            show: true,
	            objects: [
	                {dashedHorizontalLine: {
	                    name: 'wilma',
	                    y: ${basepoint},
	                    lineWidth: 2,
	                    //dashPattern: [16, 12],
	                    lineCap: 'round',
	                    xOffset: '54',
	                    xminOffset: '8px',
	                    xmaxOffset: '8px',
	                    color: '#fa5d58',
	                    shadow: false
	                   
	                }}
	            ]
	        }
	        
	     });
   }
   
   $(window).bind("load", function() {
   	var firstdiv = $('.result_list').find('div').eq(0);
   	$('#majrCode').val(firstdiv.attr('diagcode'));
	$('#buseoIndc').val(firstdiv.attr('buseoindc'));
	$('#majrName').val(firstdiv.attr('diagDesc'));
   	
   	searchList(1);
   	createGauge();
   	createChart();
   	
   	$('.graph4').find('div').eq(0).addClass('on');
	});
    
});

function goPage(url) {

    document.forms["listForm"].action = url;
    document.forms["listForm"].method = 'post';
    document.forms["listForm"].submit();
}
</script>
    
    <!-- S : footer -->
	<!-- footer -->
	<div id="wrap_footer">
		<%@ include file="/WEB-INF/jsp/cmm/footer.jsp" %>
	</div>
	<!-- footer end -->
    <!-- E : footer -->
    <div id="loading" style="display:none;" ><img style="margin:0 auto;" src="/img/loading.gif" /></div>
	<div class='DialogBox'>
	
	</div>    
</div>
<!-- E : contents -->

</body>

</html>
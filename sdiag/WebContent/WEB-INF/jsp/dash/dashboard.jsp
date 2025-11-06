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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<%@ include file="/WEB-INF/jsp/cmm/title.jsp" %>
 <link type="text/css" rel="stylesheet" href="/css/jquery-plugin.css" />
 <link rel="stylesheet" type="text/css" href="/css/jquery.jqplot.min.css" /> 
	<style>
	      #container_gaug {
	        width:250px; height:200px;
	        display: inline-block;
	        margin: 1em;
	      }
	</style>
	
<!--[if lte IE 8]><script language="javascript" type="text/javascript" src="/js/excanvas.js"></script><![endif]-->	

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
        	<li class="blockcell1">${curScoreUserOrgName}</li>
            <li class="blockcell2">${curScoreUserInfo}</li>
        </div>
        <!-- E : socre -->	        
        <!-- S : notice -->
        <h4>Notice</h4>
        <div class="indexnotice">
        	<div class="mnoticeTT"><span><img src="/img/icon_notice.png" /> Notice </span><a href="/man/noticelist.do">+ more</a></div>
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
        	<div class="mnoticeTT"><span><img src="/img/icon_question.png" /> Frequently Questions </span><a href="/man/faqlist.do">+ more</a></div>
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
        	<div class="index_block1cell"><img src="/img/icon_index.png" />보안수준진단 추이 그래프</div>
						<style type="text/css">
						
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
						        background-color: rgb(228,228,228);
						    }
						
						    td.jqplot-table-legend-label {
						      width: 8em;
						      font-size: 1.65em;
						      padding: 0px 0px 0px 5px;
						      color: rgb(0,0,0);
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
						
						  </style>
					
					    <div class="chart-container" id="chart5"></div>	
        </div>
        <div class="block_warp">
            <div class="index_block2">
                <div class="index_block2cell"><img src="/img/icon_index.png" />진단항목별 발생건수</div>
                
                <div class="graph3" style='margin:5px 0 0 8px;'>

					<div class="jqplot-target" id="chart1" style="width: 310px; height: 320px; position: relative;"></div>
					
				</div>

            </div>
            <div class="index_block3">
                <div class="index_block3cell"><img src="/img/icon_index.png" /><c:out value="${jisuTitle}"/> 지수점수</div>
                <table cellpadding="0" cellspacing="0" class="tbl" border=0>
                	<thead>
                    	<tr>
                        	<th width="60%"><c:out value="${jisuTableHead}"/></th>
                            <th width="20%">평균점수</th>
                            <th width="20%">상태</th>
                        </tr>
                    </thead>
                    <tbody>
                    
						<tr>
						<td colspan=3>
							<DIV style="width:100%;height:252px;overflow-y:auto;">
							<table border="0" width=100% cellspacing="0" cellpadding="0" style="border-top: 0px solid #fff" class="tbltbl">
								<colgroup>
									<col style="width:60%">
									<col style="width:20%">
									<col style="width:20%">
								</colgroup>							

								<c:forEach var="result" items="${resultListJisuBody}" begin="0" varStatus="status">
								
									<tr>
										<td class=cell<c:out value="${status.getCount()%2+1 }"/>><c:out value="${result.descname}" /></td>
										<td class=cell<c:out value="${status.getCount()%2+1 }"/>><c:out value="${result.score}" />점</td>
										
										<c:if test="${result.idxstatus == '양호'}">
										<td class=cell<c:out value="${status.getCount()%2+1 }"/>><span class="scondition1"><c:out value="${result.idxstatus}" /></span></td>
										</c:if>
										<c:if test="${result.idxstatus == '취약'}">
										<td class=cell<c:out value="${status.getCount()%2+1 }"/>><span class="scondition2"><c:out value="${result.idxstatus}" /></span></td>
										</c:if>									
									</tr>
								
								</c:forEach>

							</table>
							</DIV>
						
						</td>
						</tr>                    
                                     
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <!-- E : center contents -->
    
    <script src="/js/js/Chart/gaugeSVG.js"></script>
	<script language="javascript" type="text/javascript">
	<!--    
    var ckvalue = ${curScore};
    if (ckvalue < 1){ckvalue = 1;} // display value 0 : 50 -- 1
    
      window.onload = function(){
	    
		var gauge = new GaugeSVG({
			id: "container_gaug",
			value: ckvalue,
			canvasBackColor: "#e5e5e5",
			title: "보안점수",
			label: "SCORE",
			labelColor: "black",
			labelScale: 3,
			gaugeWidthScale: 1.0,
			borderColor: "#ff5533",
			min: 0,
			max: 100,
			minmaxColor: "black",
			lowerActionLimit: 40,
			lowerWarningLimit: 40,
			upperActionLimit: 100,
			showMinMax: true,
			needleColor: "#080808",
			optimumRangeColor: "#FEAB35",
			warningRangeColor: "#48CB01",
			actionRangeColor: "#E00000"
		});	    

      };
      //-->
    </script>
	
	<script language="javascript">
	<!--
		$(document).ready(function(){
			$.jqplot.config.enablePlugins = true;
			var s1 = [0,0,0,0];
		    var ticks = ["PC취약성", "소프트웨어", "프로세스", "보안위협"];
		    
		    <c:if test="${!empty stx_data }">
				s1 = [${stx_data}];
			    ticks = [${stx_tick}];
		    </c:if>
			 
		    $.jqplot('chart1', [s1], {
                animate: !$.jqplot.use_excanvas,

                seriesColors: ['#0098EF'],
                seriesDefaults: {
                    renderer: $.jqplot.BarRenderer,
                    rendererOptions: {
                        highlightMouseDown: true
                        , barWidth: 30
                        , animation: { speed: 1000 }
                    }
                    , shadow: false
                    , pointLabels: {
                        show: false
                    },
                },
               
                axes: {
                    xaxis: {
                        renderer: $.jqplot.CategoryAxisRenderer
                        , tickRenderer: $.jqplot.CanvasAxisTickRenderer
                        , tickOptions: {
                            angle: -10,
                            formatString: '%s',
                            fontSize: '9pt'
                        },
                		ticks: ticks
                    },
                    yaxis: {
		                tickOptions: {
		                    angle: 0,
		                    fontSize: '9pt',
		                    showMark: false,
		                    showGridline: true,
		                    tickInterval: 20
		                }
		            }	
                  
                },
                highlighter: {
                    show: true
                    , showLabel: true
                    , tooltipAxes: 'y'
                    , useAxexFormatters:true
                    , formatString:'%s'
                   
                },
                legend: {
                    show: false,
                    location: 'ne',
                    placement: 'outside'
                }
                , grid: { background: '#F5F5F5', borderColor: 'transparent', shadow: false, drawBorder: false, shadowColor: 'transparent' }
            });
		   
		    
			
		});
	//--> 	
	</script>
	
	<script language="javascript" type="text/javascript" class="code">
	<!--
	$(document).ready(function(){
		var line0 = ${chartList0};
		var line1 = ${chartList1};
		var line2 = ${chartList2};
		var line3 = ${chartList3};
		var line4 = ${chartList4};
		var line5 = ${chartList5};
		var line_per = ${chartList_per};
		var ticks = ["4월", "5월", "6월", "7월", "8월", "9월", "10월"];
	    //var line1 = [['1개월', 4],['2개월', 6],['3개월', 2],['4개월', 5],['5개월', 6],['6개월', 3],['7개월', 30]];
	    //var line2 = [['1개월', 2],['2개월', 3],['3개월', 5],['4개월', 7],['5개월', 2],['6개월', 2],['7개월', 10]];
	    //var line3 = [['1개월', 5],['2개월', 4],['3개월', 7],['4개월', 2],['5개월', 3],['6개월', 5],['7개월', 15]];
	
	    $.jqplot('chart5', [${plotLines}], {
	    	 seriesColors:['rgb(59, 131, 196)', 'rgb(188, 207, 11)', 'rgb(253, 150, 13)', 'rgb(176, 121, 188)', '#17BDB8'],	    	
	         animate: true,
	         animateReplot: true
	         , axes: {
	        	 xaxis:{
	            	renderer: $.jqplot.CategoryAxisRenderer
                    , tickRenderer: $.jqplot.CanvasAxisTickRenderer
                    , tickOptions: {
                        angle: -30,
                        formatString: '%s',
                        fontSize: '7.5pt',
                        showMark: false,
	                    showGridline: true
                    }
	            },
	            yaxis: {
	                tickOptions: {
	                    angle: 0,
	                    fontSize: '9pt',
	                    showMark: false,
	                    showGridline: true
	                }
	            }	        
	         },
	         seriesDefaults: {
	             rendererOptions: {
	                 animation: {
	                     speed: 1500
	                 }
	             }
		         , pointLabels: {
	                 show: false
	             }
	             ,showMarker: false
		         ,lineWidth: 1.5
		         
	         },
	         highlighter: {
	             show: true,
	             tooltipAxes: 'y',
	             sizeAdjust: 7.5, 
	             tooltipLocation: 'ne',
	             useAxesFormatters:false,
	             tooltipFormatString:'%s'
	             
	         },
	         legend: {
	             show: true,
	             location: 'ne',
	             placement: "outside",
	             marginLeft: "15px",
	             marginTop: "0px",
	             renderer: $.jqplot.EnhancedLegendRenderer
	         }
	         , series: [${labelList}]
	        , grid: { background: '#F5F5F5', borderColor: 'transparent', shadow: false, drawBorder: false, shadowColor: 'transparent' }
	     });
	    
	    
	    
	   
	});
	//-->
	</script>
	
	<script type="text/javascript" src="/js/jquery-1.11.0.min.js"></script>
	<script type="text/javascript" src="/js/jquery.jqplot.min.js"></script>	
	<script type="text/javascript" src="/js/js/Chart/jqplot.meterGaugeRenderer.min.js"></script>	
	
	<script type="text/javascript" src="/js/js/Chart/jqplot.barRenderer.min.js"></script>
	<script type="text/javascript" src="/js/js/Chart/jqplot.pieRenderer.min.js"></script>
	<script type="text/javascript" src="/js/js/Chart/jqplot.categoryAxisRenderer.min.js"></script>
	<script type="text/javascript" src="/js/js/Chart/jqplot.pointLabels.min.js"></script>	
	
	<script class="include" type="text/javascript" src="/js/js/Chart/jqplot.canvasTextRenderer.min.js"></script>
	<script class="include" type="text/javascript" src="/js/js/Chart/jqplot.canvasAxisLabelRenderer.min.js"></script>
	<script class="include" type="text/javascript" src="/js/js/Chart/jqplot.enhancedLegendRenderer.min.js"></script>
	<script class="include" type="text/javascript" src="/js/jquery-ui.js"></script>	  
	<script class="include" type="text/javascript" src="/js/js/Chart/jqplot.canvasAxisTickRenderer.min.js"></script>  
    <script class="include" type="text/javascript" src="/js/js/Chart/jqplot.highlighter.min.js"></script>
    <!-- S : footer -->
	<!-- footer -->
	<div id="wrap_footer">
		<%@ include file="/WEB-INF/jsp/cmm/footer.jsp" %>
	</div>
	<!-- footer end -->
    <!-- E : footer -->
</div>
<!-- E : contents -->

</body>

</html>
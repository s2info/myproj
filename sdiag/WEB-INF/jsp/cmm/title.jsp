<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<title>임직원보안수준진단</title>
<meta charset="utf-8">
<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="imagetoolbar" content="no" />
<meta name="description" content="임직원보안수준진단" />
<meta http-equiv="Content-Type" content="text/html;charset=utf-8;" >
<meta http-equiv="content-language" content="ko">
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />

<script type="text/javascript" src="/js/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="/js/jquery-ui.js"></script>
<script type="text/javaxcript" src="/js/common.js"></script>

<script type="text/javascript" src="/js/jquery.paging.js"></script>
<link type="text/css" rel="stylesheet" href="/css/jquery-ui.css" />
<link rel="stylesheet" type="text/css" href="/css/common.css" />

<script type="text/javaScript" language="javascript" defer="defer">

	String.prototype.isNumber = function () {
		regexp = /[^0-9.]/gi;
	    return !regexp.test(this) && this.length > 0;
	};
	
	String.prototype.TitleLength = function () {
		var title = this;
		if(title.length > evel(arguments[0])){
			title = title.substring(0, evel(arguments[0])) + "...";
		}
	    return title;
	};
	
	function tableRowSpanning(Table, spanning_row_index, span_type) {
        var RowspanTd = false;
        var RowspanText = false;
        var RowspanCount = 0;
        var Rows = $('tbody tr', Table);
        
        $.each(Rows, function () {
            
            var This = $('td', this)[spanning_row_index];
            var text = $(This).attr(span_type);//$(This).text();

            if (RowspanTd == false) {
                RowspanTd = This;
                RowspanText = text;
                RowspanCount = 1;
            }
            else if (RowspanText != text) {
                $(RowspanTd)
                .attr('rowSpan', RowspanCount);

                RowspanTd = This;
                RowspanText = text;
                RowspanCount = 1;
            }
            else {
                $(This)
                .remove();
                RowspanCount++;
            }
        });
        $(RowspanTd)
            .attr('rowSpan', RowspanCount);
    }
	
	function SelectedRowChange(row){
		row.siblings().removeClass('SelRow');
		row.addClass('SelRow');
	}
	
	function isloginAuth_check(){
		var retval = false;
		$.ajax({
			url : '/com/isauthcheck.do',
			data : {},
			type : 'POST',
			dataType : 'json',
			error : function(jqXHR, textStatus, errorThrown) {
				alert(textStatus + "\r\n" + errorThrown);
			},
			success : function(data) {
				if (data.ISOK) {
					retval = true;
				}else{
					retval = false;
				}
				alert(data.ISOK);
			},
	        complete: function () {
	        	
	        }
			
		});
		
		
	};
	
	function gopage(target, params, formid) {
	    var frm = document.createElement('form');
	    frm.id=formid;
	    frm.action = target;
	    frm.method = 'post';
	    for (var i = 0 ; i < params.length; i++) {
	        var r = JSON.parse(params[i]);
	        var param = document.createElement('input');
	        param.name = r.inputname;
	        param.value = r.inputvalue;
	        frm.appendChild(param);
	    }
	    frm.style.display = 'none';
	    document.body.appendChild(frm);
	    frm.submit();
	    return frm;
	}		
	
	$(function () {	
		
	});
	
</script>

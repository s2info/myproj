<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!--[if IE]><meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"><![endif]-->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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

<link type="text/css" rel="stylesheet" href="/css/jquery-ui.css" />
<link rel="stylesheet" type="text/css" href="/css/common.css" />

<link type="text/css" rel="stylesheet" href="/css/FileUpload/style.css" />
<link rel="stylesheet" type="text/css" href="/css/FileUpload/jquery.fileupload.css" />

<script type="text/javaxcript" src="/js/FileUpload/vendor/jquery.ui.widget.js"></script>
<script type="text/javaxcript" src="/js/FileUpload/jquery.fileupload.js"></script>
<script type="text/javaxcript" src="/js/FileUpload/jquery.iframe-transport.js"></script>
<script type="text/javaxcript" src="/js/FileUpload/jquery.fileupload-process.js"></script>



<script type="text/javascript" language="javascript">
$(function () {		
	var url = "/image/upload.do";
	$('#fileupload').fileupload({
        url: url,
        dataType: 'json',
        add:function(e, data){
        	var uploadFile = data.files[0];
        	alert(uploadFile);
        },
        done: function (e, data) {
            $.each(data.result.files, function (index, file) {
                $('<p/>').text(file.name).appendTo('#files');
            });
        },
        progressall: function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $('#progress .progress-bar').css(
                'width',
                progress + '%'
            );
        }
    }).prop('disabled', !$.support.fileInput)
        .parent().addClass($.support.fileInput ? undefined : 'disabled');
});

</script>
</head>
<body>
	<div id="wrap_body">
		
		
		<div id="wrap_contents">
		<!-- Contents Begin -->
		<!-- toMailAddress -->
		
		
		<form:form commandName="searchVO" name="listForm" method="post">
			<div class="SB1_tit">
				<ul>
					<li><img src="/img/ic_tit1.png" alt="타이틀"></li>
					<li class="SB1T_txt">파일업로드</li>
				</ul>
			</div>
			<div class="contents">
				<span class="btn btn-success fileinput-button">
			        <i class="glyphicon glyphicon-plus"></i>
			        <span>Select files...</span>
			        <!-- The file input field used as target for the file upload widget -->
			        <input id="fileupload" type="file" name="files[]" multiple>
			    </span>
			    <br /><br />
				<div id="progress" class="progress">
					<div class="progress-bar progress-bar-success"></div>
				</div>
				<div id="files" class="files"></div>
			</div>	
		</form:form>
		
		</div>
		<!-- Contents End -->
		
		
	</div>


	
</body>
</html>
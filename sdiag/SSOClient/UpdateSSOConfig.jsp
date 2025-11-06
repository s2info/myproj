<%@ page import="nets.websso.ssoclient.authcheck.ErrorMessage"%>
<%@ page import="nets.websso.ssoclient.authcheck.SSOConfig" %>
<%@ page import="nets.websso.ssoclient.authcheck.ErrorCode" %>

<%@ page contentType="text/html; charset=euc-kr"%>
<%

    String responseBody = "";
    try
    {
        //입력 파라메터 검증
        String providerDomain = request.getParameter("providerdomain");
        String notifyType = request.getParameter("notifyType");
        SSOConfig.request = request;
        SSOConfig.initialized = false;

        if(providerDomain ==  null || providerDomain.equals(""))
        {
            responseBody =ErrorMessage.FailFlag + ErrorMessage.MsgSep + ErrorCode.ERR_INVALID_SSOPROVIDER_DOMAIN;
        }
        else if(notifyType == null || notifyType.equals(""))
        {
            responseBody = ErrorMessage.FailFlag + ErrorMessage.MsgSep + ErrorCode.ERR_INVALID_NOTIFY_TYPE;
        }
        else
        {
            if (notifyType.equals("1")) // 환경 설정 캐시 변경요청에 대한 처리를 수행한(ReloadConfig)
            {
                SSOConfig.UpdateSSOConfig();
            }
            else if (notifyType.equals("2")) // 환경 설정 캐시 삭제 요청에 대한 처리를 수행함(Disabled)
            {
                SSOConfig.DsiabledSSOConfig();
            }
            responseBody = ErrorMessage.SuccessFlag + ErrorMessage.MsgSep + ErrorCode.NO_ERR;
        }
    }
    catch (Exception ex) {
        String msg = ex.toString();
        msg = msg.replace('\n', ' ');
        msg = msg.replace('\r', ' ');
        msg = msg.replace('"', ' ');
        responseBody  = ErrorMessage.FailFlag + ErrorMessage.MsgSep + msg;

    }
%>
<html>
<head><title></title></head>
<body>
<%=responseBody%>
</body>
</html>
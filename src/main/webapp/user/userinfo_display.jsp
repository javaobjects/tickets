<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="net.tencent.tickets.entity.Users"%>
    <!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看个人信息</title>
<link href="<%=request.getContextPath()%>/css/css.css" rel="stylesheet" type="text/css">
<%
Users user = (Users)request.getAttribute("userinfo");
%>
</head>
<body class="write_bg">
<form name="form1" method="post" action="">
  <table width="100%" border="0" cellspacing="0">
    <tr>
      <td height="30">&nbsp;</td>
    </tr>
  </table>
  <table width="835" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td height="20" colspan="2" align="center" ></td>
  </tr>
  <tr>
    <td width="64" align="center" ></td>
    <td width="771" height="30" align="left" valign="top" >
   		<span class="text_blod_title">查看个人信息</span>
    </td>
  </tr>
  <tr>
    <td height="15" colspan="2" >
    	<img src="<%=request.getContextPath()%>/images/line1.jpg" width="835" height="6">
   	</td>
  </tr>
  <tr>
    <td colspan="2" valign="top"  ><table width="700" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr>
            <td height="20" colspan="4"  ></td>
          </tr>
          <tr>
            <td height="15" colspan="4" align="left" class="text_title">个人详细信息</td>
          </tr>
          <tr>
            <td height="10" colspan="4" ></td>
          </tr>
          <tr>
            <td width="20" height="40" align="center" class="text_red">*</td>
            <td width="100" height="40" align="left" class="text_cray1">登录名：</td>
            <td width="350" align="left" class="text_cray"><%=user.getUserName() %></td>
            <td width="230" rowspan="5" align="center"
             background="<%=request.getContextPath() %>/images/bg_point_write.gif" class="text_cray">
             <img src="<%=request.getContextPath()+"/photos/"+user.getUserImagePath() %>" width="139" height="139">
             </td>
          </tr>
          <tr>
            <td width="20" height="40" align="center" class="text_red">*</td>
            <td width="100" height="40" align="left" class="text_cray1">真实姓名：</td>
            <td align="left" class="text_cray"><%=user.getUserRealName() %></td>
          </tr>
          <tr>
            <td width="20" height="40" align="center" class="text_red">*</td>
            <td width="100" height="40" align="left" class="text_cray1">性 别：</td>
            <td align="left" class="text_cray"><%=user.getUserSex().equals("1")?"男":"女" %></td>
          </tr>
          <tr>
            <td width="20" height="40" align="center" class="text_red">*</td>
            <td width="100" height="40" align="left" class="text_cray1">省份：</td>
            <td align="left" class="text_cray"><%=user.getCity().getProvince().getProvinceName() %></td>
          </tr>
          <tr>
            <td width="20" height="40" align="center" class="text_red">*</td>
            <td width="100" height="40" align="left" class="text_cray1">城市：</td>
            <td align="left" class="text_cray"><%=user.getCity().getCityName() %></td>
          </tr>
          <tr>
            <td width="20" height="40" align="center" class="text_red">*</td>
            <td width="100" height="40" align="left" class="text_cray1">证件类型：</td>
            <td colspan="2" align="left" class="text_cray"><%=user.getCertType().getContent() %></td>
          </tr>
          <tr>
            <td width="20" height="40" align="center" class="text_red">*</td>
            <td width="100" height="40" align="left" class="text_cray1">证件号码：</td>
            <td colspan="2" align="left" class="text_cray"><%=user.getUserCert() %></td>
          </tr>
          <tr>
            <td width="20" height="40" align="center" class="text_red">*</td>
            <td width="100" height="40" align="left" class="text_cray1">出生日期：</td>
            <td colspan="2" align="left" class="text_cray"><%=user.getUserBirthday() %></td>
          </tr>
          <tr>
            <td width="20" height="40" ></td>
            <td width="100" height="40" align="left" class="text_cray1">旅客类型：</td>
            <td colspan="3" align="left" class="text_cray"><%=user.getUserType().getContent() %></td>
          </tr>
          <tr>
            <td width="20" height="40"></td>
            <td width="100" height="40" align="left" class="text_cray1">备注：</td>
            <td height="40" colspan="2" align="left" class="text_cray"><%=user.getUserContent() %></td>
          </tr>
        </table><br>
      <table width="100%" border="0" cellspacing="0">
        <tr>
          <td></td>
        </tr>
      </table>
      <table width="263" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
          <td height="30" align="center">
          	<input name="button" type="button" class="butxg" id="btn_xg"value="">
          </td>
          </tr>
      </table>
    </table>
  <table width="100%" border="0" cellspacing="0">
    <tr>
      <td height="20"></td>
    </tr>
  </table>  
  <table width="100%" border="0" cellspacing="0">
    <tr>
      <td height="2" background="<%=request.getContextPath()%>/images/bottom_point.gif"></td>
    </tr>
    <tr>
      <td height="25" align="center" background="<%=request.getContextPath()%>/images/bottom_ny_bg.gif"
      class="text_cray">copyright@12306 购票网</td>
    </tr>
  </table>
  </td>
  </tr>
</table>
</form>
<script src="<%=request.getContextPath()%>/js/jquery-3.4.1.js"></script>
<script>
    $(function (){
        $("#btn_xg").click(function (){
            location.href = "<%=request.getContextPath()%>/ToUpdateUserServlet";
        })
    })
</script>
</body>
</html>

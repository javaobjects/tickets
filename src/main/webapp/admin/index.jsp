<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>12306购票系统admin/index.jsp</title>
</head>
<frameset rows="130,*" cols="*" frameborder="no" border="0" framespacing="0">
    <frame src="<%=request.getContextPath()%>/admin/top.jsp" name="topFrame" scrolling="No" noresize="noresize"
           id="topFrame" title="topFrame">
    <frameset rows="*" cols="247,*" framespacing="0" frameborder="no" border="0">
        <frame src="<%=request.getContextPath()%>/admin/left.jsp" name="leftFrame" scrolling="No" noresize="noresize"
               id="leftFrame" title="leftFrame">
        <frame src="<%=request.getContextPath()%>/admin/main.jsp" name="mainFrame" id="mainFrame" title="mainFrame">
    </frameset>
</frameset>
<noframes>
    <body>
    </body>
</noframes>
</html>

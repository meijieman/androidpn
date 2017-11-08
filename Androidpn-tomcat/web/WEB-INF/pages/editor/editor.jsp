<%@ page contentType="text/html;charset=UTF-8" language="java"
         errorPage="/WEB-INF/pages/error.jsp" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>
<html>
<head>
    <title>Admin Console</title>
    <meta name="menu" content="editor" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/styles/tablesorter/style.css'/>" />
    <script type="text/javascript" src="<c:url value='/scripts/jquery.tablesorter.js'/>"></script>
</head>
<body>
<h1>通过标签名获取用户列表</h1>

<form action="editor.do?action=add" method="post" style="margin: 0px">
    <table width="600" cellpadding="4" cellspacing="0" border="0">
        <tr>
            <td width="20%">标签名：</td>
            <td width="80%">
                <input type="text" id="tag_name" name="tag_name" value="${tag_name}"
                       style="width:380px;"/>
            </td>
            <td><input type="submit" value="Submit"/></td>
        </tr>
</form>

<table id="tableList" class="tablesorter" cellspacing="1">
    <thead>
    <tr>
        <th>Username</th>
        <th>Name</th>
        <th>Email</th>
        <th>Created</th>
        <th>Alias</th>
        <th>Tag</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="user" items="${userList}">
        <tr>
            <td><c:out value="${user.username}" /></td>
            <td><c:out value="${user.name}" /></td>
            <td><c:out value="${user.email}" /></td>
            <td align="center"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${user.createdDate}" /></td>
            <td><c:out value="${user.alias}" /></td>
            <td><c:out value="${user.tag}" /></td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<%
    String message = (String) request.getAttribute("message");
    if (message != null && !message.isEmpty()) {
%>
<script type="text/javascript" language="JavaScript">
    alert("<%=message%>")
</script>
<%
    }
%>

<script type="text/javascript">
    //<![CDATA[
    $(function() {
        $('#tableList').tablesorter();
        //$('#tableList').tablesorter( {sortList: [[0,0], [1,0]]} );
        //$('table tr:nth-child(odd)').addClass('odd');
        $('table tr:nth-child(even)').addClass('even');
    });
    //]]>
</script>
</body>
</html>

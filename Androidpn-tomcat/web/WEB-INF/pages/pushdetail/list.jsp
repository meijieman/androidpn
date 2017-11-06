<%@ page contentType="text/html;charset=UTF-8" errorPage="/WEB-INF/pages/error.jsp" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
    <title>Admin Console</title>
    <meta name="menu" content="pushdetail"/>
    <link rel="stylesheet" type="text/css" href="<c:url value='/styles/tablesorter/style.css'/>"/>
    <script type="text/javascript" src="<c:url value='/scripts/jquery.tablesorter.js'/>"></script>
</head>
<body>

<h1>Push Detail</h1>

<table id="tableList" class="tablesorter" cellspacing="1">
    <thead>
    <tr>
        <th>ID</th>
        <th>UUID</th>
        <th>Title</th>
        <th>Username</th>
        <th>Alias</th>
        <th>CreatedDate</th>
        <th>ReceiptDate</th>
        <th>ClickDate</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="pushDetail" items="${pushdetaillist}">
        <tr>
            <td><c:out value="${pushDetail.id}"/></td>
            <td><c:out value="${pushDetail.uuid}"/></td>
            <td><c:out value="${pushDetail.title}"/></td>
            <td><c:out value="${pushDetail.username}"/></td>
            <td><c:out value="${pushDetail.alias}"/></td>
            <td align="center"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${pushDetail.createdDate}"/></td>
            <td align="center"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${pushDetail.receiptDate}"/></td>
            <td align="center"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${pushDetail.clickDate}"/></td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<script type="text/javascript">
    //<![CDATA[
    $(function () {
        $('#tableList').tablesorter();
        //$('#tableList').tablesorter( {sortList: [[0,0], [1,0]]} );
        //$('table tr:nth-child(odd)').addClass('odd');
        $('table tr:nth-child(even)').addClass('even');
    });
    //]]>
</script>
</body>
</html>

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
<h1>Editor</h1>


<table id="tableList" class="tablesorter" cellspacing="1">
    <thead>
    <tr>
        <th>UUID</th>
    </tr>
    </thead>
    <tbody>

    </tbody>
</table>

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

<%@ page language="java" errorPage="/WEB-INF/pages/error.jsp" pageEncoding="UTF-8"
         contentType="text/html;charset=UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Admin Console</title>
<meta name="menu" content="notificationrecord" />
<link rel="stylesheet" type="text/css" href="<c:url value='/styles/tablesorter/style.css'/>" />
<script type="text/javascript" src="<c:url value='/scripts/jquery.tablesorter.js'/>"></script>
</head>

<body>

<h1>Notification Record</h1>

<table id="tableList" class="tablesorter" cellspacing="1">
	<thead>
		<tr>
			<%--
			<th width="5%">Online</th>
			<th width="30%">Username</th>
			<th width="20%">Name</th>
			<th width="20%">Email</th>
			<th width="25%">Created</th>
			--%>
			<th>UUID</th>
			<th>PushTo</th>
			<th>PushType</th>
			<th>Title</th>
			<th>Message</th>
			<th>URI</th>
			<th>State</th>
			<th>CreatedDate</th>
			<th>ValidTime</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="notification" items="${notificationList}">
			<tr>
				<td><c:out value="${notification.uuid}" /></td>
				<td><c:out value="${notification.pushTo}" /></td>
				<td><c:out value="${notification.pushType}" /></td>
				<td><c:out value="${notification.title}" /></td>
				<td><c:out value="${notification.message}" /></td>
				<td><c:out value="${notification.uri}" /></td>
				<td><c:out value="${notification.state}" /></td>
				<td align="center"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${notification.createdDate}" /></td>
				<td><c:out value="${notification.validTime}" /></td>
			</tr>
		</c:forEach>
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

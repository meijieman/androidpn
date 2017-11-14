<%@ page import="java.util.Date" %>
<%@ page language="java" errorPage="/WEB-INF/pages/error.jsp" pageEncoding="UTF-8"
         contentType="text/html;charset=UTF-8" %>
<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Admin Console</title>
    <meta name="menu" content="notification"/>
</head>

<body>

<h1>Send Notifications</h1>

<%--<div style="background:#eee; margin:20px 0px; padding:20px; width:500px; border:solid 1px #999;">--%>
<div style="margin:20px 0px;">
    <form action="notification.do?action=send" method="post" style="margin: 0px;">
        <table width="600" cellpadding="4" cellspacing="0" border="0">
            <tr>
                <td width="20%">Push To:</td>
                <td width="80%">
                    <input type="radio" name="broadcast" value="0" checked="checked"/> All (Broadcast)
                    <input type="radio" name="broadcast" value="1"/> By Username
                    <input type="radio" name="broadcast" value="2"/> By Alias
                    <input type="radio" name="broadcast" value="3"/> By Tags
                </td>
            </tr>
            <tr>
                <td width="20%">Push Type:</td>
                <td width="80%">
                    <input type="radio" name="pushtype" value="0" checked="checked"/> Notification
                    <input type="radio" name="pushtype" value="1"/> Payload
                </td>
            </tr>
            <tr>
                <td>Valid Time:(s)</td>
                <td><input type="text" id="validTime" name="validTime" value="0" style="width:380px;"/></td>
            </tr>
            <tr id="trUsername" style="display:none;">
                <td>Username:</td>
                <td><input type="text" id="username" name="username" value="" style="width:380px;"/></td>
            </tr>
            <tr id="trAlias" style="display:none;">
                <td>Alias:</td>
                <td><input type="text" id="alias" name="alias" value="" style="width:380px;"/></td>
            </tr>
            <tr id="trTags" style="display:none;">
                <td>Tags:</td>
                <td><input type="text" id="tags" name="tags" value="" style="width:380px;"/>
                <br/><span style="font-size:0.8em">input tags, split with ",", this will use "or" to find the user</span>
                </td>
            </tr>
            <tr>
                <td>Title:</td>
                <td><input type="text" id="title" name="title"
                           value="车悦宝 <fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="<%=new Date()%>" />"
                           style="width:380px;"/></td>
            </tr>
            <tr>
                <td>Message:</td>
                <td><textarea id="message" name="message"
                              style="width:380px; height:80px;">车载在线互动娱乐平台。随车行乐，听你想听!</textarea></td>
            </tr>
            <%--
            <tr>
                <td>Ticker:</td>
                <td><input type="text" id="ticker" name="ticker" value="" style="width:380px;" /></td>
            </tr>
            --%>
            <tr>
                <td>URI:</td>
                <td><input type="text" id="uri" name="uri" value="" style="width:380px;"/>
                    <br/><span tyle="font-size:0.8em">ex) http://www.dokdocorea.com, geo:37.24,131.86, tel:111-222-3333</span>
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td><input type="submit" value="Submit"/></td>
            </tr>
        </table>
    </form>
</div>

<script type="text/javascript">
    //<![CDATA[

    $(function () {
        // 点击事件
        $('input[name=broadcast]').click(function () {
            if ($('input[name=broadcast]')[0].checked) {
                $('#trUsername').hide();
                $('#trAlias').hide();
                $('#trTags').hide();
            } else if ($('input[name=broadcast]')[1].checked) {
                $('#trUsername').show();
                $('#trAlias').hide();
                $('#trTags').hide();
            } else if ($('input[name=broadcast]')[2].checked) {
                $('#trUsername').hide();
                $('#trAlias').show();
                $('#trTags').hide();
            } else if ($('input[name=broadcast]')[3].checked) {
                $('#trUsername').hide();
                $('#trAlias').hide();
                $('#trTags').show();
            }
        });

        if ($('input[name=broadcast]')[0].checked) {
            $('#trUsername').hide();
            $('#trAlias').hide();
            $('#trTags').hide();
        } else if ($('input[name=broadcast]')[1].checked) {
            $('#trUsername').show();
            $('#trAlias').hide();
            $('#trTags').hide();
        } else if ($('input[name=broadcast]')[2].checked) {
            $('#trUsername').hide();
            $('#trAlias').show();
        } else if ($('input[name=broadcast]')[3].checked) {
            $('#trUsername').hide();
            $('#trAlias').hide();
            $('#trTags').show();
        }
    });

    //]]>
</script>

</body>
</html>

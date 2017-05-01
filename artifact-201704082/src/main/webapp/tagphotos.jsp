<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Tag Photos</title>
</head>
<body>
<form action="/tag" method="post">
<% List<Entity> photos = (List<Entity>) session.getAttribute("photos"); %>
<table>
<tr><th>Photos</th><th>Tags</th></tr>
<% if (photos!=null) { %>
<% for (Entity entity : photos) { %>
      <tr>
      	<td> <img src="/serve?blob-key=<%=entity.getProperty("blob-key")%>" alt="image" height="200" width="300"/> </td>
      	<td><input type="text" id="<%=entity.getProperty("blob-key")%>" name="<%=entity.getProperty("blob-key")%>" width="200"/></td>
      </tr>
<% } %>
<% } %>
<tr><td colspan="2"><input type="submit" value="Tag Photos"></td></tr>
</table>
</form>
</body>
</html>
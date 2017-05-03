<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% UserService userService = UserServiceFactory.getUserService(); %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
	<style>
	th, td {
	    padding: 10px;
	}
	</style>
	<title>Tag Photos</title>
</head>
<body>
<nav class="navbar navbar-default">
 <div class="container-fluid">
   <div class="navbar-header">
     <a class="navbar-brand" href="/home.jsp">Smart Photo Classifier</a>
   </div>
   <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
     <ul class="nav navbar-nav navbar-right">
       <li class="dropdown">
         <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
         <%=userService.getCurrentUser().getNickname() %>
         <span class="caret"></span></a>
         <ul class="dropdown-menu">
           <li><a href="#">User Profile</a></li>
           <li><a href="#">User Setting</a></li>
           <li role="separator" class="divider"></li>
           <li><a href="<%=userService.createLogoutURL("/") %>">Sign out</a></li>
         </ul>
       </li>
     </ul>
   </div><!-- /.navbar-collapse -->
 </div><!-- /.container-fluid -->
</nav>
<form action="/tag" method="post">
<% List<Entity> photos = (List<Entity>) session.getAttribute("photos"); %>
<% Map<String,String> faceNameMap = (Map<String,String>) session.getAttribute("faceNameMap"); %>
<% Map<String,Map<String,String>> photoFaceIdMap = (Map<String,Map<String,String>>) session.getAttribute("photoFaceIdMap"); %>
<table>
<tr><th>Photos</th><th>Tags</th></tr>
<% if (photos!=null) { %>
<% for (Entity entity : photos) { %>
      <tr>
      	<td> <img src="/serve?blob-key=<%=entity.getProperty("blob-key")%>" alt="image" height="200" width="300"/> </td>
      	<td>
      		<div class="form-group">
      		<% Map<String,String> faceIdMap = photoFaceIdMap.get(entity.getProperty("blob-key")); %>
      		<% for (String faceId : faceIdMap.keySet()) { %>
      			<% if(faceNameMap.get(faceIdMap.get(faceId)) != null) { %>
      				<input class="form-control" type="text" id="<%=entity.getProperty("blob-key")+faceId%>" name="<%=entity.getProperty("blob-key")+faceId%>" 
      					value="<%=faceNameMap.get(faceIdMap.get(faceId))%>" width="200" readonly/><br></br>
      			<% } else { %>
      				<input class="form-control" type="text" id="<%=entity.getProperty("blob-key")+faceId%>" name="<%=entity.getProperty("blob-key")+faceId%>" width="200"/><br>
      			<% } %>
      		<% } %>
      		</div>
      	</td>
      </tr>
<% } %>
<% } %>
<tr>
	<td colspan="2">
		<div class="form-group">
		<input class="form-control" type="submit" value="Tag Photos">
		</div>
	</td>
</tr>
</table>
</form>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
</body>
</html>
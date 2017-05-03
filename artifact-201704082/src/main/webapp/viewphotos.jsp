<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
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
	table {
	    width: 100%;
	}
	</style>
	<title>View Photos</title>
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
<form action="/view" method="post">
<table>
	<tr>
		<td>
		<div class="form-group">
		<label for="sortby">Sort By:</label>
		<select class="form-control" id="sortby" name="sortby">
		  <option value="timestamp">Created Time (desc)</option>
		  <option value="faces">No. of faces</option>
		  <option value="faceSize">Size of faces (desc)</option>
		</select>
		</div>
		</td>
		<td>
			<div class="form-group">
			<label for="tags">People Tagged:</label>
			<input class="form-control" type="text" id="tags" name="tags">
			</div>
		</td>
		<td>
		<div class="form-group">
		<label for="emotion">Emotion:</label>
		<select class="form-control" id="emotion" name="emotion">
		  <option value="any">Any</option>
		  <option value="anger">anger</option>
		  <option value="contempt">contempt</option>
		  <option value="disgust">disgust</option>
		  <option value="fear">fear</option>
		  <option value="happiness">happiness</option>
		  <option value="neutral">neutral</option>
		  <option value="sadness">sadness</option>
		  <option value="surprise">surprise</option>
		</select>
		</div>
		</td>
		<td>
		<div class="form-group">
		<label for="faces">No. of People:</label>
		<select class="form-control" id="faces" name="faces">
		  <option value="any">Any</option>
		  <option value="1">One person</option>
		  <option value="2">Two people</option>
		  <option value="3">Three people</option>
		  <option value="4">Four people</option>
		  <option value="5">Five or more people</option>
		</select>
		</div>
		</td>
		
	</tr>
	<tr>
		<td colspan="4">
			<div class="form-group">
			<input class="form-control" type="submit" value="Apply">
			</div>
		</td>
	</tr>
</table>
</form>

<% List<Entity> photos = (List<Entity>) session.getAttribute("photos"); %>
<% if (photos!=null) { %>
<table>
<tr><th>Your Photos:</th></tr>
<% int rows = photos.size()/5; %>
<% for (int i=0; i<rows*5; i=i+5) { %>
      <tr>
      	<td> <img src="/serve?blob-key=<%=photos.get(i).getProperty("blob-key")%>" alt="image" height="200" width="250"/> </td>
      	<td> <img src="/serve?blob-key=<%=photos.get(i+1).getProperty("blob-key")%>" alt="image" height="200" width="250"/> </td>
      	<td> <img src="/serve?blob-key=<%=photos.get(i+2).getProperty("blob-key")%>" alt="image" height="200" width="250"/> </td>
      	<td> <img src="/serve?blob-key=<%=photos.get(i+3).getProperty("blob-key")%>" alt="image" height="200" width="250"/> </td>
      	<td> <img src="/serve?blob-key=<%=photos.get(i+4).getProperty("blob-key")%>" alt="image" height="200" width="250"/> </td>
      </tr>
<% } %>
<tr>
<% for (int i=rows*5; i<photos.size(); i++) { %>
   	<td> <img src="/serve?blob-key=<%=photos.get(i).getProperty("blob-key")%>" alt="image" height="200" width="250"/> </td>
<% } %>
</tr>
</table>
<% } %>
<% session.removeAttribute("photos"); %>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
</body>
</html>
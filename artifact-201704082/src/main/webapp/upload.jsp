<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	UserService userService = UserServiceFactory.getUserService();
%>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
	<title>Upload</title>
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
	 <div class="container">
	     <div class="starter-template">
	       <h1>Upload photos</h1>
		   	<form id="uploadform" action="<%=blobstoreService.createUploadUrl("/upload") %>" method="post" enctype="multipart/form-data">
		        <div class="form-group">
		        	<input class="form-control-file" type="file" name="myFile" multiple>
		        </div>
		    	<div class="form-group">
		        	<button type="submit" class="btn btn-default">Upload</button>
		        </div>
		    </form>
	     </div>
	</div>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
</body>
</html>
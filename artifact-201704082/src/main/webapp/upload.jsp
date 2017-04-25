<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href='spc_style.css' rel='stylesheet' type='text/css'>
<title>Upload Photos</title>
</head>
<body>
	<div class="topbar">
		<span class="tplogotext">Smart Photo Classifier</span>
		<a class="tbsignout">Signout</a>
	</div>
	<p class="userinstruction">Upload photos</p>
	<form id="uploadform" action="<%= blobstoreService.createUploadUrl("/upload") %>" method="post" enctype="multipart/form-data">
        <input type="file" name="myFile" multiple>
        <input type="submit" value="Submit">
    </form>
</body>
</html>
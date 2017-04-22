<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View Photos</title>
</head>
<body>
<form action="/view" method="post">
<table>
	<tr>
		<td>
		<label for="sortby">Sort By:</label>
		<select id="sortby" name="sortby">
		  <option value="face_number">No. of faces</option>
		  <option value="face_size">Size of faces (desc)</option>
		  <option value="image_size">Size of image (desc)</option>
		  <option value="timestamp">Created Time (desc)</option>
		</select>
		</td>
		<td>
		<label for="emotion">Emotion:</label>
		<select id="emotion" name="emotion">
		  <option value="anger">anger</option>
		  <option value="contempt">contempt</option>
		  <option value="disgust">disgust</option>
		  <option value="fear">fear</option>
		  <option value="happiness">happiness</option>
		  <option value="neutral">neutral</option>
		  <option value="sadness">sadness</option>
		  <option value="surprise">surprise</option>
		</select>
		</td>
		<td>
		<label for="people_number">No. of People:</label>
		<select id="people_number" name="people_number">
		  <option value="1">One person</option>
		  <option value="2">Two people</option>
		  <option value="2-5">Two to five people</option>
		  <option value="5">More than five people</option>
		</select>
		</td>
		<td><input type="submit" value="Apply"></td>
	</tr>
</table>

<% List<Entity> photos = (List<Entity>) session.getAttribute("photos"); %>
<% if (photos!=null) { %>
<table border=1>
<tr><th>Blob Key</th></tr>
<% for (Entity entity : photos) { %>
      <tr><td> <img src="project20140410.appspot.com/serve?blob-key=<%=entity.getProperty("blob-key")%>" alt="image"/> </td></tr>
<% } %>
</table>
<% } %>
<% session.removeAttribute("photos"); %>
</form>
</body>
</html>
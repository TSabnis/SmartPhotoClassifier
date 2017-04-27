package com.example.group201704082.servlets;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

public class TagServlet extends HttpServlet {
	
	private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	@Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
		
		Map<String,String[]> paramMap = req.getParameterMap();
		
		HttpSession session = req.getSession(true);
		List<Entity> photos = (List<Entity>) session.getAttribute("photos");
		
		for (Entity photo : photos) {
			photo.setProperty("tagsString", paramMap.get(photo.getProperty("blob-key"))[0]);
		}
		
		datastore.put(photos);
		session.removeAttribute("photos");
		res.sendRedirect("/");
	}

}

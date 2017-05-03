package com.example.group201704082.servlets;

import java.io.IOException;
import java.util.ArrayList;
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
		List<Entity> faces = new ArrayList<Entity>();
		
		HttpSession session = req.getSession(true);
		List<Entity> photos = (List<Entity>) session.getAttribute("photos");
		Map<String,Map<String,String>> photoFaceIdMap = (Map<String,Map<String,String>>) session.getAttribute("photoFaceIdMap");
		
		for (Entity photo : photos) {
			StringBuilder sb = new StringBuilder("");
			Map<String,String> faceIdMap = photoFaceIdMap.get(photo.getProperty("blob-key"));
			for (String faceId : faceIdMap.keySet()) {
				Entity face = new Entity("face", faceIdMap.get(faceId));
				face.setProperty("persistedFaceId", faceIdMap.get(faceId));
				face.setProperty("taggedName", paramMap.get(photo.getProperty("blob-key")+faceId)[0]);
				faces.add(face);
				sb.append(paramMap.get(photo.getProperty("blob-key")+faceId)[0]+",");
			}
			photo.setProperty("tagsString", sb.toString());
		}
		
		datastore.put(photos);
		datastore.put(faces);
		session.removeAttribute("photos");
		res.sendRedirect("/home.jsp");
	}

}

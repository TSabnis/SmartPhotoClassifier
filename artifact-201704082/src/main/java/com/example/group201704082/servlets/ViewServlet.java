package com.example.group201704082.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public class ViewServlet extends HttpServlet {
	
	private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	@Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
		
		Query q = new Query("photo");
		
	    String faces = req.getParameter("faces");
	    
	    if (!req.getParameter("emotion").equals("any")) {
	    	Filter emotionFilter = new FilterPredicate("emotion", FilterOperator.EQUAL, req.getParameter("emotion"));
	    	q.setFilter(emotionFilter);
	    }
	    
	    if (!faces.equals("any")) {
	    	Filter facesFilter;
			switch (faces) {
			case "1":
				facesFilter = new FilterPredicate("faces", FilterOperator.EQUAL, 1);
				break;
			case "2":
				facesFilter = new FilterPredicate("faces", FilterOperator.EQUAL, 2);
				break;
			case "2-5":
				facesFilter = new FilterPredicate("faces", FilterOperator.LESS_THAN_OR_EQUAL, 5);
				q.setFilter(new FilterPredicate("faces", FilterOperator.GREATER_THAN, 2));
				break;
			case "5":
				facesFilter = new FilterPredicate("faces", FilterOperator.GREATER_THAN, 5);
				break;
			default:
				facesFilter = new FilterPredicate("faces", FilterOperator.GREATER_THAN_OR_EQUAL, 0);
			}
			q.setFilter(facesFilter);
		}
	    
	    PreparedQuery pq = datastore.prepare(q);
	    List<Entity> results = pq.asList(FetchOptions.Builder.withDefaults());
		System.out.println("Result size: "+results.size());
		
		switch (req.getParameter("sortby")) {
		case "faces":
			Collections.sort(results, new Comparator<Entity>() {
				@Override
				public int compare(Entity o1, Entity o2) {
					return ((Long)o2.getProperty("faces")).compareTo((Long)o1.getProperty("faces"));
				}
			});
			break;
		case "faceSize":
			Collections.sort(results, new Comparator<Entity>() {
				@Override
				public int compare(Entity o1, Entity o2) {
					return ((Long)o2.getProperty("faceSize")).compareTo((Long)o1.getProperty("faceSize"));
				}
			});
			break;
		case "imageSize":
			Collections.sort(results, new Comparator<Entity>() {
				@Override
				public int compare(Entity o1, Entity o2) {
					return ((Long)o2.getProperty("imageSize")).compareTo((Long)o1.getProperty("imageSize"));
				}
			});
			break;
		case "timestamp":
			Collections.sort(results, new Comparator<Entity>() {
				@Override
				public int compare(Entity o1, Entity o2) {
					return ((Date)o2.getProperty("create-time")).compareTo((Date)o1.getProperty("create-time"));
				}
			});
			break;
		default:
			Collections.sort(results, new Comparator<Entity>() {
				@Override
				public int compare(Entity o1, Entity o2) {
					return ((Date)o2.getProperty("create-time")).compareTo((Date)o1.getProperty("create-time"));
				}
			});
		}
		
		List<Entity> afterTagFilter = new ArrayList<Entity>();
		
		String tags[] = req.getParameter("tags").split(",");
		for (Entity photo : results) {
			int tagsFound = 0;
			for (int i=0; i<tags.length; i++) {
				System.out.println("tags[i]: "+tags[i]);
				if (((String)photo.getProperty("tagsString")).contains(tags[i])) {
					tagsFound++;
				}
			}
			if (tagsFound==tags.length) {
				afterTagFilter.add(photo);
			}
		}
		
		HttpSession session = req.getSession(true);
		session.setAttribute("photos", afterTagFilter);
		
		res.sendRedirect("/viewphotos.jsp");
		
	}
	
public void doTestInserts() {
    	
    	Entity p1 = new Entity("p1", "bk1");
    	p1.setProperty("blob-key", "bk1");
    	p1.setProperty("create-time", new Date());
    	p1.setProperty("facesJson", "");
    	p1.setProperty("faces", 1);
    	p1.setProperty("faceSize", 100);
    	p1.setProperty("emotion", "anger");
    	datastore.put(p1);
    	
    	Entity p2 = new Entity("p2", "bk2");
    	p2.setProperty("blob-key", "bk2");
    	p2.setProperty("create-time", new Date());
    	p2.setProperty("facesJson", "");
    	p2.setProperty("faces", 2);
    	p2.setProperty("faceSize", 200);
    	p2.setProperty("emotion", "anger");
    	datastore.put(p2);
    	
    	Entity p3 = new Entity("p3", "bk3");
    	p3.setProperty("blob-key", "bk3");
    	p3.setProperty("create-time", new Date());
    	p3.setProperty("facesJson", "");
    	p3.setProperty("faces", 3);
    	p3.setProperty("faceSize", 300);
    	p3.setProperty("emotion", "contempt");
    	datastore.put(p3);
    	
    	Entity p4 = new Entity("p4", "bk4");
    	p4.setProperty("blob-key", "bk4");
    	p4.setProperty("create-time", new Date());
    	p4.setProperty("facesJson", "");
    	p4.setProperty("faces", 4);
    	p4.setProperty("faceSize", 400);
    	p4.setProperty("emotion", "contempt");
    	datastore.put(p4);
    	
    	Entity p5 = new Entity("p5", "bk5");
    	p5.setProperty("blob-key", "bk5");
    	p5.setProperty("create-time", new Date());
    	p5.setProperty("facesJson", "");
    	p5.setProperty("faces", 5);
    	p5.setProperty("faceSize", 500);
    	p5.setProperty("emotion", "disgust");
    	datastore.put(p5);
    	
    	Entity p6 = new Entity("p6", "bk6");
    	p6.setProperty("blob-key", "bk6");
    	p6.setProperty("create-time", new Date());
    	p6.setProperty("facesJson", "");
    	p6.setProperty("faces", 6);
    	p6.setProperty("faceSize", 600);
    	p6.setProperty("emotion", "fear");
    	datastore.put(p6);
    	
    	Entity p7 = new Entity("p7", "bk7");
    	p7.setProperty("blob-key", "bk7");
    	p7.setProperty("create-time", new Date());
    	p7.setProperty("facesJson", "");
    	p7.setProperty("faces", 7);
    	p7.setProperty("faceSize", 700);
    	p7.setProperty("emotion", "happiness");
    	datastore.put(p7);
    	
    	Entity p8 = new Entity("p8", "bk8");
    	p8.setProperty("blob-key", "bk8");
    	p8.setProperty("create-time", new Date());
    	p8.setProperty("facesJson", "");
    	p8.setProperty("faces", 8);
    	p8.setProperty("faceSize", 800);
    	p8.setProperty("emotion", "neutral");
    	datastore.put(p8);
    	
    	Entity p9 = new Entity("p9", "bk9");
    	p9.setProperty("blob-key", "bk9");
    	p9.setProperty("create-time", new Date());
    	p9.setProperty("facesJson", "");
    	p9.setProperty("faces", 9);
    	p9.setProperty("faceSize", 900);
    	p9.setProperty("emotion", "sadness");
    	datastore.put(p9);
    	
    	Entity p10 = new Entity("p10", "bk10");
    	p10.setProperty("blob-key", "bk10");
    	p10.setProperty("create-time", new Date());
    	p10.setProperty("facesJson", "");
    	p10.setProperty("faces", 10);
    	p10.setProperty("faceSize", 1000);
    	p10.setProperty("emotion", "surprise");
    	datastore.put(p10);
    	
    }

}

package com.example.group201704082.servlets;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
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
		
		Entity p1 = new Entity("photo", "p1");
		p1.setProperty("blob-key", "AMIfv97BOZ4y33dY0mmHzsJCrD-y2EF_zBd1w97Tkq0Undy3dmKQyl48N2qW-t-gMZL69rhj3JEvjGMa0kknbwbUkAhsLFvaRR9ZhDRwr0TH3HcS_-x_ZfGl54atJVGePPhdaZ7tjFHlyDdFEZLkFdbR767srikqdC_65JYO7oD5jyVpwwUs8g5Nm46sYAMmD1B-kAQLEuDlTvvcrsYBLtudhLBKSnvyAxgxn_8xcW06vJLCXwjqHKmF4iQHeYWnj0E7YomYQN9QfUaOK2QNN1S1luulsTcB_6Gr7DGZ1MUBxxMJwoZ386sezTDgv8JXBhPKvSk_Jq23dggt4pIpZrGQlY7AQmDGeufgyybbToEu5i72DMc3NA-s5wajZvI5LcIqyROowE09");
		p1.setProperty("create-time", new Date());
		p1.setProperty("faces", "[testfacestring]");
		p1.setProperty("userId", "testuser");
		datastore.put(p1);
		
		/*Filter propertyFilter =
	        new FilterPredicate("userId", FilterOperator.EQUAL, "testuser");*/
	    Query q = new Query("photo");
	    
	    /*List<Entity> results =
	        datastore.prepare(q.setKeysOnly()).asList(FetchOptions.Builder.withDefaults());
	      System.out.println("Result size: "+results.size());*/
	    
	    PreparedQuery pq = datastore.prepare(q);
	    List<Entity> results = pq.asList(FetchOptions.Builder.withDefaults());
		System.out.println("Result size: "+results.size());
		
		HttpSession session = req.getSession(true);
		session.setAttribute("photos", results);
		
		res.sendRedirect("/viewphotos.jsp");
		      
		/*res.setContentType("text/html");
	    res.setCharacterEncoding("UTF-8");
	    PrintWriter w = res.getWriter();
	    w.println("<!DOCTYPE html>");
	    w.println("<meta charset=\"utf-8\">");
	    w.println("<title>View Photos Response</title>");
	    w.println("<body><p>"+"Result size: "+results.size()+"</p></body>");
	    w.println("</html>");*/
		
	}

}

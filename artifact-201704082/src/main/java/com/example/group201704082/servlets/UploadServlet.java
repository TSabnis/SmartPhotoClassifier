package com.example.group201704082.servlets;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

public class UploadServlet extends HttpServlet {
	
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
        List<BlobKey> blobKeys = blobs.get("myFile");
        
        for (BlobKey bk : blobKeys) {
        	Entity photo = new Entity("photo", bk.getKeyString());
        	photo.setProperty("blob-key", bk.getKeyString());
        	photo.setProperty("create-time", new Date());
        	
        	String facesJson = getFaces(bk.getKeyString());
        	int faces = 0;
			int faceSize = 0;
            String maxEmo = "";
            JSONArray jsonArray;
    		try {
    			jsonArray = new JSONArray(facesJson);
    			faces = jsonArray.length();
    			if (faces > 0) {
    	            JSONObject jObj = jsonArray.getJSONObject(0);
    	            JSONObject faceRectangle = (JSONObject) jObj.get("faceRectangle");
    	            JSONObject scores = (JSONObject) jObj.get("scores");
    	            
    	            String emotions[] = {"anger","contempt","disgust","fear","happiness","neutral","sadness","surprise"};
    	            Double emotionScores[] = new Double[8];
    	            emotionScores[0] = scores.getDouble("anger");
    	            emotionScores[1] = scores.getDouble("contempt");
    	            emotionScores[2] = scores.getDouble("disgust");
    	            emotionScores[3] = scores.getDouble("fear");
    	            emotionScores[4] = scores.getDouble("happiness");
    	            emotionScores[5] = scores.getDouble("neutral");
    	            emotionScores[6] = scores.getDouble("sadness");
    	            emotionScores[7] = scores.getDouble("surprise");
    	            
    	            Double max=emotionScores[0];
    	            int maxIndex=0;

    	            for (int i1 = 0; i1 < emotionScores.length; i1++) {
    	                if (emotionScores[i1] > max) {
    	                    max = emotionScores[i1];
    	                    maxIndex = i1;
    	                }
    	            }
    	            
    	            
    	            faceSize = faceRectangle.getInt("height")*faceRectangle.getInt("width");
    	            maxEmo = emotions[maxIndex];
    	        }
    			
    			System.out.println(faceSize);
                System.out.println(maxEmo);
                
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        	
    		photo.setProperty("facesJson", facesJson);
    		photo.setProperty("faces", faces);
    		photo.setProperty("faceSize", faceSize);
    		photo.setProperty("emotion", maxEmo);
        	
        	datastore.put(photo);
        }
    	
    	res.sendRedirect("/");
    }
    
    public String getFaces(String blobKey) {
        HttpClient httpClient = new DefaultHttpClient();

        try {
            URIBuilder uriBuilder = new URIBuilder("https://westus.api.cognitive.microsoft.com/emotion/v1.0/recognize");

            URI uri = uriBuilder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers. Replace the example key below with your valid subscription key.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", "fbe1e7618e6d4fb5a51a6cc5de3fb500");

            // Request body. Replace the example URL below with the URL of the image you want to analyze.
            StringEntity reqEntity = new StringEntity("{ \"url\": \"https://project20140410.appspot.com/serve?blob-key="+blobKey+"\" }");
            request.setEntity(reqEntity);

            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                String faces = EntityUtils.toString(entity);
            	System.out.println(faces);
                return faces;
            }
            else {
            	return "empty";
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }
}

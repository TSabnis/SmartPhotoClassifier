package com.example.group201704082.servlets;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;

public class UploadServlet extends HttpServlet {
	
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
        List<BlobKey> blobKeys = blobs.get("myFile");
        List<Entity> photos = new ArrayList<Entity>();
        Map<String,Map<String,String>> photoFaceIdMap = new HashMap<String,Map<String,String>>();
        
        for (BlobKey bk : blobKeys) {
        	
        	String facesJson = getFaces(bk.getKeyString());
        	if (facesJson == null) {
        		continue;
        	}
        	Entity photo = new Entity("photo", bk.getKeyString());
        	photo.setProperty("blob-key", bk.getKeyString());
        	photo.setProperty("create-time", new Date());
        	int faces = 0;
			int faceSize = 0;
            String maxEmo = "";
            Map<String,String> faceIdMap = new HashMap<String,String>();
            JSONArray jsonArray;
    		try {
    			jsonArray = new JSONArray(facesJson);
    			faces = jsonArray.length();
    			
    			for(int i=0;i<faces;i++) {
	            	
	            	JSONObject faceJson = jsonArray.getJSONObject(i);
    	            JSONObject faceRectangle = (JSONObject) faceJson.get("faceRectangle");
    	            
    	            if (i==0) {
    	            	JSONObject scores = (JSONObject)((JSONObject) faceJson.get("faceAttributes")).get("emotion");
        	            
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
	            	
	            	String similarityJson = findSimilar(faceJson.getString("faceId"));
	            	JSONArray similarityJsonArray = new JSONArray(similarityJson);
	            	JSONObject similarityObj = similarityJsonArray.getJSONObject(0);
	            	if (similarityObj.getDouble("confidence")>0.5) {
	            		faceIdMap.put(faceJson.getString("faceId"),similarityObj.getString("persistedFaceId"));
	            	}
	            	else {
	            		faceIdMap.put(faceJson.getString("faceId"),
	            				addFace(bk.getKeyString(), 
	            						faceRectangle.getInt("left"), faceRectangle.getInt("top"), faceRectangle.getInt("width"), faceRectangle.getInt("height")));
	            	}
	            }
    			
    			photoFaceIdMap.put(bk.getKeyString(), faceIdMap);
    			
                photo.setProperty("facesJson", facesJson);
        		photo.setProperty("faces", faces);
        		photo.setProperty("faceSize", faceSize);
        		photo.setProperty("emotion", maxEmo);
            	
            	datastore.put(photo);
            	photos.add(photo);
                
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		
        }
        List<Entity> faces = datastore.prepare(new Query("face")).asList(FetchOptions.Builder.withDefaults());
        Map<String,String> faceNameMap = new HashMap<String,String>();
        for (Entity face : faces) {
        	faceNameMap.put(face.getProperty("persistedFaceId").toString(), face.getProperty("taggedName").toString());
        }
        HttpSession session = req.getSession(true);
		session.setAttribute("photos", photos);
		session.setAttribute("faceNameMap", faceNameMap);
		session.setAttribute("photoFaceIdMap", photoFaceIdMap);
		res.sendRedirect("/tagphotos.jsp");
    	
    }
    
    public String addFace(String blobKey, int left, int top, int width, int height) {
    	try
        {
        	HttpClient httpClient = new DefaultHttpClient();
        	
        	URIBuilder uriBuilder = new URIBuilder("https://westus.api.cognitive.microsoft.com/face/v1.0/facelists/facelistid1/persistedFaces");

            uriBuilder.setParameter("faceListId", "facelistid1");
            uriBuilder.setParameter("targetFace", left+","+top+","+width+","+height);

            URI uri = uriBuilder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers. Replace the example key below with your valid subscription key.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", "9b07c2846f7648349fce895a4b6acbce");

            // Request body. Replace the example URL below with the URL of the image you want to analyze.
            StringEntity reqEntity = new StringEntity("{\"url\":\"https://project20140410.appspot.com/serve?blob-key="+blobKey+"\"}");
            request.setEntity(reqEntity);

            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
            	JSONObject jsonObj = new JSONObject(EntityUtils.toString(entity));
            	return jsonObj.getString("persistedFaceId");
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    	return null;
    }
    
    public String findSimilar(String faceId) {
    	try
        {
        	HttpClient httpClient = new DefaultHttpClient();
        	
        	URIBuilder uriBuilder = new URIBuilder("https://westus.api.cognitive.microsoft.com/face/v1.0/findsimilars");

            URI uri = uriBuilder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers. Replace the example key below with your valid subscription key.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", "9b07c2846f7648349fce895a4b6acbce");

            StringEntity reqEntity = new StringEntity("{"+    
				    "\"faceId\":\""+faceId+"\","+
				    "\"faceListId\":\"facelistid1\","+
				    "\"maxNumOfCandidatesReturned\":1,"+
				    "\"mode\": \"matchFace\""+
				"}");
            request.setEntity(reqEntity);

            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                return EntityUtils.toString(entity);
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    	return null;
    }
    
    public String getFaces(String blobKey) {
    	try
        {
        	HttpClient httpClient = new DefaultHttpClient();
        	
        	URIBuilder uriBuilder = new URIBuilder("https://westus.api.cognitive.microsoft.com/face/v1.0/detect");

            uriBuilder.setParameter("returnFaceId", "true");
            uriBuilder.setParameter("returnFaceLandmarks", "false");
            uriBuilder.setParameter("returnFaceAttributes", "emotion");

            URI uri = uriBuilder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers. Replace the example key below with your valid subscription key.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", "9b07c2846f7648349fce895a4b6acbce");

            // Request body. Replace the example URL below with the URL of the image you want to analyze.
            StringEntity reqEntity = new StringEntity("{\"url\":\"https://project20140410.appspot.com/serve?blob-key="+blobKey+"\"}");
            //StringEntity reqEntity = new StringEntity("{\"url\":\"http://dreamatico.com/data_images/face/face-2.jpg\" }");
            request.setEntity(reqEntity);

            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() != 200) {
            	return null;
            }
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                return (EntityUtils.toString(entity));
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    	return null;
    }
    
    public String getEmotions(String blobKey) {
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

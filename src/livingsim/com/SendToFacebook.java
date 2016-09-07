package livingsim.com;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;

public class SendToFacebook extends Fragment{
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private static boolean pendingPublishReauthorization = false;
	private static final String TAG = "MainFragment";
	private static String ret = "";
	
	public static String publishStory(final Activity c, String toWrite) {
		
		ret = "true";
	    Session session = Session.getActiveSession();
	    if (session != null) {

		    // Check for publish permissions    
	    	try{
			    List<String> permissions = session.getPermissions();
			        if (!isSubsetOf(PERMISSIONS, permissions)) {
			        	pendingPublishReauthorization = true;
			            Session.NewPermissionsRequest newPermissionsRequest = new Session
			                    .NewPermissionsRequest(c, PERMISSIONS);
			            session.requestNewPublishPermissions(newPermissionsRequest);
			            ret = "false";
			       }
	    	}catch(Exception e){
	    		return "You are not logged in! Go to the Instructions page to login";
	    	}
		       
		    Bundle postParams = new Bundle();
		    postParams.putString("name", "Living Simulation");
		    postParams.putString("caption", "Nothing Wrong With Living In Sim!");
		    postParams.putString("description", toWrite);
		    postParams.putString("link", "https://developers.facebook.com/android");
		    postParams.putString("picture", "http://livingagame.com/images/LivingSimulationIcon.png");

		    Request.Callback callback= new Request.Callback() {
		        public void onCompleted(Response response) {
		            JSONObject graphResponse = response
		                                       .getGraphObject()
		                                       .getInnerJSONObject();
		            String postId = null;
		            try {
		                postId = graphResponse.getString("id");
		            } catch (JSONException e) {
		                Log.i(TAG,
		                    "JSON error "+ e.getMessage());
		                ret = "false";
		            }
		            FacebookRequestError error = response.getError();
		            if (error != null) {
		                Toast.makeText(c.getApplicationContext(),
		                     error.getErrorMessage(),
		                     Toast.LENGTH_SHORT).show();
		                	ret = "false";
		                }
		        }
		    };

		    Request request = new Request(session, "me/feed", postParams, 
		                          HttpMethod.POST, callback);

		    RequestAsyncTask task = new RequestAsyncTask(request);
		    task.execute();
		}
		return ret;
	}
	
	private static boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
	    for (String string : subset) {
	        if (!superset.contains(string)) {
	            return false;
	        }
	    }
	    return true;
	}
}

package livingsim.com;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

public class DeathFragment extends Fragment {
	
	private static final String TAG = "MainFragment";
	
	private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(final Session session, final SessionState state, final Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
    
	private Button shareButton;
	private EditText fbText;
	private Button submit;
	private EditText name;
	private DatabaseHandler db;
	
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	
	private boolean pendingPublishReauthorization = false;

	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container, 
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.funeral, container, false);
        
		LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
		authButton.setFragment(this);
		
		shareButton = (Button) view.findViewById(R.id.submit_toFB);
		fbText 		= (EditText) view.findViewById(R.id.faceBookText);
		submit = (Button) view.findViewById(R.id.submit_hs);
		name = (EditText) view.findViewById(R.id.userName);
		db = new DatabaseHandler(getActivity().getApplicationContext());
		
		fbText.setText("In Living Simulation, the Android App, I have just died. :( . " + 
					   "I scored " + Funeral.lifeTimePoints.toString() + " LifeTime points. Can you beat my score?!");
		
		shareButton.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	//If 1 then the submit will be made to Facebook as well as the app
		    	submitHighScore(1);
		    }
		});
		
        name.setOnFocusChangeListener(new OnFocusChangeListener(){
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(name.getText().toString().contains("Enter Your Name") == true){
					name.setText("");
				}
			}
        });
        
		submit.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//If 0 then the submit will not be made to Facebook
				submitHighScore(0);
			}
        });
		
		if (savedInstanceState != null) {
			pendingPublishReauthorization = 
				savedInstanceState.getBoolean(PENDING_PUBLISH_KEY, false);
		}
		return view;
	}
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        
        // For scenarios where the main activity is launched and user
		// session is not null, the session state change notification
		// may not be triggered. Trigger it if it's open/closed.
		Session session = Session.getActiveSession();
		if (session != null &&
				(session.isOpened() || session.isClosed()) ) {
			onSessionStateChange(session, session.getState(), null);
		}
		
        uiHelper.onResume();
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(PENDING_PUBLISH_KEY, pendingPublishReauthorization);
        uiHelper.onSaveInstanceState(outState);
    }
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            shareButton.setVisibility(View.VISIBLE);
            if (pendingPublishReauthorization && 
            		state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
            	pendingPublishReauthorization = false;
            	publishStory();
            }
        } else if (state.isClosed()) {
            shareButton.setVisibility(View.INVISIBLE);
        }
    }

	private void publishStory() {
		String toWrite = fbText.getText().toString();
		
	    Session session = Session.getActiveSession();
	    if (session != null) {

		    // Check for publish permissions    
		    List<String> permissions = session.getPermissions();
		        if (!isSubsetOf(PERMISSIONS, permissions)) {
		        	pendingPublishReauthorization = true;
		            Session.NewPermissionsRequest newPermissionsRequest = new Session
		                    .NewPermissionsRequest(this, PERMISSIONS);
		            session.requestNewPublishPermissions(newPermissionsRequest);
		            return;
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
		            }
		            FacebookRequestError error = response.getError();
		            if (error != null) {
		                Toast.makeText(getActivity()
		                     .getApplicationContext(),
		                     error.getErrorMessage(),
		                     Toast.LENGTH_SHORT).show();
		                }
		        }
		    };

		    Request request = new Request(session, "me/feed", postParams, 
		                          HttpMethod.POST, callback);

		    RequestAsyncTask task = new RequestAsyncTask(request);
		    task.execute();
		}
	}
	
	private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
	    for (String string : subset) {
	        if (!superset.contains(string)) {
	            return false;
	        }
	    }
	    return true;
	}

	private void submitHighScore(Integer toFacebook){
		String nameText = name.getText().toString();
		String result = "";
		boolean t = false;
		if(nameText.length() < 1 || nameText.contains("%") || 
				nameText.contains("\"")){
			result = "Please Enter Your Name and Click Submit.";
		}else{
			result = db.insertHighScore(nameText);
			db.updateLifetimePoints(187);
			t = true;
		}
		
		if(toFacebook == 1){
			publishStory();
		}
		
		Toast msg = Toast.makeText(getActivity().getApplicationContext(),
                result, Toast.LENGTH_LONG);
		msg.show();
		
		if(t){
			Intent intentMain = new Intent(getActivity().getApplicationContext(), 
					LivingSimulationActivity.class);
			intentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			getActivity().getApplicationContext().startActivity(intentMain);
		}
	}
}

package livingsim.com;

import com.facebook.widget.LoginButton;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Layout.Alignment;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Funeral extends FragmentActivity {
    /** Called when the activity is first created. */
	Boolean isDead = false;
	private DeathFragment mainFragment;
	public static Integer lifeTimePoints;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.funeral);
        final EditText name = (EditText) findViewById(R.id.userName);
        final EditText fbText = (EditText) findViewById(R.id.faceBookText);
        final LoginButton fbButton = (LoginButton) findViewById(R.id.authButton);
        final Button fbSubmit 	   = (Button) findViewById(R.id.submit_toFB);
        
        final TextView highScores = (TextView)findViewById(R.id.high_scores);
        final Button submit = (Button) findViewById(R.id.submit_hs);
        final DatabaseHandler db = new DatabaseHandler(this);
        lifeTimePoints = db.getLifetimePoints();
        
        String[][] hscores = db.getHighScores();
        Bundle checkDead = getIntent().getExtras();
        
        if (checkDead != null) {
        	isDead = checkDead.getBoolean("isDead");
        }
        
        if(isDead == false){
        	submit.setVisibility(View.GONE);
        	name.setVisibility(View.GONE);
        	fbButton.setVisibility(View.GONE);
        	fbText.setVisibility(View.GONE);
        	fbSubmit.setVisibility(View.GONE);
        }else{
        	if (savedInstanceState == null) {
            	// Add the fragment on initial activity setup
            	mainFragment = new DeathFragment();
                getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, mainFragment)
                .commit();
            } else {
            	// Or set the fragment from restored state info
            	mainFragment = (DeathFragment) getSupportFragmentManager()
            	.findFragmentById(android.R.id.content);
            }
        }
         
        TableLayout MainLayout = (TableLayout) findViewById(R.id.tLayout);
        
        if(hscores != null){
	        for(int i = 0; i<= 10; i++){
	        	String textVals[] = new String[3];
	        	Integer textSize = 0;
	        	if(i == 0){
	        		textVals[0] = "Name";
	        		textVals[1] = "Score";
	        		textVals[2] = "Date";
	        		textSize = 18;
	        	}else{
	        		textVals[0] = hscores[i-1][0];
	        		textVals[1] = hscores[i-1][1];
	        		textVals[2] = hscores[i-1][2];
	        		textSize = 14;
	        	}
	        	
		        //Create the first row and add two text views
		        TableRow row1 = new TableRow(this);
		        TextView text1 = new TextView(this);
		        text1.setText(textVals[0]);
		        text1.setTextColor(Color.WHITE);
		        text1.setTextSize(textSize);
		        text1.setGravity(Gravity.CENTER);
		        text1.setWidth(175);
		        
		        TextView text2 = new TextView(this);
		        text2.setText(textVals[1]);
		        text2.setTextColor(Color.WHITE);
		        text2.setTextSize(textSize);
		        text2.setGravity(Gravity.CENTER);
		        text2.setWidth(175);
		        
		        TextView text3 = new TextView(this);
		        text3.setText(textVals[2]);
		        text3.setTextColor(Color.WHITE);
		        text3.setTextSize(textSize);
		        text3.setGravity(Gravity.CENTER);
		        text3.setWidth(175);
		        
		        row1.addView(text1);
		        row1.addView(text2);
		        row1.addView(text3);
		        MainLayout.addView(row1);
	        }
        }
    }
    
    @Override
    public void onBackPressed() {
    	if(isDead == true){
	    	Toast msg = Toast.makeText(Funeral.this,
	                "Please Enter Your Name and Click Submit", Toast.LENGTH_LONG);
			msg.show();
    	}
    }
    
    //This create the Menu on the bottom of the page which is the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	if(isDead == false){
	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.menu, menu);
	        return true;
    	}else{
    		return false;
    	}
    }
    
    //This handles the code for the bottom of the page, the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.create:
            	startActivity(new Intent(this, CreateActivity.class));
                return true;
            case R.id.doActivity:
            	startActivity(new Intent(this, LivingSimulationActivity.class));;
                return true;
            case R.id.avatar:
            	startActivity(new Intent(this, Avatar.class));
            	return true;
            case R.id.scores:
            	startActivity(new Intent(this, Funeral.class));
            	return true;
            case R.id.instructions:
            	startActivity(new Intent(this, Instructions.class));
            	return true;
            case R.id.changeDecrease:
            	startActivity(new Intent(this, ChangeDecrease.class));
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

//Need to update the values and display the updated values

package livingsim.com;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.ParseException;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class LivingSimulationActivity extends Activity {
	Spinner spinner;
	TextView hygiene;
	TextView social;
	TextView hunger;
	TextView work;
	TextView energy;
	TextView fitness;
	TextView fun;
	Activity a;
	EditText fbText;
	CheckBox fbCheck;
	Boolean initialDisplay = true;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        a = this;
        
        final DatabaseHandler db = new DatabaseHandler(this);
        hygiene = (TextView)findViewById(R.id.hygieneValue);
        social = (TextView)findViewById(R.id.socialValue);
        work = (TextView)findViewById(R.id.workValue);
        hunger = (TextView)findViewById(R.id.hungerValue);
        energy = (TextView)findViewById(R.id.energyValue);
        fitness = (TextView)findViewById(R.id.fitnessValue);
        fun = (TextView)findViewById(R.id.funValue);
        final TextView lastUpdatedText = (TextView) findViewById(R.id.updateValue);
        spinner = (Spinner) findViewById(R.id.spinner1);
        fbText = (EditText) findViewById(R.id.faceBookText2);
        fbCheck = (CheckBox)findViewById(R.id.toFbCheck); 
        
        //FOR TESTING ONLY
        //db.updateLifetimePoints(40);
        		
        loadSpinnerData();
        
        final Button doActivity = (Button) findViewById(R.id.button1);
        
        doActivity.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(fbCheck.isChecked()){
					String s = SendToFacebook.publishStory(a, fbText.getText().toString());
					if(s == "true"){
						Toast.makeText(getApplicationContext(), "Status Updated", Toast.LENGTH_SHORT).show();
					}else{
						if(s == "false"){
							s = "Error Updating Status";
						}
							Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(getApplicationContext(), "Status NOT Updated", Toast.LENGTH_SHORT).show();
				}
				
				double[] vals = db.getValues();
				String addValue = spinner.getSelectedItem().toString();
				
				if(addValue.contains("Sleeping")){
					//Need to calculate Lifetime points
					Integer addPoints = calculatePoints(vals);
					String toastIt = "";
					if(addPoints == 420){
						addValue = "";
						toastIt = "You are not tired!";
					}else if (addPoints == 187){
						toastIt = "You are now dead!";
						Intent intentMain = new Intent(LivingSimulationActivity.this, 
								Funeral.class);
						intentMain.putExtra("isDead", true); 
						LivingSimulationActivity.this.startActivity(intentMain);
					}else{
						toastIt = db.updateLifetimePoints(addPoints);
						vals = db.getValues();
						db.toggleSleeping();
						Toast.makeText(getApplicationContext(), toastIt, Toast.LENGTH_LONG).show();
						Intent intentMain = new Intent(LivingSimulationActivity.this, 
								Sleeping.class);
						LivingSimulationActivity.this.startActivity(intentMain);
					}
					Toast.makeText(getApplicationContext(), toastIt, Toast.LENGTH_LONG).show();
					
				}
				
				if(addValue != ""){
					vals = db.addValues(vals, addValue);
					updateTextViews(vals);
				}
			}
        });
        
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				String spinActivity = spinner.getSelectedItem().toString();
				fbText.setText("I just did the activity '" + spinActivity + "' in Living Simulation! Come play Living Simulation on your Android device!");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        
        int ticker = 600000;
        
        //Timer goes off every ten minutes to update your Life Level
        new CountDownTimer(2147483647, ticker) {
        	public void onTick(long millisUntilFinished) {
        		try{
        			
        			//Initial Database calls
        	        String lastUpdate = db.getLastUpdate();
        	        
        	        lastUpdatedText.setText(lastUpdate);
        	        
        	        double[] vals = db.getValues();
        	        double[] sub = db.getSubtractVals();
        	        Integer LifeTimePoints = db.getLifetimePoints();
        	        setTitle("Living Simulation - Lifetime Points: " + LifeTimePoints.toString());
        	        
        	        int updateAmount = needUpdate(lastUpdate);
        	        
        	        if(updateAmount > 0){
        	        	for(int i =0; i<=6; i++){
        	        		vals[i] = vals[i] + (sub[i] * updateAmount);
        	        		if(vals[i] < 1){
        	        			vals[i] = 1.0;
        	        		}
        	        	}
        	        	vals = db.updateValues(vals);
        	        }
        	        
        	        updateTextViews(vals);
        		}catch(Exception e){
        			fun.setText(e.toString());
        		}
			}
            public void onFinish() {
                
            }
         }.start();
    }
    
    //This function updates the text views using the function updateTicks
    public boolean updateTextViews(double[] vals){
    	hygiene.setText(updateTicks(vals[0]));
		social.setText(updateTicks(vals[1]));
		work.setText(updateTicks(vals[2]));
		hunger.setText(updateTicks(vals[3]));
		energy.setText(updateTicks(vals[4]));
		fitness.setText(updateTicks(vals[5]));
		fun.setText(updateTicks(vals[6]));
		
		hygiene.setTextColor(Color.parseColor(updateColor(vals[0])));
		social.setTextColor(Color.parseColor(updateColor(vals[1])));
		work.setTextColor(Color.parseColor(updateColor(vals[2])));
		hunger.setTextColor(Color.parseColor(updateColor(vals[3])));
		energy.setTextColor(Color.parseColor(updateColor(vals[4])));
		fitness.setTextColor(Color.parseColor(updateColor(vals[5])));
		fun.setTextColor(Color.parseColor(updateColor(vals[6])));
		
		return true;
    }
    //This gets the color of the string based on the value
    public String updateColor(double value){
    	String st = "";
    	if(value >= 7){
    		st = "#00FF00";
    	}else if(value >= 4){
    		st = "#FFFF00";
    	}else{
    		st = "#FF0000";
    	}
    	return st;
    }
    
    //This converts the number to the ticks that make up the LifeLevel
    public String updateTicks(double value){
    	value = value * 5;
    	String retString = "";
    	for(int i =0; i< value; i++){
    		retString += "l";
    	}
    	return retString;
    }
    
    //Checks to see if the program should update the Life Level
    public int needUpdate(String lastUpdate){
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date convertedDate = new Date();
        Date currentDate = new Date();
        double timeDiff = 0;
        
        try {
            convertedDate = dateFormat.parse(lastUpdate);
            timeDiff = currentDate.getTime() - convertedDate.getTime();
            timeDiff = timeDiff / (60 * 1000);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        } catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}

    	return (int) (timeDiff / 10);
    }
   
    
    //Loads all of the information from the Activity Data table into the spinner
    private void loadSpinnerData() {
        // database handler
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
 
        // Spinner Drop down elements
        List<String> allRows = db.getAllRows();
 
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, allRows);
 
        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }
    
    public Integer calculatePoints(double[] vals){
    	Integer addPoints = 0;
    	Integer losingCount = 0;
    	Integer otherLosingCount = 0;
    	
    	//Check to see if the Energy section is too High.If it is too high, 
    	//Then the user is unable to Sleep. This code is 420
    	if(vals[4] >= 5){
    		addPoints = 420;
    	}else{
	    	for(int i = 0; i<= 6; i++){
	    		//Checks to see if the energy section is up on the list, if yes skip
	    		if(i != 4){
	    			//If vals > 6, give them 1 point for each number greater than 6
		    		if(vals[i] > 6){
		    			addPoints = (int) (addPoints + vals[i] - 6);
		    		//If vals < 4, subtract 1 point for each number less than 4
		    		//If all vals are less than 4, then DEATH
		    		}else if(vals[i] < 4){
		    			otherLosingCount += 1;
		    			addPoints = (int) (addPoints + (vals[i] - 4));
		    		}
		    		
		    		//If vals is 1, add 1 to losing count, if it equals 4 then DEATH
		    		if(vals[i] == 1){
		    			losingCount += 1;
		    		}
	    		}
	    	}
    	}
    	
    	//Checks if the user died. Since I have to return an Integer, 
    	//187 is the code for DEATH
    	if(losingCount >= 4 || otherLosingCount >= 7){
    		addPoints = 187;
    	}
        
    	return addPoints;
    }
    
    //This create the Menu on the bottom of the page which is the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
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
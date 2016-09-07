package livingsim.com;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreateActivity extends Activity {
    /** Called when the activity is first created. */
	Spinner[] spinners = new Spinner[7];
	Spinner activitySpinner;
	Spinner hygiene;
	Spinner social;
	Spinner hunger;
	Spinner work;
	Spinner energy;
	Spinner fitness;
	Spinner fun;
	EditText name;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create);
        final DatabaseHandler db = new DatabaseHandler(this);
        Button create = (Button) findViewById(R.id.create);
        name = (EditText) findViewById(R.id.activityName);
        spinners[0] = (Spinner)findViewById(R.id.spinhygieneValue);
        spinners[1] = (Spinner)findViewById(R.id.spinsocialValue);
        spinners[2] = (Spinner)findViewById(R.id.spinworkValue);
        spinners[3] = (Spinner)findViewById(R.id.spinhungerValue);
        spinners[4] = (Spinner)findViewById(R.id.spinenergyValue);
        spinners[5] = (Spinner)findViewById(R.id.spinfitnessValue);
        spinners[6] = (Spinner)findViewById(R.id.spinfunValue);
        activitySpinner = (Spinner) findViewById(R.id.activityNameSpinner);
        loadSpinnerData();
        
        create.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int hygval = Integer.parseInt(spinners[0].getSelectedItem().toString());
				int socval = Integer.parseInt(spinners[1].getSelectedItem().toString());
				int worval = Integer.parseInt(spinners[2].getSelectedItem().toString());
				int hunval = Integer.parseInt(spinners[3].getSelectedItem().toString());
				int eneval = Integer.parseInt(spinners[4].getSelectedItem().toString());
				int fitval = Integer.parseInt(spinners[5].getSelectedItem().toString());
				int funval = Integer.parseInt(spinners[6].getSelectedItem().toString());
				String origName = activitySpinner.getSelectedItem().toString();
				String nameStr = name.getText().toString();
				String val = "";
				
				if(activitySpinner.getSelectedItemPosition() == 0){
					val = db.createActivity(nameStr, hygval, socval, worval, hunval, eneval, fitval, funval);
				}else{
					val = db.updateActivity(origName, nameStr, hygval, socval, worval, hunval, eneval, fitval, funval);
				}
				
				loadSpinnerData();
				Toast msg = Toast.makeText(CreateActivity.this,
                        val, Toast.LENGTH_LONG);
				msg.show();
				
				name.setText("");
			}
        });
        
        activitySpinner.setOnItemSelectedListener(new OnItemSelectedListener()  {
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if(activitySpinner.getSelectedItemPosition() != 0){
					String activity = activitySpinner.getSelectedItem().toString();
					double[] vals = db.getValuesToAdd(activity);
					name.setText(activity);
					//Put all of the values from the activity into the spinners
					for(int i=0; i<= 6; i++){
						if(vals[i] < 0){
							spinners[i].setSelection((int) vals[i] + 10);
						}else if(vals[i] > 0){
							spinners[i].setSelection((int) vals[i]+9);
						}else{
							spinners[i].setSelection((int) vals[i]);
						}
					}
				}
			}
		});
    }
    
    //Loads all of the information from the Activity Data table into the spinner
    private void loadSpinnerData() {
        // database handler
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
 
        // Spinner Drop down elements
        List<String> allRows = db.getAllRows();
        
        allRows.add(0, "Create New Activity");
        
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, allRows);
 
        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 
        // attaching data adapter to spinner
        activitySpinner.setAdapter(dataAdapter);
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
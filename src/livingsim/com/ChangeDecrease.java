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

public class ChangeDecrease extends Activity {
    /** Called when the activity is first created. */
	Spinner[] spinners = new Spinner[7];
	EditText[] decVals = new EditText[7];
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changedecrease);
        final DatabaseHandler db = new DatabaseHandler(this);
        Button create = (Button) findViewById(R.id.editDecrease);

        decVals[0] = (EditText)findViewById(R.id.txtHygiene);
        decVals[1] = (EditText)findViewById(R.id.txtSocial);
        decVals[2] = (EditText)findViewById(R.id.txtWork);
        decVals[3] = (EditText)findViewById(R.id.txtHunger);
        decVals[4] = (EditText)findViewById(R.id.txtEnergy);
        decVals[5] = (EditText)findViewById(R.id.txtFitness);
        decVals[6] = (EditText)findViewById(R.id.txtFun);
        
        final Double[] minVals = new Double[7];
        minVals[0] = -2.0;
        minVals[1] = -2.0;
        minVals[2] = -2.0;
        minVals[3] = -2.0;
        minVals[4] = -2.0;
        minVals[5] = -2.0;
        minVals[6] = -2.0;
        
        final double[] dbVals = db.getSubtractVals();
        
        for(int i=0; i<= 6; i++){
        	//Changes dbVals to 24 hour period
        	dbVals[i] = dbVals[i] * (6 * 24);
        	decVals[i].setText(Double.toString(dbVals[i]));	
        }
        
        create.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Double finalVals[] = new Double[7];
				
				for(int i=0; i<= 6; i++){
					try{
						finalVals[i] = Double.parseDouble(decVals[i].getText().toString());
					}catch(Exception e){
						Toast.makeText(getApplicationContext(), "Check to make sure all values are numbers.", Toast.LENGTH_SHORT).show();
						return;
					}
					
					if(finalVals[i] > minVals[i]){
						Toast.makeText(getApplicationContext(), "Check to make sure all values are less than or equal to the Minimum.", Toast.LENGTH_SHORT).show();
						return;
					}
				}
				
				String isT = db.updateDecreaseVals(finalVals[0], finalVals[1], finalVals[2], 
						finalVals[3], finalVals[4], finalVals[5], finalVals[6]);
				
				if(isT == "true"){
					Toast.makeText(getApplicationContext(), "Your decrease settings have been updated!", Toast.LENGTH_SHORT).show();
					finish();
		  			startActivity(getIntent());
				}else{
					Toast.makeText(getApplicationContext(), isT, Toast.LENGTH_SHORT).show();
				}
			}
        });
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
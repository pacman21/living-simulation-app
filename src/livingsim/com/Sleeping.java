package livingsim.com;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
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

public class Sleeping extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.sleeping);
        Button wake = (Button) findViewById(R.id.wakeUp);
        final DatabaseHandler db = new DatabaseHandler(this);
        
        wake.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				db.toggleSleeping();
				Intent intentMain = new Intent(Sleeping.this, 
						LivingSimulationActivity.class);
				Sleeping.this.startActivity(intentMain);
			}
		});
        
    }
    
    @Override
    public void onBackPressed() {
    	Toast msg = Toast.makeText(Sleeping.this,
	                "Please Hit The Wake Up Button To Continue Using This App!", Toast.LENGTH_LONG);
		msg.show();
    }
}

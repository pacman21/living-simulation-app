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

public class Instructions extends FragmentActivity {
    /** Called when the activity is first created. */
	private InstructionsFragment mainFragment;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.funeral);
        final LoginButton fbButton = (LoginButton) findViewById(R.id.authButton);
        

        if(savedInstanceState == null) {
            	// Add the fragment on initial activity setup
            	mainFragment = new InstructionsFragment();
                getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, mainFragment)
                .commit();
            } else {
            	// Or set the fragment from restored state info
            	mainFragment = (InstructionsFragment) getSupportFragmentManager()
            	.findFragmentById(android.R.id.content);
            }
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

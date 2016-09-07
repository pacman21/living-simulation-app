package livingsim.com;

import com.e3roid.drawable.Drawable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Avatar extends Activity {
	ImageView head;
	ImageView body;
	ImageView pants;
	ImageView shoes;
	TextView headCost;
	TextView bodyCost;
	TextView pantsCost;
	TextView shoesCost;
	Integer lifeTimePoints;
	
    public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
      setContentView(R.layout.avatar);
      Button head_left = (Button) findViewById(R.id.head_left);
      Button head_right = (Button) findViewById(R.id.head_right);
      Button body_left = (Button) findViewById(R.id.body_left);
      Button body_right = (Button) findViewById(R.id.body_right);
      Button pants_left = (Button) findViewById(R.id.pants_left);
      Button pants_right = (Button) findViewById(R.id.pants_right);
      Button shoes_left = (Button) findViewById(R.id.shoes_left);
      Button shoes_right = (Button) findViewById(R.id.shoes_right);
      Button wear = (Button) findViewById(R.id.wearAndBuy);
      
      head = (ImageView) findViewById(R.id.head);
      body = (ImageView) findViewById(R.id.body);
      pants = (ImageView) findViewById(R.id.pants);
      shoes = (ImageView) findViewById(R.id.shoes);
      headCost = (TextView) findViewById(R.id.headPrice);
      bodyCost = (TextView) findViewById(R.id.bodyPrice);
      pantsCost = (TextView) findViewById(R.id.pantsPrice);
      shoesCost = (TextView) findViewById(R.id.shoesPrice);
      
      final Context c = getApplicationContext();
      
      final DatabaseHandler db = new DatabaseHandler(this);
      
      final StoreItem[] allHeads = db.getStoreItems(1);
      final StoreItem[] allShirts = db.getStoreItems(2);
      final StoreItem[] allPants = db.getStoreItems(3);
      final StoreItem[] allShoes = db.getStoreItems(4);
      
      lifeTimePoints = db.getLifetimePoints();
     
      
      allHeads[0].curr_item = 0;
      allShirts[0].curr_item = 0;
      allPants[0].curr_item = 0;
      allShoes[0].curr_item = 0;
      
      //Sets the clothes of the avatar
      checkWearing(allHeads, head, c);
      checkWearing(allShirts, body, c);
      checkWearing(allPants, pants, c);
      checkWearing(allShoes, shoes, c);
      
      wear.setOnClickListener(new View.OnClickListener() {
  		@Override
  		public void onClick(View arg0) {
  			String toast = buyItemsAndWearItems(db, allHeads[allHeads[0].curr_item], 
  					allShirts[allShirts[0].curr_item], 
  					allPants[allPants[0].curr_item], 
  					allShoes[allShoes[0].curr_item]);
  			
  			Toast.makeText(c, toast, Toast.LENGTH_LONG).show();
  			
  			finish();
  			startActivity(getIntent());
  		}
        });
      
      head_right.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			allHeads[0].currItemAdd();
			Integer curr_item = allHeads[0].curr_item;
			
			head.setImageResource(allHeads[curr_item].getResourceId(c));
			
			setCost(allHeads[curr_item], curr_item, headCost);
		}
      });
      
      head_left.setOnClickListener(new View.OnClickListener() {
  		@Override
  		public void onClick(View arg0) {
			allHeads[0].currItemSub();
			Integer curr_item = allHeads[0].curr_item;
			
			head.setImageResource(allHeads[curr_item].getResourceId(c));
			setCost(allHeads[curr_item], curr_item, headCost);
		}
        });
      
      body_right.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			allShirts[0].currItemAdd();
			Integer curr_item = allShirts[0].curr_item;
			
			body.setImageResource(allShirts[curr_item].getResourceId(c));
			setCost(allShirts[curr_item], curr_item, bodyCost);
		}
      });
      
      body_left.setOnClickListener(new View.OnClickListener() {
  		@Override
  		public void onClick(View arg0) {
  			allShirts[0].currItemSub();
  			Integer curr_item = allShirts[0].curr_item;
  			
  			body.setImageResource(allShirts[curr_item].getResourceId(c));
  			setCost(allShirts[curr_item], curr_item, bodyCost);
  		}
        });
      
      pants_right.setOnClickListener(new View.OnClickListener() {
  		@Override
  		public void onClick(View arg0) {
  			allPants[0].currItemAdd();
  			Integer curr_item = allPants[0].curr_item;
  			
  			pants.setImageResource(allPants[curr_item].getResourceId(c));
  			setCost(allPants[curr_item], curr_item, pantsCost);
  		}
      });
        
        pants_left.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View arg0) {
    			allPants[0].currItemSub();
      			Integer curr_item = allPants[0].curr_item;
      			
      			pants.setImageResource(allPants[curr_item].getResourceId(c));
      			setCost(allPants[curr_item], curr_item, pantsCost);
    		}
        });
        
        shoes_right.setOnClickListener(new View.OnClickListener() {
      		@Override
      		public void onClick(View arg0) {
      			allShoes[0].currItemAdd();
      			Integer curr_item = allShoes[0].curr_item;
      			
      			shoes.setImageResource(allShoes[curr_item].getResourceId(c));
      			setCost(allShoes[curr_item], curr_item, shoesCost);
      		}
          });
            
        shoes_left.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View arg0) {
    			allShoes[0].currItemSub();
    			Integer curr_item = allShoes[0].curr_item;
    			
    			shoes.setImageResource(allShoes[curr_item].getResourceId(c));
    			setCost(allShoes[curr_item], curr_item, shoesCost);
    		}
        });
        
        
    }
    
    public String buyItemsAndWearItems(DatabaseHandler db, StoreItem hc1, 
    		StoreItem bc1, StoreItem pc1, StoreItem sc1){
    	//Get cost of the items that are currently set to wearing
    	String hc = headCost.getText().toString();
    	String bc = bodyCost.getText().toString();
    	String pc = pantsCost.getText().toString();
    	String sc = shoesCost.getText().toString();
    	Integer[] cost = new Integer[4];
    	String ret = "";
    	
    	cost[0] = getCost(hc);
    	cost[1] = getCost(bc);
    	cost[2] = getCost(pc);
    	cost[3] = getCost(sc);
    	
    	Integer totalCost = cost[0] + cost[1] + cost[2] + cost[3];
    	
    	if(totalCost < lifeTimePoints){
    		Integer leftOver = lifeTimePoints - totalCost;
    		db.buyAndWearStoreItem(hc1.store_items_id);
    		db.buyAndWearStoreItem(bc1.store_items_id);
    		db.buyAndWearStoreItem(pc1.store_items_id);
    		db.buyAndWearStoreItem(sc1.store_items_id);
    		db.updateLifetimePoints((totalCost*-1));
    		ret = "You have successfully updated your clothes. You have " + 
    				leftOver + " LifeTime Points left.";
    	}else{
    		ret = "You don't have enough LifeTime Points for this purchase!";
    	}
    	
    	return ret;
    }
    
    public Integer getCost(String v){
    	Integer cost = 0;
    	if(v.contains("Own")){
    		cost = 0;
    	}else{
    		cost = Integer.parseInt(v);
    	}
    	
    	return cost;
    }
    public void setCost(StoreItem si, Integer ci, TextView text){
    	if(si.isOwned() ||
				ci == 0){
			text.setText("Owned");
		}else{
			text.setText(si.item_cost.toString());
		}
    }
    
    public void checkWearing(StoreItem si[], ImageView iv, Context c){
    	Integer ci = si[0].curr_item;
    	Boolean stop = false;
    	Integer count = 1;
    	do{
    		si[0].currItemAdd();
    		ci = si[0].curr_item;
    		
    		if(si[ci].isWearing()){
    			iv.setImageResource(si[ci].getResourceId(c));
    			stop = true;
    		}
    		
    		count++;
    	}while(ci > 0 && stop == false);
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

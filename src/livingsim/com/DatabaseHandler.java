package livingsim.com;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper  {
	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 19;
 
    // Database Name
    private static final String DATABASE_NAME = "LivingSim";
     
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_TABLE = "CREATE TABLE user_data (hygiene REAL, " +
				"social REAL, work REAL, hunger REAL, " +
				"energy REAL, fitness REAL, " +
				"fun REAL, last_updated TEXT, lifetime_points INTEGER, " + 
				"death_count INTEGER, is_sleeping INTEGER)";
        db.execSQL(CREATE_TABLE);

		CREATE_TABLE = "CREATE TABLE activity_data ("+
				"activity_name TEXT PRIMARY KEY, " + 
				"hygiene REAL, social REAL, work REAL, hunger REAL" +
				", energy REAL, fitness REAL," +
				" fun REAL)";
        db.execSQL(CREATE_TABLE);
        
        CREATE_TABLE = "CREATE TABLE avatars (avatar_id INTEGER, " +
        		"item_id INTEGER, item_cat INTEGER, store_items_id INTEGER)";
        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE store_items (store_items_id INTEGER " +
        		"PRIMARY KEY AUTOINCREMENT, item_name TEXT, item_desc TEXT, " +
        		"item_cat INTEGER, item_cost INTEGER, own_item INTEGER," +
        		"wearing_item INTEGER, item_image TEXT)";
        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE item_cat (item_cat INTEGER PRIMARY KEY " +
        		"AUTOINCREMENT, item_cat_desc TEXT)"; 
        db.execSQL(CREATE_TABLE);
        
        //This table holds the values each life level will decrease in a 24 hr
        //period
        CREATE_TABLE = "CREATE TABLE decrease_vals (hygiene REAL, " + 
        		"social REAL, work REAL, hunger REAL, energy REAL, fitness REAL, fun REAL)";
        db.execSQL(CREATE_TABLE);
        
        CREATE_TABLE = "CREATE TABLE high_scores (name TEXT, lifetime_score INT, " +
        		"date_entered TEXT);";
        db.execSQL(CREATE_TABLE);
        
        addDefaultValues(db); 
        
        if(!db.isOpen()){
        	db = db.openOrCreateDatabase(DATABASE_NAME, null);
        }
	}
	
	//Adds the default values to all of the tables in the database
	private void addDefaultValues(SQLiteDatabase db) {
		//db.execSQL("INSERT INTO user_data VALUES(9, 9, 9, 9, 9, 9, 9, now(), 0, 0)");
		db.execSQL("INSERT INTO activity_data VALUES('Showering', '5', '0', '0', '0', '0', '0', '0')");
		db.execSQL("INSERT INTO activity_data VALUES('Brushing Your Teeth', '2', '0', '0', '0', '0', '0', '0')");
		db.execSQL("INSERT INTO activity_data VALUES('Flossing/Rinsing', '1', '0', '0', '0', '0', '0', '0')");
		db.execSQL("INSERT INTO activity_data VALUES('Washing Your Hands', '1', '0', '0', '0', '0', '0', '0')");
		db.execSQL("INSERT INTO activity_data VALUES('Eating with Friends', '0', '3', '0', '9', '0', '0', '2')");
		db.execSQL("INSERT INTO activity_data VALUES('Playing Multiplayer Video Games/With Friends', '0', '2', '0', '0', '0', '0', '4')");
		db.execSQL("INSERT INTO activity_data VALUES('Playing Sports/Working out With Friends', '0', '3', '0', '0', '0', '9', '4')");
		db.execSQL("INSERT INTO activity_data VALUES('Hanging/Partying with Friends/Family', '0', '3', '0', '0', '0', '0', '4')");
		db.execSQL("INSERT INTO activity_data VALUES('Working', '0', '0', '4', '0', '0', '0', '0')");
		db.execSQL("INSERT INTO activity_data VALUES('Eating Breakfast', '0', '0', '0', '9', '0', '0', '0')");
		db.execSQL("INSERT INTO activity_data VALUES('Eating Lunch', '0', '0', '0', '9', '0', '0', '0')");
		db.execSQL("INSERT INTO activity_data VALUES('Eating Dinner', '0', '0', '0', '9', '0', '0', '0')");
		db.execSQL("INSERT INTO activity_data VALUES('Eating a Snack', '0', '0', '0', '2', '0', '0', '0')");
		db.execSQL("INSERT INTO activity_data VALUES('Sleeping', '0', '0', '0', '0', '9', '0', '0')");
		db.execSQL("INSERT INTO activity_data VALUES('Power Napping', '0', '0', '0', '0', '1', '0', '0')");
		db.execSQL("INSERT INTO activity_data VALUES('Playing Sports/Working Out Alone', '0', '0', '0', '0', '0', '9', '3')");
		db.execSQL("INSERT INTO activity_data VALUES('Playing Video Games (Solo)', '0', '0', '0', '0', '0', '0', '3')");
		db.execSQL("INSERT INTO activity_data VALUES('Watching TV/Movies', '0', '0', '0', '0', '0', '0', '3')");
		db.execSQL("INSERT INTO activity_data VALUES('Going to Amusement Park', '0', '0', '0', '0', '0', '0', '9')");
		db.execSQL("INSERT INTO item_cat (item_cat_desc) VALUES ('head')");
		db.execSQL("INSERT INTO item_cat (item_cat_desc) VALUES ('shirts')");
		db.execSQL("INSERT INTO item_cat (item_cat_desc) VALUES ('pants')");
		db.execSQL("INSERT INTO item_cat (item_cat_desc) VALUES ('shoes')");
		db.execSQL("INSERT INTO decrease_vals VALUES ('-5.333333333', '-6.666666667', '-2.666666667', "+
				"'-24', '-12', '-5.333333333', '-5.333333333')");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Blank Hair', '', '1', '30', '1', '1', 'head_blank');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Desheveled Hair', 'Brown hair that is elegantly desheveled.', '1', '30', '0', '0', 'head_desheveled');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Long Blonde Hair', 'Hair that is long and blonde.', '1', '30', '0', '0', 'head_longblonde');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Short Blonde Hair', 'Hair that is short and blonde', '1', '30', '0', '0', 'head_shortblonde');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Long Brown Hair', 'Hair that is long and brown', '1', '30', '0', '0', 'head_longbrown');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Red Cap', 'Red Hat on your head', '1', '30', '0', '0', 'head_red_cap');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Short Brown Hair', 'Hair that is short and brown', '1', '30', '0', '0', 'head_short');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Crazy Mullet Hair', 'Hair that is crazy and mullet-like', '1', '30', '0', '0', 'head_crazy');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Blank Shirt', 'Blank Shirt', '2', '30', '1', '1', 'shirt_blank');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Black Shirt', 'Black shirt', '2', '30', '0', '0', 'shirt_black');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Green Shirt', 'Green Shirt', '2', '30', '0', '0', 'shirt_green');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Navy Shirt', 'Navy Shirt', '2', '30', '0', '0', 'shirt_navy');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Orange Shirt', 'Orange Shirt', '2', '30', '0', '0', 'shirt_orange');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Pink Shirt', 'Pink Shirt', '2', '30', '0', '0', 'shirt_pink');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Purple Shirt', 'Purple Shirt', '2', '30', '0', '0', 'shirt_purple');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Red Shirt', 'Red Shirt', '2', '30', '0', '0', 'shirt_red');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Yellow Shirt', 'Yellow Shirt', '2', '30', '0', '0', 'shirt_yellow');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Blank Pants', 'Blank Pants', '3', '30', '1', '1', 'pants_blank');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Black Pants', 'Black Pants', '3', '30', '0', '0', 'pants_black');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Green Pants', 'Green Pants', '3', '30', '0', '0', 'pants_green');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Navy Pants', 'Navy Pants', '3', '30', '0', '0', 'pants_navy');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Orange Pants', 'Orange Pants', '3', '30', '0', '0', 'pants_orange');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Pink Pants', 'Pink Pants', '3', '30', '0', '0', 'pants_pink');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Purple Pants', 'Purple Pants', '3', '30', '0', '0', 'pants_purple');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Red Pants', 'Red Pants', '3', '30', '0', '0', 'pants_red');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Yellow Pants', 'Yellow Pants', '3', '30', '0', '0', 'pants_yellow');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Blank Shoes', 'Blank Shoes', '4', '30', '1', '1', 'shoes_blank');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Black Shoes', 'Black Shoes', '4', '30', '0', '0', 'shoes_black');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Green Shoes', 'Green Shoes', '4', '30', '0', '0', 'shoes_green');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Navy Shoes', 'Navy Shoes', '4', '30', '0', '0', 'shoes_navy');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Orange Shoes', 'Orange Shoes', '4', '30', '0', '0', 'shoes_orange');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Pink Shoes', 'Pink Shoes', '4', '30', '0', '0', 'shoes_pink');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Purple Shoes', 'Purple Shoes', '4', '30', '0', '0', 'shoes_purple');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Red Shoes', 'Red Shoes', '4', '30', '0', '0', 'shoes_red');");
		db.execSQL("INSERT INTO store_items (item_name, item_desc, item_cat, item_cost, own_item, wearing_item, item_image) VALUES('Yellow Shoes', 'Yellow Shoes', '4', '30', '0', '0', 'shoes_yellow');");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		db.execSQL("DROP TABLE IF EXISTS user_data");
		db.execSQL("DROP TABLE IF EXISTS activity_data");
		db.execSQL("DROP TABLE IF EXISTS avatars");
		db.execSQL("DROP TABLE IF EXISTS store_items");
		db.execSQL("DROP TABLE IF EXISTS item_cat");
		db.execSQL("DROP TABLE IF EXISTS decrease_vals");
		db.execSQL("DROP TABLE IF EXISTS high_scores");
        // Create tables again
        onCreate(db);
	}
	
	//Gets the last time the Table was updated 
	String getLastUpdate() {
        SQLiteDatabase db = this.getReadableDatabase();
        
        if(!db.isOpen()){
        	db = db.openOrCreateDatabase(DATABASE_NAME, null);
        }
        String select = "SELECT * FROM user_data";
        Cursor cursor = db.rawQuery(select, null);
        
        String updated = new String();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        Date date = new Date();

        
        if(cursor.getCount() == 0){
        	ContentValues values = new ContentValues();
        	//Hygiene, Social, Work, Hunger, Energy, Fitness, and Fun
            values.put("hygiene", 9); // Initialize the Variables
            values.put("social", 9); 
            values.put("work", 9); 
            values.put("hunger", 9); 
            values.put("energy", 9); 
            values.put("fitness", 9); 
            values.put("fun", 9); 
            values.put("last_updated", dateFormat.format(date)); 
            values.put("lifetime_points", 0);
            values.put("death_count", 0);
            values.put("is_sleeping", 0);
            
            // Inserting Row
            db.insert("user_data", null, values);
 
            select = "SELECT * FROM user_data";
            cursor = db.rawQuery(select, null);
        }
        
        if (cursor != null){
        	cursor.moveToFirst();
        	updated = cursor.getString(7);
        }
        
        cursor.close();
        
        //updated = Integer.toString(cursor.getCount());
        // return last updated text
        return updated;
    }
	
	//Gets the user's current Life Levels
	double[] getValues(){
		
		double[] vals = new double[7];
        SQLiteDatabase db = this.getReadableDatabase();
        
        if(!db.isOpen()){
        	db = db.openOrCreateDatabase(DATABASE_NAME, null);
        }
        String select = "SELECT * FROM user_data";
        Cursor cursor = db.rawQuery(select, null);
		
        if (cursor != null){
        	cursor.moveToFirst();
        	vals[0] = cursor.getDouble(0);
        	vals[1] = cursor.getDouble(1);
        	vals[2] = cursor.getDouble(2);
        	vals[3] = cursor.getDouble(3);
        	vals[4] = cursor.getDouble(4);
        	vals[5] = cursor.getDouble(5);
        	vals[6] = cursor.getDouble(6);
        }
        
		return vals;
	}
	
	//Updates the Life Levels from the Timer
	double[] updateValues(double vals[]){
		SQLiteDatabase db = this.getReadableDatabase();
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        Date date = new Date();

        if(!db.isOpen()){
        	db = db.openOrCreateDatabase(DATABASE_NAME, null);
        }

        String update = "UPDATE user_data " +
        		"SET hygiene = " + vals[0] + ", social = " + vals[1] + 
        		", work = " + vals[2] + ", hunger = " + vals[3] + 
        		", energy = " + vals[4]+ ", fitness = " + vals[5] + 
        		", fun = " + vals[6] + ", last_updated = '" + dateFormat.format(date) + "'";
        db.execSQL(update);
        
		return vals;
	}
	
	//Gets the Life Levels from the activity that is passed to the function
	double[] getValuesToAdd(String activity){
		double[] vals = new double[7];
        SQLiteDatabase db = this.getReadableDatabase();
        
        if(!db.isOpen()){
        	db = db.openOrCreateDatabase(DATABASE_NAME, null);
        }
        
        String select = "SELECT * FROM activity_data " +
        				"WHERE activity_name = '"+ activity + "';";
        Cursor cursor = db.rawQuery(select, null);
		
        if (cursor != null){
        	cursor.moveToFirst(); 
            for(int i =1; i<= 7; i++){
    	        	vals[i-1] = cursor.getDouble(i);
            }
        }
        
		return vals;
	}
	
	//Updates the Life Level values compared to the activity and the current levels
	double[] addValues(double[] currVals, String activity){
		SQLiteDatabase db = this.getReadableDatabase();
        double[] addVals = new double[9];
        
        addVals = getValuesToAdd(activity);
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        Date date = new Date();
        
        if(!db.isOpen()){
        	db = db.openOrCreateDatabase(DATABASE_NAME, null);
        }
        
        double[] addDoub = new double[10];
        
        for(int i =0; i<=6; i++){
        	currVals[i] = currVals[i] + addVals[i];
        	
        	if(currVals[i] > 9){
        		currVals[i] = 9;
        	}else if(currVals[i] < 1){
        		currVals[i] = 1;
        	}
        }
        
        String update = "UPDATE user_data " +
        		"SET hygiene = " + currVals[0] + ", social = " + currVals[1] + 
        		", work = " + currVals[2] + ", hunger = " + currVals[3] + 
        		", energy = " + currVals[4]+ ", fitness = " + currVals[5] + 
        		", fun = " + currVals[6];
        db.execSQL(update);
        
		return currVals;
	}
	
	//This is used for when the user is sleeping. You don't want the Life Levels
	//To be updated when the user is sleeping
	public void updateLastUpdate(){
		SQLiteDatabase db = this.getReadableDatabase();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        Date date = new Date();
        
		String update = "UPDATE user_data SET last_updated = '" 
						+ dateFormat.format(date) + "'";
		db.execSQL(update);
	}
	
	//Checks to see if user is sleeping or not
	public Integer checkSleeping(){
		Integer isSleeping = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        
        if(!db.isOpen()){
        	db = db.openOrCreateDatabase(DATABASE_NAME, null);
        }
        
        String select = "SELECT is_sleeping FROM user_data";
        Cursor cursor = db.rawQuery(select, null);
		
        try{
	        if (cursor != null){
	        	cursor.moveToFirst(); 
	        	isSleeping = cursor.getInt(0);
	        }
        }catch(Exception e){
        	isSleeping = 0;
        }
        
		return isSleeping;
	}
	
	public void toggleSleeping(){
		SQLiteDatabase db = this.getReadableDatabase();
		String toggle = "";
		if(this.checkSleeping() == 0){
			toggle = "UPDATE user_data SET is_sleeping = '1'";
		}else{
			toggle = "UPDATE user_data SET is_sleeping = '0'";
		}
		
		this.updateLastUpdate();
		db.execSQL(toggle);
	}
	//This function gets all of the rows in Activity data table
	public List<String> getAllRows(){
        List<String> rows = new ArrayList<String>();
 
        // Select All Query
        String selectQuery = "SELECT activity_name FROM activity_data " +
        		"ORDER BY activity_name ASC";
 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor activityCursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (activityCursor.moveToFirst()) {
            do {
            	rows.add(activityCursor.getString(0));
            } while (activityCursor.moveToNext());
        }
 
        // close connection
        activityCursor.close();
 
        // return all of the rows
        return rows;
    }  
	
	//This function creates the User Created activity in Activity Data
	public String createActivity(String name, int hygval, int socval, int worval, int hunval, 
		int eneval, int fitval, int funval){
		SQLiteDatabase db = this.getReadableDatabase();
		try{
			String sql = String.format("INSERT INTO activity_data VALUES ('%s', '%d', '%d', " +
					"'%d', '%d', '%d', '%d'," +
					"'%d');", name, hygval, socval, worval, hunval, eneval, fitval, funval);
			db.execSQL(sql);
		}catch(Exception e){
			return e.toString();	
		}
		
		return "true";
	}
	
	public String updateLifetimePoints(Integer addPoints){
		String newString = "";
		SQLiteDatabase db = this.getReadableDatabase();
		 if(!db.isOpen()){
        	db = db.openOrCreateDatabase(DATABASE_NAME, null);
		 }
		 
		//Checks to see if the user died
		if(addPoints == 187){
			//Return user to a Funeral Sequence Screen
			newString = "You have Died!";
			//Need to reset Life Levels and Add to Death Count
			Integer new_death = 0;
			
			String select = "SELECT * FROM user_data";
	        Cursor cursor = db.rawQuery(select, null);
			
	        if (cursor != null){
	        	cursor.moveToFirst();
	        	new_death = cursor.getInt(9);
	        }
	        
	        new_death += 1;
	        
			String update = "UPDATE user_data " +
		        		"SET hygiene = 9, social = 9, work = 9, " + 
		        		"hunger = 9, energy = 9, fitness = 9, fun = 9, " + 
		        		"lifetime_points = 0, death_count = " + new_death.toString(); 
			db.execSQL(update);
		}else{
			//Get current LifeTime Points
			Integer lifeTimePoints = 0;
		      
	        String select = "SELECT * FROM user_data";
	        Cursor cursor = db.rawQuery(select, null);
			
	        if (cursor != null){
	        	cursor.moveToFirst();
	        	lifeTimePoints = cursor.getInt(8);
	        }
	        
	        Integer newPoints = lifeTimePoints + addPoints;
	        
	        db.execSQL("UPDATE user_data " +
	        		"SET lifetime_points = " + newPoints.toString());
	        
	        newString = addPoints.toString() + " LifeTime Points has been added to your " +
					"account. You currently have " + newPoints.toString() + " points.";
		}
		
		return newString;
	}

	public String updateActivity(String origName, String nameStr, int hygval, int socval,
			int worval, int hunval, int eneval, int fitval, int funval) {
		SQLiteDatabase db = this.getReadableDatabase();
		if(!db.isOpen()){
			db = db.openOrCreateDatabase(DATABASE_NAME, null);
		}
		String sql = String.format("UPDATE activity_data  " +
				"SET activity_name = '%s', hygiene = '%d', social = '%d', " +
				"work = '%d', hunger = '%d', energy = '%d', fitness = '%d', " +
				"fun = '%d' WHERE activity_name = '%s';", 
				nameStr, hygval, socval, worval, hunval, eneval, fitval, funval, origName);
		db.execSQL(sql);
		return "You have updated " + nameStr;
	}
	
	//Returns the top ten High Scores into an array
	public String[][] getHighScores(){
			String[][] highScore = new String[10][3];
	        SQLiteDatabase db = this.getReadableDatabase();
	        Integer i = 0;
	        
	        if(!db.isOpen()){
	        	db = db.openOrCreateDatabase(DATABASE_NAME, null);
	        }
	        
	        //All values in this table are set to a 24 hour period
	        String select = "SELECT * FROM high_scores " +
	        		"ORDER BY lifetime_score LIMIT 10";
	        Cursor cursor = db.rawQuery(select, null);
	        
	        try{
		        // looping through all rows and adding to list
		        if (cursor.moveToFirst()) {
		            do {
		            	highScore[i][0] = cursor.getString(0);
		            	highScore[i][1] = cursor.getString(1);
		            	highScore[i][2] = cursor.getString(2);;
		            	i += 1;
		            } while (cursor.moveToNext());
		        }
	        }catch (Exception e){
	        	return null;
	        }
	        
			return highScore;
	}
	
	//Returns the current Lifetime points of the users
	public Integer getLifetimePoints(){
		Integer lifetimePoints = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        
        if(!db.isOpen()){
        	db = db.openOrCreateDatabase(DATABASE_NAME, null);
        }
        
        String select = "SELECT lifetime_points FROM user_data";
        Cursor cursor = db.rawQuery(select, null);
		
        if (cursor != null){
        	cursor.moveToFirst(); 
        	lifetimePoints = cursor.getInt(0);
        }
        
		return lifetimePoints;
	}
	
	//Inserts the high_score record for the user. Deletes old user data
	public String insertHighScore(String name){
		SQLiteDatabase db = this.getReadableDatabase();
		Integer lifetimePoints = this.getLifetimePoints();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
        Date date = new Date();
        
        //Creates a record of the high score
		String sql = String.format("INSERT INTO high_scores VALUES ('%s', '%s', '%s');",
				name, lifetimePoints, dateFormat.format(date));
		db.execSQL(sql);
		
		return "Thank you for playing! Try again!";
	}
	
	//Gets the values that need to be subtracted from the Life Levels
	double[] getSubtractVals(){
		double[] vals = new double[7];
        SQLiteDatabase db = this.getReadableDatabase();
        
        if(!db.isOpen()){
        	db = db.openOrCreateDatabase(DATABASE_NAME, null);
        }
        
        //All values in this table are set to a 24 hour period
        String select = "SELECT * FROM decrease_vals";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor != null){
        	cursor.moveToFirst();
        	//To get value for 10 minute periods, must do math.
        	//6 * 24.. 60 / 10 = 6 and 24 for hours. 
            for(int i =0; i<= 6; i++){
    	        	vals[i] = cursor.getDouble(i)/(6 * 24);
            }
        }
        
		return vals;
	}
	
	public String updateDecreaseVals(Double hygiene, Double social, Double work, Double hunger, 
			Double energy, Double fitness, Double fun){
		SQLiteDatabase db = this.getReadableDatabase();
		try{
			String sql = "UPDATE decrease_vals  " +
					"SET hygiene = " + hygiene + ", social = " + social + ", " +
					"work = " + work + ", hunger = " + hunger + ", energy = " + energy +
					", fitness = " + fitness + ", " + "fun = " + fun + ";";
			db.execSQL(sql);
			return "true";
		}catch (Exception e){
			return e.toString();
		}
		
	}
	public Boolean buyAndWearStoreItem(Integer itemId){
		SQLiteDatabase db = this.getReadableDatabase();
		
		//Set the item to owned
		String sql = "UPDATE store_items " +
				"SET own_item = 1 " +
				"WHERE store_items_id = " + itemId + ";";
		db.execSQL(sql);
	
		//Clear Out All of the Wearing flags for the same categories
		sql = "UPDATE store_items SET wearing_item = 0 " +
				     " WHERE item_cat = (SELECT item_cat FROM" +
				     " store_items WHERE store_items_id = " + itemId +");";
		db.execSQL(sql);
		
		//Update the wearing flag for that one item
		sql = "UPDATE store_items " +
						"SET wearing_item = 1 " +
						"WHERE store_items_id = " + itemId + ";";
		db.execSQL(sql);
		
		return true;
	}
	
	public StoreItem[] getStoreItems(Integer item_cat){
		StoreItem[] store_items = new StoreItem[10];
		
		for(int k = 0; k<= 9; k++){
			store_items[k] = new StoreItem();
		}
		
        SQLiteDatabase db = this.getReadableDatabase();
        Integer i = 0;
        
        if(!db.isOpen()){
        	db = db.openOrCreateDatabase(DATABASE_NAME, null);
        }
        
        //All values in this table are set to a 24 hour period
        String select = "SELECT * FROM store_items WHERE item_cat = '" 
        		+ item_cat + "';";
        Cursor cursor = db.rawQuery(select, null);
        
        try{
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	            	store_items[i].store_items_id = cursor.getInt(0);
	            	store_items[i].item_name = cursor.getString(1);
	            	store_items[i].item_desc = cursor.getString(2);
	            	store_items[i].item_cat = cursor.getInt(3);
	            	store_items[i].item_cost = cursor.getInt(4);
	            	store_items[i].own_item = cursor.getInt(5);
	            	store_items[i].wearing_item = cursor.getInt(6);
	            	store_items[i].item_image = cursor.getString(7);
	            	i += 1;
	            } while (cursor.moveToNext());
	            store_items[0].max = i-1;
	        }
        }catch (Exception e){
        	store_items[0].item_name = e.toString();
        	return store_items;
        }
        
		return store_items;
	}
}

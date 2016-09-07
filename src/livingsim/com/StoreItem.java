package livingsim.com;

import android.content.Context;

public class StoreItem {
	public Integer store_items_id;
	public String item_name;
	public String item_desc;
	public Integer item_cat;
	public Integer item_cost;
	public Integer own_item;
	public Integer wearing_item;
	public String item_image;
	public Integer curr_item;
	public Integer max;
	
	public boolean isOwned(){
		if(this.own_item == 1){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isWearing(){
		if(this.wearing_item == 1){
			return true;
		}else{
			return false;
		}
	}
	
	public void currItemAdd(){
		if(this.curr_item == this.max){
			this.curr_item = 0;
		}else{
			this.curr_item += 1;
		}
	}
	
	public void currItemSub(){
		if(this.curr_item == 0){
			this.curr_item = this.max;
		}else{
			this.curr_item -= 1;
		}
	}
	
	public Integer getResourceId(Context c){
		String uri = "drawable/" + this.item_image;
		return c.getResources().getIdentifier(uri, null, c.getPackageName());
	}
}

package livingsim.com;

import android.content.Intent;

import com.e3roid.E3Activity;
import com.e3roid.E3Engine;
import com.e3roid.E3Scene;
import com.e3roid.drawable.Sprite;
import com.e3roid.drawable.modifier.AlphaModifier;
import com.e3roid.drawable.modifier.ParallelModifier;
import com.e3roid.drawable.modifier.ProgressModifier;
import com.e3roid.drawable.modifier.ScaleModifier;
import com.e3roid.drawable.modifier.function.Linear;
import com.e3roid.drawable.texture.AssetTexture;
import com.e3roid.drawable.texture.Texture;
import com.e3roid.event.SceneUpdateListener;

public class SplashPage extends E3Activity implements SceneUpdateListener {

	private final static int WIDTH  = 320;
	private final static int HEIGHT = 480;
	
	private final static int SPLASH_MSEC = 3000;
	
	private Sprite  logo;
	private Texture logoTexture;
	
	@Override
	public E3Engine onLoadEngine() {
		E3Engine engine = new E3Engine(this, WIDTH, HEIGHT, E3Engine.RESOLUTION_FIXED_RATIO_WITH_ROTATION);
		engine.requestFullScreen();
		return engine;
	}

	@Override
	public E3Scene onLoadScene() {
		E3Scene scene = new E3Scene();
		
		// start next activity after waiting 3 seconds.
		scene.registerUpdateListener(SPLASH_MSEC, this);
		
		int centerX = (getWidth()  - logoTexture.getWidth())  / 2;
		int centerY = (getHeight() - logoTexture.getHeight()) / 2;
		
		logo = new Sprite(logoTexture, centerX, centerY);
		
		// show logo in 3 seconds and scale logo in 1 seconds.
		ParallelModifier modifier = new ParallelModifier(
				new ProgressModifier(new AlphaModifier(0, 0, 1), 3000, Linear.getInstance()),
				new ProgressModifier(new ScaleModifier(0, 0.8f, 1.0f), 1000, Linear.getInstance()));
		logo.addModifier(modifier);
		
		scene.getTopLayer().add(logo);
		scene.setBackgroundColor(243, 244, 249);
		
		return scene;
	}

	@Override
	public void onLoadResources() {
		logoTexture = new AssetTexture("logo.png", this);
	}

	@Override
	public void onUpdateScene(E3Scene scene, long elapsedMsec) {
		DatabaseHandler db = new DatabaseHandler(this);
		
		scene.unregisterUpdateListener(this);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(db.checkSleeping() == 0){
			startActivity(new Intent(this, Instructions.class));
			finish();
		}else{
			startActivity(new Intent(this, Sleeping.class));
			finish();
		}
	}

}


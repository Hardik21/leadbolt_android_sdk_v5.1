package com.leadbolt.example;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.apptracker.android.listener.AppModuleListener;
import com.apptracker.android.track.AppTracker;
// Leadbolt SDK imports

public class MainActivity extends Activity {
	
	/*
	------------------------------------------------------------------------------------------------------------------------
    This sample app is intended for developers including Leadbolt SDK in their apps
	 
	Please ensure you the required Android Permissions in your Manifest file - INTERNET and ACCESS_NETWORK_STATE
	Please ensure Google Play Services library is included in your Android Project so Google AID can be retrieved

	You will need a Leadbolt Publisher account to retrieve your App specific API Key
	------------------------------------------------------------------------------------------------------------------------
	*/
	
	// Leadbolt SDK configurations
	private static final String APP_API_KEY 		= "is2byYEVjbXiFjVjaYIt6sM4aEIqMWZ3"; // change this to your App specific API KEY
	private static final String LOCATION_CODE		= "inapp";
	
	// setup some buttons for demo
	private Button cache;
	private Button show;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		cache = (Button) findViewById(R.id.button_cache);
		show = (Button) findViewById(R.id.button_show);
		show.setEnabled(false); // disable the button till Ad is available to be displayed		
		
		
		if(savedInstanceState == null)
		{
			// Set the Leadbolt Event listener to get notified of different stages of the Ad life-cycle
			AppTracker.setModuleListener(leadboltListener);
			// Initialize Leadbolt SDK
			AppTracker.startSession(getApplicationContext(), APP_API_KEY);
			
			// Bind Leadbolt SDK actions to buttons
			cacheDirectDeal(cache);
			showDirectDeal(show);
		}
	}
	
	
	private void cacheDirectDeal(Button b)
	{
		
		b.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				AppTracker.loadModuleToCache(getApplicationContext(), LOCATION_CODE);
			}
		});
	}
	
	private void showDirectDeal(Button b)
	{	
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppTracker.loadModule(getApplicationContext(), LOCATION_CODE);
			}
		});
	}
	
	private AppModuleListener leadboltListener = new AppModuleListener() {
		
		@Override
		public void onModuleCached(final String placement) {
			Toast.makeText(MainActivity.this, "Ad successfully cached - "+placement, Toast.LENGTH_SHORT).show();
			// Ad has been cached, now enable the Show Ad button			
			show.setEnabled(true);
		}
		@Override
		public void onModuleClicked(String placement) {
			Toast.makeText(MainActivity.this, "Ad clicked", Toast.LENGTH_SHORT).show();
		}
		@Override
		public void onModuleClosed(String placement) {
			Toast.makeText(MainActivity.this, "Ad closed", Toast.LENGTH_SHORT).show();
		}
		@Override
		public void onModuleFailed(String placement, String error, boolean isCache) {
			if(isCache) {
				Toast.makeText(MainActivity.this, "Ad failed to cache - "+error, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(MainActivity.this, "Ad failed to load - "+error, Toast.LENGTH_SHORT).show();
			}
		}
		@Override
		public void onModuleLoaded(String placement) {
			Toast.makeText(MainActivity.this, "Ad displayed", Toast.LENGTH_SHORT).show();
			// Ad has been shown, now disable to the Show Ad button
			show.setEnabled(false);
		}
		@Override
		public void onMediaFinished(boolean viewCompleted) {}
	};
}

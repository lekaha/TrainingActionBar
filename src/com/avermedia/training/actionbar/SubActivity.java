package com.avermedia.training.actionbar;

import com.avermedia.training.actionbar.CustomScrollView.OnScrollChangedListener;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ScrollView;
import android.widget.Toast;

public class SubActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub);
		
		CustomScrollView sv = (CustomScrollView) findViewById(R.id.sv);
		sv.setOnScrollChangedListener(new OnScrollChangedListener() {
			
			@Override
			public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
				final ActionBar actionBar = getActionBar();
				float d = SubActivity.this.getResources().getDisplayMetrics().density;
				if(((oldt - t) > d) && (!actionBar.isShowing())) {
					Toast.makeText(SubActivity.this, "Show action bar ( " + oldt + " - " + t + " )", Toast.LENGTH_SHORT).show();
					actionBar.show();
				}
				else if(((oldt - t) < -( d )) && (actionBar.isShowing())) {
					Toast.makeText(SubActivity.this, "Hide action bar ( " + oldt + " - " + t + " )", Toast.LENGTH_SHORT).show();
					actionBar.hide();
				}
			}
		});
		
		//Navigating Up with the App Icon
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.submain, menu);
				
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	    	Toast.makeText(SubActivity.this, "Pressing navigation", Toast.LENGTH_SHORT).show();
	    	
	        Intent upIntent = NavUtils.getParentActivityIntent(this);
	        NavUtils.navigateUpTo(this, upIntent);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
}

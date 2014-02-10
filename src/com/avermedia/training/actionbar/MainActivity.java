package com.avermedia.training.actionbar;

import com.avermedia.training.actionbar.CustomScrollView.OnScrollChangedListener;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	protected Action pAction;
	protected Menu pActionMenu;
	protected Drawable pActionBarBackgroundDrawable;
	protected Drawable pActionBarIconDrawable;
	protected int pFactory;
	protected boolean isFadingMode = false;
	
	protected enum ORIENTATION{
		UP,DOWN
	}
	protected ORIENTATION pOrientation = ORIENTATION.DOWN;
	

	public MainActivity(){
		pAction = new Action();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Create a button and to navigate to sub activity when clicked.
		Button btn = (Button) findViewById(R.id.btn_go);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SubActivity.class);
				startActivity(intent);
			}
		});
		
		Button btn2 = (Button) findViewById(R.id.btn_ab);
		btn2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final ActionBar actionBar = getActionBar();
				if(actionBar.isShowing()) {
					actionBar.hide();
				}
				else {
					actionBar.show();
				}
			}
		});
		
		CheckBox ckb = (CheckBox) findViewById(R.id.ckb_ab);
		ckb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				isFadingMode = isChecked;
				resetActionBar();
			}
		});
		
		CustomScrollView sv = (CustomScrollView) findViewById(R.id.sv);
		sv.setOnScrollChangedListener(new OnScrollChangedListener() {
			
			@Override
			public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
				final ActionBar actionBar = getActionBar();
				
				final int totalHeight = who.getChildAt(0).getHeight();
            	final int height = (int)(totalHeight * 0.2f);
				final int headerHeight = height - actionBar.getHeight();
				
				float d = MainActivity.this.getResources().getDisplayMetrics().density;
				
				if(false == isFadingMode){
					
					/*
					 * Case 1: Using action-bar's hide() and show().
					 */
					
					if(((oldt - t) > d) && (!actionBar.isShowing())) {
						Toast.makeText(MainActivity.this, "Show action bar ( " + oldt + " - " + t + " )", Toast.LENGTH_SHORT).show();
						actionBar.show();
					}
					else if(((oldt - t) < -( d )) && (actionBar.isShowing())) {
						Toast.makeText(MainActivity.this, "Hide action bar ( " + oldt + " - " + t + " )", Toast.LENGTH_SHORT).show();
						actionBar.hide();
					}
				}
				else {
					
					/*
					 * Case 2: Fading out and in on action bar.
					 */
		            if((oldt - t) > d) {
		            	if(pOrientation != ORIENTATION.DOWN){
		            		pOrientation = ORIENTATION.DOWN;
		            		pFactory = t;
		            	}
		            	
		            	
	    	            final float ratio = 
	    	            		(float) Math.min(Math.max(pFactory - t, 0), headerHeight) / headerHeight ;
	    	            final int newAlpha = (int) ((ratio * 255));
	    	            int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
	    	            pActionBarBackgroundDrawable.setAlpha(newAlpha);
	    	            pActionBarIconDrawable.setAlpha(newAlpha);
	    	            
		            	if(newAlpha != 0){
				            TextView title = (TextView) findViewById(titleId);
				            title.setAlpha(newAlpha);
				            
				            actionBar.setHomeButtonEnabled(true);
				            actionBar.setDisplayHomeAsUpEnabled(true);
				            
				            pActionMenu.findItem(R.id.action_search).setEnabled(true);
				            pActionMenu.findItem(R.id.action_search).getIcon().setAlpha(newAlpha);
				            pActionMenu.findItem(R.id.action_settings).setVisible(true);
			            }
					}
		            
					else if((oldt - t) < -( d )) {
						if(pOrientation != ORIENTATION.UP){
							pOrientation = ORIENTATION.UP;
							pFactory = t;
		            	}
						
			            final float ratio = 
			            		(float) Math.min(Math.max(t - pFactory, 0), headerHeight) / headerHeight ;
			            final int newAlpha = (int) (255 - (ratio * 255));
			            int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
			            pActionBarBackgroundDrawable.setAlpha(newAlpha);
			            pActionBarIconDrawable.setAlpha(newAlpha);
			            
						if(newAlpha == 0){
							TextView title = (TextView) findViewById(titleId);
				            title.setAlpha(newAlpha);
				            
			            	actionBar.setHomeButtonEnabled(false);
			            	actionBar.setDisplayHomeAsUpEnabled(false);
			            	
			            	pActionMenu.findItem(R.id.action_search).setEnabled(false);
			            	pActionMenu.findItem(R.id.action_search).getIcon().setAlpha(newAlpha);
			            	pActionMenu.findItem(R.id.action_settings).setVisible(false);
						}
					}
				}
			}
		});
		
		resetActionBar();
        
	}
	
	protected void resetActionBar(){
		final ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    actionBar.setHomeButtonEnabled(true);
	    
	    pActionBarBackgroundDrawable = getResources().getDrawable(R.drawable.ab_solid_dark_holo);
        pActionBarBackgroundDrawable.setAlpha(255);
        actionBar.setBackgroundDrawable(pActionBarBackgroundDrawable);
        
        pActionBarIconDrawable = getResources().getDrawable(R.drawable.ic_launcher);
        pActionBarIconDrawable.setAlpha(255);
        actionBar.setIcon(pActionBarIconDrawable);
        
        if(null != pActionMenu){
	        pActionMenu.findItem(R.id.action_search).setEnabled(true);
	        pActionMenu.findItem(R.id.action_search).getIcon().setAlpha(255);
	        pActionMenu.findItem(R.id.action_settings).setVisible(true);
        }
        
        
        int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
        TextView title = (TextView) findViewById(titleId);
        title.setAlpha(255);
        
        actionBar.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		pActionMenu = menu;
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		//Search view on Action bar.
		SearchView searchView = 
				(SearchView) pActionMenu.findItem(R.id.action_search).getActionView();
				
		searchView.setOnQueryTextListener(pAction);
		
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
            	Toast.makeText(MainActivity.this, 
            			"Clicked: " + getString(R.string.action_settings), 
            			Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }
	
	protected class Action implements OnQueryTextListener{

		/**
		 * Called when the query text is changed by the user.
		 */
		@Override
		public boolean onQueryTextChange(String text) {
			Toast.makeText(MainActivity.this, "Query text: " + text, Toast.LENGTH_SHORT).show();
			
			// false if the SearchView should perform the default action of showing any suggestions 
			// if available, true if the action was handled by the listener.
			return false;
		}

		/*
		 * Called when the user submits the query. 
		 * This could be due to a key press on the keyboard or due to pressing a submit button. 
		 * The listener can override the standard behavior by returning true to 
		 * indicate that it has handled the submit request. 
		 * Otherwise return false to let the SearchView handle the submission by launching 
		 * any associated intent.
		 */
		@Override
		public boolean onQueryTextSubmit(String query) {
			Toast.makeText(MainActivity.this, "Submit query: " + query, Toast.LENGTH_SHORT).show();
			
			// true if the query has been handled by the listener, false to let the SearchView perform the default action.
			return false;
		}
		
	}   

}

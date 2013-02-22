package com.example.yamba83;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.text.Editable;
import android.text.TextWatcher;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

public class StatusActivity extends Activity implements OnClickListener, TextWatcher, OnSharedPreferenceChangeListener  {  // Declare that StatusActivity implements OnClickListener, TextWatcher and OnSharedPreferenceChangeListener
	private static final String TAG = "StatusActivity";
	EditText editText;
	Button updateButton;
	Twitter twitter;
	TextView textCount; // TextCount is the TextView defined in status.xml
	SharedPreferences prefs;
		
	/** Called when the activity is first created */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status);

    	// Find views
        editText = (EditText) findViewById(R.id.editText); // Find views inflated from xml layout and assign to Java variables
        updateButton = (Button) findViewById(R.id.buttonUpdate);
        updateButton.setOnClickListener(this); // Registers button to notify 'this' (i.e. StausActivity) when it's clicked
        
        textCount = (TextView) findViewById(R.id.textCount); // Find views inflated from xml layout and assign to Java variables
        textCount.setText(Integer.toString(140)); // TextView takes value as a number, so it converts number to text
        textCount.setTextColor(Color.GREEN); // Starts with green color
        editText.addTextChangedListener(this); // Attach TextWatcher to editText field. editText calls TextWatcher instance which refers to this object itself
        
        // Setup preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }
        
    private Twitter getTwitter() {
    	if (twitter == null) { // ONly if Twitter = null it will be created
    		String username, password, apiRoot;
        	username = prefs.getString("username",""); // Get the usrname and pwd fromt he shared pref. An empty default value is used if no usrname or pwd is found. 
        	password = prefs.getString("password","");
        	apiRoot = prefs.getString("apiRoot", "http://yamba.markana.com/api");
        		
        		// Connect to Twitter
        		twitter = new Twitter(username, password); // Log in to Twitter service with user defined usrname and pwd
        		twitter.setAPIRootUrl(apiRoot); // update the actual service that need to be used
        }
        return twitter;    
    }
    
    
    /** Asynchronously post to Twitter */
    class PostToTwitter extends AsyncTask<String, Integer, String> { // 1st data type for doInBackGround, 2nd for onProgressUpdate, 3rd for onPostExecute
        
    	// Called to initiate background activity
    	@Override
    	protected String doInBackground(String... statuses) {
    		try {
    			Yamba83Application yamba = ((Yamba83Application) getApplication()); // Call Application object via getApplication. Cast generic Application into Yamba83Application 
    			Twitter.Status status = yamba.getTwitter().updateStatus(statuses[0]); // Uses the getTwitter() method from Yamba83Application
    			return status.text;
    		}	catch (TwitterException e) {
    			Log.e(TAG, e.toString());
    			e.printStackTrace();
    			return "Failed to post your status update";
    		}
    	}
    	
    	// Called when there's a status to be updated
    	@Override
    	protected void onProgressUpdate(Integer... values) {
    		super.onProgressUpdate(values); // use for example if you have a download progress to be shown
    	}
    	   	
    	// Called once the background activity has been completed
    	@Override
    	protected void onPostExecute(String result) {
    		Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show();
    	}
    }
    
    /** Implementation of TextWatcher methods */
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { // This method is empty but we need to implement it to complete the TextWatcher in StatusActivity
    }
    
    public void onTextChanged(CharSequence s, int before, int start, int count) { // This method is empty but we need to implement it to complete the TextWatcher in StatusActivity
    }
    
    public void afterTextChanged(Editable statusText) { // This method is called wheneever a text is changed and returns that with current text
    	int count = 140 - statusText.length(); 
    	textCount.setText(Integer.toString(count));
    	textCount.setTextColor(Color.GREEN);
    		if (count < 10)
    			textCount.setTextColor(Color.BLUE);
    		if (count < 0)
    			textCount.setTextColor(Color.RED);
    }    
    
    /** Called when button is clicked */
    public void onClick(View v) {
    	String status = editText.getText().toString();
    	new PostToTwitter().execute(status);
    	Log.d(TAG,"onClicked");
    }
    
    /** Called first time when menu button is clicked */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater(); // Get menu inflater from context
    	inflater.inflate(R.menu.menu, menu); // use inflater to inflate menu from resource xml
    	return true; // 
    }
    
    /** Called when options item is clicked */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) { // Now it is only one item but for scaling purposes it will get item id to switch
    	case R.id.itemServiceStart:
    		startService(new Intent(this, UpdaterService.class)); // Launch a new activity, the UpdaterService class
    	break;
    	case R.id.itemServiceStop:
    		stopService(new Intent(this, UpdaterService.class));
    	break;
    	case R.id.itemPrefs:
    		startActivity(new Intent(this, PrefsActivity.class)); // Launch a new activity, the PrefsActivity class
    	break;
    	}
    	
    	return true; //will consume the event
    }
    
    
    // Is invoked whenever system preferences change
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) { // invalidate Twitter
    	twitter = null;
    }
}

package com.example.yamba83;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StatusActivity extends Activity implements OnClickListener  { // Make StatusActivity capable of being a button listener, needs to implement onClickListener interface
	private static final String TAG = "StatusActivity";
	EditText editText;
	Button updateButton;
	Twitter twitter;
		
	/** Called when the activity is first created */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status);

    	// Find views
        editText = (EditText) findViewById(R.id.editText); // Find views inflated from xml layout and assign to Java variables
        updateButton = (Button) findViewById(R.id.buttonUpdate);
        
        updateButton.setOnClickListener(this); // Registers button to notify 'this' (i.e. StausActivity) when it's clicked
        
        twitter = new Twitter("student", "password"); // Connect to online service that supports Twitter API, usrname and pwd hardcoded
        twitter.setAPIRootUrl("http://yamba.marakana.com/api"); 
        }

    
    /** Asynchronously post to Twitter */
    class PostToTwitter extends AsyncTask<String, Integer, String> { // 1st data type for doInBackGround, 2nd for onProgressUpdate, 3rd for onPostExecute
        
    	// Called to initiate background activity
    	@Override
    	protected String doInBackground(String... statuses) {
    		try {
    			Twitter.Status status = twitter.updateStatus(statuses[0]);
    			return status.text;
    		}	catch (TwitterException e) {
    			Log.e(TAG, e.toString());
    			e.printStackTrace();
    			return "Failed to post";
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
    
    
    /** Called when button is clicked */
    public void onClick(View v) {
    	String status = editText.getText().toString();
    	new PostToTwitter().execute(status);
    	Log.d(TAG,"onClicked");
    }
}

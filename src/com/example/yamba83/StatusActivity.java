package com.example.yamba83;

import winterwell.jtwitter.Twitter;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

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
    
    /** Called when button is clicked */
    public void onClick(View v) {
    	twitter.setStatus(editText.getText().toString()); // Make webservice API call to update the status
    	Log.d(TAG,"onClicked");
    }
}

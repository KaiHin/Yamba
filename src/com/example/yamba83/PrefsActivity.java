package com.example.yamba83;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PrefsActivity extends PreferenceActivity { // this will extend the PreferenceActivity class
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) { // Need to override to initialize the activity
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs); // Instead of calling setContentView() 
	}

}

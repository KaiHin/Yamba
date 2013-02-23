package com.example.yamba83;

import winterwell.jtwitter.Twitter;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

public class Yamba83Application extends Application implements OnSharedPreferenceChangeListener { // Moved responsibility from StatusActivity class to this class
	private static final String TAG = Yamba83Application.class.getSimpleName();
	public Twitter twitter; // Twitter and SharedPreferences are now part of this common object and not longer part of StatusActivity
	private SharedPreferences prefs;
	
	@Override
	public void onCreate() {
		super.onCreate();
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		this.prefs.registerOnSharedPreferenceChangeListener(this);
		Log.i(TAG, "onCreated");
	}
	
	@Override
	public void onTerminate() { // Placeholder for when application needs to be terminated. Nów it only logs
		super.onTerminate();
		Log.i(TAG, "onTerminate");
	}
	
	public synchronized Twitter getTwitter() { //getTwitter is also a part of this class now instead of StatusActivity
		if (this.twitter == null) {
			String username = this.prefs.getString("username","");
			String password = this.prefs.getString("password","");
			String apiRoot = this.prefs.getString("apiRoot","http://yamba.marakana.com/api");
			if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(apiRoot)) {
				this.twitter = new Twitter(username, password);
				this.twitter.setAPIRootUrl(apiRoot);
			}
		}
		return this.twitter;
	}
	
	public synchronized void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) { //onSharedPreferenceChanged is also a part of this class now instead of StatusActivity
		this.twitter = null;
	}
	
	private boolean serviceRunning; // Flag indicates whether service is running or not
	
	public boolean isServiceRunning() { // Check status of serviceRunning flag
		return serviceRunning; 
	}
	
	public void setServiceRunning(boolean serviceRunning) { // set state of flag
		this.serviceRunning = serviceRunning;
	}
}
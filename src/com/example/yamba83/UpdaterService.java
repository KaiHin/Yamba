package com.example.yamba83;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdaterService extends Service {
	static final String TAG = "UpdaterService"; 
	
	@Override
	public IBinder onBind(Intent intent) {
		return null; // Not using the binder right now so return null
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG,"onCreated");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Log.d(TAG,"onStarted");
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG,"onDestroyed");
	}

}
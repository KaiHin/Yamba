package com.example.yamba83;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import java.util.List;

public class UpdaterService extends Service {
	static final String TAG = "UpdaterService"; 
	
	static final int DELAY = 60000; // 1 minute
	private boolean runFlag = false; // Sets flag that service is not running
	private Updater updater;
	private Yamba83Application yamba; // Variable allows access to Yamba83Application class
	
	@Override
	public IBinder onBind(Intent intent) {
		return null; // Not using the binder right now so return null
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		this.yamba = (Yamba83Application) getApplication(); // Get reference to the application by getApplication call
		this.updater = new Updater();
		Log.d(TAG,"onCreated");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		this.runFlag = true; // Sets flag that service is running
		this.updater.start();
		this.yamba.setServiceRunning(true); // Update serviceRunning flag in Yampa83Application
		Log.d(TAG,"onStarted");
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		this.runFlag = false;
		this.updater.interrupt(); // Will stop the actual thread from running
		this.updater = null;
		this.yamba.setServiceRunning(false);// Update serviceRunning flag in Yampa83Application
		Log.d(TAG,"onDestroyed");
	}
	
	/* Thread that performs the actual update from the online service */
	private class Updater extends Thread { // Extends Java's Thread class
		List<Twitter.Status> timeline; // Standard Java list in Util class
		
		public Updater() {
			super("UpdaterService-Updater");
		}

	@Override
	public void run() { // Provide run method to Java thread
		UpdaterService updaterService = UpdaterService.this; //creates reference to this service
		while (updaterService.runFlag) { // Network updates keep looping
			Log.d(TAG,"Updater running");
			try { // Get the timeline from the cloud
				try {
					timeline = yamba.getTwitter().getFriendsTimeline();
				} catch (TwitterException e) {
					Log.d(TAG,"Failed to connect to twitter service", e);
				}
				
				/* Loop over timelines and print it out */
				for (Twitter.Status status : timeline) {
					Log.d(TAG, String.format("%s: %s", status.user.name, status.text));
				}	
				Log.d(TAG,"Updater ran");
				Thread.sleep(DELAY); 
			} catch (InterruptedException e) {
				updaterService.runFlag = false;
				}
			}
		}
	} 
}
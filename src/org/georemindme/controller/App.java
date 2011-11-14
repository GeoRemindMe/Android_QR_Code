package org.georemindme.controller;

import org.georemindme.mvc.mvcframework.controller.MVCControllerMessages;

import android.app.Application;
import android.util.Log;

public class App extends Application {
	
	private static final String	LOG	= "GeoRemindMe";
		
	private Controller	controller;
	
	public void onCreate()
	{
		
		Log.v (LOG, "App-onCreate");
		
		controller = Controller.getInstace(getApplicationContext());
		
	}
	
	/**
	 * If system has memory issues and it has to kill processes, this callback
	 * will be triggered.
	 * 
	 * @author Ricardo C.
	 */
	public void onLowMemory()
	{
		Log.v (LOG, "App-onLowMemory ()");
		
		super.onLowMemory();
	}
	
	/**
	 * If the applications is shutting down, this method will be called.
	 * 
	 * @author Ricardo C.
	 */
	public void onTerminate()
	{
		Log.v (LOG, "App-onTerminate ()");
		
		super.onTerminate();
		
		controller.sendMessage(MVCControllerMessages.MVCCONTROLLER_QUIT);
	}

}

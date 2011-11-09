package org.georemindme.controller;


import org.georemindme.controller.appserver.Server;
import org.georemindme.mvc.mvcframework.controller.MVCController;
import org.georemindme.mvc.mvcframework.controller.MVCControllerStateInterface;

import android.content.Context;
import android.util.Log;

public class Controller extends MVCController {
	
	private static final String LOG = "GeoRemindMe";
	
	private static Controller instance;
	private MVCControllerStateInterface	state;
	private Context context;
	private Server server;
	
	
	public static Controller getInstace(Context context)
	{
		Log.v(LOG, "Controller-getInstance (context)");
		
		if (instance == null)
			instance = new Controller(context);
		
		return instance;
	}
	
	public Context getContext()
	{
		return context;
	}
	
	protected Controller(Context context)
	{		
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
		
		Log.v(LOG, "Controller-Controller (context)");
		
		// Creamos el estado y lo registramos en el MVC
		state = new ReadyState(this);
		this.changeMVCState(state);

		
		server = Server.getInstance(context, this);
	}
	
	public Server getServerInstance()
	{
		// TODO Auto-generated method stub
		return server;
	}
}

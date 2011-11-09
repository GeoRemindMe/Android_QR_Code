package org.georemindme.controller;

import static org.georemindme.controller.ControllerProtocol.*;

import org.georemindme.mvc.mvcframework.controller.MVCControllerStateInterface;

import android.os.Message;
import android.util.Log;


public class ReadyState implements MVCControllerStateInterface
{
	private static final String LOG = "GeoRemindMe";
	
	private final Controller controller;
	
	public ReadyState(Controller controller)
	{
		this.controller = controller;
	}
	
	public boolean handleMessage(Message msg)
	{
		Log.v(LOG, "ReadyState-Receive What: " + msg.what + " || Obj: " + msg.obj);
		
		// El estado del controlador recibe los mensajes y realiza las acciones necesarias para cada uno de ellos
		switch(msg.what)
		{ 				
			case REQUEST:
				controller.getServerInstance().dameLaSiguientePista((String) msg.obj);
				return true;
			case RESPONSE_OK:
				controller.broadcastMessage(msg);
				return true;			
			case RESPONSE_ERROR:
				controller.broadcastMessage(msg);
				return true;
			
		}
		return false;
	}


	private void onRequestQuit()
	{
		// TODO Auto-generated method stub
		controller.quit();
	}
}

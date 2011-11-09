package org.georemindme.mvc.mvcframework.view;



import org.georemindme.mvc.mvcframework.controller.MVCController;

import android.os.Handler;
import android.os.Handler.Callback;

public abstract class MVCViewComponent implements Callback
{
	private MVCController _controller = null;
	private Handler _inbox = null;
	
	public MVCViewComponent()
	{
		// TODO Auto-generated constructor stub
		this(null);
	}
	
	public MVCViewComponent(MVCController controller)
	{
		super();
		_controller = controller;
		_inbox = new Handler(this);
	}
	
	public void setController(MVCController controller)
	{
		_controller = controller;
	}
	
	public Handler getInbox()
	{
		return _inbox;
	}
	
	public void sendMessage(int what, Object obj)
	{
		_controller.sendMessage(what, obj);
	}
}

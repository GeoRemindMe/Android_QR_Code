	package org.georemindme.mvc.mvcframework.controller;


import java.util.ArrayList;
import java.util.List;

import org.georemindme.mvc.mvcframework.view.MVCViewComponent;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;


public class MVCController
{
	private Context					_context;
	
	private static MVCController	_instance	= null;
	
	private List<Handler>			_outbox;
	private Handler					_inbox;
	private HandlerThread			_inboxThread;
	
	private MVCControllerStateInterface		_state		= null;
	
	
	public final static MVCController getInstance(Context context)
	{
		if (_instance == null)
			_instance = new MVCController(context);
		return _instance;
	}
	
	public final void registerMVCComponent(MVCViewComponent component)
	{
		unregisterMVCComponent(component);
		_outbox.add(component.getInbox());
	}

	public final void unregisterMVCComponent(MVCViewComponent component)
	{
		_outbox.remove(component.getInbox());
	}
	
	public final void notifyAllMVCComponents(Message msg)
	{
		if(!_outbox.isEmpty())
		{
			for(Handler h : _outbox)
			{
				h.obtainMessage(msg.what, msg.arg1, msg.arg2, msg.obj).sendToTarget();
			}
		}
	}
	
	public final void notifyAllMVCComponents(int what, int arg1, int arg2, Object obj)
	{
		if(!_outbox.isEmpty())
		{
			for(Handler h : _outbox)
			{
				h.obtainMessage(what, arg1, arg2, obj).sendToTarget();
			}
		}
	}
	
	public final void changeMVCState(MVCControllerStateInterface newState)
	{
		_state = newState;
	}
	
	public final synchronized MVCController sendMessage(int what, Object obj)
	{
		_inbox.obtainMessage(what, obj).sendToTarget();
		return this;
	}
	
	public final synchronized MVCController sendMessage(int what)
	{
		_inbox.obtainMessage(what).sendToTarget();
		return this;
	}
	
	public final synchronized MVCController broadcastMessage(int what, int arg1, int arg2, Object obj)
	{
		notifyAllMVCComponents(what, arg1, arg2, obj);
		return this;
	}
	
	public final synchronized MVCController broadcastMessage(Message msg)
	{
		notifyAllMVCComponents(msg);
		return this;
	}
	
	public final void quit()
	{
		notifyAllMVCComponents(MVCControllerMessages.MVCCONTROLLER_QUIT, 0, 0, null);
	}

	public final Message obtainInboxMessage()
	{
		return _inbox.obtainMessage();
	}
	protected MVCController(Context context)
	{
		_context = context;
		
		_inboxThread = new HandlerThread(this.getClass().getSimpleName());
		_inboxThread.start();
		
		_inbox = new Handler(_inboxThread.getLooper())
		{
			public void handleMessage(Message msg)
			{
				try
				{
					MVCController.this.handleMessage(msg);
				}
				catch (MVCControllerExceptionMessageUnknown e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (MVCControllerExceptionUnknownState e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
		_outbox = new ArrayList<Handler>();
		
	}
	

	private void handleMessage(Message msg) throws MVCControllerExceptionMessageUnknown, MVCControllerExceptionUnknownState
	{
		// TODO Auto-generated method stub
		Log.i("Message received", "What: " + msg.what + " || Object: " + msg.obj);
		if(_state != null)
		{
			if(!_state.handleMessage(msg))
			{
				throw new MVCControllerExceptionMessageUnknown("Message code: " + msg.what + 
						" || Message object: " + msg.obj);
			}
		}
		else
		{
			throw new MVCControllerExceptionUnknownState("Message code: " + msg.what + 
						" || Message object: " + msg.obj);
		}
	}
}

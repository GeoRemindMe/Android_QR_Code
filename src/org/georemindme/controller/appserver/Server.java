package org.georemindme.controller.appserver;

import static org.georemindme.controller.ControllerProtocol.*;

import java.io.Serializable;

import org.alexd.jsonrpc.JSONRPCClient;
import org.alexd.jsonrpc.JSONRPCException;
import org.georemindme.controller.Controller;
import org.georemindme.mvc.mvcframework.view.MVCViewComponent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.util.Log;



public class Server implements Serializable {
	
	private static final String LOG = "GeoRemindMe";
	
	// URL de peticion
	private static final String URL = "http://www.rauljimenez.info/dev/tallerAndroid/api.php?id=";
	
	// Instancia del servidor
	private static Server instance;

	// Controller
	private Controller controller;
	
	// Conector
	private MVCViewComponent connector = null;
	
	// Cliente para la conexion con el servidor
	private JSONRPCClient	connection;
	
	
	public static Server getInstance(Context context, Controller controller)
	{
		Log.v(LOG, "Server-getInstance (context, controller)");
		
		if (instance == null)
			instance = new Server(context, controller);
		
		return instance;
	}
	
	Server(Context context, Controller controller)
	{
		Log.v(LOG, "Server-Server (context, controller)");
		
		// Por si queremos capturar algún mensaje en el servidor
		connector = new MVCViewComponent(controller)
		{
			
			public boolean handleMessage(Message msg)
			{
				// TODO Auto-generated method stub
				return false;
			}
		};
		this.controller = controller;
		
	}
	
	private void closeConnection()
	{
		Log.v(LOG, "Server-closeConnection ()");
		
		connection = null;
	}
	
	private void openConnection()
	{
		Log.v(LOG, "Server-openConnection ()");
		
		// Abrimos la conexión
		connection = JSONRPCClient.create(Server.URL);
	}	
	
	public final void dameLaSiguientePista(final String pista)
	{
		Log.v(LOG, "Server-dameLaSiguientePista ()");
		
		// Preparamos una hebra que será la que se encarga de la conexion con el servidor
		Thread thread = new Thread("getTimeline_Thread")
		{
			public void run ()
			{
				JSONObject response = null;
				
				// Abrimos la conexion
				openConnection ();
				
				try {
					// Hacemos la peticion
					response = connection.callJSONObject(pista);
					closeConnection();

					// Capturamos los datos necesarios recibidos por el servidor
					if (response.optString("Pista"+pista) != "") {
						String pistilla = response.getString("Pista"+pista);
						// Todo ha ido perfecto, avisamos al controllador y le mandamos los datos
						controller.sendMessage(RESPONSE_OK, pistilla);
					}
					else {
						controller.sendMessage(RESPONSE_ERROR);
					}
					// No se porque me genera dos catch. REVISAR
				} catch (JSONRPCException e) {
					// TODO Auto-generated catch block
					controller.sendMessage(RESPONSE_ERROR);
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					controller.sendMessage(RESPONSE_ERROR);
					e.printStackTrace();
				}
			}
		};
		// Lanzamos la hebra
		thread.start();	
	}


}

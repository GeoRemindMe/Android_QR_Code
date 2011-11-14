package org.georemindme.view;

import static org.georemindme.controller.ControllerProtocol.*;

import org.georemindme.R;
import org.georemindme.controller.Controller;
import org.georemindme.mvc.mvcframework.view.MVCViewComponent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class captureQRCodeActivity extends Activity {
	
	private static final String LOG = "GeoRemindMe";
	
	private Controller _controller;
	
	private MVCViewComponent _connector = null;
	
	// Interface
	private TextView _pista;
	private ProgressDialog _dialog;
	
	private String _idPista = null;
	
	public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        
        Log.v(LOG, "ConectActivity-OnCreate ()");        
        
        // Delete title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        
        setContentView(R.layout.captureqr);
        
        // Capturamos el Intent y vemos si tiene algun dato para nosotros
        Intent i = getIntent();
        _idPista = getIntent().getStringExtra("pista");
        if (_idPista == null) { 
	        Log.v(LOG, "DATA-STRING: "+i.getDataString());
	        Log.v(LOG, "PARAMETERS: "+i.getData().getQueryParameter("pista"));
	        _idPista = i.getData().getQueryParameter("pista");
        }
        
        _pista = (TextView) findViewById(R.id.pista);
        
        // Controller
        _controller = Controller.getInstace(getApplicationContext());
        
        // Manejador de mensajes
        _connector = new MVCViewComponent(_controller)
		{

			public boolean handleMessage(Message msg) 
			{
				// TODO Auto-generated method stub
				Log.v(LOG, "captureQRCodeActivity-Receive What: " + msg.what + " || Obj: " + msg.obj); 
				
				// Capturamos los mensajes que le interesan a esta interfaz y realizamos las acciones necesarias
				switch (msg.what) {
					case RESPONSE_OK:
						_dialog.cancel();
						_pista.setText((CharSequence) msg.obj);
						return true;
					case RESPONSE_ERROR:
						_dialog.cancel();
						_pista.setTextColor(Color.RED);
						_pista.setText("ERROR Grumete!!");
						Toast.makeText(getApplicationContext(),"Ocurrio un error obteniendo tu pista. Tirar al desarrollador a los tiburones!! Arghhhh!", Toast.LENGTH_LONG).show();
						return true;
				}
				return false;
			}
        	
		};
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		Log.v(LOG, "captureQRCodeActivity-onResume ()");
		super.onResume();
		
		// Lazamos un dialgo
		_dialog = ProgressDialog.show(this, "", getString(R.string.dialogo), true);
		
		// Registramos el componente
		_controller.registerMVCComponent(_connector);
		
		// Mandamos al petición al controlador
		_connector.sendMessage(REQUEST, String.valueOf(_idPista));
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		_controller.unregisterMVCComponent(_connector);
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

}

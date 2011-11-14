package org.alexd.jsonrpc;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


/**
 * Implementation of JSON-RPC over HTTP/POST
 */
public class JSONRPCHttpClient extends JSONRPCClient
{
	
	/*
	 * HttpClient to issue the HTTP/POST request
	 */
	private HttpClient						httpClient;
	/*
	 * Service URI
	 */
	private String							serviceUri;	
	/*
	 * Service Method
	 */
	private String							methodUri;
	
	// HTTP 1.0
	private static final ProtocolVersion	PROTOCOL_VERSION	= new ProtocolVersion("HTTP", 1, 0);
	
	
	/**
	 * Construct a JsonRPCClient with the given service uri
	 * 
	 * @param uri
	 *            uri of the service
	 */
	public JSONRPCHttpClient(String uri)
	{
		httpClient = new DefaultHttpClient();
		serviceUri = uri;
		methodUri = null;
		
	}
	
	protected JSONObject doRequest(String method, Object[] params)
			throws JSONRPCException
	{
		Log.d("json-rpc", "JSONRPCHttpClient-doRequest(method, params)");
		
		// Copy method arguments in a json array
				JSONArray jsonParams = new JSONArray();
				if (params != null)
					for (int i = 0; i < params.length; i++)
					{
						jsonParams.put(params[i]);
					}
				
				// Create the json request object
				JSONObject jsonRequest = new JSONObject();
				methodUri = method;
				
				return doJSONRequest(jsonRequest);
	}

	protected JSONObject doJSONRequest(JSONObject jsonRequest)
			throws JSONRPCException
	{
		if (serviceUri.startsWith("https"))
		{
			HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
			
			DefaultHttpClient client = new DefaultHttpClient();
			
			SchemeRegistry registry = new SchemeRegistry();
			SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
			socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
			registry.register(new Scheme("https", socketFactory, 443));
			SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
			DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());
			
			// Set verifier
			HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
		}
        
		// Create HTTP/POST request with a JSON entity containing the request
		HttpPost request = new HttpPost(serviceUri + methodUri);
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, getConnectionTimeout());
		HttpConnectionParams.setSoTimeout(params, getSoTimeout());
		
		HttpProtocolParams.setVersion(params, PROTOCOL_VERSION);
		request.setParams(params);
		
		HttpEntity entity;
		try
		{
			entity = new JSONEntity(jsonRequest);
		}
		catch (UnsupportedEncodingException e1)
		{
			throw new JSONRPCException("Unsupported encoding", e1);
		}
		request.setEntity(entity);
		
		try
		{
			// Execute the request and try to decode the JSON Response
			long t = System.currentTimeMillis();
			HttpResponse response = httpClient.execute(request);
			t = System.currentTimeMillis() - t;
			Log.d("json-rpc", "Request Package :" + request.toString());
			Log.d("json-rpc", "Request time :" + t);
			Log.d("json-rpc", "URL :" + serviceUri + methodUri);
			Log.d("json-rpc", "Params :" + jsonRequest);
			String responseString = EntityUtils.toString(response.getEntity());
			responseString = responseString.trim();
			Log.d("json-rpc", "response: "+responseString);
			Log.d("json-rpc", "----------------------------------------------------------------------------------");
			JSONObject jsonResponse = new JSONObject(responseString);
			// Check for remote errors
			if (jsonResponse.has("error"))
			{
				Object jsonError = jsonResponse.get("error");
				if (!jsonError.equals(null))
					throw new JSONRPCException(jsonResponse.get("error"));
				return jsonResponse; // JSON-RPC 1.0
			}
			else
			{
				return jsonResponse; // JSON-RPC 2.0
			}
		}
		// Underlying errors are wrapped into a JSONRPCException instance
		catch (ClientProtocolException e)
		{
			throw new JSONRPCException("HTTP error", e);
		}
		catch (IOException e)
		{
			throw new JSONRPCException("IO error", e);
		}
		catch (JSONException e)
		{
			throw new JSONRPCException("Invalid JSON response", e);
		}
	}
	

	private static String convertStreamToString(InputStream is)
			throws IOException
	{
		/*
		 * To convert the InputStream to String we use the Reader.read(char[]
		 * buffer) method. We iterate until the Reader return -1 which means
		 * there's no more data to read. We use the StringWriter class to
		 * produce the string.
		 */
		if (is != null)
		{
			Writer writer = new StringWriter();
			
			char[] buffer = new char[1024];
			try
			{
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1)
				{
					writer.write(buffer, 0, n);
					String s = new String(buffer);
					Log.e("DATOS:", s);
				}
			}
			finally
			{
				is.close();
			}
			return writer.toString();
		}
		else
		{
			return "";
		}
		
	}
	
}

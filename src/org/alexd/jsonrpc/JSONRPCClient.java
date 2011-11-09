package org.alexd.jsonrpc;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public abstract class JSONRPCClient
{
	
	/**
	 * Create a JSONRPCClient from a given uri
	 * 
	 * @param uri
	 *            The URI of the JSON-RPC service
	 * @return a JSONRPCClient instance acting as a proxy for the web service
	 */
	public static JSONRPCClient create(String uri)
	{
		return new JSONRPCHttpClient(uri);
	}
	

	protected abstract JSONObject doJSONRequest(JSONObject request)
			throws JSONRPCException;
	

	protected JSONObject doRequest(String method, Object[] params)
			throws JSONRPCException
	{
		Log.v("json-rpc", "JSONRPCClient-doRequest(method, params)");
		
		// Copy method arguments in a json array
		JSONArray jsonParams = new JSONArray();
		if (params != null)
			for (int i = 0; i < params.length; i++)
			{
				jsonParams.put(params[i]);
			}
		
		// Create the json request object
		JSONObject jsonRequest = new JSONObject();
		try
		{
			// id hard-coded at 1 for now
			jsonRequest.put("id", 1);
			jsonRequest.put("method", method);
			jsonRequest.put("params", jsonParams);
		}
		catch (JSONException e1)
		{
			throw new JSONRPCException("Invalid JSON request", e1);
		}
		
		return doJSONRequest(jsonRequest);
	}
	
	protected int	soTimeout	= 0, connectionTimeout = 0;
	
	
	// public Object beginCall(String method, final Object ... params)
	// {
	// //Handler
	// class RequestThread extends Thread {
	// String mMethod;
	// Object[] mParams;
	// public RequestThread(String method, Object[] params)
	// {
	// mMethod = method;
	// mParams = params;
	// }
	// @Override
	// public void run() {
	// try
	// {
	// doRequest(mMethod, mParams);
	// }
	// catch (JSONRPCException e)
	// {
	//
	// }
	// }
	//
	// };
	// RequestThread requestThread = new RequestThread(method, params);
	// requestThread.start();
	//
	// return null;
	// }
	
	/**
	 * Get the socket operation timeout in milliseconds
	 */
	public int getSoTimeout()
	{
		return soTimeout;
	}
	

	/**
	 * Set the socket operation timeout
	 * 
	 * @param soTimeout
	 *            timeout in milliseconds
	 */
	public void setSoTimeout(int soTimeout)
	{
		this.soTimeout = soTimeout;
	}
	

	/**
	 * Get the connection timeout in milliseconds
	 */
	public int getConnectionTimeout()
	{
		return connectionTimeout;
	}
	

	/**
	 * Set the connection timeout
	 * 
	 * @param connectionTimeout
	 *            timeout in milliseconds
	 */
	public void setConnectionTimeout(int connectionTimeout)
	{
		this.connectionTimeout = connectionTimeout;
	}
	

	/**
	 * Perform a remote JSON-RPC method call
	 * 
	 * @param method
	 *            The name of the method to invoke
	 * @param params
	 *            Arguments of the method
	 * @return The result of the RPC
	 * @throws JSONRPCException
	 *             if an error is encountered during JSON-RPC method call
	 */
	public Object call(String method, Object... params) throws JSONRPCException
	{
		try
		{
			return doRequest(method, params).get("result");
		}
		catch (JSONException e)
		{
			throw new JSONRPCException("Cannot convert result", e);
		}
	}
	

	/**
	 * Perform a remote JSON-RPC method call
	 * 
	 * @param method
	 *            The name of the method to invoke
	 * @param params
	 *            Arguments of the method
	 * @return The result of the RPC as a String
	 * @throws JSONRPCException
	 *             if an error is encountered during JSON-RPC method call
	 */
	public String callString(String method, Object... params)
			throws JSONRPCException
	{
		return doRequest(method, params).toString();
	}
	

	/**
	 * Perform a remote JSON-RPC method call
	 * 
	 * @param method
	 *            The name of the method to invoke
	 * @param params
	 *            Arguments of the method
	 * @return The result of the RPC as an int
	 * @throws JSONRPCException
	 *             if an error is encountered during JSON-RPC method call
	 */
	public int callInt(String method, Object... params) throws JSONRPCException
	{
		try
		{
			return doRequest(method, params).getInt("result");
		}
		catch (JSONException e)
		{
			throw new JSONRPCException("Cannot convert result to int", e);
		}
	}
	

	/**
	 * Perform a remote JSON-RPC method call
	 * 
	 * @param method
	 *            The name of the method to invoke
	 * @param params
	 *            Arguments of the method
	 * @return The result of the RPC as a long
	 * @throws JSONRPCException
	 *             if an error is encountered during JSON-RPC method call
	 */
	public long callLong(String method, Object... params)
			throws JSONRPCException
	{
		try
		{
			return doRequest(method, params).getLong("result");
		}
		catch (JSONException e)
		{
			throw new JSONRPCException("Cannot convert result to long", e);
		}
	}
	

	/**
	 * Perform a remote JSON-RPC method call
	 * 
	 * @param method
	 *            The name of the method to invoke
	 * @param params
	 *            Arguments of the method
	 * @return The result of the RPC as a boolean
	 * @throws JSONRPCException
	 *             if an error is encountered during JSON-RPC method call
	 */
	public boolean callBoolean(String method, Object... params)
			throws JSONRPCException
	{
		try
		{
			return doRequest(method, params).getBoolean("result");
		}
		catch (JSONException e)
		{
			throw new JSONRPCException("Cannot convert result to boolean", e);
		}
	}
	

	/**
	 * Perform a remote JSON-RPC method call
	 * 
	 * @param method
	 *            The name of the method to invoke
	 * @param params
	 *            Arguments of the method
	 * @return The result of the RPC as a double
	 * @throws JSONRPCException
	 *             if an error is encountered during JSON-RPC method call
	 */
	public double callDouble(String method, Object... params)
			throws JSONRPCException
	{
		try
		{
			return doRequest(method, params).getDouble("result");
		}
		catch (JSONException e)
		{
			throw new JSONRPCException("Cannot convert result to double", e);
		}
	}
	

	/**
	 * Perform a remote JSON-RPC method call
	 * 
	 * @param method
	 *            The name of the method to invoke
	 * @param params
	 *            Arguments of the method
	 * @return The result of the RPC as a JSONObject
	 * @throws JSONRPCException
	 *             if an error is encountered during JSON-RPC method call
	 */
	public JSONObject callJSONObject(String method, Object... params)
			throws JSONRPCException, JSONException
	{
		return doRequest(method, params);
	}
	

	/**
	 * Perform a remote JSON-RPC method call
	 * 
	 * @param method
	 *            The name of the method to invoke
	 * @param params
	 *            Arguments of the method
	 * @return The result of the RPC as a JSONArray
	 * @throws JSONRPCException
	 *             if an error is encountered during JSON-RPC method call
	 */
	public JSONArray callJSONArray(String method, Object... params)
			throws JSONRPCException
	{
		try
		{
			return doRequest(method, params).getJSONArray("result");
		}
		catch (JSONException e)
		{
			throw new JSONRPCException("Cannot convert result to JSONArray", e);
		}
	}
}

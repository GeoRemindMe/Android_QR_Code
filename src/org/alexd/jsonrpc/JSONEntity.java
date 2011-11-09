package org.alexd.jsonrpc;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

/**
 * Provides a HttpEntity for json content
 */
class JSONEntity extends StringEntity 
{
	public JSONEntity(JSONObject jsonObject) throws UnsupportedEncodingException 
	{
		super(new String(jsonObject.toString().getBytes(), "utf-8"), "utf-8");
	}

	@Override
	public Header getContentType() 
	{
		//return new BasicHeader(HTTP.CONTENT_TYPE, "application/json");
		return new BasicHeader(HTTP.CONTENT_TYPE, "application/json");
	}	
}

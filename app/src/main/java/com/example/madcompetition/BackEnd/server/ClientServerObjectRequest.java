package com.example.madcompetition.BackEnd.server;

import java.io.Serializable;

public class ClientServerObjectRequest implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8008402538532953040L;
	private ObjectRequest objectRequest;
	private String data;
	

public ClientServerObjectRequest(ObjectRequest request, String data)
{
	this.objectRequest = request;
	this.data = data;
}

	/**
	 * @return the objectRequest
	 */
	public ObjectRequest getObjectRequest()
	{
		return objectRequest;
	}



	/**
	 * @param objectRequest the objectRequest to set
	 */
	public void setObjectRequest(ObjectRequest objectRequest)
	{
		this.objectRequest = objectRequest;
	}



	public ClientServerObjectRequest(ObjectRequest request)
	{
		this.objectRequest = request;

	}



	public String getData()
	{
		return data;
	}



	public void setData(String data)
	{
		this.data = data;
	}
	
	

}

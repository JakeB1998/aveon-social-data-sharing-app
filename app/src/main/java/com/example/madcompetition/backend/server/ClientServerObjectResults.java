package com.example.madcompetition.backend.server;

import java.io.Serializable;

public class ClientServerObjectResults implements Serializable
{
	private ObjectRequest initialRequest;
	private byte[] data;
	private boolean onReturn;

	/**
	 * 
	 * @param initialRequest
	 * @param data
	 */
	public ClientServerObjectResults(ObjectRequest initialRequest, byte[] data)
	{
		this.initialRequest = initialRequest;
		this.data = data;
		onReturn = true;
	}

	/**
	 * @return the initialRequest
	 */
	public ObjectRequest getInitialRequest()
	{
		return initialRequest;
	}

	/**
	 * @return the data
	 */
	public byte[] getData()
	{
		return data;
	}

	/**
	 * @return the onReturn
	 */
	public boolean isOnReturn()
	{
		return onReturn;
	}
	
	

}

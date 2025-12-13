package jdf.framework.logic.adapter.http;

import jdf.framework.core.data.ResourceException;
import jdf.framework.core.data.cci.Interaction;



public class ConnectionImpl extends jdf.framework.logic.adapter.AbstractConnection
{

	@Override
	public void beginTransaction() throws ResourceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rollbackTransaction() throws ResourceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commitTransaction() throws ResourceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() throws ResourceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Interaction createInteraction() throws ResourceException {
		// TODO Auto-generated method stub
		return new HttpInteraction(this);
	}
	
}
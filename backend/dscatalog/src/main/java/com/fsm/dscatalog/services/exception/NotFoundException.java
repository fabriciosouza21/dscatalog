package com.fsm.dscatalog.services.exception;

public class NotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public NotFoundException (String msg) {
		super(msg);
	}

}

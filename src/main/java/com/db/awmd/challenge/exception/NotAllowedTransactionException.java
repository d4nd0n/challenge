package com.db.awmd.challenge.exception;

public class NotAllowedTransactionException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public NotAllowedTransactionException(String message) {		
		super(message);
		//System.out.println(message);
	}


}


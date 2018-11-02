package com.huayin.printmanager.exception;

public class StockLowException extends Exception
{

	private static final long serialVersionUID = 1L;
	
	public StockLowException(String message){
		super(message);
	}

}

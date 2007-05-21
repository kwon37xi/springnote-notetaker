package com.springnote.notetaker;

public class NoteTakerException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public NoteTakerException(String msg) {
		super(msg);
	}
	
	public NoteTakerException(String msg, Exception ex) {
		super(msg, ex);
	}

}

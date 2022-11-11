package com.assignment.sdworx.enums;

public enum State {
	
	ERROR(0),
	OK(1);
	
	private int code;
	
	private State(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	

}

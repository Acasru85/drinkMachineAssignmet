package com.assignment.sdworx.enums;

public enum Status {
	
	OFF(0),
	ON(1);
	
	private int code;
	
	private Status(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
}

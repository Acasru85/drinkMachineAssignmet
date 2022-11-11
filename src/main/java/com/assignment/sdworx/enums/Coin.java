package com.assignment.sdworx.enums;

public enum Coin {
	
	CENTS_5(0.05, "5 cents"),
	CENTS_10(0.1, "10 cents"),
	CENTS_20(0.2, "20 cents"),
	CENTS_50(0.5, "50 cents"),
	EURO_1(1, "1 Euro"),
	EURO_2(2, "2 Euro");
	
	private double value;
	
	private String description;
	
	private Coin(double value, String description) {
		this.value = value;
		this.description = description;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	

}

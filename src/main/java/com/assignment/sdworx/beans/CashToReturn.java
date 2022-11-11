package com.assignment.sdworx.beans;

public class CashToReturn {
	
	private Cash fromCredit;
	
	private Cash fromCash;
	
	

	public CashToReturn(Cash fromCredit, Cash fromCash) {
		super();
		this.fromCredit = fromCredit;
		this.fromCash = fromCash;
	}

	public Cash getFromCredit() {
		return fromCredit;
	}

	public void setFromCredit(Cash fromCredit) {
		this.fromCredit = fromCredit;
	}

	public Cash getFromCash() {
		return fromCash;
	}

	public void setFromCash(Cash fromCash) {
		this.fromCash = fromCash;
	}
	
	

}

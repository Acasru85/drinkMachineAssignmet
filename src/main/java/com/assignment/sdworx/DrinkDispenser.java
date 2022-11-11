package com.assignment.sdworx;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.assignment.sdworx.beans.Cash;
import com.assignment.sdworx.beans.CashToReturn;
import com.assignment.sdworx.beans.Slot;
import com.assignment.sdworx.enums.Coin;
import com.assignment.sdworx.enums.State;
import com.assignment.sdworx.enums.Status;

public class DrinkDispenser {
	
	protected static final Logger LOG = LoggerFactory.getLogger(DrinkDispenser.class);
	
	public List<Slot> stock;
	
	public Cash cash;
	
	public Cash credit;
	
	public State state;
	
	public Status status;

	public DrinkDispenser(List<Slot> stock, Cash cash, Cash credit, State state, Status status) {
		super();
		this.stock = stock;
		this.cash = cash;
		this.credit = credit;
		this.state = state;
		this.status = status;
	}

	public List<Slot> getStock() {
		return stock;
	}

	public void setStock(List<Slot> stock) {
		this.stock = stock;
	}

	public Cash getCash() {
		return cash;
	}

	public void setCash(Cash cash) {
		this.cash = cash;
	}

	public Cash getCredit() {
		return credit;
	}

	public void setCredit(Cash credit) {
		this.credit = credit;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public void insertCoin(Coin c) {
		this.getCredit().addCoins(c, 1);
	}
	
	public void sellingProduct(int slotNumber) {
		if(this.getStatus().getCode() == Status.ON.getCode()) {
			if(this.getState().getCode() == State.OK.getCode()) {
				if(slotNumber < this.getStock().size()) {
					if(this.getStock().get(slotNumber).getAmount() > 0) {
						if(this.credit.totalCash() >= this.getStock().get(slotNumber).getProduct().getPrice()) {
							CashToReturn cashToReturn = this.calculateReturn(this.getStock().get(slotNumber).getProduct().getPrice());
							this.getStock().get(slotNumber).setAmount(this.getStock().get(slotNumber).getAmount()-1);
							returningCash(cashToReturn);
						}else {
							throw new IllegalArgumentException("Not enough credit");
						}
					}else {
						throw new IllegalArgumentException("Empty slot");
					}
				}else {
					throw new IllegalArgumentException("Wrong slot number, please choose a correct one");
				}
			}else {
				throw new IllegalStateException("Dispenser has one or more errors");
			}
		}else {
			throw new IllegalStateException("Dispenser is off, please turn on");
		}
		
	}

	private void returningCash(CashToReturn cashToReturn) {
		LOG.info("Returning cash from credit");
		cashToReturn.getFromCredit().toString();
		this.credit.subCash(cashToReturn.getFromCredit());
		LOG.info("Processing payment");
		this.credit.toString();
		this.getCash().addCash(this.credit);
		LOG.info("Returning cash from cash");
		cashToReturn.getFromCash().toString();
		this.cash.subCash(cashToReturn.getFromCash());
		LOG.info("Thank you!!");
		this.credit=new Cash();
		
	}
	
	public void cancellingOperation() {
		LOG.info("Cancelling operation... returning credit");
		this.credit=new Cash();
	}
	
public CashToReturn calculateReturn(double money) {

		
		LOG.info("Calculating money to return...");
		
		double totalCredit = this.getCredit().totalCash();
		double moneyToReturn = 0.0;

		Cash cashToReturn = new Cash();
		Cash cashToDropFromCredit = new Cash();

		if(totalCredit >= money) {
			List<Coin> coinList =  Arrays.stream(Coin.values()).sorted((c1, c2) -> Double.compare(c2.getValue(), c1.getValue())).collect(Collectors.toList());
			moneyToReturn = totalCredit - money;

			Iterator <Coin> coinIterator = coinList.iterator();
			Coin currentCoin;
			while(moneyToReturn > 0 &&  coinIterator.hasNext()) {

				currentCoin = coinIterator.next();

				if(currentCoin.getValue() <= moneyToReturn) {

					int coinsNeeded = (int) (moneyToReturn / currentCoin.getValue());

					//returning from client credit first
					if(coinsNeeded > 0) {
						if(this.getCredit().getCoins().get(currentCoin)>=coinsNeeded) {
							cashToDropFromCredit.addCoins(currentCoin, coinsNeeded);
							moneyToReturn = secureRound(moneyToReturn - (currentCoin.getValue() * coinsNeeded), 2.0);
							coinsNeeded = 0;
						}else {
							moneyToReturn =secureRound(moneyToReturn - (currentCoin.getValue() * this.getCredit().getCoins().get(currentCoin)) , 2.0);
							cashToDropFromCredit.addCoins(currentCoin, this.getCredit().getCoins().get(currentCoin));
							coinsNeeded -=  this.getCredit().getCoins().get(currentCoin);
						}
					}

					//Calculating cash to return from our cash
					if(coinsNeeded > 0) {
						if(this.getCash().getCoins().get(currentCoin) > coinsNeeded) {
							cashToReturn.addCoins(currentCoin, coinsNeeded);
							moneyToReturn = secureRound(moneyToReturn - (currentCoin.getValue() * coinsNeeded), 2.0);
						}else {
							cashToReturn.addCoins(currentCoin, this.getCash().getCoins().get(currentCoin));
							moneyToReturn = secureRound(moneyToReturn - (currentCoin.getValue() * this.getCash().getCoins().get(currentCoin)), 2.0);
						}
					}

				}
			}

			if(moneyToReturn != 0) {
				throw new IllegalArgumentException("Not enuogh cash to return");
			}

		}else{
			throw new IllegalArgumentException("Not enuogh cash to make the payment");
		}

		LOG.info("Money to return calculated...");
		return new CashToReturn(cashToDropFromCredit, cashToReturn);

	}


	public double secureRound(double number, double decimals) {
		double multiplier = Math.pow(10.0, decimals);
		
		return Math.round(number * multiplier) / multiplier;
	}

	
	
	
	
	
	
	
	

}

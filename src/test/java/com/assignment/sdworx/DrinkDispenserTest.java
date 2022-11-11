package com.assignment.sdworx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.assignment.sdworx.beans.Cash;
import com.assignment.sdworx.beans.Product;
import com.assignment.sdworx.beans.Slot;
import com.assignment.sdworx.enums.Coin;
import com.assignment.sdworx.enums.State;
import com.assignment.sdworx.enums.Status;

public class DrinkDispenserTest {

	private DrinkDispenser machine;

	@Before
	public void initTest() {
		List<Slot> stock = new ArrayList<Slot>();
		stock.add(new Slot(new Product("Coke", 1.0), 1));
		stock.add(new Slot(new Product("Redbull", 1.25), 1));
		stock.add(new Slot(new Product("Water", 0.5), 1));
		stock.add(new Slot(new Product("Orange juice", 1.95), 1));

		Map<Coin, Integer> coins = new HashMap<Coin, Integer>();

		Arrays.stream(Coin.values()).forEach(c -> coins.put(c, 1));

		Cash cash = new Cash(coins);

		machine = new DrinkDispenser(stock, cash, new Cash(), State.OK, Status.ON);

	}

	@Test
	public void simpleOperation() {
		machine.insertCoin(Coin.CENTS_50);
		assertTrue(machine.getCredit().totalCash() == 0.5);

		machine.sellingProduct(2);

		assertTrue(machine.getStock().get(2).getAmount()==0);
	}

	@Test
	public void complexOperationNoReturnFromCash() {
		machine.insertCoin(Coin.CENTS_50);
		machine.insertCoin(Coin.CENTS_50);
		machine.insertCoin(Coin.CENTS_50);
		assertTrue(machine.getCredit().totalCash() == 1.5);

		machine.sellingProduct(0);

		assertTrue(machine.getStock().get(0).getAmount()==0);
	}

	@Test
	public void complexOperationReturnFromCash() {

		double cashMoney = machine.getCash().totalCash();

		machine.insertCoin(Coin.CENTS_50);
		machine.insertCoin(Coin.CENTS_50);
		machine.insertCoin(Coin.CENTS_50);
		assertTrue(machine.getCredit().totalCash() == 1.5);

		machine.sellingProduct(1);

		double finalCash = machine.getCash().totalCash();

		assertTrue(machine.getStock().get(1).getAmount()==0);
		assertTrue(finalCash == (cashMoney + machine.getStock().get(1).getProduct().getPrice()));
	}

	@Test
	public void complexOperationReturnFromCashAndCredit() {
		double cashMoney = machine.getCash().totalCash();

		machine.insertCoin(Coin.CENTS_50);
		machine.insertCoin(Coin.CENTS_50);
		machine.insertCoin(Coin.CENTS_50);
		machine.insertCoin(Coin.EURO_1);
		assertTrue(machine.getCredit().totalCash() == 2.5);

		machine.sellingProduct(1);

		double finalCash = machine.getCash().totalCash();

		assertTrue(machine.getStock().get(1).getAmount()==0);
		assertTrue(finalCash == (cashMoney + machine.getStock().get(1).getProduct().getPrice()));
	}

	@Test
	public void outOfStock() {
		machine.insertCoin(Coin.CENTS_50);
		assertTrue(machine.getCredit().totalCash() == 0.5);

		machine.sellingProduct(2);
		assertTrue(machine.getStock().get(2).getAmount()==0);

		machine.insertCoin(Coin.CENTS_50);
		assertTrue(machine.getCredit().totalCash() == 0.5);
		assertThrows(IllegalArgumentException.class, () -> machine.sellingProduct(2));

	}

	@Test
	public void notEnoughCredit() {
		machine.insertCoin(Coin.CENTS_50);
		assertTrue(machine.getCredit().totalCash() == 0.5);

		assertThrows(IllegalArgumentException.class, () -> machine.sellingProduct(3));

	}

	@Test 
	public void noCashToReturn() {
		machine.insertCoin(Coin.EURO_2);
		assertTrue(machine.getCredit().totalCash() == 2.0);

		machine.sellingProduct(3);
		assertTrue(machine.getStock().get(3).getAmount()==0);

		machine.insertCoin(Coin.EURO_2);
		assertTrue(machine.getCredit().totalCash() == 2.0);
		assertThrows(IllegalArgumentException.class, () -> machine.sellingProduct(1));
	}
	
	@Test
	public void getStatus() {
		assertTrue(machine.getStatus().getCode() == Status.ON.getCode());
	}
	
	@Test
	public void getState() {
		assertTrue(machine.getState().getCode() == State.OK.getCode());
	}
	
	@Test
	public void cancelOperation() {
		machine.insertCoin(Coin.CENTS_50);
		machine.insertCoin(Coin.CENTS_50);
		machine.insertCoin(Coin.CENTS_50);
		machine.insertCoin(Coin.EURO_1);
		
		assertTrue(machine.getCredit().totalCash() == 2.5);
		
		machine.cancellingOperation();
		
		assertTrue(machine.getCredit().totalCash() == 0.0);
	}

}

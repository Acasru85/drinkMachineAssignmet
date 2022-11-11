package com.assignment.sdworx.beans;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.assignment.sdworx.enums.Coin;

public class Cash {

	protected static final Logger LOG = LoggerFactory.getLogger(Cash.class);

	private Map<Coin, Integer> coins;

	public Cash() {
		this.coins = new HashMap<Coin, Integer>();
		this.coins.put(Coin.CENTS_5, 0);
		this.coins.put(Coin.CENTS_10, 0);
		this.coins.put(Coin.CENTS_20, 0);
		this.coins.put(Coin.CENTS_50, 0);
		this.coins.put(Coin.EURO_1, 0);
		this.coins.put(Coin.EURO_2, 0);
	}



	

	public Cash(Map<Coin, Integer> coins) {
		super();
		this.coins = coins;
	}


	public Map<Coin, Integer> getCoins() {
		return coins;
	}

	public void setCoins(Map<Coin, Integer> coins) {
		this.coins = coins;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[ ").append("\n");

		Arrays.stream(Coin.values()).
		sorted((c1, c2) -> Double.compare(c2.getValue(), c1.getValue())).forEach(c -> {
			sb.append(c.getDescription()).append(" - ").append(this.getCoins().get(c)).append("\n");
		});

		sb.append("]").append("\n");

		return sb.toString();


	}

	public double totalCash() {

		LOG.info("Calculating total cash...");
		Optional<Double> sum = this.getCoins().keySet().stream().map(c -> this.getCoins().get(c) * c.getValue()).reduce(Double::sum);

		Double result = sum.orElseThrow(IllegalArgumentException::new);

		LOG.info("Total cash calculated");
		return result;

	}

	public void addCoins(Coin coin, int numberOfCoins) {
		this.getCoins().put(coin, this.getCoins().get(coin)+numberOfCoins);
	}

	public void subsCoins(Coin coin, int numberOfCoins) {
		this.getCoins().put(coin, this.getCoins().get(coin)-numberOfCoins);
	}

	public void addCash(Cash cash) {
		Arrays.stream(Coin.values()).forEach(c -> {
			this.addCoins(c, cash.getCoins().get(c));
		});
	}

	public void subCash(Cash cash) {
		Arrays.stream(Coin.values()).forEach(c -> {
			this.subsCoins(c, cash.getCoins().get(c));
		});
	}





}

package com.bfm.acs.crazycricket.bean;

public class Player {
	private String country;
	private String user;
	private long playerID;

	public Player(String country, String user, long playerID) {
		super();
		this.country = country;
		this.user = user;
		this.playerID = playerID;
	}

	public Player(String user, String country) {
		super();
		this.country = country;
		this.user = user;
	}

	public long getPlayerID() {
		return playerID;
	}

	public void setPlayerID(long playerID) {
		this.playerID = playerID;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}

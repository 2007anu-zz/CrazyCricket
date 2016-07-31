package com.bfm.acs.crazycricket.bean;

import java.sql.Date;

public class GameStat {
	private long gameID;
	private long winnerID;
	private long loserID;
	private String type;
	private Date gameDate;

	public GameStat(long winnerID, long loserID, String type, Date gameDate) {
		super();
		this.winnerID = winnerID;
		this.loserID = loserID;
		this.type = type;
		this.gameDate = gameDate;
	}

	public long getGameID() {
		return gameID;
	}

	public void setGameID(long gameID) {
		this.gameID = gameID;
	}

	public long getWinnerID() {
		return winnerID;
	}

	public void setWinnerID(long winnerID) {
		this.winnerID = winnerID;
	}

	public long getLoserID() {
		return loserID;
	}

	public void setLoserID(long loserID) {
		this.loserID = loserID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getGameDate() {
		return gameDate;
	}

	public void setGameDate(Date gameDate) {
		this.gameDate = gameDate;
	}

}

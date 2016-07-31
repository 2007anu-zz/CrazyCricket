package com.bfm.acs.crazycricket.dao;

import java.util.List;
import java.util.Map;

import com.bfm.acs.crazycricket.CrazyCricketProtos.Game;
import com.bfm.acs.crazycricket.exception.InvalidDateException;
import com.bfm.acs.crazycricket.bean.Player;

public interface DataStorage {
	void save(Game game);

	Player savePlayer(Player player);

	List<Map<String, Long>> getLeaderBoard(String start, String end);

	List<Map<String, Long>> getNationalLeaderboard(String start, String end);

	boolean dateValidation(String startDate, String endDate) throws InvalidDateException;
}
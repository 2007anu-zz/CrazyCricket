package com.bfm.acs.crazycricket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.bfm.acs.crazycricket.CrazyCricketProtos.Game;
import com.bfm.acs.crazycricket.exception.InvalidDateException;
import com.bfm.acs.crazycricket.bean.Player;

@Service
public class DataSourceHSQLImpl implements DataStorage {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	String dateFormat = "yyyyMMdd";

	@Override
	public List<Map<String, Long>> getLeaderBoard(String start, String end) {
		List<Map<String, Long>> leaderboard = new ArrayList<Map<String, Long>>();
		String whereClause = " WHERE GAME_DATE BETWEEN TO_DATE(?, 'YYYYMMDD') AND TO_DATE(?, 'YYYYMMDD') ";
		boolean check = start != null && !start.equals("") && end != null && !end.equals("");

		StringBuilder query = new StringBuilder();
		query.append("SELECT USER_ID, COUNT(*) WINNERCOUNT FROM ");
		query.append("(SELECT USER_ID, WINNER FROM PLAYERS P LEFT OUTER JOIN ");
		query.append("(SELECT WINNER, LOSER, GAME_DATE, GAME_TYPE FROM GAMES ");
		if (check) {
			query.append(whereClause);
		} else {
			query.append("");
		}
		query.append(") G ");
		query.append("ON (P.PLAYER_ID = G.WINNER) ) GROUP BY USER_ID ORDER BY COUNT(*) DESC, USER_ID ASC");
		PreparedStatementCreator psc = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(query.toString());
				if (check) {
					ps.setString(1, start);
					ps.setString(2, end);
				}
				return ps;
			}
		};

		leaderboard = jdbcTemplate.query(psc, new LeaderBoardMapper());
		return leaderboard;
	}

	@Override
	public List<Map<String, Long>> getNationalLeaderboard(String start, String end) {
		List<Map<String, Long>> leaderboard = new ArrayList<Map<String, Long>>();
		String whereClause = " WHERE GAME_DATE BETWEEN TO_DATE(?, 'YYYYMMDD') AND TO_DATE(?, 'YYYYMMDD') ";
		boolean check = start != null && !start.equals("") && end != null && !end.equals("");

		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNTRY, COUNT(*) WINNERCOUNT FROM ");
		query.append("(SELECT COUNTRY, WINNER FROM PLAYERS P LEFT OUTER JOIN ");
		query.append("(SELECT WINNER, LOSER, GAME_DATE, GAME_TYPE FROM GAMES ");
		if (check) {
			query.append(whereClause);
		} else {
			query.append("");
		}
		query.append(") G ");
		query.append("ON (P.PLAYER_ID = G.WINNER) ) GROUP BY COUNTRY ORDER BY COUNT(*) DESC, COUNTRY ASC");
		PreparedStatementCreator psc = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(query.toString());
				if (check) {
					ps.setString(1, start);
					ps.setString(2, end);
				}
				return ps;
			}
		};

		leaderboard = jdbcTemplate.query(psc, new NationalLeaderBoardMapper());
		return leaderboard;
	}

	@Override
	public void save(Game game) {
		// TODO Auto-generated method stub
		System.out.println("Saving Game of :" + game.getType() + " with Winner : " + game.getWinner().getUserId() + "("
				+ game.getWinner().getCountry() + ")" + game.getLoser().getUserId() + "(" + game.getLoser().getCountry()
				+ ") played on:" + game.getGameDate());
		// Save Winner
		Player winner = new Player(game.getWinner().getUserId(), game.getWinner().getCountry());
		winner = savePlayer(winner);
		// Save Loser
		Player loser = new Player(game.getLoser().getUserId(), game.getLoser().getCountry());
		loser = savePlayer(loser);

		StringBuilder query = new StringBuilder("Insert into Game(Winner, Loser, GameType,GameDate) values(?,?,?,?)");
		jdbcTemplate.update(query.toString(), winner.getPlayerID(), loser.getPlayerID(), game.getType().name(),
				new Date(game.getGameDate()));
	}

	@Override
	public boolean dateValidation(String startDate, String endDate) throws InvalidDateException {
		if ((startDate == null && endDate != null) || (startDate != null && endDate == null)) {
			throw new InvalidDateException("Either both dates are null or should be filled");
		} else if (startDate == null && endDate == null) {
			return true;
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			sdf.setLenient(false);
			Date sdate = null;
			Date edate = null;
			try {
				sdate = sdf.parse(startDate);
				System.out.println(sdate);
			} catch (ParseException e) {
				e.printStackTrace();
				throw new InvalidDateException("Start Date should be filled in " + dateFormat);
			}

			try {
				edate = sdf.parse(endDate);
				System.out.println(edate);
			} catch (ParseException e) {
				e.printStackTrace();
				throw new InvalidDateException("End Date should be filled in " + dateFormat);
			}
			if (sdate.after(edate) || sdate.equals(edate)) {
				throw new InvalidDateException("End Date should be greater than  Start Date");
			}
		}
		return true;
	}

	@Override
	public Player savePlayer(Player player) {
		long playerId = 0;
		try {
			playerId = jdbcTemplate.queryForObject("SELECT PlayerId FROM Players WHERE user ='" + player.getUser()
					+ "' and country='" + player.getCountry() + "'", Long.class);
		} catch (Exception e) {
			StringBuilder query = new StringBuilder("Insert into player(User, Country) values(?,?)");
			jdbcTemplate.update(query.toString(), player.getUser(), player.getCountry());

			playerId = jdbcTemplate.queryForObject("SELECT PlayerId FROM Players WHERE user ='" + player.getUser()
					+ "' and country='" + player.getCountry() + "'", Long.class);
		}
		return new Player(player.getCountry(), player.getUser(), playerId);
	}

	private static final class LeaderBoardMapper implements RowMapper<Map<String, Long>> {
		public Map<String, Long> mapRow(ResultSet rs, int rowNum) throws SQLException {
			Map<String, Long> leader = new HashMap<String, Long>();
			leader.put(rs.getString("USER_ID"), rs.getLong("WINNERCOUNT"));
			return leader;
		}
	}

	private static final class NationalLeaderBoardMapper implements RowMapper<Map<String, Long>> {
		public Map<String, Long> mapRow(ResultSet rs, int rowNum) throws SQLException {
			Map<String, Long> leader = new HashMap<String, Long>();
			leader.put(rs.getString("COUNTRY"), rs.getLong("WINNERCOUNT"));
			return leader;
		}
	}

}

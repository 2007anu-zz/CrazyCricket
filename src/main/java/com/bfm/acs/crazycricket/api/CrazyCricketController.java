package com.bfm.acs.crazycricket.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bfm.acs.crazycricket.exception.InvalidDateException;
import com.bfm.acs.crazycricket.dao.DataStorage;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
public class CrazyCricketController {
	@Autowired
	DataStorage datasource;

	@RequestMapping(value = "/leaderboard")
	public List<Map<String, Long>> getLeaderBoard(@RequestParam(value = "start", required = false) String start,
			@RequestParam(value = "end", required = false) String end) throws InvalidDateException {
		datasource.dateValidation(start, end);
		return datasource.getLeaderBoard(start, end);
	}

	@RequestMapping(value = "/national_leaderboard")
	public List<Map<String, Long>> getNationalLeaderboard(@RequestParam(value = "start", required = false) String start,
			@RequestParam(value = "end", required = false) String end) throws InvalidDateException {
		datasource.dateValidation(start, end);
		return datasource.getNationalLeaderboard(start, end);

	}
}

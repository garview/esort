package worm.esort.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;
import worm.esort.service.EWormThreadService;
import worm.esort.service.ScoreService;

@RestController
public class ESortController {
	public static final Logger logger = LogManager.getLogger();

	@Autowired
	EWormThreadService eWormThreadService;
	
	@Autowired
	ScoreService scoreService;
	
	

	// @RequestMapping("/getBooks")
	// @ResponseBody
	@ApiOperation(value = "获取book列表", notes = "获取已经爬取的book信息")
	@RequestMapping(value = { "/getBooks" }, method = RequestMethod.GET)
	public String getBooks() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(scoreService.getBookScores());
	}
	@ApiOperation(value = "pauseWorms", notes = "暂停所有爬虫")
	@RequestMapping(value = { "/pauseWorms" }, method = RequestMethod.GET)
	public ResponseEntity<String> pauseWorm() throws JsonProcessingException {
		eWormThreadService.pauseAllWormThread();
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}
	
	@ApiOperation(value = "notifyWorms", notes = "唤醒所有爬虫")
	@RequestMapping(value = { "/notifyWorms" }, method = RequestMethod.GET)
	public ResponseEntity<String> notifyWorms() throws JsonProcessingException {
		eWormThreadService.restartAllWormThread();
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}
	
	@ApiOperation(value = "爬取book信息", notes = "发出爬取指令，慎用")
	@RequestMapping(value = { "/collectBooks" }, method = RequestMethod.GET)
	public ResponseEntity<String> collectBooks() throws IOException {
		eWormThreadService.collectBooks();
		return new ResponseEntity<String>(HttpStatus.ACCEPTED);
	}
	
	@RequestMapping(value = { "/rateBook" }, method = RequestMethod.PUT)
	public ResponseEntity<String> rateBook(@RequestParam long bookId,@RequestParam float score) throws JsonProcessingException {
		scoreService.giveMark(1L, bookId, score);
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(value = { "/test" }, method = RequestMethod.GET)
	public ResponseEntity<String> test(HttpServletRequest request) throws JsonProcessingException {
//		scoreService.giveMark(1L, bookId, score);
		logger.info("request.getRemoteAddr:{}",request.getRemoteAddr());
		logger.info("request.getHeader(x-forwarded-for):{}",request.getHeader("x-forwarded-for"));
		logger.info("request.getHeader(WL-Proxy-Client-IP):{}",request.getHeader("WL-Proxy-Client-IP"));
		logger.info("request.getRemoteHost:{}",request.getRemoteHost());
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}
}

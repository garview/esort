package worm.esort.controller;

import java.io.IOException;

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
import worm.esort.repository.BookRepository;
import worm.esort.service.EWormThreadService;
import worm.esort.service.ScoreService;

@RestController
public class ESortController {

	@Autowired
	BookRepository bookResp;

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
		return mapper.writeValueAsString(bookResp.findByAvailable(true));
	}
	
	@ApiOperation(value = "爬取book信息", notes = "发出爬取指令，慎用")
	@RequestMapping(value = { "/collectBooks" }, method = RequestMethod.GET)
	public ResponseEntity<String> collectBooks() throws IOException {
		eWormThreadService.collectBooks();
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(value = { "/rateBook" }, method = RequestMethod.PUT)
	public ResponseEntity<String> rateBook(@RequestParam long bookId,@RequestParam float score) throws JsonProcessingException {
		scoreService.giveMark(1L, bookId, score);
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}
}

package worm.esort.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;
import worm.esort.repository.BookRepository;
import worm.esort.service.EWormThreadService;

@RestController
public class ESortController {

	@Autowired
	BookRepository bookResp;

	@Autowired
	EWormThreadService eWormThreadService;

	// @RequestMapping("/getBooks")
	// @ResponseBody
	@ApiOperation(value = "获取用户列表", notes = "")
	@RequestMapping(value = { "/getBooks" }, method = RequestMethod.GET)
	public String getBooks() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(bookResp.findByAvailable(true));
	}

	@RequestMapping(value = { "/collectBooks" }, method = RequestMethod.GET)
	public ResponseEntity<String> collectBooks() throws IOException {
		eWormThreadService.collectBooks();
		return new ResponseEntity<String>("", HttpStatus.OK);
	}
}

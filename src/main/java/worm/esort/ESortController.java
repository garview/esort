package worm.esort;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import worm.esort.repository.BookRepository;
import worm.esort.service.EWormIndexService;
import worm.esort.service.EWormThreadService;

@Controller
public class ESortController {
	
	@Autowired
	BookRepository bookResp;
	
	@Autowired 
	EWormThreadService eWormThreadService;

	@RequestMapping("/getBooks")
	@ResponseBody
	public String getBooks() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(bookResp.findByAvailable(true));
	}
	
	@RequestMapping("/collectBooks")
	@ResponseBody
	public ResponseEntity<String> collectBooks() throws IOException {
		eWormThreadService.collectBooks();
		return new ResponseEntity<String>("",HttpStatus.OK);
	}
}

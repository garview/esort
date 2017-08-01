package worm.esort.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;
import worm.esort.repository.UserRepository;

@RestController
public class UserController {

	@Autowired
	UserRepository userRepository;

	@ApiOperation(value = "获取用户列表", notes = "")
	@RequestMapping(value = { "/getUsers" }, method = RequestMethod.GET)
	public String getUsers() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(userRepository.findAll());
	}
	
}

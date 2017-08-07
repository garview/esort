package worm.esort.controller;

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
import worm.esort.repository.UserRepository;
import worm.esort.service.UserService;

@RestController
public class UserController {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserService userService;

	@ApiOperation(value = "获取用户列表", notes = "")
	@RequestMapping(value = { "/getUsers" }, method = RequestMethod.GET)
	public String getUsers() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(userRepository.findAll());
	}
	@ApiOperation(value = "创建用户--测试", notes = "用于测试幻读和死锁问题，时间单位（秒）")
	@RequestMapping(value = { "/createUserTest" }, method = RequestMethod.POST)
	public ResponseEntity<String> createUserTest(@RequestParam String username,@RequestParam(required=false) Integer sleepTime) {
		userService.createUser(username, sleepTime);
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}
	
	@ApiOperation(value = "删除用户", notes = "")
	@RequestMapping(value = { "/deleteUser" }, method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteUser(@RequestParam String username) {
		userService.deleteUser(username);
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	} 
}

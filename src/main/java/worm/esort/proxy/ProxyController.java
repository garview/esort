package worm.esort.proxy;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@Api(value = "proxy", description = "代理服务")  
@RestController
public class ProxyController {
	@Autowired
	ProxyService proxyService;
	
	@ApiOperation(value = "发出补充ip代理池的指令", notes = "")
	@RequestMapping(value = { "/supplyProxies" }, method = RequestMethod.GET)
	public ResponseEntity<String> supplyProxies() throws IOException {
		proxyService.supplyProxyData();
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}
	@ApiOperation(value = "获取ip代理", notes = "")
	@RequestMapping(value = { "/getProxies" }, method = RequestMethod.GET)
	public ResponseEntity<String> getProxies() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return new ResponseEntity<String>(mapper.writeValueAsString(proxyService.findProxies()),HttpStatus.OK);
	}
}

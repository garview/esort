package worm.esort.proxy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProxyPool {
	
	@Autowired
	ProxyInfoRepository proxyResp;
	
	private List<ProxyInfo> pool ;
	private int current;
	
	public synchronized ProxyInfo getProxy(){
		if(pool==null)
			initPool();
		if(current==pool.size())
			current=0;
		return pool.get(current++);
	}
	
	public void initPool() {
		this.current = 0;
		this.pool = proxyResp.findByStatusLessThan(ProxyInfo.WORST);
	}
}

package worm.esort.proxy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProxyPool {
	
	@Autowired
	ProxyInfoRepository proxyResp;
	
	private List<Proxyinfo> pool ;
	private int current;
	
	public synchronized Proxyinfo getProxy(){
		if(pool==null)
			initPool();
		if(current==pool.size())
			current=0;
		return pool.get(current++);
	}
	
	private void initPool() {
		this.current = 0;
		this.pool = proxyResp.findByStatusLessThan(Proxyinfo.WORST);
	}
}

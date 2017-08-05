package worm.esort.proxy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProxyService {
	public static final Logger logger = LogManager.getLogger();
	@Autowired
	ProxyInfoRepository proxyResp;
	
	@Autowired
	private ApplicationContext ctx;
	
	public Iterable<ProxyInfo> findProxies(){
		return proxyResp.findAll();
	}
	
	/**
	 * 补充代理
	 * @throws IOException
	 */
	public void supplyProxyData() throws IOException{
		List<ProxyInfo> proxies = new ArrayList<>();
		String url = "http://www.xicidaili.com/nt";
		Document doc = Jsoup.connect(url).get();
		Elements list = doc.select("#ip_list tr");
		logger.info("从{}获取到{}个代理地址",url,list.size()-1);
		for(int i=1; i<list.size(); i++){
			Element row = list.get(i);
			Elements tds = row.select("td");
			ProxyInfo p = new ProxyInfo();
			p.setIp(tds.get(1).html());
			p.setPort(tds.get(2).html());
			ProxyInfo temp = proxyResp.findProxyInfoByIpAndPort(p.getIp(), p.getPort());
			if(temp!=null)
				p = temp;
			p.setServerLocation(tds.get(3).select("a").html());
			p.setConnType(tds.get(5).html());
			proxies.add(p);
		}
		
		checkProxies(proxies);
	}

	private void checkProxies(List<ProxyInfo> proxies) {
		proxies.stream().forEach(proxy->{
			Thread t = (Thread) ctx.getBean("checkProxyThread",proxy);
			t.start();
		});
	}

}

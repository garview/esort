package worm.esort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import worm.esort.proxy.ProxyInfo;



public class IPProxyTest {
	private static final Logger logger = LogManager.getLogger();
//	@Test
	public void testIP() throws IOException{
		List<ProxyInfo> proxies = new ArrayList<>();
		String url = "http://www.xicidaili.com/nt";
		Document doc = Jsoup.connect(url).get();
//		System.out.println(doc.toString());
		Elements list = doc.select("#ip_list tr");
		for(int i=1; i<list.size(); i++){
			Element row = list.get(i);
			Elements tds = row.select("td");
			ProxyInfo p = new ProxyInfo();
			p.setIp(tds.get(1).html());
			p.setPort(tds.get(2).html());
			p.setServerLocation(tds.get(3).select("a").html());
			p.setConnType(tds.get(5).html());
			proxies.add(p);
		}
		testProxy(proxies);
	}
	
	public void testProxy(List<ProxyInfo> proxies){
		System.setProperty("proxySet", "true");
		proxies.stream().forEach(proxy->{
			 System.setProperty("http.proxyHost", proxy.getIp());
			 System.setProperty("http.proxyPort", proxy.getPort());
			 try {
				 long t1 = System.currentTimeMillis();
				Document doc = Jsoup.connect("http://www.baidu.com").get();
				long t2 = System.currentTimeMillis();
				logger.info("ip:{},port:{},耗时：{}秒",proxy.getIp(),proxy.getPort(),(t2-t1)/1000f);
				proxy.setConnectSeconds((t2-t1)/1000f);
			} catch (IOException e) {
				logger.error("连接不可用",e);
				proxy.setDisabled();
			}
		});
	}
	
	@Test
	public void testProxy() throws IOException{
		System.setProperty("proxySet", "true");
		 System.setProperty("http.proxyHost", "222.52.142.242");
		 System.setProperty("http.proxyPort", "8080");
		 long t1 = System.currentTimeMillis();
			Document doc = Jsoup.connect("http://www.baidu.com").get();
			long t2 = System.currentTimeMillis();
			System.out.println((t2-t1)/1000f+"秒");
	}
}
package worm.esort;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import worm.esort.proxy.Proxyinfo;



public class IPProxyTest {
	private static final Logger logger = LogManager.getLogger();
//	@Test
	public void testIP() throws IOException{
		List<Proxyinfo> proxies = new ArrayList<>();
		String url = "http://www.xicidaili.com/nt";
		Document doc = Jsoup.connect(url).get();
//		System.out.println(doc.toString());
		Elements list = doc.select("#ip_list tr");
		for(int i=1; i<list.size(); i++){
			Element row = list.get(i);
			Elements tds = row.select("td");
			Proxyinfo p = new Proxyinfo();
			p.setIp(tds.get(1).html());
			p.setPort(tds.get(2).html());
			p.setServerLocation(tds.get(3).select("a").html());
			p.setConnType(tds.get(5).html());
			proxies.add(p);
		}
		testProxy(proxies);
	}
	
	public void testProxy(List<Proxyinfo> proxies){
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
	
//	@Test
	public void testProxy() throws IOException{
		System.setProperty("proxySet", "true");
		 System.setProperty("http.proxyHost", "222.52.142.242");
		 System.setProperty("http.proxyPort", "8080");
		 long t1 = System.currentTimeMillis();
			Document doc = Jsoup.connect("http://www.baidu.com").get();
			long t2 = System.currentTimeMillis();
			System.out.println((t2-t1)/1000f+"秒");
	}
	
//	@Test
	public void testAnoIP() throws IOException{
		System.setProperty("proxySet", "true");
		 System.setProperty("http.proxyHost", "60.255.186.169");
		 System.setProperty("http.proxyPort", "8888");
		 long t1 = System.currentTimeMillis();
			Document doc = Jsoup.connect("http://52.221.203.10:8081/test").get();
			long t2 = System.currentTimeMillis();
			System.out.println((t2-t1)/1000f+"秒");
	}
	@Test
	public void testTimeout() throws IOException {
//		System.setProperty("proxySet", "true");
//		 System.setProperty("https.proxyHost", "110.243.166.164");
//		 System.setProperty("https.proxyPort", "80");
//		 System.setProperty("http.proxyHost", "110.243.166.164");
//		 System.setProperty("http.proxyPort", "80");
//		 Proxy p = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("110.243.166.164", 80));
		 long t1 = System.currentTimeMillis();
			Document doc = Jsoup.connect("https://www.baidu.com/s?ie=UTF-8&wd=ip")/*.proxy(p)*/.get();
			long t2 = System.currentTimeMillis();
			System.out.println(doc);
			System.out.println((t2-t1)/1000f+"秒");
	}
}

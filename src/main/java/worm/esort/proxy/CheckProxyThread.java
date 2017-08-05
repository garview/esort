package worm.esort.proxy;

import java.io.IOException;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class CheckProxyThread extends Thread {

	public static final Logger logger = LogManager.getLogger();
	@Autowired
	ProxyInfoRepository proxyResp;

	private ProxyInfo proxy;

	public CheckProxyThread(ProxyInfo proxy) {
		super();
		this.proxy = proxy;
	}

	public ProxyInfo getProxy() {
		return proxy;
	}

	public void setProxy(ProxyInfo proxy) {
		this.proxy = proxy;
	}

	@Override
	public void run() {
		System.setProperty("proxySet", "true");
		System.setProperty("http.proxyHost", proxy.getIp());
		System.setProperty("http.proxyPort", proxy.getPort());
		String testUrl = "http://www.baidu.com";
		try {
			long t1 = System.currentTimeMillis();
			Document doc = Jsoup.connect(testUrl).get();
			long t2 = System.currentTimeMillis();
			logger.info("ip:{},port:{},连接{}耗时：{}秒", proxy.getIp(), proxy.getPort(),testUrl, (t2 - t1) / 1000f);
			proxy.setConnectSeconds((t2 - t1) / 1000f);
		} catch (IOException e) {
			proxy.setDisabled();
		} finally {
			proxy.setCheckTime(new Timestamp(System.currentTimeMillis()));
			proxyResp.save(proxy);
		}
	}

}

package worm.esort.thread;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import worm.esort.App;
import worm.esort.proxy.ProxyInfo;
import worm.esort.proxy.ProxyPool;
import worm.esort.service.EWormIndexService;

@Component
@Scope("prototype")
public class WormThread extends Thread {
	private static final Logger logger = LogManager.getLogger();
	private String url;
	private int pageCount;

	@Autowired
	EWormIndexService eWormIndexService;

	@Autowired
	ProxyPool pool;

	public WormThread(String searchResultUrl) {
		super();
		this.url = searchResultUrl;
	}

	public WormThread(String searchResultUrl, int pageCount) {
		super();
		String pageUrl = new String(searchResultUrl + "&page=" + pageCount); // i=1开始，表示从第二页开始循环
		this.pageCount = pageCount;
		this.url = pageUrl;
	}

	@Override
	public void run() {
		float costTime = 0;
		try{
			ProxyInfo proxy = pool.getProxy();
			System.setProperty("proxySet", "true");
			System.setProperty("http.proxyHost", proxy.getIp());
			System.setProperty("http.proxyPort", proxy.getPort());
			logger.info("线程：{} 开始处理第{}页,代理地址：{}:{}——————————", this.getName(), this.pageCount + 1, proxy.getIp(), proxy.getPort());
			costTime = eWormIndexService.crawlListPage(url);
			App.result.expectRemainTime(costTime);
		} catch (IOException e) {
			logger.error("连接异常：" + url, e);
		}catch(Exception e){
			logger.error(e);
		}finally {
			synchronized (App.result) {
				App.result.addHandledPage(pageCount + 1);
			}
		}
	}

}

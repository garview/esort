package worm.esort.service;

import java.io.IOException;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import worm.esort.App;
import worm.esort.repository.BookRepository;
import worm.esort.thread.WormLock;
import worm.esort.thread.WormThread;

/**
 * e站索引爬虫服务，爬取书籍的基本信息。
 * 
 * @author garview
 *
 */
@Service
@Transactional
public class EWormThreadService {

	private static final Logger logger = LogManager.getLogger();

	@Autowired
	BookRepository bookResp;

	@Resource(name = "taskExecutor")
	ThreadPoolTaskExecutor taskExecutor;
	@Autowired
	private ApplicationContext ctx;
	/**
	 * 用于控制爬虫进程的暂停、继续
	 */
	WormLock wormLock = new WormLock();

	public void collectBooks() throws IOException {
		String searchResultUrl = "https://e-hentai.org/?f_doujinshi=1&f_manga=1&f_artistcg=1&f_gamecg=1&f_western=1&f_non-h=1&f_imageset=1&f_cosplay=1&f_asianporn=1&f_misc=1&f_search=chinese&f_apply=Apply+Filter";
		Document page1 = Jsoup.connect(searchResultUrl).get();
		Elements pageLinks = page1.select("table.ptb td[onclick] > a"); // 获取所有页码链接
		int totalPageCount = new Integer(pageLinks.get(pageLinks.size() - 2).html());// 总页数
		App.result.setTotalPageCount(totalPageCount);
		// crawlListPage(page1);
		taskExecutor.execute((WormThread) ctx.getBean("wormThread", searchResultUrl,wormLock));// 由于url规则与其余页不一样，所以第一页在循环外处理
		for (int i = 1; i <= totalPageCount - 1; i++) {
			taskExecutor.execute((WormThread) ctx.getBean("wormThread", searchResultUrl, i,wormLock));
		}
		taskExecutor.shutdown();
	}
	
	public void pauseAllWormThread(){
		wormLock.setShouldPause(true);
	}

	public void restartAllWormThread(){
		synchronized(wormLock){
			wormLock.setShouldPause(false);
			wormLock.notifyAll();
		}
	}
}

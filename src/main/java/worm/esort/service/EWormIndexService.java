package worm.esort.service;

import java.io.IOException;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

/**
 * e站索引爬虫服务，爬取书籍的基本信息。
 * @author garview
 *
 */
@Service("eWormIndexService")
@Transactional
public class EWormIndexService {

	private static final Logger logger = LogManager.getLogger(EWormIndexService.class);
	
	/**
	 * 对查询结果所有列表进行爬取
	 * @param searchResultUrl
	 * @throws IOException
	 */
	public void crawlSearchResult(String searchResultUrl) throws IOException{
		long t1 = System.currentTimeMillis();
		Document page1 = Jsoup.connect(searchResultUrl).get();
		crawlListPage(page1);// 先处理第一页
		Elements pageLinks = page1.select("table.ptb td[onclick] > a"); //获取所有页码链接
		int pageCount = new Integer(pageLinks.get(pageLinks.size()-2).html());// 总页数
		for(int i=1; i<=pageCount-1; i++) { 
			String pageUrl = new String(searchResultUrl +  "&page=" + i);  // i=1开始，表示从第二页开始循环
			crawlListPage(pageUrl);
		}
		long t2 = System.currentTimeMillis();
		logger.info("总耗时："+(t2-t1)/1000+"秒");
	}
	/**
	 * 爬取查询列表某页的信息
	 * @param url
	 * @throws IOException 
	 */
	public void crawlListPage(String url) throws IOException{
		long t1 = System.currentTimeMillis();
		Document doc = Jsoup.connect(url).get();
		crawlListPage(doc);
		long t2 = System.currentTimeMillis();
		logger.info("一页耗时："+(t2-t1)/1000+"秒"); //平均处理1页30s左右
	}
	public void crawlListPage(Document doc) throws IOException{
		Elements links = doc.select("a[onmouseover]"); 
		for(Element link : links){
			logger.info("开始处理："+link.html());
			crawlBook(link.attr("href"));
		}
	}
	/**
	 * 爬取某个本子的信息
	 * @param url
	 */
	public void crawlBook(String url) {
		logger.debug("===========================");
		long t1 = System.currentTimeMillis();
//		String url2 = "https://e-hentai.org/g/1085249/53796714f7/";
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			logger.error("该本子的链接失效了吧", e);
			return ;
		}
		logger.debug("评分人数:"+doc.select("#rating_count").html());// 评分人数
		logger.debug("平均评分:"+doc.select("#rating_label").html());// 平均评分
		logger.debug("番号:"+doc.select("#gn").html());// 番号
		Elements values = doc.select("#gdd .gdt2");
		logger.debug("录入时间:"+values.get(0).html());
		logger.debug("大小:"+values.get(4).html());
		logger.debug("总页数:"+values.get(5).html());
		logger.debug("收藏数:"+values.get(6).html());
		long t2 = System.currentTimeMillis();
		logger.debug("耗时："+(t2-t1)/1000+"秒");
		logger.debug("===========================");
	}
}

package worm.esort.service;

import java.io.IOException;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import worm.esort.domain.Book;
import worm.esort.repository.BookRepository;

/**
 * e站索引爬虫服务，爬取书籍的基本信息。
 * @author garview
 *
 */
@Service("eWormIndexService")
@Transactional
public class EWormIndexService {

	private static final Logger logger = LogManager.getLogger(EWormIndexService.class);
	
	@Autowired 
	BookRepository bookResp;
	
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
			Book book = new Book();
			book.setName(link.html());// 先传入本子名字信息，防止由于链接失效导致的数据记录丢失
			crawlBook(link.attr("href"),book);
		}
	}
	/**
	 * 爬取某个本子的信息
	 * @param url
	 */
	public void crawlBook(String url, Book book) {
		logger.debug("===========================");
		long t1 = System.currentTimeMillis();
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			logger.error(book.getName()+" 的链接似乎失效了："+url, e);
			bookResp.save(book);
		} 
		book.setRatingCount(str2Integer(doc.select("#rating_count").html()));
		logger.debug("评分人数:"+doc.select("#rating_count").html());// 评分人数
		book.setAverageRating(str2Float(doc.select("#rating_label").html()));
		logger.debug("平均评分:"+doc.select("#rating_label").html());// 平均评分
		book.setName(doc.select("#gn").html());
		logger.debug("番号:"+doc.select("#gn").html());// 番号
		Elements values = doc.select("#gdd .gdt2");
		logger.debug("录入时间:"+values.get(0).html());
		book.setFileSize(values.get(4).html());
		logger.debug("大小:"+values.get(4).html());
		book.setLength(str2Integer(values.get(5).html()));
		logger.debug("总页数:"+values.get(5).html());
		book.setFavourited(str2Integer(values.get(6).html()));
		logger.debug("收藏数:"+values.get(6).html());
		long t2 = System.currentTimeMillis();
		logger.debug("耗时："+(t2-t1)/1000+"秒");
		logger.debug("===========================");
		bookResp.save(book);
	}
	
	private Integer str2Integer(String str){
		String intStr = str.replaceAll("\\D", "");
		if(intStr.length()==0)
			return 0;
		return new Integer(intStr);
	}
	private Float str2Float(String str){
		String intStr = str.replaceAll("([a-z]*)([A-Z]*)(\\:*)( *)", ""); // 去除字母、冒号、空格
		if(intStr.length()==0)
			return 0f;
		return new Float(intStr);
	}
	
}

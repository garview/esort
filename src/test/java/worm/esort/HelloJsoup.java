package worm.esort;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class HelloJsoup {
	
	private static final Logger logger = LogManager.getLogger(HelloJsoup.class);

	/**
	 * 基本思路：
	 * 1. 通过url获取查询结果
	 * 2. 处理当前查询结果页（第一页）
	 * 3. 通过当前页信息得到总页数
	 * 4. 通过总页数循环拼接从第二页开始，每一页的链接
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		long t1 = System.currentTimeMillis();
		String originUrl = "https://e-hentai.org/?f_doujinshi=1&f_manga=1&f_artistcg=1&f_gamecg=1&f_western=1&f_non-h=1&f_imageset=1&f_cosplay=1&f_asianporn=1&f_misc=1&f_search=chinese&f_apply=Apply+Filter";
		Document page1 = Jsoup.connect(originUrl).get();
		handlePage(page1);// 先处理第一页
		Elements pageLinks = page1.select("table.ptb td[onclick] > a"); //获取所有页码链接
		int pageCount = new Integer(pageLinks.get(pageLinks.size()-2).html());// 总页数
		for(int i=1; i<=pageCount-1; i++) { 
			String url = new String(originUrl +  "&page=" + i);  // i=1开始，表示从第二页开始循环
			Document page = Jsoup.connect(url).get();
			handlePage(page);
		}
//		getRecordInfo("https://e-hentai.org/g/1085017/9516cdd930/"); 404的处理
		long t2 = System.currentTimeMillis();
		logger.info("总耗时："+(t2-t1)/1000+"秒");
	}
	static void handlePage(Document doc){
		long t1 = System.currentTimeMillis();
		Elements links = doc.select("a[onmouseover]"); 
		for(Element link : links){
			logger.info("开始处理："+link.html());
			getRecordInfo(link.attr("href"));
		}
		long t2 = System.currentTimeMillis();
		logger.info("一页耗时："+(t2-t1)/1000+"秒"); //平均处理1页30s左右
	}
	
	static void getRecordInfo(String url) {
		logger.trace("===========================");
		long t1 = System.currentTimeMillis();
//		String url2 = "https://e-hentai.org/g/1085249/53796714f7/";
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			logger.error("应该是404链接失效了吧", e);
			return ;
		}
		logger.trace("评分人数:"+doc.select("#rating_count").html());// 评分人数
		logger.trace("平均评分:"+doc.select("#rating_label").html());// 平均评分
		logger.trace("番号:"+doc.select("#gn").html());// 番号
		Elements values = doc.select("#gdd .gdt2");
		logger.trace("录入时间:"+values.get(0).html());
		logger.trace("大小:"+values.get(4).html());
		logger.trace("总页数:"+values.get(5).html());
		logger.trace("收藏数:"+values.get(6).html());
		long t2 = System.currentTimeMillis();
		logger.trace("耗时："+(t2-t1)/1000+"秒");
		logger.trace("===========================");
	}
}

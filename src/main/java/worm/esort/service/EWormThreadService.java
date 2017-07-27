package worm.esort.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import worm.esort.App;
import worm.esort.domain.Book;
import worm.esort.repository.BookRepository;
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
	
	@Resource(name="taskExecutor")
	ThreadPoolTaskExecutor taskExecutor;
	@Autowired
	private ApplicationContext ctx;

	
	@Value("${esort.force-grap:false}")
    private Boolean forceGrap;
	
	public void collectBooks() throws IOException {
		if(forceGrap)
			bookResp.deleteAll();
		long t1 = System.currentTimeMillis();
		String searchResultUrl = "https://e-hentai.org/?f_doujinshi=1&f_manga=1&f_artistcg=1&f_gamecg=1&f_western=1&f_non-h=1&f_imageset=1&f_cosplay=1&f_asianporn=1&f_misc=1&f_search=chinese&f_apply=Apply+Filter";
		Document page1 = Jsoup.connect(searchResultUrl).get();
		Elements pageLinks = page1.select("table.ptb td[onclick] > a"); // 获取所有页码链接
		int totalPageCount = new Integer(pageLinks.get(pageLinks.size() - 2).html());// 总页数
		App.result.setTotalPageCount(totalPageCount);
		// crawlListPage(page1);
		taskExecutor.execute((WormThread) ctx.getBean("wormThread", searchResultUrl));// 由于url规则与其余页不一样，所以第一页在循环外处理
		for (int i = 1; i <= totalPageCount - 1; i++) {
			taskExecutor.execute((WormThread) ctx.getBean("wormThread", searchResultUrl, i));
		}
		long t2 = System.currentTimeMillis();
		logger.info("总耗时：" + (t2 - t1) / 1000 + "秒");
		taskExecutor.shutdown();
	}

}

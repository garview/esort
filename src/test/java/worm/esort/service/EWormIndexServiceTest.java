package worm.esort.service;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import worm.esort.domain.Book;
import worm.esort.repository.BookRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional // 添加事务管理，在测试中自动回滚操作（不加此注解就会自动提交事务）
public class EWormIndexServiceTest {

	private static final Logger logger = LogManager.getLogger(EWormIndexServiceTest.class);

	@Resource(name = "eWormIndexService")
	EWormIndexService eWormIndexService;
	@Autowired
	BookRepository bookResp;
//	@Test
	public void testCrawlSearchResult() throws IOException {
		String originUrl = "https://e-hentai.org/?f_doujinshi=1&f_manga=1&f_artistcg=1&f_gamecg=1&f_western=1&f_non-h=1&f_imageset=1&f_cosplay=1&f_asianporn=1&f_misc=1&f_search=chinese&f_apply=Apply+Filter";
		eWormIndexService.crawlSearchResult(originUrl);
	}

	
//	 @Test
	 public void testCrawlListPage() {
		 try {
			eWormIndexService.crawlListPage("https://e-hentai.org/?f_doujinshi=1&f_manga=1&f_artistcg=1&f_gamecg=1&f_western=1&f_non-h=1&f_imageset=1&f_cosplay=1&f_asianporn=1&f_misc=1&f_search=chinese&f_apply=Apply+Filter");
		} catch (IOException e) {
			logger.error(e);
		}
	 }
	
	@Test
	@Rollback(false)
	public void testCrawlBook() throws IOException {
		String url = "https://e-hentai.org/g/1046588/81fdb4ddab/";
		Document doc = Jsoup.connect(url).get();
		String bookName = doc.select("#gn").html();
		Book b = bookResp.findBookByName(bookName);
		if(b==null)
			b=new Book();
		eWormIndexService.crawlBook(url, b);
		logger.debug(b);
	}
//	@Test
	public void testCrawlOffensiveBook() throws IOException {
		String url = "https://e-hentai.org/g/1085933/4d70973893/?nw=session";
		Document doc = Jsoup.connect(url).get();
		String bookName = doc.select("#gn").html();
		Book b = bookResp.findBookByName(bookName);
		if(b==null)
			b=new Book();
		eWormIndexService.crawlBook(url, b);
		logger.debug(b);
	}
	
//	@Test 
	public void testPrint2Excel() {
		File outFolder = new File("out");
		if(!outFolder.exists())
			outFolder.mkdirs();
		File file = new File(outFolder,"esort-{date}.xlsx".replace("{date}", new DateTime().toString("yyyyMMdd")));
		eWormIndexService.print2Excel(file);
	}

}

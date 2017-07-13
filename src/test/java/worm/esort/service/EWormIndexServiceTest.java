package worm.esort.service;

import java.io.IOException;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import worm.esort.domain.Book;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional // 添加事务管理，在测试中自动回滚操作（不加此注解就会自动提交事务）
public class EWormIndexServiceTest {

	@Resource(name="eWormIndexService")
	EWormIndexService eWormIndexService; 
//	@Test
	public void testCrawlSearchResult() throws IOException {
		String originUrl = "https://e-hentai.org/?f_doujinshi=1&f_manga=1&f_artistcg=1&f_gamecg=1&f_western=1&f_non-h=1&f_imageset=1&f_cosplay=1&f_asianporn=1&f_misc=1&f_search=chinese&f_apply=Apply+Filter";
		eWormIndexService.crawlSearchResult(originUrl);;
	}
//
//	@Test
//	public void testCrawlListPageString() {
//		fail("Not yet implemented");
//	}
//

	@Test
	public void testCrawlBook() {
		Book b = new Book();
		eWormIndexService.crawlBook("https://e-hentai.org/g/1086305/4d5acc0a41/",b);
		System.out.println(b);
	}

}
package worm.esort;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import worm.esort.thread.WormThread;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App 
{
	private static final Logger logger = LogManager.getLogger();

	public static void main(String[] args) throws InterruptedException, IOException  {
//		ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
		ApplicationContext ctx = SpringApplication.run(App.class,args);
		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) ctx.getBean("taskExecutor");
		
		long t1 = System.currentTimeMillis();
		String searchResultUrl = "https://e-hentai.org/?f_doujinshi=1&f_manga=1&f_artistcg=1&f_gamecg=1&f_western=1&f_non-h=1&f_imageset=1&f_cosplay=1&f_asianporn=1&f_misc=1&f_search=chinese&f_apply=Apply+Filter";
		taskExecutor.execute((WormThread)ctx.getBean("wormThread",searchResultUrl));
		Document page1 = Jsoup.connect(searchResultUrl).get();
//		crawlListPage(page1);// 由于url规则与其余页不一样，所以第一页在循环外处理
		Elements pageLinks = page1.select("table.ptb td[onclick] > a"); //获取所有页码链接
		int pageCount = new Integer(pageLinks.get(pageLinks.size()-2).html());// 总页数
		for(int i=1; i<=pageCount-1; i++) { 
			Thread.sleep(1000);
			taskExecutor.execute((WormThread)ctx.getBean("wormThread",searchResultUrl,i));
//			logger.info("当前进度{}/{}, 预计剩余{}秒",i+1,pageCount,costTime*(pageCount-1-i));
		}
		long t2 = System.currentTimeMillis();
		logger.info("总耗时："+(t2-t1)/1000+"秒");
//		taskExecutor.shutdown();
		boolean shouldShutdown = false;
		while(!shouldShutdown){
			Thread.sleep(3000);
			if(taskExecutor.getActiveCount()==0){
				taskExecutor.shutdown();
				shouldShutdown = true;
			}
		}
		
	}
	
	@Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
//        executor.setQueueCapacity(1);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }
}

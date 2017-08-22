package worm.esort;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App 
{
	
	public static final HandleResult result = new HandleResult();

	public static void main(String[] args) throws InterruptedException, IOException  {
		SpringApplication.run(App.class,args);
		
	}
	
	@Bean
    public TaskExecutor taskExecutor() {
		int maxThread = 2;
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(maxThread);
        executor.setMaxPoolSize(maxThread);
//        executor.setQueueCapacity(1);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }
}

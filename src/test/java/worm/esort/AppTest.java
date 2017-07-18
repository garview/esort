package worm.esort;

import java.sql.Time;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

public class AppTest {
	private static final Logger logger = LogManager.getLogger(AppTest.class);
	@Test
	public void test() {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
		DateTime dt = fmt.parseDateTime("2017-07-18 04:18");
//		logger.info("excel/esort-{}.xlsx".replace("{}", new DateTime().toString("yyyyMMdd")));
		logger.info(new Time(dt.getMillis()));
	}

}

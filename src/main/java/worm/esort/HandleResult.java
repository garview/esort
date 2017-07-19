package worm.esort;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HandleResult {

	private static final Logger logger = LogManager.getLogger();

	private Integer totalPageCount = 0;

	public List<Integer> handledPages = new ArrayList<Integer>();

	public Integer getTotalPageCount() {
		return totalPageCount;
	}

	public void setTotalPageCount(Integer totalPageCount) {
		this.totalPageCount = totalPageCount;
	}

	public boolean isRepeatHandle() {
		Set<Integer> keys = new HashSet<Integer>();
		for (Integer i : handledPages)
			keys.add(i);
		return keys.size() != handledPages.size();
	}

	public void addHandledPage(Integer pageCount) {
		handledPages.add(pageCount);
	}

	/**
	 * 估算剩余时间
	 * @param pageCostTime 每页耗时（秒）
	 */
	public void expectRemainTime(float pageCostTime) {
		float remainTime = ((totalPageCount - handledPages.size()) * pageCostTime);// 剩余时间（秒）
		int hours = (int) (remainTime / 3600);
		int minutes = (int) ((remainTime % 3600f) / 60);
		int seconds = (int) ((remainTime % 3600f) % 60);
		logger.info("剩余{}小时{}分钟{}秒", hours, minutes, seconds);
	}

//	public static void testExpectRemainTime(){
//		HandleResult rs = new HandleResult();
//		rs.setTotalPageCount(100);
//		int pageCostTime = 61 ;
//		rs.expectRemainTime(pageCostTime);
//	}
//	public static void main(String[] args) {
//		testExpectRemainTime();
//	}
}

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
	
	private float totalCostTime = 0;

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
	public synchronized void expectRemainTime(float pageCostTime) {
		totalCostTime+=pageCostTime;
		float everyPageCost = totalCostTime/handledPages.size();
		float remainTime = everyPageCost*(totalPageCount - handledPages.size());
//		float remainTime = (((totalPageCount - handledPages.size())/2) * pageCostTime);// 剩余时间（秒）
		logger.info("进度：{}/{},已耗时:{}, 预计剩余:{}", handledPages.size(),totalPageCount,convertSeconds2HMS(totalCostTime),convertSeconds2HMS(remainTime));
	}
	
	private String convertSeconds2HMS(float time){
		int hours = (int) (time / 3600);
		int minutes = (int) ((time % 3600f) / 60);
		int seconds = (int) ((time % 3600f) % 60);
		return hours+"时"+minutes+"分"+seconds+"秒";
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

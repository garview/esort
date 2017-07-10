package worm.esort;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HelloJsoup {

	public static void main(String[] args) throws IOException {
		long t1 = System.currentTimeMillis();
		Document doc = Jsoup.connect("https://e-hentai.org/?f_doujinshi=1&f_manga=1&f_artistcg=1&f_gamecg=1&f_western=1&f_non-h=1&f_imageset=1&f_cosplay=1&f_asianporn=1&f_misc=1&f_search=chinese&f_apply=Apply+Filter").get();
		Elements links = doc.select("a[onmouseover]"); 
		for(Element link : links){
			getRecordInfo(link.attr("href"));
		}
		long t2 = System.currentTimeMillis();
		System.out.println("一页耗时："+(t2-t1)/1000+"秒");
//		getRecordInfo("https://e-hentai.org/g/1085017/9516cdd930/"); 404的处理
	}
	
	static void getRecordInfo(String url) {
		System.out.println("===========================");
		long t1 = System.currentTimeMillis();
//		String url2 = "https://e-hentai.org/g/1085249/53796714f7/";
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
			return ;
		}
		System.out.println("评分人数:"+doc.select("#rating_count").html());// 评分人数
		System.out.println("平均评分:"+doc.select("#rating_label").html());// 平均评分
		System.out.println("番号:"+doc.select("#gn").html());// 番号
		Elements values = doc.select("#gdd .gdt2");
		System.out.println("录入时间:"+values.get(0).html());
		System.out.println("大小:"+values.get(4).html());
		System.out.println("总页数:"+values.get(5).html());
		System.out.println("收藏数:"+values.get(6).html());
		long t2 = System.currentTimeMillis();
		System.out.println("耗时："+(t2-t1)/1000+"秒");
		System.out.println("===========================");
	}
}

package worm.esort.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;

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
import org.springframework.stereotype.Service;

import worm.esort.domain.Book;
import worm.esort.repository.BookRepository;

/**
 * e站索引爬虫服务，爬取书籍的基本信息。
 * 
 * @author garview
 *
 */
@Service("eWormIndexService")
@Transactional
public class EWormIndexService {

	private static final Logger logger = LogManager.getLogger();

	@Autowired
	BookRepository bookResp;
	

	/**
	 * 对查询结果所有列表进行爬取 已废弃，单线程爬取效率太低。
	 * 
	 * @param searchResultUrl
	 * @throws IOException
	 */
	@Deprecated
	public void crawlSearchResult(String searchResultUrl) throws IOException {
		long t1 = System.currentTimeMillis();
		Document page1 = Jsoup.connect(searchResultUrl).get();
		crawlListPage(page1);// 由于url规则与其余页不一样，所以第一页在循环外处理
		Elements pageLinks = page1.select("table.ptb td[onclick] > a"); // 获取所有页码链接
		int pageCount = new Integer(pageLinks.get(pageLinks.size() - 2).html());// 总页数
		for (int i = 1; i <= pageCount - 1; i++) {
			logger.info("正在处理第 {} 页", i + 1);
			String pageUrl = new String(searchResultUrl + "&page=" + i); // i=1开始，表示从第二页开始循环
			float costTime = crawlListPage(pageUrl);
			logger.info("当前进度{}/{}, 预计剩余{}秒", i + 1, pageCount, costTime * (pageCount - 1 - i));
		}
		long t2 = System.currentTimeMillis();
		logger.info("总耗时：" + (t2 - t1) / 1000 + "秒");
	}
	

	/**
	 * 爬取查询列表某页的信息
	 * 
	 * @param url
	 * @throws IOException
	 */
	public float crawlListPage(String url) throws IOException {
		long t1 = System.currentTimeMillis();
		Document doc = Jsoup.connect(url).get();
		crawlListPage(doc);
		long t2 = System.currentTimeMillis();
		logger.info("本页耗时{}秒:{}", (t2 - t1) / 1000f, url); // 平均处理1页30s左右
		return (t2 - t1) / 1000f;
	}

	public void crawlListPage(Document doc) {
		Elements links = doc.select("a[onmouseover]");
		for (Element link : links) {
			if(bookResp.findBookByName(link.html())!=null)
				continue;
			Book book = new Book();
			book.setName(link.html());// 先传入本子名字信息，防止由于链接失效导致的数据记录丢失;
			String url = link.attr("href");
			try {
				crawlBook(url + "?nw=session", book);// 某些本子被举报为offensive的话，需要加上"?nw=session"参数才能正常访问，为了处理方便，统一加上"?nw=session"参数；
			} catch (IOException e) {
				logger.error(book.getName() + " 的链接似乎失效了：" + url, e);
				book.setAvailable(false);
				bookResp.save(book);
			}
		}
	}

	/**
	 * 爬取某个本子的信息
	 * 
	 * @param url
	 * @throws IOException
	 */
	public void crawlBook(String url, Book book) throws IOException {
		logger.debug("===========开始处理：{}================", book.getName());
		long t1 = System.currentTimeMillis();
		Document doc = null;
		doc = Jsoup.connect(url).get();
		book.setHref(url);
		book.setRatingCount(str2Integer(doc.select("#rating_count").html()));
		logger.debug("评分人数:" + doc.select("#rating_count").html());// 评分人数
		book.setAverageRating(str2Float(doc.select("#rating_label").html()));
		logger.debug("平均评分:" + doc.select("#rating_label").html());// 平均评分
		book.setName(doc.select("#gn").html());
		logger.debug("番号:" + doc.select("#gn").html());// 番号
		Elements values = doc.select("#gdd .gdt2");
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
		DateTime dt = fmt.parseDateTime(values.get(0).html());
		book.seteInputDate(new Date(dt.getMillis()));
		book.seteInputTime(new Time(dt.getMillis()));
		logger.debug("录入时间:" + values.get(0).html());
		book.setFileSize(values.get(4).html());
		logger.debug("大小:" + values.get(4).html());
		book.setLength(str2Integer(values.get(5).html()));
		logger.debug("总页数:" + values.get(5).html());
		book.setFavourited(str2Integer(values.get(6).html()));
		logger.debug("收藏数:" + values.get(6).html());
		long t2 = System.currentTimeMillis();
		bookResp.save(book);
		logger.debug("处理耗时{}秒：{} ", (t2 - t1) / 1000f, book.getName());
	}

	/**
	 * 把爬取结果输出到excel
	 * 
	 * @param filename
	 *            or filepath
	 */
	public void print2Excel(File file) {

		XSSFWorkbook workbook = new XSSFWorkbook();
		// 添加超链接
		CreationHelper createHelper = workbook.getCreationHelper();
		XSSFCellStyle hlinkstyle = workbook.createCellStyle();
		XSSFFont hlinkfont = workbook.createFont();
		hlinkfont.setUnderline(XSSFFont.U_SINGLE);
		hlinkfont.setColor(HSSFColor.BLUE.index);
		hlinkstyle.setFont(hlinkfont);
		XSSFSheet sheet = workbook.createSheet("esort");
		String[] headers = { "番号", "页数", "评分人数", "添加收藏的人数", "平均评分", "e站入库日期" };
		int rowCount = 0;
		Row headerRow = sheet.createRow(rowCount++);
		for (int i = 0; i < headers.length; i++) {
			String header = headers[i];
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(header);
		}
		sheet.setAutoFilter(CellRangeAddress.valueOf("A1:F1"));
		// 打印数据内容
		Iterable<Book> books = bookResp.findAll();
		for (Book b : books) {
			if (b.getLength() == null)
				continue;
			Row row = sheet.createRow(rowCount++);
			Cell cell = row.createCell(0);
			cell.setCellValue(b.getName());
			XSSFHyperlink link = (XSSFHyperlink) createHelper.createHyperlink(HyperlinkType.URL);
			link.setAddress(b.getHref());
			cell.setHyperlink(link);
			cell.setCellStyle(hlinkstyle);
			row.createCell(1).setCellValue(b.getLength());
			row.createCell(2).setCellValue(b.getRatingCount());
			row.createCell(3).setCellValue(b.getFavourited());
			row.createCell(4).setCellValue(b.getAverageRating());
			row.createCell(5).setCellValue(new DateTime(b.geteInputDate()).toString("yyyy-MM-dd"));
		}
		try {
			FileOutputStream outputStream = new FileOutputStream(file);
			workbook.write(outputStream);
			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Integer str2Integer(String str) {
		String intStr = str.replaceAll("\\D", "");
		if (intStr.length() == 0)
			return 0;
		return new Integer(intStr);
	}

	private Float str2Float(String str) {
		String intStr = str.replaceAll("([a-z]*)([A-Z]*)(\\:*)( *)", ""); // 去除字母、冒号、空格
		if (intStr.length() == 0)
			return 0f;
		return new Float(intStr);
	}

}

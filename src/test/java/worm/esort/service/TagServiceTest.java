package worm.esort.service;

import java.io.IOException;

import javax.transaction.Transactional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import worm.esort.domain.Tag;
import worm.esort.domain.Tagtype;
import worm.esort.repository.TagRepository;
import worm.esort.repository.TagTypeRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional // 添加事务管理，在测试中自动回滚操作（不加此注解就会自动提交事务）
public class TagServiceTest {
	
	@Autowired
	TagRepository tagRepository;
	@Autowired
	TagTypeRepository tagTypeRepository;

//	@Test
	public void testTagFlow() throws IOException{
		String url = "https://e-hentai.org/g/1027032/d5a8ab0015/";
		Document doc = Jsoup.connect(url).get();
		Elements elements = doc.select("#taglist tr");
		System.out.println(elements);
		for(Element e : elements) {
			String typeName = e.select(".tc").html();
			typeName = typeName.substring(0, typeName.indexOf(":")-2);
			Tagtype  type = tagTypeRepository.findTagTypeByTypeName(typeName);
			if(type == null){
				type = new Tagtype();
				type.setTypeName(typeName);
				tagTypeRepository.save(type);
			}
			Elements tagNodes = e.select("td").get(1).select(".gt");
			for(Element node : tagNodes){
				String tagName = node.select("a").html();
				Tag tag = tagRepository.findTagByTagName(tagName);
				if(tag==null){
					tag = new Tag();
					tag.setTagName(tagName);
					tag.setTagType(type);
					tagRepository.save(tag);
				}
			}
		}
	}
	
}

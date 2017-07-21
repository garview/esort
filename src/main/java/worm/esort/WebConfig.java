package worm.esort;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}
	private static final Charset UTF8 = Charset.forName("UTF-8");

	  @Override
	  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		  StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
		    stringConverter.setSupportedMediaTypes(Arrays.asList(new MediaType("text", "plain", UTF8)));
		    converters.add(stringConverter);
		    converters.add(new MappingJackson2HttpMessageConverter());
	    super.configureMessageConverters(converters);
	  }
}
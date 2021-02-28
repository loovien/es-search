package tv.zhanqi.essearch.svr;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Configuration
public class Jackson {

    @Bean
    public ObjectMapper getJacksonObjectMapper() {
        return new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE)
                .setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
                .setLocale(Locale.CHINA)
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

}

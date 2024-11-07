package sn.odc.oumar.springproject.Web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.servlet.Filter;
import sn.odc.oumar.springproject.Web.filters.ResponseFormattingFilter;

@Configuration
public class FilterConfig {

    @Bean
    public Filter responseFormattingFilter() {
        return new ResponseFormattingFilter();
    }
}

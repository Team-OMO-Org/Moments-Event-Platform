package io.github.teamomo.moment.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

  //@Bean
  public FilterRegistrationBean<CorrelationIdFilter> correlationIdFilter() {
    FilterRegistrationBean<CorrelationIdFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new CorrelationIdFilter());
    registrationBean.addUrlPatterns("/*"); // Apply to all URLs
    registrationBean.setOrder(1); // Set filter order if multiple filters exist
    return registrationBean;
  }
}

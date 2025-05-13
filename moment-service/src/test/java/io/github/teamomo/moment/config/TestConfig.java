package io.github.teamomo.moment.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {



  @Bean
  public void setupLoggingForTests() {

    System.setProperty("logging.level.org.springframework.web", "DEBUG");
    System.setProperty("logging.level.org.springframework.http.converter", "DEBUG");
    System.setProperty("logging.level.io.github.teamomo.moment", "DEBUG");
    System.setProperty("logging.level.java.lang", "DEBUG");

  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    return mapper;
  }

}

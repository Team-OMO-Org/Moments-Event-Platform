package io.github.teamomo.moment.config;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class CorrelationIdFilter implements Filter {
  private static final String CORRELATION_ID = "X-Correlation-ID";

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String correlationId = httpRequest.getHeader(CORRELATION_ID);
    if (correlationId == null) {
      correlationId = UUID.randomUUID().toString();
    }
    MDC.put(CORRELATION_ID, correlationId);
    try {
      chain.doFilter(request, response);
    } finally {
      MDC.remove(CORRELATION_ID);
    }
  }
}

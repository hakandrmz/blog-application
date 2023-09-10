package guru.hakandurmaz.blog.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) {
    String requestURI = request.getRequestURI();
    if (requestURI != null
        && (requestURI.contains("/eureka/") || requestURI.contains("/v3/api-docs"))) {
      return true;
    }
    log.info(
        "Received request: {} {} from {}",
        request.getMethod(),
        request.getRequestURI(),
        request.getRemoteAddr());
    return true;
  }

  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      @Nullable ModelAndView modelAndView) {
    String requestURI = request.getRequestURI();
    if (requestURI != null
        && !(requestURI.contains("/eureka/") || requestURI.contains("/v3/api-docs"))) {
      log.info(
          "Sent response: {} {} with status {} and exception {}",
          request.getMethod(),
          request.getRequestURI(),
          response.getStatus());
    }
  }
}

package io.github.teamomo.momentswebapp.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

  //private static final int COOKIE_MAX_AGE = 60 * 60 * 24; // 1 day in seconds
  private static final int COOKIE_MAX_AGE = 60 * 5; // 5 minutes in seconds

  // Store a value in a secure (requires HTTPS), HTTP-only cookie
  public void storeCookie(HttpServletResponse response, String name, String value) {
    Cookie cookie = new Cookie(name, value);
    cookie.setHttpOnly(true);
    //  cookie.setSecure(true); // Ensure this is true in production (requires HTTPS)
    cookie.setPath("/");
    cookie.setMaxAge(COOKIE_MAX_AGE);
    response.addCookie(cookie);
  }

  public String getCookieValue(HttpServletRequest request, String name) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (name.equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }
    return null;
  }

  public void deleteCookie(HttpServletResponse response, String name) {
    Cookie cookie = new Cookie(name, null);
    cookie.setHttpOnly(true);
    //  cookie.setSecure(true); //Ensure this is true in production (requires HTTPS)
    cookie.setPath("/");
    cookie.setMaxAge(0); // Set max age to 0 to delete the cookie
    response.addCookie(cookie);
  }
}

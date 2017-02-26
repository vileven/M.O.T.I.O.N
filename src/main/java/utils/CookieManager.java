package utils;

import org.jetbrains.annotations.Nullable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieManager {
    public static final String COOKIE_NAME = "remember_token";
    public static final int COOKIE_AGE = 2592000;

    public static void addCookie(HttpServletResponse response, String name, @Nullable String value, int maxAge) {
        final Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void removeCookie(HttpServletResponse response, String name) {
        addCookie(response, name, null, 0);
    }

    @Nullable
    public static String getCookieValue(HttpServletRequest request, String name) {
        final Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}

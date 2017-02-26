package controllers;

import models.User;
import services.AccountService;
import utils.CookieManager;
import utils.GetUserInfo;
import utils.SessionIdGenerator;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static utils.CookieManager.COOKIE_AGE;
import static utils.CookieManager.COOKIE_NAME;

@RestController
@RequestMapping(path = "/sessions")
public class SessionController {

    @NotNull
    private final AccountService accountService;

    public SessionController(@NotNull AccountService accountService) {
        this.accountService = accountService;
    }


    /**
     * Залогинить пользователя
     * @param requestBody login и password из тела запроса в json
     * @param response объект <code>HttpServletResponse</code> сессии пользователя
     * @return json <code>User</code> ответ если OK, иначе <code>HTTP</code> код соответсвующей ошибки
     */
    @RequestMapping(path = "/login", method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> loginUser(@RequestBody GetUserInfo requestBody, HttpServletResponse response) {

        final String login = requestBody.getLogin();
        final String password = requestBody.getPassword();

        // некорректный запрос
        if (login == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // аутентификация
        final User user = accountService.authenticate(requestBody.getLogin(), requestBody.getPassword());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        final String sessionId = (new SessionIdGenerator()).nextSessionId();
        CookieManager.addCookie(response, COOKIE_NAME, sessionId, COOKIE_AGE);

        accountService.addSession(sessionId, user);

        return ResponseEntity.ok("{\"content\":\"successful login\"}");
    }

    /**
     * Разлогин
     * @param request объект <code>HttpServletRequest</code> запрос
     * @param response объект <code>HttpServletResponse</code> ответ
     * @return json ответ если OK, иначе <code>HTTP</code> код соответсвующей ошибки
     */
    @RequestMapping(path = "/logout", method = RequestMethod.DELETE,
            produces = "application/json")
    public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response) {

        final String sessionId = CookieManager.getCookieValue(request, COOKIE_NAME);
        final User user = accountService.getUserBySessionId(sessionId);

        // если user не нашелся
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        CookieManager.removeCookie(response, COOKIE_NAME);
        accountService.removeSession(sessionId);

        return ResponseEntity.ok("{\"content\":\"Goodbye!\"}");
    }

    /**
     * Вернуть залогиненного пользователя
     * @param request объект <code>HttpServletRequest</code>
     * @return json <code>User</code>
     */
    @RequestMapping(path = "/current", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getLoggedUser(HttpServletRequest request) {

        final String sessionId = CookieManager.getCookieValue(request, COOKIE_NAME);
        final User currentUser = accountService.getUserBySessionId(sessionId);

        // если пользователь не нашелся
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(currentUser);
    }
}

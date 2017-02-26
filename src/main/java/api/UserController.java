package api;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "user")
public class UserController {

    @NotNull
    private final AccountService accountService;

    public UserController(@NotNull AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Вернуть пользователя по логину
     * @param requestBody json логин
     * @return json user
     */
    @RequestMapping(path = "/", method = RequestMethod.GET,
            produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> showUser(@RequestBody GetUserInfo requestBody) {
        User user = accountService.getUserByLogin(requestBody.getLogin());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error-message\":\"user not found\"}");
        }

        return ResponseEntity.ok(user);
    }

    /**
     * Зарегистрировать пользователя
     * @param requestBody <code>login, email, password</code> в формате json
     * @return json сообщение об исходе операции
     */
    @RequestMapping(path = "/", method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createUser(@RequestBody GetUserInfo requestBody) {
        if (accountService.register(requestBody.getLogin(), requestBody.getEmail(),
                requestBody.getPassword())) {
            return ResponseEntity.ok("{\"content\":\"you are signed up!\"}");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error-message\":\"user already exist\"}");
    }

    /**
     * Удалить пользователя
     * @param requestBody login из json
     * @return json сообщение об исходе операции
     */

    @RequestMapping(path = "/", method = RequestMethod.DELETE,
            consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> deleteUser(@RequestBody GetUserInfo requestBody) {
        if (accountService.delete(requestBody.getLogin())) {
            return ResponseEntity.ok("{\"content\":\"successful delete user\"");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error-message\":\"user not found\"}");
    }






}

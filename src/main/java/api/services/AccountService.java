package api.services;

import api.models.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class AccountService {

    private Map<String, User> loginToUser = new HashMap<>();
    private final Map<String, User> sessionIdToUser = new HashMap<>();
    private final AtomicLong counter = new AtomicLong();


    public boolean register(@NotNull String login, @NotNull String email, @NotNull String password) {

        User newUser = null;
        if (!loginToUser.containsKey(login)) {
            String encodedPassword = DigestUtils.md5DigestAsHex(Base64.getEncoder().encode(password.getBytes(UTF_8)));
            newUser = new User(counter.incrementAndGet(),
                    login, email, encodedPassword, Calendar.getInstance());

            loginToUser.put(login, newUser);
            return true;
        }

        return false;
    }

    @NotNull
    public boolean delete(String login) {
        if(loginToUser.remove(login) == null) {
            return false;
        }

        return true;
    }

    public User authenticate(@NotNull String login, @NotNull String password) {
        if (loginToUser.containsKey(login)) {
            User user = loginToUser.get(login);
            password = DigestUtils.md5DigestAsHex(Base64.getEncoder().encode(password.getBytes(UTF_8)));
            if (password.equals(loginToUser.get(login).getPassword())) {
                return user;
            }
        }
        return null;
    }

    public User getUserByLogin(String login) {
        return loginToUser.get(login);
    }

    public User getUserBySessionId(String sessionId) {
        return sessionIdToUser.get(sessionId);
    }

    public void addSession(String sessionId, User user) {
        sessionIdToUser.put(sessionId, user);
    }

    public void removeSession(String sessionId) {
        sessionIdToUser.remove(sessionId);
    }

}

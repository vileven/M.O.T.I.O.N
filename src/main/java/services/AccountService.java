package services;

import models.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static java.nio.charset.StandardCharsets.UTF_8;

@SuppressWarnings("unused")
@Service
public class AccountService {

    private final Map<String, User> loginToUser = new HashMap<>();
    private final Map<String, User> sessionIdToUser = new HashMap<>();
    private final AtomicLong counter = new AtomicLong();


    public boolean register(@NotNull String login, @NotNull String email, @NotNull String password) {

        if (!loginToUser.containsKey(login)) {
            final String encodedPassword = DigestUtils.md5DigestAsHex(Base64.getEncoder().encode(password.getBytes(UTF_8)));
            final User newUser = new User(counter.incrementAndGet(),
                    login, email, encodedPassword, Calendar.getInstance());

            loginToUser.put(login, newUser);
            return true;
        }

        return false;
    }

    public boolean delete(String login) {
        return loginToUser.remove(login) != null;
    }

    @Nullable
    public User authenticate(@NotNull String login, @NotNull String password) {
        if (loginToUser.containsKey(login)) {
            final User user = loginToUser.get(login);
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

    public User getUserBySessionId(@Nullable String sessionId) {
        return sessionIdToUser.get(sessionId);
    }

    public void addSession(String sessionId, User user) {
        sessionIdToUser.put(sessionId, user);
    }

    public void removeSession(@Nullable String sessionId) {
        sessionIdToUser.remove(sessionId);
    }

}

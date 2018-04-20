package com.kpi.bot.services.loader.telegram.database;

import com.kpi.bot.data.Repository;
import com.kpi.bot.services.loader.telegram.structure.User;
import org.telegram.api.contacts.TLResolvedPeer;
import org.telegram.api.engine.TelegramApi;
import org.telegram.api.functions.contacts.TLRequestContactsResolveUsername;
import org.telegram.api.functions.users.TLRequestUsersGetFullUser;
import org.telegram.api.input.user.TLAbsInputUser;
import org.telegram.api.input.user.TLInputUser;
import org.telegram.api.user.TLAbsUser;
import org.telegram.api.user.TLUserFull;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class TelegramUserRepository implements TelegramRepository<User> {

    private Repository<User> userRepository;
    private TelegramApi telegramApi;

    public TelegramUserRepository(Repository<User> backingRepository, TelegramApi telegramApi) {
        this.userRepository = backingRepository;
        this.telegramApi = telegramApi;
    }

    @Override
    public User save(User entity) {
        return userRepository.save(entity);
    }

    @Override
    public List<User> saveAll(Iterable<User> entities) {
        return userRepository.saveAll(entities);
    }

    @Override
    public User find(String key) {
        User user = userRepository.find(key);
        if (user == null) {
            try {
                TLRequestUsersGetFullUser request = new TLRequestUsersGetFullUser();
                TLInputUser userRequest = new TLInputUser();
                userRequest.setUserId(Integer.parseInt(key));
                request.setId(userRequest);

                TLUserFull tlUser = telegramApi.doRpcCall(request);
            } catch (TimeoutException | IOException e) {
                throw new RuntimeException(e);
            }
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void delete(String key) {
        userRepository.delete(key);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

}

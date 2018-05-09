package com.kpi.bot.services.loader.telegram.core;

import com.kpi.bot.data.Repository;
import com.kpi.bot.entity.data.User;
import com.kpi.bot.services.loader.telegram.TelegramConverter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.api.user.TLAbsUser;
import org.telegram.bot.handlers.interfaces.IUsersHandler;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class UsersHandler implements IUsersHandler {

    private Repository<User> userRepository;

    public UsersHandler(Repository<User> userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onUsers(List<TLAbsUser> list) {
        log.info("Updating users " + list.stream().map(u -> String.valueOf(u.getId())).collect(Collectors.joining(",")));
        userRepository.saveAll(list.stream().map(TelegramConverter::convertUser).filter(Objects::nonNull).collect(Collectors.toList()));
    }
}
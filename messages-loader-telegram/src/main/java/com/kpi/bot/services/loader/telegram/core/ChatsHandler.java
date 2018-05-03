package com.kpi.bot.services.loader.telegram.core;

import com.kpi.bot.data.Repository;
import com.kpi.bot.entity.data.Channel;
import com.kpi.bot.services.loader.telegram.TelegramConverter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.api.chat.TLAbsChat;
import org.telegram.bot.handlers.interfaces.IChatsHandler;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class ChatsHandler implements IChatsHandler {


    private Repository<Channel> userRepository;

    public ChatsHandler(Repository<Channel> userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onChats(List<TLAbsChat> list) {
        log.info("Updating chats " + list.stream().map(chat -> String.valueOf(chat.getId())).collect(Collectors.joining(",")));
        userRepository.saveAll(list.stream().map(TelegramConverter::convertChannel).filter(Objects::nonNull).collect(Collectors.toList()));
    }
}

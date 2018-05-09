package com.kpi.bot.services.loader.telegram.database;

import com.kpi.bot.data.Repository;
import com.kpi.bot.entity.data.Channel;
import com.kpi.bot.entity.data.User;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.telegram.bot.kernel.database.DatabaseManager;
import org.telegram.bot.structure.Chat;
import org.telegram.bot.structure.IUser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class TelegramDatabase implements DatabaseManager {

    private Map<Integer, int[]> differences = new ConcurrentHashMap<>();
    private Repository<User> userRepository;
    private Repository<Channel> channelRepository;

    public TelegramDatabase(Repository<User> userRepository, Repository<Channel> channelRepository) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }

    @Override
    public @Nullable Chat getChatById(int i) {

        Channel channel = channelRepository.find(String.valueOf(i));
        if (channel != null) {
            return new Chat() {
                @Override
                public int getId() {
                    return Integer.valueOf(channel.getId());
                }

                @Override
                public Long getAccessHash() {
                    return Long.valueOf(channel.getHash());
                }

                @Override
                public boolean isChannel() {
                    return true;
                }
            };
        } else {
            log.warn("Trying to get Chat with id " + i);
            return null;
        }
    }

    @Override
    public @Nullable IUser getUserById(int i) {
        User user = userRepository.find(String.valueOf(i));
        if (user != null) {
            return new IUser() {
                @Override
                public int getUserId() {
                    return Integer.valueOf(user.getId());
                }

                @Override
                public Long getUserHash() {
                    return Long.valueOf(user.getHash());
                }
            };
        } else {
            log.warn("Trying to get User with id " + i);
            return null;
        }
    }

    @NotNull
    @Override
    public Map<Integer, int[]> getDifferencesData() {
        return new HashMap<>(differences);
    }

    @Override
    public boolean updateDifferencesData(int botId, int pts, int date, int seq) {
        int[] oldData = differences.get(botId);
        int[] newData = {pts, date, seq};
        if (Arrays.equals(oldData, newData)) {
            return false;
        } else {
            differences.put(botId, newData);
            return true;
        }
    }

    public Repository<User> getUserRepository() {
        return userRepository;
    }

    public Repository<Channel> getChannelRepository() {
        return channelRepository;
    }
}

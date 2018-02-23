package com.kpi.bot.services.loader.telegram.database;

import com.kpi.bot.services.loader.telegram.structure.TelegramChat;
import com.kpi.bot.services.loader.telegram.structure.TelegramUser;
import org.telegram.bot.kernel.database.DatabaseManager;
import org.telegram.bot.structure.Chat;
import org.telegram.bot.structure.IUser;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;


public class TelegramDatabase implements DatabaseManager {
    private static Map<Integer, TelegramUser> users = new ConcurrentHashMap<>();
    private static Map<Integer, Chat> chats = new ConcurrentHashMap<>();
    private static HashMap<Integer, int[]> differences = new HashMap<>();

    @Override
    public IUser getUserById(int userId) {
        return users.get(userId);
    }

    public void addUser(TelegramUser user) {
        users.put(user.getUserId(), user);
    }

    public void updateUser(TelegramUser user) {
        users.put(user.getUserId(), user);
    }

    @Override
    public Chat getChatById(int chatId) {
        return chats.get(chatId);
    }

    public void addChat(TelegramChat chat) {
        chats.put(chat.getId(), chat);
    }

    public void updateChat(TelegramChat chat) {
        chats.put(chat.getId(), chat);
    }

    @Override
    public synchronized HashMap<Integer, int[]> getDifferencesData() {
        return new HashMap<>(differences);
    }

    @Override
    public synchronized boolean updateDifferencesData(int botId, int pts, int date, int seq) {
        int[] oldData = differences.get(botId);
        int[] newData = {pts, date, seq};
        if (Arrays.equals(oldData, newData)) {
            return false;
        } else {
            differences.put(botId, newData);
            return true;
        }
    }

    public String getAuthCode() {
        System.out.println("Enter auth code");
        return new Scanner(System.in).next().trim();
    }
}

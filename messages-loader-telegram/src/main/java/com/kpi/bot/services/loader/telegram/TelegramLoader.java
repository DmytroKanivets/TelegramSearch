package com.kpi.bot.services.loader.telegram;

import com.kpi.bot.services.loader.MessageLoader;
import com.kpi.bot.services.loader.telegram.handlers.ChatsHandler;
import com.kpi.bot.services.loader.telegram.handlers.MessageHandler;
import com.kpi.bot.services.loader.telegram.handlers.UsersHandler;
import org.telegram.bot.handlers.interfaces.IChatsHandler;
import org.telegram.bot.handlers.interfaces.IUsersHandler;
import org.telegram.bot.kernel.TelegramBot;
import org.telegram.bot.services.BotLogger;
import org.telegram.bot.structure.BotConfig;
import org.telegram.bot.structure.LoginStatus;
import com.kpi.bot.services.loader.telegram.database.TelegramDatabase;
import com.kpi.bot.services.loader.telegram.handlers.TLMessageHandler;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TelegramLoader implements MessageLoader {
    private TelegramConfiguration configuration;

    public TelegramLoader(TelegramConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void startListening() {
        Logger.getGlobal().addHandler(new ConsoleHandler());
        Logger.getGlobal().setLevel(Level.ALL);


        final TelegramDatabase databaseManager = new TelegramDatabase();

        final IUsersHandler usersHandler = new UsersHandler(databaseManager);
        final IChatsHandler chatsHandler = new ChatsHandler(databaseManager);
        final MessageHandler messageHandler = new MessageHandler();
        final TLMessageHandler tlMessageHandler = new TLMessageHandler(messageHandler, databaseManager);

        final ChatUpdatesBuilderImpl builder = new ChatUpdatesBuilderImpl(CustomUpdatesHandler.class);
        builder.setBotConfig(configuration)
                .setDatabaseManager(databaseManager)
                .setUsersHandler(usersHandler)
                .setChatsHandler(chatsHandler)
                .setMessageHandler(messageHandler)
                .setTlMessageHandler(tlMessageHandler);

        try {
            final TelegramBot kernel = new TelegramBot(configuration, builder, configuration.getApiKey(), configuration.getApiHash());
            LoginStatus status = kernel.init();
            if (status == LoginStatus.CODESENT) {
                boolean success = kernel.getKernelAuth().setAuthCode(databaseManager.getAuthCode());
                if (success) {
                    status = LoginStatus.ALREADYLOGGED;
                }
            }
            if (status == LoginStatus.ALREADYLOGGED) {
                kernel.startBot();
            } else {
                throw new Exception("Failed to log in: " + status);
            }
        } catch (Exception e) {
            BotLogger.severe("MAIN", e);
        }
    }

    public static void main(String[] args) {
        BotLogger.setLevel(Level.ALL);
        new TelegramLoader(new TelegramConfiguration(151369, "12f93c908c66d32bdf975a483b3f0691", "+380688109527")).startListening();
    }
}

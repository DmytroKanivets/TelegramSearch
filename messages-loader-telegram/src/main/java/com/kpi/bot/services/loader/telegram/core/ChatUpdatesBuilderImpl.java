package com.kpi.bot.services.loader.telegram.core;

import com.kpi.bot.services.loader.telegram.TelegramUpdatesHandler;
import com.kpi.bot.services.loader.telegram.database.TelegramDatabase;
import org.telegram.bot.ChatUpdatesBuilder;
import org.telegram.bot.handlers.UpdatesHandlerBase;
import org.telegram.bot.kernel.IKernelComm;
import org.telegram.bot.kernel.database.DatabaseManager;
import org.telegram.bot.kernel.differenceparameters.IDifferenceParametersService;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ChatUpdatesBuilderImpl implements ChatUpdatesBuilder {
    private final Class<CustomUpdatesHandler> updatesHandlerBase;
    private IKernelComm kernelComm;
    private TelegramUpdatesHandler tlMessageHandler;
    private IDifferenceParametersService differenceParametersService;
    private TelegramDatabase databaseManager;

    public ChatUpdatesBuilderImpl(Class<CustomUpdatesHandler> updatesHandlerBase) {
        this.updatesHandlerBase = updatesHandlerBase;
    }

    @Override
    public void setKernelComm(IKernelComm kernelComm) {
        this.kernelComm = kernelComm;
    }

    @Override
    public void setDifferenceParametersService(IDifferenceParametersService differenceParametersService) {
        this.differenceParametersService = differenceParametersService;
    }

    @Override
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public ChatUpdatesBuilderImpl setDatabaseManager(TelegramDatabase databaseManager) {
        this.databaseManager = databaseManager;
        return this;
    }

    public ChatUpdatesBuilderImpl setTlMessageHandler(TelegramUpdatesHandler tlMessageHandler) {
        this.tlMessageHandler = tlMessageHandler;
        return this;
    }

    public TelegramUpdatesHandler getTlMessageHandler() {
        return tlMessageHandler;
    }

    @Override
    public UpdatesHandlerBase build() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (kernelComm == null) {
            throw new NullPointerException("Can't build the handler without a KernelComm");
        }
        if (differenceParametersService == null) {
            throw new NullPointerException("Can't build the handler without a differenceParamtersService");
        }

        final Constructor<CustomUpdatesHandler> constructor = updatesHandlerBase.getConstructor(IKernelComm.class,
                IDifferenceParametersService.class, DatabaseManager.class);
        final CustomUpdatesHandler updatesHandler =
                constructor.newInstance(kernelComm, differenceParametersService, getDatabaseManager());
        updatesHandler.setHandlers(
                new UsersHandler(databaseManager.getUserRepository()),
                new ChatsHandler(databaseManager.getChannelRepository()),
                tlMessageHandler);
        return updatesHandler;
    }
}

package com.kpi.bot.services.loader.telegram.handlers;

import com.kpi.bot.services.loader.telegram.structure.TelegramUser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.telegram.api.user.TLAbsUser;
import org.telegram.api.user.TLUser;
import org.telegram.bot.handlers.interfaces.IUsersHandler;
import org.telegram.bot.services.BotLogger;
import com.kpi.bot.services.loader.telegram.database.TelegramDatabase;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ruben Bermudez
 * @version 2.0
 * Handler for received users
 */
public class UsersHandler implements IUsersHandler {
    private static final String LOGTAG = "USERSHANDLER";
    private final ConcurrentHashMap<Integer, TLAbsUser> temporalUsers = new ConcurrentHashMap<>();
    private static final int MAXTEMPORALUSERS = 4000;
    private final TelegramDatabase databaseManager;

    public UsersHandler(TelegramDatabase databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Add a list of users to database
     * @param users List of users to add
     */
    public void onUsers(@NotNull List<TLAbsUser> users) {
        if ((this.temporalUsers.size() + users.size()) > MAXTEMPORALUSERS) {
            this.temporalUsers.clear();
        }
        users.stream().forEach(x -> this.temporalUsers.put(x.getId(), x));
        users.forEach(this::onUser);
    }

    /**
     * Add a user to database
     * @param absUser TelegramUser to add
     */
    private void onUser(@NotNull TLAbsUser absUser) {
        TelegramUser currentUser = null;
        TelegramUser user = null;
        if (absUser instanceof TLUser) {
            final TLUser tlUser = (TLUser) absUser;
            if (tlUser.isMutualContact()) {
                currentUser = (TelegramUser) databaseManager.getUserById(tlUser.getId());
                user = onUserContact(currentUser, tlUser);
            } else if (tlUser.isDeleted()) {
                currentUser = (TelegramUser) databaseManager.getUserById(tlUser.getId());
                user = onUserDelete(currentUser, tlUser);
            } else if (tlUser.isContact()) {
                currentUser = (TelegramUser) databaseManager.getUserById(tlUser.getId());
                user = onUserRequest(currentUser, tlUser);
            } else if (tlUser.isSelf() || !tlUser.isBot()) {
                currentUser = (TelegramUser) databaseManager.getUserById(tlUser.getId());
                user = onUserForeign(currentUser, tlUser);
            } else {
                BotLogger.info(LOGTAG, "Bot received");
            }
        }
        if ((currentUser == null) && (user != null)) {
            databaseManager.addUser(user);
        } else if (user != null) {
            databaseManager.updateUser(user);
        }
    }

    /**
     * Create TelegramUser from a delete user
     * @param currentUser Current use from database (null if not present)
     * @param userDeleted Delete user from Telegram Server
     * @return TelegramUser information
     */
    private TelegramUser onUserDelete(@Nullable TelegramUser currentUser, @NotNull TLUser userDeleted) {
        final TelegramUser user;
        if (currentUser == null) {
            user = new TelegramUser(userDeleted.getId());
        } else {
            user = new TelegramUser(currentUser);
        }
        user.setUserHash(0L);
        BotLogger.info(LOGTAG, "userdeletedid: " + user.getUserId());
        return user;
    }

    /**
     * Create TelegramUser from a contact user
     * @param currentUser Current use from database (null if not present)
     * @param userContact Contact user from Telegram Server
     * @return TelegramUser information
     */
    private TelegramUser onUserContact(@Nullable TelegramUser currentUser, @NotNull TLUser userContact) {
        final TelegramUser user;
        if (currentUser == null) {
            user = new TelegramUser(userContact.getId());
        } else {
            user = new TelegramUser(currentUser);
        }
        user.setUserHash(userContact.getAccessHash());
        BotLogger.info(LOGTAG, "usercontactid: " + user.getUserId());
        return user;
    }

    /**
     * Create TelegramUser from a request user
     * @param currentUser Current use from database (null if not present)
     * @param userRequest Request user from Telegram Server
     * @return TelegramUser information
     */
    private TelegramUser onUserRequest(@Nullable TelegramUser currentUser, @NotNull TLUser userRequest) {
        final TelegramUser user;
        if (currentUser == null) {
            user = new TelegramUser(userRequest.getId());
        } else {
            user = new TelegramUser(currentUser);
        }
        user.setUserHash(userRequest.getAccessHash());
        BotLogger.info(LOGTAG, "userRequestId: " + user.getUserId());
        return user;
    }

    /**
     * Create TelegramUser from a foreign user
     * @param currentUser Current use from database (null if not present)
     * @param userForeign Foreign user from Telegram Server
     * @return TelegramUser information
     */
    private TelegramUser onUserForeign(@Nullable TelegramUser currentUser, @NotNull TLUser userForeign) {
        final TelegramUser user;
        if (currentUser == null) {
            user = new TelegramUser(userForeign.getId());
        } else {
            user = new TelegramUser(currentUser);
        }
        user.setUserHash(userForeign.getAccessHash());
        BotLogger.info(LOGTAG, "userforeignid: " + user.getUserId());
        return user;
    }

}

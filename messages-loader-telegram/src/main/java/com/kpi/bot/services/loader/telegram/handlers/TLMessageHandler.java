package com.kpi.bot.services.loader.telegram.handlers;

import org.telegram.api.message.TLMessage;
import org.telegram.api.peer.TLAbsPeer;
import org.telegram.api.peer.TLPeerUser;
import org.telegram.bot.kernel.database.DatabaseManager;
import org.telegram.bot.services.BotLogger;
import org.telegram.bot.structure.IUser;

public class TLMessageHandler {
    private static final String LOGTAG = "TLMESSAGEHANDLER";
    private final MessageHandler messageHandler;
    private final DatabaseManager databaseManager;

    public TLMessageHandler(MessageHandler messageHandler, DatabaseManager databaseManager) {
        this.messageHandler = messageHandler;
        this.databaseManager = databaseManager;
    }

    public void onTLMessage(TLMessage message) {
//        final TLAbsPeer absPeer = message.getToId();
//        if (absPeer instanceof TLPeerUser) {
            onTLMessageForUser(message);
//        } else {
        //send to channel, not user
//            BotLogger.severe(LOGTAG, "Unsupported Peer: " + absPeer.toString());
//        }
    }

    private void onTLMessageForUser(TLMessage message) {
//        if (!message.isSent()) {
            final IUser user = databaseManager.getUserById(message.getFromId());
//            if (user != null) {
            System.out.println(message.getMessage());

                this.messageHandler.handleMessage(user, message);
//            }
//        }
    }
}

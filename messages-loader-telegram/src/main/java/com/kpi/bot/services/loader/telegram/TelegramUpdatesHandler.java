package com.kpi.bot.services.loader.telegram;

import org.telegram.api.message.TLMessage;
import org.telegram.api.updates.TLUpdateShortMessage;

public interface TelegramUpdatesHandler {
    void onMessage(TLMessage message);
    void onUpdatedMessage(TLUpdateShortMessage message);
}

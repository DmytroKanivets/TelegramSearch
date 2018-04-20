package com.kpi.bot.services.loader.telegram.database;

import com.kpi.bot.data.Repository;
import com.kpi.bot.entity.data.Identifiable;

public interface TelegramRepository<T extends Identifiable> extends Repository<T> {
}

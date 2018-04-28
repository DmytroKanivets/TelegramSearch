package com.kpi.bot.data;

import com.kpi.bot.entity.data.Identifiable;
import com.kpi.bot.entity.data.Message;
import com.kpi.bot.entity.search.SearchCriteria;

import java.util.List;

public interface SearchableRepository<T extends Identifiable> extends Repository<T> {
    List<T> findByCriteria(SearchCriteria criteria, Long offset, Long limit);
}

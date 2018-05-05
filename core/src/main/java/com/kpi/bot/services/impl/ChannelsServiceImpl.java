package com.kpi.bot.services.impl;

import com.kpi.bot.data.Repository;
import com.kpi.bot.data.SearchableRepository;
import com.kpi.bot.entity.data.Channel;
import com.kpi.bot.entity.data.Message;
import com.kpi.bot.entity.search.SearchCriteria;
import com.kpi.bot.entity.search.SearchPredicate;
import com.kpi.bot.exceptions.ChannelNotFoundException;
import com.kpi.bot.services.ChannelsService;
import com.kpi.bot.services.Statistics;
import com.kpi.bot.services.loader.MessageLoader;
import com.kpi.bot.stats.IndexingStatistics;
import com.sun.deploy.util.ArrayUtil;

import java.util.List;

public class ChannelsServiceImpl implements ChannelsService {
    private Repository<Channel> channelRepository;
    private SearchableRepository<Message> messageRepository;
    private MessageLoader messageLoader;

    public ChannelsServiceImpl(Repository<Channel> channelRepository, SearchableRepository<Message> messageRepository, MessageLoader messageLoader) {
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
        this.messageLoader = messageLoader;
    }


    @Override
    public void save(Channel channel) {
        channelRepository.save(channel);
    }

    @Override
    public void deleteChannelById(String id) {
        messageLoader.stopIndexing(id);
        Channel channel = channelRepository.find(id);
        IndexingStatistics.removeChannel(channel.getName());

        channelRepository.delete(id);

        SearchCriteria criteria = new SearchCriteria();
        criteria.addPredicate(SearchPredicate.EQUALS("channel", channel.getName()));

        List<Message> mes;
        do {
            mes = messageRepository.findByCriteria(criteria, 0, 100);
            for (Message m : mes) {
                messageRepository.delete(m.getId());
            }
        } while (mes.size() > 0);
    }

    @Override
    public void deleteChannelByName(String name) throws ChannelNotFoundException {
        List<Channel> c = channelRepository.findAll();
        for (Channel channel : c) {
            if (channel.getName().equals(name)) {
                deleteChannelById(channel.getId());
                return;
            }
        }

        throw new RuntimeException("Channel not found");
    }

    @Override
    public List<Channel> getChannels() {
        return channelRepository.findAll();
    }
}

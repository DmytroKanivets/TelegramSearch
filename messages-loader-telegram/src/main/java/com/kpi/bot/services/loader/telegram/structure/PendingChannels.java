package com.kpi.bot.services.loader.telegram.structure;

import java.util.HashMap;
import java.util.Map;


public class PendingChannels {
    private Map<String, JoinInfo> byId = new HashMap<>();
    private Map<String, JoinInfo> byJoinHash = new HashMap<>();

    public void add(JoinInfo info) {
        byId.put(info.getChannel().getName(), info);
        byJoinHash.put(info.getJoinHash(), info);
    }

    public JoinInfo getById(String id) {
        return byId.get(id);
    }

    public JoinInfo getByJoinHash(String hash) {
        return byJoinHash.get(hash);
    }

    public JoinInfo deleteById(String id) {
        JoinInfo info = byId.remove(id);
        if (info != null) {
            byJoinHash.remove(info.getJoinHash());
        }
        return info;
    }

    public JoinInfo deleteByHash(String hash) {
        JoinInfo info = byJoinHash.remove(hash);
        if (info != null) {
            byId.remove(info.getChannel().getId());
        }
        return info;
    }

    public JoinInfo delete(JoinInfo joinInfo) {
        return deleteByHash(joinInfo.getJoinHash());
    }
}

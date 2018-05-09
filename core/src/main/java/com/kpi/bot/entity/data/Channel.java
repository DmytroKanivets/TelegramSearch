package com.kpi.bot.entity.data;

import lombok.Data;

@Data
public class Channel implements Identifiable {
    private String id;
    private String hash;
    private String name;
}

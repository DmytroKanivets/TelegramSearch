package com.kpi.bot.services.loader.telegram.structure;

import com.kpi.bot.entity.data.Identifiable;
import lombok.Data;

@Data
public class Channel implements Identifiable {
    private String id;
    private String hash;
    private String name;
}

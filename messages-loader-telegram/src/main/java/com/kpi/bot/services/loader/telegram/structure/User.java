package com.kpi.bot.services.loader.telegram.structure;

import com.kpi.bot.entity.data.Identifiable;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class User implements Identifiable {
    private String id;
    private String userName;
    private String hash;
    private Set<String> admins = new HashSet<>();
}

package com.kpi.bot.services.loader.telegram.structure;

import com.kpi.bot.entity.data.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinInfo {
    private Channel channel;
    private String joinHash;
    private Instant indexingStart;
}
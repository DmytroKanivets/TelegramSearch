package com.kpi.bot.server.frontend.controllers.admin;

import com.kpi.bot.server.frontend.data.ResponseBuilder;
import com.kpi.bot.utils.IndexingStatistics;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/stats")
public class AdminStatsController {
    @GetMapping("/messages")
    public Object getMessagesStats() {
        return ResponseBuilder.OK().add("stats", IndexingStatistics.getIndexingStatistics()).build();
    }
}

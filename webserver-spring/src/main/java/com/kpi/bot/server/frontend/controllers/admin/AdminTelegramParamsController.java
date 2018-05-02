package com.kpi.bot.server.frontend.controllers.admin;

import com.kpi.bot.server.frontend.data.ResponseBuilder;
import com.kpi.bot.services.loader.telegram.TelegramConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/telegram")
public class AdminTelegramParamsController {

    private TelegramConfiguration telegramConfiguration;

    @Autowired
    public AdminTelegramParamsController(TelegramConfiguration telegramConfiguration) {
        this.telegramConfiguration = telegramConfiguration;
    }

    @PutMapping("/auth/{code}")
    public Object setAuthCode(@PathVariable String code) {
        telegramConfiguration.setAuthCode(code);
        return ResponseBuilder.OK().build();
    }
}

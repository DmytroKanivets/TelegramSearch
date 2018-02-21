package com.kpi.bot.server.frontend.controllers;


import com.kpi.bot.server.backend.DataLoader;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataLoaderController {

    private DataLoader loader;

    public DataLoaderController(DataLoader loader) {
        this.loader = loader;
    }

    @RequestMapping("/data/load")
    public String loadData() {
        loader.loadData();
        return "Success";
    }
}

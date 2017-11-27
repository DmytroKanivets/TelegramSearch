package com.kpi.searchbot.controllers;


import com.kpi.searchbot.services.parser.DataLoader;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {

    private DataLoader loader;

    public DataController(DataLoader loader) {
        this.loader = loader;
    }

    @RequestMapping("/data/load")
    public String loadData() {
        loader.loadData();
        return "Success";
    }
}

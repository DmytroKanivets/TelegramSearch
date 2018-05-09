package com.kpi.bot.server.frontend.data;

import java.util.HashMap;
import java.util.Map;

public class ResponseBuilder {
    private Map<String, Object> response = new HashMap<>();

    private ResponseBuilder(String status) {
        response.put("status", status);
    }

    public ResponseBuilder add(String key, Object value) {
        response.put(key, value);
        return this;
    }

    public Map<String, Object> build() {
        return response;
    }

    public static ResponseBuilder OK() {
        return new ResponseBuilder("ok");
    }

    public static ResponseBuilder ERROR(String errorMessage) {
        return new ResponseBuilder("error").add("message", errorMessage);
    }

}

/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@RestController
@SpringBootApplication
public class Main {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }

    @Autowired
    DataSource dataSource;

    @RequestMapping("/")
    String index() {
        return "Hello!";
    }

    @RequestMapping("/api")
    String api() {
        return "api here";
    }

    @RequestMapping("/dto")
    Object dto() {
        return new HashMap<String, String>() {{
            put("key", "value");
            put("key2", "value2");
        }};
    }

    @RequestMapping("/db")
    List<String> db() throws SQLException {
        DatabaseMetaData md = dataSource.getConnection().getMetaData();
        ResultSet rs = md.getTables(null, null, "%", null);
        List<String> result = new LinkedList<>();
        while (rs.next()) {
            result.add(rs.getString(3));
        }

        return result;
    }
}

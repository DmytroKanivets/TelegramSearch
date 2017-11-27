package com.kpi.searchbot.entity.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Comment {
    Long postId;
    Long id;
    String name;
    String email;
    String body;
}

package com.example.jinhecitybussystem.entity.jsonEntity;

import lombok.Data;

import java.util.List;

@Data
public class Properties {
    private List<String> start;
    private List<String> end;
    private int time;
    private String line;
}

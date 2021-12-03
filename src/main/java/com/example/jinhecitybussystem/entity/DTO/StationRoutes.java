package com.example.jinhecitybussystem.entity.DTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StationRoutes {
    private Long id;
    private String name;
    private List<String> lines;

    public StationRoutes() {
        lines = new ArrayList<>();
    }
}

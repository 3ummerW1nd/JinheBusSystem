package com.example.jinhecitybussystem.entity.DTO;

import com.example.jinhecitybussystem.entity.jsonEntity.Route;
import com.example.jinhecitybussystem.entity.jsonEntity.TimeTable;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Property;

import java.util.List;

@Data
public class NewLineDTO {
    private boolean directional;
    private int interval;
    private float kilometer;
    private String name;
    private String onewayTime;
    private String route;
    private String runtime;
    private String type;
    private List<long[]> alongStations;
    private List<List<String>>[] timetable;
}

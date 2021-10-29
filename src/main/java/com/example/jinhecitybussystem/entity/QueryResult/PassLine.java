package com.example.jinhecitybussystem.entity.QueryResult;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;

import java.util.List;

public class PassLine {
    @Id
    @Property(name = "stationId")
    private long stationId;
    @Property(name = "routes")
    private List<String> routes;
}

package com.example.jinhecitybussystem.entity.jsonEntity;

import lombok.Data;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.util.List;

@Data
@RelationshipEntity(type = "next")
public class Next {
    @Property(name = "start") private Integer startNode;
    @Property(name = "end") private Integer endNode;
    private String type;
    private Properties properties;
}

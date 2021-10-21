package com.example.jinhecitybussystem.entity;

import lombok.Builder;
import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;

@NodeEntity
@Data
public class Station {
  @Id @Property(name = "id") private long id;
  @Property(name = "name") private String name;
  @Property(name = "english") private String english;
}

package com.example.jinhecitybussystem.entity;

import java.sql.Timestamp;
import java.util.List;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;

@NodeEntity
@Data
public class TimeTable {
  @Id @GeneratedValue private long id;
  @Property(name = "name") private String name;
  @Property(name = "timetable") private List<String> timetable;
}

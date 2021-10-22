package com.example.jinhecitybussystem.entity.jsonEntity;

import lombok.Builder;
import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;

@NodeEntity
@Data
public class Line {
  @Id @GeneratedValue private long id;
  @Property(name = "directional") private boolean directional;
  @Property(name = "interval") private int interval;
  @Property(name = "kilometer") private float kilometer;
  @Property(name = "name") private String name;
  @Property(name = "onewayTime") private String onewayTime;
  @Property(name = "route") private String route;
  @Property(name = "runtime") private String runtime;
  @Property(name = "type") private String type;
}

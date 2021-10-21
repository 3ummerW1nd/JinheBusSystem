package com.example.jinhecitybussystem.entity;

import lombok.Builder;
import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;

@Data
@NodeEntity
public class Route {
  private String name;
  private long[] alongStation;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long[] getAlongStation() {
    return alongStation;
  }

  public void setAlongStation(long[] alongStation) {
    this.alongStation = alongStation;
  }
}

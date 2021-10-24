package com.example.jinhecitybussystem.entity.DTO;

import com.example.jinhecitybussystem.entity.jsonEntity.Station;
import java.util.List;
import lombok.Data;

@Data
public class RouteDTO {
  private String name;
  private List<Station> stations;
  private int time;
}

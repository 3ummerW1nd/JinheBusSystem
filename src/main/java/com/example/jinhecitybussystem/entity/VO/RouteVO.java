package com.example.jinhecitybussystem.entity.VO;

import com.example.jinhecitybussystem.entity.jsonEntity.Station;
import java.util.List;
import lombok.Data;

@Data
public class RouteVO {
  private String name;
  private List<Station> stations;
  private int time;
}

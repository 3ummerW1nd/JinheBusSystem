package com.example.jinhecitybussystem.entity.VO;

import com.example.jinhecitybussystem.entity.jsonEntity.Station;
import lombok.Data;
import java.util.List;

@Data
public class RouteVO {
    private String name;
    private List<Station> stations;
    private int time;
}

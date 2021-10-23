package com.example.jinhecitybussystem.entity.VO;

import com.example.jinhecitybussystem.entity.jsonEntity.Station;
import lombok.Data;

@Data
public class StationPairVO {
    private Station start;
    private Station end;
    private int routeAmount;

    public StationPairVO(Station start, Station end, int routeAmount) {
        this.start = start;
        this.end = end;
        this.routeAmount = routeAmount;
    }
}

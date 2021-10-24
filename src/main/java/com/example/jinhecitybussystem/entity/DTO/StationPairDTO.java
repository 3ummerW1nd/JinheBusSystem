package com.example.jinhecitybussystem.entity.DTO;

import com.example.jinhecitybussystem.entity.jsonEntity.Station;
import lombok.Data;

@Data
public class StationPairDTO {
    private Station start;
    private Station end;
    private int routeAmount;

    public StationPairDTO(Station start, Station end, int routeAmount) {
        this.start = start;
        this.end = end;
        this.routeAmount = routeAmount;
    }
}

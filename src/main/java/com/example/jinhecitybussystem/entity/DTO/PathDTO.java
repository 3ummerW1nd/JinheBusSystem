package com.example.jinhecitybussystem.entity.DTO;

import com.example.jinhecitybussystem.entity.jsonEntity.Station;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PathDTO {
    @JsonProperty("next")
    List<String> next;
    @JsonProperty("stations")
    List<Station> stations;
    @JsonProperty("time")
    int time;

    public PathDTO(List<String> next, List<Station> stations, int time) {
        this.next = next;
        this.stations = stations;
        this.time = time;
    }
}

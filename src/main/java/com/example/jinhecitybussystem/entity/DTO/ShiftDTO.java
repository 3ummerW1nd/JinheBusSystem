package com.example.jinhecitybussystem.entity.DTO;

import com.example.jinhecitybussystem.entity.jsonEntity.Station;
import lombok.Data;

import java.util.List;

@Data
public class ShiftDTO {
    List<Station> stations;
    List<List<String>> timetable;

    public ShiftDTO(List<Station> stations, List<List<String>> timetable) {
        this.stations = stations;
        this.timetable = timetable;
    }
}

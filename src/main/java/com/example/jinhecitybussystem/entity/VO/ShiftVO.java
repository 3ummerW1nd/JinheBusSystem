package com.example.jinhecitybussystem.entity.VO;

import com.example.jinhecitybussystem.entity.jsonEntity.Station;
import lombok.Data;

import java.util.List;

@Data
public class ShiftVO {
    List<Station> stations;
    List<List<String>> timetable;

    public ShiftVO(List<Station> stations, List<List<String>> timetable) {
        this.stations = stations;
        this.timetable = timetable;
    }
}

package com.example.jinhecitybussystem.service;

import com.example.jinhecitybussystem.entity.Line;
import com.example.jinhecitybussystem.entity.Station;
import java.util.List;
import java.util.Map;

public interface LineService {
  Line findLineByName(String name);
  Map<Station, List<Line>> findLinesByStationName(String stationName);
}

package com.example.jinhecitybussystem.service;

import com.example.jinhecitybussystem.entity.jsonEntity.Line;
import com.example.jinhecitybussystem.entity.jsonEntity.Station;
import java.util.List;
import java.util.Map;

public interface LineService {
  Line findLineByName(String name);
  Map<Station, List<String>> findLinesByStationName(String stationName);
  List<Integer> findDifferentLinesCount();
}

package com.example.jinhecitybussystem.service;

import com.example.jinhecitybussystem.entity.jsonEntity.Line;
import com.example.jinhecitybussystem.entity.jsonEntity.Station;
import java.util.List;

public interface StationService {
  List<List<Station>> findStationsByLine(Line line);
  List<Station> findAllStations();
}

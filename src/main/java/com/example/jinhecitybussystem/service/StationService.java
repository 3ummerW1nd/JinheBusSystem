package com.example.jinhecitybussystem.service;

import com.example.jinhecitybussystem.entity.Line;
import com.example.jinhecitybussystem.entity.Station;
import java.util.List;

public interface StationService {
  List<List<Station>> findStationsByLine(Line line);
  List<Station> findAllStations();
}

package com.example.jinhecitybussystem.service;

import com.example.jinhecitybussystem.entity.DTO.StationPairDTO;
import com.example.jinhecitybussystem.entity.DTO.StationRoutes;
import com.example.jinhecitybussystem.entity.jsonEntity.Line;
import com.example.jinhecitybussystem.entity.jsonEntity.Station;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface StationService {
  List<List<Station>> findStationsByLine(Line line);
  List<Station> findStationsByRoute(String route);
  List<Station> findAllStations();
  List<StationRoutes> findStationsWithMostLines();
  List<String> findSubwayStations();
  List<String> findDepartureStations();
  List<String> findTerminalStations();
  List<String> findSingleStations(String lineName);
  Set<Station> findSameStationsByRouteNames(String lineName1, String lineName2);
  List<StationPairDTO> findMostRouteStationPairs();
}

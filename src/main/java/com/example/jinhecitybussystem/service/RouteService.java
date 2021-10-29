package com.example.jinhecitybussystem.service;

import com.example.jinhecitybussystem.entity.DTO.PathDTO;
import com.example.jinhecitybussystem.entity.DTO.RouteDTO;
import com.example.jinhecitybussystem.entity.DTO.ShiftDTO;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RouteService {
  RouteDTO findRouteByLineAndStation(String line, String start, String end);
  List<String> isDirect(String start, String end);
  Map<String, List<String>> findTransferRoutes(String routeName);
  List<Map.Entry<String, Integer>> findMostTransferRoutes();
  List<Map.Entry<String, Integer>> findMostStationsRoutes();
  List<Map.Entry<String, Integer>> findLongestRunTimeRoutes();
  List<String> findAllRoutes();
  PathDTO findShortestPathByStationIds(long startId, long endId);
  PathDTO findShortestPathByStationNames(String startName, String endName);
  ShiftDTO findShiftInformation(String route);
}

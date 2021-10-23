package com.example.jinhecitybussystem.service;

import com.example.jinhecitybussystem.entity.VO.StationPairVO;
import com.example.jinhecitybussystem.entity.jsonEntity.Line;
import com.example.jinhecitybussystem.entity.jsonEntity.Station;
import java.util.List;
import java.util.Map;

public interface StationService {
  List<List<Station>> findStationsByLine(Line line);
  List<Station> findAllStations();
  List<Map.Entry<Station, Integer>> findStationsWithMostOrLeastLines();
  List<Integer> findSpecialStationsCount();
  List<String> findSameStationsByLineNames(String lineName1, String lineName2);
  List<StationPairVO> findMostRouteStationPairs();
}

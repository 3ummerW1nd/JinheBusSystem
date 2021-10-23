package com.example.jinhecitybussystem.service;

import com.example.jinhecitybussystem.entity.VO.RouteVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RouteService {
  RouteVO findRouteByLineAndStation(String line, String start, String end);
  boolean isDirect(String start, String end);
  Set<String> findTransferRoutes(String routeName);
  List<Map.Entry<String, Integer>> findMostTransferRoutes();
  List<Map.Entry<String, Integer>> findMostStationsRoutes();
  List<String> findAllRoutes();
}

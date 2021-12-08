package com.example.jinhecitybussystem.service.serviceImpl;

import com.example.jinhecitybussystem.entity.DTO.StationPairDTO;
import com.example.jinhecitybussystem.entity.DTO.StationRoutes;
import com.example.jinhecitybussystem.entity.jsonEntity.Line;
import com.example.jinhecitybussystem.entity.jsonEntity.Station;
import com.example.jinhecitybussystem.repository.LineRepository;
import com.example.jinhecitybussystem.repository.StationRepository;
import com.example.jinhecitybussystem.service.StationService;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.neo4j.driver.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StationServiceImpl implements StationService {
  private StationRepository stationRepository;
  private LineRepository lineRepository;

  @Autowired
  public void setStationRepository(StationRepository stationRepository) {
    this.stationRepository = stationRepository;
  }
  @Autowired
  public void setLineRepository(LineRepository lineRepository) {
    this.lineRepository = lineRepository;
  }

  @Override
  public List<List<Station>> findStationsByLine(Line line) {
    List<List<Station>> answer = new ArrayList<>();
    StringBuffer lineName = new StringBuffer(line.getName());
    if (line.isDirectional()) {
      String upName = lineName.append("路上行").toString();
      lineName = new StringBuffer(line.getName());
      String downName = lineName.append("路下行").toString();
      answer.add(stationRepository.findRouteStationsByLineName(upName));
      answer.add(stationRepository.findRouteStationsByLineName(downName));
    } else {
      String routeName = lineName.append("路").toString();
      answer.add(stationRepository.findRouteStationsByLineName(routeName));
    }
    return answer;
  }

  @Override
  public List<Station> findStationsByRoute(String route) {
    return stationRepository.findRouteStationsByLineName(route);
  }

  @Override
  public List<Station> findAllStations() {
    return stationRepository.findAllStations();
  }

  @Override
  public List<StationRoutes> findStationsWithMostLines() {
    List<Value> lineList = stationRepository.findStationsWithMostLines();
    List<Object> allLineList = new ArrayList<>();
    List<StationRoutes> answer = new ArrayList<>();
    for (Value line : lineList) {
      List<Object> list = line.asList();
      allLineList.add(list);
    }
    for (Object obj : allLineList) {
      Collection<?> collection = (Collection<?>) obj;
      StationRoutes temp = new StationRoutes();
      for (Object o : collection) {
        if (o instanceof Long) {
          temp.setId((Long) o);
        } else {
          String judge = (String) o;
          if (judge.endsWith("路") || judge.endsWith("路上行") || judge.endsWith("路下行")) {
            temp.getLines().add(judge);
          } else {
            temp.setName(judge);
          }
        }
      }
      answer.add(temp);
    }
    return answer;
  }

  @Override
  public List<String> findSubwayStations() {
    return stationRepository.findSubwayStations();
  }

  @Override
  public List<String> findDepartureStations() {
    return stationRepository.findDepartureStations();
  }

  @Override
  public List<String> findTerminalStations() {
    return stationRepository.findTerminalStations();
  }

  @Override
  public List<String> findSingleStations(String lineName) {
    Line line = lineRepository.findByName(lineName);
    Set<String> singleStation = new HashSet<>();
    List<List<Station>> routes = findStationsByLine(line);
    if (routes.size() == 2) {
      Set<String> upRouteSet = new HashSet<>();
      Set<String> downRouteSet = new HashSet<>();
      List<Station> upRoute = routes.get(0);
      List<Station> downRoute = routes.get(1);
      for (Station station : upRoute) {
        upRouteSet.add(station.getName());
      }
      for (Station station : downRoute) {
        downRouteSet.add(station.getName());
      }
      System.out.println(upRouteSet);
      System.out.println(downRouteSet);
      Set<String> tmp = new HashSet<>(upRouteSet);
      tmp.removeAll(downRouteSet);
      singleStation.addAll(tmp);
      tmp = new HashSet<>(downRouteSet);
      tmp.removeAll(upRouteSet);
      singleStation.addAll(tmp);
      tmp.clear();
    }
    return new ArrayList<>(singleStation);
  }

  @Override
  public Set<Station> findSameStationsByRouteNames(String routeName1, String routeName2) {
    List<Station> routeStations1 = stationRepository.findRouteStationsByLineName(routeName1);
    List<Station> routeStations2 = stationRepository.findRouteStationsByLineName(routeName2);
    Set<Station> answer = new HashSet<>();
    for (Station s1 : routeStations1) {
      for (Station s2 : routeStations2) {
        if (s1.getName().equals(s2.getName())) {
          answer.add(s1);
        }
      }
    }
    return answer;
  }

  @Override
  public List<StationPairDTO> findMostRouteStationPairs() {
    List<StationPairDTO> answer = new ArrayList<>();
    List<Station> allStations = stationRepository.findAll();
    for (int i = 0; i < allStations.size() - 1; i++) {
      for (int j = i + 1; j < allStations.size(); j++) {
        Station start = allStations.get(i);
        Station end = allStations.get(j);
        StationPairDTO tmp = new StationPairDTO(
            start, end, lineRepository.findRoutesByStationIds(start.getId(), end.getId()).size());
      }
    }
    answer.sort(
        (a, b)
            -> Integer.valueOf(b.getRouteAmount()).compareTo(Integer.valueOf(a.getRouteAmount())));
    return answer.subList(0, 15);
  }
}

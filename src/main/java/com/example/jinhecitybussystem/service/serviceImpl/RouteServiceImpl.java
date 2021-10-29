package com.example.jinhecitybussystem.service.serviceImpl;

import com.example.jinhecitybussystem.entity.DTO.PathDTO;
import com.example.jinhecitybussystem.entity.DTO.RouteDTO;
import com.example.jinhecitybussystem.entity.DTO.ShiftDTO;
import com.example.jinhecitybussystem.entity.jsonEntity.Line;
import com.example.jinhecitybussystem.entity.jsonEntity.Station;
import com.example.jinhecitybussystem.repository.LineRepository;
import com.example.jinhecitybussystem.repository.StationRepository;
import com.example.jinhecitybussystem.service.LineService;
import com.example.jinhecitybussystem.service.RouteService;
import com.example.jinhecitybussystem.service.StationService;
import com.example.jinhecitybussystem.util.TimeUtil;
import java.util.*;
import org.neo4j.driver.internal.value.ListValue;
import org.neo4j.driver.internal.value.PathValue;
import org.neo4j.driver.types.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RouteServiceImpl implements RouteService {
  private StationService stationService;

  private LineService lineService;

  private StationRepository stationRepository;

  private LineRepository lineRepository;

  private final static String REGEX_CHINESE = "[\u4e00-\u9fa5]";

  @Autowired
  public void setStationService(StationService stationService) {
    this.stationService = stationService;
  }

  @Autowired
  public void setLineService(LineService lineService) {
    this.lineService = lineService;
  }

  @Autowired
  public void setStationRepository(StationRepository stationRepository) {
    this.stationRepository = stationRepository;
  }

  @Autowired
  public void setLineRepository(LineRepository lineRepository) {
    this.lineRepository = lineRepository;
  }

  @Override
  public RouteDTO findRouteByLineAndStation(String lineName, String start, String end) {
    RouteDTO answer = new RouteDTO();
    answer.setStations(new ArrayList<>());
    Line line = lineService.findLineByName(lineName);
    List<List<Station>> stations = stationService.findStationsByLine(line);
    if (stations.size() == 1) {
      answer.setName(lineName + "路");
      List<Station> route = stations.get(0);
      int startIndex = -1, endIndex = -1;
      for (int i = 0; i < route.size(); i++) {
        if (route.get(i).getName().equals(start))
          startIndex = i;
        if (route.get(i).getName().equals(end))
          endIndex = i;
      }
      for (int i = startIndex; i <= endIndex; i++) {
        answer.getStations().add(route.get(i));
      }
    } else {
      List<Station> upRoute = stations.get(0);
      List<Station> downRoute = stations.get(1);
      int startIndex = -1, endIndex = -1;
      for (int i = 0; i < upRoute.size(); i++) {
        if (upRoute.get(i).getName().equals(start))
          startIndex = i;
        if (upRoute.get(i).getName().equals(end))
          endIndex = i;
      }
      if (startIndex <= endIndex) {
        answer.setName(lineName + "路上行");
        for (int i = startIndex; i <= endIndex; i++) {
          answer.getStations().add(upRoute.get(i));
        }
      } else {
        answer.setName(lineName + "路下行");
        for (int i = 0; i < downRoute.size(); i++) {
          if (downRoute.get(i).getName().equals(start))
            startIndex = i;
          if (downRoute.get(i).getName().equals(end))
            endIndex = i;
        }
        for (int i = startIndex; i <= endIndex; i++) {
          answer.getStations().add(downRoute.get(i));
        }
      }
    }
    List<Station> answerRoute = answer.getStations();
    int cnt = answerRoute.size();
    String startTimeString =
        stationRepository
            .findTimetableByLineAndStartStations(answer.getName(), answerRoute.get(0).getName())
            .get(0)
            .get(0)
            .toString()
            .substring(1, 6);
    String endTimeString =
        stationRepository
            .findTimetableByLineAndEndStations(answer.getName(), answerRoute.get(cnt - 1).getName())
            .get(0)
            .get(0)
            .toString()
            .substring(1, 6);
    int time = TimeUtil.calculateTime(startTimeString, endTimeString);
    answer.setTime(time);
    return answer;
  }

  @Override
  public List<String> isDirect(String start, String end) {
    List<String> answer = new ArrayList<>();
    Map<Station, List<String>> map = lineService.findLinesByStationName(start);
    List<String> lineNames = new ArrayList<>();
    for (Map.Entry<Station, List<String>> entry : map.entrySet()) {
      lineNames.addAll(entry.getValue());
    }
    for (String lineName : lineNames) {
      List<Station> route = stationRepository.findRouteStationsByLineName(lineName);
      for (Station station : route) {
        if (station.getName().equals(end)) {
          answer.add(lineName);
        }
      }
    }
    if(answer.isEmpty())
      answer.add("无直达线路");
    return answer;
  }

  @Override
  public Map<String, List<String>> findTransferRoutes(String routeName) {
    Map<String, List<String>> map = new HashMap<>();
    List<Station> route = stationRepository.findRouteStationsByLineName(routeName);
    for (Station station : route) {
      Set<String> tmp = new HashSet<>(lineRepository.findLineNameByStationId(station.getId()));
      tmp.remove(routeName);
      if(tmp.size() != 0)
        map.put(station.getName(), new ArrayList<>(tmp));
    }
    return map;
  }

  @Override
  public List<Map.Entry<String, Integer>> findMostTransferRoutes() {
    List<String> allRoute = findAllRoutes();
    List<Map.Entry<String, Integer>> answer = new ArrayList<>();
    for (String route : allRoute) {
      answer.add(Map.entry(route, findTransferRoutes(route).size()));
    }
    answer.sort((a, b) -> b.getValue().compareTo(a.getValue()));
    return answer.subList(0, 15);
  }

  @Override
  public List<Map.Entry<String, Integer>> findMostStationsRoutes() {
    List<Map.Entry<String, Integer>> answer = new ArrayList<>();
    List<String> allRoute = findAllRoutes();
    for (String route : allRoute) {
      int cnt = stationRepository.findRouteStationsByLineName(route).size();
      answer.add(Map.entry(route, cnt));
    }
    answer.sort((a, b) -> b.getValue().compareTo(a.getValue()));
    List<Map.Entry<String, Integer>> limitedAnswer = new ArrayList<>();
    for (int i = 0; i < 15; i++) {
      limitedAnswer.add(answer.get(i));
    }
    return limitedAnswer;
  }

  @Override
  public List<Map.Entry<String, Integer>> findLongestRunTimeRoutes() {
    List<Map.Entry<String, Integer>> answer = new ArrayList<>();
    List<Map.Entry<String, Integer>> limitedAnswer = new ArrayList<>();
    List<String> routes = findAllRoutes();
    for (String route : routes) {
      List<Station> stations = stationRepository.findRouteStationsByLineName(route);
      String startTime =
          stationRepository.findTimetableByLineAndStartStations(route, stations.get(0).getName())
              .get(0)
              .get(0)
              .toString()
              .substring(1, 6);
      String endTime =
          stationRepository
              .findTimetableByLineAndEndStations(route, stations.get(stations.size() - 1).getName())
              .get(0)
              .get(0)
              .toString()
              .substring(1, 6);
      int time = TimeUtil.calculateTime(startTime, endTime);
      answer.add(Map.entry(route, time));
    }
    answer.sort((a, b) -> b.getValue().compareTo(a.getValue()));
    for (int i = 0; i < 15; i++) {
      limitedAnswer.add(answer.get(i));
    }
    return limitedAnswer;
  }

  @Override
  public List<String> findAllRoutes() {
    List<Line> allLines = lineRepository.findAll();
    List<String> allRoute = new ArrayList<>();
    for (Line line : allLines) {
      if (line.isDirectional()) {
        allRoute.add(line.getName() + "路上行");
        allRoute.add(line.getName() + "路下行");
      } else {
        allRoute.add(line.getName() + "路");
      }
    }
    return allRoute;
  }

  @Override
  public PathDTO findShortestPathByStationIds(long startId, long endId) {
    List<Object> objects = lineRepository.findShortestPathByStationIds(startId, endId);
    List<String> next = new ArrayList<>();
    List<Station> stations = new ArrayList<>();
    if (objects.size() == 0) {
      return new PathDTO(null, null, -1);
    }
    Object object = objects.get(0);
    PathValue pathValue = (PathValue) object;
    Iterator<Relationship> relationships = pathValue.asPath().relationships().iterator();
    boolean isStart = true;
    int time = 0;
    while (relationships.hasNext()) {
      Relationship relationship = relationships.next();
      long startNodeId = relationship.startNodeId();
      long endNodeId = relationship.endNodeId();
      if (isStart) {
        stations.add(stationRepository.findStationByInnerId(startNodeId));
        stations.add(stationRepository.findStationByInnerId(endNodeId));
        isStart = false;
      } else {
        stations.add(stationRepository.findStationByInnerId(endNodeId));
      }
      Iterator<String> relKeys = relationship.keys().iterator();
      while (relKeys.hasNext()) {
        String relKey = relKeys.next();
        if (relKey.equals("line")) {
          String relValue = relationship.get(relKey).asObject().toString();
          next.add(relValue);
        }
        if (relKey.equals("time")) {
          time += relationship.get(relKey).asInt();
        }
      }
    }
    return new PathDTO(next, stations, time);
  }

  @Override
  public PathDTO findShortestPathByStationNames(String startName, String endName) {
    PathDTO answer = new PathDTO();
    int minTime = Integer.MAX_VALUE;
    List<Station> startStations = stationRepository.findStationsByName(startName);
    List<Station> endStations = stationRepository.findStationsByName(endName);
    for(Station start : startStations) {
      for(Station end : endStations) {
        PathDTO tmp = findShortestPathByStationIds(start.getId(), end.getId());
        if(tmp.getTime() < minTime) {
          answer = tmp;
          minTime = tmp.getTime();
        }
      }
    }
    return answer;
  }

  @Override
  public ShiftDTO findShiftInformation(String route) {
    List<Station> stations = stationRepository.findRouteStationsByLineName(route);
    List<List<String>> timetable = new ArrayList<>();
    for (int i = 0; i < stations.size() - 1; i++) {
      List<String> time = new ArrayList<>();
      List<ListValue> tmp =
          stationRepository.findTimetableByLineAndStartStations(route, stations.get(i).getName());
      for (ListValue it : tmp) {
        for (Object o : it.asList()) time.add(o.toString());
      }
      timetable.add(time);
    }
    List<ListValue> tmp = stationRepository.findTimetableByLineAndEndStations(
        route, stations.get(stations.size() - 1).getName());
    List<String> time = new ArrayList<>();
    for (ListValue it : tmp) {
      for (Object o : it.asList()) time.add(o.toString());
    }
    timetable.add(time);
    return new ShiftDTO(stations, timetable);
  }
}

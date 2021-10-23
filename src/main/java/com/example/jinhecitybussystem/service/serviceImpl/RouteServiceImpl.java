package com.example.jinhecitybussystem.service.serviceImpl;

import com.example.jinhecitybussystem.entity.VO.RouteVO;
import com.example.jinhecitybussystem.entity.jsonEntity.Line;
import com.example.jinhecitybussystem.entity.jsonEntity.Station;
import com.example.jinhecitybussystem.repository.LineRepository;
import com.example.jinhecitybussystem.repository.StationRepository;
import com.example.jinhecitybussystem.service.LineService;
import com.example.jinhecitybussystem.service.RouteService;
import com.example.jinhecitybussystem.service.StationService;
import com.example.jinhecitybussystem.util.TimeUtil;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RouteServiceImpl implements RouteService {
  private StationService stationService;

  private LineService lineService;

  private StationRepository stationRepository;

  private LineRepository lineRepository;

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
  public RouteVO findRouteByLineAndStation(String lineName, String start, String end) {
    RouteVO answer = new RouteVO();
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
  public boolean isDirect(String start, String end) {
    Map<Station, List<String>> map = lineService.findLinesByStationName(start);
    List<String> lineNames = new ArrayList<>();
    for (Map.Entry<Station, List<String>> entry : map.entrySet()) {
      lineNames.addAll(entry.getValue());
    }
    for (String lineName : lineNames) {
      List<Station> route = stationRepository.findRouteStationsByLineName(lineName);
      for (Station station : route) {
        if (station.getName().equals(end)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public Set<String> findTransferRoutes(String routeName) {
    Set<String> answer = new HashSet<>();
    List<Station> route = stationRepository.findRouteStationsByLineName(routeName);
    for(int i = 0; i < route.size() - 1; i ++) {
      answer.addAll(lineRepository.findRoutesByStationNames(route.get(i).getId(), route.get(i + 1).getId()));
    }
    answer.remove(routeName);
    return answer;
  }
}

package com.example.jinhecitybussystem.service.serviceImpl;

import com.example.jinhecitybussystem.entity.jsonEntity.Line;
import com.example.jinhecitybussystem.entity.jsonEntity.Station;
import com.example.jinhecitybussystem.repository.LineRepository;
import com.example.jinhecitybussystem.repository.StationRepository;
import com.example.jinhecitybussystem.service.StationService;

import java.util.*;

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
  public List<Station> findAllStations() {
    return stationRepository.findAllStations();
  }

  @Override
  public List<Map.Entry<Station, Integer>> findStationsWithMostOrLeastLines() {
    List<Map.Entry<Station, Integer>> answer = new ArrayList<>();
    List<Map.Entry<Station, Integer>> list = new ArrayList<>();
    List<Station> allStations = findAllStations();
    for(Station station : allStations) {
      list.add(Map.entry(station, lineRepository.findLineCountByStationId(station.getId())));
    }
    Collections.sort(list,new Comparator<Map.Entry<Station,Integer>>() {
      @Override
      public int compare(Map.Entry<Station, Integer> o1, Map.Entry<Station, Integer> o2) {
        return o1.getValue().compareTo(o2.getValue());
      }
    });
    for(int i = 0; i < 15; i ++) {
      answer.add(list.get(i));
    }
    for(int i = list.size() - 1; i >= list.size() - 16; i --) {
      answer.add(list.get(i));
    }
    return answer;
  }

  @Override
  public List<Integer> findSpecialStationsCount() {
    List<Integer> answer = new ArrayList<>();
    answer.add(stationRepository.findSubwayStationCount());
    answer.add(stationRepository.findDepartureStationCount());
    answer.add(stationRepository.findTerminalStationCount());
    List<Line> allLines = lineRepository.findAll();
    Set<String> singleStation = new HashSet<>();
    for(Line line : allLines) {
      List<List<Station>> routes = findStationsByLine(line);
      if(routes.size() == 2) {
        Set<String> upRouteSet = new HashSet<>();
        Set<String> downRouteSet = new HashSet<>();
        Set<String> tmp = new HashSet<>();
        List<Station> upRoute = routes.get(0);
        List<Station> downRoute = routes.get(1);
        for(Station station : upRoute) {
          upRouteSet.add(station.getName());
        }
        for(Station station : downRoute) {
          downRouteSet.add(station.getName());
        }
        tmp.addAll(upRouteSet);
        tmp.removeAll(downRouteSet);
        singleStation.addAll(tmp);
        tmp.clear();
        tmp.addAll(downRouteSet);
        tmp.removeAll(upRoute);
        singleStation.addAll(tmp);
        tmp.clear();
      }
    }
//    for(String s : singleStation) {
//      System.out.println(s);
//    }
    answer.add(singleStation.size());
    return answer;
  }

  @Override
  public List<String> findSameStationsByLineNames(String lineName1, String lineName2) {
    List<String> answer = new ArrayList<>();
    Line line1 = lineRepository.findByName(lineName1);
    Line line2 = lineRepository.findByName(lineName2);
    List<List<Station>> line1Stations = findStationsByLine(line1);
    List<List<Station>> line2Stations = findStationsByLine(line2);
    for(List<Station> list1 : line1Stations) {
      for(Station line1Station : list1) {
        for(List<Station> list2 : line2Stations) {
          for(Station line2Station : list2) {
            if(line1Station.getName().equals(line2Station.getName())) {
              answer.add(line1Station.getName());
            }
          }
        }
      }
    }
    Set<String> tmp = new HashSet<>(answer);
    answer.clear();
    for(String s : tmp) {
      answer.add(s);
    }
    return answer;
  }
}

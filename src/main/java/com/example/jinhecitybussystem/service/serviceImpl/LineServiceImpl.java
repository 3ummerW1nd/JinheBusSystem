package com.example.jinhecitybussystem.service.serviceImpl;

import com.example.jinhecitybussystem.entity.jsonEntity.Line;
import com.example.jinhecitybussystem.entity.jsonEntity.Route;
import com.example.jinhecitybussystem.entity.jsonEntity.Station;
import com.example.jinhecitybussystem.entity.jsonEntity.TimeTable;
import com.example.jinhecitybussystem.repository.LineRepository;
import com.example.jinhecitybussystem.repository.StationRepository;
import com.example.jinhecitybussystem.service.LineService;
import com.example.jinhecitybussystem.service.RouteService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.jinhecitybussystem.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class LineServiceImpl implements LineService {
  private LineRepository lineRepository;
  private StationRepository stationRepository;
  @Autowired
  public void setLineRepository(LineRepository lineRepository) {
    this.lineRepository = lineRepository;
  }
  @Autowired
  public void setStationRepository(StationRepository stationRepository) {
    this.stationRepository = stationRepository;
  }

  private final static String REGEX_CHINESE = "[\u4e00-\u9fa5]"; // 中文正则
  @Override
  public Line findLineByName(String name) {
    return lineRepository.findByName(name);
  }

  @Override
  public Map<Station, List<String>> findLinesByStationName(String stationName) {
    //    Map<Station, List<Line>> answer = new HashMap<>();
    //    List<Station> stationList = stationRepository.findStationsByName(stationName);
    //    for(Station station : stationList) {
    //      List<String> lineNames= lineRepository.findLineNameByStationId(station.getId());
    //      List<Line> lines = new ArrayList<>();
    //      for(String it : lineNames) {
    //        it = it.replaceAll(REGEX_CHINESE, "");
    //        lines.add(lineRepository.findByName(it));
    //      }
    //      answer.put(station, lines);
    //    }
    //    return answer;
    Map<Station, List<String>> answer = new HashMap<>();
    List<Station> stationList = stationRepository.findStationsByName(stationName);
    for (Station station : stationList) {
      List<String> lineNames = lineRepository.findLineNameByStationId(station.getId());
      answer.put(station, lineNames);
    }
    return answer;
  }

  @Override
  public List<Integer> findDifferentLinesCount() {
    List<Integer> answer = new ArrayList<>();
    answer.add(lineRepository.findNormalLineCount());
    answer.add(lineRepository.findKLineCount());
    answer.add(lineRepository.findGLineCount());
    answer.add(lineRepository.findNLineCount());
    return answer;
  }

  @Override
  public void addNewLine(Line line, Route route, TimeTable timeTable) {
    lineRepository.save(line);
    String name = route.getName();
    long[] stations = route.getAlongStation();
    List<List<String>> timetables = timeTable.getTimetable();
    for (int j = 0; j < stations.length - 1; j++) {
      List<String> start = new ArrayList<>();
      List<String> end = new ArrayList<>();
      for (int k = 0; k < timetables.size(); k++) {
        start.add(timetables.get(k).get(j));
        end.add(timetables.get(k).get(j + 1));
      }
      int time = TimeUtil.calculateTime(start.get(0), end.get(0));
      stationRepository.buildRoute(name, stations[j], stations[j + 1], start, end, time);
    }
  }

  @Override
  public void deleteLine(Line line) {
    if(line.isDirectional()) {
      String upRoute = line.getName() + "路上行";
      lineRepository.deleteRoute(upRoute);
      String downRoute = line.getName() + "路下行";
      lineRepository.deleteRoute(downRoute);
    } else {
      String route = line.getName() + "路";
      lineRepository.deleteRoute(route);
    }
    stationRepository.deleteAllIsolatedStations();
  }
}

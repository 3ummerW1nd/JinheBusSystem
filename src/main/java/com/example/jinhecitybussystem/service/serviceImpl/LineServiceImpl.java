package com.example.jinhecitybussystem.service.serviceImpl;

import com.example.jinhecitybussystem.entity.jsonEntity.Line;
import com.example.jinhecitybussystem.entity.jsonEntity.Station;
import com.example.jinhecitybussystem.repository.LineRepository;
import com.example.jinhecitybussystem.repository.StationRepository;
import com.example.jinhecitybussystem.service.LineService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class LineServiceImpl implements LineService {
  @Autowired LineRepository lineRepository;
  @Autowired StationRepository stationRepository;
  private final static String REGEX_CHINESE = "[\u4e00-\u9fa5]";// 中文正则
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
    for(Station station : stationList) {
      List<String> lineNames= lineRepository.findLineNameByStationId(station.getId());
      answer.put(station, lineNames);
    }
    return answer;
  }
}

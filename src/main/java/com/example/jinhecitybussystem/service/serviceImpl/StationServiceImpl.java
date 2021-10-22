package com.example.jinhecitybussystem.service.serviceImpl;

import com.example.jinhecitybussystem.entity.jsonEntity.Line;
import com.example.jinhecitybussystem.entity.jsonEntity.Station;
import com.example.jinhecitybussystem.repository.StationRepository;
import com.example.jinhecitybussystem.service.StationService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StationServiceImpl implements StationService {
  @Autowired StationRepository stationRepository;

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
}

package com.example.jinhecitybussystem;

import com.alibaba.fastjson.JSON;
import com.example.jinhecitybussystem.entity.jsonEntity.Line;
import com.example.jinhecitybussystem.entity.jsonEntity.Route;
import com.example.jinhecitybussystem.entity.jsonEntity.Station;
import com.example.jinhecitybussystem.entity.jsonEntity.TimeTable;
import com.example.jinhecitybussystem.repository.LineRepository;
import com.example.jinhecitybussystem.repository.StationRepository;
import com.example.jinhecitybussystem.service.RouteService;
import com.example.jinhecitybussystem.service.StationService;
import com.example.jinhecitybussystem.util.FileUtil;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class JinheCityBusSystemApplicationTests {
  @Autowired StationRepository stationRepository;
  @Autowired LineRepository lineRepository;
  @Autowired RouteService routeService;
  @Autowired StationService stationService;

  @Test
  void saveAllStation() {
    String filePath = "src/main/resources/data/stations.json";
    String jsonContent = FileUtil.ReadFile(filePath);
    List<Station> list = JSON.parseArray(jsonContent, Station.class);
    for (Station it : list) {
      stationRepository.save(it);
    }
  }

  @Test
  void saveAllLine() {
    String filePath = "src/main/resources/data/lines.json";
    String jsonContent = FileUtil.ReadFile(filePath);
    List<Line> list = JSON.parseArray(jsonContent, Line.class);
    for (Line it : list) {
      lineRepository.save(it);
    }
  }

  @Test
  void saveAllRoute() {
    String filePath = "src/main/resources/data/routes.json";
    String routeJsonContent = FileUtil.ReadFile(filePath);
    List<Route> routeList = JSON.parseArray(routeJsonContent, Route.class);
    filePath = "src/main/resources/data/timetables.json";
    String timeTableJsonContent = FileUtil.ReadFile(filePath);
    List<TimeTable> timetableList = JSON.parseArray(timeTableJsonContent, TimeTable.class);
    for (int i = 0; i < routeList.size(); i++) {
      long[] stations = routeList.get(i).getAlongStation();
      List<List<String>> timetables = timetableList.get(i).getTimetable();
      String name = routeList.get(i).getName();
      for (int j = 0; j < stations.length - 1; j++) {
        List<String> start = new ArrayList<>();
        List<String> end = new ArrayList<>();
        for (int k = 0; k < timetables.size(); k++) {
          start.add(timetables.get(k).get(j));
          end.add(timetables.get(k).get(j + 1));
        }
        stationRepository.buildRoute(name, stations[j], stations[j + 1], start, end);
      }
    }
  }

  @Test
  void saveAllTime() {
    String filePath = "src/main/resources/data/timetables.json";
    String jsonContent = FileUtil.ReadFile(filePath);
    List<TimeTable> list = JSON.parseArray(jsonContent, TimeTable.class);
    System.out.println(list.size());
    for (TimeTable it : list) {
      for (List<String> l : it.getTimetable()) {
      }
    }
  }
  @Test
  void getTimetable() {
    System.out.println(stationRepository.findTimetableByLineAndStartStations("10路上行", "大悦城"));
  }

  @Test
  void getRouteByLineAndStation() {
    System.out.println(routeService.findRouteByLineAndStation("10", "大悦城", "小吃街"));
  }

  @Test
  void getMostAndLeast() {
    System.out.println(stationService.findStationsWithMostOrLeastLines());
  }

  @Test
  void getSpecialStationsCount() {
    System.out.println(stationService.findSpecialStationsCount());
  }

  @Test
  void getMostTransferRoutes() {
    System.out.println(routeService.findMostTransferRoutes());
  }

  @Test
  void getMostRouteStationPairs() {
    System.out.println(stationService.findMostRouteStationPairs());
  }

}

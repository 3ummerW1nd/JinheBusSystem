package com.example.jinhecitybussystem.service.serviceImpl;

import com.example.jinhecitybussystem.entity.DTO.LinesDTO;
import com.example.jinhecitybussystem.entity.DTO.NewLineDTO;
import com.example.jinhecitybussystem.entity.jsonEntity.*;
import com.example.jinhecitybussystem.repository.LineRepository;
import com.example.jinhecitybussystem.repository.StationRepository;
import com.example.jinhecitybussystem.service.LineService;
import com.example.jinhecitybussystem.service.RouteService;
import com.example.jinhecitybussystem.util.TimeUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.internal.value.ListValue;
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
  public void addNewLine(NewLineDTO newLineDTO) {}

  //  @Override
  //  public void addNewLine(NewLineDTO newLineDTO) {
  //    Line line = new Line();
  //
  //    lineRepository.save(line);
  //    String name = route.getName();
  //    long[] stations = route.getAlongStation();
  //    List<List<String>> timetables = timeTable.getTimetable();
  //    for (int j = 0; j < stations.length - 1; j++) {
  //      List<String> start = new ArrayList<>();
  //      List<String> end = new ArrayList<>();
  //      for (int k = 0; k < timetables.size(); k++) {
  //        start.add(timetables.get(k).get(j));
  //        end.add(timetables.get(k).get(j + 1));
  //      }
  //      int time = TimeUtil.calculateTime(start.get(0), end.get(0));
  //      stationRepository.buildRoute(name, stations[j], stations[j + 1], start, end, time);
  //    }
  //  }

  @Override
  public void deleteLine(Line line) {
    if (line.isDirectional()) {
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

  @Override
  public List<String> neoFindLinesByStationName(String stationName) {
    return lineRepository.findLinesByStationName(stationName);
  }


  //需求8
  @Override
  public Map<String, Integer> lineDocked(String now, Integer stationID, int time) {
    Map<String,Integer> map=new HashMap<>();
    Integer next_hour,next_minute;
    List<List<Object>> nexts=lineRepository.findEndById(stationID);
    String[] split = now.split(":");
    Integer now_hour=Integer.valueOf(split[0]);
    Integer now_minute=Integer.valueOf(split[1]);
    if(now_minute+time<60)
    {
      next_hour=now_hour;
      next_minute=now_minute+time;
    }
    else
    {
      next_minute=now_minute+time-60;
      next_hour=(now_hour+1)%24;
    }
//    System.out.println("now_hour: "+now_hour+"   now_minute: "+now_minute);
//    System.out.println("next_hour: "+next_hour+"   next_minute: "+next_minute);
//    System.out.println("==========================");
    for(Object next:nexts)
    {
      String endList_tmp =next.toString();
      String regex= String.valueOf(endList_tmp.charAt(2));
      String newList=endList_tmp.replace(regex,"").replace("[","").replace("]","").replace(" ","");
//      System.out.println(newList);
      String[] endList = newList.split(",");
      for (int i=1;i<endList.length;i++) {
        String s=endList[i];
        String[] split1 = s.split(":");
        Integer hour= Integer.parseInt(split1[0]);
        Integer minute=Integer.parseInt(split1[1]);
//        System.out.println("hour: "+hour+"   minute: "+minute);
        if(next_hour==0 && now_hour==23)
        {
          if(hour==23)
          {
            if(minute>=now_minute) map.put(endList[0],minute-now_minute);
          }
          if(hour==0)
          {
            if(minute<=next_minute) map.put(endList[0],minute-now_minute);
          }
        }
        else
        {
          if(hour==now_hour && minute>=now_minute)
          {
            if(hour==next_hour && minute<=next_minute)
            {
              map.put(endList[0],minute-now_minute);
            }
            if(hour<next_hour)
            {
              map.put(endList[0],minute-now_minute);
            }
          }
          if(hour>now_hour)
          {
            if(hour==next_hour && minute<=next_minute)
            {
              map.put(endList[0],minute-now_minute+60*(hour-now_hour));
            }
            if(hour<next_hour)
            {
              map.put(endList[0],minute-now_minute+60*(hour-now_hour));
            }
          }
        }
      }
    }
    return map;
  }

}

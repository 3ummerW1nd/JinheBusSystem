package com.example.jinhecitybussystem.service.serviceImpl;

import com.example.jinhecitybussystem.entity.DTO.NewLineDTO;
import com.example.jinhecitybussystem.entity.DTO.StationRoutes;
import com.example.jinhecitybussystem.entity.jsonEntity.Line;
import com.example.jinhecitybussystem.entity.jsonEntity.Station;
import com.example.jinhecitybussystem.repository.LineRepository;
import com.example.jinhecitybussystem.repository.StationRepository;
import com.example.jinhecitybussystem.service.LineService;

import java.util.*;

import org.neo4j.driver.Value;
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
  public List<StationRoutes> newFindLinesByStationName(String stationName) {
    List<Value> lineList = lineRepository.newFindLinesByStationName(stationName);
    List<Object> allLineList = new ArrayList<>();
    List<StationRoutes> answer = new ArrayList<>();
    for (Value line : lineList) {
      List<Object> list = line.asList();
      allLineList.add(list);
    }
    for(Object obj : allLineList) {
      Collection<?> collection = (Collection<?>) obj;
      StationRoutes temp = new StationRoutes();
      for(Object o : collection) {
        if(o instanceof Long) {
          temp.setId((Long) o);
        } else {
          temp.getLines().add((String) o);
        }
      }
      answer.add(temp);
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

  public void updateMap(Map<String,List<Integer>> map,String index,Integer update)
  {
    if(map.get(index)==null)
    {
      List<Integer> list=new ArrayList<>();
      list.add(update);
      map.put(index,list);
    }
    else
    {
      map.get(index).add(update);
    }
  }

  //需求8
  @Override
  public Map<String, List<Integer>> lineDocked(String now, Integer stationID, int time) {
    Map<String, List<Integer>> map = new HashMap<>();
    Integer next_hour, next_minute;
    List<List<Object>> nexts = lineRepository.findEndById(stationID);
    String[] split = now.split(":");
    Integer now_hour = Integer.valueOf(split[0]);
    Integer now_minute = Integer.valueOf(split[1]);
    if(now_minute + time < 60)
    {
      next_hour = now_hour;
      next_minute = now_minute + time;
    }
    else
    {
      next_minute = now_minute + time - 60;
      next_hour = (now_hour + 1) % 24;
    }
    for(Object next : nexts)
    {
      String endList_tmp = next.toString();
      String regex = String.valueOf(endList_tmp.charAt(2));
//      System.out.println(endList_tmp);
      String newList = endList_tmp.replace(regex,"").replace("[","").replace("]","").replace(" ","");
//      System.out.println(newList);
      String[] endList = newList.split(",");
      for (int i = 1; i < endList.length; i++) {
        String s = endList[i];
        String[] split1 = s.split(":");
        Integer hour = Integer.parseInt(split1[0]);
        Integer minute = Integer.parseInt(split1[1]);
        if(next_hour == 0 && now_hour == 23)
        {
          if(hour == 23)
          {
            if(minute >= now_minute) updateMap(map,endList[0],minute  -now_minute);
          }
          if(hour == 0)
          {
            if(minute <= next_minute) updateMap(map,endList[0],minute  -now_minute);
          }
        }
        else
        {
          if(hour == now_hour && minute >= now_minute)
          {
            if(hour == next_hour && minute <= next_minute)
            {
              updateMap(map,endList[0],minute  -now_minute);
            }
            if(hour < next_hour)
            {
              updateMap(map,endList[0],minute  -now_minute);
            }
          }
          if(hour > now_hour)
          {
            if(hour == next_hour && minute <= next_minute)
            {
              updateMap(map,endList[0],minute - now_minute + 60 * (hour - now_hour));
            }
            if(hour < next_hour)
            {
              updateMap(map,endList[0],minute - now_minute + 60 * (hour - now_hour));
            }
          }
        }
      }
    }
    return map;
  }

  //需求9
  @Override
  public Map<String, Integer> shiftsDocked(String now, Integer stationID) {
    Map<String, Integer> map = new HashMap<>();
    Integer next_hour, next_minute;
    List<List<Object>> nexts = lineRepository.findEndById(stationID);
    String[] split = now.split(":");
    Integer now_hour = Integer.valueOf(split[0]);
    Integer now_minute = Integer.valueOf(split[1]);
    for(Object next : nexts)
    {
      String endList_tmp = next.toString();
      String regex = String.valueOf(endList_tmp.charAt(2));
      String newList=endList_tmp.replace(regex,"").replace("[","").replace("]","").replace(" ","");
      String[] endList = newList.split(",");
      int j = 1;

      for (int i = 1; i < endList.length; i++) {
        String s = endList[i];
        String[] split1 = s.split(":");
        Integer hour = Integer.parseInt(split1[0]);
        Integer minute = Integer.parseInt(split1[1]);

        if ((hour > now_hour) && j < 4) {
          map.put(endList[0] + "班次" + String.valueOf(j), minute - now_minute + 60 * (hour - now_hour));
          j++;
        }
        else if ((hour == now_hour && minute >= now_minute) && j < 4) {
          map.put(endList[0] + "班次" + String.valueOf(j), minute - now_minute);
          j++;
        }
        else if ((hour < now_hour) && j != 1 && j < 4) {
          map.put(endList[0] + "班次" + String.valueOf(j), (24 - now_hour + hour) * 60 + minute - now_minute);
          j++;
        }
      }
    }
    return map;
  }

}

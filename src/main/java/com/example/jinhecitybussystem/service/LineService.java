package com.example.jinhecitybussystem.service;

import com.example.jinhecitybussystem.entity.DTO.LinesDTO;
import com.example.jinhecitybussystem.entity.DTO.NewLineDTO;
import com.example.jinhecitybussystem.entity.jsonEntity.Line;
import com.example.jinhecitybussystem.entity.jsonEntity.Route;
import com.example.jinhecitybussystem.entity.jsonEntity.Station;
import com.example.jinhecitybussystem.entity.jsonEntity.TimeTable;
import org.neo4j.driver.internal.value.ListValue;

import java.util.List;
import java.util.Map;

public interface LineService {
  Line findLineByName(String name);
  Map<Station, List<String>> findLinesByStationName(String stationName);
  List<Integer> findDifferentLinesCount();
  void addNewLine(NewLineDTO newLineDTO);
  void deleteLine(Line line);
  List<String> neoFindLinesByStationName(String stationName);

  Map<String,Integer> lineDocked(String now,Integer stationID, int time);


}

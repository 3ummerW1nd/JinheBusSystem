package com.example.jinhecitybussystem.controller;

import com.example.jinhecitybussystem.entity.jsonEntity.Line;
import com.example.jinhecitybussystem.entity.jsonEntity.Station;
import com.example.jinhecitybussystem.service.StationService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class StationController {
  private StationService stationService;
  @Autowired
  public void setStationService(StationService stationService) {
    this.stationService = stationService;
  }

  @ResponseBody
  @PostMapping("/station/getRouteStations")
  public List<List<Station>> getRouteStations(@RequestBody Line line) {
    return stationService.findStationsByLine(line);
  }

  @ResponseBody
  @GetMapping("/station/getAllStations")
  public List<Station> getAllStations() {
    return stationService.findAllStations();
  }
  // TODO:分页

  @ResponseBody
  @GetMapping("/station/getStationsWithMostAndLeastLines")
  public List<Map.Entry<Station, Integer>> getStationsWithMostAndLeastLines() {
    return stationService.findStationsWithMostOrLeastLines();
  }

  @ResponseBody
  @GetMapping("/station/getSpecialStationsCount")
  public List<Integer> getSpecialStationsCount() {
    return stationService.findSpecialStationsCount();
  }

  @ResponseBody
  @GetMapping("/station/getSameStations")
  public List<String> getSameStation(
      @RequestParam("lineName1") String lineName1, @RequestParam("lineName2") String lineName2) {
    return stationService.findSameStationsByLineNames(lineName1, lineName2);
  }
}

package com.example.jinhecitybussystem.controller;

import com.example.jinhecitybussystem.entity.DTO.StationRoutes;
import com.example.jinhecitybussystem.entity.jsonEntity.Station;
import com.example.jinhecitybussystem.service.StationService;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
  @GetMapping("/station/getRouteStations")
  public List<Station> getRouteStations(@RequestParam("routeName") String route) {
    return stationService.findStationsByRoute(route);
  }

  @ResponseBody
  @GetMapping("/station/getStationsWithMostLines")
  public List<StationRoutes> getStationsWithMostLines() {
    return stationService.findStationsWithMostLines();
  }

  @ResponseBody
  @GetMapping("/station/getSubwayStations")
  public List<String> getSubwayStations() {
    return stationService.findSubwayStations();
  }

  @ResponseBody
  @GetMapping("/station/getDepartureStations")
  public List<String> getDepartureStations() {
    return stationService.findDepartureStations();
  }

  @ResponseBody
  @GetMapping("/station/getTerminalStations")
  public List<String> getTerminalStations() {
    return stationService.findTerminalStations();
  }

  @ResponseBody
  @GetMapping("/station/getSingleStations")
  public List<String> getSingleStations(String lineName) {
    return stationService.findSingleStations(lineName);
  }

  @ResponseBody
  @GetMapping("/station/getSameStations")
  public Set<String> getSameStation(
      @RequestParam("routeName1") String routeName1, @RequestParam("routeName2") String routeName2) {
    return stationService.findSameStationsByRouteNames(routeName1, routeName2);
  }
}

package com.example.jinhecitybussystem.controller;

import com.example.jinhecitybussystem.entity.DTO.PathDTO;
import com.example.jinhecitybussystem.entity.DTO.RouteDTO;
import com.example.jinhecitybussystem.entity.DTO.ShiftDTO;
import com.example.jinhecitybussystem.service.RouteService;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class RouteController {
  private RouteService routeService;

  @Autowired
  public void setRouteService(RouteService routeService) {
    this.routeService = routeService;
  }

  @ResponseBody
  @GetMapping("/route/getRouteByLine")
  public RouteDTO getRouteByLine(@RequestParam("line") String line,
      @RequestParam("start") String start, @RequestParam("end") String end) {
    return routeService.findRouteByLineAndStation(line, start, end);
  }

  @ResponseBody
  @GetMapping("/route/direct")
  public List<String> isDirect(@RequestParam("start") String start, @RequestParam("end") String end) {
    return routeService.isDirect(start, end);
  }

  @ResponseBody
  @GetMapping("/route/getTransferLines")
  public Map<String, List<String>> getTransferLines(@RequestParam("route") String routeName) {
    return routeService.findTransferRoutes(routeName);
  }

  @ResponseBody
  @GetMapping("/route/getMostTransferRoutes")
  public List<Map.Entry<String, Integer>> getMostTransferRoutes() {
    return routeService.findMostTransferRoutes();
  }

  @ResponseBody
  @GetMapping("/route/getMostStationsRoutes")
  public List<Map.Entry<String, Integer>> getMostStationsRoutes() {
    return routeService.findMostStationsRoutes();
  }

  @ResponseBody
  @GetMapping("/route/getLongestRunTimeRoutes")
  public List<Map.Entry<String, Integer>> getLongestRunTimeRoutes() {
    return routeService.findLongestRunTimeRoutes();
  }

  @ResponseBody
  @GetMapping("/route/getShortestPath")
  public PathDTO getShortestPath(
      @RequestParam("startId") long startId, @RequestParam("endId") long endId) {
    return routeService.findShortestPathByStationIds(startId, endId);
  }

  @ResponseBody
  @GetMapping("/route/getShortestPathByNames")
  public PathDTO getShortestPathBynames(
          @RequestParam("startName") String startName, @RequestParam("endName") String endName) {
    return routeService.findShortestPathByStationNames(startName, endName);
  }

  @ResponseBody
  @GetMapping("/route/getShiftInformation")
  public ShiftDTO getShiftInformation(@RequestParam("route") String route) {
    return routeService.findShiftInformation(route);
  }
}

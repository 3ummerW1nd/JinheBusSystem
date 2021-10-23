package com.example.jinhecitybussystem.controller;

import com.example.jinhecitybussystem.entity.jsonEntity.Line;
import com.example.jinhecitybussystem.entity.jsonEntity.Station;
import com.example.jinhecitybussystem.service.LineService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Api
@Controller
public class LineController {
  private LineService lineService;
  @Autowired
  public void setLineService(LineService lineService) {
    this.lineService = lineService;
  }

  @ResponseBody
  @GetMapping("/line/getLine")
  public Line getLine(@RequestParam("name") String name) {
    return lineService.findLineByName(name);
  }

  @ResponseBody
  @GetMapping("/line/getLinesByStationName")
  public Map<Station, List<String>> getLinesByStationName(@RequestParam("stationName") String stationName) {
    return lineService.findLinesByStationName(stationName);
  }
}

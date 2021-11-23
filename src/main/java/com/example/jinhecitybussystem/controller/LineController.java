package com.example.jinhecitybussystem.controller;

import com.example.jinhecitybussystem.entity.DTO.NewLineDTO;
import com.example.jinhecitybussystem.entity.jsonEntity.Line;
import com.example.jinhecitybussystem.entity.jsonEntity.Station;
import com.example.jinhecitybussystem.service.LineService;
import io.swagger.annotations.Api;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
  public Map<Station, List<String>> getLinesByStationName(
      @RequestParam("stationName") String stationName) {
    return lineService.findLinesByStationName(stationName);
  }

  @ResponseBody
  @GetMapping("/line/getLinesByStationNameNew")
  public  List<String> getLinesByStationNameNew(
          @RequestParam("stationName") String stationName) {
    return lineService.neoFindLinesByStationName(stationName);
  }

  @ResponseBody
  @GetMapping("/line/getDifferentLinesCount")
  public List<Integer> getDifferentLinesCount() {
    return lineService.findDifferentLinesCount();
  }

  @ResponseBody
  @PostMapping("/line/deleteLine")
  public void deleteLine(@RequestBody Line line) {
    lineService.deleteLine(line);
  }

  @ResponseBody
  @PostMapping("/line/addLine")
  public void addLine(@RequestBody NewLineDTO newLineDTO) {
    lineService.addNewLine(newLineDTO);
  }

}

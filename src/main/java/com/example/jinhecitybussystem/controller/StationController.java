package com.example.jinhecitybussystem.controller;

import com.example.jinhecitybussystem.entity.jsonEntity.Line;
import com.example.jinhecitybussystem.entity.jsonEntity.Station;
import com.example.jinhecitybussystem.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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



}

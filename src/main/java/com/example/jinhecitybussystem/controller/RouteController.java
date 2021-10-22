package com.example.jinhecitybussystem.controller;

import com.example.jinhecitybussystem.entity.VO.RouteVO;
import com.example.jinhecitybussystem.entity.jsonEntity.Line;
import com.example.jinhecitybussystem.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RouteController {
    private RouteService routeService;

    @Autowired
    public void setInjectedBean(RouteService routeService) {
        this.routeService = routeService;
    }

    @ResponseBody
    @GetMapping("/route/getRouteByLine")
    public RouteVO getLine(@RequestParam("line") String line, @RequestParam("start") String start, @RequestParam("end") String end) {
        return routeService.findRouteByLineAndStation(line, start, end);
    }

}

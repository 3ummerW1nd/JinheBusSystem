package com.example.jinhecitybussystem.service;

import com.example.jinhecitybussystem.entity.VO.RouteVO;

public interface RouteService {
    public RouteVO findRouteByLineAndStation(String line, String start, String end);
}

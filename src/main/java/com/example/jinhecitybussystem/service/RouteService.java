package com.example.jinhecitybussystem.service;

import com.example.jinhecitybussystem.entity.VO.RouteVO;

public interface RouteService {
    RouteVO findRouteByLineAndStation(String line, String start, String end);
    boolean isDirect(String start, String end);
}

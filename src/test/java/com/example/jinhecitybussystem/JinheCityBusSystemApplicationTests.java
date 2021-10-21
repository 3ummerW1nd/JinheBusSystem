package com.example.jinhecitybussystem;

import com.alibaba.fastjson.JSON;
import com.example.jinhecitybussystem.entity.*;
import com.example.jinhecitybussystem.repository.LineRepository;
import com.example.jinhecitybussystem.repository.StationRepository;
import com.example.jinhecitybussystem.repository.TimeTableRepository;
import com.example.jinhecitybussystem.util.FileUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class JinheCityBusSystemApplicationTests {
    @Autowired
    StationRepository stationRepository;
    @Autowired
    LineRepository lineRepository;
    @Autowired
    TimeTableRepository timeTableRepository;

    @Test
    void saveAllStation() {
        String filePath = "src/main/resources/data/stations.json";
        String jsonContent = FileUtil.ReadFile(filePath);
        List<Station> list = JSON.parseArray(jsonContent,Station.class);
        for(Station it : list) {
            stationRepository.save(it);
        }
    }

    @Test
    void saveAllLine() {
        String filePath = "src/main/resources/data/lines.json";
        String jsonContent = FileUtil.ReadFile(filePath);
        List<Line> list = JSON.parseArray(jsonContent,Line.class);
        for(Line it : list) {
            lineRepository.save(it);
        }
    }

    @Test
    void saveAllRoute() {
        String filePath = "src/main/resources/data/routes.json";
        String jsonContent = FileUtil.ReadFile(filePath);
        List<Route> list = JSON.parseArray(jsonContent,Route.class);
        for(Route it : list) {
            long[] stations = it.getAlongStation();
            for(int i = 0; i < stations.length - 1; i ++) {
                stationRepository.buildRoute(it.getName(), stations[i], stations[i + 1]);
            }
        }
    }
}

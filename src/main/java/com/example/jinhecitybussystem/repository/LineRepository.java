package com.example.jinhecitybussystem.repository;

import com.example.jinhecitybussystem.entity.Line;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LineRepository extends Neo4jRepository<Line, Long> {
  Line findByName(String name);
  @Query("MATCH (s:Station)-[r:next]->() WHERE s.id = $stationId  RETURN r.line")
  List<String> findLineNameByStationId(@Param(value = "stationId")long stationId);
}

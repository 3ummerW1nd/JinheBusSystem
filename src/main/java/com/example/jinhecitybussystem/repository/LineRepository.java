package com.example.jinhecitybussystem.repository;

import com.example.jinhecitybussystem.entity.jsonEntity.Line;
import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface LineRepository extends Neo4jRepository<Line, Long> {
  Line findByName(String name);
  @Query(
      "MATCH (s:Station)-[r:next]->(ss:Station) WHERE s.id = $stationId OR ss.id = $stationId  RETURN DISTINCT r.line")
  List<String>
  findLineNameByStationId(@Param(value = "stationId") long stationId);

  @Query(
          "MATCH (s:Station)-[r:next]->(ss:Station) WHERE s.id = $stationId OR ss.id = $stationId  RETURN COUNT (DISTINCT r.line)")
  int
  findLineCountByStationId(@Param(value = "stationId") long stationId);
}

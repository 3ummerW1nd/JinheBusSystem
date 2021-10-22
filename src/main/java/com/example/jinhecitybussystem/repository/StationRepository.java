package com.example.jinhecitybussystem.repository;

import com.example.jinhecitybussystem.entity.jsonEntity.Station;
import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface StationRepository extends Neo4jRepository<Station, Long> {
  List<Station> findStationsByName(String name);

  Station findById(long Id);

  @Query(
      "MATCH (fromStation) WHERE fromStation.id = $fromId MATCH (toStation) WHERE toStation.id = $toId CREATE (fromStation)-[:next{line:$name, start:$start, end:$end}]->(toStation)")
  void
  buildRoute(@Param(value = "name") String name, @Param(value = "fromId") long fromId,
      @Param(value = "toId") long toId, @Param(value = "start") List<String> start, @Param(value = "end") List<String> end);

  @Query("MATCH p=(start:Station)-[r:next{line:$lineName}]->(end:Station) RETURN p ORDER BY id(r)")
  List<Station> findRouteStationsByLineName(@Param(value = "lineName") String name);

  @Query("MATCH (n:Station) RETURN n ORDER BY n.english")
  List<Station> findAllStations();
}

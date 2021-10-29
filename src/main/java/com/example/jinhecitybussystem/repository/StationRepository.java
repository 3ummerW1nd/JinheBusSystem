package com.example.jinhecitybussystem.repository;

import com.example.jinhecitybussystem.entity.jsonEntity.Station;
import java.util.List;
import org.neo4j.driver.internal.value.ListValue;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface StationRepository extends Neo4jRepository<Station, Long> {
  List<Station> findStationsByName(String name);
  @Query("MATCH (start:Station) WHERE id(start) = $id RETURN start")
  Station findStationByInnerId(@Param(value = "id") long id);
  @Query(
      "MATCH (fromStation) WHERE fromStation.id = $fromId MATCH (toStation) WHERE toStation.id = $toId CREATE (fromStation)-[:next{line:$name, start:$start, end:$end, time:$time}]->(toStation)")
  void
  buildRoute(@Param(value = "name") String name, @Param(value = "fromId") long fromId,
      @Param(value = "toId") long toId, @Param(value = "start") List<String> start,
      @Param(value = "end") List<String> end, @Param(value = "time") int time);

  //需求2
  @Query("MATCH p=(start:Station)-[r:next{line:$lineName}]->(end:Station) RETURN p ORDER BY id(r)")
  List<Station> findRouteStationsByLineName(@Param(value = "lineName") String name);

  @Query("MATCH (n:Station) RETURN n ORDER BY n.english") List<Station> findAllStations();

  @Query(
      "MATCH p=(start:Station{name:$stationName})-[r:next{line:$lineName}]->(end:Station) RETURN r.start")
  List<ListValue>
  findTimetableByLineAndStartStations(
      @Param(value = "lineName") String lineName, @Param(value = "stationName") String stationName);

  @Query(
      "MATCH p=(start:Station)-[r:next{line:$lineName}]->(end:Station{name:$stationName}) RETURN r.end")
  List<ListValue>
  findTimetableByLineAndEndStations(
      @Param(value = "lineName") String lineName, @Param(value = "stationName") String stationName);
  @Query("MATCH (s:Station) where s.name =~ '地铁.*' return DISTINCT s.name")
  List<String> findSubwayStations();

  @Query("MATCH (s:Station) where s.name =~ '.*始发站.*' return DISTINCT s.name")
  List<String> findDepartureStations();

  @Query("MATCH (s:Station) where s.name =~ '.*终点站.*' return DISTINCT s.name")
  List<String> findTerminalStations();

  @Query("MATCH (n:Station) WHERE NOT (n)--() DELETE n")
  List<String> deleteAllIsolatedStations();

}

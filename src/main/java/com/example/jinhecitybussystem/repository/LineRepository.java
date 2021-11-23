package com.example.jinhecitybussystem.repository;

import com.example.jinhecitybussystem.entity.DTO.LinesDTO;
import com.example.jinhecitybussystem.entity.jsonEntity.Line;
import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface LineRepository extends Neo4jRepository<Line, Long> {
  //需求1
  Line findByName(String name);
  //需求3（需要修改，目前较为低效）
  @Query(
          "MATCH (s:Station)-[r:next]->(ss:Station) WHERE s.id = $stationId OR ss.id = $stationId  RETURN DISTINCT r.line")
  List<String>
  findLineNameByStationId(@Param(value = "stationId") long stationId);
  @Query("MATCH (s:Station)-[r:next]->(ss:Station) WHERE s.id = $stationId OR ss.id = $stationId  RETURN COUNT (DISTINCT r.line)")
  int findLineCountByStationId(@Param(value = "stationId") long stationId);
  @Query("MATCH (l:Line) WHERE l.type = \"干线\" OR l.type = \"支线\" OR l.type = \"城乡线\" OR l.type = \"驳接线\" OR l.type = \"社区线\" RETURN COUNT (DISTINCT l)")
  int findNormalLineCount();
  @Query("MATCH (l:Line) WHERE l.type = \"快速公交\" RETURN COUNT (DISTINCT l)")
  int findKLineCount();
  @Query("MATCH (l:Line) WHERE l.type = \"高峰线\" RETURN COUNT (DISTINCT l)") int findGLineCount();
  @Query("MATCH (l:Line) WHERE l.type = \"夜班线\" RETURN COUNT (DISTINCT l)") int findNLineCount();
  @Query(
      "MATCH p=(start:Station)-[r:next]->(end:Station) WHERE start.id = $id1 AND end.id = $id2 RETURN DISTINCT r.line")
  List<String>
  findRoutesByStationIds(@Param(value = "id1") long id1, @Param(value = "id2") long id2);
  @Query("MATCH (a:Station) WITH a MATCH (b:Station) WHERE a.id = $id1 AND b.id = $id2\n"
      + "CALL apoc.algo.dijkstra(a,b,\">\",\"time\")YIELD path, weight\n"
      + "RETURN path")
  List<Object>
  findShortestPathByStationIds(@Param(value = "id1") long id1, @Param(value = "id2") long id2);
  @Query("MATCH p=()-[r:next{line:$route}]->() DETACH DELETE r")
  void deleteRoute(@Param(value = "route") String route);
  @Query("match (s:Station{name:$stationName})-[r:next]-(:Station) return distinct s.id+collect(r.line) as lines")
  List<String> findLinesByStationName(@Param(value = "stationName") String stationName);
}

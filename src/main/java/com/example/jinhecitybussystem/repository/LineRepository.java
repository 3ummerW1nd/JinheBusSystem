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

  @Query("MATCH (s:Station)-[r:next]->(ss:Station) WHERE s.id = $stationId OR ss.id = $stationId  RETURN COUNT (DISTINCT r.line)")
  int findLineCountByStationId(@Param(value = "stationId") long stationId);
  @Query("MATCH (l:Line) WHERE l.type = \"干线\" OR l.type = \"支线\" OR l.type = \"城乡线\" OR l.type = \"驳接线\" OR l.type = \"社区线\" RETURN COUNT (DISTINCT l)")
  int findNormalLineCount();
  @Query("MATCH (l:Line) WHERE l.type = \"快速公交\" RETURN COUNT (DISTINCT l)")
  int findKLineCount();
  @Query("MATCH (l:Line) WHERE l.type = \"高峰线\" RETURN COUNT (DISTINCT l)") int findGLineCount();
  @Query("MATCH (l:Line) WHERE l.type = \"夜班线\" RETURN COUNT (DISTINCT l)") int findNLineCount();
  @Query("MATCH p=(start:Station)-[r:next]->(end:Station) WHERE start.id = $id1 AND end.id = $id2 RETURN DISTINCT r.line")
  List<String> findRoutesByStationNames(@Param(value = "id1")long id1, @Param(value = "id2")long id2);
}

package com.example.jinhecitybussystem.repository;

import com.example.jinhecitybussystem.entity.TimeTable;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface TimeTableRepository extends Neo4jRepository<TimeTable, Long> {}

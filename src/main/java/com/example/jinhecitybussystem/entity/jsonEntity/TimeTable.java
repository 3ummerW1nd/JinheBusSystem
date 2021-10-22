package com.example.jinhecitybussystem.entity.jsonEntity;
import java.util.List;

import lombok.Data;

@Data
public class TimeTable {
  private long id;
  private String name;
  private List<List<String>> timetable;
}

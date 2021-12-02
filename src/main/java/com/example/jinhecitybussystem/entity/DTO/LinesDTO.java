package com.example.jinhecitybussystem.entity.DTO;

import lombok.Data;

import java.util.List;

/**
 * @program: JinheCityBusSystem
 * @description:
 * @author: 3ummerW1nd
 * @create: 2021-11-24 00:22
 **/
@Data
public class LinesDTO {
  private long id;
  private List<String> lines;
}

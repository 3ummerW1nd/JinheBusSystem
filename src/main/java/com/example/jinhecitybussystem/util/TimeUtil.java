package com.example.jinhecitybussystem.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
  public static int calculateTime(String start, String end) {
    DateFormat df = new SimpleDateFormat("HH:mm");
    int minutes = 0;
    try {
      Date d1 = df.parse(end);
      Date d2 = df.parse(start);
      int diff = (int) (d1.getTime() - d2.getTime());
      minutes = diff / (1000 * 60);
    } catch (ParseException e) {
      System.out.println("抱歉，时间日期解析出错。");
    }
    return minutes;
  }
}

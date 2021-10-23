package com.example.jinhecitybussystem.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUtil {
  public static int calculateTime(String start, String end) {
    DateFormat df = new SimpleDateFormat("HH:mm");
    int minutes = 0;
    try {
      Date d1 = df.parse(end);
      Date d2 = df.parse(start);
      if(d1.before(d2)) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(d1);
        calendar.add(calendar.DATE,1); //把日期往后增加一天,整数  往后推,负数往前移动
        d1=calendar.getTime(); //这个时间就是日期往后推一天的结
      }
      int diff = (int) (d1.getTime() - d2.getTime());
      minutes = diff / (1000 * 60);
    } catch (ParseException e) {
      System.out.println("抱歉，时间日期解析出错。");
    }
    return minutes;
  }
}

package com.example.springtest.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeConvert {

    //Date转yyyy-MM-dd HH:mm:ss
    public static String dateToTime(Date date)
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }
}

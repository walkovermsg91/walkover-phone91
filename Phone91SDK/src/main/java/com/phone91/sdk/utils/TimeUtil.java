package com.phone91.sdk.utils;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimeUtil {
    public static final List<Long> times = Arrays.asList(
            TimeUnit.DAYS.toMillis(365),
            TimeUnit.DAYS.toMillis(30),
            TimeUnit.DAYS.toMillis(1),
            TimeUnit.HOURS.toMillis(1),
            TimeUnit.MINUTES.toMillis(1),
            TimeUnit.SECONDS.toMillis(1));
    public static final List<String> timesString = Arrays.asList("year", "month", "day", "hour", "minute", "second");

    public static String toDuration(long duration) {

        StringBuffer res = new StringBuffer();
        for (int i = 0; i < TimeUtil.times.size(); i++) {
            Long current = TimeUtil.times.get(i);
            long temp = duration / current;
            if (temp > 0) {
                res.append(temp).append(" ").append(TimeUtil.timesString.get(i)).append(temp != 1 ? "s" : "").append(" ago");
                break;
            }
        }
        if ("".equals(res.toString()))
            return "0 seconds ago";
        else
            return res.toString();
    }



//    public static String covertTimeToText(long dataDate) {
//
//        String convTime = null;
//
//        String prefix = "";
//        String suffix = "Ago";
//
//        //            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//            Date pasTime = new Date(dataDate/10000000);
//
//        Date nowTime = new Date();
//
//        long dateDiff =  nowTime.getTime()-pasTime.getTime();
//
//        long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
//        long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
//        long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
//        long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);
//
//        if (second < 60) {
//            convTime = second + " Seconds " + suffix;
//        } else if (minute < 60) {
//            convTime = minute + " Minutes "+suffix;
//        } else if (hour < 24) {
//            convTime = hour + " Hours "+suffix;
//        } else if (day >= 7) {
//            if (day > 360) {
//                convTime = (day / 360) + " Years " + suffix;
//            } else if (day > 30) {
//                convTime = (day / 30) + " Months " + suffix;
//            } else {
//                convTime = (day / 7) + " Week " + suffix;
//            }
//        } else if (day < 7) {
//            convTime = day+" Days "+suffix;
//        }
//
//        return convTime;
//    }
    public static String covertTimeToText(long dataDate) throws ParseException {
//        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss a", Locale.ENGLISH);
//        df.setTimeZone(TimeZone.getTimeZone("UTC"));
//
//        Date sendTime=new Date(dataDate/10000000);
//        SimpleDateFormat dfo = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss a", Locale.ENGLISH);
//        dfo.setTimeZone(TimeZone.getDefault());

        Date pasTime=new Date(dataDate/10000);//uTCToLocal(dataDate);




//        String formattedDate = df.format();

        String convTime = null;

        String prefix = "";
        String suffix = "ago";



        Date nowTime = new Date();

        long dateDiff =  nowTime.getTime()-pasTime.getTime();

        long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
        long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
        long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
        long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);

        if (second < 60) {
            if(second<=0)
                convTime="now";
            else
              convTime = second + " sec " + suffix;
        } else if (minute < 60) {
            convTime = minute + " min "+suffix;
        } else if (hour < 24) {
            convTime = hour + " hrs "+suffix;
        } else if (day >= 7) {
            if (day > 360) {
                convTime = (day / 360) + " year " + suffix;
            } else if (day > 30) {
                convTime = (day / 30) + " month " + suffix;
            } else {
                convTime = (day / 7) + " week " + suffix;
            }
        } else if (day < 7) {
            convTime = day+" days "+suffix;
        }

        return convTime;
    }


    public static Date uTCToLocal(long datesToConvert) {
        String dateToReturn = null;
        Date dateMe = null;
        try {


        SimpleDateFormat sdfOutPutToSend = new SimpleDateFormat("dd/mm/YYYY HH:MM:SS");
        sdfOutPutToSend.setTimeZone(TimeZone.getDefault());

        //            gmt = sdf.format(new Date(datesToConvert));
        dateToReturn = sdfOutPutToSend.format(new Date(datesToConvert/10000));


            dateMe=sdfOutPutToSend.parse(dateToReturn);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateMe;

    }

    @NotNull
    public static long getTimestamp() {

        Date timestamp = Calendar.getInstance().getTime();
        return System.currentTimeMillis();
    }
}

package com.ssm.demo.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateTimeUtil {

    private static  final  String formatStr="yyyy-MM-dd HH:mm:ss";
    private DateTimeUtil() {

    }

        /**
         * 根据传进来的dataTimeStr和转换的格式formatStr来返回转回后的date
         * @param dataTimeStr
         * @return
         */
        public static Date stringToDate(String dataTimeStr) {
            try {
                if (null!=formatStr && !formatStr.equals("")
                && null!=dataTimeStr && !dataTimeStr.equals("")) {
                    SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
                    Date date = sdf.parse(dataTimeStr);
                    return date;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
             return null;
        }


        /**
         * 根据传进来的data和转换的格式formatStr来返回转回后的date
         * @param date
         * @return
         */
        public static String dateToString(Date date){

            try {
                if (null!=date
                        && null!=formatStr && !formatStr.equals("")) {
                    SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
                    String dateStr=sdf.format(date);
                    return dateStr;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


    }

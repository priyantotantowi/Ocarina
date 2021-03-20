package me.pete.ocarinalibrary.helper;

import android.app.Activity;
import android.util.Log;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.pete.ocarinalibrary.enumerator.DateFormatEnum;
import me.pete.ocarinalibrary.enumerator.OnTimeSetListener;
import me.pete.ocarinalibrary.enumerator.TimeEnum;
import me.pete.ocarinalibrary.enumerator.TimeFormatEnum;
import me.pete.ocarinalibrary.listener.OnDateSetListener;

/**
 * Created by Priyanto Tantowi.
 *
 * DateTime is helper to all about date or time info.
 */
public final class DateTimeHelper {
    /**
     * This function returns your date plus the number of days
     */
    public static double differenceInDay(String dateBefore, String dateAfter) {
        double daysBetween = 0;
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateBefore1 = myFormat.parse(dateBefore);
            Date dateAfter1 = myFormat.parse(dateAfter);
            long difference = dateAfter1.getTime() - dateBefore1.getTime();
            daysBetween = (difference / (1000*60*60*24));
        } catch (Exception e) {
            Log.e("differenceDate", e.toString());
            e.printStackTrace();
        }
        return daysBetween;
    }

    /**
     * This function returns difference time with time format HH:MM:SS.
     */
    public static String differenceInTime(String dateTimeBefore, String dateTimeAfter) {
        String timeBetween = "00:00:00";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = format.parse(dateTimeBefore);
            date2 = format.parse(dateTimeAfter);

            //in milliseconds
            long diff = date2.getTime() - date1.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000);

            timeBetween = (diffHours < 10 ? "0" : "") + diffHours + ":" + (diffMinutes < 10 ? "0" : "") + diffMinutes + ":" + (diffSeconds < 10 ? "0" : "") + diffSeconds;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeBetween;
    }

    /**
     * This function returns difference time in hour, minute or seconds.
     * You can set result what you want type for.
     */
    public static double differenceInTime(String oldTime, String newTime, TimeEnum timeEnum){
        double result = 0;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = format.parse(oldTime);
            Date date2 = format.parse(newTime);
            if(timeEnum == TimeEnum.HOURS){
                result = (date2.getTime() - date1.getTime()) / 1000 / 60 / 60;
            }else if(timeEnum == TimeEnum.MINUTES){
                result = (date2.getTime() - date1.getTime()) / 1000 / 60;
            }else{
                result = (date2.getTime() - date1.getTime()) / 1000;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * This function return current time.
     */
    public static String getTimeNow(){
        Date date = new Date();
        DecimalFormat decimalFormat = new DecimalFormat("00");
        return decimalFormat.format(date.getHours()) + ":" + decimalFormat.format(date.getMinutes()) + ":" + decimalFormat.format(date.getSeconds());
    }

    /**
     * This function return time now with time format.
     */
    public static String getTimeNow(TimeFormatEnum timeFormatEnum) {
        Date date = new Date();
        DecimalFormat decimalFormat = new DecimalFormat("00");
        if (timeFormatEnum == TimeFormatEnum.HHMMSS) {
            return decimalFormat.format(date.getHours()) + ":" + decimalFormat.format(date.getMinutes()) + ":" + decimalFormat.format(date.getSeconds());
        } else if(timeFormatEnum == TimeFormatEnum.HHMM) {
            return decimalFormat.format(date.getHours()) + ":" + decimalFormat.format(date.getMinutes());
        } else {
            return decimalFormat.format(date.getHours()) + ":" + decimalFormat.format(date.getMinutes()) + ":" + decimalFormat.format(date.getSeconds());
        }
    }

    /**
     * This function return date now.
     */
    public static String getDateNow(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    /**
     * This function return date and time now (ex: 2021-01-01 00:00:00).
     */
    public static String getDateTimeNow(){
        return getDateNow() + " " + getTimeNow();
    }

    /**
     * This function to show dialog date picker with a light theme.
     * @param activity          The activity calling this function.
     * @param title             The initial title for date dialog.
     * @param onDateSetListener How the parent is notified that the date is set.
     * @param isMinCurrentDate  Set true if you can't pick old date.
     */
    public static void dialogDatePickerLight(Activity activity, String title, final OnDateSetListener onDateSetListener, boolean isMinCurrentDate) {
        Calendar cur_calender = Calendar.getInstance();
        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        long date_ship_millis = calendar.getTimeInMillis();
                        onDateSetListener.onDateSet(dateFormat(date_ship_millis, DateFormatEnum.YYYYMMDD));
                    }
                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)
        );
        //set dark light
        if(!title.contentEquals("")) {
            datePicker.setTitle(title);
        }
        datePicker.setThemeDark(false);
        if(isMinCurrentDate) {
            datePicker.setMinDate(cur_calender);
        }
        datePicker.show(activity.getFragmentManager(), "Datepickerdialog");
    }

    /**
     * This function to show dialog date picker with a light theme.
     * @param activity          The activity calling this function.
     * @param title             The initial title for date dialog.
     * @param onTimeSetListener How the parent is notified that the time is set.
     * @param is24HourMode      Set true if you want 24 hour time mode.
     */
    public static void dialogTimePickerLight(Activity activity, String title, final OnTimeSetListener onTimeSetListener, boolean is24HourMode) {
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                NumberFormat format = new DecimalFormat("00");
                onTimeSetListener.onTimeSet(format.format(hourOfDay) + ":" + format.format(minute));
            }
        }, is24HourMode);
        //set dark light
        if(!title.contentEquals("")) {
            timePickerDialog.setTitle(title);
        }
        timePickerDialog.enableSeconds(false);
        timePickerDialog.setThemeDark(false);
        timePickerDialog.show(activity.getFragmentManager(), "Datepickerdialog");
    }

    /**
     * This function return your date with a changed year to current year.
     */
    public static String changeYearToNow(String date) {
        try {
            String result = "";
            String[] dates = date.split("-");
            String[] dateNows = getDateNow().split("-");
            result = dateNows[0] + "-" + dates[1] + "-" + dates[2];
            return result;
        } catch(Exception e) {
            return date;
        }
    }

    /**
     * This function return your date with date format.
     */
    public static String dateFormat(String date, DateFormatEnum dateFormatEnum) {
        String[] dates = date.split("-");
        try {
            Calendar calendar = new GregorianCalendar(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]) - 1, Integer.parseInt(dates[2]));
            return dateFormat(calendar.getTimeInMillis(), dateFormatEnum);
        } catch (Exception e) {
            return "01 January 1990";
        }
    }

    private static String dateFormat(Long dateTime, DateFormatEnum dateFormatEnum) {
        SimpleDateFormat newFormat;
        if(dateFormatEnum == DateFormatEnum.YYYYMMDD) {
            newFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        } else if(dateFormatEnum == DateFormatEnum.DDMMYYYY) {
            newFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        } else if(dateFormatEnum == DateFormatEnum.MMMMDDYYYY) {
            newFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        } else if(dateFormatEnum == DateFormatEnum.DDMMMMYYYY) {
            newFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        } else {
            newFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        }
        return newFormat.format(new Date(dateTime));
    }

    /**
     * This function returns your date plus the number of days
     */
    public static String addDate(String date, int addDate){
        String result = "";
        String date1 = date;  // Start date
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf1.parse(date1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.DATE, addDate);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        SimpleDateFormat date2 = new SimpleDateFormat("yyyy-MM-dd");
        result = date2.format(calendar.getTime());

        return result;
    }

    /**
     * This function returns day.
     *
     * 0 = SUNDAY
     * 1 = MONDAY
     * 2 = TUESDAY
     * 3 = WEDNESDAY
     * 4 = THURSDAY
     * 5 = FRIDAY
     * 6 = SATURDAY
     */
    public static int getDay(String yourDate) {
        int day = 0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            Date date = dateFormat.parse(yourDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return day;
    }

    /**
     * This function returns date time stamp with format YYYYMMDDHHMMSS
     */
    public static String getDateTimeStamp() {
        return getDateNow().replace("-", "") + getTimeNow().replace(":", "");
    }

    /**
     * This function returns date to date with format what you want
     */
    public static String convertDatePattern(String yourDate, String patternFrom, String patternTo) {
        String result = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat(patternFrom);
            Date date = format.parse(yourDate);
            SimpleDateFormat format2 = new SimpleDateFormat(patternTo);
            result = format2.format(date);
        } catch (Exception e) {
            result = yourDate;
        }
        return result;
    }

    /**
     * This function returns date to date with format what you want
     */
    public static int compareTime(String startTimeStr, String endTimeStr) {
        int result = 3;
        Pattern p = Pattern.compile("^([0-2][0-9]):([0-5][0-9]):([0-5][0-9])$"); //Regex is used to validate time format (HH:MM:SS)

        int hhS = 0;
        int mmS = 0;
        int ssS = 0;

        int hhE = 0;
        int mmE = 0;
        int ssE = 0;

        Matcher m = null;
        m = p.matcher(startTimeStr);
        if (m.matches()) {
            String hhStr = m.group(1);
            String mmStr = m.group(2);
            String ssStr = m.group(3);

            hhS = Integer.parseInt(hhStr);
            mmS = Integer.parseInt(mmStr);
            ssS = Integer.parseInt(ssStr);
        } else {
            System.exit(0);
        }

        m = p.matcher(endTimeStr);
        if (m.matches()) {
            String hhStr = m.group(1);
            String mmStr = m.group(2);
            String ssStr = m.group(3);
            hhE = Integer.parseInt(hhStr);
            mmE = Integer.parseInt(mmStr);
            ssE = Integer.parseInt(ssStr);
        } else {
            System.exit(0);
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hhS);
        cal.set(Calendar.MINUTE, mmS);
        cal.set(Calendar.SECOND, ssS);
        Time startTime = new Time(cal.getTime().getTime());

        cal.set(Calendar.HOUR_OF_DAY, hhE);
        cal.set(Calendar.MINUTE, mmE);
        cal.set(Calendar.SECOND, ssE);
        Time endTime = new Time(cal.getTime().getTime());

        if (startTime.equals(endTime)) {
            result = 0;
        } else if (startTime.before(endTime)) {
            result = 1;
        } else
            result = 2;
        return result;
    }
}

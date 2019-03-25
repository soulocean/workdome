package com.example.soulocean1.workdome;

import android.icu.util.Calendar;

public class GetNowDate {

    Calendar calendar = Calendar.getInstance();

    int day = this.calendar.get(5);

    int hour = this.calendar.get(11);

    int minute = this.calendar.get(12);

    int month = this.calendar.get(2);

    int year = this.calendar.get(1);


    public String getNowDate() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.year);
        stringBuilder.append("年");

        stringBuilder.append(String.format("%02d", new Object[]{Integer.valueOf(this.month)}));
        stringBuilder.append("月");
        stringBuilder.append(String.format("%02d", new Object[]{Integer.valueOf(this.day)}));
        stringBuilder.append("日 ");

        stringBuilder.append(String.format("%02d", new Object[]{Integer.valueOf(this.hour)}));
        stringBuilder.append(":");

        stringBuilder.append(String.format("%02d", new Object[]{Integer.valueOf(this.minute)}));
        return stringBuilder.toString();

    }


    public int getYear() {
        return this.year;

    }


    public void setYear(int i) {
        this.year = i;
    }


    public int getMonth() {
        return this.month;

    }


    public void setMonth(int i) {
        this.month = i;

    }


    public int getDay() {
        return this.day;

    }


    public void setDay(int i) {
        this.day = i;

    }


    public int getHour() {
        return this.hour;

    }


    public void setHour(int i) {
        this.hour = i;

    }


    public int getMinute() {
        return this.minute;

    }


    public void setMinute(int i) {
        this.minute = i;

    }

}
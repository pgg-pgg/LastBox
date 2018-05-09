package com.example.pgg.qboxdemo.me.calendar;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

/**
 * Created by pgg on 2018/5/9.
 */

public class LunarDecorator implements DayViewDecorator {

    private String year;
    private String month;

    public LunarDecorator(String year,String month){
        this.year=year;
        this.month=month;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return true;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new LunarSpan(year,month));
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}

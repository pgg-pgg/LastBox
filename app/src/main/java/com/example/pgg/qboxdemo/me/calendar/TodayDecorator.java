package com.example.pgg.qboxdemo.me.calendar;

import com.example.pgg.qboxdemo.utils.DateUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Date;

/**
 * Created by pgg on 2018/5/9.
 */

public class TodayDecorator implements DayViewDecorator {

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        Date date=new Date();
        String s = DateUtils.date2String(date, "yyyy-MM-dd");
        Date parse=DateUtils.string2Date(s,"yyyy-MM-dd");
        if (day.getDate().equals(parse)){
            return true;
        }
        return false;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new AnnulusSpan());
    }
}

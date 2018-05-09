package com.example.pgg.qboxdemo.me.calendar;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;

/**
 * Created by pgg on 2018/5/9.
 */

public class HighlightWeekendsDecorator implements DayViewDecorator {

    private final Calendar calendar=Calendar.getInstance();

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        int weekDay=calendar.get(Calendar.DAY_OF_WEEK);
        return weekDay==Calendar.SATURDAY||weekDay==Calendar.SUNDAY;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.parseColor("#303f9f")));
    }
}

package com.example.pgg.qboxdemo.me.calendar;

import android.text.TextUtils;

import com.example.pgg.qboxdemo.model.entities.HolidaysManager;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Map;

/**
 * Created by pgg on 2018/5/9.
 */

public class EventDecorator_Holiday implements DayViewDecorator {

    private Map<String,String> mDateStringMap;

    public EventDecorator_Holiday(Map<String,String> mDateStringMap) {
        this.mDateStringMap=mDateStringMap;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        String formatDate= HolidaysManager.formatDate(day.getDate());
        boolean b=mDateStringMap.containsKey(formatDate);
        if (b){
            String s=mDateStringMap.get(formatDate);
            if (TextUtils.isEmpty(s)){
                return false;
            }else {
                return true;
            }
        }else {
            return false;
        }
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new EventSpan_Holiday());
    }
}

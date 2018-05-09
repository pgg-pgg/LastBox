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

public class EventDecorator_Workday implements DayViewDecorator {

    private Map<String,String> mDateStringMap;

    public EventDecorator_Workday(Map<String,String> mDateStringMap) {
        this.mDateStringMap=mDateStringMap;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        boolean b = mDateStringMap.containsKey(HolidaysManager.formatDate(day.getDate()));
        if (b){
            String s = mDateStringMap.get(HolidaysManager.formatDate(day.getDate()));
            if (TextUtils.isEmpty(s)){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new EventSpan_Workday());
    }
}

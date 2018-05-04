package com.example.pgg.qboxdemo.model.entities;

/**
 * Created by pgg on 2018/5/4.
 */

public class RefreshNewsFragmentEvent {

    private int mark_code;

    public int getMark_code() {
        return mark_code;
    }

    public void setMark_code(int mark_code) {
        this.mark_code = mark_code;
    }

    public RefreshNewsFragmentEvent(int mark_code) {
        this.mark_code = mark_code;
    }

    public RefreshNewsFragmentEvent() {
    }
}

package com.parttime.enterprise.uicomman.enrollcalander;


import com.parttime.enterprise.uicomman.enrollcalander.flexiblecalender.flexiblecalendar.entity.Event;

/**
 * @author p-v
 */
public class CustomEvent implements Event {

    private int color;

    public CustomEvent(int color) {
        this.color = color;
    }

    @Override
    public int getColor() {
        return color;
    }
}

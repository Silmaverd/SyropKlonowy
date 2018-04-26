package com.blinenterprise.SyropKlonowy.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Calendar;
import java.util.Date;

@AllArgsConstructor
@Getter
public class OrderClosureCommand {
    private Date closureDate;
    private Long orderId;

    public boolean dateEquals(Date ongoingDate) {
        Calendar closureDateCalendar = Calendar.getInstance();
        closureDateCalendar.setTime(closureDate);
        Calendar ongoingDateCalendar = Calendar.getInstance();
        ongoingDateCalendar.setTime(ongoingDate);
        return ((closureDateCalendar.get(Calendar.YEAR) == ongoingDateCalendar.get(Calendar.YEAR)) &&
                (closureDateCalendar.get(Calendar.DAY_OF_YEAR) == ongoingDateCalendar.get(Calendar.DAY_OF_YEAR)) &&
                closureDateCalendar.get(Calendar.HOUR_OF_DAY) == ongoingDateCalendar.get(Calendar.HOUR_OF_DAY));
    }
}

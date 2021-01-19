package com.paymybuddy.webapp.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class DateUtils {

    /**
     * return now() in LocalDate Format using ZoneId system
     *
     * @return now in LocalDate Format
     */
    public LocalDateTime getNowLocalDateTime() {
        return LocalDateTime.now(ZoneId.systemDefault());
    }

}

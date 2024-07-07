package com.myleaderboard.leaderboard_service.util;

import com.myleaderboard.leaderboard_service.exception.GenericException;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateUtil {
    public Date getDateFromString(String dateString, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new GenericException(String.format("Exception in parsing %s to Date", dateString), e);
        }
    }
}

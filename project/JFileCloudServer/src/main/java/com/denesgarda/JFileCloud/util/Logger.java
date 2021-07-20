package com.denesgarda.JFileCloud.util;

import java.util.Calendar;

public class Logger {
    public Logger() {

    }

    public void log(Level level, String string) {
        System.out.println(Calendar.getInstance().getTime() + " [" + level.toString() + "]: " + string);
    }

    public enum Level {
        INFO,
        WARNING,
        ERROR,
        REQUEST,
        RESPONSE,
        REDACTED_REQUEST,
        REDACTED_RESPONSE
    }
}
